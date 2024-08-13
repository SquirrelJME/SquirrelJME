// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages the SquirrelJME bus for SpringCoat.
 *
 * @since 2024/08/13
 */
public final class SpringBus
{
	/** The bus ID. */
	public final int id;
	
	/** The bus queue. */
	private final List<SpringBusMessage> _queue =
		new ArrayList<>();
	
	/**
	 * Initializes the SpringCoat bus.
	 *
	 * @param __id The bus identifier.
	 * @since 2024/08/13
	 */
	public SpringBus(int __id)
	{
		this.id = __id;
	}
	
	/**
	 * Receives a message for the given task.
	 *
	 * @param __for The task this is for.
	 * @param __from Where the message is from.
	 * @param __blocking Is this blocking?
	 * @param __b The output data.
	 * @param __o The offset into the data.
	 * @param __l The length of the data.
	 * @return The number of bytes, negative if not enough room. Will be
	 * also {@link Integer#MIN_VALUE} if interrupted or there are none.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/08/13
	 */
	public int receive(SpringMachine __for, SpringMachine[] __from,
		boolean __blocking, byte[] __b, int __o, int __l)
		throws NullPointerException
	{
		if (__for == null || __from == null || __b == null)
			throw new NullPointerException("NARG");
		
		if (__o < 0 || __l < 0 || (__o + __l) < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Look in the queue for a message
		List<SpringBusMessage> queue = this._queue;
		synchronized (this)
		{
			for (;;)
			{
				// Look through every message
				for (int i = 0, n = queue.size(); i < n; i++)
				{
					SpringBusMessage message = queue.get(i);
					
					// Is this for us, or to anyone?
					if (__for == message.to || message.to == null)
					{
						byte[] data = message.data;
						
						// Too small?
						if (data.length > __l)
							return -data.length;
						
						// It fits, so remove it now
						queue.remove(i);
						
						// Debug
						Debugging.debugNote("Receiving %d on bus %08x!",
							data.length, this.id);
						
						// Fill in the data
						System.arraycopy(data, 0,
							__b, __o, data.length);
						return data.length;
					}
				}
				
				// If not blocking, just stop
				if (!__blocking)
					return Integer.MIN_VALUE;
				
				// Wait for message
				try
				{
					this.wait(1000);
				}
				catch (InterruptedException __ignored)
				{
					return Integer.MIN_VALUE;
				}
			}
		}
	}
	
	/**
	 * Queues a message for retrieval on this bus at a later time.
	 *
	 * @param __from The task this is from.
	 * @param __to The task this is being sent to, {@code null} is anyone.
	 * @param __b The bytes to send.
	 * @param __o The offset into the bytes.
	 * @param __l The length of the data.
	 * @throws IndexOutOfBoundsException If the offset and/or length are
	 * invalid or exceed the array bounds.
	 * @throws NullPointerException If no from or byte array was specified.
	 * @since 2024/08/13
	 */
	public void send(SpringMachine __from, SpringMachine __to,
		byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		if (__from == null || __b == null)
			throw new NullPointerException("NARG");
		
		if (__o < 0 || __l < 0 || (__o + __l) < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Copy data
		byte[] data = new byte[__l];
		System.arraycopy(__b, __o,
			data, 0, __l);
		
		// Setup message
		SpringBusMessage message = new SpringBusMessage(__from, __to, data);
		
		// Store in the queue
		List<SpringBusMessage> queue = this._queue;
		synchronized (this)
		{
			queue.add(message);
			
			// Send notification
			this.notifyAll();
			this.notifyAll();
		}
		
		// Note
		Debugging.debugNote("Posted %d on bus %08x!",
			__l, this.id);
	}
}
