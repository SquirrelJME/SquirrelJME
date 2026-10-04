// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.scritchui.constants;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMENativeApi;

/**
 * Element type for ScritchUI look and feel.
 *
 * @since 2024/03/09
 */
@SquirrelJMENativeApi(min = "0.4.0")
public interface ScritchLAFImageElementType
{
	/** List elements. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte LIST_ELEMENT =
		0;
	
	/** Choice groups. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte CHOICE_GROUP =
		1;
	
	/** Alert icons. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte ALERT =
		2;
	
	/** Tab items. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte TAB =
		3;
	
	/** Command items. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte COMMAND =
		4;
	
	/** Notification. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte NOTIFICATION =
		5;
	
	/** Menu. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte MENU =
		6;
	
	/** The number of look and feel element types. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte NUM_LAF_ELEMENT_TYPES =
		7;
}
