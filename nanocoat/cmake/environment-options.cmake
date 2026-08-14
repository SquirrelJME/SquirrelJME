# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Environment specific options

# Only used internally here
define_property(GLOBAL PROPERTY globalEnvOptions
	BRIEF_DOCS "globalEnvOptions"
	FULL_DOCS "globalEnvOptions")

# Path to the local configure file
if(NOT DEFINED SJME_CONFIG_USE_PATH)
	set(SJME_CONFIG_USE_PATH "${CMAKE_BINARY_DIR}/configure.h")
	set(SJME_CONFIG_MAKE_CONFIG TRUE)
else()
	set(SJME_CONFIG_MAKE_CONFIG FALSE)
endif()

# Erase the configure file if it is being generated
if(SJME_CONFIG_MAKE_CONFIG)
	file(REMOVE "${SJME_CONFIG_USE_PATH}.work")
endif()

# Always use this path when compiling
add_compile_definitions(SJME_CONFIG_USE_PATH="${SJME_CONFIG_USE_PATH}")

# Used to simplify options
macro(squirreljme_env_option key helpText default)
	# Declare the option
	option(${key} "${helpText}" ${default})

	# Add this key to the global property list
	get_property(globalEnvOptions GLOBAL PROPERTY globalEnvOptions)
	if("${globalEnvOptions}" STREQUAL "globalEnvOptions-NOTFOUND")
		set_property(GLOBAL PROPERTY globalEnvOptions "${key}")
	else()
		list(APPEND globalEnvOptions "${key}")
		set_property(GLOBAL PROPERTY globalEnvOptions "${globalEnvOptions}")
	endif()

	# Generate configure file?
	if(SJME_CONFIG_MAKE_CONFIG)
		file(APPEND "${SJME_CONFIG_USE_PATH}.work"
			"set(${key} ${${key}} CACHE STRING \"\")\n")

		if(${key})
			file(APPEND "${SJME_CONFIG_USE_PATH}.work"
				"#define ${key} ${${key}}\n")
		endif()
	endif()
endmacro()

# Generate a list of options to forward to another CMake call
function(squirreljme_env_forward result)
	# Use the configure file
	set(${result} "-C")
	list(APPEND ${result} "${SJME_CONFIG_USE_PATH}")
	list(APPEND ${result} "-DSJME_CONFIG_USE_PATH=${SJME_CONFIG_USE_PATH}")

	# Return the result
	squirreljme_bp_return_propagate(${result})
endfunction()

# Use Win32 threading when using libwine?
squirreljme_env_option(SJME_CONFIG_EO_LIBWINE_NATIVE_THREADS
	"Use libwine threading?"
	FALSE)

# Debugging options
squirreljme_env_option(SJME_CONFIG_DEBUG_ALLOC
	"Debug output: Allocations."
	FALSE)
squirreljme_env_option(SJME_CONFIG_DEBUG_BYTECODES
	"Debug output: Bytecode operations."
	FALSE)
squirreljme_env_option(SJME_CONFIG_DEBUG_CIRCLEBUF
	"Debug output: Circle buffer."
	FALSE)
squirreljme_env_option(SJME_CONFIG_DEBUG_CLOSEABLE
	"Debug output: Closeable."
	FALSE)
squirreljme_env_option(SJME_CONFIG_DEBUG_ENTRY
	"Debug output: Method entry and exit."
	FALSE)
squirreljme_env_option(SJME_CONFIG_DEBUG_FIELD
	"Debug output: Field access."
	FALSE)
squirreljme_env_option(SJME_CONFIG_DEBUG_GC
	"Debug output: Garbage collection."
	FALSE)
squirreljme_env_option(SJME_CONFIG_DEBUG_INFLATE
	"Debug output: Deflate decompression (inflate)."
	FALSE)
squirreljme_env_option(SJME_CONFIG_DEBUG_MLE
	"Debug output: Middle Layer Emulation calls."
	FALSE)
squirreljme_env_option(SJME_CONFIG_DEBUG_TREAD
	"Debug output: Stack treads."
	FALSE)
squirreljme_env_option(SJME_CONFIG_DEBUG_VERBOSE
	"Debug output: Extra verbosity for no longer needed debug messages."
	FALSE)
squirreljme_env_option(SJME_CONFIG_DEBUG_VLSVLG
	"Debug output: Variable length Set and Get."
	FALSE)
squirreljme_env_option(SJME_CONFIG_DEBUG_ZIP
	"Debug output: Zip files."
	FALSE)

# If we made the configure file, then use whatever was generated
if(SJME_CONFIG_MAKE_CONFIG)
	file(RENAME "${SJME_CONFIG_USE_PATH}.work"
		"${SJME_CONFIG_USE_PATH}")
endif()
