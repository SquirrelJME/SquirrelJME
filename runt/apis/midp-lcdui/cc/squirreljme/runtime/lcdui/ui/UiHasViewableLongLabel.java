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
 * This represents something has has a long label which can be read.
 *
 * @since 2018/04/06
 */
public interface UiHasViewableLongLabel
	extends UiInterface
{
	/**
	 * Gets the long label for this item, this is one that may be displayed
	 * when there is sufficient room available.
	 *
	 * @return The long label to be displayed, will be {@code null} if none
	 * is set.
	 * @since 2018/04/04
	 */
	public abstract String getLongLabel();
}

