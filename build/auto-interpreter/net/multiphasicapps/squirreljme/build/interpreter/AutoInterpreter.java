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

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;
import net.multiphasicapps.squirreljme.build.projects.ProjectManager;
import net.multiphasicapps.squirreljme.kernel.Kernel;
import net.multiphasicapps.squirreljme.kernel.KernelBuilder;

/**
 * This class implements the auto interpreter which uses the internal JIT to
 * provide an interpreted environment for execution.
 *
 * @since 2016/10/29
 */
public class AutoInterpreter
	implements Closeable, Runnable
{
	/** The project manager which is used to get APIs, midlets, and liblets. */
	protected final ProjectManager projects;
	
	/** The kernel manager. */
	private final AbstractKernelManager _akm;
	
	/**
	 * Initializes the auto interpreter.
	 *
	 * @param __pm The manager for projects.
	 * @param __args Interpreter arguments.
	 * @throws IllegalArgumentException If the interpreter arguments are not
	 * correct.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/29
	 */
	public AutoInterpreter(ProjectManager __pm, String... __args)
		throws IllegalArgumentException, IOException, NullPointerException
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
		Path detrecord = null, detreplay = null;
		while (!aq.isEmpty())
		{
			String arg = aq.peekFirst();
			
			// No more commands?
			if (!arg.startsWith("-"))
				break;
			
			// Remove it
			aq.removeFirst();
			
			// End of commands?
			if (arg.equals("--"))
				break;
			
			// X command which modifies the interpreter potentially
			if (arg.startsWith("-X"))
			{
				// {@squirreljme.error AV02 Empty X option specified.
				// (
				// -Xrecord=path: Use the determinisitic interpreter and record
				// run-time actions to the given path.;
				// -Xreplay=path: Use the determinisitic interpreter and replay
				// a pre-existing recording.
				// )}
				if (arg.equals("-X"))
					throw new IllegalArgumentException("AV02");
				
				// Split off key and value
				int eqs = arg.indexOf('=');
				String key = (eqs >= 0 ? arg.substring(2, eqs) :
					arg.substring(2));
				String val = (eqs >= 0 ? arg.substring(eqs + 1) : "");
				
				// Depends on the key
				switch (key)
				{
						// Deterministic record
					case "record":
						detrecord = Paths.get(val);
						break;
						
						// Determinstic playback
					case "replay":
						detreplay = Paths.get(val);
						break;
					
						// {@squirreljme.error AV03 Unknown -X option
						// specified. (The -X option)}
					default:
						throw new IllegalArgumentException(String.format(
							"AV03 %s", key));
				}
			}
			
			// {@squirreljme.error AV01 Unknown command line argument passed.
			// (The argument)}
			else
				throw new IllegalArgumentException(String.format("AV01 %s",
					arg));
		}
		
		// Determinisitic kernel manager
		AbstractKernelManager akm;
		if (detrecord != null || detreplay != null)
			akm = new DeterministicKernelManager(this, detreplay, detrecord);
		
		// Normal kernel manager
		else
			akm = new NormalKernelManager(this);
		
		// Set manager
		this._akm = akm;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/29
	 */
	@Override
	public void close()
		throws IOException
	{
		// Close the kernel manager? May be used by the deterministic code.
		AbstractKernelManager akm = this._akm;
		if (akm instanceof Closeable)
			((Closeable)akm).close();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/29
	 */
	@Override
	public void run()
	{
		// Setup abstract kernel
		AbstractKernelManager akm = this._akm;
		
		// Setup kernel parameters
		KernelBuilder kb = new KernelBuilder();
		kb.threadManager(akm);
		kb.suiteManager(akm);
		
		// Build kernel
		Kernel k = kb.build();
		
		// Run all cycles in the JVM until it terminates
		for (;; Thread.yield())
			try
			{
				// Run kernel loop
				if (!k.run())
					break;
			}
			
			// Ignore
			catch (InterruptedException e)
			{
			}
	}
}

