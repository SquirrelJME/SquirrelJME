# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: ScritchUI CMake Macros

# Macro for more easily declaring ScritchUI sub-projects
macro(squirreljme_scritchui_build ifVar subDir targetBase)
	if(${ifVar})
		message("ScritchUI: Enabling ${targetBase}!")

		add_subdirectory(${subDir})

		# Collect to Zip target
		list(APPEND SQUIRRELJME_SCRITCHUI_COLLECT_TARGETS
			"ScritchUI${targetBase}")
		list(APPEND SQUIRRELJME_SCRITCHUI_COLLECT_LIBS
			"$<TARGET_FILE:ScritchUI${targetBase}>")
		list(APPEND SQUIRRELJME_SCRITCHUI_COLLECT_NAMES
			"${targetBase}")
		set(SQUIRRELJME_SCRITCHUI_COLLECT_NAMES_CONTENT
			"${targetBase}\n${SQUIRRELJME_SCRITCHUI_COLLECT_NAMES_CONTENT}")
	else()
		message("ScritchUI: ${targetBase} not available...")
	endif()
endmacro()

