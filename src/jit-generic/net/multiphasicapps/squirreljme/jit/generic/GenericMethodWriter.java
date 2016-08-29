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
import java.util.Arrays;
import net.multiphasicapps.io.data.DataEndianess;
import net.multiphasicapps.io.data.ExtendedDataOutputStream;
import net.multiphasicapps.squirreljme.jit.base.JITCPUEndian;
import net.multiphasicapps.squirreljme.jit.base.JITException;
import net.multiphasicapps.squirreljme.jit.base.JITTriplet;
import net.multiphasicapps.squirreljme.jit.JITMethodWriter;
import net.multiphasicapps.squirreljme.jit.JITOutputConfig;
import net.multiphasicapps.squirreljme.jit.JITVariableType;

/**
 * This is the base class for the native machine code generator which is common
 * to many architectures. Support for architectures extends this class which
 * is used to create the actual machine code.
 *
 * All generic machines are assumed to be modern, where there is a limited set
 * of registers for data storage along with extra memory areas where the stack
 * is located. Values which cannot be stored in registers at any given time
 * will be placed on the stack.
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
	
	/** The output endianess. */
	protected final JITCPUEndian endianess;
	
	/** The bit level of the CPU. */
	protected final int wordsize;
	
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
		JITCPUEndian endianess;
		JITTriplet triplet = __conf.triplet();
		switch ((endianess = triplet.endianess()))
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
		this.endianess = endianess;
		
		// CPU bits
		this.wordsize = triplet.bits();
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
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/29
	 */
	@Override
	public void primeArguments(JITVariableType[] __t)
		throws JITException, NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Debug
		System.err.printf("DEBUG -- Primed args: %s%n", Arrays.asList(__t));
		
		throw new Error("TODO");
	}
}

