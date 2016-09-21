// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.projects;

import java.io.InputStream;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.nio.file.NoSuchFileException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import net.multiphasicapps.javac.base.CompilerInput;
import net.multiphasicapps.util.unmodifiable.UnmodifiableSet;

/**
 * This allows the compiler to read input source files to be compiled from
 * the source project information.
 *
 * @since 2016/09/19
 */
class __CompilerInput__
	implements CompilerInput
{
	/** The project list. */
	protected final ProjectList list;
	
	/** The project information. */
	protected final ProjectInfo info;
	
	/** Projects which are considered part of the binary class path. */
	private final ProjectInfo[] _bin;
	
	/** Binary file contents. */
	private volatile Reference<Iterable<String>> _contents;
	
	/**
	 * Initializes the input for the compiler.
	 *
	 * @param __pl The project list.
	 * @param __src The source project being compiled.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/19
	 */
	__CompilerInput__(ProjectList __pl, ProjectInfo __src)
		throws NullPointerException
	{
		// Check
		if (__pl == null || __src == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.list = __pl;
		this.info = __src;
		
		// Go through all dependencies of the source package and add every
		// binary dependency of it
		Collection<ProjectInfo> bins = new LinkedHashSet<>();
		for (ProjectName dn : __src.dependencies())
			__pl.recursiveDependencies(bins, ProjectType.BINARY, dn, false);
		
		// Never include the binary for our own project (since it is being
		// built, the classes might be out of date)
		Iterator<ProjectInfo> it = bins.iterator();
		ProjectName name = __src.name();
		while (it.hasNext())
			if (name.equals(it.next().name()))
				it.remove();
		
		// Set it
		this._bin = bins.<ProjectInfo>toArray(new ProjectInfo[bins.size()]);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/19
	 */
	@Override
	public InputStream input(boolean __src, String __name)
		throws IOException, NoSuchFileException
	{
		// If source code, read from the project being compiled
		ProjectInfo info = this.info;
		if (__src)
			return info.open(__name);
		
		// Go through all binaries to find the file
		for (ProjectInfo bin : this._bin)
			try
			{
				return bin.open(__name);
			}
			
			// Ignore
			catch (NoSuchFileException e)
			{
			}
		
		// {@squirreljme.error CI0d The specified non-source file does not
		// exist in any project. (The requested file)}
		throw new NoSuchFileException(String.format("CI0d %s", __name));
	}

	/**
	 * {@inheritDoc}
	 * @since 2016/09/19
	 */
	@Override
	public Iterable<String> list(boolean __src)
		throws IOException
	{
		// If source code, use only a single project
		ProjectInfo info = this.info;
		if (__src)
			return info.contents();
		
		// Get
		Reference<Iterable<String>> ref = this._contents;
		Iterable<String> rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
		{
			Set<String> target = new LinkedHashSet<>();
			
			// Go through all binary projects and all of their contents
			for (ProjectInfo i : this._bin)
				for (String c : i.contents())
					target.add(c);
			
			// Cache
			rv = UnmodifiableSet.<String>of(target);
			this._contents = new WeakReference<>(rv);
		}
		
		// Return it
		return rv;
	}
}

