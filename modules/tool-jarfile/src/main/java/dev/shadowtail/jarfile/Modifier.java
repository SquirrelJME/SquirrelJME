// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.jarfile;

/**
 * Represents the action modifier that is available.
 *
 * @since 2019/04/30
 */
@Deprecated
public enum Modifier
{
	/** No modification to be done. */
	@Deprecated
	NONE,
	
	/** Offset by RAM address. */
	@Deprecated
	RAM_OFFSET,
	
	/** Offset by JAR address. */
	@Deprecated
	JAR_OFFSET,
	
	/** End. */
	;
}

