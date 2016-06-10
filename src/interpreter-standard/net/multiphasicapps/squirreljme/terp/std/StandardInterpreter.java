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

import java.util.Map;
import net.multiphasicapps.descriptors.ClassNameSymbol;
import net.multiphasicapps.squirreljme.ci.CIMethod;
import net.multiphasicapps.squirreljme.ci.CIMethodID;
import net.multiphasicapps.squirreljme.classpath.ClassPath;
import net.multiphasicapps.squirreljme.sm.StructureManager;
import net.multiphasicapps.squirreljme.terp.Interpreter;
import net.multiphasicapps.squirreljme.terp.InterpreterException;
import net.multiphasicapps.squirreljme.terp.InterpreterProcess;
import net.multiphasicapps.squirreljme.terp.InterpreterThread;

/**
 * This is the standard interpreter which uses normal threads for each
 * interpreter thread.
 *
 * @since 2016/05/12
 */
public class StandardInterpreter
	extends Interpreter
{
	/**
	 * Initializes the interpreter which uses the direct byte code.
	 *
	 * @param __sm Pre-existing optional structure manager.
	 * @param __args Interpreter initialization arguments.
	 * @since 2016/05/12
	 */
	public StandardInterpreter(StructureManager __sm, String... __args)
	{
		super(__sm, __args);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/03
	 */
	@Override
	public InterpreterProcess createProcess(ClassPath __cp)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/03
	 */
	@Override
	public InterpreterThread createThread(InterpreterProcess __ip,
		ClassNameSymbol __mc, CIMethodID __mm, Object... __args)
		throws InterpreterException, NullPointerException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/29
	 */
	@Override
	protected void handleXOptions(Map<String, String> __xo)
		throws NullPointerException
	{
		// Super call
		super.handleXOptions(__xo);
		
		// Check
		if (__xo == null)
			throw new NullPointerException("NARG");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/30
	 */
	@Override
	public void runCycle()
	{
		throw new Error("TODO");
	}
}

