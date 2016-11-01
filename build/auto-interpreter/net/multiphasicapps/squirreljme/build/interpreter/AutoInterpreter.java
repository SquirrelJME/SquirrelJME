// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.interpreter;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;
import net.multiphasicapps.squirreljme.build.projects.ProjectManager;
import net.multiphasicapps.squirreljme.kernel.Kernel;

/**
 * This class implements the auto interpreter which uses the internal JIT to
 * provide an interpreted environment for execution.
 *
 * @since 2016/10/29
 */
public class AutoInterpreter
	implements Runnable
{
	/** The project manager which is used to get APIs, midlets, and liblets. */
	protected final ProjectManager projects;
	
	/**
	 * Initializes the auto interpreter.
	 *
	 * @param __pm The manager for projects.
	 * @param __args Interpreter arguments.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/29
	 */
	public AutoInterpreter(ProjectManager __pm, String... __args)
		throws NullPointerException
	{
		// Check
		if (__pm == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.projects = __pm;
		
		// Queue arguments
		Deque<String> aq = new ArrayDeque<>();
		for (String s : __args)
			aq.offerLast(Objects.toString(s, ""));
		
		// Parse them
		while (!aq.isEmpty())
		{
			String arg = aq.peekFirst();
			
			// End of commands?
			if (arg.equals("--"))
			{
				aq.removeFirst();
				break;
			}
			
			throw new Error("TODO");
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/29
	 */
	@Override
	public void run()
	{
		// Create JVM
		InterpreterInterface ii = new InterpreterInterface(this);
		Kernel k = new Kernel(ii);
		
		// Run all cycles in the JVM until it terminates
		for (;;)
		{
			throw new Error("TODO");
		}
	}
}

