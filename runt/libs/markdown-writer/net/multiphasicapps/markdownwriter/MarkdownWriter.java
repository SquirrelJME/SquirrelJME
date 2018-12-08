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

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Writer;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Formatter;
import java.util.Objects;

/**
 * This is a class which writes markdown formatted text to the specified
 * {@link Appendable} which may be any implentation of one. This handle all
 * of the standard formatting details that markdown supports.
 *
 * This writer supports closing and flushing, however those operations will
 * only be performed on the wrapped {@link Appendable} if those also implement
 * such things.
 *
 * This class is not thread safe.
 *
 * @since 2016/09/13
 */
public class MarkdownWriter
	implements Appendable, Closeable
{
	/** Markdown right column limit. */
	public static final int RIGHT_COLUMN =
		76;
	
	/** Where text may be written to. */
	protected final Appendable append;
	
	/** Formatter to write output text. */
	protected final Formatter formatter;
	
	/** The current section being written. */
	volatile __Section__ _section;
	
	/** The current text column. */
	volatile int _column;
	
	/**
	 * Initializes the markdown writer.
	 *
	 * @param __a The appendable to send characters to.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/13
	 */
	public MarkdownWriter(Appendable __a)
		throws NullPointerException
	{
		// Check
		if (__a == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.append = __a;
		
		// Setup formatter
		this.formatter = new Formatter(this);
	}
	
	/**
	 * {@inheritDoc}
	 * @sicne 2016/09/13
	 */
	@Override
	public MarkdownWriter append(char __c)
		throws IOException
	{
		__put(__c, false);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @sicne 2016/09/13
	 */
	@Override
	public MarkdownWriter append(CharSequence __cs)
		throws IOException
	{
		return this.append(__cs, 0, __cs.length());
	}
	
	/**
	 * {@inheritDoc}
	 * @sicne 2016/09/13
	 */
	@Override
	public MarkdownWriter append(CharSequence __cs, int __s, int __e)
		throws IOException
	{
		for (int i = __s; i < __e; i++)
			__put(__cs.charAt(i), false);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @sicne 2016/09/13
	 */
	@Override
	public void close()
		throws IOException
	{
		// Flush before close
		try
		{
			flush();
		}
		
		// Ignore
		catch (IOException e)
		{
		}
		
		// Only close if it is closeable
		Appendable append = this.append;
		if (append instanceof Closeable)
			((Closeable)append).close();
	}
	
	/**
	 * Flushes this writer.
	 *
	 * @throws IOException If it could not be flushed.
	 * @since 2016/09/13
	 */
	public void flush()
		throws IOException
	{
		// Java ME has no Flushable so we only know of these two classes
		Appendable append = this.append;
		if (append instanceof OutputStream)
			((OutputStream)append).flush();
		else if (append instanceof Writer)
			((Writer)append).flush();
	}
	
	/**
	 * Prints the specified header into the output document.
	 *
	 * @param __abs If {@code true} then the header is at the specified level,
	 * otherwise if {@code false} it will be relative to the existing header
	 * level.
	 * @param __level If absolute then this level is set where the level is
	 * based on an index of one, otherwise this will be the relative header
	 * level adjustment from the current header level.
	 * @param __s The text to print.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/13
	 */
	public void header(boolean __abs, int __level, String __s)
		throws IOException, NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Setup section
		__SectionHeader__ header = new __SectionHeader__(this, __abs, __level);
		
		// Print header text
		append(__s);
		
		// Enter paragraph mode
		paragraph();
	}
	
	/**
	 * End a list.
	 *
	 * @throws IOException On write errors.
	 * @since 2016/10/01
	 */
	public void listEnd()
		throws IOException
	{
		// End the list by 
		__Section__ section = this._section;
		if (section instanceof __SectionList__)
		{
			// Manual end of section
			section.__endSection();
			
			// Go back to the section before this
			this._section = section._sectionbefore;
		}
	}
	
	/**
	 * Go the next item in the list.
	 *
	 * @throws IOException On write errros.
	 * @since 2016/10/01
	 */
	public void listNext()
		throws IOException
	{
		// Set new item on the list
		__Section__ section = this._section;
		if (section instanceof __SectionList__)
			((__SectionList__)section)._newitem = true;
	}
	
	/**
	 * Start a list.
	 *
	 * @throws IOException On write errors.
	 * @since 2016/10/01
	 */
	public void listStart()
		throws IOException
	{
		// Start section
		new __SectionUnorderedList__(this);
	}
	
	/**
	 * Enters paragraph mode which may be used .
	 *
	 * @throws IOException On write errors.
	 * @since 2016/10/02
	 */
	public void paragraph()
		throws IOException
	{
		new __SectionParagraph__(this);
	}
	
	/**
	 * Prints a single character.
	 *
	 * @param __c The character to print.
	 * @throws IOException On write errors.
	 * @since 2016/10/02
	 */
	public void print(char __c)
		throws IOException
	{
		append(__c);
	}
	
	/**
	 * Prints the specified object.
	 *
	 * @param __o The object to print.
	 * @throws IOException On write errors.
	 * @since 2016/10/01
	 */
	public void print(Object __o)
		throws IOException
	{
		append(Objects.toString(__o));
	}
	
	/**
	 * Prints formatted text to the output.
	 *
	 * @param __f The format specifier.
	 * @param __args The format arguments.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/13
	 */
	public void printf(String __f, Object... __args)
		throws IOException, NullPointerException
	{
		// Check
		if (__f == null)
			throw new NullPointerException("NARG");
		
		// Format
		this.formatter.format(__f, __args);
	}
	
	/**
	 * Prints the end of the line.
	 *
	 * @throws IOException On write errors.
	 * @since 2016/10/01
	 */
	public void println()
		throws IOException
	{
		append('\n');
	}
	
	/**
	 * Prints the specified object followed by a new line.
	 *
	 * @param __o The object to print.
	 * @throws IOException On write errors.
	 * @since 2016/10/01
	 */
	public void println(Object __o)
		throws IOException
	{
		print(__o);
		println();
	}
	
	/**
	 * Prints a URI to the output document.
	 *
	 * @param __uri The URI to print.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/01
	 */
	public void uri(String __uri)
		throws IOException, NullPointerException
	{
		// Check
		if (__uri == null)
			throw new NullPointerException("NARG");
		
		// Prime
		__put('\0', false);
		
		// Print it out
		__put('<', true);
		append(__uri);
		__put('>', true);
	}
	
	/**
	 * Prints a URI to the output document with the given display text.
	 *
	 * @param __uri The URI to point to.
	 * @param __text The display text for the URI.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/01
	 */
	public void uri(String __uri, String __text)
		throws IOException, NullPointerException
	{
		// Check
		if (__uri == null || __text == null)
			throw new NullPointerException("NARG");
		
		// Prime
		__put('\0', false);
		
		// Print it out
		__put('[', true);
		append(__text);
		__put(']', true);
		__put('(', true);
		__unescapedURI(__uri);
		__put(')', true);
	}
	
	/**
	 * Prints a URI to the output document with the given display text.
	 *
	 * @param __uri The URI to point to.
	 * @param __text The display text for the URI.
	 * @param __title The text text for the URI.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/01
	 */
	public void uri(String __uri, String __text, String __title)
		throws IOException, NullPointerException
	{
		// Check
		if (__uri == null || __text == null || __title == null)
			throw new NullPointerException("NARG");
		
		// Prime
		__put('\0', false);
		
		// Print it out
		__put('[', true);
		append(__text);
		__put(']', true);
		__put('(', true);
		__unescapedURI(__uri);
		__put(' ', true);
		__put('"', true);
		append(__title);
		__put('"', true);
		__put(')', true);
	}
	
	/**
	 * Returns {@code true} if the specified character is to be escaped.
	 *
	 * @return {@code true} if the character must be escaped.
	 * @since 2016/10/02
	 */
	boolean __escaped(char __c)
	{
		return (__c == '[' || __c == '(' || __c == '*' || __c == '_' ||
			__c == '\\' || __c == '<' || (__c == '#' && this._column == 0));
	}
	
	/**
	 * Places a single character into the output sending the character to
	 * be printed to the currently being written to section.
	 *
	 * @param __c The character to put.
	 * @param __nospec If {@code true} then the character is not given
	 * special handling.
	 * @throws IOException On write errors.
	 * @since 2016/09/13
	 */
	void __put(char __c, boolean __nospec)
		throws IOException
	{
		// Ignore CR
		if (__c == '\r')
			return;
		
		// Direct place if no special handling
		if (__nospec)
		{
			// Ignore null
			if (__c == 0)
				return;
			
			// Add it
			this.append.append(__c);
			
			// Newline resets column
			int column = this._column;
			if (__c == '\n')
				this._column = (column = 0);
			
			// Otherwise it goes up
			else
				this._column = (++column);
			
			// If at the end, go to the next line
			if (column >= RIGHT_COLUMN)
				__put('\n', true);
			
			// Done
			return;
		}
		
		// Need section to always exist
		__Section__ section = this._section;
		if (section == null)
			section = new __SectionParagraph__(this);
		
		// Character needs escaping?
		if (__escaped(__c))
			section.__process('\\');
		
		// Place character
		section.__process(__c);
	}
	
	/**
	 * Performs special handling for writing URI parts.
	 *
	 * @param __s The input URI.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/02
	 */
	void __unescapedURI(String __s)
		throws IOException, NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Go through all
		int n = __s.length();
		for (int i = 0; i < n; i++)
		{
			char c = __s.charAt(i);
			
			// Never escape underscore
			if (__escaped(c) || c == '"' || MarkdownWriter.__isWhitespace(c))
				if (c == '_')
					__put(c, true);
				
				// Percent encode anything else
				else
				{
					byte[] b = Character.toString(c).getBytes("utf-8");
					int q = b.length;
					__put('%', false);
					for (int l = 0; l < q; l++)
					{
						byte d = b[l];
						__put(Character.forDigit((d >>> 4) & 0xF, 16), false);
						__put(Character.forDigit(d & 0xF, 16), false);
					}
				}
			
			// Otherwise normal print
			else
				__put(c, false);
		}
	}
	
	/**
	 * Is this a whitespace character?
	 *
	 * @param __c The character to check.
	 * @return {@code true} if it is whitespace.
	 * @since 2016/10/02
	 */
	static boolean __isWhitespace(char __c)
	{
		return __c == ' ' || __c == '\t' || __c == '\n';
	}
}

