# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# Include common macros to make things easier
squirreljme_include("scritchui.cmake")

# Core ScritchUI Library
squirreljme_multilib_add_library(ScritchUICore
	scritchComponent.c
	scritchContainer.c
	scritchCore.c
	scritchLoop.c
	scritchPanel.c
	scritchPencil.c
	scritchScreen.c
	scritchSerialize.c
	scritchWindow.c)

# Include main headers
squirreljme_multilib_target_include_directories(ScritchUICore PUBLIC
	"${CMAKE_SOURCE_DIR}/include")

# Framebuffer implementation of ScritchUI, does not require any native
# libraries other than direct framebuffer access. This library is purely
# a static library and is included as needed...
include_directories(framebuffer)

# We can only build dynamic libraries if requested to
if(NOT SQUIRRELJME_SCRITCHUI_NO_DYLIB)
	# Collection dependencies
	set(SQUIRRELJME_SCRITCHUI_COLLECT_TARGETS)
	set(SQUIRRELJME_SCRITCHUI_COLLECT_LIBS)

	# Windows?
	squirreljme_scritchui_build(WIN32 win32 Win32)

	# Motif?
	find_package(Motif)

	squirreljme_scritchui_build(MOTIF_FOUND motif Motif)

	# Include pkg-config finder
	find_package(PkgConfig)

	# Only if package config was found can we reliably use these
	if(PKG_CONFIG_FOUND)
		# X11?
		pkg_check_modules(X11 x11)

		squirreljme_scritchui_build(X11_FOUND x11 X11)

		# GTK2?
		pkg_check_modules(GTK2 gtk+-2.0)

		squirreljme_scritchui_build(GTK2_FOUND gtk2 GTK2)

		# GTK3?
		pkg_check_modules(GTK3 gtk+-3.0)

		squirreljme_scritchui_build(GTK3_FOUND gtk3 GTK3)
	else()
		message("ScritchUI: pkg-config not found...")
	endif()

	# Collect all ScritchUI outputs into a single archive
	set(SQUIRRELJME_SCRITCHUI_COLLECT_ZIP
		"${SQUIRRELJME_DYLIB_OUTPUT_DIR}/libsquirreljme-scritchui.zip")
	set(SQUIRRELJME_SCRITCHUI_COLLECT_LIST
		"${CMAKE_CURRENT_BINARY_DIR}/libsquirreljme-scritchui.list")
	file(WRITE "${SQUIRRELJME_SCRITCHUI_COLLECT_LIST}"
		"${SQUIRRELJME_SCRITCHUI_COLLECT_NAMES_CONTENT}")
	add_custom_target(ScritchUICollectZip
		COMMAND "${CMAKE_COMMAND}" -E "tar" "cv"
			"${SQUIRRELJME_SCRITCHUI_COLLECT_ZIP}" "--format=zip" "--"
			${SQUIRRELJME_SCRITCHUI_COLLECT_LIBS}
			"${SQUIRRELJME_SCRITCHUI_COLLECT_LIST}"
		DEPENDS ${SQUIRRELJME_SCRITCHUI_COLLECT_TARGETS}
		BYPRODUCTS "${SQUIRRELJME_SCRITCHUI_COLLECT_ZIP}"
		COMMENT "Collects all ScritchUI outputs into one single Zip."
		COMMAND_EXPAND_LISTS)
endif()
