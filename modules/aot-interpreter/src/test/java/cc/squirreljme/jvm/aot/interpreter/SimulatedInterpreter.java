// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.interpreter;

import cc.squirreljme.jvm.mle.ReflectionShelf;
import cc.squirreljme.jvm.mle.TypeShelf;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import net.multiphasicapps.tac.UntestableException;

/**
 * Interpreter that is used solely for testing purposes.
 *
 * @since 2022/09/08
 */
public class SimulatedInterpreter
	extends AotInterpreter
{
	/**
	 * Alternative main entry point to set up the interpreter.
	 * 
	 * @param __args The arguments to the main entry point.
	 * @throws Throwable On anything that was thrown.
	 * @since 2022/09/11
	 */
	public static void main(String... __args)
		throws Throwable
	{
		// Setup and install our own interpreter
		try
		{
			new SimulatedInterpreter().install();
		}
		catch (MLECallError e)
		{
			throw new UntestableException(e);
		}
		
		// Copy over
		String mainClass = __args[0];
		String[] mainArgs = new String[__args.length - 1];
		System.arraycopy(__args, 1,
			mainArgs, 0, __args.length - 1);
		
		// Forward to actual main
		ReflectionShelf.invokeMain(
			TypeShelf.findType(mainClass.replace('.', '/')),
			mainArgs);
	}
}
