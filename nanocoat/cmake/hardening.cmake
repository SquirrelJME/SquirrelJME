# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Various hardening options.

# Strip output executable
if(SQUIRRELJME_IS_RELEASE)
	if(SQUIRRELJME_IS_GCC)
		macro(squirreljme_executable_strip target)
			add_custom_command(TARGET ${target} POST_BUILD
				DEPENDS ${target}
				COMMAND "${CMAKE_STRIP}"
				ARGS --strip-unneeded $<TARGET_FILE:${target}>)
		endmacro()
	elseif(MSVC OR
		CMAKE_C_COMPILER_ID STREQUAL "MSVC" OR
		CMAKE_CXX_COMPILER_ID STREQUAL "MSVC")
		macro(squirreljme_executable_strip target)
			squirreljme_target_link_options(${target}
				"/PDBSTRIPPED"
				"/DEBUG:NONE")
		endmacro()
	else()
		macro(squirreljme_executable_strip target)
		endmacro()
	endif()
else()
	macro(squirreljme_executable_strip target)
	endmacro()
endif()

