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
import java.util.HashMap;
import java.util.List;
import java.util.RandomAccess;
import java.util.Map;
import net.multiphasicapps.squirreljme.classformat.CodeVariable;
import net.multiphasicapps.squirreljme.classformat.StackMapType;
import net.multiphasicapps.util.sorted.SortedTreeMap;

/**
 * This contains the state of the stack and local variables.
 *
 * This class is immutable and must be initialized from an active state.
 *
 * @since 2017/02/16
 */
public final class CacheState
{
	/** Stack code variables. */
	protected final Tread stack;
	
	/** Local code variables. */
	protected final Tread locals;
	
	/**
	 * Initializes the cache state which is a copy of the active state.
	 *
	 * @param __e The translation engine being used.
	 * @param __a The active state to copy.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/23
	 */
	CacheState(TranslationEngine __e, ActiveCacheState __a)
		throws NullPointerException
	{
		// Check
		if (__e == null || __a == null)
			throw new NullPointerException("NARG");
		
		// Copy locals and the stack
		this.locals = __snapTread(__a.locals());
		this.stack = __snapTread(__a.stack());
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
		
		throw new todo.TODO();
	}
	
	/**
	 * Returns the cached local variable assignments.
	 *
	 * @return The cached local variables.
	 * @since 2017/02/18
	 */
	public CacheState.Tread locals()
	{
		return this.locals;
	}
	
	/**
	 * Returns the cached stack variable assignments.
	 *
	 * @return The cached stack variables.
	 * @since 2017/02/18
	 */
	public CacheState.Tread stack()
	{
		return this.stack;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/20
	 */
	@Override
	public String toString()
	{
		return String.format("{stack=%s, locals=%s}", this.stack, this.locals);
	}
	
	/**
	 * Snapshots the specified tread and returns it.
	 *
	 * @param __from The tread to copy.
	 * @return The copied and snapshotted tread.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/01
	 */
	private static CacheState.Tread __snapTread(ActiveCacheState.Tread __from)
		throws NullPointerException
	{
		// Check
		if (__from == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * This represents a slot within the cache state and stores the
	 * information 
	 *
	 * @since 2017/02/18
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
		}
		
		/**
		 * Returns the binding as the specified class.
		 *
		 * @param <B> The class to cast to.
		 * @param __cl The class to cast to.
		 * @return The binding for this slot.
		 * @throws ClassCastException If the class type is incorrect.
		 * @since 2017/03/01
		 */
		public <B extends Binding> B getBinding(Class<B> __cl)
			throws ClassCastException, NullPointerException
		{
			// Check
			if (__cl == null)
				throw new NullPointerException("NARG");
			
			throw new Error("TODO");
		}
	}
	
	/**
	 * This is a tread of variables which stores cached state.
	 *
	 * @since 2017/02/18
	 */
	public final class Tread
		extends AbstractList<Slot>
		implements RandomAccess
	{
		/**
		 * Initializes the tread.
		 *
		 * @param __n The number of tread elements.
		 * @since 2017/02/23
		 */
		private Tread(int __n)
		{
			throw new todo.TODO();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/02/18
		 */
		@Override
		public Slot get(int __i)
		{
			throw new todo.TODO();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/02/18
		 */
		@Override
		public int size()
		{
			throw new todo.TODO();
		}
	}
}

