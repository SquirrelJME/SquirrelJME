// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.boot.lib;

import cc.squirreljme.jvm.boot.io.BinaryBlob;

/**
 * This class represents a class library which makes classes available for
 * usage and such. It allows one to obtain class library information and
 * pointers.
 *
 * @since 2019/09/22
 */
public abstract class ClassLibrary
{
	/**
	 * Locates the given resource.
	 *
	 * @param __name The name of the resource to get.
	 * @return The index of the resource or {@code -1} if it was not found.
	 * @since 2019/09/22
	 */
	public abstract int indexOf(String __name)
		throws NullPointerException;
	
	/**
	 * Returns the name of the library.
	 *
	 * @return The library name.
	 * @since 2019/09/22
	 */
	public abstract String libraryName();
	
	/**
	 * Returns the data pointer of the given index.
	 *
	 * @param __dx The index to get the data for.
	 * @return The binary blob data.
	 * @throws IndexOutOfBoundsException If the index is not within bounds.
	 * @since 2019/07/11
	 */
	public abstract BinaryBlob resourceData(int __dx)
		throws IndexOutOfBoundsException;
	
	/**
	 * Returns the split pool for this library, if one is available.
	 *
	 * @param __rt Access the run-time pool?
	 * @return The split pool or {@code null} if there is none.
	 * @since 2019/11/17
	 */
	public AbstractPoolParser splitPool(boolean __rt)
	{
		return null;
	}
}

