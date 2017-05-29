// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import java.io.InputStream;
import net.multiphasicapps.squirreljme.jit.java.ClassCompiler;

/**
 * This is used to access the configuration which is needed by the JIT during
 * the JIT compilation set.
 *
 * @since 2017/05/29
 */
public abstract class JITConfig
{
	/**
	 * Creates an instance of the compiler for the given class file.
	 *
	 * @param __is The stream containing the class data to compile.
	 * @param __lt The link table which is given the compiled class data.
	 * @return The compilation task.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/05/29
	 */
	public abstract ClassCompiler compileClass(InputStream __is,
		LinkTable __lt)
		throws NullPointerException;
}

