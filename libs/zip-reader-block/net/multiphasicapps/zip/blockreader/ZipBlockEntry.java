// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2017 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2017 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.zip.blockreader;

import java.io.InputStream;
import java.io.IOException;

/**
 * This represents a single entry within a ZIP file which may be opened.
 *
 * @since 2016/12/30
 */
public class ZipBlockEntry
{
	/**
	 * Initializes the block entry.
	 *
	 * @since 2016/12/30
	 */
	ZipBlockEntry()
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/12/30
	 */
	@Override
	public String toString()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Opens the input stream for this entry's data.
	 *
	 * @return The entry data.
	 * @throws IOException If it could not be opened.
	 * @since 2016/12/30
	 */
	public InputStream open()
		throws IOException
	{
		throw new Error("TODO");
	}
}

