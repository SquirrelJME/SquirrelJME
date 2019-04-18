// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.summercoat;

/**
 * This contains the memory space that is used for a task, it contains the
 * instance space and the static field space.
 *
 * @since 2019/04/17
 */
public final class MemorySpace
{
	/** Default size of the static field area. */
	public static final int DEFAULT_STATIC_SIZE =
		65536;
	
	/** Maximum limit of how many objects can be allocated at once. */
	public static final int DEFAULT_OBJECT_SIZE =
		65536;
	
	/** The static field size. */
	public final int staticfieldsize;
	
	/** The object size. */
	public final int objectsize;
	
	/** Current size of static field space. */
	private volatile int _cursfsize;
	
	/**
	 * Intializes the memory space with default settings.
	 *
	 * @since 2019/04/17
	 */
	public MemorySpace()
	{
		this(DEFAULT_STATIC_SIZE, DEFAULT_OBJECT_SIZE);
	}
	
	/**
	 * Initializes the memory space with the given settings.
	 *
	 * @param __sfs The space to allocate for static fields.
	 * @param __ojs The number of objects which can be allocated at once.
	 * @since 2019/04/17
	 */
	public MemorySpace(int __sfs, int __ojs)
	{
		// Set sizes
		this.staticfieldsize = (__sfs <= 0 ? DEFAULT_STATIC_SIZE : __sfs);
		this.objectsize = (__ojs <= 0 ? DEFAULT_OBJECT_SIZE : __ojs);
	}
	
	/**
	 * Allocates permanent static field space.
	 *
	 * @param __v The volume of space to allocate.
	 * @return The address of the allocation.
	 * @throws IllegalArgumentException On null arguments.
	 * @since 2019/04/18
	 */
	public final int allocateStaticSpace(int __v)
		throws IllegalArgumentException
	{
		// {@squirreljme.error AE0b Cannot claim negative amount of static
		// field space.}
		if (__v <= 0)
			throw new IllegalArgumentException("AE0b");
		
		// Lock on self since this is an important operation
		synchronized (this)
		{
			// {@squirreljme.error AE0c Not enough static field memory space.
			// (The amount requested; The would be size of the space; The
			// size limit of the space)}
			int cursfsize = this._cursfsize,
				nextsize = cursfsize + __v;
			if (nextsize > this.staticfieldsize)
				throw new VMOutOfMemoryException(String.format("AE0c %d %d %d",
					__v, nextsize, this.staticfieldsize));
			
			// Set new size
			this._cursfsize = nextsize;
			
			// Use the old current size
			return cursfsize;
		}
	}
}

