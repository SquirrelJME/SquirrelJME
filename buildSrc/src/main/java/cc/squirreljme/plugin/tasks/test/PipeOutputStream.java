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
import org.gradle.api.internal.tasks.testing.TestResultProcessor;
import org.gradle.api.tasks.testing.TestOutputEvent;

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
	
	/** The test ID. */
	protected final Object testId;
	
	/** The destination. */
	protected final TestOutputEvent.Destination destination;
	
	/** Results for execution. */
	private final TestResultProcessor results;
	
	/** Do we have a carriage return? */
	private boolean haveCR;
	
	/**
	 * Initializes the pipe output.
	 *
	 * @param __id The test ID.
	 * @param __r The result processor.
	 * @param __d The destination.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/03/07
	 */
	public PipeOutputStream(Object __id, TestResultProcessor __r,
		TestOutputEvent.Destination __d)
		throws NullPointerException
	{
		if (__id == null || __r == null || __d == null)
			throw new NullPointerException();
		
		this.testId = __id;
		this.results = __r;
		this.destination = __d;
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
				this.haveCR = true;
			else
			{
				this.__flush();
				this.haveCR = false;
			}
		}
		else
		{
			// Flush if we just gave a CR
			if (this.haveCR)
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
					this.haveCR = true;
				else
				{
					this.__flush();
					this.haveCR = false;
				}
			}
			else
			{
				// Flush if we just gave a CR
				if (this.haveCR)
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
		StringBuilder buffer = this.buffer;
		
		// Send to output
		this.results.output(this.testId, EmulatedTestUtilities.output(
			this.destination, buffer.toString()));
		
		// Clear the buffer
		buffer.setLength(0);
		
		// Always clear CR state
		this.haveCR = false;
	}
}
