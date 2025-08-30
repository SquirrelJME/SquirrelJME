// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.jar.Manifest;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import org.gradle.api.Action;
import org.gradle.api.Task;

/**
 * Copies over the Jar and generates a source file for NanoCoat.
 *
 * @since 2023/05/31
 */
public class NanoCoatBuiltInTaskAction
	implements Action<Task>
{
	/** The input Jar. */
	protected final NanoCoatBuiltInTaskInput input;
	
	/** The output Jar. */
	protected final NanoCoatBuiltInTaskOutput outJar;
	
	/** The output source. */
	protected final NanoCoatBuiltInTaskOutput outSrc;
	
	/** Output tests. */
	protected final NanoCoatBuiltInTaskOutput outTest;
	
	/**
	 * Initializes the task action.
	 *
	 * @param __input The input Jar.
	 * @param __outJar The output Jar.
	 * @param __outSrc The output source.
	 * @param __outTest Optional output test list.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/10/12
	 */
	public NanoCoatBuiltInTaskAction(NanoCoatBuiltInTaskInput __input,
		NanoCoatBuiltInTaskOutput __outJar, NanoCoatBuiltInTaskOutput __outSrc,
		NanoCoatBuiltInTaskOutput __outTest)
		throws NullPointerException
	{
		if (__input == null || __outJar == null || __outSrc == null)
			throw new NullPointerException("NARG");
		
		this.input = __input;
		this.outJar = __outJar;
		this.outSrc = __outSrc;
		this.outTest = __outTest;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/31
	 */
	@Override
	public void execute(Task __task)
	{
		NanoCoatBuiltInTask task = (NanoCoatBuiltInTask)__task;
		
		// This could fail to write
		Path sourceTemp = null;
		try
		{
			// Copy the Jar over
			Files.copy(this.input.call(), this.outJar.call(),
				StandardCopyOption.REPLACE_EXISTING);
			
			// We will be writing to a file then moving it over, so we only
			// need a single temporary file for everything
			sourceTemp = Files.createTempFile("source", ".x");
			
			// Output tests list?
			NanoCoatBuiltInTaskOutput outTest = this.outTest;
			if (outTest != null)
			{
				// Load in manifest and test lists
				Map<String, Manifest> manifests = new TreeMap<>();
				Map<String, byte[]> tests = new TreeMap<>();
				try (ZipFile zip = new ZipFile(this.input.call().toFile()))
				{
					// Find all tests and manifests
					for (ZipEntry entry : Collections.list(zip.entries()))
					{
						// Determine Jar name
						String name = entry.getName();
						String[] names = name.split(Pattern.quote("/"));
						if (names.length < 2)
							continue;
						String jarName = names[1];
						
						// Tests?
						if (name.endsWith("/META-INF/services/" +
							"net.multiphasicapps.tac.TestInterface"))
							try (InputStream in = zip.getInputStream(entry))
							{
								tests.put(jarName, VMHelpers.readAll(in));
							}
						
						// Manifest?
						else if (name.endsWith("/META-INF/MANIFEST.MF"))
							try (InputStream in = zip.getInputStream(entry))
							{
								manifests.put(jarName, new Manifest(in));
							}
					}
				}
				
				// Process
				try (OutputStream outRaw = Files.newOutputStream(sourceTemp,
					StandardOpenOption.WRITE,
					StandardOpenOption.TRUNCATE_EXISTING,
					StandardOpenOption.CREATE); 
					PrintStream out = new PrintStream(outRaw,
						true, "utf-8"))
				{
					this.__tests(out, manifests, tests);
				}
				
				// Move it over
				Files.move(sourceTemp, this.outTest.call(),
					StandardCopyOption.REPLACE_EXISTING);
			}
		}
		
		// It did fail to write
		catch (Exception e)
		{
			throw new RuntimeException("Could not copy ROM.", e);
		}
		
		// Cleanup after this
		finally
		{
			if (sourceTemp != null)
				try
				{
					Files.delete(sourceTemp);
				}
				catch (IOException ignored)
				{
				}
		}
	}
	
	/**
	 * Processes Jar tests.
	 *
	 * @param __out The output list.
	 * @param __manifests The manifests for input
	 * @param __tests The tests for input.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/10/16
	 */
	private void __tests(PrintStream __out, Map<String, Manifest> __manifests,
		Map<String, byte[]> __tests)
		throws IOException, NullPointerException
	{
		if (__out == null || __manifests == null || __tests == null)
			throw new NullPointerException("NARG");
		
		// Go through all Jars with tests
		for (String jarName : __tests.keySet())
		{
			// Get respective data for each
			Manifest manifest = __manifests.get(jarName);
			byte[] rawTests = __tests.get(jarName);
			if (manifest == null || rawTests == null)
				continue;
			
			// Get full classpath
			String fullPath = manifest.getMainAttributes()
				.getValue("X-SquirrelJME-Tests-ClassPath");
			
			// Process all test lines
			Set<String> testNames = new TreeSet<>();  
			try (BufferedReader br = new BufferedReader(
				new InputStreamReader(new ByteArrayInputStream(rawTests),
				"utf-8")))
			{
				for (;;)
				{
					// EOF?
					String ln = br.readLine();
					if (ln == null)
						break;
					
					// Skip blank lines
					if (ln.trim().isEmpty())
						continue;
					
					// Register test
					testNames.add(ln);
				}
			}
			
			// Write all tests
			for (String testName : testNames)
			{
				__out.print(jarName);
				__out.print('|');
				__out.print(fullPath);
				__out.print('|');
				__out.print(testName);
				__out.print('\n');
			}
			
			// Flush anything written
			__out.flush();
		}
	}
}
