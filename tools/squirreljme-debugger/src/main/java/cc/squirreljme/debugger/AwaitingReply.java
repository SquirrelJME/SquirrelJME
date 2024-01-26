// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

/**
 * A single await reply handler.
 *
 * @since 2024/01/26
 */
public class AwaitingReply
{
	/** The packet ID. */
	public final int id;
	
	/** The reply handler to use. */
	public final ReplyHandler handler;
	
	/** The time the source was created. */
	public final long nanoTime;
	
	/**
	 * Initializes the handler.
	 *
	 * @param __id The packet Id.
	 * @param __handler The handler used.
	 * @param __nanoTime The time the packet was sent.
	 * @since 2024/01/26
	 */
	public AwaitingReply(int __id, ReplyHandler __handler, long __nanoTime)
	{
		this.id = __id;
		this.handler = __handler;
		this.nanoTime = __nanoTime;
	}
}
