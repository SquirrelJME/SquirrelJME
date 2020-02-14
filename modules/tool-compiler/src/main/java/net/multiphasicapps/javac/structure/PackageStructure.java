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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This structure represents a single package.
 *
 * @since 2018/05/07
 */
@Deprecated
public final class PackageStructure
	implements Structure
{
	/** The symbol used. */
	protected final PackageSymbol symbol;
	
	/** The annotation modifiers. */
	private final AnnotationModifier[] _annotations;
	
	/**
	 * Initializes the package structure.
	 *
	 * @param __sym The symbol for the package.
	 * @param __ams The annotation modifiers.
	 * @throws NullPointerException On null arguments.
	 * @throws StructureException If the structure is not valid.
	 * @since 2018/05/07
	 */
	public PackageStructure(PackageSymbol __sym, AnnotationModifier[] __ams)
		throws NullPointerException, StructureException
	{
		this(__sym, Arrays.<AnnotationModifier>asList(
			(__ams == null ? new AnnotationModifier[0] : __ams)));
	}
	
	/**
	 * Initializes the package structure.
	 *
	 * @param __sym The symbol for the package.
	 * @param __ams The annotation modifiers.
	 * @throws NullPointerException On null arguments.
	 * @throws StructureException If the structure is not valid.
	 * @since 2018/05/07
	 */
	public PackageStructure(PackageSymbol __sym,
		Iterable<AnnotationModifier> __ams)
		throws NullPointerException, StructureException
	{
		if (__sym == null || __ams == null)
			throw new NullPointerException("NARG");
		
		List<AnnotationModifier> annotations = new ArrayList<>();
		for (AnnotationModifier am : __ams)
			if (am == null)
				throw new NullPointerException("NARG");
			else
				annotations.add(am);
		
		this.symbol = __sym;
		this._annotations = annotations.<AnnotationModifier>toArray(
			new AnnotationModifier[annotations.size()]);
	}
	
	/**
	 * Returns the used annotations.
	 *
	 * @return The used annotations.
	 * @since 2018/05/07
	 */
	public final AnnotationModifier[] annotations()
	{
		return this._annotations;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/07
	 */
	@Override
	public final boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		
		if (!(__o instanceof PackageStructure))
			return false;
		
		PackageStructure o = (PackageStructure)__o;
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/07
	 */
	@Override
	public final int hashCode()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/07
	 */
	@Override
	public final PackageSymbol symbol()
	{
		return this.symbol;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/07
	 */
	@Override
	public final String toString()
	{
		throw new todo.TODO();
	}
}

