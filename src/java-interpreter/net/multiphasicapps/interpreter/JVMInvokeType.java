// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.interpreter;

/**
 * This represents the type of method to invoke.
 *
 * @since 2016/03/29
 */
public enum JVMInvokeType
{
	/** Special invocation (call constructor or super-class). */
	SPECIAL(false),
	
	/** Standard invocation. */
	VIRTUAL(false),
	
	/** Invoke of an interface. */
	INTERFACE(false),
	
	/** Call static method. */
	STATIC(true),
	
	/** End. */
	;
	
	/** Is this a static invocation? */
	protected final boolean isstatic;
	
	/**
	 * Initializes the invocation type.
	 *
	 * @param __iss Is this a static invocation?
	 * @since 2016/03/29
	 */
	private JVMInvokeType(boolean __iss)
	{
		isstatic = __iss;
	}
	
	/**
	 * Is this an invocation on an instance of an object?
	 *
	 * @return {@code true} if this is an instance invocation.
	 * @since 2016/03/29
	 */
	public boolean isInstance()
	{
		return !isstatic;
	}
	
	/**
	 * Is this an invocation which is static (does not use an instance)?
	 *
	 * @return {@code true} if this is a static invocation.
	 * @since 2016/03/29
	 */
	public boolean isStatic()
	{
		return isstatic;
	}
}

