// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.base;

import java.io.IOException;
import java.io.Flushable;
import java.io.OutputStream;

/**
 * This is a compiler output which returns streams which ignore any data
 * that is written to them.
 *
 * @since 2016/12/26
 */
public class NullCompilerOutput
	implements CompilerOutput
{
	/**
	 * {@inheritDoc}
	 * @since 2016/12/26
	 */
	@Override
	public void flush()
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/12/26
	 */
	@Override
	public OutputStream output(String __n)
		throws IOException
	{
		return new __Null__();
	}
	
	/**
	 * Ignores any data written to it.
	 *
	 * @sine 2016/12/26
	 */
	private static class __Null__
		extends OutputStream
		implements Flushable
	{
		/**
		 * {@inheritDoc}
		 * @since 2016/12/26
		 */
		@Override
		public void close()
		{
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/12/26
		 */
		@Override
		public void flush()
		{
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/12/26
		 */
		@Override
		public void write(int __b)
		{
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/12/26
		 */
		@Override
		public void write(byte[] __b, int __o, int __l)
		{
		}
	}
}

