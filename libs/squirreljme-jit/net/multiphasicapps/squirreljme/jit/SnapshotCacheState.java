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
import net.multiphasicapps.squirreljme.classformat.CodeVariable;
import net.multiphasicapps.squirreljme.classformat.StackMapType;
import net.multiphasicapps.util.unmodifiable.UnmodifiableList;

/**
 * This contains the state of the stack and local variables.
 *
 * This class is immutable and must be initialized from an active state.
 *
 * @since 2017/02/16
 */
public final class SnapshotCacheState
	extends CacheState
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
	 * @param __cs The code stream.
	 * @param __a The state to copy.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/23
	 */
	SnapshotCacheState(__JITCodeStream__ __cs, CacheState __a)
		throws NullPointerException
	{
		super(__cs);
		
		// Check
		if (__a == null)
			throw new NullPointerException("NARG");
		
		// Copy locals and the stack
		this.locals = __snapTread(__a.locals());
		this.stack = __snapTread(__a.stack());
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
			this._string = new WeakReference<>((rv = super.toString()));
	
		return rv;
	}
	
	/**
	 * Snapshots the specified tread and returns it.
	 *
	 * @param __from The tread to copy.
	 * @return The copied and snapshotted tread.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/01
	 */
	private SnapshotCacheState.Tread __snapTread(CacheState.Tread __from)
		throws NullPointerException
	{
		// Check
		if (__from == null)
			throw new NullPointerException("NARG");
		
		// Setup copies of slots
		int n = __from.size();
		Slot[] slots = new Slot[n];
		for (int i = 0; i < n; i++)
			slots[i] = new Slot(__from.get(i));
		
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
		extends CacheState.Slot
	{
		/** Is this the stack? */
		protected final boolean isstack;
		
		/** The index of this slot. */
		protected final int index;
		
		/** The type of value stored here. */
		protected final StackMapType type;
		
		/** Aliased to the stack?. */
		protected final boolean stackalias;
		
		/** Slot this is aliased to. */
		protected final int idalias;
		
		/** Registers used */
		private final Register[] _registers;
		
		/** Read-only registers. */
		private volatile Reference<List<Register>> _roregs;
		
		/** The allocation used. */
		private volatile Reference<ArgumentAllocation> _alloc;
		
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
		private Slot(CacheState.Slot __from)
			throws NullPointerException
		{
			// Check
			if (__from == null)
				throw new NullPointerException("NARG");
			
			// Copy fields
			this.isstack = __from.thisIsStack();
			this.index = __from.thisIndex();
			this.type = __from.thisType();
			
			// Copy Registers
			List<Register> fromregisters = __from.thisRegisters();
			int n = fromregisters.size();
			Register[] registers = new Register[n];
			for (int i = 0; i < n; i++)
				registers[i] = fromregisters.get(i);
			this._registers = registers;
			
			// Copy alias
			CacheState.Slot alias = __from.value();
			if (alias != __from)
			{
				this.stackalias = alias.thisIsStack();
				this.idalias = alias.thisIndex();
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
		 * @since 2017/03/22
		 */
		@Override
		public ArgumentAllocation thisAllocation(boolean __a)
		{
			Reference<ArgumentAllocation> ref = this._alloc;
			ArgumentAllocation rv;
			
			// Cache?
			if (ref == null || null == (rv = ref.get()))
				this._alloc = new WeakReference<>(
					(rv = super.thisAllocation(false)));
			
			return rv;
		}
		
		/**
		 * {inheritDoc}
		 * @since 2017/03/01
		 */
		@Override
		public int thisIndex()
		{
			return this.index;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/03/01
		 */
		@Override
		public boolean thisIsLocal()
		{
			return !this.isstack;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/03/01
		 */
		@Override
		public boolean thisIsStack()
		{
			return this.isstack;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/03/11
		 */
		@Override
		public List<Register> thisRegisters()
		{
			Reference<List<Register>> ref = this._roregs;
			List<Register> rv;
			
			// Cache?
			if (ref == null || null == (rv = ref.get()))
				this._roregs = new WeakReference<>((rv =
					UnmodifiableList.<Register>of(
					Arrays.<Register>asList(this._registers))));
			
			return rv;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/03/07
		 */
		@Override
		public Tread thisTread()
		{
			return (this.isstack ? SnapshotCacheState.this.stack :
				SnapshotCacheState.this.locals);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/03/01
		 */
		@Override
		public StackMapType thisType()
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
			for (Slot at = this;;)
			{
				// Aliased?
				int idalias = at.idalias;
				if (idalias < 0)
					if (at == this)
						return this;
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

