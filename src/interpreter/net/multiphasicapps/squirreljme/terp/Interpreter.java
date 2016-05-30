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
	/** The interpreter lock, if required. */
	protected final Object lock =
		new Object();
	
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
	
	/**
	 * This adjusts the program to start on initial launch.
	 *
	 * @param __cp The {@link ClassPath} to adjust.
	 * @param __mm The The main method to adjust.
	 * @param __args The program arguments to adjust.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/30
	 */
	public void adjustProgramStart(ClassPath[] __cp, CIMethod[] __mm,
		String[][] __args)
		throws NullPointerException
	{
		// Check
		if (__cp == null || __mm == null || __args == null)
			throw new NullPointerException("NARG");
		
		// The behavior is modified by the sub-class.
	}
}

