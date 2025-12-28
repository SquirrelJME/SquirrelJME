// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.gcf;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;
import javax.microedition.io.Connection;
import javax.microedition.io.Connector;
import javax.microedition.io.file.ConnectionClosedException;
import javax.microedition.io.file.IllegalModeException;
import org.intellij.lang.annotations.MagicConstant;
import static cc.squirreljme.runtime.cldc.debug.ErrorCode.__error__;

/**
 * Base abstract connection class.
 *
 * @since 2025/12/27
 */
@SquirrelJMEVendorApi
public abstract class AbstractBaseConnection
	implements Connection
{
	/** The mode this connection is opened in. */
	@SquirrelJMEVendorApi
	protected final int mode;
	
	/** Has this been closed? */
	private volatile boolean _isClosed;
	
	/**
	 * Initializes the base connection.
	 *
	 * @param __mode The mode this is opened in.
	 * @throws IllegalArgumentException If the connection mode is not valid.
	 * @since 2025/12/27
	 */
	protected AbstractBaseConnection(
		@MagicConstant(valuesFromClass = Connector.class) int __mode)
		throws IllegalArgumentException
	{
		/* {@squirreljme.error EC20 Invalid connection mode. (The mode)} */
		if ((__mode & ~(Connector.READ | Connector.WRITE)) != 0)
			throw new IllegalArgumentException(__error__(
				"EC20 %d", __mode));
		
		this.mode = __mode;
	}
	
	/**
	 * The connection is entering the closed state.
	 *
	 * @throws IOException If this failed to close.
	 * @since 2025/12/27
	 */
	@SquirrelJMEVendorApi
	protected abstract void becomingClosed()
		throws IOException;
	
	/**
	 * Checks if the connection is closed. 
	 *
	 * @throws ConnectionClosedException If the connection is closed.
	 * @since 2025/12/27
	 */
	@SquirrelJMEVendorApi
	protected final void checkClosed()
		throws ConnectionClosedException
	{
		synchronized (this)
		{
			if (this._isClosed)
				throw new ConnectionClosedException("CLOS");
		}
	}
	
	/**
	 * Checks if the readable mode is set.
	 *
	 * @throws IllegalModeException If the readable mode is not set.
	 * @since 2025/12/27
	 */
	@SquirrelJMEVendorApi
	protected final void checkRead()
		throws IllegalModeException
	{
		if ((this.mode & Connector.READ) == 0)
			throw new IllegalModeException("NOWR");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/27
	 */
	@Override
	public final void close()
		throws IOException
	{
		synchronized (this)
		{
			// Do nothing if already closed
			if (this._isClosed)
				return;
			
			// Otherwise handle closing
			this._isClosed = true;
			this.becomingClosed();
		}
	}
}
