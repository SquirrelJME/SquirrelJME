package cc.squirreljme.plugin.tasks;

import cc.squirreljme.plugin.util.MIMEFileDecoder;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.LinkedList;
import javax.inject.Inject;
import org.gradle.api.Action;
import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.file.DirectoryTree;
import org.gradle.api.file.FileCollection;
import org.gradle.api.file.FileCopyDetails;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.api.tasks.SourceSetOutput;
import org.gradle.language.jvm.tasks.ProcessResources;

public class MimeDecodeResourcesTask
	extends DefaultTask
{
	/** The extension for MIME files. */
	public static final String EXTENSION =
		".__mime";
	
	/** The source set to modify. */
	protected final String sourceSet;
	
	/**
	 * Initializes the task.
	 *
	 * @param __sourceSet The source set to adjust.
	 * @param __processTask The processing task.
	 * @since 2020/02/28
	 */
	@Inject
	public MimeDecodeResourcesTask(String __sourceSet,
		ProcessResources __processTask)
	{
		// The source set we want to work with
		this.sourceSet = __sourceSet;
		
		// Set details of this task
		this.setGroup("squirreljme");
		this.setDescription("Decodes MIME encoded resource files.");
		
		// Configure tasks and such
		Project project = this.getProject();
		this.getInputs().files(
			project.provider(this::__taskInputsAsFileCollection));
		this.getOutputs().files(
			project.provider(this::__taskOutputsAsFileCollection));
		this.onlyIf(this::__onlyIf);
		
		// The action to do
		this.doLast(new Action<Task>() {
				@Override
				public void execute(Task __task)
				{
					MimeDecodeResourcesTask.this.__doLast(__task);
				}
			});
		
		// The process task depends on this task
		__processTask.dependsOn(this);
		
		// However, since we use our own files, we exclude files from this
		// task!
		__processTask.eachFile((FileCopyDetails __fcd) ->
			{
				// Exclude all MIME files from this task
				if (MimeDecodeResourcesTask.__isMimeFile(__fcd.getFile().toPath()))
					__fcd.exclude();
			});
	}
	
	/**
	 * Runs the actual task.
	 *
	 * @param __task The task to run for.
	 * @since 2020/02/28
	 */
	private void __doLast(Task __task)
	{
		Project project = this.getProject();
		Path outDir = this.__outputPath();
		
		// Anything here can fail for IO operations
		try
		{
			// Process all outputs
			for (__Output__ output : this.__taskOutputs())
			{
				Path outPath = output.output;
				
				// Make sure the output directory exists!
				Files.createDirectories(outPath.getParent());
				
				// Copy decoded MIME data to the output file
				try (MIMEFileDecoder in = new MIMEFileDecoder(
					Files.newInputStream(output.input.absolute,
							StandardOpenOption.READ));
					OutputStream out = Files.newOutputStream(outPath,
							StandardOpenOption.CREATE,
							StandardOpenOption.TRUNCATE_EXISTING,
							StandardOpenOption.WRITE))
				{
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
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Returns the output path.
	 *
	 * @return The output path.
	 * @since 2020/02/28
	 */
	private Path __outputPath()
	{
		// We might need to set an output potentially
		Project project = this.getProject();
		SourceSetOutput outSet = this.getProject().getConvention().
			getPlugin(JavaPluginConvention.class).
			getSourceSets().getByName(this.sourceSet).getOutput();
		
		// The base output directory
		File outDirFile = outSet.getResourcesDir();
		if (outDirFile == null)
			outSet.setResourcesDir((outDirFile = project.getBuildDir().
			toPath().resolve("mime-decoded-" + this.sourceSet).toFile()));
		
		return outDirFile.toPath();
	}
	
	/**
	 * Only run the task if certain conditions are met.
	 *
	 * @param __task The task to run.
	 * @return The result if it should run or not.
	 * @since 2020/02/28
	 */
	private boolean __onlyIf(Task __task)
	{
		// There is no point in running this, if nothing can be done
		return !this.__taskInputsAsFileCollection().isEmpty();
	}
	
	/**
	 * Filter for task processing.
	 *
	 * @param __file The file to filter.
	 * @return If the file is to be
	 * @since 2020/02/28
	 */
	private boolean __processTaskFilter(File __file)
	{
		return MimeDecodeResourcesTask.__isMimeFile(__file.toPath());
	}
	
	/**
	 * Gets the inputs for the task as fully informed files.
	 *
	 * @return The task inputs.
	 * @since 2020/02/28
	 */
	private Iterable<__Input__> __taskInputs()
	{
		Project project = this.getProject();
		
		// Discover all the input files
		Collection<__Input__> result = new LinkedList<>();
		for (DirectoryTree dir : this.getProject().getConvention().
			getPlugin(JavaPluginConvention.class).getSourceSets().
			getByName(this.sourceSet).getResources().getSrcDirTrees())
		{
			Path baseDir = dir.getDir().toPath();
			
			// Process all files in each directory
			for (File file : project.files(dir))
			{
				Path path = file.toPath();
				
				// Only consider MIME files
				if (!MimeDecodeResourcesTask.__isMimeFile(path))
					continue;
				
				result.add(new __Input__(path, baseDir.relativize(path)));
			}
		}
		
		return result;
	}
	
	/**
	 * Gets the inputs for the task.
	 *
	 * @return The task inputs.
	 * @since 2020/02/28
	 */
	private FileCollection __taskInputsAsFileCollection()
	{
		Collection<File> result = new LinkedList<>();
		for (__Input__ file : this.__taskInputs())
			result.add(file.absolute.toFile());
		
		return this.getProject().files(result);
	}
	
	/**
	 * Gets the outputs for the task.
	 *
	 * @return The task outputs.
	 * @since 2020/02/28
	 */
	private Iterable<__Output__> __taskOutputs()
	{
		Project project = this.getProject();
		
		// The output path to process
		Path outDir = this.__outputPath();
		
		// Store output files accordingly
		Collection<__Output__> result = new LinkedList<>();
		for (__Input__ input : this.__taskInputs())
			result.add(new __Output__(input, outDir.resolve(
				MimeDecodeResourcesTask.__removeMimeExtension(input.relative))));
		
		return result;
	}
	
	/**
	 * Gets the outputs for the task.
	 *
	 * @return The task outputs.
	 * @since 2020/02/28
	 */
	private FileCollection __taskOutputsAsFileCollection()
	{
		Collection<File> result = new LinkedList<>();
		for (__Output__ output : this.__taskOutputs())
			result.add(output.output.toFile());
		
		return this.getProject().files(result);
	}
	
	/**
	 * Is this a MIME file?
	 *
	 * @param __path The path to get.
	 * @return If this is a MIME file.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/02/28
	 */
	private static boolean __isMimeFile(Path __path)
		throws NullPointerException
	{
		if (__path == null)
			throw new NullPointerException("No path specified.");
		
		return __path.getFileName().toString().endsWith(
			MimeDecodeResourcesTask.EXTENSION);
	}
	
	/**
	 * Removes the extension from the file.
	 *
	 * @param __fn The file name to remove.
	 * @return The file name without the extension.
	 * @throws IllegalArgumentException If the file does not have the
	 * extension.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/02/28
	 */
	private static Path __removeMimeExtension(Path __fn)
		throws IllegalArgumentException, NullPointerException
	{
		if (__fn == null)
			throw new NullPointerException("No file name specified.");
		
		// Must be a MIME file
		String fileName = __fn.getFileName().toString();
		if (!fileName.endsWith(MimeDecodeResourcesTask.EXTENSION))
			throw new IllegalArgumentException("File does not end in MIME.");
		
		// The output will be a sibling of the path
		return __fn.resolveSibling(
			fileName.substring(0, fileName.length() - MimeDecodeResourcesTask.EXTENSION
				.length()));
	}
}
