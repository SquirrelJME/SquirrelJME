# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# Include common macros to make things easier
squirreljme_include("scritchui.cmake")

# Reset any state
squirreljme_scritchany_enable(ui UI)

# Enable prototype developer ScritchUI implementations?
option(SQUIRRELJME_ENABLE_GUI_DEVELOPER
	"Enable Developer Prototype GUIs" OFF)

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
	scritchPencilFontMulti.c
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

# Collection dependencies
set(SQUIRRELJME_SCRITCHUI_COLLECT_TARGETS)
set(SQUIRRELJME_SCRITCHUI_COLLECT_LIBS)

# Windows?
option(SQUIRRELJME_ENABLE_GUI_WIN32 "Enable Win32 GUI"
	${SQUIRRELJME_ON_IF_WIN32})
if(SQUIRRELJME_ENABLE_GUI_WIN32)
	squirreljme_scritchui_build(win32 Win32)
endif()

# Mac OS Cocoa
option(SQUIRRELJME_ENABLE_GUI_COCOA "Enable Cocoa GUI"
	${SQUIRRELJME_ON_IF_APPLE_OR_GNUSTEP_DESKTOP})
if (SQUIRRELJME_ENABLE_GUI_COCOA)
	squirreljme_scritchui_build(cocoa Cocoa)
endif()

# Motif?
if(SQUIRRELJME_ENABLE_GUI_DEVELOPER)
	squirreljme_include("find-motif.cmake")

	option(SQUIRRELJME_ENABLE_GUI_MOTIF "Enable Motif GUI" ON)
	if(SQUIRRELJME_ENABLE_GUI_MOTIF)
		squirreljme_scritchui_build(motif Motif)
	endif()
endif()

# X11?
if(SQUIRRELJME_ENABLE_GUI_DEVELOPER)
	squirreljme_include("find-x11.cmake")

	option(SQUIRRELJME_ENABLE_GUI_X11 "Enable X11 GUI" ON)
	if(SQUIRRELJME_ENABLE_GUI_X11)
		squirreljme_scritchui_build(x11 X11)
	endif()
endif()

# GTK2?
squirreljme_include("find-gtk2.cmake")

if(SQUIRRELJME_ENABLE_GUI_GTK2)
	squirreljme_scritchui_build(gtk2 GTK2)
endif()

# GTK3?
if(SQUIRRELJME_ENABLE_GUI_DEVELOPER)
	squirreljme_include("find-gtk3.cmake")

	option(SQUIRRELJME_ENABLE_GUI_GTK3 "Enable GTK3 GUI" OFF)
	if(SQUIRRELJME_ENABLE_GUI_GTK3)
		squirreljme_scritchui_build(gtk3 GTK3)
	endif()
endif()
