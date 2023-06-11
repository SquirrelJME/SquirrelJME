// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Represents a structure type, which will have multiple members as types.
 *
 * @since 2023/06/06
 */
public class CStructType
	extends __CAbstractType__
{
	/**
	 * {@inheritDoc}
	 * @since 2023/06/06
	 */
	@Override
	public CType constType()
		throws IllegalArgumentException
	{
		return CModifiedType.of(CConstModifier.CONST, this);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/06
	 */
	@Override
	public CType dereferenceType()
		throws IllegalArgumentException
	{
		// {@squirreljme.error CW04 Cannot dereference a struct.}
		throw new IllegalArgumentException("CW04");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/06
	 */
	@Override
	public boolean equals(Object __o)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/06
	 */
	@Override
	public int hashCode()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/06
	 */
	@Override
	public boolean isPointer()
	{
		throw Debugging.todo();
	}
	
	/**
	 * Returns the member by the index.
	 * 
	 * @param __dx The index to get.
	 * @return The variable that is the member.
	 * @throws IndexOutOfBoundsException If the member index is not valid.
	 * @since 2023/06/03
	 */
	public CVariable member(int __dx)
		throws IndexOutOfBoundsException
	{
		if (__dx < 0)
			throw new IndexOutOfBoundsException("IOOB");
		
		throw Debugging.todo();
	}
	
	/**
	 * Returns the member by the given name.
	 * 
	 * @param __identifier The name of the member.
	 * @return The variable that is the member.
	 * @throws IllegalArgumentException If the member is not valid.
	 * @throws NoSuchElementException If no such member exists.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/06
	 */
	public CVariable member(String __identifier)
		throws IllegalArgumentException, NoSuchElementException,
			NullPointerException
	{
		return this.member(new CIdentifier(__identifier));
	}
	
	/**
	 * Returns the member by the given name.
	 * 
	 * @param __identifier The name of the member.
	 * @return The variable that is the member.
	 * @throws NoSuchElementException If no such member exists.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/06
	 */
	public CVariable member(CIdentifier __identifier)
		throws NoSuchElementException, NullPointerException
	{
		if (__identifier == null)
			throw new NullPointerException("NARG");
		
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/06
	 */
	@Override
	public CType pointerType()
		throws IllegalArgumentException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/06
	 */
	@Override
	List<String> __generateTokens(CTokenSet __set)
		throws NullPointerException
	{
		throw Debugging.todo();
	}
}
