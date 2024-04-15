// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.util;

import cc.squirreljme.plugin.multivm.VMHelpers;
import lombok.ToString;
import org.gradle.api.Project;
import org.gradle.api.artifacts.ProjectDependency;
import org.gradle.api.capabilities.Capability;
import org.gradle.api.tasks.SourceSet;

/**
 * Stores a dependency and a source set for proper dependency usage.
 *
 * @since 2022/08/07
 */
@ToString
public class ProjectAndSourceSet
	implements Comparable<ProjectAndSourceSet>
{
	/** The project dependency. */
	public final Project dependency;
	
	/** The source set used. */
	public final String sourceSet;
	
	/**
	 * Initializes the container.
	 * 
	 * @param __depend The dependency to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/08/07
	 * 
	 */
	public ProjectAndSourceSet(ProjectDependency __depend)
		throws NullPointerException
	{
		this(__depend, ProjectAndSourceSet.__getSourceSet(__depend));
	}
	
	/**
	 * Initializes the container.
	 * 
	 * @param __depend The dependency to use.
	 * @param __sourceSet The source set to target.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/08/07
	 */
	public ProjectAndSourceSet(ProjectDependency __depend,
		String __sourceSet)
		throws NullPointerException
	{
		this(__depend.getDependencyProject(), __sourceSet);
	}
	
	/**
	 * Initializes the container.
	 * 
	 * @param __depend The dependency to use.
	 * @param __sourceSet The source set to target.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/08/07
	 */
	public ProjectAndSourceSet(Project __depend, String __sourceSet)
		throws NullPointerException
	{
		if (__depend == null || __sourceSet == null)
			throw new NullPointerException("NARG");
		
		this.dependency = __depend;
		this.sourceSet = __sourceSet;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/08/07
	 */
	@Override
	public int compareTo(ProjectAndSourceSet __b)
	{
		// Compare by name first
		int byName = this.dependency.getPath()
			.compareTo(__b.dependency.getPath());
		if (byName != 0)
			return byName;
		
		// Then by the source set
		return this.sourceSet.compareTo(__b.sourceSet);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/08/07
	 */
	 @Override
	 public boolean equals(Object __o)
	 {
	 	if (__o == this)
	 		return true;
	 	
	 	if (!(__o instanceof ProjectAndSourceSet))
	 		return false;
	 	
	 	// Is this the same project?
	 	ProjectAndSourceSet o = (ProjectAndSourceSet)__o;
	 	return this.sourceSet.equals(o.sourceSet) &&
	 		this.dependency.compareTo(o.dependency) == 0;
	 }
	
	/**
	 * {@inheritDoc}
	 * @since 2022/08/07
	 */
	 @Override
	 public int hashCode()
	 {
	 	return this.dependency.getPath().hashCode() ^
	 		this.sourceSet.hashCode();
	 }
	
	/**
	 * Determines if this dependency is for a given source set or not.
	 * 
	 * @param __depend The dependency to get from.
	 * @return The source set.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/08/07
	 */
	private static String __getSourceSet(ProjectDependency __depend)
		throws NullPointerException
	{
		if (__depend == null)
			throw new NullPointerException("NARG");
		
		// Use test fixtures source set if the capability has this
		for (Capability capability : __depend.getRequestedCapabilities())
			if (capability.getName()
				.equals(__depend.getName() + "-test-fixtures"))
				return VMHelpers.TEST_FIXTURES_SOURCE_SET_NAME;
		
		// Assume main source set otherwise
		return SourceSet.MAIN_SOURCE_SET_NAME;
	}
}
