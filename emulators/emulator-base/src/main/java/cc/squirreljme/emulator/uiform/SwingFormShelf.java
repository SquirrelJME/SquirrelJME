// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.uiform;

import cc.squirreljme.jvm.mle.UIFormShelf;
import cc.squirreljme.jvm.mle.brackets.UIDisplayBracket;
import cc.squirreljme.jvm.mle.brackets.UIFormBracket;
import cc.squirreljme.jvm.mle.constants.UIMetricType;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import java.awt.HeadlessException;
import java.awt.Toolkit;

/**
 * Java Swing implementation of the  form shelf.
 *
 * @since 2020/07/01
 */
public final class SwingFormShelf
{
	/**
	 * Not used.
	 * 
	 * @since 2020/07/01
	 */
	private SwingFormShelf()
	{
	}
	
	/**
	 * Handles {@link UIFormShelf#displays()}.
	 * 
	 * @return The display heads which are attached to a system, these objects
	 * here represent physical displays.
	 * @throws MLECallError If there are no displays.
	 * @since 2020/07/01
	 */
	public static UIDisplayBracket[] displays()
		throws MLECallError
	{
		// Just wrap the instance of the display
		return new UIDisplayBracket[]{SwingDisplay.getInstance()};
	}
	
	/**
	 * Handles
	 * {@link UIFormShelf#displayShow(UIDisplayBracket, UIFormBracket)}. 
	 * 
	 * @param __display The form to display on screen.
	 * @param __form The form to display.
	 * @throws MLECallError On {@code null} arguments.
	 * @since 2020/07/01
	 */
	public static void displayShow(UIDisplayBracket __display,
		UIFormBracket __form)
		throws MLECallError
	{
		if (__display == null || __form == null)
			throw new MLECallError("Null display or form.");
		
		((SwingDisplay)__display).show((SwingForm)__form);
	}
	
	/**
	 * Handles {@link UIFormShelf#formDelete(UIFormBracket)}. 
	 * 
	 * @param __bracket The form to delete.
	 * @throws MLECallError On null arguments or the form could not be deleted.
	 * @since 2020/07/01
	 */
	public static void formDelete(UIFormBracket __bracket)
		throws MLECallError
	{
		if (__bracket == null)
			throw new MLECallError("Null form.");
		
		((SwingForm)__bracket).delete();
	}
	
	/**
	 * Handles {@link UIFormShelf#formNew()}.
	 * 
	 * @return The newly created form.
	 * @throws MLECallError If the form could not be created.
	 * @since 2020/07/01
	 */
	public static UIFormBracket formNew()
		throws MLECallError
	{
		return new SwingForm();
	}
	
	/**
	 * Handles {@link UIFormShelf#metric(int)}. 
	 * 
	 * @param __metricId The {@link UIMetricType}.
	 * @return Metric value.
	 * @throws MLECallError If the call is not valid.
	 * @since 2020/07/01
	 */
	public static int metric(int __metricId)
		throws MLECallError
	{
		if (__metricId < 0 || __metricId >= UIMetricType.NUM_METRICS)
			throw new MLECallError("Invalid metric.");
		
		switch (__metricId)
		{
				// Simplest way to check if forms are supported is if
				// the display is headless or not
			case UIMetricType.UIFORMS_SUPPORTED:
				try
				{
					// Just try to read the screen size
					if (null == Toolkit.getDefaultToolkit()
						.getScreenSize())
						return 0;
					
					return 1;
				}
				catch (HeadlessException e)
				{
					return 0;
				}
			
			default:
				throw new MLECallError("Unknown metric: " + __metricId);
		}
	}
}
