// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm.ident;

import cc.squirreljme.plugin.multivm.BangletVariant;
import cc.squirreljme.plugin.multivm.ClutterLevel;
import cc.squirreljme.plugin.multivm.VMHelpers;
import cc.squirreljme.plugin.multivm.VMSpecifier;
import cc.squirreljme.plugin.multivm.VMType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.gradle.api.tasks.SourceSet;

/**
 * Represents a source set and a target classifier.
 *
 * @since 2022/10/01
 */
@Value
@AllArgsConstructor
@Builder
public class SourceTargetClassifier
{
	/** The source set used. */
	@NonNull
	String sourceSet;
	
	/** The classifier for the target that is used. */
	@NonNull
	TargetClassifier targetClassifier;
	
	/**
	 * Initializes the classifier.
	 * 
	 * @param __sourceSet The source set.
	 * @param __vmType The virtual machine type.
	 * @param __variant The variant used.
	 * @param __clutterLevel The clutter level used.
	 * @since 2022/10/01
	 */
	public SourceTargetClassifier(String __sourceSet, VMSpecifier __vmType,
		BangletVariant __variant, ClutterLevel __clutterLevel)
	{
		this(__sourceSet, new TargetClassifier(__vmType, __variant,
			__clutterLevel));
	}
	
	/**
	 * Returns the banglet variant used.
	 * 
	 * @return The banglet variant used.
	 * @since 2022/10/01
	 */
	public BangletVariant getBangletVariant()
	{
		return this.getTargetClassifier().getBangletVariant();
	}
	
	/**
	 * Returns the virtual machine type.
	 * 
	 * @return The virtual machine type.
	 * @since 2022/10/01
	 */
	public VMSpecifier getVmType()
	{
		return this.getTargetClassifier().getVmType();
	}
	
	/**
	 * Is this the main source set?
	 * 
	 * @return If this is the main source set or not.
	 * @since 2022/10/01
	 */
	public boolean isMainSourceSet()
	{
		return this.sourceSet.equals(SourceSet.MAIN_SOURCE_SET_NAME);
	}
	
	/**
	 * Is this the test fixtures source set?
	 * 
	 * @return If this is the test fixtures source set or not.
	 * @since 2022/10/01
	 */
	public boolean isTestFixturesSourceSet()
	{
		return this.sourceSet.equals(VMHelpers.TEST_FIXTURES_SOURCE_SET_NAME);
	}
	
	/**
	 * Is this the test source set?
	 * 
	 * @return If this is the test source set or not.
	 * @since 2022/10/01
	 */
	public boolean isTestSourceSet()
	{
		return this.sourceSet.equals(SourceSet.TEST_SOURCE_SET_NAME);
	}
	
	/**
	 * Sets up a new classifier with the clutter level.
	 * 
	 * @param __clutterLevel The new clutter level.
	 * @return The classifier with the new clutter level.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/02/05
	 */
	public SourceTargetClassifier withClutterLevel(ClutterLevel __clutterLevel)
		throws NullPointerException
	{
		if (__clutterLevel == null)
			throw new NullPointerException("NARG");
		
		return new SourceTargetClassifier(this.sourceSet,
			this.targetClassifier.withClutterLevel(__clutterLevel));
	}
	
	/**
	 * Modifies this classifier with the given source set.
	 * 
	 * @param __sourceSet The source set to use instead.
	 * @return The classifier for the given source set.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/10/01
	 */
	public SourceTargetClassifier withSourceSet(String __sourceSet)
		throws NullPointerException
	{
		if (__sourceSet == null)
			throw new NullPointerException("NARG");
		
		// No change?
		if (this.sourceSet.equals(__sourceSet))
			return this;
		
		// Is changed
		return new SourceTargetClassifier(__sourceSet, this.targetClassifier);
	}
	
	/**
	 * Specifies an alternative virtual machine to use, but with the same
	 * source set.
	 * 
	 * @param __vm The virtual machine to use instead.
	 * @return The modified source target classifier.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/12/23
	 */
	public SourceTargetClassifier withVm(VMSpecifier __vm)
		throws NullPointerException
	{
		if (__vm == null)
			throw new NullPointerException("NARG");
		
		return new SourceTargetClassifier(this.sourceSet,
			this.targetClassifier.withVm(__vm));
	}
	
	/**
	 * Returns the classifier to be used by the emulated JIT.
	 * 
	 * @return The classifier with the appropriate VM based on if it supports
	 * emulated JIT.
	 * @since 2022/12/23
	 */
	public SourceTargetClassifier withVmByEmulatedJit()
	{
		if (this.targetClassifier.getVmType().hasEmulatorJit())
			return new SourceTargetClassifier(this.sourceSet,
				this.targetClassifier.withVmByEmulatedJit());
		return this;
	}
}
