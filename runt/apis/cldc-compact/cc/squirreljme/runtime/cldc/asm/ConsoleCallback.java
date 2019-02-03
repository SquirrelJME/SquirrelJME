// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.asm;

import java.io.IOException;

/**
 * This is the callback used for the console.
 *
 * @since 2019/02/02
 */
public interface ConsoleCallback
{
	/**
	 * This is called when the stream has been closed.
	 *
	 * @return {@code false} if an {@link IOException} occurred.
	 * @since 2019/02/02
	 */
	public abstract boolean close();
	
	/**
	 * This is called when the callback is flushed.
	 *
	 * @return {@code false} if an {@link IOException} occurred.
	 * @since 2019/02/02
	 */
	public abstract boolean flush();
	
	/**
	 * Writes the specified bytes to the output.
	 *
	 * @param __b The bytes to write.
	 * @param __o The offset.
	 * @param __l The length.
	 * @return {@code false} if an {@link IOException} occurred.
	 * @since 2019/02/02
	 */
	public abstract boolean write(byte[] __b, int __o, int __l);
}
