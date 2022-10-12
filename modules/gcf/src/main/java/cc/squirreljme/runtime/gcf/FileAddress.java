// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.gcf;

/**
 * This is an address which represents a file.
 *
 * @since 2019/05/06
 */
public final class FileAddress
	implements SocketAddress
{
	/** The file. */
	public final String file;
	
	/**
	 * Initializes the file address.
	 *
	 * @param __p The file path.
	 * @throws IllegalArgumentException If the path is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/06
	 */
	public FileAddress(String __p)
		throws IllegalArgumentException, NullPointerException
	{
		if (__p == null)
			throw new NullPointerException("NARG");
		
		// Decode the path
		this.file = HTTPUtils.stringDecode(HTTPUrlCharacterSet.PATH, __p);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/13
	 */
	@Override
	public final String toString()
	{
		return this.file;
	}
	
	/**
	 * Decodes and build the file address.
	 *
	 * @param __p The string address.
	 * @return The resulting file address.
	 * @throws IllegalArgumentException If the path is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/06
	 */
	public static FileAddress of(String __p)
		throws IllegalArgumentException, NullPointerException
	{
		if (__p == null)
			throw new NullPointerException("NARG");
		
		return new FileAddress(__p);
	}
}

