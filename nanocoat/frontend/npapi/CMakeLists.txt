# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# Make this a
squirreljme_multilib_add_library(npapi
	plugin.c)

# Always make this position independent
squirreljme_always_fpic(npapi)

# We need to find all of the includes and such
target_include_directories(npapi PUBLIC
	"${CMAKE_SOURCE_DIR}/include"
	"${CMAKE_SOURCE_DIR}/include/3rdparty/npapi")

# Libraries needed to be linked with for the DyLib to function
squirreljme_link_libraries_required(npapi PRIVATE
	CoreStatic
	ScritchAudioCoreStatic
	ScritchUiCoreStatic)

# Where should this library be output?
squirreljme_target_binary_output(npapi
	"${SQUIRRELJME_DYLIB_OUTPUT_DIR}")

# Plugin library name
squirreljme_target_binary_name(npapi
	"squirreljme-npapi")

# Exports for Windows, etc.
squirreljme_shared_library_exports(npapi)
