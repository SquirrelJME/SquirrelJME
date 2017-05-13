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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.RandomAccess;
import java.util.Map;
import net.multiphasicapps.util.unmodifiable.UnmodifiableList;

/**
 * This contains the state of the stack and local variables.
 *
 * This class is immutable and must be initialized from an active state.
 *
 * @since 2017/02/16
 */
@Deprecated
public final class SnapshotCacheState
	extends CacheState
{
	/** Stack code variables. */
	protected final Tread stack;
	
	/** Local code variables. */
	protected final Tread locals;
	
	/** Work variables. */
	protected final Tread work;
	
	/** The size of the stack. */
	protected final int stacksize;
	
	/** String representation of this state. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the cache state which is a copy of the active state.
	 *
	 * @param __cs The code stream.
	 * @param __a The state to copy.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/23
	 */
	SnapshotCacheState(__Code__ __cs, CacheState __a)
		throws NullPointerException
	{
		super(__cs);
		
		// Check
		if (__a == null)
			throw new NullPointerException("NARG");
		
		// Copy locals and the stack
		this.locals = __snapTread(AreaType.LOCAL, __a.locals());
		this.stack = __snapTread(AreaType.STACK, __a.stack());
		this.work = __snapTread(AreaType.WORK, __a.work());
		
		// Needed for push/pop
		this.stacksize = __a.stackSize();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/18
	 */
	@Override
	public SnapshotCacheState.Tread locals()
	{
		return this.locals;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/18
	 */
	@Override
	public SnapshotCacheState.Tread stack()
	{
		return this.stack;
	}
	
	/**
	 * Returns the current size of the stack.
	 *
	 * @return The current stack size.
	 * @since 2017/04/26
	 */
	@Override
	public int stackSize()
	{
		return this.stacksize;
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
			this._string = new WeakReference<>((rv = super.toString()));
	
		return rv;
	}
	
	/**
	 * Returns the work tread, which is empty.
	 *
	 * @return The empty work tread.
	 * @since 2017/03/31
	 */
	public SnapshotCacheState.Tread work()
	{
		return this.work;
	}
	
	/**
	 * Snapshots the specified tread and returns it.
	 *
	 * @param __a The area type.
	 * @param __from The tread to copy.
	 * @return The copied and snapshotted tread.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/01
	 */
	private SnapshotCacheState.Tread __snapTread(AreaType __a,
		CacheState.Tread __from)
		throws NullPointerException
	{
		// Check
		if (__a == null || __from == null)
			throw new NullPointerException("NARG");
		
		// Setup copies of slots
		int n = __from.size();
		Slot[] slots = new Slot[n];
		for (int i = 0; i < n; i++)
			slots[i] = new Slot(__a, i, __from.get(i));
		
		// Initialize
		return new Tread(__a, slots);
	}
	
	/**
	 * This represents a slot within the cache state and stores the
	 * information 
	 *
	 * @since 2017/02/18
	 */
	public final class Slot
		extends CacheState.Slot
	{
		/** The type of value stored here. */
		protected final JavaType type;
		
		/** Aliased to the stack?. */
		protected final AreaType areaalias;
		
		/** Slot this is aliased to. */
		protected final int idalias;
		
		/** The allocation used. */
		protected final TypedAllocation alloc;
		
		/** String representation of this slot. */
		private volatile Reference<String> _string;
		
		/**
		 * Initializes the slot.
		 *
		 * @param __a The area this slot is in.
		 * @param __i The index.
		 * @param __from The source slot.
		 * @throws NullPointerException On null arguments.
		 * @since 2017/02/23
		 */
		private Slot(AreaType __a, int __i, CacheState.Slot __from)
			throws NullPointerException
		{
			super(__a, __i);
			
			// Check
			if (__from == null)
				throw new NullPointerException("NARG");
			
			// Copy fields
			this.type = __from.thisType();
			this.alloc = __from.thisAllocation();
			
			// Copy alias
			CacheState.Slot alias = __from.value();
			if (alias != __from)
			{
				this.areaalias = alias.thisArea();
				this.idalias = alias.thisIndex();
			}
			
			// Not aliased
			else
			{
				this.areaalias = null;
				this.idalias = -1;
			}
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/03/31
		 */
		@Override
		protected AreaType thisAliasedArea()
		{
			return this.areaalias;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/03/31
		 */
		@Override
		protected int thisAliasedIndex()
		{
			return this.idalias;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/03/22
		 */
		@Override
		public TypedAllocation thisAllocation()
		{
			return this.alloc;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/03/07
		 */
		@Override
		public Tread thisTread()
		{
			return (Tread)super.thisTread();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/03/01
		 */
		@Override
		public JavaType thisType()
		{
			return this.type;
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
		
		/**
		 * {@inheritDoc}
		 * @since 2017/03/06
		 */
		@Override
		public Slot value()
		{
			return (Slot)super.value();
		}
	}
	
	/**
	 * This is a tread of variables which stores cached state.
	 *
	 * @since 2017/02/18
	 */
	public final class Tread
		extends CacheState.Tread
	{
		/** Slots in this tread. */
		private final Slot[] _slots;
		
		/** String representation of this tread. */
		private volatile Reference<String> _string;
		
		/**
		 * Initializes the tread.
		 *
		 * @param __a The slot area type.
		 * @param __s The source slots, this is used directly.
		 * @throws NullPointerException On null arguments.
		 * @since 2017/02/23
		 */
		private Tread(AreaType __a, Slot[] __s)
			throws NullPointerException
		{
			super(__a);
			
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
		 * {@Override}
		 * @since 2017/03/07
		 */
		@Override
		public SnapshotCacheState state()
		{
			return SnapshotCacheState.this;
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

