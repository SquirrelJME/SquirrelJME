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
 * Color for a ScritchUI element.
 *
 * @since 2024/03/09
 */
@SquirrelJMEVendorApi
public interface ScritchLAFElementColor
{
	/** Background color. */
	@SquirrelJMEVendorApi
	byte BACKGROUND =
		0;
	
	/** Border color. */
	@SquirrelJMEVendorApi
	byte BORDER =
		1;
	
	/** Foreground color. */
	@SquirrelJMEVendorApi
	byte FOREGROUND =
		2;
	
	/** Highlighted background color. */
	@SquirrelJMEVendorApi
	byte HIGHLIGHTED_BACKGROUND =
		3;
	
	/** Highlighted border color. */
	@SquirrelJMEVendorApi
	byte HIGHLIGHTED_BORDER =
		4;
	
	/** Highlighted foreground color. */
	@SquirrelJMEVendorApi
	byte HIGHLIGHTED_FOREGROUND =
		5;
	
	@SquirrelJMEVendorApi
	byte NUM_LAF_ELEMENT_COLOR =
		6;
}
