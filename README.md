# meta-fieldkit

Yocto/OpenEmbedded layer for building production-ready edge AI field kits on NVIDIA Jetson hardware, built on top of [Avocado OS](https://github.com/avocado-linux/meta-avocado) and [vendor-meta-tegra](https://github.com/avocado-linux/vendor-meta-tegra).

## What this layer provides

- **SSH that works on read-only rootfs** — host keys on writable `/var`, standalone daemon (not socket-activated), privilege separation user
- **Full hardware connectivity** — Ethernet, WiFi (RTL8822CE), Bluetooth, NVMe, CAN bus, USB, DisplayPort audio/video
- **Docker with GPU support** — nvidia-docker + nvidia-container-toolkit for containerized AI inference
- **Camera support** — UVC + V4L2 kernel modules for USB cameras
- **Display output** — Weston/Wayland compositor for DisplayPort monitors
- **Developer experience** — writable root home on `/var`, static fallback IP, mDNS discovery, dev tools
- **First-boot setup** — automatic btrfs expansion to fill NVMe, root home initialization

## Quick start

### One-command build

```bash
kas build meta-fieldkit/kas/fieldkit.yml
```

### Add to an existing Avocado build

Include the KAS fragment in your machine YAML:

```yaml
header:
  version: 16
  includes:
    - repo: meta-avocado
      file: kas/machine/jetson-orin-nano-devkit.yml
    - repo: meta-fieldkit
      file: kas/fieldkit-include.yml
```

Or add the layer manually and set in `local.conf`:

```bitbake
ROOTFS_IMAGE_EXTRA_INSTALL:append = " \
    packagegroup-fieldkit-core \
    packagegroup-fieldkit-nvidia \
    packagegroup-fieldkit-connectivity \
"
```

## Packagegroups

| Packagegroup | Contents | Default |
|---|---|---|
| `packagegroup-fieldkit-core` | SSH, avahi/mDNS, wpa-supplicant, i2c-tools, usbutils | Yes |
| `packagegroup-fieldkit-nvidia` | tegra-firmware, nvidia-kernel-oot, tegra-tools | Yes |
| `packagegroup-fieldkit-connectivity` | Ethernet, WiFi, BT, NVMe, CAN, USB, display, audio kernel modules | Yes |
| `packagegroup-fieldkit-camera` | UVC, V4L2, videobuf2 kernel modules | Yes |
| `packagegroup-fieldkit-display` | Weston, Wayland, libdrm-tests | Yes |
| `packagegroup-fieldkit-container` | Docker, nvidia-docker, nvidia-container-toolkit | Yes |
| `packagegroup-fieldkit-devtools` | htop, vim, strace, iproute2, net-tools | Dev only |

## Configuration

Set in `local.conf` or via KAS `local_conf_header`:

| Variable | Default | Description |
|---|---|---|
| `FIELDKIT_DEV` | `"1"` | Enable dev tools + root login. Set `"0"` for production. |
| `FIELDKIT_STATIC_IP` | `"192.168.55.1/24"` | Static fallback IP on ethernet (alongside DHCP) |
| `FIELDKIT_ROOT_PASSWORD` | SHA-512 hash of `fieldkit123` | Root password hash (dev mode only) |

## Architecture

```
┌─────────────────────────────────────────────────────┐
│ meta-fieldkit (this layer)                          │
│   packagegroups, SSH config, network config,        │
│   first-boot services, dev tools                    │
├─────────────────────────────────────────────────────┤
│ meta-avocado + meta-avocado-nvidia                  │
│   Avocado OS distro, images, avocadoctl,            │
│   sysext/OTA, Tegra BSP integration                 │
├─────────────────────────────────────────────────────┤
│ vendor-meta-tegra (OE4T)                            │
│   NVIDIA L4T kernel, firmware, tegraflash tools     │
├─────────────────────────────────────────────────────┤
│ openembedded-core + meta-openembedded               │
│   Yocto/OE build system, base recipes               │
└─────────────────────────────────────────────────────┘
```

### Filesystem layout on device

```
/          read-only erofs rootfs (the OS, ~230MB)
/var       writable btrfs on NVMe data partition (auto-expanded to fill disk)
/var/roothome    root's home directory (writable, persists across reboots)
/var/lib/ssh     SSH host keys + authorized_keys (persists across reflash)
/var/lib/avocado sysext extensions (OTA-updatable)
/tmp       tmpfs (cleared on reboot)
```

## Connecting to a device

```bash
# Via mDNS (if avahi is running on your network):
ssh root@avocado-jetson-orin-nano-devkit.local

# Via static fallback IP (direct ethernet, no DHCP needed):
ssh root@192.168.55.1

# Password (dev mode): fieldkit123
```

## Flash workarounds

See [docs/flash-workarounds.md](docs/flash-workarounds.md) for known issues with tegraflash on JetPack 6.2.2 / L4T R36.5.0.

## Development workflow

For fast iteration on AI inference applications, use containers on the device:

```bash
# Push code to device
rsync -avz ./src/ root@192.168.55.1:/var/roothome/myapp/

# Run in NVIDIA container with GPU + camera access
ssh root@192.168.55.1
docker run --rm -it --runtime nvidia --network host \
    --device /dev/video0 \
    -v /var/roothome/myapp:/app \
    dustynv/ros:humble-ros-base-l4t-r36.4.0
```

## Dependencies

- [Avocado OS](https://github.com/avocado-linux/meta-avocado) (scarthgap branch)
- [vendor-meta-tegra](https://github.com/avocado-linux/vendor-meta-tegra) (scarthgap branch, R36.5.0)
- Yocto/OE scarthgap release

## License

Apache-2.0. See [LICENSE](LICENSE).
