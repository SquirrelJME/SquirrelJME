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
	
	/** The reply handler to use for passes. */
	public final ReplyHandler pass;
	
	/** The reply handle to use for failures. */
	public final ReplyHandler fail;
	
	/** The handler that is always called. */
	public final ReplyHandler always;
	
	/** The time the source was created. */
	public final long nanoTime;
	
	/**
	 * Initializes the handler.
	 *
	 * @param __id The packet ID.
	 * @param __pass The handler used for successful commands.
	 * @param __fail The handler used for failed commands.
	 * @param __always The handler that is always called.
	 * @param __nanoTime The time the packet was sent.
	 * @since 2024/01/26
	 */
	public AwaitingReply(int __id, ReplyHandler __pass,
		ReplyHandler __fail, ReplyHandler __always, long __nanoTime)
	{
		this.id = __id;
		this.pass = __pass;
		this.fail = __fail;
		this.always = __always;
		this.nanoTime = __nanoTime;
	}
}
