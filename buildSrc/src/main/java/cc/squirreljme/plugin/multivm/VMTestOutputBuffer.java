// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;

/**
 * Used to read the buffer while also printing lines as the output streams
 * are written to.
 *
 * @since 2020/09/07
 */
public class VMTestOutputBuffer
	implements Runnable
{
	/** The stream to read from. */
	protected final InputStream in;
	
	/** The complete output buffer with all byte data. */
	@SuppressWarnings("resource")
	protected final ByteArrayOutputStream complete =
		new ByteArrayOutputStream();
	
	/** The current line of output. */
	@SuppressWarnings("resource")
	protected final ByteArrayOutputStream current =
		new ByteArrayOutputStream();
	
	/** The output stream to write to. */
	protected final OutputStream out;
	
	/** Should the output not be buffered? */
	protected final boolean unbuffered;
	
	/**
	 * Initializes the output buffer.
	 * 
	 * @param __in The input stream to read from.
	 * @param __out The stream to write partial data to.
	 * @param __unbuffered Should the output not be buffered?
	 * @throws NullPointerException On null arguments.
	 * @since 2020/09/07
	 */
	public VMTestOutputBuffer(InputStream __in, OutputStream __out,
		boolean __unbuffered)
		throws NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		this.in = __in;
		this.out = __out;
		this.unbuffered = __unbuffered;
	}
	
	/**
	 * Returns bytes that were read from the queue.
	 * 
	 * @param __thread The associated thread.
	 * @return All the read bytes.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/09/07
	 */
	@SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
	public byte[] getBytes(Thread __thread)
		throws NullPointerException
	{
		if (__thread == null)
			throw new NullPointerException("NARG");
		
		// Wait for the thread to be terminated
		while (__thread.isAlive())
			try
			{
				__thread.join();
			}
			catch (InterruptedException ignored)
			{
			}
		
		// Return the complete buffer output
		ByteArrayOutputStream complete = this.complete;
		synchronized (complete)
		{
			return complete.toByteArray();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/07
	 */
	@Override
	public void run()
	{
		// Constant reading of buffers
		InputStream in = this.in;
		for (byte[] buf = new byte[32768];;)
			try
			{
				// Check if we got interrupted before we read from the buffer
				if (Thread.currentThread().isInterrupted())
					throw new InterruptedException();
				
				// Read in
				int rc = in.read(buf);
				
				// EOF?
				if (rc < 0)
					break;
				
				// Push the buffer bytes forward
				this.__push(buf, 0, rc);
			}
			
			// Thread was interrupted, so probably want to stop doing this
			catch (InterruptedIOException|InterruptedException ignored)
			{
				break;
			}
			
			// Is a failed read
			catch (IOException e)
			{
				throw new RuntimeException("Stream read error.", e);
			}
	}
	
	/**
	 * Pushes bytes to the output.
	 * 
	 * @param __b The buffer.
	 * @param __o The offset.
	 * @param __l The length.
	 * @throws IndexOutOfBoundsException If the offset and/or length exceed
	 * the array bounds or are negative.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/09/07
	 */
	@SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
	private void __push(byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, IOException, NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Nothing being written?
		if (__l == 0)
			return;
		
		// This override is used for standard error so that any output here
		// is done directly without considering the current output at all 
		boolean unbuffered = this.unbuffered;
		
		// Split the buffer at newlines, but if we are doing unbuffered data
		// we just push it out as fast as we possibly can
		boolean endingNewline = false;
		if (!unbuffered)
			for (int at = __o, end = __o + __l; at < end; at++)
				if (__b[at] == '\n')
				{
					// If this ends in a newline, then we will be outputting
					// the newline and flushing whatever we put in
					endingNewline = (at == (end - 1));
					if (endingNewline)
						break;
				
					// Push the first chunk of bytes
					this.__push(__b, __o, (at + 1) - __o);
					
					// Then the second chunk of bytes at the trailing end, this
					// may result in more splits
					this.__push(__b, at + 1, end - (at + 1));
					
					// Do not process anything
					return;
				}
		
		// Current and complete output buffers
		ByteArrayOutputStream complete = this.complete;
		ByteArrayOutputStream current = this.current;
		
		// Add to the current buffer, ignored when buffered
		if (!unbuffered)
			current.write(__b, __o, __l);
		
		// Then to the complete buffer for later
		synchronized (complete)
		{
			complete.write(__b, __o, __l);
		}
		
		// If not buffered write out output as fast as we possibly can do so
		// Do a flush so our output is always sent through
		if (unbuffered)
		{
			this.out.write(__b, __o, __l);
			this.out.flush();
		}
		
		// If we ended in a newline, flush out the write buffer
		else if (endingNewline)
		{
			// Send all the bytes out
			this.out.write(current.toByteArray());
			
			// Discard everything in the buffer
			current.reset();
		}
	}
}
