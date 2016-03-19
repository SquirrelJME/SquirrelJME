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
	
	/** Component type of the array if it is one. */
	protected final FieldSymbol componenttype;
	
	/** Array dimensions, will be zero if not an array. */
	protected final int dimensions;
	
	/** Primitive type? */
	protected final PrimitiveType primitive;
	
	/** Class type? */
	protected final BinaryNameSymbol classtype;
	
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
		
		// Is an array?
		int n = length();
		if (charAt(0) == '[')
		{
			// Read the dimensions
			int i;
			for (i = 0;	i < n; i++)
				if (charAt(i) != '[')
					break;
			
			// Just arrays?
			if (i >= n)
				throw new IllegalSymbolException(String.format("DS05 %s",
					this));
			
			// Set
			dimensions = i;
			if (dimensions < 0 || dimensions > MAX_ARRAY_DIMENSIONS)
				throw new IllegalSymbolException(String.format("DS07 %s %d",
					this, dimensions));
			
			// Decode field for it
			componenttype = new FieldSymbol(toString().substring(i));
			
			// These are not used here
			primitive = null;
			classtype = null;
		}
		
		// Not an array
		else
		{
			// Not used
			dimensions = 0;
			componenttype = null;
			
			// Depends on what the specifier character is
			char spec = charAt(0);
			
			// Is a class?
			if (spec == 'L')
			{
				// Will never be primitive
				primitive = null;
				
				// The last character must be a ;
				if (charAt(length() - 1) != ';')
					throw new IllegalSymbolException(String.format("DS09 %s",
						this));
				
				// Set it up
				classtype = new BinaryNameSymbol(toString().substring(1,
					length() - 1));
			}
			
			// Primitive
			else
			{
				// Must have length of 1
				if (length() != 1)
					throw new IllegalSymbolException(String.format("DS08 %s",
						this));
				
				// Will never be a class
				classtype = null;
				
				// Get primitive
				primitive = PrimitiveType.byCode(spec);
				if (primitive == null)
					throw new IllegalSymbolException(String.format("DS06 %s",
						this));
			}
		}
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
	 * Returns the binary name of the field.
	 *
	 * @return The binary name of the field or {@code null} if it does not
	 * represent a class type.
	 * @since 2016/03/19
	 */
	public BinaryNameSymbol binaryName()
	{
		return classtype;
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
		return componenttype;
	}
	
	/**
	 * Returns the primitive type of the field.
	 *
	 * @return The primitive type or {@code null} if not one.
	 * @since 2016/03/19
	 */
	public PrimitiveType primitiveType()
	{
		return primitive;
	}
}

