// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.fontcompile.util;

/**
 * Represents the family of a font.
 *
 * @since 2024/06/14
 */
public enum FontFamily
{
	/** Regular font. */
	REGULAR,
	
	/** Monospace. */
	MONOSPACE,
	
	/** Sans Serif. */
	SANS_SERIF,
	
	/** Serif. */
	SERIF,
	
	/* End. */
	;
	
	/**
	 * Returns the family for the given string.
	 *
	 * @param __in The input string.
	 * @return The resultant family.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/06/14
	 */
	public static FontFamily of(String __in)
		throws NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		__in = __in.toLowerCase();
		
		// Monospace?
		if (__in.contains("monospace"))
			return FontFamily.MONOSPACE;
		
		// Sans serif?
		else if (__in.contains("sansserif") ||
			__in.contains("sans serif"))
			return FontFamily.SANS_SERIF;
		
		// Serif?
		else if (__in.contains("serif"))
			return FontFamily.SERIF;
		
		// Otherwise regular
		return FontFamily.REGULAR;
	}
}
