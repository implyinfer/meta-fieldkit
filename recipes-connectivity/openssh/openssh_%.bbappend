FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += " \
    file://sshd_config_fieldkit \
    file://sshd_check_keys_fieldkit \
    file://sshdgenkeys-fieldkit.conf \
    file://sshd-daemon.service \
"

do_install:append() {
    # Use FieldKit sshd_config — host keys on writable /var, not read-only /etc
    install -m 0644 ${WORKDIR}/sshd_config_fieldkit ${D}${sysconfdir}/ssh/sshd_config

    # Install key generation script that writes to /var/lib/ssh
    install -m 0755 ${WORKDIR}/sshd_check_keys_fieldkit ${D}${libexecdir}/openssh/sshd_check_keys_fieldkit

    # Override sshdgenkeys.service to use our key generation script
    install -d ${D}${systemd_system_unitdir}/sshdgenkeys.service.d
    install -m 0644 ${WORKDIR}/sshdgenkeys-fieldkit.conf \
        ${D}${systemd_system_unitdir}/sshdgenkeys.service.d/fieldkit-keylocation.conf

    # Use standalone sshd daemon instead of socket activation
    # (sshd -i via socket activation silently fails on read-only rootfs)
    install -m 0644 ${WORKDIR}/sshd-daemon.service ${D}${systemd_system_unitdir}/sshd.service

    # Disable socket activation
    rm -f ${D}${systemd_system_unitdir}/sshd.socket
    rm -f ${D}${systemd_system_unitdir}/sshd@.service

    # Create /var/lib/ssh directory in image
    install -d ${D}/var/lib/ssh
}

SYSTEMD_SERVICE:${PN}-sshd = "sshd.service"
