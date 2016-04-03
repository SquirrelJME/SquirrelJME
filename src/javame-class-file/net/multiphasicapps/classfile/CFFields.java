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

import java.util.Map;
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
	 * @param __s The field mapping to source from.
	 * @since 2016/03/20
	 */
	public CFFields(CFClass __own, Map<CFMemberKey<FieldSymbol>, CFField> __s)
	{
		super(__own, CFField.class, __s);
		
		// Check the flags for all members
		CFClassFlags cl = owner.flags();
		for (CFField m : values())
		{
			// Get flags
			CFFieldFlags fl = m.flags();
		
			// If an interface
			if (cl.isInterface())
			{
				// {@squirreljme.error IN1c Fields of interfaces must always
				// be {@code public static final}. (The flags for this field;
				// The flags for this method)}
				if ((!fl.isPublic() || !fl.isStatic() || !fl.isFinal()) ||
					fl.isProtected() || fl.isPrivate() || fl.isVolatile() ||
					fl.isTransient() || fl.isEnum())
					throw new CFFormatException(String.format("IN1c %s %s",
							fl, cl));
			}
		}
	}
}

