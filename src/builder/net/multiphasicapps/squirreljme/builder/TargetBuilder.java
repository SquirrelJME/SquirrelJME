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

import java.util.ServiceLoader;
import net.multiphasicapps.squirreljme.jit.base.JITTriplet;
import net.multiphasicapps.squirreljme.jit.JITOutputConfig;

/**
 * This is the base class for builders which can generate binaries for a given
 * target.
 *
 * @since 2016/07/22
 */
public abstract class TargetBuilder
{
	/** Builder services. */
	private static final ServiceLoader<TargetBuilder> _SERVICES =
		ServiceLoader.<TargetBuilder>load(TargetBuilder.class);
	
	/** Supports the JIT? */
	protected final boolean canjit;
	
	/** Suggestions. */
	private final TargetSuggestion[] _suggestions;
	
	/**
	 * Initializes the target builder.
	 *
	 * @param __jit Is the JIT supported?
	 * @param __sugs The suggested build targets, a triplet followed by a
	 * short description.
	 * @since 2016/07/22
	 */
	public TargetBuilder(boolean __jit, String... __sugs)
	{
		// Set
		this.canjit = __jit;
		
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
	 * This potentially modifies and sets the initial configuration state which
	 * is used for the JIT.
	 *
	 * After this is executed, the builder will associate a cache creator and
	 * a triplet.
	 *
	 * @param __conf The target configuration to use.
	 * @param __bc The build configuration.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/22
	 */
	public abstract void outputConfig(JITOutputConfig __conf, BuildConfig __bc)
		throws NullPointerException;
	
	/**
	 * Is the given configuration supported?
	 *
	 * @param __conf The configuration to target.
	 * @return {@code true} if it is supported.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/22
	 */
	public abstract boolean supportsConfig(BuildConfig __conf)
		throws NullPointerException;
	
	/**
	 * Is the JIT supported for this target?
	 *
	 * @return {@code true} if the JIT is supported.
	 * @since 2016/07/22
	 */
	public final boolean canJIT()
	{
		return this.canjit;
	}
	
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
	 * @since 2016/07/22
	 */
	public static final TargetBuilder findBuilder(BuildConfig __conf)
		throws NullPointerException
	{
		// Check
		if (__conf == null)
			throw new NullPointerException("NARG");
		
		// Lock
		ServiceLoader<TargetBuilder> services = _SERVICES;
		synchronized (services)
		{
			// Go through all of them
			for (TargetBuilder tb : services)
				if (tb.supportsConfig(__conf))
					return tb;
		}
		
		// Not found
		return null;
	}
}

