// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang;

/**
 * This interface is used with the classes which are initialized in the
 * {@code try-with-resources} block, after the entire block has been finished
 * the {@link #close()} method is called automatically. This makes it much
 * simpler to free resources and no longer requires a {@code finally} with a
 * local variable mess to handle closing of streams.
 *
 * {@code
 *
 * try (FileInputStream fis = new FileInputStream("foo"))
 * {
 *     // fis is VISIBLE here.
 *     // Do stuff with file.
 * }
 * catch (IOException ioe)
 * {
 *     // fis is NOT VISIBLE here.
 *     // Handle exception or rethrow as needed
 * }
 * finally
 * {
 *     // fis is NOT VISIBLE here.
 *     // Other things to do regardless of success or an exception.
 * }
 * // The variable fis is NOT VISIBLE here and when this point of code has
 * // been reached, fis.close() would have been called.
 * 
 * }
 *
 * @see Closeable
 * @since 2015/03/23
 */
public interface AutoCloseable
{
	/**
	 * This releases all (or most) of the resources associated with
	 * an implementing class. When used with a try-with-resources block, this
	 * is automatically called after the scope of block has been left (it is
	 * executed after {@code finally}).
	 *
	 * It is recommended that a resource is actually closed (or at least
	 * marked as such) before an exception is thrown.
	 *
	 * Unlike {@link java.io.Closeable#close()} (and provided the class does
	 * not extend {@link java.io.Closeable}), calling this multiple times may
	 * produce side effects rather than doing nothing on a closed resource.
	 * However, it is stronly recommended and encouraged to follow the "do
	 * nothing when closed" behavior of {@link java.io.Closeable}.
	 *
	 * @throws Exception If there was an error closing the specified object.
	 * @since 2015/03/23
	 */
	public abstract void close()
		throws Exception;
}

