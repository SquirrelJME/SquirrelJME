// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.emulator;

import java.io.IOException;

/**
 * This interface is used by emulators to display output to the user which
 * may either be to a graphical device or a console.
 *
 * @since 2016/07/30
 */
public interface DisplayOutput
{
	/**
	 * Writes the given bytes to standard output.
	 *
	 * @param __b The byte array.
	 * @param __o The offset to the start.
	 * @param __l The number of bytes to write.
	 * @throws IndexOutOfBoundsException If the offset and/or length are
	 * negative or exceed the array bounds.
	 * @throws IOException On null arguments.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/30
	 */
	public abstract void stdOut(byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, IOException, NullPointerException;
}

