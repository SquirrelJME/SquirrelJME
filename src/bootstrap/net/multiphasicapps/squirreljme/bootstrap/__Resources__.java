// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.bootstrap;

import java.io.InputStream;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.List;
import net.multiphasicapps.squirreljme.bootstrap.base.launcher.
	ResourceAccessor;
import net.multiphasicapps.squirreljme.bootstrap.base.launcher.
	ResourceReference;
import net.multiphasicapps.squirreljme.projects.ProjectGroup;
import net.multiphasicapps.squirreljme.projects.ProjectInfo;
import net.multiphasicapps.squirreljme.projects.ProjectList;
import net.multiphasicapps.squirreljme.projects.ProjectName;
import net.multiphasicapps.squirreljme.projects.ProjectType;

/**
 * Provides access to resources provided by a project along with all of its
 * dependencies.
 *
 * @since 2016/09/20
 */
class __Resources__
	implements ResourceAccessor
{
	/** Projects to read resources from. */
	private final ProjectInfo[] _from;
	
	/**
	 * Initializes the resource lookup.
	 *
	 * @param __bin The binary project to source resources from.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/20
	 */
	__Resources__(ProjectInfo __bin)
		throws NullPointerException
	{
		// Check
		if (__bin == null)
			throw new NullPointerException("NARG");
		
		// Set
		this._from = __bin.projectList().recursiveDependencies(
			ProjectType.BINARY, __bin.name(), true).<ProjectInfo>toArray(
			new ProjectInfo[0]);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/20
	 */
	@Override
	public InputStream open(String __n)
		throws NullPointerException
	{
		// Check
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// Browse through all projects
		for (ProjectInfo info : this._from)
			try
			{
				// Check the content set first since that is cached, otherwise
				// if the package is a ZIP and only one thing streams from it
				// at a time then it will be opened and closed many times
				if (info.contents().contains(__n))
					return info.open(__n);
			}
			
			// Ignore if it does not exist
			catch (NoSuchFileException e)
			{
			}
			
			// {@squirreljme.error CL06 Failed to access the resource. (The
			// name of the resource)}
			catch (IOException e)
			{
				throw new RuntimeException(String.format("CL06 %s", __n), e);
			}
		
		// Not found
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/21
	 */
	@Override
	public Iterable<ResourceReference> reference(String __n)
	{
		// Check
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// Return value
		List<ResourceReference> rv = new ArrayList<>();
		
		// Browse through all projects
		for (ProjectInfo info : this._from)
			try
			{
				// If the project contains the resource then refer to it later
				if (info.contents().contains(__n))
					rv.add(new __Reference__(info, __n));
			}
			
			// Ignore
			catch (IOException e)
			{
			}
		
		// return
		return rv;
	}
}

