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
import net.multiphasicapps.io.inflate.InflaterInputStream;
import net.multiphasicapps.io.region.SizeLimitedInputStream;
import net.multiphasicapps.io.zlibdecompression.ZLibDecompressor;

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
	
	/** Palette data. */
	private volatile int[] _palette;
	
	/** Was an alpha channel used? */
	private volatile boolean _hasalpha;
	
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
			int lasttype = 0;
			try (DataInputStream data = new DataInputStream(
				new SizeLimitedInputStream(new __PNGCRCInputStream__(crc, in),
				len + 4, true, false)))
			{
				// Read the packet type
				int type = data.readInt();
				lasttype = type;
				
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
						__parsePalette(data, len);
						break;
						
						// Image data
					case 0x49444154:
						__parseImage(data);
						break;
						
						// Transparency information
					case 0x74524E53:
						throw new todo.TODO();
					
						// Unknown, ignore
					default:
						break;
				}
			}
			
			// {@squirreljme.error EB0x CRC mismatch in PNG data chunk.
			// (Desired CRC; Actual CRC; Last chunk type read)}
			int want = in.readInt(),
				real = crc.crc();
			if (want != real)
				throw new IOException(String.format("EB0x %08x %08x %08x",
					want, real, lasttype));
		}
		
		// {@squirreljme.error EB15 No image data has been loaded.}
		int[] argb = this._argb;
		if (argb == null)
			throw new IOException("EB15");
		
		// Create image
		return new Image(argb, this._width, this._height, false,
			this._hasalpha);
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
		
		// Debug
		System.err.printf("DEBUG -- Size: %dx%d%n", width, height);
		
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
		
		// These two color types have alpha
		this._hasalpha = (colortype == 4 || colortype == 6);
		
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
	
	/**
	 * Parses the PNG image data.
	 *
	 * @param __in The stream to read data from.
	 * @throws IOException On parse errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/28
	 */
	private void __parseImage(DataInputStream __in)
		throws IOException, NullPointerException
	{
		// Check
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// Get output
		int[] argb = this._argb;
		int[] palette = this._palette;
		int width = this._width,
			height = this._height,
			colortype = this._colortype,
			bitdepth = this._bitdepth,
			bitmask = (1 << bitdepth) - 1,
			numpals = (palette != null ? palette.length : 0);
		boolean adamseven = this._adamseven;
		
		// {@squirreljme.error EB16 Paletted PNG image has no palette.}
		if (colortype == 3 && palette == null)
			throw new IOException("EB16");
		
		// Calculate the number of bytes that are used to represent the image
		// pixel data.
		// Also the number of individual channels
		int readcount = bitdepth / 8,
			numchannels;
		if (readcount < 1)
			readcount = 1;
			
		// Image formats have multiple channels per set of data
		// RGB
		if (colortype == 2)
			numchannels = 3;
			
		// YA
		else if (colortype == 4)
			numchannels = 2;
		
		// RGBA
		else if (colortype == 6)
			numchannels = 4;
		
		// Just one channel
		else
			numchannels = 1;
		
		// Need to read all channel data
		readcount *= numchannels;
		int readbits = readcount * 8;
		
		// Input image data is compressed using deflate
		try (DataInputStream data = new DataInputStream(
			new ZLibDecompressor(__in)))
		{
			// Window used to pixel data, along with the remaining bits
			long window = 0;
			int valshift = 0;
			
			// All pixels are stored on scanlines
			int[] channels = new int[4];
			for (int vy = 0; vy < height; vy++)
			{
				// Clear the window, since pixels are reset on scanlines
				window = 0;
				valshift = -1;
				
				// Where data goes on the output ARGB array, linearly speaking
				int out = (vy * width);
				
				// Read in single line
				for (int vx = 0; vx < width; vx++)
				{
					// If the window is empty then read everything in
					if (valshift < 0)
					{
						// Read pixel
						for (int i = 0, sh = 0; i < readcount; i++)
						{
							window |= ((long)data.readUnsignedByte()) << sh;
							sh += 8;
						}
						
						// Initialize the rolling mask
						valshift = (readbits - bitdepth);
					}
					
					// Decode input channel data
					for (int i = 0; i < numchannels; i++)
					{
						// Read next color
						int v = ((int)(window >>> valshift)) & bitmask;
						valshift -= bitdepth;
						
						// 16-bit pixels are in MSB order, but input bytes are
						// read in little endian order
						if (bitdepth == 16)
							v = ((v >>> 8) & 0xFF) | ((v & 0xFF) << 8);
						
						// If not using paletted mode, adjust the colors so
						// that they become normalized to [0, 255]
						if (colortype != 3)
							if (bitdepth == 1)
								v = (v != 0 ? 255 : 0);
							else
								v = (v * 255) / bitmask;
						
						// Set
						channels[i] = v;
					}
					
					// Decode that information to ARGB
					int color;
					
					// Grayscale pixel
					if (colortype == 0 || colortype == 4)
					{
						int v = channels[0];
						color = (v << 16) | (v << 8) | v;
					}
					
					// Palette
					else if (colortype == 3)
					{
						// {@squirreljme.error EB17 Index exceeds the size of
						// a palette. (The index; The palette size)}
						int v = channels[0];
						if (v >= numpals)
							throw new IOException(String.format("EB17 %d %d",
								v, numpals));
						color = palette[v];
					}
					
					// Triplet
					else
						color = (channels[0] << 16) | (channels[1] << 8) |
							channels[2];
					
					// Alpha channel
					if (colortype == 4 || colortype == 6)
						color |= (channels[numchannels - 1] << 24);
					
					// No alpha
					else
						color |= 0xFF000000;
					
					// Place the pixel on the output buffer
					// Interlaced
					if (adamseven)
						throw new todo.TODO();
					
					// Linear
					else
						argb[out++] = color;
				}
			}
		}
	}
	
	/**
	 * Parses the PNG palette.
	 *
	 * @param __in The stream to read data from.
	 * @param __len The length of the palette data.
	 * @throws IOException On parse errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/28
	 */
	private void __parsePalette(DataInputStream __in, int __len)
		throws IOException, NullPointerException
	{
		// Check
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// Ignore the palette if this is not an indexed image
		if (this._colortype != 3)
			return;
		
		// Read color color
		int numcolors = __len / 3,
			maxcolors = 1 << this._bitdepth;
		if (numcolors > maxcolors)
			numcolors = maxcolors;
		
		// Load palette data
		int[] palette = new int[numcolors];
		this._palette = palette;
		for (int i = 0; i < numcolors; i++)
		{
			int r = __in.readUnsignedByte(),
				g = __in.readUnsignedByte(),
				b = __in.readUnsignedByte();
			
			// Fill in color
			palette[i] = (r << 16) | (g << 8) | b;
		}
	}
}

