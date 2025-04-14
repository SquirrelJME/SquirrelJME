// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Awaiting reply handler.
 *
 * @since 2024/01/26
 */
public class AwaitingReplies
{
	/** The replies to wait for. */
	private final Map<Integer, AwaitingReply> _replies =
		new LinkedHashMap<>();
	
	/**
	 * Awaits for the given reply.
	 *
	 * @param __id The packet ID.
	 * @param __pass Successful packet handler.
	 * @param __fail Failed packet handler.
	 * @param __always The always handler.
	 * @since 2024/01/26
	 */
	public void await(int __id, ReplyHandler __pass, ReplyHandler __fail,
		ReplyHandler __always)
	{
		Map<Integer, AwaitingReply> replies = this._replies;
		synchronized (this)
		{
			replies.put(__id,
				new AwaitingReply(__id, __pass, __fail, __always,
					System.nanoTime()));
		}
	}
	
	/**
	 * Removes an awaiting reply.
	 *
	 * @param __id The ID of the packet.
	 * @return The awaiting reply.
	 * @since 2024/01/26
	 */
	public AwaitingReply remove(int __id)
	{
		Map<Integer, AwaitingReply> replies = this._replies;
		synchronized (this)
		{
			return replies.remove(__id);
		}
	}
}
