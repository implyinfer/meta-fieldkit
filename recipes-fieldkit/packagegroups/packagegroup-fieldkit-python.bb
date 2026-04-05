SUMMARY = "FieldKit Python runtime and AI/ML libraries"
DESCRIPTION = "Python 3 with scientific computing, web framework, and hardware \
interface libraries for edge AI inference workloads."
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PACKAGE_ARCH = "${MACHINE_ARCH}"
inherit packagegroup

RDEPENDS:${PN} = " \
    python3 \
    python3-pip \
    python3-venv \
    python3-numpy \
    python3-pyyaml \
    python3-requests \
    python3-flask \
    python3-cryptography \
    python3-psutil \
    python3-websockets \
    python3-json \
    python3-logging \
    python3-asyncio \
    python3-multiprocessing \
    opencv \
    python3-opencv \
"
