// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.nanocoat;

import cc.squirreljme.emulator.vm.VMException;
import cc.squirreljme.emulator.vm.VMSuiteManager;
import cc.squirreljme.jvm.mle.JarPackageShelf;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.vm.VMClassLibrary;

/**
 * Provides virtual access to a suite for {@link JarPackageShelf}, this acts
 * as a wrapper around {@link VMSuiteManager}.
 *
 * @since 2023/12/12
 */
public final class VirtualSuite
	implements Pointer
{
	/** The suite manager. */
	protected final VMSuiteManager manager;
	
	/** The memory pool this is allocated within. */
	protected final AllocPool pool;
	
	/** The raw suite access. */
	private final AllocLink _suite;
	
	/** Cached libraries list? */
	private volatile PointerFlatList<VirtualLibrary> _libraries;
	
	static
	{
		__Native__.__loadLibrary();
	}
	
	/**
	 * Initializes the virtual suite manager.
	 *
	 * @param __pool The memory pool to get from.
	 * @param __suiteManager The actual suite manager to wrap.
	 * @throws NullPointerException On null arguments.
	 * @throws VMException If it could not be initialized.
	 * @since 2023/12/13
	 */
	public VirtualSuite(AllocPool __pool,
		VMSuiteManager __suiteManager)
		throws NullPointerException, VMException
	{
		if (__pool == null || __suiteManager == null)
			throw new NullPointerException("NARG");
		
		// Store needed fields
		this.manager = __suiteManager;
		this.pool = __pool;
		
		// Initialize suite
		this._suite = AllocLink.ofBlockPtr(
			this.__init(__pool.pointerAddress(), this));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/12/13
	 */
	@Override
	public long pointerAddress()
	{
		return this._suite.pointerAddress();
	}
	
	/**
	 * Calculates the list of libraries.
	 *
	 * @return Pointer to a {@code sjme_list_sjme_rom_library}.
	 * @since 2023/12/23
	 */
	@SuppressWarnings("unused")
	private long __list()
	{
		Debugging.debugNote("Listing libraries...");
		
		synchronized (this)
		{
			// Was this determined on a previous run already?
			PointerFlatList<VirtualLibrary> result = this._libraries;
			if (result != null)
				return result.pointerAddress();
			
			// Get the names of libraries that are available.
			VMSuiteManager manager = this.manager;
			AllocPool pool = this.pool;
			String[] libNames = manager.listLibraryNames();
			
			// Load every single one, to make it easier to use.
			int numLibs = libNames.length;
			VirtualLibrary[] virtualLibs = new VirtualLibrary[numLibs];
			for (int i = 0; i < numLibs; i++)
			{
				VMClassLibrary lib = manager.loadLibrary(
					libNames[i]);
				virtualLibs[i] = new VirtualLibrary(lib,
					manager.libraryId(lib), this);
			}
			
			// Debug
			Debugging.debugNote("Found %d libraries...", numLibs);
			
			// Store libraries and give it back to NanoCoat
			result = FlatList.fromArray(pool, virtualLibs);
			this._libraries = result;
			return result.pointerAddress();
		}
	}
	
	/**
	 * Initializes the virtual suite manager native code.
	 *
	 * @param __poolPtr The pool pointer.
	 * @param __this This current class, used as context.
	 * @return The resultant suite pointer.
	 * @throws VMException If it could not be initialized.
	 * @since 2023/12/14
	 */
	private native long __init(long __poolPtr, VirtualSuite __this)
		throws VMException;
}
