# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Compiler shims

# Determine the basename of a path
function(squirreljme_basename_path dest src)
	# Get positions of the last slashes
	string(FIND "${src}" "/" fs REVERSE)
	string(FIND "${src}" "\\" bs REVERSE)

	# Bump both up by one, to exclude the slash
	math(EXPR fs "(${fs}) + 1")
	math(EXPR bs "(${bs}) + 1")

	# Has forward slash last
	if("${fs}" GREATER "${bs}")
		string(SUBSTRING "${src}" ${fs} -1 result)

	# Has backslash last
	elseif("${bs}" GREATER "${fs}")
		string(SUBSTRING "${src}" ${bs} -1 result)

	# Has neither last, or not found (both -1)
	else()
		set(result "${src}")
	endif()

	# Return the result of it
	set(${dest} "${result}" PARENT_SCOPE)
endfunction()

# C++ compiler set but missing C compiler?
if(NOT "${CMAKE_CXX_COMPILER}" STREQUAL "" AND
	"${CMAKE_C_COMPILER}" STREQUAL "")
	set(CMAKE_C_COMPILER "${CMAKE_CXX_COMPILER}")
endif()

# Determine basename of the C compiler
squirreljme_basename_path(cCompilerBase "${CMAKE_C_COMPILER}")

# SDCC?
if("${cCompilerBase}" STREQUAL "sdcc${CMAKE_EXECUTABLE_SUFFIX}")
	message(STATUS "Shimming SDCC...")
	squirreljme_include_nanocoat("compiler-shim-sdcc.cmake")
endif()
