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
 * This contains the state for a library which is being compiled.
 *
 * @since 2018/02/24
 */
public final class LibraryState
{
	/** The current library being compiled. */
	protected final Library current;
	
	/** The dependencies this library depends upon. */
	private final Library[] _depends;
	
	/**
	 * Initializes the current library state.
	 *
	 * @param __deps The dependencies used for this library compilation.
	 * @param __current The current library being compiled.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/02/24
	 */
	public LibraryState(Library[] __deps, Library __current)
		throws NullPointerException
	{
		if (__deps == null || __current == null)
			throw new NullPointerException("NARG");
		
		this._depends = __deps.clone();
		this.current = __current;
	}
	
	/**
	 * Compiles all classes in this library to the given object file.
	 *
	 * @param __of The object file to compile into.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/02/24
	 */
	public void compile(ObjectFile __of)
		throws NullPointerException
	{
		if (__of == null)
			throw new NullPointerException("NARG");
		
		// Compile all classes to the given object file
		for (ClassFile cl : this.current.classes())
			new SingleClassCompiler(cl, this, __of);
	}
}

