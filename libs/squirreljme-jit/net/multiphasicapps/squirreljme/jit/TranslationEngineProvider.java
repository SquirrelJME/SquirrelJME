// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

/**
 * This provides a single instance creation of a translation engine which is
 * used by the JIT to translate byte code into an {@link ExecutableClass}.
 *
 * @since 2017/01/30
 */
public interface TranslationEngineProvider
{
	/**
	 * Creates a new instance of the translation engine for use during JIT
	 * compilation.
	 *
	 * @param __jsa The accessor which is used to access the JIT state.
	 * @return The translation engine.
	 * @since 2017/01/30
	 */
	public abstract TranslationEngine createEngine(JITStateAccessor __jsa);
}

