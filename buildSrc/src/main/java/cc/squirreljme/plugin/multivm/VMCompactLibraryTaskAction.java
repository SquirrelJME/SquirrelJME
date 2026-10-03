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
			
			// UNKNOWN: ???
			"class/unboxing/enum",
			
			// UNKNOWN: ???
			"code/allocation/variable",
			
			// UNKNOWN: ???
			"code/merging",
			
			// UNKNOWN: ???
			"code/removal/advanced",
			
			// UNKNOWN: ???
			"code/removal/exception",
			
			// UNKNOWN: ???
			"code/removal/simple",
			
			// UNKNOWN: ???
			"code/removal/variable",
			
			// UNKNOWN: ???
			"code/simplification/advanced",
			
			// UNKNOWN: ???
			"code/simplification/arithmetic",
			
			// UNKNOWN: ???
			"code/simplification/branch",
			
			// UNKNOWN: ???
			"code/simplification/cast",
			
			// UNKNOWN: ???
			"code/simplification/field",
			
			// UNKNOWN: ???
			"code/simplification/math",
			
			// UNKNOWN: ???
			"code/simplification/object",
			
			// UNKNOWN: ???
			"code/simplification/string",
			
			// UNKNOWN: ???
			"code/simplification/variable",
			
			// AVOID: Violates the specification
			"!field/generalization/class",
			
			// AVOID: Violates the specification
			"!field/marking/private",
			
			// UNKNOWN: ???
			// This appears to remove null checks:
			// https://github.com/Guardsquare/proguard/issues/128
			"!field/propagation/value",
			
			// AVOID: Violates the specification
			"!field/removal/writeonly",
			
			// AVOID: Violates the specification
			"!field/specialization/type",
			
			// AVOID: Violates the specification
			"!method/generalization/class",
			
			// MAYBE: Seems to break things?
			"!method/inlining/short",
			
			// MAYBE: Seems to break things?
			"!method/inlining/tailrecursion",
			
			// MAYBE: Seems to break things?
			"!method/inlining/unique",
			
			// AVOID: Violates the specification
			"!method/marking/final",
			
			// AVOID: Violates the specification
			"!method/marking/private",
			
			// AVOID: Violates the specification
			"!method/marking/static",
			
			// AVOID: Violates the specification
			"!method/marking/synchronized",
			
			// UNKNOWN: ???
			"method/propagation/parameter",
			
			// UNKNOWN: ???
			"method/propagation/returnvalue",
			
			// AVOID: Violates the specification
			// Double.toString() -> public static String toString$6f5372eb()
			"!method/removal/parameter",
			
			// AVOID: Violates the specification
			"!method/specialization/parametertype",
			
			// AVOID: Violates the specification
			"!method/specialization/returntype",
			
			// UNKNOWN: ???
			"code/removal/advanced",
			
			/*
			// Never allow access flag changes
			"!class/marking/*",
			"!field/marking/*",
			"!method/marking/*",
			
			// Never allow generalizing/specialization from one type to another
			// For example: Arrays.<T>asList() becomes
			// java.util.List
			"!field/generalization/*",
			"!method/generalization/*",
			// java.util.__ArraysList__ asList$158aa2d5(java.lang.Object[])
			// java.lang.__CanSetPrintStream__ err$5f8ce416 -> err$5f8ce416
			"!field/specialization/*",
			"!method/specialization/*",
			
			// Never remove parameters, signatures must remain the same
			"!method/removal/parameter",
			
			// There is optimization for object usage and such, however this
			// is not always correct especially with brackets and native code
			// Do the same for field load/store, as these can be used across
			// native call chains which it has no idea about
			"!code/simplification/object",
			"!code/simplification/field",
			
			// Do not optimize casts, as those can be used for class casts
			// but also there seems to be a bug where casting an unknown type
			// to a known type will cause issues
			"!code/simplification/cast",
			
			// Assume all objects and branches are taken, this is similar to
			// above as there needs to be checks for everything and considering
			// that this is library code this could remove those checks. It
			// can also assume that because no other part of the library calls
			// into this code, that the code is dead anyway.
			"!code/removal/advanced",
			"!code/simplification/object",
			"!code/simplification/branch",
			
			// Variable optimization seems to be broken at times as well
			"!code/allocation/variable",
			
			// Never remove fields
			"!field/removal/writeonly",
			
			// Inlining methods does usually increase code size, but it also
			// can cause issues where behavior gets changed
			"!method/inlining/*",
			
			// Do not merge classes together, either vertically or
			// horizontally... this otherwise has Number optimized away despite
			// being marked as @Api because it is only extended from and has
			// nothing of its own
			"!class/merging/*",
			 */
		};
	
	/** Base configuration. */
	static final String[] _BASE_CONFIG = new String[]
		{
			// Be a bit more descriptive (this prints the config)
			//"-verbose",
			
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
		"-keep,allowoptimization,includedescriptorclasses";
	
	/** Stanza for keeping standard APIs, with less pulling in. */
	public static final String STANZA_API_NO_DESC =
		"-keep,allowoptimization";
	
	/** Stanza for compacting APIs. */
	public static final String STANZA_COMPACT =
		"-keep,allowoptimization,allowobfuscation";
	
	/** Stanza for keeping everything. */
	public static final String STANZA_KEEP_ALL_NO_OPTIMIZE =
		"-keep,includecode";
	
	/** Stanza for keeping everything, but only members. */
	public static final String STANZA_KEEP_ALL_ONLY_MEMBERS =
		"-keepclasseswithmembers,includecode,allowoptimization";
	
	/** Stanza for keeping with obfuscate, but only members. */
	public static final String STANZA_KEEP_ALL_ONLY_MEMBERS_OBFUSCATE =
		"-keepclasseswithmembers,includecode,allowoptimization," +
		"allowobfuscation";
	
	/** Settings used to strip debugging. */
	static final String[] _STRIP_DEBUG = new String[]
		{
			// Assume the debug flags are always false
			"-assumevalues",
				"class", "cc.squirreljme.runtime.cldc.debug.Debugging", "{",
					"static", "boolean", "ENABLED",
						"=", "false", ";",
					"static", "boolean", "VERBOSE",
						"=", "false", ";",
				"}",
			"-assumevalues",
				"class", "cc.squirreljme.runtime.cldc.debug.__Flags__", "{",
					"static", "boolean", "_ENABLED",
						"=", "false", ";",
					"static", "boolean", "_VERBOSE",
						"=", "false", ";",
				"}",
			"-assumenosideeffects",
				"class", "cc.squirreljme.runtime.cldc.debug.__Flags__", "{",
					"void", "<clinit>", "(", ")", ";",
				"}",
			"-assumenoexternalsideeffects",
				"class", "cc.squirreljme.runtime.cldc.debug.__Flags__", "{",
					"void", "<clinit>", "(", ")", ";",
				"}",
			"-assumenoexternalreturnvalues",
				"class", "cc.squirreljme.runtime.cldc.debug.__Flags__", "{",
					"void", "<clinit>", "(", ")", ";",
				"}",
			
			// Remove any code that calls these debugging calls
			"-assumenosideeffects",
				"class", "cc.squirreljme.runtime.cldc.debug.Debugging", "{",
					"void", "debugNote", "(",
						"java.lang.String", ")", ";",
					"void", "debugNote", "(",
						"java.lang.String", ",",
						"java.lang.Object[]", ")", ";",
					"void", "notice", "(",
						"java.lang.String", ")", ";",
					"void", "notice", "(",
						"java.lang.String", ",",
						"java.lang.Object[]", ")", ";",
					"void", "todoNote", "(",
						"java.lang.String", ")", ";",
					"void", "todoNote", "(",
						"java.lang.String", ",",
						"java.lang.Object[]", ")", ";",
				"}",
			"-assumenoexternalsideeffects",
				"class", "cc.squirreljme.runtime.cldc.debug.Debugging", "{",
					"void", "debugNote", "(",
						"java.lang.String", ")", ";",
					"void", "debugNote", "(",
						"java.lang.String", ",",
						"java.lang.Object[]", ")", ";",
					"void", "notice", "(",
						"java.lang.String", ")", ";",
					"void", "notice", "(",
						"java.lang.String", ",",
						"java.lang.Object[]", ")", ";",
					"void", "todoNote", "(",
						"java.lang.String", ")", ";",
					"void", "todoNote", "(",
						"java.lang.String", ",",
						"java.lang.Object[]", ")", ";",
				"}",
			"-assumenoexternalreturnvalues",
				"class", "cc.squirreljme.runtime.cldc.debug.Debugging", "{",
					"void", "debugNote", "(",
						"java.lang.String", ")", ";",
					"void", "debugNote", "(",
						"java.lang.String", ",",
						"java.lang.Object[]", ")", ";",
					"void", "notice", "(",
						"java.lang.String", ")", ";",
					"void", "notice", "(",
						"java.lang.String", ",",
						"java.lang.Object[]", ")", ";",
					"void", "todoNote", "(",
						"java.lang.String", ")", ";",
					"void", "todoNote", "(",
						"java.lang.String", ",",
						"java.lang.Object[]", ")", ";",
				"}",
			
			// Disable some DebugShelf methods
			"-assumevalues",
				"class", "cc.squirreljme.jvm.mle.DebugShelf", "{",
					"int", "verbose", "(",
						"int", ")", "return", "0", ";",
					"int", "verboseInternalThread", "(",
						"int", ")", "return", "0", ";",
				"}",
			"-assumenosideeffects",
				"class", "cc.squirreljme.jvm.mle.DebugShelf", "{",
					"int", "verbose", "(",
						"int", ")", ";",
					"int", "verboseInternalThread", "(",
						"int", ")", ";",
					"void", "verboseStop", "(",
						"int", ")", ";",
				"}",
			"-assumenoexternalsideeffects",
				"class", "cc.squirreljme.jvm.mle.DebugShelf", "{",
					"int", "verbose", "(",
						"int", ")", ";",
					"int", "verboseInternalThread", "(",
						"int", ")", ";",
					"void", "verboseStop", "(",
						"int", ")", ";",
				"}",
			"-assumenoexternalreturnvalues",
				"class", "cc.squirreljme.jvm.mle.DebugShelf", "{",
					"int", "verbose", "(",
						"int", ")", ";",
					"int", "verboseInternalThread", "(",
						"int", ")", ";",
					"void", "verboseStop", "(",
						"int", ")", ";",
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
	
	/** Native callbacks. */
	static final String[] _CALLBACKS = new String[]
		{
			// Specific implements
			VMCompactLibraryTaskAction.STANZA_KEEP_ALL_ONLY_MEMBERS_OBFUSCATE, 
			"class", "*", "implements",
				"java.lang.Runnable", "{",
				"public", "void", "run", "(", ")", ";",
			"}",
			
			VMCompactLibraryTaskAction.STANZA_KEEP_ALL_ONLY_MEMBERS_OBFUSCATE,
			"class", "*", "implements",
				"cc.squirreljme.jvm.mle.callbacks.ShelfCallback", "{",
				"<methods>", ";",
			"}",
			
			VMCompactLibraryTaskAction.STANZA_KEEP_ALL_ONLY_MEMBERS_OBFUSCATE,
			"class", "*", "implements",
			"cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchListener", "{",
				"<methods>", ";",
			"}",
			
			// Specific annotated methods
			VMCompactLibraryTaskAction.STANZA_KEEP_ALL_ONLY_MEMBERS_OBFUSCATE,
			"class", "*", "{",
			"@cc.squirreljme.jvm.mle.scritchui.annotation.ScritchEventLoop",
				"<methods>", ";",
			"}",
			
			VMCompactLibraryTaskAction.STANZA_KEEP_ALL_ONLY_MEMBERS_OBFUSCATE,
			"class", "*", "{",
				"@cc.squirreljme.runtime.lcdui.SerializedEvent",
				"<methods>", ";",
			"}",
		};
	
	/**
	 * Settings to use in the configuration for keeping, etc.
	 * 
	 * The way this works below is that any setting which will replace all
	 * occurrences of class with interface and enum.
	 */
	static final String[] _PARSE_SETTINGS = new String[]
		{
			// NOTE: ProGuard says "class" includes classes and interfaces
			// in its documentation... however, observation says otherwise
			// There is code below that will change all "class" to "enum"
			// and "interface" so they are all shared and there is no
			// duplication here
			
			// Anything that is a bracket must be kept, no matter what
			VMCompactLibraryTaskAction.STANZA_KEEP_ALL_NO_OPTIMIZE, 
			"class", "*", "implements",
				"cc.squirreljme.jvm.mle.brackets.Bracket", "{",
			"}",
			
			// All permission types must be kept, no matter what
			VMCompactLibraryTaskAction.STANZA_KEEP_ALL_NO_OPTIMIZE, 
			"class", "*", "implements",
				"java.security.Permission", "{",
			"}",
			
			// Standard API
			VMCompactLibraryTaskAction.STANZA_API,
			"@cc.squirreljme.runtime.cldc.annotation.Api",
			"public", "class", "*", "{",
				"@cc.squirreljme.runtime.cldc.annotation.Api",
				"!private", "*", ";",
			"}",
			
			// SquirrelJMEVendorApi
			VMCompactLibraryTaskAction.STANZA_API,
			"@cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi",
			"public", "class", "*", "{",
				"@cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi",
				"!private", "*", ";",
			"}",
			
			// KeepWhenCompacting
			VMCompactLibraryTaskAction.STANZA_KEEP_ALL_ONLY_MEMBERS,
			//"@cc.squirreljme.runtime.cldc.annotation.KeepWhenCompacting",
			"class", "*", "{",
				"@cc.squirreljme.runtime.cldc.annotation.KeepWhenCompacting",
				"*", ";",
			"}",
			
			// KeepAbsolutelyEverything
			VMCompactLibraryTaskAction.STANZA_KEEP_ALL_NO_OPTIMIZE,
			"@cc.squirreljme.runtime.cldc.annotation.KeepAbsolutelyEverything",
			"class", "*", "{",
				"*", ";",
			"}",
		};
	
	/** Settings for tests. */
	static final String[] _TEST_SETTINGS =
		{
			// Do not optimize here, we want to keep everything around
			"-dontoptimize",
			"-dontshrink",
			
			// Tests can break things in specific ways that ProGuard does
			// not like much
			"-dontwarn",
			
			// This keeps everything about tests but will use pre-existing
			// mappings and otherwise if we are using obfuscated classes
			// This is the only thing I have found that works
			"-keep", "class", "*",
			"-keepnames", "class", "*",
			"-keepclassmembers", "class", "*", "{",
				"<fields>", ";",
				"<methods>", ";",
				"}",
			"-keepclassmembernames", "class", "*", "{",
				"<fields>", ";",
				"<methods>", ";",
			"}",
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
			
			// Make sure reflection works, at the minimum
			proGuardOptions.addAll(
				Arrays.asList(VMCompactLibraryTaskAction._REFLECTION));
			
			// Keep all callbacks, since they get stripped as nothing
			// sees them and everything just breaks
			proGuardOptions.addAll(
				Arrays.asList(VMCompactLibraryTaskAction._CALLBACKS));
			
			// API and SquirrelJMEVendorAPI are the same, except using
			// different labels... it is very annoying to have
			// duplicate rules for both due to ProGuard limitations
			// Has to be done for enum as well
			List<String> baseApi = new ArrayList<>();
			for (String classy : Arrays.asList("class", "interface", "enum"))
				for (String opt : VMCompactLibraryTaskAction._PARSE_SETTINGS)
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
			
			// Reduce space and obfuscate
			config.shrink = true;
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
			config.verbose = false;
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
