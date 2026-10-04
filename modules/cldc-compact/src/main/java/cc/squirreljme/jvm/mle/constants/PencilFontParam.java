// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

import cc.squirreljme.jvm.mle.brackets.PencilFontBracket;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;

/**
 * Parameters for {@link PencilFontBracket}.
 *
 * @since 2026/04/10
 */
public interface PencilFontParam
{
	/** The {@link PencilFontStyle} of the font. */
	byte STYLE =
		1;
	
	/** The pixel size of the font. */
	byte PIXEL_SIZE =
		2;
	
	/** The number of available font parameters. */
	byte NUM_PARAMS = 
		3;
}
