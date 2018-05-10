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

/**
 * This is a name lookup which additionally supports lookup into a set of
 * type parameters.
 *
 * @since 2018/05/10
 */
public final class TypeParametersNameLookup
	implements NameLookup
{
	/** The type parameters to look within. */
	protected final TypeParameters typeparameters;
	
	/** The parent name lookup. */
	protected final NameLookup parent;
	
	/**
	 * Initializes the type parameter lookup.
	 *
	 * @param __tp The type parameters to look in.
	 * @param __parent The parent name lookup for when no type parameter was
	 * found.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/05/10
	 */
	public TypeParametersNameLookup(TypeParameters __tp, NameLookup __parent)
		throws NullPointerException
	{
		if (__tp == null || __parent == null)
			throw new NullPointerException("NARG");
		
		this.typeparameters = __tp;
		this.parent = __parent;
	}
}

