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
import cc.squirreljme.jvm.mle.brackets.TaskBracket;
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
	 * Closes the given bus transport and any associated connections.
	 * 
	 * @param __bus The bus to close.
	 * @throws MLECallError If the bus could not be closed or is {@code null}.
	 * @since 2022/12/03
	 */
	@Api
	public static native void close(BusTransportBracket __bus)
		throws MLECallError;
	
	/**
	 * Connects to the given service and port that another task is connecting
	 * to.
	 * 
	 * @param __task The task to connect to.
	 * @param __service The service to use for connecting.
	 * @param __port The service port to connect on.
	 * @return The transport to the given bus.
	 * @throws MLECallError If a connection could not be established or any
	 * argument is {@code null}.
	 * @since 2022/12/03
	 */
	@Api
	public static native BusTransportBracket connect(TaskBracket __task,
		String __service, int __port)
		throws MLECallError;
	
	/**
	 * Listens on the given service for a given port.
	 * 
	 * @param __service The service to listen on.
	 * @param __port The port of the service to listen on.
	 * @return The bus transport for the given service, this returns a
	 * listening bus transport that cannot have data transmitted.
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
	 * Returns the task that is on the other side of the bus.
	 * 
	 * This should be called after {@link #accept(BusTransportBracket)} to
	 * determine the task that is on the other side of the call.
	 * 
	 * @param __bus The bus to get the remote host from.
	 * @return The task that is the host on the other side of the bus.
	 * @throws MLECallError If the bus is not valid or {@code null}.
	 * @since 2022/12/03
	 */
	@Api
	public static native TaskBracket remoteHost(BusTransportBracket __bus)
		throws MLECallError;
}
