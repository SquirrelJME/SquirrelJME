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
 * This contains utilities for reading and writing manifests.
 *
 * @since 2018/02/11
 */
final class __Utils__
{
	/**
	 * Not used.
	 *
	 * @since 2018/02/11
	 */
	private __Utils__()
	{
	}
	
	/**
	 * Gets the given value.
	 *
	 * @param __lock The locking object.
	 * @param __p The manifest path.
	 * @param __k The key to get the value for.
	 * @return The value for the given key.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/02/10
	 */
	static final String __get(Object __lock, Path __p, JavaManifestKey __k)
		throws NullPointerException
	{
		if (__lock == null || __p == null || __k == null)
			throw new NullPointerException("NARG");
		
		return __openManifest(__lock, __p).getMainAttributes().get(__k);
	}
	
	/**
	 * Opens the manifest for this trust.
	 *
	 * @param __lock The locking object.
	 * @param __p The manifest path.
	 * @return The trust manifest data.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/02/10
	 */
	static final JavaManifest __openManifest(Object __lock, Path __p)
		throws NullPointerException
	{
		if (__lock == null || __p == null)
			throw new NullPointerException("NARG");
		
		// Lock to prevent mixed read/write
		synchronized (__lock)
		{
			// Could fail
			try (InputStream in = Files.newInputStream(__p,
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
	 * @param __lock The locking object.
	 * @param __p The manifest path.
	 * @return The mutable manifest for writing.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/02/10
	 */
	static final MutableJavaManifest __openMutableManifest(Object __lock,
		Path __p)
		throws NullPointerException
	{
		if (__lock == null || __p == null)
			throw new NullPointerException("NARG");
		
		return new MutableJavaManifest(__openManifest(__lock, __p));
	}
	
	/**
	 * Sets the given key to the specified value.
	 *
	 * @param __lock The locking object.
	 * @param __p The manifest path.
	 * @param __k The key to set.
	 * @param __v The value to set, {@code null} clears it.
	 * @throws NullPointerException If no key was specified.
	 * @since 2018/02/10
	 */
	static final void __set(Object __lock, Path __p, JavaManifestKey __k,
		String __v)
		throws NullPointerException
	{
		if (__lock == null || __p == null || __k == null)
			throw new NullPointerException("NARG");
		
		// Lock to prevent mixed read/write
		synchronized (__lock)
		{
			MutableJavaManifest man = __openMutableManifest(__lock, __p);
			MutableJavaManifestAttributes attr = man.getMainAttributes();
			
			if (__v == null)
				attr.remove(__k);
			else
				attr.put(__k, __v);
			
			__writeManifest(__lock, __p, man);
		}
	}
	
	/**
	 * Writes the manifest to the target file.
	 *
	 * @param __lock The locking object.
	 * @param __p The manifest path.
	 * @param __man The manifest to write.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/02/10
	 */
	static final void __writeManifest(Object __lock, Path __p,
		MutableJavaManifest __man)
		throws NullPointerException
	{
		if (__lock == null || __p == null || __man == null)
			throw new NullPointerException("NARG");
		
		// Lock to prevent mixed read/write
		synchronized (__lock)
		{
			Path temp = null;
			try
			{
				// Need temporary file for working replacement
				temp = Files.createTempFile("file", ".MF");
				try (OutputStream os = Files.newOutputStream(temp,
					StandardOpenOption.TRUNCATE_EXISTING,
					StandardOpenOption.WRITE))
				{
					__man.write(os);
				}
				
				// Overwrite file with new manifest
				Files.createDirectories(__p.getParent());
				Files.move(temp, __p, StandardCopyOption.REPLACE_EXISTING);
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

