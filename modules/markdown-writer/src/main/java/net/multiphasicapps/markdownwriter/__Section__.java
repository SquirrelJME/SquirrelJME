// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.markdownwriter;

import java.io.IOException;
import java.util.Deque;

/**
 * This is the base class for all sections within the markdown writer, this is
 * used to modify how data is written to the output file when it is written.
 *
 * The constructor for a section will automatically set itself as the newest
 * section and initialize it from a previous one.
 *
 * @since 2016/10/02
 */
abstract class __Section__
{
	/** The target writer. */
	protected final MarkdownWriter writer;
	
	/** The section that was at the top. */
	final __Section__ _sectionbefore;
	
	/** The current text style to use. */
	volatile MarkdownTextStyle _style =
		MarkdownTextStyle.NORMAL;
	
	/** The section header level. */
	volatile int _level;
	
	/**
	 * Initializes a new section.
	 *
	 * @param __mdw The writer where characters are placed.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/02
	 */
	__Section__(MarkdownWriter __mdw)
		throws IOException, NullPointerException
	{
		// Check
		if (__mdw == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.writer = __mdw;
		
		// Is there anything above this?
		__Section__ _sectionbefore = __mdw._section;
		this._sectionbefore = _sectionbefore;
		
		// If there is a section before this then end this
		if (_sectionbefore != null)
			_sectionbefore.__endSection();
		
		// Set new section
		__mdw._section = this;
		
		// Set some basic details
		this._level = (_sectionbefore == null ? 1 : _sectionbefore._level);
	}
	
	/**
	 * Puts the specified character into the section as if it were written.
	 *
	 * @param __c The character to place.
	 * @throws IOException On write errors.
	 * @since 2016/10/02
	 */
	abstract void __process(char __c)
		throws IOException;
	
	/**
	 * This may be replaced by sub-classes to end the specified section.
	 *
	 * Classes should always call the super-class method first.
	 *
	 * @throws IOException On write errors.
	 * @since 2016/10/02
	 */
	void __endSection()
		throws IOException
	{
	}
}

