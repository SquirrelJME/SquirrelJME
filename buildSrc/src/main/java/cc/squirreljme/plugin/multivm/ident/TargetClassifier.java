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
}
