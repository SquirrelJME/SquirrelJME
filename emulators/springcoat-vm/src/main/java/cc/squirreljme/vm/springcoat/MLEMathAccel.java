// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.jvm.mle.MathAccelShelf;
import cc.squirreljme.jvm.mle.MathShelf;

/**
 * Functions for {@link MLEMathAccel}.
 *
 * @since 2025/05/03
 */
public enum MLEMathAccel
	implements MLEFunction
{
	/** {@link MathAccelShelf#accel()}. */
	ACCEL("accel:()I")
	{
		/**
		 * {@inheritDoc}
		 * @since 2025/05/03
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return MathAccelShelf.accel();
		}
	},
	
	/** {@link MathAccelShelf#acos(double)}. */
	ACOS("acos:(D)D")
	{
		/**
		 * {@inheritDoc}
		 * @since 2025/05/03
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return MathAccelShelf.acos((double)__args[0]);
		}
	},
	
	/** {@link MathAccelShelf#asin(double)}. */
	ASIN("asin:(D)D")
	{
		/**
		 * {@inheritDoc}
		 * @since 2025/05/03
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return MathAccelShelf.asin((double)__args[0]);
		}
	},
	
	/** {@link MathAccelShelf#atan(double)}. */
	ATAN("atan:(D)D")
	{
		/**
		 * {@inheritDoc}
		 * @since 2025/05/03
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return MathAccelShelf.atan((double)__args[0]);
		}
	},
	
	/** {@link MathAccelShelf#atan2(double, double)}. */
	ATAN_2("atan2:(DD)D")
	{
		/**
		 * {@inheritDoc}
		 * @since 2025/05/03
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return MathAccelShelf.atan2((double)__args[0],
				(double)__args[1]);
		}
	},
	
	/** {@link MathAccelShelf#ceil(double)}. */
	CEIL("ceil:(D)D")
	{
		/**
		 * {@inheritDoc}
		 * @since 2025/05/03
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return MathAccelShelf.ceil((double)__args[0]);
		}
	},
	
	/** {@link MathAccelShelf#cos(double)}. */
	COS("cos:(D)D")
	{
		/**
		 * {@inheritDoc}
		 * @since 2025/05/03
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return MathAccelShelf.cos((double)__args[0]);
		}
	},
	
	/** {@link MathAccelShelf#exp(double)}. */
	EXP("exp:(D)D")
	{
		/**
		 * {@inheritDoc}
		 * @since 2025/05/03
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return MathAccelShelf.exp((double)__args[0]);
		}
	},
	
	/** {@link MathAccelShelf#floor(double)}. */
	FLOOR("floor:(D)D")
	{
		/**
		 * {@inheritDoc}
		 * @since 2025/05/03
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return MathAccelShelf.floor((double)__args[0]);
		}
	},
	
	/** {@link MathAccelShelf#log(double)}. */
	LOG("log:(D)D")
	{
		/**
		 * {@inheritDoc}
		 * @since 2025/05/03
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return MathAccelShelf.log((double)__args[0]);
		}
	},
	
	/** {@link MathAccelShelf#pow(double, double)}. */
	POW("pow:(DD)D")
	{
		/**
		 * {@inheritDoc}
		 * @since 2025/05/03
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return MathAccelShelf.pow((double)__args[0],
				(double)__args[1]);
		}
	},
	
	/** {@link MathAccelShelf#round(double)}. */
	ROUND("round:(D)J")
	{
		/**
		 * {@inheritDoc}
		 * @since 2025/05/03
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return MathAccelShelf.round((double)__args[0]);
		}
	},
	
	/** {@link MathAccelShelf#signum(double)}. */
	SIGNUM("signum:(D)D")
	{
		/**
		 * {@inheritDoc}
		 * @since 2025/05/03
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return MathAccelShelf.signum((double)__args[0]);
		}
	},
	
	/** {@link MathAccelShelf#sin(double)}. */
	SIN("sin:(D)D")
	{
		/**
		 * {@inheritDoc}
		 * @since 2025/05/03
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return MathAccelShelf.sin((double)__args[0]);
		}
	},
	
	/** {@link MathAccelShelf#sqrt(double)}. */
	SQRT("sqrt:(D)D")
	{
		/**
		 * {@inheritDoc}
		 * @since 2025/05/03
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return MathAccelShelf.sqrt((double)__args[0]);
		}
	},
	
	/** {@link MathAccelShelf#tan(double)}. */
	TAN("tan:(D)D")
	{
		/**
		 * {@inheritDoc}
		 * @since 2025/05/03
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return MathAccelShelf.tan((double)__args[0]);
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
	MLEMathAccel(String __key)
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
