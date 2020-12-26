// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.jarfile;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This is used to build the initialization sequence accordingly. It is used
 * determine the initial amount of memory needed along with all the various
 * actions which need to be performed at this point.
 *
 * The initializer starts with a memory sequence chunk which could later be
 * freed when it is no longer needed potentially.
 *
 * @since 2019/04/30
 */
public final class Initializer
{
	/** Memory chunk size offset. */
	public static final int CHUNK_SIZE_OFFSET =
		0;
	
	/** Next chunk address. */
	public static final int CHUNK_NEXT_OFFSET =
		4;
	
	/** The length of chunks. */
	public static final int CHUNK_LENGTH =
		8;
	
	/** Operations. */
	private final List<Operation> _ops =
		new ArrayList<>();
	
	/** Current allocated temporary space. */
	private byte[] _bytes = new byte[65536];
	
	/** Current size of the initializer. */
	private int _size =
		0;
	
	/**
	 * Allocates memory in the initialization sequence.
	 *
	 * @param __sz The number of bytes to allocate.
	 * @return The pointer address of the allocation.
	 * @since 2019/04/30
	 */
	public final int allocate(int __sz)
	{
		// Force minimum size, otherwise things will get very messed up
		if (__sz < 1)
			__sz = 1;
		
		// Round allocation to 4-bytes
		__sz = (__sz + 3) & (~3);
		
		// Calculate the next size of the boot area
		int nowat = this._size,
			chunksize = __sz + Initializer.CHUNK_LENGTH,
			nextat = nowat + chunksize;
		
		// If the memory space is too small, grow it
		byte[] bytes = this._bytes;
		if (nextat > bytes.length)
			this._bytes = (bytes = Arrays.copyOf(bytes, nextat + 2048));
		
		// The return address is after the chunk length
		int rv = nowat + Initializer.CHUNK_LENGTH;
		
		// Record size of chunk and the next chunk position in RAM
		this.memWriteInt(null,
			nowat + Initializer.CHUNK_SIZE_OFFSET, chunksize);
		this.memWriteInt(Modifier.RAM_OFFSET,
			nowat + Initializer.CHUNK_NEXT_OFFSET, nextat);
		
		// Continue at the end
		this._size = nextat;
		return rv;
	}
	
	/**
	 * Writes a value to the given address.
	 *
	 * @param __addr The address to write to.
	 * @param __v The value to write.
	 * @since 2019/04/30
	 */
	public final void memWriteByte(int __addr, int __v)
	{
		this.memWriteByte(null, __addr, __v);
	}
	
	/**
	 * Writes a value to the given address.
	 *
	 * @param __m The modifier to use when writing.
	 * @param __addr The address to write to.
	 * @param __v The value to write.
	 * @since 2019/04/30
	 */
	public final void memWriteByte(Modifier __m, int __addr, int __v)
	{
		// Record action?
		if (__m != null && __m != Modifier.NONE)
			this._ops.add(new Operation(__m, (byte)1, __addr, __v));
		
		// Write data
		byte[] bytes = this._bytes;
		bytes[__addr] = (byte)__v;
	}
	
	/**
	 * Writes a value to the given address.
	 *
	 * @param __addr The address to write to.
	 * @param __v The value to write.
	 * @since 2019/04/30
	 */
	public final void memWriteInt(int __addr, int __v)
	{
		this.memWriteInt(null, __addr, __v);
	}
	
	/**
	 * Writes a value to the given address.
	 *
	 * @param __m The modifier to use when writing.
	 * @param __addr The address to write to.
	 * @param __v The value to write.
	 * @since 2019/04/30
	 */
	public final void memWriteInt(Modifier __m, int __addr, int __v)
	{
		// Record action?
		if (__m == null)
			__m = Modifier.NONE;
		this._ops.add(new Operation(__m, (byte)4, __addr, __v));
		
		// Write data
		byte[] bytes = this._bytes;
		bytes[__addr++] = (byte)(__v >>> 24);
		bytes[__addr++] = (byte)(__v >>> 16);
		bytes[__addr++] = (byte)(__v >>> 8);
		bytes[__addr++] = (byte)(__v);
	}
	
	/**
	 * Writes a value to the given address.
	 *
	 * @param __addr The address to write to.
	 * @param __v The value to write.
	 * @since 2019/05/25
	 */
	public final void memWriteLong(int __addr, long __v)
	{
		this.memWriteLong(null, __addr, __v);
	}
	
	/**
	 * Writes a value to the given address.
	 *
	 * @param __m The modifier to use when writing.
	 * @param __addr The address to write to.
	 * @param __v The value to write.
	 * @since 2019/05/25
	 */
	public final void memWriteLong(Modifier __m, int __addr, long __v)
	{
		// Record action?
		if (__m == null)
			__m = Modifier.NONE;
		this._ops.add(new Operation(__m, (byte)8, __addr, __v));
		
		// Write data
		byte[] bytes = this._bytes;
		bytes[__addr++] = (byte)(__v >>> 56L);
		bytes[__addr++] = (byte)(__v >>> 48L);
		bytes[__addr++] = (byte)(__v >>> 40L);
		bytes[__addr++] = (byte)(__v >>> 32L);
		bytes[__addr++] = (byte)(__v >>> 24L);
		bytes[__addr++] = (byte)(__v >>> 16L);
		bytes[__addr++] = (byte)(__v >>> 8L);
		bytes[__addr++] = (byte)(__v);
	}
	
	/**
	 * Writes a value to the given address.
	 *
	 * @param __addr The address to write to.
	 * @param __v The value to write.
	 * @since 2019/04/30
	 */
	public final void memWriteShort(int __addr, int __v)
	{
		this.memWriteShort(null, __addr, __v);
	}
	
	/**
	 * Writes a value to the given address.
	 *
	 * @param __m The modifier to use when writing.
	 * @param __addr The address to write to.
	 * @param __v The value to write.
	 * @since 2019/04/30
	 */
	public final void memWriteShort(Modifier __m, int __addr, int __v)
	{
		// Record action?
		if (__m == null)
			__m = Modifier.NONE;
		this._ops.add(new Operation(__m, (byte)2, __addr, __v));
		
		// Write data
		byte[] bytes = this._bytes;
		bytes[__addr++] = (byte)(__v >>> 8);
		bytes[__addr++] = (byte)(__v);
	}
	
	/**
	 * Converts and builds the initializer sequence.
	 *
	 * @return The byte array representing the sequence.
	 * @since 2019/04/30
	 */
	public final byte[] toByteArray()
	{
		List<Operation> ops = this._ops;
		byte[] bytes = this._bytes;
		int size = this._size;
		
		// Round up to prevent uneven sizes
		size = (size + 3) & (~3);
		
		// Give extra bytes for the terminator chunk
		size += Initializer.CHUNK_LENGTH;
		
		// Write initializer RAM
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream(4096);
			DataOutputStream dos = new DataOutputStream(baos))
		{
			// Write initializer memory chunk
			dos.writeInt(size);
			dos.write(bytes, 0, size);
			
			// Write out operations
			int n = ops.size();
			dos.writeInt(n);
			for (int i = 0; i < n; i++)
			{
				Operation op = ops.get(i);
				
				// Write operation tag and address offset
				dos.writeByte((op.size << 4) | (op.mod.ordinal()));
				dos.writeInt(op.addr);
				
				// Write the value
				Number v = op.value;
				switch (op.size)
				{
					case 1:
						dos.writeByte(v.byteValue());
						break;
						
					case 2:
						dos.writeShort(v.shortValue());
						break;
						
					case 4:
						dos.writeInt(v.intValue());
						break;
						
					case 8:
						dos.writeLong(v.longValue());
						break;
					
					default:
						throw Debugging.oops();
				}
				
				// Debug
				if (JarMinimizer._ENABLE_DEBUG)
					Debugging.debugNote("Op %-5d: %d@0x%08x = %d (%s)",
						i, op.size, op.addr, op.value, op.mod);
			}
			
			// End mark
			dos.writeInt(-1);
			
			// Debug
			if (JarMinimizer._ENABLE_DEBUG)
				Debugging.debugNote("Wrote %d bytes, %d ops", dos.size(), n);
			
			// Done!
			return baos.toByteArray();
		}
		
		// {@squirreljme.error BC01 Could not export boot area.}
		catch (IOException e)
		{
			throw new RuntimeException("BC01", e);
		}
	}
}

