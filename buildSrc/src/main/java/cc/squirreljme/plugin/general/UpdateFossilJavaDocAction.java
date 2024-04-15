// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.general;

import cc.squirreljme.plugin.util.FossilExe;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Stream;
import org.gradle.api.Action;
import org.gradle.api.Task;
import org.gradle.api.logging.Logger;
import org.gradle.api.tasks.javadoc.Javadoc;

/**
 * Performs the action for updating the JavaDoc area in the unversioned space.
 *
 * @since 2022/08/29
 */
public class UpdateFossilJavaDocAction
	implements Action<Task>
{
	/** Prefix for fossil files. */
	private static final String _DOC_PREFIX =
		"javadoc/";
	
	/**
	 * {@inheritDoc}
	 * @since 2022/08/29
	 */
	@Override
	public void execute(Task __task)
	{
		Logger logger = __task.getLogger();
		
		// Collect all unversioned files
		Map<String, Map<String, JavaDocFile>> beforeModules =
			this.__collectUnversioned();
		
		// Debugging
		logger.debug("Before: {}", beforeModules);
		
		// Collect all task files
		Map<String, Map<String, JavaDocFile>> afterModules = new TreeMap<>();
		for (Object rawDepend : __task.getDependsOn())
		{
			// Must be another JavaDoc task
			if (!(rawDepend instanceof Javadoc))
				continue;
			
			// Run task collection
			this.__collectTask(afterModules, (Javadoc)rawDepend);
		}
		
		// Debugging
		logger.debug("After: {}", afterModules);
		
		// Figure out all the modules that have existed and still exist
		Set<String> modules = new TreeSet<>();
		modules.addAll(beforeModules.keySet());
		modules.addAll(afterModules.keySet());
		
		// Process every single module and handle it accordingly
		FossilExe fossil = FossilExe.instance();
		for (String module : modules)
		{
			// Get the before and after so we know what to do
			Map<String, JavaDocFile> before =
				UpdateFossilJavaDocAction.__forModule(beforeModules, module,
					false);
			Map<String, JavaDocFile> after =
				UpdateFossilJavaDocAction.__forModule(afterModules, module,
					false);
			
			// Determine all files that are going to be touched
			Set<String> files = new TreeSet<>();
			files.addAll(before.keySet());
			files.addAll(after.keySet());
			
			// Handle each individual file
			for (String file : files)
			{
				// Debug
				logger.lifecycle("Processing {}/{}...", module, file);
				
				// Where is this file?
				String unversionPath =
					UpdateFossilJavaDocAction._DOC_PREFIX +
					module + "/" + file;
				
				// Which files does this exist in?
				boolean inBefore = before.containsKey(file);
				boolean inAfter = after.containsKey(file);
				
				// File becomes deleted
				if (inBefore && !inAfter)
					fossil.unversionDelete(unversionPath);
				
				// File becomes created
				else if (!inBefore && inAfter)
					try (InputStream in = after.get(file).supplier.open())
					{
						fossil.unversionStoreBytes(unversionPath, in);
					}
					catch (IOException e)
					{
						throw new RuntimeException(e);
					}
				
				// Files exist in both, compare before replacement
				else
				{
					byte[] beforeSha = before.get(file).checkSum();
					byte[] afterSha = after.get(file).checkSum();
					
					// Did the files actually change?
					if (!Arrays.equals(beforeSha, afterSha))
						try (InputStream in = after.get(file).supplier.open())
						{
							fossil.unversionStoreBytes(unversionPath, in);
						}
						catch (IOException e)
						{
							throw new RuntimeException(e);
						}
				}
			}
		}
		
		// Write the by-project list, while doing so build class table of
		// contents.
		Map<String, String> allClasses = new TreeMap<>();
		try (ByteArrayOutputStream byProject = new ByteArrayOutputStream();
			ByteArrayOutputStream byClass = new ByteArrayOutputStream())
		{
			// Print out by project
			try (PrintStream printer = new PrintStream(byProject,
				true, "utf-8"))
			{
				// Header
				printer.println("# By Project");
				printer.println();
				
				// Contents, for all the after modules
				for (String module : modules)
				{
					// Print link to module
					printer.printf(" * [%s](%s/table-of-contents.mkd)%n",
						module, module);
					
					// This might not exist if there are no classes!
					InputStream inClasses = fossil.unversionCat(
						UpdateFossilJavaDocAction._DOC_PREFIX +
						module + 
						"/table-of-contents.csv");
					
					// Load in class files
					if (inClasses != null)
						try (BufferedReader reader = new BufferedReader(
							new InputStreamReader(inClasses,
								"utf-8")))
						{
							for (;;)
							{
								String ln = reader.readLine();
								
								// EOF?
								if (ln == null)
									break;
								
								// Skip blank lines
								ln = ln.trim();
								if (ln.isEmpty())
									continue;
								
								// Split by first comma
								int comma = ln.indexOf(',');
								if (comma < 0)
									continue;
								
								// Store class info
								allClasses.put(ln.substring(0, comma),
									module + "/" + ln.substring(comma + 1));
							}
						}
				}
				
				// End spacer and finalize
				printer.println();
				printer.flush();
			}
			
			// Print out by class
			try (PrintStream printer = new PrintStream(byClass,
				true, "utf-8"))
			{
				// Header
				printer.println("# By Class");
				printer.println();
				
				// Write every class
				for (Map.Entry<String, String> entry : allClasses.entrySet())
					printer.printf(" * [%s](%s)%n",
						UpdateFossilJavaDocAction.__encode(entry.getKey()),
						entry.getValue());
				
				// End spacer and finalize
				printer.println();
				printer.flush();
			}
			
			// Record file bytes
			fossil.unversionStoreBytes(
				UpdateFossilJavaDocAction._DOC_PREFIX +
					"by-project.mkd", byProject.toByteArray());
			fossil.unversionStoreBytes(
				UpdateFossilJavaDocAction._DOC_PREFIX +
					"by-class.mkd", byClass.toByteArray());
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Collects the output for the given task.
	 * 
	 * @param __target The target map.
	 * @param __task The task to read from.
	 * @since 2022/08/29
	 */
	private void __collectTask(Map<String, Map<String, JavaDocFile>> __target,
		Javadoc __task)
		throws NullPointerException
	{
		if (__target == null || __task == null)
			throw new NullPointerException("NARG");
		
		// Get set for this module
		Map<String, JavaDocFile> set = UpdateFossilJavaDocAction.__forModule(
			__target, __task.getProject().getName(), true);
		
		// Go through the file outputs
		Path basePath = __task.getOutputs().getFiles()
			.getSingleFile().toPath();
		if (Files.exists(basePath) && Files.isDirectory((basePath)))
			try(Stream<Path> stream = Files.walk(basePath))
			{
				stream.forEach((__path) -> {
						// Ignore directories
						if (Files.isDirectory(__path))
							return;
						
						// Determine the file name
						StringBuilder sb = new StringBuilder();
						basePath.relativize(__path).forEach((__item) -> {
								if (sb.length() > 0)
									sb.append('/');
								sb.append(__item.getFileName().toString());
							});
						
						// Add it
						String fileName = sb.toString();
						set.put(fileName, new JavaDocFile(fileName, () ->
							Files.newInputStream(__path,
								StandardOpenOption.READ)));
					});
			}
			catch (IOException e)
			{
				throw new RuntimeException(e);
			}
	}
	
	/**
	 * Collects all unversioned files, this looks for files which are already
	 * in the unversioned space to get updated accordingly.
	 * 
	 * @return The collection of files in the unversioned space.
	 * @since 2022/08/29
	 */
	private Map<String, Map<String, JavaDocFile>> __collectUnversioned()
	{
		Map<String, Map<String, JavaDocFile>> result = new TreeMap<>();
		
		// Go through every unversioned file
		FossilExe fossil = FossilExe.instance();
		for (String path : fossil.unversionList())
		{
			// Ignore anything outside the JavaDoc area
			if (!path.startsWith(UpdateFossilJavaDocAction._DOC_PREFIX))
				continue;
			
			// Get relative path to the file
			String relativePath = path.substring(
				UpdateFossilJavaDocAction._DOC_PREFIX.length());
			
			// Get module/file split
			int firstSlash = relativePath.indexOf('/');
			if (firstSlash < 0)
				continue;
			
			// Get the module and file this is for
			String module = relativePath.substring(0, firstSlash);
			String file = relativePath.substring(firstSlash + 1);
			
			// Get the set for this
			Map<String, JavaDocFile> set = UpdateFossilJavaDocAction
				.__forModule(result, module, true);
			
			// Create file accordingly
			set.put(file,
				new JavaDocFile(file, () -> fossil.unversionCat(path)));
		}
		
		return result;
	}
	
	/**
	 * Encodes the key for JavaDoc.
	 * 
	 * @param __key The key to encode.
	 * @return The encoded key.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/08/29
	 */
	private static String __encode(String __key)
		throws NullPointerException
	{
		if (__key == null)
			throw new NullPointerException("NARG");
		
		// If there are no underscores, we do not have to wipe bold/italics
		if (__key.indexOf('_') < 0)
			return __key;
		
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0, n = __key.length(); i < n; i++)
		{
			char c = __key.charAt(i);
			
			if (c == '_')
				sb.append('\\');
			sb.append(c);
		}
		
		return sb.toString();
	}
	
	/**
	 * Looks for the given module, potentially creating it.
	 * 
	 * @param __in The map to look in.
	 * @param __module The module to get.
	 * @param __create Create if missing?
	 * @return The set for the given module or an immutable empty map if not
	 * found and not creating.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/08/29
	 */
	private static Map<String, JavaDocFile> __forModule(
		Map<String, Map<String, JavaDocFile>> __in, String __module,
		boolean __create)
		throws NullPointerException
	{
		if (__in == null || __module == null)
			throw new NullPointerException("NARG");
		
		Map<String, JavaDocFile> set = __in.get(__module);
		if (set == null)
		{
			if (!__create)
				return Collections.emptyMap();
			
			__in.put(__module, (set = new TreeMap<>()));
		}
		
		return set;
	}
}
