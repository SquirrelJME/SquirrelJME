// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.gcf.file.pseudo;

import cc.squirreljme.jvm.mle.JarPackageShelf;
import cc.squirreljme.jvm.mle.RawJarPackageBracketInputStream;
import cc.squirreljme.jvm.mle.brackets.JarPackageBracket;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.full.attrib.ExtraFileAttributes;
import cc.squirreljme.runtime.gcf.file.FileEndPoint;
import cc.squirreljme.runtime.gcf.uri.UriGenericPart;
import cc.squirreljme.runtime.gcf.uri.UriPart;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A connection to a specific library to browse its contents as if it were
 * a filesystem.
 *
 * @since 2025/12/27
 */
@SquirrelJMEVendorApi
public class LibraryEndPoint
	extends FileEndPoint
{
	/** Decoded host. */
	@SquirrelJMEVendorApi
	public static final String DECODED_HOST =
		"!?x-squirreljme-library://?!";
	
	/** Host. */
	@SquirrelJMEVendorApi
	public static final String HOST =
		"!%3Fx-squirreljme-library%3A%2F%2F%3F!";
	
	/** The Jar being accessed. */
	@SquirrelJMEVendorApi
	protected final JarPackageBracket jar;
	
	/** The cached library listing. */
	private volatile String[] _listing;
	
	/**
	 * Initializes the library connection.
	 *
	 * @param __jar The Jar to access.
	 * @param __part The initial part.
	 * @param __mode The mode this is opened in.
	 * @param __dotDot The optional parent directory to return to.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/12/27
	 */
	@SquirrelJMEVendorApi
	public LibraryEndPoint(@NotNull JarPackageBracket __jar,
		UriGenericPart __part, int __mode, @Nullable UriGenericPart __dotDot)
		throws NullPointerException
	{
		super(__part, __mode, __dotDot);
		
		if (__jar == null)
			throw new NullPointerException("NARG");
		
		this.jar = __jar;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/30
	 */
	@Override
	protected final ExtraFileAttributes attachedAttributes()
		throws SecurityException
	{
		// This could be a directory or a file
		if (this.part.getPath().endsWith("/"))
			return PseudoAttributes.DIRECTORY;
		return PseudoAttributes.FILE;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/30
	 */
	@Override
	protected FileStore attachedFileStore()
		throws SecurityException
	{
		// No filesystem is ever attached
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/27
	 */
	@Override
	protected FileSystem attachedFileSystem()
		throws SecurityException
	{
		// No filesystem is attached
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/30
	 */
	@Override
	public void close()
		throws IOException
	{
		synchronized (this)
		{
			// Dereference the listing
			this._listing = null;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/30
	 */
	@Override
	protected void listDirectory(@NotNull Map<String, UriGenericPart> __into)
		throws IOException, NullPointerException, SecurityException
	{
		if (__into == null)
			throw new NullPointerException("NARG");
		
		// Get the listing
		String[] listing;
		synchronized (this)
		{
			// Get the Jar listing
			listing = this._listing;
			if (listing == null)
			{
				listing = JarPackageShelf.list(this.jar);
				this._listing = listing;
			}
		}
		
		// Where is this?
		UriGenericPart part = this.part;
		String path = part.getPath();
		
		// Override dot-dot?
		UriGenericPart dotDot = this.dotDot;
		if (dotDot != null)
			__into.put("..", dotDot);
		else
		{
			// Parent directory at the root points to all volumes, otherwise
			// it points to the directory above
			if (path.equals("/"))
			{
				__into.put("..", new UriGenericPart("//" +
					AllVolumesEndPoint.HOST + "/"));
			}
			
			// Otherwise, strip a component
			else
			{
				int ls = path.lastIndexOf('/', path.length() - 2);
				__into.put("..",
					part.withPath(path.substring(0, ls) + "/"));
			}
		}
		
		// No listing? Just list nothing then
		if (listing == null)
			return;
		
		// Add all items which exist within this path directory, note that
		// this needs to be filtered
		path = path.substring(1);
		for (String rc : listing)
		{
			// Does not start with this, so cannot be in the subtree
			if (!rc.startsWith(path))
				continue;
			
			// Strip the entire start
			String rcSub = rc.substring(path.length());
			
			// Is this a directory or in a subdirectory?
			int fs = rcSub.indexOf('/');
			if (fs >= 0)
			{
				// Strip to the directory
				String rcDir = rcSub.substring(0, fs + 1);
				if (!__into.containsKey(rcDir))
					__into.put(rcDir, part.withPath(
						rc.substring(0, path.length()) + rcDir));
			}
			
			// Normal file
			else
				__into.put(rcSub, part.withPath(rc));
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/01/01
	 */
	@Override
	protected InputStream openInputStream()
		throws IOException, SecurityException
	{
		if (this.isDirectory())
		{
			// If this is the root, allow the entire Jar to be read as
			// a block device
			if ("/".equals(this.part.getPath()))
				return new RawJarPackageBracketInputStream(this.jar);
			throw new IOException("ADIR");
		}
		
		// Open a stream, note that the resource might not actually exist
		InputStream rc = JarPackageShelf.openResource(this.jar,
			this.part.getPath().substring(1));
		if (rc == null)
			throw new IOException("FNFE");
		
		return rc;
	}
	
	/**
	 * Returns the URI part that would be used for the given library.
	 *
	 * @param __library The arguments
	 * @param __fileName Optional output file name, before any encoding.
	 * @return The URI part for the given library.
	 * @throws NullPointerException On null arguments.
	 * @since 2026/01/08
	 */
	@SquirrelJMEVendorApi
	public static UriGenericPart libraryPart(JarPackageBracket __library,
		@Nullable String[] __fileName)
		throws NullPointerException
	{
		if (__library == null)
			throw new NullPointerException("NARG");
		
		// The ID is used if there is no name
		int id = JarPackageShelf.libraryId(__library);
		
		// Is there a name for this? We just want the base name
		String baseName = JarPackageShelf.libraryPath(__library);
		if (baseName != null && !baseName.isEmpty())
		{
			// Slash strip
			int ls = Math.max(baseName.lastIndexOf('/'),
				baseName.lastIndexOf('\\'));
			if (ls >= 0)
				baseName = baseName.substring(ls + 1);
		}
		
		// Determine the base filename that is used
		String fileName;
		if (baseName == null || baseName.isEmpty())
			fileName = id + "/";
		else
			fileName = UriPart.encode(baseName) + "/";
		
		// Determine the normal library name
		if (__fileName != null && __fileName.length > 0)
			__fileName[0] = fileName;
		return new UriGenericPart(
			"//" + LibraryEndPoint.HOST + fileName);
	}
}
