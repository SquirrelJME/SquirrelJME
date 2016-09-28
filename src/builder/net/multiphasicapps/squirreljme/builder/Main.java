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
import net.multiphasicapps.squirreljme.projects.ProjectList;
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
		ProjectList plist = new ProjectList(Paths.get(
			System.getProperty("user.dir")));
		
		// Setup build configuration
		BuildConfig config = new BuildConfig(new JITTriplet(cl._target),
			!cl._nojit, cl._tests, cl._altexename,
			cl._extraprojects.<String>toArray(
				new String[cl._extraprojects.size()]), plist);
		
		// Regardless a build instance needs to be found even if a target build
		// is not being performed (for emulation)
		BuildInstance bi = TargetBuilder.findAndCreateBuildInstance(config);
		
		// Build the target?
		Path[] actualzipfile = new Path[]{cl._outzipname};
		if (!cl._skipbuild)
		{
			// Delete unneeded files after a build
			Path tempdir = null, tempzip = null;
			try
			{
				// Create temporary output ZIP file
				tempzip = Files.createTempFile("squirreljme-distribution",
					".zip");
				
				// Create and register the temporary directory
				tempdir = Files.createTempDirectory("squirreljme-build");
				bi.__setTempDir(tempdir);
				
				// Build distribution
				try (OutputStream os = Channels.newOutputStream(
					FileChannel.open(tempzip, StandardOpenOption.CREATE_NEW,
					StandardOpenOption.WRITE));
					ZipStreamWriter zsw = new ZipStreamWriter(os))
				{
					bi.buildDistribution(zsw);
				}
				
				// Move the ZIP to its true intended location
				__moveZip(tempzip, actualzipfile);
			}
			
			// Delete temporary directory and ZIP
			finally
			{
				// Delete temporary ZIP?
				if (tempzip != null)
					try
					{
						Files.delete(tempzip);
					}
					
					// Ignore
					catch (IOException e)
					{
					}
				
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
		
		// Emulate the resulting binary (or some other one)?
		if (cl._doemu)
			try (ZipFile zip = __openZip(cl, actualzipfile[0]))
			{
				// Setup arguments
				EmulatorArguments tae = new EmulatorArguments(
					config, zip, cl._altexename,
					cl._emuargs.<String>toArray(
						new String[cl._emuargs.size()]));
				
				// Get an emulator
				out.println("Setting up emulator...");
				Emulator emu = bi.emulator(tae);
				
				// Run it
				out.println("Entering emulation loop...");
				emu.run();
				
				// Message
				out.println("Emulation terminated!");
			}
	}
	
	/**
	 * Moves the output ZIP to the intended location.
	 *
	 * @param __temp The temporary ZIP name.
	 * @param __out The path that was written.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/28
	 */
	private static void __moveZip(Path __temp, Path[] __out)
		throws IOException, NullPointerException
	{
		// Check
		if (__temp == null || __out == null)
			throw new NullPointerException("NARG");
		
		// Predetermined name
		Path p = __out[0];
		if (p != null)
		{
			// {@squirreljme.error DW06 The target distribution file already
			// exists. (The path)}
			if (Files.exists(p))
				throw new IOException(String.format("DW06 %s", p));
			
			// Move it
			Files.move(__temp, p);
			return;
		}
		
		// Try finding an output ZIP name that is not used
		for (int i = 0; i < _MAX_ZIP_TRIES; i++)
		{
			// The path to try
			Path maybe = (i == 0 ? OUTPUT_ZIP_NAME :
				Paths.get(String.format(OUTPUT_ZIP_FORMAT, i)));
			
			// Skip if it exists
			if (Files.exists(maybe))
				continue;
			
			// Move it
			Files.move(__temp, maybe);
			return;
		}
		
		// {@squirreljme.error DW08 Could not find the name to use for the
		// output ZIP file.}
		throw new IOException("DW08");
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

