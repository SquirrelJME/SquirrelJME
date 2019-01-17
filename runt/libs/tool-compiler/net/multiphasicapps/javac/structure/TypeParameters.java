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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.multiphasicapps.classfile.Identifier;
import net.multiphasicapps.javac.syntax.TypeParametersSyntax;
import net.multiphasicapps.javac.syntax.TypeParameterSyntax;

/**
 * This represents multiple {@link TypeParameter} and is used to describe
 * all of the type parameters used for methods and classes.
 *
 * @since 2018/05/10
 */
@Deprecated
public final class TypeParameters
{
	/** The type parameters used. */
	private final TypeParameter[] _typeparams;
	
	/**
	 * Initializes the type parameters.
	 *
	 * @param __tp The input type parameters.
	 * @throws NullPointerException On null arguments.
	 * @throws StructureException If the type parameters are not valid.
	 * @since 2018/05/10
	 */
	public TypeParameters(TypeParameter... __tp)
		throws NullPointerException, StructureException
	{
		this(Arrays.<TypeParameter>asList((__tp != null ? __tp :
			new TypeParameter[0])));
	}
	
	/**
	 * Initializes the type parameters.
	 *
	 * @param __tp The input type parameters.
	 * @throws NullPointerException On null arguments.
	 * @throws StructureException If the type parameters are not valid.
	 * @since 2018/05/10
	 */
	public TypeParameters(Iterable<TypeParameter> __tp)
		throws NullPointerException, StructureException
	{
		if (__tp == null)
			throw new NullPointerException("NARG");
		
		// Find duplicate identifiers
		Set<TypeParameterSymbol> idents = new HashSet<>();
		List<TypeParameter> params = new ArrayList<>();
		for (TypeParameter tp : __tp)
			if (tp == null)
				throw new NullPointerException("NARG");
			else
			{
				params.add(tp);
				
				// {@squirreljme.error AQ12 Duplicate type parameter
				// identifier. (The identifier)}
				TypeParameterSymbol i = tp.symbol();
				if (idents.contains(i))
					throw new StructureException(String.format("AQ12 %s", i));
				idents.add(i);
			}
		
		this._typeparams = params.<TypeParameter>toArray(
			new TypeParameter[params.size()]);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/10
	 */
	@Override
	public final boolean equals(Object __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/10
	 */
	@Override
	public final int hashCode()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/10
	 */
	@Override
	public final String toString()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Parses the input type parameters and performs resolution and lookup
	 * of the types.
	 *
	 * @param __syn The syntax to parse.
	 * @param __nl The name lookup for types.
	 * @throws NullPointerException On null arguments.
	 * @throws StructureException If a type is not valid.
	 * @since 2018/05/10
	 */
	public static TypeParameters parseSyntax(TypeParametersSyntax __syn,
		NameLookup __nl)
		throws NullPointerException, StructureException
	{
		if (__syn == null || __nl == null)
			throw new NullPointerException("NARG");
		
		// Parse each one
		List<TypeParameter> rv = new ArrayList<>();
		for (TypeParameterSyntax s : __syn)
			throw new todo.TODO();
		
		return new TypeParameters(rv);
	}
}

