// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel.impl.jvm;

import java.util.LinkedHashMap;
import java.util.Map;
import net.multiphasicapps.squirreljme.terp.Interpreter;

/**
 * This provides the basic initialize of a JVM based kernel.
 *
 * @since 2016/05/30
 */
public class BasicMain
	implements Runnable
{
	/** The default interpreter core. */
	public static final String DEFAULT_INTERPRETER =
		"net.multiphasicapps.squirreljme.terp.std.StandardInterpreter";
	
	/** The deterministic interpreter. */
	public static final String RR_INTERPRETER =
		"net.multiphasicapps.squirreljme.terp.rr.RRInterpreter";
	
	/** The kernel being used. */
	protected final JVMKernel kernel;
	
	/**
	 * This initializes the kernel to use for execution along with the
	 * interpreter.
	 *
	 * @param __args The arguments to the kernel.
	 * @since 2016/05/30
	 */
	public BasicMain(String... __args)
	{
		// {@squirreljme.property net.multiphasicapps.squirreljme.interpreter
		// This is the class which should be used as the interpreter for the
		// code which runs in the JVM based kernel.}
		String useterp = System.getProperty(
			"net.multiphasicapps.squirreljme.interpreter");
		if (useterp == null)
			useterp = DEFAULT_INTERPRETER;
		else if (useterp.equals("rerecord"))
			useterp = RR_INTERPRETER;
		
		// {@squirreljme.cmdline -Xsquirreljme-interpreter=(class) This
		// specifies the name of the class which should be used as the
		// interpreter instead of the default. If "rerecord" is specified then
		// the class name of the rerecording interpreter is used instead.}
		String altterp = null;
		Map<String, String> xops = new LinkedHashMap<>();
		for (String a : __args)
		{
			// If it does not start with a dash, it is the main class
			// Also stop on -jar because everything after is handled specially
			if (!a.startsWith("-") || a.equals("-jar"))
				break;
			
			// Is this an X option?
			if (a.startsWith("-X"))
			{
				// Has an equal sign?
				int eq = a.indexOf('=');
				if (eq >= 0)
					xops.put(a.substring(2, eq), a.substring(eq + 1));
				
				// Does not, use a blank string
				else
					xops.put(a.substring(2), "");
			}
		}
		
		// Change the interpreter?
		altterp = xops.get("squirreljme-interpreter");
		
		// Use alternative based on the command line?
		if (altterp != null)
			if (altterp.equals("rerecord"))
				useterp = RR_INTERPRETER;
			else
				useterp = altterp;
		
		// Create an instance of the interpreter
		Interpreter terp;
		try
		{
			// Find it
			Class<?> terpcl = Class.forName(useterp);
			
			// Initialize it
			terp = (Interpreter)terpcl.newInstance();
		}
		
		// {@squirreljme.error BC0a Could not initialize the interpreter.
		catch (ClassCastException|ClassNotFoundException|
			IllegalAccessException|InstantiationException e)
		{
			throw new RuntimeException("BC0a", e);
		}
		
		// Handle X options which sets up interpreter details
		terp.handleXOptions(xops);
		
		// Create the kernel to use
		this.kernel = createKernel(terp, __args);
		
		// {@squirreljme.error BC01 The kernel was never created.}
		if (this.kernel == null)
			throw new NullPointerException("BC01");
	}
	
	/**
	 * This creates an instance of the kernel to run on the JVM using the
	 * specified interpreter for execution.
	 *
	 * @param __terp The interpreter to use for execution.
	 * @param __args The arguments to the kernel.
	 * @throws NullPointerException If no interpreter was specified.
	 * @since 2016/05/30
	 */
	protected JVMKernel createKernel(Interpreter __terp, String... __args)
		throws NullPointerException
	{
		// Check
		if (__terp == null)
			throw new NullPointerException("NARG");
		
		// Create it
		return new JVMKernel(__terp, __args);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/30
	 */
	@Override
	public void run()
	{
		// Run kernel cycles
		JVMKernel kernel = this.kernel;
		for (;; Thread.yield())
			kernel.runCycle();
	}
	
	/**
	 * Main entry point.
	 *
	 * @param __args Kernel arguments.
	 * @since 2016/05/30
	 */
	public static void main(String... __args)
	{
		// Setup basic main and run it
		new BasicMain(__args).run();
	}
}

