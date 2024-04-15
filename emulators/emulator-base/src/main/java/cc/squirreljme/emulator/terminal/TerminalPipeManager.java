// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.terminal;

import cc.squirreljme.emulator.MLECallWouldFail;
import cc.squirreljme.jvm.mle.TaskShelf;
import cc.squirreljme.jvm.mle.TerminalShelf;
import cc.squirreljme.jvm.mle.brackets.TaskBracket;
import cc.squirreljme.jvm.mle.constants.PipeErrorType;
import cc.squirreljme.jvm.mle.constants.StandardPipeType;
import cc.squirreljme.jvm.mle.constants.TaskPipeRedirectType;
import java.io.IOException;
import java.io.OutputStream;
import java.util.NoSuchElementException;

/**
 * This manages the terminal pipes that are available.
 * 
 * There are also helper methods for MLE calls that are mappable to
 * {@link TerminalShelf} and {@link TaskShelf}.
 *
 * @since 2020/07/06
 */
public final class TerminalPipeManager
{
	/** The pipes which are available. */
	private final TerminalPipe[] _pipes =
		new TerminalPipe[StandardPipeType.NUM_STANDARD_PIPES];
	
	/**
	 * Closes all of the pipes.
	 * 
	 * @throws IOException If there were pipe closures.
	 * @since 2020/07/09
	 */
	public void closeAll()
		throws IOException
	{
		// Exceptions might be deferred
		IOException defer = null;
		
		synchronized (this)
		{
			// Close every single pipe
			for (TerminalPipe pipe : this._pipes)
				try
				{
					if (pipe != null)
						pipe.close();
				}
				catch (IOException e)
				{
					if (defer == null)
						defer = new IOException("Pipe close exception.");
					
					defer.addSuppressed(e);
				}
		}
		
		// If we deferred close exceptions, then report it
		if (defer != null)
			throw defer;
	}
	
	/**
	 * Gets the pipe for the descriptor.
	 * 
	 * @param __fd The file descriptor to get.
	 * @return The pipe for the descriptor.
	 * @throws NoSuchElementException If no pipe exists.
	 * @since 2020/07/06
	 */
	public final TerminalPipe get(int __fd)
		throws NoSuchElementException
	{
		if (__fd < 0 || __fd >= StandardPipeType.NUM_STANDARD_PIPES)
			throw new NoSuchElementException("Out of range FD: " + __fd);
		
		synchronized (this)
		{
			// Check that the pipe exists
			TerminalPipe rv = this._pipes[__fd];
			if (rv == null)
				throw new NoSuchElementException("No such pipe at: " + __fd);
			
			return rv;
		}
	}
	
	/**
	 * Gets the given file descriptor.
	 * 
	 * @param __fd The file descriptor.
	 * @return The resultant pipe.
	 * @throws MLECallWouldFail If the file descriptor is not set/valid.
	 * @since 2020/07/06
	 */
	public final TerminalPipe mleGet(int __fd)
		throws MLECallWouldFail
	{
		try
		{
			return this.get(__fd);
		}
		catch (NoSuchElementException e)
		{
			throw new MLECallWouldFail("Not a valid FD: " + __fd, e);
		}
	}
	
	/**
	 * As {@link TaskShelf#read(TaskBracket, int, byte[], int, int)}. 
	 * 
	 * @param __fd As called.
	 * @param __b As called.
	 * @param __o As called.
	 * @param __l As called.
	 * @return As called.
	 * @throws MLECallWouldFail As called.
	 * @since 2020/07/06
	 */
	@SuppressWarnings("resource")
	public final int mleRead(int __fd, byte[] __b, int __o, int __l)
		throws MLECallWouldFail
	{
		try
		{
			int rv = this.mleGet(__fd).read(__b, __o, __l);
			
			if (rv < 0)
				return PipeErrorType.END_OF_FILE;
			return rv;
		}
		catch (IOException e)
		{
			return PipeErrorType.IO_EXCEPTION;
		} 
	}
	
	/**
	 * Registers the given pipe.
	 * 
	 * @param __fd The file descriptor to register.
	 * @param __pipe The pipe to use.
	 * @throws IllegalArgumentException If the file descriptor is not valid.
	 * @throws IllegalStateException If the descriptor has already been
	 * registered.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/07/06
	 */
	public final void register(int __fd, TerminalPipe __pipe)
		throws IllegalArgumentException, IllegalStateException,
			NullPointerException
	{
		if (__pipe == null)
			throw new NullPointerException("NARG");
		
		if (__fd != StandardPipeType.STDOUT &&
			__fd != StandardPipeType.STDERR)
			throw new IllegalArgumentException("Invalid FD: " + __fd);
		
		// Prevent issues with pipe access
		TerminalPipe[] pipes = this._pipes;
		synchronized (this)
		{
			// Pipes may only be claimed once
			if (pipes[__fd] != null)
				throw new IllegalStateException("Already claimed FD: " + __fd);
			
			// Claim the descriptor
			pipes[__fd] = __pipe;
		}
	}
	
	/**
	 * Registers a buffer pipe.
	 * 
	 * @param __fd The file descriptor.
	 * @throws IllegalArgumentException If the file descriptor is not valid.
	 * @throws IllegalStateException If the descriptor has already been
	 * registered.
	 * @since 2020/07/06
	 */
	public final void registerBuffer(int __fd)
		throws IllegalArgumentException, IllegalStateException
	{
		this.register(__fd, new BufferTerminalPipe());
	}
	
	/**
	 * Registers the pipe by a default type.
	 * 
	 * Note that using {@link TaskPipeRedirectType#TERMINAL} will use the
	 * standard input and output streams.
	 * 
	 * @param __fd The file descriptor, a {@link StandardPipeType}.
	 * @param __type The {@link TaskPipeRedirectType}.
	 * @throws IllegalArgumentException If {@code __type} is not valid or
	 * if it is {@link TaskPipeRedirectType#TERMINAL} and is not one of
	 * {@link StandardPipeType#STDOUT} or {@link StandardPipeType#STDERR}.
	 * @since 2020/07/09
	 */
	public final void registerByType(int __fd, int __type)
		throws IllegalArgumentException
	{
		switch (__type)
		{
			case TaskPipeRedirectType.DISCARD:
				this.registerDiscard(__fd);
				break;
				
			case TaskPipeRedirectType.BUFFER:
				this.registerBuffer(__fd);
				break;
				
			case TaskPipeRedirectType.TERMINAL:
				boolean isStdOut = __fd == (StandardPipeType.STDOUT);
				boolean isStdErr = __fd == (StandardPipeType.STDERR);
				
				if (!isStdOut && !isStdErr)
					throw new IllegalArgumentException(
						"Is neither stdOut or stdErr.");
				
				this.registerTerminal(__fd,
					(isStdOut ? System.out : System.err));
				break;
			
			default:
				throw new IllegalArgumentException("Invalid type: " + __type);
		}
	}
	
	/**
	 * Registers a discard pipe.
	 * 
	 * @param __fd The file descriptor.
	 * @throws IllegalArgumentException If the file descriptor is not valid.
	 * @throws IllegalStateException If the descriptor has already been
	 * registered.
	 * @since 2020/07/06
	 */
	public final void registerDiscard(int __fd)
		throws IllegalArgumentException, IllegalStateException
	{
		this.register(__fd, new DiscardTerminalPipe());
	}
	
	/**
	 * Registers a pipe to another output stream.
	 * 
	 * @param __fd The file descriptor.
	 * @throws IllegalArgumentException If the file descriptor is not valid.
	 * @throws IllegalStateException If the descriptor has already been
	 * registered.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/07/06
	 */
	public final void registerTerminal(int __fd, OutputStream __out)
		throws IllegalArgumentException, IllegalStateException,
			NullPointerException
	{
		this.register(__fd, new RealTerminalPipe(__out));
	}
}
