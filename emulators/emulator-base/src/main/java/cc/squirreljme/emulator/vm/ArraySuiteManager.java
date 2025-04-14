// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.vm;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.vm.VMClassLibrary;
import java.nio.file.Path;
import java.util.Collection;
import java.util.LinkedList;

/**
 * This is a suite manager which is just a group of already specified
 * libraries.
 *
 * @since 2020/02/29
 */
public final class ArraySuiteManager
	implements VMSuiteManager
{
	/** Local libraries. */
	private final VMClassLibrary[] _libraries;
	
	/**
	 * Initializes the library manager.
	 *
	 * @param __libs Libraries to use.
	 * @since 2020/02/29
	 */
	public ArraySuiteManager(VMClassLibrary... __libs)
	{
		this._libraries = (__libs == null ? new VMClassLibrary[0] :
			__libs.clone());
			
		for (VMClassLibrary lib : this._libraries)
			if (lib == null)
				throw new NullPointerException("NARG");
	}
	
	/**
	 * Initializes the library manager.
	 *
	 * @param __libs Libraries to use.
	 * @since 2020/02/29
	 */
	public ArraySuiteManager(Iterable<VMClassLibrary> __libs)
	{
		Collection<VMClassLibrary> copy = new LinkedList<>();
		for (VMClassLibrary lib : __libs)
			copy.add(lib);
		
		this._libraries = copy.<VMClassLibrary>toArray(
			new VMClassLibrary[copy.size()]);
		
		for (VMClassLibrary lib : this._libraries)
			if (lib == null)
				throw new NullPointerException("NARG");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/12/18
	 */
	@Override
	public int libraryId(VMClassLibrary __lib)
		throws IllegalArgumentException, NullPointerException
	{
		if (__lib == null)
			throw new NullPointerException("NARG");
		
		// Map libraries to linear IDs
		VMClassLibrary[] libraries = this._libraries;
		for (int i = 0, n = libraries.length; i < n; i++)
			if (libraries[i] == __lib)
				return 1 + i;
		
		throw new IllegalArgumentException("Unknown library: " + __lib);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/02/29
	 */
	@Override
	public String[] listLibraryNames()
	{
		VMClassLibrary[] libraries = this._libraries;
		int n = libraries.length;
		
		String[] rv = new String[n];
		for (int i = 0; i < n; i++)
			rv[i] = libraries[i].name();
		
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/02/29
	 */
	@Override
	public VMClassLibrary loadLibrary(String __s)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		for (VMClassLibrary lib : this._libraries)
		{
			// There may be a path
			Path path = lib.path();
			
			if (__s.equals(lib.name()) ||
				(path != null && __s.equals(path.toString())))
				return lib;
		}
		
		return null;
	}
}
