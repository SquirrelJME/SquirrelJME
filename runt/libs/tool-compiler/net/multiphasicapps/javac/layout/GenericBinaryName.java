// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.layout;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Objects;
import net.multiphasicapps.classfile.BinaryName;

/**
 * This represents a a binary name which additionally has generic parameters
 * associated with it.
 *
 * @since 2018/04/08
 */
public final class GenericBinaryName
{
	/** The binary name. */
	protected final BinaryName name;
	
	/** The generic parameters, if any. */
	protected final GenericParameters generic;
	
	/** String representation. */
	private Reference<String> _string;
	
	/**
	 * Initializes the generic binary name with no parameters.
	 *
	 * @param __bn The binary name.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/04/09
	 */
	public GenericBinaryName(BinaryName __bn)
		throws NullPointerException
	{
		if (__bn == null)
			throw new NullPointerException("NARG");
		
		this.name = __bn;
		this.generic = null;
	}
	
	/**
	 * Initializes the generic binary name with the given parameters.
	 *
	 * @param __bn The binary name.
	 * @param __gp The generic parameters to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/04/09
	 */
	public GenericBinaryName(BinaryName __bn, GenericParameters __gp)
		throws NullPointerException
	{
		if (__bn == null || __gp == null)
			throw new NullPointerException("NARG");
			
		this.name = __bn;
		this.generic = __gp;
	}
	
	/**
	 * Returns the binary name.
	 *
	 * @return The binary name.
	 * @since 2018/04/09
	 */
	public final BinaryName binaryName()
	{
		return this.name;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/08
	 */
	@Override
	public final boolean equals(Object __o)
	{
		if (__o == this)
			return true;
		
		if (!(__o instanceof GenericBinaryName))
			return false;
		
		GenericBinaryName o = (GenericBinaryName)__o;
		return this.name.equals(o.name) &&
			Objects.equals(this.generic, o.generic);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/08
	 */
	@Override
	public final int hashCode()
	{
		return this.name.hashCode() ^
			Objects.hashCode(this.generic);
	}
	
	/**
	 * Returns the generic parameters.
	 *
	 * @return The generic parameters or {@code null} if there are none.
	 * @since 2018/04/09
	 */
	public final GenericParameters genericParameters()
	{
		return this.generic;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/08
	 */
	@Override
	public final String toString()
	{
		throw new todo.TODO();
	}
}

