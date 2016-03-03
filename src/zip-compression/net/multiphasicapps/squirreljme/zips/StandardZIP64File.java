// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.zips;

import java.io.IOException;
import java.nio.channels.SeekableByteChannel;

/**
 * This class provides read access to 64-bit ZIP files.
 *
 * 64-bit ZIPs are able to store very large files and be very large themselves.
 *
 * @since 2016/03/02
 */
public class StandardZIP64File
	extends StandardZIPFile
{
	/**
	 * Initializes a 64-bit ZIP file.
	 *
	 * @param __sbc The source channel to read from.
	 * @throws IOException On read errors.
	 * @throws ZIPFormatException If this is not a valid ZIP file.
	 * @since 2016/03/02
	 */
	public StandardZIP64File(SeekableByteChannel __sbc)
		throws IOException, ZIPFormatException
	{
		super(__sbc);
		
		throw new ZIPFormatException("TODO");
	}
}

