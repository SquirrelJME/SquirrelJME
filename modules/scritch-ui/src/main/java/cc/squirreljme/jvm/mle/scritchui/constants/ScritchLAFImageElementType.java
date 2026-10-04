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
public interface ScritchLAFImageElementType
{
	/** List elements. */
	byte LIST_ELEMENT =
		0;
	
	/** Choice groups. */
	byte CHOICE_GROUP =
		1;
	
	/** Alert icons. */
	byte ALERT =
		2;
	
	/** Tab items. */
	byte TAB =
		3;
	
	/** Command items. */
	byte COMMAND =
		4;
	
	/** Notification. */
	byte NOTIFICATION =
		5;
	
	/** Menu. */
	byte MENU =
		6;
	
	/** The number of look and feel element types. */
	byte NUM_LAF_ELEMENT_TYPES =
		7;
}
