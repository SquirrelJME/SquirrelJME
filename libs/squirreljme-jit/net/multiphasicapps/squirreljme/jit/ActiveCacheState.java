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
import net.multiphasicapps.squirreljme.classformat.CodeVariable;
import net.multiphasicapps.squirreljme.classformat.StackMapType;
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
				// Locals first
				boolean sa = __a.thisIsStack(),
					sb = __b.thisIsStack();
				if (!sa && sb)
					return -1;
				else if (sa && !sb)
					return 1;
				
				// Then the index
				return __a.thisIndex() - __b.thisIndex();
			}
		};
	
	/** The owning translation engine. */
	protected final TranslationEngine engine;
	
	/** Stack code variables. */
	protected final Tread stack;
	
	/** Local code variables. */
	protected final Tread locals;
	
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
	
	/** Generate machine code operations? */
	protected final boolean genops;
	
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
	 * @param __sv Saved registers for allocation.
	 * @param __tm Temporary registers for allocation.
	 * @param __go Generate operations?
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/23
	 */
	ActiveCacheState(__JITCodeStream__ __cs, int __ms, int __ml,
		Register[] __sv, Register[] __tm, boolean __go)
		throws NullPointerException
	{
		super(__cs);
		
		// Check
		if (__sv == null || __tm == null)
			throw new NullPointerException("NARG");
		
		// Setup treads
		this.engine = __cs._engine;
		this.genops = __go;
		this.stack = new Tread(true, __ms);
		this.locals = new Tread(false, __ml);
		
		// Copy saved registers
		Set<Register> availsaved = new LinkedHashSet<>();
		for (Register r : __sv)
			if (r != null)
				availsaved.add(r);
		this._availsaved = availsaved;
		
		// Copy saved registers
		Set<Register> availtemp = new LinkedHashSet<>();
		for (Register r : __tm)
			if (r != null)
				availtemp.add(r);
		this._availtemp = availtemp;
		
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
	public Slot getSlot(boolean __s, int __i)
		throws NullPointerException
	{
		return (Slot)super.getSlot(__s, __i);
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
			locals = this.locals;
		stack.__switchFrom(__cs.stack());
		locals.__switchFrom(__cs.locals());
		
		// Correct aliased by
		stack.__fixAliasedBy();
		locals.__fixAliasedBy();
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
		/** Is this the stack? */
		protected final boolean isstack;
		
		/** The index of this slot. */
		protected final int index;
		
		/** List of registers used. */
		private final List<Register> _registers =
			new ArrayList<>();
		
		/** Slots which alias this slot. */
		private final Set<Slot> _aliasedby =
			new SortedTreeSet<>(_SLOT_COMPARATOR);
		
		/** The type of value stored here. */
		private volatile StackMapType _type =
			StackMapType.NOTHING;
		
		/** Aliased to the stack?. */
		private volatile boolean _stackalias;
		
		/** Slot this is aliased to. */
		private volatile int _idalias =
			-1;
		
		/** Unmodifiable registers. */
		private volatile Reference<List<Register>> _umregs;
		
		/**
		 * Initializes the slot.
		 *
		 * @param __stack If {@code true} then these values are on the stack.
		 * @param __n The number of slots.
		 * @throws NullPointerException On null arguments.
		 * @since 2017/02/23
		 */
		private Slot(boolean __stack, int __i)
			throws NullPointerException
		{
			// Set
			this.isstack = __stack;
			this.index = __i;
		}
		
		/**
		 * Clears the alias for this slot.
		 *
		 * @since 2017/03/03
		 */
		public void clearAlias()
		{
			// Remove alias
			__deAliasFromThis();
			
			// Clear
			this._stackalias = false;
			this._idalias = -1;
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
			if (thisType() == StackMapType.NOTHING)
				return;
			
			// Remove aliases to this slot
			__deAliasFromThis();
			
			// Remove cached information
			if (!isAliased())
				__clearRegisters();
			this._type = StackMapType.NOTHING;
			this._stackalias = false;
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
			setAlias(__cv.isStack(), __cv.id());
		}
		
		/**
		 * Aliases the value of this slot to another slot.
		 *
		 * @param __s Is this aliased to the stack?
		 * @param __id The slot index this is aliased to.
		 * @throws IllegalArgumentException If the slot is not valid.
		 * @since 2017/03/01
		 */
		public void setAlias(boolean __s, int __id)
			throws IllegalArgumentException
		{
			// Do not have recursive aliases
			Slot target = getSlot(__s, __id).value();
			
			// {@squirreljme.error ED0o Cannot alias slot to self. (This slot)}
			if (target == this)
				throw new JITException(String.format("ED0o %s", this));
			
			// {@squirreljme.error ED0f Local variables cannot alias other
			// slots. (This slot; The target slot)}
			if (thisIsLocal())
				throw new IllegalArgumentException(String.format("ED0f %s %s",
					this, __id));
			
			// {@squirreljme.error ED0d Cannot alias the current slot to the
			// target slot becuase the target has no set value. (This slot; The
			// target slot)}
			if (target.thisType() == StackMapType.NOTHING)
				throw new JITException(String.format("ED0d %s %s", this,
					target));
			
			// Remove any information in this slot
			remove();
			
			// Set
			this._stackalias = target.thisIsStack();
			this._idalias = target.thisIndex();
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
		public void setRegisters(Register... __r)
			throws NullPointerException
		{
			// Check
			if (__r == null)
				throw new NullPointerException("NARG");
			
			// {@squirreljme.error ED0k Cannot set registers for aliased
			// slots.}
			if (isAliased())
				throw new JITException("ED0k");
			
			// Copy
			__clearRegisters();
			for (Register r : __r)
				__addRegister(r);
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
		public void setRegisters(Iterable<Register> __r)
			throws NullPointerException
		{
			// Check
			if (__r == null)
				throw new NullPointerException("NARG");
			
			// {@squirreljme.error ED0l Cannot set registers for aliased
			// slots.}
			if (isAliased())
				throw new JITException("ED0l");
			
			// Copy
			__clearRegisters();
			for (Register r : __r)
				__addRegister(r);
		}
		
		/**
		 * Sets the type of value stored in this slot.
		 *
		 * @param __t The type of value to store, if this slot is aliased and
		 * the alias is not compatible it will be removed.
		 * @return The old type.
		 * @throws JITException If the type is {@link StackMapType#TOP} type.
		 * @throws NullPointerException On null arguments.
		 * @since 2017/02/23
		 */
		public StackMapType setType(StackMapType __t)
			throws JITException, NullPointerException
		{
			// Check
			if (__t == null)
				throw new NullPointerException("NARG");
			
			// {@squirreljme.error ED0b Cannot set the top type.}
			if (__t == StackMapType.TOP)
				throw new JITException("ED0b");
			
			// {@squirreljme.error EB0p Cannot set the type to nothing, use
			// remove for that.}
			if (__t == StackMapType.NOTHING)
				throw new IllegalStateException("EB0p");
			
			// Remove the type information
			StackMapType rv = this._type;
			remove();
			
			// Allocate registers if possible
			System.err.println("TODO -- Allocate registers.");
			
			// Set, return old
			this._type = __t;
			return rv;
		}
		
		/**
		 * {@inheritDoc}
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
			return (this.isstack ? ActiveCacheState.this.stack :
				ActiveCacheState.this.locals);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/02/23
		 */
		@Override
		public StackMapType thisType()
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
				sb.append((s.thisIsStack() ? 'S' : 'L'));
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
			for (Slot at = this;;)
			{
				// Aliased?
				int idalias = at._idalias;
				if (idalias < 0)
					if (at == this)
						return this;
					else
						return at;
			
				// {@squirreljme.error ED0e Slot eventually references itself.
				// (This slot)}
				at = (at._stackalias ? ActiveCacheState.this.stack :
					ActiveCacheState.this.locals).get(idalias);
				if (at == this)
					throw new IllegalStateException(String.format("ED0e %s",
						this));
			}
		}
		
		/**
		 * Adds a register to the used registers.
		 *
		 * @param __r The register to add.
		 * @throws NullPointerException On null arguments.
		 * @since 2017/03/25
		 */
		private void __addRegister(Register __r)
			throws NullPointerException
		{
			// Check
			if (__r == null)
				throw new NullPointerException("NARG");
			
			// {@squirreljme.error ED0m Add of a register which is not
			// allocatable naturally to a slot or one which has already been
			// consumed. (The register that was removed; This slot; The
			// registers available for allocation)}
			MultiSetDeque<Register> foralloc = ActiveCacheState.this.foralloc;
			if (!foralloc.remove(__r))
				throw new JITException(String.format("ED0m %s %s %s", __r,
					this, foralloc));
			
			// Add to register list
			this._registers.add(__r);
		}
		
		/**
		 * Clears all registers.
		 *
		 * @since 2017/03/25
		 */
		private void __clearRegisters()
		{
			// {@squirreljme.error ED0n Attempt to clear registers for a slot
			// which is aliased. (This slot)}
			if (isAliased())
				throw new JITException(String.format("ED0n %s", this));
			
			// Refill free register usage
			TranslationEngine engine = ActiveCacheState.this.engine;
			Deque<Register> savedint = ActiveCacheState.this.savedint,
				savedfloat = ActiveCacheState.this.savedfloat,
				tempint = ActiveCacheState.this.tempint,
				tempfloat = ActiveCacheState.this.tempfloat;
			List<Register> registers = this._registers;
			while (!registers.isEmpty())
			{
				// Remove the last registers
				Register r = registers.remove(registers.size() - 1);
				
				// Temporary
				if (engine.isRegisterArgument(r) ||
					engine.isRegisterTemporary(r))
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
		}
		
		/**
		 * De-alias any slots which are aliased by this slot.
		 *
		 * @since 2017/03/25
		 */
		private void __deAliasFromThis()
		{
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
				this._stackalias = value.thisIsStack();
				this._idalias = value.thisIndex();
				
				// Aliased entries do not use registers because their register
				// usage is a purely virtual
				registers.clear();
			}
			
			// Not aliased
			else
			{
				this._stackalias = false;
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
		extends AbstractList<Slot>
		implements CacheState.Tread, RandomAccess
	{
		/** Slots. */
		private final Slot[] _slots;
		
		/**
		 * Initializes the tread.
		 *
		 * @param __stack If {@code true} then these values are on the stack.
		 * @param __n The number of slots.
		 * @throws NullPointerException On null arguments.
		 * @since 2017/02/23
		 */
		private Tread(boolean __stack, int __n)
			throws NullPointerException
		{
			// Initialize slots
			Slot[] slots = new Slot[__n];
			for (int i = 0; i < __n; i++)
				slots[i] = new Slot(__stack, i);
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

