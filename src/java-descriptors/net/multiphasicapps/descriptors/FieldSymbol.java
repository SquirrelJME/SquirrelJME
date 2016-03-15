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
	/** Component type of the array if it is one. */
	protected final FieldSymbol componenttype;
	
	/** Array dimensions. */
	protected final int dimensions;
	
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
		if (charAt(0) == '[')
			throw new Error("TODO");
		
		// Not an array
		else
		{
			// Not used
			dimensions = 0;
			componenttype = null;
			
			throw new Error("TODO");
		}
	}
	
	/**
	 * Returns the number of dimensions in the array.
	 *
	 * @return The dimensions in the array.
	 * @since 2016/03/14
	 */
	public int arrayDimensions()
	{
		return dimensions;
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
}

