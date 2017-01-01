// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.base;

import java.io.Closeable;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.NoSuchElementException;
import net.multiphasicapps.squirreljme.jit.base.JITException;

/**
 * This interface is used to implement namespace browsers which are used to
 * lookup namespaces that may be on the disk for the JIT to recompile.
 *
 * @since 2016/07/07
 */
public interface JITNamespaceBrowser
{
	/**
	 * Obtains the directory of the given namespace.
	 *
	 * @param __ns The namespace to get the directory of.
	 * @return The directory for the given namespace.
	 * @throws IOException On read errors.
	 * @throws JITException If the directory does not exist.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/07
	 */
	public abstract JITNamespaceBrowser.Directory directoryOf(String __ns)
		throws IOException, JITException, NullPointerException;
	
	/**
	 * Returns an iterator over the namespaces which are available.
	 *
	 * @return An iterator of available namespaces.
	 * @throws IOException On read errors.
	 * @since 2016/09/10
	 */
	public abstract Iterator<String> listNamespaces()
		throws IOException;
	
	/**
	 * This interface describes the directory of a given namespace.
	 *
	 * @since 2016/07/07
	 */
	public static interface Directory
		extends Closeable
	{
		/**
		 * Returns the next entry in the directory.
		 *
		 * @return The next entry or {@code null} if there are no entries.
		 * remaining.
		 * @throws IOException On read errors.
		 * @throws JITException If it could no be obtained.
		 * @since 2016/09/11
		 */
		public abstract Entry nextEntry()
			throws IOException, JITException;
	}
	
	/**
	 * This represents a single entry within a directory along with its data.
	 *
	 * @since 2016/07/07
	 */
	public static abstract class Entry
		extends InputStream
	{
		/**
		 * Returns the name of the entry.
		 *
		 * @return The entry name.
		 * @throws JITException If the name could not be determined.
		 * @since 2016/07/07
		 */
		public abstract String name()
			throws JITException;
	}
}

