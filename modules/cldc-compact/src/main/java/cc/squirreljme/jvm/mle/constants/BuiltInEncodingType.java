// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;

/**
 * Encoding IDs which are built-in to SquirrelJME.
 *
 * @since 2020/04/07
 */
@SquirrelJMEVendorApi
public interface BuiltInEncodingType
{
	/** Unspecified, use defined property or assume UTF-8. */
	@SquirrelJMEVendorApi
	byte UNSPECIFIED =
		0;
	
	/** UTF-8. */
	@SquirrelJMEVendorApi
	byte UTF8 =
		1;
	
	/** ASCII. */
	@SquirrelJMEVendorApi
	byte ASCII =
		2;
	
	/** IBM037 (EBCDIC). */
	@SquirrelJMEVendorApi
	byte IBM037 =
		3;
	
	/** ISO-8859-1. */
	@SquirrelJMEVendorApi
	byte ISO_8859_1 =
		4;
	
	/** ISO-8859-15. */
	@SquirrelJMEVendorApi
	byte ISO_8859_15 =
		5;
	
	/** Shift-JIS. */
	@SquirrelJMEVendorApi
	byte SHIFT_JIS =
		6;
	
	/** The number of built-in encodings. */
	@SquirrelJMEVendorApi
	byte NUM_BUILTIN_ENCODINGS =
		7;
}
