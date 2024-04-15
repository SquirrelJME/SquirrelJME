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
 * Encoding IDs which are built-in to SquirrelJME.
 *
 * @since 2020/04/07
 */
public interface BuiltInEncodingType
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
	
	/** Shift-JIS. */
	byte SHIFT_JIS =
		6;
	
	/** The number of built-in encodings. */
	byte NUM_BUILTIN_ENCODINGS =
		7;
}
