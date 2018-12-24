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
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.NoSuchFileException;
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
		
		// Copy a bunch of root files which should always exist
		DistBuilder.copyRootFile(__out, __pm, "asruntime.mkd");
		DistBuilder.copyRootFile(__out, __pm, "building.mkd");
		DistBuilder.copyRootFile(__out, __pm, "changelog.mkd");
		DistBuilder.copyRootFile(__out, __pm, "code-of-conduct.mkd");
		DistBuilder.copyRootFile(__out, __pm, "compatibility.mkd");
		DistBuilder.copyRootFile(__out, __pm, "contributing.mkd");
		DistBuilder.copyRootFile(__out, __pm, "design.mkd");
		DistBuilder.copyRootFile(__out, __pm, "history.mkd");
		DistBuilder.copyRootFile(__out, __pm, "license.mkd");
		DistBuilder.copyRootFile(__out, __pm, "public-key.gpg.mkd");
		DistBuilder.copyRootFile(__out, __pm, "readme.mkd");
		DistBuilder.copyRootFile(__out, __pm, "scope.mkd");
		DistBuilder.copyRootFile(__out, __pm, "squirreljme-version");
		
		// Fossil specific stuff
		try
		{
			DistBuilder.copyRootFile(__out, __pm, "manifest",
				"repo-manifest");
			DistBuilder.copyRootFile(__out, __pm, "manifest.uuid",
				"repo-revision");
		}
		catch (NoSuchFileException e)
		{
		}
		
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
	 * Copies a file from the project manager root to the given ZIP output.
	 *
	 * @param __out The output ZIP.
	 * @param __pm The project manager.
	 * @param __name The name of the input file.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/12/24
	 */
	public static void copyRootFile(ZipCompilerOutput __out,
		ProjectManager __pm, String __name)
		throws IOException, NullPointerException
	{
		DistBuilder.copyRootFile(__out, __pm, __name, __name);
	}
	
	/**
	 * Copies a file from the project manager root to the given ZIP output.
	 *
	 * @param __out The output ZIP.
	 * @param __pm The project manager.
	 * @param __name The name of the input file.
	 * @param __as The name the file should be in the target ZIP.
	 * @throws IOException On read/write errors.
	 * @throws NoSuchFileException If the root file does not exist.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/12/24
	 */
	public static void copyRootFile(ZipCompilerOutput __out,
		ProjectManager __pm, String __name, String __as)
		throws IOException, NoSuchFileException, NullPointerException
	{
		if (__out == null || __pm == null || __name == null || __as == null)
			throw new NullPointerException("NARG");
		
		// Source the files from the project root always
		try (InputStream in = __pm.rootFile(__name))
		{
			// {@squirreljme.error AU1d The root file does not exist.
			// (The name of the file)}
			if (in == null)
				throw new NoSuchFileException("AU1d " + __name);
			
			// Setup output using the name we want to put it under
			try (OutputStream os = __out.output(__as))
			{
				byte[] buf = new byte[4096];
				for (;;)
				{
					int rc = in.read(buf);
					
					if (rc < 0)
						break;
					
					os.write(buf, 0, rc);
				}
			}
		}
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

