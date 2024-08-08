// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c;

/**
 * The type of number something is.
 *
 * @since 2023/05/29
 */
public interface CNumberType
{
	/**
	 * Returns the number prefix.
	 * 
	 * @return The number prefix.
	 * @since 2023/06/24
	 */
	String prefix();
	
	/**
	 * Returns the number suffix.
	 * 
	 * @return The number suffix.
	 * @since 2023/06/24
	 */
	String suffix();
	
	/**
	 * Returns the number surround.
	 * 
	 * @return The number surround.
	 * @since 2023/06/24
	 */
	String surround();
}
