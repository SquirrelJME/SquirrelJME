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
import net.multiphasicapps.descriptors.ClassNameSymbol;
import net.multiphasicapps.squirreljme.ci.CIMethod;
import net.multiphasicapps.squirreljme.ci.CIMethodID;
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
	 * @param __cp The classpath that the process uses.
	 * @return The new interpreter process.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/03
	 */
	public abstract InterpreterProcess createProcess(ClassPath __cp)
		throws NullPointerException;
	
	/**
	 * Creates a new thread within the interpreter which starts execution at
	 * the specified method and uses the given arguments.
	 *
	 * @param __ip The process which owns the thread.
	 * @param __mc The main class of the thread.
	 * @param __mm The main method of the thread.
	 * @param __args The initial arguments which are passed to the starting
	 * method.
	 * @throws InterpreterException If the method is not static or an input
	 * argument is not of the expected type.
	 * @throws NullPointerException If the process or method were not
	 * specified.
	 * @since 2016/06/03
	 */
	public abstract InterpreterThread createThread(InterpreterProcess __ip,
		ClassNameSymbol __mc, CIMethodID __mm, Object... __args)
		throws InterpreterException, NullPointerException;
	
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

