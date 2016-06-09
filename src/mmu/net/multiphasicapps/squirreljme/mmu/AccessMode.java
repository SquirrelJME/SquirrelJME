// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.mmu;

/**
 * This represents the access mode of a region of memory.
 *
 * @since 2016/06/09
 */
public enum AccessMode
{
	/** No access permitted (000). */
	NO_ACCESS,
	
	/** Read-only (001). */
	READ,
	
	/** Write-only (010). */
	WRITE,
	
	/** Read/write (011). */
	READ_WRITE,
	
	/** Execute (100). */
	EXECUTE,
	
	/** Read/execute (101). */
	READ_EXECUTE,
	
	/** Write/execute (110). */
	WRITE_EXECUTE,
	
	/** Read/write/execute (111). */
	READ_WRITE_EXECUTE,
	
	/** End. */
	;
}

