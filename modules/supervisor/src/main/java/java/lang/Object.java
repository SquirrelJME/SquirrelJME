// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang;

import cc.squirreljme.jvm.Assembly;
import cc.squirreljme.jvm.CallStackItem;
import cc.squirreljme.jvm.Constants;
import cc.squirreljme.jvm.JVMFunction;
import cc.squirreljme.jvm.SystemCallIndex;

/**
 * This class defines the base class for every class which exists.
 *
 * @since 2019/05/25
 */
public class Object
{
	/**
	 * Clones this object.
	 *
	 * @return The clone of this object.
	 * @since 2019/05/26
	 */
	protected Object clone()
		throws CloneNotSupportedException
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Returns the class type for this object.
	 *
	 * @return The class type.
	 * @since 2019/05/26
	 */
	public final Class<?> getClass()
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Returns the hash code for this object.
	 *
	 * @return The object hash code.
	 * @since 2019/05/25
	 */
	public int hashCode()
	{
		return Assembly.objectToPointer(this);
	}
	
	/**
	 * Checks if this object is equal to another object.
	 *
	 * @param __o The object to check.
	 * @return If the objects are equal.
	 * @since 2019/05/25
	 */
	public boolean equals(Object __o)
	{
		return this == __o;
	}
	
	/**
	 * Returns the string representation of this object.
	 *
	 * @return The string representation.
	 * @since 2019/05/25
	 */
	public String toString()
	{
		return JVMFunction.jvmLoadString(Assembly.pointerToClassInfo(
			Assembly.memReadInt(Assembly.objectToPointer(this),
			Constants.OBJECT_CLASS_OFFSET)).namep) +
			"@" + Integer.toString(this.hashCode(), 16);
	}
}
