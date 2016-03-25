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
	
	/** Program atoms, one for each operation in the program. */
	private final List<Atom> _atoms =
		new ArrayList<>();
	
	/**
	 * Initializes the program state.
	 *
	 * @since 2016/03/24
	 */
	public JVMProgramState()
	{
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
				ll.add((-dx) - 1, rv);
				
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
		
		/**
		 * Initializes the base of the atom.
		 *
		 * @param __pc The PC address of the atom.
		 * @since 2016/03/24
		 */
		private Atom(int __pc)
		{
			pcaddr = __pc;
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
			
			// Finish it
			sb.append('}');
			return sb.toString();
		}
	}
}

