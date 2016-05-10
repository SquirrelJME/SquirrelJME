// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.narf.codeparse;

import net.multiphasicapps.narf.exception.NARFException;

/**
 * This is thrown when the native compiler is not operating correctly or there
 * is a code parse issue.
 *
 * @since 2016/05/08
 */
public class NCPException
	extends NARFException
{
	/**
	 * Initializes the exception with no message or cause.
	 *
	 * @since 2016/05/08
	 */
	public NCPException(Issue __i)
	{
		super(__i);
	}
	
	/**
	 * Initializes the exception with the given message.
	 *
	 * @param __msg The exception message.
	 * @since 2016/05/08
	 */
	public NCPException(Issue __i, String __msg)
	{
		super(__i, __msg);
	}
	
	/**
	 * Initializes the exception with the given message and cause.
	 *
	 * @param __msg The exception message.
	 * @param __c The cause.
	 * @since 2016/05/08
	 */
	public NCPException(Issue __i, String __msg, Throwable __c)
	{
		super(__i, __msg, __c);
	}
	
	/**
	 * Initializes the exception with the given cause and no message.
	 *
	 * @param __c The cause of the exception.
	 * @since 2016/05/08
	 */
	public NCPException(Issue __i, Throwable __c)
	{
		super(__i, __c);
	}
	
	/**
	 * This is the reason for failure.
	 *
	 * @since 2016/05/08
	 */
	public static enum Issue
		implements BaseIssue
	{
		/** The operation is not legal. */
		ILLEGAL_OPCODE,
		
		/** The specified instruction cannot be the start of a basic block. */
		ILLEGAL_BLOCK_START,
		
		/** Exception is out of range. */
		EXCEPTION_RANGE,
		
		/** End. */
		;
	}
}

