// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vmshader;

import cc.squirreljme.builder.support.Binary;
import cc.squirreljme.builder.support.BinaryManager;
import cc.squirreljme.builder.support.ProjectManager;
import cc.squirreljme.builder.support.TimeSpaceType;
import cc.squirreljme.runtime.cldc.asm.SystemProperties;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import net.multiphasicapps.tool.manifest.writer.MutableJavaManifest;
import net.multiphasicapps.tool.manifest.writer.MutableJavaManifestAttributes;
import net.multiphasicapps.zip.blockreader.ZipBlockEntry;
import net.multiphasicapps.zip.blockreader.ZipBlockReader;
import net.multiphasicapps.zip.streamreader.ZipStreamEntry;
import net.multiphasicapps.zip.streamreader.ZipStreamReader;
import net.multiphasicapps.zip.streamwriter.ZipStreamWriter;

/**
 * This class builds a shaded JAR of the virtual machine which contains the
 * classes for every JAR in SquirrelJME. This just uses the build system to
 * produce a JAR that is combined as one.
 *
 * @since 2018/11/16
 */
public class Main
{
	/**
	 * Main entry point.
	 *
	 * @param __args Main entry points.
	 * @since 2018/11/16
	 */
	public static void main(String... __args)
	{
		// Copy arguments for processing
		Queue<String> args = new ArrayDeque<>();
		if (__args != null)
			for (String a : __args)
				if (a != null)
					args.add(a);
		
		// Setup project manager
		ProjectManager pm = ProjectManager.fromArguments(args);
		
		// Remove this, because this is annoying
		if ("--".equals(args.peek()))
			args.remove();
		
		// Flavor this to Java SE or Java ME?
		boolean javaseflavor = false;
		Path extraruntjar = null;
		if (!args.isEmpty())
			switch (args.peek())
			{
				case "-javase":
					javaseflavor = true;
					args.remove();
					
					// {@squirreljme.error AE02 Need to specify the run-time
					// JAR to support Java SE systems, on default SquirrelJME
					// this will be `bootsjme/javase-runtime.jar`.}
					if (args.isEmpty())
						throw new IllegalArgumentException("AE02");
					extraruntjar = Paths.get(args.remove());
					break;
					
				case "-javame":
					javaseflavor = false;
					args.remove();
					break;
			}
		
		// Determine the path where our shaded JAR will be built
		Path output = Paths.get((args.isEmpty() ? "squirreljme-" +
			(javaseflavor ? "javase" : "javame") + ".jar" :
			args.remove()));
		
		// Build the output JAR at this path
		Path tempfile = null;
		try
		{
			// Build from the test system so we have access to all of those
			// JARs including the tests
			BinaryManager bm = pm.binaryManager(TimeSpaceType.RUNTIME);
			
			// Create temporary file to place the JAR at
			tempfile = Files.createTempFile("squirreljme-", ".ja_");
			
			// Open output ZIP in that spot
			try (ZipStreamWriter zsw = new ZipStreamWriter(
				Files.newOutputStream(tempfile,
					StandardOpenOption.CREATE,
					StandardOpenOption.WRITE,
					StandardOpenOption.TRUNCATE_EXISTING)))
			{
				// Files which were put in the JAR, to detect duplicates and
				// such!
				Set<String> putin = new HashSet<>();
				
				// Inject our Java SE binary?
				if (extraruntjar != null)
					Main.__injectRuntimeJar(zsw, extraruntjar, putin);
				
				// Shade the JAR
				Main.__shadeJar(zsw, bm);
				
				// We need to include some base libraries needed for the VM
				// to run on Java SE for example.
				// But the extra run-time JAR is used for this
				Binary[] sscp;
				if (javaseflavor)
					sscp = new Binary[0];
				
				// Otherwise this is running on Java ME, so use some of our
				// own provided stubs for this
				else
					sscp = bm.compile(bm.get("common-vm-stubs"));
				
				// Only use the last library for the shade support stuff
				List<Binary> mergebins = new ArrayList<>();
				if (sscp.length > 0)
					mergebins.add(sscp[sscp.length - 1]);
				
				// Then add our normal classpath dependencies as needed
				for (Binary bin : bm.compile(bm.get("all-vms")))
					mergebins.add(bin);
				
				// SpringCoat is to be placed in the JAR now
				Main.__bootIn(zsw, bm, mergebins.<Binary>toArray(
					new Binary[mergebins.size()]), putin);
			}
			
			// Move the file to the output since it was built!
			Files.move(tempfile, output,
				StandardCopyOption.REPLACE_EXISTING);
		}
		
		// {@squirreljme.error AE01 Could not build the output shaded JAR.}
		catch (IOException e)
		{
			throw new IllegalArgumentException("AE01", e);
		}
		
		// No matter what, always try to clear the temp file
		finally
		{
			if (tempfile != null)
				try
				{
					Files.delete(tempfile);
				}
				catch (IOException e)
				{
					// Ignore this
				}
		}
	}
	
	/**
	 * Builds and loads the classes into.
	 *
	 * @param __zsw The ZIP to write to.
	 * @param __bm The manager for binaries.
	 * @param __bins The binaries to write, the compiled paths.
	 * @param __putin Files already in the JAR.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/16
	 */
	private static final void __bootIn(ZipStreamWriter __zsw,
		BinaryManager __bm, Binary[] __bins, Set<String> __putin)
		throws IOException, NullPointerException
	{
		if (__zsw == null || __bm == null || __bins == null || __putin == null)
			throw new NullPointerException("NARG");
		
		// We want to write and merge all the entries
		byte[] buf = new byte[512];
		for (Binary bin : __bins)
		{
			// Will be copying every single entry to the output
			try (ZipBlockReader zbr = bin.zipBlock())
			{
				// Copy every single entry to the output
				for (ZipBlockEntry e : zbr)
				{
					// Only add entries once!
					String name = e.name();
					if (__putin.contains(name))
						continue;
					
					// Do not write manifests!
					if (name.equals("META-INF/MANIFEST.MF"))
						continue;
					
					// Not allowed to add stuff under this package?
					if (name.endsWith(".class") && !Main.__checkPackage(name))
						continue;
					
					// This was added, so it gets ignored
					__putin.add(name);
					
					// Copy the data in here
					try (InputStream is = e.open();
						OutputStream os = __zsw.nextEntry(name))
					{
						for (;;)
						{
							int rc = is.read(buf);
							
							if (rc < 0)
								break;
							
							os.write(buf, 0, rc);
						}
					}
				}
			}
		}
		
		// We need to know the classpath that is used for the launcher so
		// that the shaded entry main knows how to start the actual launcher!
		// Since all classes are needed for it!
		StringBuilder launchercp = new StringBuilder();
		for (Binary bin : __bm.classPath(__bm.get("launcher")))
		{
			if (launchercp.length() > 0)
				launchercp.append(' ');
			
			launchercp.append(bin.name());
		}
		
		// Setup manifest which allows us to run this on SpringCoat along
		// with doing a normal `java -jar` on it!
		MutableJavaManifest man = new MutableJavaManifest();
		MutableJavaManifestAttributes attr = man.getMainAttributes();
		
		// Needed for Java SE
		attr.putValue("Main-Class",
			"cc.squirreljme.vm.VMEntryShadedMain");
		
		// Needed for Java ME
		attr.putValue("MIDlet-1",
			"cc.squirreljme.vm.VMEntryShadedMIDlet,,SquirrelJME");
		attr.putValue("MicroEdition-Configuration", "CLDC-1.8");
		attr.putValue("MicroEdition-Profile", "MEEP-8.0 MIDP-3.1");
		attr.putValue("MIDlet-Name", "SquirrelJME");
		attr.putValue("MIDlet-Vendor", "Stephanie Gawroriski");
		attr.putValue("MIDlet-Version", SystemProperties.javaRuntimeVersion());
		
		// Write manifest to the output
		try (OutputStream os = __zsw.nextEntry("META-INF/MANIFEST.MF"))
		{
			man.write(os);
		}
		
		// Write VM properties into its own manifest as well
		man = new MutableJavaManifest();
		attr = man.getMainAttributes();
		
		// Needed by the VM so it knows the boot classpath for the launcher!
		attr.putValue("ClassPath", launchercp.toString());
		
		// Write manifest to the output
		try (OutputStream os = __zsw.nextEntry(
			"META-INF/SQUIRRELJME-SHADED.MF"))
		{
			man.write(os);
		}
	}
	
	/**
	 * Checks to make sure that the given packet
	 *
	 * @param __name The name of the class to check.
	 * @return If the class can be stored.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/16
	 */
	private static final boolean __checkPackage(String __name)
		throws NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		// There is no slash so this is okay!
		int ls = __name.lastIndexOf('/');
		if (ls < 0)
			return true;
		
		// Do not allow certain names
		switch (__name = __name.substring(0, ls))
		{
				// These will conflict with stuff in the CLDC and such and
				// technically it is not permitted to add these classes so
				// all of these will go away!
			case "java/io":
			case "java/lang":
			case "java/lang/annotation":
			case "java/lang/ref":
			case "java/net":
			case "java/nio":
			case "java/nio/channels":
			case "java/nio/file":
			case "java/nio/file/attribute":
			case "java/security":
			case "java/util":
				return false;
			
				// Can add this class
			default:
				return true;
		}
	}
	
	/**
	 * Injects the run-time support JAR into the classpath.
	 *
	 * @param __zsw The output ZIP.
	 * @param __ertj The support JAR.
	 * @param __putin The files which were already placed in the JAR.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/16
	 */
	private static final void __injectRuntimeJar(ZipStreamWriter __zsw,
		Path __ertj, Set<String> __putin)
		throws IOException, NullPointerException
	{
		if (__zsw == null || __ertj == null || __putin == null)
			throw new NullPointerException("NARG");
		
		// Before we write the other files, we want to inject everything from
		// this JAR in there
		byte[] buf = new byte[512];
		if (__ertj != null)
			try (ZipStreamReader copy = new ZipStreamReader(
				Files.newInputStream(__ertj, StandardOpenOption.READ)))
			{
				for (;;)
					try (ZipStreamEntry e = copy.nextEntry())
					{
						// No more entries
						if (e == null)
							break;
						
						String name = e.name();
						
						// Already in the JAR?
						if (__putin.contains(name))
							continue;
						
						// Ignore manifests
						if (name.equals("META-INF/MANIFEST.MF"))
							continue;
						
						// Only once this can be added
						__putin.add(name);
						
						// Copy data
						try (OutputStream os = __zsw.nextEntry(name))
						{
							for (;;)
							{
								int rc = e.read(buf);
								
								if (rc < 0)
									break;
								
								os.write(buf, 0, rc);
							}
						}
					}
			}
	}
	
	/**
	 * Shades the JAR to the specified output.
	 *
	 * @param __zsw The output ZIP to write to.
	 * @param __bm The binary manager with classes.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/16
	 */
	private static final void __shadeJar(ZipStreamWriter __zsw,
		BinaryManager __bm)
		throws IOException, NullPointerException
	{
		if (__zsw == null || __bm == null)
			throw new NullPointerException("NARG");
		
		// Raw suiite
		ByteArrayOutputStream rawsuites = new ByteArrayOutputStream();
		PrintStream suitelist = new PrintStream(rawsuites, true, "utf-8");
		
		// Go through all the binaries and just place them in the shaded JAR
		// with their associated names and prefixes
		byte[] buf = new byte[512];
		for (Binary bin : __bm)
		{
			// Compile the binary
			__bm.compile(bin);
			
			// Use the name for this JAR
			String name = bin.name().toString();
			
			// Write it to the suite list
			suitelist.println(name);
			
			// Base prefix for this JAR
			String base = "__squirreljme/" + name + "/";
			
			// Will be copying every single entry to the output
			try (ZipBlockReader zbr = bin.zipBlock())
			{
				// Copy every single entry to the output
				for (ZipBlockEntry e : zbr)
					try (InputStream is = e.open();
						OutputStream os = __zsw.nextEntry(base + e.name()))
					{
						for (;;)
						{
							int rc = is.read(buf);
							
							if (rc < 0)
								break;
							
							os.write(buf, 0, rc);
						}
					}
			}
		}
		
		// Write the raw suite list to the JAR since it is needed for shading
		try (OutputStream os = __zsw.nextEntry("__squirreljme/suites.list"))
		{
			rawsuites.writeTo(os);
		}
	}
}

