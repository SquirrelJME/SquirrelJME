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
 * Flags used for font information.
 *
 * @since 2020/11/14
 */
@Exported
public interface UIFontFlag
{
	/** Pixel size mask. */
	@Exported
	byte PIXEL_SIZE_MASK =
		0x7F;
	
	/** The monospace font. */
	@Exported
	short FACE_MONOSPACE =
		0x02_00;
	
	/** Proportional fonts. */
	@Exported
	short FACE_PROPORTIONAL =
		0x01_00;
	
	/** The system font. */
	@Exported
	short FACE_SYSTEM =
		0x00_00;
	
	/** Font face mask. */
	@Exported
	short FACE_MASK =
		0x7F_00;
	
	/** Bold text. */
	@Exported
	int STYLE_BOLD_FLAG =
		0x1000_0000;
	
	/** Italic (slanted) text. */
	@Exported
	int STYLE_ITALIC_FLAG =
		0x2000_0000;
	
	/** Underlined text. */
	@Exported
	int STYLE_UNDERLINED_FLAG =
		0x4000_0000;
}
