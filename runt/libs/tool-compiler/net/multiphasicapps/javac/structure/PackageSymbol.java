// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.structure;

import net.multiphasicapps.classfile.BinaryName;

/**
 * This represents the symbol name of a package, it is mostly intended to
 * handle {@code package-info} files and potentially associate any annotations
 * to the package itself.
 *
 * @since 2018/05/05
 */
@Deprecated
public final class PackageSymbol
	implements StructureSymbol
{
	/** The binary name of the package. */
	protected final BinaryName name;
	
	/**
	 * Initializes the symbol.
	 *
	 * @param __bn The binary name used.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/05/07
	 */
	public PackageSymbol(BinaryName __n)
		throws NullPointerException
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		this.name = __n;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/05
	 */
	@Override
	public final boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		
		if (!(__o instanceof PackageSymbol))
			return false;
		
		PackageSymbol o = (PackageSymbol)__o;
		return this.name.equals(o.name);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/05
	 */
	@Override
	public final int hashCode()
	{
		return this.name.hashCode();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/05
	 */
	@Override
	public final String toString()
	{
		return this.name.toString();
	}
}

