// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator;

import cc.squirreljme.jvm.mle.brackets.NativeArchiveBracket;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.Closeable;
import java.io.IOException;
import net.multiphasicapps.zip.blockreader.ZipBlockEntry;
import net.multiphasicapps.zip.blockreader.ZipBlockReader;

/**
 * Bracket for emulated memory mapped Zips.
 *
 * @since 2024/03/05
 */
public class EmulatedNativeZipArchiveBracket
	extends EmulatedNativeArchiveBracket
{
	/** The emulated Zip. */
	protected final ZipBlockReader zip;
	
	/**
	 * Initializes the emulated Zip bracket.
	 *
	 * @param __zip The Zip to access.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/03/05
	 */
	public EmulatedNativeZipArchiveBracket(ZipBlockReader __zip)
		throws NullPointerException
	{
		if (__zip == null)
			throw new NullPointerException("NARG");
		
		this.zip = __zip;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/05
	 */
	@Override
	public void close()
		throws IOException
	{
		this.zip.close();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/05
	 */
	@Override
	protected EmulatedNativeArchiveEntryBracket entry(String __name)
		throws MLECallError
	{
		if (__name == null)
			throw new MLECallError("Null arguments.");
		
		try
		{
			ZipBlockEntry entry = this.zip.get(__name);
			if (entry == null)
				return null;
			
			return new EmulatedNativeZipArchiveEntryBracket(entry);
		}
		catch (IOException __e)
		{
			throw new MLECallError("Could not read Zip archive.", __e);
		}
	}
}
