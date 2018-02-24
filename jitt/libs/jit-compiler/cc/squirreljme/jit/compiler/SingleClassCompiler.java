// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jit.compiler;

import cc.squirreljme.jit.classfile.ClassFile;
import cc.squirreljme.jit.library.Library;
import cc.squirreljme.jit.objectfile.ObjectFile;

/**
 * This is a compiler which only transforms a single class that has been
 * input.
 *
 * @since 2018/02/23
 */
public final class SingleClassCompiler
{
	/**
	 * Initializes the compiler for the class.
	 *
	 * @param __cf The input class to compile.
	 * @param __lib The state of the libraries being compiled.
	 * @param __of The object file to write to for compilation.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/02/23
	 */
	public SingleClassCompiler(ClassFile __cf, LibraryState __lib,
		ObjectFile __of)
		throws NullPointerException
	{
		if (__cf == null || __lib == null || __of == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

