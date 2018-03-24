// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.gfx;

/**
 * This represents the pixel format that is used.
 *
 * @since 2018/03/24
 */
public enum PixelFormat
{
	/** Byte Packed Indexed, 1-bit. */
	BYTE_INDEXED1,
	
	/** Byte Packed Indexed, 2-bit. */
	BYTE_INDEXED2,
	
	/** Byte Packed Indexed, 4-bit. */
	BYTE_INDEXED4,
	
	/** Byte Indexed. */
	BYTE_INDEXED8,
	
	/** Byte RGB332. */
	BYTE_RGB332,
	
	/** Short Indexed. */
	SHORT_INDEXED16,
	
	/** Short ARGB4444. */
	SHORT_ARGB4444,
	
	/** Short RGB565. */
	SHORT_RGB565,
	
	/** Integer ARGB8888. */
	INTEGER_ARGB8888,
	
	/** Integer RGB888. */
	INTEGER_RGB888,
	
	/** End. */
	;
}

