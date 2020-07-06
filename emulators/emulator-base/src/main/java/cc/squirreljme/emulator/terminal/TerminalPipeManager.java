// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.terminal;

import cc.squirreljme.jvm.mle.constants.StandardPipeType;
import java.io.OutputStream;
import java.util.NoSuchElementException;

/**
 * This manages the terminal pipe.
 *
 * @since 2020/07/06
 */
public final class TerminalPipeManager
{
	/** The pipes which are available. */
	private final TerminalPipe[] _pipes =
		new TerminalPipe[StandardPipeType.NUM_STANDARD_PIPES];
	
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
