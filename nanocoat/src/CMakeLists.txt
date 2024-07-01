# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# Declare library
squirreljme_multilib_add_library(Core
	boot.c
	bytecode.c
	classy.c
	descriptor.c
	gfx.c
	jdwp.c
	loop.c
	modelessStars.c
	nvm.c
	rom.c
	task.c
	tread.c
	vmConfig.c)

# Include main headers
squirreljme_multilib_target_include_directories(Core
	"${CMAKE_SOURCE_DIR}/include")

# Libraries for linking
squirreljme_multilib_target_link_libraries(Core
	$<TARGET_PROPERTY:Base,SQUIRRELJME_LINK_LIBRARIES>)

# Doxygen
squirreljme_doxygen(Core)
