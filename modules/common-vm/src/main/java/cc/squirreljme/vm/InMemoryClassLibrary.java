// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import net.multiphasicapps.zip.streamreader.ZipStreamEntry;
import net.multiphasicapps.zip.streamreader.ZipStreamReader;

/**
 * This represents a class library which has been read into memory.
 *
 * @since 2018/12/08
 */
public final class InMemoryClassLibrary
	implements VMClassLibrary
{
	/** The name of this library. */
	protected final String name;
	
	/** The cache. */
	private final Map<String, byte[]> _cache;
	
	/**
	 * Internally initializes the library.
	 *
	 * @param __n The name.
	 * @param __m The internal mapping.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/12/08
	 */
	private InMemoryClassLibrary(String __n, Map<String, byte[]> __m)
		throws NullPointerException
	{
		if (__n == null || __m == null)
			throw new NullPointerException("NARG");
		
		this.name = __n;
		this._cache = __m;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/21
	 */
	@Override
	public final String[] listResources()
	{
		Collection<String> names = this._cache.keySet();
		return names.<String>toArray(new String[names.size()]);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/08
	 */
	@Override
	public final String name()
	{
		return this.name;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/08
	 */
	@Override
	public final InputStream resourceAsStream(String __rc)
		throws IOException, NullPointerException
	{
		if (__rc == null)
			throw new NullPointerException("NARG");
		
		// See if the byte data exists
		byte[] rv = this._cache.get(__rc);
		if (rv == null)
			return null;
		
		// Then use a byte array on it
		return new ByteArrayInputStream(rv);
	}
	
	/**
	 * Loads ZIP file data.
	 *
	 * @param __n The library name.
	 * @param __in The input stream.
	 * @return The class library.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/12/08
	 */
	@SuppressWarnings("resource")
	public static final InMemoryClassLibrary loadZip(String __n,
		ZipStreamReader __in)
		throws IOException, NullPointerException
	{
		if (__n == null || __in == null)
			throw new NullPointerException("NARG");
		
		// Target map
		Map<String, byte[]> rv = new HashMap<>();
		
		// Temporary buffer to reading class data
		byte[] buf = new byte[4096];
		
		// Process all entries
		ZipStreamEntry zse;
		while (true)
		{
			// No more entries?
			zse = __in.nextEntry();
			if (zse == null)
				break;
			
			// Copy data over
			try (ByteArrayOutputStream baos =
				new ByteArrayOutputStream(8192))
			{
				for (;;)
				{
					int rc = zse.read(buf);
					
					if (rc < 0)
						break;
					
					baos.write(buf, 0, rc);
				}
				
				// Cache
				rv.put(zse.name(), baos.toByteArray());
			}
			
			// Close
			zse.close();
		}
		
		return new InMemoryClassLibrary(__n, rv);
	}
}

