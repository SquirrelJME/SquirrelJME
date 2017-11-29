// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.builder.javase.javac;

import java.io.PrintStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.tools.JavaCompiler;
import net.multiphasicapps.io.PrintStreamWriter;
import net.multiphasicapps.javac.Compiler;
import net.multiphasicapps.javac.CompilerException;
import net.multiphasicapps.javac.CompilerInput;
import net.multiphasicapps.javac.CompilerInputLocation;
import net.multiphasicapps.javac.CompilerOptions;
import net.multiphasicapps.javac.CompilerOutput;
import net.multiphasicapps.javac.CompilerPathSet;

/**
 * This is the compiler which utilizes the system Java compiler, if one is
 * available.
 *
 * @since 2017/11/28
 */
public class HostCompiler
	extends Compiler
{
	/** The real Java compiler to use. */
	protected final JavaCompiler javac;
	
	/**
	 * Initializes the host compiler which uses the given Java compiler.
	 *
	 * @param __javac The Java compiler to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/28
	 */
	public HostCompiler(JavaCompiler __javac)
		throws NullPointerException
	{
		if (__javac == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.javac = __javac;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/29
	 */
	@Override
	protected Runnable newCompilerRunnable(CompilerOutput __out,
		PrintStream __log, CompilerOptions __opt,
		CompilerPathSet[][] __paths, CompilerInput[] __input)
		throws CompilerException, NullPointerException
	{
		if (__out == null || __log == null || __opt == null ||
			__paths == null || __input == null)
			throw new NullPointerException("NARG");
		
		JavaCompiler javac = this.javac;
		
		// Decode compilation options
		List<String> options = new ArrayList<>();
		
		// Which Java version to target?
		String targver;
		switch (__opt.version())
		{
			case JAVA_7:
				targver = "1.7";
			
				// {@squirreljme.error BM03 Unsupported Java version.}
			default:
				throw new CompilerException("BM03");
		}
		options.add("-source");
		options.add(targver);
		options.add("-target");
		options.add(targver);
		
		// Debugging?
		if (__opt.debug())
			options.add("-g");
		
		// Setup file manager lookup of files
		Writer log = new PrintStreamWriter(__log);
		HostFileManager fm = new HostFileManager(__paths, __out);
		
		// Wrap all input to file objects
		Set<InputHostFileObject> input = new LinkedHashSet<>();
		for (CompilerInput i : __input)
			input.add(new InputHostFileObject(fm, i));
		
		// Wrap task
		return new HostCompilerRunnable(javac.getTask(
			log, fm, null, options, null, input));
	}
}

