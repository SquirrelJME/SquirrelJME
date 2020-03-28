// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.jvm.Assembly;
import cc.squirreljme.jvm.memory.MemoryAccessException;
import cc.squirreljme.jvm.memory.ReadableBasicMemory;
import cc.squirreljme.jvm.memory.ReadableByteMemory;
import cc.squirreljme.jvm.memory.WritableBasicMemory;
import cc.squirreljme.jvm.memory.WritableByteMemory;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.vm.springcoat.exceptions.SpringVirtualMachineException;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * This class manages the allocation of memory within SpringCoat, it is used to
 * allocate and access raw blocks as required.
 *
 * @since 2020/03/03
 */
public final class MemoryManager
	implements ReadableBasicMemory, WritableBasicMemory
{
	/** RAM storage starting area. */
	public static final long RAM_START_ADDRESS =
		0x1000_0000;
	
	/** ROM storage starting area. */
	private static final long _ROM_START_POINTER =
		0x7000_0000;
	
	/** The pool of UTF constant strings which are somewhere in memory. */
	private final Map<String, SpringPointer> _utfPool =
		new TreeMap<>();
	
	/** Readable memory. */
	private final ConcurrentNavigableMap<Integer, ReadableByteMemory> _read =
		new ConcurrentSkipListMap<>();
	
	/** Writable memory. */
	private final ConcurrentNavigableMap<Integer, WritableByteMemory> _write =
		new ConcurrentSkipListMap<>();
	
	/** Chunks of memory that make up the RAM. */
	private final List<WritableByteMemory> _ramChunks =
		new LinkedList<>();
	
	/** Chunks of memory that make up the ROM. */
	private final List<WritableByteMemory> _romChunks =
		new LinkedList<>();
	
	/** The currently bounds methods. */
	private final ConcurrentMap<SpringPointer, SpringMethod> _boundMethods =
		new ConcurrentHashMap<>();
	
	/** The next location for RAM chunks. */
	private long _ramNext =
		MemoryManager.RAM_START_ADDRESS;
	
	/** The next location for ROM chunks. */
	private long _romNext =
		MemoryManager._ROM_START_POINTER;
	
	/** The current lock. */
	private int _lock;
	
	/**
	 * Appends the given bytes to the ROM region of memory to persist at
	 * run-time.
	 *
	 * @param __data The chunk of bytes to be added.
	 * @return The resultant pointer to the chunk.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/03/14
	 */
	public final SpringPointer appendRom(byte[] __data)
		throws NullPointerException
	{
		if (__data == null)
			throw new NullPointerException("NARG");
		
		// Setup memory chunk and store here
		MemoryChunk chunk = new MemoryChunk((__data.length + 3) & (~3));
		chunk.write(0, __data, 0, __data.length);
		
		// Protect ourself because we will be adjusting the chain links
		synchronized (this)
		{
			// Store chunks into ROM region
			List<WritableByteMemory> romChunks = this._romChunks;
			romChunks.add(chunk);
			
			// Map chunk into memory as read-only
			long romNext = this._romNext;
			this.map(romNext, chunk, false);
			
			// Prepare the next placement region for more ROM data
			this._romNext = romNext + ((chunk.size() + 7) & (~7));
			
			return new SpringPointer(romNext);
		}
	}
	
	/**
	 * Attaches the specified amount of RAM to the system.
	 *
	 * @param __len The amount of RAM to attach.
	 * @return The resulting memory pointer.
	 * @throws IllegalArgumentException If the length is zero or negative.
	 * @since 2020/03/26
	 */
	public final SpringPointer attachRam(int __len)
		throws IllegalArgumentException
	{
		if (__len <= 0)
			throw new IllegalArgumentException("Cannot attach negative RAM.");
		
		// Allocate chunk
		MemoryChunk chunk = new MemoryChunk(__len);
		
		// Protect ourself because we will be adjusting the chain links
		synchronized (this)
		{
			// Store chunks into ROM region
			List<WritableByteMemory> ramChunks = this._ramChunks;
			ramChunks.add(chunk);
			
			// Map chunk into memory as read-only
			long ramNext = this._ramNext;
			this.map(ramNext, chunk, true);
			
			// Prepare the next placement region for more ROM data
			this._ramNext = ramNext + ((chunk.size() + 7) & (~7));
			
			return new SpringPointer(ramNext);
		}
	}
	
	/**
	 * Binds a method to the given memory address which is used to dynamically
	 * refer to it.
	 *
	 * @param __method The method to bind.
	 * @return The pointer of the bound method.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/03/21
	 */
	public SpringPointer bindMethod(SpringMethod __method)
		throws NullPointerException
	{
		if (__method == null)
			throw new NullPointerException("NARG");
		
		// Setup ROM space for this method
		byte[] methodRom = new byte[]{-1, -1, -1, -1, -1, -1, -1, -1};
		SpringPointer rv = this.appendRom(methodRom);
		
		// Store the binding of this method
		synchronized (this)
		{
			this._boundMethods.put(rv, __method);
		}
		
		return rv;
	}
	
	/**
	 * Maps this given memory into the global memory.
	 *
	 * @param __addr The address to bind to.
	 * @param __mem The memory to map in.
	 * @param __write Should this memory be writable?
	 * @throws IllegalArgumentException If the address is not valid or if
	 * non-writable memory is attempted to be mapped as writable.
	 * @throws NullPointerException On null arguments.
	 * @throws SpringVirtualMachineException If the memory overlaps.
	 * @since 2020/03/14
	 */
	public void map(long __addr, ReadableByteMemory __mem, boolean __write)
		throws IllegalArgumentException, NullPointerException,
			SpringVirtualMachineException
	{
		if (__mem == null)
			throw new NullPointerException("NARG");
		if (__addr < 0 || __addr > Integer.MAX_VALUE ||
			__addr + __mem.size() > Integer.MAX_VALUE)
			throw new IllegalArgumentException(String.format(
				"Out of bounds map at %08x-%08x.", __addr,
				__addr + __mem.size()));
		if (__write && !(__mem instanceof WritableByteMemory))
			throw new IllegalArgumentException(
				"Cannot add non-writable memory as writable.");
		
		NavigableMap<Integer, ReadableByteMemory> memRead = this._read;
		NavigableMap<Integer, WritableByteMemory> memWrite = this._write;
		
		// Region of this memory area
		int baseAddr = (int)__addr;
		int endAddr = baseAddr + __mem.size();
		
		// Prevent the allocator from allocating memory since we are going to
		// change the memory structures now
		int code = AtomicTicker.next();
		try
		{
			// Lock
			this.waitLock(code);
			
			// Memory layout structures will be modified
			synchronized (this)
			{
				// Get possible memory that could overlap in this area
				Map.Entry<Integer, ReadableByteMemory> floor =
					memRead.floorEntry(baseAddr);
				Map.Entry<Integer, ReadableByteMemory> ceil =
					memRead.ceilingEntry(baseAddr + 1);
				
				// Check collision with the floor
				if (floor != null)
				{
					int start = floor.getKey();
					int end = start + floor.getValue().size();
					
					if ((baseAddr >= start && baseAddr < end) ||
						(endAddr > start && endAddr < end))
						throw new SpringVirtualMachineException(String.format(
							"Mapping collision: %08x-%08x ~~> %08x-%08x",
							baseAddr, endAddr, start, end));
				}
				
				// Check collision with the ceiling
				if (ceil != null)
				{
					int start = ceil.getKey();
					int end = start + ceil.getValue().size();
					
					if ((baseAddr >= start && baseAddr < end) ||
						(endAddr > start && endAddr < end))
						throw new SpringVirtualMachineException(String.format(
							"Mapping collision: %08x-%08x ~~> %08x-%08x",
							baseAddr, endAddr, start, end));
				}
				
				// Does not collide with another mapping
				memRead.put(baseAddr, __mem);
				if (__write)
					memWrite.put(baseAddr, (WritableByteMemory)__mem);
			}
		}
		finally
		{
			this.unlock(code);
		}
	}
	
	/**
	 * Loads the specified UTF string into memory.
	 *
	 * @param __string The string to load.
	 * @return The loaded string.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/03/14
	 */
	public final SpringPointer loadUtf(String __string)
		throws NullPointerException
	{
		if (__string == null)
			throw new NullPointerException("NARG");
		
		// These strings stack up into a certain region of memory
		Map<String, SpringPointer> utfPool = this._utfPool;
		synchronized (this)
		{
			// Already cached?
			SpringPointer rv = utfPool.get(__string);
			if (rv != null)
				return rv;
			
			// Load the data into a chunk
			byte[] chunk;
			try (ByteArrayOutputStream baos = new ByteArrayOutputStream(
				__string.length() << 1);
				DataOutputStream dos = new DataOutputStream(baos))
			{
				dos.writeUTF(__string);
				
				chunk = baos.toByteArray();
			}
			catch (IOException e)
			{
				throw new IllegalArgumentException(
					"Could not write string.", e);
			}
			
			// Append the UTF data into ROM region of memory
			rv = this.appendRom(chunk);
			utfPool.put(__string, rv);
			
			return rv;
		}
	}
	
	/**
	 * Locks the memory manager.
	 *
	 * @param __code The locking code.
	 * @return If this was locked.
	 * @since 2020/03/04
	 */
	public boolean lock(int __code)
	{
		synchronized (this)
		{
			// Is this unlocked?
			if (this._lock == 0)
			{
				this._lock = __code;
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/03/03
	 */
	@Override
	public byte read(long __addr)
		throws MemoryAccessException
	{
		if (__addr < 0 || __addr > Integer.MAX_VALUE)
			throw new MemoryAccessException(__addr, "Read out of bounds.");
		
		Debugging.debugNote("read: %08x%n", __addr);
		
		// Get entry where the chunk would be located
		Map.Entry<Integer, ReadableByteMemory> chunk =
			this._read.floorEntry((int)__addr);
		if (chunk == null)
			throw new MemoryAccessException(__addr,
				String.format("Unmapped read: %08x", __addr));
		
		// Check to see if this is still within bounds of that chunk
		long baseAddr = __addr - chunk.getKey();
		ReadableByteMemory mem = chunk.getValue();
		if (baseAddr >= mem.size())
			throw new MemoryAccessException(__addr, "Over-mapped read.");
		
		// Read value
		return mem.read(baseAddr);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/03/03
	 */
	@Override
	public void read(long __addr, byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, MemoryAccessException,
		NullPointerException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/03/03
	 */
	@Override
	public int readInt(long __addr)
		throws MemoryAccessException
	{
		return (((this.read(__addr) & 0xFF) << 24) |
			((this.read(__addr + 1) & 0xFF) << 16) |
			((this.read(__addr + 2) & 0xFF) << 8) |
			((this.read(__addr + 3) & 0xFF)));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/03/03
	 */
	@Override
	public long readLong(long __addr)
		throws MemoryAccessException
	{
		return Assembly.longPack(this.readInt(__addr),
			this.readInt(__addr + 4));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/03/03
	 */
	@Override
	public short readShort(long __addr)
		throws MemoryAccessException
	{
		return (short)(((this.read(__addr) & 0xFF) << 8) |
			((this.read(__addr + 1) & 0xFF)));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/03/14
	 */
	@Override
	public int size()
	{
		// This is a virtual region of memory starting from NULL, so the size
		// is rather large
		return Integer.MAX_VALUE;
	}
	
	/**
	 * Unlocks the allocator.
	 *
	 * @param __code The locking code.
	 * @since 2020/03/14
	 */
	public void unlock(int __code)
	{
		synchronized (this)
		{
			// Unlocking only happens if we matched the right code
			if (this._lock == __code)
			{
				this._lock = 0;
				
				// Signal any threads which are lock-polling this lock
				this.notify();
			}
		}
	}
	
	/**
	 * Waits for the lock to be cleared before continuing.
	 *
	 * @param __code The locking code we are using.
	 * @since 2020/03/14
	 */
	public void waitLock(int __code)
	{
		synchronized (this)
		{
			for (;;)
			{
				// Did we claim this lock?
				if (this.lock(__code))
					return;
				
				// Wait for a signal or to try this lock again
				try
				{
					this.wait(1000);
				}
				catch (InterruptedException e)
				{
					// Ignore
				}
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/03/03
	 */
	@Override
	public void write(long __addr, byte __b)
		throws MemoryAccessException
	{
		if (__addr < 0 || __addr > Integer.MAX_VALUE)
			throw new MemoryAccessException(__addr, "Write out of bounds.");
		
		Debugging.debugNote("write: %08x <- %d%n", __addr, __b);
		
		// Get entry where the chunk would be located
		Map.Entry<Integer, WritableByteMemory> chunk =
			this._write.floorEntry((int)__addr);
		if (chunk == null)
			throw new MemoryAccessException(__addr,
				String.format("Unmapped write: %08x", __addr));
		
		// Check to see if this is still within bounds of that chunk
		long baseAddr = __addr - chunk.getKey();
		WritableByteMemory mem = chunk.getValue();
		if (baseAddr >= mem.size())
			throw new MemoryAccessException(__addr, "Over-mapped write.");
		
		// Write value
		mem.write(baseAddr, __b);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/03/03
	 */
	@Override
	public void write(long __addr, byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, MemoryAccessException,
		NullPointerException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/03/03
	 */
	@Override
	public void writeInt(long __addr, int __v)
		throws MemoryAccessException
	{
		this.write(__addr, (byte)(__v >>> 24));
		this.write(__addr + 1, (byte)(__v >>> 16));
		this.write(__addr + 2, (byte)(__v >>> 8));
		this.write(__addr + 3, (byte)(__v));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/03/03
	 */
	@Override
	public void writeLong(long __addr, long __v)
		throws MemoryAccessException
	{
		this.write(__addr, (byte)(__v >>> 56));
		this.write(__addr + 1, (byte)(__v >>> 48));
		this.write(__addr + 2, (byte)(__v >>> 40));
		this.write(__addr + 3, (byte)(__v >>> 32));
		this.write(__addr + 4, (byte)(__v >>> 24));
		this.write(__addr + 5, (byte)(__v >>> 16));
		this.write(__addr + 6, (byte)(__v >>> 8));
		this.write(__addr + 7, (byte)(__v));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/03/03
	 */
	@Override
	public void writeShort(long __addr, long __v)
		throws MemoryAccessException
	{
		this.write(__addr, (byte)(__v >>> 8));
		this.write(__addr + 1, (byte)(__v));
	}
}
