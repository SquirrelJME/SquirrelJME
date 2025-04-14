// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.fontcompile.out.struct;

/**
 * Flags for SQF Fonts.
 *
 * @since 2024/06/07
 */
public interface SqfFontCharFlag
{
	/** The character is valid. */
	byte VALID =
		1;
	
	/** Compressed with RaFoCES with huffman table. */
	byte RAFOCES =
		2;
}
