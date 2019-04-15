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
	
	/** Full transparency color map. */
	private int[] _transmap;
	
	/** Was an alpha channel used? */
	private boolean _hasalpha;
	
	/** If this has been set, then do not force color sets. */
	private boolean _blippedalpha;
	
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
							byte[] xtrachunk = PNGReader.__chunkLater(
								new ZLibDecompressor(data));
							
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
							imagechunk = PNGReader.__chunkLater(
								new ZLibDecompressor(data));
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
		
		// If no alpha channel is set or there is no transparency info then
		// make all pixels opaque
		if (!this._hasalpha && !this._blippedalpha)
			for (int i = 0, n = argb.length; i < n; i++)
				argb[i] = 0xFF000000;
		
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
		if (imagechunk != null)
			try (InputStream data = new ByteArrayInputStream(imagechunk))
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
				
				// Old image parse
				/*this.__parseImage(data);*/
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
			
		// Get output
		int[] argb = this._argb;
		int[] palette = this._palette;
		int width = this._width,
			height = this._height,
			colortype = this._colortype,
			bitdepth = this._bitdepth,
			bitmask = (1 << bitdepth) - 1,
			numpals = (palette != null ? palette.length : 0),
			numcolors = this._numcolors,
			maxcolors = this._maxcolors;
		boolean adamseven = this._adamseven;
		
		// For these color types, only a specified color in a list
		if (colortype == 0 || colortype == 2)
		{
			// Fill all colors
			for (int i = 0, n = argb.length; i < n; i++)
				argb[i] |= 0xFF000000;
				
			// Do not touch later
			this._blippedalpha = true;
		}
		
		// Full transparency map
		int[] transmap = null;
		
		// Depends on the color type
		switch (colortype)
		{
				// Grayscale, this specified which color indexes are to be
				// fully transparent
			case 0:
				{
					// Allocate
					transmap = new int[__dlen / 2];
					
					// Read double-byte values
					for (int at = 0;;)
					{
						// Read in color
						int col = __in.read();
						__in.read();
						
						// EOF?
						if (col < 0)
							break;
						
						// Add color
						col &= 0xFF;
						transmap[at++] = (col << 16) | (col << 8) | col;
					}
				}
				break;
			
				// True-color
			case 2:
				{
					// Allocate
					transmap = new int[__dlen / 6];
					
					// Read double-byte values
					for (int at = 0;;)
					{
						// Read in color
						int r = __in.read();
						__in.read();
						int g = __in.read();
						__in.read();
						int b = __in.read();
						__in.read();
						
						// EOF?
						if (r < 0 || g < 0 || b < 0)
							break;
						
						// Add color
						transmap[at++] = ((r & 0xFF) << 16) |
							((g & 0xFF) << 8) | (g & 0xFF);
					}
				}
				break;
				
				// Indexed color;
			case 3:
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
					
					// The palette data can be short, which means that all of
					// the following colors are fully opaque
					for (; i < numcolors; i++)
						palette[i] |= 0xFF000000;
				}
				break;
			
				// Just ignore the chunk if the color type is not valid
			default:
				return;
		}
		
		// Sort the transparency map so the colors can be found easier
		if (transmap != null)
		{
			Arrays.sort(transmap);
			this._transmap = transmap;
		}
		
		// Alpha channel was read!
		this._hasalpha = true;
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
		this._hasalpha = (colortype == 4 || colortype == 6);
		
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
	}
	
	/**
	 * Parses the PNG image data.
	 *
	 * @param __in The stream to read data from.
	 * @throws IOException On parse errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/28
	 */
	@Deprecated
	private void __parseImage(InputStream __in)
		throws IOException, NullPointerException
	{
		// Check
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// Get output
		int[] argb = this._argb;
		int[] palette = this._palette;
		int[] transmap = this._transmap;
		int width = this._width,
			height = this._height,
			colortype = this._colortype,
			bitdepth = this._bitdepth,
			bitmask = (1 << bitdepth) - 1,
			numpals = (palette != null ? palette.length : 0);
		boolean adamseven = this._adamseven;
		boolean hastransmap = (transmap != null);
		
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
		try (DataInputStream data = new DataInputStream(__in))
		{
			// Window used to pixel data, along with the remaining bits
			long window = 0;
			int valshift = 0;
			
			// Get initializing positions and clear, because multiple IDAT
			// chunks are treated somewhat as a single data stream
			int initvy = this._initvy,
				initvx = this._initvx;
			this._initvy = 0;
			this._initvx = 0;
			
			// All pixels are stored on scanlines
			int[] channels = new int[4];
			for (int vy = initvy; vy < height; vy++, initvx = 0)
			{
				// Clear the window, since pixels are reset on scanlines
				window = 0;
				valshift = -1;
				
				// Where data goes on the output ARGB array, linearly speaking
				int out = (vy * width);
				
				// Read in single line
				for (int vx = initvx; vx < width; vx++)
				{
					// If the window is empty then read everything in
					if (valshift < 0)
					{
						// Read pixel
						for (int i = 0, sh = 0; i < readcount; i++)
						{
							// If the EOF is unexpected it is possible that
							// the remaining image data is in another chunk
							int rv = data.read();
							if (rv < 0)
							{
								// Store the read parameters
								this._initvx = vx;
								this._initvy = vy;
								
								return;
							}
							
							// Shift in
							window |= ((long)rv) << sh;
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
						color = palette[channels[0]];
					
					// Triplet
					else
						color = (channels[0] << 16) | (channels[1] << 8) |
							channels[2];
					
					// Alpha channel defined in the image data, if it is
					// defined in another chunk then the ARGB will be set
					// already
					if (colortype == 4 || colortype == 6)
						color |= (channels[numchannels - 1] << 24);
					
					// If there is a full transparency map then any colors
					// which are not in the map will be opaque.
					if (hastransmap)
						if (Arrays.binarySearch(transmap, color) < 0)
							color |= 0xFF000000;
					
					// Place the pixel on the output buffer
					// Interlaced
					if (adamseven)
						throw new todo.TODO();
					
					// Linear
					else
						argb[out++] |= color;
				}
			}
			
			// Waste all remaining bytes until EOF so that the checksum is
			// read in the source stream
			int rc;
			while ((rc = data.read()) >= 0)
				;
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
		int[] transmap = this._transmap;
		int width = this._width,
			height = this._height,
			limit = width * height,
			bitdepth = this._bitdepth,
			bitmask = (1 << bitdepth) - 1,
			numpals = (palette != null ? palette.length : 0);
		boolean hastransmap = (transmap != null);
		
		throw new todo.TODO();
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
			int a = __dis.read() & 0x800000FF,
				r = __dis.read() & 0x800000FF,
				g = __dis.read() & 0x800000FF,
				b = (__alpha ? (__dis.read() & 0x800000FF) : 0xFF);
			
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
		
		throw new todo.TODO();
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

