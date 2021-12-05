// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.jvm.mle.PencilShelf;
import cc.squirreljme.jvm.mle.constants.NativeImageLoadParameter;
import cc.squirreljme.jvm.mle.constants.NativeImageLoadType;
import cc.squirreljme.jvm.mle.constants.UIPixelFormat;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.lcdui.image.ImageReaderDispatcher;
import cc.squirreljme.vm.springcoat.exceptions.SpringMLECallError;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.lcdui.Image;

/**
 * Handling for {@link PencilShelf}.
 *
 * @since 2020/09/26
 */
public enum MLEPencil
	implements MLEFunction
{
	/** {@link PencilShelf#capabilities(int)}. */
	CAPABILITIES("capabilities:(I)I")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/09/26
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			int pf = (int)__args[0];
			if (pf < 0 || pf >= UIPixelFormat.NUM_PIXEL_FORMATS)
				throw new SpringMLECallError("Invalid pixel format: " +
					pf);
			
			return 0;
		}
	},
	
	/** {@link PencilShelf#nativeImageLoadTypes()}. */
	NATIVE_IMAGE_LOAD_TYPES("nativeImageLoadTypes:()I")
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/12/05
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			// Say we support all, because we just use our own dispatcher!
			return NativeImageLoadType.ALL_TYPES;
		}
	},
	
	/** {@link PencilShelf#nativeImageLoadRGBA(int, byte[], int, int)}. */
	NATIVE_IMAGE_LOAD_RGBA("nativeImageLoadRGBA:(I[BII)[I")
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/12/05
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			// Load parameters
			int type = (Integer)__args[0];
			SpringArrayObjectByte buf = (SpringArrayObjectByte)__args[1];
			int off = (Integer)__args[2];
			int len = (Integer)__args[3];
			
			// Read from our image
			try (InputStream in = new ByteArrayInputStream(
				buf.array(), off, len))
			{
				// Load the image using our more native code, this will
				// forward into our native handler which means that we will
				// essentially get hyper accelerated images potentially
				Image image = ImageReaderDispatcher.parse(in);
				
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
			}
			catch (IndexOutOfBoundsException|IOException|
				NullPointerException e)
			{
				throw new SpringMLECallError(e);
			}
		}
	}, 
	
	/* End. */
	;
	
	/** The dispatch key. */
	protected final String key;
	
	/**
	 * Initializes the dispatcher info.
	 *
	 * @param __key The key.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/09/26
	 */
	MLEPencil(String __key)
		throws NullPointerException
	{
		if (__key == null)
			throw new NullPointerException("NARG");
		
		this.key = __key;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/26
	 */
	@Override
	public String key()
	{
		return this.key;
	}
}
