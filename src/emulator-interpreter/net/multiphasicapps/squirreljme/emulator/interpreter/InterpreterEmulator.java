// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.emulator.interpreter;

import net.multiphasicapps.squirreljme.emulator.DisplayOutput;
import net.multiphasicapps.squirreljme.emulator.Emulator;

/**
 * This is the emulator which is able to parse and decode the binaries used
 * by the interpreter test.
 *
 * @since 2016/07/30
 */
public class InterpreterEmulator
	extends Emulator
{
	/**
	 * Initializes the interpreter emulator.
	 *
	 * @param __do The display output to use.
	 * @since 2016/07/30
	 */
	public InterpreterEmulator(DisplayOutput __do)
	{
		super(__do);
	}
}

