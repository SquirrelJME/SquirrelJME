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
import java.lang.ref.WeakReference;
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
	
	/** The token representation of this type. */
	private volatile Reference<List<String>> _tokens;
	
	/**
	 * Initializes the pointer type.
	 * 
	 * @param __pointedType The type to point to.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/29
	 */
	private CPointerType(CType __pointedType)
		throws IllegalArgumentException, NullPointerException
	{
		if (__pointedType == null)
			throw new NullPointerException("NARG");
		
		this.pointedType = __pointedType;
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
		return this.pointedType.equals(o.pointedType);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/29
	 */
	@Override
	public int hashCode()
	{
		return this.pointedType.hashCode();
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
	 * {@inheritDoc}
	 * @since 2023/05/29
	 */
	@Override
	public List<String> tokens()
	{
		Reference<List<String>> ref = this._tokens;
		List<String> rv;
		
		if (ref == null || (rv = ref.get()) == null)
		{
			List<String> build = new ArrayList<>();
			
			// Determine if this ultimately leads to a function
			CType root = this.pointedType;
			int functionDepth = 0;
			CFunctionType function = null;
			for (CType at = root;;)
			{
				// This is a function pointer
				if (at instanceof CFunctionType)
				{
					function = (CFunctionType)at;
					break;
				}
				
				// Go down the chain
				if (at.isPointer())
				{
					functionDepth++;
					at = at.dereferenceType();
				}
				else
					break;
			}
			
			// Functions are a bit different
			if (function != null)
			{
				// Return type, all the tokens used for it
				build.addAll(function.returnType.tokens());
				
				// Add function surround
				// The more pointers on the function,
				// the more stars attached: void (*foo)() -> void (**foo)()
				build.add("(");
				for (int i = 0; i <= functionDepth; i++)
					build.add("*");
				build.add(function.name.identifier);
				build.add(")");
				
				// Add all arguments
				build.add("(");
				List<CVariable> arguments = function.arguments;
				for (int i = 0, n = arguments.size(); i < n; i++)
				{
					if (i > 0)
						build.add(",");
					
					// We do not care about the parameter names for functions,
					// only their types
					build.addAll(arguments.get(i).type.tokens());
				}
				build.add(")");
			}
			
			// Star follows the type
			else
			{
				build.addAll(root.tokens());
				build.add("*");
			}
			
			// Build and cache
			rv = UnmodifiableList.of(build);
			this._tokens = new WeakReference<>(rv);
		}
			
		return rv;
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
		if (__type == null)
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
		return new CPointerType(__type);
	}
}
