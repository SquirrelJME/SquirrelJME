// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;
import net.multiphasicapps.squirreljme.jit.base.JITException;

/**
 * This is associated with a {@link JITOutputConfig} and if it exists, it
 * specifies that the {@link JITOutput} implementations should call the creator
 * to create output streams for namespace caching.
 *
 * @since 2016/07/06
 */
public interface JITCacheCreator
{
	/**
	 * This returns an array of all the namespaces which were previously
	 * cached by this creator.
	 *
	 * @return An array of previously cached namespaces.
	 * @throws JITException If it could not be obtained.
	 * @since 2016/07/18
	 */
	public abstract String[] cachedNamespaces()
		throws JITException;
	
	/**
	 * Returns an output stream which creates a serialized on-disk cache of
	 * executable code for later linking or loading.
	 *
	 * @param __ns The namespace to create a cache for.
	 * @return An output stream to the cache file.
	 * @throws IOException If the output stream could not be created.
	 * @throws JITException On other unknown reasons.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/06
	 */
	public abstract OutputStream createCache(String __ns)
		throws IOException, JITException, NullPointerException;
	
	/**
	 * Attempts to open the previously cached content as an input stream.
	 *
	 * It is undefined what happens when a cache that is currently being
	 * written is opened.
	 *
	 * @param __ns The name of a previously cached namespace.
	 * @throws IOException If the cache could not be opened.
	 * @throws JITException If the cache could not be opened for a non-I/O
	 * related reason.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/18
	 */
	public abstract InputStream openCache(String __ns)
		throws IOException, JITException, NullPointerException;
}

