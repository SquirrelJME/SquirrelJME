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

/**
 * Represents an array type.
 *
 * @since 2023/06/24
 */
public final class CArrayType
	extends __CAbstractType__
{
	/** The element type. */
	private final CType elementType;
	
	/**
	 * Initializes the array type.
	 * 
	 * @param __type The type used.
	 * @param __size The size of the array.
	 * @return The type used.
	 * @throws IllegalArgumentException If the length is not valid.
	 * @since 2023/06/24
	 */
	private CArrayType(CType __type, int __size)
		throws IllegalArgumentException
	{
		if (__size < 0)
			throw new IllegalArgumentException("NEGI");
		
		this.elementType = __type;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/24
	 */
	@Override
	public CType arrayType(int __size)
		throws IllegalArgumentException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/24
	 */
	@Override
	public CType constType()
		throws IllegalArgumentException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/24
	 */
	@Override
	public List<String> declareTokens(CIdentifier __name)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/24
	 */
	@Override
	public CType dereferenceType()
		throws IllegalArgumentException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/24
	 */
	@Override
	public boolean isPointer()
	{
		// Arrays are actually pointers!
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/24
	 */
	@Override
	public boolean equals(Object __o)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/24
	 */
	@Override
	public int hashCode()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/24
	 */
	@Override
	public CType pointerType()
		throws IllegalArgumentException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/24
	 */
	@Override
	public CType pointerType(CPointerCloseness __closeness)
		throws IllegalArgumentException
	{
		throw Debugging.todo();
	}
	
	/**
	 * Initializes the array type.
	 * 
	 * @param __type The type used.
	 * @param __size The size of the array.
	 * @return The type used.
	 * @throws IllegalArgumentException If the length is not valid.
	 * @since 2023/06/24
	 */
	public static CType of(CType __type, int __size)
		throws IllegalArgumentException
	{
		if (__size < 0)
			throw new IllegalArgumentException("NEGI");
		
		return new CArrayType(__type, __size);
	}
}
