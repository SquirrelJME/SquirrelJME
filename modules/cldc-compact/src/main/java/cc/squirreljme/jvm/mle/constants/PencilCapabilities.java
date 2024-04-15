// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

/**
 * Capabilities for the hardware accelerated pencil graphics drawing.
 * 
 * This interface contains bit-fields.
 *
 * @since 2020/09/25
 */
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
	byte MINIMUM =
		1;
	
	/**
	 * Capable of the following:
	 * - {@code copyArea}. 
	 */
	byte COPY_AREA =
		2;
	
	/**
	 * Capable of the following:
	 * - {@code drawARGB16}. 
	 * - {@code drawRGB16}.
	 */
	byte DRAW_XRGB16_SIMPLE =
		4;
	
	/**
	 * Reserved.
	 */
	@Deprecated
	byte RESERVED_8 =
		8;
	
	/**
	 * Capable of the following:
	 * - {@code drawText}.
	 */
	byte TEXT_ADVANCED =
		16;
	
	/**
	 * Capable of the following:
	 * - {@code drawRGB}.
	 * - {@code drawImage}. 
	 * - {@code drawRegion}.
	 */
	byte DRAW_XRGB32_REGION =
		32;
	
	/**
	 * Capable of the following:
	 * - {@code drawArc}. 
	 */
	byte DRAW_ARC =
		64;
	
	/**
	 * Capable of the following:
	 * - {@code drawLine}. 
	 */
	short DRAW_LINE =
		128;
	
	/**
	 * Capable of the following:
	 * - {@code drawRect}. 
	 */
	short DRAW_RECT =
		256;
	
	/**
	 * Capable of the following:
	 * - {@code drawRoundRect}. 
	 */
	short DRAW_ROUND_RECT =
		512;
	
	/**
	 * Capable of the following:
	 * - {@code fillArc}. 
	 */
	short FILL_ARC =
		1024;
	
	/**
	 * Capable of the following:
	 * - {@code fillRect}. 
	 */
	short FILL_RECT =
		2048;
	
	/**
	 * Capable of the following:
	 * - {@code fillRoundRect}. 
	 */
	short FILL_ROUND_RECT =
		4096;
	
	/**
	 * Capable of the following:
	 * - {@code fillTriangle}. 
	 */
	short FILL_TRIANGLE =
		8192;
	
	/**
	 * Capable of the following:
	 * - {@code drawChar}.
	 * - {@code drawChars}.
	 * - {@code drawString}.
	 * - {@code drawSubstring}.
	 * - {@code getFont}.
	 * - {@code setFont}.
	 */
	short TEXT_BASIC =
		16384;
}
