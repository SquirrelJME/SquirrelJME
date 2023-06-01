// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat;

/**
 * This is the type of linkage that is used for the constant pool.
 *
 * @since 2023/05/31
 */
public enum PoolLinkageType
{
	/** Invoke special linkage, uses source and destination class. */
	INVOKE_SPECIAL,
	
	/* End. */
	;
}
