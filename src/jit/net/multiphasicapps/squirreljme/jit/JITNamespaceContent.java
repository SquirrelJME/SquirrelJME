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

import java.io.Closeable;
import java.io.IOException;

/**
 * This interface is used with {@link JITNamespaceProcessor} to provide the
 * content for namespaces and such.
 *
 * @since 2016/07/07
 */
public interface JITNamespaceContent
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
	public abstract JITNamespaceContent.Directory directoryOf(String __ns)
		throws IOException, JITException, NullPointerException;
	
	/**
	 * This interface describes the directory of a given namespace.
	 *
	 * @since 2016/07/07
	 */
	public static interface Directory
		extends Closeable, Iterable<Entry>
	{
	}
	
	/**
	 * This represents a single entry within a directory.
	 *
	 * @since 2016/07/07
	 */
	public static interface Entry
	{
	}
}

