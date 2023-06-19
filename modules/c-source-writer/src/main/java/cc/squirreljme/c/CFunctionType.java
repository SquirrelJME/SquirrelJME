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
import java.util.Arrays;
import java.util.List;
import net.multiphasicapps.collections.UnmodifiableList;

/**
 * Represents a C function.
 *
 * @since 2023/06/04
 */
public class CFunctionType
	extends __CAbstractType__
	implements CDeclarable, CDefinable
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
			
			// {@squirreljme.error CW0d An argument variable type cannot be
			// extern or static.}
			else if (var.isExtern() || var.isStatic())
				throw new IllegalArgumentException("CW0d");
		}
		
		// Setup
		this.name = __name;
		this.returnType = (__rVal == null ? CPrimitiveType.VOID : __rVal);
		this.arguments = UnmodifiableList.of(Arrays.asList(__args));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/06
	 */
	@Override
	public CType constType()
		throws IllegalArgumentException
	{
		// {@squirreljme.error CW02 Cannot have a const function.}
		throw new IllegalArgumentException("CW02");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/05
	 */
	@Override
	public CType dereferenceType()
		throws IllegalArgumentException
	{
		// {@squirreljme.error CW0c Cannot dereference a function.}
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
	 * {@inheritDoc}
	 * @since 2023/06/19
	 */
	@Override
	public List<String> tokens(CNamedTokenSet __set, CIdentifier __name)
		throws NotTokenizableException, NullPointerException
	{
		throw Debugging.todo();
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
}
