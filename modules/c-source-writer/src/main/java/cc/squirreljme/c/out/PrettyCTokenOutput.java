// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c.out;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;

/**
 * Wraps the output and makes it very pretty.
 *
 * @since 2023/06/19
 */
public class PrettyCTokenOutput
	implements CTokenOutput
{
	/**
	 * Initializes the output wrapper.
	 * 
	 * @param __wrap The output to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/19
	 */
	public PrettyCTokenOutput(CTokenOutput __wrap)
		throws NullPointerException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/22
	 */
	@Override
	public void close()
		throws IOException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/22
	 */
	@Override
	public void indent(int __adjust)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/22
	 */
	@Override
	public void newLine(boolean __force)
		throws IOException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/22
	 */
	@Override
	public void space()
		throws IOException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/22
	 */
	@Override
	public void tab()
		throws IOException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/22
	 */
	@Override
	public void token(CharSequence __cq, boolean __forceNewline)
		throws IOException, NullPointerException
	{
		throw Debugging.todo();
	}
}
