// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.ci;

import net.multiphasicapps.descriptors.IdentifierSymbol;
import net.multiphasicapps.descriptors.MethodSymbol;

/**
 * This identifies a method by its name and type.
 *
 * @since 2016/04/22
 */
public final class CIMethodID
	extends CIMemberID<MethodSymbol>
{
	/**
	 * Initializes the method identifier.
	 *
	 * @param __n The name of this method.
	 * @param __t The type of this member.
	 * @throws CIException If the method name is not valid.
	 * @since 2016/04/22
	 */
	public CIMethodID(IdentifierSymbol __n, MethodSymbol __t)
		throws CIException
	{
		super(__n, __t);
		
		// {@squirreljme.error AO20 The specified name is not a valid name for
		// a method. (The name of the method)}
		if (!this.name.isValidMethod())
			throw new CIException(String.format("AO20 %s", __n));
		
		// {@squirreljme.error AO09 The static initializer for a class must
		// take no arguments and return void. (The type of the method method)}
		if (isStaticInitializer() && __t.equals("()V"))
			throw new CIException(String.format("AO09 s%", __t));
	}
	
	/**
	 * Returns true if this method is named for a constructor of a class.
	 *
	 * @return {@code true} if this is a constructor.
	 * @since 2016/05/28
	 */
	public final boolean isConstructor()
	{
		return this.name.isConstructor();
	}
	
	/**
	 * Returns true if this method is named for the static initializer of a
	 * class.
	 *
	 * @return {@code true} if this is the static initializer.
	 * @since 2016/05/28
	 */
	public final boolean isStaticInitializer()
	{
		return this.name.isStaticInitializer();
	}
}

