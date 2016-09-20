// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.bootstrap.javase;

import java.io.Closeable;
import java.io.File;
import java.io.Flushable;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.tools.FileObject;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import net.multiphasicapps.javac.base.CompilerInput;
import net.multiphasicapps.javac.base.CompilerOutput;

/**
 * This is used by the Java compiler to manage files used by the compiler
 * for compilation and ones which are output.
 *
 * @since 2016/09/19
 */
class __FileManager__
	implements StandardJavaFileManager
{
	/** Compiler output. */
	protected final CompilerOutput output;
	
	/** Compiler input. */
	protected final CompilerInput input;
	
	/**
	 * Initializes the file manager.
	 *
	 * @param __co The compiler output.
	 * @param __ci The compiler input.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/19
	 */
	__FileManager__(CompilerOutput __co, CompilerInput __ci)
		throws NullPointerException
	{
		// Check
		if (__co == null || __ci == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.output = __co;
		this.input = __ci;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/19
	 */
	@Override
	public void close()
		throws IOException
	{
		// Do nothing
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/19
	 */
	@Override
	public void flush()
		throws IOException
	{
		// Do nothing
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/19
	 */
	@Override
	public ClassLoader getClassLoader(JavaFileManager.Location __a)
	{
		// No plug-ins permitted at all
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/19
	 */
	@Override
	public FileObject getFileForInput(JavaFileManager.Location __a,
		String __b, String __c)
		throws IOException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/19
	 */
	@Override
	public FileObject getFileForOutput(JavaFileManager.Location __a,
		String __b, String __c, FileObject __d)
		throws IOException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/19
	 */
	@Override
	public JavaFileObject getJavaFileForInput(
		JavaFileManager.Location __a, String __b, JavaFileObject.Kind __c)
		throws IOException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/19
	 */
	@Override
	public JavaFileObject getJavaFileForOutput(
		JavaFileManager.Location __a, String __b, JavaFileObject.Kind __c,
		FileObject __d)
		throws IOException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/19
	 */
	@Override
	public Iterable<? extends JavaFileObject> getJavaFileObjects(
		File... __a)
	{
		return getJavaFileObjectsFromFiles(Arrays.<File>asList(__a));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/19
	 */
	@Override
	public Iterable<? extends JavaFileObject> getJavaFileObjects(
		String... __a)
	{
		return getJavaFileObjectsFromStrings(Arrays.<String>asList(__a));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/19
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
	 * @since 2016/09/19
	 */
	@Override
	public Iterable<? extends JavaFileObject>
		getJavaFileObjectsFromStrings(Iterable<String> __a)
	{
		Set<JavaFileObject> rv = new LinkedHashSet<>();
		CompilerInput input = this.input;
		for (String s : __a)
			if (s.endsWith(".java"))
				rv.add(new __FileObject__(this, input, s));
			
			// {@squirreljme.error DE02 Do not know how to handle getting a
			// file object from the given file name. (The name of the file)}
			else
				throw new IllegalArgumentException(String.format("DE02 %s",
					s));
		
		// Return
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/19
	 */
	@Override
	public Iterable<? extends File> getLocation(
		JavaFileManager.Location __a)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/19
	 */
	@Override
	public boolean handleOption(String __a, Iterator<String> __b)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/19
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
	 * @since 2016/09/19
	 */
	@Override
	public String inferBinaryName(JavaFileManager.Location __a,
		JavaFileObject __b)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/19
	 */
	@Override
	public boolean isSameFile(FileObject __a, FileObject __b)
	{
		throw new Error("TODO");
	}
		
	/**
	 * {@inheritDoc}
	 * @since 2016/09/19
	 */
	@Override
	public int isSupportedOption(String __a)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/19
	 */
	@Override
	public Iterable<JavaFileObject> list(JavaFileManager.Location
		__l, String __pk, Set<JavaFileObject.Kind> __kinds, boolean __rec)
		throws IOException
	{
		// Debug
		System.err.printf("DEBUG -- list(%s, %s, %s, %b)%n", __l, __pk,
			__kinds, __rec);
		
		// Setup target
		Set<JavaFileObject> rv = new LinkedHashSet<>();
		
		// Go through input files
		CompilerInput input = this.input;
		
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
					files = input.list(false);
					break;
				
					// Source inputs
				case SOURCE_PATH:
					files = input.list(true);
					break;
			
					// Unknown, return nothing
				default:
					return rv;
			}
		
		// Prefix to consider?
		String prefix = (__pk == null ? "" : __pk.replace('.', '/') + "/");
		int prefixn = prefix.length();
		
		// Go through all files
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
			rv.add(new __FileObject__(this, input, f));
		};
		
		// Return
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/19
	 */
	@Override
	public void setLocation(JavaFileManager.Location __a, Iterable<?
		extends File> __b)
		throws IOException
	{
		throw new Error("TODO");
	}
}

