// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel.client;

import java.lang.ref.Reference;
import net.multiphasicapps.squirreljme.runtime.packets.Packet;
import net.multiphasicapps.squirreljme.runtime.packets.PacketStreamHandler;

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
	public void end()
	{
		// Just terminate the VM becuse there is nothing else to do since
		// it seems the kernel just died
		System.exit(1);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/01/01
	 */
	@Override
	public Packet handle(Packet __p)
		throws NullPointerException
	{
		if (__p == null)
			throw new NullPointerException("NARG");
		
		ClientCaller caller = this.__caller();
		
		switch (__p.type())
		{
				// Kernel said hello
			case PacketTypes.HELLO:
				caller._gothello = true;
				return null;
			
				// {@squirreljme.error AR06 Unknown packet type. (The packet)}
			default:
				throw new RuntimeException(String.format("AR06", __p));
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

