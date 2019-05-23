// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.vki;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.lang.ApiLevel;

/**
 * This contains the index of system calls.
 *
 * @since 2019/05/23
 */
public final class SystemCallIndex
{
	/** Checks if the system call is supported. */
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_3_0_DEV)
	public static final short QUERY_INDEX =
		0;
	
	/** Gets the last error state. */
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_3_0_DEV)
	public static final short ERROR_GET =
		1;
	
	/** Sets the last error state. */
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_3_0_DEV)
	public static final short ERROR_SET =
		2;
	
	/** Current wall clock milliseconds (low). */
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_3_0_DEV)
	public static final short TIME_LO_MILLI_WALL =
		3;
	
	/** Current wall clock milliseconds (high). */
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_3_0_DEV)
	public static final short TIME_HI_MILLI_WALL =
		4;
	
	/** Current monotonic clock nanoseconds (low). */
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_3_0_DEV)
	public static final short TIME_LO_NANO_MONO =
		5;
	
	/** Current monotonic clock nanoseconds (high). */
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_3_0_DEV)
	public static final short TIME_HI_NANO_MONO =
		6;
	
	/** VM Information: Free memory in bytes. */
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_3_0_DEV)
	public static final short VMI_MEM_FREE =
		7;
	
	/** VM Information: Used memory in bytes. */
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_3_0_DEV)
	public static final short VMI_MEM_USED =
		8;
	
	/** VM Information: Max memory in bytes. */
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_3_0_DEV)
	public static final short VMI_MEM_MAX =
		9;
	
	/** Invoke the garbage collector. */
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_3_0_DEV)
	public static final short GARBAGE_COLLECT =
		10;
	
	/** Exit the VM. */
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_3_0_DEV)
	public static final short EXIT =
		11;
	
	/** System call count. */
	public static final int NUM_SYSCALLS =
		12;
	
	/**
	 * Not used.
	 *
	 * @since 2019/05/23
	 */
	private SystemCallIndex()
	{
	}
}

