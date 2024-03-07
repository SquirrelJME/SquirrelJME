// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.scritchui;

import cc.squirreljme.jvm.mle.scritchui.constants.ScritchWindowManagerType;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.Range;

/**
 * Interface which describes the environment ScritchUI is running under.
 *
 * @since 2024/03/07
 */
@SquirrelJMEVendorApi
public interface ScritchEnvironmentInterface
{
	/**
	 * Returns the type of window manager ScritchUI is running on.
	 *
	 * @return One of {@link ScritchWindowManagerType}.
	 * @see ScritchWindowManagerType
	 * @since 2024/03/07
	 */
	@SquirrelJMEVendorApi
	@Range(from = 0, to = ScritchWindowManagerType.NUM_TYPES)
	@MagicConstant(valuesFromClass = ScritchWindowManagerType.class)
	int windowManagerType();
}
