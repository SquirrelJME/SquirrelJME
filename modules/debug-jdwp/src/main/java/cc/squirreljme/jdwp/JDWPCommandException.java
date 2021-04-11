// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

/**
 * This is thrown when there is an error parsing and executing a packet.
 *
 * @since 2021/04/11
 */
public class JDWPCommandException
	extends JDWPException
{
	/** The type of error this is. */
	public final ErrorType type;
	
	/**
	 * Initializes the exception.
	 * 
	 * @param __errorType The error type.
	 * @param __m The message for the error.
	 * @since 2021/04/11
	 */
	public JDWPCommandException(ErrorType __errorType, String __m)
	{
		super(__m);
		
		this.type = __errorType;
	}
}
