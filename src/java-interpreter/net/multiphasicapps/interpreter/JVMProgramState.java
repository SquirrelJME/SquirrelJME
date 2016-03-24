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

import java.util.ArrayList;
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
{
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
	 * Gets the atom for the given address, if it does not exist then
	 * {@code null} is returned..
	 *
	 * @param __i The PC address to get the atom for.
	 * @return The atom for the given address, {@code null} if it does not
	 * exist.
	 * @throws IndexOutOfBoundsException If the index is negative.
	 * @since 2016/03/24
	 */
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
			
			throw new Error("TODO");
		}
	}
	
	/**
	 * This represents the state of a single operation as it appears in the
	 * program.
	 *
	 * @since 2016/03/24
	 */
	public class Atom
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
		 * Returns the address associated with this atom.
		 *
		 * @return The PC address.
		 * @since 2016/03/24
		 */
		public int getAddress()
		{
			return pcaddr;
		}
	}
}

