# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# Declare object only library, as this is a fallback library
squirreljme_multilib_add_static_library(ScritchUiFB OBJECT
	fbChoice.c
	fbComponent.c
	fbContainer.c
	fbCore.c
	fbFont.c
	fbGraphics.c
	fbIntern.c
	fbLabel.c
	fbLaf.c
	fbList.c
	fbLoop.c
	fbMenu.c
	fbPanel.c
	fbScreen.c
	fbScrollPanel.c
	fbView.c
	fbWindow.c)

# Include main headers
squirreljme_multilib_static_include_directories(ScritchUiFB PUBLIC
	"${CMAKE_SOURCE_DIR}/include")

# The base library is required
squirreljme_multilib_static_link_libraries_required(ScritchUiFB PUBLIC
	Base)

# Export this target
squirreljme_multilib_export(ScritchUiFB)
