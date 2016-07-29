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
import java.util.Objects;
import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;
import net.multiphasicapps.squirreljme.jit.base.JITClassFlags;
import net.multiphasicapps.squirreljme.jit.base.JITException;
import net.multiphasicapps.squirreljme.jit.JITClassWriter;
import net.multiphasicapps.squirreljme.jit.JITCompilerOrder;
import net.multiphasicapps.squirreljme.os.generic.BlobContentType;
import net.multiphasicapps.squirreljme.os.generic.GenericBlobConstants;
import net.multiphasicapps.io.data.ExtendedDataOutputStream;

/**
 * This writes classes to the output namespace.
 *
 * @since 2016/07/27
 */
public final class GenericClassWriter
	extends __BaseWriter__
	implements JITClassWriter
{
	/** The name of the class being written. */
	protected final ClassNameSymbol classname;
	
	/** Where class data is written to. */
	protected final ExtendedDataOutputStream output;
	
	/** The current order. */
	private volatile JITCompilerOrder _order =
		JITCompilerOrder.FIRST;
	
	/** Has this been closed? */
	private volatile boolean _closed;
	
	/**
	 * Initializes the generic class writer.
	 *
	 * @param __nsw The owning namespace writer.
	 * @param __cn The class name.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/27
	 */
	GenericClassWriter(GenericNamespaceWriter __nsw, ClassNameSymbol __cn)
		throws NullPointerException
	{
		super(__nsw, Objects.toString(__cn), BlobContentType.CLASS);
		
		// Check
		if (__cn == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.classname = __cn;
		
		// Setup output
		ExtendedDataOutputStream output = new ExtendedDataOutputStream(
			this.rawoutput);
		output.setEndianess(__nsw.__endianess());
		this.output = output;
		
		// Write the class header
		try
		{
			output.writeInt(GenericBlobConstants.CLASS_MAGIC);
		}
		
		// {@squirreljme.error BA0a Failed to write the initial header.}
		catch (IOException e)
		{
			throw new JITException("BA0a");
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/27
	 */
	@Override
	public void classFlags(JITClassFlags __cf)
		throws JITException, NullPointerException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/27
	 */
	@Override
	public void close()
		throws JITException
	{
		// Lock
		synchronized (this.lock)
		{
			// Close if not closed
			if (!this._closed)
			{
				// Mark closed
				this._closed = true;
			}
			
			// Super close
			super.close();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/27
	 */
	@Override
	public void interfaceClasses(ClassNameSymbol... __ins)
		throws JITException, NullPointerException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/27
	 */
	@Override
	public void superClass(ClassNameSymbol __cn)
		throws JITException
	{
		throw new Error("TODO");
	}
	
	/**
	 * Checks that the current order is the given expected order and proceeds
	 * to the next order.
	 *
	 * @param __exp The current order that is expected.
	 * @throws JITException If the order is not correct.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/22
	 */
	private void __order(JITCompilerOrder __exp)
		throws JITException, NullPointerException
	{
		// Check
		if (__exp == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			// {@squirreljme.error BA0j JIT invocation is not in order.
			// (The order that was attempted to be used; The expected order)}
			JITCompilerOrder order = this._order;
			if (order != __exp)
				throw new JITException(String.format("BA0j %s %s", __exp,
					order));
			
			// Set next
			this._order = order.next();
		}
	}
}

