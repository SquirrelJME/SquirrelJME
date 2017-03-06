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

/**
 * This contains the state of the stack and local variables.
 *
 * This class is immutable and must be initialized from an active state.
 *
 * @since 2017/02/16
 */
public final class SnapshotCacheState
	implements CacheState
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
	 * @param __a The state to copy.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/23
	 */
	SnapshotCacheState(TranslationEngine __e, CacheState __a)
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
	public SnapshotCacheState.Tread locals()
	{
		return this.locals;
	}
	
	/**
	 * Returns the cached stack variable assignments.
	 *
	 * @return The cached stack variables.
	 * @since 2017/02/18
	 */
	public SnapshotCacheState.Tread stack()
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
	private SnapshotCacheState.Tread __snapTread(TranslationEngine __e,
		CacheState.Tread __from)
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
		implements CacheState.Slot
	{
		/** Is this the stack? */
		protected final boolean isstack;
		
		/** The index of this slot. */
		protected final int index;
		
		/** The binding for this slot. */
		protected final SnapshotBinding binding;
		
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
		private Slot(TranslationEngine __e, CacheState.Slot __from)
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
			CacheState.Slot alias = __from.alias();
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
		 * {@inheritDoc}
		 * @since 2017/03/01
		 */
		@Override
		public Slot alias()
		{
			for (Slot at = this;;)
			{
				// Aliased?
				int idalias = at.idalias;
				if (idalias < 0)
					if (at == this)
						return null;
					else
						return at;
			
				// {@squirreljme.error ED0c Slot eventually references itself.
				// (This slot)}
				at = (at.stackalias ? SnapshotCacheState.this.stack :
					SnapshotCacheState.this.locals).get(idalias);
				if (at == this)
					throw new IllegalStateException(String.format("ED0c %s",
						this));
			}
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/03/01
		 */
		@Override
		public <B extends Binding> B binding(Class<B> __cl)
			throws ClassCastException, NullPointerException
		{
			// Check
			if (__cl == null)
				throw new NullPointerException("NARG");
			
			// Use the aliased binding
			Slot alias = alias();
			if (alias != null)
				return alias.<B>binding(__cl);
			
			// Otherwise, use our own binding
			return __cl.cast(this.binding);
		}
		
		/**
		 * {inheritDoc}
		 * @since 2017/03/01
		 */
		@Override
		public int index()
		{
			return this.index;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/03/01
		 */
		@Override
		public boolean isLocal()
		{
			return !this.isstack;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/03/01
		 */
		@Override
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
					this.index, type(), binding(Binding.class), alias)));
			}
		
			return rv;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/03/01
		 */
		@Override
		public StackMapType type()
		{
			// Use aliased yype
			Slot alias = alias();
			if (alias != null)
				return alias.type();
			
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
		implements CacheState.Tread, RandomAccess
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

