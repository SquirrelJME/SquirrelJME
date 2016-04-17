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

import java.util.AbstractList;
import java.util.Arrays;
import net.multiphasicapps.classprogram.CPVariableType;

/**
 * This class provides a linear stream for storage of data which is used by the
 * static area of classes along with the stack which is used by the execution
 * of methods.
 *
 * This class is thread safe.
 *
 * @since 2016/04/17
 */
public class JVMDataStore
	extends AbstractList<Object>
{
	/** The data storage fragment size. */
	protected static final int FRAGMENT_SIZE =
		32;
	
	/** The fragment shift. */
	protected static final int FRAGMENT_SHIFT =
		Integer.numberOfTrailingZeros(FRAGMENT_SIZE);
	
	/** The fragment mask. */
	protected static final int FRAGMENT_MASK =
		FRAGMENT_SIZE - 1;
	
	/** Lock. */
	protected final Object lock =
		new Object();
	
	/** The fragments which are known. */
	private volatile __Fragment__[] _fragments;
	
	/** The window top. */
	private volatile int _wintop;
	
	/**
	 * Initializes the data store.
	 *
	 * @since 2016/04/17
	 */
	public JVMDataStore()
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/17
	 */
	@Override
	public Object get(int __i)
		throws IndexOutOfBoundsException
	{
		// Lock
		synchronized (lock)
		{
			// Get the curren type here
			CPVariableType t = getType(__i);
			
			// Nothing?
			if (t == null)
				return null;
			
			// Depends
			switch (t)
			{
					// int
				case INTEGER:
					return getInt(__i);
					
					// long
				case LONG:
					return getLong(__i);
					
					// float
				case FLOAT:
					return getFloat(__i);
					
					// double
				case DOUBLE:
					return getDouble(__i);
				
					// Unknown
				default:
					throw new RuntimeException("WTFX");
			}
		}
	}
	
	/**
	 * Obtains the {@code double} value at this position.
	 *
	 * @param __i The index of the entry.
	 * @return The value at this position.
	 * @throws JVMEngineException If the data here is not a double.
	 * @throws IndexOutOfBoundsException If the index is not within the
	 * storage bounds.
	 * @since 2016/04/17
	 */
	public double getDouble(int __i)
		throws JVMEngineException, IndexOutOfBoundsException
	{
		// Lock
		synchronized (lock)
		{
			throw new Error("TODO");
		}
	}
	
	/**
	 * Obtains the {@code float} value at this position.
	 *
	 * @param __i The index of the entry.
	 * @return The value at this position.
	 * @throws JVMEngineException If the data here is not a float.
	 * @throws IndexOutOfBoundsException If the index is not within the
	 * storage bounds.
	 * @since 2016/04/17
	 */
	public float getFloat(int __i)
		throws JVMEngineException, IndexOutOfBoundsException
	{
		// Lock
		synchronized (lock)
		{
			return __fragment(__i).getFloat(__i & FRAGMENT_MASK);
		}
	}
	
	/**
	 * Obtains the {@code int} value at this position.
	 *
	 * @param __i The index of the entry.
	 * @return The value at this position.
	 * @throws JVMEngineException If the data here is not an integer.
	 * @throws IndexOutOfBoundsException If the index is not within the
	 * storage bounds.
	 * @since 2016/04/17
	 */
	public int getInt(int __i)
		throws JVMEngineException, IndexOutOfBoundsException
	{
		// Lock
		synchronized (lock)
		{
			return __fragment(__i).getInt(__i & FRAGMENT_MASK);
		}
	}
	
	/**
	 * Obtains the {@code long} value at this position.
	 *
	 * @param __i The index of the entry.
	 * @return The value at this position.
	 * @throws JVMEngineException If the data here is not a long.
	 * @throws IndexOutOfBoundsException If the index is not within the
	 * storage bounds.
	 * @since 2016/04/17
	 */
	public long getLong(int __i)
		throws JVMEngineException, IndexOutOfBoundsException
	{
		// Lock
		synchronized (lock)
		{
			throw new Error("TODO");
		}
	}
	
	/**
	 * Obtains the {@link JVMValue} value at this position.
	 *
	 * @param __i The index of the entry.
	 * @return The value at this position.
	 * @throws JVMEngineException If the data here is not an object.
	 * @throws IndexOutOfBoundsException If the index is not within the
	 * storage bounds.
	 * @since 2016/04/17
	 */
	public JVMObject getObject(int __i)
		throws JVMEngineException, IndexOutOfBoundsException
	{
		// Lock
		synchronized (lock)
		{
			return __fragment(__i).getObject(__i & FRAGMENT_MASK);
		}
	}
	
	/**
	 * Obtains the type used at this given position.
	 *
	 * @param __i The index to get the type of.
	 * @return The type used at this position or {@code null} if not set.
	 * @throws IndexOutOfBoundsException If the index is not within bounds.
	 * @since 2016/04/17
	 */
	public CPVariableType getType(int __i)
		throws IndexOutOfBoundsException
	{
		// Lock
		synchronized (lock)
		{
			return __fragment(__i).getType(__i & FRAGMENT_MASK);
		}
	}
	
	/**
	 * Pushes a window over the data storage, new fragments are created as
	 * needed to store the window data.
	 *
	 * @param __nvars The number of variables to store.
	 * @return The window over the storage are.
	 * @throws IllegalArgumentException On null arguments.
	 * @throws IllegalStateException If there is no room for the window.
	 * @since 2016/04/17
	 */
	public Window pushWindow(int __nvars)
		throws IllegalArgumentException, IllegalStateException
	{
		// Check
		if (__nvars < 0)
			throw new IllegalArgumentException("NEGI");
		
		// Lock
		synchronized (lock)
		{
			// Calculate start and end
			int start = _wintop;
			
			// {@squirreljme.error IN13 Too much storage is in use.}
			int newtop = start + __nvars;
			if (newtop < start)
				throw new IllegalStateException("IN13");
			
			// Set new top
			_wintop = newtop;
			
			// Need to resize?
			__Fragment__[] frags = _fragments;
			int newl = (newtop >> FRAGMENT_SHIFT);
			if (frags == null || newl > frags.length)
			{
				// Direct allocation
				if (frags == null)
					frags = new __Fragment__[newl];
				
				// Resized
				else
					frags = Arrays.<__Fragment__>copyOf(frags, newl);
				
				// Set new
				_fragments = frags;
			}
			
			// Create the window
			return new Window(start, __nvars);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/17
	 */
	@Override
	public Object set(int __i, Object __v)
	{
		// Lock
		synchronized (lock)
		{
			// Get the old value
			Object old = get(__i);
			
			// Integer
			if (__v instanceof Byte || __v instanceof Short ||
				__v instanceof Integer)
				setInt(__i, ((Number)__v).intValue());
			
			// Long
			else if (__v instanceof Long)
				setLong(__i, ((Number)__v).longValue());
			
			// Float
			else if (__v instanceof Float)
				setFloat(__i, ((Number)__v).floatValue());
			
			// Double
			else if (__v instanceof Double)
				setDouble(__i, ((Number)__v).doubleValue());
			
			// Object
			else if (__v instanceof JVMObject || __v == null)
				setObject(__i, (JVMObject)__v);
			
			// {@squirreljme.error IN11 Unknown data storage class type.}
			else
				throw new ClassCastException("IN11");
			
			// Return the old value which allows any value
			return old;
		}
	}
	
	/**
	 * Sets the given position to the given double value.
	 *
	 * @param __i The index to set.
	 * @param __v The value to set it as.
	 * @return The old value as a double, if not compatible with a
	 * floating point number this will be {@code NaN}.
	 * @throws IndexOutOfBoundsException If the index is not within the
	 * storage bounds.
	 * @since 2016/04/17
	 */
	public double setDouble(int __i, double __v)
		throws IndexOutOfBoundsException
	{
		// Lock
		synchronized (lock)
		{
			// Get previous type
			CPVariableType ot = getType(__i);
			long rrv = 0;
			
			// Convert value for high and low
			long val = Double.doubleToRawLongBits(__v);
			int hi = (int)(val >>> 32L),
				lo = (int)val;
			
			// Set the value
			int orig;
			rrv |= ((long)(orig = __fragment(__i).setDoubleHigh(
				(__i & FRAGMENT_MASK), hi))) << 32L;
			rrv |= (long)__fragment(__i + 1).setDoubleLow(
				((__i + 1) & FRAGMENT_MASK), lo);
			
			// Convert the value
			switch (ot)
			{
				case OBJECT: return Double.NaN;
				case INTEGER: return (double)orig;
				case LONG: return (double)rrv;
				case FLOAT: return Float.intBitsToFloat(orig);
				default: return Double.longBitsToDouble(rrv);
			}
		}
	}
	
	/**
	 * Sets the given position to the given float value.
	 *
	 * @param __i The index to set.
	 * @param __v The value to set it as.
	 * @return The old value as a float, if not compatible with a
	 * floating point number this will be {@code NaN}.
	 * @throws IndexOutOfBoundsException If the index is not within the
	 * storage bounds.
	 * @since 2016/04/17
	 */
	public float setFloat(int __i, double __v)
		throws IndexOutOfBoundsException
	{
		// Lock
		synchronized (lock)
		{
			throw new Error("TODO");
		}
	}
	
	/**
	 * Sets the given position to the given integer value.
	 *
	 * @param __i The index to set.
	 * @param __v The value to set it as.
	 * @return The old value as an integer, will be
	 * {@link Integer#MIN_VALUE} if not compatible with an integer.
	 * @throws IndexOutOfBoundsException If the index is not within the
	 * storage bounds.
	 * @since 2016/04/17
	 */
	public int setInt(int __i, int __v)
		throws IndexOutOfBoundsException
	{
		// Lock
		synchronized (lock)
		{
			throw new Error("TODO");
		}
	}
	
	/**
	 * Sets the given position to the given long value.
	 *
	 * @param __i The index to set.
	 * @param __v The value to set it as.
	 * @return The old value as an long, will be {@link Long#MIN_VALUE}
	 * if not compatible with a long.
	 * @throws IndexOutOfBoundsException If the index is not within the
	 * storage bounds.
	 * @since 2016/04/17
	 */
	public long setLong(int __i, long __v)
		throws IndexOutOfBoundsException
	{
		// Lock
		synchronized (lock)
		{
			// Get previous type
			CPVariableType ot = getType(__i);
			long rrv = 0;
			
			// Convert value for high and low
			int hi = (int)(__v >>> 32L),
				lo = (int)__v;
			
			// Set the value
			int orig;
			rrv |= ((long)(orig = __fragment(__i).setLongHigh(
				(__i & FRAGMENT_MASK), hi))) << 32L;
			rrv |= (long)__fragment(__i + 1).setLongLow(
				((__i + 1) & FRAGMENT_MASK), lo);
			
			// Return it
			switch (ot)
			{
				case OBJECT: return Long.MIN_VALUE;
				case INTEGER: return orig;
				case FLOAT: return (long)Float.intBitsToFloat(orig);
				case DOUBLE: return (long)Double.longBitsToDouble(rrv);
				default: return rrv;
			}
		}
	}
	
	/**
	 * Sets the given position to the given object value.
	 *
	 * @param __i The index to set.
	 * @param __v The value to set it as.
	 * @return The old value as an object, will be {@code null}
	 * if not compatible with an object.
	 * @throws IndexOutOfBoundsException If the index is not within the
	 * storage bounds.
	 * @since 2016/04/17
	 */
	public JVMObject setObject(int __i, JVMObject __v)
		throws IndexOutOfBoundsException
	{
		// Lock
		synchronized (lock)
		{
			throw new Error("TODO");
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/17
	 */
	@Override
	public int size()
	{
		// Lock
		synchronized (lock)
		{
			__Fragment__[] frags = _fragments;
			
			// Calculate the size
			if (frags == null)
				return 0;
			return frags.length * FRAGMENT_SIZE;
		}
	}
	
	/**
	 * Returns the fragment associated with this index.
	 *
	 * @param __i The index of the fragment.
	 * @return The fragment for this position.
	 * @throws IndexOutOfBoundsException If the index is not within the
	 * fragment bounds.
	 * @since 2016/04/17
	 */
	private __Fragment__ __fragment(int __i)
		throws IndexOutOfBoundsException
	{
		// Check
		if (__i < 0)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Get shifted index
		int shdx = __i >> FRAGMENT_SHIFT;
		
		// Lock
		synchronized (lock)
		{
			__Fragment__[] frags = _fragments;
			
			// If there are no fragments then it will always be out of bounds
			if (frags == null)
				throw new IndexOutOfBoundsException("IOOB");
			
			// Otherwise use the one
			__Fragment__ rv = frags[shdx];
			
			// Needs initialization
			if (rv == null)
				frags[shdx] = (rv = new __Fragment__());
			
			// Return it
			return rv;
		}
	}
	
	/**
	 * This provides a window into the data storage area since it appears as
	 * a large chunk of data.
	 *
	 * @since 2016/04/17
	 */
	public class Window
		extends AbstractList<Object>
	{
		/** The start of the window. */
		protected final int start;
		
		/** The length of the window. */
		protected final int length;
		
		/**
		 * This initializes the variable storage window.
		 *
		 * @param __start The window start.
		 * @param __len The window length.
		 * @throws IndexOutOfBoundsException If the start or length are
		 * negative.
		 * @since 2016/04/17
		 */
		private Window(int __start, int __len)
			throws IndexOutOfBoundsException
		{
			if (__start < 0 || __len < 0)
				throw new IndexOutOfBoundsException("IOOB");
			
			// Set
			start = __start;
			length = __len;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/04/17
		 */
		@Override
		public Object get(int __i)
			throws IndexOutOfBoundsException
		{
			return JVMDataStore.this.get(start + __bounds(__i, false));
		}
	
		/**
		 * Obtains the {@code double} value at this position.
		 *
		 * @param __i The index of the entry.
		 * @return The value at this position.
		 * @throws JVMEngineException If the data here is not a double.
		 * @throws IndexOutOfBoundsException If the index is not within the
		 * storage bounds.
		 * @since 2016/04/17
		 */
		public double getDouble(int __i)
			throws JVMEngineException, IndexOutOfBoundsException
		{
			return JVMDataStore.this.getDouble(start + __bounds(__i, true));
		}
	
		/**
		 * Obtains the {@code float} value at this position.
		 *
		 * @param __i The index of the entry.
		 * @return The value at this position.
		 * @throws JVMEngineException If the data here is not a float.
		 * @throws IndexOutOfBoundsException If the index is not within the
		 * storage bounds.
		 * @since 2016/04/17
		 */
		public float getFloat(int __i)
			throws JVMEngineException, IndexOutOfBoundsException
		{
			return JVMDataStore.this.getFloat(start + __bounds(__i, false));
		}
	
		/**
		 * Obtains the {@code int} value at this position.
		 *
		 * @param __i The index of the entry.
		 * @return The value at this position.
		 * @throws JVMEngineException If the data here is not an integer.
		 * @throws IndexOutOfBoundsException If the index is not within the
		 * storage bounds.
		 * @since 2016/04/17
		 */
		public int getInt(int __i)
			throws JVMEngineException, IndexOutOfBoundsException
		{
			return JVMDataStore.this.getInt(start + __bounds(__i, false));
		}
	
		/**
		 * Obtains the {@code long} value at this position.
		 *
		 * @param __i The index of the entry.
		 * @return The value at this position.
		 * @throws JVMEngineException If the data here is not a long.
		 * @throws IndexOutOfBoundsException If the index is not within the
		 * storage bounds.
		 * @since 2016/04/17
		 */
		public long getLong(int __i)
			throws JVMEngineException, IndexOutOfBoundsException
		{
			return JVMDataStore.this.getLong(start + __bounds(__i, true));
		}
	
		/**
		 * Obtains the {@link JVMValue} value at this position.
		 *
		 * @param __i The index of the entry.
		 * @return The value at this position.
		 * @throws JVMEngineException If the data here is not an object.
		 * @throws IndexOutOfBoundsException If the index is not within the
		 * storage bounds.
		 * @since 2016/04/17
		 */
		public JVMObject getObject(int __i)
			throws JVMEngineException, IndexOutOfBoundsException
		{
			return JVMDataStore.this.getObject(start + __bounds(__i, false));
		}
		
		/**
		 * Obtains the type used at this given position.
		 *
		 * @param __i The index to get the type of.
		 * @return The type used at this position or {@code null} if not set.
		 * @throws IndexOutOfBoundsException If the index is not within bounds.
		 * @since 2016/04/17
		 */
		public CPVariableType getType(int __i)
			throws IndexOutOfBoundsException
		{
			return JVMDataStore.this.getType(start + __bounds(__i, false));
		}
		
		/**
		 * Pops this window from the top of the window stack.
		 *
		 * @return The owning data store.
		 * @throws IllegalStateException If this is not the topmost window.
		 * @since 2016/04/17
		 */
		public JVMDataStore popWindow()
			throws IllegalStateException
		{
			// Lock
			synchronized (lock)
			{
				throw new Error("TODO");
			}
		}
	
		/**
		 * {@inheritDoc}
		 * @since 2016/04/17
		 */
		@Override
		public Object set(int __i, Object __v)
		{
			return JVMDataStore.this.set(start + __bounds(__i,
				(__v instanceof Long || __v instanceof Double)), __v);
		}
	
		/**
		 * Sets the given position to the given double value.
		 *
		 * @param __i The index to set.
		 * @param __v The value to set it as.
		 * @return The old value as a double, if not compatible with a
		 * floating point number this will be {@code NaN}.
		 * @throws IndexOutOfBoundsException If the index is not within the
		 * storage bounds.
		 * @since 2016/04/17
		 */
		public double setDouble(int __i, double __v)
			throws IndexOutOfBoundsException
		{
			return JVMDataStore.this.setDouble(start + __bounds(__i, true),
				__v);
		}
	
		/**
		 * Sets the given position to the given float value.
		 *
		 * @param __i The index to set.
		 * @param __v The value to set it as.
		 * @return The old value as a float, if not compatible with a
		 * floating point number this will be {@code NaN}.
		 * @throws IndexOutOfBoundsException If the index is not within the
		 * storage bounds.
		 * @since 2016/04/17
		 */
		public float setFloat(int __i, double __v)
			throws IndexOutOfBoundsException
		{
			return JVMDataStore.this.setFloat(start + __bounds(__i, false),
				__v);
		}
	
		/**
		 * Sets the given position to the given integer value.
		 *
		 * @param __i The index to set.
		 * @param __v The value to set it as.
		 * @return The old value as an integer, will be
		 * {@link Integer#MIN_VALUE} if not compatible with an integer.
		 * @throws IndexOutOfBoundsException If the index is not within the
		 * storage bounds.
		 * @since 2016/04/17
		 */
		public int setInt(int __i, int __v)
			throws IndexOutOfBoundsException
		{
			return JVMDataStore.this.setInt(start + __bounds(__i, false),
				__v);
		}
	
		/**
		 * Sets the given position to the given long value.
		 *
		 * @param __i The index to set.
		 * @param __v The value to set it as.
		 * @return The old value as an long, will be {@link Long#MIN_VALUE}
		 * if not compatible with a long.
		 * @throws IndexOutOfBoundsException If the index is not within the
		 * storage bounds.
		 * @since 2016/04/17
		 */
		public long setLong(int __i, long __v)
			throws IndexOutOfBoundsException
		{
			return JVMDataStore.this.setLong(start + __bounds(__i, true),
				__v);
		}
	
		/**
		 * Sets the given position to the given object value.
		 *
		 * @param __i The index to set.
		 * @param __v The value to set it as.
		 * @return The old value as an object, will be {@code null}
		 * if not compatible with an object.
		 * @throws IndexOutOfBoundsException If the index is not within the
		 * storage bounds.
		 * @since 2016/04/17
		 */
		public JVMObject setObject(int __i, JVMObject __v)
			throws IndexOutOfBoundsException
		{
			return JVMDataStore.this.setObject(start + __bounds(__i, false),
				__v);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/04/17
		 */
		@Override
		public int size()
		{
			return length;
		}
		
		/**
		 * Checks the bounds within the internal window.
		 *
		 * @param __i The value to check the bounds of
		 * @param __wide Is this a long or double?
		 * @return {@code __i}.
		 * @throws IndexOutOfBoundsException If the value is not within
		 * bounds.
		 * @since 2016/04/17
		 */
		private int __bounds(int __i, boolean __wide)
			throws IndexOutOfBoundsException
		{
			// Check
			if (__i < 0 || __i >= length - (__wide ? 1 : 0))
				throw new IndexOutOfBoundsException(String.format("IOOB %d",
					__i));
			
			// Return it
			return __i;
		}
	}
	
	/**
	 * This is a fragment which is a linear chunk of data storage information
	 *
	 * @since 2016/04/17
	 */
	private class __Fragment__
	{
		/**
		 * Initializes the fragment.
		 *
		 * @since 2016/04/17
		 */
		private __Fragment__()
		{
		}
		
		/**
		 * Obtains the {@code double} value at this position.
		 *
		 * @param __i The index of the entry.
		 * @return The value at this position.
		 * @throws JVMEngineException If the data here is not a double.
		 * @throws IndexOutOfBoundsException If the index is not within the
		 * storage bounds.
		 * @since 2016/04/17
		 */
		public double getDouble(int __i)
			throws JVMEngineException, IndexOutOfBoundsException
		{
			// Lock
			synchronized (lock)
			{
				throw new Error("TODO");
			}
		}
	
		/**
		 * Obtains the {@code float} value at this position.
		 *
		 * @param __i The index of the entry.
		 * @return The value at this position.
		 * @throws JVMEngineException If the data here is not a float.
		 * @throws IndexOutOfBoundsException If the index is not within the
		 * storage bounds.
		 * @since 2016/04/17
		 */
		public float getFloat(int __i)
			throws JVMEngineException, IndexOutOfBoundsException
		{
			// Lock
			synchronized (lock)
			{
				throw new Error("TODO");
			}
		}
	
		/**
		 * Obtains the {@code int} value at this position.
		 *
		 * @param __i The index of the entry.
		 * @return The value at this position.
		 * @throws JVMEngineException If the data here is not an integer.
		 * @throws IndexOutOfBoundsException If the index is not within the
		 * storage bounds.
		 * @since 2016/04/17
		 */
		public int getInt(int __i)
			throws JVMEngineException, IndexOutOfBoundsException
		{
			// Lock
			synchronized (lock)
			{
				throw new Error("TODO");
			}
		}
	
		/**
		 * Obtains the {@code long} value at this position.
		 *
		 * @param __i The index of the entry.
		 * @return The value at this position.
		 * @throws JVMEngineException If the data here is not a long.
		 * @throws IndexOutOfBoundsException If the index is not within the
		 * storage bounds.
		 * @since 2016/04/17
		 */
		public long getLong(int __i)
			throws JVMEngineException, IndexOutOfBoundsException
		{
			// Lock
			synchronized (lock)
			{
				throw new Error("TODO");
			}
		}
	
		/**
		 * Obtains the {@link JVMValue} value at this position.
		 *
		 * @param __i The index of the entry.
		 * @return The value at this position.
		 * @throws JVMEngineException If the data here is not an object.
		 * @throws IndexOutOfBoundsException If the index is not within the
		 * storage bounds.
		 * @since 2016/04/17
		 */
		public JVMObject getObject(int __i)
			throws JVMEngineException, IndexOutOfBoundsException
		{
			// Lock
			synchronized (lock)
			{
				throw new Error("TODO");
			}
		}
		
		/**
		 * Obtains the type used at this given position.
		 *
		 * @param __i The index to get the type of.
		 * @return The type used at this position or {@code null} if not set.
		 * @throws IndexOutOfBoundsException If the index is not within bounds.
		 * @since 2016/04/17
		 */
		public CPVariableType getType(int __i)
			throws IndexOutOfBoundsException
		{
			// Lock
			synchronized (lock)
			{
				throw new Error("TODO");
			}
		}
	
		/**
		 * Sets the given high position to the given double value.
		 *
		 * @param __i The index to set.
		 * @param __v The value to set it as.
		 * @return The old value as a double, if not compatible with a
		 * floating point number this will be {@code NaN}.
		 * @throws IndexOutOfBoundsException If the index is not within the
		 * storage bounds.
		 * @since 2016/04/17
		 */
		public int setDoubleHigh(int __i, int __v)
			throws IndexOutOfBoundsException
		{
			// Lock
			synchronized (lock)
			{
				throw new Error("TODO");
			}
		}
	
		/**
		 * Sets the given low position to the given double value.
		 *
		 * @param __i The index to set.
		 * @param __v The value to set it as.
		 * @return The old value as a double, if not compatible with a
		 * floating point number this will be {@code NaN}.
		 * @throws IndexOutOfBoundsException If the index is not within the
		 * storage bounds.
		 * @since 2016/04/17
		 */
		public int setDoubleLow(int __i, int __v)
			throws IndexOutOfBoundsException
		{
			// Lock
			synchronized (lock)
			{
				throw new Error("TODO");
			}
		}
	
		/**
		 * Sets the given position to the given float value.
		 *
		 * @param __i The index to set.
		 * @param __v The value to set it as.
		 * @return The old value as a float, if not compatible with a
		 * floating point number this will be {@code NaN}.
		 * @throws IndexOutOfBoundsException If the index is not within the
		 * storage bounds.
		 * @since 2016/04/17
		 */
		public float setFloat(int __i, double __v)
			throws IndexOutOfBoundsException
		{
			// Lock
			synchronized (lock)
			{
				throw new Error("TODO");
			}
		}
	
		/**
		 * Sets the given position to the given integer value.
		 *
		 * @param __i The index to set.
		 * @param __v The value to set it as.
		 * @return The old value as an integer, will be
		 * {@link Integer#MIN_VALUE} if not compatible with an integer.
		 * @throws IndexOutOfBoundsException If the index is not within the
		 * storage bounds.
		 * @since 2016/04/17
		 */
		public int setInt(int __i, int __v)
			throws IndexOutOfBoundsException
		{
			// Lock
			synchronized (lock)
			{
				throw new Error("TODO");
			}
		}
	
		/**
		 * Sets the given high position to the given long value.
		 *
		 * @param __i The index to set.
		 * @param __v The value to set it as.
		 * @return The old value as an int, will be {@link Integer#MIN_VALUE}
		 * if not compatible with a int.
		 * @throws IndexOutOfBoundsException If the index is not within the
		 * storage bounds.
		 * @since 2016/04/17
		 */
		public int setLongHigh(int __i, int __v)
			throws IndexOutOfBoundsException
		{
			// Lock
			synchronized (lock)
			{
				throw new Error("TODO");
			}
		}
	
		/**
		 * Sets the given low position to the given long value.
		 *
		 * @param __i The index to set.
		 * @param __v The value to set it as.
		 * @return The old value as an int, will be {@link Integer#MIN_VALUE}
		 * if not compatible with a int.
		 * @throws IndexOutOfBoundsException If the index is not within the
		 * storage bounds.
		 * @since 2016/04/17
		 */
		public int setLongLow(int __i, int __v)
			throws IndexOutOfBoundsException
		{
			// Lock
			synchronized (lock)
			{
				throw new Error("TODO");
			}
		}
	
		/**
		 * Sets the given position to the given object value.
		 *
		 * @param __i The index to set.
		 * @param __v The value to set it as.
		 * @return The old value as an object, will be {@code null}
		 * if not compatible with an object.
		 * @throws IndexOutOfBoundsException If the index is not within the
		 * storage bounds.
		 * @since 2016/04/17
		 */
		public JVMObject setObject(int __i, JVMObject __v)
			throws IndexOutOfBoundsException
		{
			// Lock
			synchronized (lock)
			{
				throw new Error("TODO");
			}
		}
		
		/**
		 * Returns the fragment size.
		 *
		 * @return The fragment size.
		 * @since 2016/04/17
		 */
		public int size()
		{
			return FRAGMENT_SIZE;
		}
	}
}

