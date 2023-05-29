// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat;

import java.util.Arrays;
import java.util.List;
import net.multiphasicapps.collections.UnmodifiableList;

/**
 * Represents a modifier in C.
 *
 * @since 2023/05/29
 */
public enum CBasicModifier
	implements CModifier
{
	/** External symbol. */
	EXTERN("extern"),
	
	/** Constant. */
	CONST("SJME_CONST"),
	
	/* End. */
	;
	
	/** The token used. */
	public final List<String> token;
	
	/**
	 * Initializes the modifier.
	 * 
	 * @param __token The tokens used.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/29
	 */
	CBasicModifier(String __token)
		throws NullPointerException
	{
		if (__token == null)
			throw new NullPointerException("NARG");
		
		this.token = UnmodifiableList.of(Arrays.asList(__token));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/29
	 */
	@Override
	public List<String> modifierTokens()
	{
		return this.token;
	}
}
