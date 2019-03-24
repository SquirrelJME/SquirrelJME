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
			int pac = __ag.paintcolor;
			int[] buffer = __ag.buffer;
			int pitch = __ag.pitch,
				offset = __ag.offset,
				__x = __vi[0],
				__y = __vi[1],
				__ex = __vi[2],
				__ey = __vi[3],
				__w = __vi[4],
				__h = __vi[5];
			
			for (int y = __y; y < __ey; y++)
				for (int dest = offset + (y * pitch) + __x, pex = dest + __w;
					dest < pex; dest++)
					buffer[dest] = pac;
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
			int pac = __ag.paintcolor;
			int[] buffer = __ag.buffer;
			int pitch = __ag.pitch,
				offset = __ag.offset,
				__x = __vi[0],
				__y = __vi[1],
				__ex = __vi[2],
				__ey = __vi[3],
				__w = __vi[4],
				__h = __vi[5],
				sa = __ag.paintalpha,
				na = (sa ^ 0xFF),
				srb = ((pac & 0xFF00FF) * sa),
				sgg = (((pac >>> 8) & 0xFF) * sa);
			
			// Blend each color
			int mod = 0;
			for (int y = __y; y < __ey; y++)
				for (int dest = offset + (y * pitch) + __x, pex = dest + __w;
					dest < pex; dest++)
				{
					int dcc = buffer[dest],
						xrb = (srb + ((dcc & 0xFF00FF) * na)) >>> 8,
						xgg = (((sgg + (((dcc >>> 8) & 0xFF) * na)) + 1) * 257)
							>>> 16;
					
					buffer[dest] = ((xrb & 0xFF00FF) | ((xgg & 0xFF) << 8));
				}
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

