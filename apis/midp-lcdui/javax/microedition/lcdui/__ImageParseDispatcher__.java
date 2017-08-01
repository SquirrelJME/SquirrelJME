// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.IOException;

/**
 * This is used to dispatch to the correct parser when loading images.
 *
 * @since 2017/02/28
 */
final class __ImageParseDispatcher__
{
	/**
	 * Not used.
	 *
	 * @since 2017/02/28
	 */
	private __ImageParseDispatcher__()
	{
	}
	
	/**
	 * Parses the image stream.
	 *
	 * @param __is The stream to read from.
	 * @return The parsed image data.
	 * @throws IOException If it could not be parsed.
	 */
	static Image __parse(DataInputStream __is)
		throws IOException, NullPointerException
	{
		// Check
		if (__is == null)
			throw new NullPointerException("NARG");
		
		// Read first byte
		int head = __is.readUnsignedByte();
		
		// GIF?
		if (head == 0x47)
			throw new todo.TODO();
		
		// PNG?
		else if (head == 0x89)
			return new __PNGImageParser__(__is).__parse();
		
		// JPEG?
		else if (head == 0xFF)
			throw new todo.TODO();
		
		// SVG?
		else if (head == '<')
			throw new todo.TODO();
		
		// {@squirreljme.error EB0v Could not detect the image format used
		// specified by the starting byte. (The starting byte)}
		else
			throw new IOException(String.format("EB0v %d", head));
	}
}

