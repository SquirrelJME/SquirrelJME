// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.jvm.mle.BucketShelf;
import cc.squirreljme.jvm.mle.brackets.BucketBracket;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Wrapper for buckets.
 *
 * @since 2025/04/25
 */
public enum MLEBucket
	implements MLEFunction
{
	/** {@link BucketShelf#delete(BucketBracket, String)}. */
	DELETE("delete:(Lcc/squirreljme/jvm/mle/brackets/BucketBracket;" +
		"Ljava/lang/String;)Z")
	{
		/**
		 * {@inheritDoc}
		 * @since 2025/04/25
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			throw Debugging.todo();
		}
	},
	
	/** {@link BucketShelf#lastModifiedTime(BucketBracket, String)}. */
	LAST_MODIFIED_TIME("lastModifiedTime:(Lcc/squirreljme/jvm/mle/" +
		"brackets/BucketBracket;Ljava/lang/String;)J")
	{
		/**
		 * {@inheritDoc}
		 * @since 2025/04/25
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			throw Debugging.todo();
		}
	},
	
	/** {@link BucketShelf#exists(BucketBracket, String)}. */
	EXISTS("exists:(Lcc/squirreljme/jvm/mle/brackets/BucketBracket;" +
		"Ljava/lang/String;)Z")
	{
		/**
		 * {@inheritDoc}
		 * @since 2025/04/25
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			throw Debugging.todo();
		}
	},
	
	/** {@link BucketShelf#bucket(int)}. */
	BUCKET("bucket:(I)Lcc/squirreljme/jvm/mle/brackets/BucketBracket;")
	{
		/**
		 * {@inheritDoc}
		 * @since 2025/04/25
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			throw Debugging.todo();
		}
	},
	
	/** {@link BucketShelf#list(BucketBracket)}. */
	LIST("list:(Lcc/squirreljme/jvm/mle/brackets/BucketBracket;)" +
		"LLjava/lang/String;")
	{
		/**
		 * {@inheritDoc}
		 * @since 2025/04/25
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			throw Debugging.todo();
		}
	},
	
	/**
	 * {@link BucketShelf#list(BucketBracket, boolean, String, String,
	 * String)}.
	 */
	LIST_FILTERED("list:(Lcc/squirreljme/jvm/mle/brackets/BucketBracket;" +
		"ZLjava/lang/String;Ljava/lang/String;Ljava/lang/String;)" +
		"LLjava/lang/String;")
	{
		/**
		 * {@inheritDoc}
		 * @since 2025/04/25
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			throw Debugging.todo();
		}
	},
	
	/** {@link BucketShelf#length(BucketBracket, String)}. */
	LENGTH("length:(Lcc/squirreljme/jvm/mle/brackets/BucketBracket;" +
		"Ljava/lang/String;)J")
	{
		/**
		 * {@inheritDoc}
		 * @since 2025/04/25
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			throw Debugging.todo();
		}
	},
	
	/**
	 * {@link BucketShelf#read(BucketBracket, String, int, byte[], int,
	 * int).
	 */
	READ("read:(Lcc/squirreljme/jvm/mle/brackets/BucketBracket;" +
		"Ljava/lang/String;I[BII)I")
	{
		/**
		 * {@inheritDoc}
		 * @since 2025/04/25
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			throw Debugging.todo();
		}
	},
	
	/**
	 * {@link BucketShelf#write(BucketBracket, String, int, byte[], int,
	 * int, int)}.
	 */
	WRITE("write:(Lcc/squirreljme/jvm/mle/brackets/BucketBracket;" +
		"Ljava/lang/String;I[BIII)V")
	{
		/**
		 * {@inheritDoc}
		 * @since 2025/04/25
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
	 * @since 2020/06/18
	 */
	MLEBucket(String __key)
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
