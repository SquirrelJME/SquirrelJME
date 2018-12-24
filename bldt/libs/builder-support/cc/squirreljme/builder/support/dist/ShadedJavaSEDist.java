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

/**
 * Distribution for building shaded Java SE JARs.
 *
 * @since 2018/12/24
 */
public class ShadedJavaSEDist
	extends DistBuilder
{
	/**
	 * Initializes the distribution.
	 *
	 * @since 2018/12/24
	 */
	public ShadedJavaSEDist()
	{
		super("shaded-javase");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/24
	 */
	@Override
	protected void specific(ProjectManager __pm, ZipCompilerOutput __out)
		throws IOException, NullPointerException
	{
		if (__pm == null || __out == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

