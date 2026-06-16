// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.csv;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import net.multiphasicapps.collections.UnmodifiableMap;

/**
 * Reads CSV files and writes to an output.
 *
 * @param <T> The type of value to read.
 * @since 2023/09/12
 */
public final class CsvReader<T>
	implements Closeable
{
	/** The deserializer for incoming lines. */
	protected final CsvDeserializer<T> deserializer;
	
	/** The input stream for CSV lines. */
	protected final CsvInputStream input;
	
	/** Temporary reading buffer. */
	private final StringBuilder _buffer =
		new StringBuilder();
	
	/** Working buffer. */
	private final StringBuilder _work =
		new StringBuilder();
	
	/** Working entries. */
	private final List<String> _workColumns =
		new ArrayList<>();
	
	/** Working input map. */
	private final Map<String, String> _workMap =
		new LinkedHashMap<>();
	
	/** Read only working input map. */
	private final Map<String, String> _workRoMap =
		UnmodifiableMap.of(this._workMap);
	
	/** The mapping of CSV headers. */
	private volatile List<String> _headerMap;
	
	/**
	 * Initializes the reader.
	 *
	 * @param __deserializer The deserializer input.
	 * @param __input The input to read from.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/09/12
	 */
	public CsvReader(CsvDeserializer<T> __deserializer,
		CsvInputStream __input)
		throws NullPointerException
	{
		if (__deserializer == null || __input == null)
			throw new NullPointerException("NARG");
		
		this.deserializer = __deserializer;
		this.input = __input;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/09/14
	 */
	@Override
	public void close()
		throws IOException
	{
		this.input.close();
	}
	
	/**
	 * Reads a single value.
	 *
	 * @return The single value.
	 * @throws NoSuchElementException If there is nothing left.
	 * @throws IOException On read errors.
	 * @since 2023/09/12
	 */
	public T read()
		throws NoSuchElementException, IOException
	{
		StringBuilder buffer = this._buffer;
		CsvInputStream input = this.input;
		List<String> headerMap = this._headerMap;
		
		// Read in line, loop to handle empty lines, etc.
		for (;;)
		{
			// Initialize buffer and read in data
			buffer.setLength(0);
			if (!input.next(buffer))
				throw new NoSuchElementException("NSEE");
			
			// Skip empty lines
			int bufLen = buffer.length();
			if (buffer.length() == 0)
				continue;
			
			// Re-initialize for future working
			List<String> workColumns = this._workColumns;
			StringBuilder work = this._work;
			
			// Clear before processing
			workColumns.clear();
			work.setLength(0);
			
			// Parse line
			boolean inQuote = false;
			boolean lastQuote = false;
			for (int i = 0; i < bufLen; i++)
			{
				// Get character for processing
				char c = buffer.charAt(i);
				
				// Quotes are special
				if (c == '"')
				{
					// Previously read a quote?
					if (lastQuote)
					{
						// We wanted an actual quote here
						work.append('"');
						
						// Clear last quote state
						lastQuote = false;
					}
					
					// Reading fresh quote
					else
						lastQuote = true;
					
					// Flip being in quotes because:
					// "" would result in not being quoted
					// while """ would
					inQuote = !inQuote;
				}
				
				// End column
				else if (c == ',' && !inQuote)
				{
					// Add from column
					workColumns.add(work.toString());
					work.setLength(0);
					
					// Reset state
					lastQuote = false;
				}
				
				// Normal character
				else
				{
					work.append(c);
					
					// Was not a quote
					lastQuote = false;
				}
			}
			
			// If there is anything left over, treat as last column
			if (work.length() > 0)
			{
				workColumns.add(work.toString());
				work.setLength(0);
			}
			
			// For later cleanup...
			try
			{
				// If there are no headers read yet, use them
				if (headerMap == null)
				{
					headerMap = new ArrayList<>(workColumns);
					this._headerMap = headerMap;
					
					// Run loop again
					continue;
				}
				
				// Load into a map
				else
				{
					// Fill in columns for the entire map, default to empty
					// string if there is no value there
					int max = workColumns.size();
					Map<String, String> workMap = this._workMap;
					for (int i = 0, n = headerMap.size(); i < n; i++)
						workMap.put(headerMap.get(i),
							(i < max ? workColumns.get(i) : ""));
					
					// Process with deserializer
					return this.deserializer.deserialize(this._workRoMap);
				}
			}
			
			// Cleanup after a processing run
			finally
			{
				workColumns.clear();
			}
		}
	}
	
	/**
	 * Reads all entries into a list.
	 *
	 * @return The resultant list.
	 * @throws IOException On read errors.
	 * @since 2023/09/12
	 */
	public List<T> readAll()
		throws IOException
	{
		return this.readAll(new ArrayList<T>());
	}
	
	/**
	 * Reads all the values into the given collection.
	 *
	 * @param <C> The collection type.
	 * @param __into The collection to write into.
	 * @return {@code __into}.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/09/12
	 */
	public <C extends Collection<? super T>> C readAll(C __into)
		throws IOException, NullPointerException
	{
		for (;;)
			try
			{
				__into.add(this.read());
			}
			catch (NoSuchElementException __ignored)
			{
				break;
			}
		
		// Return the target
		return __into;
	}
}
