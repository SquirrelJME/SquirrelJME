// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.builder.support.vm;

import cc.squirreljme.builder.support.Binary;
import cc.squirreljme.vm.VMClassLibrary;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.multiphasicapps.zip.blockreader.ZipBlockReader;
import net.multiphasicapps.zip.blockreader.ZipBlockEntry;
import net.multiphasicapps.zip.blockreader.ZipEntryNotFoundException;

/**
 * This represents a single library that is layered on top of the binary
 * provided by the build system.
 *
 * @since 2018/09/13
 */
public final class BuildClassLibrary
	implements VMClassLibrary
{
	/** The binary to source from. */
	protected final Binary binary;
	
	/**
	 * Initializes the library.
	 *
	 * @param __b The binary to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/13
	 */
	public BuildClassLibrary(Binary __b)
		throws NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		
		this.binary = __b;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/21
	 */
	@Override
	public final String[] listResources()
	{
		// Build name list
		List<String> rv = new ArrayList<>();
		try (ZipBlockReader zip = this.binary.zipBlock())
		{
			for (ZipBlockEntry e : zip)
				rv.add(e.name());
		}
		
		// Failed read
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
		
		// Return it
		return rv.<String>toArray(new String[rv.size()]);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/13
	 */
	@Override
	public final String name()
	{
		return this.binary.name().toString() + ".jar";
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/13
	 */
	@Override
	public final InputStream resourceAsStream(String __rc)
		throws IOException, NullPointerException
	{
		if (__rc == null)
			throw new NullPointerException("NARG");
		
		try (ZipBlockReader zip = this.binary.zipBlock())
		{
			// Might not exist
			try (InputStream in = zip.open(__rc))
			{
				// Copy all the data
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte[] buf = new byte[512];
				for (;;)
				{
					int rc = in.read(buf);
					
					if (rc < 0)
						break;
					
					baos.write(buf, 0, rc);
				}
				
				// Wrap and return
				return new ByteArrayInputStream(baos.toByteArray());
			}
			
			// No entry exists to be read
			catch (ZipEntryNotFoundException e)
			{
				return null;
			}
		}
	}
}

