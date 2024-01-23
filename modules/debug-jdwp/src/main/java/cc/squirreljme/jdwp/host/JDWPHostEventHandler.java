// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp.host;

import cc.squirreljme.jdwp.JDWPException;
import cc.squirreljme.jdwp.JDWPPacket;

/**
 * Handler for host events.
 *
 * @since 2024/01/23
 */
public interface JDWPHostEventHandler
{
	/**
	 * Writes the packet event data.
	 * 
	 * @param __controller The controller used.
	 * @param __thread The current thread of execution.
	 * @param __packet The packet to write to.
	 * @param __args The arguments to the packet, the first value should
	 * always be {@code thread}.
	 * @throws JDWPException If it could not be written.
	 * @since 2021/03/16
	 */
	void write(JDWPHostController __controller, Object __thread,
		JDWPPacket __packet, Object... __args)
		throws JDWPException;
}
