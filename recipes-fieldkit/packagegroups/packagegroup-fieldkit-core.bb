SUMMARY = "FieldKit core packages for edge AI field kits"
DESCRIPTION = "Essential services for a usable, SSH-accessible embedded device \
with service discovery and writable home directory on read-only rootfs."
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PACKAGE_ARCH = "${MACHINE_ARCH}"
inherit packagegroup

RDEPENDS:${PN} = " \
    openssh \
    openssh-sshd \
    openssh-sftp-server \
    openssh-scp \
    avahi-daemon \
    libnss-mdns \
    wpa-supplicant \
    i2c-tools \
    usbutils \
    dtc \
"
