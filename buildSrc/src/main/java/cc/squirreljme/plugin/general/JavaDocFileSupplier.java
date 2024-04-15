// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.general;

import java.io.IOException;
import java.io.InputStream;

/**
 * Supplier for opening files.
 *
 * @since 2022/08/29
 */
@FunctionalInterface
public interface JavaDocFileSupplier
{
	/**
	 * Opens the given file.
	 * 
	 * @return The stream to the given file.
	 * @throws IOException On read errors.
	 * @since 2022/08/29
	 */
	InputStream open()
		throws IOException;
}
