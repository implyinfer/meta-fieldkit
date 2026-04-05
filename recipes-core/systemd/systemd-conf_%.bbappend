FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append = " file://wired-fieldkit.network"

FIELDKIT_STATIC_IP ??= "192.168.55.1/24"

do_install:append() {
    # Override wired network config with FieldKit version (DHCP + static fallback)
    sed -e "s|@FIELDKIT_STATIC_IP@|${FIELDKIT_STATIC_IP}|" \
        ${WORKDIR}/wired-fieldkit.network \
        > ${D}${systemd_unitdir}/network/80-wired.network
}
do_install[vardeps] += "FIELDKIT_STATIC_IP"
