// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.interpreter;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;

/**
 * This represents a local variable slot which is assigned to a specific
 * location and may optionally change the pre-existing value.
 *
 * Slots are either purely virtual or actually exist.
 *
 * Slots may be used as keys within a map provide there are also not
 * {@link Integer} keys, which would lead to undefined results. These slots
 * compare to {@link Integer} except {@link Integer} does not compare to
 * slots.
 *
 * @since 2016/03/25
 */
public final class JVMProgramSlot
{
	/** The lock. */
	final Object lock;	
	
	/** Owning variables for value diffing. */
	protected final JVMProgramVars variables;
	
	/** The virtual position of this slot. */
	protected final int position;
	
	/** The logical position of this slot (if it is not virtual). */
	private volatile int _logpos =
		-1;
	
	/** The type this slot contains a value for. */
	private volatile JVMVariableType _type;
	
	/** The operator link. */
	private volatile JVMOperatorLink _link;
	
	/** Has an operator link? */
	private volatile boolean _haslink;
	
	/**
	 * Initializes the slot.
	 *
	 * @param __v The owning variables.
	 * @param __pos The position of this slot.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/25
	 */
	JVMProgramSlot(JVMProgramVars __v, int __pos)
		throws NullPointerException
	{
		// Check
		if (__v == null)
			throw new NullPointerException("NARG");
		
		// Set
		variables = __v;
		lock = variables.lock;
		position = __pos;
	}
	
	/**
	 * Clears the operator link for the current slot.
	 *
	 * Note that the operation is considered to be set, but to a
	 * {@code null} value.
	 *
	 * @return {@code this}.
	 * @since 2016/03/27
	 */
	public JVMProgramSlot clearLink()
	{
		// Lock
		synchronized (lock)
		{
			_haslink = true;
			_link = null;
		}
		
		// Self
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/25
	 */
	@Override
	public boolean equals(Object __o)
	{
		// If comparing against an integer, check the position to make sure
		// that it matches
		if (__o instanceof Integer)
			return position == ((Integer)__o).intValue();
		
		// Otherwise must be this only
		return __o == this;
	}
	
	/**
	 * Returns the current operator link associated with this value.
	 *
	 * If there is no link here, then this propogates up the slot mappings
	 * until a link is found. Propogation additionally stops when the
	 * current slot value is {@link JVMVariableType#NOTHING}.
	 *
	 * @return The current link that this has an associated value for, or
	 * {@code null} if there is no value.
	 * @since 2016/03/26
	 */
	public JVMOperatorLink getLink()
	{
		// Lock
		synchronized (lock)
		{
			// Loop until the start
			for (JVMProgramSlot s = this; s != null; s = s.previousPC())
			{
				// Get the type of this variable, this will propogate
				// itself to determine the type. Thus, if this returns
				// NOTHING then no extra work needs to be done.
				// TOP is treated the same way because it destroys values.
				JVMVariableType it = s.getType();
				if (it == JVMVariableType.NOTHING ||
					it == JVMVariableType.TOP)
					return null;
				
				// If it has a link, use it. Note that null may be a valid
				// link if it is cleared
				if (s._haslink)
					return s._link;
			}
			
			// Not found
			return null;
		}
	}
	
	/**
	 * Returns the type of value contained in this slot.
	 *
	 * If there is no value here, then this propogates up the slot mappings
	 * until a type is found. This should never return {@code null}.
	 *
	 * @return The value stored in this slot.
	 * @since 2016/03/25
	 */
	public JVMVariableType getType()
	{
		return getType(false);
	}
	
	/**
	 * Returns the type of value contained in this slot.
	 *
	 * If there is no value here, then this propogates up the slot mappings
	 * until a type is found. This should never return {@code null}.
	 *
	 * @param __iso If {@code true} then stack overflows are ignored.
	 * @return The value stored in this slot.
	 * @since 2016/03/29
	 */
	public JVMVariableType getType(boolean __iso)
	{
		// Lock
		synchronized (lock)
		{
			// Stack elements?
			boolean isstack = variables.isstack;
			int mtop = variables._stacktop;
			int ipos = position;
			
			// Loop until the start
			for (JVMProgramSlot s = this; s != null; s = s.previousPC())
			{
				// When getting types, the top of the stack must be
				// handled because when a get is at or exceeds the top
				// of the stack then it must always return nothing.
				// But might not want stack overflows to stop the value here
				if (isstack && !__iso)
				{
					// Get the top of the stack for this slot
					mtop = Math.min(mtop, s.variables._stacktop);
				
					// Would get chopped off, stop
					if (ipos >= mtop)
						return JVMVariableType.NOTHING;
				}
				
				// Only return if a type was actually set
				JVMVariableType rv = _type;
				if (rv != null)
					return rv;
			}
			
			// Not found, use an implied nothing
			return JVMVariableType.NOTHING;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/25
	 */
	@Override
	public int hashCode()
	{
		// To make map finding easier and allowing slots to be used as keys
		// if needed, the position is used.
		return position;
	}
	
	/**
	 * Returns the next slot following this one, if this is at the end
	 * then an exception may be thrown.
	 *
	 * @param __toss If {@code true} then an exception is thrown.
	 * @return The next slot which follows this one or {@code null} if this
	 * is at the end and an exception is not being thrown.
	 * @since 2016/03/26
	 */
	public JVMProgramSlot nextSlot(boolean __toss)
		throws JVMClassFormatError
	{
		// Calculate the next slot
		int use = position + 1;
		
		// After the end?
		JVMProgramVars vars = variables;
		if (use >= vars.size())
			if (__toss)
				throw new JVMClassFormatError(String.format("IN1z %d",
					use));
			else
				return null;
		
		// Return it
		return vars.get(use);
	}
	
	/**
	 * Returns the slot on the next lower PC address which is at this
	 * position.
	 *
	 * @return The previous slot or {@code null} if this is the first.
	 * @since 2016/03/25
	 */
	public JVMProgramSlot previousPC()
	{
		// Get the previous atom
		JVMProgramAtom patom = variables.atom.previous();
		
		// At the start?
		if (patom == null)
			return null;
		
		// Find the variable type to use
		JVMProgramVars pvars = (variables.isstack ? patom.stack() :
			patom.locals());
		
		// Get the slot here
		return pvars.get(position);
	}
	
	/**
	 * Resets the link.
	 *
	 * @return {@code this}.
	 * @since 2016/03/27
	 */
	public JVMProgramSlot resetLink()
	{
		// Lock
		synchronized (lock)
		{
			_haslink = false;
			_link = null;
		}
		
		// Self
		return this;
	}
	
	/**
	 * Resets the type.
	 *
	 * @return {@code this}.
	 * @since 2016/03/27
	 */
	public JVMProgramSlot resetType()
	{
		// Lock
		synchronized (lock)
		{
			_type = null;
		}
		
		// Self
		return this;
	}
	
	/**
	 * Sets the operator link for this slot which contains the current
	 * calculated value.
	 *
	 * @param __ol The value of this slot.
	 * @return {@code this}.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/26
	 */
	public JVMProgramSlot setLink(JVMOperatorLink __ol)
		throws NullPointerException
	{
		// Check
		if (__ol == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (lock)
		{
			// The slot becomes very real now
			__makeLogical();
			
			// Set
			_link = __ol;
			_haslink = true;
		}
		
		// Set
		return this;
	}
	
	/**
	 * Sets the type of variable this local or stack variable uses.
	 *
	 * @param __vt The variable type to set.
	 * @return {@code this}.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/25
	 */
	public JVMProgramSlot setType(JVMVariableType __vt)
		throws NullPointerException
	{
		// Check
		if (__vt == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (lock)
		{
			// The slot becomes very real now
			__makeLogical();
			
			// Set the type
			_type = __vt;
		}
		
		// Self
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/25
	 */
	@Override
	public String toString()
	{
		// Build string
		StringBuilder sb = new StringBuilder();
		sb.append('{');
		
		// Add position
		sb.append(position);
		
		// Add the ttype
		sb.append(':');
		sb.append(getType());
		
		// The operator chain
		JVMOperatorLink ol = getLink();
		if (ol != null)
		{
			sb.append('=');
			sb.append(ol);
		}
		
		// Finish
		sb.append('}');
		return sb.toString();
	}
	
	/**
	 * Returns the unique identifier of this slot.
	 *
	 * @return The slot unique identifier.
	 * @since 2016/03/26
	 */
	public long unique()
	{
		JVMProgramVars vars = variables;
		return (vars.isstack ? 0x8000_0000__0000_0000L : 0) |
			(((long)vars.atom.pcaddr) << 32L) | ((long)position);
	}
	
	/**
	 * Makes this slot logical and places it into the list of slots used
	 * by the owning variables. This means that it will not go away and is
	 * no longer a virtually cached slot.
	 *
	 * @return {@code this}.
	 * @since 2016/03/25
	 */
	private JVMProgramSlot __makeLogical()
	{
		// Lock
		synchronized (lock)
		{
			// If already logical, ignore
			if (_logpos >= 0)
				return this;
			
			// Get the slots to insert into
			JVMProgramVars vars = variables;
			List<JVMProgramSlot> act = vars._dslots;
			
			// Find the insertion point of this slot
			int pos = Collections.<Object>binarySearch(act, position,
				JVMProgramVars._SLOT_COMPARATOR);
			
			// Cannot be happening
			if (pos >= 0)
				throw new IllegalStateException(String.format("WTFX %d",
					pos));
			
			// Insert it there
			int at = (-pos) - 1;
			act.add(at, this);
			
			// Correct logical positions for everything
			int n = act.size();
			for (int i = at; i < n; i++)
				act.get(i)._logpos = i;
				
			// Destroy the reference because it is no longer needed
			vars._vslots.remove(this);
		}
		
		// Self
		return this;
	}
	
	/**
	 * Resets the state of this slot and clears the type and link.
	 *
	 * @return {@code this}.
	 * @since 2016/03/27
	 */
	private JVMProgramSlot __resetAll()
	{
		// Lock
		synchronized (lock)
		{
			resetType();
			resetLink();
		}
		
		// Self
		return this;
	}
}

