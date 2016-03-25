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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This represents the state of a program through all of its operations and
 * defines operation chains and the type of values stored in local variable
 * and the stack.
 *
 * This is used for optimization.
 *
 * @since 2016/03/24
 */
public class JVMProgramState
	extends AbstractList<JVMProgramState.Atom>
{
	/** The comparator used for the binary sort to find atoms by address. */
	private static final Comparator<Object> _SEARCH_COMPARATOR =
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
					int a = ((__a instanceof Atom) ? ((Atom)__a).pcaddr :
						((Number)__a).intValue());
					int b = ((__b instanceof Atom) ? ((Atom)__b).pcaddr :
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
	protected final Object lock =
		new Object();
	
	/** Maximum locals. */
	protected final int maxlocal;
	
	/** Maximum stack. */
	protected final int maxstack;
	
	/** Program atoms, one for each operation in the program. */
	private final List<Atom> _atoms =
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
	public Atom get(int __i)
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
	public Atom get(int __i, boolean __create)
		throws IndexOutOfBoundsException
	{
		// Cannot be negative
		if (__i < 0)
			throw new IndexOutOfBoundsException(String.format("IOOB %d", __i));
		
		// Lock
		synchronized (lock)
		{
			// Get the atom list
			List<Atom> ll = _atoms;
			
			// Perform a binary search through the list
			int dx = Collections.<Object>binarySearch(ll, __i,
				_SEARCH_COMPARATOR);
			
			// Does not exist
			if (dx < 0)
			{
				// Do not create?
				if (!__create)
					return null;
				
				// Create new
				Atom rv = new Atom(__i);
				
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
			List<Atom> ll = _atoms;
			
			// If empty, return nothing
			int sz = ll.size();
			if (sz <= 0)
				return 0;
			
			// Otherwise return the highest address
			return ll.get(sz - 1).pcaddr + 1;
		}
	}
	
	/**
	 * This represents the state of a single operation as it appears in the
	 * program.
	 *
	 * @since 2016/03/24
	 */
	public class Atom
		implements Comparable<Atom>
	{
		/** The address of this operation. */
		protected final int pcaddr;
		
		/** Locals. */
		protected final Variables locals;
		
		/** Stack. */
		protected final Variables stack;
		
		/** The current array index. */
		private volatile int _index;
		
		/**
		 * Initializes the base of the atom.
		 *
		 * @param __pc The PC address of the atom.
		 * @since 2016/03/24
		 */
		private Atom(int __pc)
		{
			// Set address
			pcaddr = __pc;
			
			// Setup state
			locals = new Variables(this, false);
			stack = new Variables(this, true);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/03/24
		 */
		@Override
		public int compareTo(Atom __b)
			throws NullPointerException
		{
			// Check
			if (__b == null)
				throw new NullPointerException("NARG");
			
			// Check addresses
			int bpc = __b.pcaddr;
			if (pcaddr < bpc)
				return -1;
			else if (pcaddr > bpc)
				return 1;
			return 0;
		}
		
		/**
		 * Returns the address associated with this atom.
		 *
		 * @return The PC address.
		 * @since 2016/03/24
		 */
		public int getAddress()
		{
			return pcaddr;
		}
		
		/**
		 * Returns the local variables state.
		 *
		 * @return The state of local variables.
		 * @since 2016/03/25
		 */
		public Variables locals()
		{
			return locals;
		}
		
		/**
		 * Returns the atom which follows this one.
		 *
		 * @return The atom after this one or {@code null} if this is the last
		 * one.
		 * @since 2016/03/25
		 */
		public Atom next()
		{
			// Lock
			synchronized (lock)
			{
				// Get atoms
				List<Atom> ll = _atoms;
				int udx = _index + 1;
				
				// Must be within bounds
				if (udx >= ll.size())
					return null;
				return ll.get(udx);
			}
		}
		
		/**
		 * Returns the atom which this one follows.
		 *
		 * @return The atom before this one or {@code null} if this is the
		 * first one.
		 * @since 2016/03/25
		 */
		public Atom previous()
		{
			// Lock
			synchronized (lock)
			{
				// Get atoms
				List<Atom> ll = _atoms;
				int udx = _index - 1;
				
				// Must be within bounds
				if (udx < 0)
					return null;
				return ll.get(udx);
			}
		}
		
		/**
		 * Returns the stack variables state.
		 *
		 * @rteurn The state of stack variables.
		 * @since 2016/03/25
		 */
		public Variables stack()
		{
			return stack;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/03/24
		 */
		@Override
		public String toString()
		{
			// Build up
			StringBuilder sb = new StringBuilder();
			sb.append('{');
			
			// Add address
			sb.append("pc=");
			sb.append(pcaddr);
			
			// Locals
			sb.append(", locals=");
			sb.append(locals);
			
			// Stack
			sb.append(", stack=");
			sb.append(stack);
			
			// Finish it
			sb.append('}');
			return sb.toString();
		}
	}
	
	/**
	 * This represents the state of variables within an atom.
	 *
	 * @since 2016/03/25
	 */
	public class Variables
	{
		/** The owning atom. */
		protected final Atom atom;
		
		/** Is this the stack? */
		protected final boolean isstack;
		
		/**
		 * Initializes the local variable state.
		 *
		 * @param __a The owning atom.
		 * @param __stack Is this the stack?
		 * @throws NullPointerException On null arguments.
		 * @since 2016/03/24
		 */
		private Variables(Atom __a, boolean __stack)
			throws NullPointerException
		{
			// Check
			if (__a == null)
				throw new NullPointerException("NARG");
			
			atom = __a;
			isstack = __stack;
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
		 * Returns the number of elements in this variable table.
		 *
		 * @return The variable table size.
		 * @since 2016/03/25
		 */
		public int size()
		{
			return (isstack ? maxstack : maxlocal);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/03/25
		 */
		@Override
		public String toString()
		{
			throw new Error("TODO");
		}
	}
}

