# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# Declare library
squirreljme_multilib_add_static_library(ScritchUIFB
	fbChoice.c
	fbComponent.c
	fbContainer.c
	fbCore.c
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
squirreljme_multilib_static_target_include_directories(ScritchUIFB PUBLIC
	"${CMAKE_SOURCE_DIR}/include")

# ScritchUI requires a normalized library name
squirreljme_target_binary_name(ScritchUIFB
	"squirreljme-scritchui-fb")
