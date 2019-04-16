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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.Arrays;
import javax.microedition.lcdui.Image;
import net.multiphasicapps.io.ChecksumInputStream;
import net.multiphasicapps.io.CRC32Calculator;
import net.multiphasicapps.io.InflaterInputStream;
import net.multiphasicapps.io.SizeLimitedInputStream;
import net.multiphasicapps.io.ZLibDecompressor;

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
public class PNGReader
{
	/** The input source. */
	protected final DataInputStream in;
	
	/** Image width. */
	private int _width;
	
	/** Scanline length. */
	private int _scanlen;
	
	/** Image height. */
	private int _height;
	
	/** The bit depth. */
	private int _bitdepth;
	
	/** The color type. */
	private int _colortype;
	
	/** Is adam7 interlacing being used? */
	private boolean _adamseven;
	
	/** RGB image data. */
	private int[] _argb;
	
	/** Palette data. */
	private int[] _palette;
	
	/** Was an alpha channel used? */
	private boolean _hasalpha;
	
	/** Initial value for read Y position, for multiple IDAT chunks. */
	private int _initvy;
	
	/** Initial value for read X position, for multiple IDAT chunks. */
	private int _initvx;
	
	/** The number of colors used. */
	private int _numcolors;
	
	/** The maximum number of permitted colors. */
	private int _maxcolors;
	
	/**
	 * Initializes the PNG parser.
	 *
	 * @param __in The input stream.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/28
	 */
	PNGReader(DataInputStream __in)
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
		
		// {@squirreljme.error EB0t Illegal PNG magic number.}
		if (in.readUnsignedByte() != 80 ||
			in.readUnsignedByte() != 78 ||
			in.readUnsignedByte() != 71 ||
			in.readUnsignedByte() != 13 ||
			in.readUnsignedByte() != 10 ||
			in.readUnsignedByte() != 26 ||
			in.readUnsignedByte() != 10)
			throw new IOException("EB0t");
		
		// Some J2ME games such as Bobby Carrot have invalid PNG files that
		// contain a tRNS chunk after the IDAT chunk. This violates the PNG
		// standard so the image chunk has to cached and process later,
		// otherwise the images will be corrupt.
		byte[] imagechunk = null;
		
		// Keep reading chunks in the file
		for (;;)
		{
			// {@squirreljme.erorr EB1l Length of chunk is negative.}
			int len = in.readInt();
			if (len < 0)
				throw new IOException("EB1l");
			
			// Setup data stream for reading packet data, do not propogate
			// close
			CRC32Calculator crc = new CRC32Calculator(true, true, 0x04C11DB7,
				0xFFFFFFFF, 0xFFFFFFFF);
			int lasttype = 0;
			try (DataInputStream data = new DataInputStream(
				new SizeLimitedInputStream(new ChecksumInputStream(crc, in),
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
						// There may be multiple consecutive IDAT chunks which
						// just continue where the previous one left off, so
						// just smash them together
						if (imagechunk != null)
						{
							// Read chunk data, decompress the data
							// additionally so that the decoder does not need
							// to worry about the data being compressed at
							// all...
							byte[] xtrachunk = PNGReader.__chunkLater(data);
							
							// Setup new array which contains the original
							// data but has more space
							int gn = imagechunk.length,
								xn = xtrachunk.length,
								nl = gn + xn;
							imagechunk = Arrays.copyOf(imagechunk, nl);
							
							// Write in all the data
							for (int i = 0, o = gn; i < xn; i++, o++)
								imagechunk[o] = xtrachunk[i];
						}
						
						// The first chunk
						else
							imagechunk = PNGReader.__chunkLater(data);
						break;
						
						// Transparency information
					case 0x74524E53:
						this.__parseAlpha(data, len);
						break;
					
						// Unknown, ignore
					default:
						break;
				}
			}
			
			// {@squirreljme.error EB0u CRC mismatch in PNG data chunk.
			// (Desired CRC; Actual CRC; Last chunk type read)}
			int want = in.readInt(),
				real = crc.checksum();
			if (want != real)
				throw new IOException(String.format("EB0u %08x %08x %08x",
					want, real, lasttype));
		}
		
		// {@squirreljme.error EB0v No image data has been loaded.}
		int[] argb = this._argb;
		if (argb == null)
			throw new IOException("EB0v");
		
		// Is an alpha channel being used?
		if (!this._hasalpha)
		{
			// Force all pixels to opaque
			for (int i = 0, n = argb.length; i < n; i++)
				argb[i] = 0xFF_000000;
			
			// Make all pixels opaque in the palette
			int[] palette = this._palette;
			if (palette != null)
				for (int i = 0, n = palette.length; i < n; i++)
					palette[i] |= 0xFF_000000;
		}
		
		// {@squirreljme.error EB32 Unsupported bit-depth. (The bitdepth)}
		int bitdepth = this._bitdepth;
		if (Integer.bitCount(bitdepth) != 1 || bitdepth > 8)
			throw new IOException("EB32 " + bitdepth);
		
		// {@squirreljme.error EB33 Adam7 interlacing not supported.}
		if (this._adamseven)
			throw new IOException("EB33");
		
		// {@squirreljme.error EB13 Paletted PNG image has no palette.}
		if (this._colortype == 3 && this._palette == null)
			throw new IOException("EB13");
		
		// Process the image chunk now that the other information was read
		// Note that the chunk needs to be unfiltered first
		try (InputStream data = new ByteArrayInputStream(this.__unfilter(
			new ZLibDecompressor(new ByteArrayInputStream(imagechunk)))))
		{
			int colortype = this._colortype;
			
			// Grayscale or Indexed
			if (colortype == 0 || colortype == 3)
				this.__pixelIndexed(data, (colortype == 3));
			
			// RGB(A)
			else if (colortype == 2 || colortype == 6)
				this.__pixelsRGB(data, (colortype == 6));
			
			// YA (Grayscale + Alpha)
			else
				this.__pixelsYA(data);
		}
		
		// Create image
		return Image.createRGBImage(argb, this._width, this._height,
			this._hasalpha);
	}
	
	/**
	 * Parses the alpha transparency data.
	 *
	 * @param __in The stream to read data from.
	 * @param __dlen The data length/
	 * @throws IOException On parse errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/14
	 */
	private void __parseAlpha(DataInputStream __in, int __dlen)
		throws IOException, NullPointerException
	{
		// Check
		if (__in == null)
			throw new NullPointerException("NARG");
		
		int[] palette = this._palette;
		int colortype = this._colortype,
			numpals = (palette != null ? palette.length : 0),
			numcolors = this._numcolors;
		
		// Force alpha channel to be set
		this._hasalpha = true;
		
		// Alpha values for grayscale or true-color
		if (colortype == 0)
		{
			// Read double-byte values
			for (int at = 0;;)
			{
				// Read in color
				int col = __in.read(),
					ign = __in.read();
				
				// EOF?
				if ((col | ign) < 0)
					break;
				
				// Find color to remove the alpha channel from the palette
				for (int p = 0; p < numpals; p++)
					if (palette[p] == col)
						palette[p] &= 0xFFFFFF;
			}
		}
		
		// Alpha values for indexed values
		else if (colortype == 3)
		{
			// Read as many entries as possible
			int i = 0;
			for (; i < numcolors; i++)
			{
				int val = __in.read();
				
				// Reached end of data, the rest are implied opaque
				if (val < 0)
					break;
				
				// Fill in color
				palette[i] |= ((val & 0xFF) << 24);
			}
			
			// The alpha data can be short, which means that all of
			// the following colors are fully opaque
			for (; i < numcolors; i++)
				palette[i] |= 0xFF_000000;
		}
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
		
		// {@squirreljme.error EB0w Image has zero or negative width.
		// (The width)}
		int width = __in.readInt();
		if (width <= 0)
			throw new IOException(String.format("EB0w %d", width));
		this._width = width;
		
		// {@squirreljme.error EB0x Image has zero or negative height. (The
		// height)}
		int height = __in.readInt();
		if (height <= 0)
			throw new IOException(String.format("EB0x %d", height));
		this._height = height;
		
		// Debug
		todo.DEBUG.note("Size: %dx%d%n", width, height);
		
		// Read the bit depth and the color type
		int bitdepth = __in.readUnsignedByte(),
			colortype = __in.readUnsignedByte();
		
		// {@squirreljme.error EB0y Invalid PNG bit depth.
		// (The bit depth)}
		if (Integer.bitCount(bitdepth) != 1 || bitdepth < 0 || bitdepth > 16)
			throw new IOException(String.format("EB0y %d", bitdepth));
		
		// {@squirreljme.error EB0z Invalid PNG bit depth and color type
		// combination. (The color type; The bit depth)}
		if ((bitdepth < 8 && (colortype != 0 && colortype != 3)) ||
			(bitdepth > 8 && colortype != 3))
			throw new IOException(String.format("EB0z %d %d", colortype,
				bitdepth));
			
		// Set
		this._bitdepth = bitdepth;
		this._colortype = colortype;
		
		// These two color types have alpha, this field may be set later on
		// if a transparency chunk was found
		boolean hasalpha;
		this._hasalpha = (hasalpha = (colortype == 4 || colortype == 6));
		
		// Determine number of channels
		int channels = (colortype == 0 || colortype == 3 ? 1 :
			(colortype == 2 ? 3 :
			(colortype == 4 ? 2 :
			(colortype == 6 ? 4 : 1))));
		
		// Scan length
		this._scanlen = width * ((channels * bitdepth) / 8);
		
		// {@squirreljme.error EB10 Only deflate compressed PNG images are
		// supported. (The compression method)}
		int compressionmethod = __in.readUnsignedByte();
		if (compressionmethod != 0)
			throw new IOException(String.format("EB10 %d", compressionmethod));
		
		// {@squirreljme.error EB11 Only adapative filtered PNGs are supported.
		// (The filter type)}
		int filter = __in.readUnsignedByte();
		if (filter != 0)
			throw new IOException(String.format("EB11 %d", filter));
		
		// {@squirreljme.error EB12 Unsupported PNG interlace method. (The
		// interlace type)}
		int interlace = __in.readUnsignedByte();
		if (interlace != 0 && interlace != 1)
			throw new IOException(String.format("EB12 %d", interlace));
		this._adamseven = (interlace == 1);
		
		// Allocate image buffer
		this._argb = new int[width * height];
		
		// If this is grayscale, then force a palette to be initialized so the
		// colors are more easily read without needing to process them further
		// So all values are treated as indexed
		if (colortype == 0)
		{
			// 2^d colors available
			int numcolors = (1 << bitdepth);
			
			// Build palette, force everything to opaque, it will be cleared
			// later
			int[] palette = new int[numcolors];
			for (int i = 0; i < numcolors; i++)
				palette[i] = ((int)(((double)i / (double)numcolors) * 255.0)) |
					0xFF_000000;
			
			// Set
			this._palette = palette;
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
		
		// Set
		this._numcolors = numcolors;
		this._maxcolors = maxcolors;
		
		// Load palette data, any remaining colors are left uninitialized and
		// are fully transparent or just black
		int[] palette = new int[maxcolors];
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
	
	/**
	 * Decodes grayscale/indexed image data.
	 *
	 * @param __dis Input Stream.
	 * @param __idx Indexed colors instead of just grayscale?
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/15
	 */
	private final void __pixelIndexed(InputStream __dis, boolean __idx)
		throws IOException, NullPointerException
	{
		if (__dis == null)
			throw new NullPointerException("NARG");
			
		int[] argb = this._argb;
		int[] palette = this._palette;
		int width = this._width,
			height = this._height,
			limit = width * height,
			bitdepth = this._bitdepth,
			bitmask = (1 << bitdepth) - 1,
			numpals = (palette != null ? palette.length : 0);
		
		// Read of multiple bits
		for (int o = 0;;)
		{
			// Read and check EOF
			int v = __dis.read();
			if (v < 0)
				break;
			
			// Handle each bit
			for (int b = 0; b < 8 && o < limit; b += bitdepth, v >>>= bitdepth)
				argb[o++] = palette[(v & bitmask) % numpals];
		}
	}
	
	/**
	 * Decodes RGB or RGBA image data.
	 *
	 * @param __dis Input Stream.
	 * @param __alpha RGBA is used?
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/15
	 */
	private final void __pixelsRGB(InputStream __dis, boolean __alpha)
		throws IOException, NullPointerException
	{
		if (__dis == null)
			throw new NullPointerException("NARG");
		
		// Get output
		int[] argb = this._argb;
		int width = this._width,
			height = this._height,
			limit = width * height;
		
		// Keep reading in data
		for (int o = 0; o < limit; o++)
		{
			// Read in all values, the mask is used to keep the sign bit in
			// place but also cap the value to 255!
			int r = __dis.read() & 0x800000FF,
				g = __dis.read() & 0x800000FF,
				b = __dis.read() & 0x800000FF;
			int a = (__alpha ? (__dis.read() & 0x800000FF) : 0xFF);
			
			// Have any hit EOF? Just need to OR all the bits
			if ((r | g | b | a) < 0)
				break;
			
			// Write pixel
			argb[o] = (a << 24) | (r << 16) | (g << 8) | b;
		}
	}
	
	/**
	 * Decodes image data.
	 *
	 * @param __dis Input Stream.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/15
	 */
	private final void __pixelsYA(InputStream __dis)
		throws IOException, NullPointerException
	{
		if (__dis == null)
			throw new NullPointerException("NARG");
		
		// Get output
		int[] argb = this._argb;
		int width = this._width,
			height = this._height,
			limit = width * height;
		
		// Keep reading in data
		for (int o = 0; o < limit;)
		{
			// Read in all values, the mask is used to keep the sign bit in
			// place but also cap the value to 255!
			int a = __dis.read() & 0x800000FF,
				y = __dis.read() & 0x800000FF;
			
			// Have any hit EOF? Just need to OR all the bits
			if ((a | y) < 0)
				break;
			
			// Write pixel
			argb[o++] = (a << 24) | (y << 16) | (y << 8) | y;
		}
	}
	
	/**
	 * Unfilters the PNG data.
	 *
	 * @param __in The stream to read from.
	 * @return The unfiltered data.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/15
	 */
	private final byte[] __unfilter(InputStream __in)
		throws IOException, NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// Need these
		int scanlen = this._scanlen,
			height = this._height;
		
		// Allocate buffer that will be returned, containing the unfiltered
		// data
		byte[] rv = new byte[scanlen * height];
		
		// Read the image scanline by scanline and process it
		for (int dy = 0; dy < height; dy++)
		{
			// Base output for this scanline
			int ibase = scanlen * dy;
			
			// At the start of every scanline is the filter type, which
			// describes how the data should be treated
			// {@squirreljme.error EB34 Unknown filter type. (The type)}
			int type = __in.read();
			if (type < 0 || type > 4)
				throw new IOException("EB34 " + type);
			
			todo.DEBUG.note("Line %d: %d", dy, type);
			
			// Go through each byte in the scanline
			for (int dx = 0; dx < scanlen; dx++)
			{
				// The current position in the buffer
				int di = ibase + dx;
				
				// The filter algorithm is a bit confusing and it uses the
				// prior and old pixel information, so according to the PNG
				// spec just to be easier to use the variables will be named
				// the same. Anywhere that bleeds off the image will always be
				// treated as zero.
				
				// The current byte being filtered
				int x = __in.read() & 0xFF,
				
				// The byte to the left of (x, y) [-1, 0]
					a = (dx <= 0 ? 0 : rv[di - 1]) & 0xFF,
				
				// The byte to the top of (x, y) [0, -1]
					b = (dy <= 0 ? 0 : rv[di - scanlen]) & 0xFF,
				
				// The byte to the top and left of (x, y) [-1, -1]
					c = ((dx <= 0 || dy <= 0) ? 0 :
						rv[(di - scanlen) - 1]) & 0xFF;
				
				// Depends on the decoding algorithm
				int res = 0;
				switch (type)
				{
						// None
					case 0:
						res = x;
						break;
						
						// Sub
					case 1:
						res = x + a;
						break;
						
						// Up
					case 2:
						res = x + b;
						break;
						
						// Average
					case 3:
						res = x + ((a + b) >>> 2);
						break;
						
						// Paeth
					case 4:
						{
							// Calculate these
							int p = a + b - c,
								pa = p - a,
								pb = p - b,
								pc = p - c;
							
							// Absolute values
							pa = (pa < 0 ? -pa : pa);
							pb = (pb < 0 ? -pb : pb);
							pc = (pc < 0 ? -pc : pc);
							
							// Perform some checks
							if (pa <= pb && pa <= pc)
								res = x + a;
							else if (pb <= pc)
								res = x + b;
							else
								res = x + c;
						}
						break;
				}
				
				// Set result
				rv[di] = (byte)res;
			}
		}
		
		return rv;
	}
	
	/**
	 * Reads all the input data and returns a byte array for the data, so it
	 * may be processed later.
	 *
	 * @param __dis The stream to read from.
	 * @return The read data.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/14
	 */
	private static final byte[] __chunkLater(InputStream __dis)
		throws IOException, NullPointerException
	{
		if (__dis == null)
			throw new NullPointerException("NARG");
		
		// Read into this byte array
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream(512))
		{
			// Read loop
			byte[] buf = new byte[512];
			for (;;)
			{
				int rc = __dis.read(buf);
				
				if (rc < 0)
					break;
				
				baos.write(buf, 0, rc);
			}
			
			// Return the data
			return baos.toByteArray();
		}
	}
}

