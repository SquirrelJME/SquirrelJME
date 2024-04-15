// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.markdownwriter;

import java.io.IOException;

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
		__Section__ sectionBefore = __mdw._section;
		this._sectionbefore = sectionBefore;
		
		// If there is a section before this then end this
		if (sectionBefore != null)
			sectionBefore.__endSection();
		
		// Set new section
		__mdw._section = this;
		
		// Set some basic details
		this._level = (sectionBefore == null ? 1 : sectionBefore._level);
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

