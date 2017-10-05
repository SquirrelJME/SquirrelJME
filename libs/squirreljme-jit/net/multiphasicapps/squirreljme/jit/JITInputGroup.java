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

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import net.multiphasicapps.squirreljme.jit.cff.ClassFile;
import net.multiphasicapps.squirreljme.jit.cff.ClassName;
import net.multiphasicapps.squirreljme.jit.rc.Resource;
import net.multiphasicapps.util.sorted.SortedTreeMap;
import net.multiphasicapps.util.unmodifiable.UnmodifiableMap;

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
}

