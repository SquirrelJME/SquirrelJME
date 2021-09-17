// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.jvm.mle.ReferenceShelf;
import cc.squirreljme.jvm.mle.brackets.RefLinkBracket;
import cc.squirreljme.vm.springcoat.brackets.RefLinkObject;
import cc.squirreljme.vm.springcoat.exceptions.SpringMLECallError;

/**
 * Functions for {@link ReferenceShelf}.
 *
 * @since 2020/06/18
 */
public enum MLEReference
	implements MLEFunction
{
	/** {@link ReferenceShelf#deleteLink(RefLinkBracket)}. */
	DELETE_LINK("deleteLink:(Lcc/squirreljme/jvm/mle/brackets/" +
		"RefLinkBracket;)V")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/29
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			// Check it, but otherwise do nothing
			MLEReference.__refLink(__args[0]);
			
			return null;
		}
	},
	
	/** {@link ReferenceShelf#linkGetNext(RefLinkBracket)}. */
	LINK_GET_NEXT("linkGetNext:(Lcc/squirreljme/jvm/mle/brackets/" +
		"RefLinkBracket;)Lcc/squirreljme/jvm/mle/brackets/RefLinkBracket;")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/29
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return MLEReference.__refLink(__args[0]).getNext();
		}
	},
	
	/** {@link ReferenceShelf#linkGetObject(RefLinkBracket)}. */
	LINK_GET_OBJECT("linkGetObject:(Lcc/squirreljme/jvm/mle/brackets/" +
		"RefLinkBracket;)Ljava/lang/Object;")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/18
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return MLEReference.__refLink(__args[0]).getObject();
		}
	},
	
	/** {@link ReferenceShelf#linkGetPrev(RefLinkBracket)}. */
	LINK_GET_PREV("linkGetPrev:(Lcc/squirreljme/jvm/mle/brackets/" +
		"RefLinkBracket;)Lcc/squirreljme/jvm/mle/brackets/RefLinkBracket;")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/29
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return MLEReference.__refLink(__args[0]).getPrev();
		}
	},
	
	/** {@link ReferenceShelf#linkSetNext(RefLinkBracket, RefLinkBracket)}. */
	LINK_SET_NEXT("linkSetNext:(Lcc/squirreljme/jvm/mle/brackets/" +
		"RefLinkBracket;Lcc/squirreljme/jvm/mle/brackets/RefLinkBracket;)V")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/29
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			MLEReference.__refLink(__args[0]).setNext(
				MLEReference.__refLink(__args[1]));
			return null;
		}
	},
	
	/** {@link ReferenceShelf#linkSetObject(RefLinkBracket, Object)}. */
	LINK_SET_OBJECT("linkSetObject:(Lcc/squirreljme/jvm/mle/brackets/" +
		"RefLinkBracket;Ljava/lang/Object;)V")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/18
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			MLEReference.__refLink(__args[0])
				.setObject((SpringObject)__args[1]);
			return null;
		}
	},
	
	/** {@link ReferenceShelf#linkSetPrev(RefLinkBracket, RefLinkBracket)}. */
	LINK_SET_PREV("linkSetPrev:(Lcc/squirreljme/jvm/mle/brackets/" +
		"RefLinkBracket;Lcc/squirreljme/jvm/mle/brackets/RefLinkBracket;)V")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/29
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			MLEReference.__refLink(__args[0]).setPrev(
				MLEReference.__refLink(__args[1]));
			return null;
		}
	},
	
	/** {@link ReferenceShelf#newLink()}. */
	NEW_LINK("newLink:()Lcc/squirreljme/jvm/mle/brackets/" +
		"RefLinkBracket;")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/18
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return new RefLinkObject(__thread.machine);
		}
	},
	
	/** {@link ReferenceShelf#objectGet(Object)}. */
	OBJECT_GET("objectGet:(Ljava/lang/Object;)" +
		"Lcc/squirreljme/jvm/mle/brackets/RefLinkBracket;")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/18
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			SpringObject object = (SpringObject)__args[0];
			if (!(object instanceof SpringSimpleObject))
				throw new SpringMLECallError("Invalid object"); 
			
			return object.refLink().get();
		}
	},
	
	/** {@link ReferenceShelf#objectSet(Object, RefLinkBracket)}. */
	OBJECT_SET("objectSet:(Ljava/lang/Object;" +
		"Lcc/squirreljme/jvm/mle/brackets/RefLinkBracket;)V")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/18
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			SpringObject object = (SpringObject)__args[0];
			if (!(object instanceof SpringSimpleObject))
				throw new SpringMLECallError("Invalid object"); 
			
			object.refLink().set(MLEReference.__refLink(__args[1]));
			return null;
		}
	}
	
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
	
	/**
	 * Checks if this is a {@link RefLinkObject}.
	 * 
	 * @param __object The object to check.
	 * @return As a {@link RefLinkObject} if this is one.
	 * @throws SpringMLECallError If this is not a {@link RefLinkObject}.
	 * @since 2020/06/28
	 */
	static RefLinkObject __refLink(Object __object)
		throws SpringMLECallError
	{
		if (!(__object instanceof RefLinkObject))
			throw new SpringMLECallError("Not a RefLinkObject.");
		
		return (RefLinkObject)__object; 
	}
}
