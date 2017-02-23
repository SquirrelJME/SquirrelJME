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

import java.util.AbstractList;
import java.util.RandomAccess;
import net.multiphasicapps.squirreljme.classformat.CodeVariable;

/**
 * This represents the currently active cache state which is used to store
 * bindings.
 *
 * @since 2017/02/23
 */
public final class ActiveCacheState
{
	/** Stack code variables. */
	protected final Tread stack;
	
	/** Local code variables. */
	protected final Tread locals;
	
	/**
	 * Initializes the active cache state which stores the current state
	 * information.
	 *
	 * @param __te The translation engine being used.
	 * @param __ms The number of stack entries.
	 * @param __ml The number of local entries.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/23
	 */
	ActiveCacheState(TranslationEngine __te, int __ms, int __ml)
		throws NullPointerException
	{
		// Check
		if (__te == null)
			throw new NullPointerException("NARG");
		
		// Setup treads
		this.stack = new Tread(__te, true, __ms);
		this.locals = new Tread(__te, false, __ml);
	}
	
	/**
	 * Returns the slot which is associated with the given variable.
	 *
	 * @param __cv The variable to get the slot of.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/23
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
	 * Returns the cached local variable assignments.
	 *
	 * @return The cached local variables.
	 * @since 2017/02/23
	 */
	public ActiveCacheState.Tread locals()
	{
		return this.locals;
	}
	
	/**
	 * Returns the cached stack variable assignments.
	 *
	 * @return The cached stack variables.
	 * @since 2017/02/23
	 */
	public ActiveCacheState.Tread stack()
	{
		return this.stack;
	}
	
	/**
	 * Switches to the specified cache state.
	 *
	 * @param __cs The state to set.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/23
	 */
	public void switchFrom(CacheState __cs)
		throws NullPointerException
	{
		// Check
		if (__cs == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * The slot stores the information on the current element.
	 *
	 * @since 2017/02/23
	 */
	public final class Slot
	{
		/** Is this the stack? */
		protected final boolean isstack;
		
		/** The index of this slot. */
		protected final int index;
		
		/** The active binding. */
		protected final ActiveBinding binding;
		
		/**
		 * Initializes the slot.
		 *
		 * @param __te The translation engine (used to obtain bindings).
		 * @param __stack If {@code true} then these values are on the stack.
		 * @param __n The number of slots.
		 * @throws NullPointerException On null arguments.
		 * @since 2017/02/23
		 */
		private Slot(TranslationEngine __te, boolean __stack, int __i)
			throws NullPointerException
		{
			// Check
			if (__te == null)
				throw new NullPointerException("NARG");
			
			// Set
			this.isstack = __stack;
			this.index = __i;
			
			// Setup binding
			this.binding = __te.createActiveBinding();
		}
		
		/**
		 * This returns the active binding for this slot.
		 *
		 * @param <B> The active binding type used.
		 * @param __cl The class to cast to.
		 * @return The active binding.
		 * @throws ClassCastException If the class type is not valid.
		 * @throws NullPointerException On null arguments.
		 * @since 2017/02/23
		 */
		public <B extends ActiveBinding> B binding(Class<B> __cl)
			throws ClassCastException, NullPointerException
		{
			// Check
			if (__cl == null)
				throw new NullPointerException("NARG");
			
			// Cast
			return __cl.cast(this.binding);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/02/23
		 */
		@Override
		public String toString()
		{
			throw new Error("TODO");
		}
	}
	
	/**
	 * This is used to represent an active tread.
	 *
	 * @since 2017/02/23
	 */
	public final class Tread
		extends AbstractList<Slot>
		implements RandomAccess
	{
		/** Slots. */
		private final Slot[] _slots;
		
		/**
		 * Initializes the tread.
		 *
		 * @param __te The translation engine (used to obtain bindings).
		 * @param __stack If {@code true} then these values are on the stack.
		 * @param __n The number of slots.
		 * @throws NullPointerException On null arguments.
		 * @since 2017/02/23
		 */
		private Tread(TranslationEngine __te, boolean __stack, int __n)
			throws NullPointerException
		{
			// Check
			if (__te == null)
				throw new NullPointerException("NARG");
			
			// Initialize slots
			Slot[] slots = new Slot[__n];
			for (int i = 0; i < __n; i++)
				slots[i] = new Slot(__te, __stack, i);
			this._slots = slots;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/02/23
		 */
		@Override
		public Slot get(int __i)
		{
			return this._slots[__i];
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/02/23
		 */
		@Override
		public int size()
		{
			return this._slots.length;
		}
	}
}

