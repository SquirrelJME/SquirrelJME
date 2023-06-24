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
		CType pointedType = this.pointedType;
		
		// Process pointer tokens
		List<String> result = new ArrayList<>();
		
		// Function pointers are a bit different
		if (pointedType instanceof CFunctionType)
		{
			CFunctionType function = (CFunctionType)pointedType;
			
			result.addAll(function.returnType.declareTokens(null));
			result.add("(");
			result.add(this.closeness.token + "*");
			
			// Name of what we refer to is here, not the original function
			// name
			if (__name != null)
				result.add(__name.identifier);
			
			result.add(")");
			
			// Add all arguments, note that the actual argument names are
			// not important here
			result.add("(");
			List<CVariable> arguments = function.arguments;
			for (int i = 0, n = arguments.size(); i < n; i++)
				result.addAll(
					arguments.get(i).type.declareTokens(null));
			
			result.add(")");
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
