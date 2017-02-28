// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.host.javase;

import java.io.Closeable;
import java.io.File;
import java.io.Flushable;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.tools.FileObject;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import net.multiphasicapps.squirreljme.build.base.CompilerOutput;
import net.multiphasicapps.squirreljme.build.base.FileDirectory;
import net.multiphasicapps.util.unmodifiable.UnmodifiableList;

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
	protected final List<FileDirectory> classpath;
	
	/** The source code input. */
	protected final FileDirectory sourcepath;
	
	/** List of files in the class path. */
	private volatile Reference<Iterable<String>> _classpathlist;
	
	/** List of files in the source path. */
	private volatile Reference<Iterable<String>> _sourcepathlist;
	
	/**
	 * Initializes the file manager.
	 *
	 * @param __co The compiler output.
	 * @param __bin The class path.
	 * @param __src Input source code to compile.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/19
	 */
	__FileManager__(CompilerOutput __co, Iterable<FileDirectory> __bin,
		FileDirectory __src)
		throws NullPointerException
	{
		// Check
		if (__co == null || __bin == null || __src == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.output = __co;
		
		// Set source class path
		List<FileDirectory> classpath = new ArrayList<>();
		for (FileDirectory fd : __bin)
			if (fd != null)
				classpath.add(fd);
		this.classpath = UnmodifiableList.<FileDirectory>of(classpath);
		
		// Set source file path
		this.sourcepath = __src;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/19
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
	 * @since 2016/09/19
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
		throw new todo.TODO();
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
		throw new todo.TODO();
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
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/19
	 */
	@Override
	public JavaFileObject getJavaFileForOutput(
		JavaFileManager.Location __loc, String __cn, JavaFileObject.Kind __k,
		FileObject __sib)
		throws IOException
	{
		// {@squirreljme.error BM04 Only class file output is supported.}
		if (!JavaFileObject.Kind.CLASS.equals(__k))
			throw new IllegalArgumentException("BM04");
		
		// Convert class to "class" name
		String name = __cn.replace('.', '/') + __k.extension;
		
		// Open output file
		return new __FileObject__(this, this.output, name);
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
	 * @since 2016/09/19
	 */
	@Override
	public Iterable<? extends File> getLocation(
		JavaFileManager.Location __a)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/19
	 */
	@Override
	public boolean handleOption(String __a, Iterator<String> __b)
	{
		throw new todo.TODO();
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
	 * @since 2016/09/19
	 */
	@Override
	public boolean isSameFile(FileObject __a, FileObject __b)
	{
		throw new todo.TODO();
	}
		
	/**
	 * {@inheritDoc}
	 * @since 2016/09/19
	 */
	@Override
	public int isSupportedOption(String __a)
	{
		throw new todo.TODO();
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
	 * @since 2016/09/19
	 */
	@Override
	public void setLocation(JavaFileManager.Location __a, Iterable<?
		extends File> __b)
		throws IOException
	{
		// Ignore any location setting as all locations are completely
		// internal
	}
	
	/**
	 * Lists all files within the given directories.
	 *
	 * @param __fds The directories to process.
	 * @return All of the directory sources.
	 * @since 2016/12/26
	 */
	private Iterable<String> __list(Iterable<FileDirectory> __fds)
		throws NullPointerException
	{
		// Check
		if (__fds == null)
			throw new NullPointerException("NARG");
		
		// Fill target lists
		List<String> rv = new ArrayList<>();
		for (FileDirectory fd : __fds)
			for (String s : fd)
				rv.add(s);
		
		// Lock it
		return UnmodifiableList.<String>of(rv);
	}
	
	/**
	 * Returns the directory for the given file.
	 *
	 * @param __src Lookup source code?
	 * @param __s The file to get the directory of.
	 * @return The directory for the given file.
	 * @throws IOException If it could not be determined.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/12/26
	 */
	private FileDirectory __findDirectory(boolean __src, String __s)
		throws IOException, NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Check sources
		if (__src)
		{
			FileDirectory sourcepath = this.sourcepath;
			if (sourcepath.contains(__s))
				return sourcepath;
		}
		
		// Check class path
		else
			for (FileDirectory fd : this.classpath)
				if (fd.contains(__s))
					return fd;
		
		// {@squirreljme.error BM09 Could not find the directory for the
		// requested file. (The requested file)}
		throw new IOException(String.format("BM09 %s", __s));
	}
	
	/**
	 * Lists the files in the class path.
	 *
	 * @return The files in the class path.
	 * @since 2016/12/26
	 */
	private Iterable<String> __listClassPath()
	{
		Reference<Iterable<String>> ref = this._classpathlist;
		Iterable<String> rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
			this._classpathlist = new WeakReference<>(
				(rv = __list(this.classpath)));
		
		return rv;
	}
	
	/**
	 * Lists the files in the source path.
	 *
	 * @return The files in the source path.
	 * @since 2016/12/26
	 */
	private Iterable<String> __listSourcePath()
	{
		Reference<Iterable<String>> ref = this._sourcepathlist;
		Iterable<String> rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
			this._sourcepathlist = new WeakReference<>(
				(rv = __list(Arrays.<FileDirectory>asList(this.sourcepath))));
		
		return rv;
	}
}

