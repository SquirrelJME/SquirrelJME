// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c;

import java.util.Arrays;
import java.util.List;

/**
 * This is a type that is a typedef of another type.
 *
 * @since 2023/06/06
 */
public final class CTypeDefType
	extends __CAbstractType__
{
	/** The base type of the typedef. */
	protected final CType baseType;
	
	/** The name of the typedef. */
	protected final CIdentifier identifier;
	
	/**
	 * Initializes the new type.
	 * 
	 * @param __baseType The base type.
	 * @param __identifier The identifier for the new type.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/24
	 */
	private CTypeDefType(CType __baseType, CIdentifier __identifier)
		throws NullPointerException
	{
		if (__baseType == null || __identifier == null)
			throw new NullPointerException("NARG");
		
		this.baseType = __baseType;
		this.identifier = __identifier;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/24
	 */
	@Override
	public List<String> declareTokens(CIdentifier __name)
	{
		if (__name == null)
			return Arrays.asList(this.identifier.identifier);
		return Arrays.asList(this.identifier.identifier, __name.identifier);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/06
	 */
	@Override
	public CType dereferenceType()
		throws IllegalArgumentException
	{
		return this.baseType.dereferenceType();
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
		if (!(__o instanceof CTypeDefType))
			return false;
		
		CTypeDefType o = (CTypeDefType)__o;
		return this.baseType.equals(o.baseType) &&
			this.identifier.equals(o.identifier);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/24
	 */
	@Override
	public int hashCode()
	{
		return this.baseType.hashCode() ^
			this.identifier.hashCode();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/06
	 */
	@Override
	public boolean isPointer()
	{
		return this.baseType.isPointer();
	}
	
	/**
	 * Initializes the new type.
	 * 
	 * @param __baseType The base type.
	 * @param __identifier The identifier for the new type.
	 * @return The created type.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/24
	 */
	public static CType of(CType __baseType, String __identifier)
		throws NullPointerException
	{
		return CTypeDefType.of(__baseType, CIdentifier.of(__identifier));
	}
	
	/**
	 * Initializes the new type.
	 * 
	 * @param __baseType The base type.
	 * @param __identifier The identifier for the new type.
	 * @return The created type.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/24
	 */
	public static CType of(CType __baseType, CIdentifier __identifier)
		throws NullPointerException
	{
		if (__baseType == null || __identifier == null)
			throw new NullPointerException("NARG");
		
		return new CTypeDefType(__baseType, __identifier);
	}
}
