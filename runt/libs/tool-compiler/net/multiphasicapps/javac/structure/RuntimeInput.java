// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.structure;

import net.multiphasicapps.javac.CompilerPathSet;

/**
 * This class contians the input for the .
 *
 * @since 2018/05/03
 */
public final class RuntimeInput
{
	/** The class path. */
	private final CompilerPathSet[] _classpath;
	
	/** The source path. */
	private final CompilerPathSet[] _sourcepath;
	
	/**
	 * Initializes the runtime input.
	 *
	 * @param __class The class path.
	 * @param __src The source path.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/05/03
	 */
	public RuntimeInput(CompilerPathSet[] __class, CompilerPathSet[] __src)
		throws NullPointerException
	{
		if (__class == null || __src == null)
			throw new NullPointerException("NARG");
		
		// Check classpath
		__class = __class.clone();
		for (CompilerPathSet p : __class)
			if (p == null)
				throw new NullPointerException("NARG");
		
		// Check sources
		__src = __src.clone();
		for (CompilerPathSet p : __src)
			if (p == null)
				throw new NullPointerException("NARG");
		
		this._classpath = __class;
		this._sourcepath = __src;
	}
}

