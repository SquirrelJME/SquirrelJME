// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.base;

import java.io.InputStream;
import java.io.IOException;
import java.io.Flushable;
import java.io.OutputStream;

/**
 * This interface is used to allow classes utilizing the compiler to place
 * output files in a target JAR potentially.
 *
 * @since 2016/09/18
 */
public interface CompilerOutput
	extends Flushable
{
	/**
	 * This is called when a file has been or is being compiled and it is
	 * required to output the file.
	 *
	 * @param __n The name of the file being written, this should match the
	 * same format that is used in ZIP files.
	 * @return An output stream which writes bytes to the output.
	 * @throws IOException On read/write errors.
	 * @since 2016/09/18
	 */
	public abstract OutputStream output(String __n)
		throws IOException;
}

