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

import javax.tools.JavaCompiler;
import net.multiphasicapps.javac.CompilationFailedException;
import net.multiphasicapps.javac.CompilerException;

/**
 * This is the runnable which runs the compilation task.
 *
 * @since 2017/11/29
 */
public class HostCompilerRunnable
	implements Runnable
{
	/** The task to run. */
	protected final JavaCompiler.CompilationTask task;
	
	/**
	 * Initializes the compiler runnable.
	 *
	 * @param __t The task to for running.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/29
	 */
	public HostCompilerRunnable(JavaCompiler.CompilationTask __t)
		throws NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		this.task = __t;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/29
	 */
	@Override
	public void run()
	{
		try
		{
			// {@squirreljme.error BM03 Compilation failed.}
			if (!this.task.call())
				throw new CompilationFailedException("BM03");
		}
		
		// {@squirreljme.error BM03 The compiler has failed.}
		catch (RuntimeException e)
		{
			throw new CompilerException("BM03", e);
		}
	}
}

