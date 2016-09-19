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
import java.util.Iterator;
import java.util.Set;
import javax.tools.FileObject;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
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
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/19
	 */
	@Override
	public void flush()
		throws IOException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/19
	 */
	@Override
	public ClassLoader getClassLoader(JavaFileManager.Location __a)
	{
		throw new Error("TODO");
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
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/19
	 */
	@Override
	public Iterable<? extends JavaFileObject> getJavaFileObjects(
		String... __a)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/19
	 */
	@Override
	public Iterable<? extends JavaFileObject>
		getJavaFileObjectsFromFiles(Iterable<? extends File> __a)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/19
	 */
	@Override
	public Iterable<? extends JavaFileObject>
		getJavaFileObjectsFromStrings(Iterable<String> __a)
	{
		throw new Error("TODO");
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
		throw new Error("TODO");
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
		__a, String __b, Set<JavaFileObject.Kind> __c, boolean __d)
		throws IOException
	{
		throw new Error("TODO");
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

