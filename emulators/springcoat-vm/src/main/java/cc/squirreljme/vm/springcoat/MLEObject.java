// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.jvm.mle.ObjectShelf;
import cc.squirreljme.jvm.mle.brackets.TypeBracket;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.vm.springcoat.brackets.TypeObject;
import net.multiphasicapps.classfile.MethodDescriptor;

/**
 * Functions for {@link MLEObject}.
 *
 * @since 2020/06/18
 */
public enum MLEObject
	implements MLEFunction
{
	/** {@link ObjectShelf#arrayLength(Object)}. */
	ARRAY_LENGTH("arrayLength:(Ljava/lang/Object;)I")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/18
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			SpringObject object = (SpringObject)__args[0];
			if (object instanceof SpringArrayObject)
				return ((SpringArrayObject)object).length();
			return -1;
		}
	},
	
	/** {@link ObjectShelf#arrayNew(TypeBracket, int)}. */
	ARRAY_NEW( "arrayNew:(Lcc/squirreljme/jvm/mle/brackets/" +
		"TypeBracket;I)Ljava/lang/Object;")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/18
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return __thread.allocateArray(((TypeObject)__args[0])
				.getSpringClass(), (int)__args[1]);
		}
	},
	
	/** {@link ObjectShelf#identityHashCode(Object)}. */
	IDENTITY_HASH_CODE("identityHashCode:(Ljava/lang/Object;)I")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/18
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return System.identityHashCode(__args[0]);
		}
	},
	
	/** {@link ObjectShelf#newInstance(TypeBracket)}. */
	NEW_INSTANCE("newInstance:(Lcc/squirreljme/jvm/mle/brackets/" +
		"TypeBracket;)Ljava/lang/Object;")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/18
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return __thread.newInstance(
				((TypeObject)__args[0]).getSpringClass(),
				new MethodDescriptor("()V"));
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
	MLEObject(String __key)
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
