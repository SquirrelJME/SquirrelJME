// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.pack.constants;

import cc.squirreljme.runtime.cldc.annotation.Exported;

/**
 * Represents a flag property for a TOC.
 *
 * @since 2022/01/08
 */
@Exported
public interface PackFlag
{
	/** This pack consists of traditional Java classes. */
	@Exported
	byte IS_SPRINGCOAT =
		0x1;
}
