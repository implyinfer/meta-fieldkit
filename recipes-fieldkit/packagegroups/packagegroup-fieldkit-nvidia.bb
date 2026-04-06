SUMMARY = "FieldKit NVIDIA Tegra runtime packages"
DESCRIPTION = "NVIDIA firmware, CUDA libraries, multimedia, power management, \
and tools for Jetson platforms. Matches the JetPack out-of-box experience."
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PACKAGE_ARCH = "${MACHINE_ARCH}"
inherit packagegroup

# Core Tegra runtime
RDEPENDS_TEGRA_CORE = " \
    tegra-firmware \
    tegra-libraries-core \
    tegra-tools \
    tegra-nvfancontrol \
    nvidia-kernel-oot \
    tegra-configs-udev \
    tegra-nvstartup \
    tegra-nvpmodel \
    tegra-nvpower \
"

# CUDA runtime libraries (not the compiler — that goes in containers)
RDEPENDS_CUDA = " \
    tegra-libraries-cuda \
    tegra-cuda-utils \
    cudnn \
"

# Multimedia (hardware video encode/decode, camera ISP)
RDEPENDS_MULTIMEDIA = " \
    tegra-libraries-multimedia \
    tegra-libraries-multimedia-utils \
    tegra-libraries-multimedia-v4l \
    tegra-mmapi \
"

# A/B boot and OTA
RDEPENDS_OTA = " \
    tegra-redundant-boot \
    tegra-redundant-boot-base \
"

RDEPENDS:${PN} = " \
    ${RDEPENDS_TEGRA_CORE} \
    ${RDEPENDS_CUDA} \
    ${RDEPENDS_MULTIMEDIA} \
    ${RDEPENDS_OTA} \
"
