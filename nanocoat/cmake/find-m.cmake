# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Check if the math library is needed

# Locate the math library, if applicable
# There are multiple ways to go about this
check_library_exists(m
	"fmodf" "" SJME_CONFIG_HAS_LIBM)
if(SJME_CONFIG_HAS_LIBM)
	set(SQUIRRELJME_LIBM "m")
else()
	# Fallback to Solaris SUN Math
	check_library_exists(sunmath
		"fmodf" "" SJME_CONFIG_HAS_LIBSUNMATH)
	if(SJME_CONFIG_HAS_LIBSUNMATH)
		set(SQUIRRELJME_LIBM "sunmath")
	else()
		find_library(SQUIRRELJME_LIBM
			NAMES m sunmath)
	endif()
endif()
