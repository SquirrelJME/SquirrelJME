// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import net.multiphasicapps.squirreljme.jit.base.JITException;

/**
 * This interface is used to write resources to the output namespace writer.
 *
 * @since 2016/07/22
 */
public interface JITResourceWriter
	extends AutoCloseable
{
	/**
	 * {@inheritDoc}
	 * @since 2016/07/22
	 */
	@Override
	public abstract void close()
		throws JITException;
	
	/**
	 * Writes the specified bytes to the output resource.
	 *
	 * @param __b The bytes to write.
	 * @param __o The base address in the array to copy bytes from.
	 * @param __l The number of bytes to write.
	 * @throws IndexOutOfBoundsException If the offset and/or length are
	 * negative or combined exceed the array size.
	 * @throws JITException If the bytes could not be written.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/22
	 */
	public abstract void write(byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, JITException, NullPointerException;
}

