// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c.out;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;

/**
 * Wraps the output and makes it very pretty.
 *
 * @since 2023/06/19
 */
public class PrettyCTokenOutput
	extends __FormattedCTokenOutput__
{
	/** The tab size. */
	private static final int _TAB_SIZE =
		4;
	
	/** The column limit. */
	private static final int _GUTTER =
		69;
	
	/** Current indentation level. */
	private volatile int _indent;
	
	/** Did the indentation level change? */
	private volatile boolean _indentChanged;
	
	/** Is a post newline pending? */
	private volatile boolean _pendingPostNewline;
	
	/**
	 * Initializes the output wrapper.
	 * 
	 * @param __wrap The output to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/19
	 */
	public PrettyCTokenOutput(CTokenOutput __wrap)
		throws NullPointerException
	{
		super(__wrap);
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
		// Adjust indentation level
		int lastIndent = this._indent;
		this._indent = Math.max(0, this._indent + __adjust);
		
		// Did the indentation actually change?
		this._indentChanged = (lastIndent != this._indent);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/07/21
	 */
	@Override
	public void newLine(boolean __force)
		throws IOException
	{
		super.newLine(__force);
		
		// We newlined, so forget that indentation changed
		this._indentChanged = false;
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
		
		// Specifically handle single character tokens
		int len = __cq.length();
		char first = (len == 0 ? 0 : __cq.charAt(0));
		char second = (len <= 1 ? 0 : __cq.charAt(1));
		char last = (len == 0 ? 0 : __cq.charAt(len - 1));
		
		// Always indent around these characters
		boolean alwaysIndent = ((len == 1 && (first == '{' || first == '}')) ||
			(len == 2 && (first == '}' && second == ';')));
		
		// Do we hug the line start with this?
		boolean hugLineStart = (first == '#' ||
			(first == '/' && second == '/'));
		
		// Do not always indent if the last characters are these, for
		// making formatting better...
		boolean forgetIndent = (last == ',' || last == ';' || last == '.' ||
			last == ':' || last == ')');
		
		// Space before?
		boolean spaceBefore = (len == 1 && (first == '='));
		
		// Space after?
		boolean spaceAfter = (len == 1 && (first == '=' || first == ',' ||
			first == '*'));
		
		// If we were pending a post newline, emit it now accordingly
		if (this._pendingPostNewline)
		{
			// Emit newline, as long as we are not forgetting the indent
			if (!forgetIndent)
				this.newLine(true);
			
			// Clear
			this._pendingPostNewline = false;
		}
		
		// If column is past the gutter, start on a fresh line
		// Note that forgetting indent can override this!
		// But do emit a newline if our indentation actually changed
		if ((!forgetIndent && this.column >= PrettyCTokenOutput._GUTTER) ||
			(this._indentChanged))
			this.newLine(true);
		
		// Newline before indentation, as long as we are not on the first
		// column
		if (alwaysIndent && !forgetIndent && this.column > 0)
			this.newLine(true);
		
		// As long as we are not going to hug the line start
		if (this.column == 0 && !hugLineStart)
			for (int i = 0, n = this._indent; i < n; i++)
			{
				this.out.tab();
				this.column += PrettyCTokenOutput._TAB_SIZE;
			}
		
		// Pad space before?
		if (spaceBefore)
			super.token(" ", false);
		
		// Emit token
		super.token(__cq, __forceNewline);
		
		// Pad space after?
		if (spaceAfter)
			super.token(" ", false);
		
		// Always add indentation after this token, put post it instead of
		// actually adding it.
		if (alwaysIndent)
			this._pendingPostNewline = true;
	}
}
