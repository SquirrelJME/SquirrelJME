// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.util;

/**
 * Specifies that the Fossil executable is not valid or is too old.
 *
 * @since 2020/06/24
 */
public class InvalidFossilExeException
	extends IllegalArgumentException
{
	/**
	 * Initializes the exception.
	 * 
	 * @param __message The message to use.
	 * @since 2020/06/24
	 */
	public InvalidFossilExeException(String __message)
	{
		super(__message);
	}
	
	/**
	 * Initializes the exception.
	 * 
	 * @param __message The message to use.
	 * @param __cause The cause.
	 * @since 2020/06/25
	 */
	public InvalidFossilExeException(String __message, Throwable __cause)
	{
		super(__message, __cause);
	}
}
