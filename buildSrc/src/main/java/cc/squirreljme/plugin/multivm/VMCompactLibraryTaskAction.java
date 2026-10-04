// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import cc.squirreljme.plugin.SquirrelJMEPluginConfiguration;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.gradle.api.Action;
import org.gradle.api.Task;
import org.gradle.api.tasks.SourceSet;
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
	/** The optimizations to use. */
	static final String[] _OPTIMIZATIONS = new String[]
		{
			// AVOID: Violates the specification
			"!class/marking/final",
			
			// AVOID: Merging appears to be broken
			// https://github.com/Guardsquare/proguard/issues/37
			"!class/merging/horizontal",
			
			// AVOID: Merging appears to be broken
			// https://github.com/Guardsquare/proguard/issues/37
			"!class/merging/vertical",
			
			// AVOID: Merging appears to be broken
			// https://github.com/Guardsquare/proguard/issues/37
			"!class/merging/wrapper",
			
			// Untouched and unchecked, seems to be fine
			"class/unboxing/enum",
			
			// Untouched and unchecked, seems to be fine
			"code/allocation/variable",
			
			// AVOID: Merging appears to be broken
			// https://github.com/Guardsquare/proguard/issues/37
			"!code/merging",
			
			// AVOID: Dead code removal based on this kind of determination
			// does not work well across native method calls as everything is
			// outside what ProGuard sees
			// Simplifies code based on control flow analysis and data flow
			// analysis. Removes dead code based on control flow analysis and
			// data flow analysis.
			// ProGuard says this should be used with
			// code/simplification/advanced, however this specific rule here
			// breaks everything as above
			"!code/removal/advanced",
			
			// Untouched and unchecked, seems to be fine
			"code/removal/exception",
			
			// Untouched and unchecked, seems to be fine
			"code/removal/simple",
			
			// Untouched and unchecked, seems to be fine
			"code/removal/variable",
			
			// KEEP: Although ProGuard says this should be used with
			// code/removal/advanced, and that this does simplification based
			// on dead code. Having this on and the other off seems to work
			// just fine.
			"code/simplification/advanced",
			
			// Untouched and unchecked, seems to be fine
			"code/simplification/arithmetic",
			
			// Untouched and unchecked, seems to be fine
			"code/simplification/branch",
			
			// Untouched and unchecked, seems to be fine
			"code/simplification/cast",
			
			// Untouched and unchecked, seems to be fine
			"code/simplification/field",
			
			// Untouched and unchecked, seems to be fine
			"code/simplification/math",
			
			// Untouched and unchecked, seems to be fine
			"code/simplification/object",
			
			// Untouched and unchecked, seems to be fine
			"code/simplification/string",
			
			// Untouched and unchecked, seems to be fine
			"code/simplification/variable",
			
			// AVOID: Violates the specification
			"!field/generalization/class",
			
			// AVOID: Violates the specification
			"!field/marking/private",
			
			// KEEP?: This appears to work, it does remove the majority of
			// debug checks.
			// This appears to remove null checks:
			// https://github.com/Guardsquare/proguard/issues/128
			"field/propagation/value",
			
			// AVOID: Violates the specification
			"!field/removal/writeonly",
			
			// AVOID: Violates the specification
			"!field/specialization/type",
			
			// AVOID: Violates the specification
			"!method/generalization/class",
			
			// KEEP: This can be used to remove some instances of debug calls
			"method/inlining/short",
			
			// AVOID: Inlining increases code size, but also forces a specific
			// object instance to be used if it thinks it is known
			"!method/inlining/tailrecursion",
			
			// KEEP?: Seems to be needed for this
			// public static final boolean enabled() {
			// boolean var10000 = false;
			// return true; }
			"method/inlining/unique",
			
			// AVOID: Violates the specification
			"!method/marking/final",
			
			// AVOID: Violates the specification
			"!method/marking/private",
			
			// AVOID: Violates the specification
			"!method/marking/static",
			
			// AVOID: Violates the specification
			"!method/marking/synchronized",
			
			// KEEP?: Seems to work fine?
			"method/propagation/parameter",
			
			// KEEP?: Seems to work fine?
			// Propagates the values of method return values from methods to
			// their invocations.
			"method/propagation/returnvalue",
			
			// AVOID: Violates the specification
			// Double.toString() -> public static String toString$6f5372eb()
			"!method/removal/parameter",
			
			// AVOID: Violates the specification
			"!method/specialization/parametertype",
			
			// AVOID: Violates the specification
			"!method/specialization/returntype",
		};
	
	/** Base configuration. */
	static final String[] _BASE_CONFIG = new String[]
		{
			// Ignore all JetBrains IntelliJ related annotations
			"-dontwarn", "org.jetbrains.annotations.**",
			"-dontwarn", "org.intellij.lang.annotations.**",
			
			// Adjust manifest resources
			"-adaptresourcefilenames", "**",
			"-adaptresourcefilecontents",
				"META-INF/MANIFEST.MF,META-INF/services/**",
			
			// Do not let ProGuard consider classes as up-to-date itself,
			// the build system handles this for us
			"-forceprocessing",
		};
	
	/** Stanza for keeping standard APIs. */
	public static final String STANZA_API =
		"-keep,allowoptimization";
	
	/** Obfuscates specified class members. */ 
	public static final String STANZA_OBFUSCATE_MEMBERS =
		"-keepclassmembers,allowoptimization,allowobfuscation";
	
	/** Stanza for keeping everything. */
	public static final String STANZA_DO_NOT_TOUCH =
		"-keep,includecode";
	
	/** Settings used to strip debugging. */
	static final String[] _STRIP_DEBUG = new String[]
		{
			// Assume the debug flags are always false
			"-assumevalues",
				"class", "cc.squirreljme.runtime.cldc.debug.Debugging", "{",
					"static", "final", "boolean", "_ENABLED",
						"=", "false", ";",
					"static", "final", "boolean", "_VERBOSE",
						"=", "false", ";",
				"}",
			"-assumevalues",
				"class", "cc.squirreljme.runtime.cldc.debug.__Flags__", "{",
					"static", "final", "boolean", "_ENABLED",
						"=", "false", ";",
					"static", "final", "boolean", "_VERBOSE",
						"=", "false", ";",
				"}",
			"-assumenosideeffects",
				"class", "cc.squirreljme.runtime.cldc.debug.__Flags__", "{",
					"void", "<clinit>", "(", "...", ")", ";",
				"}",
			"-assumenoexternalsideeffects",
				"class", "cc.squirreljme.runtime.cldc.debug.__Flags__", "{",
					"void", "<clinit>", "(", "...", ")", ";",
				"}",
			"-assumenoexternalreturnvalues",
				"class", "cc.squirreljme.runtime.cldc.debug.__Flags__", "{",
					"void", "<clinit>", "(", "...", ")", ";",
				"}",
			
			// Remove any code that calls these debugging calls
			"-assumenosideeffects",
				"class", "cc.squirreljme.runtime.cldc.debug.Debugging", "{",
					"public", "static", "void", "debugNote", 
						"(", "...", ")", ";",
					"public", "static", "void", "notice", "(", "...", ")", ";",
					"public", "static", "void", "todoNote", 
						"(", "...", ")", ";",
				"}",
			"-assumenoexternalsideeffects",
				"class", "cc.squirreljme.runtime.cldc.debug.Debugging", "{",
					"public", "static", "void", "debugNote", 
						"(", "...", ")", ";",
					"public", "static", "void", "notice", "(", "...", ")", ";",
					"public", "static", "void", "todoNote", 
						"(", "...", ")", ";",
				"}",
			"-assumenoexternalreturnvalues",
				"class", "cc.squirreljme.runtime.cldc.debug.Debugging", "{",
					"public", "static", "void", "debugNote", 
						"(", "...", ")", ";",
					"public", "static", "void", "notice", "(", "...", ")", ";",
					"public", "static", "void", "todoNote", 
						"(", "...", ")", ";",
				"}",
			"-assumevalues",
				"class", "cc.squirreljme.runtime.cldc.debug.Debugging", "{",
					"public", "static", "boolean", "enabled", "(", "...", ")", 
						"return", "false", ";",
					"public", "static", "boolean", "verbose", "(", "...", ")", 
						"return", "false", ";",
				"}",
			
			// Disable some DebugShelf methods
			"-assumevalues",
				"class", "cc.squirreljme.jvm.mle.DebugShelf", "{",
					"public", "static", "int", "verbose", "(", "...", ")",
						"return", "0", ";",
					"public", "static", "int", "verboseInternalThread", 
						"(", "...", ")",
						"return", "0", ";",
				"}",
			"-assumenosideeffects",
				"class", "cc.squirreljme.jvm.mle.DebugShelf", "{",
					"public", "static", "int", "verbose", "(", "...", ")", ";",
					"public", "static", "int", "verboseInternalThread", 
						"(", "...", ")", ";",
					"public", "static", "void", "verboseStop", 
						"(", "...", ")", ";",
				"}",
			"-assumenoexternalsideeffects",
				"class", "cc.squirreljme.jvm.mle.DebugShelf", "{",
					"public", "static", "int", "verbose", "(", "...", ")", ";",
					"public", "static", "int", "verboseInternalThread", 
						"(", "...", ")", ";",
					"public", "static", "void", "verboseStop", 
						"(", "...", ")", ";",
				"}",
			"-assumenoexternalreturnvalues",
				"class", "cc.squirreljme.jvm.mle.DebugShelf", "{",
					"public", "static", "int", "verbose", "(", "...", ")", ";",
					"public", "static", "int", "verboseInternalThread", 
						"(", "...", ")", ";",
					"public", "static", "void", "verboseStop", 
						"(", "...", ")", ";",
				"}",
		};
	
	/** Settings used to help make reflection work properly. */
	static final String[] _REFLECTION = new String[]
		{
			// Do not trash enumerations as we need those to work properly
			"-keepclassmembers", "enum", "*", "{",
				"<fields>", ";",
				"public", "static", "**[]", "values",
					"(", ")", ";",
				"public", "static", "**", "valueOf",
					"(", "java.lang.String", ")", ";",
				"}",
			
			"-keepclassmembernames", 
			"enum", "*", "{",
				"<fields>", ";",
				"public", "static", "**[]", "values",
					"(", ")", ";",
				"public", "static", "**", "valueOf",
					"(", "java.lang.String", ")", ";",
				"}",
			
			// Keep non-static constructors, since they can be called and
			// utilized... if they are removed then some things actually break
			// and stop working properly
			"-keepclassmembers", 
			"class", "*", "{",
					"!private", "<init>", "(", "...", ")", ";",
				"}",
			
			// Keep anything that can be launched
			"-keepclasseswithmembers", 
			"class", "*", "{",
				"public", "static", "void", "main", "(",
					"java.lang.String[]", ")", ";",
			"}",
			"-keep",
			"class", "*", "extends",
				"javax.microedition.midlet.MIDlet", "{",
				"void", "destroyApp()", ";",
				"void", "startApp()", ";",
			"}",
			"-keep", 
			"class", "*", "extends",
				"com.nttdocomo.ui.IApplication",
		};
	
	/**
	 * Newer settings to be parsed which effectively keeps everything that
	 * is public for the most part, due to the vast number of ProGuard issues.
	 */
	static final String[] _PARSE_SETTINGS = new String[]
		{
			// Keep any class that is public
			VMCompactLibraryTaskAction.STANZA_API,
			"public", "class", "*", "{",
				// Keep public members
				"public", "<init>", "(", "...", ")", ";",
				"public", "<methods>", ";",
				"public", "<fields>", ";",
				
				// Keep protected members 
				"protected", "<init>", "(", "...", ")", ";",
				"protected", "<methods>", ";",
				"protected", "<fields>", ";",
				
				// Keep native methods
				"native", "<methods>", ";",
			"}",
			
			VMCompactLibraryTaskAction.STANZA_DO_NOT_TOUCH,
			"@cc.squirreljme.runtime.cldc.annotation.KeepAbsolutelyEverything",
			"class", "*", "{",
				"*", ";",
			"}",
		};
	
	/** Settings for tests. */
	static final String[] _TEST_SETTINGS =
		{
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
		// It is possible for ProGuard to run out of memory
		for (int attempt = 0; attempt < 3; attempt++)
			try
			{
				// Force a double GC because pre-existing ProGuard runs and
				// caches just leave stuff in memory that just causes ProGuard
				// to fail more often than not, even with 4GiB+ memory assigned
				Runtime.getRuntime().gc();
				System.gc();
				
				// Try to run normally, or with less aggression
				if (attempt == 0 || attempt == 1)
					this.__execute(__task, attempt == 0);
				
				// Fallback to just copy the library
				else
				{
					// Get the task being worked on
					VMCompactLibraryTask compactTask =
						(VMCompactLibraryTask)__task;
					
					// Where are we reading/writing to/from?
					Path inputJarPath = compactTask.inputBaseJarPath().get();
					Path outputJarPath = compactTask.outputJarPath().get();
					Path outputMapPath = compactTask.outputMapPath().get();
					
					// Could fail
					try
					{
						// Copy the input to the output
						Files.copy(inputJarPath, outputJarPath,
							StandardCopyOption.REPLACE_EXISTING);
						
						// Initialize a blank mapping file
						Files.write(outputMapPath, new byte[0],
							StandardOpenOption.CREATE,
							StandardOpenOption.WRITE,
							StandardOpenOption.TRUNCATE_EXISTING);
					}
					
					// Just forward out write failures
					catch (IOException __e)
					{
						throw new RuntimeException(__e.getMessage(), __e);
					}
				}
				
				// Success!
				return;
			}
			
			// ProGuard has actually run out of memory, note that it
			// erroneously wraps it in RuntimeException as well
			catch (RuntimeException|OutOfMemoryError|StackOverflowError __oom)
			{
				// Print it out just to be verbose
				__oom.printStackTrace();
				
				// We need to find if this was ever thrown up the exception tree
				// as ProGuard wraps errors when it should not
				Throwable found = null;
				if (__oom instanceof OutOfMemoryError)
					found = __oom;
				if (__oom instanceof StackOverflowError)
					found = __oom;
				else if (__oom instanceof RuntimeException)
					do
					{
						found = (found == null ? __oom.getCause() :
							found.getCause());
					} while (found != null &&
						!(found instanceof OutOfMemoryError));
				
				// Did not find an out of memory error?
				if (!(found instanceof OutOfMemoryError) &&
					!(found instanceof StackOverflowError))
					throw __oom;
				
				// Double-GC to force it to run, hopefully since we did have an
				// actual out of memory event
				Runtime.getRuntime().gc();
				System.gc();
			}
	}
	
	/**
	 * The actual execution of the ask.
	 * 
	 * @param __task The task being executed.
	 * @param __aggressive Optimize aggressively.
	 * @throws OutOfMemoryError If this ran out of memory.
	 * @throws StackOverflowError If the stack overflows.
	 * @since 2023/02/01
	 */
	private void __execute(Task __task, boolean __aggressive)
		throws OutOfMemoryError, StackOverflowError
	{
		VMCompactLibraryTask compactTask = (VMCompactLibraryTask)__task;
		
		// Where are we reading/writing to/from?
		Path inputPath = compactTask.inputBaseJarPath().get();
		Path outputJarPath = compactTask.outputJarPath().get();
		Path outputMapPath = compactTask.outputMapPath().get();
		
		// Some settings may be configured
		SquirrelJMEPluginConfiguration projectConfig =
			SquirrelJMEPluginConfiguration.configuration(__task.getProject());
		
		// Set an inline limit for ProGuard, so it does not produce very large
		// inlined methods.
		try
		{
			System.setProperty("maximum.resulting.code.length", "2000");
		}
		catch (SecurityException ignored)
		{
		}
		
		// Run the task
		Path tempJarFile = null;
		Path tempInputMapFile = null;
		Path tempOutputMapFile = null;
		try
		{
			// Look into the Jar file and check if there are class files, if
			// there are none then there is nothing to compact
			boolean atLeastOneClass = false;
			try (InputStream in = Files.newInputStream(inputPath,
					StandardOpenOption.READ);
				ZipInputStream zip = new ZipInputStream(in))
			{
				for (;;)
				{
					// Get the next entry
					ZipEntry entry = zip.getNextEntry();
					if (entry == null)
						break;
					
					String name = entry.getName();
					if (name.endsWith(".class"))
						atLeastOneClass = true;
				}
			}
			
			// No classes were found, so do nothing
			if (!atLeastOneClass)
			{
				Files.copy(inputPath, outputJarPath,
					StandardCopyOption.REPLACE_EXISTING);
				
				return;
			}
			
			// Setup temporary file to output to when finished
			tempJarFile = Files.createTempFile("out", ".jar");
			tempInputMapFile = Files.createTempFile("in", ".map");
			tempOutputMapFile = Files.createTempFile("out", ".map");
			
			// Need to delete the created temporary file, otherwise Proguard
			// will just say "The output appears up to date" and do nothing
			Files.delete(tempJarFile);
			Files.delete(tempOutputMapFile);
			
			// We need to include all the inputs that were already ran through
			// ProGuard, so we basically need to look at the dependencies and
			// map them around accordingly
			// We also need to combine the mapping files as well
			ClassPath libraryJars = new ClassPath();
			boolean applyMapping = false;
			for (VMCompactLibraryTask compactDep :
				VMHelpers.compactLibTaskDepends(__task.getProject(),
					this.sourceSet))
			{
				Path baseJarFile = compactDep.baseJar.getOutputs().getFiles()
					.getSingleFile().toPath();
				
				// Add the library, but the pre-obfuscated form since we need
				// to know what it is
				if (Files.exists(baseJarFile))
					libraryJars.add(new ClassPathEntry(
						compactDep.baseJar.getOutputs().getFiles()
							.getSingleFile(), false));
				
				// If the mapping file exists, concatenate it
				if (Files.exists(compactDep.outputMapPath().get()))
				{
					// Do use mapping now
					applyMapping = true;
					
					// Add all the information
					Files.write(tempInputMapFile,
						Files.readAllLines(compactDep.outputMapPath().get()),
						StandardOpenOption.APPEND, StandardOpenOption.WRITE);
				}
			}
			
			// Base options to use
			List<String> proGuardOptions = new ArrayList<>();
			
			// Strip all debug info
			proGuardOptions.addAll(
				Arrays.asList(VMCompactLibraryTaskAction._STRIP_DEBUG));
			
			// Add base configuration settings
			proGuardOptions.addAll(
				Arrays.asList(VMCompactLibraryTaskAction._BASE_CONFIG));
			
			if (false)
			{
				// Make sure reflection works, at the minimum
				proGuardOptions.addAll(
					Arrays.asList(VMCompactLibraryTaskAction._REFLECTION));
			}
			
			// API and SquirrelJMEVendorAPI are the same, except using
			// different labels... it is very annoying to have
			// duplicate rules for both due to ProGuard limitations
			// Has to be done for enum as well
			List<String> baseApi = new ArrayList<>();
			for (String classy : Arrays.asList("class", "interface", "enum"))
				for (String opt :
					VMCompactLibraryTaskAction._PARSE_SETTINGS)
				{
					// Change class to something else?
					if (opt.equals("class"))
						baseApi.add(classy);
					
					// Otherwise, plainly copy it
					else
						baseApi.add(opt);
			}
			
			// Base parsed settings, for all interface types
			proGuardOptions.addAll(baseApi);
			
			// Strip all debug info
			proGuardOptions.addAll(
				Arrays.asList(VMCompactLibraryTaskAction._STRIP_DEBUG));
			
			// Optimization settings
			proGuardOptions.add("-optimizations");
			StringBuilder optimizationOptions = new StringBuilder();
			for (String optimize : VMCompactLibraryTaskAction._OPTIMIZATIONS)
			{
				if (optimizationOptions.length() > 0)
					optimizationOptions.append(',');
				
				optimizationOptions.append(optimize);
			}
			proGuardOptions.add(optimizationOptions.toString());
			
			// Are we testing?
			boolean isTesting =
				SourceSet.TEST_SOURCE_SET_NAME.equals(this.sourceSet) ||
				VMHelpers.TEST_FIXTURES_SOURCE_SET_NAME.equals(this.sourceSet);
			
			// Test settings?
			if (isTesting)
				proGuardOptions.addAll(Arrays.asList(
					VMCompactLibraryTaskAction._TEST_SETTINGS));
			
			// Add any additional options as needed
			List<String> projectOptions =
				VMCompactLibraryTask.__optionsBySourceSet(
					__task.getProject(), this.sourceSet).get();
			
			// Add the options
			if (projectOptions != null && !projectOptions.isEmpty())
				proGuardOptions.addAll(projectOptions);
			
			// Parse initial configuration with settings
			Configuration config = new Configuration();
			try (ConfigurationParser parser = new ConfigurationParser(
				proGuardOptions.toArray(new String[proGuardOptions.size()]),
				new Properties()))
			{
				parser.parse(config);
			}
			
			// We are neither of these platforms, we say we are not Java ME
			// because it will remove StackMapTable and instead use StackMap
			// which is not what we want
			config.android = false;
			config.microEdition = false;
			
			// Consumers of the libraries/APIs need to see the annotation
			// information if it is there, to make sure it is retained
			if (!isTesting)
				config.keepAttributes = Arrays.asList(
					"InnerClasses",
					"RuntimeInvisibleAnnotations",
					"RuntimeVisibleAnnotations",
					"Exceptions",
					"Signature");
				
			// Keep more debugging attributes, so we can more easily figure
			// things out when debugging
			else
				config.keepAttributes = Arrays.asList(
					"InnerClasses",
					"*Annotation*",
					"Exceptions", 
					"Signature", 
					"LineNumberTable",
					"LocalVariableTable", 
					"LocalVariableTypeTable",
					"SourceFile");
			
			// Do not skip parsing classes
			config.skipNonPublicLibraryClasses = false;
			config.skipNonPublicLibraryClassMembers = false;
			
			// These will break in general
			config.mergeInterfacesAggressively = false;
			config.allowAccessModification = false;
			
			// Not Kotlin
			config.enableKotlinAsserter = false;
			config.keepKotlinMetadata = false;
			/*config.dontProcessKotlinMetadata = true;*/
			
			// ZIPs do not need to be aligned, make them as small as possible
			config.zipAlign = 1;
			
			// Target Java 7
			config.preverify = true;
			config.targetClassVersion = (51 << 16);
			
			// Reduce space by obfuscating
			// Note that shrinking will delete classes and methods, this must
			// not occur... ProGuard is just too broken to handle this.
			config.shrink = false;
			config.obfuscate = true;
			config.optimize = !isTesting;
			config.flattenPackageHierarchy = "$" +
				(projectConfig.javaDocErrorCode == null ? "??" :
				projectConfig.javaDocErrorCode);
			config.repackageClasses = config.flattenPackageHierarchy;
			
			// Aggressive?
			if (__aggressive)
			{
				config.optimizationPasses = 8;
				/*config.optimizeConservatively = false;*/
			}
			
			// If not, we likely crashed and/or ran out of memory
			else
			{
				config.optimizationPasses = 4;
				/*config.optimizeConservatively = true;*/
			}
			
			if (false)
			{
				// For mapping files, members do need to be unique
				// Do not use mix case class names, so that more strings can
				// be compacted together accordingly
				config.useUniqueClassMemberNames = true;
				config.useMixedCaseClassNames = false;
			}
			else
			{
				// More compact, but also seems to be more compatible with
				// how ProGuard operates
				config.useUniqueClassMemberNames = false;
				config.useMixedCaseClassNames = true;
			}
			
			// Write mapping to the output file, since we will use it later on
			config.printMapping = tempOutputMapFile.toFile();
			
			// Utilize the combined mapping file that was made so that we can
			// use everything we have?
			if (applyMapping)
				config.applyMapping = tempInputMapFile.toFile();
			
			// Be noisy
			config.verbose = true;
			//config.dump = Configuration.STD_OUT;
			//config.printUsage = Configuration.STD_OUT;
			//config.printConfiguration = Configuration.STD_OUT;
			
			// Use whatever libraries were found
			config.libraryJars = libraryJars;
			
			// Setup input and output Jar
			ClassPath programJars = new ClassPath();
			config.programJars = programJars;
			
			// Input source Jar
			programJars.add(
				new ClassPathEntry(inputPath.toFile(), false));
			
			// Output temporary Jar
			programJars.add(new ClassPathEntry(
				tempJarFile.toFile(), true));
			
			// Run the shrinking/obfuscation
			try
			{
				new ProGuard(config).execute();
			}
			finally
			{
				Files.move(tempInputMapFile,
					outputMapPath.resolveSibling(
						outputMapPath.getFileName() + ".in"),
					StandardCopyOption.REPLACE_EXISTING);
			}
			
			// Insurance
			if (Files.size(tempJarFile) <= 12)
				throw new RuntimeException("Nothing happened?");
			
			// Move to output
			Files.move(tempJarFile,
				outputJarPath,
				StandardCopyOption.REPLACE_EXISTING);
			
			if (Files.exists(tempOutputMapFile))
				Files.move(tempOutputMapFile,
					outputMapPath,
					StandardCopyOption.REPLACE_EXISTING);
		}
		catch (Exception __e)
		{
			throw new RuntimeException("Failed to shrink/obfuscate.", __e);
		}
		
		// Cleanup anything left over
		finally
		{
			if (tempJarFile != null)
				try
				{
					Files.delete(tempJarFile);
				}
				catch (IOException ignored)
				{
				}
			
			if (tempInputMapFile != null)
				try
				{
					Files.delete(tempInputMapFile);
				}
				catch (IOException ignored)
				{
				}
			
			if (tempOutputMapFile != null)
				try
				{
					Files.delete(tempOutputMapFile);
				}
				catch (IOException ignored)
				{
				}
		}
	}
}
