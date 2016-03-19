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
 * This represents a field descriptor.
 *
 * @since 2016/03/14
 */
public final class FieldSymbol
	extends MemberTypeSymbol
{
	/** Maximum array size. */
	public static final int MAX_ARRAY_DIMENSIONS =
		255;
	/** Array dimensions, will be zero if not an array. */
	protected final int dimensions;
	
	/** Component type of the array if it is one. */
	private volatile Reference<FieldSymbol> _componenttype;
	
	/** Base type for the field? */
	private volatile Reference<FieldBaseTypeSymbol> _basetype;
	
	/**
	 * Initializes the field symbol which represents the type of a field.
	 *
	 * @param __s Field descriptor data.
	 * @throws IllegalSymbolException If the field descriptor is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/14
	 */
	public FieldSymbol(String __s)
		throws IllegalSymbolException, NullPointerException
	{
		super(__s);
		
		// Read the number of dimensions this has, this modifies how the symbol
		// is handled.
		int n = length();
		int i;
		for (i = 0;	i < n; i++)
			if (charAt(i) != '[')
				break;
		
		// Set
		dimensions = i;
		if (dimensions < 0 || dimensions > MAX_ARRAY_DIMENSIONS)
			throw new IllegalSymbolException(String.format("DS07 %s %d",
				this, dimensions));
		
		// Just cache all of them to check for symbol validity
		binaryName();
		baseType();
	}
	
	/**
	 * Returns the number of dimensions in the array.
	 *
	 * @return The dimensions in the array, or {@code 0} if not an array.
	 * @since 2016/03/14
	 */
	public int arrayDimensions()
	{
		return dimensions;
	}
	
	/**
	 * This returns the base type that the field is.
	 *
	 * @return The base type of the field or {@code null} if it is an array.
	 * @since 2016/03/19
	 */
	public FieldBaseTypeSymbol baseType()
	{
		// Not valid if an array
		if (dimensions != 0)
			return null;
		
		throw new Error("TODO");
	}
	
	/**
	 * Returns the binary name of the field.
	 *
	 * @return The binary name of the field or {@code null} if it does not
	 * represent a class type.
	 * @since 2016/03/19
	 */
	public BinaryNameSymbol binaryName()
	{
		// If the base type is a binary name then use it
		FieldBaseTypeSymbol rv = baseType();
		if (rv instanceof BinaryNameSymbol)
			return (BinaryNameSymbol)rv;
		return null;
	}
	
	/**
	 * Returns the component of the array.
	 *
	 * @return The component type of the array or {@code null} if not an
	 * array.
	 * @since 2016/03/14
	 */
	public FieldSymbol componentType()
	{
		// Not valid if not an array
		if (dimensions != 0)
			return null;
		
		throw new Error("TODO");
	}
	
	/**
	 * Returns the primitive type of the field.
	 *
	 * @return The primitive type or {@code null} if not one.
	 * @since 2016/03/19
	 */
	public PrimitiveSymbol primitiveType()
	{
		// If the base type is a primitive type then use it
		FieldBaseTypeSymbol rv = baseType();
		if (rv instanceof PrimitiveSymbol)
			return (PrimitiveSymbol)rv;
		return null;
	}
}

