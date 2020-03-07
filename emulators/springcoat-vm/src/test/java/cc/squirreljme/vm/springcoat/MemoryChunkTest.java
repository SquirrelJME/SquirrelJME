// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.jvm.memory.MemoryAccessException;
import java.util.Random;
import org.testng.Assert;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

/**
 * Tests for the memory chunks.
 *
 * @since 2020/03/03
 */
public class MemoryChunkTest
{
	/** The size of the chunk to use. */
	private static final int _MEMORY_SIZE =
		32768;
	
	/** Byte value. */
	private static final byte _BYTE =
		123;
	
	/**
	 * Tests that values written to memory
	 *
	 * @since 2020/03/03
	 */
	@Test
	public void readWriteSingleByte()
	{
		MemoryChunk chunk = new MemoryChunk(MemoryChunkTest._MEMORY_SIZE);
		
		chunk.write(0, MemoryChunkTest._BYTE);
		Assert.assertEquals(chunk.read(0), MemoryChunkTest._BYTE);
	}
	
	/**
	 * Tests bulk read/write operations.
	 *
	 * @since 2020/03/03
	 */
	@Test
	public void readWriteMultipleBytes()
	{
		MemoryChunk chunk = new MemoryChunk(MemoryChunkTest._MEMORY_SIZE);
		
		// Write a large number of bytes
		byte[] written = new byte[MemoryChunkTest._MEMORY_SIZE];
		new Random(MemoryChunkTest._MEMORY_SIZE).nextBytes(written);
		chunk.write(0, written, 0, MemoryChunkTest._MEMORY_SIZE);
		
		// Read in large number of bytes
		byte[] read = new byte[MemoryChunkTest._MEMORY_SIZE];
		chunk.read(0, read, 0, MemoryChunkTest._MEMORY_SIZE);
		
		Assert.assertEquals(read, written);
	}
	
	/**
	 * Tests that out of bounds failure works.
	 *
	 * @since 2020/03/03
	 */
	@Test(expectedExceptions = {MemoryAccessException.class})
	public void outOfBounds()
	{
		MemoryChunk chunk = new MemoryChunk(MemoryChunkTest._MEMORY_SIZE);
		
		chunk.write(MemoryChunkTest._MEMORY_SIZE, (byte)123);
	}
}
