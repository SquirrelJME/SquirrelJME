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
 * Represents a pointer type.
 *
 * @since 2023/05/29
 */
public class CPointerType
	extends __CAbstractType__
{
	/** The type this points to. */
	protected final CType pointedType;
	
	/** The closeness of this pointer. */
	protected final CPointerCloseness closeness;
	
	/** The token representation of this type. */
	private volatile Reference<List<String>> _tokens;
	
	/**
	 * Initializes the pointer type.
	 * 
	 * @param __pointedType The type to point to.
	 * @param __closeness The closeness of the pointer.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/29
	 */
	private CPointerType(CType __pointedType, CPointerCloseness __closeness)
		throws IllegalArgumentException, NullPointerException
	{
		if (__pointedType == null || __closeness == null)
			throw new NullPointerException("NARG");
		
		this.pointedType = __pointedType;
		this.closeness = __closeness;
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
		// Process pointer tokens
		List<String> result = new ArrayList<>();
		
		// Pivot from modified type?
		CType pointedType = this.pointedType;
		CType pivotType = pointedType;
		CModifier modifier = null;
		if (pointedType instanceof CModifiedType)
		{
			CModifiedType modified = (CModifiedType)pointedType;
			
			pivotType = modified.type;
			modifier = modified.modifier;
		}
		
		// Function pointers are a bit different
		if (pivotType instanceof CFunctionType)
		{
			return this.__declareFunction(result, __name,
				(CFunctionType)pivotType, modifier, -1);
		}
		
		// Arrays are also different as well
		else if (pivotType instanceof CArrayType)
		{
			return this.__declareArray(result, __name,
				(CArrayType)pivotType, modifier, -1);
		}
		
		// Simpler type used
		else
		{
			// Pointers are to the right, generally
			result.addAll(pointedType.declareTokens(null));
			result.add(this.closeness.token + "*");
			
			if (__name != null)
				result.add(__name.identifier);
		}
		
		return UnmodifiableList.of(result);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/29
	 */
	@Override
	public CType dereferenceType()
		throws IllegalArgumentException
	{
		return this.pointedType;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/29
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (__o == this)
			return true;
		if (!(__o instanceof CPointerType))
			return false;
		
		CPointerType o = (CPointerType)__o;
		return this.pointedType.equals(o.pointedType) &&
			this.closeness.equals(o.closeness);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/29
	 */
	@Override
	public int hashCode()
	{
		return this.pointedType.hashCode() ^
			this.closeness.hashCode();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/05
	 */
	@Override
	public boolean isPointer()
	{
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/29
	 */
	@Override
	public CType pointerType()
		throws IllegalArgumentException
	{
		return CPointerType.of(this);
	}
	
	/**
	 * Declares an array.
	 *
	 * @param __result The output result.
	 * @param __name The identifier name.
	 * @param __arrayType The array type.
	 * @param __modifier The modifier.
	 * @param __arraySize The size of the array.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/14
	 */
	List<String> __declareArray(List<String> __result,
		CIdentifier __name, CArrayType __arrayType, CModifier __modifier,
		int __arraySize)
		throws NullPointerException
	{
		if (__result == null || __arrayType == null)
			throw new NullPointerException("NARG");
		
		__result.addAll(__arrayType.elementType.declareTokens(null));
		__result.add("(");
		__result.add(this.closeness.token + "*");
		
		// All modifiers after the star
		if (__modifier != null)
			__result.addAll(__modifier.tokens());
		
		// Name of what we refer to is here, not the original function
		// name
		if (__name != null)
			__result.add(__name.identifier);
		
		// Array of pointers to array?
		if (__arraySize >= 0)
		{
			__result.add("[");
			__result.add(Integer.toString(__arraySize, 10));
			__result.add("]");
		}
		
		__result.add(")");
		
		// Array size
		__result.add("[");
		__result.add(Integer.toString(__arrayType.size, 10));
		__result.add("]");
		
		return UnmodifiableList.of(__result);
	}
	
	/**
	 * Declares a function.
	 *
	 * @param __result The output result.
	 * @param __name The identifier name.
	 * @param __pointedType The pointed type.
	 * @param __modifier The modifier.
	 * @param __arraySize The size of the array.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/14
	 */
	List<String> __declareFunction(List<String> __result,
		CIdentifier __name, CFunctionType __pointedType,
		CModifier __modifier, int __arraySize)
		throws NullPointerException
	{
		if (__result == null || __pointedType == null)
			throw new NullPointerException("NARG");
		
		__result.addAll(__pointedType.returnType.declareTokens(null));
		__result.add("(");
		__result.add(this.closeness.token + "*");
		
		// All modifiers after the star
		if (__modifier != null)
			__result.addAll(__modifier.tokens());
		
		// Name of what we refer to is here, not the original function
		// name
		if (__name != null)
			__result.add(__name.identifier);
		
		// Array of pointers to array of function pointers?
		if (__arraySize >= 0)
		{
			__result.add("[");
			__result.add(Integer.toString(__arraySize, 10));
			__result.add("]");
		}
		
		__result.add(")");
		
		// Add all arguments, note that the actual argument names are
		// not important here
		__result.add("(");
		List<CVariable> arguments = __pointedType.arguments;
		for (int i = 0, n = arguments.size(); i < n; i++)
			__result.addAll(
				arguments.get(i).type.declareTokens(null));
		
		__result.add(")");
		
		return UnmodifiableList.of(__result);
	}
	
	/**
	 * Initializes the pointer type.
	 * 
	 * @param __type The type to use.
	 * @throws IllegalArgumentException If the resultant type is invalid.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/29
	 */
	public static CType of(CType __type)
		throws IllegalArgumentException, NullPointerException
	{
		return CPointerType.of(__type, CPointerCloseness.NEAR);
	}
	
	/**
	 * Initializes the pointer type.
	 * 
	 * @param __type The type to use.
	 * @throws IllegalArgumentException If the resultant type is invalid.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/24
	 */
	public static CType of(CType __type, CPointerCloseness __closeness)
		throws IllegalArgumentException, NullPointerException
	{
		if (__type == null || __closeness == null)
			throw new NullPointerException("NARG");
		
		// If the root type is not a basic type, we always want to classify
		// pointer levels based on that for cache purposes, possibly anyway
		if (!(__type instanceof CPrimitiveType))
		{
			// There are restrictions as to what can get a pointer of
			if (__type instanceof CModifiedType)
			{
				// {@squirreljme.error CW0i Cannot pointer an extern or
				// static type.}
				CModifiedType modifiedType = (CModifiedType)__type;
				if (modifiedType.modifier instanceof CExternModifier ||
					modifiedType.modifier instanceof CStaticModifier)
					throw new IllegalArgumentException("CW0i");
			}
		}
		
		// Just wrap in a pointer
		return new CPointerType(__type, __closeness);
	}
}
