// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.cicd;

import cc.squirreljme.cicd.circleci.CircleCiArtifact;
import cc.squirreljme.cicd.circleci.CircleCiJob;
import cc.squirreljme.cicd.circleci.CircleCiJobArtifacts;
import cc.squirreljme.cicd.circleci.CircleCiWorkflowJobs;
import cc.squirreljme.runtime.cldc.util.StreamUtils;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Main entry point.
 *
 * @since 2024/10/04
 */
public class Main
{
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
		return "unstable";
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
			"date: %s\nfossil:%s\ngit: %s\n",
			dateCommit, fossilCommit,
			gitCommit).getBytes(StandardCharsets.UTF_8);
		
		// Upload files into the un-versioned space
		// romNanoCoatRelease=/home/.../squirreljme.jar
		FossilCommand fossil = FossilCommand.instance();
		if (fossil != null)
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
				fossil.add(path, target);
				fossil.add(mark, target + ".mkd");
			}
		
		// Read in workflow jobs
		String workflowId = System.getenv("CIRCLE_WORKFLOW_ID");
		if (workflowId != null)
		{
			CircleCiWorkflowJobs jobs = CircleCiComm.workflowJobs(workflowId);
			
			// Go through all jobs
			for (CircleCiJob job : jobs.getItems())
			{
				// Is this a job we care about?
				String target;
				try
				{
					target = Main.uvTarget(baseDir, version, job.getName());
					if (target == null)
						continue;
				}
				catch (IllegalArgumentException __ignored)
				{
					continue;
				}
				
				// Get artifacts for this job
				CircleCiJobArtifacts artifacts =
					CircleCiComm.jobArtifacts(job.getJobNumber());
				for (CircleCiArtifact artifact : artifacts.getItems())
				{
					// Get the URL to the artifact
					System.err.printf("Downloading `%s` as `%s`...%n",
						artifact.getUrl(), target);
					try (InputStream in = URI.create(artifact.getUrl())
						.toURL().openStream())
					{
						// Store into un-versioned space
						fossil.add(StreamUtils.readAll(1048576, in),
							target);
						fossil.add(mark, target + ".mkd");
					}
					
					// Only care about the first
					break;
				}
			}
		}
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
			
			default:
				throw new IllegalArgumentException(
					"Unknown target: " + __name);
		}
		
		// Construct
		return String.format("%s/%s", __baseDir,
			String.format(name, __version));
	}
}
