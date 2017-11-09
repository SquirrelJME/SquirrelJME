// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.host.javase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.ReflectiveOperationException;
import java.lang.reflect.Method;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.Map;
import javax.microedition.midlet.MIDlet;
import net.multiphasicapps.io.hex.HexInputStream;
import net.multiphasicapps.squirreljme.build.projects.FileChannelBlockAccessor;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifest;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifestAttributes;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifestKey;
import net.multiphasicapps.squirreljme.launcher.EntryPoint;
import net.multiphasicapps.squirreljme.launcher.EntryPoints;
import net.multiphasicapps.zip.blockreader.ZipBlockEntry;
import net.multiphasicapps.zip.blockreader.ZipBlockReader;
import net.multiphasicapps.zip.util.InflaterInputStream;

/**
 * This is a hosted launch which runs the first MIDlet detected.
 *
 * @since 2017/02/24
 */
public class HostedLaunch
{
	/** This is the prefix used to override settings. */
	private static final String _APP_PROPERTY_OVERRIDE =
		"net.multiphasicapps.squirreljme.midlet.override.";
	
	/**
	 * Main entry point.
	 *
	 * @param __args Program arguments.
	 * @since 2017/02/24
	 */
	public static void main(String... __args)
	{
		// Initialize if missing
		if (__args == null)
			__args = new String[0];
		
		// Put arguments into a deque for easier handling
		Deque<String> args = new ArrayDeque<>();
		for (String a : __args)
			if (a != null)
				args.offerLast(a);
		
		// Use an alternative MIDlet ID?
		int entryid = 1;
		String peeked = args.peekFirst();
		if (peeked != null && peeked.startsWith("-"))
			entryid = Integer.parseInt(args.removeFirst().substring(1));
		
		// {@squirreljme.error BM0b Insufficient number of arguments.}
		if (args.size() < 1)
			throw new IllegalArgumentException("BM0b");
		
		// Load the target ZIP to the read manifest
		Path p = Paths.get(args.removeFirst());
		Class<?> mainclass;
		boolean stdmain = false;
		try (FileChannel fc = FileChannel.open(p, StandardOpenOption.READ);
			ZipBlockReader zip = new ZipBlockReader(
				new FileChannelBlockAccessor(fc)))
		{
			// Read the manifest
			ZipBlockEntry entry = zip.get("META-INF/MANIFEST.MF");
			JavaManifest man;
			try (InputStream in = entry.open())
			{
				man = new JavaManifest(in);
			}
			JavaManifestAttributes attr = man.getMainAttributes();
			
			// Decode entry points in the MIDlet
			EntryPoints eps = new EntryPoints(attr);
			
			// {@squirreljme.error BM0d The JAR to launch does not specify
			// any midlets and there is no main class.}
			if (eps.isEmpty())
				throw new RuntimeException("BM0d");
			
			// Debug print entry points
			System.err.println("Available programs:");
			for (int i = 0, n = eps.size(); i < n; i++)
				System.err.printf("%d> %s%n", i, eps.get(i));
			System.err.println();
			
			// {@squirreljme.error BM0i The requested entry point identifier is
			// out of range. (The entry point identifier)}
			if (entryid < 0 || entryid >= eps.size())
				throw new RuntimeException(String.format("BM0i %d", entryid));
			EntryPoint ep = eps.get(entryid);
			
			// Set needed information
			mainclass = Class.forName(ep.entryPoint());
			stdmain = !ep.isMidlet();
			
			// This is used to hack around the application properties so that
			// they exist (using their manifest values) without needing to have
			// a very buggy and broken ClassLoader setup to mimick the
			// proper Java ME getResourceAsStream.
			for (Map.Entry<JavaManifestKey, String> e : attr.entrySet())
				System.setProperty(_APP_PROPERTY_OVERRIDE + e.getKey(),
					e.getValue());
		}
		
		// {@squirreljme.error BM0f Failed to find the main class.}
		catch (ClassNotFoundException e)
		{
			throw new RuntimeException("BM0f", e);
		}
		
		// {@squirreljme.error BM0c Failed to read the manifest.}
		catch (IOException e)
		{
			throw new RuntimeException("BM0c", e);
		}
		
		// Construct the main MIDlet then start it
		try
		{
			// Old style main
			if (stdmain)
			{
				// Get method
				Method startmethod = mainclass.getDeclaredMethod("main",
					(Class)String[].class);
				
				// Execute
				startmethod.invoke(null, (Object)args.<String>toArray(
					new String[args.size()]));
			}
			
			// Launch midlet
			else
			{
				// Get constructor
				Constructor<?> cons = mainclass.getConstructor();
			
				// Make sure it can be called
				cons.setAccessible(true);
			
				// So it can be started
				Method startmethod = MIDlet.class.
					getDeclaredMethod("startApp");
				startmethod.setAccessible(true);
			
				// Create instance and start
				System.err.println("Starting application...");
				MIDlet mid = (MIDlet)cons.newInstance();
				startmethod.invoke(mid);
			}
		}
		
		// {@squirreljme.error BM0g Failed to launch the MIDlet.}
		catch (ReflectiveOperationException|SecurityException e)
		{
			// Forward these unwrapped
			if (e instanceof InvocationTargetException)
			{
				Throwable t = e.getCause();
				
				if (t instanceof Error)
					throw (Error)t;
				else if (t instanceof RuntimeException)
					throw (RuntimeException)t;
			}
			
			throw new RuntimeException("BM0g", e);
		}
	}
}

