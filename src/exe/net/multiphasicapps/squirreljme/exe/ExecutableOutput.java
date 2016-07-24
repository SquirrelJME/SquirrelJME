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
public interface ExecutableOutput
{
	/**
	 * Adds a system property to be included in the target binary.
	 *
	 * @param __k The the key.
	 * @param __v The value.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/24
	 */
	public abstract void addSystemProperty(String __k, String __v)
		throws NullPointerException;
	
	/**
	 * This links the input namespaces and their data into a single binary.
	 *
	 * @param __os The stream where the binary is to be placed.
	 * @param __names The namespaces associated with the blobs.
	 * @param __blobs The input streams for reading the blob data.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/24
	 */
	public abstract void linkBinary(OutputStream __os, String[] __names,
		InputStream[] __blobs)
		throws IOException, NullPointerException;
}

