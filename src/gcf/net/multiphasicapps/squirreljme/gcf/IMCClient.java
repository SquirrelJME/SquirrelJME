// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.gcf;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.microedition.io.IMCConnection;
import javax.microedition.midlet.MIDletIdentity;
import net.multiphasicapps.squirreljme.midletid.MidletSuiteID;
import net.multiphasicapps.squirreljme.midletid.MidletVersion;

/**
 * This implements the client side of the IMC connection.
 *
 * @since 2016/10/13
 */
public class IMCClient
	implements IMCConnection
{
	/** The actually connected remote end. */
	protected final MidletSuiteID connectid;
	
	/** The server name. */
	protected final String servername;
	
	/** The server version. */
	protected final MidletVersion serverversion;
	
	/** Use authorization? */
	protected final boolean authmode;
	
	/**
	 * Initializes the inter-midlet communication client.
	 *
	 * @param __id The midlet to connect to, if {@code null} then the first
	 * midlet found is used.
	 * @param __sv The server to connect to.
	 * @param __ver The server version.
	 * @param __authmode Is the connection authorized?
	 * @throws NullPointerException If no server name or version were
	 * specified.
	 * @since 2016/10/13
	 */
	public IMCClient(MidletSuiteID __id, String __sv, MidletVersion __ver,
		boolean __authmode)
		throws NullPointerException
	{
		// Check
		if (__sv == null || __ver == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.servername = __sv;
		this.serverversion = __ver;
		this.authmode = __authmode;
		
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/13
	 */
	@Override
	public void close()
		throws IOException
	{
		throw new Error("TODO");
	}
		
	/**
	 * {@inheritDoc}
	 * @since 2016/10/13
	 */
	@Override
	public MIDletIdentity getRemoteIdentity()
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/13
	 */
	@Override
	public String getRequestedServerVersion()
	{
		return this.serverversion.toString();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/13
	 */
	@Override
	public String getServerName()
	{
		return this.servername;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/13
	 */
	@Override
	public DataInputStream openDataInputStream()
		throws IOException
	{
		return new DataInputStream(openInputStream());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/13
	 */
	@Override
	public DataOutputStream openDataOutputStream()
		throws IOException
	{
		return new DataOutputStream(openOutputStream());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/13
	 */
	@Override
	public InputStream openInputStream()
		throws IOException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/13
	 */
	@Override
	public OutputStream openOutputStream()
		throws IOException
	{
		throw new Error("TODO");
	}
}

