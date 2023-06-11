// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.io.file;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;

/**
 * This represents a safe variant of temporary file output.
 *
 * @since 2022/08/29
 */
public final class SafeTemporaryFileOutputStream
	extends OutputStream
{
	/** The target path for the final move. */
	protected final Path targetPath;
	
	/** The temporary output stream. */
	private volatile OutputStream _output;
	
	/** The current temporary file, for later atomic move. */
	private volatile Path _tempPath;
	
	/** Has this been closed? */
	private volatile boolean _closed;
	
	/** Failure exception. */
	private volatile Throwable _throwable;
	
	/**
	 * Initializes the safe output.
	 * 
	 * @param __target The target file to write to.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/08/29
	 */
	public SafeTemporaryFileOutputStream(Path __target)
		throws NullPointerException
	{
		if (__target == null)
			throw new NullPointerException("NARG");
		
		this.targetPath = __target;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/08/29
	 */
	@Override
	public void close()
		throws IOException
	{
		if (!this._closed)
		{
			// Perform init, if needed and flush temporary output
			// This also does the close on the output as well
			try (OutputStream out = this.__init())
			{
				if (out != null)
					out.flush();
			}
			
			// Mark as closed
			this._closed = true;
			
			// Do not copy over the original file if we ever failed
			if (this._throwable != null)
				return;
			
			// Atomically move the file over
			Path targetPath = this.targetPath;
			Path tempPath = this._tempPath;
			try
			{
				Files.createDirectories(targetPath.getParent());
				Files.move(tempPath, targetPath,
					StandardCopyOption.REPLACE_EXISTING);
			}
			
			// Delete the temporary file always
			finally
			{
				try
				{
					Files.delete(tempPath);
				}
				catch (IOException ignored)
				{
				}
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/08/29
	 */
	@Override
	public void flush()
		throws IOException
	{
		// Ignore if closed
		if (this._closed)
			return;
		
		// Forward
		try
		{
			this.__init().flush();
		}
		catch (Throwable t)
		{
			this.__throw(t);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/08/29
	 */
	@Override
	public void write(int __b)
		throws IOException
	{
		// Has this already been closed?
		if (this._closed)
			throw new IOException("CLSD");
		
		// Forward
		try
		{
			this.__init().write(__b);
		}
		catch (Throwable t)
		{
			this.__throw(t);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/08/29
	 */
	@Override
	public void write(byte[] __b)
		throws IOException, NullPointerException
	{
		// Has this already been closed?
		if (this._closed)
			throw new IOException("CLSD");
		
		// Forward
		try
		{
			this.__init().write(__b);
		}
		catch (Throwable t)
		{
			this.__throw(t);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/08/29
	 */
	@Override
	public void write(byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, IOException, NullPointerException
	{
		// Has this already been closed?
		if (this._closed)
			throw new IOException("CLSD");
		
		// Forward
		try
		{
			this.__init().write(__b, __o, __l);
		}
		catch (Throwable t)
		{
			this.__throw(t);
		}
	}
	
	/**
	 * Initializes the output, if not already setup.
	 * 
	 * @return The output stream to write to.
	 * @throws IOException If this could not be initialized.
	 * @since 2022/08/29
	 */
	private OutputStream __init()
		throws IOException
	{
		// Stream already opened?
		OutputStream output = this._output;
		if (output != null)
			return output;
		
		// Setup temporary file
		Path tempPath = Files.createTempFile("temp", ".file");
		this._tempPath = tempPath;
		
		// Setup output
		output = Files.newOutputStream(tempPath,
			StandardOpenOption.WRITE, StandardOpenOption.CREATE,
			StandardOpenOption.TRUNCATE_EXISTING);
		this._output = output;
		
		// Use this
		return output;
	}
	
	/**
	 * Tracks the exception and rethrows it.
	 * 
	 * @param __t The exception to throw.
	 * @throws IOException If an {@link IOException}.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/08/29
	 */
	private void __throw(Throwable __t)
		throws IOException, NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Store for later use in close
		this._throwable = __t;
		
		// Throw this exception
		if (__t instanceof IOException)
			throw (IOException)__t;
		else if (__t instanceof RuntimeException)
			throw (RuntimeException)__t;
		else if (__t instanceof Error)
			throw (Error)__t;
		else
			throw new IOException("WRAP", __t);
	}
}
