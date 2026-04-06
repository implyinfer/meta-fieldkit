# Disable useradd — weston user is managed by avocado-users via meta-fieldkit
USERADDEXTENSION = ""
USERADD_PACKAGES = ""
USERADD_PARAM:${PN} = ""
GROUPADD_PARAM:${PN} = ""

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"
SRC_URI += "file://weston-fieldkit.service"

do_install:append() {
    # Replace weston.service with fieldkit version that works on read-only rootfs
    install -m 0644 ${WORKDIR}/weston-fieldkit.service ${D}${systemd_system_unitdir}/weston.service

    # Remove socket activation (same issue as sshd — fails on read-only rootfs)
    rm -f ${D}${systemd_system_unitdir}/weston.socket
}
