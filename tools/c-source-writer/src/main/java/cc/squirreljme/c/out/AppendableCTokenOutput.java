// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c.out;

import java.io.Closeable;
import java.io.IOException;

/**
 * Token output to a {@link Appendable}.
 *
 * @since 2023/06/19
 */
public class AppendableCTokenOutput
	implements CTokenOutput
{
	/** The output we are writing to. */
	protected final Appendable out;
	
	/**
	 * Initializes the token output to the appendable.
	 * 
	 * @param __out The output to write to.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/22
	 */
	public AppendableCTokenOutput(Appendable __out)
		throws NullPointerException
	{
		if (__out == null)
			throw new NullPointerException("NARG");
		
		this.out = __out;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/22
	 */
	@Override
	public void close()
		throws IOException
	{
		// This might not actually be a closeable
		Appendable out = this.out;
		if (out instanceof Closeable)
			((Closeable)out).close();
		else if (out instanceof AutoCloseable)
			try
			{
				((AutoCloseable)out).close();
			}
			catch (Exception __e)
			{
				if (__e instanceof RuntimeException)
					throw (RuntimeException)__e;
				throw new IOException(__e);
			}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/22
	 */
	@Override
	public void indent(int __adjust)
	{
		// Does nothing here
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/22
	 */
	@Override
	public void newLine(boolean __force)
		throws IOException
	{
		// Always outputs a newline
		this.out.append("\n");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/07/22
	 */
	@Override
	public void pivot(CPivotPoint __pivot)
		throws IOException, NullPointerException
	{
		// Ignore, not used here
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/22
	 */
	@Override
	public void space()
		throws IOException
	{
		this.out.append(" ");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/22
	 */
	@Override
	public void tab()
		throws IOException
	{
		// Always emits a tab
		this.out.append("\t");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/22
	 */
	@Override
	public void token(CharSequence __cq, boolean __forceNewline)
		throws IOException, NullPointerException
	{
		if (__cq == null)
			throw new NullPointerException("NARG");
		
		// Send token directly to the output
		Appendable out = this.out;
		out.append(__cq);
		
		// Emit also a newline?
		if (__forceNewline)
			out.append("\n");
	}
}

