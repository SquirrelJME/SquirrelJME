// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.config;

import java.io.InputStream;

/**
 * Supplies an input stream for use for reading configurations.
 *
 * @since 2020/05/25
 */
public interface InputStreamSupplier
{
	/**
	 * Gets an input stream for configuration reading.
	 *
	 * @return An input stream for configuration reading.
	 * @since 2020/05/25
	 */
	InputStream get();
}
