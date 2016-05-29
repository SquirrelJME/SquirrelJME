// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.terp.std;

import net.multiphasicapps.squirreljme.terp.Interpreter;

/**
 * This is the standard interpreter which uses normal threads for each
 * interpreter thread.
 *
 * @since 2016/05/12
 */
public class StandardInterpreter
	implements Interpreter
{
	/**
	 * Initializes the interpreter which uses the direct byte code.
	 *
	 * @since 2016/05/12
	 */
	public StandardInterpreter()
	{
	}
}

