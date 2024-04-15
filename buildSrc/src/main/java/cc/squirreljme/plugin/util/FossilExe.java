// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
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
	@SuppressWarnings({"StaticVariableMayNotBeInitialized", "unused"})
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
	 * Cats the given file name.
	 * 
	 * @param __fileName The file name.
	 * @return The file data.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/27
	 */
	public final byte[] cat(String __fileName)
		throws NullPointerException
	{
		if (__fileName == null)
			throw new NullPointerException("NARG");
		
		return this.runRawOutput("cat", __fileName);
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
		
		// Standard output is always printed to the console, for debugging
		builder.redirectError(ProcessBuilder.Redirect.INHERIT);
		
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
			
			// Wait for it to complete
			for (;;)
				try
				{
					int exitCode = process.waitFor();
					if (0 != exitCode)
						throw new RuntimeException(String.format(
							"Exited %s with failure %d: %d bytes",
							Arrays.asList(__args), exitCode,
							out.toByteArray().length));
					break;
				}
				catch (InterruptedException ignored)
				{
				}
			
			// Use the result
			return out.toByteArray();
		}
		
		// Could not read the command result
		catch (IOException e)
		{
			throw new RuntimeException("Raw read/write error.", e);
		}
		
		// Make sure the process stops
		finally
		{
			// Make sure the process is destroyed
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
	 * Deletes the specified file.
	 * 
	 * @param __path The path to delete.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/08/29
	 */
	public void unversionDelete(String __path)
		throws NullPointerException
	{
		if (__path == null)
			throw new NullPointerException("NARG");
		
		this.runLineOutput("unversion", "rm", __path);
	}
	
	/**
	 * Returns the list of unversioned files.
	 * 
	 * @return The list of unversioned files.
	 * @since 2020/06/25
	 */
	public final Collection<String> unversionList()
	{
		return this.runLineOutput("unversion", "ls");
	}
	
	/**
	 * Stores unversion bytes.
	 * 
	 * @param __fileName The file name.
	 * @param __data The data to store.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/27
	 */
	public final void unversionStoreBytes(String __fileName, byte[] __data)
		throws NullPointerException
	{
		if (__fileName == null || __data == null)
			throw new NullPointerException("NARG");
		
		// Fossil accepts files as unversioned input
		Path tempFile = null;
		try
		{
			// Setup temporary file
			tempFile = Files.createTempFile("squirreljme-uvfile",
				".bin");
			
			// Write data to file
			Files.write(tempFile, __data, StandardOpenOption.WRITE,
				StandardOpenOption.TRUNCATE_EXISTING,
				StandardOpenOption.CREATE);
			
			// Store the file data
			this.runRawOutput("unversion", "add",
				tempFile.toAbsolutePath().toString(),
				"--as", __fileName);
		}
		
		// Could not write data
		catch (IOException e)
		{
			throw new RuntimeException(
				"Could not store file: " + __fileName, e);
		}
		
		// Clean out file, if it exists
		finally
		{
			if (tempFile != null)
				try
				{
					Files.delete(tempFile);
				}
				catch (IOException ignored)
				{
				}
		}
	}
	
	/**
	 * Stores unversion bytes.
	 * 
	 * @param __fileName The file name.
	 * @param __data The data to store.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/08/29
	 */
	public final void unversionStoreBytes(String __fileName,
		InputStream __data)
		throws IOException, NullPointerException
	{
		if (__fileName == null || __data == null)
			throw new NullPointerException("NARG");
		
		byte[] bytes;
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream())
		{
			// Copy over
			byte[] buf = new byte[16384];
			for (;;)
			{
				int rc = __data.read(buf);
				
				// EOF?
				if (rc < 0)
					break;
				
				baos.write(buf, 0, rc);
			}
			
			// Get all the bytes
			bytes = baos.toByteArray();
		}
		
		// Forward to store call
		this.unversionStoreBytes(__fileName, bytes);
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
		
		// Try to find it
		Path maybe = PathUtils.findPathExecutable("fossil");
		if (maybe == null)
			throw new InvalidFossilExeException(
				"Could not find Fossil executable.");
		
		// Cache for later
		rv = new FossilExe(maybe);
		FossilExe._cached = rv;		
		return rv;
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
