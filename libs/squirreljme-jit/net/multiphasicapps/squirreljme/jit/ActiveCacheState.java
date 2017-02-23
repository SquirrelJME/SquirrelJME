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
		
		throw new Error("TODO");
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
		
		throw new Error("TODO");
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
		/**
		 * Initializes the slot.
		 *
		 * @since 2017/02/23
		 */
		private Slot()
		{
			throw new Error("TODO");
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
			throw new Error("TODO");
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
		/**
		 * Initializes the tread.
		 *
		 * @param __n The number of slots.
		 * @since 2017/02/23
		 */
		private Tread(int __n)
		{
			throw new Error("TODO");
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/02/23
		 */
		@Override
		public Slot get(int __i)
		{
			throw new Error("TODO");
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/02/23
		 */
		@Override
		public int size()
		{
			throw new Error("TODO");
		}
	}
}

