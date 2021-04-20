// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.jdwp.JDWPState;
import cc.squirreljme.jdwp.JDWPValue;
import cc.squirreljme.jdwp.views.JDWPViewObject;
import cc.squirreljme.jdwp.trips.JDWPTripValue;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.lang.ref.Reference;

/**
 * This is a view around an object within the debugger.
 *
 * @since 2021/04/10
 */
public class DebugViewObject
	implements JDWPViewObject
{
	/** The state of the debugger. */
	protected final Reference<JDWPState> state;
	
	/**
	 * Initializes the object viewer.
	 * 
	 * @param __state The state.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/04/10
	 */
	public DebugViewObject(Reference<JDWPState> __state)
		throws NullPointerException
	{
		if (__state == null)
			throw new NullPointerException("NARG");
		
		this.state = __state;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/10
	 */
	@Override
	public boolean isValid(Object __which)
	{
		return (__which instanceof SpringObject);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/11
	 */
	@Override
	public int arrayLength(Object __which)
	{
		if (__which instanceof SpringArrayObject)
			return ((SpringArrayObject)__which).length;
		return -1;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/11
	 */
	@Override
	public boolean readArray(Object __which, int __index, JDWPValue __out)
	{
		__out.set(DebugViewObject.__normalizeNull(
			((SpringArrayObject)__which).get(Object.class, __index)));
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/10
	 */
	@Override
	public boolean readValue(Object __which, int __index, JDWPValue __out)
	{
		// Nulls never have a value
		if (__which == SpringNullObject.NULL)
			return false;
		
		// Is a simple object representation, so we can read a field value
		// directly from the object representation without knowing about
		// the class details.
		if (__which instanceof SpringSimpleObject)
		{
			SpringFieldStorage[] store = ((SpringSimpleObject)__which)._fields;
			if (__index >= 0 && __index < store.length)
			{
				__out.set(DebugViewObject.__normalizeNull(
					store[__index].get()));
				return true;
			}
		}
		
		// Not a valid object or one where a value can be read from
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/11
	 */
	@Override
	public boolean setTrip(Object __which, int __index, JDWPTripValue __trip)
	{
		// Simple representation of an object
		if (__which instanceof SpringSimpleObject)
		{
		}
		
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/11
	 */
	@Override
	public Object type(Object __which)
	{
		return ((SpringObject)__which).type();
	}
	
	/**
	 * Normalizes a null value so null references become null and not the
	 * special holder value.
	 * 
	 * @param __o The value to normalize.
	 * @return If the value points to a null object it becomes null.
	 * @since 2021/04/11
	 */
	static Object __normalizeNull(Object __o)
	{
		if (__o == SpringNullObject.NULL)
			return null;
		return __o;
	}
}
