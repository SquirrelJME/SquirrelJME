// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.gradle.internal.os.OperatingSystem;

/**
 * This class provides support for the Fossil executable, to do some tasks
 * and otherwise from within Gradle.
 *
 * @since 2020/06/24
 */
public final class FossilExe
{
	/** Cached executable. */
	@SuppressWarnings({"StaticVariableMayNotBeInitialized", "unused", 
		"FieldNamingConvention"})
	private static FossilExe _cached;
	
	/** The executable path. */
	private final Path exe;
	
	/**
	 * Initializes the executable reference with the given path.
	 * 
	 * @param __exe The executable path.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/24
	 */
	public FossilExe(Path __exe)
		throws NullPointerException
	{
		if (__exe == null)
			throw new NullPointerException("NARG");
		
		this.exe = __exe;
	}
	
	/**
	 * Returns the current fossil user.
	 * 
	 * @return The current fossil user.
	 * @throws InvalidFossilExeException If the user is not valid.
	 * @since 2020/06/27
	 */
	public final String currentUser()
		throws InvalidFossilExeException
	{
		// Use first line found for the user
		for (String line : this.runLineOutput("user", "default"))
			if (!line.isEmpty())
				return line.trim();
		
		// Fail
		throw new InvalidFossilExeException("No default user set in Fossil, " +
			"please set `fossil user default user.name`");
	}
	
	/**
	 * Returns the executable path.
	 * 
	 * @return The executable path.
	 * @since 2020/06/25
	 */
	public final Path exePath()
	{
		return this.exe;
	}
	
	/**
	 * Runs the specified command and returns the process for it.
	 * 
	 * @param __args Arguments to the command.
	 * @return The process of the command.
	 * @since 2020/06/24
	 */
	@SuppressWarnings("UseOfProcessBuilder")
	public final Process runCommand(String... __args)
	{
		ProcessBuilder builder = new ProcessBuilder();
		
		// The first argument is always the command
		List<String> command = new ArrayList<>();
		command.add(this.exe.toAbsolutePath().toString());
		
		// Add all subsequent arguments
		if (__args != null)
			command.addAll(Arrays.<String>asList(__args));
		
		// Use this command
		builder.command(command);
		
		// Force specific locales and otherwise
		Map<String, String> env = builder.environment();
		env.put("LC_ALL", "C");
		
		// Start the command but wrap IOException as it is annoying
		try
		{
			return builder.start();
		}
		catch (IOException e)
		{
			throw new RuntimeException("Could not execute command.", e);
		}
	}
	
	/**
	 * Runs an HTTP command on the Fossil executable, this uses the
	 * {@code http} command which accepts standard input and output.
	 * 
	 * @param __uri The URI to run.
	 * @return The result of the call.
	 * @since 2020/06/24
	 */
	public final SimpleHTTPCall runHttp(String __uri)
	{
		return this.runHttp(URI.create(__uri));
	}
	
	/**
	 * Runs an HTTP command on the Fossil executable, this uses the
	 * {@code http} command which accepts standard input and output.
	 * 
	 * @param __uri The URI to run.
	 * @return The result of the call.
	 * @since 2020/06/24
	 */
	public final SimpleHTTPCall runHttp(URI __uri)
	{
		// Start the Fossil process
		Process process = this.runCommand("http");
		
		// Send in the HTTP stream
		try (OutputStream out = process.getOutputStream())
		{
			// Send the request
			SimpleHTTPCall.request(out, __uri);
			
			// Flush to ensure that our request was sent
			out.flush();
			
			// Read the result of the process
			try (InputStream in = process.getInputStream())
			{
				return SimpleHTTPCall.parse(in);
			}
		}
		
		// Could not read or write the command
		catch (IOException e)
		{
			throw new RuntimeException("HTTP read/write error.", e);
		}
		
		// Make sure the process stops regardless of what happens before
		finally
		{
			process.destroy();
		}
	}
	
	/**
	 * Executes the command and returns the lines used.
	 * 
	 * @param __args The arguments to call.
	 * @return The lines used in the command.
	 * @since 2020/06/25
	 */
	public final Collection<String> runLineOutput(String... __args)
	{
		// Get the raw command bytes
		Collection<String> rv = new ArrayList<>();
		try (BufferedReader in = new BufferedReader(new InputStreamReader(
			new ByteArrayInputStream(this.runRawOutput(__args)),
			StandardCharsets.UTF_8)))
		{
			for (;;)
			{
				String ln = in.readLine();
				
				if (ln == null)
					break;
				
				rv.add(ln);
			}
		}
		
		// Could no read the command result
		catch (IOException e)
		{
			throw new RuntimeException("Line read/write error.", e);
		}
		
		return rv;
	}
	
	/**
	 * Runs the command and returns the raw output.
	 * 
	 * @param __args The commands to run.
	 * @return The raw output of the command.
	 * @since 2020/06/25
	 */
	public final byte[] runRawOutput(String... __args)
	{
		// Start the Fossil process
		Process process = this.runCommand(__args);
		
		// Read in the command data
		try (InputStream in = process.getInputStream();
			ByteArrayOutputStream out = new ByteArrayOutputStream())
		{
			// Copy data
			byte[] buf = new byte[4096];
			for (;;)
			{
				int rc = in.read(buf);
				
				if (rc < 0)
					break;
				
				out.write(buf, 0, rc);
			}
			
			return out.toByteArray();
		}
		
		// Could no read the command result
		catch (IOException e)
		{
			throw new RuntimeException("Raw read/write error.", e);
		}
		
		// Make sure the process stops
		finally
		{
			process.destroy();
		}
	}
	
	/**
	 * Gets the content of the specified file.
	 * 
	 * @param __fileName The file name.
	 * @return The stream for the file data or {@code null} if no such file
	 * exists or it has no content.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/25
	 */
	public final InputStream unversionCat(String __fileName)
		throws NullPointerException
	{
		if (__fileName == null)
			throw new NullPointerException("NARG");
		
		byte[] data = this.unversionCatBytes(__fileName);
		if (data == null)
			return null;
		
		return new ByteArrayInputStream(data);
	}
	
	/**
	 * Gets the content of the specified file.
	 * 
	 * @param __fileName The file name.
	 * @return The stream for the file data or {@code null} if no such file
	 * exists or it has no content.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/25
	 */
	public final byte[] unversionCatBytes(String __fileName)
		throws NullPointerException
	{
		// If no data is available, return nothing
		byte[] data = this.runRawOutput("unversion", "cat", __fileName);
		if (data.length == 0)
			return null;
		
		return data;
	}
	
	/**
	 * Returns the list of unversioned files.
	 * 
	 * @return The list of unversioned files.
	 * @since 2020/06/25
	 */
	public final Collection<String> unversionedLs()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Returns the fossil version.
	 * 
	 * @return The fossil version.
	 * @since 2020/06/25
	 */
	public final FossilVersion version()
	{
		Collection<String> lines = this.runLineOutput("version");
		
		// Try to find the fossil version
		for (String line : lines)
		{
			// Ignore lines not containing this text
			if (!line.toLowerCase().contains("this is fossil version"))
				continue;
			
			// Try to find the version number
			for (String split : line.split("[ \t]"))
			{
				// Ignore blank sequences
				if (split.isEmpty())
					continue;
				
				// Use the split that starts with a number
				char first = split.charAt(0);
				if (first >= '1' && first <= '9')
					try
					{
						return new FossilVersion(split);
					}
					catch (IllegalArgumentException e)
					{
						throw new InvalidFossilExeException("Invalid Exe", e);
					}
			}
		}
		
		// This is not a good thing
		throw new InvalidFossilExeException("Could not find fossil version.");
	}
	
	/**
	 * Attempts to locate the fossil executable.
	 * 
	 * @return The executable instance.
	 * @throws InvalidFossilExeException If an executable could not be found.
	 * @since 2020/06/24
	 */
	@SuppressWarnings({"CallToSystemGetenv", 
		"StaticVariableUsedBeforeInitialization"})
	public static FossilExe instance()
		throws InvalidFossilExeException
	{
		// Pre-cached already?
		FossilExe rv = FossilExe._cached;
		if (rv != null)
			return rv;
		
		// Path should exist, but it might not
		String pathEnv = System.getenv("PATH");
		if (pathEnv == null)
			throw new InvalidFossilExeException("No PATH variable is set.");
		
		// The executable we are looking for
		Path exeName = Paths.get(
			(OperatingSystem.current() == OperatingSystem.WINDOWS ?
			"fossil.exe" : "fossil"));
		
		// Search each path piece for the given executable
		for (String pathSegment : pathEnv.split(
			Pattern.quote(System.getProperty("path.separator"))))
		{
			Path fullPath = Paths.get(pathSegment).resolve(exeName);
			
			// If we find it, cache it
			if (Files.isRegularFile(fullPath) && Files.isExecutable(fullPath))
			{
				rv = new FossilExe(fullPath);
				
				FossilExe._cached = rv;
				
				return rv;
			}
		}
		
		// Not found
		throw new InvalidFossilExeException(
			"Could not find Fossil executable.");
	}
	
	/**
	 * Check to see if Fossil is available.
	 * 
	 * @param __withUser Also check that the user is set?
	 * @return If Fossil is available.
	 * @since 2020/06/25
	 */
	public static boolean isAvailable(boolean __withUser)
	{
		try
		{
			FossilExe exe = FossilExe.instance();
			
			// These will throw exceptions if not valid
			exe.version();
			
			// Check user as well?
			if (__withUser)
				exe.currentUser();
			
			return true;
		}
		
		// Not available
		catch (InvalidFossilExeException ignored)
		{
			return false;
		}
	}
}
