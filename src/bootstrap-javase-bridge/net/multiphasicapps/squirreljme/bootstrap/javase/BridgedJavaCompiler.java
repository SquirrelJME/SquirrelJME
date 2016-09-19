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
import javax.tools.JavaCompiler;
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
	public boolean compile(CompilerOutput __co, CompilerInput __ci)
		throws IOException
	{
		throw new Error("TODO");
	}
}

