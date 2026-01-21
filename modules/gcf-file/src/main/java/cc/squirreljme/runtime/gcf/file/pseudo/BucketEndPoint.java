// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.gcf.file.pseudo;

import cc.squirreljme.jvm.mle.BucketShelf;
import cc.squirreljme.jvm.mle.brackets.BucketBracket;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.full.attrib.ExtraFileAttributes;
import cc.squirreljme.runtime.gcf.file.FileEndPoint;
import cc.squirreljme.runtime.gcf.uri.UriGenericPart;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * File end point which wraps {@link BucketShelf} and {@link BucketBracket}.
 *
 * @since 2026/01/16
 */
@SquirrelJMEVendorApi
public class BucketEndPoint
	extends FileEndPoint
{
	/** Decoded host. */
	@SquirrelJMEVendorApi
	public static final String DECODED_HOST =
		"!?x-squirreljme-bucket://?!";
	
	/** Host. */
	@SquirrelJMEVendorApi
	public static final String HOST =
		"!%3Fx-squirreljme-bucket%3A%2F%2F%3F!";
	
	/** The bracket to access. */
	private final BucketBracket _bucket;
	
	/**
	 * Initializes the endpoint.
	 *
	 * @param __part The URI part of the endpoint.
	 * @param __mode The mode the endpoint is opened in.
	 * @param __dotDot Optional {@code ..} replacement override.
	 * @param __bracket The bracket being accessed.
	 * @throws NullPointerException On null arguments.
	 * @since 2026/01/16
	 */
	protected BucketEndPoint(@NotNull UriGenericPart __part, int __mode,
		@Nullable UriGenericPart __dotDot, BucketBracket __bracket)
		throws NullPointerException
	{
		super(__part, __mode, __dotDot);
		
		if (__bracket == null)
			throw new NullPointerException("NARG");
		
		this._bucket = __bracket;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/01/16
	 */
	@Override
	protected ExtraFileAttributes attachedAttributes()
		throws SecurityException
	{
		// This could be a directory or a file
		if (this.part.getPath().endsWith("/"))
			return PseudoAttributes.DIRECTORY;
		return PseudoAttributes.FILE;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/01/16
	 */
	@Override
	protected FileStore attachedFileStore()
		throws SecurityException
	{
		// There is no filesystem
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/01/16
	 */
	@Override
	protected FileSystem attachedFileSystem()
		throws SecurityException
	{
		// There is no filesystem
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/01/16
	 */
	@Override
	public void close()
		throws IOException
	{
		// Nothing needs to be done here
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/01/16
	 */
	@Override
	protected void listDirectory(@NotNull Map<String, UriGenericPart> __into)
		throws IOException, NullPointerException, SecurityException
	{
		// Can only dot-dot if it is known
		UriGenericPart dotDot = this.dotDot;
		if (dotDot != null)
			__into.put("..", dotDot);
		
		// List specific files in the bucket
		UriGenericPart part = this.part;
		for (String file : BucketShelf.list(this._bucket))
			__into.put(file, part.withPath(
				"/" + UriGenericPart.encode(file)));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/01/16
	 */
	@Override
	protected InputStream openInputStream()
		throws IOException, SecurityException
	{
		// Cannot open directories
		UriGenericPart part = this.part;
		if (part.isDirectory())
			throw new IOException("ADIR");
		
		// Which bucket and file are we using? 
		BucketBracket bucket = this._bucket;
		String file = part.getPath().substring(1);
		
		// This could fail
		try
		{
			// Get the length of the file
			long longLen = BucketShelf.length(bucket, file);
			if (longLen < 0)
				throw new IOException("FNFE");
			
			// Limit to 4GiB
			int len = (int)Math.min(Integer.MAX_VALUE, longLen);
			
			// Read in the entire file chunk
			byte[] chunk = new byte[len];
			for (int at = 0; at < len;)
			{
				// Read in as much data as possible
				int rc = BucketShelf.read(bucket, file, at, chunk,
					at, len - at);
				
				// Failed?
				if (rc < 0)
					throw new IOException("IOIO");
				
				// Bump up
				at += rc;
			}
			
			// Wrap in a stream
			return new ByteArrayInputStream(chunk);
		}
		catch (MLECallError __e)
		{
			throw new IOException(__e.getMessage(), __e);
		}
	}
}
