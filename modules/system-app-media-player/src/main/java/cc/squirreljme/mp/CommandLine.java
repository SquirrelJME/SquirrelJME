// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.mp;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.util.EnumerationToIterator;
import cc.squirreljme.runtime.gcf.ContentTypeUtil;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Iterator;
import javax.microedition.io.Connection;
import javax.microedition.io.Connector;
import javax.microedition.io.InputConnection;
import javax.microedition.io.file.FileConnection;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import org.intellij.lang.annotations.Language;
import org.intellij.lang.annotations.PrintFormat;

/**
 * Command line version of the media player.
 *
 * @since 2026/01/04
 */
@SquirrelJMEVendorApi
public class CommandLine
{
	/**
	 * Prints a failure message then exits.
	 *
	 * @param __format The format.
	 * @param __args The arguments to the format.
	 * @since 2026/01/16
	 */
	@SquirrelJMEVendorApi
	public static void fail(@PrintFormat String __format, Object... __args)
	{
		// Print error text
		System.err.printf(__format, __args);
		
		// print help
		CommandLine.printHelp();
		
		// Fail
		System.exit(1);
	}
	
	/**
	 * Finishes the player.
	 *
	 * @param __player The player to finish.
	 * @since 2026/01/16
	 */
	public static void finish(Player __player)
	{
		if (__player == null)
			return;
		
		// Stop
		try
		{
			__player.stop();
		}
		catch (Throwable ignored)
		{
		}
		
		// Close
		try
		{
			__player.close();
		}
		catch (Throwable ignored)
		{
		}
	}
	
	/**
	 * Lists the given URL.
	 *
	 * @param __url The URL to list.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2026/01/16
	 */
	@SquirrelJMEVendorApi
	public static void list(@Language("http-url-reference") String __url)
		throws IOException, NullPointerException
	{
		if (__url == null)
			throw new NullPointerException("NARG");
		
		// Open connection
		try (Connection conn = Connector.open(__url))
		{
			// Can only list files!
			if (!(conn instanceof FileConnection))
				throw new IOException("Not a FileConnection!");
			
			// Cast
			FileConnection file = (FileConnection)conn;
			
			// Where is this going?
			PrintStream out = System.out;
			
			// List contents of the directory and add them accordingly
			Iterator<String> it = new EnumerationToIterator<>(file.list());
			while (it.hasNext())
			{
				// Get the file
				String fn = it.next();
				
				// Determine the content type
				String contentType;
				if (fn.endsWith("/"))
					contentType = "inode/directory";
				else
					contentType = ContentTypeUtil.guessByPath(fn);
				
				// Print content type
				out.print(contentType);
				out.print('\t');
				
				// Navigate to the file to get its true URL
				file.setFileConnection(fn);
				out.print(file.getURL());
				file.setFileConnection("..");
				
				// Then print out the base file name
				out.print('\t');
				out.println(fn);
			}
		}
	}
	
	/**
	 * Main entry point.
	 *
	 * @param __args Program arguments.
	 * @throws InterruptedException If playback is interrupted.
	 * @throws IOException On read/write errors,
	 * @throws MediaException If media could not be played.
	 * @since 2026/01/04
	 */
	@SquirrelJMEVendorApi
	public static void main(String... __args)
		throws InterruptedException, IOException, MediaException
	{
		if (__args == null || __args.length < 1 || __args[0] == null)
		{
			// Print help
			CommandLine.printHelp();
			
			// Fail
			System.exit(1);
			return;
		}
		
		// Which command was passed?
		@Language("http-url-reference")
		String url = null;
		@Language("mime-type-reference")
		String contentType = null;
		switch (__args[0])
		{
				// List directory contents
			case "ls":
			case "list":
				if (__args.length == 1)
					url = Binder.INITIAL_ROOT;
				else if (__args.length == 2 && __args[1] != null)
					url = __args[1];
				else 
				{
					CommandLine.fail("list expects a URL.");
					return;
				}
				
				// Run the list command
				CommandLine.list(url);
				break;
				
				// Play a given URL
			case "play":
				// Grab the URL
				if ((__args.length == 2 || __args.length == 3) &&
					__args[1] != null)
					url = __args[1];
				else
				{
					CommandLine.fail("play expects a URL.");
					return;
				}
				
				// Grab the optional content type
				if (__args.length == 3 && __args[2] != null)
					contentType = __args[2];
				
				// Run the play command
				CommandLine.play(url, contentType);
				break;
			
				// Help
			case "help":
				CommandLine.printHelp();
				break;
			
			default:
				CommandLine.fail("Unknown command: %s", __args[0]);
				break;
		}
	}
	
	/**
	 * Plays the given URL.
	 *
	 * @param __url The URL to play.
	 * @param __contentType The optional content type to use.
	 * @throws InterruptedException If playback is interrupted.
	 * @throws IOException On read errors.
	 * @throws MediaException If media playback fails.
	 * @throws NullPointerException On null arguments.
	 * @since 2026/01/16
	 */
	@SquirrelJMEVendorApi
	public static void play(@Language("http-url-reference") String __url,
		@Language("mime-type-reference") String __contentType)
		throws InterruptedException, IOException,
			MediaException, NullPointerException
	{
		if (__url == null)
			throw new NullPointerException("NARG");
		
		// Where is this going?
		PrintStream out = System.out;
		
		// Open connection to the media
		Player player = null;
		try (Connection conn = Connector.open(__url))
		{
			try (InputStream in = ((InputConnection)conn).openInputStream())
			{
				// Create player for the URL
				player = Manager.createPlayer(in, __contentType);
				
				// What is the player type?
				String playerType = player.getClass().getName();
				int ld = Math.max(playerType.lastIndexOf('.'),
					playerType.lastIndexOf('/'));
				if (ld >= 0)
					playerType = playerType.substring(ld + 1);
				
				// Initial space
				out.println();
				
				// Progress before realization
				CommandLine.printProgress(out, playerType, player,
					Utils.formatTime(Player.TIME_UNKNOWN));
				
				// Realize
				player.realize();
				
				// Progress before prefetch
				CommandLine.printProgress(out, playerType, player,
					Utils.formatTime(Player.TIME_UNKNOWN));
				
				// Prefetch
				player.prefetch();
				
				// Only play once!
				player.setLoopCount(1);
				
				// How long is this song?
				long duration = player.getDuration();
				String formatDur = Utils.formatTime(duration);
				
				// Progress before start
				CommandLine.printProgress(out, playerType, player, formatDur);
				
				// Play and wait until the song ends
				player.start();
				do
				{
					// Print progress
					CommandLine.printProgress(out, playerType, player, formatDur);
					
					// Wait for the next update
					Thread.sleep(250);
				} while (player.getState() >= Player.STARTED);
				
				// Print last set of progress after stop occurred
				CommandLine.printProgress(out, playerType, player, formatDur);
				
				// End space
				out.println();
			}
		}
		
		// Make sure the player is fully closed before leaving
		finally
		{
			CommandLine.finish(player);
		}
	}
	
	/**
	 * Prints help text.
	 *
	 * @since 2026/01/04
	 */
	@SquirrelJMEVendorApi
	public static void printHelp()
	{
		PrintStream out = System.err;
		
		// Print everything
		out.println("Usage: command [arguments]");
		out.println("\tlist (url)");
		out.println("\t     -- Lists contents of the root directory.");
		out.println("\t     url: The URL to list the contents of.");
		out.println("\tplay url (contentType)");
		out.println("\t     -- Plays the given media by its URL.");
		out.println("\t     url: The URL to play.");
		out.println("\t     contentType: The content type to use.");
	}
	
	/**
	 * Prints progress.
	 *
	 * @param __out Where to print to.
	 * @param __playerType The player type.
	 * @param __player The current player.
	 * @param __formatDur The formatted duration.
	 * @throws NullPointerException On null arguments.
	 * @since 2026/01/16
	 */
	@SquirrelJMEVendorApi
	public static void printProgress(PrintStream __out, String __playerType,
		Player __player, String __formatDur)
		throws NullPointerException
	{
		if (__out == null || __player == null)
			throw new NullPointerException("NARG");
		
		__out.printf("~%s: [%s / %s] %s^", __playerType,
			Utils.formatTime(__player.getMediaTime()), __formatDur,
			Utils.formatState(__player.getState()));
		__out.print("\r");
	}
}
