// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm;

/**
 * This represents local thread data.
 *
 * @since 2019/12/29
 */
public final class ThreadData
{
	/** Errors used in system calls. */
	public final int[] errors =
		new int[SystemCallIndex.NUM_SYSCALLS];
	
	/** The exception pointer. */
	public int exception;
}

