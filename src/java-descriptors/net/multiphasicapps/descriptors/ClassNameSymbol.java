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

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This is the name of the class as it appears in the constant pool of a class.
 *
 * It is either a {@link FieldSymbol} or a {@link BinaryNameSymbol}.
 *
 * @since 2016/03/15
 */
public final class ClassNameSymbol
	extends __BaseSymbol__
{
	/** Is this an array? */
	protected final boolean isarray;
	
	/** As a field symbol. */
	private volatile Reference<FieldSymbol> _asfield;
	
	/** As a binary name symbol. */
	private volatile Reference<BinaryNameSymbol> _asbinary;
	
	/**
	 * Initializes the class name symbol.
	 *
	 * @throws IllegalSymbolException If the class name is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/15
	 */
	public ClassNameSymbol(String __s)
		throws IllegalSymbolException, NullPointerException
	{
		super(__s);
		
		// Is an array?
		isarray = charAt(0) == '[';
		
		// Check it by making it
		if (isarray)
			asField();
		else
			asBinaryName();
	}
	
	/**
	 * Returns the class name as a binary name.
	 *
	 * @return The binary name representation of the class.
	 * @throws IllegalSymbolException If this is an array or it is not a valid.
	 * @since 2016/03/15
	 */
	public BinaryNameSymbol asBinaryName()
		throws IllegalSymbolException
	{
		// Arrays will never be compatible
		if (isarray)
			throw new IllegalSymbolException(String.format("DS04 %s",
				toString()));
		
		// Get reference
		Reference<BinaryNameSymbol> ref = _asbinary;
		BinaryNameSymbol rv = null;
		
		// In the ref?
		if (ref != null)
			rv = ref.get();
		
		// Needs creation?
		if (rv == null)
			_asbinary = new WeakReference<>(
				(rv = new BinaryNameSymbol(toString())));
		
		// Return it
		return rv;
	}
	
	/**
	 * Returns the class name as a field.
	 *
	 * @return The field representation of the class.
	 * @throws IllegalSymbolException If this would produce an invalid field
	 * symbol.
	 * @since 2016/03/15
	 */
	public FieldSymbol asField()
		throws IllegalSymbolException
	{
		// Get reference
		Reference<FieldSymbol> ref = _asfield;
		FieldSymbol rv = null;
		
		// In the ref?
		if (ref != null)
			rv = ref.get();
		
		// Needs creation?
		if (rv == null)
			_asfield = new WeakReference<>((rv = new FieldSymbol(
				(isarray ? toString() : 'L' + toString() + ';'))));
		
		// Return it
		return rv;
	}
	
	/**
	 * Is this an array?
	 *
	 * @return {@code true} if it is an array.
	 * @since 2016/03/15
	 */
	public boolean isArray()
	{
		return isarray;
	}
}

