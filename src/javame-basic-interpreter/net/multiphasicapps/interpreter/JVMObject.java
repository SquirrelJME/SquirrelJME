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
import net.multiphasicapps.descriptors.ClassNameSymbol;

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
	
	/** The class object this uses. */
	protected final JVMObject classobject;
	
	/** Is the class object a special null? (This is Class) */
	protected final boolean specialclassobject;
	
	/** String representation of this object. */
	private volatile Reference<String> _stringrep;
	
	/**
	 * Initializes the object, which may be an array.
	 *
	 * @param __objs Object manager.
	 * @param __th The thread creating this object.
	 * @param __type The array type.
	 * @throws NullPointerException On null arguments, except for {@code __th}.
	 * @since 2016/04/05
	 */
	JVMObject(JVMObjects __objs, JVMThread __th, JVMClass __type)
		throws NullPointerException
	{
		// Check
		if (__objs == null || __type == null)
			throw new NullPointerException("NARG");
		
		// Set
		classtype = __type;
		objects = __objs;
		
		// Get the class object, this is the value which would be returned
		// by the getClass() method.
		ClassNameSymbol cn = __type.thisName();
		specialclassobject = cn.equals("java/lang/Class");
		classobject = (specialclassobject ? null :
			classtype.classObject(__th));
		
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
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/07
	 */
	@Override
	public String toString()
	{
		// Get reference
		Reference<String> ref = _stringrep;
		String rv;
		
		// In a reference?
		if (ref == null || null == (rv = ref.get()))
			_stringrep = new WeakReference<>((rv = String.format("%s@%08x",
				classtype.thisName().toString(), identityHashCode())));
		
		// Return it
		return rv;
	}
}

