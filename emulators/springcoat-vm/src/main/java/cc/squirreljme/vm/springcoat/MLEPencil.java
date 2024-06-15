// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.jvm.mle.PencilShelf;
import cc.squirreljme.jvm.mle.brackets.PencilBracket;
import cc.squirreljme.jvm.mle.constants.NativeImageLoadType;
import cc.squirreljme.jvm.mle.constants.PencilCapabilities;
import cc.squirreljme.jvm.mle.constants.PencilShelfError;
import cc.squirreljme.jvm.mle.constants.UIPixelFormat;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.lcdui.mle.SoftwareGraphicsFactory;
import cc.squirreljme.vm.springcoat.brackets.PencilObject;
import cc.squirreljme.vm.springcoat.callbacks.NativeImageLoadCallbackAdapter;
import cc.squirreljme.vm.springcoat.exceptions.SpringMLECallError;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.lcdui.AnimatedImage;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.ScalableImage;

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
				PencilCapabilities.COPY_AREA |
				PencilCapabilities.DRAW_LINE |
				PencilCapabilities.DRAW_RECT |
				PencilCapabilities.DRAW_XRGB32_REGION |
				PencilCapabilities.FILL_RECT |
				PencilCapabilities.FILL_TRIANGLE |
				PencilCapabilities.TEXT_BASIC;
		}
	},
	
	/**
	 * {@link PencilShelf#hardwareCopyArea(PencilBracket, int, int, int, int,
	 * int, int, int)}.
	 */
	HARDWARE_COPY_AREA("hardwareCopyArea:" +
		"(Lcc/squirreljme/jvm/mle/brackets/PencilBracket;IIIIIII)V")
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/02/19
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			try
			{
				MLEObjects.pencilGraphics(__args[0])
					.copyArea((Integer)__args[1],
						(Integer)__args[2],
						(Integer)__args[3],
						(Integer)__args[4],
						(Integer)__args[5],
						(Integer)__args[6],
						(Integer)__args[7]);
			}
			catch (IllegalArgumentException e)
			{
				throw new SpringMLECallError(e,
					PencilShelfError.ILLEGAL_ARGUMENT);
			}
			catch (IllegalStateException e)
			{
				throw new SpringMLECallError(e, 
					PencilShelfError.ILLEGAL_STATE);
			}
			
			return null;
		}
	},
	
	/**
	 * {@link PencilShelf#hardwareDrawChars(PencilBracket, char[], int, int,
	 * int, int, int)}.
	 */
	HARDWARE_DRAW_CHARS("hardwareDrawChars:" +
		"(Lcc/squirreljme/jvm/mle/brackets/PencilBracket;[CIIIII)V")
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/02/19
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			try
			{
				MLEObjects.pencilGraphics(__args[0])
					.drawChars(((SpringArrayObjectChar)__args[1]).array(),
						(Integer)__args[2],
						(Integer)__args[3],
						(Integer)__args[4],
						(Integer)__args[5],
						(Integer)__args[6]);
			}
			catch (IllegalArgumentException e)
			{
				throw new SpringMLECallError(e,
					PencilShelfError.ILLEGAL_ARGUMENT);
			}
			catch (IndexOutOfBoundsException e)
			{
				throw new SpringMLECallError(e,
					PencilShelfError.INDEX_OUT_OF_BOUNDS);
			}
			
			return null;
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
			MLEObjects.pencilGraphics(__args[0])
				.drawLine((Integer)__args[1],
					(Integer)__args[2],
					(Integer)__args[3],
					(Integer)__args[4]);
			
			return null;
		}
	},
	
	/**
	 * {@link PencilShelf#hardwareDrawRect(PencilBracket, int, int, int, int)}.
	 */
	HARDWARE_DRAW_RECT("hardwareDrawRect:" +
		"(Lcc/squirreljme/jvm/mle/brackets/PencilBracket;IIII)V")
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/12/05
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			MLEObjects.pencilGraphics(__args[0])
				.drawRect((Integer)__args[1],
					(Integer)__args[2],
					(Integer)__args[3],
					(Integer)__args[4]);
			
			return null;
		}
	},
	
	/**
	 * {@link PencilShelf#hardwareDrawSubstring(PencilBracket, String, int,
	 * int, int, int, int)}.
	 */
	HARDWARE_DRAW_SUBSTRING("hardwareDrawSubstring:" +
		"(Lcc/squirreljme/jvm/mle/brackets/PencilBracket;" +
		"Ljava/lang/String;IIIII)V")
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/02/19
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			try
			{
				MLEObjects.pencilGraphics(__args[0])
					.drawSubstring(
						__thread.asNativeObject(String.class, __args[1]),
						(Integer)__args[2],
						(Integer)__args[3],
						(Integer)__args[4],
						(Integer)__args[5],
						(Integer)__args[6]);
			}
			catch (IllegalArgumentException e)
			{
				throw new SpringMLECallError(e,
					PencilShelfError.ILLEGAL_ARGUMENT);
			}
			catch (StringIndexOutOfBoundsException e)
			{
				throw new SpringMLECallError(e,
					PencilShelfError.INDEX_OUT_OF_BOUNDS);
			}
			
			return null;
		}
	},
	
	/**
	 * {@link PencilShelf#hardwareDrawXRGB32Region(PencilBracket, int[], int,
	 * int, boolean, int, int, int, int, int, int, int, int, int, int, int,
	 * int)}.
	 */
	HARDWARE_DRAW_XRGB32_REGION("hardwareDrawXRGB32Region:" +
		"(Lcc/squirreljme/jvm/mle/brackets/PencilBracket;[IIIZIIIIIIIIIIII)V")
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/01/26
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			// Try to get our buffer
			SpringArrayObjectInteger objBuf;
			
			try
			{
				objBuf = SpringNullObject.<SpringArrayObjectInteger>checkCast(
						SpringArrayObjectInteger.class, __args[1]);
				if (objBuf == null)
					throw new MLECallError("Null arguments.");
			}
			catch (ClassCastException e)
			{
				throw new MLECallError("Wrong buffer type.", e);
			}
			
			// Extract all the arguments
			int[] __buf = objBuf.array();
			int __off = (Integer)__args[2];
			int __scanLen = (Integer)__args[3];
			boolean __alpha = ((Integer)__args[4] != 0);
			int __xSrc = (Integer)__args[5];
			int __ySrc = (Integer)__args[6];
			int __wSrc = (Integer)__args[7];
			int __hSrc = (Integer)__args[8];
			int __trans = (Integer)__args[9];
			int __xDest = (Integer)__args[10];
			int __yDest = (Integer)__args[11];
			int __anch = (Integer)__args[12];
			int __wDest = (Integer)__args[13];
			int __hDest = (Integer)__args[14];
			int __origImgWidth = (Integer)__args[15];
			int __origImgHeight = (Integer)__args[16];
			
			// If the offset and/or the scan length is off, we need to correct
			// and move this over for the plain region call
			boolean booped = false;
			if (__off != 0 || __scanLen != __origImgWidth)
			{
				// Setup new buffer
				int maxSize = __origImgWidth * __origImgHeight;
				int[] newBuf = new int[maxSize];
				
				// Copy each scanline off
				int xSrc = __off;
				int xDst = 0;
				for (int y = 0; y < __origImgHeight; y++)
				{
					// Copy row
					System.arraycopy(__buf, xSrc,
						newBuf, xDst, __origImgWidth);
					
					// Go to the next scan
					xSrc += __scanLen;
					xDst += __origImgWidth;
				}
				
				// We un-quirked the image, so reset these
				__buf = newBuf;
				__off = 0;
				__scanLen = __origImgWidth;
				booped = true;
			}
			
			// Load in source image
			Image wrapped = Image.createRGBImage(__buf,
				__origImgWidth, __origImgHeight, __alpha);
			
			// Forward to normal call and have our own graphics code handle
			// this one
			MLEObjects.pencilGraphics(__args[0])
				.drawRegion(wrapped, __xSrc, __ySrc, __wSrc, __hSrc,
					__trans, __xDest, __yDest, __anch, __wDest, __hDest);
			
			// No result from this one
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
			MLEObjects.pencilGraphics(__args[0])
				.fillRect((Integer)__args[1],
					(Integer)__args[2],
					(Integer)__args[3],
					(Integer)__args[4]);
			
			return null;
		}
	},
	
	/**
	 * {@link PencilShelf#hardwareFillTriangle(PencilBracket, int, int, int,
	 * int, int, int)}.
	 */
	HARDWARE_FILL_TRIANGLE("hardwareFillTriangle:" +
		"(Lcc/squirreljme/jvm/mle/brackets/PencilBracket;IIIIII)V")
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/02/16
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			MLEObjects.pencilGraphics(__args[0])
				.fillTriangle((Integer)__args[1],
					(Integer)__args[2],
					(Integer)__args[3],
					(Integer)__args[4],
					(Integer)__args[5],
					(Integer)__args[6]);
			
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
			MLEObjects.pencilGraphics(__args[0])
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
			MLEObjects.pencilGraphics(__args[0])
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
			MLEObjects.pencilGraphics(__args[0])
				.setClip((Integer)__args[1],
					(Integer)__args[2],
					(Integer)__args[3],
					(Integer)__args[4]);
			
			return null;
		}
	},
	
	/** {@link PencilShelf#hardwareSetDefaultFont(PencilBracket)}. */
	HARDWARE_SET_DEFAULT_FONT("hardwareSetDefaultFont:" +
		"(Lcc/squirreljme/jvm/mle/brackets/PencilBracket;)V")
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/02/19
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			MLEObjects.pencilGraphics(__args[0])
				.setFont(null);
			
			return null;
		}
	},
	
	/**
	 * {@link PencilShelf#hardwareSetFont(PencilBracket, String, int, int)}.
	 */
	HARDWARE_SET_FONT("hardwareSetFont:" +
		"(Lcc/squirreljme/jvm/mle/brackets/PencilBracket;" +
		"Ljava/lang/String;II)V")
		{
			/**
			 * {@inheritDoc}
			 * @since 2023/02/19
			 */
			@Override
			public Object handle(SpringThreadWorker __thread, Object... __args)
			{
				MLEObjects.pencilGraphics(__args[0])
					.setFont(Font.getFont(
						__thread.asNativeObject(String.class, __args[1]),
						(Integer)__args[2],
						(Integer)__args[3]));
				
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
			MLEObjects.pencilGraphics(__args[0])
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
			MLEObjects.pencilGraphics(__args[0])
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
	
	/**
	 * {@link PencilShelf#nativeImageLoadRGBA(int, byte[], int, int,
	 * cc.squirreljme.jvm.mle.callbacks.NativeImageLoadCallback)}.
	 */
	NATIVE_IMAGE_LOAD_RGBA("nativeImageLoadRGBA:(I[BII" +
		"Lcc/squirreljme/jvm/mle/callbacks/NativeImageLoadCallback;)" +
		"Ljava/lang/Object;")
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
			SpringObject callbackRaw = (SpringObject)__args[4];
			
			if (buf == null || callbackRaw == null)
				throw new SpringMLECallError("No buf or callback.");
			if (off < 0 || len < 0 || (off + len) > buf.length())
				throw new SpringMLECallError("Out of bounds data.");
			
			// Callback for image loading
			NativeImageLoadCallbackAdapter callback =
				new NativeImageLoadCallbackAdapter(__thread.machine,
					callbackRaw);
			
			// Does the native MLE handler support this?
			if ((PencilShelf.nativeImageLoadTypes() & type) != 0)
			{
				// Forward to MLE handler
				Object result = PencilShelf.nativeImageLoadRGBA(type,
					buf.array(), off, len, callback); 
					
				// Was this cancelled?
				if (result == null || result == SpringNullObject.NULL)
					return SpringNullObject.NULL;
			}
			
			// Read from our image
			try (InputStream in = new ByteArrayInputStream(
				buf.array(), off, len))
			{
				// Load the image using our more native code, this will
				// forward into our native handler which means that we will
				// essentially get hyper accelerated images potentially
				Image image = Image.createImage(in);
				
				// Send initial image parameters
				callback.initialize(image.getWidth(),
					image.getHeight(),
					image.isAnimated(),
					image.isScalable());
				
				// Scalable image
				if (image instanceof ScalableImage)
					throw Debugging.todo();
				
				// Processing depends on the type of image this is
				else if (image instanceof AnimatedImage)
				{
					// Set up the loop count of the image
					AnimatedImage animated = (AnimatedImage)image;
					callback.setLoopCount(animated.getLoopCount());
					
					for (int i = 0; i >= 0; i++)
						try
						{
							MLEObjects.addImage(callback,
								animated.getFrame(i),
								animated.getFrameDelay(i));
						}
						catch (IndexOutOfBoundsException ignored)
						{
							break;
						}
				}
				
				// Still image
				else
					MLEObjects.addImage(callback, image, -1);
				
				// Check if it was cancelled
				Object finished = callback.finish();
				if (finished == null || finished == SpringNullObject.NULL)
					return SpringNullObject.NULL;
				
				// Should finish as a SpringObject
				if (!(finished instanceof SpringObject))
					throw new SpringMLECallError("Not an object?");
				return finished;
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
