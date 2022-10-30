// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator;

import cc.squirreljme.jvm.mle.ReflectionShelf;
import cc.squirreljme.jvm.mle.callbacks.ReflectiveLoaderCallback;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements a class loader which is wrapped for {@link ReflectionShelf}.
 *
 * @since 2022/10/30
 */
public class WrappedClassLoader
	extends ClassLoader
{
	/** The parent class loader. */
	protected final ClassLoader parent;
	
	/** The available reflective loaders. */
	private final List<ReflectiveLoaderCallback> _available =
		new ArrayList<>();
	
	/**
	 * Initializes the wrapped class loader.
	 * 
	 * @param __parent The parent class loader, if any.
	 * @since 2022/10/30
	 */
	public WrappedClassLoader(ClassLoader __parent)
	{
		// Use the parent if specified, but otherwise fallback to the system
		// class loader accordingly
		this.parent = (__parent != null ? __parent :
			ClassLoader.getSystemClassLoader());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/10/30
	 */
	@Override
	protected Class<?> findClass(String __name)
		throws ClassNotFoundException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		// If this is an array, ignore and just forward to the parent
		if (__name.startsWith("["))
			return this.parent.loadClass(__name);
		
		// Get available loaders
		List<ReflectiveLoaderCallback> available = this._available;
		ReflectiveLoaderCallback[] loaders;
		synchronized (this)
		{
			loaders = available.toArray(
				new ReflectiveLoaderCallback[available.size()]);
		}
		
		// Normalize binary name
		String normalizedName = __name.replace('.', '/');
		
		// Load in class with the first found loader
		for (ReflectiveLoaderCallback loader : loaders)
		{
			byte[] classBytes = loader.classBytes(normalizedName);
			if (classBytes != null && classBytes.length > 0)
				return this.defineClass(__name, classBytes, 0,
					classBytes.length);
		}
		
		// Delegate to the parent accordingly
		return this.parent.loadClass(__name);
	}
	
	/**
	 * Registers the loader to be used.
	 * 
	 * @param __cb The callback to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/10/30
	 */
	public void registerLoader(ReflectiveLoaderCallback __cb)
		throws NullPointerException
	{
		if (__cb == null)
			throw new NullPointerException("NARG");
		
		Debugging.debugNote("Register " + __cb);
		
		// Add to the end!
		synchronized (this)
		{
			this._available.add(__cb);
		}
	}
}
