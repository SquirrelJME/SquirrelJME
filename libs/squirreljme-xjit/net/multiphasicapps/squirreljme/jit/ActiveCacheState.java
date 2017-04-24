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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.RandomAccess;
import java.util.Set;
import net.multiphasicapps.util.msd.MultiSetDeque;
import net.multiphasicapps.util.sorted.SortedTreeSet;
import net.multiphasicapps.util.unmodifiable.UnmodifiableList;

/**
 * This represents the currently active cache state which is used to store
 * bindings.
 *
 * @since 2017/02/23
 */
public final class ActiveCacheState
	extends CacheState
{
	/** Comparator for aliased by slots. */
	private static final Comparator<ActiveCacheState.Slot> _SLOT_COMPARATOR =
		new Comparator<ActiveCacheState.Slot>()
		{
			/**
			 * {@inheritDoc}
			 * @since 2017/03/25
			 */
			@Override
			public int compare(ActiveCacheState.Slot __a,
				ActiveCacheState.Slot __b)
			{
				// Area first
				int rv = __a.thisArea().ordinal() - __b.thisArea().ordinal();
				if (rv != 0)
					return rv;
				
				// Then the index
				return __a.thisIndex() - __b.thisIndex();
			}
		};
	
	/** Stack code variables. */
	protected final Tread stack;
	
	/** Local code variables. */
	protected final Tread locals;
	
	/** The working tread. */
	protected final Tread work;
	
	/** Registers available for allocation. */
	protected final MultiSetDeque<Register> foralloc =
		new MultiSetDeque<>();
	
	/** Saved integer registers available for allocation. */
	protected final Deque<Register> savedint =
		this.foralloc.subDeque();
	
	/** Saved float registers available for allocation. */
	protected final Deque<Register> savedfloat =
		this.foralloc.subDeque();
	
	/** Temporary integer registers available for allocation. */
	protected final Deque<Register> tempint =
		this.foralloc.subDeque();
	
	/** Temporary float registers available for allocation. */
	protected final Deque<Register> tempfloat =
		this.foralloc.subDeque();
	
	/** The register dictionary. */
	protected final RegisterDictionary rdict;
	
	/** Saved registers available for allocation. */
	private final Set<Register> _availsaved;
	
	/** Temporary registers available for allocation. */
	private final Set<Register> _availtemp;
	
	/**
	 * Initializes the active cache state which stores the current state
	 * information.
	 *
	 * @param __te The code stream
	 * @param __ms The number of stack entries.
	 * @param __ml The number of local entries.
	 * @param __conf The JIT configuration.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/23
	 */
	ActiveCacheState(__Code__ __cs, int __ms, int __ml,
		JITConfig __conf)
		throws NullPointerException
	{
		super(__cs);
		
		// Check
		if (__conf == null)
			throw new NullPointerException("NARG");
		
		// Setup treads
		this.stack = new Tread(AreaType.STACK, __ms);
		this.locals = new Tread(AreaType.LOCAL, __ml);
		this.work = new Tread(AreaType.WORK, __Code__._WORK_COUNT);
		
		// Initialize dictionary
		RegisterDictionary rdict = __conf.registerDictionary();
		this.rdict = rdict;
		
		// Available register sets
		this._availsaved = rdict.bothAllocationRegisters(true);
		this._availtemp = rdict.bothAllocationRegisters(false);
		
		// Initialize register deque
		__initDeque();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/03
	 */
	@Override
	public Slot getSlot(CodeVariable __cv)
		throws NullPointerException
	{
		return (Slot)super.getSlot(__cv);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/03
	 */
	@Override
	public Slot getSlot(AreaType __s, int __i)
		throws NullPointerException
	{
		return (Slot)super.getSlot(__s, __i);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/31
	 */
	@Override
	public Tread getTread(AreaType __a)
	{
		return (Tread)super.getTread(__a);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/23
	 */
	@Override
	public ActiveCacheState.Tread locals()
	{
		return this.locals;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/23
	 */
	@Override
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
		
		// Initialize the deques for allocation
		__initDeque();
		
		// Restore state
		Tread stack = this.stack,
			locals = this.locals,
			work = this.work;
		stack.__switchFrom(__cs.stack());
		locals.__switchFrom(__cs.locals());
		work.__switchFrom(__cs.work());
		
		// Correct aliased by
		stack.__fixAliasedBy();
		locals.__fixAliasedBy();
		work.__fixAliasedBy();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/25
	 */
	@Override
	public String toString()
	{
		// Start with the parent state
		StringBuilder sb = new StringBuilder("{state=");
		sb.append(super.toString());
		
		// Then add the remaining registers
		sb.append(", free=");
		sb.append(this.foralloc);
		
		// Finish
		sb.append('}');
		return sb.toString();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/31
	 */
	@Override
	public Tread work()
	{
		return this.work;
	}
	
	/**
	 * Initializes the register dequeues.
	 *
	 * @sine 2017/03/25
	 */
	private void __initDeque()
	{
		// Remove all allocations
		this.foralloc.clear();
		
		// Saved the temporary
		Deque<Register> toint, tofloat;
		for (int z = 0; z < 2; z++)
		{
			// Saved
			Set<Register> source;
			if (z == 0)
			{
				source = this._availsaved;
				toint = this.savedint;
				tofloat = this.savedfloat;
			}
			
			// Temporary
			else
			{
				source = this._availtemp;
				toint = this.tempint;
				tofloat = this.tempfloat;
			}
			
			// Fill
			for (Register r : source)
			{
				if (r.isInteger())
					toint.offerLast(r);
				if (r.isFloat())
					tofloat.offerLast(r);
			}
		}
	}
	
	/**
	 * The slot stores the information on the current element.
	 *
	 * @since 2017/02/23
	 */
	public final class Slot
		extends CacheState.Slot
	{
		/** List of registers used. */
		@Deprecated
		private final List<Register> _registers =
			new ArrayList<>();
		
		/** Slots which alias this slot. */
		private final Set<Slot> _aliasedby =
			new SortedTreeSet<>(_SLOT_COMPARATOR);
		
		/** The type of value stored here. */
		private volatile JavaType _type =
			JavaType.NOTHING;
		
		/** Aliased to the stack?. */
		private volatile AreaType _atalias;
		
		/** Slot this is aliased to. */
		private volatile int _idalias =
			-1;
		
		/** Unmodifiable registers. */
		private volatile Reference<List<Register>> _umregs;
		
		/**
		 * Initializes the slot.
		 *
		 * @param __stack The area this slot is in.
		 * @param __i The index of this slot.
		 * @throws NullPointerException On null arguments.
		 * @since 2017/02/23
		 */
		private Slot(AreaType __a, int __i)
			throws NullPointerException
		{
			super(__a, __i);
		}
		
		/**
		 * Clears the used registers.
		 *
		 * @since 2017/03/12
		 */
		public void clearRegisters()
		{
			this._registers.clear();
		}
		
		/**
		 * Forces the slot to have the given allocation and the given type.
		 *
		 * @param __aa The allocation to use for this slot. If the type is
		 * {@link JavaType#TOP} then this must be {@code null}.
		 * @param __t The Java type to store in this slot, this cannot be null.
		 * would cause an invalid state to be set.
		 * @throws JITException If the allocation cannot be set because it
		 * would not be valid.
		 * @throws NullPointerException The Java type to store in this slot.
		 * @since 2017/04/24
		 */
		public void forceAllocation(TypedAllocation __aa, JavaType __t)
			throws JITException, NullPointerException
		{
			// Check
			if (__t == null)
				throw new NullPointerException("NARG");
			
			// {@squirreljme.error AQ1x The top type cannot have an allocation
			// specified for it. (The allocation)}
			if (__t == JavaType.TOP && __aa != null)
				throw new JITException(String.format("AQ1x %s", __aa));
			
			// {@squirreljme.error AQ1y The specified type cannot be set on
			// a slot. (The type)}
			else if (!__t.isValid() && !(__t == JavaType.TOP && __aa == null))
				throw new JITException(String.format("AQ1y %s", __t));
			
			throw new todo.TODO();
		}
		
		/**
		 * Removes the value contained within this slot.
		 *
		 * If other slots alias this slot then they will lose their value and
		 * become copies.
		 *
		 * @since 2017/03/25
		 */
		public void remove()
		{
			// Do nothing if there is no value here
			if (thisType() == JavaType.NOTHING)
				return;
			
			// Remove aliases to this slot
			__deAliasFromThis();
			
			// Clear registers if not aliased
			if (!isAliased())
				__clearRegisters();
			
			// Otherwise remove alias links for this slot
			else
				value()._aliasedby.remove(this);
			
			// Clear other info
			this._type = JavaType.NOTHING;
			this._atalias = null;
			this._idalias = -1;
		}
		
		/**
		 * Aliases the value of this slot to another slot.
		 *
		 * @param __cv The slot to alias to.
		 * @throws IllegalArgumentException If the slot is not valid.
		 * @throws NullPointerException On null arguments.
		 * @since 2017/03/01
		 */
		public void setAlias(CodeVariable __cv)
			throws IllegalArgumentException, NullPointerException
		{
			// Check
			if (__cv == null)
				throw new NullPointerException("NARG");
			
			// Set
			setAlias(__cv.area(), __cv.id());
		}
		
		/**
		 * Aliases the value of this slot to another slot.
		 *
		 * @param __a The area to alias to.
		 * @param __id The slot index this is aliased to.
		 * @throws IllegalArgumentException If the slot is not valid.
		 * @throws NullPointerException On null arguments.
		 * @since 2017/03/01
		 */
		@Deprecated
		public void setAlias(AreaType __a, int __id)
			throws IllegalArgumentException, NullPointerException
		{
			throw new todo.TODO();
			/*
			// Check
			if (__a == null)
				throw new NullPointerException("NARG");
			
			// Do not have recursive aliases
			Slot target = getSlot(__a, __id).value();
			
			// {@squirreljme.error AQ1m Cannot alias slot to self. (This slot)}
			if (target == this)
				throw new JITException(String.format("AQ1m %s", this));
			
			// {@squirreljme.error AQ1n Local or work variables cannot alias
			// other slots. (This slot; The target slot)}
			AreaType myarea = thisArea();
			if (myarea == AreaType.LOCAL || myarea == AreaType.WORK)
				throw new IllegalArgumentException(String.format("AQ1n %s %s",
					this, __id));
			
			// {@squirreljme.error AQ1o Cannot alias the current slot to the
			// target slot becuase the target has no set value. (This slot; The
			// target slot)}
			if (target.thisType() == JavaType.NOTHING)
				throw new JITException(String.format("AQ1o %s %s", this,
					target));
			
			// Remove any information in this slot
			remove();
			
			// Set
			this._atalias = target.thisArea();
			this._idalias = target.thisIndex();
			*/
		}
		
		/**
		 * Sets the registers that the slot allocates to.
		 *
		 * Lower indexes are the least significant values.
		 *
		 * @param __r The registers to set the slot.
		 * @throws NullPointerException On null arguments.
		 * @since 2017/03/11
		 */
		@Deprecated
		public void setRegisters(Register... __r)
			throws NullPointerException
		{
			throw new todo.TODO();
			
			/*
			// Check
			if (__r == null)
				throw new NullPointerException("NARG");
			
			// {@squirreljme.error AQ1p Cannot set registers for aliased
			// slots.}
			if (isAliased())
				throw new JITException("AQ1p");
			
			// Copy
			__clearRegisters();
			for (Register r : __r)
				__addRegister(r);
			*/
		}
		
		/**
		 * Sets the registers that the slot allocates to.
		 *
		 * Lower indexes are the least significant values.
		 *
		 * @param __r The registers to set the slot.
		 * @throws NullPointerException On null arguments.
		 * @since 2017/03/11
		 */
		@Deprecated
		public void setRegisters(Iterable<Register> __r)
			throws NullPointerException
		{
			throw new todo.TODO();
			
			/*
			// Check
			if (__r == null)
				throw new NullPointerException("NARG");
			
			// {@squirreljme.error AQ1q Cannot set registers for aliased
			// slots.}
			if (isAliased())
				throw new JITException("AQ1q");
			
			// Copy
			__clearRegisters();
			for (Register r : __r)
				__addRegister(r);
			*/
		}
		
		/**
		 * Sets the type of value stored in this slot.
		 *
		 * @param __t The type of value to store, if this slot is aliased and
		 * the alias is not compatible it will be removed.
		 * @return The old type.
		 * @throws JITException If the type is {@link JavaType#TOP} type.
		 * @throws NullPointerException On null arguments.
		 * @since 2017/02/23
		 */
		@Deprecated
		public JavaType setType(JavaType __t)
			throws JITException, NullPointerException
		{
			throw new todo.TODO();
			
			/*
			// Check
			if (__t == null)
				throw new NullPointerException("NARG");
			
			// {@squirreljme.error AQ1i Cannot set the top type.}
			if (__t == JavaType.TOP)
				throw new JITException("AQ1i");
			
			// {@squirreljme.error AQ1j Cannot set the type to nothing, use
			// remove for that.}
			if (__t == JavaType.NOTHING)
				throw new IllegalStateException("AQ1j");
			
			// Remove the type information
			JavaType rv = this._type;
			remove();
			
			// Allocate registers if possible
			System.err.println("TODO -- Allocate registers.");
			
			// Set, return old
			this._type = __t;
			return rv;
			*/
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/03/31
		 */
		@Override
		protected AreaType thisAliasedArea()
		{
			return this._atalias;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/03/31
		 */
		@Override
		protected int thisAliasedIndex()
		{
			return this._idalias;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/03/11
		 */
		@Override
		public List<Register> thisRegisters()
		{
			Reference<List<Register>> ref = this._umregs;
			List<Register> rv;
			
			if (ref == null || null == (rv = ref.get()))
				this._umregs = new WeakReference<>(
					(rv = UnmodifiableList.<Register>of(this._registers)));
			
			return rv;
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
		 * @since 2017/02/23
		 */
		@Override
		public JavaType thisType()
		{
			return this._type;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/03/25
		 */
		@Override
		public String toString()
		{
			// If aliased by nothing, do not print that information
			Set<Slot> aliasedby = this._aliasedby;
			if (aliasedby.isEmpty())
				return super.toString();
			
			// Otherwise do build it
			StringBuilder sb = new StringBuilder(super.toString());
			sb.append("<~[");
			
			// Go through aliases and just print their IDs
			boolean comma = false;
			for (Slot s : aliasedby)
			{
				// Space?
				if (!comma)
					comma = true;
				else
					sb.append(", ");
				
				// Print info
				sb.append(s.thisArea());
				sb.append('#');
				sb.append(s.thisIndex());
			}
			
			// Finish
			sb.append("]");
			return sb.toString();
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
		
		/**
		 * Adds a register to the used registers.
		 *
		 * @param __r The register to add.
		 * @throws NullPointerException On null arguments.
		 * @since 2017/03/25
		 */
		@Deprecated
		private void __addRegister(Register __r)
			throws NullPointerException
		{
			throw new todo.TODO();
			
			/*
			// Check
			if (__r == null)
				throw new NullPointerException("NARG");
			
			// {@squirreljme.error AQ1k Add of a register which is not
			// allocatable naturally to a slot or one which has already been
			// consumed. (The register that was removed; This slot; The
			// registers available for allocation)}
			MultiSetDeque<Register> foralloc = ActiveCacheState.this.foralloc;
			if (!foralloc.remove(__r))
				throw new JITException(String.format("AQ1k %s %s %s", __r,
					this, foralloc));
			
			// Add to register list
			this._registers.add(__r);
			*/
		}
		
		/**
		 * Clears all registers.
		 *
		 * @since 2017/03/25
		 */
		@Deprecated
		private void __clearRegisters()
		{
			throw new todo.TODO();
			
			/*
			// {@squirreljme.error AQ1l Attempt to clear registers for a slot
			// which is aliased. (This slot)}
			if (isAliased())
				throw new JITException(String.format("AQ1l %s", this));
			
			// Refill free register usage
			__Code__ engine = ActiveCacheState.this._code;
			Deque<Register> savedint = ActiveCacheState.this.savedint,
				savedfloat = ActiveCacheState.this.savedfloat,
				tempint = ActiveCacheState.this.tempint,
				tempfloat = ActiveCacheState.this.tempfloat;
			List<Register> registers = this._registers;
			RegisterDictionary rdict = ActiveCacheState.this.rdict;
			while (!registers.isEmpty())
			{
				// Remove the last registers
				Register r = registers.remove(registers.size() - 1);
				
				// Temporary
				if (rdict.isRegisterArgument(r) ||
					rdict.isRegisterTemporary(r))
				{
					if (r.isInteger())
						tempint.offerFirst(r);
					if (r.isFloat())
						tempfloat.offerFirst(r);
				}
				
				// Saved
				else
				{
					if (r.isInteger())
						savedint.offerFirst(r);
					if (r.isFloat())
						savedfloat.offerFirst(r);
				}
			}
			*/
		}
		
		/**
		 * De-alias any slots which are aliased by this slot.
		 *
		 * @since 2017/03/25
		 */
		@Deprecated
		private void __deAliasFromThis()
		{
			throw new todo.TODO();
			
			/*
			// Remove alias to this slot
			boolean genops = ActiveCacheState.this.genops;
			Set<Slot> aliasedby = this._aliasedby;
			for (Slot by : aliasedby)
			{
				if (true)
					throw new todo.TODO();
				
				// Generate operation?
				if (genops)
					throw new todo.TODO();
			}
			
			// Clear it
			aliasedby.clear();
			*/
		}
		
		/**
		 * Switches to the specified state.
		 *
		 * @param __t The slot to copy from.
		 * @throws NullPointerException On null arguments.
		 * @since 2017/03/01
		 */
		private void __switchFrom(CacheState.Slot __s)
			throws NullPointerException
		{
			// Check
			if (__s == null)
				throw new NullPointerException("NARG");
			
			// The aliased by is set later
			this._aliasedby.clear();
			
			// Copy state
			CacheState.Slot value = __s.value();
			this._type = value.thisType();
			
			// Aliased?
			List<Register> registers = this._registers;
			if (value != __s)
			{
				this._atalias = value.thisArea();
				this._idalias = value.thisIndex();
				
				// Aliased entries do not use registers because their register
				// usage is a purely virtual
				registers.clear();
			}
			
			// Not aliased
			else
			{
				this._atalias = null;
				this._idalias = -1;
				
				// Use all registers
				registers.clear();
				setRegisters(__s.thisRegisters());
			}
		}
	}
	
	/**
	 * This is used to represent an active tread.
	 *
	 * @since 2017/02/23
	 */
	public final class Tread
		extends CacheState.Tread
	{
		/** Slots. */
		private final Slot[] _slots;
		
		/**
		 * Initializes the tread.
		 *
		 * @param __a The area type.
		 * @param __n The number of slots.
		 * @throws NullPointerException On null arguments.
		 * @since 2017/02/23
		 */
		private Tread(AreaType __a, int __n)
			throws NullPointerException
		{
			super(__a);
			
			// Initialize slots
			Slot[] slots = new Slot[__n];
			for (int i = 0; i < __n; i++)
				slots[i] = new Slot(__a, i);
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
		
		/**
		 * {@Override}
		 * @since 2017/03/07
		 */
		@Override
		public ActiveCacheState state()
		{
			return ActiveCacheState.this;
		}
		
		/**
		 * Fixes the aliased by set for the slots.
		 *
		 * @since 2017/03/25
		 */
		private void __fixAliasedBy()
		{
			Slot[] slots = this._slots;
			for (int i = 0, n = slots.length; i < n; i++)
			{
				// This slot and its value
				Slot self = slots[i],
					value = self.value();
				
				// If this slot aliases another then point to it
				if (self != value)
					value._aliasedby.add(self);
			}
		}
		
		/**
		 * Switches to the specified state.
		 *
		 * @param __t The tread to copy from.
		 * @throws NullPointerException On null arguments.
		 * @since 2017/03/01
		 */
		private void __switchFrom(CacheState.Tread __t)
			throws NullPointerException
		{
			// Check
			if (__t == null)
				throw new NullPointerException("NARG");
			
			// Go through all variables
			Slot[] slots = this._slots;
			for (int i = 0, n = slots.length; i < n; i++)
				slots[i].__switchFrom(__t.get(i));
		}
	}
}

