// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.emulator;

import java.io.IOException;
import net.multiphasicapps.zip.blockreader.ZipEntry;
import net.multiphasicapps.zip.blockreader.ZipFile;

/**
 * This is a volume which provides access to its files through a ZIP file.
 *
 * @since 2016/07/30
 */
public class ZipFileVolume
	implements Volume
{
	/** The ZIP to use. */
	protected final ZipFile zip;
	
	/**
	 * Initializes the volume using the given ZIP.
	 *
	 * @param __zip The ZIP file to use as a volume.
	 * @since 2016/07/30
	 */
	public ZipFileVolume(ZipFile __zip)
	{
		// Check
		if (__zip == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.zip = __zip;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/30
	 */
	@Override
	public void close()
		throws IOException
	{
		this.zip.close();
	}
}

