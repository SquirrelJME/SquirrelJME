// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat.linkage;

import cc.squirreljme.c.CBasicExpression;
import cc.squirreljme.c.CStructVariableBlock;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;

/**
 * Linkage for a single string.
 *
 * @since 2023/07/16
 */
public final class StringLinkage
	implements Linkage
{
	/** The string value. */
	protected final String string;
	
	/**
	 * Initializes the string linkage.
	 * 
	 * @param __string The string value.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/16
	 */
	public StringLinkage(String __string)
		throws NullPointerException
	{
		if (__string == null)
			throw new NullPointerException("NARG");
		
		this.string = __string;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/07/16
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		if (!(__o instanceof StringLinkage))
			return false;
		
		return this.string.equals(((StringLinkage)__o).string);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/07/16
	 */
	@Override
	public int hashCode()
	{
		return this.string.hashCode();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/07/25
	 */
	@Override
	public void write(CStructVariableBlock __output)
		throws IOException, NullPointerException
	{
		try (CStructVariableBlock struct =
			 __output.memberStructSet("stringObject"))
		{
			struct.memberSet("string",
				CBasicExpression.string(this.string));
		}
	}
}
