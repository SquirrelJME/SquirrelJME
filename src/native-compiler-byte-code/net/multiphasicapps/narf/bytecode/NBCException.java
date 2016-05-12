// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.narf.bytecode;

import net.multiphasicapps.narf.exception.NARFException;

/**
 * This represents an exception which may be thrown if the Java byte code is
 * not valid or does not verify correctly.
 *
 * @since 2016/05/11
 */
public class NBCException
	extends NARFException
{
	/**
	 * Initializes the exception with no message or cause.
	 *
	 * @since 2016/05/11
	 */
	public NBCException(Issue __i)
	{
		super(__i);
	}
	
	/**
	 * Initializes the exception with the given message.
	 *
	 * @param __msg The exception message.
	 * @since 2016/05/11
	 */
	public NBCException(Issue __i, String __msg)
	{
		super(__i, __msg);
	}
	
	/**
	 * Initializes the exception with the given message and cause.
	 *
	 * @param __msg The exception message.
	 * @param __c The cause.
	 * @since 2016/05/11
	 */
	public NBCException(Issue __i, String __msg, Throwable __c)
	{
		super(__i, __msg, __c);
	}
	
	/**
	 * Initializes the exception with the given cause and no message.
	 *
	 * @param __c The cause of the exception.
	 * @since 2016/05/11
	 */
	public NBCException(Issue __i, Throwable __c)
	{
		super(__i, __c);
	}
	
	/**
	 * This is the reason for failure.
	 *
	 * @since 2016/05/11
	 */
	public static enum Issue
		implements BaseIssue
	{
		/** An operation is not valid. */
		ILLEGAL_OPCODE,
		
		/** The stack overflows or underflows. */
		STACK_OVERFLOW,
		
		/** Not enough local variables for method call. */
		NOT_ENOUGH_LOCALS,
		
		/** The field type is not known. */
		UNKNOWN_FIELD_TYPE,
		
		/** A reference was made to a class which does not exit. */
		MISSING_CLASS,
		
		/** An attempt would be made to initialize an abstract class. */
		INIT_ABSTRACT_CLASS,
		
		/** End. */
		;
	}
}

