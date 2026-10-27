# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# Declare base library
squirreljme_multilib_add_static_library(ScritchUiPure
	pureComponent.c
	pureContainer.c
	pureCore.c
	pureIntern.c
	pureLabel.c
	pureLaf.c
	pureLoop.c
	pureMenu.c
	purePanel.c
	purePencil.c
	pureScreen.c
	pureScrollPanel.c
	pureView.c
	pureWindow.c)

# Include main headers
squirreljme_multilib_static_include_directories(ScritchUiPure PUBLIC
	"${CMAKE_SOURCE_DIR}/include")

# ScritchUi requires a normalized library name
squirreljme_target_binary_name(ScritchUiPure
	"squirreljme-scritchui-pure")

# Set C language if not set
squirreljme_multilib_static_linker_language(ScritchUiPure C)
