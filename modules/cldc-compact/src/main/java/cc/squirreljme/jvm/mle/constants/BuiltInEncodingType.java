// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

import cc.squirreljme.runtime.cldc.annotation.Exported;

/**
 * Encoding IDs which are built-in to SquirrelJME.
 *
 * @since 2020/04/07
 */
@Exported
public interface BuiltInEncodingType
{
	/** Unspecified, use defined property or assume UTF-8. */
	@Exported
	byte UNSPECIFIED =
		0;
	
	/** UTF-8. */
	@Exported
	byte UTF8 =
		1;
	
	/** ASCII. */
	@Exported
	byte ASCII =
		2;
	
	/** IBM037 (EBCDIC). */
	@Exported
	byte IBM037 =
		3;
	
	/** ISO-8859-1. */
	@Exported
	byte ISO_8859_1 =
		4;
	
	/** ISO-8859-15. */
	@Exported
	byte ISO_8859_15 =
		5;
	
	/** Shift-JIS. */
	@Exported
	byte SHIFT_JIS =
		6;
	
	/** The number of built-in encodings. */
	@Exported
	byte NUM_BUILTIN_ENCODINGS =
		7;
}
