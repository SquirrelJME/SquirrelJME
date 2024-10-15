# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# Include common macros to make things easier
squirreljme_include("scritchui.cmake")

# Framebuffer implementation of ScritchUI, does not require any native
# libraries other than direct framebuffer access. This library is purely
# a static library and is included as needed...
add_subdirectory(framebuffer)

# Pure Framebuffer implementation, this is the underlayer which provides
# panels and other basic Widgets
add_subdirectory(pure)

# Source files for ScritchUI
set(SJME_ScritchUICore_SOURCES
	scritchChoice.c
	scritchComponent.c
	scritchContainer.c
	scritchCore.c
	scritchFallbackFont.c
	scritchLabel.c
	scritchLaf.c
	scritchList.c
	scritchLoop.c
	scritchMenu.c
	scritchPanel.c
	scritchPencil.c
	scritchPencilBasic.c
	scritchPencilBitLine.c
	scritchPencilFont.c
	scritchPencilFontPseudo.c
	scritchPencilFontSqf.c
	scritchPencilRasterChunky.c
	scritchPencilRasterMisc.c
	scritchPencilRasterScan.c
	scritchPencilRasterShapes.c
	scritchPencilRasterSimple.c
	scritchPencilRasterText.c
	scritchScreen.c
	scritchScrollPanel.c
	scritchSerialize.c
	scritchText.c
	scritchView.c
	scritchWindow.c)

# Core ScritchUI Library
squirreljme_multilib_add_static_library(ScritchUICore
	${SJME_ScritchUICore_SOURCES})

# Include main headers
squirreljme_multilib_static_target_include_directories(ScritchUICore PUBLIC
	"${CMAKE_SOURCE_DIR}/include")

# We can only build dynamic libraries if requested to
if(NOT SQUIRRELJME_SCRITCHUI_NO_DYLIB OR
	DEFINED SQUIRRELJME_GRADLE)
	# Collection dependencies
	set(SQUIRRELJME_SCRITCHUI_COLLECT_TARGETS)
	set(SQUIRRELJME_SCRITCHUI_COLLECT_LIBS)

	# Windows?
	option(SQUIRRELJME_ENABLE_GUI_WIN32 "Enable Win32 GUI"
		${SQUIRRELJME_ON_IF_WIN32})
	if(SQUIRRELJME_ENABLE_GUI_WIN32)
		squirreljme_scritchui_build(WIN32 win32 Win32)
	endif()

	# Mac OS Cocoa
	option(SQUIRRELJME_ENABLE_GUI_COCOA "Enable Cocoa GUI"
		${SQUIRRELJME_ON_IF_APPLE_OR_GNUSTEP_DESKTOP})
	if (SQUIRRELJME_ENABLE_GUI_COCOA)
		squirreljme_scritchui_build(SQUIRRELJME_ENABLE_GUI_COCOA cocoa Cocoa)
	endif()

	# Motif?
	find_package(Motif)

	option(SQUIRRELJME_ENABLE_GUI_MOTIF "Enable Motif GUI" ON)
	if(SQUIRRELJME_ENABLE_GUI_MOTIF)
		squirreljme_scritchui_build(MOTIF_FOUND motif Motif)
	endif()

	# Include pkg-config finder
	find_package(PkgConfig)

	# Only if package config was found can we reliably use these
	if(PKG_CONFIG_FOUND)
		# X11?
		pkg_check_modules(X11 x11)

		option(SQUIRRELJME_ENABLE_GUI_X11 "Enable X11 GUI" ON)
		if(SQUIRRELJME_ENABLE_GUI_X11)
			squirreljme_scritchui_build(X11_FOUND x11 X11)
		endif()

		# GTK2?
		pkg_check_modules(GTK2 gtk+-2.0)

		option(SQUIRRELJME_ENABLE_GUI_GTK2 "Enable GTK2 GUI" ON)
		if(SQUIRRELJME_ENABLE_GUI_GTK2)
			squirreljme_scritchui_build(GTK2_FOUND gtk2 GTK2)
		endif()

		# GTK3?
		pkg_check_modules(GTK3 gtk+-3.0)

		option(SQUIRRELJME_ENABLE_GUI_GTK3 "Enable GTK3 GUI" OFF)
		if(SQUIRRELJME_ENABLE_GUI_GTK3)
			squirreljme_scritchui_build(GTK3_FOUND gtk3 GTK3)
		endif()
	else()
		message("ScritchUI: pkg-config not found...")
	endif()
else()
	message("ScritchUI: Dynamic library support was not enabled...")
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
