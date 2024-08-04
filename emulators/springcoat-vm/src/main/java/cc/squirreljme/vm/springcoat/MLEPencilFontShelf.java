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
	/** {@link PencilFontShelf#equals}. */
	EQUALS("equals")
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
	METRIC_CHAR_VALID("metricCharValid")
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
	
	/** {@link PencilFontShelf#metricFontFace}. */
	METRIC_FONT_FACE("metricFontFace:(Lcc/squirreljme/jvm/mle/" +
		"brackets/PencilFontBracket;)I")
	{
		/**
		 * {@inheritDoc}
		 * @since 2024/08/04
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return PencilFontShelf.metricFontName(
				SpringVisObject.asNative(__thread,
					PencilFontBracket.class, __args[0]));
		}
	},
	
	/** {@link PencilFontShelf#metricFontName}. */
	METRIC_FONT_NAME("metricFontName:(Lcc/squirreljme/jvm/mle/" +
		"brackets/PencilFontBracket;)Ljava/lang/String;")
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
	METRIC_FONT_STYLE("metricFontStyle")
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
	
	/** {@link PencilFontShelf#metricPixelAscent}. */
	METRIC_PIXEL_ASCENT("metricPixelAscent")
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
	
	/** {@link PencilFontShelf#metricPixelBaseline}. */
	METRIC_PIXEL_BASELINE("metricPixelBaseline")
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
	
	/** {@link PencilFontShelf#metricPixelDescent}. */
	METRIC_PIXEL_DESCENT("metricPixelDescent")
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
	
	/** {@link PencilFontShelf#metricPixelLeading}. */
	METRIC_PIXEL_LEADING("metricPixelLeading")
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
	
	/** {@link PencilFontShelf#metricPixelSize}. */
	METRIC_PIXEL_SIZE("metricPixelSize")
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
	
	/** {@link PencilFontShelf#pixelCharWidth}. */
	PIXEL_CHAR_WIDTH("pixelCharWidth")
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
