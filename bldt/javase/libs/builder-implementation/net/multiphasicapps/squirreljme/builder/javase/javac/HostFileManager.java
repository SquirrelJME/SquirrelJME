// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.builder.javase.javac;

import javax.tools.FileObject;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import net.multiphasicapps.javac.CompilerInputLocation;
import net.multiphasicapps.javac.CompilerOutput;
import net.multiphasicapps.javac.CompilerPathSet;

/**
 * This is used by the Java compiler to manage files used by the compiler
 * for compilation and ones which are output.
 *
 * @since 2017/11/29
 */
public class HostFileManager
	implements StandardJavaFileManager
{
	/** Paths to search for input within. */
	protected final CompilerPathSet[][] paths;
	
	/** The output for the compiler. */
	protected final CompilerOutput output;
	
	/**
	 * Initializes the host file manager.
	 *
	 * @param __paths Output paths.
	 * @param __out Compiler output.
	 * @since 2017/11/29
	 */
	public HostFileManager(CompilerPathSet[][] __paths, CompilerOutput __out)
		throws NullPointerException
	{
		if (__paths == null || __out == null)
			throw new NullPointerException("NARG");
		
		this.paths = __paths;
		this.output = __out;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/29
	 */
	@Override
	public void close()
		throws IOException
	{
		// Flush the output writer so that any entries which are waiting to
		// be written are written to the ZIP
		this.output.flush();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/29
	 */
	@Override
	public void flush()
		throws IOException
	{
		// Forward to output
		this.output.flush();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/29
	 */
	@Override
	public ClassLoader getClassLoader(JavaFileManager.Location __a)
	{
		// No plug-ins permitted at all
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/29
	 */
	@Override
	public FileObject getFileForInput(JavaFileManager.Location __a,
		String __b, String __c)
		throws IOException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/29
	 */
	@Override
	public FileObject getFileForOutput(JavaFileManager.Location __a,
		String __b, String __c, FileObject __d)
		throws IOException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/29
	 */
	@Override
	public JavaFileObject getJavaFileForInput(
		JavaFileManager.Location __a, String __b, JavaFileObject.Kind __c)
		throws IOException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/29
	 */
	@Override
	public JavaFileObject getJavaFileForOutput(
		JavaFileManager.Location __loc, String __cn, JavaFileObject.Kind __k,
		FileObject __sib)
		throws IOException
	{
		// NEEDS IMPLEMENTING
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/29
	 */
	@Override
	public Iterable<? extends JavaFileObject> getJavaFileObjects(
		File... __a)
	{
		return getJavaFileObjectsFromFiles(Arrays.<File>asList(__a));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/29
	 */
	@Override
	public Iterable<? extends JavaFileObject> getJavaFileObjects(
		String... __a)
	{
		return getJavaFileObjectsFromStrings(Arrays.<String>asList(__a));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/29
	 */
	@Override
	public Iterable<? extends JavaFileObject>
		getJavaFileObjectsFromFiles(Iterable<? extends File> __a)
	{
		// Forward call to string version
		Set<String> args = new LinkedHashSet<>();
		for (File f : __a)
			args.add(String.valueOf(f));
		return getJavaFileObjectsFromStrings(args);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/29
	 */
	@Override
	public Iterable<? extends JavaFileObject>
		getJavaFileObjectsFromStrings(Iterable<String> __a)
	{
		Set<JavaFileObject> rv = new LinkedHashSet<>();
		FileDirectory sourcepath = this.sourcepath;
		for (String s : __a)
			if (s.endsWith(".java"))
				rv.add(new __FileObject__(this, sourcepath, s));
			
			// {@squirreljme.error BM07 Do not know how to handle getting a
			// file object from the given file name. (The name of the file)}
			else
				throw new IllegalArgumentException(String.format("BM07 %s",
					s));
		
		// Return
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/29
	 */
	@Override
	public Iterable<? extends File> getLocation(
		JavaFileManager.Location __l)
	{
		// Ignore null
		if (__l == null)
			return null;
		
		// Standard locations
		if (__l instanceof StandardLocation)
			switch ((StandardLocation)__l)
			{
					// This is required for Java 9's compiler to generate
					// code
				case CLASS_OUTPUT:
					return Arrays.<File>asList(new File("virtual://"));
				
					// Unknown
				default:
					return null;
			}
		
		// Unknown
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/29
	 */
	@Override
	public boolean handleOption(String __a, Iterator<String> __b)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/29
	 */
	@Override
	public boolean hasLocation(JavaFileManager.Location __a)
	{
		// Only use standard locations
		if (!(__a instanceof StandardLocation))
			return false;
		
		// Depends
		switch ((StandardLocation)__a)
		{
				// Knows class input and output
			case CLASS_OUTPUT:
			case CLASS_PATH:
			case PLATFORM_CLASS_PATH:
			case SOURCE_PATH:
				return true;
			
				// Unknown
			default:
				return false;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/29
	 */
	@Override
	public String inferBinaryName(JavaFileManager.Location __a,
		JavaFileObject __b)
	{
		// Get name
		String name = __b.getName();
		
		// Try to remove the extension
		int ls = name.lastIndexOf('/'),
			ld = name.lastIndexOf('.');
		
		// Remove everything up to the extension
		if (ld > ls && ld >= 0)
			name = name.substring(0, ld);
		
		// Convert any slashes to dots
		return name.replace('/', '.');
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/29
	 */
	@Override
	public boolean isSameFile(FileObject __a, FileObject __b)
	{
		throw new todo.TODO();
	}
		
	/**
	 * {@inheritDoc}
	 * @since 2017/11/29
	 */
	@Override
	public int isSupportedOption(String __o)
	{
		// Ignore nulls
		if (__o == null)
			return -1;
		
		// Depends on the options
		switch (__o)
		{
				// Not supported
			case "--multi-release":		// Java 9 multi-release JARs
			default:
				return -1;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/29
	 */
	@Override
	public Iterable<JavaFileObject> list(JavaFileManager.Location
		__l, String __pk, Set<JavaFileObject.Kind> __kinds, boolean __rec)
		throws IOException
	{
		// Setup target
		Set<JavaFileObject> rv = new LinkedHashSet<>();
		
		// Determine which input file source to use
		Iterable<String> files;
		if (!(__l instanceof StandardLocation))
			return rv;
		else
			switch ((StandardLocation)__l)
			{
					// Class inputs
				case CLASS_PATH:
				case PLATFORM_CLASS_PATH:
					files = __listClassPath();
					break;
				
					// Source inputs
				case SOURCE_PATH:
					files = __listSourcePath();
					break;
			
					// Unknown, return nothing
				default:
					return rv;
			}
		
		// Prefix to consider?
		String prefix = (__pk == null ? "" : __pk.replace('.', '/') + "/");
		int prefixn = prefix.length();
		
		// Go through all files
		boolean issource = (__l == StandardLocation.SOURCE_PATH);
		for (String f : files)
		{
			// Prefix does not match?
			if (!f.startsWith(prefix))
				continue;
			
			// If not recursive, then the last slash that appears must be
			// at the same length of the prefix
			if (!__rec)
			{
				int ls = Math.max(-1, f.lastIndexOf('/')) + 1;
				if (ls != prefixn)
					continue;
			}
			
			// Only consider files with these extensions
			boolean hit = false;
			for (JavaFileObject.Kind k : __kinds)
				if (f.endsWith(k.extension))
				{
					hit = true;
					break;
				}
			
			// Missed extension?
			if (!hit)
				continue;
			
			// Add file
			rv.add(new __FileObject__(this, __findDirectory(issource, f), f));
		}
		
		// Return
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/29
	 */
	@Override
	public void setLocation(JavaFileManager.Location __a, Iterable<?
		extends File> __b)
		throws IOException
	{
		// Ignore any location setting as all locations are completely
		// internal
	}
}

