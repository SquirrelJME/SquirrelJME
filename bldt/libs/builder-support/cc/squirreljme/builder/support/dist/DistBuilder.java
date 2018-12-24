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
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.NoSuchFileException;
import java.util.Date;
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
	 * Performs specific build stuff.
	 *
	 * @param __pm The project manager used.
	 * @param __out The output ZIP.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/12/24
	 */
	protected abstract void specific(ProjectManager __pm,
		ZipCompilerOutput __out)
		throws IOException, NullPointerException;
	
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
		
		// Add some information about the build
		long buildtime = System.currentTimeMillis();
		DistBuilder.copyStrings(__out, "SQUIRRELJME-BUILD.MF",
			"Manifest-Version: 1.0",
			"Distribution-Name: " + this.name,
			"Build-Date: " + new Date(buildtime),
			"Build-Time: " + buildtime,
			"Build-Host-Java-Version: "
				+ System.getProperty("java.version"),
			"Build-Host-Java-Vendor: "
				+ System.getProperty("java.vendor"),
			"Build-Host-Java-Vendor-EMail: "
				+ System.getProperty("java.vendor.email"),
			"Build-Host-Java-Vendor-URL: "
				+ System.getProperty("java.vendor.url"),
			"Build-Host-Java-VM-Name: "
				+ System.getProperty("java.vm.name"),
			"Build-Host-Java-VM-Version: "
				+ System.getProperty("java.vm.version"),
			"Build-Host-SquirrelJME-APILevel: "
				+ System.getProperty("cc.squirreljme.apilevel"),
			"Build-Host-Java-VM-Vendor: "
				+ System.getProperty("java.vm.vendor"),
			"Build-Host-Java-VM-Vendor-EMail: "
				+ System.getProperty("java.vm.vendor.email"),
			"Build-Host-Java-VM-Vendor-URL: "
				+ System.getProperty("java.vm.vendor.url"),
			"Build-Host-Java-Runtime-Name: "
				+ System.getProperty("java.runtime.name"),
			"Build-Host-Java-Runtime-Version: "
				+ System.getProperty("java.runtime.version"),
			"Build-Host-OS-Name: "
				+ System.getProperty("os.name"),
			"Build-Host-OS-Arch: "
				+ System.getProperty("os.arch"),
			"Build-Host-OS-Version: "
				+ System.getProperty("os.version"));
		
		// Copy a bunch of root files which should always exist
		DistBuilder.copyRootFile(__out, "asruntime.mkd", __pm);
		DistBuilder.copyRootFile(__out, "building.mkd", __pm);
		DistBuilder.copyRootFile(__out, "changelog.mkd", __pm);
		DistBuilder.copyRootFile(__out, "code-of-conduct.mkd", __pm);
		DistBuilder.copyRootFile(__out, "compatibility.mkd", __pm);
		DistBuilder.copyRootFile(__out, "contributing.mkd", __pm);
		DistBuilder.copyRootFile(__out, "design.mkd", __pm);
		DistBuilder.copyRootFile(__out, "history.mkd", __pm);
		DistBuilder.copyRootFile(__out, "license.mkd", __pm);
		DistBuilder.copyRootFile(__out, "public-key.gpg.mkd", __pm);
		DistBuilder.copyRootFile(__out, "readme.mkd", __pm);
		DistBuilder.copyRootFile(__out, "scope.mkd", __pm);
		DistBuilder.copyRootFile(__out, "squirreljme-version", __pm);
		
		// Fossil specific stuff
		try
		{
			DistBuilder.copyRootFile(__out, "repo-manifest",
				__pm, "manifest");
			DistBuilder.copyRootFile(__out, "repo-revision",
				__pm, "manifest.uuid");
		}
		catch (NoSuchFileException e)
		{
		}
		
		// Do specific build stuff, which depends on the distrubution target.
		this.specific(__pm, __out);
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
	 * Copies the given bytes to the output.
	 *
	 * @param __out The output ZIP.
	 * @param __as The name to write the file as
	 * @param __b The bytes to store.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/12/24
	 */
	public static void copyBytes(ZipCompilerOutput __out, String __as,
		byte... __b)
		throws IOException, NullPointerException
	{
		DistBuilder.copyInputStream(__out, __as,
			new ByteArrayInputStream(__b));
	}
	
	/**
	 * Copies the given input stream to the output ZIP.
	 *
	 * @param __out The output ZIP.
	 * @param __as The name to write the file as
	 * @param __in The stream to read from.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/12/24
	 */
	public static void copyInputStream(ZipCompilerOutput __out, String __as,
		InputStream __in)
		throws IOException, NullPointerException
	{
		if (__out == null || __in == null || __as == null)
			throw new NullPointerException("NARG");
		
		// Setup output using the name we want to put it under
		try (OutputStream os = __out.output(__as))
		{
			byte[] buf = new byte[4096];
			for (;;)
			{
				int rc = __in.read(buf);
				
				if (rc < 0)
					break;
				
				os.write(buf, 0, rc);
			}
		}
	}
	
	/**
	 * Copies a file from the project manager root to the given ZIP output.
	 *
	 * @param __out The output ZIP.
	 * @param __as The name of the input file to load, it is named the in the
	 * ZIP as the name.
	 * @param __pm The project manager.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/12/24
	 */
	public static void copyRootFile(ZipCompilerOutput __out, String __as,
		ProjectManager __pm)
		throws IOException, NullPointerException
	{
		DistBuilder.copyRootFile(__out, __as, __pm, __as);
	}
	
	/**
	 * Copies a file from the project manager root to the given ZIP output.
	 *
	 * @param __out The output ZIP.
	 * @param __as The name the file should be in the target ZIP.
	 * @param __pm The project manager.
	 * @param __name The name of the input file.
	 * @throws IOException On read/write errors.
	 * @throws NoSuchFileException If the root file does not exist.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/12/24
	 */
	public static void copyRootFile(ZipCompilerOutput __out, String __as,
		ProjectManager __pm, String __name)
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
			
			// Copy the input
			DistBuilder.copyInputStream(__out, __as, in);
		}
	}
	
	/**
	 * Copies the given strings to the output. A CRLF is added at the end
	 * of each line.
	 *
	 * @param __out The output ZIP.
	 * @param __as The name to write the file as
	 * @param __s The strings to store.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/12/24
	 */
	public static void copyStrings(ZipCompilerOutput __out, String __as,
		String... __s)
		throws IOException, NullPointerException
	{
		DistBuilder.copyStrings("\r\n", __out, __as, __s);
	}
	
	/**
	 * Copies the given strings to the output.
	 *
	 * @param __eol The end of line sequence to use.
	 * @param __out The output ZIP.
	 * @param __as The name to write the file as
	 * @param __s The strings to store.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/12/24
	 */
	public static void copyStrings(String __eol,
		ZipCompilerOutput __out, String __as, String... __s)
		throws IOException, NullPointerException
	{
		if (__eol == null || __out == null || __as == null || __s == null)
			throw new NullPointerException("NARG");
		
		// Setup string which has all the lines
		StringBuilder sb = new StringBuilder();
		for (String s : __s)
			if (s != null)
			{
				sb.append(s);
				sb.append(__eol);
			}
		
		// Treat this file as a bunch of 
		DistBuilder.copyBytes(__out, __as, sb.toString().getBytes("utf-8"));
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

