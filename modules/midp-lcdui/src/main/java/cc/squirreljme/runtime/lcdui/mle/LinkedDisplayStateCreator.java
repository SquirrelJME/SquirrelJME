// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.mle;

/**
 * Creator for linked display states.
 *
 * @since 2023/01/12
 */
public interface LinkedDisplayStateCreator
{
	/**
	 * Creates the linked display state.
	 * 
	 * @param __display The display to create for.
	 * @return The created state.
	 * @since 2023/01/12
	 */
	LinkedDisplayState create(LinkedDisplay __display);
}
