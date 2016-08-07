// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.chv.generic.elf;

import net.multiphasicapps.squirreljme.emulator.VonNeumannAddressing;
import net.multiphasicapps.squirreljme.jit.base.JITCPUEndian;

/**
 * This is used to parse the ELF binary and load it into memory.
 *
 * @since 2016/08/06
 */
public class ELFLoader
{
	/** The ELF binary to load. */
	protected final byte[] elf;
	
	/** The addressing of the CPU. */
	protected final VonNeumannAddressing addressing;
	
	/** The number of bits that the ELF is. */
	protected final int bits;
	
	/** The ELF endianess. */
	protected final JITCPUEndian endianess;
	
	/** The ELF version. */
	protected final int version;
	
	/** The ELF ABI. */
	protected final int abi;
	
	/** The entry point of the ELF. */
	private volatile long _entrypoint;
	
	/** The instruction set used. */
	private volatile int _archid;
	
	/**
	 * Initializes the ELF loader.
	 *
	 * @param __vna The addressing system.
	 * @param __b The binary to be loaded.
	 * @param __o The starting offset.
	 * @param __l The number of bytes to load.
	 * @throws IllegalStateException If the ELF is not correctly formed.
	 * @throws IndexOutOfBoundsException If the offset and/or length are
	 * negative or exceed the array size.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/06
	 */
	public ELFLoader(VonNeumannAddressing __vna, byte[] __b, int __o, int __l)
		throws IllegalStateException, IndexOutOfBoundsException,
			NullPointerException
	{
		// Check
		if (__b == null || __vna == null)
			throw new NullPointerException("NARG");
		int n = __b.length;
		if (__o < 0 || __l < 0 || (__o + __l) > n)
			throw new IndexOutOfBoundsException("BAOB");
		
		// Set
		this.addressing = __vna;
		
		// Defensive copy the binary
		byte[] elf = new byte[__l];
		System.arraycopy(__b, __o, elf, 0, __l);
		this.elf = elf;
		
		// {@squirreljme.error AD01 The ELF file is too short.}
		if (__l < 16)
			throw new IllegalStateException("AD01");
		
		// {@squirreljme.error AD02 The ELF contains an illegal magic number.}
		if (elf[0] != 0x7F || elf[1] != 'E' || elf[2] != 'L' || elf[3] != 'F')
			throw new IllegalStateException("AD02");
		
		// Determine bits
		switch (elf[4])
		{
			case 1: this.bits = 32; break;
			case 2: this.bits = 64; break;
			
				// {@squirreljme.error AD03 The ELF has an unknown number of
				// bits specified.}
			default:
				throw new IllegalStateException("AD03");
		}
		
		// Determine endianess
		switch (elf[5])
		{
			case 1: this.endianess = JITCPUEndian.LITTLE; break;
			case 2: this.endianess = JITCPUEndian.BIG; break;
			
				// {@squirreljme.error AD04 The ELF has an unknown endianess
				// specified.}
			default:
				throw new IllegalStateException("AD04");
		}
		
		// {@squirreljme.error AD05 The ELF version number is not supported.
		// (The version number)}
		int version = elf[6] & 0xFF;
		if (version != 1)
			throw new IllegalStateException(String.format("AD05 %d", version));
		this.version = version;
		
		// Read ABI
		this.abi = elf[7] & 0xFF;
	}
	
	/**
	 * This performs the actual loading of the binary.
	 *
	 * @since 2016/08/06
	 */
	public void load()
	{
		switch (this.bits)
		{
				// 32-bit ELF
			case 32:
				__load32();
				break;
			
				// 64-bit ELF
			case 64:
				__load64();
				break;
			
				// Should not occur
			default:
				throw new RuntimeException("OOPS");
		}
	}
	
	/**
	 * Reads a 32-bit value.
	 *
	 * @param __p The position to read from.
	 * @return The read value.
	 * @since 2016/08/07
	 */
	private int __readInt(int __p)
	{
		// Read two bytes
		byte[] elf = this.elf;
		int a = elf[__p] & 0xFF,
			b = elf[__p + 1] & 0xFF,
			c = elf[__p + 2] & 0xFF,
			d = elf[__p + 3] & 0xFF;
		
		// Depends on the endianess
		switch (this.endianess)
		{
			case BIG: return (a << 24) | (b << 16) | (c << 8) | d;
			case LITTLE: return (d << 24) | (c << 16) | (b << 8) | a;
			
				// Unknown
			default:
				throw new RuntimeException("OOPS");
		}
	}
	
	/**
	 * Reads a 64-bit value.
	 *
	 * @param __p The position to read from.
	 * @return The read value.
	 * @since 2016/08/07
	 */
	private long __readLong(int __p)
	{
		// Read two bytes
		byte[] elf = this.elf;
		int a = elf[__p] & 0xFF,
			b = elf[__p + 1] & 0xFF,
			c = elf[__p + 2] & 0xFF,
			d = elf[__p + 3] & 0xFF,
			e = elf[__p + 4] & 0xFF,
			f = elf[__p + 5] & 0xFF,
			g = elf[__p + 6] & 0xFF,
			h = elf[__p + 7] & 0xFF;
		
		// Depends on the endianess
		int x, y;
		switch (this.endianess)
		{
			case BIG:
				x = (a << 24) | (b << 16) | (c << 8) | d;
				y = (e << 24) | (f << 16) | (g << 8) | h;
				break;
			
			case LITTLE:
				x = (h << 24) | (g << 16) | (f << 8) | e;
				y = (d << 24) | (c << 16) | (b << 8) | a;
				break;
			
				// Unknown
			default:
				throw new RuntimeException("OOPS");
		}
		
		// Merge Values
		return (((long)x) << 32L) | (y & 0xFFFF_FFFFL);
	}
	
	/**
	 * Reads a 16-bit value.
	 *
	 * @param __p The position to read from.
	 * @return The read value.
	 * @since 2016/08/07
	 */
	private int __readShort(int __p)
	{
		// Read two bytes
		byte[] elf = this.elf;
		int a = elf[__p] & 0xFF,
			b = elf[__p + 1] & 0xFF;
		
		// Depends on the endianess
		switch (this.endianess)
		{
			case BIG: return (a << 8) | b;
			case LITTLE: return (b << 8) | a;
			
				// Unknown
			default:
				throw new RuntimeException("OOPS");
		}
	}
	
	/**
	 * Loads a 32-bit ELF file.
	 *
	 * @since 2016/08/07
	 */
	private void __load32()
	{
		// Read the entry point
		this._entrypoint = __readInt(24) & 0xFFFF_FFFFL;
		
		// The HV might care about the architecture
		this._archid = __readShort(18);
		
		// Really the only thing that is required is finding the program
		// headers and loading it in
		int phpos = __readInt(28);		// Position
		int phesz = __readShort(42);	// Entry size
		int phnum = __readShort(44);	// Entry count
		
		// Load all program headers
		for (int i = 0; i < phnum; i++)
		{
			// Calculate base position
			int base = phpos + (i * phesz);
			
			// Get the segment type
			int ptype = __readInt(base);
			
			// {@squirreljme.error AD06 Dynamic sections are not supported.}
			if (ptype == 2)
				throw new IllegalStateException("AD06");
			
			// Ignore anything that is not LOAD
			if (ptype != 1)
				continue;
			
			// Read header details
			int psoff = __readInt(base + 4);
			int psvad = __readInt(base + 8);
			int pspad = __readInt(base + 12);
			int psfsz = __readInt(base + 16);
			int psmsz = __readInt(base + 20);
			int psflg = __readInt(base + 24);
			int psalg = __readInt(base + 28);
			
			System.err.printf("DEBUG -- %08x %08x %08x %d %d %x %d%n",
				psoff, psvad, pspad, psfsz, psmsz, psflg, psalg);
			
			throw new Error("TODO");
		}
	}
	
	/**
	 * Loads a 64-bit ELF file.
	 *
	 * @since 2016/08/06
	 */
	private void __load64()
	{
		throw new Error("TODO");
	}
}

