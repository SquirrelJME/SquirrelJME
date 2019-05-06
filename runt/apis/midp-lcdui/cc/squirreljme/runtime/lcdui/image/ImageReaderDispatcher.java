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
	public static Image parse(InputStream __is)
		throws IOException, NullPointerException
	{
		// Check
		if (__is == null)
			throw new NullPointerException("NARG");
		
		// Mark header
		__is.mark(4);
		
		// Read in the header magic number
		int first = (__is.read() & 0xFF);
		int magic = (first << 24) |
			((__is.read() & 0xFF) << 16) |
			((__is.read() & 0xFF) << 8) |
			(__is.read() & 0xFF);
		
		// Reset for future read
		__is.reset();
		
		// GIF?
		if (magic == 0x47494638)
			throw new todo.TODO();
		
		// PNG?
		else if (magic == 0x89504E47)
			return new PNGReader(__is).parse();
		
		// JPEG?
		else if ((magic & 0xFFFFFF00) == 0xFFD8FF00)
			return new JPEGReader(__is).parse();
		
		// SVG?
		else if (first == '<')
			throw new todo.TODO();
		
		// XPM?
		else if (first == '/')
			return new XPMReader(__is).parse();
		
		// {@squirreljme.error EB0s Could not detect the image format used
		// specified by the starting byte. (The magic number; The first byte)}
		else
			throw new IOException(String.format("EB0s %08x %02x", magic,
				first));
	}
}

