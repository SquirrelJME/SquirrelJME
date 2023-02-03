// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io;

import cc.squirreljme.runtime.cldc.annotation.Exported;

/**
 * This can be set on a stream which sets the default endianess on methods
 * that do not read using a specified endianess.
 *
 * @since 2016/07/10
 */
@Exported
public enum DataEndianess
{
	/** Big endian. */
	@Exported
	BIG,
	
	/** Little endian. */
	@Exported
	LITTLE,
	
	/** End. */
	;
}

