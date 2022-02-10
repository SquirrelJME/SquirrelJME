// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.image;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import javax.microedition.lcdui.Image;

/**
 * Serialization and deserialization for native images.
 *
 * @since 2022/02/10
 */
public final class NativeImageSerialize
{
	/**
	 * Deserializes the native image.
	 * 
	 * @param __b The buffer.
	 * @param __o The offset into the buffer.
	 * @param __l The length of the buffer.
	 * @param __factory The factory for creating images.
	 * @return The resultant deserialized image.
	 * @throws IndexOutOfBoundsException If the offet and/or length exceed
	 * the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/02/10
	 */
	public static Image deserialize(int[] __b, int __o, int __l,
		ImageFactory __factory)
		throws IndexOutOfBoundsException, NullPointerException
	{
		if (__b == null || __factory == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		throw Debugging.todo();
		/*
		// Load image parameters
		int numParameters = imageData[0];
		boolean useAlpha = false;
		int imageWidth = -1;
		int imageHeight = -1;
		int scanLength = -1;
		for (int i = 0; i < numParameters; i++)
		{
			int value = imageData[i];
			switch (i)
			{
				// Does nothing but is used above
				case NativeImageLoadParameter.STORED_PARAMETER_COUNT:
					break;
				
				case NativeImageLoadParameter.USE_ALPHA:
					useAlpha = (value != 0);
					break;
				
				case NativeImageLoadParameter.WIDTH:
					imageWidth = value;
					break;
				
				case NativeImageLoadParameter.HEIGHT:
					imageHeight = value;
					break;
				
				case NativeImageLoadParameter.SCAN_LENGTH:
					scanLength = value;
					break;
				
				default:
					Debugging.debugNote("Native image param %d = %d?",
						i, value);
					break;
			}
		}
		
		// {@squirreljme.error EB3s Image has invalid parameters.}
		if (imageWidth <= 0 || imageHeight <= 0)
			throw new IOException("EB3s");
		
		// The scan length needs to at least be the image width
		if (scanLength < imageWidth)
			scanLength = imageWidth;
		
		// Copy image data to a new buffer if the scan is the same size
		if (scanLength == imageWidth)
		{
			int rgbArea = scanLength * imageHeight;
			int[] rgbData = new int[rgbArea];
			System.arraycopy(imageData, numParameters,
				rgbData, 0, rgbArea);
			
			return Image.createRGBImage(rgbData,
				imageWidth, imageHeight, useAlpha);
		}
		
		// Otherwise, we need to unscan it so we can use our native drawing
		// functions for this process
		else
		{
			Image mutImage = Image.createImage(imageWidth, imageHeight,
				useAlpha, 0);
			
			// Draw the RGB data directly onto a mutable image
			Graphics g = mutImage.getGraphics();
			g.setBlendingMode(Graphics.SRC);
			g.drawRGB(imageData, numParameters, scanLength,
				0, 0, imageWidth, imageHeight, useAlpha);
			
			// Make it immutable
			return Image.createImage(mutImage);
		}
		*/
	}
	
	/**
	 * Serializes the given image.
	 * 
	 * @param __image The image to serialize. 
	 * @return The resultant serialization.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/02/10
	 */
	public static int[] serialize(Image __image)
		throws NullPointerException
	{
		if (__image == null)
			throw new NullPointerException("NARG");
		
		throw Debugging.todo();
		/*
		Image image = ImageReaderDispatcher.parse(in, null);
		
		// Extract image parameters
		boolean useAlpha = image.hasAlpha();
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();
		int imageArea = imageWidth * imageHeight;
		
		// Setup resultant buffer
		int[] result = new int[
			NativeImageLoadParameter.NUM_PARAMETERS + imageArea];
		
		// Setup base result with parameters
		result[NativeImageLoadParameter.STORED_PARAMETER_COUNT] =
			NativeImageLoadParameter.NUM_PARAMETERS;
		result[NativeImageLoadParameter.USE_ALPHA] =
			(useAlpha ? 1 : 0);
		result[NativeImageLoadParameter.WIDTH] = imageWidth;
		result[NativeImageLoadParameter.HEIGHT] = imageHeight;
		
		// Load the RGB into the rest of the image
		image.getRGB(result,
			NativeImageLoadParameter.NUM_PARAMETERS, imageWidth,
			0, 0, imageWidth, imageHeight);
		
		// Use this image array
		return result;
		 */
	}
}
