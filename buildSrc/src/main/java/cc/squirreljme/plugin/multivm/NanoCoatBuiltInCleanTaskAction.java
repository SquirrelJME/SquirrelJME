// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import cc.squirreljme.plugin.multivm.csv.SharedCsvEntry;
import cc.squirreljme.plugin.multivm.ident.SourceTargetClassifier;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.gradle.api.Action;
import org.gradle.api.Task;
import org.gradle.api.provider.Provider;

/**
 * Performs the actual cleaning of the NanoCoat Built-In ROM generated sources.
 * 
 * This task is a bit more complicated because it will only clean up for a
 * given ROM's output, while using workarounds for the shared directory.
 *
 * @since 2023/09/03
 */
public class NanoCoatBuiltInCleanTaskAction
	implements Action<Task>
{
	/** The classifier for the cleaning. */
	protected final SourceTargetClassifier classifier;
	
	/** The ROM base path. */
	private final Provider<Path> _romBasePath;
	
	/** The shared path. */
	private final Provider<Path> _sharedPath;
	
	/** The specific path. */
	private final Provider<Path> _specificPath;
	
	/**
	 * Initializes the cleaning action.
	 *
	 * @param __classifier The classifier of what this is cleaning for.
	 * @param __romBasePath The base ROM path.
	 * @param __specificPath The specific module path.
	 * @param __sharedPath The shared path.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/09/03
	 */
	public NanoCoatBuiltInCleanTaskAction(SourceTargetClassifier __classifier,
		Provider<Path> __romBasePath, Provider<Path> __specificPath,
		Provider<Path> __sharedPath)
		throws NullPointerException
	{
		if (__classifier == null || __romBasePath == null ||
			__specificPath == null || __sharedPath == null)
			throw new NullPointerException("NARG");
		
		this.classifier = __classifier;
		this._romBasePath = __romBasePath;
		this._specificPath = __specificPath;
		this._sharedPath = __sharedPath;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/09/03
	 */
	@Override
	public void execute(Task __task)
	{
		try
		{
			// Determine our specific ROM being used
			Path ourSpecificPath = this._specificPath.get();
			String ourSpecific = ourSpecificPath.getFileName().toString();
			
			// Get all the specifics that have been found
			Map<String, Path> specifics = this.__specifics();
			
			// Nothing to actually delete?
			if (specifics.isEmpty())
				return;
			
			// There are two sets of shared, the ones that are in our share
			// set and the ones that are in the other share set... we can only
			// remove ones that they do not use at all
			Set<SharedCsvEntry> ourShared = new LinkedHashSet<>();
			Set<SharedCsvEntry> otherShared = new LinkedHashSet<>();
			
			// Load in all the shared entries
			for (Map.Entry<String, Path> entry : specifics.entrySet())
			{
				String specific = entry.getKey();
				Path specificPath = entry.getValue();
				
				// Which one are we loading into?
				Set<SharedCsvEntry> into;
				if (specific.equals(ourSpecific))
					into = ourShared;
				else
					into = otherShared;
				
				// Perform the load
				this.__load(into, specificPath);
			}
			
			// By removing everything from ours that is used by others, we
			// have a set of shared entries which are not used elsewhere
			ourShared.removeAll(otherShared);
			
			// Go through everything and remove accordingly
			Path root = this._romBasePath.get();
			for (SharedCsvEntry entry : ourShared)
			{
				// Delete source and header file
				Files.deleteIfExists(root.resolve(
					VMHelpers.stringToPath(entry.getHeader())));
				Files.deleteIfExists(root.resolve(
					VMHelpers.stringToPath(entry.getSource())));
			}
			
			// Delete our own specific set
			__task.getProject().delete(ourSpecificPath);
		}
		catch (IOException __e)
		{
			throw new RuntimeException(__e);
		}
	}
	
	/**
	 * Determines all the specific ROMs.
	 *
	 * @return The specific ROMs.
	 * @throws IOException On read errors.
	 * @since 2023/09/03
	 */
	private Map<String, Path> __specifics()
		throws IOException
	{
		// If the build is clean, this will always return nothing
		Path root = this._romBasePath.get();
		Path specificRoot = root.resolve("specific");
		if (!Files.exists(root) || !Files.exists(specificRoot))
			return Collections.emptyMap();
		
		// Determine all specifics available
		Map<String, Path> result = new TreeMap<>();
		try (Stream<Path> stream = Files.list(specificRoot))
		{
			for (Path path : stream.collect(Collectors.toList()))
				if (Files.exists(path.resolve("shared.csv")))
					result.put(path.getFileName().toString(), path);
		}
		
		return Collections.unmodifiableMap(result);
	}
	
	/**
	 * Loads the shared entry lists.
	 *
	 * @param __into Which is this being loaded into?
	 * @param __base The base.
	 * @return {@code __into}.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/09/03
	 */
	private static Set<SharedCsvEntry> __load(Set<SharedCsvEntry> __into,
		Path __base)
		throws IOException, NullPointerException
	{
		if (__into == null || __base == null)
			throw new NullPointerException("NARG");
		
		// Make sure the target exists first
		Path sharedPath = __base.resolve("shared.csv");
		if (!Files.exists(sharedPath))
			return __into;
		
		// Read in everything
		try (InputStream in = Files.newInputStream(sharedPath,
				StandardOpenOption.READ); 
			Reader reader = new InputStreamReader(in, "utf-8"))
		{
			// Setup parser
			CsvToBean<SharedCsvEntry> parser =
				new CsvToBeanBuilder<SharedCsvEntry>(reader)
					.withType(SharedCsvEntry.class)
					.withIgnoreEmptyLine(true)
					.withIgnoreLeadingWhiteSpace(true)
					.build();
			
			// Read in all entries
			for (SharedCsvEntry entry : parser)
				__into.add(entry);
		}
		
		return __into;
	}
}
