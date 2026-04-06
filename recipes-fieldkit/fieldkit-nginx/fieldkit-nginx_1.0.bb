SUMMARY = "FieldKit nginx configuration"
DESCRIPTION = "Default nginx reverse proxy config for FieldKit web dashboard."
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

SRC_URI = "file://fieldkit-nginx.conf"

inherit allarch

RDEPENDS:${PN} = "nginx"

do_install() {
    # Install as the default nginx site config
    install -d ${D}${sysconfdir}/nginx/sites-enabled
    install -m 0644 ${WORKDIR}/fieldkit-nginx.conf ${D}${sysconfdir}/nginx/sites-enabled/default
}
