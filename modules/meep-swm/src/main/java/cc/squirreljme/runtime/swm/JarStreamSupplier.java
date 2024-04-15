// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.swm;

import java.io.IOException;
import java.io.InputStream;

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
	InputStream get()
		throws IOException;
}

