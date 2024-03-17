// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.scritchui;

import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.jvm.mle.scritchui.constants.ScritchBorderLayoutType;
import java.awt.BorderLayout;

/**
 * General Swing utilities.
 *
 * @since 2024/03/17
 */
public final class SwingScritchUtils
{
	/**
	 * Not used.
	 *
	 * @since 2024/03/17
	 */
	private SwingScritchUtils()
	{
	}
	
	/**
	 * Maps the layout information based on the container's layout.
	 *
	 * @param __container The container to map.
	 * @param __layoutInfo The value to map.
	 * @return The resultant mapped value.
	 * @throws MLECallError On null arguments or the layout is not valid.
	 * @since 2024/03/17
	 */
	public static Object mapLayout(SwingContainerObject __container,
		int __layoutInfo)
		throws MLECallError
	{
		if (__container == null)
			throw new MLECallError("Null arguments.");
		
		// Everything for now is just BorderLayout
		return SwingScritchUtils.mapLayoutBorder(__container, __layoutInfo);
	}
	
	/**
	 * Maps {@link ScritchBorderLayoutType} to {@link BorderLayout}.
	 *
	 * @param __container The container to map.
	 * @param __layoutInfo The value to map.
	 * @return The resultant mapped value.
	 * @throws MLECallError On null arguments or the layout is not valid.
	 * @since 2024/03/17
	 */
	public static Object mapLayoutBorder(SwingContainerObject __container,
		int __layoutInfo)
		throws MLECallError
	{
		if (__container == null)
			throw new MLECallError("Null arguments.");
		
		switch (__layoutInfo)
		{
			case ScritchBorderLayoutType.CENTER:
				return BorderLayout.CENTER;
				
			case ScritchBorderLayoutType.PAGE_START:
				return BorderLayout.PAGE_START;
			
			case ScritchBorderLayoutType.PAGE_END:
				return BorderLayout.PAGE_END;
			
			case ScritchBorderLayoutType.LINE_START:
				return BorderLayout.LINE_START;
			
			case ScritchBorderLayoutType.LINE_END:
				return BorderLayout.LINE_END;
		}
		
		throw new MLECallError("Invalid ScritchBorderLayoutType: " +
			__layoutInfo);
	}
}
