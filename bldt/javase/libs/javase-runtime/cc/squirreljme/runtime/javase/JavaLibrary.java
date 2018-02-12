// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.javase;

import cc.squirreljme.kernel.impl.base.file.FileLibrary;
import cc.squirreljme.runtime.cldc.SystemResourceScope;
import java.nio.file.Path;

/**
 * This represents a library in the Java filesystem.
 *
 * @since 2018/02/11
 */
public class JavaLibrary
	extends FileLibrary
{
	/**
	 * Initializes the library.
	 *
	 * @param __p The path to the library.
	 * @since 2018/02/011
	 */
	public JavaLibrary(Path __p)
	{
		super(__p);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/02/11
	 */
	@Override
	protected byte[] loadResourceBytes(SystemResourceScope __scope,
		String __n)
		throws NullPointerException
	{
		if (__scope == null || __n == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

