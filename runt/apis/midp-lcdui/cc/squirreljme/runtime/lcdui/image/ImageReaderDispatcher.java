// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.image;

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.IOException;
import javax.microedition.lcdui.Image;

/**
 * This is used to dispatch to the correct parser when loading images.
 *
 * @since 2017/02/28
 */
public class ImageReaderDispatcher
{
	/**
	 * Not used.
	 *
	 * @since 2017/02/28
	 */
	private ImageReaderDispatcher()
	{
	}
	
	/**
	 * Parses the image stream.
	 *
	 * @param __is The stream to read from.
	 * @return The parsed image data.
	 * @throws IOException If it could not be parsed.
	 */
	public static Image parse(DataInputStream __is)
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
			return new PNGReader(__is).__parse();
		
		// JPEG?
		else if (head == 0xFF)
			throw new todo.TODO();
		
		// SVG?
		else if (head == '<')
			throw new todo.TODO();
		
		// {@squirreljme.error EB0s Could not detect the image format used
		// specified by the starting byte. (The starting byte)}
		else
			throw new IOException(String.format("EB0s %d", head));
	}
}

