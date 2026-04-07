SUMMARY = "FieldKit environment and tool configuration"
DESCRIPTION = "Shell environment setup for read-only rootfs: pip cache dirs, \
PYTHONPATH, and aliases for containerized tools."
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

SRC_URI = "file://fieldkit-env.sh"

inherit allarch

do_install() {
    install -d ${D}${sysconfdir}/profile.d
    install -m 0644 ${WORKDIR}/fieldkit-env.sh ${D}${sysconfdir}/profile.d/fieldkit-env.sh
}
