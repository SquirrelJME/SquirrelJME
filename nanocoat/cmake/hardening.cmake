# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Various hardening options.

# For RetroArch disable specific features so they cannot be used
if(SQUIRRELJME_IS_LIBRETRO)
	# Disable dynamic library support, we do not want to allow loading
	# native libraries of any kind
	add_compile_definitions(SJME_CONFIG_HAS_NO_DYLIB_SUPPORT=1)

	# Disable ScritchUI dynamic libraries
	add_compile_definitions(SJME_CONFIG_SCRITCHUI_NO_DYLIB=1)
	set(SQUIRRELJME_SCRITCHUI_NO_DYLIB ON)
endif()

# Strip output executable
if(SQUIRRELJME_IS_RELEASE)
	if(CMAKE_COMPILER_IS_GNUCC OR
		CMAKE_COMPILER_IS_GNUCXX OR
		CMAKE_C_COMPILER_ID STREQUAL "GNU" OR
		CMAKE_CXX_COMPILER_ID STREQUAL "Clang")
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
			target_link_options(${target}
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
