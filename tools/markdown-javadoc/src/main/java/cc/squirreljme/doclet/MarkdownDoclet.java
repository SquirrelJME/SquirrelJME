// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.doclet;

import cc.squirreljme.io.file.SafeTemporaryFileOutputStream;
import cc.squirreljme.runtime.cldc.util.SortedTreeMap;
import cc.squirreljme.runtime.cldc.util.SortedTreeSet;
import cc.squirreljme.runtime.cldc.util.StringUtils;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.RootDoc;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.classfile.BinaryName;
import net.multiphasicapps.classfile.ClassIdentifier;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.markdownwriter.MarkdownWriter;

/**
 * This contains the older standard Doclet for API generation.
 *
 * @since 2022/08/23
 */
public class MarkdownDoclet
	implements Runnable
{
	/** The document title flag. */
	public static final String DOC_TITLE_FLAG =
		"-doctitle";
	
	/** Window title flag. */
	public static final String WINDOW_TITLE_FLAG =
		"-windowtitle";
	
	/** No timestamp flag. */
	public static final String NO_TIMESTAMP_FLAG =
		"-notimestamp";
	
	/** The flag to set the output directory. */
	public static final String OUTPUT_DIRECTORY_FLAG =
		"-d";
	
	/** Java sources for SquirrelJME. */
	public static final String SQUIRRELJME_JAVA_SOURCES =
		"-squirreljmejavasources";
	
	/** Locations of other projects' Markdown JavaDoc in SquirrelJME. */
	public static final String SQUIRRELJME_PROJECT_DOCS =
		"-squirreljmeprojectmjd";
	
	/** The project name. */
	public static final String SQUIRRELJME_PROJECT =
		"-squirreljmeproject";
	
	/** The classes which have been processed. */
	protected final Map<ClassName, ProcessedClass> processedClasses =
		new SortedTreeMap<>();
	
	/** The root document. */
	protected final RootDoc rootDoc;
	
	/** The output directory. */
	protected final Path outputDir;
	
	/** The title of this document. */
	protected final String documentTitle;
	
	/** The name of this project. */
	protected final String projectName;
	
	/** Paths to every project that exists. */
	private final List<Path> _allProjects;
	
	/** Classes which are remotely elsewhere. */
	private volatile Map<String, RemoteClass> _remoteClasses;
	
	/**
	 * Initializes the doclet handler.
	 * 
	 * @param __rootDoc The root document to write into.
	 * @param __outputDir The directory where the output goes.
	 * @param __docTitle The document title.
	 * @param __allProjects All project paths.
	 * @param __projectName
	 * @throws NullPointerException On null arguments.
	 * @since 2022/08/23
	 */
	public MarkdownDoclet(RootDoc __rootDoc, Path __outputDir,
		String __docTitle, List<Path> __allProjects, String __projectName)
		throws NullPointerException
	{
		if (__rootDoc == null || __outputDir == null ||
			__allProjects == null || __projectName == null)
			throw new NullPointerException("NARG");
		
		this.rootDoc = __rootDoc;
		this.outputDir = __outputDir;
		this.documentTitle = __docTitle;
		this._allProjects = __allProjects;
		this.projectName = __projectName;
	}
	
	/**
	 * Processes the given class.
	 * 
	 * @param __classDoc The class documentation.
	 * @return The processed class file.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/08/24
	 */
	public ProcessedClass processClass(ClassDoc __classDoc)
		throws NullPointerException
	{
		if (__classDoc == null)
			throw new NullPointerException("NARG");
		
		// What is this class called?
		ClassName name = new ClassName(__classDoc.qualifiedName()
			.replace('.', '/'));
		
		// Has this class already been processed?
		Map<ClassName, ProcessedClass> processed = this.processedClasses;
		ProcessedClass result = processed.get(name);
		if (result != null)
			return result;
		
		// Otherwise, set up a process for it
		result = new ProcessedClass(this, name, __classDoc);
		processed.put(name, result);
		
		// Perform stage one processing, since it should not have happened
		// here yet
		result.stageOne();
		
		// Done processing
		return result;
	}
	
	/**
	 * Returns an already processed class.
	 * 
	 * @param __className The name of the class.
	 * @return The already processed class.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/08/29
	 */
	public ProcessedClass processedClass(ClassName __className)
		throws NullPointerException
	{
		if (__className == null)
			throw new NullPointerException("NARG");
		
		return this.processedClasses.get(__className);
	}
	
	/**
	 * Attempts to locate a remote class for the project it belongs to.
	 * 
	 * @param __name The name of the class to get.
	 * @return The module for the remote class or {@code null} if not found.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/08/29
	 */
	public RemoteClass remoteClassProject(String __name)
		throws NullPointerException
	{
		// Do we need to load all the remote classes?
		Map<String, RemoteClass> remoteClasses = this._remoteClasses;
		if (remoteClasses == null)
		{
			// Setup
			remoteClasses = new SortedTreeMap<>();
			
			// Go through all known projects
			for (Path projectPath : this._allProjects)
			{
				// CSV that contains the class links along with the project
				// name
				Path csvPath = projectPath.resolve("table-of-contents.csv");
				Path namePath = projectPath.resolve("project.name");
				if (!Files.exists(csvPath) || !Files.exists(namePath))
					continue;
				
				// Read in the project name
				String projectName = null;
				try (BufferedReader reader = new BufferedReader(
					new InputStreamReader(Files.newInputStream(namePath,
						StandardOpenOption.READ), "utf-8")))
				{
					projectName = reader.readLine();
				}
				catch (IOException ignored)
				{
				}
				
				// Read everything in
				try (BufferedReader reader = new BufferedReader(
					new InputStreamReader(Files.newInputStream(csvPath,
						StandardOpenOption.READ), "utf-8")))
				{
					for (;;)
					{
						// Read next line, check for EOF
						String ln = reader.readLine();
						if (ln == null)
							break;
						
						// Ignore blank lines
						ln = ln.trim();
						if (ln.isEmpty())
							continue;
						
						// Read in splitting comma
						int comma = ln.indexOf(',');
						if (comma < 0)
							continue;
						
						// Split fields
						String className = ln.substring(0, comma);
						String markdownPath = ln.substring(comma + 1);
						
						// Store it
						remoteClasses.put(className,
							new RemoteClass(projectName, markdownPath,
								className));
					}
				}
				catch (IOException ignored)
				{
				}
			} 
		}
		
		// Locate it
		return remoteClasses.get(__name);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/08/24
	 */
	@Override
	public void run()
	{
		// Process all input classes first
		Path outputDir = this.outputDir;
		for (ClassDoc classDoc : this.rootDoc.classes())
		{
			ProcessedClass processedClass = this.processClass(classDoc);
			
			// Set initial variables
			processedClass._implicit = true;
			processedClass._documentPath = MarkdownDoclet.__resolveDocPath(
				outputDir, processedClass.name);
		}
		
		// Table of contents, sorted by package
		Map<BinaryName, Set<ProcessedClass>> toc = new SortedTreeMap<>();
		
		// Write all resultant class documentation
		for (ClassDoc classDoc : this.rootDoc.classes())
		{
			ProcessedClass processedClass = this.processClass(classDoc);
			
			// Store into table of contents for later, but only top-level
			// classes
			if (processedClass.parentClass() == null)
			{
				BinaryName inPackage = processedClass.name.inPackage();
				Set<ProcessedClass> subToc = toc.get(inPackage);
				if (subToc == null)
					toc.put(inPackage, (subToc = new SortedTreeSet<>(
						new ProcessedClassComparator())));
				subToc.add(processedClass);
			}
			
			// What is this called?
			Path documentPath = processedClass._documentPath;
			
			// Write document out
			try
			{
				// Write document in a temporary file first
				try (OutputStream out  = new SafeTemporaryFileOutputStream(
						documentPath);
					MarkdownWriter writer = new MarkdownWriter(
						new OutputStreamWriter(out, "utf-8")))
				{
					processedClass.write(writer);
				}
			}
			catch (IOException e)
			{
				throw new Error(e);
			}
		}
		
		// Write table of contents
		try
		{
			// What is this called?
			Path tocPath = outputDir.resolve("table-of-contents.mkd");
			Path csvPath = outputDir.resolve("table-of-contents.csv");
			Path namePath = outputDir.resolve("project.name");
			
			// Write project name
			try (PrintStream printer = new PrintStream(
				new SafeTemporaryFileOutputStream(namePath),
					true, "utf-8"))
			{
				printer.println(this.projectName);
			}
			
			// Write to table of contents
			try (MarkdownWriter writer = new MarkdownWriter(
					new OutputStreamWriter(
						new SafeTemporaryFileOutputStream(tocPath),
							"utf-8"));
				PrintStream csv = new PrintStream(
					new SafeTemporaryFileOutputStream(csvPath),
						true, "utf-8"))
			{
				// Header for this library
				writer.header(true, 1, this.documentTitle);
				
				// Start list
				writer.listStart();
				
				// Handle each package
				for (Map.Entry<BinaryName, Set<ProcessedClass>> entry :
					toc.entrySet())
				{
					// Write CSV entry
					String packageName = entry.getKey().toClass()
						.toRuntimeString();
					
					// Describe the package
					writer.printf(true, "`%s`", packageName);
					
					// Start package list
					writer.listStart();
					
					// Go through each in the package
					for (ProcessedClass processed : entry.getValue())
					{
						// Write class into the CSV
						String docPath = Utilities.relativePath(tocPath,
							processed._documentPath);
						csv.printf("%s,%s%n",
							processed.name.toRuntimeString(), docPath);
						
						// URI to the document
						writer.uri(docPath,
							processed.name.simpleName().identifier());
						
						// Dash for split
						writer.print(true, ": ");
						
						// What is the purpose of this class?
						writer.print(true, Utilities.firstLine(
							processed.description()));
						writer.listNext();
					}
					
					// Stop class list
					writer.listEnd();
					writer.listNext();
				}
				
				// End package list
				writer.listEnd();
			}
		}
		catch (IOException e)
		{
			throw new Error(e);
		}
	}
	
	/**
	 * Returns the length of the given option.
	 * 
	 * @param __s The option to check.
	 * @return The length of the given option.
	 * @since 2022/08/24
	 */
	public static int optionLength(String __s)
	{
		switch (__s)
		{
			case MarkdownDoclet.NO_TIMESTAMP_FLAG:
				return 1;
			
			case MarkdownDoclet.DOC_TITLE_FLAG:
			case MarkdownDoclet.OUTPUT_DIRECTORY_FLAG:
			case MarkdownDoclet.WINDOW_TITLE_FLAG:
			case MarkdownDoclet.SQUIRRELJME_JAVA_SOURCES:
			case MarkdownDoclet.SQUIRRELJME_PROJECT_DOCS:
			case MarkdownDoclet.SQUIRRELJME_PROJECT:
				return 2;
			
			default:
				return 0;
		}
	}
	
	/**
	 * Starts processing of the documentation.
	 * 
	 * @param __rd The root document.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/08/24
	 */
	public static boolean start(RootDoc __rd)
		throws NullPointerException
	{
		if (__rd == null)
			throw new NullPointerException("NARG");
		
		// Process options
		Path outputDir = null;
		String docTitle = null;
		List<Path> allProjects = new ArrayList<>();
		String projectName = null;
		for (String[] option : __rd.options())
			switch (option[0])
			{
					// Where to place the output
				case MarkdownDoclet.OUTPUT_DIRECTORY_FLAG:
					outputDir = Paths.get(option[1]);
					break;
					
					// Project directories to locate class CSVs
				case MarkdownDoclet.SQUIRRELJME_PROJECT_DOCS:
					for (String source : StringUtils.basicSplit(
						File.pathSeparatorChar, option[1]))
						allProjects.add(Paths.get(source));
					break;
					
					// The name of the project
				case MarkdownDoclet.SQUIRRELJME_PROJECT:
					projectName = option[1];
					break;
					
					// The title of the document
				case MarkdownDoclet.DOC_TITLE_FLAG:
					docTitle = option[1];
					break;
				
					// Unhandled, ignored
				default:
					break;
			}
		
		// Perform processing
		try
		{
			new MarkdownDoclet(__rd, outputDir, docTitle, allProjects,
				projectName).run();
			return true;
		}
		
		// Runtime exceptions are completely hidden
		catch (Exception e)
		{
			throw new Error(e);
		}
	}
	
	/**
	 * Resolves the path of the document.
	 * 
	 * @param __base The base path to resolve.
	 * @param __name The class name to resolve.
	 * @return The resolved path.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/08/24
	 */
	private static Path __resolveDocPath(Path __base, ClassName __name)
		throws NullPointerException
	{
		if (__base == null || __name == null)
			throw new NullPointerException("NARG");
		
		// Get the package path
		BinaryName inPackage = __name.inPackage();
		if (inPackage != null)
			for (ClassIdentifier identifier : inPackage)
				__base = __base.resolve(identifier.identifier());
		
		// Then add the markdown reference
		return __base.resolve(__name.simpleName().identifier() + ".mkd");
	}
}
