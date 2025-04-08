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
	pkg_check_modules(GTK3 gtk+-3.0)
endif()
