SUMMARY = "FieldKit camera support"
DESCRIPTION = "USB camera (UVC) and V4L2 support for vision inference workloads."
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PACKAGE_ARCH = "${MACHINE_ARCH}"
inherit packagegroup

RDEPENDS:${PN} = " \
    v4l-utils \
    kernel-module-uvcvideo \
    kernel-module-videobuf2-v4l2 \
    kernel-module-videobuf2-vmalloc \
    kernel-module-videobuf2-common \
    kernel-module-videobuf2-memops \
    kernel-module-videodev \
"
