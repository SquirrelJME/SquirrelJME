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
import net.multiphasicapps.descriptors.IdentifierSymbol;
import net.multiphasicapps.descriptors.MethodSymbol;

/**
 * This represents an interpreted method within a class.
 *
 * @since 2016/03/01
 */
public class JVMMethod
	extends JVMMember<MethodSymbol>
{
	/**
	 * Initializes the interpreted method.
	 *
	 * @param __owner The class which owns this method.
	 * @param __name The name of the method.
	 * @param __type The type of the method.
	 * @param __fl The flags this method uses.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/01
	 */
	public JVMMethod(JVMClass __owner, IdentifierSymbol __name,
		MethodSymbol __type, Set<JVMMethodFlag> __fl)
		throws NullPointerException
	{
		super(__owner, MethodSymbol.class, __name, __type, __fl);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/19
	 */
	@Override
	@SuppressWarnings({"unchecked"})
	public Set<JVMMethodFlag> flags()
	{
		return (Set<JVMMethodFlag>)flags;
	}
	
	/**
	 * Is this method public?
	 *
	 * @return {@code true} if this method is public.
	 * @since 2016/03/01
	 */
	public boolean isPublic()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Is this method static?
	 *
	 * @return {@code true} if this method is static.
	 * @since 2016/03/01
	 */
	public boolean isStatic()
	{
		throw new Error("TODO");
	}
}

