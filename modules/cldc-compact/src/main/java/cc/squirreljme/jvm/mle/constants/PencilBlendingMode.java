// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMENativeApi;

/**
 * Blending modes that are possible under ScritchPencil.
 *
 * @since 2025/12/22
 */
@SquirrelJMENativeApi(min = "0.4.0")
public interface PencilBlendingMode
{
	/** Blend with source and multiply. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte SRC_OVER = 
		0;
	
	/** Use only the source alpha color. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte SRC = 
		1;
	
	/** Discard source pixels that do not overlap the destination. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte SRC_ATOP = 
		2;
	
	/** Keep source pixels that overlap the destination, discard others. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte SRC_IN = 
		3;
	
	/** Keep source pixels that do not overlap the destination. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte SRC_OUT = 
		4;
	
	/** Blend destination and source. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte DEST_OVER = 
		5;
	
	/** Use only the destination. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte DEST = 
		6;
	
	/** Discard destination pixels that do not overlap the source. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte DEST_ATOP = 
		7;
	
	/** Keep destination pixels that overlap the source, discard others. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte DEST_IN = 
		8;
	
	/** Keep destination pixels that do not overlap the source. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte DEST_OUT = 
		9;
	
	/** Clear everything. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte CLEAR = 
		10;
	
	/** XOR. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte XOR = 
		11;
	
	/** The number of blending modes. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte NUM_BLENDS = 
		12;
}
