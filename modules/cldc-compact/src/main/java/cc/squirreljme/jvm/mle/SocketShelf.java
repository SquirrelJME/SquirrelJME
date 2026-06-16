// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle;

import cc.squirreljme.jvm.mle.brackets.DatagramBracket;
import cc.squirreljme.jvm.mle.brackets.PipeBracket;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import org.jetbrains.annotations.NotNull;

/**
 * This shelf provides access to the system's native socket interfaces which
 * is generally used for named pipes, TCP/IP, UDP/IP, and otherwise.
 * 
 * Generally non-datagram stream based sockets will return {@link PipeBracket}
 * whereas datagram based sockets will return {@link DatagramBracket}.
 *
 * @see PipeBracket
 * @see TerminalShelf
 * @see DatagramBracket
 * @see DatagramShelf
 * @since 2026/05/17
 */
@SquirrelJMEVendorApi
public final class SocketShelf
{
	/**
	 * Not used.
	 *
	 * @since 2026/05/17
	 */
	private SocketShelf()
	{
	}
}
