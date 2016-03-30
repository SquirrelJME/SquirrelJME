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
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;

/**
 * This represents the state of variables within an atom.
 *
 * @since 2016/03/25
 */
@Deprecated
public class JVMProgramVars
	extends AbstractList<JVMProgramSlot>
{
	/** The comparator used for the binary search to find slots by index. */
	static final Comparator<Object> _SLOT_COMPARATOR =
		new Comparator<Object>()
			{
				/**
				 * {@inheritDoc}
				 * @since 2016/03/24
				 */
				@Override
				public int compare(Object __a, Object __b)
				{
					// Get the address of both items
					int a = ((__a instanceof JVMProgramSlot) ?
						((JVMProgramSlot)__a).position :
						((Number)__a).intValue());
					int b = ((__b instanceof JVMProgramSlot) ?
						((JVMProgramSlot)__b).position :
						((Number)__b).intValue());
					
					// Compare the addresses
					if (a < b)
						return -1;
					else if (a > b)
						return 1;
					return 0;
				}
			};
	
	/** Lock. */
	final Object lock;
	
	/** The owning atom. */
	protected final JVMProgramAtom atom;
	
	/** Is this the stack? */
	protected final boolean isstack;
	
	/** The maximum count of this. */
	protected final int maxcount;
	
	/** The currently active and defined slots with differences. */
	final List<JVMProgramSlot> _dslots =
		new ArrayList<>();
	
	/** The slot cache, if applicable. */
	final Map<JVMProgramSlot, Reference<JVMProgramSlot>> _vslots =
		new WeakHashMap<>();
	
	/** The top of the stack, if a stack. */
	volatile int _stacktop;
	
	/**
	 * Initializes the local variable state.
	 *
	 * @param __a The owning atom.
	 * @param __stack Is this the stack?
	 * @param __copy If not {@code null} then the entire state of the
	 * given variables is copied to this one.
	 * @throws NullPointerException If the owning atom was not specified.
	 * @since 2016/03/24
	 */
	JVMProgramVars(JVMProgramAtom __a, boolean __stack,
		JVMProgramVars __copy)
		throws NullPointerException
	{
		// Check
		if (__a == null)
			throw new NullPointerException("NARG");
		
		atom = __a;
		lock = atom.lock;
		isstack = __stack;
		maxcount = (isstack ? atom._programstate.maxStack() :
			atom._programstate.maxLocals());
		
		// Lock
		synchronized (lock)
		{
			// Copy the state?
			if (__copy != null)
				this.copyFrom(__copy);
		}
	}
	
	/**
	 * Copies from the given variable state into this one.
	 *
	 * @param __copy The variables to copy from.
	 * @return {@code this}.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/27
	 */
	public JVMProgramVars copyFrom(JVMProgramVars __copy)
		throws NullPointerException
	{
		// Check
		if (__copy == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (lock)
		{
			// Set stack top?
			if (isstack)
				setStackTop(__copy.getStackTop());
			
			// Copy lines
			int n = size();
			for (int i = 0; i < n; i++)
			{
				// Get both slots
				JVMProgramSlot m = get(i);
				JVMProgramSlot o = __copy.get(i);
				
				// Copy type
				m.setType(o.getType());
				
				// The operator link may be clearable
				JVMOperatorLink l = o.getLink();
				if (l == null)
					m.clearLink();
				else
					m.setLink(l);
			}
		}
		
		// Self
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/25
	 */
	@Override
	public JVMProgramSlot get(int __i)
	{
		// Check
		if (__i < 0 || __i >= size())
			throw new IndexOutOfBoundsException(String.format("IOOB %d",
				__i));
		
		// Lock
		synchronized (lock)
		{
			// Get slots and such
			List<JVMProgramSlot> act = _dslots;
			Map<JVMProgramSlot, Reference<JVMProgramSlot>> map = _vslots;
			
			// Could be an actual slot?
			Integer prei = Integer.valueOf(__i);
			if (act != null)
			{
				// Search for the slot for the given position
				int pos = Collections.<Object>binarySearch(act, prei,
					_SLOT_COMPARATOR);
				
				// Found?
				if (pos >= 0)
					return act.get(pos);
			}
			
			// Check to see if a virtual slot exists for it
			Reference<JVMProgramSlot> ref = map.get(prei);
			JVMProgramSlot rv;
			
			// Needs to be cached?
			if (ref == null || null == (rv = ref.get()))
			{
				rv = new JVMProgramSlot(this, __i);
				
				// Cache it
				map.put(rv, new WeakReference<>(rv));
			}
			
			// Return it
			return rv;
		}
	}
	
	/**
	 * Obtains the top of the stack.
	 *
	 * @return The top of the stack.
	 * @throws IllegalStateException If this is not a stack.
	 * @since 2016/03/26
	 */
	public int getStackTop()
		throws IllegalStateException
	{
		// If not a stack, fail
		if (!isstack)
			throw new IllegalStateException("IN1s");
		
		// Lock
		synchronized (lock)
		{
			return _stacktop;
		}
	}
	
	/**
	 * Does this represent the stack?
	 *
	 * @since 2016/03/25
	 */
	public boolean isStack()
	{
		return isstack;
	}
	
	/**
	 * Pops a slot from the stack and returns it.
	 *
	 * @return The entry which was at the top of the stack.
	 * @throws IllegalStateException If this is not a stack.
	 * @throws JVMVerifyException If this underflows the stack.
	 * @since 2016/03/29
	 */
	public JVMProgramSlot pop()
		throws IllegalStateException, JVMVerifyException
	{
		// If not a stack, fail
		if (!isstack)
			throw new IllegalStateException("IN1s");
		
		// Lock
		synchronized (lock)
		{
			// Get the slot at the top
			int top = _stacktop;
			JVMProgramSlot rv;
			try
			{
				rv = this.get(top - 1);
			}
			
			// Underflowed
			catch (IndexOutOfBoundsException e)
			{
				throw new JVMVerifyException("IN25", e);
			}
			
			// Set the new top
			setStackTop(top - 1);
			
			// Return the slot
			return rv;
		}
	}
	
	/**
	 * Pushes a slot to the stack and returns it, it is initialized to
	 * the {@link JVMVariableType#NOTHING} type with no link.
	 *
	 * @return The slot at the top of the stack.
	 * @throws IllegalStateException If this is not a stack.
	 * @throws JVMVerifyException If this overflows the stack.
	 * @since 2016/03/27
	 */
	public JVMProgramSlot push()
		throws IllegalStateException, JVMVerifyException
	{
		// If not a stack, fail
		if (!isstack)
			throw new IllegalStateException("IN1s");
		
		// Lock
		synchronized (lock)
		{
			// Get the slot at the top
			int top = _stacktop;
			JVMProgramSlot rv;
			try
			{
				rv = this.get(top);
			}
			
			// Overflowed
			catch (IndexOutOfBoundsException e)
			{
				throw new JVMVerifyException("IN24", e);
			}
			
			// Initialize to nothing
			rv.setType(JVMVariableType.NOTHING);
			rv.clearLink();
			
			// Set the new top
			setStackTop(top + 1);
			
			// Return the slot
			return rv;
		}
	}
	
	/**
	 * Sets the top of the stack.
	 *
	 * @param __s The new stack top to use.
	 * @return {@code this}.
	 * @throws IllegalStateException If this is not a stack.
	 * @throws JVMVerifyException If the top of the stack entry is
	 * not within bounds.
	 * @since 2016/03/26
	 */
	public JVMProgramVars setStackTop(int __s)
		throws IllegalStateException, JVMVerifyException
	{
		// If not a stack, fail
		if (!isstack)
			throw new IllegalStateException("IN1s");
		
		// Must be in bounds
		if (__s < 0 || __s > size())
			throw new JVMVerifyException(String.format("IOOB %d",
				__s));
		
		// Lock
		synchronized (lock)
		{
			_stacktop = __s;
		}
		
		// Self
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/25
	 */
	@Override
	public int size()
	{
		return maxcount;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/26
	 */
	@Override
	public String toString()
	{
		// If not a stack, use normal string stuff
		if (!isstack)
			return super.toString();
		
		// Otherwise printing must stop at the end of the stack, that is
		// the size is pretty much faked here.
		StringBuilder sb = new StringBuilder("[");
		int stoptop = _stacktop;
		
		// Go through all of them
		for (int i = 0; i < stoptop; i++)
		{
			// Comma?
			if (i > 0)
				sb.append(", ");
			
			// Add entry here
			sb.append(get(i));
		}
		
		// Done
		sb.append(']');
		return sb.toString();
	}
}

