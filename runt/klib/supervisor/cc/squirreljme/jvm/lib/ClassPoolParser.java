// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.lib;

import cc.squirreljme.jvm.io.BinaryBlob;

/**
 * This class is used to parse individual pool treads.
 *
 * @see ClassDualPoolParser
 * @since 2019/10/12
 */
public final class ClassPoolParser
	extends AbstractPoolParser
{
	/** The blob for this pool. */
	protected final BinaryBlob blob;
	
	/** The size of this pool. */
	private int _size =
		-1;
	
	/**
	 * Initializes the constant pool parser.
	 *
	 * @param __b The blob data for the pool.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/10/12
	 */
	public ClassPoolParser(BinaryBlob __blob)
		throws NullPointerException
	{
		if (__blob == null)
			throw new NullPointerException("NARG");
		
		this.blob = __blob;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/11/24
	 */
	@Override
	public final BinaryBlob entryData(int __dx)
		throws IndexOutOfBoundsException, InvalidClassFormatException
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the size of the constant pool.
	 *
	 * @return The pool size.
	 * @since 2019/10/13
	 */
	public final int size()
	{
		// If the size is negative, it has never been read before
		int rv = this._size;
		if (rv < 0)
			this._size = (rv = this.blob.readJavaInt(
				ClassPoolConstants.OFFSET_OF_INT_ENTRY_OFFSET));
		
		return rv;
	}
}

