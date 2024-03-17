// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.scritchui.constants;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;

/**
 * Border layout placements.
 *
 * @since 2024/03/17
 */
@SquirrelJMEVendorApi
public interface ScritchBorderLayoutType
{
	/** The center of the layout. */
	@SquirrelJMEVendorApi
	byte CENTER =
		0;
	
	/** The start of the page. */
	@SquirrelJMEVendorApi
	byte PAGE_START =
		1;
	
	/** The end of the page. */
	@SquirrelJMEVendorApi
	byte PAGE_END =
		2;
	
	/** The start of a line. */
	@SquirrelJMEVendorApi
	byte LINE_START =
		3;
	
	/** The end of a line. */
	@SquirrelJMEVendorApi
	byte LINE_END =
		4;
	
	/** The number of layout types. */
	@SquirrelJMEVendorApi
	byte NUM_TYPES =
		5;
}
