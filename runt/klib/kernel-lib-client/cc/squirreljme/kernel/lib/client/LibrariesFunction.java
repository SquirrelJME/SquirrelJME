// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.kernel.lib.client;

/**
 * Functions for the library interface.
 *
 * @since 2018/01/05
 */
public enum LibrariesFunction
{
	/** List programs which are available. */
	LIST_PROGRAMS,
	
	/** Install a program. */
	INSTALL_PROGRAM,
	
	/** Load bytes from a resource. */
	LOAD_RESOURCE_BYTES,
	
	/** End. */
	;
}

