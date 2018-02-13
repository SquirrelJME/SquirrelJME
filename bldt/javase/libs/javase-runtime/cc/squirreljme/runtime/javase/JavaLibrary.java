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
import cc.squirreljme.kernel.lib.Library;
import cc.squirreljme.runtime.cldc.SystemResourceScope;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.nio.file.Path;
import net.multiphasicapps.zip.blockreader.FileChannelBlockAccessor;
import net.multiphasicapps.zip.blockreader.ZipBlockReader;
import net.multiphasicapps.zip.blockreader.ZipEntryNotFoundException;

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
		
		// Only consider resources
		if (__scope != SystemResourceScope.RESOURCE)
			return null;
		
		// Read data into temporary buffer
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream())
		{
			// Not compressed
			baos.write(Library.DATA_NORMAL);
			
			// Read in the data
			try (ZipBlockReader zip = new ZipBlockReader(
				new FileChannelBlockAccessor(this.binpath)))
			{
				byte[] buf = new byte[512];
				try (InputStream in = zip.open(__n))
				{
					for (;;)
					{
						int rc = in.read(buf);
					
						if (rc < 0)
							break;
					
						baos.write(buf, 0, rc);
					}
				}
			}
			
			// Does not exist
			catch (ZipEntryNotFoundException e)
			{
				return null;
			}
			
			return baos.toByteArray();
		}
		
		// {@squirreljme.error AF05 Failed to read the resource data.}
		catch (IOException e)
		{
			throw new RuntimeException("AF05", e);
		}
	}
}

