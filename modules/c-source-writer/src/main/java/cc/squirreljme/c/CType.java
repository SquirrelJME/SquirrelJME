// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
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
	 * Returns the token representation of the type.
	 * 
	 * @return The token representation of the type.
	 * @since 2023/05/29
	 */
	List<String> tokens();
}
