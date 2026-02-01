# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# Declare base library
squirreljme_scritchany_add_library(audio OSS
	ossCore.c
	ossLoop.c
	ossMidi.c
	ossStream.c)

# Standard properties
squirreljme_scritchany_standard_properties(audio OSS)

# The file where the include lives
squirreljme_scritchany_compile_definitions(audio OSS PRIVATE
	SQUIRRELJME_OSS_INCLUDE_FILE="${OSS_INCLUDE_FILE}")

# Include OSS headers
if(DEFINED OSS_INCLUDE_DIR AND NOT OSS_INCLUDE_DIR STREQUAL "")
	squirreljme_scritchany_include_directories(audio OSS PRIVATE
		"${OSS_INCLUDE_DIR}")
endif()
