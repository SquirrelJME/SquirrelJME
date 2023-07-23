# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Functions and macros for ROM source code, allows all the
# ROM source information to be duplicated accordingly and to change all of
# at once instead of auto-generating new code.

# Initializes the structure and otherwise needed for ROM libraries
# Remaining argv are sources
function(squirreljme_romLibrary libNameStr filesList)
	# Add object library which includes all the output object files
	add_library("SquirrelJMEROM${libNameStr}Object" OBJECT
		${filesList})

	# Define static library for linking
	add_library("SquirrelJMEROM${libNameStr}Static" STATIC
		$<TARGET_OBJECTS:SquirrelJMEROM${libNameStr}Object>)

	# Define dynamic library for dynamic linking
	add_library("SquirrelJMEROM${libNameStr}Shared" SHARED
		$<TARGET_OBJECTS:SquirrelJMEROM${libNameStr}Object>)
endfunction()
