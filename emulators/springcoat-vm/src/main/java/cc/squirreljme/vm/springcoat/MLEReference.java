// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.jvm.mle.ReferenceShelf;
import cc.squirreljme.jvm.mle.brackets.RefLinkBracket;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.vm.springcoat.brackets.RefLinkObject;
import java.lang.ref.Reference;

/**
 * Functions for {@link ReferenceShelf}.
 *
 * @since 2020/06/18
 */
public enum MLEReference
	implements MLEFunction
{
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
			return ((RefLinkObject)__args[0]).getObject();
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
			((RefLinkObject)__args[0]).setObject((SpringObject)__args[1]);
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
			return new RefLinkObject();
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
			return ((SpringObject)__args[0]).refLink().get();
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
			((SpringObject)__args[0]).refLink().set(
				(RefLinkObject)__args[1]);
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
