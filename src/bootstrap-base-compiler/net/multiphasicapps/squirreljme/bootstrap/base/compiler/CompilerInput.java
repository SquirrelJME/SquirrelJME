// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.bootstrap.base.compiler;

import java.io.InputStream;
import java.io.IOException;
import java.nio.file.NoSuchFileException;

/**
 * This interface is used for obtaining files needed for the compiler input.
 *
 * All requested file names are treated as if they were named accordingly to
 * ZIP files.
 *
 * @since 2016/09/18
 */
public interface CompilerInput
{
	/**
	 * Opens the file associated with the input name.
	 *
	 * @param __name The name of the file.
	 * @return The stream for the file data.
	 * @throws IOException On read errors.
	 * @throws NoSuchFileException If the file does not exist.
	 * @since 2016/09/19
	 */
	public abstract InputStream input(String __name)
		throws IOException, NoSuchFileException;
}

