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
public class JVMProgramState
	extends AbstractList<JVMProgramState.Atom>
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
	
	/** The comparator used for the binary search to find slots by index. */
	private static final Comparator<Object> _SLOT_COMPARATOR =
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
					int a = ((__a instanceof Slot) ? ((Slot)__a).position :
						((Number)__a).intValue());
					int b = ((__b instanceof Slot) ? ((Slot)__b).position :
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
				_ATOM_COMPARATOR);
			
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
	 * This represents a local variable slot which is assigned to a specific
	 * location and may optionally change the pre-existing value.
	 *
	 * Slots are either purely virtual or actually exist.
	 *
	 * Slots may be used as keys within a map provide there are also not
	 * {@link Integer} keys, which would lead to undefined results. These slots
	 * compare to {@link Integer} except {@link Integer} does not compare to
	 * slots.
	 *
	 * @since 2016/03/25
	 */
	public class Slot
	{
		/** Owning variables for value diffing. */
		protected final Variables variables;
		
		/** The virtual position of this slot. */
		protected final int position;
		
		/** The logical position of this slot (if it is not virtual). */
		private volatile int _logpos =
			-1;
		
		/** The type this slot contains a value for. */
		private volatile JVMVariableType _type;
		
		/**
		 * Initializes the slot.
		 *
		 * @param __v The owning variables.
		 * @param __pos The position of this slot.
		 * @throws NullPointerException On null arguments.
		 * @since 2016/03/25
		 */
		private Slot(Variables __v, int __pos)
			throws NullPointerException
		{
			// Check
			if (__v == null)
				throw new NullPointerException("NARG");
			
			// Set
			variables = __v;
			position = __pos;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/03/25
		 */
		@Override
		public boolean equals(Object __o)
		{
			// If comparing against an integer, check the position to make sure
			// that it matches
			if (__o instanceof Integer)
				return position == ((Integer)__o).intValue();
			
			// Otherwise must be this only
			return __o == this;
		}
		
		/**
		 * Returns the type of value contained in this slot.
		 *
		 * If there is no value here, then this propogates up the slot mappings
		 * until a type is found. This should never return {@code null}.
		 *
		 * @return The value stored in this slot.
		 * @since 2016/03/25
		 */
		public JVMVariableType getType()
		{
			// Lock
			synchronized (lock)
			{
				// Loop until the start
				for (Slot s = this; s != null; s = s.previous())
				{
					// Only return if a type was actually set
					JVMVariableType rv = _type;
					if (rv != null)
						return rv;
				}
				
				// Not found, use an implied nothing
				return JVMVariableType.NOTHING;
			}
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/03/25
		 */
		@Override
		public int hashCode()
		{
			// To make map finding easier and allowing slots to be used as keys
			// if needed, the position is used.
			return position;
		}
		
		/**
		 * Returns the slot before this one.
		 *
		 * @return The previous slot or {@code null} if this is the first.
		 * @since 2016/03/25
		 */
		public Slot previous()
		{
			// Calculate previous position
			int prev = position - 1;
			
			// If out of bounds, stop
			if (prev < 0)
				return null;
			
			// Otherwise get the slot before this one
			return variables.get(prev);
		}
		
		/**
		 * Sets the type of variable this local or stack variable uses.
		 *
		 * @param __vt The variable type to set.
		 * @return {@code this}.
		 * @throws NullPointerException On null arguments.
		 * @since 2016/03/25
		 */
		public Slot setType(JVMVariableType __vt)
			throws NullPointerException
		{
			// Check
			if (__vt == null)
				throw new NullPointerException("NARG");
			
			// Lock
			synchronized (lock)
			{
				// The slot becomes very real now
				__makeLogical();
				
				// Set the type
				_type = __vt;
			}
			
			// Self
			return this;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/03/25
		 */
		@Override
		public String toString()
		{
			// Build string
			StringBuilder sb = new StringBuilder();
			sb.append('{');
			
			// Add position
			sb.append(position);
			
			// Add the ttype
			sb.append(':');
			sb.append(getType());
			
			// Finish
			sb.append('}');
			return sb.toString();
		}
		
		/**
		 * Makes this slot logical and places it into the list of slots used
		 * by the owning variables. This means that it will not go away and is
		 * no longer a virtually cached slot.
		 *
		 * @return {@code this}.
		 * @since 2016/03/25
		 */
		private Slot __makeLogical()
		{
			// Lock
			synchronized (lock)
			{
				// If already logical, ignore
				if (_logpos >= 0)
					return this;
				
				// Get the slots to insert into
				Variables vars = variables;
				List<Slot> act = vars._dslots;
				
				// Find the insertion point of this slot
				int pos = Collections.<Object>binarySearch(act, position,
					_SLOT_COMPARATOR);
				
				// Cannot be happening
				if (pos >= 0)
					throw new IllegalStateException(String.format("WTFX %d",
						pos));
				
				// Insert it there
				int at = (-pos) - 1;
				act.add(at, this);
				
				// Correct logical positions for everything
				int n = act.size();
				for (int i = at; i < n; i++)
					act.get(i)._logpos = i;
					
				// Destroy the reference because it is no longer needed
				vars._vslots.remove(this);
			}
			
			// Self
			return this;
		}
	}
	
	/**
	 * This represents the state of variables within an atom.
	 *
	 * @since 2016/03/25
	 */
	public class Variables
		extends AbstractList<Slot>
	{
		/** The owning atom. */
		protected final Atom atom;
		
		/** Is this the stack? */
		protected final boolean isstack;
		
		/** The currently active and defined slots with differences. */
		private final List<Slot> _dslots =
			new ArrayList<>();
		
		/** The slot cache, if applicable. */
		private final Map<Slot, Reference<Slot>> _vslots =
			new WeakHashMap<>();
		
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
		 * {@inheritDoc}
		 * @since 2016/03/25
		 */
		@Override
		public Slot get(int __i)
		{
			// Check
			if (__i < 0 || __i >= size())
				throw new IndexOutOfBoundsException(String.format("IOOB %d",
					__i));
			
			// Lock
			synchronized (lock)
			{
				// Get slots and such
				List<Slot> act = _dslots;
				Map<Slot, Reference<Slot>> map = _vslots;
				
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
				Reference<Slot> ref = map.get(prei);
				Slot rv;
				
				// Needs to be cached?
				if (ref == null || null == (rv = ref.get()))
				{
					rv = new Slot(this, __i);
					
					// Cache it
					map.put(rv, new WeakReference<>(rv));
				}
				
				// Return it
				return rv;
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
		 * {@inheritDoc}
		 * @since 2016/03/25
		 */
		@Override
		public int size()
		{
			return (isstack ? maxstack : maxlocal);
		}
	}
}

