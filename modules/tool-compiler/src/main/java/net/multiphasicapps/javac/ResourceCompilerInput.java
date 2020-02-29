// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac;

import java.io.InputStream;

/**
 * This is a resource input which uses a class name.
 *
 * @since 2019/06/30
 */
public final class ResourceCompilerInput
	implements CompilerInput
{
	/** The pivot to use. */
	protected final Class<?> pivot;
	
	/** The resource name used. */
	protected final String rcname;
	
	/** The file name used. */
	protected final String filename;
	
	/**
	 * Initializes the resource input.
	 *
	 * @param __pivot The class pivot used.
	 * @param __rc The resource name.
	 * @param __fn The file name.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/06/30
	 */
	public ResourceCompilerInput(Class<?> __pivot, String __rc, String __fn)
		throws NullPointerException
	{
		if (__pivot == null || __rc == null || __fn == null)
			throw new NullPointerException("NARG");
		
		this.pivot = __pivot;
		this.rcname = __rc;
		this.filename = __fn;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/06/30
	 */
	@Override
	public final boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		
		if (!(__o instanceof ResourceCompilerInput))
			return false;
		
		ResourceCompilerInput o = (ResourceCompilerInput)__o;
		return this.pivot.equals(o.pivot) &&
			this.rcname.equals(o.rcname) &&
			this.filename.equals(o.filename);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/06/30
	 */
	@Override
	public final int hashCode()
	{
		return this.pivot.hashCode() ^
			this.rcname.hashCode() ^
			this.filename.hashCode();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/06/30
	 */
	@Override
	public final long lastModifiedTime()
		throws CompilerException
	{
		return Long.MIN_VALUE;
	}

	/**
	 * {@inheritDoc}
	 * @since 2019/06/30
	 */
	@Override
	public final String fileName()
		throws CompilerException
	{
		return this.filename;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/06/30
	 */
	@Override
	public final InputStream open()
		throws CompilerException, NoSuchInputException
	{
		// {@squirreljme.error AQ39 No resource input exists. (The filename)}
		InputStream rv = this.pivot.getResourceAsStream(this.rcname);
		if (rv == null)
			throw new NoSuchInputException("AQ39 " + this.filename);
		return rv;
	}
}

