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
			
			// {@squirreljme.error AQ0y Duplicate modifier. (The modifier)}
			if (rv.contains(m))
				throw new StructureException(String.format("AQ0y %s", m));
			
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
	 * Is this abstract?
	 *
	 * @return If this is abstract.
	 * @since 2018/05/13
	 */
	public final boolean isAbstract()
	{
		return this._modifiers.contains(BasicModifierSyntax.ABSTRACT);
	}
	
	/**
	 * Is this final?
	 *
	 * @return If this is final.
	 * @since 2018/05/13
	 */
	public final boolean isFinal()
	{
		return this._modifiers.contains(BasicModifierSyntax.FINAL);
	}
	
	/**
	 * Is this native?
	 *
	 * @return If this is native.
	 * @since 2018/05/13
	 */
	public final boolean isNative()
	{
		return this._modifiers.contains(BasicModifierSyntax.NATIVE);
	}
	
	/**
	 * Is this private?
	 *
	 * @return If this is private.
	 * @since 2018/05/13
	 */
	public final boolean isPrivate()
	{
		return this._modifiers.contains(BasicModifierSyntax.PRIVATE);
	}
	
	/**
	 * Is this protected?
	 *
	 * @return If this is protected.
	 * @since 2018/05/13
	 */
	public final boolean isProtected()
	{
		return this._modifiers.contains(BasicModifierSyntax.PROTECTED);
	}
	
	/**
	 * Is this public?
	 *
	 * @return If this is public.
	 * @since 2018/05/13
	 */
	public final boolean isPublic()
	{
		return this._modifiers.contains(BasicModifierSyntax.PUBLIC);
	}
	
	/**
	 * Is this static?
	 *
	 * @return If this is static.
	 * @since 2018/05/13
	 */
	public final boolean isStatic()
	{
		return this._modifiers.contains(BasicModifierSyntax.STATIC);
	}
	
	/**
	 * Is this using strict floating point?
	 *
	 * @return If this is using struct floating point.
	 * @since 2018/05/13
	 */
	public final boolean isStrictFloatingPoint()
	{
		return this._modifiers.contains(BasicModifierSyntax.STRICTFP);
	}
	
	/**
	 * Is this synchronized?
	 *
	 * @return If this is synchronized.
	 * @since 2018/05/13
	 */
	public final boolean isSynchronized()
	{
		return this._modifiers.contains(BasicModifierSyntax.SYNCHRONIZED);
	}
	
	/**
	 * Is this transient?
	 *
	 * @return If this is transient.
	 * @since 2018/05/13
	 */
	public final boolean isTransient()
	{
		return this._modifiers.contains(BasicModifierSyntax.TRANSIENT);
	}
	
	/**
	 * Is this volatile?
	 *
	 * @return If this is volatile.
	 * @since 2018/05/13
	 */
	public final boolean isVolatile()
	{
		return this._modifiers.contains(BasicModifierSyntax.VOLATILE);
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
			
			// {@squirreljme.error AQ0z Unsupported modifier type. (The type)}
			else
				throw new RuntimeException(String.format("AQ0z %s",
					mod.getClass()));
		}
		
		return new StructureModifiers(rv);
	}
}

