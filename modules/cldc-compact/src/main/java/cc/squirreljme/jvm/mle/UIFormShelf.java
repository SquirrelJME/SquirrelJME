// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle;

import cc.squirreljme.jvm.mle.constants.UIFormSpecialPositionType;
import cc.squirreljme.jvm.mle.constants.UIMetricType;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;

/**
 * This is the shelf which manages all of the form based user interface that
 * LCDUI uses and such.
 * 
 * Every item on the form has an index, while some have special index numbers
 * which indicate that the element should be placed in a unique position. The
 * special positions are identified in {@link UIFormSpecialPositionType}.
 *
 * @since 2020/06/30
 */
public final class UIFormShelf
{
	/**
	 * Not used.
	 * 
	 * @since 2020/06/30
	 */
	private UIFormShelf()
	{
	}
	
	/**
	 * Returns a metric which describes something about the user interface
	 * forms implementation or other details about the system.
	 * 
	 * @param __metric One of {@link UIMetricType}. The metric
	 * {@link UIMetricType#UIFORMS_SUPPORTED} is always a valid metric and
	 * must be supported, even if the implementation lacks forms.
	 * @return The value of the metric.
	 * @throws MLECallError If the metric is out of range or forms are not
	 * supported and the metric is not {@link UIMetricType#UIFORMS_SUPPORTED}.
	 * @since 2020/06/30
	 */
	public static native int metric(int __metric)
		throws MLECallError;
}
