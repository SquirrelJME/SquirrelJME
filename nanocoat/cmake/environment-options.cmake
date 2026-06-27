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

# Used to simplify options
macro(squirreljme_env_option key value default)
	# Declare the option
	option(${key} "${value}" ${default})

	# Add this key to the global property list
	get_property(globalEnvOptions GLOBAL PROPERTY globalEnvOptions)
	if("${globalEnvOptions}" STREQUAL "globalEnvOptions-NOTFOUND")
		set_property(GLOBAL PROPERTY globalEnvOptions "${key}")
	else()
		list(APPEND globalEnvOptions "${key}")
		set_property(GLOBAL PROPERTY globalEnvOptions "${globalEnvOptions}")
	endif()
endmacro()

# Generate a list of options to forward to another CMake call
function(squirreljme_env_forward result)
	# Initialize list to nothing
	set(${result})

	# Go through each possible property
	get_property(globalEnvOptions GLOBAL PROPERTY globalEnvOptions)
	foreach(globalEnvOption IN LISTS globalEnvOptions)
		list(APPEND ${result} "-D${globalEnvOption}=${${globalEnvOption}}")
	endforeach()

	# Return the result
	squirreljme_bp_return_propagate(${result})
endfunction()

# Use Win32 threading when using libwine?
squirreljme_env_option(SJME_CONFIG_EO_LIBWINE_NATIVE_THREADS
	"Use libwine threading?"
	NO)
