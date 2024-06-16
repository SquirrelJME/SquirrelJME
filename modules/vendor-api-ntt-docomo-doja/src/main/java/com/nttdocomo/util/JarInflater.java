// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.util;

import cc.squirreljme.jvm.mle.NativeArchiveShelf;
import cc.squirreljme.jvm.mle.brackets.NativeArchiveBracket;
import cc.squirreljme.jvm.mle.brackets.NativeArchiveEntryBracket;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.DubiousImplementationError;
import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.ErrorCode;
import cc.squirreljme.runtime.cldc.util.StreamUtils;
import java.io.IOException;
import java.io.InputStream;

/**
 * This utility is used to decompress Jar files, or Zip files, from a byte
 * array or stream.  Note that this keeps the contents of the Jar in memory
 * for the lifetime of the instance and as such this should only be used on
 * very small Jars and not be retained via any kind of reference.
 * 
 * There is no signature verification in this inflater, so as such any Jar
 * could be modified accordingly.
 * 
 * Note that in SquirrelJME this uses the block Zip support which means that
 * this will not support linear reading of Zip files.
 *
 * @since 2024/01/13
 */
@Api
public class JarInflater
{
	/** The wrapped Zip blocks. */
	private final NativeArchiveBracket _zip;
	
	/** Is this closed? */
	private volatile boolean _isClosed;
	
	/**
	 * Initializes a Jar/Zip accessor around the given byte array.
	 *
	 * @param __buffer The buffer to wrap around.
	 * @throws JarFormatException If the Jar/Zip is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/13
	 */
	@Api
	public JarInflater(byte[] __buffer)
		throws JarFormatException, NullPointerException
	{
		if (__buffer == null)
			throw new NullPointerException("NARG");
		
		// Initialize the block reader
		try
		{
			this._zip = NativeArchiveShelf.archiveOpenZip(
				__buffer, 0, __buffer.length);
		}
		catch (MLECallError __e)
		{
			/* {@squirreljme.error AH17 Malformed Zip file.} */
			JarFormatException toss = new JarFormatException(
				ErrorCode.__error__("AH17"));
			
			toss.initCause(__e);
			
			throw toss;
		}
	}
	
	/**
	 * Reads all bytes from the input and then loads the Zip from the data,
	 * the stream is not closed at all.
	 * 
	 * The stream's position after reading is unspecified.
	 * 
	 * The stream is not retained by this class at all, as such it is
	 * imperative for users of this class to close the passed stream
	 * accordingly.
	 *
	 * @param __in The stream to load the Jar/Zip from, it is unspecified
	 * whether the next read position is at the end of the Zip's central
	 * directory or at the end of the input stream.
	 * @throws JarFormatException If the Jar/Zip is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/13
	 */
	@Api
	public JarInflater(InputStream __in)
		throws IOException, JarFormatException, NullPointerException
	{
		this(StreamUtils.readAll(__in));
	}
	
	/**
	 * This closes the input stream, however the documentation does not
	 * actually state what this does nor does it mark any exceptions as
	 * being closed. In SquirrelJME this is assumed to close the input stream
	 * that was passed in the event it was constructed as one in the event
	 * one obtains a resource but never closes it.
	 *
	 * @since 2024/01/13
	 */
	@Api
	public void close()
	{
		// Mark closed, do not run multiple times
		if (!this._isClosed)
			this._isClosed = true;
		
		// Close the Zip
		try
		{
			NativeArchiveShelf.archiveClose(this._zip);
		}
		catch (MLECallError __e)
		{
			/* {@squirreljme.error AH12 Closing array based Zip should not
			have failed, however there is no definitive source on what
			happens if closing does fail in the DoJa documentation.} */
			throw new DubiousImplementationError(
				ErrorCode.__error__("AH12"), __e);
		}
	}
	
	/**
	 * Returns an input stream to access the given entry.
	 *
	 * @param __name The name of the entry to open.
	 * @return The resultant stream if the entry exists; {@code null} if no
	 * such entry exists, or it is a directory.
	 * @throws IllegalArgumentException If the name is not valid, for example
	 * it contains relative path elements such
	 * as {@code /./} or {@code '/../'}.
	 * @throws IllegalStateException When {@link #close()} has been called.
	 * @throws JarFormatException If the Jar/Zip is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/13
	 */
	@Api
	public InputStream getInputStream(String __name)
		throws IllegalArgumentException, IllegalStateException,
			JarFormatException, NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		if (this._isClosed)
			throw new IllegalStateException("CLOS");
		
		try
		{
			NativeArchiveEntryBracket entry = this.__entry(__name,
				true);
			
			// Not found, or is a directory? Then just fail
			if (entry == null || NativeArchiveShelf.entryIsDirectory(entry))
				return null;
			
			// Return stream to access the file
			return NativeArchiveShelf.entryOpen(entry);
		}
		
		/* {@squirreljme.error AH15 Could not open the given entry as a
		stream. (The entry name)} */
		catch (MLECallError __e)
		{
			JarFormatException toss = new JarFormatException(
				ErrorCode.__error__("AH15", __name));
			toss.initCause(__e);
			throw toss;
		}
	}
	
	/**
	 * Returns the size of a given entry.
	 * 
	 * It is unspecified whether this checks for relative path entries such
	 * as {@code /./} or {@code '/../'} or not.
	 * 
	 * @param __name The name of the entry to get the size of
	 * @return The size of the given entry, if it is a directory then the
	 * returned size will be zero, otherwise it will be {@code -1} if not
	 * found.
	 * @throws IllegalStateException When {@link #close()} has been called.
	 * @throws JarFormatException If the Jar/Zip is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/13
	 */
	@Api
	public long getSize(String __name)
		throws IllegalStateException, NullPointerException, JarFormatException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		if (this._isClosed)
			throw new IllegalStateException("CLOS");
		
		try
		{
			NativeArchiveEntryBracket entry = this.__entry(__name,
				true);
			
			// Not found
			if (entry == null)
				return -1;
			
			// Directories are considered as empty files in terms of size
			else if (NativeArchiveShelf.entryIsDirectory(entry))
				return 0;
			
			// Otherwise request the uncompressed size
			return NativeArchiveShelf.entryUncompressedSize(entry);
		}
		
		/* {@squirreljme.error AH16 Could not get the size of the
		given entry. (The entry name)} */
		catch (MLECallError __e)
		{
			JarFormatException toss = new JarFormatException(
				ErrorCode.__error__("AH16", __name));
			toss.initCause(__e);
			throw toss;
		}
	}
	
	/**
	 * Finds the entry with the given name, if an entry is not found it will
	 * attempt to find a directory if one was not specified.
	 *
	 * @param __name The name to this.
	 * @param __relCheck Check for relative name?
	 * @return The given entry.
	 * @throws IllegalArgumentException If the name is not valid, for example
	 * it contains relative path elements such
	 * as {@code /./} or {@code '/../'}.
	 * @throws JarFormatException If the Jar is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/13
	 */
	private final NativeArchiveEntryBracket __entry(String __name,
		boolean __relCheck)
		throws IllegalArgumentException, JarFormatException,
			NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		// Requesting a directory?
		boolean wantDir = __name.endsWith("/");
		
		/* {@squirreljme.error AH13 JarInflater path cannot be used to obtain
		relative directory entries. (The entry name)} */
		if (wantDir && __relCheck)
			if (__name.contains("/./") || __name.contains("/../"))
				throw new IllegalArgumentException(
					ErrorCode.__error__("AH13", __name));
		
		// Obtain the given entry
		try
		{
			NativeArchiveEntryBracket entry =
				NativeArchiveShelf.archiveEntry(this._zip, __name);
			
			// Could we potentially be requesting a directory?
			if (entry == null && !wantDir)
				return this.__entry(__name, __relCheck);
			
			return entry;
		}
		catch (MLECallError __e)
		{
			/* {@squirreljme.error AH14 Could not obtain entry from the Zip,
			it may be corrupted or invalid. (The entry name)} */
			JarFormatException toss = new JarFormatException(
				ErrorCode.__error__("AH14", __name));
			toss.initCause(__e);
			throw toss;
		}
	}
}
