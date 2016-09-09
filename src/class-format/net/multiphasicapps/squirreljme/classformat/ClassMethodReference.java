// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;
import net.multiphasicapps.squirreljme.java.symbols.IdentifierSymbol;
import net.multiphasicapps.squirreljme.java.symbols.MethodSymbol;

/**
 * This represents a reference to an interface method or a standard method.
 *
 * @since 2016/08/14
 */
public final class JITMethodReference
	extends JITMemberReference
{
	/** Is this an interface method reference? */
	protected final boolean isinterface;
	
	/**
	 * Initializes the method reference.
	 *
	 * @param __cn The class name.
	 * @param __mn The member name.
	 * @param __mt the member type.
	 * @since 2016/08/14
	 */
	public JITMethodReference(ClassNameSymbol __cn, IdentifierSymbol __mn,
		MethodSymbol __mt, boolean __int)
	{
		super(__cn, __mn, __mt);
		
		// Set
		this.isinterface =__int;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/14
	 */
	@Override
	public boolean equals(Object __o)
	{
		return super.equals(__o) && (__o instanceof JITMethodReference) &&
			this.isinterface == ((JITMethodReference)__o).isinterface;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/14
	 */
	@Override
	public int hashCode()
	{
		return super.hashCode() ^ (this.isinterface ? 0xFFFFFFFF : 0);
	}
	
	/**
	 * Is this an interface method reference?
	 *
	 * @return {@code true} if this refers to an interface method.
	 * @since 2016/08/14
	 */
	public final boolean isInterfaceMethod()
	{
		return this.isinterface;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/14
	 */
	@Override
	public MethodSymbol memberType()
	{
		return (MethodSymbol)this.membertype;
	}
	
	/**
	 * Returns the type of method that this is.
	 *
	 * @return The method type.
	 * @since 2016/09/06
	 */
	public MethodSymbol methodType()
	{
		return (MethodSymbol)this.membertype;
	}
}

