// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.cute;

import java.io.PrintStream;
import net.multiphasicapps.javac.Compiler;
import net.multiphasicapps.javac.CompilerException;
import net.multiphasicapps.javac.CompilerInput;
import net.multiphasicapps.javac.CompilerInputLocation;
import net.multiphasicapps.javac.CompilerOptions;
import net.multiphasicapps.javac.CompilerOutput;
import net.multiphasicapps.javac.CompilerPathSet;

/**
 * This is used to create instances of the cute compiler.
 *
 * @since 2017/11/28
 */
public class CuteCompiler
	extends Compiler
{
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
		
		return new CuteRunnable(__out, __log, __opt, __paths, __input);
	}
}

