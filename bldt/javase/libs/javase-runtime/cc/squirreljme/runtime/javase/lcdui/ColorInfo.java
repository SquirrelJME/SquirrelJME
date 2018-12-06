// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.javase.lcdui;

import cc.squirreljme.runtime.lcdui.gfx.PixelFormat;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.DataBufferShort;
import java.awt.image.IndexColorModel;
import java.awt.Transparency;

/**
 * The type of color information to use.
 *
 * @since 2018/03/24
 */
public final class ColorInfo
{
	/**
	 * {@squirreljme.property cc.squirreljme.runtime.javase.lcdui.pixelformat=f
	 * This selects the format to use for the display when creating images.}
	 */
	public static final String PIXEL_FORMAT_PROPERTY =
		"cc.squirreljme.runtime.javase.lcdui.pixelformat";
	
	/** The pixel format of the frame. */
	public static final PixelFormat PIXEL_FORMAT;
	
	/** The type of image to create. */
	public static final int IMAGE_TYPE;
	
	/** The color model to use. */
	public static final IndexColorModel COLOR_MODEL;
	
	/**
	 * Initializes the color information.
	 *
	 * @since 2018/03/24
	 */
	static
	{
		// Set the pixel format
		PixelFormat pf;
		PIXEL_FORMAT = (pf = PixelFormat.valueOf(
			System.getProperty(PIXEL_FORMAT_PROPERTY, "INTEGER_RGB888")));
		
		// Set the type of data to use for buffered images
		int btype;
		IndexColorModel icm;
		switch (pf)
		{
			case BYTE_INDEXED1:
				btype = BufferedImage.TYPE_BYTE_BINARY;
				icm = new IndexColorModel(1, 2, new int[]{
					0xFF000000, 0xFFFFFFFF}, 0, false, Transparency.OPAQUE,
					DataBuffer.TYPE_BYTE);
				break;
				
			case BYTE_INDEXED2:
				btype = BufferedImage.TYPE_BYTE_BINARY;
				icm = new IndexColorModel(2, 4, new int[]{
					0xFF_000000,
					0xFF_55FFFF,
					0xFF_FF55FF,
					0xFF_FFFFFF}, 0, false, Transparency.OPAQUE,
					DataBuffer.TYPE_BYTE);
				break;
				
			case BYTE_INDEXED4:
				btype = BufferedImage.TYPE_BYTE_BINARY;
				icm = new IndexColorModel(4, 16, new int[]{
					0xFF_000000,
					0xFF_808080,
					0xFF_C0C0C0,
					0xFF_FFFFFF,
					0xFF_800000,
					0xFF_FF0000,
					0xFF_808000,
					0xFF_FFFFFF,
					0xFF_008000,
					0xFF_00FF00,
					0xFF_008080,
					0xFF_00FFFF,
					0xFF_000080,
					0xFF_0000FF,
					0xFF_800080,
					0xFF_FF00FF,
					}, 0, false, Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
				break;
				
			case BYTE_INDEXED8:
				btype = BufferedImage.TYPE_BYTE_INDEXED;
				throw new todo.TODO();
				
			case SHORT_RGB565:
				btype = BufferedImage.TYPE_USHORT_565_RGB;
				icm = null;
				break;
				
			case INTEGER_ARGB8888:
				btype = BufferedImage.TYPE_INT_ARGB;
				icm = null;
				break;
				
			case INTEGER_RGB888:
				btype = BufferedImage.TYPE_INT_RGB;
				icm = null;
				break;
			
				// {@squirreljme.error AF09 Cannot use the specified pixel
				// format. (The pixel format to use)}
			case BYTE_RGB332:
			case SHORT_INDEXED16:
			case SHORT_ARGB4444:
				throw new RuntimeException(String.format("AF09 %s", pf));
			
			default:
				throw new todo.OOPS();
		}
		
		// Store
		IMAGE_TYPE = btype;
		COLOR_MODEL = icm;
	}
	
	/**
	 * Creates a buffered image using the color parameters.
	 *
	 * @param __w The width.
	 * @param __h The height.
	 * @param __c The initial color.
	 * @return The resulting image.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/24
	 */
	public static BufferedImage create(int __w, int __h, Color __c)
		throws NullPointerException
	{
		BufferedImage rv;
		
		// Setup image
		IndexColorModel icm = ColorInfo.COLOR_MODEL;
		if (icm != null)
			rv = new BufferedImage(__w, __h, ColorInfo.IMAGE_TYPE, icm);
		else
			rv = new BufferedImage(__w, __h, ColorInfo.IMAGE_TYPE);
		
		// Fill the background with the default background color
		Graphics2D g = (Graphics2D)rv.getGraphics();
		g.setColor(__c);
		g.fillRect(0, 0, __w, __h);
		
		return rv;
	}
	
	/**
	 * Returns the array for the given image.
	 *
	 * @param __bi The buffer to read from.
	 * @return The array for the image data.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/24
	 */
	public static Object getArray(BufferedImage __bi)
		throws NullPointerException
	{
		if (__bi == null)
			throw new NullPointerException("NARG");
		
		DataBuffer db = __bi.getRaster().getDataBuffer();
		PixelFormat pf;
		switch ((pf = ColorInfo.PIXEL_FORMAT))
		{
			case BYTE_INDEXED1:
			case BYTE_INDEXED2:
			case BYTE_INDEXED4:
			case BYTE_INDEXED8:
			case BYTE_RGB332:
				return ((DataBufferByte)db).getData();
				
			case SHORT_INDEXED16:
			case SHORT_ARGB4444:
			case SHORT_RGB565:
				return ((DataBufferShort)db).getData();
				
			case INTEGER_ARGB8888:
			case INTEGER_RGB888:
				return ((DataBufferInt)db).getData();
			
				// {@squirreljme.error AF0a Unsupported pixel format.
				// (The pixel format to use)}
			default:
				throw new RuntimeException(String.format("AF0a %s", pf));
		}
	}
	
	/**
	 * Returns the palette to be used for the given image.
	 *
	 * @param __bi The palette to get for the image.
	 * @return The resulting palette or {@code null} if there is none.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/02/24
	 */
	public static int[] getPalette(BufferedImage __bi)
		throws NullPointerException
	{
		if (__bi == null)
			throw new NullPointerException("NARG");
		
		IndexColorModel model = ColorInfo.COLOR_MODEL;
		PixelFormat pf;
		switch ((pf = ColorInfo.PIXEL_FORMAT))
		{
			case BYTE_INDEXED1:
			case BYTE_INDEXED2:
			case BYTE_INDEXED4:
			case BYTE_INDEXED8:
			case SHORT_INDEXED16:
				int n = model.getNumColorComponents();
				int[] rv = new int[n];
				model.getRGBs(rv);
				return rv;
				
			case BYTE_RGB332:
			case SHORT_ARGB4444:
			case SHORT_RGB565:
			case INTEGER_ARGB8888:
			case INTEGER_RGB888:
				return null;
			
				// {@squirreljme.error AF0b Unsupported pixel format.
				// (The pixel format to use)}
			default:
				throw new RuntimeException(String.format("AF0b %s", pf));
		}
	}
}

