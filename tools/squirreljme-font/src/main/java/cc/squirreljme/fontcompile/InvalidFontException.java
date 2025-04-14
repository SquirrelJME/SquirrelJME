// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.fontcompile;

import java.io.IOException;

/**
 * Thrown when the font is not valid.
 *
 * @since 2024/05/24
 */
public class InvalidFontException
	extends IOException
{
	/**
	 * Initializes the exception.
	 *
	 * @param __m The message for the exception.
	 * @since 2024/05/24
	 */
	public InvalidFontException(String __m)
	{
		super(__m);
	}
}
