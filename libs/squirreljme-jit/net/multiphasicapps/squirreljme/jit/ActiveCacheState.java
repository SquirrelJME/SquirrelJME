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
import java.util.List;
import java.util.RandomAccess;
import net.multiphasicapps.squirreljme.classformat.CodeVariable;
import net.multiphasicapps.squirreljme.classformat.StackMapType;
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
		super(__te);
		
		// Setup treads
		this.stack = new Tread(true, __ms);
		this.locals = new Tread(false, __ml);
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
		
		// Restore state
		this.stack.__switchFrom(__cs.stack());
		this.locals.__switchFrom(__cs.locals());
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
			// {@squirreljme.error ED0d The specified index exceeds the
			// bounds of the target tread. (Aliased to the stack?; The index
			// aliased to)}
			Tread against = (__s ? ActiveCacheState.this.stack :
				ActiveCacheState.this.locals);
			if (__id < 0 || __id >= against.size())
				throw new IllegalArgumentException(String.format("ED0d %b %d",
					__s, __id));
			
			// {@squirreljme.error ED0f Local variables cannot alias values on
			// the stack. (The index on the stack to be aliased to)}
			if (thisIsLocal() && this._stackalias)
				throw new IllegalArgumentException(String.format("ED0f %d",
					__id));
			
			// Set
			this._stackalias = __s;
			this._idalias = __id;
		}
		
		/**
		 * Sets the registers that the slot allocates to.
		 *
		 * @param __r The registers to set the slot.
		 * @since 2017/03/11
		 */
		public void setRegisters(Register... __r)
		{
			List<Register> registers = this._registers;
			registers.clear();
			for (Register r : __r)
				registers.add(r);
		}
		
		/**
		 * Sets the registers that the slot allocates to.
		 *
		 * @param __r The registers to set the slot.
		 * @since 2017/03/11
		 */
		public void setRegisters(Iterable<Register> __r)
		{
			List<Register> registers = this._registers;
			registers.clear();
			for (Register r : __r)
				registers.add(r);
		}
		
		/**
		 * Sets the type of value stored in this slot along with also causing
		 * a binding change.
		 *
		 * @param __t The type of value to store.
		 * @return The old type.
		 * @throws JITException If the type is {@link StackMapType#TOP} type.
		 * @throws NullPointerException On null arguments.
		 * @since 2017/02/23
		 */
		public StackMapType setType(StackMapType __t)
			throws JITException, NullPointerException
		{
			return setType(__t, true);
		}
		
		/**
		 * Sets the type of value stored in this slot.
		 *
		 * @param __t The type of value to store, if this slot is aliased and
		 * the alias is not compatible it will be removed.
		 * @param __ebc If the type is to change, should the binding be
		 * notified of the change so it may potentially adjust the binding
		 * data?
		 * @return The old type.
		 * @throws JITException If the type is {@link StackMapType#TOP} type.
		 * @throws NullPointerException On null arguments.
		 * @since 2017/02/23
		 */
		public StackMapType setType(StackMapType __t, boolean __ebc)
			throws JITException, NullPointerException
		{
			// Check
			if (__t == null)
				throw new NullPointerException("NARG");
			
			// {@squirreljme.error ED0b Cannot set the top type.}
			if (__t == StackMapType.TOP)
				throw new JITException("ED0b");
			
			// Do nothing if the type remains the same
			StackMapType rv = this._type;
			if (__t == rv)
				return __t;
			
			// Depending on the target type, specify the change
			if (__ebc)
			{
				throw new todo.TODO();
			}
			
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
			
			// Copy state
			CacheState.Slot value = __s.value();
			this._type = value.thisType();
			
			// Aliased?
			if (value != __s)
			{
				this._stackalias = value.thisIsStack();
				this._idalias = value.thisIndex();
			}
			
			// Not aliased
			else
			{
				this._stackalias = false;
				this._idalias = -1;
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

