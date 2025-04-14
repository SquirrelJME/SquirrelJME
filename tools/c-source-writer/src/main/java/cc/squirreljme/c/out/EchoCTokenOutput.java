// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c.out;

import java.io.IOException;

/**
 * Token output that is used for debugging output.
 *
 * @since 2023/06/25
 */
public class EchoCTokenOutput
	implements CTokenOutput
{
	/** The debug output. */
	protected final Appendable debug;
	
	/** What this actually outputs to. */
	protected final CTokenOutput out;
	
	/**
	 * Initializes the debugging C token output.
	 * 
	 * @param __debug The debug output.
	 * @param __out The output to forward to.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/25
	 */
	public EchoCTokenOutput(Appendable __debug, CTokenOutput __out)
		throws NullPointerException
	{
		if (__debug == null || __out == null)
			throw new NullPointerException("NARG");
		
		this.debug = __debug;
		this.out = __out;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/25
	 */
	@Override
	public void close()
		throws IOException
	{
		this.out.close();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/25
	 */
	@Override
	public void indent(int __adjust)
	{
		this.out.indent(__adjust);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/25
	 */
	@Override
	public void newLine(boolean __force)
		throws IOException
	{
		this.debug.append("\n");
		this.out.newLine(__force);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/07/22
	 */
	@Override
	public void pivot(CPivotPoint __pivot)
		throws IOException, NullPointerException
	{
		// Just forward it
		this.out.pivot(__pivot);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/25
	 */
	@Override
	public void space()
		throws IOException
	{
		this.debug.append(" ");
		this.out.space();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/25
	 */
	@Override
	public void tab()
		throws IOException
	{
		this.debug.append("\t");
		this.out.tab();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/25
	 */
	@Override
	public void token(CharSequence __cq, boolean __forceNewline)
		throws IOException, NullPointerException
	{
		this.debug.append(__cq);
		if (__forceNewline)
			this.debug.append("\n");
		
		this.out.token(__cq, __forceNewline);
	}
}
