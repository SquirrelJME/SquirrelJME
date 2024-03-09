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
 * Element type for ScritchUI look and feel.
 *
 * @since 2024/03/09
 */
@SquirrelJMEVendorApi
public interface ScritchLAFImageElementType
{
	/** List elements. */
	@SquirrelJMEVendorApi
	byte LIST_ELEMENT =
		0;
	
	/** Choice groups. */
	@SquirrelJMEVendorApi
	byte CHOICE_GROUP =
		1;
	
	/** Alert icons. */
	@SquirrelJMEVendorApi
	byte ALERT =
		2;
	
	/** Tab items. */
	@SquirrelJMEVendorApi
	byte TAB =
		3;
	
	/** Command items. */
	@SquirrelJMEVendorApi
	byte COMMAND =
		4;
	
	/** Notification. */
	@SquirrelJMEVendorApi
	byte NOTIFICATION =
		5;
	
	/** Menu. */
	@SquirrelJMEVendorApi
	byte MENU =
		6;
	
	/** The number of look and feel element types. */
	@SquirrelJMEVendorApi
	byte NUM_LAF_ELEMENT_TYPES =
		7;
}
