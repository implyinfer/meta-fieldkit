SUMMARY = "FieldKit core packages for edge AI field kits"
DESCRIPTION = "Essential services, networking, and web infrastructure for a \
production-ready embedded device with SSH, mDNS, DNS, and HTTP server."
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
    networkmanager \
    nginx \
    dnsmasq \
    curl \
    wget \
    jq \
    i2c-tools \
    usbutils \
    dtc \
    ca-certificates \
    zlib \
"
