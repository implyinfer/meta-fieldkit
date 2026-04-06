SUMMARY = "FieldKit camera and video support"
DESCRIPTION = "USB camera (UVC), V4L2, and NVIDIA hardware-accelerated GStreamer \
plugins for video capture, encode/decode, and display."
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PACKAGE_ARCH = "${MACHINE_ARCH}"
inherit packagegroup

# Camera kernel modules
RDEPENDS_CAMERA = " \
    v4l-utils \
    kernel-module-uvcvideo \
    kernel-module-videobuf2-v4l2 \
    kernel-module-videobuf2-vmalloc \
    kernel-module-videobuf2-common \
    kernel-module-videobuf2-memops \
    kernel-module-videodev \
"

# GStreamer base
RDEPENDS_GSTREAMER = " \
    gstreamer1.0 \
    gstreamer1.0-plugins-base \
    gstreamer1.0-plugins-good \
    gstreamer1.0-plugins-bad \
"

# NVIDIA hardware-accelerated GStreamer plugins
RDEPENDS_GSTREAMER_NV = " \
    gstreamer1.0-plugins-nvarguscamerasrc \
    gstreamer1.0-plugins-nvcompositor \
    gstreamer1.0-plugins-nvdrmvideosink \
    gstreamer1.0-plugins-nveglgles \
    gstreamer1.0-plugins-nvjpeg \
    gstreamer1.0-plugins-nvtee \
    gstreamer1.0-plugins-nvvidconv \
    gstreamer1.0-plugins-nvvideo4linux2 \
    gstreamer1.0-plugins-nvvideosinks \
    gstreamer1.0-plugins-nvv4l2camerasrc \
    gstreamer1.0-plugins-tegra \
    nvgstapps \
"

RDEPENDS:${PN} = " \
    ${RDEPENDS_CAMERA} \
    ${RDEPENDS_GSTREAMER} \
    ${RDEPENDS_GSTREAMER_NV} \
"
