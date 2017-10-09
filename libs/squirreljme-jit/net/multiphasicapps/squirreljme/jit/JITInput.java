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
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import net.multiphasicapps.squirreljme.jit.cff.ClassFile;
import net.multiphasicapps.squirreljme.jit.cff.ClassName;
import net.multiphasicapps.squirreljme.jit.rc.Resource;
import net.multiphasicapps.util.sorted.SortedTreeMap;
import net.multiphasicapps.zip.streamreader.ZipStreamEntry;
import net.multiphasicapps.zip.streamreader.ZipStreamReader;

/**
 * This class is used to provide class and resource input for the JIT.
 *
 * This class is thread safe.
 *
 * @since 2017/09/26
 */
public final class JITInput
{
	/** Lock for the input. */
	final Object _lock =
		new Object();
	
	/** The progress notifier to use. */
	protected final JITProgressNotifier notifier;
	
	/** Input groups. */
	final Map<String, JITInputGroup> _groups =
		new LinkedHashMap<>();
	
	/** Classes which are available. */
	final Map<ClassName, ClassFile> _classes =
		new SortedTreeMap<>();
	
	/**
	 * Initializes the JIT input.
	 *
	 * @since 2017/09/26
	 */
	public JITInput()
	{
		this.notifier = new NullProgressNotifier();
	}
	
	/**
	 * Initializes the JIT input with the given progress notifier.
	 *
	 * @param __pn The notifier for progress.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/09/26
	 */
	public JITInput(JITProgressNotifier __pn)
		throws NullPointerException
	{
		// Check
		if (__pn == null)
			throw new NullPointerException("NARG");
		
		this.notifier = new CatchingProgressNotifier(__pn);
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
	public final JITInput readZip(String __gn, InputStream __in)
		throws IOException, JITException, NullPointerException
	{
		if (__gn == null || __in == null)
			throw new NullPointerException("NARG");
		
		return readZip(__gn, new ZipStreamReader(__in));
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
	public final JITInput readZip(String __n, ZipStreamReader __in)
		throws IOException, NullPointerException
	{
		if (__n == null || __in == null)
			throw new NullPointerException("NARG");
		
		// Log processing
		JITProgressNotifier notifier = this.notifier;
		notifier.beginJar(__n);
		
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
						notifier.processClass(__n, name, ++numcl);
						tcl.add(ClassFile.decode(__n, e));
					}
				
					// Appending a resource
					else
					{
						notifier.processResource(__n, name, ++numrc);
						trc.add(Resource.read(__n, name, e));
					}
				}
		}
		
		// Count duration
		finally
		{
			notifier.endJar(__n, System.nanoTime() - start, numrc, numcl);
		}
		
		// Setup group
		JITInputGroup grp = new JITInputGroup(__n, trc, tcl);
		
		// Add group
		synchronized (this._lock)
		{
			// {@squirreljme.error JI2t A group with the specified name already
			// is being used as input. (The name of the group)}
			Map<String, JITInputGroup> groups = this._groups;
			if (groups.containsKey(__n))
				throw new JITException(String.format("JI2t %s", __n));
			
			// Check classes
			Map<ClassName, ClassFile> clput = new HashMap<>();
			Map<ClassName, ClassFile> classes = this._classes;
			for (ClassFile c : grp.classes().values())
			{
				// {@squirreljme.error JI31 A duplicate class exists within
				// the input and the group. (The name of the class)}
				ClassName n = c.thisName();
				if (classes.containsKey(n))
					throw new JITException(String.format("JI31 %s", n));
				
				// Is added in bulk later
				clput.put(n, c);
			}
			
			// Add everything
			groups.put(__n, grp);
			classes.putAll(clput);
		}
		
		// All done!
		return this;
	}
}

