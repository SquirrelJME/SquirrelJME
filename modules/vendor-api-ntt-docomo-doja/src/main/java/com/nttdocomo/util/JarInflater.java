// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.util;

import cc.squirreljme.runtime.cldc.DubiousImplementationError;
import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;
import java.io.InputStream;
import net.multiphasicapps.zip.blockreader.ZipBlockEntry;
import net.multiphasicapps.zip.blockreader.ZipBlockReader;

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
	private final ZipBlockReader _zip;
	
	/** The stream that was wrapped, only used for {@link #close()}. */
	private final InputStream _stream;
	
	/** Is this closed? */
	private volatile boolean _isClosed;
	
	/**
	 * Initializes a Jar/Zip inflater around the given byte array.
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
			this._zip = new ZipBlockReader(__buffer);
		}
		catch (IOException __e)
		{
			JarFormatException toss = new JarFormatException();
			
			toss.initCause(__e);
			
			throw toss;
		}
		
		// Not used in this version
		this._stream = null;
	}
	
	/**
	 * Reads all bytes from the input and then loads the Zip from the data,
	 * the stream is not closed at all.
	 *
	 * @param __in The stream to load the Jar/Zip from
	 * @throws JarFormatException If the Jar/Zip is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/13
	 */
	@Api
	public JarInflater(InputStream __in)
		throws IllegalStateException, JarFormatException, NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
	
		throw Debugging.todo();
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
		// Mark closed
		if (!this._isClosed)
			this._isClosed = true;
		
		// Close the stream?? This is dubious and the documentation helpfully
		// says: "Reading from the input stream already obtained from this
		// JarInflater is It is not guaranteed." or "Reading from an input
		// stream already retrieved from this JarInflater is not guaranteed.".
		InputStream stream = this._stream;
		if (stream != null)
			try
			{
				Debugging.todoNote(
					"Close assumption made in JarInflater.close()");
				
				stream.close();
			}
			catch (IOException __e)
			{
				/* {@squirreljme.error AH12 Closing assumed target stream in
				   JarInflater, which threw an exception resulted in a dubious
				   operation.} */
				throw new DubiousImplementationError("AH12", __e);
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
		
		throw Debugging.todo();
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
	
		throw Debugging.todo();
	}
	
	/**
	 * Finds the entry with the given name, if an entry is not found it will
	 * attempt to find a directory if one was not specified.
	 *
	 * @param __name The name to this.
	 * @return The given entry.
	 * @throws JarFormatException If the Jar is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/13
	 */
	private final ZipBlockEntry __entry(String __name)
		throws JarFormatException, NullPointerException
	{
		if (__name == null)
			throws new NullPointerException("NARG");
		
		throw Debugging.todo();
	}
}
