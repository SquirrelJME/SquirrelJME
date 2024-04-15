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
import org.gradle.api.Task;
import org.gradle.api.tasks.Internal;

/**
 * The base for any VM related task.
 *
 * @since 2022/09/30
 */
public interface VMBaseTask
	extends Task
{
	/**
	 * Returns the used source target classifier.
	 * 
	 * @return The source target classifier to use.
	 * @since 2022/10/01
	 */
	@Internal
	SourceTargetClassifier getClassifier();
	
	/**
	 * Returns the banglet variant used.
	 * 
	 * @return The banglet variant used.
	 * @since 2022/10/01
	 */
	@Internal
	default BangletVariant getBangletVariant()
	{
		return this.getClassifier().getTargetClassifier()
			.getBangletVariant();
	}
	
	/**
	 * Returns the source set that is used.
	 * 
	 * @return The source set for the task.
	 * @since 2020/08/21
	 */
	@Internal
	default String getSourceSet()
	{
		return this.getClassifier().getSourceSet();
	}
	
	/**
	 * Returns the virtual machine type used.
	 * 
	 * @return The virtual machine type used.
	 * @since 2022/09/30
	 */
	@Internal
	default VMSpecifier getVmType()
	{
		return this.getClassifier().getTargetClassifier()
			.getVmType();
	}
}
