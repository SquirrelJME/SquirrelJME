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
 * Indicates the face of the font.
 *
 * @since 2024/05/17
 */
@SquirrelJMENativeApi(min = "0.4.0")
public interface PencilFontFace
{
	/** Monospaced. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte MONOSPACE =
		1;
	
	/** Serifs. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte SERIF =
		2;
	
	/** Symbol. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte SYMBOL =
		4;
	
	/** Normal, nothing different from anything. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte NORMAL =
		8;
	
	/** Special case for automatic font selection. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte AUTOMATIC =
		16;
	
	/** Stylistic and artistic fonts. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte STYLISTIC =
		32;
}
