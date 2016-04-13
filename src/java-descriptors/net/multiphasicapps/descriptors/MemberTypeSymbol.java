// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.descriptors;

/**
 * This is a symbol which is used as a type for a field or a method.
 *
 * @since 2016/03/15
 */
public abstract class MemberTypeSymbol
	extends __BaseSymbol__
{
	/**
	 * Initializes the symbol.
	 *
	 * @since 2016/03/15
	 */
	MemberTypeSymbol(String __s)
	{
		super(__s);
	}
	
	/**
	 * Returns this member type symbol as a field symbol.
	 *
	 * @return This as a field symbol.
	 * @throws ClassCastException If this is not a field symbol.
	 * @since 2016/03/17
	 */
	public final FieldSymbol asField()
		throws ClassCastException
	{
		return (FieldSymbol)this;
	}
	
	/**
	 * Returns this member type symbol as a method symbol.
	 *
	 * @return This as a method symbol.
	 * @throws ClassCastException If this is not a method symbol.
	 * @since 2016/03/17
	 */
	public final MethodSymbol asMethod()
		throws ClassCastException
	{
		return (MethodSymbol)this;
	}
	
	/**
	 * Creates a new symbol from the specified string.
	 *
	 * @param __sym The symbol to decode.
	 * @return The member type symbol.
	 * @throws IllegalSymbolException If the symbol is not valid for members.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/18
	 */
	public static MemberTypeSymbol create(String __sym)
		throws IllegalSymbolException, NullPointerException
	{
		// Check
		if (__sym == null)
			throw new NullPointerException("NARG");
		
		// If it starts with (, it is a method
		if (__sym.startsWith("("))
			return new MethodSymbol(__sym);
		
		// Otherwise a field
		return new FieldSymbol(__sym);
	}
}

