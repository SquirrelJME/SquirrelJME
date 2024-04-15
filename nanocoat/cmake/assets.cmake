# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Asset support


# Packing asset base directories
set(SQUIRRELJME_ASSET_RAW
	"${CMAKE_SOURCE_DIR}/cmake/packing")
set(SQUIRRELJME_ASSET_BIN
	"${CMAKE_BINARY_DIR}/packing")

# Decode all files accordingly
squirreljme_decode_dir("${SQUIRRELJME_ASSET_RAW}"
	"${SQUIRRELJME_ASSET_BIN}")
