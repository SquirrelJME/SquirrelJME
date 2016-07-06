// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import java.io.IOException;
import java.io.OutputStream;
import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;

/**
 * This is associated with a {@link JITOutputConfig} and if it exists, it
 * specifies that the {@link JITOutput} implementations should call the creator
 * to create output streams for class caching.
 *
 * @since 2016/07/06
 */
public interface JITCacheCreator
{
	/**
	 * Returns an output stream which creates a serialized on-disk cache of
	 * executable code for later linking or loading.
	 *
	 * @param __ns The namespace the class is in, this would be the name of the
	 * JAR file.
	 * @param __cn The name of the class to create a cache for.
	 * @return An output stream to the cache file.
	 * @throws IOException If the output stream could not be created.
	 * @throws JITException On other unknown reasons.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/06
	 */
	public abstract OutputStream createCache(String __ns, ClassNameSymbol __cn)
		throws IOException, JITException, NullPointerException;
}

