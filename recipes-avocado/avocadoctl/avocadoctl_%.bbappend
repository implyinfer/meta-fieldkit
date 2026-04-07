# Ensure avocado-users (which provides /etc/passwd with the avocado user)
# is installed before avocadoctl's useradd preinst runs
RDEPENDS:${PN}:prepend = "avocado-users "
