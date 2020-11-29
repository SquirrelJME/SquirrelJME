// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.summercoat;

import cc.squirreljme.jvm.Assembly;
import cc.squirreljme.jvm.SystemCallError;
import cc.squirreljme.jvm.SystemCallIndex;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.jvm.summercoat.brackets.ClassInfoBracket;

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
	 * {@link SystemCallIndex#CLASS_INFO_GET_SIZE}: Returns the allocated size
	 * of the class.
	 * 
	 * @param __info The information to get.
	 * @return The size of the class information.
	 * @throws MLECallError If {@code __info} is {@code null} or is not a
	 * valid class.
	 * @since 2020/11/29
	 */
	public static native int classInfoGetSize(ClassInfoBracket __info)
		throws MLECallError;
	
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
}
