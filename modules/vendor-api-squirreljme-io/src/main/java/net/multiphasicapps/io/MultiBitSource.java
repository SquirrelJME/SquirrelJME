// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import java.io.IOException;

/**
 * Similar to {@link BitSource} except that this supports reading multiple
 * bits at once.
 *
 * @since 2026/10/03
 */
public interface MultiBitSource
	extends BitSource
{
	/**
	 * Reads bits from the input source.
	 *
	 * @param __n The number of bits to read.
	 * @param __msb If {@code true} the most significant bits are first.
	 * @return The read data.
	 * @throws IOException On read errors.
	 * @since 2026/10/03
	 */
	int readBits(int __n, boolean __msb)
		throws IOException;
}
