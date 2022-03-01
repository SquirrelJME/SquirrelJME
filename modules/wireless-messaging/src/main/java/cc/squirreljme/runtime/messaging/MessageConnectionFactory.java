// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.messaging;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.gcf.CustomConnectionFactory;
import java.io.IOException;
import javax.microedition.io.Connection;
import javax.microedition.io.ConnectionOption;

/**
 * This is the factory which handles the {@code sms} protocol.
 *
 * @since 2022/02/27
 */
public class MessageConnectionFactory
	implements CustomConnectionFactory
{
	/**
	 * {@inheritDoc}
	 * @since 2022/02/27
	 */
	@Override
	public Connection connect(String __part, int __mode, boolean __timeouts,
		ConnectionOption<?>[] __opts)
		throws IOException, NullPointerException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/27
	 */
	@Override
	public String scheme()
	{
		return "sms";
	}
}
