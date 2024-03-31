# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Various hardening options.

# For RetroArch disable specific features so they cannot be used
if(LIBRETRO)
	# Disable dynamic library support, we do not want to allow loading
	# native libraries of any kind
	add_compile_definitions(SJME_CONFIG_HAS_NO_DYLIB_SUPPORT=1)

	# Disable ScritchUI dynamic libraries
	add_compile_definitions(SJME_CONFIG_SCRITCHUI_NO_DYLIB=1)
	set(SQUIRRELJME_SCRITCHUI_NO_DYLIB ON)
endif()
