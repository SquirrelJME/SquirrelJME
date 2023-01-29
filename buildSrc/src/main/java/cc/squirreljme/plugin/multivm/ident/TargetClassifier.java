// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm.ident;

import cc.squirreljme.plugin.multivm.BangletVariant;
import cc.squirreljme.plugin.multivm.ClutterLevel;
import cc.squirreljme.plugin.multivm.VMSpecifier;
import cc.squirreljme.plugin.multivm.VMType;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

/**
 * Represents the target classifier, the kind of virtual machine and variant
 * is being targeted.
 *
 * @since 2022/10/01
 */
@Value
@AllArgsConstructor
@Builder
public class TargetClassifier
	implements Serializable
{
	/** The virtual machine type. */
	@NonNull
	VMSpecifier vmType;
	
	/** The banglet variant used. */
	@NonNull
	BangletVariant bangletVariant;
	
	/** The clutter level used. */
	@NonNull
	ClutterLevel clutterLevel;
	
	/**
	 * Specifies an alternative virtual machine to use, but with the same
	 * banglet.
	 *
	 * @param __vm The virtual machine to use instead.
	 * @return The modified target classifier.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/12/23
	 */
	public TargetClassifier withVm(VMSpecifier __vm)
		throws NullPointerException
	{
		if (__vm == null)
			throw new NullPointerException("NARG");
		
		return new TargetClassifier(__vm, this.bangletVariant,
			this.clutterLevel);
	}
	
	/**
	 * Returns the classifier to be used by the emulated JIT.
	 * 
	 * @return The classifier with the appropriate VM based on if it supports
	 * emulated JIT.
	 * @since 2022/12/23
	 */
	public TargetClassifier withVmByEmulatedJit()
	{
		if (this.vmType.hasEmulatorJit())
			return new TargetClassifier(VMType.SPRINGCOAT,
				BangletVariant.NONE, ClutterLevel.DEBUG);
		return this;
	}
}
