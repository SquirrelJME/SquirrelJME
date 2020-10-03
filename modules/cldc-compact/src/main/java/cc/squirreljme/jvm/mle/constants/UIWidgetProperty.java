// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

import cc.squirreljme.jvm.mle.brackets.UIItemBracket;

/**
 * Integer and String properties for {@link UIItemBracket}.
 *
 * @since 2020/07/19
 */
public interface UIWidgetProperty
{
	/** Null property. */
	byte NULL =
		0;
	
	/** The label of the item. */
	byte STRING_LABEL =
		1;
	
	/** Widget width. */
	byte INT_WIDTH =
		2;
	
	/** Widget height. */
	byte INT_HEIGHT =
		3;
	
	/** Signals that a repaint should happen. */
	byte INT_SIGNAL_REPAINT =
		4;
	
	/** X position. */
	byte INT_X_POSITION =
		5;
	
	/** Y position. */
	byte INT_Y_POSITION =
		6;
	
	/** The number of properties. */
	byte NUM_PROPERTIES =
		7;
}
