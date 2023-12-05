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
 * An opaque type which is something but not really known.
 *
 * @since 2023/06/24
 */
public final class COpaqueType
	extends __CAbstractType__
{
	/** The identifier used for this type. */
	protected final CIdentifier identifier;
	
	/** Is this a pointer type? */
	protected final boolean isPointer;
	
	/**
	 * Initializes the opaque type.
	 * 
	 * @param __identifier The identifier of the type.
	 * @param __isPointer Is this a pointer?
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/24
	 */
	private COpaqueType(CIdentifier __identifier, boolean __isPointer)
		throws NullPointerException
	{
		if (__identifier == null)
			throw new NullPointerException("NARG");
		
		this.identifier = __identifier;
		this.isPointer = __isPointer;
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
	 * @since 2023/06/24
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		if (!(__o instanceof COpaqueType))
			return false;
		
		COpaqueType o = (COpaqueType)__o;
		return this.isPointer == o.isPointer &&
			this.identifier.equals(o.identifier);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/24
	 */
	@Override
	public int hashCode()
	{
		return this.identifier.hashCode() + (this.isPointer ? 1 : 0);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/24
	 */
	@Override
	public CType dereferenceType()
		throws IllegalArgumentException
	{
		/* {@squirreljme.error CW23 Cannot throw an opaque type.} */
		throw new IllegalArgumentException("CW23");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/24
	 */
	@Override
	public boolean isPointer()
	{
		return this.isPointer;
	}
	
	/**
	 * Builds an opaque type.
	 * 
	 * @param __token The token used.
	 * @param __isPointer Is this a pointer type?
	 * @return The opaque type.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/24
	 */
	public static CType of(String __token, boolean __isPointer)
		throws NullPointerException
	{
		return COpaqueType.of(CIdentifier.of(__token), __isPointer);
	}
	
	/**
	 * Builds an opaque type.
	 * 
	 * @param __token The token used.
	 * @param __isPointer Is this a pointer type?
	 * @return The opaque type.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/24
	 */
	public static CType of(CIdentifier __token, boolean __isPointer)
		throws NullPointerException
	{
		if (__token == null)
			throw new NullPointerException("NARG");
		
		return new COpaqueType(__token, __isPointer);
	}
}
