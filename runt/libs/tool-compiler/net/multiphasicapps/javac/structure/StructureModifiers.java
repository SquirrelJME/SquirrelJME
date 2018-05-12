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

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import net.multiphasicapps.javac.syntax.AnnotationSyntax;
import net.multiphasicapps.javac.syntax.BasicModifierSyntax;
import net.multiphasicapps.javac.syntax.ModifiersSyntax;
import net.multiphasicapps.javac.syntax.ModifierSyntax;

/**
 * Represents modifiers that may be associated with a structure.
 *
 * @since 2018/05/10
 */
public final class StructureModifiers
{
	/** The modifiers which are available. */
	private final Set<StructureModifier> _modifiers;
	
	/**
	 * Initializes the modifiers.
	 *
	 * @param __m The input modifiers.
	 * @throws NullPointerException On null arguments.
	 * @throws StructureException If the modifiers are not valid.
	 * @since 2018/05/12
	 */
	public StructureModifiers(StructureModifier... __m)
		throws NullPointerException, StructureException
	{
		this(Arrays.<StructureModifier>asList((__m != null ? __m :
			new StructureModifier[0])));
	}
	
	/**
	 * Initializes the modifiers.
	 *
	 * @param __m The input modifiers.
	 * @throws NullPointerException On null arguments.
	 * @throws StructureException If the modifiers are not valid.
	 * @since 2018/05/12
	 */
	public StructureModifiers(Iterable<StructureModifier> __m)
		throws NullPointerException, StructureException
	{
		if (__m == null)
			throw new NullPointerException("NARG");
		
		Set<StructureModifier> rv = new LinkedHashSet<>();
		for (StructureModifier m : __m)
		{
			if (m == null)
				throw new NullPointerException("NARG");
			
			// {@squirreljme.error AQ5h Duplicate modifier. (The modifier)}
			if (rv.contains(m))
				throw new StructureException(String.format("AQ5h %s", m));
			
			rv.add(m);
		}
		
		this._modifiers = rv;
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
	 * Parses the given modifiers syntax and builds modifier structures from
	 * them.
	 *
	 * @param __syn The input syntax.
	 * @param __nl The lookup for names (used for annotation).
	 * @return The parsed modifiers.
	 * @throws NullPointerException On null arguments.
	 * @throws StructureException If the modifiers are not valid.
	 * @since 2018/05/10
	 */
	public static StructureModifiers parse(ModifiersSyntax __syn,
		NameLookup __nl)
		throws NullPointerException, StructureException
	{
		if (__syn == null || __nl == null)
			throw new NullPointerException("NARG");
		
		// Go through all modifiers
		Set<StructureModifier> rv = new LinkedHashSet<>();
		for (ModifierSyntax mod : __syn)
		{
			// Is this just an annotation?
			if (mod instanceof AnnotationSyntax)
				rv.add(AnnotationModifier.parse((AnnotationSyntax)mod, __nl));
			
			// Standard modifier
			else if (mod instanceof BasicModifierSyntax)
			{
				BasicModifier got;
				switch ((BasicModifierSyntax)mod)
				{
					case PUBLIC:
						got = BasicModifier.PUBLIC;
						break;
						
					case PROTECTED:
						got = BasicModifier.PROTECTED;
						break;
						
					case PRIVATE:
						got = BasicModifier.PRIVATE;
						break;
						
					case STATIC:
						got = BasicModifier.STATIC;
						break;
						
					case ABSTRACT:
						got = BasicModifier.ABSTRACT;
						break;
						
					case FINAL:
						got = BasicModifier.FINAL;
						break;
						
					case NATIVE:
						got = BasicModifier.NATIVE;
						break;
						
					case SYNCHRONIZED:
						got = BasicModifier.SYNCHRONIZED;
						break;
						
					case TRANSIENT:
						got = BasicModifier.TRANSIENT;
						break;
						
					case VOLATILE:
						got = BasicModifier.VOLATILE;
						break;
						
					case STRICTFP:
						got = BasicModifier.STRICTFP;
						break;
						
					default:
						throw new RuntimeException("OOPS");
				}
				
				rv.add(got);
			}
			
			// {@squirreljme.error AQ5i Unsupported modifier type. (The type)}
			else
				throw new RuntimeException(String.format("AQ5i %s",
					mod.getClass()));
		}
		
		return new StructureModifiers(rv);
	}
}

