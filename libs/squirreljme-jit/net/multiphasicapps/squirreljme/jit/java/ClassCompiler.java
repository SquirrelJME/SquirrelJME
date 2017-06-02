// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.java;

import java.io.InputStream;
import java.io.IOException;
import net.multiphasicapps.squirreljme.jit.ClusterIdentifier;
import net.multiphasicapps.squirreljme.jit.JITConfig;
import net.multiphasicapps.squirreljme.jit.LinkTable;

/**
 * This class reads the Java class file format and then passes that to the
 * JIT compiler which recompiles the given class.
 *
 * @since 2017/05/29
 */
public final class ClassCompiler
	implements Runnable
{
	/**
	 * Creates an instance of the compiler for the given class file.
	 *
	 * @param __jc The JIT configuration.
	 * @param __is The stream containing the class data to compile.
	 * @param __ci The cluster the class is in.
	 * @param __lt The link table which is given the compiled class data.
	 * @return The compilation task.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/06/02
	 */
	public ClassCompiler(JITConfig __jc, InputStream __is,
		ClusterIdentifier __ci, LinkTable __lt)
		throws NullPointerException
	{
		// Check
		if (__jc == null || __is == null || __ci == null || __lt == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/06/02
	 */
	@Override
	public void run()
	{
		throw new todo.TODO();
	}
}

