SUMMARY = "FieldKit kiosk display"
DESCRIPTION = "Lightweight fullscreen web kiosk for DisplayPort output. \
Boots directly into a fullscreen browser pointed at the local web dashboard. \
No desktop environment — just Cage (single-app compositor) + Cog (WPE browser)."
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PACKAGE_ARCH = "${MACHINE_ARCH}"
inherit packagegroup

RDEPENDS:${PN} = " \
    cage \
    cog \
    wpewebkit \
    wpebackend-fdo \
"
