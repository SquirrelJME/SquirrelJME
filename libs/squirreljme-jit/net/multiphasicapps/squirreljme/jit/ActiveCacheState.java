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
import net.multiphasicapps.squirreljme.classformat.StackMapType;

/**
 * This represents the currently active cache state which is used to store
 * bindings.
 *
 * @since 2017/02/23
 */
public final class ActiveCacheState
	implements CacheState
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
	 * {@inheritDoc}
	 * @since 2017/02/23
	 */
	@Override
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
	 * {@inheritDoc}
	 * @since 2017/03/01
	 */
	@Override
	public String toString()
	{
		return String.format("{stack=%s, locals=%s}", this.stack, this.locals);
	}
	
	/**
	 * The slot stores the information on the current element.
	 *
	 * @since 2017/02/23
	 */
	public final class Slot
		implements CacheState.Slot
	{
		/** Is this the stack? */
		protected final boolean isstack;
		
		/** The index of this slot. */
		protected final int index;
		
		/** The active binding. */
		protected final ActiveBinding binding;
		
		/** The type of value stored here. */
		private volatile StackMapType _type =
			StackMapType.NOTHING;
		
		/** Aliased to the stack?. */
		private volatile boolean _stackalias;
		
		/** Slot this is aliased to. */
		private volatile int _idalias =
			-1;
		
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
			this.binding = __te.createActiveBinding(this);
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
				int idalias = at._idalias;
				if (idalias < 0)
					if (at == this)
						return null;
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
		 * {@inheritDoc}
		 * @since 2017/02/23
		 */
		@Override
		public <B extends Binding> B binding(Class<B> __cl)
			throws ClassCastException, NullPointerException
		{
			// Check
			if (__cl == null)
				throw new NullPointerException("NARG");
			
			// Cast
			return __cl.cast(this.binding);
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
		 * {@inheritDoc}
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
			
			// Set
			this._stackalias = __s;
			this._idalias = __id;
		}
		
		/**
		 * Sets the type of value stored in this slot.
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
			ActiveBindingChangeType ct;
			switch (__t)
			{
				case NOTHING:	ct = ActiveBindingChangeType.CLEARED; break;
				case INTEGER:	ct = ActiveBindingChangeType.TO_INTEGER; break;
				case LONG:		ct = ActiveBindingChangeType.TO_LONG; break;
				case FLOAT:		ct = ActiveBindingChangeType.TO_FLOAT; break;
				case DOUBLE:	ct = ActiveBindingChangeType.TO_DOUBLE; break;
				case OBJECT:	ct = ActiveBindingChangeType.TO_OBJECT; break;
				
					// Unknown
				default:
					throw new RuntimeException("OOPS");
			}
			
			// Set change
			this.binding.changeBinding(ct);
			
			// Set, return old
			this._type = __t;
			return rv;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/02/23
		 */
		@Override
		public String toString()
		{
			int idalias = this._idalias; 
			String alias = (idalias >= 0 ? String.format("copied=%c#%d",
				(this._stackalias ? 'S' : 'L'), idalias) : "not aliased");
			return String.format("{%c#%d: type=%s, binding=%s, %s}",
				(this.isstack ? 'S' : 'L'), this.index, this._type,
				this.binding, alias);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/02/23
		 */
		@Override
		public StackMapType type()
		{
			return this._type;
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
			this._type = __s.type();
			this.binding.switchFrom(__s.<Binding>binding(Binding.class));
			
			// Aliased?
			CacheState.Slot alias = __s.alias();
			if (alias != null)
			{
				this._stackalias = alias.isStack();
				this._idalias = alias.index();
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

