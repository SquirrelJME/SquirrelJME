// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.kernel.lib.client;

import cc.squirreljme.runtime.cldc.library.Library;
import cc.squirreljme.runtime.cldc.library.LibraryResourceScope;
import cc.squirreljme.runtime.cldc.library.LibraryType;
import cc.squirreljme.runtime.cldc.library.NoSuchLibraryException;
import cc.squirreljme.runtime.cldc.service.ServiceCaller;
import cc.squirreljme.runtime.cldc.system.type.IntegerArray;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Map;
import net.multiphasicapps.collections.SortedTreeMap;

/**
 * This class is used to manage libraries as needed by client tasks.
 *
 * @since 2018/01/02
 */
public final class LibrariesClient
{
	/** The caller for the library server. */
	protected final ServiceCaller caller;
	
	/** Mapping of libraries. */
	private final Map<Integer, Reference<Library>> _libraries =
		new SortedTreeMap<>();
	
	/**
	 * Initializes the library client.
	 *
	 * @param __sc The service caller to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/05
	 */
	public LibrariesClient(ServiceCaller __sc)
		throws NullPointerException
	{
		if (__sc == null)
			throw new NullPointerException("NARG");
		
		this.caller = __sc;
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
		
		// Send entire program to the server
		int result = this.caller.integerCall(LibrariesFunction.INSTALL_PROGRAM,
			__b, __o, __l);
		
		// {@squirreljme.error AV01 Failed to install the library.
		// (Result code)}
		if (result < 0)
			return new LibraryInstallationReport(result,
				String.format("AV01 %d", result));
		
		// Success!
		else
			return new LibraryInstallationReport(this.__mapLibrary(result));
	}
	
	/**
	 * Lists the libraries which are available for usage.
	 *
	 * @param __mask The mask for the library types.
	 * @return The list of libraries available under the given mask.
	 * @since 2018/01/02
	 */
	public final Library[] list(LibraryType __mask)
	{
		// Request library indexes
		IntegerArray dxrv = this.caller.integerArrayCall(
			LibrariesFunction.LIST_PROGRAMS, __mask);
		
		// Setup return value
		int n = dxrv.length();
		Library[] rv = new Library[n];
		
		// Map all libraries
		synchronized (this._libraries)
		{
			for (int i = 0; i < n; i++)
				rv[i] = this.__mapLibrary(dxrv.get(i));
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
					(rv = new __ClientLibrary__(__dx, this.caller))));
			
			return rv;
		}
	}
}

