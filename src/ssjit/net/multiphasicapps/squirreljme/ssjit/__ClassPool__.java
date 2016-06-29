// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.ssjit;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * This represents the constant pool of a class which has been read, this is
 * only temporarily used during the translation of the class.
 *
 * @since 2016/06/29
 */
class __ClassPool__
{
	/**
	 * Decodes the constant pool of an input class file.
	 *
	 * @param __dis The input stream to read for class files.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @throws SSJITException If the constant pool is malformed.
	 * @since 2016/06/29
	 */
	__ClassPool__(DataInputStream __dis)
		throws IOException, NullPointerException, SSJITException
	{
		// Check
		if (__dis == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error DV05 The input class has an empty constant
		// pool.}
		int count = __dis.readUnsignedShort();
		if (count <= 0)
			throw new SSJITException("DV05");
		
		throw new Error("TODO");
	}
}

