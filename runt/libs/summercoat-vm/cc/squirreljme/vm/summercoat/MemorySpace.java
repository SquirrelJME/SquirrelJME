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
}

