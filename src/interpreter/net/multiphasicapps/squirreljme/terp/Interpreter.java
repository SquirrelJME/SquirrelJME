// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.terp;

import java.util.Map;
import net.multiphasicapps.squirreljme.ci.CIMethod;
import net.multiphasicapps.squirreljme.classpath.ClassPath;
import net.multiphasicapps.squirreljme.classpath.ClassUnit;

/**
 * This is the base class which is used by implementations of the interpreter
 * to handle and managed interpretation.
 *
 * @since 2016/05/27
 */
public abstract class Interpreter
{
	/**
	 * Creates a new process in the interpreter for storing object states
	 * for a group of threads.
	 *
	 * @return The new interpreter process.
	 * @since 2016/06/03
	 */
	public abstract InterpreterProcess createProcess();
	
	/**
	 * Handles the X options which may be passed to the interpreter.
	 *
	 * @param __xo The X options to handle.
	 * @since 2016/05/29
	 */
	public abstract void handleXOptions(Map<String, String> __xo);
	
	/**
	 * This runs a single cycle in the interpreter.
	 *
	 * @since 2016/05/30
	 */
	public abstract void runCycle();
}

