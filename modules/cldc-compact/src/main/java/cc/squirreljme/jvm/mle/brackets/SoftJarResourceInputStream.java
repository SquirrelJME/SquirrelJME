// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.brackets;

import java.io.Closeable;

/**
 * An input stream for reading resources within {@link SoftJarWrapper}.
 *
 * @since 2022/02/27
 */
public interface SoftJarResourceInputStream
	extends Closeable
{
	/** End of file reached. */
	int EOF =
		-1;
	
	/** I/O Exception. */
	int IO_EXCEPTION =
		-2;
	
	/**
	 * Reads the given number of bytes from the input stream.
	 * 
	 * @param __b The number of bytes to read.
	 * @param __o The offset into the buffer.
	 * @param __l The number of bytes to read.
	 * @return The number of bytes read or a special code for specific
	 * circumstances.
	 * @since 2022/02/27
	 */
	int read(byte[] __b, int __o, int __l);
}
