# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# Enable prototype developer ScritchUi implementations?
option(SQUIRRELJME_ENABLE_GUI_DEVELOPER
	"Enable Developer Prototype GUIs" FALSE)

# Framebuffer implementation of ScritchUi, does not require any native
# libraries other than direct framebuffer access. This library is purely
# a static library and is included as needed...
add_subdirectory(framebuffer)

# Core ScritchUi Library
squirreljme_multilib_add_library(ScritchUiCore
	scritchAtomic.c
	scritchChoice.c
	scritchComponent.c
	scritchContainer.c
	scritchCore.c
	scritchFallbackFont.c
	scritchGeneric.c
	scritchIntern.c
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
	scritchPencilFontResource.c
	scritchPencilFontSqf.c
	scritchPencilRasterBlend.c
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

# The base SquirrelJME library is always needed
squirreljme_multilib_link_libraries_required(ScritchUiCore PUBLIC
	Base
	ScritchUiFB)

# Include main headers
squirreljme_multilib_include_directories(ScritchUiCore PUBLIC
	"${CMAKE_SOURCE_DIR}/include")

# Export the core library
squirreljme_multilib_export(ScritchUiCore)

# Pure Framebuffer implementation, this is the underlayer which provides
# panels and other basic Widgets
add_subdirectory(pure)

# Windows?
if(SQUIRRELJME_ENABLE_GUI_WIN32)
	squirreljme_scritchui_build(win32 Win32)
endif()

# Motif?
if(SQUIRRELJME_ENABLE_GUI_DEVELOPER)
	squirreljme_include("find-motif.cmake")

	option(SQUIRRELJME_ENABLE_GUI_MOTIF "Enable Motif GUI" TRUE)
	if(SQUIRRELJME_ENABLE_GUI_MOTIF)
		squirreljme_scritchui_build(motif Motif)
	endif()
endif()

# X11?
if(SQUIRRELJME_ENABLE_GUI_DEVELOPER)
	squirreljme_include("find-x11.cmake")

	option(SQUIRRELJME_ENABLE_GUI_X11 "Enable X11 GUI" TRUE)
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

	option(SQUIRRELJME_ENABLE_GUI_GTK3 "Enable GTK3 GUI" FALSE)
	if(SQUIRRELJME_ENABLE_GUI_GTK3)
		squirreljme_scritchui_build(gtk3 GTK3)
	endif()
endif()
