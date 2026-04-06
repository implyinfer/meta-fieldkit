SUMMARY = "FieldKit kiosk auto-start service"
DESCRIPTION = "Systemd service that launches a fullscreen Cage+Cog kiosk \
browser pointed at the local nginx dashboard on boot."
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

SRC_URI = "file://fieldkit-kiosk.service"

inherit systemd allarch
SYSTEMD_SERVICE:${PN} = "fieldkit-kiosk.service"

do_install() {
    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${WORKDIR}/fieldkit-kiosk.service ${D}${systemd_system_unitdir}/
}
