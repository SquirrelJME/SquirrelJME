// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.interpreter;

import java.util.Map;
import java.util.Objects;
import net.multiphasicapps.descriptors.IdentifierSymbol;
import net.multiphasicapps.descriptors.MemberTypeSymbol;

/**
 * This represents an indexable member within a class which is associated
 * with an identifier name and a type.
 *
 * This is used as a key internally.
 *
 * @param <S> The type of symbol to use for the value.
 * @since 2016/03/17
 */
public final class CFMemberKey<S extends MemberTypeSymbol>
	implements Map.Entry<IdentifierSymbol, S>
{
	/** The name of this entry. */
	protected final IdentifierSymbol name;
	
	/** The type of this entry. */
	protected final S type;
	
	/**
	 * Initializes the member entry key.
	 *
	 * @param __id The identifier of the entry.
	 * @param __ty The type of entry to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/17
	 */
	public CFMemberKey(IdentifierSymbol __id, S __ty)
		throws NullPointerException
	{
		// Check
		if (__id == null || __ty == null)
			throw new NullPointerException("NARG");
		
		// Set
		name = __id;
		type = __ty;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/17
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Not another entry?
		if (!(__o instanceof Map.Entry))
			return false;
		
		// Cast and check
		Map.Entry<?, ?> o = (Map.Entry<?, ?>)__o;
		return Objects.equals(getKey(), o.getKey()) &&
			Objects.equals(getValue(), o.getValue());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/17
	 */
	@Override
	public IdentifierSymbol getKey()
	{
		return name;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/17
	 */
	@Override
	public S getValue()
	{
		return type;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/17
	 */
	@Override
	public int hashCode()
	{
		return Objects.hashCode(getKey()) ^ Objects.hashCode(getValue());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/17
	 */
	@Override
	public S setValue(S __v)
	{
		throw new UnsupportedOperationException("RORO");
	}
}

