// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.builder;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.DirectoryStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import net.multiphasicapps.sjmepackages.PackageList;
import net.multiphasicapps.squirreljme.emulator.Emulator;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifest;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifestAttributes;
import net.multiphasicapps.squirreljme.jit.base.JITTriplet;
import net.multiphasicapps.zip.blockreader.ZipFile;
import net.multiphasicapps.zip.ZipCompressionType;
import net.multiphasicapps.zip.streamwriter.ZipStreamWriter;

/**
 * This is the main entry point for the builder.
 *
 * @since 2016/06/24
 */
public class Main
{
	/** One second in pico seconds. */
	public static final long ONE_SECOND_IN_PICOSECONDS =
		1_000_000_000_000L;
	
	/** One second in nano seconds. */
	public static final long ONE_SECOND_IN_NANOSECONDS =
		1_000_000_000L;
	
	/** The output ZIP file name. */
	public static final Path OUTPUT_ZIP_NAME =
		Paths.get("squirreljme.zip");
	
	/** The format used for duplicate output names. */
	public static final String OUTPUT_ZIP_FORMAT =
		"squirreljme-%d.zip";
	
	/** The number of ZIPs to attempt to create before ultimately failing. */
	private static final int _MAX_ZIP_TRIES =
		1024;
	
	/**
	 * Main entry point.
	 *
	 * @param __args Main program arguments.
	 * @since 2016/06/24
	 */
	public static void main(String... __args)
	{
		// Must exist
		if (__args == null)
			__args = new String[0];
		
		// Fill in queue for argument handling
		Deque<String> args = new LinkedList<>();
		for (String s : __args)
			args.offerLast(s);
		
		// Handle arguments
		boolean doemu = false;
		boolean nojit = false;
		boolean tests = false;
		boolean skipbuild = false;
		String target = null;
		String outzipname = null;
		String altexename = null;
		List<String> emuargs = new ArrayList<>();
		while (!args.isEmpty())
		{
			String a = args.removeFirst();
			
			// Emulate also?
			if (a.equals("-e"))
				doemu = tests = true;
			
			// Do not include a JIT?
			else if (a.equals("-n"))
				nojit = true;
			
			// Include tests also?
			else if (a.equals("-t"))
				tests = true;
			
			// Skip building?
			else if (a.equals("-s"))
				skipbuild = true;
			
			// Alternative executable name?
			else if (a.equals("-x"))
			{
				altexename = args.removeFirst();
				
				// {@squirreljme.error DW0u The alternative executable name
				// requires an argument.}
				if (altexename == null)
					throw new IllegalArgumentException("DW0u");
			}
			
			// {@squirreljme.error DW02 Unknown command line argument.
			// (The argument)}
			else if (a.startsWith("-"))
				throw new IllegalArgumentException(String.format("DW02 %s",
					a));
			
			// Get the target
			else
			{
				// Set
				target = a.trim();
				
				// Get the output ZIP name.
				if (args.size() >= 1)
					outzipname = args.removeFirst();
				
				// Add emulator arguments
				while (!args.isEmpty())
					emuargs.add(args.removeFirst());
				
				// Stop
				break;
			}
		}
		
		// Output
		PrintStream out = System.out;
		
		// If no target, print usage
		if (target == null)
		{
			__printUsage(out);
			
			// {@squirreljme.error DW0h Not enough arguments.}
			throw new IllegalArgumentException("DW0h");
		}
		
		// Setup build configuration
		BuildConfig config = new BuildConfig(new JITTriplet(target),
			!nojit, tests, altexename);
		
		// Find a target builder which is compatible with this configuration
		TargetBuilder tb = TargetBuilder.findBuilder(config);
		
		// {@squirreljme.error DW0i No available builder targets the given
		// triplet. (The triplet)}
		if (tb == null)
			throw new IllegalArgumentException(String.format("DW0i %s",
				config.triplet()));
		
		// If not skipping the build then build
		Path[] distoutpath = new Path[1];
		if (!skipbuild)
		{
			// Could fail
			PackageList plist;
			Path tempdir = null;
			try
			{
				// Load the package list
				out.println("Loading the package lists...");
				plist = new PackageList(Paths.get(
					System.getProperty("user.dir")), null);
			
				// Create temporary directory
				tempdir = Files.createTempDirectory("squirreljme-build");
			
				// Setup
				NewBuilder nb = new NewBuilder(out, config, tb, plist,
					tempdir);
			
				// Build
				nb.build();
			
				// Indicate where the binary is
				try (OutputStream distout = __openOutputZip(
					(outzipname != null ? Paths.get(outzipname) : null),
					distoutpath))
				{
					out.printf("Generating distribution at `%s`...%n",
						distoutpath[0]);
					nb.linkAndGeneratePackage(distout);
				}
			}
		
			// {@squirreljme.error DW0j There was an error building the
			// output distribution ZIP file.}
			catch (IOException|RuntimeException|Error e)
			{
				// Delete the failed output
				if (distoutpath[0] != null)
					try
					{
						Files.delete(distoutpath[0]);
					}
				
					// Ignore
					catch (IOException f)
					{
						e.addSuppressed(f);
					}
				
				// Toss
				throw new RuntimeException("DW0j", e);
			}
		
			// Delete temporary directory
			finally
			{
				// Delete if it exists
				if (tempdir != null)
					try
					{
						// Delete all files in the directory
						try (DirectoryStream<Path> ds = Files.
							newDirectoryStream(tempdir))
						{
							for (Path p : ds)
								try
								{
									Files.delete(p);
								}
							
								// Ignore
								catch (IOException e)
								{
								}
						}
					
						// Delete the directory
						Files.delete(tempdir);
					}
				
					// Ignore
					catch (IOException e)
					{
					}
			}
		}
		
		// Get output ZIP file used
		else
			distoutpath[0] = Paths.get((outzipname != null ? outzipname :
				"squirreljme.zip"));
		
		// Emulate?
		if (doemu)
			try (FileChannel fc = FileChannel.open(distoutpath[0],
				StandardOpenOption.READ);
				ZipFile zip = ZipFile.open(fc))
			{
				// Get the target emulator
				TargetEmulator te = tb.emulate(new TargetEmulatorArguments(
					config, zip, altexename,
					emuargs.<String>toArray(new String[emuargs.size()])));
				
				// Get an emulator
				out.println("Setting up emulator...");
				Emulator emu = te.emulator();
				
				// Run it
				out.println("Entering emulation loop...");
				emu.run();
				
				// Message
				out.println("Emulation terminated!");
			}
			
			// {@squirreljme.error DW0t I/O error while emulating.}
			catch (IOException e)
			{
				throw new RuntimeException("DW0t", e);
			}
	}
	
	/**
	 * Attempts to create output ZIP files which target a given system.
	 *
	 * @param __oz Alternative ZIP output name to use.
	 * @param __out The path that was written.
	 * @return An output stream to the output ZIP.
	 * @throws IOException If it could not created.
	 * @throws NullPointerException On null arguments, except for {@code __oz}.
	 * @since 2016/07/20
	 */
	private static OutputStream __openOutputZip(Path __oz, Path[] __out)
		throws IOException, NullPointerException
	{
		// Check
		if (__out == null)
			throw new NullPointerException("NARG");
		
		// Might already exist
		try
		{
			// Name to use
			Path usepath = (__oz != null ? __oz : OUTPUT_ZIP_NAME);
			
			// Try it
			__out[0] = usepath;
			return Channels.newOutputStream(FileChannel.open(usepath,
				StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE));
		}
		
		// Try again
		catch (FileAlreadyExistsException e)
		{
			for (int i = 1; i < _MAX_ZIP_TRIES && __oz == null; i++)
				try
				{
					Path p = Paths.get(String.format(OUTPUT_ZIP_FORMAT, i));
					__out[0] = p;
					return Channels.newOutputStream(FileChannel.open(p,
						StandardOpenOption.CREATE_NEW,
						StandardOpenOption.WRITE));
				}
				
				// Does not exist
				catch (FileAlreadyExistsException f)
				{
					continue;
				}
			
			// {@squirreljme.error DW0m Could not create output ZIP file.}
			throw new IOException("DW0m");
		}
	}
	
	/**
	 * Prints usage information.
	 *
	 * @param __ps The output print stream.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/22
	 */
	private static void __printUsage(PrintStream __ps)
		throws NullPointerException
	{
		// Check
		if (__ps == null)
			throw new NullPointerException("NARG");
		
		// Print header
		__ps.println("Usage: [-e] [-n] [-s] [-t] [-x name]" +
			"(target) [squirreljme.zip] [emulator arguments...]");
		__ps.println();
		__ps.println("\tThe output ZIP is optionally specified, however");
		__ps.println("\tif emulator arguments are specified the ZIP must");
		__ps.println("\talso be specified.");
		__ps.println();
		__ps.println("\t-e\tAfter building, emulate the target binary,");
		__ps.println("\t\timplies -t.");
		__ps.println("\t-n\tDo not include a JIT.");
		__ps.println("\t-s\tSkip building and just emulate the ZIP.");
		__ps.println("\t-t\tInclude tests.");
		__ps.println("\t-x\tAlternative name for the binary executable");
		__ps.println("\t\tinstead of using the default name.");
		__ps.println();
		
		// Suggest target header
		__ps.println("Suggested Triplets (Target):");
		__ps.printf("%74s (?)", "Name of Suggestion");
		__ps.println();
		__ps.println();
		
		// Print suggested targets
		for (TargetBuilder tb : ServiceLoader.<TargetBuilder>load(
			TargetBuilder.class))
			for (TargetSuggestion s : tb.suggestedTargets())
			{
				__ps.printf("%-78s%n%74s (%c)", s.triplet(), s.name(),
					(tb.canJIT() ? '+' : '-'));
				__ps.println();
			}
		__ps.println();
		__ps.println(" (+) The JIT is supported.");
		__ps.println(" (-) The JIT is not supported.");
		
		// End
		__ps.println();
	}
}

