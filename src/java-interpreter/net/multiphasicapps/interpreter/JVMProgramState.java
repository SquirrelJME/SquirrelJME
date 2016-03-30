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
 * This represents the state of a program through all of its operations and
 * defines operation chains and the type of values stored in local variable
 * and the stack.
 *
 * This is used for optimization.
 *
 * @since 2016/03/24
 */
@Deprecated
public class JVMProgramState
	extends AbstractList<JVMProgramAtom>
{
	/** The comparator used for the binary search to find atoms by address. */
	private static final Comparator<Object> _ATOM_COMPARATOR =
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
					int a = ((__a instanceof JVMProgramAtom) ?
						((JVMProgramAtom)__a).pcaddr :
						((Number)__a).intValue());
					int b = ((__b instanceof JVMProgramAtom) ?
						((JVMProgramAtom)__b).pcaddr :
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
	final Object lock =
		new Object();
	
	/** Maximum locals. */
	protected final int maxlocal;
	
	/** Maximum stack. */
	protected final int maxstack;
	
	/** Program atoms, one for each operation in the program. */
	final List<JVMProgramAtom> _atoms =
		new ArrayList<>();
	
	/**
	 * Initializes the program state.
	 *
	 * @param __ml Max local values.
	 * @param __ms Max stack values.
	 * @since 2016/03/24
	 */
	public JVMProgramState(int __ml, int __ms)
	{
		// Check
		if (__ml < 0 || __ms <= 0)
			throw new IllegalArgumentException(String.format("IN1j %d %d",
				__ml, __ms));
		
		// Set
		maxlocal = __ml;
		maxstack = __ms;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/24
	 */
	@Override
	public JVMProgramAtom get(int __i)
		throws IndexOutOfBoundsException
	{
		return get(__i, false);
	}
	
	/**
	 * Gets the atom for the given address, if it does not exist then it may be
	 * created.
	 *
	 * @param __i The PC address to get the atom for.
	 * @param __create If {@code true} and the atom does not exist then it is
	 * created.
	 * @return The atom for the given address, {@code null} if it does not
	 * exist and {@link __create} is {@code false}.
	 * @throws IndexOutOfBoundsException If the index is negative.
	 * @since 2016/03/24
	 */
	public JVMProgramAtom get(int __i, boolean __create)
		throws IndexOutOfBoundsException
	{
		// Cannot be negative
		if (__i < 0)
			throw new IndexOutOfBoundsException(String.format("IOOB %d", __i));
		
		// Lock
		synchronized (lock)
		{
			// Get the atom list
			List<JVMProgramAtom> ll = _atoms;
			
			// Perform a binary search through the list
			int dx = Collections.<Object>binarySearch(ll, __i,
				_ATOM_COMPARATOR);
			
			// Does not exist
			if (dx < 0)
			{
				// Do not create?
				if (!__create)
					return null;
				
				// Create new
				JVMProgramAtom rv = new JVMProgramAtom(this, __i);
				
				// Insert it
				int at = (-dx) - 1;
				ll.add(at, rv);
				
				// Set and fix indexes
				int n = ll.size();
				for (int i = at; i < n; i++)
					ll.get(i)._index = i;
				
				// Return it
				return rv;
			}
			
			// Use it
			else
				return ll.get(dx);
		}
	}
	
	/**
	 * Obtains the spoecified slot at the given address from either the stack
	 * or the local variables.
	 *
	 * @param __i The PC address to get the atom for.
	 * @param __stack Is the desired slot on the stack?
	 * @param __s The slot index.
	 * @return The slot associated with the given inputs, or {@code null} if
	 * it does not exist and {@code __create} is {@code false}.
	 * @throws IndexOutOfBoundsException If the unique ID uses a slot which is
	 * not within the bounds of the local variabels or stack.
	 * @since 2016/03/26
	 */
	public JVMProgramSlot get(int __i, boolean __stack, int __s)
		throws IndexOutOfBoundsException
	{
		return get(__i, false, __stack, __s);
	}
	
	/**
	 * Obtains the spoecified slot at the given address from either the stack
	 * or the local variables. If the atom does not exist then it may
	 * optionally be created.
	 *
	 * @param __i The PC address to get the atom for.
	 * @param __create If {@code true}
	 * @param __stack Is the desired slot on the stack?
	 * @param __s The slot index.
	 * @return The slot associated with the given inputs, or {@code null} if
	 * it does not exist and {@code __create} is {@code false}.
	 * @throws IndexOutOfBoundsException If the unique ID uses a slot which is
	 * not within the bounds of the local variabels or stack.
	 * @since 2016/03/26
	 */
	public JVMProgramSlot get(int __i, boolean __create, boolean __stack,
		int __s)
		throws IndexOutOfBoundsException
	{
		// Get the atom
		JVMProgramAtom at = get(__i, __create);
		if (at == null)
			return null;
		
		// Stack or locals?
		JVMProgramVars xv = (__stack ? at.stack() : at.locals());
		
		// Get the slot there
		return xv.get(__s);
	}
	
	/**
	 * Returns the slot which is associated with the given unique ID.
	 *
	 * @param __u The unique slot ID to obtain.
	 * @return The slot which has the associated ID or {@code null} if there is
	 * no atom associated with the unique ID.
	 * @throws IndexOutOfBoundsException If the unique ID uses a slot which is
	 * not within the bounds of the local variabels or stack.
	 * @since 2016/03/26
	 */
	public JVMProgramSlot getUnique(long __u)
		throws IndexOutOfBoundsException
	{
		return getUnique(__u, false);
	}
	
	/**
	 * Returns the slot which is associated with the given unique ID and
	 * optionally may create the atom if it is missing.
	 *
	 * @param __u The unique slot ID to obtain.
	 * @param __create If {@code true} then if the atom is missing, it will be
	 * created.
	 * @return The slot which has the associated ID or {@code null} if there is
	 * no atom associated with the unique ID and {@code __create} is
	 * {@code false}.
	 * @throws IndexOutOfBoundsException If the unique ID uses a slot which is
	 * not within the bounds of the local variabels or stack.
	 * @since 2016/03/26
	 */
	public JVMProgramSlot getUnique(long __u, boolean __create)
		throws IndexOutOfBoundsException
	{
		return get(((int)(__u  >>> 32L)) & 0x7FFF_FFFF,
			__create, (0L != (__u & 0x8000_0000__0000_0000L)),
			(int)(__u & 0xFFFF_FFFFL));
	}
	
	/**
	 * Returns the maximum number of local variables.
	 *
	 * @return The local variable maximum.
	 * @since 2016/03/27
	 */
	public int maxLocals()
	{
		return maxlocal;
	}
	
	/**
	 * Returns the maximum number of stack variables.
	 *
	 * @return The stack variable maximum.
	 * @since 2016/03/27
	 */
	public int maxStack()
	{
		return maxstack;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/24
	 */
	@Override
	public int size()
	{
		// Lock
		synchronized (lock)
		{
			// Get the atom list
			List<JVMProgramAtom> ll = _atoms;
			
			// If empty, return nothing
			int sz = ll.size();
			if (sz <= 0)
				return 0;
			
			// Otherwise return the highest address
			return ll.get(sz - 1).pcaddr + 1;
		}
	}
}

