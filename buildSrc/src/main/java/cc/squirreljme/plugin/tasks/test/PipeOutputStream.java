// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.tasks.test;

import java.io.OutputStream;

/**
 * Pipes output stream to the results output accordingly.
 *
 * @since 2020/03/07
 */
public class PipeOutputStream
	extends OutputStream
{
	/** The output buffer. */
	protected final StringBuilder buffer =
		new StringBuilder();
	
	/** The test result. */
	protected final EmulatedTestResult result;
	
	/** Is this standard error. */
	protected final boolean stdErr;
	
	/** Do we have a carriage return? */
	private boolean _haveCr;
	
	/**
	 * Initializes the pipe output.
	 * 
	 * @param __result The result to send to.
	 * @param __stdErr Is this standard error?
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/22
	 */
	public PipeOutputStream(EmulatedTestResult __result, boolean __stdErr)
		throws NullPointerException
	{
		if (__result == null)
			throw new NullPointerException("NARG");
		
		this.result = __result;
		this.stdErr = __stdErr;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/03/07
	 */
	@SuppressWarnings("DuplicatedCode")
	@Override
	public void write(int __b)
	{
		StringBuilder buffer = this.buffer;
		
		// Flush the buffer on newlines
		if (__b == '\r' || __b == '\n')
		{
			if (__b == '\r')
				this._haveCr = true;
			else
			{
				this.__flush();
				this._haveCr = false;
			}
		}
		else
		{
			// Flush if we just gave a CR
			if (this._haveCr)
				this.__flush();
			
			if (__b < 0)
				buffer.append('?');
			else
				buffer.append((char)__b);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/03/07
	 */
	@Override
	public void write(byte[] __b)
	{
		this.write(__b, 0, __b.length);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/03/07
	 */
	@SuppressWarnings("DuplicatedCode")
	@Override
	public void write(byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		if (__b == null)
			throw new NullPointerException();
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException();
		
		// Copy bytes accordingly
		StringBuilder buffer = this.buffer;
		for (int i = 0; i < __l; i++)
		{
			byte b = __b[__o++];
			
			// Flush the buffer on newlines
			if (b == '\r' || b == '\n')
			{
				if (b == '\r')
					this._haveCr = true;
				else
				{
					this.__flush();
					this._haveCr = false;
				}
			}
			else
			{
				// Flush if we just gave a CR
				if (this._haveCr)
					this.__flush();
				
				if (b < 0)
					buffer.append('?');
				else
					buffer.append((char)b);
			}
		}
	}
	
	/**
	 * Flushes the buffer.
	 *
	 * @since 2020/03/07
	 */
	private void __flush()
	{
		// Send buffer to the output
		StringBuilder buffer = this.buffer;
		this.result.addLine(new ConsoleLine(this.stdErr,
			System.currentTimeMillis(), buffer.toString()));
		
		// Clear the buffer
		buffer.setLength(0);
		
		// Always clear CR state
		this._haveCr = false;
	}
}
