// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.interpreter;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Deque;
import java.util.LinkedList;

/**
 * This represents a variable which is used to store a value.
 *
 * @param <T> The boxed type.
 * @since 2016/04/07
 */
public abstract class JVMVariable<T>
{
	/**
	 * Initializes the base variable.
	 *
	 * @since 2016/04/07
	 */
	private JVMVariable()
	{
	}
	
	/**
	 * Obtains the next free item from the queue.
	 *
	 * @param <X> The variable type.
	 * @param __q The queue.
	 * @return The next item in the queue or {@code null} if it has nothing.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/07
	 */
	private static <X extends JVMVariable<?>> X __nextQueue(Deque<X> __q)
		throws NullPointerException
	{
		// Check
		if (__q == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	public static final class OfDouble
		extends JVMVariable<Double>
	{
		/** Next value type queue. */
		private static final Deque<OfDouble> _FREE_QUEUE =
			new LinkedList<>();
		
		/**
		 * Returns the next empty element.
		 *
		 * @return The next empty element.
		 * @since 2016/04/07
		 */
		public static OfDouble nextEmpty()
		{
			// In queue?
			OfDouble rv = JVMVariable.<OfDouble>__nextQueue(_FREE_QUEUE);
			
			// Missing?
			if (rv == null)
				rv = new OfDouble();
			
			// Return it
			return rv;
		}
	}
	
	public static final class OfFloat
		extends JVMVariable<Float>
	{
		/** Next value type queue. */
		private static final Deque<OfFloat> _FREE_QUEUE =
			new LinkedList<>();
		
		/**
		 * Returns the next empty element.
		 *
		 * @return The next empty element.
		 * @since 2016/04/07
		 */
		public static OfFloat nextEmpty()
		{
			// In queue?
			OfFloat rv = JVMVariable.<OfFloat>__nextQueue(_FREE_QUEUE);
			
			// Missing?
			if (rv == null)
				rv = new OfFloat();
			
			// Return it
			return rv;
		}
	}
	
	public static final class OfInteger
		extends JVMVariable<Integer>
	{
		/** Next value type queue. */
		private static final Deque<OfInteger> _FREE_QUEUE =
			new LinkedList<>();
		
		/**
		 * Returns the next empty element.
		 *
		 * @return The next empty element.
		 * @since 2016/04/07
		 */
		public static OfInteger nextEmpty()
		{
			// In queue?
			OfInteger rv = JVMVariable.<OfInteger>__nextQueue(_FREE_QUEUE);
			
			// Missing?
			if (rv == null)
				rv = new OfInteger();
			
			// Return it
			return rv;
		}
	}
	
	public static final class OfLong
		extends JVMVariable<Long>
	{
		/** Next value type queue. */
		private static final Deque<OfLong> _FREE_QUEUE =
			new LinkedList<>();
		
		/**
		 * Returns the next empty element.
		 *
		 * @return The next empty element.
		 * @since 2016/04/07
		 */
		public static OfLong nextEmpty()
		{
			// In queue?
			OfLong rv = JVMVariable.<OfLong>__nextQueue(_FREE_QUEUE);
			
			// Missing?
			if (rv == null)
				rv = new OfLong();
			
			// Return it
			return rv;
		}
	}
	
	public static final class OfObject
		extends JVMVariable<JVMObject>
	{
		/** Next value type queue. */
		private static final Deque<OfObject> _FREE_QUEUE =
			new LinkedList<>();
		
		/**
		 * Returns the next empty element.
		 *
		 * @return The next empty element.
		 * @since 2016/04/07
		 */
		public static OfObject nextEmpty()
		{
			// In queue?
			OfObject rv = JVMVariable.<OfObject>__nextQueue(_FREE_QUEUE);
			
			// Missing?
			if (rv == null)
				rv = new OfObject();
			
			// Return it
			return rv;
		}
	}
}

