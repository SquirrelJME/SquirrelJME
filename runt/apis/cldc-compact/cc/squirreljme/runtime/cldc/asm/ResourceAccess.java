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

/**
 * This class provides access to resources and their raw data streams.
 *
 * @since 2018/10/07
 */
public final class ResourceAccess
{
	/** Resource does not exist. */
	public static final int OPEN_STATUS_NO_RESOURCE =
		-1;
	
	/** JAR does not exist. */
	public static final int OPEN_STATUS_NO_JAR =
		-2;
	
	/** IOException. */
	public static final int OPEN_STATUS_IOEXCEPTION =
		-3;
	
	/** Read returned EOF. */
	public static final int READ_STATUS_EOF =
		-1;
	
	/** Read returned unknown file descriptor. */
	public static final int READ_STATUS_UNKNOWN_FD =
		-2;
	
	/** Read returned IOException. */
	public static final int READ_STATUS_IOEXCEPTION =
		-3;
	
	/** Close resulted in any IOException. */
	public static final int CLOSE_ANY_IOEXCEPTION =
		-1;
	
	/**
	 * Returns the number of bytes which are known to be available. This is
	 * not required to be supported but is available for usage if it would
	 * result in optimization.
	 *
	 * If this is not supported by a resource then zero or a negative value
	 * may be returned.
	 *
	 * @param __fd The file descriptor to check.
	 * @return The number of available bytes.
	 * @since 2018/10/07
	 */
	public static final native int available(int __fd);
	
	/**
	 * Closes the given resource.
	 *
	 * @param __fd The resource descriptor to close.
	 * @return A negative value indicating the reason for the failure.
	 * @since 2018/10/07
	 */
	public static final native int close(int __fd);
	
	/**
	 * Opens the specified resource in the given JAR, according to the class
	 * the JAR is in.
	 *
	 * @param __jar The JAR the resource is in.
	 * @param __res The name of the resource to load.
	 * @return The file descriptor or a negative value if it does not exist.
	 * If {@code -2} is returned that means there was an exception trying to
	 * load the resource.
	 * @since 2018/10/07
	 */
	public static final native int open(String __jar, String __res);
	
	/**
	 * Reads data from the given resource.
	 *
	 * @param __fd The file descriptor to read from.
	 * @param __b The output byte array.
	 * @param __o The offset.
	 * @param __l The length.
	 * @return The number of bytes read or a negative value if the end of
	 * stream was reached.
	 * @since 2018/10/07
	 */
	public static final native int read(int __fd, byte[] __b, int __o,
		int __l);
	
	/**
	 * Skips the given number of bytes in the resource stream.
	 *
	 * @param __fd The file descriptor to skip in.
	 * @param __n The number of bytes to skip.
	 * @return The number of skipped bytes, this may be zero if no bytes
	 * were skipped.
	 * @since 2018/10/07
	 */
	public static final native int skip(int __fd, int __n);
}

