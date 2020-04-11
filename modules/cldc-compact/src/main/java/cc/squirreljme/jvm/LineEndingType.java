// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm;

/**
 * The type of line ending used.
 *
 * @since 2020/04/09
 */
public interface LineEndingType
{
	/** LF. */
	byte LF =
		0;
	
	/** CR. */
	byte CR =
		1;
	
	/** CRLF. */
	byte CRLF =
		2;
}
