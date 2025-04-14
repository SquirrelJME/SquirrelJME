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
import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import net.multiphasicapps.io.ForwardInputToOutput;

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
	
	/** The input pipe forwarder. */
	private volatile ForwardInputToOutput _in;
	
	/** The output pipe forwarder. */
	private volatile ForwardInputToOutput _out;
	
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
		
		if (this._in != null)
			this._in.close();
		if (this._out != null)
			this._out.close();
		
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
			
			// Use no delay for faster communication and keep alive, so it
			// does not just die from a bad route
			socket.setTcpNoDelay(true);
			socket.setKeepAlive(true);
			
			// Proxy input
			ForwardInputToOutput in = new ForwardInputToOutput(
				socket.getInputStream(),
				this.jdwpFactory.out());
			this._in = in;
			
			// Proxy output
			ForwardInputToOutput out = new ForwardInputToOutput(
				this.jdwpFactory.in(),
				socket.getOutputStream());
			this._out = out;
			
			// Start both
			in.runThread("JDWPProxyIn");
			out.runThread("JDWPProxyOut");
		}
		catch (IOException __e)
		{
			__e.printStackTrace();
		}
	}
}
