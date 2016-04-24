// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.narf.exception;

/**
 * This is the base class for all exceptions which are thrown by the native
 * compiler system. The purpose of this is to quickly determine why something
 * has failed and to cache those errors instead of critical compiler
 * failures.
 *
 * @since 2016/04/23
 */
public class NARFException
	extends RuntimeException
{
	/**
	 * Initializes the exception with no message or cause.
	 *
	 * @param __k The issue type.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/23
	 */
	public NCPException(BaseIssue __k)
		throws NullPointerException
	{
		// Check
		if (__k == null)
			throw new NullPointerException("NARG");
		
		// Set
		kind = __k;
	}
	
	/**
	 * Initializes the exception with the given message.
	 *
	 * @param __k The issue type.
	 * @param __msg The exception message.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/23
	 */
	public NCPException(BaseIssue __k, String __msg)
		throws NullPointerException
	{
		super(__msg);
		
		// Check
		if (__k == null)
			throw new NullPointerException("NARG");
		
		// Set
		kind = __k;
	}
	
	/**
	 * Initializes the exception with the given message and cause.
	 *
	 * @param __k The issue type.
	 * @param __msg The exception message.
	 * @param __c The cause.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/23
	 */
	public NCPException(BaseIssue __k, String __msg, Throwable __c)
		throws NullPointerException
	{
		super(__msg, __c);
		
		// Check
		if (__k == null)
			throw new NullPointerException("NARG");
		
		// Set
		kind = __k;
	}
	
	/**
	 * Initializes the exception with the given cause and no message.
	 *
	 * @param __k The issue type.
	 * @param __c The cause of the exception.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/23
	 */
	public NCPException(BaseIssue __k, Throwable __c)
		throws NullPointerException
	{
		super(__c);
		
		// Check
		if (__k == null)
			throw new NullPointerException("NARG");
		
		// Set
		kind = __k;
	}
	
	/**
	 * This is the base class for issues.
	 *
	 * @since 2016/04/23
	 */
	public static interface BaseIssue
	{
		/**
		 * Returns the name of this issue.
		 *
		 * @return The issue name.
		 * @since 2016/04/23
		 */
		public abstract String name();
	}
}

