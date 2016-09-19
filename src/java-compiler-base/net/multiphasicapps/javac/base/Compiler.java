// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.base;

import java.io.IOException;
import java.util.Iterator;

/**
 * This is the interface which is used to describe a compiler. It is 
 *
 * @since 2016/09/18
 */
public interface Compiler
{
	/**
	 * Compiles the specified files.
	 *
	 * @param __co The output of the compiler.
	 * @param __ci File input for the compiler.
	 * @param __opts The options that modify the state of compilation.
	 * @param __files The source code files to be compiled.
	 * @return {@code true} if compilation has succeeded, otherwise this will
	 * return {@code false}.
	 * @throws IOException On read/write errors.
	 * @since 2016/09/18
	 */
	public abstract boolean compile(CompilerOutput __co, CompilerInput __ci,
		Iterable<String> __opts, Iterable<String> __files)
		throws IOException;
}

