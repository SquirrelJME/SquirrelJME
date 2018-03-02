// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.datagrampackets;

import java.io.PrintStream;

/**
 * This is thrown when there is a fatal error.
 *
 * @since 2018/01/07
 */
public class RemoteError
	extends Error
	implements RemoteThrowable
{
	/** The exception detail. */
	protected final RemoteThrowableDetail detail;
	
	/**
	 * Initializes the exception.
	 *
	 * @param __d The exception detail.
	 * @throws NullPointerException On null arguments.
	 * @sicne 2018/01/07
	 */
	public RemoteError(RemoteThrowableDetail __d)
		throws NullPointerException
	{
		super(__d.message(), __d.cause());
		
		this.detail = __d;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/01/07
	 */
	@Override
	public RemoteThrowableDetail detail()
	{
		return this.detail;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/01/07
	 */
	@Override
	public void printLocalStackTrace(PrintStream __ps)
		throws NullPointerException
	{
		super.printStackTrace(__ps);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/01/07
	 */
	@Override
	public void printStackTrace()
	{
		this.printStackTrace(System.err);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/01/07
	 */
	@Override
	public void printStackTrace(PrintStream __ps)
	{
		__ThrowableUtil__.__printStackTrace(__ps, this);
	}
}

