package cc.squirreljme.emulator;

// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

import cc.squirreljme.jvm.SystemCallIndex;

/**
 * This contains the needed context for emulator threads.
 *
 * @since 2020/02/26
 */
public final class EmulatorThreadContext
{
	/** Error codes that are available. */
	public final int[] errorCodes =
		new int[SystemCallIndex.NUM_SYSCALLS];
	
	/**
	 * Gets the error for the given system call.
	 *
	 * @param __si The system call error to get.
	 * @return The error code.
	 * @since 2020/02/26
	 */
	public final int getError(short __si)
	{
		// If the index is out of bounds, write to the first system call
		return this.errorCodes[((__si < 0 ||
			__si >= SystemCallIndex.NUM_SYSCALLS) ? 0 : __si)];
	}
	
	/**
	 * Sets the error for the given system call.
	 *
	 * @param __si The system call error to set.
	 * @param __code The new code.
	 * @since 2020/02/26
	 */
	public final void setError(short __si, int __code)
	{
		// If the index is out of bounds, write to the first system call
		this.errorCodes[((__si < 0 ||
			__si >= SystemCallIndex.NUM_SYSCALLS) ? 0 : __si)] = __code;
	}
}
