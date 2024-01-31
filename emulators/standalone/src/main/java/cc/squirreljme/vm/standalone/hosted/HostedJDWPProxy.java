// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.standalone.hosted;

import cc.squirreljme.jdwp.host.JDWPHostFactory;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Acts as a proxy between the start process and the resultant virtual
 * machine.
 *
 * @since 2024/01/30
 */
public class HostedJDWPProxy
	implements Closeable
{
	/** The port to connect to. */
	public final int port;
	
	/** Factory for creating JDWP connections. */
	protected final JDWPHostFactory jdwpFactory;
	
	/** The server connection. */
	protected final ServerSocket server;
	
	/** The acceptor thread. */
	protected final Thread threadAccept;
	
	/** The input pipe thread. */
	private volatile Thread _threadIn;
	
	/** The output pipe thread. */
	private volatile Thread _threadOut;
	
	/**
	 * Initializes the hosted JDWP proxy.
	 *
	 * @param __jdwpFactory The pipes to proxy to.
	 * @throws IOException If the socket could not be opened.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/30
	 */
	public HostedJDWPProxy(JDWPHostFactory __jdwpFactory)
		throws IOException, NullPointerException
	{
		if (__jdwpFactory == null)
			throw new NullPointerException("NARG");
		
		// Store for later proxying
		this.jdwpFactory = __jdwpFactory;
		
		// Make sure the proxy is closed on exit
		Runtime.getRuntime().addShutdownHook(new Thread(this::kill,
			"JDWPProxyKiller"));
		
		// Setup acceptor for connections from the JVM itself
		ServerSocket server = new ServerSocket(0);
		this.server = server;
		this.port = server.getLocalPort();
		
		// Setup acceptor for when the debugger connects
		Thread threadAccept = new Thread(this::proxyAccept,
			"JDWPAccept");
		this.threadAccept = threadAccept;
		
		// Run the acceptor
		threadAccept.start();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/30
	 */
	@Override
	public void close()
		throws IOException
	{
		// Interrupt all threads
		if (this.threadAccept != null)
			this.threadAccept.interrupt();
		if (this._threadIn != null)
			this._threadIn.interrupt();
		if (this._threadOut != null)
			this._threadOut.interrupt();
		
		// Close the server
		if (this.server != null)
			this.server.close();
		
		// Close the original debug pipes
		if (this.jdwpFactory != null)
		{
			this.jdwpFactory.in().close();
			this.jdwpFactory.out().close();
		}
	}
	
	/**
	 * Kills this proxy.
	 *
	 * @since 2024/01/30
	 */
	protected void kill()
	{
		try
		{
			this.close();
		}
		catch (IOException __e)
		{
			__e.printStackTrace();
		}
	}
	
	/**
	 * Accepts the proxy connection.
	 * 
	 * @since 2024/01/30
	 */
	protected void proxyAccept()
	{
		try
		{
			// Accept the first connection that appears
			Socket socket = this.server.accept();
			
			// Use no delay for faster communication and keep alive so it
			// does not just die from a bad route
			socket.setTcpNoDelay(true);
			socket.setKeepAlive(true);
			
			// Proxy input
			Thread threadIn = new Thread(() -> {
					try
					{
						this.proxyComm(socket.getInputStream(),
							this.jdwpFactory.out());
					}
					catch (IOException __e)
					{
						__e.printStackTrace();
					}
				}, "JDWPProxyIn");
			this._threadIn = threadIn;
			
			// Proxy output
			Thread threadOut = new Thread(() -> {
				try
				{
					this.proxyComm(this.jdwpFactory.in(),
						socket.getOutputStream());
				}
				catch (IOException __e)
				{
					__e.printStackTrace();
				}
				}, "JDWPProxyIn");
			this._threadOut = threadOut;
			
			// Start both
			threadIn.start();
			threadOut.start();
		}
		catch (IOException __e)
		{
			__e.printStackTrace();
		}
	}
	
	/**
	 * Proxies from the input to the output.
	 *
	 * @param __in The input stream to read from.
	 * @param __out The output stream to write to.
	 * @since 2024/01/30
	 */
	protected void proxyComm(InputStream __in, OutputStream __out)
		throws NullPointerException
	{
		if (__in == null || __out == null)
			throw new NullPointerException();
		
		// Run communication in a loop
		try
		{
			byte[] buf = new byte[1048576];
			for (;;)
			{
				// Read in data
				int rc = __in.read(buf, 0, buf.length);
				
				// EOF?
				if (rc < 0)
					break;
				
				// Write to the output and flush the target
				__out.write(buf, 0, rc);
				__out.flush();
			}
		}
		
		// Failed?
		catch (IOException __e)
		{
			__e.printStackTrace();
		}
		
		// Make sure everything is cleaned up
		finally
		{
			try
			{
				this.close();
			}
			catch (IOException __e)
			{
				__e.printStackTrace();
			}
		}
	}
}
