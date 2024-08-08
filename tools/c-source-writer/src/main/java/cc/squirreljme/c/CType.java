// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c;

import java.util.List;

/**
 * Represents a C type.
 *
 * @since 2023/05/29
 */
public interface CType
{
	/**
	 * Returns an array type.
	 * 
	 * @param __size The size of the array.
	 * @return The array type.
	 * @throws IllegalArgumentException If the array is negative.
	 * @since 2023/06/24
	 */
	CType arrayType(int __size)
		throws IllegalArgumentException;
	
	/**
	 * Cast the given type.
	 * 
	 * @param <T> The type to cast as.
	 * @param __as The type to cast as.
	 * @return The cast type.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/25
	 */
	<T extends CType> T cast(Class<T> __as)
		throws NullPointerException;
	
	/**
	 * Gets the type as a constant.
	 * 
	 * @return The constant version of this type.
	 * @throws IllegalArgumentException If this would not make a valid const
	 * type.
	 * @since 2023/06/06
	 */
	CType constType()
		throws IllegalArgumentException;
	
	/**
	 * Returns the declaring tokens for the type with the given identifying
	 * name.
	 * 
	 * @param __name The name to use.
	 * @return The tokens for the declared type.
	 * @since 2023/06/24
	 */
	List<String> declareTokens(CIdentifier __name);
	
	/**
	 * Returns the type that would be used if this were de-referenced.
	 * 
	 * @return The de-referenced type.
	 * @throws IllegalArgumentException If this is not a pointer.
	 * @since 2023/05/29
	 */
	CType dereferenceType()
		throws IllegalArgumentException;
	
	/**
	 * Is this a pointer type?
	 * 
	 * @return If this is a pointer type.
	 * @since 2023/05/05
	 */
	boolean isPointer();
	
	/**
	 * Gets the pointer of this type.
	 * 
	 * @return The pointer type.
	 * @since 2023/05/29
	 */
	CType pointerType()
		throws IllegalArgumentException;
	
	/**
	 * Gets the pointer of this type.
	 * 
	 * @param __closeness The closeness of the pointer.
	 * @return The pointer type.
	 * @since 2023/06/24
	 */
	CType pointerType(CPointerCloseness __closeness)
		throws IllegalArgumentException;
}
