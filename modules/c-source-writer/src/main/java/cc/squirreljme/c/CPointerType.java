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
		
		// Function pointers and arrays need some work
		if (CPointerType.__isComplex(pointedType))
			return CPointerType.__declareComplex(this, __name);
		
		// Simpler type used
		else
		{
			// Process pointer tokens
			List<String> result = new ArrayList<>();
			
			// Pointers are to the right, generally
			result.addAll(pointedType.declareTokens(null));
			result.add(this.closeness.token + "*");
			
			if (__name != null)
				result.add(__name.identifier);
			
			return UnmodifiableList.of(result);
		}
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
	 * Casts the dereferenced type.
	 * 
	 * @param <T> The type to cast as.
	 * @param __type The type to cast as.
	 * @return The casted dereferenced type.
	 * @since 2023/07/03
	 */
	public <T extends CType> T dereferenceType(Class<T> __type)
		throws IllegalArgumentException
	{
		return __type.cast(this.pointedType);
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
				/* {@squirreljme.error CW0i Cannot pointer an extern or
				static type.} */
				CModifiedType modifiedType = (CModifiedType)__type;
				if (modifiedType.modifier instanceof CExternModifier ||
					modifiedType.modifier instanceof CStaticModifier)
					throw new IllegalArgumentException("CW0i");
			}
		}
		
		// Just wrap in a pointer
		return new CPointerType(__type, __closeness);
	}
	
	/**
	 * Declaration loop.
	 *
	 * @param __start The starting type.
	 * @param __name The name of the type to use.
	 * @return The resultant declared tokens.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/25
	 */
	static List<String> __declareComplex(CType __start, CIdentifier __name)
		throws NullPointerException
	{
		if (__start == null)
			throw new NullPointerException("NARG");
		
		// Resultant tokens
		List<String> result = new ArrayList<>();
		
		// We need to know if this refers to a function or not, but while
		// we do so load all the types we are hitting here
		CFunctionType baseFunction = null;
		CType rootType = null;
		List<CType> allTypes = new ArrayList<>();
		boolean lastModified = false;
		for (CType at = __start;;)
		{
			// Debug
			if (false)
				Debugging.debugNote("at: %s", at);
			
			// Add type to all of them, only if the type was not modified
			if (!lastModified)
				allTypes.add(at);
			
			// Dereference modified, pointer, and array types
			if (at instanceof CModifiedType)
			{
				lastModified = true;
				rootType = at;
				at = ((CModifiedType)at).type;
				
				// Debug
				if (false)
					Debugging.debugNote("at (modified): %s", at);
			}
			else if (at instanceof CPointerType)
			{
				lastModified = false;
				at = ((CPointerType)at).pointedType;
				
				// Debug
				if (false)
					Debugging.debugNote("at (pointer): %s", at);
			}
			else if (at instanceof CArrayType)
			{
				lastModified = false;
				at = ((CArrayType)at).elementType;
				
				// Debug
				if (false)
					Debugging.debugNote("at (array): %s", at);
			}
			
			// Is function pointer?
			else if (at instanceof CFunctionType)
			{
				// This is the root type, we cannot go deeper
				lastModified = false;
				baseFunction = (CFunctionType)at;
				rootType = at;
				break;
			}
			
			// We can stop as we reached a root type
			else
			{
				if (!lastModified)
					rootType = at;
				lastModified = false;
				break;
			}
		}
		
		// Debug
		if (false)
			Debugging.debugNote("All: %s", allTypes);
		
		// Return type, if a function? Also need to handle ending arrays
		List<CArrayType> endArrays = new ArrayList<>();
		if (baseFunction != null)
		{
			// Collect all the subsequent array types
			CType returnType = baseFunction.returnType;
			for (;;)
			{
				if (returnType instanceof CArrayType)
				{
					CArrayType returnArray = (CArrayType)returnType;
					
					endArrays.add(0, returnArray);
					returnType = returnArray.elementType;
				}
				
				// Stop here
				else
					break;
			}
			
			// Add the return type, with arrays removed
			result.addAll(returnType.declareTokens(null));
		}
		
		// Otherwise whatever the element type is, assuming array
		else
			result.addAll(rootType.declareTokens(null));
		
		// Pointers and arrays are like onions, they have layers
		List<__PointerOnion__> layers = new ArrayList<>();
		__PointerOnion__ currentLayer = new __PointerOnion__();
		layers.add(currentLayer);
		
		// Go through all type items and add around them
		CPointerType lastPointer = null;
		CArrayType lastArray = null;
		for (int n = allTypes.size(), i = n - 1; i >= 0; i--)
		{
			CType at = allTypes.get(i);
			
			// Un-modify types, they are added in the chain accordingly
			CModifier modifier = null;
			if (at instanceof CModifiedType)
			{
				CModifiedType modifiedType = (CModifiedType)at;
				
				modifier = modifiedType.modifier;
				at = modifiedType.type;
			}
			
			// Determine which type this is
			CPointerType nowPointer = null;
			CArrayType nowArray = null;
			if (at instanceof CPointerType)
				nowPointer = (CPointerType)at;
			else if (at instanceof CArrayType)
				nowArray = (CArrayType)at;
			
			// Arrays are always last, so if we are going from an array to
			// a pointer, we need to wrap with parenthesis
			if (nowPointer != null && lastArray != null)
			{
				currentLayer = new __PointerOnion__();
				layers.add(currentLayer);
			}
			
			// Only perform calculation if we have the both of these
			if (nowPointer != null || nowArray != null)
			{
				// Pointer?
				if (nowPointer != null)
				{
					currentLayer.left.add(nowPointer.closeness.token + "*");
					
					// All modifiers after the star
					if (modifier != null)
						currentLayer.left.addAll(modifier.tokens());
				}
				
				// Array?
				else if (nowArray != null)
				{
					currentLayer.right.add("[");
					if (nowArray.size != CArrayType.UNDEFINED_SIZE)
						currentLayer.right.add(
							Integer.toString(nowArray.size, 10));
					currentLayer.right.add("]");
				}
				
				// Store last state, for future potential parenthesis
				lastPointer = nowPointer;
				lastArray = nowArray;
			}
		}
		
		// Debug
		if (false)
			Debugging.debugNote("Layers: %s", layers);
		
		// Clamped value in the middle
		List<String> clamped = new ArrayList<>();
		if (__name != null)
			clamped.add(__name.identifier);
		
		// Go through each onion and add accordingly
		for (int n = layers.size(), i = n - 1; i >= 0; i--)
		{
			__PointerOnion__ layer = layers.get(i);
			
			// Only surround with parenthesis if we are on another layer
			if (i < n - 1)
			{
				clamped.add(0, "(");
				clamped.add(clamped.size(), ")");
			}
			
			clamped.addAll(0, layer.left);
			clamped.addAll(clamped.size(), layer.right);
		}
		
		// Build final result, function pointers always have a surround
		if (baseFunction != null)
			result.add("(");
		result.addAll(clamped);
		if (baseFunction != null)
			result.add(")");
		
		// Add all arguments if a function, note that the actual argument
		// names are not important here
		if (baseFunction != null)
		{
			result.add("(");
			List<CVariable> args = baseFunction.arguments;
			for (int i = 0, n = args.size(); i < n; i++)
				result.addAll(args.get(i).type.declareTokens(null));
			result.add(")");
		}
		
		// Arrays at the very end for return types?
		for (CArrayType endArray : endArrays)
		{
			result.add("[");
			if (endArray.size != CArrayType.UNDEFINED_SIZE)
				result.add(Integer.toString(endArray.size, 10));
			result.add("]");
		}
		
		return UnmodifiableList.of(result);
	}
	
	/**
	 * Is this a complex type?
	 * 
	 * @param __start The type to check.
	 * @return If this is a complex type or not.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/25
	 */
	static boolean __isComplex(CType __start)
		throws NullPointerException
	{
		if (__start == null)
			throw new NullPointerException("NARG");
		
		for (CType at = __start;;)
		{
			// Dereference modified and pointer types
			if (at instanceof CModifiedType)
				at = ((CModifiedType)at).type;
			else if (at instanceof CPointerType)
				at = ((CPointerType)at).pointedType;
			
			// These two are complex
			else if (at instanceof CArrayType ||
				at instanceof CFunctionType)
				return true;
			
			// Should be at root type
			else
				break;
		}
		
		// Not one
		return false;
	}
}
