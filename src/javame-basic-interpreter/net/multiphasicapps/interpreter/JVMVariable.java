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
import java.util.Objects;

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
	 * Returns the value.
	 *
	 * @return The current value.
	 * @since 2016/04/07
	 */
	public abstract T get();
	
	/**
	 * Sets this to the given value, if {@code null} it is reset.
	 *
	 * @param __v The value to set it as.
	 * @return {@code this}.
	 * @since 2016/04/07
	 */
	public abstract JVMVariable<T> set(T __v);
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/07
	 */
	@Override
	public String toString()
	{
		return Objects.toString(get());
	}
	
	/**
	 * Wraps the given object and sets the value of it and returns the value.
	 *
	 * @param __v The value to wrap.
	 * @throws JVMEngineException If the type is not known.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/07
	 */
	public static JVMVariable<?> wrap(Object __v)
		throws JVMEngineException, NullPointerException
	{
		// Check
		if (__v == null)
			throw new NullPointerException("NARG");
		
		// Treat Boolean as an integer
		if (__v instanceof Boolean)
			return OfInteger.empty().set(
				((Boolean)__v).booleanValue() ? 1 : 0);
		
		// Integer or promotes to one
		else if ((__v instanceof Short) || (__v instanceof Integer))
			return OfInteger.empty().set(((Number)__v).intValue());
		
		// Character
		else if (__v instanceof Character)
			return OfInteger.empty().set((int)((Character)__v).charValue());
		
		// Long
		else if (__v instanceof Long)
			return OfLong.empty().set(((Number)__v).longValue());
		
		// Float
		else if (__v instanceof Float)
			return OfFloat.empty().set(((Number)__v).floatValue());
		
		// Double
		else if (__v instanceof Double)
			return OfDouble.empty().set(((Number)__v).doubleValue());
		
		// Objects
		else if (__v instanceof JVMObject)
			return OfObject.empty().set((JVMObject)__v);
		
		// {@squirreljme.error IN0d Cannot wrap the given value because it is
		// of an unknown type. (The type of value to wrap).}
		else
			throw new JVMEngineException(String.format("IN0d %s",
				__v.getClass()));
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
	private static <X extends JVMVariable<?>> X __nextQueue(
		Deque<Reference<X>> __q)
		throws NullPointerException
	{
		// Check
		if (__q == null)
			throw new NullPointerException("NARG");
		
		// Lock on the queue
		synchronized (__q)
		{
			// While it is not empty
			while (!__q.isEmpty())
			{
				// Get the next item
				Reference<X> ref = __q.pollFirst();
				
				// In the reference?
				X rv;
				if (null != (rv = ref.get()))
					return rv;
			}
		}
		
		// Not in it at all
		return null;
	}
	
	/**
	 * Stores a double value.
	 *
	 * @since 2016/04/07
	 */
	public static final class OfDouble
		extends JVMVariable<Double>
	{
		/** Next value type queue. */
		private static final Deque<Reference<OfDouble>> _FREE_QUEUE =
			new LinkedList<>();
		
		/** The current value. */
		private volatile double _value;
		
		/**
		 * Initializes the value storage.
		 *
		 * @since 2016/04/07
		 */
		private OfDouble()
		{
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/04/07
		 */
		@Override
		public Double get()
		{
			return _value;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/04/07
		 */
		@Override
		public OfDouble set(Double __v)
		{
			if (__v == null)
				_value = 0;
			else
				_value = __v;
			return this;
		}
		
		/**
		 * Returns the next empty element.
		 *
		 * @return The next empty element.
		 * @since 2016/04/07
		 */
		public static OfDouble empty()
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
	
	/**
	 * Stores a float value.
	 *
	 * @since 2016/04/07
	 */
	public static final class OfFloat
		extends JVMVariable<Float>
	{
		/** Next value type queue. */
		private static final Deque<Reference<OfFloat>> _FREE_QUEUE =
			new LinkedList<>();
		
		/** The value of this. */
		private volatile float _value;
		
		/**
		 * Initializes the value storage.
		 *
		 * @since 2016/04/07
		 */
		private OfFloat()
		{
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/04/07
		 */
		@Override
		public Float get()
		{
			return _value;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/04/07
		 */
		@Override
		public OfFloat set(Float __v)
		{
			if (__v == null)
				_value = 0;
			else
				_value = __v;
			return this;
		}
		
		/**
		 * Returns the next empty element.
		 *
		 * @return The next empty element.
		 * @since 2016/04/07
		 */
		public static OfFloat empty()
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
	
	/**
	 * Stores an integer value.
	 *
	 * @since 2016/04/07
	 */
	public static final class OfInteger
		extends JVMVariable<Integer>
	{
		/** Next value type queue. */
		private static final Deque<Reference<OfInteger>> _FREE_QUEUE =
			new LinkedList<>();
		
		/** The current value. */
		private volatile int _value;
		
		/**
		 * Initializes the value storage.
		 *
		 * @since 2016/04/07
		 */
		private OfInteger()
		{
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/04/07
		 */
		@Override
		public Integer get()
		{
			return _value;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/04/07
		 */
		@Override
		public OfInteger set(Integer __v)
		{
			if (__v == null)
				_value = 0;
			else
				_value = __v;
			return this;
		}
		
		/**
		 * Returns the next empty element.
		 *
		 * @return The next empty element.
		 * @since 2016/04/07
		 */
		public static OfInteger empty()
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
	
	/**
	 * Stores a long value.
	 *
	 * @since 2016/04/07
	 */
	public static final class OfLong
		extends JVMVariable<Long>
	{
		/** Next value type queue. */
		private static final Deque<Reference<OfLong>> _FREE_QUEUE =
			new LinkedList<>();
		
		/** The current value. */
		private volatile long _value;
		
		/**
		 * Initializes the value storage.
		 *
		 * @since 2016/04/07
		 */
		private OfLong()
		{
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/04/07
		 */
		@Override
		public Long get()
		{
			return _value;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/04/07
		 */
		@Override
		public OfLong set(Long __v)
		{
			if (__v == null)
				_value = 0;
			else
				_value = __v;
			return this;
		}
		
		/**
		 * Returns the next empty element.
		 *
		 * @return The next empty element.
		 * @since 2016/04/07
		 */
		public static OfLong empty()
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
	
	/**
	 * Stores a double value.
	 *
	 * @since 2016/04/07
	 */
	public static final class OfObject
		extends JVMVariable<JVMObject>
	{
		/** Next value type queue. */
		private static final Deque<Reference<OfObject>> _FREE_QUEUE =
			new LinkedList<>();
		
		/** The current value. */
		private volatile JVMObject _value;
		
		/**
		 * Initializes the value storage.
		 *
		 * @since 2016/04/07
		 */
		private OfObject()
		{
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/04/07
		 */
		@Override
		public JVMObject get()
		{
			return _value;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/04/07
		 */
		@Override
		public OfObject set(JVMObject __v)
		{
			_value = __v;
			return this;
		}
		
		/**
		 * Returns the next empty element.
		 *
		 * @return The next empty element.
		 * @since 2016/04/07
		 */
		public static OfObject empty()
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

