SUMMARY = "FieldKit display and compositor support"
DESCRIPTION = "Weston/Wayland compositor for DisplayPort output on Jetson."
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PACKAGE_ARCH = "${MACHINE_ARCH}"
inherit packagegroup

RDEPENDS:${PN} = " \
    weston \
    weston-init \
    wayland \
    wayland-utils \
    libdrm-tests \
    xkeyboard-config \
"
