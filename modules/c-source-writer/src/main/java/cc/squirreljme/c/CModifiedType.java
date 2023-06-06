// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.List;

/**
 * Represents a type which is modified by a modifier.
 *
 * @since 2023/06/05
 */
public final class CModifiedType
	implements CType
{
	private CModifiedType()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/05
	 */
	@Override
	public CType dereferenceType()
		throws IllegalArgumentException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/05
	 */
	@Override
	public boolean equals(Object __o)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/05
	 */
	@Override
	public int hashCode()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/05
	 */
	@Override
	public int pointerLevel()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/05
	 */
	@Override
	public CType pointerType()
		throws IllegalArgumentException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/05
	 */
	@Override
	public CType rootType()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/05
	 */
	@Override
	public List<String> tokens()
	{
		throw Debugging.todo();
	}
	
	/**
	 * Modifies the given type.
	 * 
	 * @param __modifier The modifier to attach to the type.
	 * @param __type The type to modify.
	 * @return The modified type.
	 * @throws IllegalArgumentException If the type is not valid.
	 * @throws NullPointerException If no type was specified.
	 * @since 2023/06/05
	 */
	public static CType of(CModifier __modifier, CType __type)
		throws IllegalArgumentException, NullPointerException
	{
		if (__type == null)
			throw new NullPointerException("NARG");
		
		// Not actually modifying the type?
		if (__modifier == null)
			return __type;
		
		// If the type is already modified, merge the modifiers together
		if (__type instanceof CModifiedType)
		{
			CModifiedType modifiedType = (CModifiedType)__type;
			return CModifiedType.of(CModifiers.of(__modifier,
				modifiedType.modifiers), modifiedType.baseType);
		}
		
		throw Debugging.todo();
	}
}
