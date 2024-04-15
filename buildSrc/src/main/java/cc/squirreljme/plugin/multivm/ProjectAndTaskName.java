// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import org.gradle.api.Project;
import org.gradle.api.Task;

/**
 * This holds the name of a project and task and is used for storing them
 * for resolution as needed.
 * 
 * This class is immutable.
 *
 * @since 2020/08/15
 */
public final class ProjectAndTaskName
{
	/** The project name. */
	protected final String project;
	
	/** The task name. */
	protected final String task;
	
	/**
	 * Initializes the holder.
	 * 
	 * @param __project The project name.
	 * @param __task The task name.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/15
	 */
	public ProjectAndTaskName(String __project, String __task)
		throws NullPointerException
	{
		if (__project == null || __task == null)
			throw new NullPointerException("NARG");
		
		this.project = __project;
		this.task = __task;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/08/15
	 */
	@Override
	public int hashCode()
	{
		return this.project.hashCode() ^
			this.task.hashCode();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/08/15
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (__o == this)
			return true;
		
		if (!(__o instanceof ProjectAndTaskName))
			return false;
		
		ProjectAndTaskName o = (ProjectAndTaskName)__o;
		return this.project.equals(o.project) &&
			this.task.equals(o.task);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/08/15
	 */
	@Override
	public String toString()
	{
		return this.project + ":" + this.task;
	}
	
	/**
	 * Maps the project and task name.
	 * 
	 * @param __project The project to get from.
	 * @param __task The task to use.
	 * @return The project and task name.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/15
	 */
	public static ProjectAndTaskName of(Project __project, String __task)
		throws NullPointerException
	{
		if (__project == null || __task == null)
			throw new NullPointerException("NARG");
		
		return new ProjectAndTaskName(__project.getPath(), __task);
	}
	
	/**
	 * Maps from a task.
	 * 
	 * @param __task The task to use.
	 * @return The project and task name.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/15
	 */
	public static ProjectAndTaskName of(Task __task)
		throws NullPointerException
	{
		if (__task == null)
			throw new NullPointerException("NARG");
		
		return ProjectAndTaskName.of(__task.getProject(), __task.getName()); 
	}
}
