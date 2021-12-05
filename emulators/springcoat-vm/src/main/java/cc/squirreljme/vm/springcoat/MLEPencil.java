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
import cc.squirreljme.jvm.mle.brackets.PencilBracket;
import cc.squirreljme.jvm.mle.constants.NativeImageLoadParameter;
import cc.squirreljme.jvm.mle.constants.NativeImageLoadType;
import cc.squirreljme.jvm.mle.constants.PencilCapabilities;
import cc.squirreljme.jvm.mle.constants.UIPixelFormat;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.lcdui.image.ImageReaderDispatcher;
import cc.squirreljme.runtime.lcdui.mle.SoftwareGraphicsFactory;
import cc.squirreljme.vm.springcoat.brackets.PencilObject;
import cc.squirreljme.vm.springcoat.exceptions.SpringMLECallError;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.lcdui.Graphics;
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
			
			return PencilCapabilities.MINIMUM |
				PencilCapabilities.FILL_RECT |
				PencilCapabilities.DRAW_LINE;
		}
	},
	
	/**
	 * {@link PencilShelf#hardwareDrawLine(PencilBracket, int, int, int, int)}.
	 */
	HARDWARE_DRAW_LINE("hardwareDrawLine:" +
		"(Lcc/squirreljme/jvm/mle/brackets/PencilBracket;IIII)V")
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/12/05
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			MLEPencil.__graphics(__args[0])
				.drawLine((Integer)__args[1],
					(Integer)__args[2],
					(Integer)__args[3],
					(Integer)__args[4]);
			
			return null;
		}
	}, 
	
	/**
	 * {@link PencilShelf#hardwareFillRect(PencilBracket, int, int, int, int)}.
	 */
	HARDWARE_FILL_RECT("hardwareFillRect:" +
		"(Lcc/squirreljme/jvm/mle/brackets/PencilBracket;IIII)V")
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/12/05
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			MLEPencil.__graphics(__args[0])
				.fillRect((Integer)__args[1],
					(Integer)__args[2],
					(Integer)__args[3],
					(Integer)__args[4]);
			
			return null;
		}
	}, 
	
	/**
	 * {@link PencilShelf#hardwareGraphics(int, int, int, Object, int, int[],
	 * int, int, int, int)}.
	 */ 
	HARDWARE_GRAPHICS("hardwareGraphics:(IIILjava/lang/Object;I[IIIII)" +
		"Lcc/squirreljme/jvm/mle/brackets/PencilBracket;")
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/12/05
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			// Attempt to initialize the graphics
			try
			{
				int __pf = (Integer)__args[0];
				int __bw = (Integer)__args[1];
				int __bh = (Integer)__args[2];
				SpringArrayObject __buf =
					SpringNullObject.<SpringArrayObject>checkCast(
						SpringArrayObject.class, __args[3]);
				int __offset = (Integer)__args[4];
				SpringArrayObjectInteger __pal =
					SpringNullObject.<SpringArrayObjectInteger>checkCast(
						SpringArrayObjectInteger.class, __args[5]);
				int __sx = (Integer)__args[6];
				int __sy = (Integer)__args[7];
				int __sw = (Integer)__args[8];
				int __sh = (Integer)__args[9];
				
				return new PencilObject(__thread.machine,
					SoftwareGraphicsFactory.softwareGraphics(
						__pf, __bw, __bh,
						(__buf != null ? __buf.array() : null),
						__offset, (__pal != null ? __pal.array() : null),
						__sx, __sy, __sw, __sh));
			}
			catch (ClassCastException|IllegalArgumentException|
				NullPointerException __e)
			{
				throw new SpringMLECallError(__e);
			}
		}
	},
	
	/** {@link PencilShelf#hardwareSetAlphaColor(PencilBracket, int)}. */
	HARDWARE_SET_ALPHA_COLOR("hardwareSetAlphaColor:" +
		"(Lcc/squirreljme/jvm/mle/brackets/PencilBracket;I)V")
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/12/05
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			MLEPencil.__graphics(__args[0])
				.setAlphaColor((Integer)__args[1]);
			
			return null;
		}
	}, 
	
	/** {@link PencilShelf#hardwareSetBlendingMode(PencilBracket, int)}. */
	HARDWARE_SET_BLENDING_MODE("hardwareSetBlendingMode:" +
		"(Lcc/squirreljme/jvm/mle/brackets/PencilBracket;I)V")
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/12/05
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			MLEPencil.__graphics(__args[0])
				.setBlendingMode((Integer)__args[1]);
			
			return null;
		}
	},
	
	/**
	 * {@link PencilShelf#hardwareSetClip(PencilBracket, int, int, int, int)}.
	 */
	HARDWARE_SET_CLIP("hardwareSetClip:" +
		"(Lcc/squirreljme/jvm/mle/brackets/PencilBracket;IIII)V")
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/12/05
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			MLEPencil.__graphics(__args[0])
				.setClip((Integer)__args[1],
					(Integer)__args[2],
					(Integer)__args[3],
					(Integer)__args[4]);
			
			return null;
		}
	}, 
	
	/** {@link PencilShelf#hardwareSetStrokeStyle(PencilBracket, int)}. */
	HARDWARE_SET_STROKE_STYLE("hardwareSetStrokeStyle:" +
		"(Lcc/squirreljme/jvm/mle/brackets/PencilBracket;I)V")
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/12/05
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			MLEPencil.__graphics(__args[0])
				.setStrokeStyle((Integer)__args[1]);
			
			return null;
		}
	}, 
	
	/** {@link PencilShelf#hardwareTranslate(PencilBracket, int, int)}. */
	HARDWARE_TRANSLATE("hardwareTranslate:" +
		"(Lcc/squirreljme/jvm/mle/brackets/PencilBracket;II)V")
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/12/05
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			MLEPencil.__graphics(__args[0])
				.translate((Integer)__args[1],
					(Integer)__args[2]);
			
			return null;
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
	
	/**
	 * Ensures that this is a {@link PencilObject} and returns the graphics
	 * object for it.
	 * 
	 * @param __object The object to check.
	 * @return As a {@link PencilObject}.
	 * @throws SpringMLECallError If this is not one.
	 * @since 2021/12/05
	 */
	static Graphics __graphics(Object __object)
		throws SpringMLECallError
	{
		if (!(__object instanceof PencilObject))
			throw new SpringMLECallError("Not a PencilObject.");
		
		return ((PencilObject)__object).graphics; 
	}
}
