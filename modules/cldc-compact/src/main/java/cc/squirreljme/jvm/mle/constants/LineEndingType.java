// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

import cc.squirreljme.runtime.cldc.annotation.Exported;

/**
 * Constants for line ending.
 *
 * @since 2020/06/09
 */
@Exported
public interface LineEndingType
{
	/** Unknown. */
	@Exported
	byte UNSPECIFIED =
		0;
	
	/** LF. */
	@Exported
	byte LF =
		1;
	
	/** CR. */
	@Exported
	byte CR =
		2;
	
	/** CRLF. */
	@Exported
	byte CRLF =
		3;
		
	/** Number of line types. */
	@Exported
	byte NUM_LINE_ENDINGS =
		4;
}
