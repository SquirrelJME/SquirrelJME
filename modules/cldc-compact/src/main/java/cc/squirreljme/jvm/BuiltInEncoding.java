// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm;

/**
 * Encoding IDs which are built-in to SquirrelJME.
 *
 * @since 2020/04/07
 */
public interface BuiltInEncoding
{
	/** Unspecified, use defined property or assume UTF-8. */
	byte UNSPECIFIED =
		0;
	
	/** UTF-8. */
	byte UTF8 =
		1;
	
	/** ASCII. */
	byte ASCII =
		2;
	
	/** IBM037 (EBCDIC). */
	byte IBM037 =
		3;
	
	/** ISO-8859-1. */
	byte ISO_8859_1 =
		4;
	
	/** ISO-8859-15. */
	byte ISO_8859_15 =
		5;
	
	/** The number of built-in encodings. */
	byte NUM_BUILTIN_ENCODINGS =
		6;
}
