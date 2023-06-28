// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c.out;

import java.io.IOException;

/**
 * Wraps the output and makes it very compact.
 *
 * @since 2023/06/19
 */
public class CompactCTokenOutput
	implements CTokenOutput
{
	/** The output to wrap. */
	protected final CTokenOutput out;
	
	/** The last character written. */
	private volatile char _lastChar;
	
	/** Push a space? */
	private volatile boolean _pushSpace;
	
	/**
	 * Initializes the output wrapper.
	 * 
	 * @param __wrap The output to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/19
	 */
	public CompactCTokenOutput(CTokenOutput __wrap)
		throws NullPointerException
	{
		if (__wrap == null)
			throw new NullPointerException("NARG");
		
		this.out = __wrap;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/22
	 */
	@Override
	public void close()
		throws IOException
	{
		this.out.close();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/22
	 */
	@Override
	public void indent(int __adjust)
	{
		// Ignore all indentation, we do not care for it here
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/22
	 */
	@Override
	public void newLine(boolean __force)
		throws IOException
	{
		// Only add newline if forced, and the last was not already a newline
		if (__force && this._lastChar != '\n')
		{
			this.out.newLine(true);
			
			// We did whitespace here
			this._lastChar = '\n';
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/22
	 */
	@Override
	public void space()
		throws IOException
	{
		// Only emit space when token requested
		this._pushSpace = true;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/22
	 */
	@Override
	public void tab()
		throws IOException
	{
		// Only emit space when token requested
		this._pushSpace = true;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/22
	 */
	@Override
	public void token(CharSequence __cq, boolean __forceNewline)
		throws IOException, NullPointerException
	{
		if (__cq == null)
			throw new NullPointerException("NARG");
		
		// Always forward these
		int len = __cq.length();
		if (len > 0)
		{
			// Do we need a space after the last token?
			if (this._pushSpace)
			{
				char firstChar = __cq.charAt(0);
				if (this.__needSpace(firstChar))
					this.__space();
				
				// Do not push anymore spaces
				this._pushSpace = false;
			}
			
			// Output token
			this.out.token(__cq, __forceNewline);
		}
		
		// If we forced a newline then we already have the whitespace there
		// so we do not need to emit it at the end
		if (__forceNewline)
		{
			this._lastChar = '\n';
			this._pushSpace = false;
		}
		else if (len > 0)
			this._lastChar = __cq.charAt(len - 1);
	}
	
	/**
	 * Returns if the last character was whitespace.
	 *
	 * @return If the last character was a whitespace.
	 * @since 2023/06/28
	 */
	boolean __lastWhitespace()
	{
		char lastChar = this._lastChar;
		return lastChar == '\r' || lastChar == '\n' ||
			lastChar == ' ' || lastChar == '\t';
	}
	
	/**
	 * Is a space needed after the last token?
	 * 
	 * @param __first The first character of the new token.
	 * @return If a space is needed after the last token.
	 * @since 2023/06/28
	 */
	boolean __needSpace(char __first)
	{
		char last = this._lastChar;
		
		// A space is never needed here
		if (__first == ' ' || __first == '\t' ||
			__first == '\r' || __first == '\n')
			return false;
		
		// Last was an identifier of sorts
		else if ((last >= 'a' && last <= 'z') ||
			(last >= 'A' && last <= 'Z') ||
			(last >= '0' && last <= '9') ||
			last == '_')
		{
			// We only need a space if we are doing another identifier
			return ((__first >= 'a' && __first <= 'z') ||
				(__first >= 'A' && __first <= 'Z') ||
				(__first >= '0' && __first <= '9') ||
				__first == '_');
		}
		
		// We do not want to accidentally make trigraphs
		else if (last == '?')
			return __first == '?';
		
		// Do not make = = or + = into == or +=
		else if (__first == '=')
			return !(last == '+' || last == '-' ||
				last == '*' || last == '/' ||
				last == '%' || last == '^' ||
				last == '&' || last == '|' ||
				last == '=' || last == '!' ||
				last == '<' || last == '>');
		
		// Do not turn < < or + + into << or ++
		return __first == last && (last == '=' ||
			last == '<' || last == '>' ||
			last == '|' || last == '&' ||
			last == '+' || last == '-');
	}
	
	/**
	 * Handles both spaces and tabs so that they are only emitted once.
	 *
	 * @throws IOException On write errors.
	 * @since 2023/06/22
	 */
	private void __space()
		throws IOException
	{
		// Do not emit multiple spaces
		if (!this.__lastWhitespace())
		{
			this.out.space();
			
			// Do not emit more whitespace
			this._lastChar = ' ';
		}
	}
}
