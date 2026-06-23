# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# Check pkg-config first
if(PKG_CONFIG_FOUND)
	pkg_check_modules(X11 x11)
endif()

# Otherwise, fallback to CMake detection
if(NOT X11_FOUND)
	find_package(X11)
endif()
