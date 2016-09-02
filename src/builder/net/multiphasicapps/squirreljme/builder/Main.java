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

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
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
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;
import net.multiphasicapps.io.hexdumpstream.HexDumpOutputStream;
import net.multiphasicapps.squirreljme.projects.PackageList;
import net.multiphasicapps.squirreljme.emulator.Emulator;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifest;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifestAttributes;
import net.multiphasicapps.squirreljme.jit.base.JITTriplet;
import net.multiphasicapps.util.seekablearray.SeekableByteArrayChannel;
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
	 * @throws IOException On read/write errors.
	 * @since 2016/06/24
	 */
	public static void main(String... __args)
		throws IOException
	{
		// Parse the command line
		PrintStream out = System.out;
		__CommandLine__ cl = new __CommandLine__(__args);
		
		// {@squirreljme.error DW10 Skipping the build and not emulating, there
		// is nothing to be done.}
		if (cl._skipbuild && !cl._doemu)
			throw new IllegalArgumentException("DW10");
		
		// Load the package list
		out.println("Loading the package lists...");
		PackageList plist = new PackageList(Paths.get(
			System.getProperty("user.dir")), null);
		
		// Setup build configuration
		BuildConfig config = new BuildConfig(new JITTriplet(cl._target),
			!cl._nojit, cl._tests, cl._altexename,
			cl._extraprojects.<String>toArray(
				new String[cl._extraprojects.size()]));
		
		// Regardless a build instance needs to be found even if a target build
		// is not being performed (for emulation)
		BuildInstance bi = TargetBuilder.findAndCreateBuildInstance(__conf);
		
		// Build the target?
		Path[] actualzipfile = new Path[]{cl._outzipname};
		if (!cl._skipbuild)
		{
			// Delete unneeded files after a build
			Path tempdir = null;
			try
			{
				// Create and register the temporary directory
				tempdir = Files.createTempDirectory("squirreljme-build");
				bi.__setTempDir(tempdir);
				
				// Build target
				if (true)
					throw new Error("TODO");
				
				// Build distribution
				try (ZipStreamWriter zsw = __openOutputZip(actualzipfile[0],
					actualzipfile))
				{
					out.printf("Generating distribution at `%s`...%n",
						actualzipfile[0]);
					bi.buildDistribution(zsw);
				}
				
				// Delete the output file so an illegal partial file does not
				// exist
				catch (IOException|RuntimeException|Error e)
				{
					// Delete it
					try
					{
						Files.delete(actualzipfile[0]);
					}
					
					// Ignore
					catch (IOException f)
					{
					}
					
					// Re-toss
					throw e;
				}
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
		
		// Emulate the resulting binary?
		if (cl._doemu)
		{
			throw new Error("TODO");
		}
		
		if (true)
			throw new Error("TODO");
		
		// Find a target builder which is compatible with this configuration
		TargetBuilder tb = TargetBuilder.findBuilder(config);
		
		// {@squirreljme.error DW0i No available builder targets the given
		// triplet. (The triplet)}
		if (tb == null)
			throw new IllegalArgumentException(String.format("DW0i %s",
				config.triplet()));
		
		// If not skipping the build then build
		Path[] distoutpath = new Path[1];
		if (!cl._skipbuild)
		{
			// Could fail
			Path tempdir = null;
			try
			{
				// Create temporary directory
				tempdir = Files.createTempDirectory("squirreljme-build");
			
				// Setup
				NewBuilder nb = new NewBuilder(out, config, tb, plist,
					tempdir);
			
				// Build
				nb.build();
			
				// Indicate where the binary is
				try (OutputStream distout = __openOutputZip(
					(cl._outzipname != null ? Paths.get(cl._outzipname) :
						null), distoutpath))
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
			distoutpath[0] = Paths.get((cl._outzipname != null ?
				cl._outzipname : "squirreljme.zip"));
		
		// Emulate?
		if (cl._doemu)
			try (ZipFile zip = __openZip(cl, distoutpath[0]))
			{
				// Get the target emulator
				TargetEmulator te = tb.emulate(new TargetEmulatorArguments(
					config, zip, cl._altexename,
					cl._emuargs.<String>toArray(
						new String[cl._emuargs.size()])));
				
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
	 * Opens the ZIP file. Depending on the input arguments it will either be
	 * the target output to be tested or if {@code -l} was specified it will
	 * then be a temporary ZIP containing the binary to emulate.
	 *
	 * @param __cl The parsed command line arguments.
	 * @param __dop The distribution output ZIP.
	 * @return The zip file for the binary to be emulated.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/20
	 */
	private static ZipFile __openZip(__CommandLine__ __cl, Path __dop)
		throws IOException, NullPointerException
	{
		// If directly emulating an executable then it must be opened first
		if (__cl._directemu)
			try (InputStream is = Channels.newInputStream(FileChannel.open(
				__cl._directemupath, StandardOpenOption.READ));
				ByteArrayOutputStream baos = new ByteArrayOutputStream())
			{
				// Copy the binary data into the output ZIP
				try (ZipStreamWriter zsw = new ZipStreamWriter(baos);
					OutputStream os = zsw.nextEntry(__cl._altexename,
						ZipCompressionType.DEFAULT_COMPRESSION))
				{
					byte[] buf = new byte[512];
					for (;;)
					{
						int rc = is.read(buf);
						
						// EOF?
						if (rc < 0)
							break;
						
						// Write
						os.write(buf, 0, rc);
					}
				}
				
				// Load the output ZIP
				return ZipFile.open(new SeekableByteArrayChannel(
					baos.toByteArray()));
			}
		
		// Otherwise open the target ZIP
		return ZipFile.open(FileChannel.open(__dop, StandardOpenOption.READ));
	}
}

