// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.zips;

import java.io.IOException;
import java.nio.channels.SeekableByteChannel;

/**
 * This class provides read access to 64-bit ZIP files.
 *
 * 64-bit ZIPs are able to store very large files and be very large themselves.
 *
 * @since 2016/03/02
 */
public class Zip64File
	extends ZipFile
{
	/**
	 * Initializes a 64-bit ZIP file.
	 *
	 * @param __sbc The source channel to read from.
	 * @throws IOException On read errors.
	 * @throws ZipFormatException If this is not a valid ZIP file.
	 * @since 2016/03/02
	 */
	public Zip64File(SeekableByteChannel __sbc)
		throws IOException, ZipFormatException
	{
		super(__sbc);
		
		throw new ZipFormatException("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/05
	 */
	@Override
	protected ZipDirectory readDirectory()
		throws IOException
	{
		throw new Error("TODO");
	}
}

