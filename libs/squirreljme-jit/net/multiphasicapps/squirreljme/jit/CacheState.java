// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import java.util.List;
import java.util.RandomAccess;
import net.multiphasicapps.squirreljme.classformat.CodeVariable;
import net.multiphasicapps.squirreljme.classformat.StackMapType;

/**
 * This interface acts as the base for the mutable and immutable cache states.
 *
 * @since 2017/03/03
 */
public abstract class CacheState
{
	/**
	 * Base initialization.
	 *
	 * @since 2017/03/11
	 */
	CacheState()
	{
	}
	
	/**
	 * Returns the cached local variable assignments.
	 *
	 * @return The cached local variables.
	 * @since 2017/03/03
	 */
	public abstract Tread locals();
	
	/**
	 * Returns the cached stack variable assignments.
	 *
	 * @return The cached stack variables.
	 * @since 2017/03/03
	 */
	public abstract Tread stack();
	
	/**
	 * Returns the slot which is associated with the given variable.
	 *
	 * @param __cv The variable to get the slot of.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/03
	 */
	public Slot getSlot(CodeVariable __cv)
		throws NullPointerException
	{
		// Check
		if (__cv == null)
			throw new NullPointerException("NARG");
		
		// Depends
		int id = __cv.id();
		if (__cv.isStack())
			return this.stack.get(id);
		return this.locals.get(id);
	}
	
	/**
	 * Returns the slot which is associated with the given tread and index.
	 *
	 * @param __s Is the slot on the stack?
	 * @param __i The index of the slot.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/06
	 */
	public Slot getSlot(boolean __s, int __i)
		throws NullPointerException
	{
		if (__s)
			return this.stack.get(__i);
		return this.locals.get(__i);
	}
	
	/**
	 * This interface acts as the base for slots within the cache state.
	 *
	 * @since 2017/03/03
	 */
	public abstract class Slot
	{
		/**
		 * Initializes the base slot.
		 *
		 * @since 2017/03/11
		 */
		Slot()
		{
		}
		
		/**
		 * Returns the slot that this is aliased to or {@code null} if it
		 * is not aliased. If the slot is aliased to another which has an
		 * alias then it should follow and return that alias instead.
		 *
		 * @return The aliased slot or {@code null} if not aliased.
		 * @since 2017/03/03
		 */
		public abstract Slot alias();
		
		/**
		 * This returns the active binding for this slot.
		 *
		 * @param <B> The active binding type used.
		 * @param __cl The class to cast to.
		 * @return The active binding.
		 * @throws ClassCastException If the class type is not valid.
		 * @throws NullPointerException On null arguments.
		 * @since 2017/03/03
		 */
		public abstract <B extends Binding> B binding(Class<B> __cl)
			throws ClassCastException, NullPointerException;
		
		/**
		 * Returns the index of this slot.
		 *
		 * @return The slot index.
		 * @since 2017/03/03
		 */
		public abstract int index();
		
		/**
		 * Returns {@code true} if this is a local slot.
		 *
		 * @return {@code true} if a local slot.
		 * @since 2017/03/03
		 */
		public abstract boolean isLocal();
		
		/**
		 * Returns {@code true} if this is a stack slot.
		 *
		 * @return {@code true} if a stack slot.
		 * @since 2017/03/03
		 */
		public abstract boolean isStack();
		
		/**
		 * Returns the list of registers which are stored in this slot.
		 *
		 * @return the list of associated registers.
		 * @since 2017/03/11
		 */
		public abstract List<Register> registers();
		
		/**
		 * Returns the tread this slot is in.
		 *
		 * @return The tread this slot is in.
		 * @since 2017/03/07
		 */
		public abstract Tread tread();
		
		/**
		 * Returns the type of value that is stored here.
		 *
		 * @return The type of value to store.
		 * @since 2017/03/03
		 */
		public abstract StackMapType type();
		
		/**
		 * Returns the slot which contains the value for this slot, if the
		 * slot is aliased it will return the alias, otherwise this slot.
		 *
		 * @return The aliased slot or {@code this} if not aliased.
		 * @since 2017/03/06
		 */
		public abstract Slot value();
	}
	
	/**
	 * This represents a tread of local and stack variables.
	 *
	 * @since 2017/03/03
	 */
	public abstract class Tread
		implements RandomAccess
	{
		/**
		 * Initializes the base tread.
		 *
		 * @since 2017/03/11
		 */
		Tread()
		{
		}
		
		/**
		 * Gets the specified slot in this tread.
		 *
		 * @param __i The slot to get.
		 * @since 2017/03/03
		 */
		public abstract CacheState.Slot get(int __i);
		
		/**
		 * Returns the size of this tread.
		 *
		 * @return The tread size.
		 * @since 2017/03/06
		 */
		public abstract int size();
		
		/**
		 * Returns the state this tread is within.
		 *
		 * @return The owning state.
		 * @since 2017/03/07
		 */
		public abstract CacheState state();
	}
}

