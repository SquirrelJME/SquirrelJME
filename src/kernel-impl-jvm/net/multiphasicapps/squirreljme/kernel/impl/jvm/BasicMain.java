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
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import net.multiphasicapps.squirreljme.terp.Interpreter;
import net.multiphasicapps.squirreljme.terp.InterpreterFactory;

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
		"standard";
	
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
		// This is the interpreter which should be used by the the JVM based
		// interpretive virtual machine.}
		String useterp = System.getProperty(
			"net.multiphasicapps.squirreljme.interpreter");
		if (useterp == null)
			useterp = DEFAULT_INTERPRETER;
		
		// {@squirreljme.cmdline -Xsquirreljme-interpreter=(name) This
		// specifies the name of the interpreter which should be used as the
		// interpreter instead of the default. If "rerecording" is specified
		// then the rerecording interpreter is used instead.}
		for (String a : __args)
		{
			// If it does not start with a dash, it is the main class
			// Also stop on -jar because everything after is handled specially
			if (!a.startsWith("-") || a.equals("-jar"))
				break;
			
			// Interpreter name?
			if (a.startsWith("-Xsquirreljme-interpreter="))
				useterp = a.substring("-Xsquirreljme-interpreter=".length());
		}
		
		// Find the interpreter service
		ServiceLoader<InterpreterFactory> ld =
			ServiceLoader.<InterpreterFactory>load(InterpreterFactory.class);
		InterpreterFactory usefact = null;
		for (InterpreterFactory fact : ld)
			if (fact.toString().equalsIgnoreCase(useterp))
			{
				usefact = fact;
				break;
			}
		
		// {@squirreljme.error BC02 The interpreter with the specified name
		// does not exist. (The requested interpreter)}
		if (usefact == null)
			throw new IllegalArgumentException(String.format("BC02 %s",
				useterp));
		
		// Create the interpreter
		Interpreter terp = usefact.createInterpreter(null, __args);
		
		// {@squirreljme.error BC03 The interpreter factory did not produce
		// the interpreter with the specified name. (The requested
		// interpreter)}
		if (terp == null)
			throw new IllegalArgumentException(String.format("BC03 %s",
				useterp));
		
		// Create the kernel to use (use the interpreter arguments)
		List<String> initargs = terp.getInitialArguments();
		this.kernel = createKernel(terp,
			initargs.<String>toArray(new String[initargs.size()]));
		
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

