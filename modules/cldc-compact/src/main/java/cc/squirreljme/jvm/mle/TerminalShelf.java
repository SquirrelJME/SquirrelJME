// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle;

import cc.squirreljme.jvm.mle.constants.StandardPipeType;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;

/**
 * This contains the shell for printing to the console and otherwise.
 *
 * @since 2020/06/14
 */
public final class TerminalShelf
{
	/**
	 * Not used.
	 *
	 * @since 2002/06/14
	 */
	private TerminalShelf()
	{
	}
	
	/**
	 * Closes the output of the current process.
	 * 
	 * @param __fd The {@link StandardPipeType} to close.
	 * @return A negative value on any kind of error.
	 * @throws MLECallError If {@code __fd} is not valid.
	 * @since 2020/07/02
	 */
	public static native int close(int __fd)
		throws MLECallError;
	
	/**
	 * Flushes the stream.
	 *
	 * @param __fd The {@link StandardPipeType} to flush.
	 * @return Zero on success, negative values for failure.
	 * @throws MLECallError If {@code __fd} is not valid.
	 * @since 2018/12/08
	 */
	public static native int flush(int __fd)
		throws MLECallError;
	
	/**
	 * Writes the character to the console output.
	 *
	 * @param __fd The {@link StandardPipeType} to write to.
	 * @param __c The byte to write, only the lowest 8-bits are used.
	 * @return Zero on success, negative values for failure.
	 * @throws MLECallError If {@code __fd} is not valid.
	 * @since 2018/09/21
	 */
	public static native int write(int __fd, int __c)
		throws MLECallError;
	
	/**
	 * Writes the given bytes to the console output.
	 *
	 * @param __fd The {@link StandardPipeType} to write to.
	 * @param __b The bytes to write.
	 * @param __o The offset.
	 * @param __l The length.
	 * @return Zero on success, negative values for failure.
	 * @throws MLECallError If {@code __fd} is not valid, the offset and/or
	 * length are negative or exceed the buffer size, or {@code __b} is
	 * {@code null}.
	 * @since 2018/12/05
	 */
	public static native int write(int __fd, byte[] __b, int __o, int __l)
		throws MLECallError;
}
