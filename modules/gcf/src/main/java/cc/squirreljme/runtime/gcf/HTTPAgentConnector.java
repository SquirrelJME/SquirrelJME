// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.gcf;

import java.io.IOException;
import javax.microedition.io.StreamConnection;

/**
 * This interface is used to source the socket used to open a connection on
 * the underlying stream.
 *
 * @since 2022/10/07
 */
public interface HTTPAgentConnector
{
	/**
	 * Connects to the given stream.
	 * 
	 * @param __address The address to connect to.
	 * @return The connection to the address.
	 * @throws IOException If it could not connect.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/10/07
	 */
	StreamConnection connectStream(HTTPAddress __address)
		throws IOException, NullPointerException;
}
