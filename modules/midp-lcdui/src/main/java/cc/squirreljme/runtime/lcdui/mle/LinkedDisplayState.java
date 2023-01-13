// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.mle;

import javax.microedition.lcdui.Displayable;

/**
 * This is the base class for any linked display state.
 *
 * @since 2023/01/12
 */
public abstract class LinkedDisplayState
{
	/** The display this is linked to. */
	protected final LinkedDisplay display;
	
	/**
	 * Initializes the linked display state.
	 * 
	 * @param __display The display to initialize for.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/01/12
	 */
	public LinkedDisplayState(LinkedDisplay __display)
		throws NullPointerException
	{
		if (__display == null)
			throw new NullPointerException("NARG");
		
		this.display = __display;
	}
	
	/**
	 * This is called when the display is made current or not current.
	 * 
	 * @param __widget The associated widget this is bound to now.
	 * @param __isCurrent {@code true} if the display was made current,
	 * otherwise the display is no longer current or is going away from it.
	 * @since 2023/01/12
	 */
	public abstract void onCurrent(DisplayWidget __widget,
		boolean __isCurrent);
}
