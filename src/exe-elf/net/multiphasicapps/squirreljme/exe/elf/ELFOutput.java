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
				break;
				
				// 64-bit
			case 64:
				this.headersize = 64;
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
		
		// It is not possible to write the program header with a fixed size
		// since it would either waste space or be too short. As such, load
		// all blob data into arrays;
		int n = __names.length;
		byte[][] preblobs = new byte[n][];
		byte[] buf = new byte[512];
		int headersize = this.headersize;
		for (int i = 0; i < n; i++)
			try (ByteArrayOutputStream baos = new ByteArrayOutputStream())
			{
				// Copy all the data
				for (InputStream is = __blobs[i];;)
				{
					int rc = is.read(buf);
					
					// EOF?
					if (rc < 0)
						break;
					
					// Copy
					baos.write(buf, 0, rc);
				}
				
				// Store
				preblobs[i] = baos.toByteArray();
				
				// Determine if this contains the entry point
				System.err.println("TODO -- Determine blob entry point.");
			}
		
		// Write the output binary
		try (ExtendedDataOutputStream dos = new ExtendedDataOutputStream(__os))
		{
			// Correct endian
			JITCPUEndian endianess = this.endianess;
			int endid;
			switch (endianess)
			{
					// Big endian
				case BIG:
					dos.setEndianess(DataEndianess.BIG);
					endid = 2;
					break;
					
					// Little endian
				case LITTLE:
					dos.setEndianess(DataEndianess.LITTLE);
					endid = 1;
					break;
				
					// Unknown
				default:
					throw new RuntimeException("OOPS");
			}
			
			// Write header
			dos.writeByte(0x7F);
			dos.writeByte('E');
			dos.writeByte('L');
			dos.writeByte('F');
			
			// Which class?
			int bits = this.bits;
			switch (bits)
			{
					// 32-bit
				case 32:
					dos.writeByte(1);
					headersize = 52;
					break;
				
					// 64-bit:
				case 64:
					dos.writeByte(2);
					break;
				
					// Unknown
				default:
					throw new RuntimeException("OOPS");
			}
			
			// Which endianess
			dos.writeByte(endid);
			
			// Always version 1
			dos.writeByte(1);
			
			// The OS ABI
			dos.writeByte(this.osabi);
			
			// Ignore padding
			dos.writeInt(0);
			dos.writeInt(0);
			
			// ELFs are always static executable
			dos.writeShort(2);
			
			// The instruction set
			dos.writeShort(this.cputype);
			
			// Always version  1
			dos.writeInt(1);
			
			// Entry point (always zero);
			// ph offset (after this header);
			// sh offset (not used, no sections are used)
			switch (bits)
			{
					// 32-bit
				case 32:
					dos.writeInt(0);
					dos.writeInt(headersize);
					dos.writeInt(0);
					break;
				
					// 64-bit
				case 64:
					dos.writeLong(0);
					dos.writeLong(headersize);
					dos.writeLong(0);
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
			System.err.println("TODO -- Write program header size+count.");
			dos.writeShort(0);	// size
			dos.writeShort(0);	// count
			
			// There are no sections
			dos.writeShort(0);
			dos.writeShort(0);
			
			// There is no string index
			dos.writeShort(0);
			
			// Write program header
			System.err.println("TODO -- Write program header table");
			
			// Write blob information
			System.err.println("TODO -- Write blob data");
		}
	}
}

