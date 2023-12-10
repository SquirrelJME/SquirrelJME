# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Docker Detection, really only used for testing

# Find Docker
find_program(DOCKER docker)

# Do we have Docker?
if(DOCKER)
	# Set that we have it
	set(DOCKER_FOUND ON)
endif()
