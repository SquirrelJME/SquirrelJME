// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import net.multiphasicapps.zip.streamreader.ZipStreamReader;

/**
 * A class library which is backed by a JAR file on the disk.
 *
 * @since 2020/04/19
 */
public class JarClassLibrary
	implements VMClassLibrary
{
	/** The path of the library. */
	protected final Path path;
	
	/** The base class library as loaded into memory. */
	private VMClassLibrary _loaded;
	
	/**
	 * Initializes the class library.
	 *
	 * @param __path The path to the library.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/04/19
	 */
	public JarClassLibrary(Path __path)
		throws NullPointerException
	{
		if (__path == null)
			throw new NullPointerException("NARG");
		
		this.path = __path;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/04/19
	 */
	@Override
	public String[] listResources()
	{
		try
		{
			return this.__load().listResources();
		}
		catch (IOException e)
		{
			// {@squirreljme.error AK01 Could not read contents. (Jar Path)}
			throw new RuntimeException("AK01 " + this.path, e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/04/19
	 */
	@Override
	public String name()
	{
		return this.path.getFileName().toString();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/06/13
	 */
	@Override
	public Path path()
	{
		return this.path;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/04/19
	 */
	@Override
	public InputStream resourceAsStream(String __rc)
		throws IOException, NullPointerException
	{
		return this.__load().resourceAsStream(__rc);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/06/13
	 */
	@Override
	public final String toString()
	{
		return this.path.toString();
	}
	
	/**
	 * Loads the library into memory.
	 *
	 * @return The loaded library.
	 * @throws IOException If it could not be read.
	 * @since 2020/04/19
	 */
	private VMClassLibrary __load()
		throws IOException
	{
		// Already pre-cached?
		VMClassLibrary rv = this._loaded;
		if (rv != null)
			return rv;
		
		Path path = this.path;
		try (InputStream in = Files.newInputStream(path,
			StandardOpenOption.READ);
			ZipStreamReader zip = new ZipStreamReader(in))
		{
			this._loaded = (rv = InMemoryClassLibrary.loadZip(
				path.getFileName().toString(), zip));
			return rv;
		}
	}
	
	/**
	 * Checks if this is a JAR or not.
	 * 
	 * @param __s The file name.
	 * @return If this is a JAR.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/06/13
	 */
	public static boolean isJar(Path __s)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		return JarClassLibrary.isJar(__s.toString());
	}
	
	/**
	 * Checks if this is a JAR or not.
	 * 
	 * @param __s The file name.
	 * @return If this is a JAR.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/06/13
	 */
	public static boolean isJar(String __s)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		return __s.endsWith(".jar") || __s.endsWith(".JAR") ||
			__s.endsWith(".kjx") || __s.endsWith(".KJX");
	}
	
	/**
	 * Returns either a {@link JarClassLibrary} or a
	 * {@link DirectoryClassLibrary} depending on if the given path is
	 * not a directory or is one.
	 *
	 * @param __path The path to evaluate and check.
	 * @return A class library for the given path.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/04/19
	 */
	public static VMClassLibrary of(Path __path)
		throws NullPointerException
	{
		if (__path == null)
			throw new NullPointerException("NARG");
		
		if (Files.isDirectory(__path))
			return new DirectoryClassLibrary(__path);
		return new JarClassLibrary(__path);
	}
}
