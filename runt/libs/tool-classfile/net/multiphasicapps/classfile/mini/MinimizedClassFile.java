// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile.mini;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import net.multiphasicapps.classfile.InvalidClassFormatException;

/**
 * This contains the minimized class file which is a smaller format of the
 * {@link net.multiphasicapps.classfile.ClassFile} that is more optimized to
 * virtual machines for usage.
 *
 * @since 2019/03/10
 */
public final class MinimizedClassFile
{
	/** The magic number for the files. */
	public static final int MAGIC_NUMBER =
		0x00586572;
	
	/**
	 * Decodes and returns the minimized representation of the class file.
	 *
	 * @param __is The stream to decode from.
	 * @return The resulting minimized class.
	 * @throws InvalidClassFormatException If the class is not formatted
	 * correctly.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/10
	 */
	public static final MinimizedClassFile decode(InputStream __is)
		throws InvalidClassFormatException, IOException, NullPointerException
	{
		if (__is == null)
			throw new NullPointerException("NARG");
		
		// Wrap stream for reading
		DataInputStream dis = new DataInputStream(__is);
		
		// {@squirreljme.error JC2l Invalid minimized class magic number.}
		if (MAGIC_NUMBER != dis.readInt())
			throw new InvalidClassFormatException("JC2l");
		
		throw new todo.TODO();
	}
}

