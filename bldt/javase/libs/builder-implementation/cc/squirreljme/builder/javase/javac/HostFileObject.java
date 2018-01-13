// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.builder.javase.javac;

import java.net.URI;
import java.net.URISyntaxException;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.NestingKind;
import javax.tools.JavaFileObject;

/**
 * This is the base for file objects.
 *
 * @since 2017/11/29
 */
public abstract class HostFileObject
	implements JavaFileObject
{
	/** The name of the file. */
	protected final String name;
	
	/**
	 * Initializes the base file object.
	 *
	 * @param __name The name of the file.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/29
	 */
	public HostFileObject(String __name)
		throws NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		this.name = __name;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/29
	 */
	@Override
	public final boolean delete()
	{
		// Unsupported
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/29
	 */
	@Override
	public final Modifier getAccessLevel()
	{
		// Unknown
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/29
	 */
	@Override
	public final JavaFileObject.Kind getKind()
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
	 * @since 2017/11/29
	 */
	@Override
	public final long getLastModified()
	{
		// Unsupported
		return Long.MIN_VALUE;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/29
	 */
	@Override
	public final String getName()
	{
		return this.name;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/29
	 */
	@Override
	public final NestingKind getNestingKind()
	{
		// Unknown
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/29
	 */
	@Override
	public final boolean isNameCompatible(String __sn,
		JavaFileObject.Kind __k)
	{
		// Get name
		String name = this.name;
		
		// Does not match extension?
		if (!name.endsWith(__k.extension))
			return false;
		
		// Only consider anything after the last slash
		int ls = name.lastIndexOf('/');
		if (ls > 0)
			name = name.substring(ls + 1);
		
		// And remove the extension
		int ld = name.lastIndexOf('.');
		if (ld > 0)
			name = name.substring(0, ld);
		
		// Compare base
		return name.equals(__sn);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/01/22
	 */
	@Override
	public final String toString()
	{
		return this.name;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/29
	 */
	@Override
	public final URI toUri()
	{
		// Can fail, but it should not
		try
		{
			return new URI("squirreljme", this.name, null);
		}
		
		// {@squirreljme.error BM06 Could not create a URI for the file.}
		catch (URISyntaxException e)
		{
			throw new RuntimeException("BM06", e);
		}
	}
}

