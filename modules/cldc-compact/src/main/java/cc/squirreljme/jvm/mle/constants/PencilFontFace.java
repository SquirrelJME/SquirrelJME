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
 * Indicates the face of the font.
 *
 * @since 2024/05/17
 */
public interface PencilFontFace
{
	/** Monospaced. */
	byte MONOSPACE =
		1;
	
	/** Serifs. */
	byte SERIF =
		2;
	
	/** Symbol. */
	byte SYMBOL =
		4;
	
	/** Normal, nothing different from anything. */
	byte NORMAL =
		8;
	
	/** Special case for automatic font selection. */
	byte AUTOMATIC =
		16;
	
	/** Stylistic and artistic fonts. */
	byte STYLISTIC =
		32;
}
