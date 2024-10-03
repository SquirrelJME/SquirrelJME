// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.gh.classic;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.util.StreamUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Main entry point.
 *
 * @since 2024/10/03
 */
public class Main
{
	/**
	 * Runs the GitHub API command. 
	 *
	 * @param __uri The URI to call.
	 * @return The data from the call.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/10/03
	 */
	public static byte[] gh(String __uri)
		throws IOException, NullPointerException
	{
		if (__uri == null)
			throw new NullPointerException("NARG");
		
		ProcessBuilder builder = new ProcessBuilder(
			"gh", "api", __uri);
		
		// Redirect
		builder.redirectOutput(ProcessBuilder.Redirect.PIPE);
		builder.redirectError(ProcessBuilder.Redirect.INHERIT);
		
		// Run command
		Process proc = builder.start();
		try
		{
			proc.waitFor();
		}
		catch (InterruptedException __e)
		{
			throw new IOException("Interrupted", __e);
		}
		
		// Read in everything
		return StreamUtils.readAll(1048576, proc.getInputStream());
	}
	
	/**
	 * Main arguments.
	 *
	 * @param __args Arguments to the program.
	 * @throws IOException On read/write errors.
	 * @since 2024/10/03
	 */
	public static void main(String... __args)
		throws IOException
	{
		// Needs only one!
		if (__args == null || __args.length != 1)
		{
			System.err.printf("Usage: UserOrOrg/Repo");
			System.exit(1);
			return;
		}
		
		// /repos/SquirrelJME/SquirrelJME/projects
		// columns_url https://api.github.com/projects/12033917/columns
		// cards_url https://api.github.com/projects/columns/13510680/cards
		
		// Start from base
		GitHubProject[] projects = Main.storeLoad(GitHubProject[].class,
			Paths.get("projects.json"),
			String.format("/repos/%s/projects", __args[0]));
		for (GitHubProject project : projects)
		{
			// Get column Url
			String columnUrl = project.columnsUrl;
			if (columnUrl == null)
				continue;
			
			// Map that as well
			GitHubColumn[] columns = Main.storeLoad(GitHubColumn[].class,
				Paths.get(String.format("project/%d.json", project.id)),
				columnUrl);
			for (GitHubColumn column : columns)
			{
				// Get cards Url
				String cardsUrl = column.cardsUrl;
				if (cardsUrl == null)
					continue;
				
				// Map all of those
				GitHubCard[] cards = Main.storeLoad(GitHubCard[].class,
					Paths.get(String.format("columns/%d.json", column.id)),
					cardsUrl);
			}
		}
	}
	
	/**
	 * Loads and stores the API data.
	 *
	 * @param <T> The class to map as.
	 * @param __cl The class to map as.
	 * @param __where Where to store on the disk.
	 * @param __uri The URI to read.
	 * @return The mapped class.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/10/03
	 */
	public static <T> T storeLoad(Class<T> __cl, Path __where, String __uri)
		throws IOException, NullPointerException
	{
		if (__uri == null)
			throw new NullPointerException("NARG");
		
		// Load in raw JSON first
		byte[] rawJson = Main.gh(__uri);
		
		// Write to disk
		if (__where.getParent() != null)
			Files.createDirectories(__where.getParent());
		Files.write(__where, rawJson);
		
		// Debug
		System.err.printf(">> %s -> %s%n",
			__uri, __where);
		
		// Map object
		return new ObjectMapper().readValue(rawJson, __cl);
	}
}
