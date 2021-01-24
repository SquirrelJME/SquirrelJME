// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.summercoat;

import cc.squirreljme.jvm.SystemCallError;
import cc.squirreljme.jvm.SystemCallIndex;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.jvm.summercoat.constants.MemHandleKind;
import cc.squirreljme.jvm.summercoat.struct.StaticVmAttributesStruct;

/**
 * This is a helper wrapper around system calls.
 *
 * @since 2020/11/29
 */
public final class SystemCall
{
	/**
	 * Not used.
	 * 
	 * @since 2020/11/29
	 */
	private SystemCall()
	{
	}
	
	/**
	 * {@link SystemCallIndex#ARRAY_ALLOCATION_BASE}: Returns the allocation
	 * base of arrays.
	 * 
	 * @return The allocation base for arrays.
	 * @since 2021/01/24
	 */
	public static native int arrayAllocationBase();
	
	/**
	 * {@link SystemCallIndex#ERROR_GET}: Gets the error code of the given
	 * system call.
	 * 
	 * @param __dx The {@link SystemCallIndex}.
	 * @return A {@link SystemCallError}.
	 * @since 2020/11/29
	 */
	public static native int errorGet(int __dx);
	
	/**
	 * {@link SystemCallIndex#ERROR_SET}: Sets the error code of the given
	 * system call.
	 * 
	 * @param __dx The {@link SystemCallIndex}.
	 * @param __err A {@link SystemCallError} to set.
	 * @return The old error value that was set.
	 * @since 2020/11/29
	 */
	public static native int errorSet(int __dx, int __err);
	
	/**
	 * {@link SystemCallIndex#MEM_HANDLE_NEW}: Attempts to allocate a new
	 * memory handle of the given kind.
	 * 
	 * @param __memHandleKind The {@link MemHandleKind}.
	 * @param __allocSize The allocation size.
	 * @return The allocated value or {@code 0} if no memory remains.
	 * @throws MLECallError If the input arguments are not valid.
	 * @since 2021/01/23
	 */
	public static native int memHandleNew(int __memHandleKind, int __allocSize)
		throws MLECallError;
	
	/**
	 * Returns the pipe descriptor of standard error.
	 * 
	 * @return The pipe Id.
	 * @throws MLECallError On errors.
	 * @since 2020/11/29
	 */
	public static native int pdOfStdErr()
		throws MLECallError;
	
	/**
	 * Returns the pipe descriptor of standard input.
	 * 
	 * @return The pipe Id.
	 * @throws MLECallError On errors.
	 * @since 2020/11/29
	 */
	public static native int pdOfStdIn()
		throws MLECallError;
	
	/**
	 * Returns the pipe descriptor of standard output.
	 * 
	 * @return The pipe Id.
	 * @throws MLECallError On errors.
	 * @since 2020/11/29
	 */
	public static native int pdOfStdOut()
		throws MLECallError;
	
	/**
	 * Writes a single byte to the output.
	 * 
	 * @param __pipe The pipe to write to. 
	 * @param __c The character to write.
	 * @return The bytes read or a negative value on errors.
	 * @throws MLECallError On errors.
	 * @since 2020/11/29
	 */
	public static native int pdWriteByte(int __pipe, int __c)
		throws MLECallError;
	
	/**
	 * Returns the static virtual machine attributes structure.
	 * 
	 * @return The static virtual machine attributes.
	 * @since 2021/01/24
	 */
	public static native StaticVmAttributesStruct staticVmAttributes();
}
