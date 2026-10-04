// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMENativeApi;

/**
 * Encoding IDs which are built-in to SquirrelJME.
 *
 * @since 2020/04/07
 */
@SquirrelJMENativeApi(min = "0.4.0")
public interface BuiltInEncodingType
{
	/** Unspecified, use defined property or assume UTF-8. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte UNSPECIFIED =
		0;
	
	/** UTF-8. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte UTF8 =
		1;
	
	/** ASCII. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte ASCII =
		2;
	
	/** IBM037 (EBCDIC). */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte IBM037 =
		3;
	
	/** ISO-8859-1. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte ISO_8859_1 =
		4;
	
	/** ISO-8859-15. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte ISO_8859_15 =
		5;
	
	/** Shift-JIS. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte SHIFT_JIS =
		6;
	
	/** IBM437. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte IBM437 =
		7;
	
	/** The number of built-in encodings. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte NUM_BUILTIN_ENCODINGS =
		8;
}
