# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# Declare base library
squirreljme_scritchany_add_library(audio WinMM
	winmmCore.c
	winmmLoop.c
	winmmMidi.c
	winmmStream.c)

# Standard properties for dynamic libraries
squirreljme_scritchany_standard_properties(audio WinMM)

# Windows Multimedia Library is needed here
squirreljme_scritchany_link_libraries(audio WinMM PUBLIC
	"winmm${SQUIRRELJME_WIN_LIB_SUFFIX}")
