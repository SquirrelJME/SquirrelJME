// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.builder.support.vmshader;

import cc.squirreljme.builder.support.Binary;
import cc.squirreljme.builder.support.BinaryManager;
import cc.squirreljme.builder.support.NoSourceAvailableException;
import cc.squirreljme.builder.support.ProjectManager;
import cc.squirreljme.builder.support.TimeSpaceType;
import cc.squirreljme.runtime.cldc.asm.SystemProperties;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import java.util.LinkedList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
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
public class Shader
{
	/**
	 * Shades the JAR.
	 *
	 * @param __pm The project manager to use.
	 * @param __ts The timespace type.
	 * @param __withboot Use the bootstrap?
	 * @param __bootjar Bootstrap JAR to use.
	 * @param __out The stream to write the ZIP to.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/12/22
	 */
	public static void shade(ProjectManager __pm, TimeSpaceType __ts,
		boolean __withboot, Path __bootjar, OutputStream __out)
		throws IOException, NullPointerException
	{
		if (__pm == null || __ts == null ||
			(__withboot && __bootjar == null) || __out == null)
			throw new NullPointerException("NARG");
		
		// Setup output ZIP stream
		ZipStreamWriter zsw = new ZipStreamWriter(__out);
		
		// Files which were put in the JAR, to detect duplicates and
		// such!
		Set<String> putin = new HashSet<>();
		
		// Make sure that the bootstrap is always placed in first, this
		// provides the API and such (for example on Java SE systems we need
		// to provide the Swing code and such)
		if (__withboot)
			Shader.__injectRuntimeJar(zsw, __bootjar, putin);
		
		// Completely shade in the JAR for the entire run-time which we
		// selected
		BinaryManager bm = __pm.binaryManager(__ts);
		Shader.__shadeJar(zsw, bm);
		
		// All of our binaries are going to be merged into one and shaded in
		// the top level JAR. This includes the VM implementation
		Set<Binary> mergebins = new LinkedHashSet<>();
		
		// We might be runnign on a Java SE or Java ME system which does not
		// have the full API available, so in that case provide a bunch of
		// stub APIs so that things still somewhat work.
		// However add these in reverse order so that the stubs are always
		// first!
		Binary[] vmstubs = bm.compile(bm.get("common-vm-stubs"));
		for (int i = vmstubs.length - 1; i >= 0; i--)
			mergebins.add(vmstubs[i]);
		
		// Then we just include every single virtual machine that exists, so
		// that way they can be switched to in the event an older one is
		// desired.
		for (Binary bin : bm.compile(bm.get("all-vms")))
			mergebins.add(bin);
		
		// Merge all of those and those binaries into the output JAR as needed
		Shader.__bootIn(zsw, bm, mergebins.<Binary>toArray(
			new Binary[mergebins.size()]), putin);
		
		// End the stream
		zsw.flush();
		zsw.close();
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
		
		// Services that are available
		Map<String, ServicesMerge> services = new LinkedHashMap<>();
		
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
					// Merge in services?
					String name = e.name();
					if (name.startsWith("META-INF/services/"))
					{
						// Get just the name of the class
						name = name.substring(18);
						
						// Setup merge for services
						ServicesMerge merge = services.get(name);
						if (merge == null)
							services.put(name, (merge = new ServicesMerge()));
						
						// Merge in service implementations
						try (BufferedReader br = new BufferedReader(
							new InputStreamReader(e.open())))
						{
							for (;;)
							{
								String ln = br.readLine();
								if (ln == null)
									break;
								
								merge._implementations.add(ln);
							}
						}
						
						// Do not process any further
						continue;
					}
					
					// Only add entries once!
					if (__putin.contains(name))
						continue;
					
					// Do not write manifests!
					if (name.equals("META-INF/MANIFEST.MF"))
						continue;
					
					// Not allowed to add stuff under this package?
					if (name.endsWith(".class") &&
						!Shader.__checkPackage(name))
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
		attr.putValue("MIDlet-1", "SquirrelJME," +
			"squirrejme-vm.png,cc.squirreljme.vm.VMEntryShadedMIDlet");
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
		
		// Write in any services
		for (Map.Entry<String, ServicesMerge> e : services.entrySet())
			try (PrintStream os = new PrintStream(__zsw.nextEntry(
				"META-INF/services/" + e.getKey()), true))
			{
				for (String s : e.getValue().implementations())
					os.println(s);
				os.flush();
			}
	}
	
	/**
	 * Checks to make sure that the given package should be included.
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
			try
			{
				__bm.compile(bin);
			}
			
			// If no source code available, just ignore because it would
			// already be in binary form
			catch (NoSourceAvailableException e)
			{
			}
			
			// Use the name for this JAR
			String name = bin.name().toString();
			
			// Write it to the suite list
			suitelist.println(name);
			
			// Base prefix for this JAR
			String base = "__-squirreljme/" + name + "/";
			
			// Will be copying every single entry to the output
			try (ZipBlockReader zbr = bin.zipBlock())
			{
				// List of resources that are available
				List<String> jarresources = new LinkedList<>();
				
				// Copy every single entry to the output
				for (ZipBlockEntry e : zbr)
				{
					// Ignore directories
					if (e.isDirectory())
						continue;
					
					// Add to resource list
					jarresources.add(e.name());
					
					// Copy the data
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
				
				// Write resource list into the JAR
				try (PrintStream os = new PrintStream(__zsw.nextEntry(base +
					"META-INF/squirreljme/resources.list"), true, "utf-8"))
				{
					// Write to stream
					for (String rcn : jarresources)
						os.println(rcn);
					
					// Make sure it is there!
					os.flush();
				}
			}
		}
		
		// Write the raw suite list to the JAR since it is needed for shading
		try (OutputStream os = __zsw.nextEntry("__-squirreljme/suites.list"))
		{
			rawsuites.writeTo(os);
		}
	}
}

