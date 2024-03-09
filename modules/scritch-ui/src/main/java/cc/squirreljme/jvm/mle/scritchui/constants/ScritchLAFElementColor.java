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
	@SquirrelJMEVendorApi
	byte BACKGROUND =
		0;
	
	@SquirrelJMEVendorApi
	byte BORDER =
		1;
	
	@SquirrelJMEVendorApi
	byte FOREGROUND =
		2;
	
	@SquirrelJMEVendorApi
	byte HIGHLIGHTED_BACKGROUND =
		3;
	
	@SquirrelJMEVendorApi
	byte HIGHLIGHTED_BORDER =
		4;
	
	@SquirrelJMEVendorApi
	byte HIGHLIGHTED_FOREGROUND =
		5;
	
	@SquirrelJMEVendorApi
	byte IDLE_BACKGROUND =
		6;
	
	@SquirrelJMEVendorApi
	byte IDLE_FOREGROUND =
		7;
	
	@SquirrelJMEVendorApi
	byte IDLE_HIGHLIGHTED_BACKGROUND =
		8;
	
	@SquirrelJMEVendorApi
	byte IDLE_HIGHLIGHTED_FOREGROUND =
		9;
	
	@SquirrelJMEVendorApi
	byte NUM_ELEMENT_COLOR =
		10;
}
