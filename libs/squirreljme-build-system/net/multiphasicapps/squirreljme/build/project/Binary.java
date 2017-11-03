// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.project;

import java.lang.ref.Reference;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * This class represents a binary which has been loaded by the binary manager.
 *
 * @since 2017/10/31
 */
public final class Binary
{
	/**
	 * Initializes the binary.
	 *
	 * @param __ref The reference to the binary manager, to find other binaries
	 * such as for dependencies.
	 * @param __name The name of this binary.
	 * @param __source The source of this binary, may be {@code null} if there
	 * is no source.
	 * @
	 */
	Binary(Reference<BinaryManager> __ref, SourceName __name, Source __source,
		Path __path)
		throws NoSuchBinaryException, NullPointerException
	{
		if (__ref == null || __name == null || __path == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AO0i Cannot open the specified path as a project
		// because it does not exist and there is no base source code. (The
		// path to open as a binary)}
		if (__source == null && !Files.exists(__path))
			throw new NoSuchBinaryException(String.format("AO0i %s", __path));
		
		throw new todo.TODO();
	}
}

