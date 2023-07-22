// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import cc.squirreljme.plugin.multivm.ident.TargetClassifier;
import org.gradle.api.Task;
import org.gradle.api.specs.Spec;

/**
 * Check to see if the ROM is valid.
 *
 * @since 2020/08/23
 */
public class CheckRomShouldBuild
	implements Spec<Task>
{
	/** The classifier used. */
	protected final TargetClassifier classifier;
	
	/**
	 * Checks if a ROM should be built.
	 * 
	 * @param __classifier The classifier used.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/23
	 */
	public CheckRomShouldBuild(TargetClassifier __classifier)
		throws NullPointerException
	{
		if (__classifier == null)
			throw new NullPointerException("NARG");
		
		this.classifier = __classifier;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/08/23
	 */
	@Override
	public boolean isSatisfiedBy(Task __task)
		throws NullPointerException
	{
		if (__task == null)
			throw new NullPointerException("NARG");
		
		return this.classifier.getVmType().hasRom(
			this.classifier.getBangletVariant());
	}
}
