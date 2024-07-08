# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# Lua implementation
if(SQUIRRELJME_ENABLE_LUA)
	add_subdirectory(lua)
endif()

# Mini Zip implementation
if(SQUIRRELJME_ENABLE_MINIZ)
	add_subdirectory(miniz)
endif()
