# meta-fieldkit

Yocto/OpenEmbedded layer for building production-ready edge AI field kits on NVIDIA Jetson hardware, built on top of [Avocado OS](https://github.com/avocado-linux/meta-avocado) and [vendor-meta-tegra](https://github.com/avocado-linux/vendor-meta-tegra).

## What you get

A fully configured Jetson Orin Nano with:

- **SSH access** out of the box (password: `fieldkit123`)
- **Full hardware support** — Ethernet, WiFi, Bluetooth, NVMe, CAN, USB, DisplayPort
- **Docker + NVIDIA GPU** — run AI containers with `--runtime nvidia`
- **Python 3 + OpenCV + NumPy** — ready for inference workloads
- **Nginx, NetworkManager, curl, wget** — web server and networking tools
- **Build tools** — gcc, cmake, ninja, autoconf for on-device compilation
- **mDNS discovery** — find your device at `avocado-jetson-orin-nano-devkit.local`
- **118GB writable storage** — `/var` auto-expands to fill your NVMe on first boot

---

## Build, Flash, and Connect

### Prerequisites

**Host machine:**
- Ubuntu 22.04 or 24.04 (x86_64), at least 16GB RAM, 100GB free disk
- Install build dependencies:

```bash
sudo apt install -y gawk wget git diffstat unzip texinfo gcc build-essential \
    chrpath socat cpio python3 python3-pip python3-pexpect xz-utils debianutils \
    iputils-ping python3-git python3-jinja2 python3-subunit zstd liblz4-tool file \
    locales libacl1 lz4
sudo locale-gen en_US.UTF-8
```

- Install [kas](https://kas.readthedocs.io/) (the build orchestrator):

```bash
pip3 install kas
```

**Target hardware:**
- NVIDIA Jetson Orin Nano 8GB Developer Kit
- NVMe SSD installed in M.2 slot
- USB-C cable connected to host (for flashing)
- Ethernet cable (for network access after boot)
- Jumper wire for FC_REC to GND (recovery mode)

### Step 1: Clone the repos

```bash
mkdir -p ~/fieldkit-os && cd ~/fieldkit-os
git clone https://github.com/avocado-linux/meta-avocado.git
git clone https://github.com/implyinfer/meta-fieldkit.git
```

### Step 2: Build the OS image

```bash
cd ~/fieldkit-os/meta-avocado
kas build ../meta-fieldkit/kas/fieldkit.yml
```

This downloads all layers, cross-compiles the entire OS, and produces flash artifacts. **First build takes 2-4 hours.** Subsequent builds with changes take minutes.

Build output lands in:
```
build-jetson-orin-nano-devkit/build/tmp/deploy/images/avocado-jetson-orin-nano-devkit/
```

### Step 3: Prepare flash directory

```bash
# Set up a working directory for flashing
export IMAGES=build-jetson-orin-nano-devkit/build/tmp/deploy/images/avocado-jetson-orin-nano-devkit
mkdir -p /tmp/fieldkit-flash/tegraflash

# Copy BSP, tools, and images
cp -a $IMAGES/tegraflash-bsp/* /tmp/fieldkit-flash/tegraflash/
cp -a $IMAGES/tegraflash-tools/* /tmp/fieldkit-flash/tegraflash/
cp $IMAGES/avocado-image-rootfs-jetson-orin-nano-devkit.erofs-lz4 /tmp/fieldkit-flash/tegraflash/
cp $IMAGES/avocado-image-var-jetson-orin-nano-devkit.btrfs /tmp/fieldkit-flash/tegraflash/
cp $IMAGES/avocado-image-initramfs-jetson-orin-nano-devkit.cpio.gz /tmp/fieldkit-flash/tegraflash/
```

### Step 4: Apply flash workarounds

Two known bugs in the L4T R36.5.0 tegraflash tooling must be fixed before flashing. See [docs/flash-workarounds.md](docs/flash-workarounds.md) for full details.

```bash
cd /tmp/fieldkit-flash/tegraflash

# Fix 1: UEFI binary name mismatch in partition layout
sed -i 's/uefi_t23x_general_with_dtb\.bin/uefi_jetson_with_dtb.bin/g' \
    flash.xml flash.xml.in internal-flash.xml bupgen-internal-flash.xml

# Fix 2: bpmp_dtb UnboundLocalError in tegraflash_impl_t234.py
# Find the line with 'bpmp_dtb_in_layout = get_partition_filename' (~line 1565)
# and add 'bpmp_dtb = None' on the line before it:
sed -i '/bpmp_dtb_in_layout = get_partition_filename/i\        bpmp_dtb = None' \
    tegraflash_impl_t234.py
```

### Step 5: Put Jetson in recovery mode

1. Connect USB-C cable from Jetson to your host machine
2. Short the **FC_REC** pin to **GND** on the carrier board (use a jumper wire)
3. Power on (or power cycle) the Jetson
4. Remove the jumper wire
5. Verify recovery mode:

```bash
lsusb | grep -i nvidia
# Should show: 0955:7523 NVIDIA Corp. APX
```

### Step 6: Flash

```bash
cd /tmp/fieldkit-flash/tegraflash
export BOARDID=3767 BOARDSKU=0005 FAB=000 BOARDREV=

sudo bash ./initrd-flash --erase-nvme 2>&1 | tee /tmp/flash.log
```

Flash takes about 3-4 minutes. You'll see 5 steps:
1. Signing binaries
2. RCM boot
3. Sending flash commands
4. Writing partitions to NVMe
5. Final status: **SUCCESS**

### Step 7: Boot and connect

1. Remove FC_REC jumper (if still connected)
2. Power cycle the Jetson
3. Plug in an ethernet cable
4. Wait ~30 seconds for boot + DHCP

```bash
# Connect via mDNS:
ssh root@avocado-jetson-orin-nano-devkit.local

# Or scan your network to find the IP:
sudo arp-scan --localnet | grep -i "3c:6d:66\|unknown"

# Or use the static fallback (direct ethernet cable, no router needed):
ssh root@192.168.55.1

# Password: fieldkit123
```

### Step 8: Verify

Once logged in:

```bash
# System info
uname -a
cat /proc/device-tree/model

# Storage (should show ~118GB free on /var)
df -h /var

# Network interfaces
ip -br addr

# Docker
docker run --rm hello-world

# Python
python3 -c "import numpy; print(f'NumPy {numpy.__version__}')"

# GPU
ls /dev/nvhost-ctrl-gpu && echo "GPU accessible"
```

---

## Rebuilding after changes

After modifying recipes or packagegroups:

```bash
cd ~/fieldkit-os/meta-avocado

# If you changed a recipe, clean it first:
# (from the build environment)
export PATH="build-jetson-orin-nano-devkit/bitbake/bin:build-jetson-orin-nano-devkit/openembedded-core/scripts:$PATH"
source build-jetson-orin-nano-devkit/openembedded-core/oe-init-build-env build-jetson-orin-nano-devkit/build
bitbake <changed-recipe> -c cleansstate

# Rebuild image
bitbake avocado-image-rootfs

# Copy new rootfs to flash directory (no need to re-copy BSP/tools)
sudo cp tmp/deploy/images/avocado-jetson-orin-nano-devkit/avocado-image-rootfs-jetson-orin-nano-devkit.erofs-lz4 \
    /tmp/fieldkit-flash/tegraflash/

# Re-flash (put Jetson in recovery mode first)
cd /tmp/fieldkit-flash/tegraflash
export BOARDID=3767 BOARDSKU=0005 FAB=000 BOARDREV=
sudo rm -rf signed signed_save secureflash.xml secureflash.xml.save \
    secureflash.sh flash_signed.sh rcmboot_blob boardvars.sh flashcmd.txt \
    APPFILE APPFILE_b DATAFILE
sudo bash ./initrd-flash --erase-nvme 2>&1 | tee /tmp/flash.log
```

---

## Packagegroups

| Packagegroup | Contents | Included |
|---|---|---|
| `packagegroup-fieldkit-core` | SSH, nginx, NetworkManager, dnsmasq, curl, wget, jq, avahi/mDNS, wpa-supplicant, i2c-tools, usbutils, ca-certs | Always |
| `packagegroup-fieldkit-nvidia` | tegra-firmware, nvidia-kernel-oot, tegra-tools, fan control | Always |
| `packagegroup-fieldkit-connectivity` | Ethernet (r8169), WiFi (rtw88), Bluetooth (btusb), NVMe, CAN, USB, DisplayPort, HDMI audio | Always |
| `packagegroup-fieldkit-camera` | UVC, V4L2, videobuf2 kernel modules | Always |
| `packagegroup-fieldkit-display` | Weston, Wayland, libdrm-tests, xkeyboard-config | Always |
| `packagegroup-fieldkit-container` | Docker, nvidia-docker, nvidia-container-toolkit | Always |
| `packagegroup-fieldkit-python` | Python 3, pip, venv, numpy, opencv, flask, requests, pyyaml, cryptography, psutil, websockets | Always |
| `packagegroup-fieldkit-devtools` | htop, vim, strace, cmake, ninja, gcc/g++, autoconf, git, tmux, rsync, zip | Dev only |

## Configuration

| Variable | Default | Description |
|---|---|---|
| `FIELDKIT_DEV` | `"1"` | Enable dev tools + root password. Set `"0"` for production. |
| `FIELDKIT_STATIC_IP` | `"192.168.55.1/24"` | Static fallback IP on ethernet (alongside DHCP) |
| `FIELDKIT_ROOT_PASSWORD` | SHA-512 hash of `fieldkit123` | Root password hash. Generate new: `openssl passwd -6 'yourpassword'` |

## Architecture

```
+-----------------------------------------------------+
| meta-fieldkit (this layer)                          |
|   packagegroups, SSH config, network config,        |
|   first-boot services, dev tools, Python, Docker    |
+-----------------------------------------------------+
| meta-avocado + meta-avocado-nvidia                  |
|   Avocado OS distro, images, avocadoctl,            |
|   sysext/OTA, Tegra BSP integration                 |
+-----------------------------------------------------+
| vendor-meta-tegra (OE4T)                            |
|   NVIDIA L4T kernel, firmware, tegraflash tools     |
+-----------------------------------------------------+
| openembedded-core + meta-openembedded               |
|   Yocto/OE build system, base recipes               |
+-----------------------------------------------------+
```

### Filesystem layout on device

```
/              read-only erofs rootfs (the OS, ~300MB)
/var           writable btrfs on NVMe (auto-expanded to fill disk on first boot)
/var/roothome  root's home directory (writable, persists across reboots and OTA)
/var/lib/ssh   SSH host keys + authorized_keys (persists across reflash)
/var/lib/avocado  sysext extensions (OTA-updatable application packages)
/tmp           tmpfs (cleared on reboot)
```

## Development workflow

For fast iteration on your application, use containers on the device:

```bash
# Push code from your laptop to the Jetson
rsync -avz ./src/ root@192.168.55.1:/var/roothome/myapp/

# Run with GPU + camera access
ssh root@192.168.55.1
docker run --rm -it --runtime nvidia --network host \
    --device /dev/video0 \
    -v /var/roothome/myapp:/app \
    dustynv/ros:humble-ros-base-l4t-r36.4.0
```

Edit code locally, rsync, restart container. Seconds per iteration.

## Dependencies

- [Avocado OS](https://github.com/avocado-linux/meta-avocado) (scarthgap branch)
- [vendor-meta-tegra](https://github.com/avocado-linux/vendor-meta-tegra) (scarthgap branch, R36.5.0)
- Yocto/OE scarthgap release

## License

Apache-2.0. See [LICENSE](LICENSE).
