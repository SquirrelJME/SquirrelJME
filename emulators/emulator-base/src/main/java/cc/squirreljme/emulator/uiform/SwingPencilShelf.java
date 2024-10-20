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
import cc.squirreljme.jvm.mle.callbacks.NativeImageLoadCallback;
import cc.squirreljme.jvm.mle.constants.NativeImageLoadType;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

/**
 * Swing implementation of {@link PencilShelf}.
 *
 * @since 2020/09/26
 */
@SquirrelJMEVendorApi
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
	@SquirrelJMEVendorApi
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
			int[] rgb;
			
			// Is there an alpha channel?
			ColorModel model = result.getColorModel();
			boolean hasAlpha = (result.getAlphaRaster() != null);
			
			// If this is indexed, then there is a palette
			if (model instanceof IndexColorModel ||
				result.getType() == BufferedImage.TYPE_BYTE_INDEXED)
			{
				IndexColorModel indexModel = (IndexColorModel)model;
				
				// Determine how big the palette is
				int num = indexModel.getMapSize();
				
				// Load in palette
				int[] palette = new int[num];
				indexModel.getRGBs(palette);
				
				// Read indexed values
				Raster raster = result.getRaster();
				rgb = raster.getPixels(0, 0, w, h,
					new int[totalPixels]);
				
				// Which transparency is used?
				int transDx = -1;
				switch (indexModel.getTransparency())
				{
						// Completely opaque
					case Transparency.OPAQUE:
						hasAlpha = false;
						break;
						
						// Either fully opaque or transparent
					case Transparency.BITMASK:
						transDx = indexModel.getTransparentPixel();
						if (transDx < 0)
							hasAlpha = false;
						else
						{
							// Get color value of this pixel
							int px = indexModel.getRGB(transDx) & 0xFFFFFF;
						
							// We need to find the transparent index manually
							transDx = -1;
							for (int i = 0, n = palette.length;
								 i < n; i++)
								if ((palette[i] & 0xFFFFFF) == px)
								{
									transDx = i;
									break;
								}
							
							// Always has alpha as long as we found an index
							hasAlpha = (transDx >= 0);
						}
						break;
						
						// Arbitrary alpha values (such as PNG)
					case Transparency.TRANSLUCENT:
						hasAlpha = true;
						break;
				}
				
				// Send to callback
				if (!__callback.setPalette(palette, 0, num,
					hasAlpha, transDx))
				{
					// Image handler does not support indexed mode so fall
					// back to RGB
					rgb = new int[totalPixels];
					result.getRGB(0, 0, w, h, rgb, 0, w);
				}
			}
			
			// RGB Data
			else
			{
				rgb = new int[totalPixels];
				result.getRGB(0, 0, w, h, rgb, 0, w);
			}
			
			// Add image data
			__callback.addImage(rgb, 0, rgb.length,
				0, hasAlpha);
			
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
	@SquirrelJMEVendorApi
	public static int nativeImageLoadTypes()
	{
		return NativeImageLoadType.LOAD_JPEG |
			NativeImageLoadType.LOAD_GIF |
			NativeImageLoadType.LOAD_PNG;
	}
}
