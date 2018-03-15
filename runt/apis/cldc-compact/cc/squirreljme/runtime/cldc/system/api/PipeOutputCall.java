// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.system.api;

import cc.squirreljme.runtime.cldc.system.SystemFunction;

/**
 * Interface for {@link SystemFunction#PIPE_OUTPUT_ZI} and {@link
 * SystemFunction#PIPE_OUTPUT_ZABII}.
 *
 * @since 2018/03/14
 */
public interface PipeOutputCall
{
	/**
	 * Pipe a single byte to standard output or standard error.
	 *
	 * @param __err To standard error?
	 * @param __b The value to pipe.
	 * @since 2018/03/01
	 */
	public abstract void pipeOutput(boolean __err, int __b);

	/**
	 * Pipes multiple bytes to standard output or standard error.
	 *
	 * @param __err To standard error?
	 * @param __b The values to pipe.
	 * @param __o The offset.
	 * @param __l The length.
	 * @throws IndexOutOfBoundsException If the offset and/or length are
	 * negative or exceed the array bounds.
	 * @throws NullPointerException
	 * @since 2018/03/01
	 */
	public abstract void pipeOutput(boolean __err, ByteArray __b, int __o,
		int __l)
		throws IndexOutOfBoundsException, NullPointerException;
}

