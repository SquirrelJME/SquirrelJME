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
	
	/** Character bitmap, no blending. */
	CHARBITMAP_NOBLEND
	{
		/**
		 * {@inheritDoc}
		 * @since 2019/03/24
		 */
		public void function(AdvancedGraphics __ag, int[] __vi, Object[] __va)
		{
			int __color = __vi[0],
				__dsx = __vi[1],
				__dsy = __vi[2],
				__bytesperscan = __vi[3],
				__scanoff = __vi[4],
				__scanlen = __vi[5],
				__lineoff = __vi[6],
				__linelen = __vi[7];
			byte[] __bmp = (byte[])__va[0];
			
			int[] data = __ag.buffer;
			int offset = __ag.offset,
				pitch = __ag.pitch;
			
			// Treat lens as end indexes
			int origscanlen = __scanlen;
			__scanlen += __scanoff;
			__linelen += __lineoff;
			
			// Remember the old scan offset, because we reset
			int resetscanoff = __scanoff;
			
			// Determine the draw pointer for this line
			int basep = offset + (__dsy * pitch) + __dsx;
			
			// Base source offset line according to the line offset
			int bi = __lineoff * __bytesperscan;
			
			// Drew each line
			for (; __lineoff < __linelen; __lineoff++, __dsy++)
			{
				// Reset parameters for this line
				int p = basep;
				__scanoff = resetscanoff;
				
				// Draw each scan from the bitmap
				for (; __scanoff < __scanlen; __scanoff++, p++)
				{
					// Get the byte that represents the scan here
					byte b = __bmp[bi + (__scanoff >>> 3)];	
					
					// If there is a pixel here, draw it
					if ((b & (1 << (__scanoff & 0x7))) != 0)
						data[p] = __color;
				}
				
				// Move the source and dest pointers to the next line
				basep += pitch;
				bi += __bytesperscan;
			}
		}
	},
	
	/** Character bitmap, with blending. */
	CHARBITMAP_BLEND
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

