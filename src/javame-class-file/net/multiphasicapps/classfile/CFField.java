// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

import java.util.Set;
import net.multiphasicapps.descriptors.FieldSymbol;
import net.multiphasicapps.descriptors.IdentifierSymbol;

/**
 * This represents a field which is defined in a class.
 *
 * @since 2016/03/17
 */
public class CFField
	extends CFMember<FieldSymbol, CFFieldFlags>
{
	/** The constant value of the field. */
	private volatile Object _constant;
	
	/**
	 * Initializes the interpreted method.
	 *
	 * @param __owner The class which owns this method.
	 * @param __nat The name and type of the field.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/01
	 */
	public CFField(JVMClass __owner, CFMemberKey<FieldSymbol> __nat)
		throws NullPointerException
	{
		super(__owner, FieldSymbol.class, __nat, CFFieldFlags.class);
	}
	
	/**
	 * Returns the field constant value, if any has been set.
	 *
	 * @return The constant value or {@code null} if there is no value set.
	 * @since 2016/04/01
	 */
	public Object getConstantValue()
	{
		// Lock
		synchronized (lock)
		{
			return _constant;
		}
	}
	
	/**
	 * Sets the constant value of this field.
	 *
	 * @param __cv The field constant value.
	 * @return {@code this}.
	 * @throws ClassCastException If the constant is not a valid constant and
	 * primitive type.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/01
	 */
	public CFField setConstantValue(Object __cv)
		throws ClassCastException, NullPointerException
	{
		// Check
		if (__cv == null)
			throw new NullPointerException("NARG");
		if (!(__cv instanceof Boolean || __cv instanceof Byte ||
			__cv instanceof Short || __cv instanceof Character ||
			__cv instanceof Integer || __cv instanceof Long ||
			__cv instanceof Float || __cv instanceof Double ||
			__cv instanceof String))
			throw new ClassCastException(String.format("IN2q %s",
				__cv.getClass()));
		
		// Lock
		synchronized (lock)
		{
			// Set
			_constant = __cv;
		}
		
		// Self
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/20
	 */
	@Override
	public CFField setFlags(CFFieldFlags __fl)
		throws CFFormatException, NullPointerException
	{
		// Check
		if (__fl == null)
			throw new NullPointerException("NARG");
		
		// Get class flags
		JVMClassFlags cl = inclass.getFlags();
		
		// If an interface
		if (cl.isInterface())
		{
			// Must have these flags set and some not set
			if ((!__fl.isPublic() || !__fl.isStatic() || !__fl.isFinal()) ||
				__fl.isProtected() || __fl.isPrivate() || __fl.isVolatile() ||
				__fl.isTransient() || __fl.isEnum())
				throw new CFFormatException(String.format("IN1c %s %s",
						__fl, cl));
		}
		
		// Continue with super call
		return (CFField)super.setFlags(__fl);
	}
}

