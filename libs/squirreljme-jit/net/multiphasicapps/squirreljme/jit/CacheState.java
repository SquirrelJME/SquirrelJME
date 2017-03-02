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

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
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
	
	/** String representation of this state. */
	private volatile Reference<String> _string;
	
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
		this.locals = __snapTread(__e, __a.locals());
		this.stack = __snapTread(__e, __a.stack());
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
		Reference<String> ref = this._string;
		String rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>((rv = String.format(
				"{stack=%s, locals=%s}", this.stack, this.locals)));
		
		return rv;
	}
	
	/**
	 * Snapshots the specified tread and returns it.
	 *
	 * @param __e The translation engine.
	 * @param __from The tread to copy.
	 * @return The copied and snapshotted tread.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/01
	 */
	private CacheState.Tread __snapTread(TranslationEngine __e,
		ActiveCacheState.Tread __from)
		throws NullPointerException
	{
		// Check
		if (__from == null)
			throw new NullPointerException("NARG");
		
		// Setup copies of slots
		int n = __from.size();
		Slot[] slots = new Slot[n];
		for (int i = 0; i < n; i++)
			slots[i] = new Slot(__e, __from.get(i));
		
		// Initialize
		return new Tread(slots);
	}
	
	/**
	 * This represents a slot within the cache state and stores the
	 * information 
	 *
	 * @since 2017/02/18
	 */
	public final class Slot
	{
		/** Is this the stack? */
		protected final boolean isstack;
		
		/** The index of this slot. */
		protected final int index;
		
		/** The binding for this slot. */
		protected final Binding binding;
		
		/** The type of value stored here. */
		protected final StackMapType type;
		
		/** Aliased to the stack?. */
		protected final boolean stackalias;
		
		/** Slot this is aliased to. */
		protected final int idalias;
		
		/** String representation of this slot. */
		private volatile Reference<String> _string;
		
		/**
		 * Initializes the slot.
		 *
		 * @param __e The translation engine used.
		 * @param __from The source slot.
		 * @throws NullPointerException On null arguments.
		 * @since 2017/02/23
		 */
		private Slot(TranslationEngine __e, ActiveCacheState.Slot __from)
			throws NullPointerException
		{
			// Check
			if (__e == null || __from == null)
				throw new NullPointerException("NARG");
			
			// Copy fields
			this.isstack = __from.isStack();
			this.index = __from.index();
			this.binding = __e.snapshotBinding(
				__from.<ActiveBinding>binding(ActiveBinding.class));
			this.type = __from.type();
			
			// Aliased?
			ActiveCacheState.Slot alias = __from.alias();
			if (alias != null)
			{
				this.stackalias = alias.isStack();
				this.idalias = alias.index();
			}
			
			// Not aliased
			else
			{
				this.stackalias = false;
				this.idalias = -1;
			}
		}
		
		/**
		 * Returns the slot that this is aliased to or {@code null} if it
		 * is not aliased.
		 *
		 * @return The aliased slot or {@code null} if not aliased.
		 * @since 2017/03/01
		 */
		public Slot alias()
		{
			// Aliased?
			int idalias = this.idalias;
			if (idalias < 0)
				return null;
			
			// In that tread
			return (this.stackalias ? CacheState.this.stack :
				CacheState.this.locals).get(idalias);
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
		public <B extends Binding> B binding(Class<B> __cl)
			throws ClassCastException, NullPointerException
		{
			// Check
			if (__cl == null)
				throw new NullPointerException("NARG");
			
			return __cl.cast(this.binding);
		}
		
		/**
		 * Returns the index of this slot.
		 *
		 * @return The slot index.
		 * @since 2017/03/01
		 */
		public int index()
		{
			return this.index;
		}
		
		/**
		 * Returns {@code true} if this is a local slot.
		 *
		 * @return {@code true} if a local slot.
		 * @since 2017/03/01
		 */
		public boolean isLocal()
		{
			return !this.isstack;
		}
		
		/**
		 * Returns {@code true} if this is a stack slot.
		 *
		 * @return {@code true} if a stack slot.
		 * @since 2017/03/01
		 */
		public boolean isStack()
		{
			return this.isstack;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/03/01
		 */
		@Override
		public String toString()
		{
			Reference<String> ref = this._string;
			String rv;
		
			// Cache?
			if (ref == null || null == (rv = ref.get()))
			{
				int idalias = this.idalias;
				String alias = (idalias >= 0 ? String.format("copied=%c#%d",
					(this.stackalias ? 'S' : 'L'), idalias) : "not aliased");
				this._string = new WeakReference<>((rv = String.format(
					"{%c#%d: type=%s, binding=%s, %s}",
					(this.isstack ? 'S' : 'L'),
					this.index, this.type, this.binding, alias)));
			}
		
			return rv;
		}
		
		/**
		 * Returns the type of value that is stored here.
		 *
		 * @return The type of value to store.
		 * @since 2017/03/01
		 */
		public StackMapType type()
		{
			return this.type;
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
		/** Slots in this tread. */
		private final Slot[] _slots;
		
		/** String representation of this tread. */
		private volatile Reference<String> _string;
		
		/**
		 * Initializes the tread.
		 *
		 * @param __s The source slots, this is used directly.
		 * @throws NullPointerException On null arguments.
		 * @since 2017/02/23
		 */
		private Tread(Slot[] __s)
			throws NullPointerException
		{
			// Check
			if (__s == null)
				throw new NullPointerException("NARG");
			
			// Set
			this._slots = __s;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/02/18
		 */
		@Override
		public Slot get(int __i)
		{
			return this._slots[__i];
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/02/18
		 */
		@Override
		public int size()
		{
			return this._slots.length;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/03/01
		 */
		@Override
		public String toString()
		{
			Reference<String> ref = this._string;
			String rv;
		
			// Cache?
			if (ref == null || null == (rv = ref.get()))
				this._string = new WeakReference<>((rv = super.toString()));
		
			return rv;
		}
	}
}

