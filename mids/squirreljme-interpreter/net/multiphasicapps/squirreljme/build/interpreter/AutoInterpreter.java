// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.interpreter;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;
import java.util.Set;
import net.multiphasicapps.squirreljme.build.projects.ProjectManager;
import net.multiphasicapps.squirreljme.jit.mips.MIPSConfig;
import net.multiphasicapps.squirreljme.jit.mips.MIPSEngineProvider;
import net.multiphasicapps.squirreljme.jit.TranslationEngine;
import net.multiphasicapps.squirreljme.jit.TranslationEngineProvider;
import net.multiphasicapps.squirreljme.kernel.Kernel;
import net.multiphasicapps.squirreljme.kernel.KernelBuilder;
import net.multiphasicapps.squirreljme.kernel.KernelLaunchParameters;
import net.multiphasicapps.squirreljme.kernel.KernelLaunchParametersBuilder;
import net.multiphasicapps.util.sorted.SortedTreeSet;

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
	
	/** The translation engine used for the JIT. */
	protected final TranslationEngineProvider engineprovider;
	
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
		if (__args == null)
			__args = new String[0];
		
		// Set
		this.projects = __pm;
		
		// Queue arguments
		Deque<String> aq = new ArrayDeque<>();
		for (String s : __args)
			aq.offerLast(Objects.toString(s, ""));
		
		// Launch parameters builder which is used to initialize the kernel
		// with a pre-determined set of parameters
		KernelLaunchParametersBuilder klpb =
			new KernelLaunchParametersBuilder();
		
		// Add initial arguments
		klpb.parseCommandLine(__args);
		
		// Parse them
		Path detrecord = null, detreplay = null;
		Set<String> selects = new SortedTreeSet<>();
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
				// (-Xrecord=path: Use the determinisitic interpreter and
				// record run-time actions to the given path.;
				// -Xreplay=path: Use the determinisitic interpreter and replay
				// a pre-existing recording.;
				// -Xselect=items(,items...): Selects the API, MIDlet, and
				// LIBlet categories to use during the run-time process.
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
						
						// Select APIs, MIDlets, and LIBlets to use
					case "select":
						{
							// Go through fields
							int n = val.length();
							for (int i = 0; i < n;)
							{
								// Find next comma, if not found then use the
								// length to use all of it
								int nc = val.indexOf(',', i);
								if (nc < 0)
									nc = n;
								
								// Use it
								selects.add(val.substring(i, nc));
								
								// Go to index following the comma
								i = nc + 1;
							}
						}
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
		
		// Store selected software for usage
		{
			StringBuilder sb = new StringBuilder();
			for (String s : selects)
			{
				if (sb.length() > 0)
					sb.append(',');
				sb.append(s);
			}
			klpb.addSystemProperty(InterpreterSystemSuites.SELECT_PROPERTY,
				sb.toString());
		}
		
		// Finish building the parameters
		KernelLaunchParameters klp = klpb.build();
		
		// Determinisitic kernel manager
		AbstractKernelManager akm;
		if (detrecord != null || detreplay != null)
			akm = new DeterministicKernelManager(this, klp, detreplay,
				detrecord);
		
		// Normal kernel manager
		else
			akm = new NormalKernelManager(this, klp);
		
		// Set manager
		this._akm = akm;
		
		// Setup translation engine the JIT uses
		this.engineprovider = new MIPSEngineProvider(new MIPSConfig(
			"mips.cpu", "mips1",
			"generic.endian", "big",
			"generic.bits", "32"));
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
	 * Returns the project manager which is used to access suites and such.
	 *
	 * @return The used project manager.
	 * @since 2016/11/20
	 */
	public final ProjectManager projectManager()
	{
		return this.projects;
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
		kb.launchParameters(akm);
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
	
	/**
	 * Returns the translation engine provider which the interpreter
	 * understands when executing target code.
	 *
	 * @return The translation engine provider.
	 * @since 2017/01/30
	 */
	public TranslationEngineProvider translationEngineProvider()
	{
		return this.engineprovider;
	}
}

