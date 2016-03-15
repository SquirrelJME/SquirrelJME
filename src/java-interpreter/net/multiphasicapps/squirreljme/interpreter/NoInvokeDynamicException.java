// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.interpreter;

/**
 * This is thrown when it has been detected that a class actually uses the
 * {@code invokedynamic} instruction which is not valid for JavaME.
 *
 * @since 2016/03/15
 */
public class NoInvokeDynamicException
	extends JVMClassFormatError
{
	/**
	 * Initializes the exception with a built-in message.
	 *
	 * @since 2016/03/15
	 */
	public NoInvokeDynamicException()
	{
		super("Invokedynamic (lambdas, MethodHandles, etc.) is " +
			"not supported.");
	}
}

