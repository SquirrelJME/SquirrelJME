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
import net.multiphasicapps.collections.UnmodifiableList;

/**
 * Represents an identifier in C.
 *
 * @since 2023/06/04
 */
public final class CIdentifier
	implements Comparable<CIdentifier>, CExpression, CTokenizable
{
	/** The string identifier. */
	protected final String identifier;
	
	/**
	 * Initializes the C identifier.
	 * 
	 * @param __identifier The C identifier.
	 * @throws IllegalArgumentException If the identifier contains invalid
	 * characters.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/04
	 */
	CIdentifier(String __identifier)
		throws IllegalArgumentException, NullPointerException
	{
		if (__identifier == null)
			throw new NullPointerException("NARG");
		
		/* {@squirreljme.error CW0a Identifier cannot be blank.} */
		if (__identifier.isEmpty())
			throw new IllegalArgumentException("CW0a");
		
		// Check identifier
		for (int i = 0, n = __identifier.length(); i < n; i++)
		{
			char c = __identifier.charAt(i);
			
			/* {@squirreljme.error CW01 Identifier cannot start with a number.
			(The identifier)} */
			if (i == 0 && c >= '0' && c <= '9')
				throw new IllegalArgumentException("CW01 " + __identifier);
			
			/* {@squirreljme.error CW09 Identifier contains an invalid
			character. (The identifier)} */
			if (!((c >= 'a' && c <= 'z') ||
				(c >= 'A' && c <= 'Z') ||
				(c >= '0' && c <= '9') ||
				c == '_'))
				throw new IllegalArgumentException("CW09 " + __identifier);
		}
		
		// Is fine
		this.identifier = __identifier;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public int compareTo(CIdentifier __b)
	{
		return this.identifier.compareTo(__b.identifier);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (__o == this)
			return true;
		if (!(__o instanceof CIdentifier))
			return false;
		
		return this.identifier.equals(((CIdentifier)__o).identifier);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public int hashCode()
	{
		return this.identifier.hashCode();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public String toString()
	{
		return this.identifier;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/11
	 */
	@Override
	public List<String> tokens()
	{
		return UnmodifiableList.of(Arrays.asList(this.identifier));
	}
	
	/**
	 * Initializes the C identifier.
	 * 
	 * @param __identifier The C identifier.
	 * @throws IllegalArgumentException If the identifier contains invalid
	 * characters.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/04
	 */
	public static CIdentifier of(String __identifier)
		throws IllegalArgumentException, NullPointerException
	{
		return new CIdentifier(__identifier);
	}
}
