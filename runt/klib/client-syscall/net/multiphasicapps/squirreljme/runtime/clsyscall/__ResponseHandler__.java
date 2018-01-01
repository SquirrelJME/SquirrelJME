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

import java.lang.ref.Reference;

/**
 * This handles responses for the system call interface.
 *
 * @since 2018/01/01
 */
final class __ResponseHandler__
	implements PacketStreamHandler
{
	/** The client caller to handle. */
	protected final Reference<ClientCaller> caller;
	
	/**
	 * Initializes the response handler.
	 *
	 * @param __cr The reference to the caller.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/01
	 */
	__ResponseHandler__(Reference<ClientCaller> __cr)
		throws NullPointerException
	{
		if (__cr == null)
			throw new NullPointerException("NARG");
		
		this.caller = __cr;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/01/01
	 */
	@Override
	public byte[] handle(int __t, byte[] __b, int __o, int __l)
		throws ArrayIndexOutOfBoundsException, NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new ArrayIndexOutOfBoundsException("IOOB");
	
		ClientCaller caller = this.__caller();
		
		switch (__t)
		{
				// {@squirreljme.error AR06 Unknown response type. (The type)}
			default:
				throw new RuntimeException(String.format("AR06", __t));
		}
	}
	
	/**
	 * Returns the caller.
	 *
	 * @return The caller.
	 * @throws RuntimeException If it was garbage collected.
	 * @since 2018/01/01
	 */
	final ClientCaller __caller()
		throws RuntimeException
	{
		// {@squirreljme.error AR05 The caller was garbage collected.}
		ClientCaller rv = this.caller.get();
		if (rv == null)
			throw new RuntimeException("AR05");
		return rv;
	}
}

