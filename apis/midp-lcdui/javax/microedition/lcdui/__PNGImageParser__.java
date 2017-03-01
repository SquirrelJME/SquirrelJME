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
	
	/** Image width. */
	private volatile int _width;
	
	/** Image height. */
	private volatile int _height;
	
	/** The bit depth. */
	private volatile int _bitdepth;
	
	/** The color type. */
	private volatile int _colortype;
	
	/** Is adam7 interlacing being used? */
	private volatile boolean _adamseven;
	
	/** RGB image data. */
	private volatile int[] _argb;
	
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
		
		// {@squirreljme.error EB0y Image has zero or negative width.
		// (The width)}
		int width = __in.readInt();
		if (width <= 0)
			throw new IOException(String.format("EB0y %d", width));
		this._width = width;
		
		// {@squirreljme.error EB0z Image has zero or negative height. (The
		// height)}
		int height = __in.readInt();
		if (height <= 0)
			throw new IOException(String.format("EB0z %d", height));
		this._height = height;
		
		// Read the bit depth and the color type
		int bitdepth = __in.readUnsignedByte(),
			colortype = __in.readUnsignedByte();
		
		// {@squirreljme.error EB12 Invalid PNG bit depth.
		// (The bit depth)}
		if (Integer.bitCount(bitdepth) != 1 || bitdepth < 0 || bitdepth > 16)
			throw new IOException(String.format("EB12 %d", bitdepth));
		
		// {@squirreljme.error EB11 Invalid PNG bit depth and color type
		// combination. (The color type; The bit depth)}
		if ((bitdepth < 8 && (colortype != 0 && colortype != 3)) ||
			(bitdepth > 8 && colortype != 3))
			throw new IOException(String.format("EB11 %d %d", colortype,
				bitdepth));
			
		// Set
		this._bitdepth = bitdepth;
		this._colortype = colortype;
		
		// {@squirreljme.error EB10 Only deflate compressed PNG images are
		// supported. (The compression method)}
		int compressionmethod = __in.readUnsignedByte();
		if (compressionmethod != 0)
			throw new IOException(String.format("EB10 %d", compressionmethod));
		
		// {@squirreljme.error EB13 Only adapative filtered PNGs are supported.
		// (The filter type)}
		int filter = __in.readUnsignedByte();
		if (filter != 0)
			throw new IOException(String.format("EB13 %d", filter));
		
		// {@squirreljme.error EB14 Unsupported PNG interlace method. (The
		// interlace type)}
		int interlace = __in.readUnsignedByte();
		if (interlace != 0 && interlace != 1)
			throw new IOException(String.format("EB14 %d", interlace));
		this._adamseven = (interlace == 1);
		
		// Allocate image buffer
		this._argb = new int[width * height];
	}
}

