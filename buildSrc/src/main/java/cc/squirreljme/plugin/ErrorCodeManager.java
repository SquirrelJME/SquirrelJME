// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin;

import java.io.PrintStream;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import org.gradle.api.Project;

/**
 * This is a mapping of error codes to projects.
 *
 * @since 2020/02/22
 */
public final class ErrorCodeManager
{
	/** The project for discovery. */
	protected final Project project;
	
	/** The mapping of error codes to projects. */
	private final Map<String, Project> _codeMap =
		new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
	
	/** Unclaimed error codes. */
	private final SortedSet<String> _unclaimed =
		new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
	
	/**
	 * Initializes the error code manager.
	 *
	 * @param __project The root project to manage.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/02/22
	 */
	public ErrorCodeManager(Project __project)
		throws NullPointerException
	{
		if (__project == null)
			throw new NullPointerException("No project.");
		
		this.project = __project.getRootProject();
		
		// Add all unclaimed codes
		Set<String> unclaimed = this._unclaimed;
		for (char a = 'A'; a <= 'Z'; a++)
			for (char b = 'A'; b <= 'Z'; b++)
				unclaimed.add(a + "" + b);
	}
	
	/**
	 * Returns an error list manager for the given code ID.
	 *
	 * @param __code The code to get.
	 * @return The error list manager.
	 * @since 2020/02/22
	 */
	public final ErrorListManager errorList(String __code)
	{
		return new ErrorListManager(this.projectByCode(__code));
	}
	
	/**
	 * Returns the next available error code.
	 *
	 * @return The next available error code.
	 * @since 2020/02/22
	 */
	public final String next()
	{
		this.__init();
		
		synchronized (this)
		{
			return this._unclaimed.first();
		}
	}
	
	/**
	 * Prints the error codes to the given stream.
	 *
	 * @param __out The stream to print to.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/02/22
	 */
	public final void print(PrintStream __out)
		throws NullPointerException
	{
		if (__out == null)
			throw new NullPointerException("No output stream.");
		
		this.__init();
		
		// Print out every project
		Map<String, Project> codeMap = this._codeMap;
		synchronized (this)
		{
			for (Map.Entry<String, Project> e : codeMap.entrySet())
				__out.printf("%2s: %s%n", e.getKey(), e.getValue().getName());
		}
	}
	
	/**
	 * Returns the project which owns the given code.
	 *
	 * @param __code The code to obtain.
	 * @throws NoSuchElementException If no such project exists.
	 * @return The resulting
	 */
	public final Project projectByCode(String __code)
		throws NoSuchElementException
	{
		this.__init();
		
		// Search
		Map<String, Project> codeMap = this._codeMap;
		synchronized (this)
		{
			Project rv = codeMap.get(__code);
			if (rv == null)
				throw new IllegalArgumentException(
					String.format("No project for code %s", __code));
			
			return rv;
		}
	}
	
	/**
	 * Initializes the code map.
	 *
	 * @since 2020/02/22
	 */
	private void __init()
	{
		Map<String, Project> codeMap = this._codeMap;
		Set<String> unclaimed = this._unclaimed;
		synchronized (this)
		{
			// Already has been loaded?
			if (!codeMap.isEmpty())
				return;
			
			// Add all projects found to the list
			for (Project sub : this.project.getAllprojects())
			{
				// Only choose SquirrelJME projects
				SquirrelJMEPluginConfiguration config = sub.getExtensions()
					.<SquirrelJMEPluginConfiguration>findByType(
						SquirrelJMEPluginConfiguration.class);
				if (config == null)
					continue;
				
				String code = config.javaDocErrorCode;
				if (code == null)
				{
					System.err.printf(
						"WARNING: Project %s has no error code.\n",
						sub.getName());
					
					// Cannot do anything at this point
					continue;
				}
				
				Project dup = codeMap.put(code.toUpperCase(), sub);
				if (dup != null)
					System.err.printf(
						"WARNING: Project %s shares error code %s with %s.\n",
						sub.getName(), code, dup.getName());
				
				unclaimed.remove(code.toUpperCase());
			}
		}
	}
}
