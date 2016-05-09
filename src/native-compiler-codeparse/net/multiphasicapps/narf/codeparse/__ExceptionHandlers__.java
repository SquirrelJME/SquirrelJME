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

import java.util.List;
import net.multiphasicapps.narf.classinterface.NCICodeException;
import net.multiphasicapps.narf.classinterface.NCICodeExceptions;
import net.multiphasicapps.util.unmodifiable.UnmodifiableList;

/**
 * This handles the exceptions which are handled in the code block of a method.
 *
 * Essentially when it comes to future generation of code, the exception
 * handler is placed at the start of the generated code. It uses a variable to
 * store the table of exception handlers to use. That table points to other
 * tables which are then checked for a matching exception. Due to the potential
 * for there to be overla
 *
 * @since 2016/05/08
 */
final class __ExceptionHandlers__
{
	/**
	 * Initializes the set of exception handlers.
	 *
	 * @param __cp Code parser.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/08
	 */
	__ExceptionHandlers__(NCPCodeParser __cp, NCICodeExceptions __exs)
		throws NullPointerException
	{
		// Check
		if (__cp == null || __exs == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
}

