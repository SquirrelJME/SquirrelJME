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

import net.multiphasicapps.descriptors.FieldSymbol;
import net.multiphasicapps.descriptors.IdentifierSymbol;

/**
 * This represents fields which exist in a class.
 *
 * @since 2016/03/19
 */
public class CFFields
	extends CFMembers<FieldSymbol, CFFieldFlags, CFField>
{
	/**
	 * Initializes the field mappings.
	 *
	 * @param __own The owning class.
	 * @since 2016/03/20
	 */
	public CFFields(CFClass __own)
	{
		super(__own, CFField.class);
		
		throw new Error("TODO");
		/*
		// FIELD
		// Get class flags
		CFClassFlags cl = inclass.getFlags();
		
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
		*/
	}
}

