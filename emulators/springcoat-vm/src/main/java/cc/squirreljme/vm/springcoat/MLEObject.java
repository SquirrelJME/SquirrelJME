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
	/** {@link ObjectShelf#arrayCopy(char[], int, char[], int, int)}. */
	ARRAY_COPY_CHAR("arrayCopy:([CI[CII)V")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/22
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			SpringArrayObjectChar src = (SpringArrayObjectChar)__args[0];
			int srcOff = (int)__args[1];
			SpringArrayObjectChar dest = (SpringArrayObjectChar)__args[2];
			int destOff = (int)__args[3];
			int len = (int)__args[4];
			
			System.arraycopy(src.array(), srcOff,
				dest.array(), destOff, len);
			
			return null;
		}
	}, 
	
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
	},
	
	/** {@link ObjectShelf#notify(Object, boolean)}. */
	NOTIFY("notify:(Ljava/lang/Object;Z)I")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/22
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			SpringObject target = (SpringObject)__args[0];
			boolean notifyAll = (int)__args[1] != 0; 
			
			// Signal the monitor
			return target.monitor().monitorNotify(__thread.thread, notifyAll);
		}
	},
	
	/** {@link ObjectShelf#wait(Object, long, int)}. */
	WAIT("wait:(Ljava/lang/Object;JI)I")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/22
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			SpringObject target = (SpringObject)__args[0];
			long ms = (long)__args[1];
			int ns = (int)__args[2];
			
			return target.monitor().monitorWait(__thread.thread, ms, ns);
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
