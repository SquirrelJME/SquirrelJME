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
 * This represents the scope of resources within the system.
 *
 * @since 2018/02/11
 */
public enum LibraryResourceScope
{
	/** Normal resource. */
	RESOURCE,
	
	/** Resources required by the compiler to function. */
	COMPILER,
	
	/** End. */
	;
}

