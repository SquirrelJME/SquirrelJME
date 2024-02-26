// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

/**
 * Only in a specific class pattern modifier, anything that ends in a
 * wildcard. The exact name may be specified which will emit for a given
 * specific class name.
 * 
 * Patterns are one of:
 *  - {@code Class}.
 *  - {@code *}.
 *  - {@code Class*}.
 *  - {@code *Class}.
 *
 * @since 2021/03/13
 */
public final class JDWPClassPatternMatcher
{
	/** Is this a wildcard? */
	protected final boolean isWildCard;
	
	/** Is this prefix? */
	protected final boolean isPrefix;
	
	/** Matching sequence. */
	protected final String sequence;
	
	/**
	 * Initializes the only in class modifier.
	 * 
	 * @param __pattern The pattern used.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/03/13
	 */
	public JDWPClassPatternMatcher(String __pattern)
		throws NullPointerException
	{
		if (__pattern == null)
			throw new NullPointerException("NARG");
		
		// Prefix wildcard
		if (__pattern.startsWith("*"))
		{
			this.isWildCard = true;
			this.isPrefix = true;
			this.sequence = __pattern.substring(1);
		}
		
		// Postfix wildcard
		else if (__pattern.endsWith("*"))
		{
			this.isWildCard = true;
			this.isPrefix = false;
			this.sequence = __pattern.substring(0, __pattern.length() - 1);
		}
		
		// Exact match
		else
		{
			this.isWildCard = false;
			this.isPrefix = false;
			this.sequence = __pattern;
		}
	}
	
	/**
	 * Checks if the class meets the given string.
	 * 
	 * @param __s The string to check.
	 * @return If this meets the string. 
	 * @throws NullPointerException On null arguments.
	 * @since 2021/04/18
	 */
	public final boolean meets(String __s)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Is this a straight string comparison?
		String sequence = this.sequence;
		if (!this.isWildCard)
			return sequence.equals(__s);
		
		// Check the end or the start of the string for a match
		if (this.isPrefix)
			return sequence.endsWith(__s);
		return sequence.startsWith(__s);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/03/16
	 */
	@Override
	public final String toString()
	{
		return "ClassPattern(" + this.sequence + ")";
	}
}
