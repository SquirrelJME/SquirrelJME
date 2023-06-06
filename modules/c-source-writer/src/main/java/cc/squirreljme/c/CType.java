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
	 * Returns the type that would be used if this were de-referenced.
	 * 
	 * @return The de-referenced type.
	 * @throws IllegalArgumentException If this is not a pointer.
	 * @since 2023/05/29
	 */
	CType dereferenceType()
		throws IllegalArgumentException;
	
	/**
	 * Returns the number of pointers this type has.
	 * 
	 * @return The number of pointers this type has.
	 * @since 2023/05/29
	 */
	int pointerLevel();
	
	/**
	 * Gets the pointer of this type.
	 * 
	 * @return The pointer type.
	 * @since 2023/05/29
	 */
	CType pointerType()
		throws IllegalArgumentException;
	
	/**
	 * Returns the root type of this type, if this is a pointer then this
	 * will be the non-pointer type or otherwise just the root type.
	 * 
	 * @return The root of this type without any pointers.
	 * @since 2023/05/29
	 */
	CType rootType();
	
	/**
	 * Returns the token representation of the type.
	 * 
	 * @return The token representation of the type.
	 * @since 2023/05/29
	 */
	List<String> tokens();
}
