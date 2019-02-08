// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.scrf;

/**
 * This represents the type of invocation to perform.
 *
 * @since 2019/02/07
 */
public enum InvokeType
{
	/** Static method. */
	STATIC,
	
	/** Virtual method. */
	VIRTUAL,
	
	/** Interface method. */
	INTERFACE,
	
	/** Special invoke (private, constructor, or supermethod). */
	SPECIAL,
	
	/** End. */
	;
}

