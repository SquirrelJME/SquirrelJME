// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c;

import cc.squirreljme.c.std.CTypeProvider;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import net.multiphasicapps.collections.UnmodifiableList;

/**
 * Represents a C function.
 *
 * @since 2023/06/04
 */
public class CFunctionType
	extends __CAbstractType__
{
	/** The name of the function. */
	public final CIdentifier name;
	
	/** The return type. */
	public final CType returnType;
	
	/** Arguments to the function. */
	public final List<CVariable> arguments;
	
	/**
	 * Initializes the C function.
	 * 
	 * @param __name The function name.
	 * @param __rVal The return value, may be {@code null}.
	 * @param __args Arguments to the function.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/04
	 */
	private CFunctionType(CIdentifier __name, CType __rVal, CVariable... __args)
		throws NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		// Check arguments for validity
		__args = __args.clone();
		for (CVariable var : __args)
		{
			if (var == null)
				throw new NullPointerException("NARG");
			
			/* {@squirreljme.error CW0d An argument variable type cannot be
			extern or static.} */
			else if (var.isExtern() || var.isStatic())
				throw new IllegalArgumentException("CW0d");
		}
		
		// Setup
		this.name = __name;
		this.returnType = (__rVal == null ? CPrimitiveType.VOID : __rVal);
		this.arguments = UnmodifiableList.of(Arrays.asList(__args));
	}
	
	/**
	 * Returns the argument of the given name.
	 * 
	 * @param __memberName The member to get.
	 * @return The found member.
	 * @throws NoSuchElementException If no such member exists.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/25
	 */
	public CVariable argument(String __memberName)
		throws NoSuchElementException, NullPointerException
	{
		return this.argument(CIdentifier.of(__memberName));
	}
	
	/**
	 * Returns the argument of the given name.
	 * 
	 * @param __memberName The member to get.
	 * @return The found member.
	 * @throws NoSuchElementException If no such member exists.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/25
	 */
	public CVariable argument(CIdentifier __memberName)
		throws NoSuchElementException, NullPointerException
	{
		if (__memberName == null)
			throw new NullPointerException("NARG");
		
		for (CVariable arg : this.arguments)
			if (__memberName.equals(arg.name))
				return arg;
		
		throw new NoSuchElementException("NSEE");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/06
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
		List<String> result = new ArrayList<>();
		
		// Return type
		result.addAll(this.returnType.declareTokens(null));
		
		// Function name
		result.add((__name == null ? this.name.identifier :
			__name.identifier));
		
		// All the arguments
		result.add("(");
		List<CVariable> arguments = this.arguments;
		for (int i = 0, n = arguments.size(); i < n; i++)
		{
			if (i > 0)
				result.add(",");
			
			result.addAll(arguments.get(i).declareTokens());
		}
		result.add(")");
		
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
		/* {@squirreljme.error CW0c Cannot dereference a function.} */
		throw new IllegalArgumentException("CW0c");
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
		return this.name.hashCode() ^
			this.returnType.hashCode() ^
			this.arguments.hashCode();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/05
	 */
	@Override
	public boolean isPointer()
	{
		return false;
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
	 * Renames the function.
	 * 
	 * @param __newIdentifier The new identifier.
	 * @return The newly named function.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/25
	 */
	public CFunctionType rename(String __newIdentifier)
		throws NullPointerException
	{
		return this.rename(CIdentifier.of(__newIdentifier));
	}
	
	/**
	 * Renames the function.
	 * 
	 * @param __newIdentifier The new identifier.
	 * @return The newly named function.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/25
	 */
	public CFunctionType rename(CIdentifier __newIdentifier)
		throws NullPointerException
	{
		if (__newIdentifier == null)
			throw new NullPointerException("NARG");
		
		return new CFunctionType(__newIdentifier, this.returnType,
			this.arguments.toArray(new CVariable[this.arguments.size()]));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/09
	 */
	@Override
	public String toString()
	{
		return this.declareTokens(null).toString();
	}
	
	/**
	 * Initializes the C function.
	 * 
	 * @param __name The function name.
	 * @param __rVal The return value, may be {@code null}.
	 * @param __args Arguments to the function.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/24
	 */
	public static CFunctionType of(String __name, CType __rVal,
		CVariable... __args)
		throws NullPointerException
	{
		return CFunctionType.of(CIdentifier.of(__name), __rVal, __args);
	}
	
	/**
	 * Initializes the C function.
	 * 
	 * @param __name The function name.
	 * @param __rVal The return value, may be {@code null}.
	 * @param __args Arguments to the function.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/16
	 */
	public static CFunctionType of(String __name, CTypeProvider __rVal,
		CVariable... __args)
		throws NullPointerException
	{
		return CFunctionType.of(CIdentifier.of(__name), __rVal, __args);
	}
	
	/**
	 * Initializes the C function.
	 * 
	 * @param __name The function name.
	 * @param __rVal The return value, may be {@code null}.
	 * @param __args Arguments to the function.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/04
	 */
	public static CFunctionType of(CIdentifier __name, CType __rVal,
		CVariable... __args)
		throws NullPointerException
	{
		return new CFunctionType(__name, __rVal, __args);
	}
	
	/**
	 * Initializes the C function.
	 * 
	 * @param __name The function name.
	 * @param __rVal The return value, may be {@code null}.
	 * @param __args Arguments to the function.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/16
	 */
	public static CFunctionType of(CIdentifier __name, CTypeProvider __rVal,
		CVariable... __args)
		throws NullPointerException
	{
		if (__rVal == null)
			throw new NullPointerException("NARG");
		
		return CFunctionType.of(__name, __rVal.type(), __args);
	}
}
