// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.exe.elf;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import net.multiphasicapps.io.data.DataEndianess;
import net.multiphasicapps.io.data.ExtendedDataOutputStream;
import net.multiphasicapps.squirreljme.jit.base.JITCPUEndian;
import net.multiphasicapps.squirreljme.exe.ExecutableOutput;

/**
 * This is used to output ELF binaries.
 *
 * @since 2016/08/07
 */
public class ELFOutput
	implements ExecutableOutput
{
	/** The size of the entry table. */
	private static final int _ENTRY_SIZE =
		12;
	
	/** The name of the single section, the .text section. */
	private static final byte[] _TEXT_NAME =
		new byte[]{0, '.', 't', 'e', 'x', 't', 0};
	
	/** System properties which are available. */
	protected final Map<String, String> properties =
		new HashMap<>();
	
	/** The target bits. */
	protected final int bits;
	
	/** The target endianess. */
	protected final JITCPUEndian endianess;
	
	/** The operating system ABI. */
	protected final int osabi;
	
	/** The CPU type. */
	protected final int cputype;
	
	/** The size of the ELF header. */
	protected final int headersize;
	
	/** The program header size. */
	protected final int pheadersize;
	
	/** Section header size. */
	protected final int sheadersize;
	
	/** The start of the section header. */
	protected final int sheaderstart;
	
	/** Start of the blob data. */
	protected final int blobstartpos;
	
	/** The start of the string table. */
	protected final int stringtablestart;
	
	/**
	 * Initializes the ELF output with the specified parameters.
	 *
	 * @param __bits The number of bits the CPU uses.
	 * @param __end The endianess of the CPU.
	 * @param __osabi The operating system ABI.
	 * @param __cputype The CPU type.
	 * @throws IllegalArgumentException If the CPU bit level is not 32 or 64;
	 * or the endianess is not known.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/08
	 */
	public ELFOutput(int __bits, JITCPUEndian __end, int __osabi,
		int __cputype)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__end == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AX01 Unsupported output CPU bits. (The bit
		// size of the CPU)}
		switch (__bits)
		{
				// 32-bit
			case 32:
				this.headersize = 52;
				this.pheadersize = 32;
				this.sheadersize = 40;
				break;
				
				// 64-bit
			case 64:
				this.headersize = 64;
				this.pheadersize = 56;
				this.sheadersize = 64;
				break;
				
				// Unknown
			default:
				throw new IllegalArgumentException(String.format("AX01 %d",
					__bits));
		}
		
		// {@squirreljme.error AX02 Unsupported endianess. (The endianess)}
		if (__end != JITCPUEndian.BIG && __end != JITCPUEndian.LITTLE)
			throw new IllegalArgumentException(String.format("AX02 %s",
				__end));
		
		// Set
		this.bits = __bits;
		this.endianess = __end;
		this.osabi = __osabi;
		this.cputype = __cputype;
		
		// Section header follows the program heder
		this.sheaderstart = this.headersize + this.pheadersize;
		
		// The start of the string table
		this.stringtablestart = this.sheaderstart + (this.sheadersize * 3);
		
		// The blob starts at an aligned address following the program header
		// and normal header
		this.blobstartpos = (this.stringtablestart + _TEXT_NAME.length + 7) &
			(~7);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/07
	 */
	@Override
	public void addSystemProperty(String __k, String __v)
		throws NullPointerException
	{
		// Check
		if (__k == null || __v == null)
			throw new NullPointerException("NARG");
		
		// Lock
		Map<String, String> properties = this.properties;
		synchronized (properties)
		{
			properties.put(__k, __v);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/07
	 */
	@Override
	public void linkBinary(OutputStream __os, String[] __names,
		InputStream[] __blobs)
		throws IOException, NullPointerException
	{
		// Check
		if (__os == null || __names == null || __blobs == null)
			throw new NullPointerException("NARG");
		
		// Load blobs into byte arrays
		int n = __names.length;
		byte[][] preblobs = __preloadBlobs(__blobs);
		
		// Determine positions where the blobs exist
		int[] blobpos = __blobPositions(preblobs);
		int tablestart = blobpos[n];
		
		// Setup the table and determine the table size
		__BlobEntry__[] entries = __setupEntries(__names, preblobs, blobpos,
			tablestart);
		int tablesize = entries.length * _ENTRY_SIZE;
		
		// Calculate the end of the blob and table data
		int blobstartpos = this.blobstartpos;
		int payloadsize = (tablesize + tablestart);
		int endinfile = payloadsize + blobstartpos;
		
		// Start writing ELF details
		try (ExtendedDataOutputStream dos = new ExtendedDataOutputStream(__os);
			ByteArrayOutputStream rawpayload = new ByteArrayOutputStream();
			ExtendedDataOutputStream rpdos =
				new ExtendedDataOutputStream(rawpayload))
		{
			// Write the ELF identification
			__writeIdentification(dos);
			
			// Write a small bootstrap
			System.err.println("TODO -- Generate bootstrap.");
			
			// Make sure the bootstrap size is at least 4
			if (rawpayload.size() < 4)
				rpdos.writeInt(0);
			byte[] bootstrap = rawpayload.toByteArray();
			
			// Calculate the full payload size
			int fullpayloadsize = payloadsize + bootstrap.length;
			
			// ELFs are always static executable
			dos.writeShort(2);
			
			// The instruction set
			dos.writeShort(this.cputype);
			
			// Always version  1
			dos.writeInt(1);
			
			// Entry point (starts at the table end);
			// ph offset (after this header);
			// sh offset (not used, no sections are used)
			int sheaderstart = this.sheaderstart;
			switch (bits)
			{
					// 32-bit
				case 32:
					dos.writeInt(payloadsize);
					dos.writeInt(headersize);
					dos.writeInt(sheaderstart);
					break;
				
					// 64-bit
				case 64:
					dos.writeLong(payloadsize);
					dos.writeLong(headersize);
					dos.writeLong(sheaderstart);
					break;
				
					// Unknown
				default:
					throw new RuntimeException("OOPS");
			}
			
			// Ignore flags
			dos.writeInt(0);
			
			// Header is the standard size
			dos.writeShort(headersize);
			
			// Program header details
			dos.writeShort(pheadersize);	// size
			dos.writeShort(1);	// count
			
			// 3 Sections: NULL, .text, strings
			int sheadersize = this.sheadersize;
			dos.writeShort(sheadersize);
			dos.writeShort(3);
			
			// String index is the last section
			dos.writeShort(2);
			
			// Write program header (only one) and the two sections
			dos.writeInt(1);	// Load this section
			byte[] textname = _TEXT_NAME;
			int stringtablestart = this.stringtablestart;
			switch (bits)
			{
					// 32-bit
				case 32:
					// Program
					dos.writeInt(blobstartpos);	// Offset in file
					dos.writeInt(0);	// Virtual load address
					dos.writeInt(0);	// Physical address?
					dos.writeInt(fullpayloadsize);	// Program size (in file)
					dos.writeInt(fullpayloadsize);	// Program size (in memory)
					dos.writeInt(5);	// RX
					
					// Padding
					dos.writeInt(0);
					
					// Null section
					for (int p = 0; p < sheadersize; p++)
						dos.writeByte(0);
					
					// Text Section
					dos.writeInt(1);						// name
					dos.writeInt(1);						// type
					dos.writeInt(0x6);						// Alloc+Exec
					dos.writeInt(0);						// Address
					dos.writeInt(blobstartpos);				// File Start
					dos.writeInt(fullpayloadsize);			// Size
					dos.writeInt(0);						// Ignore link
					dos.writeInt(0);						// Ignore info
					dos.writeInt(4);						// Address align
					dos.writeInt(0);						// Not used
					
					// String section
					dos.writeInt(textname.length - 1);		// name
					dos.writeInt(3);						// type
					dos.writeInt(0x20);						// Strings
					dos.writeInt(0);						// Address
					dos.writeInt(stringtablestart);			// File Start
					dos.writeInt(textname.length);			// Size
					dos.writeInt(0);						// Ignore link
					dos.writeInt(0);						// Ignore info
					dos.writeInt(4);						// Address align
					dos.writeInt(1);						// char 1 byte
					break;
				
					// 64-bit
				case 64:
					// Program
					dos.writeInt(5);	// RX
					dos.writeLong(blobstartpos);	// Offset in file
					dos.writeLong(0);	// Virtual load address
					dos.writeLong(0);	// Physical address?
					dos.writeLong(fullpayloadsize);	// Program size in file
					dos.writeLong(fullpayloadsize);	// Program size in memory
					
					// Padding
					dos.writeLong(0);
					
					// Null section
					for (int p = 0; p < sheadersize; p++)
						dos.writeByte(0);
					
					// Text Section
					dos.writeInt(1);						// name
					dos.writeInt(1);						// type
					dos.writeLong(0x6);						// Alloc+Exec
					dos.writeLong(0);						// Address
					dos.writeLong(blobstartpos);			// File Start
					dos.writeLong(fullpayloadsize);			// Size
					dos.writeInt(0);						// Ignore link
					dos.writeInt(0);						// Ignore info
					dos.writeLong(8);						// Address align
					dos.writeLong(0);						// Not used
					
					// String section
					dos.writeInt(textname.length - 1);		// name
					dos.writeInt(3);						// type
					dos.writeLong(0x20);					// Strings
					dos.writeLong(0);						// Address
					dos.writeLong(stringtablestart);		// File Start
					dos.writeLong(textname.length);			// Size
					dos.writeInt(0);						// Ignore link
					dos.writeInt(0);						// Ignore info
					dos.writeLong(8);						// Address align
					dos.writeLong(1);						// char 1 byte
					break;
				
					// Unknown
				default:
					throw new RuntimeException("OOPS");
			}
			
			// Write the string table data
			dos.write(_TEXT_NAME);
			
			// Write the blob data
			for (int i = 0; i < n; i++)
			{
				// Align to blob start
				int blobstart = blobpos[i];
				while ((dos.size() - blobstartpos) < blobstart)
					dos.writeByte(0);
				
				// Write it out
				dos.write(preblobs[i]);
			}
			
			// Write the entry table
			for (int i = 0; i < n; i++)
			{
				// Get entry
				__BlobEntry__ e = entries[i];
				
				// Write details
				dos.writeInt(e._reloff);
				dos.writeInt(e._size);
				
				// Write flags
				System.err.println("TODO -- Write entry point flag.");
				dos.writeInt(0);
			}
			
			// Write the bootstrap
			dos.write(bootstrap);
		}
	}
	
	/**
	 * Determines the position where all blobs start.
	 *
	 * @param __preblobs The positions where each blob starts.
	 * @return The positions of all blobs.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/09
	 */
	private int[] __blobPositions(byte[][] __preblobs)
		throws NullPointerException
	{
		// Check
		if (__preblobs == null)
			throw new NullPointerException("NARG");
		
		// Setup
		int n = __preblobs.length;
		int[] rv = new int[n + 1];
		
		// Determine all blob positions
		int at = 0;
		for (int i = 0; i < n; i++)
		{
			// Align
			at = (at + 7) & (~7);
			rv[i] = at;
			
			// Add size of the blob
			at += __preblobs[i].length;
		}
		
		// The last address is the final position (where the table is written)
		rv[n] = (at + 7) & (~7);
		
		// Return
		return rv;
	}
	
	/**
	 * Preloads blobs into byte arrays since there are unknowns when it comes
	 * to size.
	 *
	 * @param __blobs The blobs to preload.
	 * @return The preloaded blobs.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/09
	 */
	private byte[][] __preloadBlobs(InputStream[] __blobs)
		throws IOException, NullPointerException
	{
		// Check
		if (__blobs == null)
			throw new NullPointerException("NARG");
		
		// Preload all blobs
		int n = __blobs.length;
		byte[][] rv = new byte[n][];
		byte[] buf = new byte[512];
		for (int i = 0; i < n; i++)
			try (InputStream is = __blobs[i];
				ByteArrayOutputStream os = new ByteArrayOutputStream())
			{
				// Copy data
				for (;;)
				{
					int rc = is.read(buf);
					
					// EOF?
					if (rc < 0)
						break;
					
					// Write
					os.write(buf, 0, rc);
				}
				
				// Extract the byte array
				rv[i] = os.toByteArray();
			}
		
		// Return it
		return rv;
	}
	
	/**
	 * Sets the endianess of the output stream.
	 *
	 * @param __dos The output stream.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/09
	 */
	private void __setEndianess(ExtendedDataOutputStream __dos)
		throws NullPointerException
	{
		// Check
		if (__dos == null)
			throw new NullPointerException("NARG");
		
		// Correct endian
		JITCPUEndian endianess = this.endianess;
		switch (endianess)
		{
				// Big endian
			case BIG:
				__dos.setEndianess(DataEndianess.BIG);
				break;
				
				// Little endian
			case LITTLE:
				__dos.setEndianess(DataEndianess.LITTLE);
				break;
			
				// Unknown
			default:
				throw new RuntimeException("OOPS");
		}
	}
	
	/**
	 * Setups entries for writing to the table data.
	 *
	 * @param __name Blob names.
	 * @param __blob Blob preloaded buffers.
	 * @param __blobpos The start of all the blobs.
	 * @param __tablestart Where the table starts.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/09
	 */
	private __BlobEntry__[] __setupEntries(String[] __names,
		byte[][] __preblobs, int[] __blobpos, int __tablestart)
		throws NullPointerException
	{
		// Check
		if (__names == null || __preblobs == null || __blobpos == null)
			throw new NullPointerException("NARG");
		
		// Setup
		int n = __names.length;
		__BlobEntry__[] rv = new __BlobEntry__[n];
		
		// Build table
		for (int i = 0; i < n; i++)
		{
			// Create entry
			__BlobEntry__ e = new __BlobEntry__();
			rv[i] = e;
			
			// Relative offset from the start of the table data
			e._reloff = __blobpos[i] - __tablestart;
			e._size = __preblobs[i].length;
		}
		
		// Return
		return rv;
	}
	
	/**
	 * Writes the ELF identification.
	 *
	 * @param __dos The output stream to use.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/09
	 */
	private void __writeIdentification(ExtendedDataOutputStream __dos)
		throws IOException, NullPointerException
	{
		// Check
		if (__dos == null)
			throw new NullPointerException("NARG");
	
		// Correct endian
		__setEndianess(__dos);
		JITCPUEndian endianess = this.endianess;
		int endid;
		switch (endianess)
		{
				// Big endian
			case BIG:
				endid = 2;
				break;
				
				// Little endian
			case LITTLE:
				endid = 1;
				break;
			
				// Unknown
			default:
				throw new RuntimeException("OOPS");
		}
		
		// Write header
		__dos.writeByte(0x7F);
		__dos.writeByte('E');
		__dos.writeByte('L');
		__dos.writeByte('F');
		
		// Which class?
		int bits = this.bits;
		switch (bits)
		{
				// 32-bit
			case 32:
				__dos.writeByte(1);
				break;
			
				// 64-bit:
			case 64:
				__dos.writeByte(2);
				break;
			
				// Unknown
			default:
				throw new RuntimeException("OOPS");
		}
		
		// Which endianess
		__dos.writeByte(endid);
		
		// Always version 1
		__dos.writeByte(1);
		
		// The OS ABI
		__dos.writeByte(this.osabi);
		
		// Ignore padding
		__dos.writeInt(0);
		__dos.writeInt(0);
	}
}

