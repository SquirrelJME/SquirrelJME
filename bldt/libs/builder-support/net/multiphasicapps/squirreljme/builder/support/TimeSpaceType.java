// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.builder.support;

/**
 * This specifies the type of timespace to select when looking for projects.
 *
 * @since 2017/11/14
 */
public enum TimeSpaceType
{
	/** Runtime. */
	RUNTIME,
	
	/** JIT time. */
	JIT,
	
	/** Build time. */
	BUILD,
	
	/** End. */
	;
}

