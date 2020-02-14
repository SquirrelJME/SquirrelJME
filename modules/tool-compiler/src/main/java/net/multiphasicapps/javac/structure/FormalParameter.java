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
import net.multiphasicapps.classfile.FieldName;

/**
 * This represents a single formal parameter which is used as input for a
 * method.
 *
 * @since 2018/05/10
 */
@Deprecated
public final class FormalParameter
{
	/** The name of the parameter. */
	protected final FieldName name;
	
	/**
	 * Initializes the single formal parameter.
	 *
	 * @param __ams The annotations which modify the parameter.
	 * @param __final Is this parameter final?
	 * @param __type The type of the parameter.
	 * @param __name The name of the parameter.
	 * @throws NullPointerException On null arguments.
	 * @throws StructureException If the formal parameter is not valid.
	 * @since 2018/05/10
	 */
	public FormalParameter(AnnotationModifier[] __ams, boolean __final,
		TypeSymbol __type, FieldName __name)
		throws NullPointerException, StructureException
	{
		this(Arrays.<AnnotationModifier>asList((__ams == null ?
			new AnnotationModifier[0] : __ams)), __final, __type, __name);
	}
	
	/**
	 * Initializes the single formal parameter.
	 *
	 * @param __ams The annotations which modify the parameter.
	 * @param __final Is this parameter final?
	 * @param __type The type of the parameter.
	 * @param __name The name of the parameter.
	 * @throws NullPointerException On null arguments.
	 * @throws StructureException If the formal parameter is not valid.
	 * @since 2018/05/10
	 */
	public FormalParameter(Iterable<AnnotationModifier> __ams, boolean __final,
		TypeSymbol __type, FieldName __name)
		throws NullPointerException, StructureException
	{
		if (__ams == null || __type == null || __name == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
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
	 * Returns the name of the parameter.
	 *
	 * @return The parameter name.
	 * @since 2018/05/10
	 */
	public final FieldName name()
	{
		return this.name;
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
}

