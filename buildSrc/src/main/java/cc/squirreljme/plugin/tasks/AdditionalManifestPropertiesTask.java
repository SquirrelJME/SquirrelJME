package cc.squirreljme.plugin.tasks;

import cc.squirreljme.plugin.swm.JavaMEMidlet;
import cc.squirreljme.plugin.swm.JavaMEMidletType;
import cc.squirreljme.plugin.SquirrelJMEPluginConfiguration;
import cc.squirreljme.plugin.swm.SuiteDependency;
import cc.squirreljme.plugin.swm.SuiteDependencyLevel;
import cc.squirreljme.plugin.swm.SuiteDependencyType;
import cc.squirreljme.plugin.swm.SuiteName;
import cc.squirreljme.plugin.swm.SuiteVendor;
import cc.squirreljme.plugin.swm.SuiteVersion;
import cc.squirreljme.plugin.swm.SuiteVersionRange;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;
import java.util.jar.Attributes;
import javax.inject.Inject;
import org.gradle.api.Action;
import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.ModuleDependency;
import org.gradle.api.file.FileCollection;
import org.gradle.api.java.archives.Manifest;
import org.gradle.jvm.tasks.Jar;
import org.gradle.language.jvm.tasks.ProcessResources;

/**
 * Adds additional properties to the manifest file.
 *
 * @since 2020/02/28
 */
public class AdditionalManifestPropertiesTask
	extends DefaultTask
{
	/**
	 * Initializes the task.
	 *
	 * @param __jar The JAR Task.
	 * @param __pr The process resources task.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/02/28
	 */
	@Inject
	public AdditionalManifestPropertiesTask(Jar __jar, ProcessResources __pr)
		throws NullPointerException
	{
		if (__jar == null)
			throw new NullPointerException("No JAR specified.");
		
		// Set details of this task
		this.setGroup("squirreljme");
		this.setDescription("Builds an additional manifest for this project.");
		
		// Configure inputs and outputs
		Project project = this.getProject();
		this.getInputs().files(
			project.provider(project::getBuildFile));
		this.getOutputs().files(
			project.provider(this::__taskOutputAsFileCollection));
		
		// This action creates the actual manifest file
		this.doLast(new Action<Task>()
			{
				@Override
				public void execute(Task __task)
				{
					AdditionalManifestPropertiesTask.this.__doLast(__task);
				}
			});
		
		// When the JAR is created it gets additional manifests from us
		__jar.manifest(new Action<Manifest>()
			{
				@Override
				public void execute(Manifest __manifest)
				{
					__manifest.from(AdditionalManifestPropertiesTask.this
						.__taskOutput().toFile());
				}
			});
		
		// Jar and resources depend on this task
		__jar.dependsOn(this);
		__pr.dependsOn(this);
	}
	
	/**
	 * Performs the task actions.
	 *
	 * @since 2002/02/28
	 */
	private void __doLast(Task __task)
	{
		// Get the project and the config details
		Project project = this.getProject();
		SquirrelJMEPluginConfiguration config =
			SquirrelJMEPluginConfiguration.configuration(project);
		
		// Setup manifest to write into
		java.util.jar.Manifest javaManifest = new java.util.jar.Manifest();
		Attributes attributes = javaManifest.getMainAttributes();
		
		// Set manifest to 1.0
		attributes.put(Attributes.Name.MANIFEST_VERSION, "1.0");
		
		// Project name is used internally for dependency lookup
		attributes.putValue("X-SquirrelJME-InternalProjectName",
			project.getName());
		
		// Generation really depends on the application type
		JavaMEMidletType type = config.swmType;
		
		// Add common keys
		attributes.putValue(type.nameKey(), config.swmName);
		attributes.putValue(type.vendorKey(), config.swmVendor);
		attributes.putValue(type.versionKey(),
			new SuiteVersion(project.getVersion().toString()).toString());
			
		// Application
		if (type == JavaMEMidletType.APPLICATION)
		{
			// Main class of entry?
			if (config.mainClass != null)
				attributes.putValue("Main-Class", config.mainClass);
			
			// Add any defined MIDlets
			int midletId = 1;
			for (JavaMEMidlet midlet : config.midlets)
				attributes.putValue("MIDlet-" + (midletId++),
					midlet.toString());
			
			// Ignored in the launcher?
			if (config.ignoreInLauncher)
				attributes.putValue("X-SquirrelJME-NoLauncher",
					"true");
		}
		
		// Library
		else if (type == JavaMEMidletType.LIBRARY)
		{
		}
		
		// API
		else if (type == JavaMEMidletType.API)
		{
			// Configurations defined?
			if (!config.definedConfigurations.isEmpty())
				attributes.putValue(
					"X-SquirrelJME-DefinedConfigurations",
					AdditionalManifestPropertiesTask
						.__delimate(config.definedConfigurations, ' '));
			
			// Profiles defined?
			if (!config.definedProfiles.isEmpty())
				attributes.putValue(
					"X-SquirrelJME-DefinedProfiles",
					AdditionalManifestPropertiesTask
						.__delimate(config.definedProfiles, ' '));
			
			// Standards defined?
			if (!config.definedStandards.isEmpty())
				attributes.putValue(
					"X-SquirrelJME-DefinedStandards",
					AdditionalManifestPropertiesTask
						.__delimate(config.definedStandards, ' '));
		}
		
		// Find all module dependencies
		Map<String, ModuleDependency> dependencies = new TreeMap<>();
		for (String configurationName : Arrays.<String>asList(
				"api", "implementation"))
		{
			// The configuration might not even exist
			Configuration buildConfig = project.getConfigurations()
				.findByName(configurationName);
			if (buildConfig == null)
				continue;
			
			// Add all module based dependencies
			for (Dependency dependency : buildConfig.getDependencies())
				if (dependency instanceof ModuleDependency)
				{
					ModuleDependency mod = (ModuleDependency)dependency;
					
					dependencies.put(mod.getName(), mod);
				}
		}
		
		// Put in as many dependencies as possible
		int normalDep = 1;
		for (ModuleDependency dependency : dependencies.values())
		{
			// Find the associated project
			Project subProject = project.findProject(dependency.getName());
			if (subProject == null)
			{
				// Does it exist in the parent?
				Project parent = project.getParent();
				if (parent != null)
					subProject = parent.findProject(dependency.getName());
				
				// Really does not exist
				if (subProject == null)
					throw new RuntimeException(String.format(
						"Module %s (%s) does not exist?", dependency,
						dependency.getName()));
			}
			
			// Get the project config
			SquirrelJMEPluginConfiguration subConfig =
				SquirrelJMEPluginConfiguration.configuration(subProject);
			
			// The dependency being constructed (which might be added)
			SuiteDependency suiteDependency = null;
			boolean didDepend = false;
			
			// Nothing can depend on a MIDlet!
			if (subConfig.swmType == JavaMEMidletType.APPLICATION)
				throw new RuntimeException(String.format(
					"Project %s cannot depend on application %s.",
						project.getName(), subProject.getName()));
			
			// This is another library
			else if (subConfig.swmType == JavaMEMidletType.LIBRARY)
				suiteDependency = new SuiteDependency(
					SuiteDependencyType.LIBLET,
					SuiteDependencyLevel.REQUIRED,
					new SuiteName(subConfig.swmName),
					new SuiteVendor(subConfig.swmVendor),
					SuiteVersionRange.exactly(new SuiteVersion(
						subProject.getVersion().toString())));
			
			// Is otherwise an API
			else
			{
				// Configuration specified?
				try
				{
					attributes.putValue("Microedition-Configuration",
						Collections.max(subConfig.definedConfigurations)
							.toString());
					didDepend = true;
				}
				catch (NoSuchElementException e)
				{
					// Ignore
				}
				
				// Profile specified?
				try
				{
					attributes.putValue("Microedition-Profile",
						Collections.max(subConfig.definedProfiles).toString());
					didDepend = true;
				}
				catch (NoSuchElementException e)
				{
					// Ignore
				}
			}
			
			// Unknown, do a generic project dependency
			if (!didDepend && suiteDependency == null)
				suiteDependency = new SuiteDependency(
					SuiteDependencyType.PROPRIETARY,
					SuiteDependencyLevel.REQUIRED,
					new SuiteName("squirreljme.project@" +
						subProject.getName()),
					null,
					null);
			
			// Write out the dependency if one was requested
			if (suiteDependency != null)
				attributes.putValue(type.dependencyKey(normalDep++),
					suiteDependency.toString());
		}
		
		// Write the manifest output
		try (OutputStream out = Files.newOutputStream(this.__taskOutput(),
			StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING,
			StandardOpenOption.WRITE))
		{
			javaManifest.write(out);
			
			// Make sure it is really written!
			out.flush();
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Returns the output manifest file.
	 *
	 * @return The output manifest file.
	 * @since 2020/02/28
	 */
	private Path __taskOutput()
	{
		return this.getProject().getBuildDir().toPath().
			resolve("SQUIRRELJME.MF");
	}
	
	/**
	 * Returns the output manifest file.
	 *
	 * @return The output manifest file.
	 * @since 2020/02/28
	 */
	private FileCollection __taskOutputAsFileCollection()
	{
		return this.getProject().files(this.__taskOutput().toFile());
	}
	
	/**
	 * Turns an iterable into a string with the given delimeter.
	 *
	 * @param __it The iteration to convert.
	 * @param __delim The delimiter.
	 * @return The converted string.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/02/15
	 */
	private static String __delimate(Iterable<?> __it, char __delim)
		throws NullPointerException
	{
		if (__it == null)
			throw new NullPointerException("NARG");
		
		// Build output string
		StringBuilder sb = new StringBuilder();
		for (Object o : __it)
		{
			// Add delimeter?
			if (sb.length() > 0)
				sb.append(__delim);
			
			// Add object
			sb.append(o);
		}
		
		return sb.toString();
	}
}
