// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.vki;

/**
 * This contains the index of system calls.
 *
 * @since 2019/05/23
 */
public interface SystemCallIndex
{
	/** Checks if the system call is supported. */
	public static final short QUERY_INDEX =
		0;
	
	/** Gets the last error state. */
	public static final short ERROR_GET =
		1;
	
	/** Sets the last error state. */
	public static final short ERROR_SET =
		2;
}

