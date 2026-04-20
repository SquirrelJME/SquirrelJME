// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.jvm.mle.PencilFontShelf;
import cc.squirreljme.jvm.mle.brackets.PencilFontBracket;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * SpringCoat wrapper for {@link PencilFontShelf}.
 *
 * @since 2024/08/04
 */
public enum MLEPencilFontShelf
	implements MLEFunction
{
	/** 
	 * {@link PencilFontShelf#equals(PencilFontBracket, int[],
	 * PencilFontBracket, int[])}. 
	 */
	EQUALS(MLEDispatcher.methodKey("equals", "Z",
		PencilFontBracket.class, int[].class,
		PencilFontBracket.class, int[].class))
	{
		/**
		 * {@inheritDoc}
		 * @since 2024/08/04
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			throw Debugging.todo();
		}
	},
	
	/** {@link PencilFontShelf#metricCharDirection}. */
	METRIC_CHAR_DIRECTION("metricCharDirection")
	{
		/**
		 * {@inheritDoc}
		 * @since 2024/08/04
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			throw Debugging.todo();
		}
	},
	
	/** {@link PencilFontShelf#metricCharValid}. */
	METRIC_CHAR_VALID(MLEDispatcher.methodKey("metricCharValid",
		boolean.class, PencilFontBracket.class, int.class))
	{
		/**
		 * {@inheritDoc}
		 * @since 2024/08/04
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return PencilFontShelf.metricCharValid(
				SpringVisObject.asNative(__thread,
					PencilFontBracket.class, __args[0]),
				SpringVisObject.asNative(__thread,
					Integer.class, __args[1]));
		}
	},
	
	/** {@link PencilFontShelf#metricFontFace}. */
	METRIC_FONT_FACE(MLEDispatcher.methodKey("metricFontFace",
		int.class, PencilFontBracket.class))
	{
		/**
		 * {@inheritDoc}
		 * @since 2024/08/04
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return PencilFontShelf.metricFontFace(
				SpringVisObject.asNative(__thread,
					PencilFontBracket.class, __args[0]));
		}
	},
	
	/** {@link PencilFontShelf#metricFontName}. */
	METRIC_FONT_NAME(MLEDispatcher.methodKey("metricFontName",
		String.class, PencilFontBracket.class))
	{
		/**
		 * {@inheritDoc}
		 * @since 2024/08/04
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return SpringVisObject.asVm(__thread, null,
				PencilFontShelf.metricFontName(
					SpringVisObject.asNative(__thread,
						PencilFontBracket.class, __args[0])));
		}
	},
	
	/** {@link PencilFontShelf#metricFontStyle}. */
	METRIC_FONT_STYLE(MLEDispatcher.methodKey("metricFontStyle",
		int.class, PencilFontBracket.class))
	{
		/**
		 * {@inheritDoc}
		 * @since 2024/08/04
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return PencilFontShelf.metricFontStyle(
				SpringVisObject.asNative(__thread,
					PencilFontBracket.class, __args[0]));
		}
	},
	
	/** {@link PencilFontShelf#metricPixelAscent}. */
	METRIC_PIXEL_ASCENT(MLEDispatcher.methodKey("metricPixelAscent",
		int.class, PencilFontBracket.class, int[].class, boolean.class))
	{
		/**
		 * {@inheritDoc}
		 * @since 2024/08/04
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return PencilFontShelf.metricPixelAscent(
				SpringVisObject.asNative(__thread,
					PencilFontBracket.class, __args[0]),
				SpringVisObject.asNative(__thread,
					int[].class, __args[1]),
				SpringVisObject.asNative(__thread,
					Boolean.class, __args[2]));
		}
	},
	
	/** {@link PencilFontShelf#metricPixelBaseline}. */
	METRIC_PIXEL_BASELINE(MLEDispatcher.methodKey("metricPixelBaseline",
		int.class, PencilFontBracket.class, int[].class))
	{
		/**
		 * {@inheritDoc}
		 * @since 2024/08/04
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return PencilFontShelf.metricPixelBaseline(
				SpringVisObject.asNative(__thread,
					PencilFontBracket.class, __args[0]),
				SpringVisObject.asNative(__thread,
					int[].class, __args[1]));
		}
	},
	
	/** {@link PencilFontShelf#metricPixelDescent}. */
	METRIC_PIXEL_DESCENT(MLEDispatcher.methodKey("metricPixelDescent",
		int.class, PencilFontBracket.class, int[].class, boolean.class))
	{
		/**
		 * {@inheritDoc}
		 * @since 2024/08/04
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return PencilFontShelf.metricPixelDescent(
				SpringVisObject.asNative(__thread,
					PencilFontBracket.class, __args[0]),
				SpringVisObject.asNative(__thread,
					int[].class, __args[1]),
				SpringVisObject.asNative(__thread,
					Boolean.class, __args[2]));
		}
	},
	
	/** {@link PencilFontShelf#metricPixelLeading}. */
	METRIC_PIXEL_LEADING(MLEDispatcher.methodKey("metricPixelLeading",
		int.class, PencilFontBracket.class, int[].class))
	{
		/**
		 * {@inheritDoc}
		 * @since 2024/08/04
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return PencilFontShelf.metricPixelLeading(
				SpringVisObject.asNative(__thread,
					PencilFontBracket.class, __args[0]),
				SpringVisObject.asNative(__thread,
					int[].class, __args[1]));
		}
	},
	
	/** {@link PencilFontShelf#metricPixelSize}. */
	METRIC_PIXEL_SIZE(MLEDispatcher.methodKey("metricPixelSize",
		int.class, PencilFontBracket.class, int[].class, int.class))
	{
		/**
		 * {@inheritDoc}
		 * @since 2024/08/04
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return PencilFontShelf.metricPixelSize(
				SpringVisObject.asNative(__thread,
					PencilFontBracket.class, __args[0]),
				SpringVisObject.asNative(__thread,
					int[].class, __args[1]),
				(Integer)__args[2]);
		}
	},
	
	/** {@link PencilFontShelf#pixelCharWidth}. */
	PIXEL_CHAR_WIDTH(MLEDispatcher.methodKey("pixelCharWidth",
		int.class, PencilFontBracket.class, int[].class, int.class))
	{
		/**
		 * {@inheritDoc}
		 * @since 2024/08/04
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return PencilFontShelf.pixelCharWidth(
				SpringVisObject.asNative(__thread,
					PencilFontBracket.class, __args[0]),
				SpringVisObject.asNative(__thread,
					int[].class, __args[1]),
				SpringVisObject.asNative(__thread,
					Integer.class, __args[2]));
		}
	},
	
	/** {@link PencilFontShelf#renderBitmap}. */
	RENDER_BITMAP("renderBitmap")
	{
		/**
		 * {@inheritDoc}
		 * @since 2024/08/04
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			throw Debugging.todo();
		}
	},
	
	/** {@link PencilFontShelf#renderChar}. */
	RENDER_CHAR("renderChar")
	{
		/**
		 * {@inheritDoc}
		 * @since 2024/08/04
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			throw Debugging.todo();
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
	 * @since 2024/08/04
	 */
	MLEPencilFontShelf(String __key)
		throws NullPointerException
	{
		if (__key == null)
			throw new NullPointerException("NARG");
		
		this.key = __key;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/04
	 */
	@Override
	public String key()
	{
		return this.key;
	}
}
