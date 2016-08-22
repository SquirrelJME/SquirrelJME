// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.generic;

import java.io.IOException;
import java.io.OutputStream;
import net.multiphasicapps.io.data.DataEndianess;
import net.multiphasicapps.io.data.ExtendedDataOutputStream;
import net.multiphasicapps.squirreljme.jit.base.JITException;
import net.multiphasicapps.squirreljme.jit.JITMethodWriter;
import net.multiphasicapps.squirreljme.jit.JITOutputConfig;

/**
 * This is the base class for the native machine code generator which is common
 * to many architectures. Support for architectures extends this class which
 * is used to create the actual machine code.
 *
 * @since 2016/08/19
 */
public abstract class GenericMethodWriter
	implements JITMethodWriter
{
	/** The used configuration. */
	protected final JITOutputConfig.Immutable config;	
	
	/** The stream to write to. */
	protected final ExtendedDataOutputStream output;
	
	/**
	 * Initializes the generic method writer.
	 *
	 * @param __conf The used configuration.
	 * @param __os The stream to write to.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/21
	 */
	public GenericMethodWriter(JITOutputConfig.Immutable __conf,
		OutputStream __os)
		throws NullPointerException
	{
		// Check
		if (__os == null)
			throw new NullPointerException("NARG");
		
		// Wrap
		this.config = __conf;
		ExtendedDataOutputStream dos = new ExtendedDataOutputStream(__os);
		this.output = dos;
		
		// Set endianess
		switch (__conf.triplet().endianess())
		{
				// Big endian
			case BIG:
				dos.setEndianess(DataEndianess.BIG);
				break;
				
				// Little endian
			case LITTLE:
				dos.setEndianess(DataEndianess.LITTLE);
				break;
			
				// Unknown
			default:
				throw new RuntimeException("OOPS");
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/19
	 */
	@Override
	public void close()
		throws JITException
	{
		// Close the output
		try
		{
			this.output.close();
		}
		
		// {@squirreljme.error BA06 Could not close the method.}
		catch (IOException e)
		{
			throw new JITException("BA06", e);
		}
	}
}

