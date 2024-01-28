// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

import cc.squirreljme.emulator.NativeBinding;
import cc.squirreljme.jdwp.JDWPCommLink;
import cc.squirreljme.jdwp.JDWPCommLinkDirection;
import cc.squirreljme.jdwp.JDWPEventKind;
import cc.squirreljme.jdwp.JDWPSuspendPolicy;
import java.awt.Frame;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Main entry point for the debugger.
 *
 * @since 2024/01/19
 */
public class Main
{
	static
	{
		// We need to poke native binding, so it loads our emulation backend
		NativeBinding.loadedLibraryPath();
		
		// Set look and feel, decorating greatly improved speed
		JFrame.setDefaultLookAndFeelDecorated(true);
	}
	
	/**
	 * Main entry point.
	 *
	 * @param __args Arguments to the program.
	 * @since 2024/01/19
	 */
	public static void main(String... __args)
	{
		try
		{
			// If no options passed, ask for them
			String connect;
			if (__args.length == 0)
				connect = (String)JOptionPane.showInputDialog(
					null,
					"Connection address ([hostname]:port):",
					"Debugger Connect", JOptionPane.QUESTION_MESSAGE,
					null,
					null, ":5005");
			else
				connect = __args[0];
			
			// Setup communication link
			JDWPCommLink commLink = Main.__connect(connect);
			
			// Start the main debug session
			Main.start(commLink);
		}
		
		// Failed to emit exception
		catch (Throwable __e)
		{
			// Oh well...
			__e.printStackTrace(System.err);
			
			// Emit dialog
			Utils.throwableTraceDialog(null,
				"Failed to start debugger", __e);
			
			// Failed
			System.exit(1);
		}
	}
	
	/**
	 * Connects to the specified server.
	 *
	 * @param __connect The connection to use.
	 * @return The resultant communication link with JDWP.
	 * @throws IOException On open/read/write errors.
	 * @throws IllegalArgumentException If the connect string is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/19
	 */
	private static JDWPCommLink __connect(String __connect)
		throws IOException, IllegalArgumentException, NullPointerException
	{
		if (__connect == null)
			throw new NullPointerException("NARG");
		
		// There should be a port
		int lastCol = __connect.lastIndexOf(':');
		if (lastCol < 0)
			throw new IOException("Invalid address: " + __connect);
		
		// Decode
		int port;
		try
		{
			port = Integer.parseInt(
				__connect.substring(lastCol + 1), 10);
		}
		catch (NumberFormatException __e)
		{
			throw new IOException("Invalid port: " + __connect, __e);
		}
		
		// Listen?
		Socket socket;
		if (lastCol == 0)
			socket = new ServerSocket(port).accept();
		
		// Connect?
		else
			socket = new Socket(__connect.substring(0, lastCol), port);
		
		// Set some socket options
		socket.setKeepAlive(true);
		socket.setTcpNoDelay(true);
		
		// Kill socket on exit
		Runtime.getRuntime().addShutdownHook(new Thread(
			new __SocketKill__(socket), "socketKill"));
		
		// Setup communication link
		return new JDWPCommLink(socket.getInputStream(),
			socket.getOutputStream(), JDWPCommLinkDirection.DEBUGGER_TO_CLIENT);
	}
	
	/**
	 * Starts the debugging session.
	 *
	 * @param __in The stream to read from.
	 * @param __out The stream to write from.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/19
	 */
	public static void start(InputStream __in, OutputStream __out)
		throws NullPointerException
	{
		if (__in == null || __out == null)
			throw new NullPointerException("NARG");
		
		// Forward accordingly
		Main.start(new JDWPCommLink(__in, __out,
			JDWPCommLinkDirection.DEBUGGER_TO_CLIENT));
	}
	
	/**
	 * Starts the debugging session.
	 *
	 * @param __commLink The communication link to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/19
	 */
	public static void start(JDWPCommLink __commLink)
		throws NullPointerException
	{
		if (__commLink == null)
			throw new NullPointerException("NARG");
		
		// Wrap into primary debugger state which tracks everything
		DebuggerState state = new DebuggerState(__commLink, (__state) -> {
		});
		
		// Start the debug loop
		new Thread(state, "debugLoop").start();
		
		// Spawn the application
		PrimaryFrame frame = new PrimaryFrame(state);
		
		// Show it in a good spot and maximized as well
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setExtendedState(frame.getExtendedState() |
			Frame.MAXIMIZED_BOTH);
	}
}
