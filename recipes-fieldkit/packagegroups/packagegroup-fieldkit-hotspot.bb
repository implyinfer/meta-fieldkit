SUMMARY = "FieldKit WiFi hotspot support"
DESCRIPTION = "Packages for creating a WiFi access point on USB WiFi adapters \
for offline field deployment without existing network infrastructure."
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PACKAGE_ARCH = "${MACHINE_ARCH}"
inherit packagegroup

RDEPENDS:${PN} = " \
    networkmanager \
    iw \
    iptables \
    hostapd \
    dnsmasq \
    fieldkit-hotspot \
"
