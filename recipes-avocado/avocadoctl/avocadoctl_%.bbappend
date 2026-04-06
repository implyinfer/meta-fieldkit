# User already exists in avocado-users static passwd — make useradd a no-op
# Can't remove inherit, so set param to create the same user that already exists
USERADD_PARAM:${PN} = "--system --no-create-home --shell /sbin/nologin avocado"
