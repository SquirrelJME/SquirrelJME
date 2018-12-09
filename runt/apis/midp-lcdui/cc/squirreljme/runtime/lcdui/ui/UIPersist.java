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
 * Contains persistent UI information such as the selected items.
 *
 * @since 2018/12/08
 */
public final class UIPersist
{
	/** Is a recalculation going to be done? */
	public volatile boolean recalc;
	
	/** Should a repaint be done? */
	public volatile boolean repaint;
	
	/**
	 * This is called when something has been updated visually, so redraw
	 * must happen.
	 *
	 * @param __recalc Should everything be recalculated? This means there
	 * were entry changes where we need to completely determine how things
	 * are laid out instead of just rendered.
	 * @since 2018/12/09
	 */
	public final void visualUpdate(boolean __recalc)
	{
		// Repaint always
		this.repaint = true;	
		
		// Recalculate?
		if (__recalc)
			this.recalc |= true;
	}
}

