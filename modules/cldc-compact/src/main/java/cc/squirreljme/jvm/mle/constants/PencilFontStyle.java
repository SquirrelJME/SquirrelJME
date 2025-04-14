// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;

/**
 * Font style for pencil fonts.
 *
 * @since 2024/05/17
 */
@SquirrelJMEVendorApi
public interface PencilFontStyle
{
	/** Bold text. */
	@SquirrelJMEVendorApi
	byte BOLD =
		1;
	
	/** Italic (slanted) text. */
	@SquirrelJMEVendorApi
	byte ITALIC =
		2;
	
	/** Underlined text. */
	@SquirrelJMEVendorApi
	byte UNDERLINED =
		4;
}
