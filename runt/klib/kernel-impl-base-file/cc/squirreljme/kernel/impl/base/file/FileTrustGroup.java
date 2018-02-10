// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.kernel.impl.base.file;

import cc.squirreljme.runtime.cldc.SystemTrustGroup;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import net.multiphasicapps.tool.manifest.JavaManifest;
import net.multiphasicapps.tool.manifest.JavaManifestKey;
import net.multiphasicapps.tool.manifest.writer.MutableJavaManifest;
import net.multiphasicapps.tool.manifest.writer.MutableJavaManifestAttributes;

/**
 * This represents a trust group which is backed by the file system.
 *
 * @since 2018/02/10
 */
public final class FileTrustGroup
	implements SystemTrustGroup
{
	/** Is this trust group trusted? */
	public static final JavaManifestKey IS_TRUSTED =
		new JavaManifestKey("Is-Trusted");
	
	/** The name this is associated with. */
	public static final JavaManifestKey NAME =
		new JavaManifestKey("Name");
	
	/** The vendor this is associated with. */
	public static final JavaManifestKey VENDOR =
		new JavaManifestKey("Vendor");
	
	/** Lock to prevent intertwined read/write. */
	protected final Object lock =
		new Object();
	
	/** The path to the group information. */
	protected final Path path;
	
	/** The index of this group. */
	protected final int index;
	
	/**
	 * Initializes the file trust group.
	 *
	 * @param __p The path to the group.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/02/10
	 */
	FileTrustGroup(Path __p)
		throws NullPointerException
	{
		if (__p == null)
			throw new NullPointerException("NARG");
		
		this.path = __p;
		this.index = Integer.parseInt(__p.getFileName().toString(), 10);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/02/10
	 */
	@Override
	public final void checkPermission(String __cl, String __n, String __a)
		throws NullPointerException, SecurityException
	{
		if (__cl == null || __n == null || __a == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/02/10
	 */
	@Override
	public final int index()
	{
		return this.index;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/02/10
	 */
	@Override
	public final boolean isTrusted()
	{
		return Boolean.valueOf(this.__get(FileTrustGroup.IS_TRUSTED));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/02/10
	 */
	@Override
	public final String name()
	{
		return this.__get(FileTrustGroup.NAME);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/02/10
	 */
	@Override
	public final String vendor()
	{
		return this.__get(FileTrustGroup.VENDOR);
	}
	
	/**
	 * Gets the given value.
	 *
	 * @param __k The key to get the value for.
	 * @return The value for the given key.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/02/10
	 */
	final String __get(JavaManifestKey __k)
		throws NullPointerException
	{
		if (__k == null)
			throw new NullPointerException("NARG");
		
		return this.__openManifest().getMainAttributes().get(__k);
	}
	
	/**
	 * Opens the manifest for this trust.
	 *
	 * @return The trust manifest data.
	 * @since 2018/02/10
	 */
	final JavaManifest __openManifest()
	{
		// Lock to prevent mixed read/write
		synchronized (this.lock)
		{
			// Could fail
			try (InputStream in = Files.newInputStream(this.path,
				StandardOpenOption.READ))
			{
				return new JavaManifest(in);
			}
		
			// Does not exist, use a blank manifest
			catch (NoSuchFileException e)
			{
				return new JavaManifest();
			}
		
			// {@squirreljme.error BH03 Failed to read the trust manifest.}
			catch (IOException e)
			{
				throw new RuntimeException("BH03", e);
			}
		}
	}
	
	/**
	 * Opens the mutable manifest so that it may be modified.
	 *
	 * @return The mutable manifest for writing.
	 * @since 2018/02/10
	 */
	final MutableJavaManifest __openMutableManifest()
	{
		return new MutableJavaManifest(this.__openManifest());
	}
	
	/**
	 * Sets the given key to the specified value.
	 *
	 * @param __k The key to set.
	 * @param __v The value to set, {@code null} clears it.
	 * @throws NullPointerException If no key was specified.
	 * @since 2018/02/10
	 */
	final void __set(JavaManifestKey __k, String __v)
		throws NullPointerException
	{
		if (__k == null)
			throw new NullPointerException("NARG");
		
		// Lock to prevent mixed read/write
		synchronized (this.lock)
		{
			MutableJavaManifest man = this.__openMutableManifest();
			MutableJavaManifestAttributes attr = man.getMainAttributes();
			
			if (__v == null)
				attr.remove(__k);
			else
				attr.put(__k, __v);
			
			this.__writeManifest(man);
		}
	}
	
	/**
	 * Writes the manifest to the target file.
	 *
	 * @param __man The manifest to write.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/02/10
	 */
	final void __writeManifest(MutableJavaManifest __man)
		throws NullPointerException
	{
		if (__man == null)
			throw new NullPointerException("NARG");
		
		// Lock to prevent mixed read/write
		synchronized (this.lock)
		{
			Path temp = null;
			try
			{
				// Need temporary file for working replacement
				temp = Files.createTempFile("trust", ".MF");
				try (OutputStream os = Files.newOutputStream(temp,
					StandardOpenOption.TRUNCATE_EXISTING,
					StandardOpenOption.WRITE))
				{
					__man.write(os);
				}
				
				// Overwrite file with new manifest
				Path path = this.path;
				Files.createDirectories(path.getParent());
				Files.move(temp, path,
					StandardCopyOption.REPLACE_EXISTING);
			}
			
			// {@squirreljme.error BH04 Could not write the manifest.}
			catch (IOException e)
			{
				throw new RuntimeException("BH04", e);
			}
			
			// Make sure temporary file is cleaned up
			finally
			{
				if (temp != null)
					try
					{
						Files.delete(temp);
					}
					catch (IOException e)
					{
					}
			}
		}
	}
}

