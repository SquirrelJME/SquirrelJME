// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Represents a unidirectional pipe which only permits communication in a
 * single direction.
 *
 * @since 2024/01/19
 */
public class UnidirectionalPipe
{
	/** The input end of the pipe. */
	protected final InputStream in;
	
	/** The output end of the pipe. */
	protected final OutputStream out;
	
	/** The byte deque used for communication. */
	protected final ByteDeque queue;
	
	/**
	 * Initializes the bidirectional pipe.
	 *
	 * @since 2024/01/19
	 */
	public UnidirectionalPipe()
	{
		// Setup initial stream
		ByteDeque queue = new ByteDeque();
		this.queue = queue;
		
		// Setup streams for both ends
		this.in = new ByteDequeInputStream(queue);
		this.out = new ByteDequeOutputStream(queue);
	}
	
	/**
	 * Returns the input end of the pipe.
	 *
	 * @return The pipe input end.
	 * @since 2024/01/19
	 */
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
	public OutputStream out()
	{
		return this.out;
	}
}
