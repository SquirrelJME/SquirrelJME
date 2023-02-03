// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import cc.squirreljme.plugin.multivm.ident.SourceTargetClassifier;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Properties;
import org.gradle.api.Action;
import org.gradle.api.Task;
import proguard.ClassPath;
import proguard.ClassPathEntry;
import proguard.Configuration;
import proguard.ConfigurationParser;
import proguard.ProGuard;

/**
 * Performs the actual compaction of the Jar.
 *
 * @since 2023/02/01
 */
public class VMCompactLibraryTaskAction
	implements Action<Task>
{
	/** Settings to use in the configuration for keeping, etc. */
	private static final String[] _PARSE_SETTINGS = new String[]
		{
			/*"-keep", "public", "class", "*", "{",
			    "public", "protected", "*", ";",
				"}",*/
			
			// Keep anything with main in it
			"-keepclasseswithmembers", "class", "*", "{",
				"public", "static", "void", "main", "(",
					"java.lang.String[]", ")", ";",
			"}",
			
			// Keep classes annotation with @Api and @Exported
			"-keep", "public",
				"@cc.squirreljme.runtime.cldc.annotation.Api",
				"class", "*", "{",
				"public", "protected", "*", ";",
				"}",
			"-keep", "public",
				"@cc.squirreljme.runtime.cldc.annotation.Exported",
				"class", "*", "{",
				"public", "protected", "*", ";",
				"}",
			
			// Keep the names of these classes as well
			"-keepnames", "public",
				"@cc.squirreljme.runtime.cldc.annotation.Api",
				"class", "*",
			"-keepnames", "public",
				"@cc.squirreljme.runtime.cldc.annotation.Exported",
				"class", "*",
			
			// Keep members with these two annotations
			"-keepclassmembers", "public", "class", "*", "{",
				"@cc.squirreljme.runtime.cldc.annotation.Api",
				"public", "protected", "*", ";",
				"}",
			"-keepclassmembers", "public", "class", "*", "{",
				"@cc.squirreljme.runtime.cldc.annotation.Exported",
				"public", "protected", "*", ";",
				"}",
			
			// Keep names as well
			"-keepclassmembernames", "public", "class", "*", "{",
				"@cc.squirreljme.runtime.cldc.annotation.Api",
				"public", "protected", "*", ";",
				"}",
			"-keepclassmembernames", "public", "class", "*", "{",
				"@cc.squirreljme.runtime.cldc.annotation.Exported",
				"public", "protected", "*", ";",
				"}",
		};
	
	/** The classifier used. */
	public final SourceTargetClassifier classifier;
	
	/**
	 * Initializes the task action.
	 * 
	 * @param __classifier The classifier used.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/02/01
	 */
	public VMCompactLibraryTaskAction(SourceTargetClassifier __classifier)
		throws NullPointerException
	{
		if (__classifier == null)
			throw new NullPointerException("NARG");
		
		this.classifier = __classifier;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/02/01
	 */
	@Override
	public void execute(Task __task)
	{
		// Where are we writing to?
		Path outputPath = __task.getOutputs().getFiles().getSingleFile()
			.toPath();
		
		// Run the task
		Path tempFile = null;
		try
		{
			// Setup temporary file to output to when finished
			tempFile = Files.createTempFile("tiny", ".jar");
			
			// Need to delete the created temporary file, otherwise Proguard
			// will just say "The output appears up to date" and do nothing
			Files.delete(tempFile);
			
			// Parse initial configuration with settings
			Configuration config = new Configuration();
			try (ConfigurationParser parser = new ConfigurationParser(
				VMCompactLibraryTaskAction._PARSE_SETTINGS, new Properties()))
			{
				parser.parse(config);
			}
			
			// Setup shrink/obfuscation rules
			config.android = false;
			config.flattenPackageHierarchy = "_sjme";
			config.microEdition = false;
			config.optimize = true;
			config.shrink = true;
			
			// Be noisy
			config.verbose = true;
			//config.dump = Configuration.STD_OUT;
			//config.printUsage = Configuration.STD_OUT;
			//config.printMapping = Configuration.STD_OUT;
			config.printConfiguration = Configuration.STD_OUT;
			
			// We need to include all the inputs that were already ran through
			// ProGuard...
			ClassPath libraryJars = new ClassPath();
			config.libraryJars = libraryJars;
			for (Path jar : VMHelpers.runClassPath(__task, this.classifier))
			{
				// Ignore our own output as it will never actually work, or
				// will go badly as it would be stale
				if (jar.equals(outputPath) ||
					Files.isSameFile(jar, outputPath))
					continue;
				
				// Add otherwise
				libraryJars.add(new ClassPathEntry(jar.toFile(),
					false));
			}
			
			// Setup input and output Jar
			ClassPath programJars = new ClassPath();
			config.programJars = programJars;
			
			// Input source Jar
			programJars.add(
				new ClassPathEntry(__task.getInputs().getFiles()
					.getSingleFile(), false));
			
			// Output temporary Jar
			programJars.add(new ClassPathEntry(
				tempFile.toFile(), true));
			
			// Run the shrinking/obfuscation
			new ProGuard(config).execute();
			
			// Insurance
			if (Files.size(tempFile) <= 12)
				throw new RuntimeException("Nothing happened?");
			
			// Move to output
			Files.move(tempFile,
				outputPath,
				StandardCopyOption.REPLACE_EXISTING);
		}
		catch (Exception __e)
		{
			throw new RuntimeException("Failed to shrink/obfuscate.", __e);
		}
		
		// Cleanup anything left over
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
}
