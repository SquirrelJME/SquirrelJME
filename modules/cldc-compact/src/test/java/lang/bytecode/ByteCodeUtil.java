// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package lang.bytecode;

/**
 * Utilities for byte code tests.
 *
 * @since 2023/07/09
 */
@SuppressWarnings("unused")
public final class ByteCodeUtil
{
	/**
	 * Not used.
	 * 
	 * @since 2023/07/09
	 */
	private ByteCodeUtil()
	{
	}
	
	/**
	 * Makes a reference array.
	 * 
	 * @return A reference array.
	 * @since 2023/07/09
	 */
	public static Object[] makeReferenceArray()
	{
		return new Object[]{
			"squirrels", "are", "very", "cute"
		};
	}
	
	/**
	 * Null reference to reference array.
	 * 
	 * @return A reference array.
	 * @since 2023/07/09
	 */
	public static Object[] nullReferenceArray()
	{
		return null;
	}
}
