// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c;

import java.lang.ref.Reference;
import java.util.ArrayList;
import java.util.List;
import net.multiphasicapps.collections.UnmodifiableList;

/**
 * Represents a type which is modified by a modifier.
 *
 * @since 2023/06/05
 */
public final class CModifiedType
	extends __CAbstractType__
{
	/** The modifier for the type. */
	protected final CModifier modifier;
	
	/** The type. */
	protected final CType type;
	
	/** The tokens that make up this modified type. */
	private volatile Reference<List<String>> _tokens;
	
	/**
	 * Initializes the modified type.
	 * 
	 * @param __modifier The modifier to use.
	 * @param __type The type to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/05
	 */
	private CModifiedType(CModifier __modifier, CType __type)
		throws NullPointerException
	{
		if (__modifier == null || __type == null)
			throw new NullPointerException("NARG");
		
		this.modifier = __modifier;
		this.type = __type;
	}
	
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
	 * @since 2023/06/24
	 */
	@Override
	public List<String> declareTokens(CIdentifier __name)
	{
		CType type = this.type;
		
		List<String> result = new ArrayList<>();
		
		// Pointers are a bit different in their order
		if (type instanceof CPointerType)
		{
			CPointerType pointerType = ((CPointerType)type);
			CType pointedType = pointerType.pointedType;
			
			// Functions and arrays are different
			if (CPointerType.__isComplex(pointedType))
				return CPointerType.__declareComplex(this, __name);
			
			// Otherwise place on right side
			else
			{
				result.addAll(this.type.declareTokens(null));
				result.addAll(this.modifier.tokens());
				
				if (__name != null)
					result.add(__name.identifier);
			}
		}
		
		// Otherwise normal modification, on left side
		else
		{
			result.addAll(this.modifier.tokens());
			result.addAll(this.type.declareTokens(__name));
		}
		
		// Make static first if it is not first
		int staticDx = result.indexOf("static");
		if (staticDx > 0)
		{
			result.remove(staticDx);
			result.add(0, "static");
		}
		
		return UnmodifiableList.of(result);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/05
	 */
	@Override
	public CType dereferenceType()
		throws IllegalArgumentException
	{
		/* {@squirreljme.error CW0j Not a pointer that can be de-referenced.} */
		if (!this.isPointer())
			throw new IllegalArgumentException("CW0j");
		
		// We might be a non-const pointer to a const, in which case if we
		// dereference our own pointer we get the type we are pointing to
		return this.type.dereferenceType();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/05
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (__o == this)
			return true;
		if (!(__o instanceof CModifiedType))
			return false;
		
		CModifiedType o = (CModifiedType)__o;
		return this.type.equals(o.type) &&
			this.modifier.equals(o.modifier);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/05
	 */
	@Override
	public int hashCode()
	{
		return this.modifier.hashCode() ^
			this.type.hashCode();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/05
	 */
	@Override
	public boolean isPointer()
	{
		return this.type.isPointer();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/05
	 */
	@Override
	public CType pointerType()
		throws IllegalArgumentException
	{
		return CPointerType.of(this);
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
				modifiedType.modifier), modifiedType.type);
		}
		
		// Functions are limited in what they can become
		if (__type instanceof CFunctionType)
		{
			/* {@squirreljme.error CW0h Cannot extern a function.} */
			if (CExternModifier.isExtern(__modifier))
				throw new IllegalArgumentException("CW0h");
			
			/* {@squirreljme.error CW3g Cannot const a function.} */
			if (CConstModifier.isConst(__modifier))
				throw new IllegalArgumentException("CW3g");
			
			return new CModifiedType(CStaticModifier.STATIC, __type);
		}
		
		// Arrays cannot be made const
		if (__type instanceof CArrayType)
		{
			/* {@squirreljme.error CW3f Cannot const an array type.} */
			if (CConstModifier.isConst(__modifier))
				throw new IllegalArgumentException("CW3f");
		}
		
		// Build modified type
		return new CModifiedType(__modifier, __type);
	}
}
