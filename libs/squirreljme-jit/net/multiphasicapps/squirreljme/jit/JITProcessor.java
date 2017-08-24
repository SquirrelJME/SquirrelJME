// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import java.io.InputStream;
import java.io.IOException;
import net.multiphasicapps.zip.streamreader.ZipStreamReader;

/**
 * This class is used to process classes and resources for compilation for the
 * JIT.
 *
 * @since 2017/08/24
 */
public class JITProcessor
{
	/**
	 * Opens the input stream as a ZIP file then processes it.
	 *
	 * @param __is The stream to read ZIP file contents from.
	 * @throws IOException If it is not a ZIP or if the stream could not be
	 * read.
	 * @throws JITException If the JIT could not process the input.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/24
	 */
	public final void process(InputStream __is)
		throws IOException, JITException, NullPointerException
	{
		// Check
		if (__is == null)
			throw new NullPointerException("NARG");
		
		// Wrap in a ZIP
		process(new ZipStreamReader(__is));
	}
	
	/**
	 * Processes the given ZIP file
	 *
	 * @param __zip The ZIP file to read from.
	 * @throws IOException If the ZIP could not be read.
	 * @throws JITException If the JIT could not process the input.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/24
	 */
	public final void process(ZipStreamReader __zip)
		throws IOException, JITException, NullPointerException
	{
		// Check
		if (__zip == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

