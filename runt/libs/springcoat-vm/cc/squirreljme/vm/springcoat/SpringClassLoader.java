// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.vm.VMClassLibrary;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import net.multiphasicapps.classfile.ClassFile;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.InvalidClassFormatException;

/**
 * This class acts as the equivalent to {@code ClassLoader} in that it manages
 * the class path and the eventual loading of classes.
 *
 * @since 2018/09/01
 */
public final class SpringClassLoader
{
	/** Class loading lock. */
	protected final Object loaderlock =
		new Object();
	
	/** The class path for the machine. */
	private final VMClassLibrary[] _classpath;
	
	/** The classes which have been loaded by the virtual machine. */
	private final Map<ClassName, SpringClass> _classes =
		new HashMap<>();
	
	/** Next special class index. */
	private int _nexcsi =
		1;
	
	/**
	 * Initializes the class loader.
	 *
	 * @param __classpath The classpath.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/01
	 */
	public SpringClassLoader(VMClassLibrary... __classpath)
		throws NullPointerException
	{
		for (VMClassLibrary b : __classpath = (__classpath == null ?
			new VMClassLibrary[0] : __classpath.clone()))
			if (b == null)
				throw new NullPointerException("NARG");
		this._classpath = __classpath;
	}
	
	/**
	 * Returns the library that is used for booting, the main entry JAR.
	 *
	 * @return The boot library.
	 * @since 2018/09/13
	 */
	public final VMClassLibrary bootLibrary()
	{
		VMClassLibrary[] classpath = this._classpath;
		return classpath[classpath.length - 1];
	}
	
	/**
	 * Returns the class loading lock.
	 *
	 * @return The class loading lock.
	 * @since 2018/09/08
	 */
	public final Object classLoadingLock()
	{
		return this.loaderlock;
	}
	
	/**
	 * Returns the class path.
	 *
	 * @return The classpath.
	 * @since 2018/12/06
	 */
	public final VMClassLibrary[] classPath()
	{
		return this._classpath.clone();
	}
	
	/**
	 * Finds the specified library.
	 *
	 * @param __n The library to find.
	 * @return The given library or {@code null} if it was not found.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/10/07
	 */
	public final VMClassLibrary findLibrary(String __n)
		throws NullPointerException
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		for (VMClassLibrary lib : this._classpath)
			if (__n.equals(lib.name()))
				return lib;
		
		return null;
	}
	
	/**
	 * Loads the specified class.
	 *
	 * @param __cn The name of the class to load.
	 * @return The loaded class.
	 * @throws NullPointerException On null arguments.
	 * @throws SpringClassFormatException If the class is not formatted
	 * properly.
	 * @throws SpringClassNotFoundException If the class was not found.
	 * @since 2018/09/01
	 */
	public final SpringClass loadClass(ClassName __cn)
		throws NullPointerException, SpringClassFormatException,
			SpringClassNotFoundException
	{
		if (__cn == null)
			throw new NullPointerException("NARG");
		
		// Lock on classes
		Map<ClassName, SpringClass> classes = this._classes;
		synchronized (this.loaderlock)
		{
			// If the class has already been initialized, use that
			SpringClass rv = classes.get(__cn);
			if (rv != null)
				return rv;
			
			// Debug
			/*todo.DEBUG.note("Loading class `%s`...", __cn);*/
			
			// Load class file for this class
			ClassFile cf;
			String[] injar = new String[1];
			try
			{
				cf = this.loadClassFile(__cn, injar);
			}
			catch (InvalidClassFormatException e)
			{
				// {@squirreljme.error BK0n Could not load class. (The class
				// to load)}
				throw new InvalidClassFormatException(
					String.format("BK0n %s", __cn), e);
			}
			
			// Load the super class
			ClassName supername = cf.superName();
			SpringClass superclass = (supername == null ? null :
				this.loadClass(supername));
			
			// Load any interfaces
			ClassName[] interfacenames = cf.interfaceNames();
			int numinterfaces = interfacenames.length;
			SpringClass[] interfaceclasses = new SpringClass[numinterfaces];
			for (int i = 0; i < numinterfaces; i++)
				interfaceclasses[i] = this.loadClass(interfacenames[i]);
			
			// Component?
			SpringClass component = null;
			if (__cn.isArray())
				component = this.loadClass(__cn.componentType());
			
			// Load class information
			rv = new SpringClass(superclass, interfaceclasses, cf,
				this._nexcsi++, component, injar[0]);
			
			// Store for later use
			classes.put(__cn, rv);
			
			return rv;
		}
	}
	
	/**
	 * This goes through the classpath and loads the specified class file for
	 * the given class.
	 *
	 * @param __cn The class to load.
	 * @param __ij The input JAR file for the class.
	 * @return The loaded class file data.
	 * @throws NullPointerException On null arguments.
	 * @throws SpringClassFormatException If the class is not formatted
	 * properly.
	 * @throws SpringClassNotFoundException If the class was not found.
	 * @since 2018/09/01
	 */
	public final ClassFile loadClassFile(ClassName __cn, String[] __ij)
		throws NullPointerException, SpringClassFormatException,
			SpringClassNotFoundException
	{
		if (__cn == null)
			throw new NullPointerException("NARG");
		
		// Debug
		/*todo.DEBUG.note("Loading class file `%s`...", __cn);*/
		
		// If this is an array type use virtual class representation
		if (__cn.isPrimitive() || __cn.isArray())
			return ClassFile.special(__cn.field());
		
		// This is the class that is read, in binary form
		String fileform = __cn.toString() + ".class";
		
		// Otherwise we need to go through every single binary to find
		// the class we want, which can take awhile
		byte[] data = null;
		for (VMClassLibrary b : this._classpath)
			try (InputStream in = b.resourceAsStream(fileform))
			{
				// Class or file does not exist
				if (in == null)
					continue;
				
				// Read in the data
				byte[] buf = new byte[512];
				try (ByteArrayOutputStream baos =
					new ByteArrayOutputStream(1024))
				{
					for (;;)
					{
						int rc = in.read(buf);
						
						if (rc < 0)
						{
							baos.flush();
							data = baos.toByteArray();
							break;
						}
						
						baos.write(buf, 0, rc);
					}
				}
				
				// Record the binary
				if (__ij != null && __ij.length > 0)
					__ij[0] = b.name();
				
				break;
			}
			catch (IOException e)
			{
				// {@squirreljme.error BK0o Failed to read from the class
				// path.}
				throw new SpringException("BK0o", e);
			}
		
		// {@squirreljme.error BK0p Could not locate the specified class.
		// (The class which was not found; The class file which was
		// attempted to be located)}
		if (data == null)
			throw new SpringClassNotFoundException(__cn, String.format(
				"BK0p %s %s", __cn, fileform));
		
		// Decode class file
		ClassFile cf;
		try (ByteArrayInputStream bais = new ByteArrayInputStream(data))
		{
			return ClassFile.decode(bais);
		}
		catch (IOException e)
		{
			// {@squirreljme.error BK0q Could not read from the source
			// class file. (The class being read)}
			throw new SpringVirtualMachineException(String.format(
				"BK0q %s", __cn), e);
		}
		catch (InvalidClassFormatException e)
		{
			// {@squirreljme.error BK0r The class is not formatted
			// correctly. (The class being read)}
			throw new SpringClassFormatException(__cn, String.format(
				"BK0r %s", __cn), e);
		}
	}
}

