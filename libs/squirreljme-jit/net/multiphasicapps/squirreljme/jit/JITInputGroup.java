// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import java.io.InputStream;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import net.multiphasicapps.squirreljme.jit.cff.ClassFile;
import net.multiphasicapps.squirreljme.jit.cff.ClassName;
import net.multiphasicapps.squirreljme.jit.rc.NoSuchResourceException;
import net.multiphasicapps.squirreljme.jit.rc.Resource;
import net.multiphasicapps.collections.SortedTreeMap;
import net.multiphasicapps.collections.UnmodifiableMap;
import net.multiphasicapps.zip.streamreader.ZipStreamEntry;
import net.multiphasicapps.zip.streamreader.ZipStreamReader;

/**
 * This represents a group of resources and classes together as a single
 * unit.
 *
 * @since 2017/09/27
 */
public final class JITInputGroup
{
	/** The name of this group. */
	protected final String name;
	
	/** Resources within this group. */
	private final Map<String, Resource> _resources =
		new SortedTreeMap<>();
	
	/** Classes within this group. */
	private final Map<ClassName, ClassFile> _classes =
		new SortedTreeMap<>();
	
	/** Unmodifiable class view. */
	private volatile Reference<Map<ClassName, ClassFile>> _roclasses;
	
	/**
	 * This initializes an input group from the given collections.
	 *
	 * @param __n The name of the group.
	 * @param __trc The resources in the group.
	 * @param __tcl The classes in the group.
	 * @throws JITException If there are duplicate resources or classes.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/09/27
	 */
	JITInputGroup(String __n, Collection<Resource> __trc,
		Collection<ClassFile> __tcl)
		throws JITException, NullPointerException
	{
		if (__n == null || __trc == null || __tcl == null)
			throw new NullPointerException("NARG");
		
		// This is trivial and requires no complex work
		this.name = __n;
		
		// Fill in classes
		Map<ClassName, ClassFile> classes = this._classes;
		for (ClassFile c : __tcl)
		{
			// {@squirreljme.error JI2z A duplicate class exists within the
			// input group. (The name of the class)}
			ClassName n = c.thisName();
			if (classes.containsKey(n))
				throw new JITException(String.format("JI2z %s", n));
			
			classes.put(n, c);
		}
		
		// Fill in resources
		Map<String, Resource> resource = this._resources;
		for (Resource r : __trc)
		{
			// {@squirreljme.error JI30 A duplicate resource exists within the
			// input group. (The name of the resource)}
			String n = r.name();
			if (resource.containsKey(n))
				throw new JITException(String.format("JI30 %s", n));
			
			resource.put(n, r);
		}
	}
	
	/**
	 * Returns the classes map.
	 *
	 * @return The classes map.
	 * @since 2017/10/03
	 */
	public final Map<ClassName, ClassFile> classes()
	{
		Reference<Map<ClassName, ClassFile>> ref = this._roclasses;
		Map<ClassName, ClassFile> rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._roclasses = new WeakReference<>(
				rv = UnmodifiableMap.<ClassName, ClassFile>of(this._classes));
		
		return rv;
	}
	
	/**
	 * Loads the resource with the specified name.
	 *
	 * @param __f The name of the resource to load.
	 * @return The input stream to the resource data.
	 * @throws NoSuchResourceException If the resource does not exist.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/01/05
	 */
	public final InputStream loadResource(String __f)
		throws NoSuchResourceException, NullPointerException
	{
		if (__f == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error JI33 No such resource exists. (The name of
		// the resource)}
		Map<String, Resource> resources = this._resources;
		Resource rc = resources.get(__f);
		if (rc == null)
			throw new NoSuchResourceException(String.format("JI33 %s", __f));
		
		// Load it
		return rc.load();
	}
	
	/**
	 * Returns the name of the group.
	 *
	 * @return The group name.
	 * @since 2017/10/09
	 */
	public final String name()
	{
		return this.name;
	}
	
	/**
	 * Reads the specified ZIP file and appends it to the input for the JIT.
	 *
	 * @param __gn The group name of the ZIP.
	 * @param __in The input stream to be treated as a ZIP file for input.
	 * @return {@code this}.
	 * @throws IOException On read errors.
	 * @throws JITException If the input is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/09/26
	 */
	public static JITInputGroup readZip(String __n, InputStream __in)
		throws IOException, JITException, NullPointerException
	{
		return readZip(new NullProgressNotifier(), __n,
			new ZipStreamReader(__in));
	}
	
	/**
	 * Reads the specified ZIP file and appends it to the input for the JIT.
	 *
	 * @param __pn The notifier for progress on parsing of input groups.
	 * @param __gn The group name of the ZIP.
	 * @param __in The input stream to be treated as a ZIP file for input.
	 * @return {@code this}.
	 * @throws IOException On read errors.
	 * @throws JITException If the input is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/09/26
	 */
	public static JITInputGroup readZip(JITProgressNotifier __pn, String __n,
		InputStream __in)
		throws IOException, JITException, NullPointerException
	{
		return readZip(__pn, __n, new ZipStreamReader(__in));
	}
	
	/**
	 * Reads the specified ZIP file and appends it to the input for the JIT.
	 *
	 * @param __n The group name of the ZIP.
	 * @param __in The ZIP file to read from for input.
	 * @return {@code this}.
	 * @throws IOException On read errors.
	 * @throws JITException If the input is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/09/26
	 */
	public static JITInputGroup readZip(String __n, ZipStreamReader __in)
		throws IOException, JITException, NullPointerException
	{
		return readZip(new NullProgressNotifier(), __n, __in);
	}
	
	/**
	 * Reads the specified ZIP file and appends it to the input for the JIT.
	 *
	 * @param __pn The notifier for progress on parsing of input groups.
	 * @param __n The group name of the ZIP.
	 * @param __in The ZIP file to read from for input.
	 * @return {@code this}.
	 * @throws IOException On read errors.
	 * @throws JITException If the input is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/09/26
	 */
	public static JITInputGroup readZip(JITProgressNotifier __pn, String __n,
		ZipStreamReader __in)
		throws IOException, JITException, NullPointerException
	{
		if (__pn == null || __n == null || __in == null)
			throw new NullPointerException("NARG");
		
		// Log processing
		__pn = new CatchingProgressNotifier(__pn);
		__pn.beginJar(__n);
		
		// Target class and resources for groups
		List<ClassFile> tcl = new LinkedList<>();
		List<Resource> trc = new LinkedList<>();
		
		// Process entries
		long start = System.nanoTime();
		int numrc = 0, numcl = 0;
		try
		{
			for (;;)
				try (ZipStreamEntry e = __in.nextEntry())
				{
					// No more input
					if (e == null)
						break;
				
					// Compiling a class?
					String name = e.name();
					if (name.endsWith(".class"))
					{
						__pn.processClass(__n, name, ++numcl);
						tcl.add(ClassFile.decode(__n, e));
					}
				
					// Appending a resource
					else
					{
						__pn.processResource(__n, name, ++numrc);
						trc.add(Resource.read(__n, name, e));
					}
				}
		}
		
		// Count duration
		finally
		{
			__pn.endJar(__n, System.nanoTime() - start, numrc, numcl);
		}
		
		// Setup group
		return new JITInputGroup(__n, trc, tcl);
	}
}

