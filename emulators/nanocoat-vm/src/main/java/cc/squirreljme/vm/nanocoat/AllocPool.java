// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.nanocoat;

import cc.squirreljme.emulator.vm.VMException;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * Native allocation pool.
 *
 * @since 2023/12/08
 */
public final class AllocPool
	implements Pointer
{
	/** The pointer to the native pool. */
	private final long _pointer;
	
	/**
	 * Initializes the allocation pool.
	 *
	 * @param __size The size of the pool.
	 * @throws IllegalArgumentException If {@code __size} is zero or negative.
	 * @throws VMException If pool initialization failed.
	 * @since 2023/12/08
	 */
	public AllocPool(int __size)
		throws IllegalArgumentException, VMException
	{
		if (__size <= 0)
			throw new IllegalArgumentException("Invalid pool size: " + __size);
		
		// Allocate the native pool
		this._pointer = AllocPool.__poolMalloc(__size, this);
	}
	
	/**
	 * Initializes a pool using a static address.
	 *
	 * @param __address The address where the storage is.
	 * @param __size The number of bytes to allocate.
	 * @throws IllegalArgumentException If the size is not valid.
	 * @throws NullPointerException On null arguments.
	 * @throws VMException On null arguments.
	 * @since 2023/12/14
	 */
	public AllocPool(long __address, int __size)
		throws IllegalArgumentException, NullPointerException, VMException
	{
		if (__address == 0)
			throw new NullPointerException("NARG");
		
		if (__size <= 0)
			throw new IllegalArgumentException("Zero or negative size.");
		
		// Initialize a static pool
		this._pointer = AllocPool.__poolStatic(__address, __size, this);
	}
	
	/**
	 * Allocates memory.
	 *
	 * @param __size The number of bytes to allocate.
	 * @return The resultant link.
	 * @throws IllegalArgumentException If the size is zero or negative.
	 * @throws VMException If there was an error allocating the data.
	 * @since 2023/12/14
	 */
	public AllocLink alloc(int __size)
		throws IllegalArgumentException, VMException
	{
		if (__size <= 0)
			throw new IllegalArgumentException("Zero or negative size.");
		
		/** Allocate memory. */
		long block = AllocPool.__alloc(this._pointer, __size);
		return new AllocLink(block, AllocPool.__getLink(block));
	}
	
	/**
	 * Allocates the given type.
	 *
	 * @param __type The type to allocate.
	 * @return The resultant link.
	 * @throws NullPointerException On null arguments.
	 * @throws VMException If the allocation could not be performed.
	 * @since 2023/12/14
	 */
	public AllocLink alloc(AllocSizeOf __type)
		throws NullPointerException, VMException
	{
		return this.alloc(__type, 0);
	}
	
	/**
	 * Allocates the given type.
	 *
	 * @param __type The type to allocate.
	 * @param __count The number of elements to allocate.
	 * @return The resultant link.
	 * @throws NullPointerException On null arguments.
	 * @throws VMException If the allocation could not be performed.
	 * @since 2023/12/14
	 */
	public AllocLink alloc(AllocSizeOf __type, int __count)
		throws IndexOutOfBoundsException, NullPointerException, VMException
	{
		return this.alloc(__type.size(__count));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/12/08
	 */
	@Override
	public long pointerAddress()
	{
		return this._pointer;
	}
	
	/**
	 * Duplicates the given string and returns a character array backed by
	 * a link.
	 *
	 * @param __string The string to map.
	 * @return The mapped and translated string.
	 * @throws NullPointerException On null arguments.
	 * @throws VMException If it could not be initialized.
	 * @since 2023/12/16
	 */
	public LinkedCharStar strDup(String __string)
		throws NullPointerException, VMException
	{
		if (__string == null)
			throw new NullPointerException("NARG");
		
		// Map string to bytes
		byte[] buf;
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream(
			2 + (__string.length() * 2)); 
			DataOutputStream dos = new DataOutputStream(baos))
		{
			// Write string in modified UTF form
			dos.writeUTF(__string);
			dos.writeByte(0);
			
			buf = baos.toByteArray();
		}
		catch (IOException __e)
		{
			throw new VMException(__e);
		}
		
		// We ignore the starting length and add a NUL
		int len = buf.length - 2;
		
		// Allocate a link to write into
		AllocLink link = this.alloc(len);
		link.write(0, buf, 2, len);
		
		// Wrap the given link
		return new LinkedCharStar(link);
	}
	
	/**
	 * Duplicates and returns an entire array.
	 *
	 * @param __strings The strings to get the array form of.
	 * @return The resultant duplicated string array.
	 * @throws NullPointerException On null arguments.
	 * @throws VMException If it could be created.
	 * @since 2023/12/16
	 */
	public CharStarPointerArray strDupArray(String... __strings)
		throws NullPointerException, VMException
	{
		if (__strings == null)
			throw new NullPointerException("NARG");
		
		return this.strDupArray(Arrays.asList(__strings));
	}
	
	/**
	 * Duplicates and returns an entire array.
	 *
	 * @param __strings The strings to get the array form of.
	 * @return The resultant duplicated string array.
	 * @throws NullPointerException On null arguments.
	 * @throws VMException If it could be created.
	 * @since 2023/12/16
	 */
	public CharStarPointerArray strDupArray(List<String> __strings)
		throws NullPointerException, VMException
	{
		if (__strings == null)
			throw new NullPointerException("NARG");
		
		// The number of strings being written, remember their base offset
		int count = __strings.size();
		int[] baseOff = new int[count];
		
		// It is optimal to keep the allocated memory a single chunk of bytes
		// rather than having multiple allocations... so fill in everything
		// accordingly as such
		byte[] buf;
		int pointerSize = Utils.pointerSize();
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
			DataOutputStream dos = new DataOutputStream(baos))
		{
			// Reserve space for pointer storage
			for (int i = 0, n = count * pointerSize; i < n; i++)
				dos.writeByte(0);
			
			// Write each string accordingly
			for (int i = 0; i < count; i++)
			{
				String string = __strings.get(i);
				
				// Skip if null
				if (string == null)
					continue;
				
				// Store address for later
				baseOff[i] = baos.size();
				
				// Write string in modified UTF form with ending NUL
				dos.writeUTF(string);
				dos.writeByte(0);
			}
			
			// Get the final byte array
			buf = baos.toByteArray();
		}
		catch (IOException __e)
		{
			throw new VMException(__e);
		}
		
		// Allocate and write into the link directly
		int bufLen = buf.length;
		AllocLink link = this.alloc(bufLen);
		link.write(0, buf, 0, bufLen);
		
		// Go back and write in all the pointer base offsets
		long base = link.pointerAddress();
		for (int i = 0, p = 0; i < count; i++, p += pointerSize)
		{
			int off = baseOff[i];
			
			// Write NULL accordingly
			if (off == 0)
				link.writePointer(p, 0);
			else
				link.writePointer(p, base + off);
		}
		
		// Set list accordingly
		return new CharStarPointerArray(count, link);
	}
	
	/**
	 * Allocates memory within the pool.
	 *
	 * @param __poolPtr The pool to allocate in.
	 * @param __size The number of bytes to allocate.
	 * @return The resultant pool pointer.
	 * @throws VMException If the pointer could not be allocated.
	 * @since 2023/12/14
	 */
	private static native long __alloc(long __poolPtr, int __size)
		throws VMException;
	
	/**
	 * Returns the raw address link for the given block.
	 *
	 * @param __blockPtr The block pointer used.
	 * @return The address of the block.
	 * @throws VMException If the link could not be obtained.
	 * @since 2023/12/14
	 */
	private static native long __getLink(long __blockPtr)
		throws VMException;
	
	/**
	 * Allocates a pool via {@code malloc()} and returns the pointer to it.
	 *
	 * @param __size The size of the pool.
	 * @param __this The front end wrapper.
	 * @return The native pointer to the pool.
	 * @throws VMException If the pool could not be allocated or initialized.
	 * @since 2023/12/08
	 */
	private static native long __poolMalloc(
		@Range(from = 1, to = Integer.MAX_VALUE) int __size,
		@NotNull AllocPool __this)
		throws VMException;
	
	/**
	 * Initializes a static pool at the given address.
	 *
	 * @param __address The address to initialize at.
	 * @param __size The number of bytes to allocate for.
	 * @param __this The reference to this object for the front end.
	 * @return The pointer to the given pool.
	 * @throws VMException If the pool could not be initialized.
	 * @since 2023/12/14
	 */
	private static native long __poolStatic(long __address, int __size,
		AllocPool __this)
		throws VMException;
}
