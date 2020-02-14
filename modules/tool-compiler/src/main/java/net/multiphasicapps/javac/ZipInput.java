// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac;

import java.io.InputStream;
import java.io.IOException;
import net.multiphasicapps.zip.blockreader.ZipBlockEntry;

/**
 * This wraps a single entry within a ZIP file.
 *
 * @since 2017/11/29
 */
public final class ZipInput
	implements CompilerInput
{
	/** The entry to wrap. */
	protected final ZipBlockEntry entry;
	
	/**
	 * Initializes the ZIP input.
	 *
	 * @param __e The entry to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/29
	 */
	public ZipInput(ZipBlockEntry __e)
		throws NullPointerException
	{
		if (__e == null)
			throw new NullPointerException("NARG");
		
		this.entry = __e;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/29
	 */
	@Override
	public final boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		
		if (!(__o instanceof ZipInput))
			return false;
		
		return this.entry.equals(((ZipInput)__o).entry);
	}

	/**
	 * {@inheritDoc}
	 * @since 2017/11/29
	 */
	@Override
	public final String fileName()
		throws CompilerException
	{
		return this.entry.name();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/29
	 */
	@Override
	public final int hashCode()
	{
		return this.fileName().hashCode();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/06
	 */
	@Override
	public final long lastModifiedTime()
		throws CompilerException
	{
		return this.entry.lastModifiedTime();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/29
	 */
	@Override
	public final InputStream open()
		throws CompilerException, NoSuchInputException
	{
		try
		{
			return this.entry.open();
		}
		
		// {@squirreljme.error AQ0d Failed to open the ZIP entry. (The name
		// of the entry)}
		catch (IOException e)
		{
			throw new CompilerException(
				String.format("AQ0d %s", this.fileName()), e);
		}
	}
}

