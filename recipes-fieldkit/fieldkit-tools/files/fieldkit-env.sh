#!/bin/sh
# FieldKit environment setup — sourced by /etc/profile.d/
# Makes pip and local packages work on read-only rootfs

export HOME="${HOME:-/var/roothome}"
export XDG_CACHE_HOME="/var/roothome/.cache"
export TMPDIR="/tmp"
export PIP_CACHE_DIR="/var/roothome/.cache/pip"
export PYTHONUSERBASE="/var/roothome/.local"
export PATH="/var/roothome/.local/bin:$PATH"
export PYTHONPATH="/var/roothome/.local/lib/python3.12/site-packages:$PYTHONPATH"

# Aliases for containerized tools
alias jtop='tegrastats'
