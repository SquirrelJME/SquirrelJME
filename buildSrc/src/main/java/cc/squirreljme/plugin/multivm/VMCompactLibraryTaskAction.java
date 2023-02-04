// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import cc.squirreljme.plugin.SquirrelJMEPluginConfiguration;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
	static final String[] _PARSE_SETTINGS = new String[]
		{
			/*"-keep", "public", "class", "*", "{",
			    "public", "protected", "*", ";",
				"}",*/
			
			// Adjust manifest resources
			"-adaptresourcefilecontents",
				"META-INF/MANIFEST.MF,META-INF/services/**",
			
			// Consumers of the libraries/APIs need to see the annotation
			// information if it is there, to make sure it is retained
			"-keepattributes", "*Annotation*",
			
			// Keep anything with main in it
			"-keepclasseswithmembers", "class", "*", "{",
				"public", "static", "void", "main", "(",
					"java.lang.String[]", ")", ";",
			"}",
			
			// Keep any MIDlet
			"-keep", "class", "*", "extends",
				"javax.microedition.midlet.MIDlet",
			
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
			
			// Keep implementors of the annotations
			/*"-keep", "class", "*", "implements",
				"@cc.squirreljme.runtime.cldc.annotation.Api", "*",
			"-keep", "public", "class", "*", "implements",
			"@cc.squirreljme.runtime.cldc.annotation.Api", "*",*/
			
			/*"-keep", "class", "*", "implements",
				"@cc.squirreljme.runtime.cldc.annotation.Exported", "*",
			"-keep", "public", "class", "*", "implements",
				"@cc.squirreljme.runtime.cldc.annotation.Exported", "*",*/
		};
	
	/** The source set used. */
	public final String sourceSet;
	
	/**
	 * Initializes the task action.
	 * 
	 * @param __sourceSet The source set used.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/02/01
	 */
	public VMCompactLibraryTaskAction(String __sourceSet)
		throws NullPointerException
	{
		if (__sourceSet == null)
			throw new NullPointerException("NARG");
		
		this.sourceSet = __sourceSet;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/02/01
	 */
	@Override
	public void execute(Task __task)
	{
		// Where are we writing to?
		Path inputPath = __task.getInputs().getFiles().getSingleFile()
			.toPath();
		Path outputPath = __task.getOutputs().getFiles().getSingleFile()
			.toPath();
		
		Path outMap = outputPath.resolveSibling("mapping.txt");
		
		// Some settings may be configured
		SquirrelJMEPluginConfiguration projectConfig =
			SquirrelJMEPluginConfiguration.configuration(__task.getProject());
		
		// Run the task
		Path tempFile = null;
		try
		{
			// If we are not shrinking, since we cannot check the config at
			// initialization stage... just do a copy operation here
			if (projectConfig.noShrinking)
			{
				Files.copy(inputPath, outputPath,
					StandardCopyOption.REPLACE_EXISTING);
				
				return;
			}
			
			// Setup temporary file to output to when finished
			tempFile = Files.createTempFile("tiny", ".jar");
			
			// Need to delete the created temporary file, otherwise Proguard
			// will just say "The output appears up to date" and do nothing
			Files.delete(tempFile);
			
			// Base options to use
			List<String> proGuardOptions = new ArrayList<>();
			proGuardOptions.addAll(
				Arrays.asList(VMCompactLibraryTaskAction._PARSE_SETTINGS));
			
			// Add any additional options as needed
			if (projectConfig.proGuardOptions != null &&
				!projectConfig.proGuardOptions.isEmpty())
				proGuardOptions.addAll(projectConfig.proGuardOptions);
			
			// Parse initial configuration with settings
			Configuration config = new Configuration();
			try (ConfigurationParser parser = new ConfigurationParser(
				proGuardOptions.toArray(new String[proGuardOptions.size()]),
				new Properties()))
			{
				parser.parse(config);
			}
			
			// Setup shrink/obfuscation rules
			config.android = false;
			config.flattenPackageHierarchy = "_sjme";
			config.microEdition = false;
			config.optimize = true;
			config.shrink = true;
			
			// For mapping files, members do need to be unique
			config.useUniqueClassMemberNames = true;
			
			// Do not use mix case class names, so that more strings can
			// be compacted together accordingly
			config.useMixedCaseClassNames = false;
			
			// Be noisy
			config.verbose = true;
			//config.dump = Configuration.STD_OUT;
			//config.printUsage = Configuration.STD_OUT;
			config.printMapping = outMap.toFile();
			config.printConfiguration = Configuration.STD_OUT;
			
			// We need to include all the inputs that were already ran through
			// ProGuard, so we basically need to look at the dependencies and
			// map them around accordingly
			ClassPath libraryJars = new ClassPath();
			config.libraryJars = libraryJars;
			for (VMCompactLibraryTask compactDep :
				VMHelpers.compactLibTaskDepends(__task.getProject(),
					this.sourceSet))
				libraryJars.add(new ClassPathEntry(
					compactDep.outputPath().get().toFile(), false));
			
			// Setup input and output Jar
			ClassPath programJars = new ClassPath();
			config.programJars = programJars;
			
			// Input source Jar
			programJars.add(
				new ClassPathEntry(inputPath.toFile(), false));
			
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
