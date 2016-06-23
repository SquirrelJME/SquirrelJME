// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.java.ci;

import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;
import net.multiphasicapps.squirreljme.java.symbols.IdentifierSymbol;
import net.multiphasicapps.squirreljme.java.symbols.MethodSymbol;

/**
 * This refers to another method.
 *
 * @since 2016/04/24
 */
public final class CIMethodReference
	extends CIMemberReference<MethodSymbol>
{
	/** Does this refer to an interface? */
	protected final boolean isinterface;
	
	/**
	 * Initializes the method reference.
	 *
	 * @param __ii Is this an interface?
	 * @param __cl The class it is in.
	 * @param __name The name of the member.
	 * @param __type The type of the member.
	 * @since 2016/04/24
	 */
	public CIMethodReference(boolean __ii, ClassNameSymbol __cl,
		IdentifierSymbol __name, MethodSymbol __type)
	{
		super(__cl, __name, __type);
		
		// Set
		isinterface = __ii;
	}
	
	/**
	 * Returns {@code true} if this refers to an interface.
	 *
	 * @return {@code true} if an interface.
	 * @since 2016/04/24
	 */
	public boolean isInterface()
	{
		return isinterface;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/24
	 */
	@Override
	public CIPoolTag tag()
	{
		return (isinterface ? CIPoolTag.INTERFACEMETHODREF :
			CIPoolTag.METHODREF);
	}
}

