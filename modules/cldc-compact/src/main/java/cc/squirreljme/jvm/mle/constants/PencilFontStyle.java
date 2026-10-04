// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMENativeApi;

/**
 * Font style for pencil fonts.
 *
 * @since 2024/05/17
 */
@SquirrelJMENativeApi(min = "0.4.0")
public interface PencilFontStyle
{
	/** Bold text. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte BOLD =
		1;
	
	/** Italic (slanted) text. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte ITALIC =
		2;
	
	/** Underlined text. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte UNDERLINED =
		4;
	
	/** Special case for automatic style selection. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte AUTOMATIC =
		8;
}
