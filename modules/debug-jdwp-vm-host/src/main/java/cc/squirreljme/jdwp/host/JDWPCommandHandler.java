// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp.host;

import cc.squirreljme.jdwp.JDWPCommand;
import cc.squirreljme.jdwp.JDWPException;
import cc.squirreljme.jdwp.JDWPPacket;

/**
 * Interface for JDWP Command execution.
 *
 * @since 2021/03/12
 */
public interface JDWPCommandHandler
	extends JDWPCommand
{
	/**
	 * Returns the command that this handled.
	 * 
	 * @return The handled command.
	 * @since 2024/01/23
	 */
	JDWPCommand command();
	
	/**
	 * Executes the given command.
	 * 
	 * @param __controller The controller used.
	 * @param __packet The packet being input.
	 * @return The result packet, if there is one.
	 * @since 2021/03/12
	 */
	JDWPPacket execute(JDWPHostController __controller, JDWPPacket __packet)
		throws JDWPException;
}
