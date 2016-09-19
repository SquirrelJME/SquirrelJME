// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.bootstrap.javase;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import net.multiphasicapps.javac.base.Compiler;
import net.multiphasicapps.javac.base.CompilerInput;
import net.multiphasicapps.javac.base.CompilerOutput;

/**
 * This bridges Java SE's compiler to the SquirrelJME bootstrap system.
 *
 * @since 2016/09/18
 */
public class BridgedJavaCompiler
	implements Compiler
{
	/** The Java compiler instance. */
	protected final JavaCompiler javac;
	
	/**
	 * Initializes the bridged compiler.
	 *
	 * @since 2016/09/18
	 */
	public BridgedJavaCompiler()
	{
		// {@squirreljme.error DE01 No system Java compiler is available.}
		JavaCompiler javac = ToolProvider.getSystemJavaCompiler();
		if (javac == null)
			throw new RuntimeException("DE01");
		this.javac = javac;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/18
	 */
	@Override
	public boolean compile(PrintStream __log, CompilerOutput __co,
		CompilerInput __ci, Iterable<String> __opts, Iterable<String> __files)
		throws IOException
	{
		// Check
		if (__log == null || __co == null || __ci == null || __opts == null ||
			__files == null)
			throw new NullPointerException("NARG");
		
		// Get the compiler
		JavaCompiler javac = this.javac;
		
		// Setup virtual file manager to wrap the compiler inputs and
		// outputs
		StandardJavaFileManager jfm = null;
		if (true)
			throw new Error("TODO");
		
		// Determine files to compile
		Set<JavaFileObject> ccthese = new LinkedHashSet<>();
		for (String f : __files)
			for (JavaFileObject o : jfm.getJavaFileObjects(f))
				ccthese.add(o);
		
		// Create the compilation task
		JavaCompiler.CompilationTask task = javac.getTask(
			new OutputStreamWriter(__log), jfm, null, __opts, null, ccthese);
		
		// Run the task
		return task.call();
	}
}

