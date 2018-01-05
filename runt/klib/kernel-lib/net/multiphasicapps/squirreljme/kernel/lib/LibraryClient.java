// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel.lib;

import net.multiphasicapps.squirreljme.kernel.service.ClientInstance;
import net.multiphasicapps.squirreljme.kernel.service.ServicePacketStream;

/**
 * This class is used to manage libraries as needed by client tasks.
 *
 * @since 2018/01/02
 */
public final class LibraryClient
	extends ClientInstance
{
	/**
	 * Initializes the library client.
	 *
	 * @param __sps The stream to the server.
	 * @since 2018/01/05
	 */
	public LibraryClient(ServicePacketStream __sps)
	{
		super(__sps);
	}
	
	/**
	 * Returns the library by the given index.
	 *
	 * @param __id The index of the library to get.
	 * @return The library for the given index.
	 * @throws NoSuchLibraryException If no library exists by that index.
	 * @since 2018/01/03
	 */
	public final Library byIndex(int __id)
		throws NoSuchLibraryException
	{
		throw new todo.TODO();
	}
	
	/**
	 * Installs a JAR file.
	 *
	 * @param __b The Jar file data.
	 * @param __o The offset into the array.
	 * @param __l The length of the JAR.
	 * @return The installation report which indicates success or failure.
	 * @throws ArrayIndexOutOfBoundsException If the offset and/or length are
	 * negative or exceed the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/03
	 */
	public final LibraryInstallationReport install(byte[] __b, int __o,
		int __l)
		throws ArrayIndexOutOfBoundsException, NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new ArrayIndexOutOfBoundsException("IOOB");
		
		throw new todo.TODO();
	}
	
	/**
	 * Lists the libraries which are available for usage.
	 *
	 * @param __mask The mask for the library types.
	 * @return The list of libraries available under the given mask.
	 * @since 2018/01/02
	 */
	public final Library[] list(int __mask)
	{
		throw new todo.TODO();
	}
}

