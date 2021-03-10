// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * This exception is thrown if there is any issue with the JDWP connection.
 *
 * @since 2021/03/10
 */
public class JDWPException
	extends RuntimeException
{
	/**
	 * Initializes the exception.
	 * 
	 * @param __m The message used.
	 * @since 2021/03/10
	 */
	public JDWPException(String __m)
	{
		super(__m);
	}
	
	/**
	 * Initializes the exception.
	 * 
	 * @param __m The message used.
	 * @param __c The cause of the exception.
	 * @since 2021/03/10
	 */
	public JDWPException(String __m, Throwable __c)
	{
		super(__m, __c);
	}
}
