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
import java.io.IOException;
import java.io.OutputStream;
import java.util.ServiceLoader;
import java.util.Set;
import net.multiphasicapps.collections.SortedTreeSet;
import net.multiphasicapps.javac.ZipCompilerOutput;

/**
 * This is the base class which is used to build distributions.
 *
 * The service loader is used.
 *
 * @since 2018/12/24
 */
public abstract class DistBuilder
{
	/** The name of this distribution. */
	protected final String name;
	
	/**
	 * Initializes the base distribution builder.
	 *
	 * @param __n The name of the distribution.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/12/24
	 */
	public DistBuilder(String __n)
		throws NullPointerException
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		this.name = __n;
	}
	
	/**
	 * Builds this distribution.
	 *
	 * @param __pm The project manager, needed to get resources.
	 * @param __os The output ZIP file.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/12/24
	 */
	public final void build(ProjectManager __pm, ZipCompilerOutput __out)
		throws IOException, NullPointerException
	{
		if (__pm == null || __out == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Returns the name of the distribution.
	 *
	 * @return The distribution name.
	 * @since 2018/12/24
	 */
	public final String name()
	{
		return this.name;
	}
	
	/**
	 * Locates the builder for the given name and returns it.
	 *
	 * @param __n The name of the builder to locate.
	 * @return The builder.
	 * @throws IllegalArgumentException If the builder was not found.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/12/24
	 */
	public static DistBuilder builder(String __n)
		throws IllegalArgumentException, NullPointerException
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// Find the one with the given name
		for (DistBuilder b : ServiceLoader.<DistBuilder>load(
			DistBuilder.class))
			if (__n.equals(b.name()))
				return b;
		
		// {@squirreljme.error AU17 No distribution exists under the given
		// name. (The distribution name)}
		throw new IllegalArgumentException("AU17 " + __n);
	}
	
	/**
	 * Returns the list of builders which are available.
	 *
	 * @return The list of available builders.
	 * @since 2018/12/24
	 */
	public static String[] listBuilders()
	{
		// Build list but make sure it is always sorted since that works
		// much better
		Set<String> rv = new SortedTreeSet<>();
		for (DistBuilder b : ServiceLoader.<DistBuilder>load(
			DistBuilder.class))
			rv.add(b.name());
		
		return rv.<String>toArray(new String[rv.size()]);
	}
}

