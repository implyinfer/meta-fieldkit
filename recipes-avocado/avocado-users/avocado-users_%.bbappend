FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += " \
    file://var-resize.service \
    file://var-roothome-init.service \
"

inherit systemd
SYSTEMD_SERVICE:${PN} += "var-resize.service var-roothome-init.service"

FIELDKIT_DEV ??= "1"
FIELDKIT_ROOT_PASSWORD ??= "$6$WkKOwPovv1ZGqD.F$dvkrBCkkTEGXzPxQKgZUzh2qY1UovNfiT.5xrT9bzNKqTF9YFuEx9FGOXfGPCF9MdXHG2LyyzCa6/M9VhVTz/0"

do_install:append() {
    # Add sshd user for privilege separation
    grep -q '^sshd:' ${D}${sysconfdir}/passwd || \
        echo 'sshd:x:74:74:sshd privsep:/var/empty/sshd:/sbin/nologin' >> ${D}${sysconfdir}/passwd
    grep -q '^sshd:' ${D}${sysconfdir}/group || \
        echo 'sshd:x:74:' >> ${D}${sysconfdir}/group

    # Add avahi user for mDNS service discovery
    grep -q '^avahi:' ${D}${sysconfdir}/passwd || \
        echo 'avahi:x:75:75:avahi-daemon:/var/run/avahi-daemon:/sbin/nologin' >> ${D}${sysconfdir}/passwd
    grep -q '^avahi:' ${D}${sysconfdir}/group || \
        echo 'avahi:x:75:' >> ${D}${sysconfdir}/group

    # Add weston user for display compositor
    grep -q '^weston:' ${D}${sysconfdir}/passwd || \
        echo 'weston:x:76:76:weston:/var/roothome:/bin/sh' >> ${D}${sysconfdir}/passwd
    grep -q '^weston:' ${D}${sysconfdir}/group || \
        echo 'weston:x:76:' >> ${D}${sysconfdir}/group
    grep -q '^wayland:' ${D}${sysconfdir}/group || \
        echo 'wayland:x:77:weston' >> ${D}${sysconfdir}/group
    grep -q '^render:' ${D}${sysconfdir}/group || \
        echo 'render:x:78:weston' >> ${D}${sysconfdir}/group
    # Add weston to video and input groups
    sed -i '/^video:/s/$/ weston/' ${D}${sysconfdir}/group 2>/dev/null || \
        echo 'video:x:44:weston' >> ${D}${sysconfdir}/group
    sed -i '/^input:/s/$/ weston/' ${D}${sysconfdir}/group 2>/dev/null || \
        echo 'input:x:79:weston' >> ${D}${sysconfdir}/group

    # Move root home to writable /var partition
    sed -i 's|^root:x:0:0:root:/root:|root:x:0:0:root:/var/roothome:|' ${D}${sysconfdir}/passwd

    # sshd privilege separation directory
    install -d -m 0755 ${D}/var/empty/sshd

    # Root home directory (on writable /var)
    install -d -m 0700 ${D}/var/roothome

    # Dev mode: set root password (default: fieldkit123)
    if [ "${FIELDKIT_DEV}" = "1" ]; then
        sed -i 's|^root:\*:|root:${FIELDKIT_ROOT_PASSWORD}:|' ${D}${sysconfdir}/shadow
        bbwarn "Dev mode: root login with password enabled. Set FIELDKIT_DEV=0 for production."
    fi

    # Install first-boot services
    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${WORKDIR}/var-resize.service ${D}${systemd_system_unitdir}/
    install -m 0644 ${WORKDIR}/var-roothome-init.service ${D}${systemd_system_unitdir}/
}
do_install[vardeps] += "FIELDKIT_DEV FIELDKIT_ROOT_PASSWORD"
