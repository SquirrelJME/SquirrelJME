// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;

/**
 * Flags used for font information.
 *
 * @since 2020/11/14
 */
@SquirrelJMEVendorApi
public interface UIFontFlag
{
	/** Pixel size mask. */
	@SquirrelJMEVendorApi
	byte PIXEL_SIZE_MASK =
		0x7F;
	
	/** The monospace font. */
	@SquirrelJMEVendorApi
	short FACE_MONOSPACE =
		0x02_00;
	
	/** Proportional fonts. */
	@SquirrelJMEVendorApi
	short FACE_PROPORTIONAL =
		0x01_00;
	
	/** The system font. */
	@SquirrelJMEVendorApi
	short FACE_SYSTEM =
		0x00_00;
	
	/** Font face mask. */
	@SquirrelJMEVendorApi
	short FACE_MASK =
		0x7F_00;
	
	/** Bold text. */
	@SquirrelJMEVendorApi
	int STYLE_BOLD_FLAG =
		0x1000_0000;
	
	/** Italic (slanted) text. */
	@SquirrelJMEVendorApi
	int STYLE_ITALIC_FLAG =
		0x2000_0000;
	
	/** Underlined text. */
	@SquirrelJMEVendorApi
	int STYLE_UNDERLINED_FLAG =
		0x4000_0000;
}
