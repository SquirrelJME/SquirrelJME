// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.jvm.mle.ReferenceShelf;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.vm.springcoat.exceptions.SpringMLECallError;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;

/**
 * Functions for {@link ReferenceShelf}.
 *
 * @since 2020/06/18
 */
public enum MLEReference
	implements MLEFunction
{
	/** {@link ReferenceShelf#weakGet(Reference)}. */
	WEAK_GET(MLEDispatcher.methodKey("weakGet", Object.class,
		Reference.class))
	{
		/**
		 * {@inheritDoc}
		 * @since 2025/06/21
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			SpringWeakObject weak = MLEObjects.notNull(
				SpringWeakObject.class, __args[0]);
			
			return weak.get();
		}
	},
	
	/** {@link ReferenceShelf#weakInit(Reference, Object, ReferenceQueue)}. */
	WEAK_INIT(MLEDispatcher.methodKey("weakInit", "V",
		Reference.class, Object.class, ReferenceQueue.class))
	{
		/**
		 * {@inheritDoc}
		 * @since 2025/06/21
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			SpringWeakObject weak = MLEObjects.notNull(
				SpringWeakObject.class, __args[0]);
			SpringObject value = MLEObjects.notNull(__args[1]);
			
			SpringObject queue = MLEObjects.simpleOptional(__args[2]);
			if (queue != null && !queue.type().isAssignableFrom(
				__thread.loadClass("java/lang/ref/ReferenceQueue")))
				throw new SpringMLECallError("Wrong type.");
			
			// Initialize the weak reference
			weak.init(value, queue);
			return null;
		}
	},
	
	/** {@link ReferenceShelf#weakIsEnqueued(Reference)}. */
	WEAK_IS_ENQUEUED(MLEDispatcher.methodKey("weakIsEnqueued", "Z",
		Reference.class))
	{
		/**
		 * {@inheritDoc}
		 * @since 2025/06/21
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			SpringWeakObject weak = MLEObjects.notNull(
				SpringWeakObject.class, __args[0]);
			
			return weak.isEnqueued() ? 1 : 0;
		}
	},
	
	/** {@link ReferenceShelf#weakUnlinkAndClear(Reference)}. */
	WEAK_UNLINK_AND_CLEAR(MLEDispatcher.methodKey("weakUnlinkAndClear",
		ReferenceQueue.class, Reference.class))
	{
		/**
		 * {@inheritDoc}
		 * @since 2025/06/21
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			SpringWeakObject weak = MLEObjects.notNull(
				SpringWeakObject.class, __args[0]);
			
			return weak.clear();
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
	MLEReference(String __key)
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
