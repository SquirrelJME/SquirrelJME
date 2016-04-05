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
public final class JVMObject
{
	/** The owning engine. */
	protected final JVMEngine engine;
	
	/** The class this object is. */
	protected final JVMClass classtype;
	
	/** The length of this array . */
	protected final int arraylength;
	
	/**
	 * Initializes the object, which may be an array.
	 *
	 * @param __type The array type.
	 * @param __length The length of the array, if not an array type this must
	 * be negative.
	 * @throws IllegalStateException If 
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/05
	 */
	JVMObject(JVMClass __type, int __length)
		throws IllegalStateException, NullPointerException
	{
		// Check
		if (__type == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error LI0d Attempting to create a new instance of an
		// object which is or is not an array type yet if it is an array it has
		// negative length. If not an array then it has positive length.
		// (The type; The length of the array)}
		if (__type.isArray() != (__length >= 0))
			throw new IllegalStateException(String.format("LI0d %s %d", __type,
				__length));
		
		// Set
		classtype = __type;
		engine = classtype.engine();
		arraylength = (__length >= 0 ? __length : Integer.MIN_VALUE);
		
		// Register this object with the engine
		engine.__registerObject(this);
	}
	
	/**
	 * Returns the engine which owns this object.
	 *
	 * @return The owning engine.
	 * @since 2016/04/05
	 */
	public JVMEngine engine()
	{
		return engine;
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
}

