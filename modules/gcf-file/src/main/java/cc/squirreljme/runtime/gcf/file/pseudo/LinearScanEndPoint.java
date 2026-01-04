// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.gcf.file.pseudo;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.full.attrib.ExtraFileAttributes;
import cc.squirreljme.runtime.cldc.util.StreamUtils;
import cc.squirreljme.runtime.gcf.ContentTypeUtil;
import cc.squirreljme.runtime.gcf.file.FileEndPoint;
import cc.squirreljme.runtime.gcf.uri.UriGenericPart;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.microedition.io.InputConnection;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This is a file end point that scans through a file to detect magic numbers
 * and provides access to those regions as files. This is so that certain
 * types of files such as DoJa Scratchpads can be accessed via the media
 * player despite not a file structure.
 * 
 * Note that while this works, it is not the most efficient means of accessing
 * the contents of arbitrary blocks of data.
 *
 * @since 2026/01/02
 */
@SquirrelJMEVendorApi
public class LinearScanEndPoint
	extends FileEndPoint
{
	/** The number of bytes to scan attempt at once. */
	@SquirrelJMEVendorApi
	public static final int SCAN_LEN =
		12;
	
	/** The number of bytes to skip at once. */
	@SquirrelJMEVendorApi
	public static final int SKIP =
		4;
	
	/** Host. */
	@SquirrelJMEVendorApi
	public static final String HOST =
		"!%3Fx-squirreljme-linear-scan%3A%2F%2F%3F!";
	
	/** Decoded host. */
	@SquirrelJMEVendorApi
	public static final String DECODED_HOST =
		"!?x-squirreljme-linear-scan://?!";
	
	/** The connection to wrap. */
	@SquirrelJMEVendorApi
	protected final InputConnection wrapped;
	
	/** The scanned contents and magic numbers. */
	private volatile String[] _contents;
	
	/**
	 * Initializes the endpoint.
	 *
	 * @param __part The URI part of the endpoint.
	 * @param __mode The mode the endpoint is opened in.
	 * @param __wrapped The wrapped connection.
	 * @param __dotDot The optional parent directory to return to.
	 * @throws NullPointerException On null arguments.
	 * @since 2026/01/02
	 */
	@SquirrelJMEVendorApi
	public LinearScanEndPoint(@NotNull UriGenericPart __part, int __mode,
		InputConnection __wrapped, @Nullable UriGenericPart __dotDot)
		throws NullPointerException
	{
		super(__part, __mode, __dotDot);
		
		if (__wrapped == null)
			throw new NullPointerException("NARG");
		
		this.wrapped = __wrapped;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/01/03
	 */
	@Override
	protected ExtraFileAttributes attachedAttributes()
		throws SecurityException
	{
		if (this.part.getPath().endsWith("/"))
			return PseudoAttributes.DIRECTORY;
		return PseudoAttributes.FILE;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/01/03
	 */
	@Override
	protected FileStore attachedFileStore()
		throws SecurityException
	{
		// Not attached to a filesystem
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/01/03
	 */
	@Override
	protected FileSystem attachedFileSystem()
		throws SecurityException
	{
		// Not attached to a filesystem
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/01/03
	 */
	@Override
	public void close()
		throws IOException
	{
		// Close the wrapped connection
		this.wrapped.close();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/01/03
	 */
	@Override
	protected void listDirectory(@NotNull Map<String, UriGenericPart> __into)
		throws IOException, NullPointerException, SecurityException
	{
		if (__into == null)
			throw new NullPointerException("NARG");
		
		// Only add dot-dot if it is known
		UriGenericPart dotDot = this.dotDot;
		if (dotDot != null)
			__into.put("..", dotDot);
		
		// Only root is valid
		UriGenericPart part = this.part;
		if (!"/".equals(part.getPath()))
			return;
		
		// Have the contents already been determined?
		String[] contents;
		synchronized (this)
		{
			contents = this._contents;
		}
		
		// Are there contents cached?
		if (contents != null)
		{
			// Load in contents
			for (String item : contents)
				__into.put(item, part.withPath(item));
			
			// No more processing is needed
			return;
		}
		
		// Scanning is a very inefficient process, read everything all at
		// once
		byte[] entireChunk;
		try (InputStream in = this.wrapped.openInputStream())
		{
			entireChunk = StreamUtils.readAll(in);
		}
		
		// Scan length and where to write for the skip
		int scanLen = LinearScanEndPoint.SCAN_LEN;
		int skip = LinearScanEndPoint.SKIP;
		
		// The last found magic
		@Language("mime-type-reference")
		String lastMagic = null;
		int lastMagicPos = -1;
		
		// Scan and detect magic numbers
		List<String> buildContent = new ArrayList<>();
		for (int checkAt = 0, limit = entireChunk.length - scanLen;
			checkAt < limit; checkAt++)
		{
			// Try to find a magic number
			@Language("mime-type-reference")
			String magic = ContentTypeUtil.guess(entireChunk,
				checkAt, scanLen);
			
			// Do not allow plain text to be detected
			if ("text/plain".equals(magic))
				magic = null;
			
			// Was a new magic detected?
			if (magic != null)
			{
				// Was there a previous magic?
				if (lastMagic != null)
					LinearScanEndPoint.__add(buildContent,
						lastMagic, lastMagicPos, checkAt - lastMagicPos);
				
				// Remember this
				lastMagic = magic;
				lastMagicPos = checkAt;
			}
		}
		
		// Final magic number in file?
		if (lastMagic != null)
			LinearScanEndPoint.__add(buildContent, lastMagic, lastMagicPos,
				Integer.MAX_VALUE);
		
		// Cache contents for later runs
		contents = buildContent.toArray(new String[buildContent.size()]);
		synchronized (this)
		{
			this._contents = contents;
		}
		
		// Load in contents
		for (String item : contents)
			__into.put(item, part.withPath(item));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/01/03
	 */
	@Override
	protected InputStream openInputStream()
		throws IOException, SecurityException
	{
		// Need to extract the positional components
		String path = this.part.getPath();
		int ls = path.lastIndexOf('/');
		if (ls < 0)
			throw new IOException("ADIR");
		
		// Get the base name, remove the extension
		path = path.substring(ls + 1);
		int ld = path.lastIndexOf('.');
		if (ld >= 0)
			path = path.substring(0, ld);
		
		// Base position and length
		int pos = 0;
		int len = Integer.MAX_VALUE;
		
		// The numbers could be invalid
		try
		{
			// Is there a plus, for the length?
			int pl = path.indexOf('+');
			if (pl >= 0)
			{
				pos = Math.abs(Integer.parseInt(
					path.substring(0, pl), 16));
				len = Math.abs(Integer.parseInt(
					path.substring(pl + 1), 10));
			}
			else
			{
				pos = Integer.parseInt(path, 16);
			}
		}
		catch (NumberFormatException __e)
		{
			throw new IOException(__e.getMessage(), __e);
		}
		
		// Open the input stream
		byte[] chunk;
		try (InputStream in = this.wrapped.openInputStream())
		{
			// Skip over all the bytes to get to the target location
			long total = 0;
			while (total < pos)
				total += in.skip(pos - total);
			
			// Read up until EOF?
			if (len == Integer.MAX_VALUE)
				chunk = StreamUtils.readAll(in);
			
			// Only read up to a specific number of bytes?
			else
			{
				chunk = new byte[len];
				StreamUtils.readMostly(in, chunk);
			}
		}
		
		// Wrap the data stream
		return new ByteArrayInputStream(chunk);
	}
	
	/**
	 * Adds the detected magic number.
	 *
	 * @param __build The target list of files.
	 * @param __magic The content type.
	 * @param __pos The position in the stream.
	 * @param __len The number of bytes in the chunk.
	 * @throws NullPointerException On null arguments.
	 * @since 2026/01/03
	 */
	private static void __add(List<String> __build,
		@Language("mime-type-reference") String __magic,
		int __pos, int __len)
		throws NullPointerException
	{
		if (__magic == null || __build == null)
			throw new NullPointerException("NARG");
		
		// Build the file name
		String fileName;
		if (__len > 0 && __len < Integer.MAX_VALUE)
			fileName = String.format("%08x+%d.%s",
				__pos, __len, ContentTypeUtil.toExtension(__magic));
		else
			fileName = String.format("%08x.%s",
				__pos, ContentTypeUtil.toExtension(__magic));
		
		// Debug
		Debugging.debugNote("Found %s!", fileName);
		
		// Add the file
		__build.add(fileName);
	}
}
