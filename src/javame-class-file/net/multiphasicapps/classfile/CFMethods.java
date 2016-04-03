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
import net.multiphasicapps.descriptors.IdentifierSymbol;
import net.multiphasicapps.descriptors.MethodSymbol;

/**
 * This represents methods which exist in a class.
 *
 * @since 2016/03/19
 */
public class CFMethods
	extends CFMembers<MethodSymbol, CFMethodFlags, CFMethod>
{
	/**
	 * Initializes the method mappings.
	 *
	 * @param __own The owning class.
	 * @param __s The methods to source from.
	 * @since 2016/03/20
	 */
	public CFMethods(CFClass __own,
		Map<CFMemberKey<MethodSymbol>, CFMethod> __s)
	{
		super(__own, CFMethod.class, __s);
		
		// Check the flags for all members
		CFClassFlags cl = owner.flags();
		for (CFMethod m : values())
		{
			// Initializer flags are ignored
			if (!m.isClassInitializer())
				continue;
			
			// Get flags
			CFMethodFlags fl = m.flags();
		
			// If an interface
			if (cl.isInterface())
			{
				// {@squirreljme.error CF11 Default members and private methods
				// in interfaces are not supported. (The method flags;
				// The class flags)}
				if (fl.isPrivate() || !fl.isAbstract())
					throw new CFFormatException(String.format("CF11 %s %s",
						fl, cl));
				
				// {@squirreljme.error CF12 Interface methods cannot be
				// {@code protected}, {@code final}, {@code synchronized}, or
				// {@code native}. (The method flags; The class flags)}
				if (fl.isProtected() || fl.isFinal() ||
					fl.isSynchronized() || fl.isNative())
					throw new CFFormatException(String.format("CF12 %s %s",
						fl, cl));
			}
		}
	}
}

