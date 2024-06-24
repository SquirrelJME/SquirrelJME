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
import cc.squirreljme.jvm.mle.brackets.RefLinkBracket;
import cc.squirreljme.vm.springcoat.brackets.RefLinkHolder;
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
			synchronized (GlobalState.class)
			{
				// Check it, but otherwise do nothing
				MLEObjects.refLink(__args[0]);
			}
			
			return null;
		}
	},
	
	/** {@link ReferenceShelf#linkChain(RefLinkBracket, Object)}. */
	LINK_CHAIN("linkChain:(Lcc/squirreljme/jvm/mle/brackets/" +
		"RefLinkBracket;Ljava/lang/Object;)V")
	{
		/**
		 * {@inheritDoc}
		 * @since 2022/09/01
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			RefLinkObject link = MLEObjects.refLink(__args[0]);
			
			if (__args[1] == null)
				throw new SpringMLECallError("Null object.");
			if (!(__args[1] instanceof SpringSimpleObject))
				throw new SpringMLECallError("Invalid object");
			
			SpringSimpleObject object = (SpringSimpleObject)__args[1];
			RefLinkHolder objRefLink = object.refLink();
			
			synchronized (GlobalState.class)
			{
				// If the object has an existing link, then we need to chain
				// links
				synchronized (objRefLink)
				{
					RefLinkObject oldLink = objRefLink.get();
					if (oldLink != null)
					{
						// New link -> Old link
						link.setNext(oldLink);
						
						// New link <- Old link
						oldLink.setPrev(link);
					}
					
					// The object uses the current link as the head now
					objRefLink.set(link);
				}
			}
			
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
			synchronized (GlobalState.class)
			{
				return MLEObjects.refLink(__args[0]).getNext();
			}
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
			synchronized (GlobalState.class)
			{
				return MLEObjects.refLink(__args[0]).getObject();
			}
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
			synchronized (GlobalState.class)
			{
				return MLEObjects.refLink(__args[0]).getPrev();
			}
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
			synchronized (GlobalState.class)
			{
				MLEObjects.refLink(__args[0]).setNext(
					MLEObjects.refLink(__args[1]));
			}
			
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
			synchronized (GlobalState.class)
			{
				MLEObjects.refLink(__args[0])
					.setObject((SpringObject)__args[1]);
			}
			
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
			synchronized (GlobalState.class)
			{
				MLEObjects.refLink(__args[0]).setPrev(
					MLEObjects.refLink(__args[1]));
			}
			
			return null;
		}
	},
	
	/** {@link ReferenceShelf#linkUnchain(RefLinkBracket)}. */
	LINK_UNCHAIN("linkUnchain:(Lcc/squirreljme/jvm/mle/brackets/" +
		"RefLinkBracket;Lcc/squirreljme/jvm/mle/brackets/RefLinkBracket;)V")
	{
		/**
		 * {@inheritDoc}
		 * @since 2022/09/01
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			RefLinkObject thisLink = MLEObjects.refLink(__args[0]);
			
			synchronized (GlobalState.class)
			{
				// Get the previous and next links to re-chain
				RefLinkObject prev = thisLink.getPrev();
				RefLinkObject next = thisLink.getNext();
				
				// Have the previous link point to our next
				if (prev != null)
					prev.setNext(next);
				
				// Have the next link point to our previous
				if (next != null)
					next.setPrev(prev);
				
				// Clear our links because they are no longer valid
				thisLink.setPrev(null);
				thisLink.setNext(null);
			}
			
			return null;
		}
	},
	
	/** {@link ReferenceShelf#linkUnlinkAndClear(RefLinkBracket)}. */ 
	LINK_UNLINK_AND_CLEAR("linkUnlinkAndClear:(Lcc/squirreljme/jvm/" +
		"mle/brackets/RefLinkBracket;)V")
	{
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			RefLinkObject thisLink = MLEObjects.refLink(__args[0]);
			
			synchronized (GlobalState.class)
			{
				synchronized (thisLink)
				{
					// Unchain all the connected links atomically
					MLEReference.LINK_UNCHAIN.handle(__thread, __args[0]);
					
					// Clear the object this links to
					MLEReference.LINK_SET_OBJECT.handle(__thread, __args[0],
						SpringNullObject.NULL);
					
					// We can delete our link now and free any associated
					// memory because it is dangling and serves no purpose
					// otherwise
					MLEReference.DELETE_LINK.handle(__thread, __args[0]);
				}
			}
			
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
			synchronized (GlobalState.class)
			{
				SpringObject object = (SpringObject)__args[0];
				if (!(object instanceof SpringSimpleObject))
					throw new SpringMLECallError("Invalid object"); 
				
				return object.refLink().get();
			}
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
			synchronized (GlobalState.class)
			{
				SpringObject object = (SpringObject)__args[0];
				if (!(object instanceof SpringSimpleObject))
					throw new SpringMLECallError("Invalid object"); 
				
				object.refLink().set(MLEObjects.refLink(__args[1]));
			}
			
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
	
}
