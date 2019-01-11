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
import cc.squirreljme.vm.VMSuiteManager;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.scrf.RegisterClass;

/**
 * This is a class loader which manages and can cache multiple classes.
 *
 * @since 2019/01/05
 */
public final class ClassLoader
{
	/** The class cache. */
	protected final RegisterClassCache scrfcache;
	
	/** The suite manager. */
	protected final VMSuiteManager suites;
	
	/** Loaded class cache. */
	private final Map<ClassName, LoadedClass> _classes =
		new HashMap<>();
	
	/** The classes in the class path. */
	private final VMClassLibrary[] _classpath;
	
	/**
	 * Initializes the class loader.
	 *
	 * @param __rcc The SCRF class cache.
	 * @param __sm The suite manager.
	 * @param __cp The classpath.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/01/05
	 */
	public ClassLoader(RegisterClassCache __rcc, VMSuiteManager __sm,
		VMClassLibrary[] __cp)
		throws NullPointerException
	{
		if (__rcc == null || __sm == null || __cp == null)
			throw new NullPointerException("NARG");
		
		this.scrfcache = __rcc;
		this.suites = __sm;
		this._classpath = __cp.clone();
	}
	
	/**
	 * Loads the given class.
	 *
	 * @param __n The class to load.
	 * @return The loaded class.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/01/06
	 */
	public final LoadedClass loadClass(String __n)
		throws NullPointerException
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
	 * @since 2019/01/10
	 */
	public final LoadedClass loadClass(ClassName __n)
		throws NullPointerException
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

