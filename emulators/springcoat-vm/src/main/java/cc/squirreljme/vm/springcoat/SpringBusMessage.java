// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

/**
 * Represents a message waiting on the SpringCoat bus.
 *
 * @since 2024/08/13
 */
public final class SpringBusMessage
{
	/** The data being sent. */
	public final byte[] data;
	
	/** Where this is from. */
	public final SpringMachine from;
	
	/** Where this is going to. */
	public final SpringMachine to;
	
	/**
	 * Initializes the message store.
	 *
	 * @param __from The task this is from.
	 * @param __to The task this is being sent to.
	 * @param __data The data to send.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/08/13
	 */
	public SpringBusMessage(SpringMachine __from, SpringMachine __to,
		byte[] __data)
		throws NullPointerException
	{
		if (__from == null || __data == null)
			throw new NullPointerException("NARG");
		
		this.from = __from;
		this.to = __to;
		this.data = __data;
	}
}
