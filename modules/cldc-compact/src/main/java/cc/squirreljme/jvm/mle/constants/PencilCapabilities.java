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
 * Capabilities for the hardware accelerated pencil graphics drawing.
 * 
 * This interface contains bit-fields.
 *
 * @since 2020/09/25
 */
@Exported
public interface PencilCapabilities
{
	/**
	 * Minimum capabilities required by the implementation, this includes all
	 * of that state operations such as: transforms, clips, colors, styles,
	 * and otherwise.
	 * 
	 * Capable of the following:
	 * - {@code clipRect}. 
	 * - {@code getAlpha}.
	 * - {@code getAlphaColor}.
	 * - {@code getBlendingMode}.
	 * - {@code getBlueComponent}.
	 * - {@code getClipHeight}.
	 * - {@code getClipWidth}.
	 * - {@code getClipX}.
	 * - {@code getClipY}.
	 * - {@code getColor}.
	 * - {@code getDisplayColor}.
	 * - {@code getGrayScale}.
	 * - {@code getGreenComponent}.
	 * - {@code getRedComponent}.
	 * - {@code getStrokeStyle}.
	 * - {@code getTranslateX}.
	 * - {@code getTranslateY}.
	 * - {@code setAlpha}.
	 * - {@code setAlphaColor}.
	 * - {@code setBlendingMode}.
	 * - {@code setClip}.
	 * - {@code setColor}.
	 * - {@code setGrayScale}.
	 * - {@code setStrokeStyle}.
	 * - {@code translate}.
	 */
	@Exported
	byte MINIMUM =
		1;
	
	/**
	 * Capable of the following:
	 * - {@code copyArea}. 
	 */
	@Exported
	byte COPY_AREA =
		2;
	
	/**
	 * Capable of the following:
	 * - {@code drawARGB16}. 
	 * - {@code drawRGB16}.
	 */
	@Exported
	byte DRAW_XRGB16_SIMPLE =
		4;
	
	/**
	 * Reserved.
	 */
	@Exported
	@Deprecated
	byte RESERVED_8 =
		8;
	
	/**
	 * Reserved.
	 */
	@Exported
	@Deprecated
	byte RESERVED_16 =
		16;
	
	/**
	 * Capable of the following:
	 * - {@code drawRGB}.
	 * - {@code drawImage}. 
	 * - {@code drawRegion}.
	 */
	@Exported
	byte DRAW_XRGB32_REGION =
		32;
	
	/**
	 * Capable of the following:
	 * - {@code drawArc}. 
	 */
	@Exported
	byte DRAW_ARC =
		64;
	
	/**
	 * Capable of the following:
	 * - {@code drawLine}. 
	 */
	@Exported
	short DRAW_LINE =
		128;
	
	/**
	 * Capable of the following:
	 * - {@code drawRect}. 
	 */
	@Exported
	short DRAW_RECT =
		256;
	
	/**
	 * Capable of the following:
	 * - {@code drawRoundRect}. 
	 */
	@Exported
	short DRAW_ROUND_RECT =
		512;
	
	/**
	 * Capable of the following:
	 * - {@code fillArc}. 
	 */
	@Exported
	short FILL_ARC =
		1024;
	
	/**
	 * Capable of the following:
	 * - {@code fillRect}. 
	 */
	@Exported
	short FILL_RECT =
		2048;
	
	/**
	 * Capable of the following:
	 * - {@code fillRoundRect}. 
	 */
	@Exported
	short FILL_ROUND_RECT =
		4096;
	
	/**
	 * Capable of the following:
	 * - {@code fillTriangle}. 
	 */
	@Exported
	short FILL_TRIANGLE =
		8192;
	
	/**
	 * Capable of the following:
	 * - {@code drawChar}.
	 * - {@code drawChars}.
	 * - {@code drawString}.
	 * - {@code drawSubstring}.
	 * - {@code drawText}.
	 * - {@code getFont}.
	 * - {@code setFont}.
	 */
	@Exported
	short FONT_TEXT =
		16384;
}
