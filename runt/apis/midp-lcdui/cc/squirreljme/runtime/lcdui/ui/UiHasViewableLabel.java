// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.ui;

/**
 * This represents something which has a label that can be read.
 *
 * @since 2018/04/04
 */
public interface UiHasViewableLabel
	extends UiInterface
{
	/**
	 * Gets the label for this item.
	 *
	 * @return The label to be displayed.
	 * @since 2018/04/04
	 */
	public abstract String getLabel();
}

