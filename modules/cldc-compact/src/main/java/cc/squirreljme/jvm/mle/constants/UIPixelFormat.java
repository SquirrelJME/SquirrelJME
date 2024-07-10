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
	byte INT_XBGR8888 =
		13;
	
	/** The number of pixel formats. */
	byte NUM_PIXEL_FORMATS =
		14;
}
