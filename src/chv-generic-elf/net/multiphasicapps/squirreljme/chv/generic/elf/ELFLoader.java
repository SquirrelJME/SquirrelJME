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
		throw new Error("TODO");
	}
}

