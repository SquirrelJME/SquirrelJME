// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.multiphasicapps.collections.EmptyIterator;
import net.multiphasicapps.collections.UnmodifiableIterator;

/**
 * This is a path set which uses the class resource area as input.
 *
 * @since 2019/06/30
 */
public class ResourcePathSet
	implements CompilerPathSet
{
	/** The pivot class to lookup resources from. */
	protected final Class<?> pivot;
	
	/** The prefix to use for lookup. */
	protected final String prefix;
	
	/**
	 * Initializes the resource path set.
	 *
	 * @param __cl The class to load resources from.
	 * @param __prefix The resource prefix.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/06/30
	 */
	public ResourcePathSet(Class<?> __cl, String __prefix)
		throws NullPointerException
	{
		if (__cl == null || __prefix == null)
			throw new NullPointerException("NARG");
		
		this.pivot = __cl;
		this.prefix = __prefix;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/06/30
	 */
	@Override
	public final void close()
		throws CompilerException
	{
		// Does nothing
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/06/30
	 */
	@Override
	public final Iterator<CompilerInput> iterator()
	{
		// Try to read the list file
		try (InputStream in = pivot.getResourceAsStream(this.prefix + "list"))
		{
			// If there is none, then just return no files
			if (in == null)
				return EmptyIterator.<CompilerInput>empty();
			
			// Read in lines
			List<CompilerInput> inputs = new ArrayList<>();
			try (BufferedReader br = new BufferedReader(
				new InputStreamReader(in)))
			{
				// Read in
				for (;;)
				{
					String ln = br.readLine();
					
					if (ln == null)
						break;
					
					// Try to see if this even exists
					try
					{
						inputs.add(this.input(ln));
					}
					
					// It does not, so ignore
					catch (NoSuchInputException e)
					{
					}
				}
			}
			
			// Return input list
			return UnmodifiableIterator.<CompilerInput>of(
				inputs.<CompilerInput>toArray(
					new CompilerInput[inputs.size()]));
		}
		
		// {@squirreljme.error AQ3b Could not read list file.}
		catch (IOException e)
		{
			throw new CompilerException("AQ3b", e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/06/30
	 */
	@Override
	public final CompilerInput input(String __n)
		throws CompilerException, NoSuchInputException, NullPointerException
	{
		// Build full resource name
		Class<?> pivot = this.pivot;
		String rcname = this.prefix + __n;
		
		// Only return if it exists
		try (InputStream in = pivot.getResourceAsStream(rcname))
		{
			// {@squirreljme.error AQ38 No input resource exists. (The name)}
			if (in == null)
				throw new NoSuchInputException("AQ38 " + __n);
			
			// Build it
			return new ResourceCompilerInput(pivot, rcname, __n);
		}
		
		// {@squirreljme.error AQ3a Exception closing class resource.}
		catch (IOException e)
		{
			throw new CompilerException("AQ3a", e);
		}
	}
}

