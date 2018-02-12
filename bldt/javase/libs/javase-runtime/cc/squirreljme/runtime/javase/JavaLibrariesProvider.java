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

import cc.squirreljme.kernel.impl.base.file.FileLibrariesProvider;
import cc.squirreljme.kernel.lib.Library;
import cc.squirreljme.kernel.lib.server.LibraryCompilerInput;
import cc.squirreljme.kernel.lib.server.LibrariesProvider;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import net.multiphasicapps.zip.blockreader.ZipBlockEntry;
import net.multiphasicapps.zip.streamwriter.ZipStreamWriter;

/**
 * This is used to manage programs which are natively installed on the Java
 * SE virtualized system.
 *
 * @since 2018/01/05
 */
public class JavaLibrariesProvider
	extends FileLibrariesProvider
{
	/**
	 * Initializes the server.
	 *
	 * @param __k The creating kernel.
	 * @since 2018/01/05
	 */
	public JavaLibrariesProvider()
	{
		// Register system library
		super.registerLibrary(new JavaSystemLibrary());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/02/11
	 */
	@Override
	protected final Library compileLibrary(LibraryCompilerInput __lci,
		Path __p, OutputStream __out)
		throws IOException, NullPointerException
	{
		if (__lci == null || __p == null || __out == null)
			throw new NullPointerException("NARG");
		
		// Just copy the ZIP contents, the Java SE VM takes care of everything
		// for the most part
		try (ZipStreamWriter zsw = new ZipStreamWriter(__out))
		{
			byte[] buf = new byte[512];
			for (ZipBlockEntry e : __lci.zip())
				try (InputStream in = e.open();
					OutputStream os = zsw.nextEntry(e.name()))
				{
					for (;;)
					{
						int rc = in.read(buf);
						
						if (rc < 0)
							break;
						
						os.write(buf, 0, rc);
					}
				}
		}
		
		// Setup library for it
		return new JavaLibrary(__p);
	}
}

