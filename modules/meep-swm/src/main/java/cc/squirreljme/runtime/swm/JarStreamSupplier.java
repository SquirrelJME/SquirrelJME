// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.swm;

import java.io.InputStream;
import java.io.IOException;

/**
 * This is used to obtain an input stream which contains JAR file data which
 * would then be passed through the JIT for recompilation.
 *
 * @since 2017/12/28
 */
public interface JarStreamSupplier
{
	/**
	 * Returns an input stream over the JAR data.
	 *
	 * @return The input stream over the JAR data.
	 * @throws IOException If it could not be obtained.
	 * @since 2017/12/28
	 */
	public abstract InputStream get()
		throws IOException;
}

