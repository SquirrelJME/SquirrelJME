// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import cc.squirreljme.plugin.SquirrelJMEPluginConfiguration;
import cc.squirreljme.plugin.general.cmake.CMakeBuildTask;
import cc.squirreljme.plugin.multivm.ident.SourceTargetClassifier;
import cc.squirreljme.plugin.multivm.ident.TargetClassifier;
import cc.squirreljme.plugin.swm.JavaMEMidlet;
import cc.squirreljme.plugin.util.FileLocation;
import cc.squirreljme.plugin.util.TestDetection;
import cc.squirreljme.plugin.util.UnassistedLaunchEntry;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.function.Supplier;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.ProjectDependency;
import org.gradle.api.capabilities.Capability;
import org.gradle.api.file.FileCollection;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.SourceSet;
import org.gradle.jvm.tasks.Jar;

/**
 * Helpers for the multi-VM handlers.
 *
 * @since 2020/08/07
 */
@SuppressWarnings("OverlyComplexClass")
public final class VMHelpers
{
	/** The class used for single test runs. */
	public static final String SINGLE_TEST_RUNNER =
		"net.multiphasicapps.tac.MainSingleRunner";
	
	/** Source set name for test fixtures. */
	public static final String TEST_FIXTURES_SOURCE_SET_NAME =
		"testFixtures";
	
	/** Main configurations. */
	private static final String[] _MAIN_CONFIGS =
		new String[]{"api", "implementation"};
	
	/** Test configurations. */
	private static final String[] _TEST_CONFIGS =
		new String[]{"testImplementation", "testImplementation"};
	
	/** Declaration of hyper-test parameters. */
	private static final String _HYPER_PARAMETERS_KEY =
		"hyper-parameters";
	
	/** Declaration of multi-test parameters. */
	private static final String _MULTI_PARAMETERS_KEY =
		"multi-parameters";
	
	/* Copy buffer size. */
	public static final int COPY_BUFFER =
		4096;
	
	/**
	 * Not used.
	 * 
	 * @since 2020/08/07
	 */
	private VMHelpers()
	{
	}
	
	/**
	 * Returns a collection of the tests that are available.
	 * 
	 * @param __project The project to check.
	 * @param __sourceSet The source set for the project.
	 * @return The list of available tests.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/07
	 */
	public static Map<String, CandidateTestFiles> availableTests(
		Project __project, String __sourceSet)
		throws NullPointerException
	{
		if (__project == null || __sourceSet == null)
			throw new NullPointerException("NARG");
		
		// Mappings of both source and expected files
		Set<String> names = new TreeSet<>();
		Map<String, FileLocation> sources = new HashMap<>();
		Map<String, FileLocation> secondarySources = new HashMap<>();
		Map<String, FileLocation> expects = new HashMap<>();
		
		// Setup initial set of sources and files for lookup
		Set<CandidateTestFileSource> everything = new LinkedHashSet<>();
		everything.add(new CandidateTestFileSource(true,
			TestDetection.sourceSetFiles(__project, __sourceSet)));
		
		// Go through dependencies since we need to know about those test
		// details and such
		for (ProjectAndTaskName projectTask : VMHelpers.runClassTasks(
			__project, new SourceTargetClassifier(__sourceSet, VMType.HOSTED,
				BangletVariant.NONE, ClutterLevel.DEBUG)))
		{
			// Only consider actual projects
			Project subProject = __project.project(projectTask.project);
			Task subTask = subProject.getTasks().getByName(projectTask.task);
			if (!(subTask instanceof VMExecutableTask))
				continue;
			
			// Add to be evaluated by everything
			everything.add(new CandidateTestFileSource(false,
				TestDetection.sourceSetFiles(subProject,
					((VMExecutableTask)subTask).getSourceSet())));
		}
		
		// Go through all the potential candidate sources
		for (CandidateTestFileSource candidateSource : everything)
		{
			// Scan through every file and match sources and expected tests
			for (FileLocation file : candidateSource.collection)
			{
				// If this is a MIME encoded file, normalize the name, so it
				// does not include the mime extension as that is removed at
				// JAR build time
				Path normalized;
				if ("__mime".equals(VMHelpers.getExtension(
						file.getRelative())))
					normalized = VMHelpers.stripExtension(file.getRelative());
				else
					normalized = file.getRelative();
				
				// Determine the name of the class, used to filter if this is
				// a valid test or not at a later stage
				String testName = VMHelpers.pathToString('.',
						VMHelpers.stripExtension(normalized));
				
				// Always add the test now, since this could be a super class
				// of a test but not something that should be called
				names.add(testName);
				
				// Determine how this file is to be handled
				switch (VMHelpers.getExtension(normalized))
				{
						// Executable Classes
					case "class":
					case "java":
					case "j":
						if (candidateSource.primary)
							sources.put(testName, file);
						else
							secondarySources.put(testName, file);
						break;
					
						// Test expectations
					case "in":
						expects.put(testName, file);
						break;
				}
			}
		}
		
		// Setup full set of possible candidates
		Map<String, CandidateTestFiles> fullSet = new TreeMap<>();
		for (String testName : names)
		{
			// Maybe an abstract test?
			FileLocation source = sources.get(testName);
			FileLocation secondarySource = secondarySources.get(testName);
			if (source == null && secondarySource == null)
				continue;
			
			// Store it
			fullSet.put(testName, new CandidateTestFiles((source != null),
				(source != null ? source : secondarySource),
				expects.get(testName)));
		}
		
		// Map tests and candidate sets to normal candidates
		Map<String, CandidateTestFiles> result = new TreeMap<>();
		for (Map.Entry<String, CandidateTestFiles> entry : fullSet.entrySet())
		{
			String testName = entry.getKey();
			CandidateTestFiles candidate = entry.getValue();
			
			// Ignore if this does not match the expected name form
			if (!TestDetection.isTest(testName))
				continue;
			
			// If this is not a primary test, then it does not get to be added
			// to the test list nor do we need to do any processing for it
			if (!candidate.primary)
				continue;
			
			// Load in the manifest for the candidate
			Manifest manifest = VMHelpers.__loadExpectedResults(testName,
				fullSet);
			
			// Replace candidate entry with what is fully available here
			Map<String, String> expectedValues = new LinkedHashMap<>();
			for (Map.Entry<Object, Object> value : manifest.getMainAttributes()
				.entrySet())
				expectedValues.put(Objects.toString(value.getKey()),
					Objects.toString(value.getValue()));
			candidate = new CandidateTestFiles(candidate.primary,
				candidate.sourceCode, candidate.expectedResult,
				Collections.unmodifiableMap(expectedValues));
			
			// Load the expected results and see if there multi-parameters
			Collection<String> multiParams = VMHelpers.__parseMultiParams(
				manifest);
			
			// Single test, has no parameters
			if (multiParams == null || multiParams.isEmpty())
				result.put(testName, candidate);
			
			// Otherwise, signify all the parameters within
			else
			{
				for (String multiParam : multiParams)
					result.put(testName + "@" + multiParam, candidate);
			}
		}
		
		return Collections.unmodifiableMap(result);
	}
	
	/**
	 * Returns the cache directory of the project.
	 * 
	 * @param __project The project to get the cache directory of.
	 * @param __classifier The classifier for the VM.
	 * @return The path provider to the project cache directory.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/15
	 */
	public static Provider<Path> cacheDir(Project __project,
		SourceTargetClassifier __classifier)
		throws NullPointerException
	{
		if (__project == null || __classifier == null)
			throw new NullPointerException("NARG");
		
		return __project.provider(() -> __project.getBuildDir().toPath()
			.resolve("squirreljme").resolve(
				String.format("vm-%s-%s-%s", __classifier.getSourceSet(),
					__classifier.getVmType().vmName(VMNameFormat.LOWERCASE),
					__classifier.getTargetClassifier().getClutterLevel()))
			.resolve(__classifier.getBangletVariant().properNoun));
	}
	
	/**
	 * Returns the class path as a string.
	 *
	 * @param __paths Class paths.
	 * @return The class path as a string.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/21
	 */
	public static String classpathAsString(Path... __paths)
		throws NullPointerException
	{
		return VMHelpers.classpathAsString(false, __paths);
	}
	
	/**
	 * Returns the class path as a string.
	 *
	 * @param __paths Class paths.
	 * @return The class path as a string.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/02/29
	 */
	public static String classpathAsString(Iterable<Path> __paths)
		throws NullPointerException
	{
		return VMHelpers.classpathAsString(false, __paths);
	}
	
	/**
	 * Returns the class path as a string.
	 *
	 * @param __unix Use UNIX separator and not the system one.
	 * @param __paths Class paths.
	 * @return The class path as a string.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/25
	 */
	public static String classpathAsString(boolean __unix, Path... __paths)
		throws NullPointerException
	{
		if (__paths == null)
			throw new NullPointerException("NARG");
		
		return VMHelpers.classpathAsString(Arrays.asList(__paths));
	}
	
	/**
	 * Returns the class path as a string.
	 *
	 * @param __unix Use UNIX separator and not the system one.
	 * @param __paths Class paths.
	 * @return The class path as a string.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/25
	 */
	public static String classpathAsString(boolean __unix,
		Iterable<Path> __paths)
		throws NullPointerException
	{
		if (__paths == null)
			throw new NullPointerException("NARG");
		
		StringBuilder sb = new StringBuilder();
		
		char separator = (__unix ? ':' : File.pathSeparatorChar);
		for (Path path : __paths)
		{
			if (sb.length() > 0)
				sb.append(separator);
			sb.append(path);
		}
		
		return sb.toString();
	}
	
	/**
	 * Decodes the classpath string.
	 * 
	 * @param __string The string to decode.
	 * @return The decoded path.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/10/17
	 */
	public static Path[] classpathDecode(String __string)
		throws NullPointerException
	{
		if (__string == null)
			throw new NullPointerException("NARG");
		
		Collection<Path> result = new LinkedList<>();
		for (String split : __string.split(Pattern.quote(File.pathSeparator)))
			result.add(Paths.get(split));
		
		return result.<Path>toArray(new Path[result.size()]);
	}
	
	/**
	 * Returns all the compact library tasks that the specified project
	 * depends upon.
	 * 
	 * @param __project The project to get the dependencies from.
	 * @param __sourceSet The source set to base off.
	 * @return The tasks which are part of the compaction dependencies.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/02/02
	 */
	public static Collection<VMCompactLibraryTask> compactLibTaskDepends(
		Project __project, String __sourceSet)
		throws NullPointerException
	{
		if (__project == null || __sourceSet == null)
			throw new NullPointerException("NARG");
		
		// Where does this go?
		Collection<VMCompactLibraryTask> result = new LinkedHashSet<>();
		
		// Are we testing?
		boolean isTest = SourceSet.TEST_SOURCE_SET_NAME.equals(__sourceSet);
		boolean isTestFixtures =
			VMHelpers.TEST_FIXTURES_SOURCE_SET_NAME.equals(__sourceSet);
		
		// This is a bit messy but it works for now
		Collection<ProjectAndTaskName> runTasks =
			VMHelpers.runClassTasks(__project,
				SourceTargetClassifier.builder()
					.sourceSet(__sourceSet)
					.targetClassifier(TargetClassifier.builder()
						.bangletVariant(BangletVariant.NONE)
						.vmType(VMType.SPRINGCOAT)
						.clutterLevel(ClutterLevel.RELEASE)
						.build())
					.build());
		for (ProjectAndTaskName projectAndTask : runTasks)
		{
			// Find the referenced project
			Project subProject = __project.project(projectAndTask.project); 
			
			// Ignore our own project, if not testing
			if (__project.equals(subProject) && !isTest && !isTestFixtures)
				continue;
			
			// Check the main source set
			String checkName = TaskInitialization.task("compactLib",
				SourceSet.MAIN_SOURCE_SET_NAME);
			Task maybe = subProject.getTasks().findByName(checkName);
			if (maybe instanceof VMCompactLibraryTask)
			{
				VMCompactLibraryTask task = (VMCompactLibraryTask)maybe;
				result.add(task);
			}
			
			// If we are testing, see if we can pull in any test fixtures
			if (isTest)
			{
				String check = TaskInitialization.task("compactLib",
					VMHelpers.TEST_FIXTURES_SOURCE_SET_NAME);
				Task fixture = subProject.getTasks().findByName(check);
				if (fixture instanceof VMCompactLibraryTask)
				{
					VMCompactLibraryTask task = (VMCompactLibraryTask)fixture;
					result.add(task);
				}
			}
		}
		
		return result;
	}
	
	/**
	 * Copies from the input into the output.
	 * 
	 * @param __in The input.
	 * @param __out The output.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/15
	 */
	public static void copy(InputStream __in, OutputStream __out)
		throws IOException, NullPointerException
	{
		if (__in == null || __out == null)
			throw new NullPointerException("NARG");
		
		byte[] buf = new byte[VMHelpers.COPY_BUFFER];
		for (;;)
		{
			int rc = __in.read(buf);
			
			if (rc < 0)
				return;
			
			__out.write(buf, 0, rc);
		}
	}
	
	/**
	 * Copies from the input into the output while recompressing the Zip file.
	 * 
	 * @param __in The input.
	 * @param __out The output.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/08/08
	 */
	public static void copyRecompressZip(InputStream __in, OutputStream __out)
		throws IOException, NullPointerException
	{
		if (__in == null || __out == null)
			throw new NullPointerException("NARG");
		
		try (ZipInputStream inZip = new ZipInputStream(__in);
			 ZipOutputStream outZip = new ZipOutputStream(__out))
		{
			// Maximum compression
			outZip.setMethod(ZipOutputStream.DEFLATED);
			outZip.setLevel(9);
			
			// Recompress each entry
			for (;;)
			{
				// Get next entry, if null there are none left
				ZipEntry entry = inZip.getNextEntry();
				if (entry == null)
					break;
				
				// Make sure it is compressed
				entry.setMethod(ZipOutputStream.DEFLATED);
				
				// Start entry
				outZip.putNextEntry(entry);
				
				// Copy entry data
				VMHelpers.copy(inZip, outZip);
				
				// Finished writing
				outZip.closeEntry();
			}
			
			// Finalize zip
			outZip.finish();
			outZip.flush();
		}
		
		// Make sure output is flushed
		__out.flush();
	}
	
	/**
	 * Deletes the given directory tree.
	 *
	 * @param __task The task deleting for.
	 * @param __path The path to delete.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/04/08
	 */
	public static void deleteDirTree(Task __task, Path __path)
		throws NullPointerException
	{
		if (__task == null || __path == null)
			throw new NullPointerException("NARG");
		
		// Ignore if not a directory
		Path base = __path.toAbsolutePath().normalize();
		if (!Files.isDirectory(__path))
			return;
		
		// Collect files to delete
		Set<Path> deleteFiles = new LinkedHashSet<>();
		Set<Path> deleteDirs = new LinkedHashSet<>();
		
		// Perform the walk to collect files
		try (Stream<Path> walk = Files.walk(__path))
		{
			walk.forEach((__it) -> {
				Path normal = __it.toAbsolutePath().normalize();
				
				if (Files.isDirectory(normal))
					deleteDirs.add(normal);
				else
					deleteFiles.add(normal);
			});
		}
		catch (IOException __e)
		{
			__e.printStackTrace();
		}
		
		// Run through and delete files then directories
		for (Set<Path> rawByes : Arrays.asList(deleteFiles, deleteDirs))
		{
			List<Path> byes = new ArrayList<>(rawByes);
			Collections.reverse(byes);
			
			for (Path bye : byes)
			{
				// Note
				__task.getLogger().lifecycle(
					String.format("Cleaning %s...", bye));
				
				// Skip out of tree files
				if (!bye.startsWith(base))
				{
					__task.getLogger().lifecycle(
						String.format("%s is out of tree, skipping...", bye));
					continue;
				}
				
				// Perform deletion
				try
				{
					Files.deleteIfExists(bye);
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Attempts to find the emulator library so that can be loaded directly
	 * instead of being extracted by each test process, if possible.
	 * 
	 * @param __anyProject Any project.
	 * @return The path to the emulator library.
	 * @since 2020/12/01
	 */
	@SuppressWarnings("ConstantConditions")
	public static Path findEmulatorLib(Project __anyProject)
		throws NullPointerException
	{
		if (__anyProject == null)
			throw new NullPointerException("NARG");
		
		// We need to look through the emulator base tasks to determine
		// the library to select
		Project emuBase = __anyProject.getRootProject()
			.findProject(":emulators:emulator-base");
		
		// Get the CMake Task for this
		CMakeBuildTask cmake = (CMakeBuildTask)emuBase.getTasks()
			.getByName("libNativeEmulatorBase");
		
		// Use the resultant library
		return cmake.cmakeOutFile;
	}
	
	/**
	 * Returns the full suite libraries for a given task.
	 * 
	 * @param __task The task to get.
	 * @return The path for the full suite libraries.
	 * @since 2022/06/13
	 */
	public static Collection<Path> fullSuiteLibraries(Task __task)
		throws NullPointerException
	{
		Collection<Path> libPath = new LinkedHashSet<>();
		for (VMLibraryTask dep : VMHelpers.fullSuiteLibrariesTasks(__task))
			for (File file : dep.getOutputs().getFiles())
				libPath.add(file.toPath());
		
		return libPath;
	}
	
	/**
	 * Returns the full suite library tasks for a given task.
	 * 
	 * @param __task The task to get.
	 * @return The path for the full suite libraries.
	 * @since 2024/03/05
	 */
	public static Collection<VMLibraryTask> fullSuiteLibrariesTasks(
		Task __task)
		throws NullPointerException
	{
		// Load executable library tasks from our own VM
		Collection<VMLibraryTask> result = new LinkedHashSet<>();
		for (Task dep : __task.getTaskDependencies().getDependencies(__task))
			if (dep instanceof VMLibraryTask)
				result.add((VMLibraryTask)dep);
		
		return result;
	}
	
	/**
	 * Gets the base name of the file without the extension, if there is one.
	 * 
	 * @param __path The path to get from.
	 * @return The file base name.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/02/05
	 */
	public static String getBaseName(Path __path)
		throws NullPointerException
	{
		if (__path == null)
			throw new NullPointerException("NARG");
		
		// Does this file even have an extension to it?
		String fileName = __path.getFileName().toString();
		int ld = fileName.lastIndexOf('.');
		if (ld < 0)
			return fileName;
		
		// Otherwise extract it
		return fileName.substring(0, ld);
	}
	
	/**
	 * Gets the extension from the given path.
	 * 
	 * @param __path The path to get from.
	 * @return The file extension.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/09/06
	 */
	public static String getExtension(Path __path)
		throws NullPointerException
	{
		if (__path == null)
			throw new NullPointerException("NARG");
		
		// Does this file even have an extension to it?
		String fileName = __path.getFileName().toString();
		int ld = fileName.lastIndexOf('.');
		if (ld < 0)
			return "";
		
		// Otherwise extract it
		return fileName.substring(ld + 1);
	}
	
	/**
	 * Returns the task that creates the JAR.
	 * 
	 * @param __project The project to get from.
	 * @param __sourceSet The source set.
	 * @return The jar task used, or {@code null} if not found.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/07
	 */
	public static Jar jarTask(Project __project, String __sourceSet)
		throws NullPointerException
	{
		if (__project == null || __sourceSet == null)
			throw new NullPointerException("NARG");
		
		switch (__sourceSet)
		{
			case SourceSet.MAIN_SOURCE_SET_NAME:
				return (Jar)__project.getTasks().getByName("jar");
			
			case SourceSet.TEST_SOURCE_SET_NAME:
				return (Jar)__project.getTasks().getByName("testJar");
			
			case VMHelpers.TEST_FIXTURES_SOURCE_SET_NAME:
				return (Jar)__project.getTasks()
					.getByName("testFixturesJar");
			
			default:
				throw new IllegalStateException("Unknown sourceSet: " +
					__sourceSet);
		}
	}
	
	/**
	 * Returns the main class to execute.
	 *
	 * @param __cfg The configuration.
	 * @param __midlet The MIDlet to be run.
	 * @return The main class.
	 * @throws NullPointerException If {@code __cfg} is {@code null}.
	 * @since 2020/03/06
	 */
	public static String mainClass(SquirrelJMEPluginConfiguration __cfg,
		JavaMEMidlet __midlet)
		throws NullPointerException
	{
		if (__cfg == null)
			throw new NullPointerException("NARG");
		
		// We either run the MIDlet or we do not
		return VMHelpers.mainClass(__midlet, __cfg.mainClass);
	}
	
	/**
	 * Determines the main class to use.
	 *
	 * @param __midlet The MIDlet to execute.
	 * @param __mainClass The main class to run.
	 * @return The class for execution.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/07/28
	 */
	public static String mainClass(JavaMEMidlet __midlet, String __mainClass)
		throws NullPointerException
	{
		if (__midlet == null && __mainClass == null)
			throw new NullPointerException("No main class specified.");
		
		// We either run the MIDlet or we do not
		if (__midlet != null)
			return UnassistedLaunchEntry.MIDLET_MAIN_CLASS;
		return __mainClass;
	}
	
	/**
	 * Returns the only file with the given extension in the given collection.
	 * 
	 * @param __collection The collection to get.
	 * @param __ext The extension to get from.
	 * @return The only file in the collection with the extension.
	 * @throws IllegalStateException If there are multiple or no files
	 * available.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/02/05
	 */
	public static Path onlyFile(FileCollection __collection, String __ext)
		throws IllegalStateException, NullPointerException
	{
		if (__collection == null || __ext == null)
			throw new NullPointerException("NARG");
		
		// Scan through everything and look for it
		Path result = null;
		for (File file : __collection.getFiles())
		{
			Path path = file.toPath();
			
			if (__ext.equals(VMHelpers.getExtension(path)))
			{
				if (result != null)
					throw new IllegalStateException(
						String.format("Multiple .%s in %s",
							__ext, __collection));
				
				result = path;
			}
		}
		
		// None found?
		if (result == null)
			throw new IllegalStateException(
				String.format("No .%s in %s", __ext, __collection));
		return result;
	}
	
	/**
	 * Returns the optional dependencies for a given project.
	 * 
	 * @param __project The project to get for.
	 * @param __sourceSet The source set to look within.
	 * @return The optional project dependencies or an empty list if unknown.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/09/05
	 */
	public static List<Project> optionalDepends(Project __project,
		String __sourceSet)
		throws NullPointerException
	{
		SquirrelJMEPluginConfiguration config =
			SquirrelJMEPluginConfiguration.configurationOrNull(__project);
		if (config == null)
			return Collections.emptyList();
		
		if (__sourceSet.equals(SourceSet.MAIN_SOURCE_SET_NAME))
			return config.optionalDependencies;
		else if (__sourceSet.equals(SourceSet.TEST_SOURCE_SET_NAME))
			return config.optionalDependenciesTest;
		else if (__sourceSet.equals(VMHelpers.TEST_FIXTURES_SOURCE_SET_NAME))
			return config.optionalDependenciesTestFixtures;
		
		return Collections.emptyList();
	}
	
	/**
	 * Converts the given path to a String using the delimiter.
	 * 
	 * @param __delim The delimiter.
	 * @param __path The path to convert.
	 * @return The path as a string.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/30
	 */
	public static String pathToString(char __delim, Path __path)
		throws NullPointerException
	{
		if (__path == null)
			throw new NullPointerException("NARG");
		
		// If the path is of only a single element (or none) then it is just
		// whatever the string form of the path is
		int n = __path.getNameCount();
		if (n <= 1)
			return __path.toString();
		
		// Build together the string
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < n; i++)
		{
			if (i > 0)
				sb.append(__delim);
			sb.append(__path.getName(i).toString());
		}
		
		return sb.toString();
	}
	
	/**
	 * Returns the directory where profiler snapshots go.
	 * 
	 * @param __project The project to get the cache directory of.
	 * @param __classifier The classifier used.
	 * @return The path provider to the profiler snapshot directory.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/09/06
	 */
	public static Provider<Path> profilerDir(Project __project,
		SourceTargetClassifier __classifier)
		throws NullPointerException
	{
		if (__project == null || __classifier == null)
			throw new NullPointerException("NARG");
		
		return __project.provider(() -> VMHelpers.cacheDir(
			__project, __classifier).get().resolve("nps"));
	}
	
	/**
	 * Returns the internal name via the source set.
	 * 
	 * @param __project The project.
	 * @param __sourceSet The source set.
	 * @return The internal name that is used by SquirrelJME.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/08/07
	 */
	public static String projectInternalNameViaSourceSet(Project __project,
		String __sourceSet)
		throws NullPointerException
	{
		if (__project == null || __sourceSet == null)
			throw new NullPointerException("NARG");
		
		// If main project, just use the normal base name
		if (__sourceSet.equals(SourceSet.MAIN_SOURCE_SET_NAME))
			return __project.getName();
		
		// Otherwise, append the source set
		return String.format("%s-%s", __project.getName(),
			__sourceSet.toLowerCase(Locale.ROOT));
	}
	
	/**
	 * Returns the project classpath.
	 *
	 * @param __project The project.
	 * @return The class path.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/02/29
	 */
	public static Iterable<File> projectRuntimeClasspath(Project __project)
		throws NullPointerException
	{
		if (__project == null)
			throw new NullPointerException("No project specified.");
		
		return __project.getConfigurations().
			getByName("runtimeClasspath").getFiles();
	}
	
	/**
	 * Returns the name of the suite that should be used for the dependency.
	 * 
	 * @param __project The project to get for.
	 * @param __sourceSet The source set used.
	 * @return The suite name that should be used.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/08/07
	 */
	public static String projectSwmNameViaSourceSet(Project __project,
		String __sourceSet)
		throws NullPointerException
	{
		if (__project == null || __sourceSet == null)
			throw new NullPointerException("NARG");
		
		// Need this to get the key
		SquirrelJMEPluginConfiguration config =
			SquirrelJMEPluginConfiguration.configuration(__project);
		
		// Just uses the set name
		if (__sourceSet.equals(SourceSet.MAIN_SOURCE_SET_NAME))
			return config.swmName;
		
		// Otherwise, gets prefixed
		return TaskInitialization.uppercaseFirst(__sourceSet) + " for " +
			config.swmName;
	}
	
	/**
	 * Reads all the bytes from the stream.
	 * 
	 * @param __in The stream to read from.
	 * @return All the read bytes.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/09/07
	 */
	public static byte[] readAll(InputStream __in)
		throws IOException, NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		try (ByteArrayOutputStream out = new ByteArrayOutputStream(4096))
		{
			byte[] buf = new byte[4096];
			for (;;)
			{
				int rc = __in.read(buf);
				
				if (rc < 0)
					return out.toByteArray();
				
				out.write(buf, 0, rc);
			}
		}
	}
	
	/**
	 * Recompresses the given Zip file.
	 *
	 * @param __zip The ZIP to recompress.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/08/08
	 */
	public static void recompressZip(Path __zip)
		throws IOException, NullPointerException
	{
		if (__zip == null)
			throw new NullPointerException("NARG");
		
		// Load in everything for copy
		byte[] result;
		byte[] inZip = Files.readAllBytes(__zip);
		try (InputStream in = new ByteArrayInputStream(inZip);
			 ByteArrayOutputStream out = new ByteArrayOutputStream(
				 inZip.length))
		{
			// Perform recompression
			VMHelpers.copyRecompressZip(in, out);
			
			// Get resultant output
			result = out.toByteArray();
		}
		
		// Replace everything
		Files.write(__zip, result,
			StandardOpenOption.TRUNCATE_EXISTING,
			StandardOpenOption.WRITE,
			StandardOpenOption.CREATE);
	}
	
	/**
	 * Resolves path elements as needed to determine where a file is.
	 * 
	 * @param __in The input to resolve.
	 * @return The path of the given object.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/12/27
	 */
	public static Iterable<Path> resolvePath(Object __in)
		throws NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// Direct file paths
		if (__in instanceof Path)
			return Collections.singleton((Path)__in);
		else if (__in instanceof File)
			return Collections.singleton(((File)__in).toPath());
		
		// A produced value
		else if (__in instanceof Callable)
			try
			{
				return VMHelpers.resolvePath(
					((Callable<?>)__in).call());
			}
			catch (Exception e)
			{
				if (e instanceof RuntimeException)
					throw (RuntimeException)e;
				
				throw new RuntimeException("Could not run Callable.", e);
			}
		
		// A supplied value
		else if (__in instanceof Supplier)
			return VMHelpers.resolvePath(
					((Supplier<?>)__in).get());
		
		// An iterable sequence
		else if (__in instanceof Iterable)
		{
			List<Path> result = new ArrayList<>();
			
			// Process each one
			for (Object obj : (Iterable<?>)__in)
				for (Path sub : VMHelpers.resolvePath(obj))
					result.add(sub);
			
			return result;
		}
		
		// Unknown
		else
			throw new RuntimeException(String.format(
				"Unknown input path type %s", __in.getClass()));			
	}
	
	/**
	 * Resolves tasks from the projects and tasks.
	 * 
	 * @param <T> The class to resolve as.
	 * @param __class The class to resolve as.
	 * @param __project The project to latch onto for lookup.
	 * @param __in The input project and task names.
	 * @return An iterable which has the projects resolved.
	 * @throws NullPointerException On null arguments.
	 */
	public static <T extends Task> Iterable<T> resolveProjectTasks(
		Class<T> __class, Project __project, Iterable<ProjectAndTaskName> __in)
		throws NullPointerException
	{
		if (__project == null || __in == null)
			throw new NullPointerException("NARG");
		
		Collection<T> result = new LinkedList<>();
		
		// Map projects and tasks back into tasks
		for (ProjectAndTaskName depend : __in)
		{
			T task = __class.cast(__project.project(depend.project)
				.getTasks().findByName(depend.task));
			
			if (task != null)
				result.add(task);
		}
		
		return Collections.unmodifiableCollection(result);
	}
	
	/**
	 * Returns the path of the all the JARs which make up the classpath for
	 * running an executable.
	 *
	 * @param __task The task to get for.
	 * @param __classifier The classifier used.
	 * @return An array of paths containing the class path of execution.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/11/21
	 */
	public static Path[] runClassPath(Task __task,
		SourceTargetClassifier __classifier)
	{
		return VMHelpers.runClassPath(__task, __classifier, false);
	}
	
	/**
	 * Returns the path of the all the JARs which make up the classpath for
	 * running an executable.
	 * 
	 * @param __task The task to get for.
	 * @param __classifier The classifier used.
	 * @param __optional use optional dependencies?
	 * @return An array of paths containing the class path of execution.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/09/05
	 */
	public static Path[] runClassPath(Task __task,
		SourceTargetClassifier __classifier, boolean __optional)
		throws NullPointerException
	{
		return VMHelpers.runClassPath(__task.getProject(),
			__classifier, __optional);
	}
	
	/**
	 * Returns the path of the all the JARs which make up the classpath for
	 * running an executable.
	 *
	 * @param __project The project to get for.
	 * @param __classifier The classifier used.
	 * @return An array of paths containing the class path of execution.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/20
	 */
	public static Path[] runClassPath(Project __project,
		SourceTargetClassifier __classifier)
		throws NullPointerException
	{
		return VMHelpers.runClassPath(__project, __classifier, false);
	}
	
	/**
	 * Returns the path of the all the JARs which make up the classpath for
	 * running an executable.
	 * 
	 * @param __project The project to get for.
	 * @param __classifier The classifier used.
	 * @param __optional use optional dependencies?
	 * @return An array of paths containing the class path of execution.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/09/05
	 */
	public static Path[] runClassPath(Project __project,
		SourceTargetClassifier __classifier, boolean __optional)
		throws NullPointerException
	{
		if (__project == null || __classifier == null)
			throw new NullPointerException("NARG");
		
		// Get tasks that are used for dependency running
		Iterable<VMLibraryTask> tasks =
			VMHelpers.<VMLibraryTask>resolveProjectTasks(
				VMLibraryTask.class, __project, VMHelpers
					.runClassTasks(__project, __classifier, __optional));
		
		// Get the outputs of these, as they will be used. Ensure the order is
		// kept otherwise execution may be non-deterministic and could break.
		Set<Path> classPath = new LinkedHashSet<>();
		for (VMLibraryTask vmLib : tasks)
			for (File file : vmLib.getOutputs().getFiles().getFiles())
				classPath.add(file.toPath());
		
		return classPath.toArray(new Path[classPath.size()]);
	}
	
	/**
	 * Returns the internal class path specifier to run the given tasks. 
	 *
	 * @param __project The project.
	 * @param __classifier The classifier used.
	 * @param __optional Include optional JARs?
	 * @return The class path string.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/25
	 */
	public static String runClassPathAsInternalClassPath(
		Project __project, SourceTargetClassifier __classifier,
		boolean __optional)
		throws NullPointerException
	{
		// Get tasks that are used for dependency running
		Iterable<VMLibraryTask> tasks =
			VMHelpers.<VMLibraryTask>resolveProjectTasks(
				VMLibraryTask.class, __project, VMHelpers
					.runClassTasks(__project, __classifier, __optional));
		
		// Build paths from it
		StringBuilder result = new StringBuilder();
		for (VMLibraryTask task : tasks)
		{
			// Path separator before
			if (result.length() > 0)
				result.append(":");
			
			// Use Jar name here
			result.append(VMHelpers.projectInternalNameViaSourceSet(
				task.getProject(), task.getSourceSet()) + ".jar");
		}
		
		return result.toString();
	}
	
	/**
	 * Returns the task dependencies to get outputs from that would be
	 * considered a part of the project's class path used at execution time.
	 *
	 * @param __project The task to get from.
	 * @param __classifier The classifier used.
	 * @return The direct run dependencies for the task.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/15
	 */
	public static Collection<ProjectAndTaskName> runClassTasks(
		Project __project, SourceTargetClassifier __classifier)
		throws NullPointerException
	{
		return VMHelpers.runClassTasks(__project, __classifier,
			false, null);
	}
	
	/**
	 * Returns the task dependencies to get outputs from that would be
	 * considered a part of the project's class path used at execution time.
	 * 
	 * @param __project The task to get from.
	 * @param __classifier The classifier used.
	 * @param __optional Include optional dependencies?
	 * @return The direct run dependencies for the task.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/09/05
	 */
	public static Collection<ProjectAndTaskName> runClassTasks(
		Project __project, SourceTargetClassifier __classifier,
		boolean __optional)
		throws NullPointerException
	{
		return VMHelpers.runClassTasks(__project, __classifier, __optional,
			null);
	}
	
	/**
	 * Returns the task dependencies to get outputs from that would be
	 * considered a part of the project's class path used at execution time.
	 * 
	 * @param __project The task to get from.
	 * @param __classifier The classifier used.
	 * @param __optional Include optional dependencies?
	 * @param __did Projects that have been processed.
	 * @return The direct run dependencies for the task.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/15
	 */
	public static Collection<ProjectAndTaskName> runClassTasks(
		Project __project, SourceTargetClassifier __classifier,
		boolean __optional, Set<ProjectAndTaskName> __did)
		throws NullPointerException
	{
		if (__project == null || __classifier == null)
			throw new NullPointerException("NARG");
		
		// Make sure this is always set
		if (__did == null)
			__did = new HashSet<>();
		
		// If this was already processed, ignore it
		ProjectAndTaskName selfProjectTask = ProjectAndTaskName.of(__project,
			TaskInitialization.task("lib",
				__classifier.withVmByEmulatedJit()));
		if (__did.contains(selfProjectTask))
			return Collections.emptySet();
		
		Set<ProjectAndTaskName> result = new LinkedHashSet<>();
		
		// If we are testing then we depend on the main TAC library, otherwise
		// we will not be able to do any actual testing
		boolean isTesting = __classifier.isTestSourceSet();
		if (isTesting)
		{
			// Depend on TAC
			result.addAll(VMHelpers.runClassTasks(
				__project.findProject(":modules:tac"),
				__classifier.withSourceSet(SourceSet.MAIN_SOURCE_SET_NAME)
					.withVmByEmulatedJit(),
				__optional, __did));
			
			// Depend on our main project as we will be testing it
			result.addAll(VMHelpers.runClassTasks(__project,
				__classifier.withSourceSet(SourceSet.MAIN_SOURCE_SET_NAME)
					.withVmByEmulatedJit(),
				__optional, __did));
		}
		
		// Go through the configurations to yank in the dependencies as needed
		for (String config : (isTesting ? VMHelpers._TEST_CONFIGS :
			VMHelpers._MAIN_CONFIGS))
		{
			// The configuration may be optional
			Configuration foundConfig = __project.getConfigurations()
				.findByName(config);
			if (foundConfig == null)
				continue;
			
			// Handle dependencies
			for (Dependency depend : foundConfig.getDependencies())
			{
				// Only consider projects
				if (!(depend instanceof ProjectDependency))
					continue;
				
				ProjectDependency projectDepend = (ProjectDependency)depend;
				Project sub = projectDepend.getDependencyProject();
				
				// Only consider SquirrelJME projects
				SquirrelJMEPluginConfiguration squirreljmeConf =
					SquirrelJMEPluginConfiguration.configurationOrNull(sub);
				if (squirreljmeConf == null)
					continue;
				
				// Does this depend on test fixtures?
				boolean isTestFixture = false;
				for (Capability capability :
					projectDepend.getRequestedCapabilities())
					if (capability.getName()
						.equals(sub.getName() + "-test-fixtures"))
						isTestFixture = true;
				
				// Otherwise, handle the dependencies
				String targetSourceSet = (isTestFixture ?
					VMHelpers.TEST_FIXTURES_SOURCE_SET_NAME :
					SourceSet.MAIN_SOURCE_SET_NAME);
				Collection<ProjectAndTaskName> selected =
					VMHelpers.runClassTasks(sub,
						__classifier.withSourceSet(targetSourceSet)
							.withVmByEmulatedJit(),
						__optional, __did);
				
				result.addAll(selected);
			}
		}
		
		// Finally, add our own library for usages
		result.add(selfProjectTask);
		
		// Ignore our own project
		__did.add(selfProjectTask);
		
		// Include optional dependencies as well, so that they are actually
		// used accordingly...
		if (__optional)
			for (Project optional : VMHelpers.optionalDepends(__project,
				__classifier.getSourceSet()))
				result.addAll(VMHelpers.runClassTasks(optional,
					__classifier.withSourceSet(SourceSet.MAIN_SOURCE_SET_NAME)
						.withVmByEmulatedJit(),
						true, __did));
		
		// Debug as needed
		__project.getLogger().debug("Run Depends: {}", result);
		
		return Collections.unmodifiableCollection(result);
	}
	
	/**
	 * Returns all the tests to run.
	 * 
	 * @param __project The project to check.
	 * @param __sourceSet The source set to check.
	 * @return All the tests that should be run.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/30
	 */
	public static AvailableTests runningTests(
		Project __project, String __sourceSet)
		throws NullPointerException
	{
		Map<String, CandidateTestFiles> available =
			VMHelpers.availableTests(__project, __sourceSet);
		
		// If specifying a single test to run only specify that
		String singleTest = System.getProperty(
			VMLegacyTestTask.SINGLE_TEST_PROPERTY,
			System.getProperty(VMLegacyTestTask.SINGLE_TEST_PROPERTY_B));
		if (singleTest != null)
		{
			// We need to check every test, since we may have multi-parameters
			// to consider
			Map<String, CandidateTestFiles> singles = new LinkedHashMap<>();
			for (Map.Entry<String, CandidateTestFiles> e : available
				.entrySet())
				if (VMHelpers.__isSingleTest(e.getKey(), singleTest))
					singles.put(e.getKey(), e.getValue());
			
			// If we found at least one test then we can test those, there may
			// be multiple ones due to multi-parameters
			if (!singles.isEmpty())
				return new AvailableTests(
					Collections.unmodifiableMap(singles), true);
			
			// If the test has no matching file, then just ignore it
			throw new IllegalArgumentException(String.format(
				"Could not find test %s, failing.", singleTest));
		}
		
		// Is only valid if there is at least one test
		return new AvailableTests(available, false);
	}
	
	/**
	 * Translates a path to a string.
	 *
	 * @param __name The input string file name.
	 * @return The resultant path.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/09/03
	 */
	public static Path stringToPath(String __name)
		throws NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		return Paths.get("", __name.split(Pattern.quote("/")));
	}
	
	/**
	 * Strips the extension from the path.
	 * 
	 * @param __path The path to strip the extension from.
	 * @return The input path with the extension stripped.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/30
	 */
	public static Path stripExtension(Path __path)
		throws NullPointerException
	{
		if (__path == null)
			throw new NullPointerException("NARG");
		
		// If there is no extension part then nothing has to be done
		String fileName = __path.getFileName().toString();
		int lastDot = fileName.lastIndexOf('.');
		if (lastDot < 0)
			return __path;
		
		// The "renamed" file is in the same parent directory
		return __path.resolveSibling(fileName.substring(0, lastDot));
	}
	
	/**
	 * Returns the directory where test results go.
	 * 
	 * @param __project The project to get the cache directory of.
	 * @param __classifier The classifier used.
	 * @return The path provider to the test result directory.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/09/06
	 */
	public static Provider<Path> testResultXmlDir(Project __project,
		SourceTargetClassifier __classifier)
		throws NullPointerException
	{
		if (__project == null || __classifier == null)
			throw new NullPointerException("NARG");
		
		return __project.provider(() -> VMHelpers.cacheDir(__project,
			__classifier).get().resolve("junit"));
	}
	
	/**
	 * Returns the test result XML file name.
	 * 
	 * @param __testName The test name.
	 * @return The name of the XML to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/09/06
	 */
	public static String testResultXmlName(String __testName)
		throws NullPointerException
	{
		if (__testName == null)
			throw new NullPointerException("NARG");
		
		// When Gradle normally makes a test, it encodes @ to #40.
		return "TEST-" + __testName.replaceAll(Pattern.quote("@"),
			Matcher.quoteReplacement("#40")) + ".xml";
	}
	
	/**
	 * Returns the directory where test results go.
	 * 
	 * @param __project The project to get the cache directory of.
	 * @param __classifier The classifier used.
	 * @return The path provider to the test result directory.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/11/26
	 */
	public static Provider<Path> testResultsCsvDir(Project __project,
		SourceTargetClassifier __classifier)
		throws NullPointerException
	{
		if (__project == null || __classifier == null)
			throw new NullPointerException("NARG");
		
		return __project.provider(() -> VMHelpers.cacheDir(
			__project, __classifier).get().resolve("csv"));
	}
	
	/**
	 * Returns the name for the CSV file.
	 * 
	 * @param __project The project this falls under.
	 * @return The path of the CSV results file.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/11/26
	 */
	public static Path testResultsCsvName(Project __project)
		throws NullPointerException
	{
		if (__project == null)
			throw new NullPointerException("NARG");
		
		return Paths.get("RESULTS-" + __project.getName() + ".csv");
	}
	
	/**
	 * Returns the unassisted launch entry.
	 * 
	 * @param __cfg The configuration to get from.
	 * @param __midlet The MIDlet to load.
	 * @return The unassisted launch entry for the given MIDlet.
	 * @throws NullPointerException If no configuration was specified.
	 * @since 2021/08/22
	 */
	public static UnassistedLaunchEntry unassistedLaunch(
		SquirrelJMEPluginConfiguration __cfg, JavaMEMidlet __midlet)
		throws NullPointerException
	{
		if (__cfg == null)
			throw new NullPointerException("NARG");
		
		// Starting arguments?
		String[] args;
		if (__midlet != null)
			args = new String[]{__midlet.mainClass};
		else
			args = new String[0];
		
		return new UnassistedLaunchEntry(
			VMHelpers.mainClass(__cfg, __midlet),
			args);
	}
	
	/**
	 * Checks if this is the single test to run, depending if multi-parameters
	 * are used or not.
	 * 
	 * @param __key The key to check.
	 * @param __singleTest The single test that was requested.
	 * @return If this is the matching single test.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/10/11
	 */
	private static boolean __isSingleTest(String __key, String __singleTest)
		throws NullPointerException
	{
		if (__key == null || __singleTest == null)
			throw new NullPointerException("NARG");
		
		// If the test does not have a multi-parameter match it exactly.
		// However if we requested a specific multi-parameter then match that
		// as well.
		// Convert slashes to dots as well for binary name usage
		int la = __key.indexOf('@');
		if (la < 0 || __singleTest.indexOf('@') >= 0)
			return __key.equals(__singleTest) ||
				__key.equals(__singleTest.replace('/', '.'));
		
		// Only match by the basename, if multi-parameter assume all of them
		// But also convert all slashes to dots in the event binary names
		// are used.
		return __key.substring(0, la).equals(__singleTest) ||
			__key.substring(0, la).equals(
				__singleTest.replace('/', '.'));
	}
	
	/**
	 * Loads the expected results for a test.
	 * 
	 * @param __testName  The test being parsed.
	 * @param __candidates The candidates available, used for super classes.
	 * @return The expected results.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/10/09
	 */
	private static Manifest __loadExpectedResults(String __testName,
		Map<String, CandidateTestFiles> __candidates)
		throws NullPointerException
	{
		if (__testName == null || __candidates == null)
			throw new NullPointerException("NARG");
		
		// If there is no candidate for this test, always just return a
		// blank manifest to be parsed
		CandidateTestFiles candidate = __candidates.get(__testName);
		if (candidate == null)
			return new Manifest();
		
		// Load information gleaned from the source code
		__SourceInfo__ info;
		try (InputStream in = Files.newInputStream(
			candidate.sourceCode.getAbsolute(), StandardOpenOption.READ))
		{
			String extension = VMHelpers.getExtension(
				candidate.sourceCode.getAbsolute());
			if ("class".equals(extension))
				info = __SourceInfo__.loadClass(in);
			else if ("j".equals(extension))
				info = __SourceInfo__.loadJasmin(in);
			else
				info = __SourceInfo__.loadJava(in);
		}
		catch (IOException e)
		{
			throw new RuntimeException("Could not parse source: " +
				__testName, e);
		}
		
		// Read the current manifest
		Manifest over;
		if (candidate.expectedResult == null)
			over = new Manifest();
		else
			try (InputStream in = Files.newInputStream(
				candidate.expectedResult.getAbsolute(), StandardOpenOption.READ))
			{
				over = new Manifest(in);
			}
			catch (IOException e)
			{
				throw new RuntimeException("Could not parse manifest: " +
					__testName, e);
			}
		
		// If there is no super class there is no need to even try reading or
		// merging any of them
		if (info.superClass == null)
			return over;
		
		Attributes overAttr = over.getMainAttributes();
		
		// Load super class information
		VMHelpers.__loadExpectedResultsSub(overAttr, info.superClass,
			__candidates);
		
		// And interfaces...
		for (String implementsClass : info.implementsClasses)
			VMHelpers.__loadExpectedResultsSub(overAttr, implementsClass,
				__candidates);
		
		return over;
	}
	
	/**
	 * Loads the expected classes from the result.
	 * 
	 * @param __overAttr The attributes to potentially write over.
	 * @param __class The class to check.
	 * @param __candidates The candidates for finding the class.
	 * @since 2022/09/05
	 */
	private static void __loadExpectedResultsSub(Attributes __overAttr,
		String __class, Map<String, CandidateTestFiles> __candidates)
	{
		// Load the manifest that belongs to this class if it is possible
		Manifest under = VMHelpers.__loadExpectedResults(__class,
			__candidates);
		
		// Add the underlying manifest, provided it does not replace anything
		// on the higher level
		for (Map.Entry<Object, Object> e : under.getMainAttributes()
			.entrySet())
			__overAttr.putIfAbsent(e.getKey(), e.getValue());
	}
	
	/**
	 * Parses multi-parameters from test results.
	 * 
	 * @param __expected The expected results to parse.
	 * @return The multi-parameters, if any.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/10/09
	 */
	private static Collection<String> __parseMultiParams(
		Manifest __expected)
		throws NullPointerException
	{
		if (__expected == null)
			throw new NullPointerException("NARG");
		
		// Are there hyperparameters?
		String hyperIn = __expected.getMainAttributes()
			.getValue(VMHelpers._HYPER_PARAMETERS_KEY);
		
		// Get the possible parameter values
		String multiIn = __expected.getMainAttributes()
			.getValue(VMHelpers._MULTI_PARAMETERS_KEY);
			
		// Do nothing if there is neither
		if (hyperIn == null && multiIn == null)
			return null;
		
		// Split fields,
		String[] hyperSplit = (hyperIn == null ? new String[0] :
			hyperIn.trim().split("[ \t]"));
		String[] multiSplit = (multiIn == null ? new String[0] :
			multiIn.trim().split("[ \t]"));
		
		// Has both parameters
		if (hyperSplit.length > 0 && multiSplit.length > 0)
		{
			List<String> result = new ArrayList<>(
				hyperSplit.length * multiSplit.length);
			
			// Combine every possible variant of this
			for (String hyper : hyperSplit)
				for (String multi : multiSplit)
					result.add(hyper + "@" + multi);
			
			return result;
		}
		
		// Has only one
		else if (hyperSplit.length > 0)
			return Arrays.asList(hyperSplit);
		else if (multiSplit.length > 0)
			return Arrays.asList(multiSplit);
		
		// Has nothing
		return null;
	}
}
