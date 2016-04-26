// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.narf.interpreter;

import net.multiphasicapps.narf.exception.NARFException;

/**
 * This is the base for exceptions which may be thrown by the interpreter.
 *
 * @since 2016/04/22
 */
public class NIException
	extends NARFException
{
	/**
	 * Initializes the exception with no message.
	 *
	 * @param __ic The interpretation core.
	 * @param __t The exception type.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/22
	 */
	public NIException(NICore __ic, Issue __t)
		throws NullPointerException
	{
		super(__t);
		
		// Check
		if (__ic == null)
			throw new NullPointerException("NARG");
	}
	
	/**
	 * Initializes exception with the given message.
	 *
	 * @param __ic The interpretation core.
	 * @param __t The exception type.
	 * @param __msg The exception message.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/22
	 */
	public NIException(NICore __ic, Issue __t, String __msg)
		throws NullPointerException
	{
		super(__t, __msg);
		
		// Check
		if (__ic == null)
			throw new NullPointerException("NARG");
	}
	
	/**
	 * Initializes exception with the given message and cause.
	 *
	 * @param __ic The interpretation core.
	 * @param __t The exception type.
	 * @param __msg The exception message.
	 * @param __c The cause.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/22
	 */
	public NIException(NICore __ic, Issue __t, String __msg,
		Throwable __c)
		throws NullPointerException
	{
		super(__t, __msg, __c);
		
		// Check
		if (__ic == null)
			throw new NullPointerException("NARG");
	}
	
	/**
	 * Initializes the exception with the given cause and no message.
	 *
	 * @param __ic The interpretation core.
	 * @param __t The exception type.
	 * @param __c The cause of the exception.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/22
	 */
	public NIException(NICore __ic, Issue __t, Throwable __c)
		throws NullPointerException
	{
		super(__t, __c);
		
		// Check
		if (__ic == null)
			throw new NullPointerException("NARG");
	}
	
	/**
	 * The exception type.
	 *
	 * @since 2016/04/22
	 */
	public static enum Issue
		implements BaseIssue
	{
		/** The name of a class mismatches the one which was read. */
		CLASS_NAME_MISMATCH,
		
		/** The class extends or implements itself eventually. */
		CLASS_CIRCULARITY,
		
		/** A class does not implement an abstract method. */
		ABSTRACT_NOT_IMPLEMENTED,
		
		/** End. */
		;
	}
}

