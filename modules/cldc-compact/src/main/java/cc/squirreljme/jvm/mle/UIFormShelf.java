// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle;

import cc.squirreljme.jvm.mle.brackets.UIDisplayBracket;
import cc.squirreljme.jvm.mle.brackets.UIFormBracket;
import cc.squirreljme.jvm.mle.brackets.UIItemBracket;
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
	 * Returns the displays which are attached to the system.
	 * 
	 * @return The display heads which are attached to a system, these objects
	 * here represent physical displays.
	 * @throws MLECallError If there are no displays.
	 * @since 2020/07/01
	 */
	public static native UIDisplayBracket[] displays()
		throws MLECallError;
	
	/**
	 * Returns the form that is currently being shown on the display.
	 * 
	 * @param __display The display to query.
	 * @return The form that is currently shown or {@code null}.
	 * @throws MLECallError On null arguments.
	 * @since 2020/07/01
	 */
	public static native UIFormBracket displayCurrent(
		UIDisplayBracket __display)
		throws MLECallError;
	
	/**
	 * Show the given form on the display.
	 * 
	 * @param __display The form to display on screen.
	 * @param __form The form to display.
	 * @throws MLECallError On {@code null} arguments.
	 * @since 2020/07/01
	 */
	public static native void displayShow(UIDisplayBracket __display,
		UIFormBracket __form)
		throws MLECallError;
	
	/**
	 * Checks if the two displays represent the same {@link UIDisplayBracket}.
	 * 
	 * @param __a The first.
	 * @param __b The second.
	 * @return If these are the same display.
	 * @throws MLECallError If either is {@code null}.
	 * @since 2020/07/01
	 */
	public static native boolean equals(UIDisplayBracket __a,
		UIDisplayBracket __b)
		throws MLECallError;
	
	/**
	 * Checks if the two displays represent the same {@link UIFormBracket}.
	 * 
	 * @param __a The first.
	 * @param __b The second.
	 * @return If these are the same form.
	 * @throws MLECallError If either is {@code null}.
	 * @since 2020/07/01
	 */
	public static native boolean equals(UIFormBracket __a,
		UIFormBracket __b)
		throws MLECallError;
	
	/**
	 * Checks if the two displays represent the same {@link UIItemBracket}.
	 * 
	 * @param __a The first.
	 * @param __b The second.
	 * @return If these are the same item.
	 * @throws MLECallError If either is {@code null}.
	 * @since 2020/07/01
	 */
	public static native boolean equals(UIItemBracket __a,
		UIItemBracket __b)
		throws MLECallError;
	
	/**
	 * Deletes the given form.
	 * 
	 * @param __bracket The form to delete.
	 * @throws MLECallError On null arguments or the form could not be deleted.
	 * @since 2020/07/01
	 */
	public static native void formDelete(UIFormBracket __bracket)
		throws MLECallError;
	
	/**
	 * Creates a new form.
	 * 
	 * @return The newly created form.
	 * @throws MLECallError If the form could not be created.
	 * @since 2020/07/01
	 */
	public static native UIFormBracket formNew()
		throws MLECallError;
	
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
