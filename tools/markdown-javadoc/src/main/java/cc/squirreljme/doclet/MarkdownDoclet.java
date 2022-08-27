// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.doclet;

import cc.squirreljme.runtime.cldc.util.SortedTreeMap;
import cc.squirreljme.runtime.cldc.util.SortedTreeSet;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.RootDoc;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
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
	
	/** The classes which have been processed. */
	protected final Map<ClassName, ProcessedClass> processedClasses =
		new SortedTreeMap<>();
	
	/** The root document. */
	protected final RootDoc rootDoc;
	
	/** The output directory. */
	protected final Path outputDir;
	
	/** The title of this document. */
	protected final String documentTitle;
	
	/**
	 * Initializes the doclet handler.
	 * 
	 * @param __rootDoc The root document to write into.
	 * @param __outputDir The directory where the output goes.
	 * @param __docTitle The document title.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/08/23
	 */
	public MarkdownDoclet(RootDoc __rootDoc, Path __outputDir,
		String __docTitle)
		throws NullPointerException
	{
		if (__rootDoc == null || __outputDir == null)
			throw new NullPointerException("NARG");
		
		this.rootDoc = __rootDoc;
		this.outputDir = __outputDir;
		this.documentTitle = __docTitle;
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
			
			// Perform stage one processing
			processedClass.stageOne();
		}
		
		// Table of contents, sorted by package
		Map<BinaryName, Set<ProcessedClass>> toc = new SortedTreeMap<>();
		
		// Write all resultant class documentation
		for (ClassDoc classDoc : this.rootDoc.classes())
		{
			ProcessedClass processedClass = this.processClass(classDoc);
			
			// Store into table of contents for later, but only top-level
			// classes
			if (processedClass.parent() == null)
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
			Path tempFile = null;
			try
			{
				// Write document in a temporary file first
				tempFile = Files.createTempFile(
					"document", ".mkd");
				try (OutputStream out  = Files.newOutputStream(tempFile,
					StandardOpenOption.WRITE, StandardOpenOption.CREATE,
					StandardOpenOption.TRUNCATE_EXISTING);
					MarkdownWriter writer = new MarkdownWriter(
						new OutputStreamWriter(out, "utf-8")))
				{
					processedClass.write(writer);
				}
				
				// Move over
				Files.createDirectories(documentPath.getParent());
				Files.move(tempFile, documentPath,
					StandardCopyOption.REPLACE_EXISTING);
			}
			catch (IOException e)
			{
				throw new Error(e);
			}
			finally
			{
				if (tempFile != null)
					try
					{
						Files.deleteIfExists(tempFile);
					}
					catch (IOException ignored)
					{
					}
			}
		}
		
		// Write table of contents
		Path tempFile = null;
		try
		{
			// What is this called?
			Path tocPath = outputDir.resolve("table-of-contents.mkd");
			
			// Write to table of contents
			tempFile = Files.createTempFile(
				"toc", ".mkd");
			try (OutputStream out  = Files.newOutputStream(tempFile,
				StandardOpenOption.WRITE, StandardOpenOption.CREATE,
				StandardOpenOption.TRUNCATE_EXISTING);
				MarkdownWriter writer = new MarkdownWriter(
					new OutputStreamWriter(out, "utf-8")))
			{
				// Header for this library
				writer.header(true, 1, this.documentTitle);
				
				// Start list
				writer.listStart();
				
				// Handle each package
				for (Map.Entry<BinaryName, Set<ProcessedClass>> entry :
					toc.entrySet())
				{
					// Describe the package
					writer.printf(true, "`%s`",
						entry.getKey().toClass().toRuntimeString());
					
					// Start package list
					writer.listStart();
					
					// Go through each in the package
					for (ProcessedClass processed : entry.getValue())
					{
						// URI to the document
						writer.uri(MarkdownDoclet.relativePath(
							tocPath, processed._documentPath),
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
			
			// Move over
			Files.createDirectories(tocPath.getParent());
			Files.move(tempFile, tocPath,
				StandardCopyOption.REPLACE_EXISTING);
		}
		catch (IOException e)
		{
			throw new Error(e);
		}
		finally
		{
			if (tempFile != null)
				try
				{
					Files.deleteIfExists(tempFile);
				}
				catch (IOException ignored)
				{
				}
		}
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
		
		// Done processing
		return result;
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
				return 2;
			
			default:
				return 0;
		}
	}
	
	/**
	 * Returns the relative path to another document.
	 * 
	 * @param __from The source path.
	 * @param __to The target path.
	 * @return The relative string to the path for usage in the document.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/08/24
	 */
	public static String relativePath(Path __from, Path __to)
		throws NullPointerException
	{
		if (__from == null || __to == null)
			throw new NullPointerException("NARG");
		
		// We want to stay in the same directory
		if (!Files.isDirectory(__from))
			__from = __from.getParent();
		
		// Build path
		StringBuilder sb = new StringBuilder();
		for (Path fragment : __from.relativize(__to))
		{
			if (sb.length() > 0)
				sb.append('/');
			
			sb.append(fragment.getFileName());
		}
		
		return sb.toString();
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
		for (String[] option : __rd.options())
			switch (option[0])
			{
					// Where to place the output
				case MarkdownDoclet.OUTPUT_DIRECTORY_FLAG:
					outputDir = Paths.get(option[1]);
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
			new MarkdownDoclet(__rd, outputDir, docTitle).run();
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
