// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.projects;

import java.io.InputStream;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import net.multiphasicapps.squirreljme.bootstrap.base.compiler.CompilerInput;

/**
 * DESCRIBE THIS.
 *
 * @since 2016/09/19
 */
class __CompilerInput__
	implements CompilerInput
{
	/** The project list. */
	protected final ProjectList list;
	
	/** The project information. */
	protected final ProjectInfo info;
	
	/**
	 * Initializes the input for the compiler.
	 *
	 * @param __pl The project list.
	 * @param __src The source project being compiled.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/19
	 */
	__CompilerInput__(ProjectList __pl, ProjectInfo __src)
		throws NullPointerException
	{
		// Check
		if (__pl == null || __src == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.list = __pl;
		this.info = __src;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/19
	 */
	@Override
	public InputStream input(String __name)
		throws IOException, NoSuchFileException
	{
		throw new Error("TODO");
	}
}

