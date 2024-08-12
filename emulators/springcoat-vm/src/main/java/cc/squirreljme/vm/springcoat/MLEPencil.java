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
import cc.squirreljme.jvm.mle.brackets.PencilFontBracket;
import cc.squirreljme.jvm.mle.constants.NativeImageLoadType;
import cc.squirreljme.jvm.mle.constants.UIPixelFormat;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.vm.springcoat.callbacks.NativeImageLoadCallbackAdapter;
import cc.squirreljme.vm.springcoat.exceptions.SpringMLECallError;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.lcdui.AnimatedImage;
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
				PencilShelf.hardwareCopyArea(
					SpringVisObject.asNative(__thread,
						PencilBracket.class, __args[0]),
					SpringVisObject.asNative(__thread,
						Integer.class, __args[1]),
					SpringVisObject.asNative(__thread,
						Integer.class, __args[2]),
					SpringVisObject.asNative(__thread,
						Integer.class, __args[3]),
					SpringVisObject.asNative(__thread,
						Integer.class, __args[4]),
					SpringVisObject.asNative(__thread,
						Integer.class, __args[5]),
					SpringVisObject.asNative(__thread,
						Integer.class, __args[6]),
					SpringVisObject.asNative(__thread,
						Integer.class, __args[7]));
				return null;
			}
			catch (MLECallError __e)
			{
				return new SpringMLECallError(__e, __e.distinction);
			}
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
				PencilShelf.hardwareDrawChars(
					SpringVisObject.asNative(__thread,
						PencilBracket.class, __args[0]),
					SpringVisObject.asNative(__thread,
						char[].class, __args[1]),
					SpringVisObject.asNative(__thread,
						Integer.class, __args[2]),
					SpringVisObject.asNative(__thread,
						Integer.class, __args[3]),
					SpringVisObject.asNative(__thread,
						Integer.class, __args[4]),
					SpringVisObject.asNative(__thread,
						Integer.class, __args[5]),
					SpringVisObject.asNative(__thread,
						Integer.class, __args[6]));
				return null;
			}
			catch (MLECallError __e)
			{
				return new SpringMLECallError(__e, __e.distinction);
			}
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
			try
			{
				PencilShelf.hardwareDrawLine(
					SpringVisObject.asNative(__thread,
						PencilBracket.class, __args[0]),
					SpringVisObject.asNative(__thread,
						Integer.class, __args[1]),
					SpringVisObject.asNative(__thread,
						Integer.class, __args[2]),
					SpringVisObject.asNative(__thread,
						Integer.class, __args[3]),
					SpringVisObject.asNative(__thread,
						Integer.class, __args[4]));
				return null;
			}
			catch (MLECallError __e)
			{
				return new SpringMLECallError(__e, __e.distinction);
			}
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
			try
			{
				PencilShelf.hardwareDrawRect(
					SpringVisObject.asNative(__thread,
						PencilBracket.class, __args[0]),
					SpringVisObject.asNative(__thread,
						Integer.class, __args[1]),
					SpringVisObject.asNative(__thread,
						Integer.class, __args[2]),
					SpringVisObject.asNative(__thread,
						Integer.class, __args[3]),
					SpringVisObject.asNative(__thread,
						Integer.class, __args[4]));
				return null;
			}
			catch (MLECallError __e)
			{
				return new SpringMLECallError(__e, __e.distinction);
			}
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
				PencilShelf.hardwareDrawSubstring(
					SpringVisObject.asNative(__thread,
						PencilBracket.class, __args[0]),
					SpringVisObject.asNative(__thread,
						String.class, __args[1]),
					SpringVisObject.asNative(__thread,
						Integer.class, __args[2]),
					SpringVisObject.asNative(__thread,
						Integer.class, __args[3]),
					SpringVisObject.asNative(__thread,
						Integer.class, __args[4]),
					SpringVisObject.asNative(__thread,
						Integer.class, __args[5]),
					SpringVisObject.asNative(__thread,
						Integer.class, __args[6]));
				return null;
			}
			catch (MLECallError __e)
			{
				return new SpringMLECallError(__e, __e.distinction);
			}
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
			try
			{
				PencilShelf.hardwareDrawXRGB32Region(
					SpringVisObject.asNative(__thread,
						PencilBracket.class, __args[0]),
					SpringVisObject.asNative(__thread,
						int[].class, __args[1]),
					SpringVisObject.asNative(__thread,
						Integer.class, __args[2]),
					SpringVisObject.asNative(__thread,
						Integer.class, __args[3]),
					SpringVisObject.asNative(__thread,
						Boolean.class, __args[4]),
					SpringVisObject.asNative(__thread,
						Integer.class, __args[5]),
					SpringVisObject.asNative(__thread,
						Integer.class, __args[6]),
					SpringVisObject.asNative(__thread,
						Integer.class, __args[7]),
					SpringVisObject.asNative(__thread,
						Integer.class, __args[8]),
					SpringVisObject.asNative(__thread,
						Integer.class, __args[9]),
					SpringVisObject.asNative(__thread,
						Integer.class, __args[10]),
					SpringVisObject.asNative(__thread,
						Integer.class, __args[11]),
					SpringVisObject.asNative(__thread,
						Integer.class, __args[12]),
					SpringVisObject.asNative(__thread,
						Integer.class, __args[13]),
					SpringVisObject.asNative(__thread,
						Integer.class, __args[14]),
					SpringVisObject.asNative(__thread,
						Integer.class, __args[15]),
					SpringVisObject.asNative(__thread,
						Integer.class, __args[16]));
				return null;
			}
			catch (MLECallError __e)
			{
				return new SpringMLECallError(__e, __e.distinction);
			}
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
			try
			{
				PencilShelf.hardwareFillRect(
					SpringVisObject.asNative(__thread,
						PencilBracket.class, __args[0]),
					SpringVisObject.asNative(__thread,
						Integer.class, __args[1]),
					SpringVisObject.asNative(__thread,
						Integer.class, __args[2]),
					SpringVisObject.asNative(__thread,
						Integer.class, __args[3]),
					SpringVisObject.asNative(__thread,
						Integer.class, __args[4]));
				return null;
			}
			catch (MLECallError __e)
			{
				return new SpringMLECallError(__e, __e.distinction);
			}
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
			try
			{
				PencilShelf.hardwareFillTriangle(
					SpringVisObject.asNative(__thread,
						PencilBracket.class, __args[0]),
					SpringVisObject.asNative(__thread,
						Integer.class, __args[1]),
					SpringVisObject.asNative(__thread,
						Integer.class, __args[2]),
					SpringVisObject.asNative(__thread,
						Integer.class, __args[3]),
					SpringVisObject.asNative(__thread,
						Integer.class, __args[4]),
					SpringVisObject.asNative(__thread,
						Integer.class, __args[5]),
					SpringVisObject.asNative(__thread,
						Integer.class, __args[6]));
				return null;
			}
			catch (MLECallError __e)
			{
				return new SpringMLECallError(__e, __e.distinction);
			}
		}
	},
	
	/** {@link PencilShelf#hardwareHasAlpha(PencilBracket)}. */
	HARDWARE_HAS_ALPHA("hardwareHasAlpha:" +
		"(Lcc/squirreljme/jvm/mle/brackets/PencilBracket;)Z")
	{
		/**
		 * {@inheritDoc}
		 * @since 2024/08/04
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			try
			{
				return SpringVisObject.asVm(__thread, Boolean.TYPE,
					PencilShelf.hardwareHasAlpha(
					SpringVisObject.asNative(__thread,
						PencilBracket.class, __args[0])));
			}
			catch (MLECallError __e)
			{
				return new SpringMLECallError(__e, __e.distinction);
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
			try
			{
				PencilShelf.hardwareSetAlphaColor(
					SpringVisObject.asNative(__thread,
						PencilBracket.class, __args[0]),
					SpringVisObject.asNative(__thread,
						Integer.class, __args[1]));
				return null;
			}
			catch (MLECallError __e)
			{
				return new SpringMLECallError(__e, __e.distinction);
			}
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
			try
			{
				PencilShelf.hardwareSetBlendingMode(
					SpringVisObject.asNative(__thread,
						PencilBracket.class, __args[0]),
					SpringVisObject.asNative(__thread,
						Integer.class, __args[1]));
				return null;
			}
			catch (MLECallError __e)
			{
				return new SpringMLECallError(__e, __e.distinction);
			}
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
			try
			{
				PencilShelf.hardwareSetClip(
					SpringVisObject.asNative(__thread,
						PencilBracket.class, __args[0]),
					SpringVisObject.asNative(__thread,
						Integer.class, __args[1]),
					SpringVisObject.asNative(__thread,
						Integer.class, __args[2]),
					SpringVisObject.asNative(__thread,
						Integer.class, __args[3]),
					SpringVisObject.asNative(__thread,
						Integer.class, __args[4]));
				return null;
			}
			catch (MLECallError __e)
			{
				return new SpringMLECallError(__e, __e.distinction);
			}
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
			try
			{
				PencilShelf.hardwareSetDefaultFont(
					SpringVisObject.asNative(__thread,
						PencilBracket.class, __args[0]));
				return null;
			}
			catch (MLECallError __e)
			{
				return new SpringMLECallError(__e, __e.distinction);
			}
		}
	},
	
	/**
	 * {@link PencilShelf#hardwareSetFont(PencilBracket, PencilFontBracket)}.
	 */
	HARDWARE_SET_FONT("hardwareSetFont:" +
		"(Lcc/squirreljme/jvm/mle/brackets/PencilBracket;" +
		"Lcc/squirreljme/jvm/mle/brackets/PencilFontBracket;)V")
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
				PencilShelf.hardwareSetFont(
					SpringVisObject.asNative(__thread,
						PencilBracket.class, __args[0]),
					SpringVisObject.asNative(__thread,
						PencilFontBracket.class, __args[1]));
				return null;
			}
			catch (MLECallError __e)
			{
				return new SpringMLECallError(__e, __e.distinction);
			}
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
			try
			{
				PencilShelf.hardwareSetStrokeStyle(
					SpringVisObject.asNative(__thread,
						PencilBracket.class, __args[0]),
					SpringVisObject.asNative(__thread,
						Integer.class, __args[1]));
				return null;
			}
			catch (MLECallError __e)
			{
				return new SpringMLECallError(__e, __e.distinction);
			}
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
			try
			{
				PencilShelf.hardwareTranslate(
					SpringVisObject.asNative(__thread,
						PencilBracket.class, __args[0]),
					SpringVisObject.asNative(__thread,
						Integer.class, __args[1]),
					SpringVisObject.asNative(__thread,
						Integer.class, __args[2]));
				return null;
			}
			catch (MLECallError __e)
			{
				return new SpringMLECallError(__e, __e.distinction);
			}
		}
	}, 
	
	/** {@link PencilShelf#hardwareTranslateXY(PencilBracket, boolean)}. */
	HARDWARE_TRANSLATE_XY("hardwareTranslateXY:" +
		"(Lcc/squirreljme/jvm/mle/brackets/PencilBracket;Z)I")
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/12/05
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			try
			{
				return PencilShelf.hardwareTranslateXY(
					SpringVisObject.asNative(__thread,
						PencilBracket.class, __args[0]),
					(int)__args[1] != 0);
			}
			catch (MLECallError __e)
			{
				return new SpringMLECallError(__e, __e.distinction);
			}
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
