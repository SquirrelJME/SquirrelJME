// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import java.io.DataInputStream;
import java.io.IOException;
import net.multiphasicapps.io.region.SizeLimitedInputStream;
import net.multiphasicapps.squirreljme.jit.base.ClassFormatException;

/**
 * This is the base for anything that requires decoding where that thing to
 * be decoded has atributes.
 *
 * @since 2016/08/27
 */
abstract class __HasAttributes__
{
	/**
	 * Initializes the base.
	 *
	 * @since 2016/08/27
	 */
	__HasAttributes__()
	{
	}
	
	/**
	 * Handles the attribute data.
	 *
	 * @param __name The attribute name.
	 * @param __is The attribute data.
	 * @throws IOException On read errors.
	 * @since 2016/08/27
	 */
	abstract void __handleAttribute(String __name, DataInputStream __is)
		throws IOException;
	
	/**
	 * Handles a single attribute.
	 *
	 * @param __pool The input constant pool.
	 * @param __di The input stream.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/18
	 */
	void __readAttribute(ClassConstantPool __pool, DataInputStream __di)
		throws IOException, NullPointerException
	{
		// Check
		if (__di == null || __pool == null)
			throw new NullPointerException("NARG");
		
		// Read the attribute name and length
		ClassConstantEntry eaname = __pool.get(__di.readUnsignedShort());
		String aname = eaname.get(false, String.class);
		
		// {@squirreljme.error ED19 The length of the attribute exceeds
		// 2GiB.}
		int len = __di.readInt();
		if (len < 0)
			throw new ClassFormatException("ED19");
		
		// Handle it, but do not propogate close to the wrapped stream
		// because any read attribute that gets closed will close the upper
		// input stream.
		try (DataInputStream cis = new DataInputStream(
			new SizeLimitedInputStream(__di, len, true, false)))
		{
			__handleAttribute(aname, cis);
		}
	}
}

