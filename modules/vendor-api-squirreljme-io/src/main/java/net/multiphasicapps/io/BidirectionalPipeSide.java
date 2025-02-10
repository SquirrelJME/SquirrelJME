// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Represents a single side of a bidirectional pipe.
 *
 * @since 2024/01/19
 */
@SquirrelJMEVendorApi
public class BidirectionalPipeSide
{
	/** The input end of the pipe. */
	@SquirrelJMEVendorApi
	protected final InputStream in;
	
	/** The output end of the pipe. */
	@SquirrelJMEVendorApi
	protected final OutputStream out;
	
	/**
	 * Initializes the bidirectional pipe side.
	 *
	 * @param __in The stream to read from.
	 * @param __out The stream to write to.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/19
	 */
	@SquirrelJMEVendorApi
	public BidirectionalPipeSide(InputStream __in, OutputStream __out)
		throws NullPointerException
	{
		if (__in == null || __out == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.in = __in;
		this.out = __out;
	}
	
	/**
	 * Returns the input end of the pipe.
	 *
	 * @return The pipe input end.
	 * @since 2024/01/19
	 */
	@SquirrelJMEVendorApi
	public InputStream in()
	{
		return this.in;
	}
	
	/**
	 * Returns the output end of the pipe.
	 *
	 * @return The pipe input end.
	 * @since 2024/01/19
	 */
	@SquirrelJMEVendorApi
	public OutputStream out()
	{
		return this.out;
	}
}
