// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.exe.interpreter;

import net.multiphasicapps.squirreljme.exe.ExecutableOutput;

/**
 * This is able to link the blobs built for the interpreter as a single binary
 * which can then be run on the interpreter.
 *
 * @since 2016/07/24
 */
public class InterpreterExecutableOutput
	implements ExecutableOutput
{
	/**
	 * {@inheritDoc}
	 * @since 2016/07/24
	 */
	@Override
	public void addSystemProperty(String __k, String __v)
		throws NullPointerException
	{
		// Check
		if (__k == null || __v == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
}

