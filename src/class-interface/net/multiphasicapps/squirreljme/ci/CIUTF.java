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

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import net.multiphasicapps.squirreljme.java.symbols.BinaryNameSymbol;
import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;
import net.multiphasicapps.squirreljme.java.symbols.IdentifierSymbol;
import net.multiphasicapps.squirreljme.java.symbols.MemberTypeSymbol;

/**
 * This represents a UTF-8 constant entry.
 *
 * @since 2016/04/24
 */
public final class CIUTF
	implements CharSequence, CIPoolEntry
{
	/** Internally read string. */
	protected final String string;
	
	/** This string as a member type symbol. */
	private volatile Reference<MemberTypeSymbol> _memtype;
	
	/** The string as the name of a class. */
	private volatile Reference<ClassNameSymbol> _classname;
	
	/** The string as an identifier. */
	private volatile Reference<IdentifierSymbol> _id;
	
	/**
	 * Initializes the constant with a value.
	 *
	 * @param __v The string which makes up this value.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/24
	 */
	public CIUTF(String __v)
		throws NullPointerException
	{
		// Check
		if (__v == null)
			throw new NullPointerException("NARG");
		
		// Set
		string = __v.intern();
	}
	
	/**
	 * Returns the current string as a binary name.
	 *
	 * @return The binary name representing this symbol.
	 * @since 2016/05/08
	 */
	public BinaryNameSymbol asBinaryName()
	{
		return asClassName().asBinaryName();
	}
	
	/**
	 * Returns the string as a class name.
	 *
	 * @return The string as a class name symbol.
	 * @since 2016/04/25
	 */
	public ClassNameSymbol asClassName()
	{
		// Get reference
		Reference<ClassNameSymbol> ref = _classname;
		ClassNameSymbol rv;
		
		// Needs caching?
		if (ref == null || null == (rv = ref.get()))
			_classname = new WeakReference<>(
				(rv = ClassNameSymbol.of(toString())));
		
		// Return it
		return rv;
	}
	
	/**
	 * Returns the string as an identifier symbol.
	 *
	 * @return The string as an identifier symbol.
	 * @since 2016/04/25
	 */
	public IdentifierSymbol asIdentifier()
	{
		// Get reference
		Reference<IdentifierSymbol> ref = _id;
		IdentifierSymbol rv;
		
		// Needs caching?
		if (ref == null || null == (rv = ref.get()))
			_id = new WeakReference<>(
				(rv = IdentifierSymbol.of(toString())));
		
		// Return it
		return rv;
	}
	
	/**
	 * Returns the string data contained here as a member.
	 *
	 * @return This string as a member symbol.
	 * @since 2016/04/25
	 */
	public MemberTypeSymbol asMember()
	{
		// Get reference
		Reference<MemberTypeSymbol> ref = _memtype;
		MemberTypeSymbol rv;
		
		// Needs caching?
		if (ref == null || null == (rv = ref.get()))
			_memtype = new WeakReference<>(
				(rv = MemberTypeSymbol.of(toString())));
		
		// Return it
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/13
	 */
	@Override
	public char charAt(int __i)
	{
		return string.charAt(__i);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/13
	 */
	@Override
	public int length()
	{
		return string.length();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/13
	 */
	@Override
	public CharSequence subSequence(int __s, int __e)
	{
		return string.subSequence(__s, __e);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/24
	 */
	@Override
	public CIPoolTag tag()
	{
		return CIPoolTag.UTF8;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/13
	 */
	@Override
	public String toString()
	{
		return string;
	}
}

