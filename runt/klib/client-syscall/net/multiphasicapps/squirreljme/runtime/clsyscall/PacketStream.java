// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.clsyscall;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * This represents a packet stream which is used to read and write events from
 * the local end to the remote end.
 *
 * This class spawns a new thread to manage asynchronous events.
 *
 * @since 2018/01/01
 */
public final class PacketStream
{
	/** The input stream. */
	protected final DataInputStream in;
	
	/** The output stream. */
	protected final DataOutputStream out;
	
	/**
	 * Initializes the packet stream.
	 *
	 * @param __in The input stream.
	 * @param __out The output stream.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/12/31
	 */
	public PacketStream(InputStream __in, OutputStream __out)
		throws NullPointerException
	{
		if (__in == null || __out == null)
			throw new NullPointerException("NARG");
		
		this.in = new DataInputStream(__in);
		this.out = new DataOutputStream(__out);
	}
}

