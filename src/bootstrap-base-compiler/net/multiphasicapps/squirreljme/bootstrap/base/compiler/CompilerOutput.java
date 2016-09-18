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

/**
 * This interface is used to allow classes utilizing the compiler to place
 * output files in a target JAR potentially.
 *
 * @since 2016/09/18
 */
public interface CompilerOutput
{
	/**
	 * This is called when a file has been compiled, the bytes which make up
	 * the file are provided via the given {@link InputStream}.
	 *
	 * @param __n The name of the file being written, this is normalized to
	 * the name of entries in ZIP files.
	 * @param __is The bytes which make up the file.
	 * @throws IOException On read/write errors.
	 * @since 2016/09/18
	 */
	public abstract void output(String __n, InputStream __is)
		throws IOException;
}

