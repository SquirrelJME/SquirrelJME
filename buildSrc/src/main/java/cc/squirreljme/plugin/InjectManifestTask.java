package cc.squirreljme.plugin;

import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.java.archives.Attributes;
import org.gradle.jvm.tasks.Jar;

/**
 * Task for injecting the manifest properties.
 *
 * @since 2020/02/26
 */
public class InjectManifestTask
	implements Action<Task>
{
	/** The project to inject into. */
	protected final Project project;
	
	/**
	 * Initializes the task.
	 *
	 * @param __project The project.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/02/27
	 */
	public InjectManifestTask(Project __project)
		throws NullPointerException
	{
		if (__project == null)
			throw new NullPointerException("No project specified.");
		
		this.project = __project;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/02/27
	 */
	@Override
	public void execute(Task __task)
	{
		Project project = this.project;
		
		// Find our config
		SquirrelJMEPluginConfiguration config = project.getExtensions()
			.<SquirrelJMEPluginConfiguration>getByType(
				SquirrelJMEPluginConfiguration.class);
		
		// Obtain the manifest to modify
		Jar jar = (Jar)project.getTasks().getByName("jar");
		Attributes attributes = jar.getManifest().getAttributes();
		
		// Project name is used internally for dependency lookup
		attributes.put("X-SquirrelJME-InternalProjectName",
			project.getName());
		
		// Standard Application
		if (config.swmType == JavaMEMidletType.APPLICATION)
		{
			attributes.put("MIDlet-Name", config.swmName);
			attributes.put("MIDlet-Vendor", config.swmVendor);
			attributes.put("MIDlet-Version",
				project.getVersion().toString());
			
			// Main class of entry?
			if (config.mainClass != null)
				attributes.put("Main-Class", config.mainClass);
			
			// Add any defined MIDlets
			int midletId = 1;
			for (JavaMEMidlet midlet : config.midlets)
				attributes.put("MIDlet-" + (midletId++), midlet.toString());
			
			// Ignored in the launcher?
			if (config.ignoreInLauncher)
				attributes.put("X-SquirrelJME-NoLauncher", true);
		}
		
		// Library defining classes
		if (config.swmType == JavaMEMidletType.LIBRARY)
		{
			attributes.put("LIBlet-Name", config.swmName);
			attributes.put("LIBlet-Vendor", config.swmVendor);
			attributes.put("LIBlet-Version",
				project.getVersion().toString());
		}
		
		// SquirrelJME defined APIs
		if (config.swmType == JavaMEMidletType.API)
		{
			attributes.put("X-SquirrelJME-API-Name", config.swmName);
			attributes.put("X-SquirrelJME-API-Vendor", config.swmVendor);
			attributes.put("X-SquirrelJME-API-Version",
				project.getVersion().toString());
			
			// Configurations defined?
			if (!config.definedConfigurations.isEmpty())
				attributes.put("X-SquirrelJME-DefinedConfigurations",
					__delimate(config.definedConfigurations, ' '));
			
			// Profiles defined?
			if (!config.definedProfiles.isEmpty())
				attributes.put("X-SquirrelJME-DefinedProfiles",
					__delimate(config.definedProfiles, ' '));
			
			// Standards defined?
			if (!config.definedStandards.isEmpty())
				attributes.put("X-SquirrelJME-DefinedStandards",
					__delimate(config.definedStandards, ' '));
		}
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
