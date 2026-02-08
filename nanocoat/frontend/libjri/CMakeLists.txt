# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# Define library
add_library(libJri SHARED
	jvInit.c)

# Always make this position independent
squirreljme_always_fpic(libJri)

# The base SquirrelJME core needs to be built
squirreljme_link_libraries_required(libJri PUBLIC
	CoreStatic
	ScritchAudioCoreStatic
	ScritchUiCoreStatic)

# We need to find all of the includes and such
target_include_directories(libJri PUBLIC
	"${CMAKE_SOURCE_DIR}/include"
	"${CMAKE_SOURCE_DIR}/include/3rdparty/jri")

