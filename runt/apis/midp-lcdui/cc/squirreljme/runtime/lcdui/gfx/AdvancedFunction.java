// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.gfx;

/**
 * This contains the various functions that may be set by
 * {@link AdvancedGraphics} and called accordingly when needed.
 *
 * All functions operate on pixels which are backed by a 32-bit integer
 * array.
 *
 * @since 2019/03/24
 */
public enum AdvancedFunction
{
	/** Do nothing. */
	NOP
	{
		/**
		 * {@inheritDoc}
		 * @since 2019/03/24
		 */
		public void function(AdvancedGraphics __ag, int[] __vi, Object[] __va)
		{
		}
	},
	
	/** Fill rectangle, no blending. */
	FILLRECT_NOBLEND
	{
		/**
		 * {@inheritDoc}
		 * @since 2019/03/24
		 */
		public void function(AdvancedGraphics __ag, int[] __vi, Object[] __va)
		{
			throw new todo.TODO();
		}
	},
	
	/** Fill rectangle, with blending. */
	FILLRECT_BLEND
	{
		/**
		 * {@inheritDoc}
		 * @since 2019/03/24
		 */
		public void function(AdvancedGraphics __ag, int[] __vi, Object[] __va)
		{
			throw new todo.TODO();
		}
	},
	
	/** End. */
	;
	
	/**
	 * Executes the graphics function.
	 *
	 * @param __ag The advanced graphics state.
	 * @param __vi Integer arguments.
	 * @param __va Object arguments.
	 * @since 2019/03/24
	 */
	public abstract void function(AdvancedGraphics __ag, int[] __vi,
		Object[] __va);
}

