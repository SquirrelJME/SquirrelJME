// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jit.input;

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
import net.multiphasicapps.collections.SortedTreeMap;
import net.multiphasicapps.collections.UnmodifiableCollection;
import net.multiphasicapps.collections.UnmodifiableSet;
import cc.squirreljme.jit.classfile.ClassFile;
import cc.squirreljme.jit.classfile.ClassName;
import cc.squirreljme.jit.JITException;
import net.multiphasicapps.zip.streamreader.ZipStreamEntry;
import net.multiphasicapps.zip.streamreader.ZipStreamReader;

/**
 * This class is used to provide class and resource input for the JIT.
 *
 * After initialization, groups are only internally used when it comes to
 * resources and not classes with regards to the input.
 *
 * Note that duplicate classes can exist within groups, however classes which
 * have been added first will always take precedence (those that are in
 * lower groups).
 *
 * @since 2017/09/26
 */
public final class JITInput
{
	/** Input groups. */
	final Map<String, JITInputGroup> _groups =
		new LinkedHashMap<>();
	
	/** Classes which are available. */
	final Map<ClassName, ClassFile> _classes =
		new SortedTreeMap<>();
	
	/** The collection of input classes. */
	private volatile Reference<Collection<ClassFile>> _classfiles;
	
	/**
	 * Initializes the JIT input.
	 *
	 * @param __g The input groups.
	 * @throws JITException If there is a duplicate input group.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/09/26
	 */
	public JITInput(JITInputGroup... __g)
		throws JITException, NullPointerException
	{
		if (__g == null)
			throw new NullPointerException("NARG");
		
		Map<ClassName, ClassFile> classes = this._classes;
		Map<String, JITInputGroup> groups = this._groups;
		
		// Add groups
		for (JITInputGroup g : __g)
		{
			if (g == null)
				throw new NullPointerException("NARG");	
			
			// {@squirreljme.error AJ09 A group with the specified name already
			// is being used as input. (The name of the group)}
			String grpname = g.name();
			if (groups.containsKey(grpname))
				throw new JITException(String.format("AJ09 %s", grpname));
			
			// Add group
			groups.put(grpname, g);
			
			// Check classes
			Map<ClassName, ClassFile> clput = new HashMap<>();
			for (ClassFile c : g.classes().values())
			{
				// Any classes which are duplicated
				ClassName n = c.thisName();
				if (classes.containsKey(n))
					continue;
				
				// Add class
				classes.put(n, c);
			}
		}
	}
	
	/**
	 * Returns the collection of class files which are available.
	 *
	 * @return The collection of class files.
	 * @since 2017/10/09
	 */
	public final Collection<ClassFile> classFiles()
	{
		Reference<Collection<ClassFile>> ref = this._classfiles;
		Collection<ClassFile> rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._classfiles = new WeakReference<>(
				(rv = UnmodifiableCollection.<ClassFile>of(
				this._classes.values())));
		
		return rv;
	}
	
	/**
	 * Returns the specified class which is used as input.
	 *
	 * @param __n The name of the class to get.
	 * @return The class file for that class.
	 * @throws NoSuchClassException If that class does not exist.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/10
	 */
	public final ClassFile getClass(ClassName __n)
		throws NoSuchClassException, NullPointerException
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AJ0a The specified class does not exist within
		// the input. (The name of the class)}
		ClassFile rv = this._classes.get(__n);
		if (rv == null)
			throw new NoSuchClassException(String.format("AJ0a %s", __n));
		return rv;
	}
	
	/**
	 * Returns the input group by the given name.
	 *
	 * @param __g The group to use as input.
	 * @return The input group.
	 * @throws NoSuchGroupException If the group does not exist.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/09
	 */
	public final JITInputGroup getGroup(String __g)
		throws NoSuchGroupException, NullPointerException
	{
		if (__g == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AJ0b The specified group does not exist within
		// the input. (The name of the group)}
		JITInputGroup rv = this._groups.get(__g);
		if (rv == null)
			throw new NoSuchGroupException(String.format("AJ0b %s", __g));
		return rv;
	}
}

