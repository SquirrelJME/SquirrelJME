// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.builder.support.dist;

import cc.squirreljme.builder.support.ProjectManager;
import cc.squirreljme.builder.support.TimeSpaceType;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.io.OutputStream;
import net.multiphasicapps.javac.ZipCompilerOutput;

/**
 * This is used to build the packed SummerCoat ROM file.
 *
 * @since 2019/05/28
 */
public class SummerCoatROM
	extends DistBuilder
{
	/**
	 * Initializes the service.
	 *
	 * @since 2019/05/28
	 */
	public SummerCoatROM()
	{
		super("summercoatrom");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/28
	 */
	@Override
	protected void specific(ProjectManager __pm, ZipCompilerOutput __zip)
		throws IOException, NullPointerException
	{
		throw new todo.TODO();
	}
}

