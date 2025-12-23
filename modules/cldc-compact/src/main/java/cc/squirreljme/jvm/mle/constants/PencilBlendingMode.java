// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;

/**
 * Blending modes that are possible under ScritchPencil.
 *
 * @since 2025/12/22
 */
@SquirrelJMEVendorApi
public interface PencilBlendingMode
{
	/** Blend with source and multiply. */
	@SquirrelJMEVendorApi
	byte SRC_OVER = 
		0;
	
	/** Use only the source alpha color. */
	@SquirrelJMEVendorApi
	byte SRC = 
		1;
	
	/** Discard source pixels that do not overlap the destination. */
	@SquirrelJMEVendorApi
	byte SRC_ATOP = 
		2;
	
	/** Keep source pixels that overlap the destination, discard others. */
	@SquirrelJMEVendorApi
	byte SRC_IN = 
		3;
	
	/** Keep source pixels that do not overlap the destination. */
	@SquirrelJMEVendorApi
	byte SRC_OUT = 
		4;
	
	/** Blend destination and source. */
	@SquirrelJMEVendorApi
	byte DEST_OVER = 
		5;
	
	/** Use only the destination. */
	@SquirrelJMEVendorApi
	byte DEST = 
		6;
	
	/** Discard destination pixels that do not overlap the source. */
	@SquirrelJMEVendorApi
	byte DEST_ATOP = 
		7;
	
	/** Keep destination pixels that overlap the source, discard others. */
	@SquirrelJMEVendorApi
	byte DEST_IN = 
		8;
	
	/** Keep destination pixels that do not overlap the source. */
	@SquirrelJMEVendorApi
	byte DEST_OUT = 
		9;
	
	/** Clear everything. */
	@SquirrelJMEVendorApi
	byte CLEAR = 
		10;
	
	/** XOR. */
	@SquirrelJMEVendorApi
	byte XOR = 
		11;
	
	/** The number of blending modes. */
	@SquirrelJMEVendorApi
	byte NUM_BLENDS = 
		12;
}
