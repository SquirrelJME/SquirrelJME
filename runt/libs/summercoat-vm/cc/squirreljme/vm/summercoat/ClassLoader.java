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
import net.multiphasicapps.classfile.InvalidClassFormatException;
import net.multiphasicapps.classfile.ClassFile;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.scrf.compiler.ClassProcessException;
import net.multiphasicapps.scrf.compiler.ClassProcessor;
import net.multiphasicapps.scrf.RegisterClass;
import net.multiphasicapps.scrf.SummerClass;

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
			
			// If this is a primitive type or array then it is a special thing
			// and will not be in a resource
			ClassFile cf;
			VMClassLibrary inlib;
			if (__n.isPrimitive() || __n.isArray())
			{
				cf = ClassFile.special(__n.field());
				inlib = null;
			}
			
			// Load class from resource instead
			else
			{
				// This is the class that is read, in binary form
				String fileform = __n.toString() + ".class";
				
				// Go through our classpath looking for this class in its
				// resource format
				cf = null;
				inlib = null;
				for (VMClassLibrary b : this._classpath)
					try (InputStream in = b.resourceAsStream(fileform))
					{
						// Not found
						if (in == null)
							continue;
						
						// Load it
						cf = ClassFile.decode(in);
						inlib = b;
						
						// Stop
						break;
					}
					catch (InvalidClassFormatException e)
					{
						// {@squirreljme.error AE05 The class is not formatted
						// correctly. (The class name)}
						throw new VMException("AE05 " + __n, e);
					}
					catch (IOException e)
					{
						// {@squirreljme.error AE04 Read error trying to read
						// the class file. (The class)}
						throw new VMException("AE04 " + __n, e);
					}
				
				// {@squirreljme.error AE03 Could not find the specified class.
				// (The name of the class)}
				if (cf == null || inlib == null)
					throw new VMClassNotFoundException("AE03 " + __n);
			}
			
			// Process the class and compile it to the register format
			SummerClass rc;
			try
			{
				rc = SummerClass.ofClassFile(cf);
			}
			catch (ClassProcessException e)
			{
				// {@squirreljme.error AE06 The register compiler could not
				// process the input class. (The class)}
				throw new VMException("AE06 " + __n, e);
			}
			
			throw new todo.TODO();
		}
	}
}

