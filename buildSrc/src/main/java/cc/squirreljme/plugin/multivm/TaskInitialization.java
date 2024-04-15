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
import cc.squirreljme.plugin.general.UpdateFossilJavaDoc;
import cc.squirreljme.plugin.multivm.ident.SourceTargetClassifier;
import cc.squirreljme.plugin.tasks.AdditionalManifestPropertiesTask;
import cc.squirreljme.plugin.tasks.GenerateTestsListTask;
import cc.squirreljme.plugin.tasks.JasminAssembleTask;
import cc.squirreljme.plugin.tasks.MimeDecodeResourcesTask;
import cc.squirreljme.plugin.tasks.TestsJarTask;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.gradle.api.GradleException;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.UnknownDomainObjectException;
import org.gradle.api.file.FileCollection;
import org.gradle.api.internal.AbstractTask;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.Delete;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.api.tasks.bundling.AbstractArchiveTask;
import org.gradle.api.tasks.javadoc.Javadoc;
import org.gradle.api.tasks.testing.Test;
import org.gradle.external.javadoc.CoreJavadocOptions;
import org.gradle.external.javadoc.MinimalJavadocOptions;
import org.gradle.jvm.tasks.Jar;

/**
 * This is used to initialize the Gradle tasks for projects accordingly.
 *
 * @since 2020/08/07
 */
public final class TaskInitialization
{
	/** Use legacy testing? */
	public static final boolean LEGACY_TEST_FRAMEWORK =
		Boolean.getBoolean("squirreljme.test.legacy");
	
	/** Source sets that are used. */
	private static final String[] _SOURCE_SETS =
		new String[]{SourceSet.MAIN_SOURCE_SET_NAME,
			VMHelpers.TEST_FIXTURES_SOURCE_SET_NAME,
			SourceSet.TEST_SOURCE_SET_NAME};
	
	/**
	 * Not used.
	 * 
	 * @since 2020/08/07
	 */
	private TaskInitialization()
	{
	}
	
	/**
	 * Initializes the project for the tasks and such 
	 * 
	 * @param __project The project to initialize for.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/07
	 */
	public static void initialize(Project __project)
		throws NullPointerException
	{
		if (__project == null)
			throw new NullPointerException("NARG");
		
		// Disable the test task, since it is non-functional
		// However this might fail
		try
		{
			__project.getTasks().replace("test", DefunctTestTask.class);
		}
		catch (IllegalStateException|GradleException e)
		{
			__project.getLogger().debug("Could not defunct test task.", e);
		}
		
		Task check = __project.getTasks().getByName("check");
		for (Iterator<Object> it = check.getDependsOn().iterator();
			it.hasNext();)
		{
			// Get the root item, if a provider of one
			Object item = it.next();
			if (item instanceof Provider)
				item = ((Provider<?>)item).get();
			
			// Only consider tasks
			if (!(item instanceof Task))
				continue;
			
			// Remove the test task, since we do not want it to run here
			if ("test".equals(((Task)item).getName()))
				it.remove();
		}
		
		// Initialize or both main classes and such
		for (String sourceSet : TaskInitialization._SOURCE_SETS)
			TaskInitialization.initialize(__project, sourceSet);
	}
	
	/**
	 * Initializes the source set for the given project.
	 * 
	 * @param __project The project to initialize for.
	 * @param __sourceSet The source set to be initialized.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/07
	 */
	public static void initialize(Project __project, String __sourceSet)
		throws NullPointerException
	{
		if (__project == null || __sourceSet == null)
			throw new NullPointerException("NARG");
		
		// Used for Jasmin and Mime Decoding tasks
		Task processResources = __project.getTasks()
			.getByName(TaskInitialization.task(
				"process", __sourceSet, "resources"));
		
		// Make sure process resources is run after any cleans so output is
		// not destroyed after it is processed
		Task clean = __project.getTasks().getByName("clean");
		processResources.mustRunAfter(clean);
			
		// Generate the list of tests that are available (only tests)
		if (__sourceSet.equals(SourceSet.TEST_SOURCE_SET_NAME))
			__project.getTasks().create("generateTestsList",
				GenerateTestsListTask.class,
				processResources, clean);
		
		// The current Jar Task
		String jarTaskName = TaskInitialization.task(
			"", __sourceSet, "jar");
		Jar jarTask = (Jar)__project.getTasks()
			.findByName(jarTaskName);
		
		// We need to know how to make the classes
		Task classes = __project.getTasks()
			.getByName(TaskInitialization.task(
				"", __sourceSet, "classes"));
		
		// If it does not exist, create it
		if (jarTask == null)
			jarTask = (Jar)__project.getTasks()
				.create("testJar", TestsJarTask.class,
				classes, processResources);
		
		// Correct name of the Jar archive
		String normalJarName;
		if (__sourceSet.equals(SourceSet.MAIN_SOURCE_SET_NAME))
			normalJarName = __project.getName() + ".jar";
		else
			normalJarName = __project.getName() + "-" + __sourceSet + ".jar";
		jarTask.getArchiveFileName().set(normalJarName);
		
		// Jasmin assembling
		__project.getTasks().create(TaskInitialization.task(
				"assemble", __sourceSet, "jasmin"),
			JasminAssembleTask.class,
			__sourceSet, processResources, clean);
		
		// Mime Decoding
		__project.getTasks().create(TaskInitialization.task(
				"mimeDecode", __sourceSet, "resources"),
			MimeDecodeResourcesTask.class,
			__sourceSet, processResources, clean);
			
		// Add SquirrelJME properties to the manifest
		__project.getTasks().create(TaskInitialization.task(
				"additional", __sourceSet, "jarProperties"),
			AdditionalManifestPropertiesTask.class,
			jarTask, processResources, __sourceSet, clean);
		
		// Initialize for each VM
		for (VMType vmType : VMType.values())
			TaskInitialization.initialize(__project, __sourceSet, vmType);
	}
	
	/**
	 * Initializes the virtual machine for the given project's sourceset.
	 * 
	 * @param __project The project to initialize for.
	 * @param __sourceSet The source set.
	 * @param __vmType The virtual machine type.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/07
	 */
	public static void initialize(Project __project, String __sourceSet,
		VMSpecifier __vmType)
		throws NullPointerException
	{
		if (__project == null || __sourceSet == null || __vmType == null)
			throw new NullPointerException("NARG");
		
		for (ClutterLevel clutterLevel : ClutterLevel.values())
		{
			// Only allow debug targets for this?
			if (__vmType.allowOnlyDebug() && !clutterLevel.isDebug())
				continue;
			
			// Initialize tasks
			for (BangletVariant variant : __vmType.banglets())
				TaskInitialization.initialize(__project,
					new SourceTargetClassifier(__sourceSet, __vmType, variant,
						clutterLevel));
		}
	}
	
	/**
	 * Initializes the virtual machine for the given project's sourceset.
	 * 
	 * @param __project The project to initialize for.
	 * @param __classifier The classifier used.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/07
	 */
	public static void initialize(Project __project,
		SourceTargetClassifier __classifier)
		throws NullPointerException
	{
		if (__project == null || __classifier == null)
			throw new NullPointerException("NARG");
		
		// Everything will be working on these tasks
		TaskContainer tasks = __project.getTasks();
		
		// Make sure the source set exists first
		try
		{
			__project.getConvention().getPlugin(JavaPluginConvention.class)
				.getSourceSets().getByName(__classifier.getSourceSet());
		}
		catch (UnknownDomainObjectException e)
		{
			__project.getLogger().debug(String.format(
				"Could not find sourceSet %s in project %s (available: %s)",
				__classifier.getSourceSet(), __project.getPath(),
				new ArrayList<>(
					__project.getConvention()
					.getPlugin(JavaPluginConvention.class).getSourceSets())),
				e);
		}
		
		// The source library task depends on whether we are debugging or not
		Jar sourceJar = VMHelpers.jarTask(__project,
			__classifier.getSourceSet());
		AbstractTask usedSourceJar;
		
		// If we are debugging, then we keep everything... otherwise we just
		// strip everything out that we can to minimize the size as much as
		// possible...
		// Or it is just disabled completely
		if (__classifier.getTargetClassifier().getClutterLevel().isDebug())
			usedSourceJar = sourceJar;
		
		// Otherwise set up a new task to compact the Jar and remove any
		// debugging information and unneeded symbols for execution.
		else
		{
			// Look for that task first
			String checkName = TaskInitialization.task("compactLib",
				__classifier.getSourceSet());
			AbstractTask maybe = (AbstractTask)tasks.findByName(checkName);
			
			// If it exists, use that one
			if (maybe != null)
				usedSourceJar = maybe;
			
			// Otherwise, create it
			else
			{
				usedSourceJar = tasks.create(checkName,
					VMCompactLibraryTask.class, __classifier.getSourceSet(),
					sourceJar);
			}
		}
		
		// Library that needs to be constructed so execution happens properly
		VMLibraryTask libTask = tasks.create(
			TaskInitialization.task("lib", __classifier),
			VMLibraryTask.class, __classifier, usedSourceJar);
		
		// Is dumping available?
		if (__classifier.getVmType().hasDumping())
			tasks.create(
				TaskInitialization.task("dump", __classifier),
				VMDumpLibraryTask.class, __classifier, libTask);
		
		// Emulator targets, which run the VM with the resultant code
		if (__classifier.getVmType().hasEmulator())
		{
			// Running the target
			if (__classifier.isMainSourceSet())
			{
				tasks.create(
					TaskInitialization.task("run", __classifier),
					VMRunTask.class, __classifier, libTask);
			}
			
			// Testing the target
			else if (__classifier.isTestSourceSet())
			{
				Task vmTest;
				String taskName = TaskInitialization.task("test",
					__classifier);
				
				// Creating the legacy or modern test task? Using the modern
				// one is recommended if using IntelliJ or otherwise...
				if (TaskInitialization.LEGACY_TEST_FRAMEWORK)
					vmTest = tasks.create(taskName, VMLegacyTestTask.class,
						__classifier, libTask);
				else
					vmTest = tasks.create(taskName, VMModernTestTask.class,
						__classifier, libTask);
				
				// Since there is a release and debug variant, have the base
				// test refer to both of these
				String bothName = TaskInitialization.task("test",
					__classifier.getSourceSet(), __classifier.getVmType(),
					__classifier.getBangletVariant(), null);
				
				// If the task is missing, create it
				Test bothTest = (Test)__project.getTasks()
					.findByName(bothName);
				if (bothTest == null)
				{
					// Create a test task, so IDEs like IntelliJ can pick this
					// up despite there being no actual tests that exist
					bothTest = __project.getTasks().create(bothName,
						Test.class);
					
					// Setup description of these
					bothTest.setGroup("squirreljme");
					
					// Make sure the description makes sense
					if (__classifier.getVmType().allowOnlyDebug())
						bothTest.setDescription(
							String.format("Alias for %s.", taskName));
					else
						bothTest.setDescription(
							String.format("Runs both test tasks %s and %s.",
								taskName, TaskInitialization.task(
									"test",
									__classifier.withClutterLevel(
										__classifier.getTargetClassifier()
											.getClutterLevel().opposite()))));
					
					// Gradle will think these are JUnit tests and then fail
					// so exclude everything
					bothTest.setScanForTestClasses(false);
					bothTest.include();
					bothTest.exclude("**");
				}
				
				// Add to the both task as a dependency
				bothTest.dependsOn(vmTest);
				
				// Make the standard test task depend on these two VM tasks
				// so that way if it is run, both are run accordingly
				if (__classifier.getVmType().isGoldTest())
				{
					Test test = (Test)__project.getTasks()
						.getByName("test");
					
					// Test needs this
					test.dependsOn(vmTest);
					
					// Gradle will think these are JUnit tests and then fail
					// so exclude everything
					test.setScanForTestClasses(false);
					test.include();
					test.exclude("**");
				}
			}
		}
	}
	
	/**
	 * Initializes the task which puts the entire Markdown documentation into
	 * Fossil's versioned space.
	 * 
	 * @param __project The project to initialize for.
	 * @param __javaDoc The JavaDoc Task.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/08/29
	 */
	public static void initializeFossilMarkdownTask(Project __project,
		Javadoc __javaDoc)
		throws NullPointerException
	{
		if (__project == null || __javaDoc == null)
			throw new NullPointerException("NARG");
		
		// Find existing task, create if it does not yet exist
		UpdateFossilJavaDoc task = (UpdateFossilJavaDoc)__project.getTasks()
			.findByName("updateFossilJavaDoc");
		if (task == null)
			task = (UpdateFossilJavaDoc)__project.getTasks()
				.create("updateFossilJavaDoc", UpdateFossilJavaDoc.class);
		
		// Add dependency to the task for later usage
		task.dependsOn(__javaDoc);
	}
	
	/**
	 * Initializes the full-suite run which selects every API and library
	 * module available, along with allowing an external 3rd library classpath
	 * launching.
	 * 
	 * @param __project The root project.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/10/17
	 */
	public static void initializeFullSuiteTask(Project __project)
		throws NullPointerException
	{
		if (__project == null)
			throw new NullPointerException("NARG");
		
		for (ClutterLevel clutterLevel : ClutterLevel.values())
			for (VMType vmType : VMType.values())
				for (BangletVariant variant : vmType.banglets())
					for (String sourceSet : TaskInitialization._SOURCE_SETS)
						TaskInitialization.initializeFullSuiteTask(__project,
							new SourceTargetClassifier(sourceSet, vmType,
								variant, clutterLevel));
	}
	
	/**
	 * Initializes the full-suite run which selects every API and library
	 * module available, along with allowing an external 3rd library classpath
	 * launching.
	 * 
	 * @param __project The root project.
	 * @param __classifier The classifier used.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/10/17
	 */
	private static void initializeFullSuiteTask(Project __project,
		SourceTargetClassifier __classifier)
		throws NullPointerException
	{
		if (__project == null || __classifier == null)
			throw new NullPointerException("NARG");
		
		// Is this a single source set ROM?
		VMSpecifier vmType = __classifier.getVmType();
		boolean isSingleSourceSetRom = vmType.isSingleSourceSetRom(
			__classifier.getBangletVariant());
		
		// Do not run if there is no emulator
		if (!__classifier.getVmType().hasEmulator())
			return;
		
		// Standard run everything as one, only allow main and test source
		// sets to be a candidate for full
		if (!__classifier.isMainSourceSet() && !__classifier.isTestSourceSet())
			return;
		
		// If this is a debug only target and the requested clutter level is
		// not debugging, then do not make such a task
		if (__classifier.getVmType().allowOnlyDebug() &&
			!__classifier.getTargetClassifier().getClutterLevel().isDebug())
			return;
		
		// Create task
		__project.getTasks().create(
			TaskInitialization.task("full", __classifier),
			VMFullSuite.class, __classifier);
	}
	
	/**
	 * Late initialization step.
	 * 
	 * @param __project The project to initialize.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/08/29
	 */
	public static void lateInitialize(Project __project)
		throws NullPointerException
	{
		if (__project == null)
			throw new NullPointerException("NARG");
		
		// Configuration, for modifiers
		SquirrelJMEPluginConfiguration squirreljmeConf =
			SquirrelJMEPluginConfiguration.configuration(__project);
			
		// We need to evaluate the Doclet project first since we need
		// the Jar task, which if we use normal evaluation does not exist
		// yet...
		Project docletProject =
			__project.evaluationDependsOn(":tools:markdown-javadoc");
		
		// Setup task for creating JavaDoc
		Javadoc mdJavaDoc = __project.getTasks()
			.create("markdownJavaDoc", Javadoc.class);
		
		// What does this do?
		mdJavaDoc.setGroup("squirreljme");
		mdJavaDoc.setDescription("Generates Markdown JavaDoc.");
		
		// This has a hard dependency and we do not want to get out of order
		mdJavaDoc.mustRunAfter(
			docletProject.getTasks().getByName("clean"),
			docletProject.getTasks().getByName("jar"));
		
		// We are using a specific classpath, in this case it is just
		// SpringCoat's libraries for runtime
		SourceTargetClassifier classifier = new SourceTargetClassifier(
			SourceSet.MAIN_SOURCE_SET_NAME, VMType.SPRINGCOAT,
			BangletVariant.NONE, ClutterLevel.DEBUG);
		FileCollection useClassPath = __project.files(
			(Object[])VMHelpers.runClassPath(__project,
				classifier));
				
		// We need to know how to make the classes
		Task classes = __project.getTasks().getByName(TaskInitialization.task(
			"", SourceSet.MAIN_SOURCE_SET_NAME, "classes"));
		
		// Where do we find the JAR?
		Provider<Task> jarProvider = __project.provider(() ->
			__project.getRootProject().findProject(
			":tools:markdown-javadoc").getTasks().getByName("shadowJar"));
		
		// SpringCoat related tasks
		Provider<Iterable<Task>> springCoatTasks = __project.provider(() ->
			VMHelpers.<Task>resolveProjectTasks(
				Task.class, __project, VMHelpers.runClassTasks(__project,
				classifier)));
		
		// Classes need to compile first, and we need the doclet Jar too
		// However we do not know it exists yet
		mdJavaDoc.dependsOn(classes);
		mdJavaDoc.dependsOn(springCoatTasks);
		mdJavaDoc.dependsOn(jarProvider);
		
		// We also need to depend on other markdownJavaDoc tasks of our
		// dependencies... this is so we can do cross-project links with
		// our JavaDoc generation
		mdJavaDoc.dependsOn(__project.provider(() -> {
				Map<String, Javadoc> result = new LinkedHashMap<>();
				
				for (Task task : springCoatTasks.get()) {
					// Ignore our own project, otherwise recursive!
					Project subProject = task.getProject();
					if (subProject.equals(__project))
						continue;
					
					// Only refer to projects once
					String subName = subProject.getPath();
					if (!result.containsKey(subName))
						result.put(subName,
							(Javadoc)subProject.getTasks()
							.getByName("markdownJavaDoc"));
				}
				
				return result.values();
			}));
		
		// Where are the sources?
		SourceSet sourceSet = __project.getConvention().getPlugin(
			JavaPluginConvention.class).getSourceSets().getByName(
			SourceSet.MAIN_SOURCE_SET_NAME);
		
		// Configure the JavaDoc task
		mdJavaDoc.setDestinationDir(TaskInitialization.markdownPath(__project)
			.toFile());
		mdJavaDoc.source(sourceSet.getAllJava());
		mdJavaDoc.setClasspath(useClassPath);
		mdJavaDoc.setTitle(squirreljmeConf.swmName);
		
		// Determine the paths where all markdown JavaDocs are being stored
		List<Path> projectPaths = new ArrayList<>();
		for (Project subProject : __project.getRootProject().getAllprojects())
		{
			// Only consider SquirrelJME projects
			if (null ==
				SquirrelJMEPluginConfiguration.configurationOrNull(subProject))
				continue;
			
			// We just store this here, since we do not know what exists
			// and does not exist
			projectPaths.add(TaskInitialization.markdownPath(subProject));
		}
		
		// Setup more advanced options
		mdJavaDoc.options((MinimalJavadocOptions __options) ->
				{
					// We need to set the bootstrap class path otherwise
					// we will get derivations from whatever JDK the system
					// is using, and we definitely do not want that.
					__options.bootClasspath(useClassPath.getFiles()
						.toArray(new File[0]));
				
					// We get this by forcing evaluation
					Task mdJavaDocletJar = jarProvider.get();
					
					// Set other options
					__options.showFromProtected();
					__options.encoding("utf-8");
					__options.locale("en_US");
					__options.docletpath(mdJavaDocletJar.getOutputs()
						.getFiles().getSingleFile());
					__options.doclet(
						"cc.squirreljme.doclet.MarkdownDoclet");
					
					// Used for completion counting
					if (__options instanceof CoreJavadocOptions)
					{
						CoreJavadocOptions coreOptions =
							(CoreJavadocOptions)__options;
						
						// Where to find our own sources (for TODOs)
						coreOptions.addStringOption(
							"squirreljmejavasources",
							sourceSet.getAllJava().getAsPath());
						
						// Directories to all the other markdown JavaDocs
						coreOptions.addStringOption(
							"squirreljmeprojectmjd",
							VMHelpers.classpathAsString(projectPaths));
						
						// The name of the project
						coreOptions.addStringOption(
							"squirreljmeproject",
							__project.getName());
					}
				});
			
		// Add markdown task
		TaskInitialization.initializeFossilMarkdownTask(
			__project.getRootProject(), mdJavaDoc);
	}
	
	/**
	 * Returns the path to the markdown JavaDoc for a project.
	 * 
	 * @param __project The project to get for.
	 * @return The path to the project's markdown JavaDoc.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/08/29
	 */
	public static Path markdownPath(Project __project)
		throws NullPointerException
	{
		if (__project == null)
			throw new NullPointerException("NARG");
		
		return __project.getBuildDir().toPath().resolve("markdownJavaDoc");
	}
	
	/**
	 * Initializes ROM tasks for the given base project.
	 * 
	 * @param __project The root project.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/23
	 */
	public static void romTasks(Project __project)
		throws NullPointerException
	{
		if (__project == null)
			throw new NullPointerException("NARG");
			
		// Initialize or both main classes and such
		for (VMType vmType : VMType.values())
		{
			// Sequential clean storage?
			List<Task> sequentialClean = new ArrayList<>();
			
			// Process for each possible combination
			for (ClutterLevel clutterLevel : ClutterLevel.values())
				for (String sourceSet : TaskInitialization._SOURCE_SETS)
					for (BangletVariant variant : vmType.banglets())
						TaskInitialization.romTasks(__project,
							new SourceTargetClassifier(sourceSet, vmType,
								variant, clutterLevel), sequentialClean);
		}
	}
	
	/**
	 * Initializes ROM tasks for the given base project.
	 * 
	 * @param __project The root project.
	 * @param __classifier The classifier used.
	 * @param __sequentialClean Sequential clean list.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/23
	 */
	private static void romTasks(Project __project,
		SourceTargetClassifier __classifier, List<Task> __sequentialClean)
		throws NullPointerException
	{
		if (__project == null || __classifier == null ||
			__sequentialClean == null)
			throw new NullPointerException("NARG");
			
		// Everything will be working on these tasks
		TaskContainer tasks = __project.getTasks();
		
		// Determine if this is a single source set ROM
		VMSpecifier vmType = __classifier.getVmType();
		boolean isSingleSourceSetRom = vmType.isSingleSourceSetRom(
			__classifier.getBangletVariant());
		
		// Does the VM utilize ROMs?
		// Test fixtures are just for testing, so there is no test fixtures
		// ROM variant... unless we are a single source set ROM variant
		if (vmType.hasRom(__classifier.getBangletVariant()) &&
			(isSingleSourceSetRom || !__classifier.isTestFixturesSourceSet()))
		{
			String baseName = TaskInitialization.task("rom",
				__classifier);
			VMRomTask rom = tasks.create(baseName, VMRomTask.class,
				__classifier);
			
			// Which native ports are supported?
			for (NativePortSupport nativePort :
				__classifier.getVmType().hasNativePortSupport())
			{
				Task nativeTask;
				String taskName;
				switch (nativePort)
				{
					case RATUFACOAT:
						taskName = baseName + "RatufaCoat";
						nativeTask = tasks.create(
							taskName,
							RatufaCoatBuiltInTask.class, __classifier, rom);
						break;
					
					case NANOCOAT:
						// Create task
						taskName = baseName + "NanoCoat";
						nativeTask = tasks.create(
							taskName,
							NanoCoatBuiltInTask.class,
							__classifier, rom);
						break;
						
					default:
						throw new Error(nativePort.toString());
				}
				
				// Setup cleaning task
				Task cleanTask = nativePort.cleanTask(nativeTask,
					__classifier);
				
				// Clean should call these accordingly
				__project.afterEvaluate((__p) ->
					cleanTask.getProject().getRootProject().getTasks()
						.getByName("clean").dependsOn(cleanTask));
				
				// Does clean have to be done sequentially and not in
				// parallel? This means the clean task is quite complicated
				// and not easily determined, probably because it looks at
				// all the ROM files.
				if (isSingleSourceSetRom || nativePort.isSequentialClean())
				{
					// Have this task run after the previous clean task that
					// was generated
					if (!__sequentialClean.isEmpty())
						cleanTask.mustRunAfter(__sequentialClean.get(
							__sequentialClean.size() - 1));
					
					// Add self to the sequential clean list
					__sequentialClean.add(cleanTask);
				}
				
				// Clean, if it occurs, must happen before
				nativeTask.mustRunAfter(cleanTask);
			}
		}
	}
	
	/**
	 * Builds a name for a task, without the virtual machine type.
	 * 
	 * @param __name The task name.
	 * @param __sourceSet The source set for the task base.
	 * @return A string representing the task.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/12/19
	 */
	public static String task(String __name, String __sourceSet)
		throws NullPointerException
	{
		return TaskInitialization.task(__name, __sourceSet, "");
	}
	
	/**
	 * Builds a name for a task, without the virtual machine type.
	 * 
	 * @param __name The task name.
	 * @param __sourceSet The source set for the task base.
	 * @param __suffix The task suffix.
	 * @return A string representing the task.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/08/07
	 */
	public static String task(String __name, String __sourceSet,
		String __suffix)
		throws NullPointerException
	{
		if (__name == null || __sourceSet == null || __suffix == null)
			throw new NullPointerException("NARG");
		
		// We need to later determine how the suffix works
		String baseName;
		
		// If this is the main source set, never include the source set as
		// it becomes implied. Additionally, if the name and the source set
		// are the same, reduce the confusion so there is no "testTestHosted".
		if (__sourceSet.equals(SourceSet.MAIN_SOURCE_SET_NAME) ||
			__sourceSet.equals(__name) || __sourceSet.isEmpty())
			baseName = __name;
		
		// Otherwise, include it
		else
		{
			// If just the source set, then just keep that lowercase
			if (__name.isEmpty())
				baseName = __sourceSet;
			else
				baseName = __name +
					TaskInitialization.uppercaseFirst(__sourceSet);
		}
		
		// If there is no suffix, just return the base
		if (__suffix.isEmpty())
			return baseName;
		
		// If there is no base, just return the suffix
		if (baseName.isEmpty())
			return __suffix;
		
		// Otherwise, perform needed capitalization
		// "additionalJarProperties" or "additionalTestJarProperties"
		return baseName + TaskInitialization.uppercaseFirst(__suffix);
	}
	
	/**
	 * Builds a name for a task.
	 * 
	 * @param __name The task name.
	 * @param __sourceSet The source set for the task base.
	 * @param __vmType The type of virtual machine used.
	 * @return A string representing the task.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/07
	 */
	public static String task(String __name, String __sourceSet,
		VMSpecifier __vmType)
		throws NullPointerException
	{
		return TaskInitialization.task(__name, __sourceSet, __vmType,
			BangletVariant.NONE, ClutterLevel.DEBUG);
	}
	
	/**
	 * Initializes the task name.
	 * 
	 * @param __name The name of the task.
	 * @param __classifier The classifier for the target.
	 * @return The task name.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/10/01
	 */
	public static String task(String __name,
		SourceTargetClassifier __classifier)
		throws NullPointerException
	{
		if (__name == null || __classifier == null)
			throw new NullPointerException("NARG");
		
		return TaskInitialization.task(__name,
			__classifier.getSourceSet(),
			__classifier.getTargetClassifier().getVmType(),
			__classifier.getTargetClassifier().getBangletVariant(),
			__classifier.getTargetClassifier().getClutterLevel());
	}
	
	/**
	 * Builds a name for a task.
	 * 
	 * @param __name The task name.
	 * @param __sourceSet The source set for the task base.
	 * @param __vmType The type of virtual machine used.
	 * @param __variant The banglet variant.
	 * @param __clutterLevel Release or debug, may be {@code null}.
	 * @return A string representing the task.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/10/01
	 */
	public static String task(String __name, String __sourceSet,
		VMSpecifier __vmType, BangletVariant __variant,
		ClutterLevel __clutterLevel)
		throws NullPointerException
	{
		if (__name == null || __sourceSet == null || __vmType == null ||
			__variant == null)
			throw new NullPointerException("NARG");
		
		return TaskInitialization.task(__name, __sourceSet) +
			__vmType.vmName(VMNameFormat.PROPER_NOUN) +
			TaskInitialization.uppercaseFirst(__variant.properNoun) +
			(__clutterLevel == null ? "" :
				TaskInitialization.uppercaseFirst(__clutterLevel.toString()));
	}
	
	/**
	 * Uppercases the first character of a string.
	 * 
	 * @param __input The input string.
	 * @return The string with the first character uppercased.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/08/07
	 */
	public static String uppercaseFirst(String __input)
		throws NullPointerException
	{
		if (__input == null)
			throw new NullPointerException("NARG");
		
		if (__input.isEmpty())
			return __input;
		
		return Character.toUpperCase(__input.charAt(0)) +
			__input.substring(1);
	}
}
