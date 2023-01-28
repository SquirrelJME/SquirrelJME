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
 * Standard pipe descriptor identifiers.
 *
 * @since 2020/06/14
 */
@Exported
public interface StandardPipeType
{
	/** Standard input. */
	@Exported
	byte STDIN =
		0;
	
	/** Standard output. */
	@Exported
	byte STDOUT =
		1;
	
	/** Standard error. */
	@Exported
	byte STDERR =
		2;
	
	/** The number of standard pipes. */
	@Exported
	byte NUM_STANDARD_PIPES =
		3;
}
