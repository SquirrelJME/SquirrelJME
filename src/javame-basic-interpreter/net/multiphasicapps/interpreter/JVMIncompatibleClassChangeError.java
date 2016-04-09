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
 * This is thrown when a class which was previously compiled along with other
 * classes which were recompiled have been broken interface wise.
 *
 * @since 2016/04/09
 */
public class JVMIncompatibleClassChangeError
	extends JVMEngineException
{
	/**
	 * Initializes the exception with no message.
	 *
	 * @since 2016/04/09
	 */
	public JVMIncompatibleClassChangeError()
	{
		super();
	}
	
	/**
	 * Initializes exception with the given message.
	 *
	 * @param __msg The exception message.
	 * @since 2016/04/09
	 */
	public JVMIncompatibleClassChangeError(String __msg)
	{
		super(__msg);
	}
	
	/**
	 * Initializes exception with the given message and cause.
	 *
	 * @param __msg The exception message.
	 * @param __c The cause.
	 * @since 2016/04/09
	 */
	public JVMIncompatibleClassChangeError(String __msg, Throwable __c)
	{
		super(__msg, __c);
	}
	
	/**
	 * Initializes the exception with the given cause and no message.
	 *
	 * @param __c The cause of the exception.
	 * @since 2016/04/09
	 */
	public JVMIncompatibleClassChangeError(Throwable __c)
	{
		super(__c);
	}
}

