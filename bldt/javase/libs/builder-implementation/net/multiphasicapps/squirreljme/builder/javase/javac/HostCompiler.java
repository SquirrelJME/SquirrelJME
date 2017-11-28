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
}

