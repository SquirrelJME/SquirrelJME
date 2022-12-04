package cc.squirreljme.emulator;

import cc.squirreljme.jvm.mle.BusTransportShelf;
import cc.squirreljme.jvm.mle.brackets.BusTransportBracket;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Implements {@link BusTransportShelf} for the Java SE environment.
 * 
 * @since 2022/12/03
 */
public class EmulatedBusTransportShelf
{
	/**
	 * Returns the connection to the primary bus manager or if this is the
	 * manager this will be the listener.
	 *
	 * @return The connection to the primary bus manager or if this is the
	 * manager this will be the listener.
	 * @since 2022/12/03
	 */
	public static BusTransportBracket primary()
	{
		throw Debugging.todo();
	}
}
