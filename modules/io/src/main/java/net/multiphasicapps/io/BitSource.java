// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io;

import java.io.IOException;

/**
 * This is used for huffman tree traversal.
 *
 * @since 2016/08/16
 */
public interface BitSource
{
	/**
	 * Returns the next bit.
	 *
	 * @return The next bit.
	 * @throws IOException On read errors.
	 * @since 2016/08/16
	 */
	boolean nextBit()
		throws IOException;
}

