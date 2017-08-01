// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io.data;

/**
 * This can be set on a stream which sets the default endianess on methods
 * that do not read using a specified endianess.
 *
 * @since 2016/07/10
 */
public enum DataEndianess
{
	/** Big endian. */
	BIG,
	
	/** Little endian. */
	LITTLE,
	
	/** End. */
	;
}

