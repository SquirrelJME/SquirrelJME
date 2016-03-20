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
	extends JVMMember<MethodSymbol, JVMMethodFlags>
{
	/**
	 * Initializes the interpreted method.
	 *
	 * @param __owner The class which owns this method.
	 * @param __name The name of the method.
	 * @param __type The type of the method.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/01
	 */
	public JVMMethod(JVMClass __owner, IdentifierSymbol __name,
		MethodSymbol __type)
		throws NullPointerException
	{
		super(__owner, MethodSymbol.class, __name, __type,
			JVMMethodFlags.class);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/20
	 */
	@Override
	public boolean isFinal()
	{
		return getFlags().contains(JVMMethodFlag.FINAL);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/20
	 */
	@Override
	public boolean isPrivate()
	{
		return getFlags().contains(JVMMethodFlag.PRIVATE);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/20
	 */
	@Override
	public boolean isProtected()
	{
		return getFlags().contains(JVMMethodFlag.PROTECTED);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/20
	 */
	@Override
	public boolean isPublic()
	{
		return getFlags().contains(JVMMethodFlag.PUBLIC);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/20
	 */
	@Override
	public boolean isStatic()
	{
		return getFlags().contains(JVMMethodFlag.STATIC);
	}
}

