// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.builder.support;

import cc.squirreljme.jit.library.Library;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import net.multiphasicapps.collections.UnmodifiableList;
import net.multiphasicapps.zip.blockreader.ZipBlockReader;
import net.multiphasicapps.zip.blockreader.ZipBlockEntry;
import net.multiphasicapps.zip.blockreader.ZipEntryNotFoundException;

/**
 * This wraps a binary and provides a library used by the JIT.
 *
 * @since 2018/02/21
 */
public final class BinaryLibrary
	extends Library
{
	/** The binary to wrap. */
	protected final Binary binary;
	
	/** Iterable data source. */
	private volatile Reference<Iterable<String>> _entries;
	
	/**
	 * Initializes the library.
	 *
	 * @param __bin The binary to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/02/21
	 */
	public BinaryLibrary(Binary __bin)
		throws NullPointerException
	{
		super(__bin.name().toString());
		
		if (__bin == null)
			throw new NullPointerException("NARG");
		
		this.binary = __bin;	
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/02/23
	 */
	@Override
	public final Iterable<String> entries()
	{
		Reference<Iterable<String>> ref = this._entries;
		Iterable<String> rv;
		
		if (ref == null || null == (rv = ref.get()))
			try (ZipBlockReader zip = this.binary.zipBlock())
			{
				List<String> list = new ArrayList<>();
				
				for (ZipBlockEntry e : zip)
					list.add(e.name());
				
				// Store 
				this._entries = new WeakReference<>(
					(rv = UnmodifiableList.<String>of(list)));
			}
			
			// {@squirreljme.error AU15 Could not get entry list for library.}
			catch (IOException e)
			{
				throw new RuntimeException("AU15");
			}
		
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/02/23
	 */
	@Override
	public final InputStream open(String __name)
		throws NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		try (ZipBlockReader zip = this.binary.zipBlock();
			InputStream is = zip.open(__name))
		{
			try (ByteArrayOutputStream baos = new ByteArrayOutputStream())
			{
				byte[] buf = new byte[512];
				
				for (;;)
				{
					int rc = is.read(buf);
					
					if (rc < 0)
						return new ByteArrayInputStream(baos.toByteArray());
					
					baos.write(buf, 0, rc);
				}
			}
		}
		
		// No entry exists
		catch (ZipEntryNotFoundException e)
		{
			return null;
		}
		
		// {@squirreljme.error AU16 Could not read the ZIP entry.}
		catch (IOException e)
		{
			throw new RuntimeException("AU16", e);
		}
	}
}

