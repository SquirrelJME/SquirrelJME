// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.jvm.mle.TerminalShelf;
import cc.squirreljme.jvm.mle.constants.StandardPipeType;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Functions for {@link MLETerminal}.
 *
 * @since 2020/06/18
 */
public enum MLETerminal
	implements MLEFunction
{
	/** {@link TerminalShelf#flush(int)}. */
	FLUSH("flush:(I)I")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/18
		 */
		@SuppressWarnings("resource")
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			try
			{
				MLETerminal.__fdOutput((int)__args[0]).flush();
				return 1;
			}
			catch (IllegalArgumentException| IOException e)
			{
				return -1;
			}
		}
	},
	
	/** {@link TerminalShelf#write(int, int)}. */
	WRITE_BYTE("write:(II)I")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/18
		 */
		@SuppressWarnings("resource")
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			try
			{
				MLETerminal.__fdOutput((int)__args[0]).write((int)__args[1]);
				return 1;
			}
			catch (IllegalArgumentException|IOException e)
			{
				return -1;
			}
		}
	},
	
	/** {@link TerminalShelf#write(int, byte[], int, int)}. */
	WRITE_BYTES("write:(I[BII)I")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/18
		 */
		@SuppressWarnings("resource")
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			int len = (int)__args[3];
			
			try
			{
				MLETerminal.__fdOutput((int)__args[0])
					.write(((SpringArrayObjectByte)__args[1]).array(),
					(int)__args[2],
					len);
				return len;
			}
			catch (IllegalArgumentException|IOException e)
			{
				return -1;
			}
		}
	},
	
	/* End. */
	;
	
	/** The dispatch key. */
	protected final String key;
	
	/**
	 * Initializes the dispatcher info.
	 *
	 * @param __key The key.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/18
	 */
	MLETerminal(String __key)
		throws NullPointerException
	{
		if (__key == null)
			throw new NullPointerException("NARG");
		
		this.key = __key;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/06/18
	 */
	@Override
	public String key()
	{
		return this.key;
	}
	
	/**
	 * Returns the output stream for the given descriptor.
	 *
	 * @param __fd The file descriptor.
	 * @return The output stream for it.
	 * @throws IllegalArgumentException If the descriptor is not valid.
	 * @since 2020/06/13
	 */
	static OutputStream __fdOutput(int __fd)
		throws IllegalArgumentException
	{
		switch (__fd)
		{
			case StandardPipeType.STDOUT:	return System.out;
			case StandardPipeType.STDERR:	return System.err;
			
			default:
				throw new IllegalArgumentException("Unknown FD: " + __fd);
		}
	}
}
