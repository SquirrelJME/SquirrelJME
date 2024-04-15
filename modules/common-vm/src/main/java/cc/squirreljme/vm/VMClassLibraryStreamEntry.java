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
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;
import java.io.InputStream;

/**
 * Provides access to a class library entry.
 *
 * @since 2022/08/20
 */
public final class VMClassLibraryStreamEntry
	implements ArchiveStreamEntry
{
	/** The stream to read from. */
	private final InputStream _input;
	
	/** The name of the entry. */
	private final String _name;
	
	/**
	 * Initializes the reader for the given entry.
	 * 
	 * @param __name The name of the entry.
	 * @param __input The stream to read from.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/08/20
	 */
	public VMClassLibraryStreamEntry(String __name, InputStream __input)
		throws NullPointerException
	{
		if (__name == null || __input == null)
			throw new NullPointerException("NARG");
		
		this._name = __name;
		this._input = __input;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/08/20
	 */
	@Override
	public void close()
		throws IOException
	{
		this._input.close();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/08/20
	 */
	@Override
	public String name()
	{
		return this._name;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/08/20
	 */
	@Override
	public int read()
		throws IOException
	{
		return this._input.read();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/08/20
	 */
	@Override
	public int read(byte[] __b)
		throws IOException, NullPointerException
	{
		return this._input.read(__b);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/08/20
	 */
	@Override
	public int read(byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, IOException, NullPointerException
	{
		return this._input.read(__b, __o, __l);
	}
}
