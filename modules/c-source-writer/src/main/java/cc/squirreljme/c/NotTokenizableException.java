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
 * Exception for when something cannot be tokenized with this.
 *
 * @deprecated Tokens cannot be obtained directly anymore.
 * @since 2023/06/12
 */
@Deprecated
public class NotTokenizableException
	extends IllegalArgumentException
{
	/**
	 * Initializes the exception.
	 * 
	 * @param __set The set this cannot be tokenized with.
	 * @since 2023/06/12
	 */
	public NotTokenizableException(CTokenSet __set)
	{
		// {@squirreljme.error CW0b Cannot be tokenized with the
		// following set. (The set)}
		super("CW0b " + __set);
	}
}
