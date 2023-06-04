// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c;

import java.util.ArrayList;
import java.util.List;
import net.multiphasicapps.collections.UnmodifiableList;

/**
 * Multiple modifiers.
 *
 * @since 2023/05/29
 */
public class CModifiers
	implements CModifier
{
	/** Extern const as it is very common. */
	public static final CModifiers EXTERN_CONST =
		CModifiers.of(CBasicModifier.EXTERN, CBasicModifier.CONST);
	
	/** All the tokens combined. */
	private final List<String> _tokens;
	
	/**
	 * Initializes multiple modifiers.
	 * 
	 * @param __of The modifiers to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/29
	 */
	CModifiers(CModifier... __of)
		throws NullPointerException
	{
		if (__of == null || __of.length == 0)
			throw new NullPointerException("NARG");
		
		// Add all the tokens together
		List<String> result = new ArrayList<>();
		for (CModifier modifier : __of)
			result.addAll(modifier.modifierTokens());
		
		this._tokens = UnmodifiableList.of(result);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/29
	 */
	@Override
	public List<String> modifierTokens()
	{
		return this._tokens;
	}
	
	/**
	 * Initializes multiple modifiers.
	 * 
	 * @param __of The modifiers to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/29
	 */
	public static CModifiers of(CModifier... __of)
		throws NullPointerException
	{
		if (__of == null || __of.length == 0)
			throw new NullPointerException("NARG");
		
		return new CModifiers(__of);
	}
}
