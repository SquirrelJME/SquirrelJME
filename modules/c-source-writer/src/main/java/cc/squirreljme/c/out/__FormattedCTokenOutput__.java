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
 * Base class for specially formatted token outputs.
 *
 * @since 2023/07/20
 */
abstract class __FormattedCTokenOutput__
	implements CTokenOutput
{
	/** The output to wrap. */
	protected final CTokenOutput out;
	
	/** The last character written. */
	protected volatile char _lastChar;
	
	/** Push a space? */
	protected volatile boolean _pushSpace;
	
	/**
	 * Initializes the output wrapper.
	 * 
	 * @param __wrap The output to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/19
	 */
	public __FormattedCTokenOutput__(CTokenOutput __wrap)
		throws NullPointerException
	{
		if (__wrap == null)
			throw new NullPointerException("NARG");
		
		this.out = __wrap;
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
	void __space()
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
