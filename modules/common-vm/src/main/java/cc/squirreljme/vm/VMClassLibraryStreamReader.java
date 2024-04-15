// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm;

import cc.squirreljme.runtime.cldc.archive.ArchiveStreamEntry;
import cc.squirreljme.runtime.cldc.archive.ArchiveStreamReader;
import java.io.Closeable;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Stream reader for any class library, could be used with the AOT framework.
 *
 * @since 2022/08/20
 */
public final class VMClassLibraryStreamReader
	implements ArchiveStreamReader
{
	/** The source library to read from. */
	private final VMClassLibrary _library;
	
	/** Iterator over library entries. */
	private Iterator<String> _iterator;
	
	/**
	 * Initializes the library reader.
	 * 
	 * @param __library The library to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/08/20
	 */
	public VMClassLibraryStreamReader(VMClassLibrary __library)
		throws NullPointerException
	{
		if (__library == null)
			throw new NullPointerException("NARG");
		
		this._library = __library;
		this._iterator = Arrays.asList(__library.listResources()).iterator();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/08/20
	 */
	@Override
	public void close()
		throws IOException
	{
		VMClassLibrary library = this._library;
		if (library instanceof Closeable)
			((Closeable)library).close();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/08/20
	 */
	@Override
	public ArchiveStreamEntry nextEntry()
		throws IOException
	{
		// Nothing left to process?
		Iterator<String> iterator = this._iterator;
		if (!iterator.hasNext())
			return null;
		
		// Setup next entry
		String name = iterator.next();
		return new VMClassLibraryStreamEntry(name,
			this._library.resourceAsStream(name));
	}
}
