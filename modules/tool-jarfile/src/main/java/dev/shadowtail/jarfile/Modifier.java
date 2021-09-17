// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
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
public enum Modifier
{
	/** No modification to be done. */
	NONE,
	
	/** Offset by RAM address. */
	RAM_OFFSET,
	
	/** Offset by JAR address. */
	JAR_OFFSET,
	
	/** End. */
	;
}

