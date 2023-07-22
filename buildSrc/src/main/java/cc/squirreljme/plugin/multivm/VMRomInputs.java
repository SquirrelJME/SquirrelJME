// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import cc.squirreljme.plugin.multivm.ident.SourceTargetClassifier;
import java.io.File;
import java.nio.file.Path;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.concurrent.Callable;

/**
 * Returns the inputs for the ROM.
 *
 * @since 2020/08/23
 */
public class VMRomInputs
	implements Callable<Iterable<Path>>
{
	/** The task to generate for. */
	protected final VMRomTask task;
	
	/** The classifier used. */
	protected final SourceTargetClassifier classifier;
	
	/**
	 * Initializes the handler.
	 * 
	 * @param __task The task to create for.
	 * @param __classifier The classifier used.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/23
	 */
	public VMRomInputs(VMRomTask __task,
		SourceTargetClassifier __classifier)
		throws NullPointerException
	{
		if (__task == null || __classifier == null)
			throw new NullPointerException("NARG");
		
		this.task = __task;
		this.classifier = __classifier;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/08/23
	 */
	@Override
	public Iterable<Path> call()
	{
		// All the inputs for the ROM are the outputs of all the library tasks
		Collection<Path> rv = new LinkedHashSet<>();
		for (VMLibraryTask task : VMRomDependencies.libraries(this.task,
			this.classifier))
			for (File f : task.getOutputs().getFiles().getFiles())
				rv.add(f.toPath());
		
		return rv;
	}
}
