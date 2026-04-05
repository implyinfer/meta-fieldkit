SUMMARY = "FieldKit container runtime"
DESCRIPTION = "Docker with NVIDIA GPU support for containerized AI inference workloads."
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PACKAGE_ARCH = "${MACHINE_ARCH}"
inherit packagegroup

RDEPENDS:${PN} = " \
    docker-moby \
    nvidia-docker \
    nvidia-container-toolkit \
"
