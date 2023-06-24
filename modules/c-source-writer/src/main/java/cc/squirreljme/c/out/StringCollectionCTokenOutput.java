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
import java.util.Collection;

/**
 * Outputs to a {@link Collection} of {@link String}.
 *
 * @since 2023/06/19
 */
public class StringCollectionCTokenOutput
	implements CTokenOutput
{
	/** The output collection. */
	protected final Collection<String> out;
	
	/**
	 * Initializes the output to the string collection.
	 * 
	 * @param __out The output.
	 * @param __whitespace Should whitespace be considered?
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/19
	 */
	public StringCollectionCTokenOutput(Collection<String> __out,
		boolean __whitespace)
		throws NullPointerException
	{
		if (__out == null)
			throw new NullPointerException("NARG");
		
		this.out = __out;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/19
	 */
	@Override
	public void close()
		throws IOException
	{
		// Collections do not get closed
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/22
	 */
	@Override
	public void indent(int __adjust)
	{
		// Does nothing, string collection lacks this completely
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/22
	 */
	@Override
	public void newLine(boolean __force)
		throws IOException
	{
		// Only if forced
		if (__force)
			this.out.add("\n");
	}
	
	/**
	 * Returns the output collection.
	 * 
	 * @return The output.
	 * @since 2023/06/22
	 */
	public final Collection<String> output()
	{
		return this.out;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/22
	 */
	@Override
	public void space()
		throws IOException
	{
		// Does nothing, string collection lacks this completely
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/22
	 */
	@Override
	public void tab()
		throws IOException
	{
		// Does nothing, string collection lacks this completely
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
		
		Collection<String> out = this.out;
		
		// Add to the output
		out.add(__cq.toString());
		
		// Force newline at end, if preprocessor for example?
		if (__forceNewline)
			out.add("\n");
	}
}
