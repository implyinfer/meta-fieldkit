SUMMARY = "FieldKit WiFi hotspot service"
DESCRIPTION = "Creates a WiFi access point on a USB WiFi adapter for offline \
field deployment. Provides NAT to share internet from ethernet."
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

SRC_URI = " \
    file://fieldkit-hotspot.sh \
    file://fieldkit-hotspot.service \
"

inherit systemd allarch
SYSTEMD_SERVICE:${PN} = "fieldkit-hotspot.service"

RDEPENDS:${PN} = "networkmanager iw iptables"

do_install() {
    install -d ${D}${libexecdir}
    install -m 0755 ${WORKDIR}/fieldkit-hotspot.sh ${D}${libexecdir}/fieldkit-hotspot.sh

    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${WORKDIR}/fieldkit-hotspot.service ${D}${systemd_system_unitdir}/

    # Default config directory on writable /var
    install -d ${D}/var/lib/fieldkit
}
