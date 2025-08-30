// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import cc.squirreljme.plugin.multivm.ident.SourceTargetClassifier;
import java.nio.file.Path;
import java.util.concurrent.Callable;

/**
 * Determines the name of the built-in.
 *
 * @since 2024/10/12
 */
public class NanoCoatBuiltInTaskInput
	implements Callable<Path>
{
	/** The ROM task used. */
	protected final VMRomTask romTask;
	
	/**
	 * Initializes the provider.
	 *
	 * @param __classifier The classifier used.
	 * @param __romTask The ROM task to source from.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/10/12
	 */
	public NanoCoatBuiltInTaskInput(SourceTargetClassifier __classifier,
		VMRomTask __romTask)
		throws NullPointerException
	{
		if (__classifier == null || __romTask == null)
			throw new NullPointerException("NARG");
		
		this.romTask = __romTask;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/10/12
	 */
	@Override
	public Path call()
	{
		return this.romTask.getOutputs().getFiles().getSingleFile().toPath();
	}
}
