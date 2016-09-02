// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.builder;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import java.util.Random;
import java.util.ServiceLoader;
import net.multiphasicapps.io.hexdumpstream.HexDumpOutputStream;
import net.multiphasicapps.squirreljme.basicassets.BasicAsset;
import net.multiphasicapps.squirreljme.exe.ExecutableOutput;
import net.multiphasicapps.squirreljme.jit.base.JITException;
import net.multiphasicapps.squirreljme.jit.base.JITTriplet;
import net.multiphasicapps.squirreljme.jit.JITOutputConfig;
import net.multiphasicapps.zip.streamwriter.ZipStreamWriter;

/**
 * This is the base class for builders which can generate binaries for a given
 * target.
 *
 * @since 2016/07/22
 */
public abstract class TargetBuilder
{
	/**
	 * {@squirreljme.property
	 * net.multiphasicapps.squirreljme.builder.dumptarget=(true/false)
	 * If this is set to {@code true} then the output binary that would be
	 * run on the target system is dumped for debugging purposes.}
	 */
	private static final boolean _HEX_DUMP_OUTPUT =
		Boolean.getBoolean(
			"net.multiphasicapps.squirreljme.builder.dumptarget");
	
	/** Builder services. */
	private static final ServiceLoader<TargetBuilder> _SERVICES =
		ServiceLoader.<TargetBuilder>load(TargetBuilder.class);
	
	/** Suggestions. */
	private final TargetSuggestion[] _suggestions;
	
	/**
	 * Initializes the target builder.
	 *
	 * @param __sugs The suggested build targets, a triplet followed by a
	 * short description.
	 * @since 2016/07/22
	 */
	public TargetBuilder(String... __sugs)
	{
		// Setup suggestions
		if (__sugs != null)
		{
			// Setup target
			int n = __sugs.length;
			TargetSuggestion[] tss = new TargetSuggestion[n >>> 1];
			this._suggestions = tss;
			
			// Fill suggestions
			for (int i = 0, j = 0; (i + 1) < n; i += 2)
				tss[j++] = new TargetSuggestion(new JITTriplet(__sugs[i]),
					__sugs[i + 1]);
		}
		
		// No suggestions
		else
			this._suggestions = new TargetSuggestion[0];
	}
	
	/**
	 * Creates an instance of the builder with the specified configuration.
	 *
	 * @param __conf The build configuration to use.
	 * @return The build instance which targets the given system.
	 * @throws TargetNotSupportedException If the target is not supported.
	 * @since 2016/09/02
	 */
	public abstract BuildInstance createBuildInstance(BuildConfig __conf) 
		throws TargetNotSupportedException;
	
	/**
	 * Returns the suggested targets.
	 *
	 * @return An array of suggested targets.
	 * @since 2016/07/22
	 */
	public final TargetSuggestion[] suggestedTargets()
	{
		return this._suggestions.clone();
	}
	
	/**
	 * Goes through all builders to find one that supports the given
	 * configuration.
	 *
	 * @param __conf The configuration to target.
	 * @return The found builder or {@code null} if none was found.
	 * @throws NullPointerException On null arguments.
	 * @throws TargetNotSupportedException If no target was ever found.
	 * @since 2016/07/22
	 */
	public static final BuildInstance findAndCreateBuildInstance(
		BuildConfig __conf)
		throws NullPointerException, TargetNotSupportedException
	{
		// Check
		if (__conf == null)
			throw new NullPointerException("NARG");
		
		// Lock
		ServiceLoader<TargetBuilder> services = _SERVICES;
		synchronized (services)
		{
			// Go through all of them and try to create build instances
			for (TargetBuilder tb : services)
				try
				{
					return tb.createBuildInstance(__conf);
				}
				
				// Ignore
				catch (TargetNotSupportedException e)
				{
				}
		}
		
		// {@squirreljme.error DW07 Could not locate and create a build
		// instance for the given configuration. (The configuration)}
		throw new TargetNotSupportedException(
			String.format("DW07 %s", __conf));
	}
	
	/**
	 * Potentially hex dumps the output binary that is natively being written
	 * for progressive debugging.
	 *
	 * @param __os The stream to potentially wrap.
	 * @return The hexdump wrapping of the given stream or {@code __os} if it
	 * is not to be wrapped.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/16
	 */
	public static OutputStream hexDump(OutputStream __os)
		throws NullPointerException
	{
		// Check
		if (__os == null)
			throw new NullPointerException("NARG");
		
		// Dump the output binary?
		if (_HEX_DUMP_OUTPUT)
			return new HexDumpOutputStream(__os, System.err);
		return __os;
	}
	
	/**
	 * Reads the first line from a basic asset file.
	 *
	 * @param __asset The asset to read.
	 * @return The first line of the asset.
	 * @throws JITException If the string could not be read.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/24
	 */
	private static String __readFirstLine(String __asset)
		throws JITException, NullPointerException
	{
		// Check
		if (__asset == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error DW0o Could not locate the specified asset.
		// (The asset name)}
		BasicAsset ba = BasicAsset.getAsset(__asset);
		if (ba == null)
			throw new JITException(String.format("DW0o %s", __asset));
		
		// Opens the input asset
		try (InputStream is = ba.open();
			InputStreamReader isr = new InputStreamReader(is, "utf-8");
			BufferedReader br = new BufferedReader(isr))
		{
			// Read a line
			String rv = br.readLine();
			
			// {@squirreljme.error DW0q No line was read from the asset.
			// (The asset name)}
			if (rv == null)
				throw new JITException(String.format("DW0q %s", __asset));
			
			// {@squirreljme.error DW0r The read line from the asset was
			// blank. (The asset name)}
			rv = rv.trim();
			if (rv.length() <= 0)
				throw new JITException(String.format("DW0r %s", __asset));
			
			// Use it
			return rv;
		}
		
		// {@squirreljme.error DW0p Could not read the first line of the
		// given asset. (The asset name)}	
		catch (IOException e)
		{
			throw new JITException(String.format("DW0p %s", __asset), e);
		}
	}
}

