// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.nttdocomo.io;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import com.nttdocomo.io.HttpConnection;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Wraps a DoJa HTTPConnection to GCF's HTTPConnection.
 *
 * @since 2022/10/07
 */
public class DoJaHttpConnectionAdapter
	implements HttpConnection
{
	/** The GCF based connection. */
	private final javax.microedition.io.HttpConnection gcf;
	
	/**
	 * Initializes the connection adapter.
	 * 
	 * @param __gcf The GCF connection to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/10/07
	 */
	public DoJaHttpConnectionAdapter(
		javax.microedition.io.HttpConnection __gcf)
		throws NullPointerException
	{
		if (__gcf == null)
			throw new NullPointerException("NARG");
		
		this.gcf = __gcf;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/10/07
	 */
	@Override
	public void close()
		throws IOException
	{
		this.gcf.close();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/10/07
	 */
	@Override
	public void connect()
		throws IOException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/10/07
	 */
	@Override
	public long getDate()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/10/07
	 */
	@Override
	public String getEncoding()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/10/07
	 */
	@Override
	public long getExpiration()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/10/07
	 */
	@Override
	public String getHeaderField()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/10/07
	 */
	@Override
	public long getLastModified()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/10/07
	 */
	@Override
	public long getLength()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/10/07
	 */
	@Override
	public int getResponseCode()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/10/07
	 */
	@Override
	public String getResponseMessage()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/10/07
	 */
	@Override
	public String getType()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/10/07
	 */
	@Override
	public void getURL()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/10/07
	 */
	@Override
	public DataInputStream openDataInputStream()
		throws IOException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/10/07
	 */
	@Override
	public DataOutputStream openDataOutputStream()
		throws IOException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/10/07
	 */
	@Override
	public InputStream openInputStream()
		throws IOException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/10/07
	 */
	@Override
	public OutputStream openOutputStream()
		throws IOException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/10/07
	 */
	@Override
	public void setIfModifiedState(long __ifModifiedSince)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/10/07
	 */
	@Override
	public void setRequestMethod(String __method)
		throws IllegalArgumentException, IOException, NullPointerException
	{
		if (__method == null)
			throw new NullPointerException("NARG");
		
		// Only these methods are valid
		switch (__method)
		{
			case "HEAD":
			case "GET":
			case "POST":
				this.gcf.setRequestMethod(__method);
				break;
		}
		
		// {@squirreljme.error AH0z Invalid method specified.}
		throw new IllegalArgumentException("AH0z");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/10/07
	 */
	@Override
	public void setRequestProperty(String __key, String __value)
		throws IOException
	{
		throw Debugging.todo();
	}
}
