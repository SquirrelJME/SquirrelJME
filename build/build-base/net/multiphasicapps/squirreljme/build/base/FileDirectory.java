// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.base;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

/**
 * A file directory represents the contents of a binary JAR file or a tree of
 * source code within a directory.
 *
 * Entries are named according to how they would appear in ZIP files with
 * no distinction of directories being made (so a file would be called for
 * example {@code META-INF/MANIFEST.MF} regardless of the host system).
 *
 * Iteration goes over the contents of the directory.
 *
 * @since 2016/12/21
 */
public interface FileDirectory
	extends Closeable, Iterable<String>
{
	/**
	 * Opens the specified file and returns the stream to the file data.
	 *
	 * @param __fn The file name to open.
	 * @return The stream of the input file.
	 * @throws IOException If the file does not exist or it could not be
	 * opened.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/12/21
	 */
	public abstract InputStream open(String __fn)
		throws IOException, NullPointerException;
}

