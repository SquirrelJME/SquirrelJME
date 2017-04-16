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
import java.util.List;
import java.util.RandomAccess;
import net.multiphasicapps.util.unmodifiable.UnmodifiableList;

/**
 * This interface acts as the base for the mutable and immutable cache states.
 *
 * @since 2017/03/03
 */
public abstract class CacheState
{
	/** The owning code stream. */
	private final __Code__ _code;
	
	/**
	 * Base initialization.
	 *
	 * @param __cs The owning code stream.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/11
	 */
	CacheState(__Code__ __cs)
		throws NullPointerException
	{
		// Check
		if (__cs == null)
			throw new NullPointerException("NARG");
		
		// Set
		this._code = __cs;
	}
	
	/**
	 * Returns the cached local variable assignments.
	 *
	 * @return The cached local variables.
	 * @since 2017/03/03
	 */
	public abstract Tread locals();
	
	/**
	 * Returns the cached stack variable assignments.
	 *
	 * @return The cached stack variables.
	 * @since 2017/03/03
	 */
	public abstract Tread stack();
	
	/**
	 * The working tread.
	 *
	 * @return The working tread of variables.
	 * @since 2017/03/31
	 */
	public abstract Tread work();
	
	/**
	 * Returns the slot which is associated with the given variable.
	 *
	 * @param __cv The variable to get the slot of.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/03
	 */
	public Slot getSlot(CodeVariable __cv)
		throws NullPointerException
	{
		// Check
		if (__cv == null)
			throw new NullPointerException("NARG");
		
		// Depends
		return getTread(__cv.area()).get(__cv.id());
	}
	
	/**
	 * Returns the slot which is associated with the given tread and index.
	 *
	 * @param __t Which area is this tread on?
	 * @param __i The index of the slot.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/06
	 */
	public Slot getSlot(AreaType __t, int __i)
		throws NullPointerException
	{
		return getTread(__t).get(__i);
	}
	
	/**
	 * Returns the tread with the given area type.
	 * 
	 * @param __t The tread to get.
	 * @return The tread for the given area.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/31
	 */
	public Tread getTread(AreaType __t)
		throws NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Depends
		switch (__t)
		{
			case LOCAL: return locals();
			case STACK: return stack();
			case WORK: return work();
			default:
				throw new RuntimeException("OOPS");
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/20
	 */
	@Override
	public String toString()
	{
		return String.format("{stack=%s, locals=%s, work=%s}", stack(),
			locals(), work());
	}
	
	/**
	 * This interface acts as the base for slots within the cache state.
	 *
	 * @since 2017/03/03
	 */
	public abstract class Slot
	{
		/** The area this slot is in. */
		protected final AreaType area;
		
		/** The slot index. */
		protected final int index;
		
		/**
		 * Initializes the base slot.
		 *
		 * @param __a The slot area.
		 * @param __i The index of this slot.
		 * @throws NullPointerException On null arguments.
		 * @since 2017/03/11
		 */
		Slot(AreaType __a, int __i)
			throws NullPointerException
		{
			// Check
			if (__a == null)
				throw new NullPointerException("NARG");
			
			this.area = __a;
			this.index = __i;
		}
		
		/**
		 * Returns the area that this aliases to.
		 *
		 * @return The aliased area or {@code null} if it is not aliased.
		 * @since 2017/03/31
		 */
		protected abstract AreaType thisAliasedArea();
		
		/**
		 * Returns the index that this aliases to.
		 *
		 * @return The aliased index or a negative value if it is not aliased.
		 * @since 2017/03/31
		 */
		protected abstract int thisAliasedIndex();
		
		/**
		 * Returns the list of registers which are stored in this slot.
		 *
		 * Lower indexes are the least significant values.
		 *
		 * @return the list of associated registers.
		 * @since 2017/03/11
		 */
		public abstract List<Register> thisRegisters();
		
		/**
		 * Returns the type of value that is stored here.
		 *
		 * @return The type of value to store.
		 * @since 2017/03/03
		 */
		public abstract JavaType thisType();
		
		/**
		 * Returns the slot which contains the value for this slot, if the
		 * slot is aliased it will return the alias, otherwise this slot.
		 *
		 * @return The aliased slot or {@code this} if not aliased.
		 * @since 2017/03/06
		 */
		public Slot value()
		{
			for (Slot at = this;;)
			{
				// Aliased?
				AreaType idarea = at.thisAliasedArea();
				int idalias = at.thisAliasedIndex();
				if (idarea == null || idalias < 0)
					if (at == this)
						return this;
					else
						return at;
			
				// {@squirreljme.error AQ1s Slot eventually references itself.
				// (This slot)}
				at = CacheState.this.getTread(idarea).get(idalias);
				if (at == this)
					throw new IllegalStateException(String.format("AQ1s %s",
						this));
			}
		}
		
		/**
		 * Returns {@code true} if this slot is aliased.
		 *
		 * @return {@code true} if this is an aliased slot.
		 * @since 2017/03/11
		 */
		public final boolean isAliased()
		{
			return value() != this;
		}
		
		/**
		 * The aliased variable for this slot.
		 *
		 * @return The variable that this aliases or {@code null} if this is
		 * not aliased to anything.
		 * @since 2017/03/31
		 */
		public final CodeVariable thisAliasedVariable()
		{
			AreaType area = thisAliasedArea();
			if (area == null)
				return null;
			return CodeVariable.of(area, thisAliasedIndex());
		}
		
		/**
		 * Returns the allocation of this slot, the stack offset is assigned by
		 * default.
		 *
		 * @return The allocation of this slot, or {@code null} if there is
		 * none.
		 * @since 2017/03/22
		 */
		public final ArgumentAllocation thisAllocation()
		{
			return thisAllocation(true);
		}
		
		/**
		 * Returns the allocation of this slot.
		 *
		 * @param __a If {@code true} the stack offset is assigned if it is
		 * not assigned and this is on the stack.
		 * @return The allocation of this slot, or {@code null} if there is
		 * none.
		 * @since 2017/03/22
		 */
		public ArgumentAllocation thisAllocation(boolean __a)
		{
			// Nothing has no allocation
			JavaType type = thisType();
			if (type == null || type == JavaType.NOTHING)
				return null;
			
			// Need data type, used by the allocation class
			NativeType dt = CacheState.this._code.__toNative(type);
			
			// Purely on the stack?
			List<Register> registers = thisRegisters();
			if (registers.isEmpty())
				return new ArgumentAllocation(dt, thisStackOffset(__a));
			
			// In registers
			return new ArgumentAllocation(dt, registers.<Register>toArray(
				new Register[registers.size()]));
		}
		
		/**
		 * Returns the area of this slot.
		 *
		 * @return The area of this slot.
		 * @since 2017/03/31
		 */
		public final AreaType thisArea()
		{
			return this.area;
		}
		
		/**
		 * Are registers used in this slot?
		 *
		 * @return {@code true} if registers are used in the slot.
		 * @since 2017/03/31
		 */
		public final boolean thisHasRegisters()
		{
			return !thisRegisters().isEmpty();
		}
		
		/**
		 * Returns the index of this slot.
		 *
		 * @return The slot index.
		 * @since 2017/03/03
		 */
		public final int thisIndex()
		{
			return this.index;
		}
		
		/**
		 * Returns the registers using the given type.
		 *
		 * @param <R> The type contained in the list.
		 * @return The registers for this slot.
		 * @since 2017/03/19
		 */
		@SuppressWarnings({"unchecked"})
		public final <R extends Register> List<R> thisRegistersAs()
		{
			return (List<R>)((Object)thisRegisters());
		}
		
		/**
		 * Returns the stack offset of this slot. By default the offset is
		 * allocated.
		 *
		 * @return The stack offset of this slot or
		 * {@code Integer#MIN_VALUE} if it is not valid.
		 * @since 2017/03/16
		 */
		public final int thisStackOffset()
		{
			return thisStackOffset(true);
		}
		
		/**
		 * Returns the stack offset of this slot.
		 *
		 * @param __a If {@code true} then the offset is allocated, otherwise
		 * it is not allocated.
		 * @return The stack offset of this slot or
		 * {@code Integer#MIN_VALUE} if it is not valid.
		 * @since 2017/03/30
		 */
		public final int thisStackOffset(boolean __a)
		{
			// If the type is nothing it will never have a stack offset
			JavaType type = thisType();
			if (type == JavaType.NOTHING)
				return Integer.MIN_VALUE;
			
			throw new todo.TODO();
			/*
			// Return the offset for the given entry
			__Code__ jcs = CacheState.this._code;
			NativeType dt = jcs._engine.toDataType(type);
			AreaType iss = thisArea();
			int idx = thisIndex();
			if (__a)
				return ((JITStateAccessor)jcs).stackSlotOffsets().assign(
				iss, idx, dt);
			return ((JITStateAccessor)jcs).stackSlotOffsets().get(
				iss, idx, dt);
			*/
		}
		
		/**
		 * Returns the tread this slot is in.
		 *
		 * @return The tread this slot is in.
		 * @since 2017/03/07
		 */
		public Tread thisTread()
		{
			return CacheState.this.getTread(this.area);
		}
		
		/**
		 * Returns the allocation value of this slot. The stack offset is
		 * assigned if it is not assigned.
		 *
		 * @return The allocation value, or {@code null} if there is none.
		 * @since 2017/03/22
		 */
		public final ArgumentAllocation valueAllocation()
		{
			return value().thisAllocation(true);
		}
		
		/**
		 * Returns the allocation value of this slot.
		 *
		 * @param __a If {@code true} then a stack offset is assigned if it
		 * has not been assigned and the value is on the stack.
		 * @return The allocation value, or {@code null} if there is none.
		 * @since 2017/03/22
		 */
		public final ArgumentAllocation valueAllocation(boolean __a)
		{
			return value().thisAllocation(__a);
		}
		
		/**
		 * Returns the area of the value slot.
		 *
		 * @return The area of the value slot.
		 * @since 2017/03/31
		 */
		public final AreaType valueArea()
		{
			return value().thisArea();
		}
		
		/**
		 * Are registers used in the value?
		 *
		 * @return {@code true} if registers are used in the value.
		 * @since 2017/03/31
		 */
		public final boolean valueHasRegisters()
		{
			return value().thisHasRegisters();
		}
		
		/**
		 * Returns the value index of this slot.
		 *
		 * @return The slot index.
		 * @since 2017/03/11
		 */
		public final int valueIndex()
		{
			return value().thisIndex();
		}
		
		/**
		 * Returns the list of registers which are stored in this slot.
		 *
		 * Lower indexes are the least significant values.
		 *
		 * @return the list of associated registers.
		 * @since 2017/03/11
		 */
		public final List<Register> valueRegisters()
		{
			return value().thisRegisters();
		}
		
		/**
		 * Returns the registers using the given type.
		 *
		 * @param <R> The type contained in the list.
		 * @return The value registers.
		 * @since 2017/03/19
		 */
		@SuppressWarnings({"unchecked"})
		public final <R extends Register> List<R> valueRegistersAs()
		{
			return (List<R>)((Object)valueRegisters());
		}
		
		/**
		 * Returns the stack offset of the value slot. By default the offset is
		 * allocated if it is not allocated.
		 *
		 * @return The stack offset of the value slot or
		 * {@code Integer#MIN_VALUE} if it is not valid.
		 * @since 2017/03/16
		 */
		public final int valueStackOffset()
		{
			return value().thisStackOffset();
		}
		
		/**
		 * Returns the stack offset of the value slot.
		 *
		 * @param __a If {@code true} then the offset is allocated if it is
		 * not allocated.
		 * @return The stack offset of the value slot or
		 * {@code Integer#MIN_VALUE} if it is not valid.
		 * @since 2017/03/30
		 */
		public final int valueStackOffset(boolean __a)
		{
			return value().thisStackOffset(__a);
		}
		
		/**
		 * Returns the tread this slot is in.
		 *
		 * @return The tread this slot is in.
		 * @since 2017/03/11
		 */
		public final Tread valueTread()
		{
			return value().thisTread();
		}
		
		/**
		 * Returns the type of value that is stored here.
		 *
		 * @return The type of value to store.
		 * @since 2017/03/11
		 */
		public final JavaType valueType()
		{
			return value().thisType();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/02/23
		 */
		@Override
		public String toString()
		{
			// Is this slot aliased?
			Slot value = value();
			
			// Build string
			StringBuilder sb = new StringBuilder();
			
			// If the slot is aliased, show the aliased value, the actual
			// slot information will appear in parenthesis to show what would
			// have been masked away
			if (value != this)
			{
				sb.append(value.toString());
				sb.append('(');
			}
			
			// Add the actual slot identifier
			sb.append('<');
			sb.append(thisArea());
			sb.append('#');
			sb.append(thisIndex());
			sb.append('>');
			
			// Type
			sb.append(':');
			sb.append(thisType());
			
			// Registers
			sb.append(':');
			sb.append('r');
			sb.append(thisRegisters().toString());
			
			// Stack
			sb.append(':');
			int so = thisStackOffset(false);
			if (so == Integer.MIN_VALUE)
				sb.append("s---");
			else
			{
				sb.append('s');
				sb.append(so);
			}
			
			// End marker
			if (value != this)
				sb.append(')');
			
			return sb.toString();
		}
	}
	
	/**
	 * This represents a tread of local and stack variables.
	 *
	 * @since 2017/03/03
	 */
	public abstract class Tread
		implements RandomAccess
	{
		/** The area this tread belongs in. */
		protected final AreaType area;
		
		/**
		 * Initializes the base tread.
		 *
		 * @param __a The area type.
		 * @throws NullPointerException On null arguments.
		 */
		Tread(AreaType __a)
			throws NullPointerException
		{
			// Check
			if (__a == null)
				throw new NullPointerException("NARG");
			
			// Set
			this.area = __a;
		}
		
		/**
		 * Gets the specified slot in this tread.
		 *
		 * @param __i The slot to get.
		 * @since 2017/03/03
		 */
		public abstract CacheState.Slot get(int __i);
		
		/**
		 * Returns the size of this tread.
		 *
		 * @return The tread size.
		 * @since 2017/03/06
		 */
		public abstract int size();
		
		/**
		 * Returns the state this tread is within.
		 *
		 * @return The owning state.
		 * @since 2017/03/07
		 */
		public abstract CacheState state();
		
		/**
		 * {@inheritDoc}
		 * @since 2017/03/31
		 */
		@Override
		public String toString()
		{
			// Build tread
			StringBuilder sb = new StringBuilder("[");
			for (int i = 0, n = size(); i < n; i++)
			{
				if (i > 0)
					sb.append(", ");
				sb.append(get(i));
			}
			
			// Finish
			sb.append(']');
			return sb.toString();
		}
	}
}

