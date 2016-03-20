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

import java.util.Set;
import net.multiphasicapps.descriptors.FieldSymbol;
import net.multiphasicapps.descriptors.IdentifierSymbol;

/**
 * This represents a field which is defined in a class.
 *
 * @since 2016/03/17
 */
public class JVMField
	extends JVMMember<FieldSymbol, JVMFieldFlags>
{
	/**
	 * Initializes the interpreted method.
	 *
	 * @param __owner The class which owns this method.
	 * @param __nat The name and type of the field.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/01
	 */
	public JVMField(JVMClass __owner, JVMMemberKey<FieldSymbol> __nat)
		throws NullPointerException
	{
		super(__owner, FieldSymbol.class, __nat, JVMFieldFlags.class);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/20
	 */
	@Override
	public JVMField setFlags(JVMFieldFlags __fl)
		throws JVMClassFormatError, NullPointerException
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
				throw new JVMClassFormatError(String.format("IN1c %s %s",
						__fl, cl));
		}
		
		// Continue with super call
		return (JVMField)super.setFlags(__fl);
	}
}

