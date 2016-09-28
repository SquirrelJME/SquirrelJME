// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.exe;

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * This is the base interface for all executable outputs that the target
 * builder will use to generate a binary.
 *
 * For simplicity, instances of this class are not required to be reused.
 *
 * All executable inputs are blobs, which may potentially be decoded or not.
 *
 * @since 2016/07/23
 */
@Deprecated
public interface DeprecutableOutput
{
	/**
	 * Adds a system property to be included in the target binary.
	 *
	 * @param __k The the key.
	 * @param __v The value.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/24
	 */
	public abstract void addSystemProperty(String __k, String __v)
		throws IOException, NullPointerException;
	
	/**
	 * Generates the actual binary.
	 *
	 * @param __os The stream to write to.
	 * @throws IllegalStateException If the output stream does not match an
	 * optionally previously specified primed stream.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/15
	 */
	public abstract void generate(OutputStream __os)
		throws IllegalStateException, IOException, NullPointerException;
	
	/**
	 * Inserts the namespace into the specified output.
	 *
	 * @param __name The name of the namespace.
	 * @param __data The input namespace data.
	 * @return An executable output dependent object that represents the
	 * namespace or {@code null} if it is not required.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/15
	 */
	public abstract Object insertNamespace(String __name, InputStream __data)
		throws IOException, NullPointerException;
	
	/**
	 * This primes the given stream for output for the given binary, generally
	 * this should always be called first with the specified output stream in
	 * the event the writer is capable of streamed output. If the writer does
	 * not need to write to the stream as soon as possible then this method
	 * can do nothing.
	 *
	 * @param __os The stream to prime.
	 * @throws IllegalStateException If the output has already been primed.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/15
	 */
	public abstract void primeOutput(OutputStream __os)
		throws IllegalStateException, IOException, NullPointerException;
}

