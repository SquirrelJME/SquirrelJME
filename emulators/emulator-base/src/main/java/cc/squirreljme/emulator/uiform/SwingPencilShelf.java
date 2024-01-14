// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.uiform;

import cc.squirreljme.jvm.mle.PencilShelf;
import cc.squirreljme.jvm.mle.brackets.PencilBracket;
import cc.squirreljme.jvm.mle.callbacks.NativeImageLoadCallback;
import cc.squirreljme.jvm.mle.constants.NativeImageLoadType;
import cc.squirreljme.jvm.mle.constants.PencilCapabilities;
import cc.squirreljme.jvm.mle.constants.UIPixelFormat;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;

/**
 * Swing implementation of {@link PencilShelf}.
 *
 * @since 2020/09/26
 */
public final class SwingPencilShelf
{
	/**
	 * Not used.
	 * 
	 * @since 2020/09/26
	 */
	private SwingPencilShelf()
	{
	}
	
	/**
	 * Returns the capabilities of the native possibly hardware accelerated
	 * pencil graphics drawing for the given pixel format.
	 * 
	 * @param __pf The {@link UIPixelFormat} being used for drawing.
	 * @throws MLECallError If the pixel format is not valid.
	 * @return The capabilities, will be the bit-field of
	 * {@link PencilCapabilities}. If there is not capability for this format
	 * then {@code 0} will be returned.
	 * @since 2020/09/25
	 */
	public static int capabilities(int __pf)
		throws MLECallError
	{
		if (__pf < 0 || __pf >= UIPixelFormat.NUM_PIXEL_FORMATS)
			throw new MLECallError("Invalid pixel format.");
		
		// Not supported at all
		return 0;
	}
	
	/**
	 * Creates a hardware reference bracket to the native hardware graphics.
	 * 
	 * @param __pf The {@link UIPixelFormat} used for the draw.
	 * @param __bw The buffer width.
	 * @param __bh The buffer height.
	 * @param __buf The target buffer to draw to, this is cast to the correct
	 * buffer format.
	 * @param __offset The offset to the start of the buffer.
	 * @param __pal The color palette, may be {@code null}. 
	 * @param __sx Starting surface X coordinate.
	 * @param __sy Starting surface Y coordinate.
	 * @param __sw Surface width.
	 * @param __sh Surface height.
	 * @throws MLECallError If the requested graphics are not valid.
	 * @since 2020/09/25
	 */
	@SuppressWarnings("unused")
	public static PencilBracket hardwareGraphics(int __pf, int __bw,
		int __bh, Object __buf, int __offset, int[] __pal, int __sx, int __sy,
		int __sw, int __sh)
		throws MLECallError
	{
		throw new MLECallError("Not supported.");
	}
	
	/**
	 * As {@link PencilShelf#nativeImageLoadRGBA(int, byte[], int, int,
	 * NativeImageLoadCallback)}.
	 * 
	 * @param __type The {@link NativeImageLoadType} to load.
	 * @param __b The buffer.
	 * @param __o The offset.
	 * @param __l The length.
	 * @param __callback The callback that performs the image loading.
	 * @return The object returned will be passed through the callback from
	 * the native callback, should return {@code null} if the load has been
	 * cancelled.
	 * @throws MLECallError If the image could not be loaded.
	 * @see NativeImageLoadCallback
	 * @since 2024/01/14
	 */
	public static Object nativeImageLoadRGBA(
		int __type, byte[] __b, int __o, int __l,
		NativeImageLoadCallback __callback)
		throws MLECallError
	{
		if (__b == null || __callback == null || __b == null)
			throw new MLECallError("Null arguments.");
		
		// Parse the image
		try (InputStream in = new ByteArrayInputStream(__b, __o, __l))
		{
			BufferedImage result = ImageIO.read(in);
			
			// Initialize image information
			int w = result.getWidth();
			int h = result.getHeight();
			__callback.initialize(w, h,
				false,
				false);
			
			// Extract image
			int totalPixels = w * h;
			int[] rgb = new int[totalPixels];
			result.getRGB(0, 0, w, h, rgb, 0, w);
			
			// If this is indexed or less, then there is a palette
			ColorModel model = result.getColorModel();
			if (model instanceof IndexColorModel)
			{
				IndexColorModel indexModel = (IndexColorModel)model;
				
				// Determine how big the palette is
				int num = indexModel.getMapSize();
				
				// Load in palette
				int[] palette = new int[num];
				indexModel.getRGBs(palette);
				
				// Send to callback
				__callback.setPalette(palette, 0, num,
					indexModel.hasAlpha());
			}
			
			// Add image data
			__callback.addImage(rgb, 0, rgb.length,
				0, result.getAlphaRaster() != null);
			
			// Finish resultant image
			return __callback.finish();
		}
		catch (IOException __e)
		{
			throw new MLECallError("Native image load failed.", __e);
		}
	}
	
	/**
	 * As {@link PencilShelf#nativeImageLoadTypes()}.
	 * 
	 * @return The bit field of {@link NativeImageLoadType} that can be
	 * natively loaded.
	 * @since 2024/01/14
	 */
	public static int nativeImageLoadTypes()
	{
		return NativeImageLoadType.LOAD_JPEG |
			NativeImageLoadType.LOAD_GIF |
			NativeImageLoadType.LOAD_PNG;
	}
}
