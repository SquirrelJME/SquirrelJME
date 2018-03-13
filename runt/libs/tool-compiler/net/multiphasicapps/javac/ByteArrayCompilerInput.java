// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;

/**
 * This is input for the compiler which uses the specified byte array as
 * input.
 *
 * @since 2018/03/06
 */
public final class ByteArrayCompilerInput
	implements CompilerInput
{
	/** The name of the input. */
	protected final String name;
	
	/** The last modified time. */
	protected final long lastmodifiedtime;
	
	/** The data for the compiler. */
	private final byte[] _data;
	
	/**
	 * Initializes the compiler input at the current time.
	 *
	 * @param __name The name of the input.
	 * @param __data The data to use for the input.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/06
	 */
	public ByteArrayCompilerInput(String __name, byte[] __data)
		throws NullPointerException
	{
		this(__name, System.currentTimeMillis(), __data);
	}
	
	/**
	 * Initializes the compiler input.
	 *
	 * @param __name The name of the input.
	 * @param __lmt The time this input was last modified.
	 * @param __data The data to use for the input.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/06
	 */
	public ByteArrayCompilerInput(String __name, long __lmt, byte[] __data)
		throws NullPointerException
	{
		if (__name == null || __data == null)
			throw new NullPointerException("NARG");
		
		this.name = __name;
		this.lastmodifiedtime = __lmt;
		this._data = __data.clone();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/06
	 */
	@Override
	public final boolean equals(Object __o)
	{
		if (__o == this)
			return true;
		
		if (!(__o instanceof ByteArrayCompilerInput))
			return false;
		
		ByteArrayCompilerInput o = (ByteArrayCompilerInput)__o;
		return this.name.equals(o.name) &&
			Arrays.equals(this._data, o._data);
	}

	/**
	 * {@inheritDoc}
	 * @since 2018/03/06
	 */
	@Override
	public final String fileName()
		throws CompilerException
	{
		return this.name;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/06
	 */
	@Override
	public final int hashCode()
	{
		return this.name.hashCode();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/06
	 */
	@Override
	public final long lastModifiedTime()
		throws CompilerException
	{
		return this.lastmodifiedtime;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/06
	 */
	@Override
	public final InputStream open()
		throws CompilerException, NoSuchInputException
	{
		return new ByteArrayInputStream(this._data);
	}
}

