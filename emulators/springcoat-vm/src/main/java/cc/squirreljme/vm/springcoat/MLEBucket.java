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

/**
 * Wrapper for {@link BucketShelf}.
 *
 * @since 2025/04/25
 */
public enum MLEBucket
	implements MLEFunction
{
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
			return __thread.machine.bucket((int)__args[0]);
		}
	},
	
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
			return (BucketShelf.delete(
				MLEObjects.bucket(__args[0]),
				MLEObjects.string(__args[1])) ? 1 : 0);
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
			return (BucketShelf.exists(
				MLEObjects.bucket(__args[0]),
				MLEObjects.string(__args[1])) ? 1 : 0);
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
			return BucketShelf.lastModifiedTime(
				MLEObjects.bucket(__args[0]),
				MLEObjects.string(__args[1]));
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
			return BucketShelf.length(
				MLEObjects.bucket(__args[0]),
				MLEObjects.string(__args[1]));
		}
	},
	
	/** {@link BucketShelf#list(BucketBracket)}. */
	LIST("list:(Lcc/squirreljme/jvm/mle/brackets/BucketBracket;)" +
		"[Ljava/lang/String;")
	{
		/**
		 * {@inheritDoc}
		 * @since 2025/04/25
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return __thread.asVMObject(BucketShelf.list(
				MLEObjects.bucket(__args[0])));
		}
	},
	
	/**
	 * {@link BucketShelf#list(BucketBracket, boolean, String, String,
	 * String)}.
	 */
	LIST_FILTERED("list:(Lcc/squirreljme/jvm/mle/brackets/BucketBracket;" +
		"ZLjava/lang/String;Ljava/lang/String;Ljava/lang/String;)" +
		"[Ljava/lang/String;")
	{
		/**
		 * {@inheritDoc}
		 * @since 2025/04/25
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return __thread.asVMObject(BucketShelf.list(
				MLEObjects.bucket(__args[0]),
				(int)__args[1] != 0,
				MLEObjects.string(__args[2]),
				MLEObjects.string(__args[3]),
				MLEObjects.string(__args[4])));
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
			return BucketShelf.read(
				MLEObjects.bucket(__args[0]),
				MLEObjects.string(__args[1]),
				(int)__args[2],
				MLEObjects.byteArray(__args[3]),
				(int)__args[4],
				(int)__args[5]);
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
			BucketShelf.write(
				MLEObjects.bucket(__args[0]),
				MLEObjects.string(__args[1]),
				(int)__args[2],
				MLEObjects.byteArray(__args[3]),
				(int)__args[4],
				(int)__args[5],
				(int)__args[6]);
			return null;
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
