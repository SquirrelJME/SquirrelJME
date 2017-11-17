// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.builder.support;

import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.collections.SortedTreeMap;

/**
 * This class is used to manage binaries which are available for running
 * and/or compilation.
 *
 * @since 2017/10/31
 */
public final class BinaryManager
{
	/** The output directory where built binaries are to be placed. */
	protected final Path output;
	
	/** Projects which may exist that provide access to source code. */
	protected final SourceManager sources;
	
	/** Projects which have been read by the manager. */
	private final Map<SourceName, Binary> _binaries =
		new SortedTreeMap<>();
	
	/**
	 * Initializes the binary manager.
	 *
	 * @param __out The output directory, this directory is scanned for
	 * binaries as requested.
	 * @param __src The source code where projects may be sourced from, may
	 * be {@code null} if there is no source code.
	 * @throws NullPointerException If no output path was specified.
	 * @since 2017/10/31
	 */
	public BinaryManager(Path __out, SourceManager __src)
		throws NullPointerException
	{
		if (__out == null)
			throw new NullPointerException("NARG");
		
		this.output = __out;
		this.sources = __src;
	}
	
	/**
	 * This returns the set of binaries which are needed for this project
	 * to build and operate correctly.
	 *
	 * @param __b The binary to get the dependencies of.
	 * @return All dependencies which are required for this project to
	 * operate.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/17
	 */
	public final Binary[] allDependencies(Binary __b)
		throws NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Compiles the specified binary and all of their dependencies.
	 *
	 * @param __b The binary to compile.
	 * @return The 
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/17
	 */
	public final Binary[] compile(Binary __b)
		throws NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Returns all of the binaries which are direct dependencies of this
	 * binary.
	 *
	 * @param __b The binary to get the dependencies for.
	 * @return The direct dependencies for this binary.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/17
	 */
	public final Binary[] dependencies(Binary __b)
		throws NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Obtains the binary which uses the given source name.
	 *
	 * @param __n The name of the project to get.
	 * @return The binary for the given name.
	 * @throws NoSuchBinaryException If no binary with the given name exists.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/17
	 */
	public final Binary get(String __n)
		throws NoSuchBinaryException, NullPointerException
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		return get(new SourceName(__n));
	}
	
	/**
	 * Obtains the binary which uses the given source name.
	 *
	 * @param __n The name of the project to get.
	 * @return The binary for the given name.
	 * @throws NoSuchBinaryException If no binary with the given name exists.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/31
	 */
	public final Binary get(SourceName __n)
		throws NoSuchBinaryException, NullPointerException
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// Before locking see if the source package exists
		SourceManager sources = this.sources;
		Source source = sources.get(__n);
		
		// Resolve path to the binary root
		Path binp = this.output.resolve(__n.toString() + ".jar");
		
		// Lock on the map since it is dynamically generated
		Map<SourceName, Binary> binaries = this._binaries;
		synchronized (binaries)
		{
			// If the binary has already been cached, use that
			Binary rv = binaries.get(__n);
			if (rv != null)
				return rv;
			
			// Create the project
			rv = new Binary(__n, source, binp);
			
			// Cache it
			binaries.put(__n, rv);
			return rv;
		}
	}
	
	/**
	 * Creates a virtual binary which is sourced from the given JAR file and
	 * where it has no backing in source code.
	 *
	 * @param __p The path to JAR to be opened as a binary.
	 * @return The binary for the given path.
	 * @throws NoSuchBinaryException If no such binary exists.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/31
	 */
	public final Binary getVirtual(Path __p)
		throws NoSuchBinaryException, NullPointerException
	{
		if (__p == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AU01 Cannot open the specified path as a project
		// because it does not exist. (The path to open as a binary)}
		if (!Files.exists(__p))
			throw new NoSuchBinaryException(String.format("AU01 %s", __p));
		
		// Just create the binary
		return new Binary(SourceName.ofBinaryPath(__p), null, __p);
	}
	
	/**
	 * This returns the set of binaries which are needed for this project
	 * to build and operate correctly.
	 *
	 * @param __b The binary to get the dependencies of.
	 * @return All dependencies which are required for this project to
	 * operate.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/17
	 */
	private final Binary[] __basicAllDependencies(Binary __b)
		throws NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		
		Set<Binary> rv = new LinkedHashSet<>(); 
		Deque<Binary> queue = new ArrayDeque<>();
		
		// Initially start with the current dependencies
		for (Binary b : dependencies(__b))
			queue.addLast(b);
		
		// Always drain the queue
		while (!queue.isEmpty())
		{
			// Only process once
			Binary b = queue.removeFirst();
			if (!rv.add(b))
				continue;
			
			// Go through those dependencies
			for (Binary d : dependencies(b))
				queue.addLast(d);
		}
		
		// Always remove this from the return value
		rv.remove(this);
		return rv.<Binary>toArray(new Binary[rv.size()]);
	}
}

