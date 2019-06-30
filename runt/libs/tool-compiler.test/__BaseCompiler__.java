// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

import net.multiphasicapps.javac.Compiler;
import net.multiphasicapps.javac.CompilerException;
import net.multiphasicapps.javac.CompilerInput;
import net.multiphasicapps.javac.CompilerInputLocation;
import net.multiphasicapps.javac.CompilerOptions;
import net.multiphasicapps.javac.CompilerPathSet;
import net.multiphasicapps.javac.CompilerService;
import net.multiphasicapps.javac.ResourcePathSet;
import net.multiphasicapps.javac.NullCompilerOutput;
import net.multiphasicapps.tac.TestRunnable;

/**
 * This tests the cute compiler.
 *
 * @since 2019/06/30
 */
abstract class __BaseCompiler__
	extends TestRunnable
{
	/** Class files to compile. */
	static final String[] _JAVA_FILES =
		{"CrossTypeParameter.java", "HelloSquirrels.java",
		"IntAnnotation.java"};
	
	/** The compiler service to use. */
	protected final CompilerService service;
	
	/**
	 * Initializes the compiler.
	 *
	 * @param __cc The compiler to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/06/30
	 */
	__BaseCompiler__(CompilerService __cc)
		throws NullPointerException
	{
		if (__cc == null)
			throw new NullPointerException("NARG");
		
		this.service = __cc;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/06/30
	 */
	@Override
	public void test()
	{
		// Get service
		CompilerService service = this.service;
		
		// Class library
		CompilerPathSet cldc = new ResourcePathSet(
				this.getClass(), "/mini-cldc/"),
			src = new ResourcePathSet(this.getClass(), "/class-files/");
		
		// Go through sources
		for (String javafile : _JAVA_FILES)
			try
			{
				// Get new compiler
				Compiler compiler = service.createInstance();
				
				// Setup properties
				compiler.setLocation(CompilerInputLocation.CLASS, cldc);
				compiler.setLocation(CompilerInputLocation.SOURCE, src);
				compiler.addInput(src.input(javafile));
				
				// Run the compiler
				compiler.compile(new NullCompilerOutput()).run();
				
				// Okay!
				this.secondary(javafile, true);
			}
			catch (CompilerException e)
			{
				// Did not work!
				this.secondary(javafile, e);
			}
	}
}

