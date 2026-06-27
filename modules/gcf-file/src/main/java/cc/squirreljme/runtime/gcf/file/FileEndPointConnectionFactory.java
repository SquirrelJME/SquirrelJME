// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.gcf.file;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.util.StringUtils;
import cc.squirreljme.runtime.gcf.CustomConnectionFactory;
import cc.squirreljme.runtime.gcf.uri.UriAuthority;
import cc.squirreljme.runtime.gcf.uri.UriGenericPart;
import cc.squirreljme.runtime.gcf.uri.UriPart;
import cc.squirreljme.runtime.gcf.uri.UriSchemeSpecificPart;
import java.io.IOException;
import java.util.Objects;
import javax.microedition.io.Connection;
import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.io.ConnectionOption;
import static cc.squirreljme.runtime.cldc.debug.ErrorCode.__error__;

/**
 * Factory for creating endpoint files.
 *
 * @since 2025/12/29
 */
@SquirrelJMEVendorApi
public class FileEndPointConnectionFactory
	implements CustomConnectionFactory
{
	/**
	 * {@inheritDoc}
	 * @since 2025/12/29
	 */
	@Override
	public Connection connect(UriPart __part, int __mode, boolean __timeouts,
		ConnectionOption<?>[] __opts)
		throws IOException, NullPointerException
	{
		if (__part == null)
			throw new NullPointerException("NARG");
		
		// If this is a scheme specific part, then this is very likely blank
		/* {@squirreljme.error GF09 File URI is of the incorrect syntax.
		(The URI)} */
		if (__part instanceof UriSchemeSpecificPart)
		{
			// It really must be blank
			if (!"".equals(__part.toString()))
				throw new IOException(
					__error__("GF09 %s", __part));
			
			__part = null;
		}
		
		// And now it must truly be a generic part
		if (!(__part instanceof UriGenericPart))
			throw new IOException(
				__error__("GF09 %s", __part));
		
		// The connection could start connected to an endpoint
		FileEndPointConnection connection = new FileEndPointConnection(__mode);
		if (__part != null)
			try
			{
				return this.__both(connection, (UriGenericPart)__part);
			}
			catch (IOException|RuntimeException|Error __e)
			{
				// Close the connection, since we could not open it
				IOException fail = null;
				try
				{
					// Try closing it
					connection.close();
				}
				catch (IOException __g)
				{
					fail = __g;
				}
				
				// Suppress if failed to close
				if (fail != null)
					__e.addSuppressed(fail);
				
				// Retoss
				throw __e;
			}
		
		// Not connected to anything yet
		return connection;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/06/27
	 */
	@Override
	public boolean implementsInterface(Class<? extends Connection> __cl)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		return __cl.isAssignableFrom(FileEndPointConnection.class);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/29
	 */
	@Override
	public String scheme()
	{
		return "file";
	}
	
	/**
	 * First tries a direct connection, if that fails then a traversal
	 * connection is made instead.
	 *
	 * @param __conn The base connection.
	 * @param __part The part to connect as.
	 * @return The resultant final connection.
	 * @throws IOException If the connection could not be opened.
	 * @throws NullPointerException On null arguments.
	 * @since 2026/01/16
	 */
	private FileEndPointConnection __both(FileEndPointConnection __conn,
		UriGenericPart __part)
		throws IOException, NullPointerException
	{
		if (__conn == null || __part == null)
			throw new NullPointerException("NARG");
		
		// Try a direct connection first, this is the fastest
		IOException failDirect;
		try
		{
			return this.__direct(__conn, (UriGenericPart)__part);
		}
		catch (IOException __e)
		{
			failDirect = __e;
		}
		
		// Then try a traversal, this is much slower however in the event
		// there are multiple VFS file end points this will handle such
		// situations
		Throwable failTraverse;
		try
		{
			return this.__traverse(__conn, (UriGenericPart)__part);
		}
		catch (IllegalArgumentException|IndexOutOfBoundsException|
			IOException __e)
		{
			failTraverse = __e;
		}
		
		// Both failed? Suppress the traverse and toss the direct
		failDirect.addSuppressed(failTraverse);
		throw failDirect;
	}
	
	/**
	 * Attempts a direct connection to the URI.
	 *
	 * @param __conn The base connection.
	 * @param __part The part to connect as.
	 * @return The resultant final connection.
	 * @throws IOException If the connection could not be opened.
	 * @throws NullPointerException On null arguments.
	 * @since 2026/01/16
	 */
	private FileEndPointConnection __direct(FileEndPointConnection __conn,
		UriGenericPart __part)
		throws IOException, NullPointerException
	{
		if (__conn == null || __part == null)
			throw new NullPointerException("NARG");
		
		// We can just try to change the path here directly
		return __conn.__changeEndPoint(__part, null);
	}
	
	/**
	 * Traverses the URI to connect to it through a longer path.
	 *
	 * @param __conn The base connection.
	 * @param __part The part to connect as.
	 * @return The resultant final connection.
	 * @throws IOException If the connection could not be opened.
	 * @throws NullPointerException On null arguments.
	 * @since 2026/01/16
	 */
	private FileEndPointConnection __traverse(FileEndPointConnection __conn,
		UriGenericPart __part)
		throws IOException, NullPointerException
	{
		if (__conn == null || __part == null)
			throw new NullPointerException("NARG");
		
		// Splice the path, make sure it does not contain any relative
		// components or nulls
		String[] splice = StringUtils.basicSplit('/',
			__part.getPath().substring(1));
		for (int n = splice.length, i = 0; i < n; i++)
		{
			String sub = splice[i];
			
			// no part may contain "." or ".."
			/* {@squirreljme.error Path must be absolute. (The URI) */
			if (sub == null || ".".equals(sub) || "..".equals(sub))
				throw new ConnectionNotFoundException(
					__error__("GF0f %s", __part));
		}
		
		// Start at the authority root
		UriAuthority auth = __part.getAuthority();
		__conn.__changeEndPoint(new UriGenericPart(
			"//" + auth + "/"), null);
		
		// Splice the path, skip the first because it is root
		for (int n = splice.length, i = 0; i < n; i++)
		{
			String sub = splice[i];
			
			// If this is not the last one, traverse the directory
			if (i + 1 < n)
				__conn.setFileConnection(sub + "/");
			
			// Otherwise, set the final file
			else
				__conn.setFileConnection(sub);
		}
		
		// Return the final connection
		return __conn;
	}
}
