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

import cc.squirreljme.kernel.lib.client.Library;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * This is the system library which just provides the special overridden
 * manifest for server usage.
 *
 * @since 2018/01/15
 */
public final class JavaSystemLibrary
	extends Library
{
	/**
	 * Initializes the library.
	 *
	 * @since 2018/01/15
	 */
	public JavaSystemLibrary()
	{
		super(0);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/01/15
	 */
	@Override
	protected final byte[] loadResourceBytes(String __n)
		throws NullPointerException
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// Load the system manifest resource
		if (__n.equals("META-INF/MANIFEST.MF"))
		{
			throw new todo.TODO();
		}
		
		// All other resources do not exist
		return null;
	}
}

