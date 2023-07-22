// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.gfx;

import cc.squirreljme.runtime.cldc.debug.Debugging;

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
		@Override
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
		@Override
		public void function(AdvancedGraphics __ag, int[] __vi, Object[] __va)
		{
			int pac = __ag.paintcolorhigh;
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
		@Override
		public void function(AdvancedGraphics __ag, int[] __vi, Object[] __va)
		{
			int pac = __ag.paintalphacolor;
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
		@Override
		public void function(AdvancedGraphics __ag, int[] __vi, Object[] __va)
		{
			int __color = __vi[0] | 0xFF_000000,
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
		@Override
		public void function(AdvancedGraphics __ag, int[] __vi, Object[] __va)
		{
			// Not implemented, use basic function
			AdvancedFunction.CHARBITMAP_NOBLEND.function(__ag, __vi, __va);
		}
	},
	
	/** Draw line, no blend or dots. */
	LINE_NOBLEND_NODOT
	{
		/**
		 * {@inheritDoc}
		 * @since 2019/03/24
		 */
		@Override
		public void function(AdvancedGraphics __ag, int[] __vi, Object[] __va)
		{
			int __x1 = __vi[0],
				__y1 = __vi[1],
				__x2 = __vi[2],
				__y2 = __vi[3];
			
			int[] data = __ag.buffer;
			int iw = __ag.pitch,
				dx = __x2 - __x1;
			
			int dy = __y2 - __y1;
			boolean neg;
			if ((neg = dy < 0))
				dy = -dy;	
			
			int sy = (__y1 < __y2 ? 1 : -1),
				ssy = iw * sy,
				err = (dx > dy ? dx : -dy) >> 2,
				color = __ag.paintcolorhigh,
				dest = __ag.offset + (iw * __y1) + __x1;
			
			for (;;)
			{
				// Nothing left to draw?
				if (__x1 >= __x2 &&
					((neg && __y1 <= __y2) || (!neg && __y1 >= __y2)))
					break;
				
				data[dest] = color;
				
				// Increase X
				int brr = err;
				if (brr > -dx)
				{
					err -= dy;
					__x1++;
					dest++;
				}
		
				// Increase Y
				if (brr < dy)
				{
					err += dx;
					__y1 += sy;
					dest += ssy;
				}
			}
		}
	},
	
	/** Draw line, no blend with dots. */
	LINE_NOBLEND_DOT
	{
		/**
		 * {@inheritDoc}
		 * @since 2019/03/24
		 */
		@Override
		public void function(AdvancedGraphics __ag, int[] __vi, Object[] __va)
		{
			int __x1 = __vi[0],
				__y1 = __vi[1],
				__x2 = __vi[2],
				__y2 = __vi[3];
			
			int[] data = __ag.buffer;
			int iw = __ag.pitch,
				dx = __x2 - __x1;
			
			int dy = __y2 - __y1;
			boolean neg;
			if ((neg = dy < 0))
				dy = -dy;	
			
			int sy = (__y1 < __y2 ? 1 : -1),
				ssy = iw * sy,
				err = (dx > dy ? dx : -dy) >> 2,
				color = __ag.paintcolorhigh,
				dest = __ag.offset + (iw * __y1) + __x1;
			
			/*for (boolean blip = true;; blip = !blip)*/
			for (int blip = 0, nblip = ~0;; nblip = blip, blip = ~blip)
			{
				// Nothing left to draw?
				if (__x1 >= __x2 &&
					((neg && __y1 <= __y2) || (!neg && __y1 >= __y2)))
					break;
				
				/*if (blip)
					data[dest] = color;*/
				data[dest] = (data[dest] & blip) | (color & nblip);
				
				// Increase X
				int brr = err;
				if (brr > -dx)
				{
					err -= dy;
					__x1++;
					dest++;
				}
		
				// Increase Y
				if (brr < dy)
				{
					err += dx;
					__y1 += sy;
					dest += ssy;
				}
			}
		}
	},
	
	/** Draw line, blend with no dots. */
	LINE_BLEND_NODOT
	{
		/**
		 * {@inheritDoc}
		 * @since 2019/03/24
		 */
		@Override
		public void function(AdvancedGraphics __ag, int[] __vi, Object[] __va)
		{
			// Not implemented, use basic function
			if (true)
			{
				AdvancedFunction.LINE_NOBLEND_NODOT.function(__ag, __vi, __va);
				return;
			}
			
			int __x1 = __vi[0],
				__y1 = __vi[1],
				__x2 = __vi[2],
				__y2 = __vi[3];
			
			int[] data = __ag.buffer;
			int iw = __ag.pitch,
				dx = __x2 - __x1;
			
			int dy = __y2 - __y1;
			boolean neg;
			if ((neg = dy < 0))
				dy = -dy;	
			
			int sy = (__y1 < __y2 ? 1 : -1),
				ssy = iw * sy,
				err = (dx > dy ? dx : -dy) >> 2,
				color = __ag.paintalphacolor,
				dest = __ag.offset + (iw * __y1) + __x1;
			
			throw Debugging.todo();
		}
	},
	
	/** Draw line, blend with dots. */
	LINE_BLEND_DOT
	{
		/**
		 * {@inheritDoc}
		 * @since 2019/03/24
		 */
		@Override
		public void function(AdvancedGraphics __ag, int[] __vi, Object[] __va)
		{
			// Not implemented, use basic function
			if (true)
			{
				AdvancedFunction.LINE_NOBLEND_DOT.function(__ag, __vi, __va);
				return;
			}
			
			int __x1 = __vi[0],
				__y1 = __vi[1],
				__x2 = __vi[2],
				__y2 = __vi[3];
			
			int[] data = __ag.buffer;
			int iw = __ag.pitch,
				dx = __x2 - __x1;
			
			int dy = __y2 - __y1;
			boolean neg;
			if ((neg = dy < 0))
				dy = -dy;	
			
			int sy = (__y1 < __y2 ? 1 : -1),
				ssy = iw * sy,
				err = (dx > dy ? dx : -dy) >> 2,
				color = __ag.paintalphacolor,
				dest = __ag.offset + (iw * __y1) + __x1;
			
			throw Debugging.todo();
		}
	},
	
	/** RGB Tile, no blending. */
	RGBTILE_NOBLEND
	{
		/**
		 * {@inheritDoc}
		 * @since 2019/03/24
		 */
		@Override
		public void function(AdvancedGraphics __ag, int[] __vi, Object[] __va)
		{
			int __o = __vi[0],
				__l = __vi[1],
				__x = __vi[2],
				__y = __vi[3],
				__w = __vi[4],
				__h = __vi[5],
				__subX = __vi[6],
				__subY = __vi[7];
			int[] __b = (int[])__va[0];
			
			int[] data = __ag.buffer;
			int iw = __ag.pitch;
			
			// The distance from the end of a row to the scanline, this way the
			// source variable does not need an extra copy
			int eosa = __l - __w;
			
			// Draw tile data
			int dest = __ag.offset + (__y * iw) + __x;
			int src = __o + __subX + (__subY * iw);
			int ey = __y + __h;
			for (; __y < ey; __y++, dest += iw, src += eosa)
				for (int spend = src + __w, dp = dest; src < spend;
					dp++, src++)
					try
					{
						data[dp] = __b[src] | 0xFF_000000;
					}
					catch (IndexOutOfBoundsException e)
					{
						if (!AdvancedFunction._didOops)
						{
							AdvancedFunction._didOops = true;
						
							Debugging.debugNote("Oops: " +
								"__o=%d, __l=%d, __x=%d, __y=%d, " +
								"__w=%d, __h=%d, __subX=%d, __subY=%d, " +
								"__b[%d]=%s, data[%d]=%s, iw=%d, eosa=%d, " +
								"dest=%d, src=%d, ey=%d, spend=%d !!",
								__o, __l, __x, __y, __w, __h, __subX, __subY,
								__b.length, __b, data.length, data, iw, eosa,
								dest, src, ey, spend);
						}
					}
		}
	},
	
	/** RGB Tile, blending. */
	RGBTILE_BLEND
	{
		/**
		 * {@inheritDoc}
		 * @since 2019/03/24
		 */
		@Override
		public void function(AdvancedGraphics __ag, int[] __vi, Object[] __va)
		{
			int __o = __vi[0],
				__l = __vi[1],
				__x = __vi[2],
				__y = __vi[3],
				__w = __vi[4],
				__h = __vi[5],
				__subX = __vi[6],
				__subY = __vi[7];
			int[] __b = (int[])__va[0];
			
			int[] data = __ag.buffer;
			int iw = __ag.pitch;
			
			// The distance from the end of a row to the scanline, this way the
			// source variable does not need an extra copy
			int eosa = __l - __w;
			
			// Draw tile data
			int dest = __ag.offset + (__y * iw) + __x;
			int src = __o + __subX + (__subY * iw);
			int ey = __y + __h;
			for (; __y < ey; __y++, dest += iw, src += eosa)
				for (int spend = src + __w, dp = dest; src < spend;
					dp++, src++)
				{
					int pac = __b[src] | 0xFF_000000,
						srb = ((pac & 0xFF00FF) * 0xFF),
						sgg = (((pac >>> 8) & 0xFF) * 0xFF),
						dcc = data[dp],
						xrb = (srb + ((dcc & 0xFF00FF) * 0x00)) >>> 8,
						xgg = (((sgg +
							(((dcc >>> 8) & 0xFF) * 0x00)) + 1) * 257) >>> 16;
					
					data[dp] = ((xrb & 0xFF00FF) | ((xgg & 0xFF) << 8));
				}
		}
	},
	
	/** ARGB Tile, no blending. */
	ARGBTILE_NOBLEND
	{
		/**
		 * {@inheritDoc}
		 * @since 2019/03/24
		 */
		@Override
		public void function(AdvancedGraphics __ag, int[] __vi, Object[] __va)
		{
			int __o = __vi[0],
				__l = __vi[1],
				__x = __vi[2],
				__y = __vi[3],
				__w = __vi[4],
				__h = __vi[5],
				__subX = __vi[6],
				__subY = __vi[7];
			int[] __b = (int[])__va[0];
			
			int[] data = __ag.buffer;
			int iw = __ag.pitch;
			
			// The distance from the end of a row to the scanline, this way the
			// source variable does not need an extra copy
			int eosa = __l - __w;
			
			// Draw tile data
			int dest = __ag.offset + (__y * iw) + __x;
			int src = __o + __subX + (__subY * iw);
			int ey = __y + __h;
			for (; __y < ey; __y++, dest += iw, src += eosa)
				for (int spend = src + __w, dp = dest; src < spend;
					dp++, src++)
				{
					int pac = __b[src],
						sa = pac >>> 24,
						na = (sa ^ 0xFF),
						srb = ((pac & 0xFF00FF) * sa),
						sgg = (((pac >>> 8) & 0xFF) * sa),
						dcc = data[dp] | 0xFF_000000,
						xrb = (srb + ((dcc & 0xFF00FF) * na)) >>> 8,
						xgg = (((sgg + (((dcc >>> 8) & 0xFF) * na)) + 1) * 257)
							>>> 16;
					
					data[dp] = ((xrb & 0xFF00FF) | ((xgg & 0xFF) << 8));
				}
		}
	},
	
	/** ARGB Tile, blending. */
	ARGBTILE_BLEND
	{
		/**
		 * {@inheritDoc}
		 * @since 2019/03/24
		 */
		@Override
		public void function(AdvancedGraphics __ag, int[] __vi, Object[] __va)
		{
			int __o = __vi[0],
				__l = __vi[1],
				__x = __vi[2],
				__y = __vi[3],
				__w = __vi[4],
				__h = __vi[5],
				__subX = __vi[6],
				__subY = __vi[7];
			int[] __b = (int[])__va[0];
			
			int[] data = __ag.buffer;
			int iw = __ag.pitch;
			
			// The distance from the end of a row to the scanline, this way the
			// source variable does not need an extra copy
			int eosa = __l - __w;
			
			// Draw tile data
			int dest = __ag.offset + (__y * iw) + __x;
			int src = __o + __subX + (__subY * iw);
			int ey = __y + __h;
			for (; __y < ey; __y++, dest += iw, src += eosa)
				for (int spend = src + __w, dp = dest; src < spend;
					dp++, src++)
				{
					int pac = __b[src],
						sa = pac >>> 24,
						na = (sa ^ 0xFF),
						srb = ((pac & 0xFF00FF) * sa),
						sgg = (((pac >>> 8) & 0xFF) * sa),
						dcc = data[dp],
						xrb = (srb + ((dcc & 0xFF00FF) * na)) >>> 8,
						xgg = (((sgg + (((dcc >>> 8) & 0xFF) * na)) + 1) * 257)
							>>> 16;
					
					data[dp] = ((xrb & 0xFF00FF) | ((xgg & 0xFF) << 8));
				}
		}
	},
	
	/** End. */
	;
	
	/** Did an oops when drawing? */
	static volatile boolean _didOops;
	
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

