// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.system.target.webdemo;

import java.io.IOException;
import java.io.OutputStream;
import net.multiphasicapps.squirreljme.build.projects.ProjectManager;
import net.multiphasicapps.squirreljme.build.system.target.AbstractTarget;
import net.multiphasicapps.squirreljme.build.system.target.TargetConfig;

/**
 * This is the target which generates the Web demo which runs on top of
 * Javascript.
 *
 * @since 2017/03/13
 */
public class WebDemoTarget
	extends AbstractTarget
{
	/**
	 * Initializes the target to the Web demo.
	 *
	 * @param __pm The projects available for usage.
	 * @param __conf The configuration to use during build.
	 * @param __os The stream where the output target is to be placed.
	 * @throws IOException On read/write errors.
	 * @since 2017/03/13
	 */
	public WebDemoTarget(ProjectManager __pm, TargetConfig __conf,
		OutputStream __os)
		throws IOException
	{
		super(__pm, __conf, __os);
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/13
	 */
	@Override
	public void run()
	{
		throw new todo.TODO();
	}
}

