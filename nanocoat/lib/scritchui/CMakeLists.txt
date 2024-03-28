# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# Include pkg-config finder
find_package(PkgConfig)

# Windows?
if(WIN32)
	message("ScritchUI: Enabling Win32!")

	add_subdirectory(win32)
else()
	message("ScritchUI: Win32 not available...")
endif()

# Only if package config was found can we reliably use these
if(PKG_CONFIG_FOUND)
	# X11?
	pkg_check_modules(X11 x11)

	if(X11_FOUND)
		message("ScritchUI: Enabling X11!")

		add_subdirectory(x11)
	else()
		message("ScritchUI: X11 not available...")
	endif()

	# Wayland?
	pkg_check_modules(WAYLAND wayland-client)

	if(WAYLAND_FOUND)
		message("ScritchUI: Enabling Wayland!")

		add_subdirectory(wayland)
	else()
		message("ScritchUI: Wayland not found...")
	endif()
else()
	message("ScritchUI: pkg-config not found...")
endif()
