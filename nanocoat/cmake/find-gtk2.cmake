# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# Common find support
squirreljme_include("find.cmake")

# Check pkg-config first
if(PKG_CONFIG_FOUND)
	pkg_check_modules(GTK2 gtk+-2.0)
endif()

# Otherwise, fallback to CMake detection
if(NOT GTK2_FOUND)
	# Sets GTK2_INCLUDE_DIRS and GTK2_LIBRARIES
	find_package(GTK2 2.0 COMPONENTS gtk)
endif()
