package cc.squirreljme.plugin;

import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * This is a task for un-mime of resources.
 *
 * @since 2020/02/27
 */
public class UnMimeResourcesTask
	implements Action<Task>
{
	/** The project to un-mime in. */
	protected final Project project;
	
	/** The base portion to modify. */
	protected final String base;
	
	/**
	 * Initializes the task.
	 *
	 * @param __project The project.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/02/27
	 */
	public UnMimeResourcesTask(Project __project, String __base)
		throws NullPointerException
	{
		if (__project == null)
			throw new NullPointerException("No project specified.");
		
		this.project = __project;
		this.base = __base;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/02/27
	 */
	@Override
	public void execute(Task __task)
	{
		Project project = this.project;
		String base = this.base;
		
		// Where are the resources going to go?
		Path genResourcesRoot = project.getBuildDir().toPath()
			.resolve("generated-" + base + "-resources");
		
		// Go through any resources with MIME data
		try
		{
			// Make sure our output exists
			Files.createDirectories(genResourcesRoot);
			
			// Only process if the input directories exist
			Path rcRoot = project.getProjectDir().toPath()
				.resolve("src").resolve(base).resolve("resources");
			if (!Files.exists(rcRoot))
				return;
			
			// Walk file tree
			Files.walk(rcRoot).forEach(
				(Path __visit) ->
				{
					// The normal file
					String fileName = __visit.getFileName().toString();
					
					// Ignore directories and any non-MIME file
					if (Files.isDirectory(__visit) ||
						!fileName.endsWith(".__mime"))
						return;
					
					// Remove the mime extension
					Path noMime = genResourcesRoot.resolve(rcRoot.relativize(
						__visit.getParent())).resolve(fileName.substring(0,
						fileName.length() - ".__mime".length()));
					
					// Inner loop
					try
					{
						// Make sure the output exists
						Files.createDirectories(noMime.getParent());
					
						// Decode the MIME file
						try (MIMEFileDecoder in = new MIMEFileDecoder(
							Files.newInputStream(__visit,
								StandardOpenOption.READ));
							OutputStream out = Files.newOutputStream(noMime,
								StandardOpenOption.WRITE,
								StandardOpenOption.CREATE,
								StandardOpenOption.TRUNCATE_EXISTING))
						{
							// Copy all of it!
							byte[] buf = new byte[4096];
							for (;;)
							{
								int rc = in.read(buf);
								
								if (rc < 0)
									break;
								
								out.write(buf, 0, rc);
							}
						}
					}
					catch (IOException e)
					{
						throw new RuntimeException(String.format(
							"Could not decode file %s.", __visit), e);
					}
				});
		}
		catch (IOException e)
		{
			throw new RuntimeException(String.format(
				"Failed to un-mime %s for %s.", base,
				project.getName()), e);
		}
	}
}
