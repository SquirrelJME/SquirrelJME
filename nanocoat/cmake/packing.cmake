# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: CMake Packaging

# Include support for packing
include(CPack)

# Generic
set(CPACK_PACKAGE_NAME
	"SquirrelJME")
set(CPACK_PACKAGE_VENDOR
	"Stephanie Gawroriski")
set(CPACK_PACKAGE_DESCRIPTION
	"Portable Java ME Virtual Machine.")
set(CPACK_PACKAGE_HOMEPAGE_URL
	"https://squirreljme.cc/")

# Debian
set(CPACK_DEBIAN_PACKAGE_NAME
	"squirreljme")
set(CPACK_DEBIAN_PACKAGE_EPOCH
	"")
set(CPACK_DEBIAN_PACKAGE_MAINTAINER
	"Stephanie Gawroriski <xerthesquirrel@gmail.com>")
set(CPACK_DEBIAN_ENABLE_COMPONENT_DEPENDS
	TRUE)
set(CPACK_DEBIAN_PACKAGE_SOURCE
	"squirreljme")

# NSIS
set(CPACK_NSIS_MUI_ICON
	"${SQUIRRELJME_ASSET_BIN}/win32.ico")
