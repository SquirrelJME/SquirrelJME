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
	/** The owning translation engine. */
	protected final TranslationEngine engine;
	
	/**
	 * Base initialization.
	 *
	 * @param __te The translation engine used.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/11
	 */
	CacheState(TranslationEngine __te)
		throws NullPointerException
	{
		// Check
		if (__te == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.engine = __te;
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
		return String.format("{stack=%s, locals=%s}", this.stack, this.locals);
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
		 * Returns the value index of this slot.
		 *
		 * @return The slot index.
		 * @since 2017/03/11
		 */
		public final int valueIndex()
		{
			return value().index();
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
		 * @return the list of associated registers.
		 * @since 2017/03/11
		 */
		public final List<Register> valueRegisters()
		{
			return value().thisRegisters();
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
			Slot alias = alias();
			
			// Build string
			StringBuiler sb = new StringBuilder();
			
			// If the slot is aliased, show the aliased value, the actual
			// slot information will appear in parenthesis to show what would
			// have been masked away
			if (alias != this)
			{
				sb.append('(');
				sb.append(alias.toString());
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
			
			// End marker
			if (alias != this)
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

