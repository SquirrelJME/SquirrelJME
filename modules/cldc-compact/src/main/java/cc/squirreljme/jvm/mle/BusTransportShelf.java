// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle;

import cc.squirreljme.jvm.mle.brackets.BusTransportBracket;
import cc.squirreljme.jvm.mle.brackets.PipeBracket;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.annotation.Api;

/**
 * This handles interprocess communication by allowing different tasks that
 * are running to communicate with each other. All communications and otherwise
 * are performed under the primary bus which will then delegate accordingly
 * to other tasks by a bus manager.
 *
 * @since 2022/10/18
 */
@Api
public final class BusTransportShelf
{
	/**
	 * Not used.
	 * 
	 * @since 2022/10/18
	 */
	private BusTransportShelf()
	{
	}
	
	/**
	 * Accepts a new connection on the given bus.
	 * 
	 * @param __listenBus The listening bus to accept the connection on.
	 * @return The transport for the newly accepted transport, or {@code null}
	 * if none was accepted.
	 * @throws MLECallError If the bus is not a listener bus.
	 * @since 2022/10/18
	 */
	@Api
	public static native BusTransportBracket accept(
		BusTransportBracket __listenBus)
		throws MLECallError;
	
	/**
	 * Listens on the given service for a given port.
	 * 
	 * @param __service The service to listen on.
	 * @param __port The port of the service to listen on.
	 * @return The bus transport for the given service.
	 * @throws MLECallError If the service already exists or none was
	 * specified.
	 * @since 2022/10/18
	 */
	@Api
	@Deprecated
	public static native BusTransportBracket listen(String __service,
		int __port)
		throws MLECallError;
	
	/**
	 * Returns the pipe that is associated with the given bus, this is actually
	 * used to send data over the channel.
	 * 
	 * @param __bus The bus this is for.
	 * @return The pipe for the given bus.
	 * @throws MLECallError If the bus is not valid.
	 * @since 2022/10/18
	 */
	@Api
	public static native PipeBracket pipe(BusTransportBracket __bus)
		throws MLECallError;
	
	/**
	 * Returns the connection to the primary bus manager or if this is the
	 * manager this will be the listener.
	 * 
	 * @return The connection to the primary bus manager or if this is the
	 * manager this will be the listener.
	 * @since 2022/10/18
	 */
	public static native BusTransportBracket primary();
}
