// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c;

import java.util.ArrayList;
import java.util.List;
import net.multiphasicapps.collections.UnmodifiableList;

/**
 * Represents an array type.
 *
 * @since 2023/06/24
 */
public final class CArrayType
	extends __CAbstractType__
{
	/** The array is of undefined size. */
	public static final int UNDEFINED_SIZE =
		Integer.MIN_VALUE;
	
	/** The element type. */
	protected final CType elementType;
	
	/** The size of the array. */
	protected final int size;
	
	/**
	 * Initializes the array type.
	 * 
	 * @param __type The type used.
	 * @param __size The size of the array.
	 * @throws IllegalArgumentException If the length is not valid.
	 * @since 2023/06/24
	 */
	private CArrayType(CType __type, int __size)
		throws IllegalArgumentException
	{
		if (__size < 0 && __size != CArrayType.UNDEFINED_SIZE)
			throw new IllegalArgumentException("NEGI");
		
		this.elementType = __type;
		this.size = __size;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/24
	 */
	@Override
	public CType constType()
		throws IllegalArgumentException
	{
		/* {@squirreljme.error CW02 Cannot have a const function.} */
		throw new IllegalArgumentException("CW02");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/24
	 */
	@Override
	public List<String> declareTokens(CIdentifier __name)
	{
		// Is this a complex type?
		if (CPointerType.__isComplex(this))
			return CPointerType.__declareComplex(this, __name);
	
		// Setup builder
		List<String> result = new ArrayList<>();	
		
		// Type and such
		CType elementType = this.elementType;
		result.addAll(elementType.declareTokens(__name));
		
		// Array size
		result.add("[");
		if (this.size != CArrayType.UNDEFINED_SIZE)
			result.add(Integer.toString(this.size, 10));
		result.add("]");
		
		return UnmodifiableList.of(result);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/24
	 */
	@Override
	public CType dereferenceType()
		throws IllegalArgumentException
	{
		// Dereferences to the element type
		return this.elementType;
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
		if (this == __o)
			return true;
		if (!(__o instanceof CArrayType))
			return false;
		
		CArrayType o = (CArrayType)__o;
		return this.size == o.size &&
			this.elementType.equals(o.elementType);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/24
	 */
	@Override
	public int hashCode()
	{
		return this.elementType.hashCode() + this.size;
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
		if (__size < 0 && __size != CArrayType.UNDEFINED_SIZE)
			throw new IllegalArgumentException("NEGI");
		
		/* {@squirreljme.error CW3h Cannot get array of function.} */
		if (__type instanceof CFunctionType)
			throw new IllegalArgumentException("CW3h");
		
		// Some modifiers are not valid, it makes no sense to have an array
		// static or extern, but there can be an extern/static array.
		if (__type instanceof CModifiedType)
		{
			CModifier modifier = ((CModifiedType)__type).modifier;
			
			/* {@squirreljme.error CW3l Cannot array an extern or static.} */
			if (CExternModifier.isExtern(modifier) ||
				CStaticModifier.isStatic(modifier))
				throw new IllegalArgumentException("CW3l");
		}
		
		// Create
		return new CArrayType(__type, __size);
	}
}
