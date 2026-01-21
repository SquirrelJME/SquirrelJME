// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nokia.mid.ui;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.jvm.mle.constants.UIPixelFormat;
import cc.squirreljme.runtime.lcdui.gfx.ExtraGraphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;
import javax.microedition.lcdui.Graphics;
import org.intellij.lang.annotations.MagicConstant;

/**
 * This is an implementation of Nokia's graphic interface which allows for
 * direct pixel access.
 *
 * @since 2021/01/04
 */
class __NokiaGraphics__
	implements DirectGraphics
{
	/** The raw graphics to use. */
	final Graphics _graphics;
	
	/**
	 * Initializes the Nokia Graphics wrapper.
	 *
	 * @param __g The graphics to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/01/04
	 */
	__NokiaGraphics__(Graphics __g)
		throws NullPointerException
	{
		if (__g == null)
			throw new NullPointerException("NARG");
		
		this._graphics = __g;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/07
	 */
	@Override
	@SquirrelJMEVendorApi
	public void drawImage(Image __img, int __x, int __y, int __anchor,
		int __manipulation)
	{
		this._graphics.drawRegion(__img, 0, 0, __img.getWidth(),
			__img.getHeight(),
			__NokiaGraphics__.__nokiaToMIDPTransform(__manipulation),
			__x, __y, __anchor);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/07
	 */
	@Override
	@SquirrelJMEVendorApi
	public void drawPixels(byte[] __pixels, byte[] __alphaMask,
		int __offset, int __scanlength, int __x, int __y, int __w, int __h,
		int __manipulation, int __format)
	{
		if (__w < 0 || __h < 0)
			throw new IllegalArgumentException("EB0b");

		if (__pixels == null)
			throw new NullPointerException("NARG");

		// Check if the manipulation is valid and fail early if not
		__NokiaGraphics__.__nokiaToMIDPTransform(__manipulation);

		// Nothing to draw, so we can exit early
		if (__w == 0 || __h == 0)
			return;

		int c = 0;
		int a = 0xFF;
		int bit, y, x, ypos, line, r, g, b;
		int[] data = new int[__w * __h];
		ExtraGraphics graphics = (ExtraGraphics)this._graphics;

		switch (__format)
		{
			// NOTE on gray scales: The higher the __pixels value on a given
			// position, the darker the pixel. This is so that the output
			// matches actual nokia devices like a 3310, 3410, etc. Which is
			// why (n - c) is used in color scaling. Alpha is scaled normally.
			case DirectGraphics.TYPE_BYTE_1_GRAY_VERTICAL:
				// Bit offset, GRAY_VERTICAL packs 8 vertical pixels in a byte.
				bit = (__offset / __scanlength) % 8;
				for (y = 0; y < __h; y++)
				{
					ypos = y * __w;
					line = ((__offset / __scanlength) + y) / 8 *
						__scanlength + (__offset % __scanlength);
					for (x = 0; x < __w; x++)
					{
						c = ((__pixels[line + x] >> bit) & 1);

						if (__alphaMask != null)
						{
							a = ((__alphaMask[line + x] >> bit) & 1)
								<< 1;

							a *= 255;
						}

						c = (1 - c) * 255;

						data[ypos + x] = (a << 24) | (c << 16) | (c << 8) | c;
					}
					bit++;
					if (bit > 7)
						bit = 0;
				}
				break;

			case DirectGraphics.TYPE_BYTE_1_GRAY:
				bit = 7 - __offset % 8;
				for (y = 0; y < __h; y++)
				{
					line = __offset + y * __scanlength;
					ypos = y * __w;
					for (x = 0; x < __w; x++)
					{
						c = ((__pixels[(line + x) / 8] >> bit) & 1);

						if (__alphaMask != null)
						{
							a = ((__alphaMask[(line + x) / 8] >> bit)
								& 1) << 1;

							a *= 255;
						}

						c = (1 - c) * 255;

						data[ypos + x] = (a << 24) | (c << 16) | (c << 8) | c;

						bit--;
						if (bit < 0)
							bit = 7;
					}

					bit -= (__scanlength - __w) % 8;

					if (bit < 0)
						bit = 8 + bit;
				}
				break;

			case DirectGraphics.TYPE_BYTE_2_GRAY:
				for (y = 0; y < __h; y++)
				{
					line = __offset + y * __scanlength;
					ypos = y * __w;

					for (x = 0; x < __w; x++)
					{
						c = (__pixels[line + x / 4] >> (6 - (2 * (x % 4))) &
							0x03);

						if (__alphaMask != null)
						{
							a = (__alphaMask[line + x / 4] >> (6 -
								(2 * (x % 4))) & 0x03);

							a *= 85;
						}

						c = (3 - c) * 85;

						data[ypos + x] = (a << 24) | (c << 16) | (c << 8) | c;
					}
				}
				break;

			case DirectGraphics.TYPE_BYTE_332_RGB:
				for (y = 0; y < __h; y++)
				{
					line = __offset + y * __scanlength;
					ypos = y * __w;

					for (x = 0; x < __w; x++)
					{
						// We have 3 bytes for red and green, 2 for blue
						c = __pixels[line + x] & 0xFF;
						r = (c >> 5) & 0x07;
						g = (c >> 2) & 0x07;
						b = (c & 0x03);

						// Thus we have to expand them to 8 bits for 888_RGB.
						// This one is a bit more complex than the one for
						// BYTE_4 and BYTE_8 types, due to 3 bits not mapping
						// perfectly to the 0x00-0xFF range with a single mul
						// operation.
						r = (r * 255) / 7;
						g = (g * 255) / 7;
						b *= 85;

						// If a transparencyMask is available, it will have
						// a full 8 bits of alpha information on each position,
						// since the transparencyMask's alpha data has to be as
						// wide as the color/gray data for a given pixel on all
						// byte types.
						if (__alphaMask != null)
							a = __alphaMask[line + x] & 0xFF;

						data[ypos + x] = (a << 24) | (r << 16) | (g << 8) | b;
					}
				}
				break;

			case DirectGraphics.TYPE_BYTE_4_GRAY:
				for (y = 0; y < __h; y++)
				{
					line = __offset + y * __scanlength;
					ypos = y * __w;

					for (x = 0; x < __w; x++)
					{
						c = (__pixels[line + x / 2] >> (4 * (1 - (x % 2)))
							& 0x0F);
						if (__alphaMask != null)
						{
							a = (__alphaMask[line + x / 2] >> (4 * (1 -
								(x % 2))) & 0x0F);

							a *= 17;
						}

						c = (15 - c) * 17;

						data[ypos + x] = (a << 24) | (c << 16) | (c << 8) | c;
					}
				}
				break;

			case DirectGraphics.TYPE_BYTE_8_GRAY:
				for (y = 0; y < __h; y++)
				{
					line = __offset + y * __scanlength;
					ypos = y * __w;

					for (x = 0; x < __w; x++)
					{
						c = 255 - (__pixels[line + x] & 0xFF);
						if (__alphaMask != null)
							a = __alphaMask[line + x] & 0xFF;

						data[ypos + x] = (a << 24) | (c << 16) | (c << 8) | c;
					}
				}
				break;

			default:
				// Unsupported format
				throw new IllegalArgumentException("EB0k: " + __format);
		}

		graphics.drawPfRegion(
			__NokiaGraphics__.__convertFormat(
			DirectGraphics.TYPE_INT_8888_ARGB, true), data, 0, __w,
			__alphaMask != null, 0, 0, __w, __h,
			__NokiaGraphics__.__nokiaToMIDPTransform(__manipulation),
			__x, __y, Graphics.TOP | Graphics.LEFT, __w, __h, __w, __h);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/07
	 */
	@Override
	@SquirrelJMEVendorApi
	public void drawPixels(short[] __pixels, boolean __transparency,
		int __offset, int __scanlength, int __x, int __y, int __w, int __h,
		int __manipulation, int __format)
	{
		if (__w < 0 || __h < 0)
			throw new IllegalArgumentException("EB0b");
		
		if (__pixels == null)
			throw new NullPointerException("NARG");

		// Check if the manipulation is valid and fail early if not
		__NokiaGraphics__.__nokiaToMIDPTransform(__manipulation);

		// Nothing to draw, so we can exit early
		if (__w == 0 || __h == 0)
			return;

		short[] data = new short[__w * __h];
		int srcIndex, y, x, line;
		ExtraGraphics graphics = (ExtraGraphics)this._graphics;

		// Prepare the pixel data
		for (y = 0; y < __h; y++)
		{
			line = y * __w;
			srcIndex = __offset + (y * __scanlength);
			for (x = 0; x < __w; x++)
			{
				data[line + x] = __pixels[srcIndex + x];
			}
		}

		graphics.drawPfRegion(__NokiaGraphics__.__convertFormat(__format,
			true), data, 0, __w, __transparency, 0, 0, __w, __h,
			__NokiaGraphics__.__nokiaToMIDPTransform(__manipulation),
			__x, __y, Graphics.TOP | Graphics.LEFT, __w, __h, __w, __h);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/07
	 */
	@Override
	@SquirrelJMEVendorApi
	public void drawPixels(int[] __pixels, boolean __transparency,
		int __offset, int __scanlength, int __x, int __y, int __w, int __h,
		@MagicConstant(flagsFromClass = DirectGraphics.class) 
		int __manipulation,
		@MagicConstant(valuesFromClass = DirectGraphics.class) int __format)
	{
		if (__pixels == null)
			throw new NullPointerException("NARG");

		if (__w < 0 || __h < 0)
			throw new IllegalArgumentException("EB0b");

		// Check if the manipulation is valid and fail early if not
		__NokiaGraphics__.__nokiaToMIDPTransform(__manipulation);

		// Nothing to draw, so we can exit early
		if (__w == 0 || __h == 0)
			return;

		int[] data = new int[__w * __h];
		int srcIndex, y, x, line;
		ExtraGraphics graphics = (ExtraGraphics)this._graphics;

		// Prepare the pixel data
		for (y = 0; y < __h; y++)
		{
			line = y * __w;
			srcIndex = __offset + (y * __scanlength);
			for (x = 0; x < __w; x++)
			{
				data[line + x] = __pixels[srcIndex + x];
			}
		}

		graphics.drawPfRegion(__NokiaGraphics__.__convertFormat(__format,
			true), data, 0, __w, __transparency, 0, 0, __w, __h,
			__NokiaGraphics__.__nokiaToMIDPTransform(__manipulation),
			__x, __y, Graphics.TOP | Graphics.LEFT, __w, __h, __w, __h);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/07
	 */
	@Override
	@SquirrelJMEVendorApi
	public void drawPolygon(int[] __xPoints, int __xOffset, int[] __yPoints,
		int __yOffset, int __nPoints, int __argbColor)
	{
		if (__xPoints == null || __yPoints == null)
			throw new NullPointerException("NARG");

		if (__xOffset < 0 || __yOffset < 0 || __nPoints < 0 ||
			__xOffset + __nPoints > __xPoints.length ||
			__yOffset + __nPoints > __yPoints.length)
			throw new IllegalArgumentException("EB0d");

		ExtraGraphics graphics = (ExtraGraphics)this._graphics;
		int oldColor = graphics.getAlphaColor();

		graphics.setAlphaColor(__argbColor, true);
		graphics.drawPolyline(__xPoints, __xOffset, __yPoints, __yOffset,
			__nPoints);
		graphics.setAlphaColor(oldColor, true);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/07
	 */
	@Override
	@SquirrelJMEVendorApi
	public void drawTriangle(int __x1, int __y1, int __x2, int __y2, int __x3,
		int __y3, int __argbColor)
	{
		ExtraGraphics graphics = (ExtraGraphics)this._graphics;
		int oldColor = graphics.getAlphaColor();

		graphics.setAlphaColor(__argbColor, true);
		graphics.drawTriangle(__x1, __y1, __x2, __y2, __x3, __y3);
		graphics.setAlphaColor(oldColor, true);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/07
	 */
	@Override
	@SquirrelJMEVendorApi
	public void fillPolygon(int[] __xPoints, int __xOffset, int[] __yPoints,
		int __yOffset, int __nPoints, int __argbColor)
	{
		if (__xPoints == null || __yPoints == null)
			throw new NullPointerException("NARG");

		if (__xOffset < 0 || __yOffset < 0 || __nPoints < 0 ||
			__xOffset + __nPoints > __xPoints.length ||
			__yOffset + __nPoints > __yPoints.length)
			throw new IllegalArgumentException("EB0d");

		ExtraGraphics graphics = (ExtraGraphics)this._graphics;
		int oldColor = graphics.getAlphaColor();

		graphics.setAlphaColor(__argbColor, true);
		graphics.fillPolygon(__xPoints, __xOffset, __yPoints, __yOffset,
			__nPoints);
		graphics.setAlphaColor(oldColor, true);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/07
	 */
	@Override
	@SquirrelJMEVendorApi
	public void fillTriangle(int __x1, int __y1, int __x2, int __y2, int __x3,
		int __y3, int __argbColor)
	{
		ExtraGraphics graphics = (ExtraGraphics)this._graphics;
		int oldColor = graphics.getAlphaColor();

		graphics.setAlphaColor(__argbColor, true);
		graphics.fillTriangle(__x1, __y1, __x2, __y2, __x3, __y3);
		graphics.setAlphaColor(oldColor, true);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/07
	 */
	@Override
	@SquirrelJMEVendorApi
	public int getAlphaComponent()
	{
		return this._graphics.getAlpha();
	}

	/**
	 * {@inheritDoc}
	 * @since 2025/12/07
	 */
	@Override
	@SquirrelJMEVendorApi
	public int getNativePixelFormat()
	{
		ExtraGraphics graphics = (ExtraGraphics)this._graphics;

		return __NokiaGraphics__.__convertFormat(graphics.getPixelFormat(),
			false);
	}

	/**
	 * {@inheritDoc}
	 * @since 2025/12/07
	 */
	@Override
	@SquirrelJMEVendorApi
	public void getPixels(byte[] __pixels, byte[] __alphaMask,
		int __offset, int __scanlength, int __x, int __y, int __w, int __h,
		int __format)
	{
		if (__pixels == null)
			throw new NullPointerException("NARG");

		int x, y, pixelPos, pixelIndex, pixelValue, byteIndex, bitIndex, c, a;
		int r, g, b;
		int[] data = new int[__w * __h];
		ExtraGraphics graphics = (ExtraGraphics)this._graphics;

		// Get data as standard RGB first, then we get it into the expected
		// format with the optional alpha mask buffer
		graphics.getPfRegion(
			__NokiaGraphics__.__convertFormat(
			DirectGraphics.TYPE_INT_8888_ARGB, true), data, 0, __w, false,
			__x, __y, __w, __h, 0);

		switch (__format)
		{
			case DirectGraphics.TYPE_BYTE_1_GRAY_VERTICAL:
				for (y = 0; y < __h; y++)
				{
					for (x = 0; x < __w; x++)
					{
						pixelIndex = (__y + y) * __w + (__x + x);
						pixelValue = data[pixelIndex];

						// Store pixel value as a bit in the pixels array
						byteIndex = (__offset + y) * __scanlength + (x / 8);
						bitIndex = x % 8;

						// Set the bit in the current byte to the scaled value.
						__pixels[byteIndex] |= ((pixelValue & 0xFF) != 0 ? 0 :
							1) << (7 - bitIndex);

						if (__alphaMask != null)
							__alphaMask[byteIndex] |=
								((pixelValue & 0xFF000000) != 0 ? 0 : 1)
								<< (7 - bitIndex);
					}
				}
				break;

			case DirectGraphics.TYPE_BYTE_1_GRAY:
				for (y = 0; y < __h; y++)
				{
					for (x = 0; x < __w; x++)
					{
						pixelIndex = (__y + y) * __w + (__x + x);
						pixelValue = data[pixelIndex];
						byteIndex = (__offset / 8) + ((y * __w + x) / 8);
						bitIndex = (y * __w + x) % 8;

						__pixels[byteIndex] |= ((pixelValue & 0xFF) != 0 ? 0 :
							1) << (7 - bitIndex);

						if (__alphaMask != null)
							__alphaMask[byteIndex] |=
								((pixelValue & 0xFF000000) != 0 ? 0 : 1)
								<< (7 - bitIndex);
					}
				}
				break;

			case DirectGraphics.TYPE_BYTE_2_GRAY:
				for (y = 0; y < __h; y++)
				{
					for (x = 0; x < __w; x++)
					{
						pixelIndex = (__y + y) * __w + (__x + x);
						pixelValue = data[pixelIndex];

						byteIndex = (__offset + y) * __scanlength + (x / 4);
						pixelPos = x % 4;

						c = (pixelValue & 0xFF) / 85;

						__pixels[byteIndex] |= c << (6 - (2 * pixelPos));
						if (__alphaMask != null)
						{
							a = ((pixelValue >> 24) & 0xFF) / 85;
							__alphaMask[byteIndex] |= a << (6 -
								(2 * pixelPos));
						}
					}
				}
				break;

			case DirectGraphics.TYPE_BYTE_332_RGB:
				for (y = 0; y < __h; y++)
				{
					for (x = 0; x < __w; x++)
					{
						pixelIndex = (__y + y) * __w + (__x + x);
						pixelValue = data[pixelIndex];

						byteIndex = (__offset + y) * __scanlength + x;
						r = (pixelValue >> 16) & 0xFF;
						g = (pixelValue >> 8) & 0xFF;
						b = pixelValue & 0xFF;

						__pixels[byteIndex] = (byte) (((r * 7 / 255) << 5)
							| ((g * 7 / 255) << 2) | (b * 3 / 255));
						if (__alphaMask != null)
						{
							a = (pixelValue >> 24) & 0xFF;
							__alphaMask[byteIndex] = (byte) a;
						}
					}
				}
				break;

			case DirectGraphics.TYPE_BYTE_4_GRAY:
				for (y = 0; y < __h; y++)
				{
					for (x = 0; x < __w; x++)
					{
						pixelIndex = (__y + y) * __w + (__x + x);
						pixelValue = data[pixelIndex];

						byteIndex = (__offset + y) * __scanlength + (x / 2);
						pixelPos = x % 2;

						c = (pixelValue & 0xFF) / 17;

						__pixels[byteIndex] |= c << (4 * (1 - pixelPos));
						if (__alphaMask != null)
						{
							a = ((pixelValue >> 24) & 0xFF) / 17;
							__alphaMask[byteIndex] |= a << (4 * (1 -
								pixelPos));
						}
					}
				}
				break;

			case DirectGraphics.TYPE_BYTE_8_GRAY:
				for (y = 0; y < __h; y++)
				{
					for (x = 0; x < __w; x++)
					{
						pixelIndex = (__y + y) * __w + (__x + x);
						pixelValue = data[pixelIndex];
						byteIndex = (__offset + y) * __scanlength + x;

						c = (pixelValue & 0xFF);
						__pixels[byteIndex] = (byte) c;

						if (__alphaMask != null)
						{
							a = (pixelValue >> 24) & 0xFF;
							__alphaMask[byteIndex] = (byte) a;
						}
					}
				}
				break;

			default:
				// Unsupported format
				throw new IllegalArgumentException("EB0k: " + __format);
		}
	}

	/**
	 * {@inheritDoc}
	 * @since 2025/12/07
	 */
	@Override
	@SquirrelJMEVendorApi
	public void getPixels(int[] __pixels, int __offset, int __scanlength,
		int __x, int __y, int __w, int __h, int __format)
	{
		ExtraGraphics graphics = (ExtraGraphics)this._graphics;

		graphics.getPfRegion(__NokiaGraphics__.__convertFormat(__format, true),
			__pixels, __offset, __scanlength, true, __x, __y, __w, __h, 0);
	}

	/**
	 * {@inheritDoc}
	 * @since 2025/12/07
	 */
	@Override
	@SquirrelJMEVendorApi
	public void getPixels(short[] __pixels, int __offset, int __scanlength,
		int __x, int __y, int __w, int __h, int __format)
	{
		ExtraGraphics graphics = (ExtraGraphics)this._graphics;

		graphics.getPfRegion(__NokiaGraphics__.__convertFormat(__format, true),
			__pixels, __offset, __scanlength, true, __x, __y, __w, __h, 0);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/07
	 */
	@Override
	@SquirrelJMEVendorApi
	public void setARGBColor(int __argbColor)
	{
		ExtraGraphics graphics = (ExtraGraphics)this._graphics;

		graphics.setAlphaColor(__argbColor, true);
	}

	/**
	 * Converts a format between {@link DirectGraphics} formats and their
	 * SquirrelJME's {@link UIPixelFormat} counterparts, in order to facilitate
	 * usage of native calls for image manipulations.
	 *
	 * @param __format The {@link DirectGraphics} format to be swapped for its
	 * {@link UIPixelFormat} equivalent or vice-versa.
	 * @param __nokiaToSJME Dictates if the format is {@link DirectGraphics}
	 * and has to be converted to {@link UIPixelFormat}, or the opposite.
	 * @return A {@link UIPixelFormat} equivalent format of the one requested
	 * by {@link DirectGraphics}, or the inverse if {@code __nokiaToSJME} is
	 * {@code false}.
	 * @throws IllegalArgumentException If the received format is not
	 * supported by either {@link DirectGraphics} or {@link UIPixelFormat}.
	 * @since 2025/12/07
	 */
	private static final int __convertFormat(int __format,
		boolean __nokiaToSJME)
	{
		if (!__nokiaToSJME)
			switch (__format)
			{
				case UIPixelFormat.INT_ARGB8888:
					return DirectGraphics.TYPE_INT_8888_ARGB;

				case UIPixelFormat.INT_RGB888:
					return DirectGraphics.TYPE_INT_888_RGB;

				case UIPixelFormat.SHORT_ARGB4444:
					return DirectGraphics.TYPE_USHORT_4444_ARGB;

				case UIPixelFormat.SHORT_RGB565:
					return DirectGraphics.TYPE_USHORT_565_RGB;

				case UIPixelFormat.SHORT_RGB555:
					return DirectGraphics.TYPE_USHORT_555_RGB;

				case UIPixelFormat.BYTE_INDEXED256:
					return DirectGraphics.TYPE_BYTE_8_GRAY;

				case UIPixelFormat.PACKED_INDEXED4:
					return DirectGraphics.TYPE_BYTE_4_GRAY;

				case UIPixelFormat.PACKED_INDEXED2:
					return DirectGraphics.TYPE_BYTE_2_GRAY;

				case UIPixelFormat.PACKED_INDEXED1:
					return DirectGraphics.TYPE_BYTE_1_GRAY;

				case UIPixelFormat.PACKED_INDEXED1_VERTICAL:
					return DirectGraphics.TYPE_BYTE_1_GRAY_VERTICAL;

				case UIPixelFormat.SHORT_RGB444:
					return DirectGraphics.TYPE_USHORT_444_RGB;

				case UIPixelFormat.SHORT_ARGB1555:
					return DirectGraphics.TYPE_USHORT_1555_ARGB;

				case UIPixelFormat.BYTE_RGB332:
					throw Debugging.todo("Support RGB_332 format");

					// Format is unsupported
				default:
					throw new IllegalArgumentException("EB0k: " + __format);
			}
		else
			switch (__format)
			{
				case DirectGraphics.TYPE_INT_8888_ARGB:
					return UIPixelFormat.INT_ARGB8888;

				case DirectGraphics.TYPE_INT_888_RGB:
					return UIPixelFormat.INT_RGB888;

				case DirectGraphics.TYPE_USHORT_4444_ARGB:
					return UIPixelFormat.SHORT_ARGB4444;

				case DirectGraphics.TYPE_USHORT_565_RGB:
					return UIPixelFormat.SHORT_RGB565;

				case DirectGraphics.TYPE_USHORT_555_RGB:
					return UIPixelFormat.SHORT_RGB555;

				case DirectGraphics.TYPE_BYTE_8_GRAY:
					return UIPixelFormat.BYTE_INDEXED256;

				case DirectGraphics.TYPE_BYTE_4_GRAY:
					return UIPixelFormat.PACKED_INDEXED4;

				case DirectGraphics.TYPE_BYTE_2_GRAY:
					return UIPixelFormat.PACKED_INDEXED2;

				case DirectGraphics.TYPE_BYTE_1_GRAY:
					return UIPixelFormat.PACKED_INDEXED1;

				case DirectGraphics.TYPE_BYTE_1_GRAY_VERTICAL:
					return UIPixelFormat.PACKED_INDEXED1_VERTICAL;

				case DirectGraphics.TYPE_USHORT_444_RGB:
					return UIPixelFormat.SHORT_RGB444;

				case DirectGraphics.TYPE_USHORT_1555_ARGB:
					return UIPixelFormat.SHORT_ARGB1555;

				case DirectGraphics.TYPE_BYTE_332_RGB:
					throw Debugging.todo("Support RGB_332 format");

					// Format is unsupported
				default:
					throw new IllegalArgumentException("EB0k: " + __format);
			}
	}

	/**
	 * Converts {@link DirectGraphics} transforms into their MIDP
	 * {@link Sprite} counterparts, in order to facilitate usage of standard
	 * Graphics API calls within a DirectGraphics object. Note that
	 * DirectGraphics' manipulations are done counter-clockwise, which means
	 * that a ROTATE_90 will resolve into MIDP {@link Sprite#TRANS_ROT270}
	 * for example.
	 *
	 * @param __trans The {@link DirectGraphics} manipulation mode to be
	 * swapped for its MIDP {@link Sprite} equivalent.
	 * @return A MIDP {@link Sprite} equivalent transform of the one requested
	 * by {@link DirectGraphics}.
	 * @throws IllegalArgumentException If the received manipulation is not
	 * supported by {@link DirectGraphics}.
	 * @since 2025/11/25
	 */
	@MagicConstant(flagsFromClass = Sprite.class)
	private static final int __nokiaToMIDPTransform(
		@MagicConstant(flagsFromClass = DirectGraphics.class) int __trans)
	{
		// Return early if there's no manipulation to be done
		if (__trans == 0 ||
			__trans == (DirectGraphics.FLIP_HORIZONTAL |
				DirectGraphics.FLIP_VERTICAL | DirectGraphics.ROTATE_180))
			return 0;

		switch (__trans)
		{
			case DirectGraphics.FLIP_VERTICAL | DirectGraphics.ROTATE_180:
			case DirectGraphics.FLIP_HORIZONTAL:
				return Sprite.TRANS_MIRROR;

			case DirectGraphics.FLIP_HORIZONTAL | DirectGraphics.ROTATE_180:
			case DirectGraphics.FLIP_VERTICAL:
				return Sprite.TRANS_MIRROR_ROT180;

			case DirectGraphics.FLIP_HORIZONTAL |
				DirectGraphics.FLIP_VERTICAL | DirectGraphics.ROTATE_270:
			case DirectGraphics.ROTATE_90:
				return Sprite.TRANS_ROT270;

			case DirectGraphics.FLIP_HORIZONTAL | DirectGraphics.FLIP_VERTICAL:
			case DirectGraphics.ROTATE_180:
				return Sprite.TRANS_ROT180;

			case DirectGraphics.FLIP_HORIZONTAL |
				DirectGraphics.FLIP_VERTICAL | DirectGraphics.ROTATE_90:
			case DirectGraphics.ROTATE_270:
				return Sprite.TRANS_ROT90;

			case DirectGraphics.FLIP_VERTICAL | DirectGraphics.ROTATE_270:
			case DirectGraphics.FLIP_HORIZONTAL | DirectGraphics.ROTATE_90:
				return Sprite.TRANS_MIRROR_ROT90;

			case DirectGraphics.FLIP_VERTICAL | DirectGraphics.ROTATE_90:
			case DirectGraphics.FLIP_HORIZONTAL | DirectGraphics.ROTATE_270:
				return Sprite.TRANS_MIRROR_ROT270;

				// The app is trying undefined transforms
			default:
				throw new IllegalArgumentException("EB3k: " + __trans);
		}
	}
}
