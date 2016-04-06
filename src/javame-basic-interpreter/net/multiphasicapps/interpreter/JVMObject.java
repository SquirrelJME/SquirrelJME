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

/**
 * This represents an object within the interpreter.
 *
 * @since 2016/03/01
 */
public class JVMObject
{
	/** The owning object manager. */
	protected final JVMObjects objects;
	
	/** The class this object is. */
	protected final JVMClass classtype;
	
	/**
	 * Initializes the object, which may be an array.
	 *
	 * @param __objs Object manager.
	 * @param __type The array type.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/05
	 */
	JVMObject(JVMObjects __objs, JVMClass __type)
		throws NullPointerException
	{
		// Check
		if (__objs == null || __type == null)
			throw new NullPointerException("NARG");
		
		// Set
		classtype = __type;
		objects = __objs;
		
		// Register this object with the engine
		objects.__registerObject(this);
	}
	
	/**
	 * Returns the identity hashcode of the object this represents. In the
	 * case of this interpreter that hash code will be the same identity hash
	 * code as it is on the host virtual machine. This way objects have a one
	 * to one mapping.
	 *
	 * @return The identity hash code of this object.
	 * @since 2016/04/05
	 */
	public int identityHashCode()
	{
		return System.identityHashCode(this);
	}
	
	/**
	 * Returns {@code true} if this is an array, otherwise false.
	 *
	 * @return If this is an array or not.
	 * @since 2016/04/06
	 */
	public boolean isArray()
	{
		return false;
	}
	
	/**
	 * Returns the object manager which manages this object.
	 *
	 * @return The owning object manager.
	 * @since 2016/04/05
	 */
	public JVMObjects objects()
	{
		return objects;
	}
}

