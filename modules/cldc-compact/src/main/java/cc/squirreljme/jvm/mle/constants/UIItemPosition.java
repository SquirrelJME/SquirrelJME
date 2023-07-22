// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

/**
 * These indexes in this class are the special form indexes which specify that
 * certain form items be placed in a special position.
 * 
 * All of these values are negative and there is {@link #MIN_VALUE} that
 * is permitted.
 *
 * @since 2020/06/30
 */
public interface UIItemPosition
{
	/** The left command button. */
	@Deprecated
	byte LEFT_COMMAND =
		-1;
	
	/** The right command button. */
	@Deprecated
	byte RIGHT_COMMAND =
		-2;
	
	/** The main title which is at the top of the form always. */
	@Deprecated
	byte TITLE =
		-3;
	
	/**
	 * The ticker position which is always below the title, a scrolling effect
	 * may happen on the item.
	 */
	@Deprecated
	byte TICKER =
		-4;
	
	/** The body of the form, this acts as a "full-screen" view. */
	byte BODY =
		-5;
	
	/** The touch-screen control interface. */
	byte TOUCH_CONTROLS =
		-6;
	
	/** The lowest permitted value, no value can be lower than this. */
	byte MIN_VALUE =
		-6;
	
	/** The number of special items, for shifting. */
	byte SPECIAL_SHIFT =
		6;
	
	/** The item is not on the form. */
	int NOT_ON_FORM =
		Integer.MIN_VALUE;
}
