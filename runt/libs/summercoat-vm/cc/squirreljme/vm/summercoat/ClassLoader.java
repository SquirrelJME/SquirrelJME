// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.summercoat;

import cc.squirreljme.vm.VMClassLibrary;
import cc.squirreljme.vm.VMException;
import cc.squirreljme.vm.VMSuiteManager;
import java.io.InputStream;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import net.multiphasicapps.classfile.ClassFile;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.InvalidClassFormatException;
import net.multiphasicapps.classfile.mini.MinimizedClassFile;
import net.multiphasicapps.classfile.mini.Minimizer;

/**
 * This is a class loader which manages and can cache multiple classes.
 *
 * @since 2019/01/05
 */
public final class ClassLoader
{
	/** The suite manager. */
	protected final CachingSuiteManager suites;
	
	/** Loaded class cache. */
	private final Map<ClassName, LoadedClass> _classes =
		new HashMap<>();
	
	/** The classes in the class path. */
	private final CachingClassLibrary[] _classpath;
	
	/**
	 * Initializes the class loader.
	 *
	 * @param __sm The suite manager.
	 * @param __cp The classpath.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/01/05
	 */
	public ClassLoader(CachingSuiteManager __sm, CachingClassLibrary[] __cp)
		throws NullPointerException
	{
		if (__sm == null || __cp == null)
			throw new NullPointerException("NARG");
		
		this.suites = __sm;
		this._classpath = __cp.clone();
	}
	
	/**
	 * Loads the given class.
	 *
	 * @param __n The class to load.
	 * @return The loaded class.
	 * @throws NullPointerException On null arguments.
	 * @throws VMClassNotFoundException If the class was not found.
	 * @since 2019/01/06
	 */
	public final LoadedClass loadClass(String __n)
		throws NullPointerException, VMClassNotFoundException
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		return this.loadClass(new ClassName(__n));
	}
	
	/**
	 * Loads the given class.
	 *
	 * @param __n The class to load.
	 * @return The loaded class.
	 * @throws NullPointerException On null arguments.
	 * @throws VMClassNotFoundException If the class was not found.
	 * @since 2019/01/10
	 */
	public final LoadedClass loadClass(ClassName __n)
		throws NullPointerException, VMClassNotFoundException
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// Lock on self as new classes are loaded
		synchronized (this)
		{
			// If the class has already been loaded use that one
			Map<ClassName, LoadedClass> classes = this._classes;
			LoadedClass rv = classes.get(__n);
			if (rv != null)
				return rv;
			
			// Resulting minimized class and the library it is within
			MinimizedClassFile cf;
			CachingClassLibrary inlib;
			
			// If this is a primitive type or array then it is a special thing
			// and will not be in a resource
			if (__n.isPrimitive() || __n.isArray())
			{
				// Just minimize these dynamically generated classes
				cf = Minimizer.minimize(ClassFile.special(__n.field()));
				inlib = null;
			}
			
			// Load class from resource instead
			else
			{
				// Nothing is found initially yet
				cf = null;
				inlib = null;
				
				// The name to check, multiple extensions will be used!
				String basename = __n.toString();
				
				// Go through each library and check cache for the class data
				for (CachingClassLibrary b : this._classpath)
					if (null != (cf = b.cacheClass(basename)))
					{
						inlib = b;
						break;
					}
				
				// {@squirreljme.error AE03 Could not find the specified class.
				// (The name of the class)}
				if (cf == null || inlib == null)
					throw new VMClassNotFoundException("AE03 " + __n);
			}
			
			throw new todo.TODO();
		}
	}
}

