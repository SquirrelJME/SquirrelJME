// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.builder.support.dist;

import cc.squirreljme.builder.support.ProjectManager;
import cc.squirreljme.builder.support.TimeSpaceType;
import cc.squirreljme.builder.support.vmshader.Shader;
import java.io.IOException;
import java.io.OutputStream;
import net.multiphasicapps.javac.ZipCompilerOutput;

/**
 * Distribution for building shaded Java ME JARs.
 *
 * @since 2018/12/24
 */
public class ShadedJavaMEDist
	extends DistBuilder
{
	/**
	 * Initializes the distribution.
	 *
	 * @since 2018/12/24
	 */
	public ShadedJavaMEDist()
	{
		super("javame");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/24
	 */
	@Override
	protected void specific(ProjectManager __pm, ZipCompilerOutput __zip)
		throws IOException, NullPointerException
	{
		if (__pm == null || __zip == null)
			throw new NullPointerException("NARG");
		
		// Write to the output ZIP
		try (OutputStream out = __zip.output("squirreljme-javame.jar"))
		{
			Shader.shade(__pm, TimeSpaceType.RUNTIME, false,
				null, out);
		}
	}
}

