// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import java.io.DataInputStream;
import java.io.IOException;
import net.multiphasicapps.io.crc32.CRC32Calculator;
import net.multiphasicapps.io.region.SizeLimitedInputStream;

/**
 * This class parses PNG images.
 *
 * PNG specifications:
 * {@link http://www.libpng.org/pub/png/pngdocs.html}
 * {@link http://www.libpng.org/pub/png/spec/iso/index-object.html}
 * {@link https://www.w3.org/TR/PNG/}
 * {@link https://tools.ietf.org/html/rfc2083}
 *
 * @since 2017/02/28
 */
class __PNGImageParser__
{
	/** The input source. */
	protected final DataInputStream in;
	
	/**
	 * Initializes the PNG parser.
	 *
	 * @param __in The input stream.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/28
	 */
	__PNGImageParser__(DataInputStream __in)
		throws NullPointerException
	{
		// Check
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.in = __in;
	}
	
	/**
	 * Parses the PNG image data.
	 *
	 * @return The read image.
	 * @since 2017/02/28
	 */
	Image __parse()
		throws IOException
	{
		DataInputStream in = this.in;
		
		// {@squirreljme.error EB0w Illegal PNG magic number.}
		if (in.readUnsignedByte() != 80 ||
			in.readUnsignedByte() != 78 ||
			in.readUnsignedByte() != 71 ||
			in.readUnsignedByte() != 13 ||
			in.readUnsignedByte() != 10 ||
			in.readUnsignedByte() != 26 ||
			in.readUnsignedByte() != 10)
			throw new IOException("EB0w");
		
		// Keep reading chunks in the file
		for (;;)
		{
			// {@squirreljme.erorr EB0x Length of chunk is negative.}
			int len = in.readInt();
			if (len < 0)
				throw new IOException("EB0x");
			
			// Setup data stream for reading packet data, do not propogate
			// close
			CRC32Calculator crc = new CRC32Calculator(true, true, 0x04C11DB7,
				0xFFFFFFFF, 0xFFFFFFFF);
			try (DataInputStream data = new DataInputStream(
				new __PNGCRCInputStream__(crc, new SizeLimitedInputStream(
				in, len + 4, true, false))))
			{
				// Read the packet type
				int type = data.readInt();
				
				// End of PNG, stop processing
				if (type == 0x49454E44)
					break;
				
				// Depends on the type
				switch (type)
				{
						// Header
					case 0x49484452:
						__parseHeader(data);
						break;
						
						// Palette
					case 0x504c5445:
						throw new todo.TODO();
						
						// Image data
					case 0x49444154:
						throw new todo.TODO();
						
						// Transparency information
					case 0x74524E53:
						throw new todo.TODO();
					
						// Unknown, ignore
					default:
						break;
				}
			}
			
			// {@squirreljme.error EB0x CRC mismatch in PNG data chunk.
			// (Desired CRC; Actual CRC)}
			int want = in.readInt(),
				real = crc.crc();
			if (want != real)
				throw new IOException(String.format("EB0x %08x %08x",
					want, real));
		}
		
		throw new todo.TODO();
	}
	
	/**
	 * Parses the PNG header.
	 *
	 * @param __in The stream to read data from.
	 * @throws IOException On parse errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/28
	 */
	private void __parseHeader(DataInputStream __in)
		throws IOException, NullPointerException
	{
		// Check
		if (__in == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
}

