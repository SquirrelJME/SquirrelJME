// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin;

import cc.squirreljme.plugin.util.CommentReader;
import cc.squirreljme.plugin.util.ErrorListTokenizer;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.TreeMap;
import org.gradle.api.Project;
import org.gradle.api.file.FileTree;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.api.tasks.SourceSet;

/**
 * This is a mapping of individual error codes for a project.
 *
 * @since 2020/02/22
 */
public final class ErrorListManager
{
	/** The project to manage the list for. */
	protected final Project project;
	
	/** The listed errors. */
	private final Map<Integer, SourceError> _errors =
		new TreeMap<>();
	
	/** The error code for this project. */
	private String _projectErrorCode;
	
	/**
	 * Initializes the error code manager.
	 *
	 * @param __project The project to manage.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/02/22
	 */
	public ErrorListManager(Project __project)
		throws NullPointerException
	{
		if (__project == null)
			throw new NullPointerException("No project.");
		
		this.project = __project;
	}
	
	/**
	 * Returns the next available error.
	 *
	 * @return The next available error.
	 * @since 2020/02/22
	 */
	public final String next()
	{
		this.__init();
		
		return this._projectErrorCode +
			SourceError.indexToString(this.nextIndex());
	}
	
	/**
	 * Returns the next available error.
	 *
	 * @return The next available error.
	 * @since 2020/02/22
	 */
	public final int nextIndex()
	{
		this.__init();
		
		Map<Integer, SourceError> errors = this._errors;
		synchronized (this)
		{
			for (int i = 1; i <= SourceError._MAX_ERROR_CODE; i++)
				if (!errors.containsKey(i))
					return i;
			
			throw new IllegalArgumentException("Ran out of error indexes.");
		}
	}
	
	/**
	 * Prints the errors to the given stream.
	 *
	 * @param __out The stream to write to.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/02/22
	 */
	public final void print(PrintStream __out)
		throws NullPointerException
	{
		if (__out == null)
			throw new NullPointerException("No stream specified.");
		
		this.__init();
		
		synchronized (this)
		{
			for (SourceError e : this._errors.values())
				__out.println(e);
		}
	}
	
	/**
	 * Initializes the code map.
	 *
	 * @since 2020/02/22
	 */
	private void __init()
	{
		Project project = this.project;
		Map<Integer, SourceError> errors = this._errors;
		
		synchronized (this)
		{
			// Already loaded?
			if (this._projectErrorCode != null)
				return;
			
			// This must exist
			SquirrelJMEPluginConfiguration config = project.getExtensions()
				.<SquirrelJMEPluginConfiguration>getByType(
					SquirrelJMEPluginConfiguration.class);
			
			// Fail if this is missing
			String projectErrorCode = config.javaDocErrorCode;
			if (projectErrorCode == null)
				throw new IllegalStateException(String.format(
					"Project %s has no error code.", project.getName()));
			
			// Used as a flag and to indicate the group for this
			this._projectErrorCode = projectErrorCode;
			
			// Temporary buffer for data reading
			StringBuilder buffer = new StringBuilder();
			
			// Get source code
			FileTree sources = project.getConvention()
				.getPlugin(JavaPluginConvention.class).getSourceSets()
				.getByName(SourceSet.MAIN_SOURCE_SET_NAME).getAllJava()
				.getAsFileTree();
			
			// Parse source code
			for (File source : sources)
				try (InputStream in = Files.newInputStream(source.toPath(),
					StandardOpenOption.READ);
					ErrorListTokenizer tokenizer = new ErrorListTokenizer(
						source.toPath().getFileName(),
						new CommentReader(in)))
				{
					// Keep reading in errors
					for (;;)
					{
						// Read the next error
						SourceError error = tokenizer.next();
						if (error == null)
							break;
						
						// Store error in the map
						SourceError old = errors.put(error.index, error);
						
						// Check duplicate
						if (old != null)
							System.err.printf("Duplicate error %s and %s.%n",
								error, old);
						
						// Wrong project
						if (!error.projectCode.equals(projectErrorCode))
							System.err.printf("Error has wrong project: %s," +
								"should be: %s%n", error, projectErrorCode);
					}
				}
				catch (IOException e)
				{
					throw new RuntimeException("Could not decode file.", e);
				}
		}
	}
	
}
