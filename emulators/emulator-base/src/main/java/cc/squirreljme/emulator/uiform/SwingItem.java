// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.uiform;

import cc.squirreljme.jvm.mle.brackets.UIItemBracket;

/**
 * This is the base call for all of the item types that are implementing on
 * Swing.
 *
 * @since 2020/07/18
 */
public abstract class SwingItem
	implements UIItemBracket
{
	/**
	 * Deletes the item.
	 * 
	 * @since 2020/07/18
	 */
	public abstract void delete();
}
