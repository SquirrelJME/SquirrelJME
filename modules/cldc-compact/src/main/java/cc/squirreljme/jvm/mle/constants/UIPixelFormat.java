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
 * Pixel format used for user interface drawing and otherwise.
 *
 * @since 2020/09/20
 */
public interface UIPixelFormat
{
	/** 32-bit RGBA ({@code int}) [Java ME Standard]. */
	byte INT_ARGB8888 =
		0;
	
	/** 32-bit RGB ({@code int}) [Java ME Standard]. */
	byte INT_RGB888 =
		1;
	
	/** 16-bit ARGB4444. ({@code short}) [Java ME Standard]. */
	byte SHORT_ARGB4444 =
		2;
	
	/** 16-bit RGB565. ({@code short}) [Java ME Standard]. */
	byte SHORT_RGB565 =
		3;
	
	/** 16-bit RGB555. ({@code short}). */
	byte SHORT_RGB555 =
		4;
	
	/** 16-bit ABGR1555. ({@code short}) [PlayStation 2]. */
	byte SHORT_ABGR1555 =
		5;
	
	/** 65536 Colors ({@code short}). */
	byte SHORT_INDEXED65536 =
		6;
	
	/** 256 Colors ({@code byte}). */
	byte BYTE_INDEXED256 =
		7;
	
	/** Packed 16 colors (4-bit). ({@code packed byte}) */
	byte PACKED_INDEXED4 =
		8;
	
	/** Packed 4 Colors (2-bit). ({@code packed byte}) */
	byte PACKED_INDEXED2 =
		9;
	
	/** Packed 2 colors (1-bit). ({@code packed byte}) */
	byte PACKED_INDEXED1 =
		10;
	
	/** 32-bit BGRA ({@code int}). */
	byte INT_BGRA8888 =
		11;
	
	/** 32-bit BGRX ({@code int}). */
	byte INT_BGRX8888 =
		12;
	
	/** 32-bit XBGR ({@code int}). */
	byte INT_BGR888 =
		13;
	
	/** 32-bit RGBX ({@code int}). */
	byte INT_RGBX8888 =
		14;
	
	/** 24-bit RGB consisting of three bytes. */
	byte BYTE3_RGB888 =
		15;
	
	/** 24-bit BGR consisting of three bytes. */
	byte BYTE3_BGR888 =
		16;
	
	/** 65536 Colors ({@code short} ), alpha enabled. */
	byte SHORT_INDEXED65536A = 
		17;
	
	/** 256 Colors ({@code byte} ), alpha enabled. */
	byte BYTE_INDEXED256A = 
		18;
	
	/** Packed 16 colors (4-bit), alpha enabled. (packed {@code byte} ) */
	byte PACKED_INDEXED4A = 
		19;
	
	/** Packed 4 Colors (2-bit), alpha enabled. (packed {@code byte} ) */
	byte PACKED_INDEXED2A = 
		20;
	
	/** Packed 2 colors (1-bit), alpha enabled. (packed {@code byte} ) */
	byte PACKED_INDEXED1A = 
		21;
	
	/** 16-bit RGB444. ({@code short} ). */
	byte SHORT_RGB444 =
		22;

	/** 16-bit ARGB1555. ({@code short}) [Nokia]. */
	byte SHORT_ARGB1555 =
		23;

	/** INDEXED1 but with vertical pixel disposition. (packed {@code byte}) */
	byte PACKED_INDEXED1_VERTICAL = 
		24;

	/** 8-bit RGB332. {@code byte}. */
	byte BYTE_RGB332 = 
		25;
	
	/** Only 8-bit alpha (@c uint8_t ). */
	byte BYTE_A8 = 
		26;
	
	/** Only 4-bit alpha (packed @c uint8_t ). */
	byte PACKED_A4 = 
		27;
	
	/** Only 2-bit alpha (packed @c uint8_t ). */
	byte PACKED_A2 = 
		28;
	
	/** Only 1-bit alpha (packed @c uint8_t ). */
	byte PACKED_A1 = 
		29;
	
	/** Only 8-bit red (@c uint8_t ). */
	byte BYTE_R8 = 
		30;
	
	/** Only 8-bit green (@c uint8_t ). */
	byte BYTE_G8 = 
		31;
	
	/** Only 8-bit blue (@c uint8_t ). */
	byte BYTE_B8 = 
		32;
	
	/** The number of pixel formats. */
	byte NUM_PIXEL_FORMATS =
		33;
}
