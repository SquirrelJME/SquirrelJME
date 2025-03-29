// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.cicd;

import cc.squirreljme.cicd.circleci.CircleCiJob;
import cc.squirreljme.cicd.circleci.CircleCiWorkflowJobs;
import cc.squirreljme.runtime.cldc.util.StreamUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Main entry point.
 *
 * @since 2024/10/04
 */
public class Main
{
	/** The instance of Fossil. */
	public static final FossilCommand FOSSIL =
		FossilCommand.instance();
	
	/**
	 * Determines the base directory based on the version.
	 * 
	 * @param __version The input version.
	 * @return The resultant base directory.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/10/11
	 */
	public static String baseDir(String __version)
		throws NullPointerException
	{
		if (__version == null)
			throw new NullPointerException("NARG");
		
		// Split version
		String[] fragments = __version.split(Pattern.quote("."));
		if (fragments == null || fragments.length != 3)
			throw new IllegalArgumentException("Invalid version: " +
				__version);
		
		// Determine version digits
		int[] versions = new int[3];
		for (int i = 0; i < 3; i++)
			try
			{
				versions[i] = Integer.parseInt(fragments[i], 10);
			}
			catch (NumberFormatException __e)
			{
				throw new IllegalArgumentException("Invalid version: " +
					__version, __e);
			}
		
		// An even minor version is a stable version
		if ((versions[1] % 2) == 0)
			return String.format("stable/%s", __version);
		
		// Otherwise unstable
		return String.format("unstable/%s", __version);
	}
	
	/**
	 * Loads a Zip into the given map.
	 *
	 * @param __into The map to load into.
	 * @param __from The Zip to load from.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/03/29
	 */
	public static void loadZip(Map<String, byte[]> __into, InputStream __from)
		throws IOException, NullPointerException
	{
		if (__into == null || __from == null)
			throw new NullPointerException("NARG");
		
		try (ZipInputStream zip = new ZipInputStream(__from))
		{
			// Keep reading entries
			for (;;)
			{
				ZipEntry entry = zip.getNextEntry();
				if (entry == null)
					continue;
				
				// Ignore directories
				if (entry.isDirectory())
					continue;
				
				// Load into the map
				__into.put(entry.getName(),
					StreamUtils.readAll(1048576, zip));
			}
		}
	}
	
	/**
	 * Loads a Zip into the given map.
	 *
	 * @param __into The map to load into.
	 * @param __from The Zip to load from.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/03/29
	 */
	public static void loadZip(Map<String, byte[]> __into, byte[] __from)
		throws IOException, NullPointerException
	{
		if (__into == null || __from == null)
			throw new NullPointerException("NARG");
		
		try (InputStream in = new ByteArrayInputStream(__from))
		{
			Main.loadZip(__into, in); 
		}
	}
	
	/**
	 * Main entry point.
	 *
	 * @param __args Program arguments.
	 * @throws IOException On read/write errors.
	 * @since 2024/10/04
	 */
	public static void main(String... __args)
		throws IOException
	{
		// Needs to have something
		if (__args == null || __args.length < 1)
			throw new IllegalArgumentException(
				"Usage: [version] [task=output...]");
		
		// Get the SquirrelJME version
		String version = __args[0];
		String baseDir = Main.baseDir(version);
		
		// Load in Git/Fossil commit and the current date
		String dateCommit = new Date().toString();
		String fossilCommit = null;
		try
		{
			fossilCommit = Files.readAllLines(
				Paths.get("manifest.uuid"),
				StandardCharsets.UTF_8).get(0).trim();
		}
		catch (Throwable __ignored)
		{
		}
		String gitCommit = System.getenv("CIRCLE_SHA1");
		byte[] mark = String.format(
			"date:%s\r\nfossil:%s\r\ngit:%s\r\n",
			dateCommit, fossilCommit,
			gitCommit).getBytes(StandardCharsets.UTF_8);
		
		// Upload files into the un-versioned space
		// romNanoCoatRelease=/home/.../squirreljme.jar
		if (Main.FOSSIL != null)
		{
			// .tgz and .zip files, for distros generally
			Path tempFile = Files.createTempFile("archive", ".tmp");
			for (String fileType : Arrays.asList("tar", "zip"))
				try
				{
					// Delete the file to overwrite it
					Files.deleteIfExists(tempFile);
					
					// Obtain source archive
					Main.FOSSIL.exec(fileType,
						Objects.toString(fossilCommit, "trunk"),
						tempFile.toAbsolutePath().toString(),
						"--name",
						String.format("squirreljme-%s-src", version));
					
					// Determine filename
					String name = String.format("squirreljme-%s-src.%s",
						version, (fileType.equals("tar") ? "tgz" : fileType));
					String target = Main.uvTarget(baseDir, version, name);
					
					// Upload source
					Main.FOSSIL.add(tempFile, target);
					Main.FOSSIL.add(mark, target + ".mkd");
				}
				finally
				{
					Files.deleteIfExists(tempFile);
				}
			
			// Artifacts from the build
			for (String arg : Arrays.asList(__args).subList(1, __args.length))
			{
				int eq = arg.indexOf('=');
				if (eq < 0)
					continue;
				
				// Split task name and the target file
				String name = arg.substring(0, eq);
				Path path = Paths.get(arg.substring(eq + 1));
				
				// Determine target name
				String target = Main.uvTarget(baseDir, version, name);
				
				// Store into un-versioned space
				System.err.printf("Storing `%s` as `%s`...%n",
					path, target);
				Main.FOSSIL.add(path, target);
				Main.FOSSIL.add(mark, target + ".mkd");
			}
		}
		
		// Natives used for the standalone
		Artifact standaloneBase = null;
		List<Artifact> standaloneNative = new ArrayList<>();
		
		// Read in workflow jobs
		String workflowId = System.getenv("CIRCLE_WORKFLOW_ID");
		if (workflowId != null && Main.FOSSIL != null)
		{
			CircleCiWorkflowJobs jobs = CircleCiComm.workflowJobs(workflowId);
			
			// Go through all jobs
			for (CircleCiJob job : jobs.getItems())
			{
				String jobName = job.getName();
				
				// Part of the universal standalone?
				if (jobName.endsWith("_natives"))
					standaloneNative.addAll(
						CircleCiComm.download(job.getJobNumber()));
				else if (jobName.equals("build_linux_amd64_standalone"))
					standaloneBase = CircleCiComm.download(job.getJobNumber())
						.get(0);
				
				// Standard upload?
				if (jobName.startsWith("rom") ||
					jobName.contains("_standalone"))
					Main.taskDirectUpload(job, baseDir, version, mark);
			}
		}
		
		// Build universal Jar that contains every architecture
		if (Main.FOSSIL != null && standaloneBase != null)
			Main.taskUniversal(baseDir, version, mark,
				standaloneBase, standaloneNative);
	}
	
	/**
	 * Performs a direct upload of the artifact.
	 *
	 * @param __job The job this is.
	 * @param __baseDir The base directory.
	 * @param __version The SquirrelJME version.
	 * @param __mark The marking to use.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/03/29
	 */
	public static void taskDirectUpload(CircleCiJob __job, String __baseDir,
		String __version, byte[] __mark)
		throws IOException, NullPointerException
	{
		if (__job == null || __baseDir == null || __version == null ||
			__mark == null)
			throw new NullPointerException("NARG");
		
		// Is this a job we care about?
		String target;
		try
		{
			target = Main.uvTarget(__baseDir, __version, __job.getName());
			if (target == null)
				return;
		}
		catch (IllegalArgumentException __ignored)
		{
			return;
		}
		
		// Get artifacts for this job
		for (Artifact artifact : CircleCiComm.download(__job.getJobNumber()))
		{
			// Get the URL to the artifact
			System.err.printf("Uploading `%s` as `%s`...%n",
				artifact.getPath(), target);
			
			// Store into un-versioned space
			Main.FOSSIL.add(artifact.getData(), target);
			Main.FOSSIL.add(__mark, target + ".mkd");
			
			// Only care about the first
			break;
		}
	}
	
	/**
	 * Combines a universal SquirrelJME standalone Jar. 
	 *
	 * @param __baseDir The base directory.
	 * @param __version The SquirrelJME version.
	 * @param __mark The marking to use.
	 * @param __standaloneBase The base standalone.
	 * @param __standaloneNative The natives to merge.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/03/29
	 */
	public static void taskUniversal(String __baseDir, String __version,
		byte[] __mark, Artifact __standaloneBase,
		List<Artifact> __standaloneNative)
		throws IOException, NullPointerException
	{
		if (__baseDir == null || __version == null || __mark == null ||
			__standaloneBase == null || __standaloneNative == null)
			throw new NullPointerException("NARG");
		
		// Debug
		System.err.printf("Combining universal from %s %s...%n",
			__standaloneBase, __standaloneNative);
		
		// Load in and merge all ZIPs
		Map<String, byte[]> merged = new TreeMap<>();
		Main.loadZip(merged, __standaloneBase.getData());
		for (Artifact artifact : __standaloneNative)
			Main.loadZip(merged, artifact.getData());
		
		// Build resultant Zip
		byte[] result;
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ZipOutputStream zip = new ZipOutputStream(baos,
				StandardCharsets.UTF_8))
		{
			// Use compression!
			zip.setLevel(9);
			
			// Write each entry
			for (Map.Entry<String, byte[]> item : merged.entrySet())
			{
				zip.putNextEntry(new ZipEntry(item.getKey()));
				
				zip.write(item.getValue());
				
				zip.closeEntry();
			}
			
			// Close the Zip
			zip.finish();
			zip.flush();
			
			// Grab the resultant final Zip
			result = baos.toByteArray();
		}
		
		// Store Zip into the fossil repository
		String target = Main.uvTarget(__baseDir, __version,
			"squirreljme-standalone-%s.jar");
		Main.FOSSIL.add(result, target);
		Main.FOSSIL.add(__mark, target + ".mkd");
	}
	
	/**
	 * Determines the un-versioned space target.
	 *
	 * @param __baseDir The base directory.
	 * @param __version The version of SquirrelJME.
	 * @param __name The task name.
	 * @return The resultant target.
	 * @throws IllegalArgumentException If the target is unknown.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/10/05
	 */
	public static String uvTarget(String __baseDir,
		String __version, String __name)
		throws IllegalArgumentException, NullPointerException
	{
		if (__baseDir == null || __version == null || __name == null)
			throw new NullPointerException("NARG");
		
		// Determine actual name
		String name;
		switch (__name)
		{
			case "romNanoCoatRelease":
				name = "squirreljme-%s-fast.jar";
				break;
				
			case "romNanoCoatDebug":
				name = "squirreljme-%s-slow.jar";
				break;
				
			case "romTestNanoCoatDebug":
				name = "squirreljme-%s-slow-test.jar";
				break;
				
			case "build_windows_i386_standalone":
				name = "squirreljme-standalone-%s-windows-i386.jar";
				break;
				
			case "build_windows_aarch64_standalone":
				name = "squirreljme-standalone-%s-windows-aarch64.jar";
				break;
				
			case "build_windows_amd64_standalone":
				name = "squirreljme-standalone-%s-windows-amd64.jar";
				break;
				
			case "build_macosx_aarch64_standalone":
				name = "squirreljme-standalone-%s-macos-aarch64.jar";
				break;
				
			case "build_macosx_amd64_standalone":
				name = "squirreljme-standalone-%s-macos-amd64.jar";
				break;
				
			case "build_linux_aarch64_standalone":
				name = "squirreljme-standalone-%s-linux-aarch64.jar";
				break;
				
			case "build_linux_amd64_standalone":
				name = "squirreljme-standalone-%s-linux-amd64.jar";
				break;
				
			case "build_linux_aarch64_standalone_flatpak":
				name = "squirreljme-%s-aarch64.flatpak";
				break;
				
			case "build_linux_amd64_standalone_flatpak":
				name = "squirreljme-%s-amd64.flatpak";
				break;
				
				// Allowed same
			case "squirreljme-standalone-%s.jar":
				name = __name;
				break;
			
			default:
				name = null;
				break;
		}
		
		// Construct
		return String.format("%s/%s", __baseDir,
			(name != null ? String.format(name, __version) : __name));
	}
}
