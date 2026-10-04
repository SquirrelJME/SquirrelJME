// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.markdownwriter;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
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
	
	/** Formatted text buffer. */
	protected final StringBuilder formatterBuffer =
		new StringBuilder();
	
	/** Formatter to write output text. */
	protected final Formatter formatter;
	
	/** The current section being written. */
	volatile __Section__ _section;
	
	/** The current text column. */
	volatile int _column;
	
	/** Do not create newlines. */
	private volatile boolean _noNewlines;
	
	/** The number of zero sized lines. */
	volatile int _zeroLines;
	
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
		this.formatter = new Formatter(this.formatterBuffer);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/13
	 */
	@Override
	public MarkdownWriter append(char __c)
		throws IOException
	{
		this.__put(__c, false);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/13
	 */
	@Override
	public MarkdownWriter append(CharSequence __cs)
		throws IOException
	{
		return this.append(false, __cs, 0, __cs.length());
	}
	
	/**
	 * Appends text to the buffer.
	 * 
	 * @param __wholeWords Print in whole words?
	 * @param __cs The sequence to print.
	 * @return {@code this}.
	 * @throws IOException On write errors.
	 * @since 2022/08/26
	 */
	public MarkdownWriter append(boolean __wholeWords, CharSequence __cs)
		throws IOException
	{
		return this.append(__wholeWords, __cs, 0, __cs.length());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/13
	 */
	@Override
	public MarkdownWriter append(CharSequence __cs, int __s, int __e)
		throws IndexOutOfBoundsException, IOException
	{
		return this.append(false, __cs, __s, __e);
	}
	
	/**
	 * Appends text to the output.
	 * 
	 * @param __wholeWords Print in whole words?
	 * @param __cs The sequence to append.
	 * @param __s The start index.
	 * @param __e The end index.
	 * @return {@code this}.
	 * @throws IndexOutOfBoundsException If the start or end is outside of
	 * the bounds of the sequence, or the start exceeds the end.
	 * @since 2022/08/26
	 */
	public MarkdownWriter append(boolean __wholeWords, CharSequence __cs,
		int __s, int __e)
		throws IndexOutOfBoundsException, IOException
	{
		// Use actual sequence?
		if (__cs == null)
			__cs = "null";
		
		// Check bounds
		int vn = __cs.length();
		if (__s < 0 || __e < 0 || __e > vn || __s > __e)
			throw new IndexOutOfBoundsException("IOOB");
		
		// If not outputting whole words, we need not do much
		if (!__wholeWords)
		{
			for (int i = __s; i < __e; i++)
				this.__put(__cs.charAt(i), false);
			
			return this;
		}
		
		// Keep track of the current state for later restoration
		boolean originalState = this._noNewlines;
		try
		{
			boolean currentState = originalState;
			for (int i = __s; i < __e; i++)
			{
				char c = __cs.charAt(i);
				
				// Allow whitespace to break lines
				if (Character.isWhitespace(c))
				{
					// If state changing, allow newlines and check for a new
					// one...
					if (currentState)
					{
						this._noNewlines = (currentState = false);
						this.__checkNewline(true);
					}
				}
				
				// Otherwise, do not allow
				else
				{
					// If state changing, allow newlines and check for a new
					// one...
					if (!currentState)
					{
						this._noNewlines = (currentState = true);
						this.__checkNewline(true);
						
						// Determine where the next whitespace is for lookahead
						int look = i;
						for (; look < __e; look++)
							if (Character.isWhitespace(__cs.charAt(look)))
								break;
						
						// Determine if we need to newline to fit this
						this.__checkNewlineLookAhead(look - i);
					}
				}
				
				// Put character
				this.__put(c, false);
			}
			
			return this;
		}
		
		// Restore to old state
		finally
		{
			this._noNewlines = originalState;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/13
	 */
	@Override
	public void close()
		throws IOException
	{
		// Flush before close
		try
		{
			this.flush();
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
		__SectionHeader__ header = new __SectionHeader__(
			this, __abs, __level);
		
		// Print header text, do not allow newlines for headers to happen
		try
		{
			this._noNewlines = true;
			
			this.append(__s);
		}
		finally
		{
			this._noNewlines = false;
		}
		
		// Enter paragraph mode
		this.paragraph();
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
	 * @throws IOException On write errors.
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
	 * Enters paragraph mode which may be used to start a new block of text.
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
		this.append(__c);
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
		this.append(false, Objects.toString(__o));
	}
	
	/**
	 * Prints the specified object.
	 *
	 * @param __wholeWords Print in whole words?
	 * @param __o The object to print.
	 * @throws IOException On write errors.
	 * @since 2022/08/26
	 */
	public void print(boolean __wholeWords, Object __o)
		throws IOException
	{
		this.append(__wholeWords, Objects.toString(__o));
	}
	
	/**
	 * Prints formatted text to the output.
	 *
	 * @param __f The format specifier.
	 * @param __args The format arguments.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/08/26
	 */
	public void printf(String __f, Object... __args)
		throws IOException, NullPointerException
	{
		this.printf(false, __f, __args);
	}
	
	/**
	 * Prints formatted text to the output.
	 *
	 * @param __wholeWords Print in whole words?
	 * @param __f The format specifier.
	 * @param __args The format arguments.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/13
	 */
	public void printf(boolean __wholeWords, String __f, Object... __args)
		throws IOException, NullPointerException
	{
		// Check
		if (__f == null)
			throw new NullPointerException("NARG");
		
		// Setup buffer
		StringBuilder buffer = this.formatterBuffer;
		buffer.setLength(0);
		
		// Format into the buffer
		this.formatter.format(__f, __args);
		
		// Append whatever was in the buffer
		this.append(__wholeWords, buffer);
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
		this.append('\n');
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
		this.print(__o);
		this.println();
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
		this.__put('\0', false);
		
		try
		{
			this._noNewlines = true;
			
			// Print it out
			this.__put('<', true);
			this.append(__uri);
			this.__put('>', true);
		}
		finally
		{
			this._noNewlines = false;
		}
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
		this.uri(__uri, __text, null);
	}
	
	/**
	 * Prints a URI to the output document with the given display text.
	 *
	 * @param __uri The URI to point to.
	 * @param __text The display text for the URI.
	 * @param __title The text text for the URI.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments, except for
	 * {@code __title}.
	 * @since 2016/10/01
	 */
	public void uri(String __uri, String __text, String __title)
		throws IOException, NullPointerException
	{
		// Check
		if (__uri == null || __text == null)
			throw new NullPointerException("NARG");
		
		// Prime
		this.__put('\0', false);
		
		// Print it out
		try
		{
			this._noNewlines = true;
			
			this.__put('[', true);
			this.append(__text);
			this.__put(']', true);
			
			this.__put('(', true);
			this.__unescapedURI(__uri);
			
			if (__title != null)
			{
				this.__put(' ', true);
				this.__put('"', true);
				this.append(__title);
				this.__put('"', true);
			}
			
			this.__put(')', true);
		}
		finally
		{
			this._noNewlines = false;
		}
	}
	
	/**
	 * Checks if a newline can be put down.
	 * 
	 * @param __allowAnyway Should a newline check be done anyway regardless
	 * of {@link #_noNewlines}?
	 * @throws IOException On write errors.
	 * @since 2022/08/26
	 */
	private void __checkNewline(boolean __allowAnyway)
		throws IOException
	{
		// If at the end, go to the next line, but if that was turned off
		// then do not do that
		if ((__allowAnyway || !this._noNewlines) &&
			this._column >= MarkdownWriter.RIGHT_COLUMN)
			this.__put('\n', true);
	}
	
	/**
	 * Checks the count against the current column and determines if a newline
	 * should be placed.
	 * 
	 * @param __count The number of characters to check.
	 * @throws IllegalArgumentException If count is negative.
	 * @throws IOException On write errors.
	 * @since 2022/08/26
	 */
	private void __checkNewlineLookAhead(int __count)
		throws IllegalArgumentException, IOException
	{
		if (__count < 0)
			throw new IllegalArgumentException("IAEE");
		
		// Put a newline if we would exceed the column
		if (this._column + __count >= MarkdownWriter.RIGHT_COLUMN)
			 this.__put('\n', true);
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
			{
				if (column == 0)
					this._zeroLines++;
				else
					this._zeroLines = 0;
				
				this._column = (column = 0);
			}
			
			// Otherwise, it goes up
			else
				this._column = (++column);
			
			// Do we need to put a newline?
			this.__checkNewline(false);
			
			// Done
			return;
		}
		
		// Need section to always exist
		__Section__ section = this._section;
		if (section == null)
			section = new __SectionParagraph__(this);
		
		// Character needs escaping?
		if (this.__escaped(__c))
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
			if (this.__escaped(c) || c == '"' ||
				MarkdownWriter.__isWhitespace(c))
				if (c == '_')
					this.__put(c, true);
				
				// Percent encode anything else
				else
				{
					byte[] b = Character.toString(c)
						.getBytes("utf-8");
					int q = b.length;
					this.__put('%', false);
					for (int l = 0; l < q; l++)
					{
						byte d = b[l];
						this.__put(Character.forDigit(
							(d >>> 4) & 0xF, 16), false);
						this.__put(Character.forDigit(
							d & 0xF, 16), false);
					}
				}
			
			// Otherwise normal print
			else
				this.__put(c, false);
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

