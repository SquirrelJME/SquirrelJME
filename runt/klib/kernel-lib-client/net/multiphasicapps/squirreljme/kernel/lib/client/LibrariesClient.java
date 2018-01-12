// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel.lib.client;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Map;
import net.multiphasicapps.collections.SortedTreeMap;
import net.multiphasicapps.squirreljme.kernel.packets.Packet;
import net.multiphasicapps.squirreljme.kernel.service.ClientInstance;
import net.multiphasicapps.squirreljme.kernel.service.ServicePacketStream;

/**
 * This class is used to manage libraries as needed by client tasks.
 *
 * @since 2018/01/02
 */
public final class LibrariesClient
	extends ClientInstance
{
	/** Mapping of libraries. */
	private final Map<Integer, Reference<Library>> _libraries =
		new SortedTreeMap<>();
	
	/**
	 * Initializes the library client.
	 *
	 * @param __sps The stream to the server.
	 * @since 2018/01/05
	 */
	public LibrariesClient(ServicePacketStream __sps)
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
	 * {@inheritDoc}
	 * @since 2018/01/05
	 */
	@Override
	protected Packet handlePacket(Packet __p)
		throws NullPointerException
	{
		if (__p == null)
			throw new NullPointerException("NARG");
		
		switch (__p.type())
		{
				// {@squirreljme.error BC03 Unknown packet. (The packet)}
			default:
				throw new IllegalArgumentException(
					String.format("BC03 %s", __p));
		}
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
		int[] indexes;
		Library[] rv;
		
		// Read library indexes from the server
		ServicePacketStream stream = this.stream;
		try (Packet p = stream.farm().create(
			PacketTypes.LIST_PROGRAMS, 4))
		{
			// Just send the mask used to filter programs
			p.writeInteger(0, __mask);
			
			// Send to server
			try (Packet r = stream.send(p))
			{
				// Read count
				int n = r.readUnsignedShort(0);
				indexes = new int[n];
				rv = new Library[n];
				
				// Read in values
				for (int i = 0, q = 2; i < n; i++, q += 4)
					indexes[i] = r.readInteger(q);
			}
		}
		
		// Map libraries
		Map<Integer, Reference<Library>> libraries = this._libraries;
		synchronized (libraries)
		{
			for (int i = 0, n = indexes.length; i < n; i++)
				rv[i] = __mapLibrary(indexes[i]);
		}
		
		return rv;
	}
	
	/**
	 * Maps the index to a cached library.
	 *
	 * @param __dx The library index.
	 * @return The library for the given index.
	 * @since 2018/01/12
	 */
	private final Library __mapLibrary(int __dx)
	{
		Map<Integer, Reference<Library>> libraries = this._libraries;
		synchronized (libraries)
		{
			Integer idx = __dx;
			Reference<Library> ref = libraries.get(idx);
			Library rv;
			
			if (ref == null || null == (rv = ref.get()))
				libraries.put(idx, new WeakReference<>(
					(rv = new __ClientLibrary__(__dx))));
			
			return rv;
		}
	}
}

