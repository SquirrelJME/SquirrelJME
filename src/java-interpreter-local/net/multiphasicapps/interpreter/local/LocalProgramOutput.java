// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.interpreter.local;

import net.multiphasicapps.interpreter.JVMProgramOutput;

/**
 * This is used to generate programs which are used by the interpreter for
 * the execution of translated Java byte code.
 *
 * @since 2016/03/23
 */
public class LocalProgramOutput
	extends JVMProgramOutput
{
	/**
	 * This is the factory which creates local program outputs.
	 *
	 * @since 2016/03/23
	 */
	public static class LocalFactory
		extends JVMProgramOutput.Factory
	{
	}
}

