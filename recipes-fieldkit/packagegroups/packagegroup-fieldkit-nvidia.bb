SUMMARY = "FieldKit NVIDIA Tegra runtime packages"
DESCRIPTION = "NVIDIA firmware, libraries, and tools for Jetson platforms."
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PACKAGE_ARCH = "${MACHINE_ARCH}"
inherit packagegroup

RDEPENDS:${PN} = " \
    tegra-firmware \
    tegra-libraries-core \
    tegra-tools \
    tegra-nvfancontrol \
    nvidia-kernel-oot \
    tegra-configs-udev \
    tegra-nvstartup \
"
