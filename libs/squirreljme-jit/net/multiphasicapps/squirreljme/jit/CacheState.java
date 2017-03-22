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
import net.multiphasicapps.squirreljme.classformat.CodeVariable;
import net.multiphasicapps.squirreljme.classformat.StackMapType;
import net.multiphasicapps.util.unmodifiable.UnmodifiableList;

/**
 * This interface acts as the base for the mutable and immutable cache states.
 *
 * @since 2017/03/03
 */
public abstract class CacheState
{
	/** The owning code stream. */
	private final __JITCodeStream__ _codestream;
	
	/**
	 * Base initialization.
	 *
	 * @param __cs The owning code stream.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/11
	 */
	CacheState(__JITCodeStream__ __cs)
		throws NullPointerException
	{
		// Check
		if (__cs == null)
			throw new NullPointerException("NARG");
		
		// Set
		this._codestream = __cs;
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
		int id = __cv.id();
		if (__cv.isStack())
			return stack().get(id);
		return locals().get(id);
	}
	
	/**
	 * Returns the slot which is associated with the given tread and index.
	 *
	 * @param __s Is the slot on the stack?
	 * @param __i The index of the slot.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/06
	 */
	public Slot getSlot(boolean __s, int __i)
		throws NullPointerException
	{
		if (__s)
			return stack().get(__i);
		return locals().get(__i);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/20
	 */
	@Override
	public String toString()
	{
		return String.format("{stack=%s, locals=%s}", stack(), locals());
	}
	
	/**
	 * This interface acts as the base for slots within the cache state.
	 *
	 * @since 2017/03/03
	 */
	public abstract class Slot
	{
		/**
		 * Initializes the base slot.
		 *
		 * @since 2017/03/11
		 */
		Slot()
		{
		}
		
		/**
		 * Returns the index of this slot.
		 *
		 * @return The slot index.
		 * @since 2017/03/03
		 */
		public abstract int thisIndex();
		
		/**
		 * Returns {@code true} if this is a local slot.
		 *
		 * @return {@code true} if a local slot.
		 * @since 2017/03/03
		 */
		public abstract boolean thisIsLocal();
		
		/**
		 * Returns {@code true} if this is a stack slot.
		 *
		 * @return {@code true} if a stack slot.
		 * @since 2017/03/03
		 */
		public abstract boolean thisIsStack();
		
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
		 * Returns the tread this slot is in.
		 *
		 * @return The tread this slot is in.
		 * @since 2017/03/07
		 */
		public abstract Tread thisTread();
		
		/**
		 * Returns the type of value that is stored here.
		 *
		 * @return The type of value to store.
		 * @since 2017/03/03
		 */
		public abstract StackMapType thisType();
		
		/**
		 * Returns the slot which contains the value for this slot, if the
		 * slot is aliased it will return the alias, otherwise this slot.
		 *
		 * @return The aliased slot or {@code this} if not aliased.
		 * @since 2017/03/06
		 */
		public abstract Slot value();
		
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
		 * Returns the allocation of this slot.
		 *
		 * @return The allocation of this slot, or {@code null} if there is
		 * none.
		 * @since 2017/03/22
		 */
		public ArgumentAllocation thisAllocation()
		{
			// Nothing has no allocation
			StackMapType type = thisType();
			if (type == null)
				return null;
			
			// Need data type, used by the allocation class
			DataType dt = CacheState.this._codestream._engine.toDataType(type);
			
			// Purely on the stack?
			List<Register> registers = thisRegisters();
			if (registers.isEmpty())
				return new ArgumentAllocation(dt, thisStackOffset());
			
			// In registers
			return new ArgumentAllocation(dt, registers.<Register>toArray(
				new Register[registers.size()]));
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
		 * Returns the stack offset of this slot.
		 *
		 * @return The stack offset of this slot or
		 * {@code Integer#MIN_VALUE} if it is not valid.
		 * @since 2017/03/16
		 */
		public final int thisStackOffset()
		{
			// If the type is nothing it will never have a stack offset
			StackMapType type = thisType();
			if (type == StackMapType.NOTHING)
				return Integer.MIN_VALUE;
			
			// Return the offset for the given entry
			__JITCodeStream__ jcs = CacheState.this._codestream;
			return ((JITStateAccessor)jcs).stackSlotOffsets().get(
				thisIsStack(), thisIndex(), jcs._engine.toDataType(type));
		}
		
		/**
		 * Returns the allocation value of this slot.
		 *
		 * @return The allocation value, or {@code null} if there is none.
		 * @since 2017/03/22
		 */
		public final ArgumentAllocation valueAllocation()
		{
			return value().thisAllocation();
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
		 * Returns {@code true} if this is a local slot.
		 *
		 * @return {@code true} if a local slot.
		 * @since 2017/03/11
		 */
		public final boolean valueIsLocal()
		{
			return value().thisIsLocal();
		}
		
		/**
		 * Returns {@code true} if this is a stack slot.
		 *
		 * @return {@code true} if a stack slot.
		 * @since 2017/03/11
		 */
		public final boolean valueIsStack()
		{
			return value().thisIsStack();
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
		 * Returns the stack offset of the value slot.
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
		public final StackMapType valueType()
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
			sb.append(thisIsStack() ? 'S' : 'L');
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
			int so = thisStackOffset();
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
	public interface Tread
		extends RandomAccess
	{
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
	}
}

