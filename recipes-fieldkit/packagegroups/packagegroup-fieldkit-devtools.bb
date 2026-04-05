SUMMARY = "FieldKit development tools"
DESCRIPTION = "CLI tools for interactive development and debugging. \
Not included in production builds — set FIELDKIT_DEV = 0 to exclude."
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PACKAGE_ARCH = "${MACHINE_ARCH}"
inherit packagegroup

RDEPENDS:${PN} = " \
    htop \
    vim \
    less \
    strace \
    procps \
    iproute2 \
    net-tools \
    ethtool \
    iputils-ping \
"
