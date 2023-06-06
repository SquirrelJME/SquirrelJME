// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c;

import java.util.List;

/**
 * Base class for non-basic C types.
 *
 * @since 2023/06/06
 */
public abstract class __CAbstractType__
	implements CType
{
	/** The token cache. */
	private final __CTokenSetCache__ _tokenCache =
		new __CTokenSetCache__();
	
	/**
	 * Generates the set of tokens for this type.
	 * 
	 * @param __set The set to generate for.
	 * @return The resultant tokens.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/06
	 */
	abstract List<String> __generateTokens(CTokenSet __set)
		throws NullPointerException;
	
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
	 * @since 2023/06/06
	 */
	@Override
	public CType pointerType()
		throws IllegalArgumentException
	{
		return CPointerType.of(this);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/06
	 */
	@Override
	public final List<String> tokens(CTokenSet __set)
		throws NullPointerException
	{
		return this._tokenCache.__get(__set, this);
	}
}
