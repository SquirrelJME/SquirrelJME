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

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.NestingKind;
import javax.tools.JavaFileObject;
import net.multiphasicapps.javac.base.CompilerInput;
import net.multiphasicapps.javac.base.CompilerOutput;

/**
 * This is the base for file objects.
 *
 * @since 2016/09/19
 */
class __FileObject__
	implements JavaFileObject
{
	/** The owning file manager. */
	protected final __FileManager__ manager;
	
	/** The name of the file. */
	protected final String name;
	
	/** The input, if it is for input. */
	protected final CompilerInput input;
	
	/** The output, if it is for output. */
	protected final CompilerOutput output;
	
	/**
	 * Represents a file object as input.
	 *
	 * @param __in The compiler input source.
	 * @param __s The file to represent.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/19
	 */
	__FileObject__(__FileManager__ __fm, CompilerInput __in, String __s)
		throws NullPointerException
	{
		// Check
		if (__fm == null || __in == null || __s == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.manager = __fm;
		this.input = __in;
		this.output = null;
		this.name = __s;
	}
	
	/**
	 * Represents a file object as output.
	 *
	 * @param __out The output compiler source.
	 * @param __s The file to represent.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/19
	 */
	__FileObject__(__FileManager__ __fm, CompilerOutput __out, String __s)
		throws NullPointerException
	{
		// Check
		if (__fm == null || __out == null || __s == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.manager = __fm;
		this.input = null;
		this.output = __out;
		this.name = __s;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/19
	 */
	@Override
	public boolean delete()
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/19
	 */
	@Override
	public Modifier getAccessLevel()
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/19
	 */
	@Override
	public CharSequence getCharContent(boolean __a)
		throws IOException
	{
		// {@squirreljme.error DE03 This file was opened for output.}
		CompilerInput input = this.input;
		if (input == null)
			throw new IllegalStateException("DE03");
		
		// Read in all the characters
		StringBuilder sb = new StringBuilder();
		try (Reader r = new InputStreamReader(
			input.input(__isSource(), this.name), "utf-8"))
		{
			char[] buf = new char[2048];
			for (;;)
			{
				int rc = r.read(buf);
				
				// EOF?
				if (rc < 0)
					break;
				
				// Write
				sb.append(buf, 0, rc);
			}
		}
		
		// Return it
		return sb;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/19
	 */
	@Override
	public JavaFileObject.Kind getKind()
	{
		// Depends on the extension
		String name = this.name;
		
		// Java source code
		if (name.endsWith(".java"))
			return JavaFileObject.Kind.SOURCE;
		
		// Java class file
		else if (name.endsWith(".class"))
			return JavaFileObject.Kind.CLASS;
		
		// HTML
		else if (name.endsWith(".htm") || name.endsWith(".html"))
			return JavaFileObject.Kind.HTML;
		
		// Unknoen
		return JavaFileObject.Kind.OTHER;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/19
	 */
	@Override
	public long getLastModified()
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/19
	 */
	@Override
	public String getName()
	{
		return this.name;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/19
	 */
	@Override
	public NestingKind getNestingKind()
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/19
	 */
	@Override
	public boolean isNameCompatible(String __sn, JavaFileObject.Kind
		__k)
	{
		// Determine the extension
		String ext;
		switch (__k)
		{
				// Source code
			case SOURCE:
				ext = ".java";
				break;
			
				// Class file
			case CLASS:
				ext = ".class";
				break;
				
				// Unknown, ignore
			default:
				return false;
		}
		
		// Compare file name
		return this.name.equals(__sn.replace('.', '/') + ext);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/19
	 */
	@Override
	public InputStream openInputStream()
		throws IOException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/19
	 */
	@Override
	public OutputStream openOutputStream()
		throws IOException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/19
	 */
	@Override
	public Reader openReader(boolean __a)
		throws IOException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/19
	 */
	@Override
	public Writer openWriter()
		throws IOException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/19
	 */
	@Override
	public URI toUri()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Is this source code?
	 *
	 * @return {@code true} if source code.
	 * @since 2016/09/19
	 */
	private boolean __isSource()
	{
		return JavaFileObject.Kind.SOURCE == getKind();
	}
}

