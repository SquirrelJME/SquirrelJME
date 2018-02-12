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

import cc.squirreljme.kernel.lib.Library;
import cc.squirreljme.kernel.lib.LibraryType;
import cc.squirreljme.runtime.cldc.SystemResourceScope;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;

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
	protected final byte[] loadResourceBytes(SystemResourceScope __scope,
		String __n)
		throws NullPointerException
	{
		if (__scope == null || __n == null)
			throw new NullPointerException("NARG");
		
		// Load the system manifest resource
		if (__n.equals("META-INF/MANIFEST.MF"))
			try (ByteArrayOutputStream out = new ByteArrayOutputStream();
				InputStream in = JavaSystemLibrary.class.getResourceAsStream(
					"SYSTEM.MF"))
			{
				// Write single byte to say it is not compressed
				out.write(0);
				
				// Copy data
				byte[] buf = new byte[512];
				for (;;)
				{
					int rc = in.read(buf);
					
					if (rc < 0)
						break;
					
					out.write(buf, 0, rc);
				}
				
				out.flush();
				return out.toByteArray();
			}
			
			// {@squirreljme.error AF01 Could not read the system resource.}
			catch (IOException e)
			{
				throw new RuntimeException("AF01", e);
			}
		
		// All other resources do not exist
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/01/27
	 */
	@Override
	public final int type()
	{
		return LibraryType.SYSTEM;
	}
}

