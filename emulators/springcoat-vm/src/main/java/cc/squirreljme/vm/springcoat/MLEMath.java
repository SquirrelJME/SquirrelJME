// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.jvm.mle.MathShelf;

/**
 * Functions for {@link MathShelf}.
 *
 * @since 2020/06/18
 */
public enum MLEMath
	implements MLEFunction
{
	/** {@link MathShelf#longPack(int, int)}. */
	LONG_PACK("longPack:(II)J")
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/05/30
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return MathShelf.longPack((int)__args[0], (int)__args[1]);
		}
	},
	
	/** {@link MathShelf#longUnpackHigh(long)}. */
	LONG_UNPACK_HIGH("longUnpackHigh:(J)I")
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/05/30
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return MathShelf.longUnpackHigh((long)__args[0]);
		}
	},
	
	/** {@link MathShelf#longUnpackLow(long)}. */
	LONG_UNPACK_LOW("longUnpackLow:(J)I")
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/05/30
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return MathShelf.longUnpackLow((long)__args[0]);
		}
	},
	
	/** {@link MathShelf#rawDoubleToLong(double)}. */
	RAW_DOUBLE_TO_LONG("rawDoubleToLong:(D)J")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/18
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return Double.doubleToRawLongBits((double)__args[0]);
		}
	},
	
	/** {@link MathShelf#rawFloatToInt(float)}. */
	RAW_FLOAT_TO_INT("rawFloatToInt:(F)I")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/18
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return Float.floatToRawIntBits((float)__args[0]);
		}
	},
	
	/** {@link MathShelf#rawIntToFloat(int)}. */
	RAW_INT_TO_FLOAT("rawIntToFloat:(I)F")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/18
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return Float.intBitsToFloat((int)__args[0]);
		}
	},
	
	/** {@link MathShelf#rawLongToDouble(long)}. */
	RAW_LONG_TO_DOUBLE("rawLongToDouble:(J)D")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/18
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return Double.longBitsToDouble((long)__args[0]);
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
	 * @since 2020/06/18
	 */
	MLEMath(String __key)
		throws NullPointerException
	{
		if (__key == null)
			throw new NullPointerException("NARG");
		
		this.key = __key;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/06/18
	 */
	@Override
	public String key()
	{
		return this.key;
	}
}
