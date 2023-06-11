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
import java.util.List;

/**
 * This is a type that is a typedef of another type.
 *
 * @since 2023/06/06
 */
public class CTypeDefType
	extends __CAbstractType__
{
	/**
	 * {@inheritDoc}
	 * @since 2023/06/06
	 */
	@Override
	public CType dereferenceType()
		throws IllegalArgumentException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/06
	 */
	@Override
	public boolean isPointer()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/06
	 */
	@Override
	List<String> __generateTokens(CTokenSet __set)
		throws NullPointerException
	{
		throw Debugging.todo();
	}
}
