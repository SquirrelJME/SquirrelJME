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
		
		throw new Error("TODO");
		/*
		// METHOD
				// Class initializer flags are ignored for the most part
		if (!isClassInitializer())
		{
			// If the class is an interface...
			if (cl.isInterface())
			{
				// Default methods are not supported
				if (__fl.isPrivate() || !__fl.isAbstract())
					throw new CFFormatException(String.format("IN19 %s",
						__fl));
				
				// Cannot have these flags
				if (__fl.isProtected() || __fl.isFinal() ||
					__fl.isSynchronized() || __fl.isNative())
					throw new CFFormatException(String.format("IN1a %s %s",
						__fl, cl));
			}
		*/
	}
}

