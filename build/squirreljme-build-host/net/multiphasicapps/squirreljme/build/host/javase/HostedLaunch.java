// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
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
import java.lang.reflect.Method;
import java.lang.ReflectiveOperationException;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Map;
import javax.microedition.midlet.MIDlet;
import net.multiphasicapps.io.hex.HexInputStream;
import net.multiphasicapps.io.inflate.InflaterInputStream;
import net.multiphasicapps.squirreljme.build.projects.FileChannelBlockAccessor;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifest;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifestAttributes;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifestKey;
import net.multiphasicapps.zip.blockreader.ZipBlockEntry;
import net.multiphasicapps.zip.blockreader.ZipBlockReader;

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
		
		// {@squirreljme.error BM0b Insufficient number of arguments.}
		if (__args.length < 1)
			throw new IllegalArgumentException("BM0b");
		
		// Load the target ZIP to the read manifest
		Path p = Paths.get(__args[0]);
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
			
			// This might not be an actual MIDlet
			String midletval = attr.get(new JavaManifestKey("MIDlet-1"));
			if (midletval == null)
			{
				// {@squirreljme.error BM0d The JAR to launch does not specify
				// any midlets and there is no main class.}
				String oldclass = attr.get(new JavaManifestKey("Main-Class"));
				if (oldclass == null)
					throw new RuntimeException("BM0d");
				
				// Use standard main
				mainclass = Class.forName(oldclass);
				stdmain = true;
			}
			
			else
			{
				// The MIDlet field is in 3 fields: name, icon, class
				// {@squirreljme.error BM0e Expected two commas in the MIDlet
				// field.}
				int pc = midletval.indexOf(','),
					sc = midletval.indexOf(',', Math.max(pc + 1, 0));
				if (pc < 0 || sc < 0)
					throw new RuntimeException("BM0e");
			
				// Split fields
				String name = midletval.substring(0, pc).trim(),
					icon = midletval.substring(pc + 1, sc).trim(),
					main = midletval.substring(sc + 1).trim();
			
				// Get the main class, which is in our own classpath!
				mainclass = Class.forName(main);
			}
			
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
				Method startmethod = mainclass.getMethod("main",
					(Class)String[].class);
				
				// Execute
				startmethod.invoke(null, (Object)Arrays.<String>copyOfRange(
					__args, 1, __args.length));
			}
			
			// Launch midlet
			else
			{
				// Get constructor
				Constructor<?> cons = mainclass.getConstructor();
			
				// Make sure it can be called
				cons.setAccessible(true);
			
				// Create instance
				MIDlet mid = (MIDlet)cons.newInstance();
			
				// Start it
				Method startmethod = mainclass.getMethod("startApp");
				startmethod.setAccessible(true);
			
				// Call it
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

