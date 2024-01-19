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
import cc.squirreljme.jdwp.CommLink;
import cc.squirreljme.jdwp.CommLinkDirection;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringWriter;
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
	/**
	 * Main entry point.
	 *
	 * @param __args Arguments to the program.
	 * @since 2024/01/19
	 */
	public static void main(String... __args)
	{
		// We need to poke native binding, so it loads our emulation backend
		NativeBinding.loadedLibraryPath();
		
		// Set look and feel, decorating greatly improved speed
		JFrame.setDefaultLookAndFeelDecorated(true);
		
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
			CommLink commLink = Main.__connect(connect);
			
			// Wrap into primary debugger state which tracks everything
			DebuggerState state = new DebuggerState(commLink);
			new Thread(state, "debugLoop").start();
			
			// Spawn the application
			new PrimaryFrame(state).setVisible(true);
		}
		
		// Failed to emit exception
		catch (Throwable __e)
		{
			String message;
			try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
				PrintStream ps = new PrintStream(baos, true,
					"utf-8"))
			{
				// Print to the output
				__e.printStackTrace(ps);
				
				// Make sure it is flushed
				ps.flush();
				
				message = baos.toString("utf-8");
			}
			catch (IOException __f)
			{
				// Ignore
				message = "Could not emit trace, check stderr.";
			}
			
			// Oh well...
			__e.printStackTrace(System.err);
			
			// Emit dialog
			JOptionPane.showMessageDialog(null,
				message,
				"Debugger start failed",
				JOptionPane.ERROR_MESSAGE);
			
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
	private static CommLink __connect(String __connect)
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
		return new CommLink(socket.getInputStream(),
			socket.getOutputStream(), CommLinkDirection.DEBUGGER_TO_CLIENT);
	}
}
