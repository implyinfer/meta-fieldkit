SUMMARY = "FieldKit connectivity kernel modules and firmware"
DESCRIPTION = "Kernel modules for Ethernet, WiFi, Bluetooth, NVMe, CAN, USB, \
audio, and display on NVIDIA Jetson Orin Nano devkit, plus firmware blobs."
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PACKAGE_ARCH = "${MACHINE_ARCH}"
inherit packagegroup

# Ethernet (Realtek r8169 on devkit carrier board)
RDEPENDS_ETHERNET = " \
    kernel-module-r8169 \
    kernel-module-realtek \
    kernel-module-pcie-tegra194 \
    kernel-module-phy-tegra194-p2u \
    kernel-module-stmmac \
    kernel-module-stmmac-platform \
    kernel-module-dwmac-tegra \
    linux-firmware-rtl8168 \
    linux-firmware-rtl-nic \
"

# WiFi (RTL8822CE on Orin Nano devkit)
RDEPENDS_WIFI = " \
    kernel-module-cfg80211 \
    kernel-module-mac80211 \
    kernel-module-rtw88-core \
    kernel-module-rtw88-pci \
    kernel-module-rtw88-8822c \
    kernel-module-rtw88-8822ce \
    linux-firmware-rtl8822 \
    wireless-regdb-static \
"

# Bluetooth
RDEPENDS_BT = " \
    kernel-module-btusb \
    kernel-module-btrtl \
    kernel-module-btbcm \
    kernel-module-btintel \
    kernel-module-bnep \
    kernel-module-rfcomm \
    kernel-module-hidp \
"

# Storage
RDEPENDS_STORAGE = " \
    kernel-module-nvme \
    kernel-module-nvme-core \
"

# CAN bus
RDEPENDS_CAN = " \
    kernel-module-can \
    kernel-module-can-dev \
    kernel-module-can-raw \
    kernel-module-can-bcm \
    kernel-module-can-gw \
"

# USB
RDEPENDS_USB = " \
    kernel-module-dwc3 \
    kernel-module-tegra-xudc \
    kernel-module-xhci-plat-hcd \
    kernel-module-usbnet \
    kernel-module-cdc-ether \
    kernel-module-cdc-ncm \
    kernel-module-r8152 \
    kernel-module-asix \
    kernel-module-ax88179-178a \
"

# Display
RDEPENDS_DISPLAY = " \
    kernel-module-host1x \
    kernel-module-tegra-drm \
"

# Audio (HDMI/DP)
RDEPENDS_AUDIO = " \
    kernel-module-snd-hda-tegra \
    kernel-module-snd-hda-codec-hdmi \
    kernel-module-snd-hda-codec \
    kernel-module-snd-hda-codec-generic \
    kernel-module-snd-hda-core \
    kernel-module-tegra210-adma \
    kernel-module-tegra-aconnect \
"

# System (PWM, sensors, SPI, VPN)
RDEPENDS_SYSTEM = " \
    kernel-module-pwm-tegra \
    kernel-module-pwm-fan \
    kernel-module-ina3221 \
    kernel-module-spidev \
    kernel-module-tun \
    kernel-module-wireguard \
"

RDEPENDS:${PN} = " \
    ${RDEPENDS_ETHERNET} \
    ${RDEPENDS_WIFI} \
    ${RDEPENDS_BT} \
    ${RDEPENDS_STORAGE} \
    ${RDEPENDS_CAN} \
    ${RDEPENDS_USB} \
    ${RDEPENDS_DISPLAY} \
    ${RDEPENDS_AUDIO} \
    ${RDEPENDS_SYSTEM} \
"
