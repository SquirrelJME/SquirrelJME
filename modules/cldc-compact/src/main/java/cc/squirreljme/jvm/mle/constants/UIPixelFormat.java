// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
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
	/** 32-bit RGBA ({@code int}). */
	byte INT_RGBA8888 =
		0;
	
	/** 256 Colors ({@code byte}). */
	byte BYTE_INDEXED256 =
		1;
	
	/** The number of pixel formats. */
	byte NUM_PIXEL_FORMATS =
		2;
}
