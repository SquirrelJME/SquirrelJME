// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator;

import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import java.io.IOException;
import java.io.InputStream;
import net.multiphasicapps.zip.blockreader.ZipBlockEntry;

/**
 * Not Described.
 *
 * @since 2024/03/05
 */
public class EmulatedNativeZipArchiveEntryBracket
	extends EmulatedNativeArchiveEntryBracket
{
	/** The entry. */
	protected final ZipBlockEntry entry;
	
	/**
	 * Initializes the Zip entry bracket.
	 *
	 * @param __entry The entry to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/03/05
	 */
	public EmulatedNativeZipArchiveEntryBracket(ZipBlockEntry __entry)
		throws NullPointerException
	{
		if (__entry == null)
			throw new NullPointerException("NARG");
		
		this.entry = __entry;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/05
	 */
	@Override
	public boolean isDirectory()
	{
		try
		{
			return this.entry.isDirectory();
		}
		catch (IOException __e)
		{
			throw new MLECallError("Invalid entry.", __e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/05
	 */
	@Override
	public InputStream open()
		throws MLECallError
	{
		try
		{
			return this.entry.open();
		}
		catch (IOException __e)
		{
			throw new MLECallError("Invalid entry.", __e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/05
	 */
	@Override
	public long uncompressedSize()
	{
		try
		{
			return this.entry.uncompressedSize();
		}
		catch (IOException __e)
		{
			throw new MLECallError("Invalid entry.", __e);
		}
	}
}
