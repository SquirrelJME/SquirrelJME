# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# Windows?
if(WIN32)
	message("ScritchUI: Enabling Win32!")

	add_subdirectory(win32)
else()
	message("ScritchUI: Win32 not available...")
endif()

# Motif
find_package(Motif)

# Is there Motif (Common Desktop Environment)?
if(MOTIF_FOUND)
	message("ScritchUI: Enabling Motif!")

	add_subdirectory(motif)
else()
	message("ScritchUI: Motif not available...")
endif()

# Include pkg-config finder
find_package(PkgConfig)

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

	# GTK2?
	pkg_check_modules(GTK2 gtk+-2.0)

	if(GTK2_FOUND)
		message("ScritchUI: Enabling GTK2!")

		add_subdirectory(gtk2)
	else()
		message("ScritchUI: GTK2 not found...")
	endif()

	# GTK3?
	pkg_check_modules(GTK3 gtk+-3.0)

	if (GTK3_FOUND)
		message("ScritchUI: Enabling GTK3!")

		add_subdirectory(gtk3)
	else()
		message("ScritchUI: GTK3 not found...")
	endif()
else()
	message("ScritchUI: pkg-config not found...")
endif()
