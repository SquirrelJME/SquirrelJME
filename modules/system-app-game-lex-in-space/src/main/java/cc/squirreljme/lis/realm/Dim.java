// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.lis.realm;

/**
 * Dimension and area related constants.
 *
 * @since 2025/12/21
 */
public interface Dim
{
	/** The dimension of an area in pixels. */
	byte AREA_PIXEL =
		64;
	
	/** The number of pixels an area spans. */
	short AREA_PIXEL_SPAN =
		4096;
	
	/** The dimension of an area in blocks. */
	byte AREA_BLOCK =
		16;
	
	/** The number of blocks that an area spans. */
	short AREA_BLOCK_SPAN =
		256;
	
	/** The dimension of an area in areas (identity). */
	byte AREA_AREA =
		1;
	
	/** The dimension of a block in pixels. */
	byte BLOCK_PIXEL =
		4;
	
	/** The number of pixels a block spans. */
	byte BLOCK_PIXEL_SPAN =
		16;
	
	/** The dimension of a block in blocks (identity). */
	byte BLOCK_TILE =
		1;
}
