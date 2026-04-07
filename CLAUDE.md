# CLAUDE.md — FieldKit OS System Guide

## What is this?

FieldKit OS is a custom Linux distribution for NVIDIA Jetson Orin Nano edge AI devices, built for deploying healthcare inference field kits. It's built on Avocado OS (Yocto/OpenEmbedded) with the `meta-fieldkit` layer providing hardware support, developer experience, and production hardening.

## Architecture

```
Immutable root (/)           Writable data (/var)
erofs-lz4, ~2.7GB           btrfs, expands to fill NVMe
read-only, A/B OTA           persists across OTA updates
├── /usr                     ├── /var/roothome     ← root's HOME
├── /etc (mostly)            ├── /var/lib/docker   ← containers
├── /lib/modules             ├── /var/lib/ssh      ← host keys
└── /bin, /sbin              ├── /var/lib/avocado  ← sysext
                             └── /var/log
```

**Key principle:** The OS is immutable. All user data, containers, configs, and application state live on `/var`. Nothing writes to `/` at runtime.

## Read-Only Rootfs Implications

This is the most important thing to understand. The root filesystem is **read-only erofs**. This means:

- `apt install` does not work (there is no apt)
- `pip install` without `--user` fails (can't write to `/usr/lib`)
- Writing to `/root`, `/etc`, `/home` fails
- Any tool that assumes a writable `/` needs environment overrides

### How we handle it

- `HOME=/var/roothome` — set by sshd via `SetEnv`, not from `/etc/passwd`
- `PIP_CACHE_DIR=/var/roothome/.cache/pip` — pip writes to writable partition
- `PYTHONUSERBASE=/var/roothome/.local` — `pip install --user` works
- `XDG_CACHE_HOME=/var/roothome/.cache` — all cache goes to `/var`
- Docker data at `/var/lib/docker` — containers and images on writable partition
- SSH host keys at `/var/lib/ssh` — generated on first boot, persist across reflash

### When you need to modify /etc

Mount a tmpfs overlay (lost on reboot):
```bash
mkdir -p /tmp/etc-upper /tmp/etc-work
mount -t overlay overlay -o lowerdir=/etc,upperdir=/tmp/etc-upper,workdir=/tmp/etc-work /etc
# Now /etc is writable until reboot
```

## Accessing the Device

```bash
# Via DHCP (check your router or arp-scan)
ssh root@192.168.1.196

# Via mDNS
ssh root@avocado-jetson-orin-nano-devkit.local

# Via static fallback (direct ethernet, no router needed)
ssh root@192.168.55.1

# Password: fieldkit123
```

UART serial console: `/dev/ttyUSB0` at 115200 baud (from the build host).

## Installing Software

### Python packages
```bash
pip3 install --user <package>
# Installs to /var/roothome/.local/lib/python3.12/site-packages
# Already on PATH via .bashrc
```

### System tools
Tools that need system-level access (useradd, systemd services, etc.) must run in Docker containers. The rootfs cannot be modified.

### Docker containers (preferred for heavy dependencies)
```bash
docker run --runtime nvidia --network host --privileged \
  -v /var/roothome/myapp:/app \
  <image> <command>
```
Docker uses nvidia runtime by default. GPU, camera (`/dev/video*`), and all devices are accessible with `--privileged`.

### Adding packages to the OS image
Add to a packagegroup in `meta-fieldkit/recipes-fieldkit/packagegroups/` and rebuild:
```bash
bitbake avocado-image-rootfs
```
This requires reflashing.

## Hardware

- **Board:** NVIDIA Jetson Orin Nano 8GB Developer Kit Super
- **Board ID:** 3767, SKU: 0005
- **Kernel:** 6.6.111-yocto-standard (aarch64)
- **GPU:** NVIDIA Tegra Orin (nvgpu), CUDA 12.6
- **Ethernet MAC:** 3c:6d:66:b5:3c:d6

### Interfaces available
| Interface | Status | Notes |
|-----------|--------|-------|
| eth0 | UP | Realtek r8169, DHCP + static 192.168.55.1 |
| wlan0 | DOWN | RTL8822CE, needs wpa_supplicant config |
| can0 | DOWN | CAN bus, ready for use |
| /dev/video0-5 | Available | RealSense D435 (video4 = RGB) |
| /dev/dri/card0 | Available | NVIDIA DRM, DisplayPort output |
| /dev/nvhost-* | Available | NVIDIA GPU compute nodes |

### Camera mapping (RealSense D435)
```
/dev/video0 — Depth (Z16)
/dev/video2 — IR left (Y8)
/dev/video4 — RGB (YUYV) ← use this for vision
```

## Services

| Service | Port | Description |
|---------|------|-------------|
| sshd | 22 | Standalone daemon (not socket-activated) |
| nginx | 80 | Reverse proxy to port 5000 |
| avahi-daemon | 5353 | mDNS discovery |
| NetworkManager | — | Ethernet, WiFi, hotspot management |
| docker | — | Container runtime with nvidia default |
| weston | — | Wayland compositor on DisplayPort |
| var-resize | — | Expands /var btrfs on boot |
| var-roothome-init | — | Creates root home on first boot |

## Docker

Docker is configured with nvidia as the default runtime:
```json
{
    "runtimes": { "nvidia": { "path": "nvidia-container-runtime" } },
    "default-runtime": "nvidia",
    "data-root": "/var/lib/docker"
}
```

All containers get GPU access by default. No need for `--gpus all`.

### Running the Roboflow inference server
```bash
docker run -d --name rf-inference \
  --runtime nvidia --network host \
  -v /var/roothome/fieldkit/models:/cache \
  -e MODEL_CACHE_DIR=/cache \
  --device /dev/video4 \
  roboflow/roboflow-inference-server-jetson-6.2.0:latest
```

## Build System

### Repository structure
```
meta-fieldkit/                    ← This layer (github.com/implyinfer/meta-fieldkit)
├── recipes-fieldkit/packagegroups/  ← What packages go in the image
├── recipes-connectivity/openssh/    ← SSH for read-only rootfs
├── recipes-core/systemd/            ← Network config (static IP)
├── recipes-avocado/avocado-users/   ← System users, HOME, first-boot
├── recipes-graphics/wayland/        ← Weston fix for read-only rootfs
├── tools/                           ← Dockerfile for CUDA AI stack
└── kas/                             ← Build configs
```

### Building
```bash
cd ~/experiments/fieldkit-os/meta-avocado
kas build ../meta-fieldkit/kas/fieldkit.yml
```

### Rebuilding after recipe changes
```bash
bitbake <changed-recipe> -c cleansstate
bitbake avocado-image-rootfs
```
**Important:** Yocto caches aggressively. Always `cleansstate` changed recipes before rebuilding, or the old cached version will be used.

### Flashing
```bash
sudo cp <new-rootfs.erofs-lz4> /tmp/avocado-flash5/tegraflash/
cd /tmp/avocado-flash5/tegraflash
export BOARDID=3767 BOARDSKU=0005 FAB=000 BOARDREV=
sudo rm -rf signed signed_save secureflash.xml secureflash.xml.save \
  secureflash.sh flash_signed.sh rcmboot_blob boardvars.sh flashcmd.txt \
  APPFILE APPFILE_b DATAFILE
# Put Jetson in recovery mode (FC_REC to GND, power cycle, remove jumper)
sudo bash ./initrd-flash --erase-nvme
```

### Flash workarounds (JetPack 6.2.2 / L4T R36.5.0)
Two bugs must be fixed in the flash directory before first flash:
1. `sed -i 's/uefi_t23x_general_with_dtb.bin/uefi_jetson_with_dtb.bin/g' flash.xml flash.xml.in internal-flash.xml bupgen-internal-flash.xml`
2. Add `bpmp_dtb = None` before `bpmp_dtb_in_layout = get_partition_filename(...)` in `tegraflash_impl_t234.py`

## Monitoring

```bash
tegrastats              # GPU/CPU/RAM/temp/power (built-in, no install needed)
docker stats            # Container resource usage
df -h /var              # Storage usage
ip -br addr             # Network status
journalctl -f           # System logs
```

## Common Issues

**"Read-only file system"** — You're trying to write to the immutable rootfs. Use `/var/roothome` or `/tmp` instead. Check that `$HOME` is `/var/roothome`.

**pip fails with cache errors** — Ensure `PIP_CACHE_DIR=/var/roothome/.cache/pip` is set. New SSH sessions should have this via `.bashrc`.

**Docker "no space left on device"** — Check `df -h /var`. Docker images can be large (5-10GB each).

**sshd connection reset** — sshd may need restart after `/etc` overlay is lost. Via UART: `/usr/sbin/sshd -D -o "SetEnv HOME=/var/roothome" &`

**Camera not detected** — Check `lsusb` and `ls /dev/video*`. UVC module should auto-load. RealSense RGB is typically `/dev/video4`, not `/dev/video0`.

**useradd/groupadd fails in build** — Recipes that `inherit useradd` conflict with static passwd. Add the user to `avocado-users` bbappend in meta-fieldkit.

## Project Links

- **meta-fieldkit:** https://github.com/implyinfer/meta-fieldkit
- **Field kit apps:** https://github.com/implyinfer/jetson-orin-nano-field-kit
- **Avocado OS:** https://github.com/avocado-linux/meta-avocado
- **vendor-meta-tegra:** https://github.com/avocado-linux/vendor-meta-tegra
