// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.library;

/**
 * System defined control keys, used to store internal details about a library.
 *
 * @since 2017/12/31
 */
public enum LibraryControlKey
{
	/** Download URL. */
	DOWNLOAD_UR,
	
	/** Is the program installed? */
	IS_INSTALLED,
	
	/** Is the program trusted? */
	IS_TRUSTED,
	
	/** The trust group for this library. */
	TRUST_GROUP,
	
	/** The type of library this is. */
	TYPE,
	
	/** Is the program available? */
	STATE_FLAG_AVAILABLE,
	
	/** Is the program enabled? */
	STATE_FLAG_ENABLED,
	
	/** Dependencies. */
	DEPENDENCIES,
	
	/** End. */
	;
}

