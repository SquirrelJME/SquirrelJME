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
import cc.squirreljme.jvm.mle.scritchui.ScritchLAFInterface;
import cc.squirreljme.jvm.mle.scritchui.constants.ScritchLAFImageElementType;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.util.MathUtils;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.image.BufferedImage;
import javax.swing.UIManager;

/**
 * Swing look and feel interface.
 *
 * @since 2024/03/13
 */
@Deprecated
public class SwingLAFInterface
	implements ScritchLAFInterface
{
	/** Fallback size for widgets. */
	public static final int FALLBACK_SIZE =
		16;
	
	/** The size of message icons. */
	public static final int MESSAGE_ICON_SIZE = 
		32;
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/13
	 */
	@Override
	public int elementColor(int __element)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/13
	 */
	@Override
	public int focusBorderStyle(boolean __focused)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/13
	 */
	@Override
	public int imageSize(int __elem, boolean __height)
		throws MLECallError
	{
		switch (__elem)
		{
			case ScritchLAFImageElementType.LIST_ELEMENT:
				return SwingLAFInterface.__sizeFont("List.font");
				
			case ScritchLAFImageElementType.CHOICE_GROUP:
				return SwingLAFInterface.__sizeFont("RadioButton.font");
				
			case ScritchLAFImageElementType.TAB:
				return SwingLAFInterface.__sizeFont("TabbedPane.font");
				
			case ScritchLAFImageElementType.ALERT:
			case ScritchLAFImageElementType.NOTIFICATION:
				return SwingLAFInterface.MESSAGE_ICON_SIZE;
				
			case ScritchLAFImageElementType.COMMAND:
			case ScritchLAFImageElementType.MENU:
				return SwingLAFInterface.__sizeFont("MenuItem.font");
		}
		
		throw new MLECallError("Invalid element type: " + __elem);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/24
	 */
	@Override
	public boolean isDarkMode()
	{
		// Never in dark mode
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/19
	 */
	@Override
	public int panelBackgroundColor()
	{
		return SwingLAFInterface.__color("Panel.background",
			0xFF_FFFFFF);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/19
	 */
	@Override
	public int panelForegroundColor()
	{
		return SwingLAFInterface.__color("Panel.foreground",
			0xFF_000000);
	}
	
	/**
	 * Returns the given color.
	 *
	 * @param __key The color to get.
	 * @param __default The default color to use.
	 * @return The resultant color or {@code __default}.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/03/19
	 */
	private static int __color(String __key, int __default)
		throws NullPointerException
	{
		if (__key == null)
			throw new NullPointerException("NARG");
		
		Color color = UIManager.getColor(__key);
		if (color != null)
			return color.getRGB();
		return __default;
	}
	
	/**
	 * Returns the size of the given font in pixels.
	 *
	 * @param __key The key to get.
	 * @return The resultant size in pixels.
	 * @since 2024/03/17
	 */
	private static int __sizeFont(String __key)
	{
		Font font = UIManager.getFont(__key);
		if (font == null)
			return SwingLAFInterface.FALLBACK_SIZE;
		
		// We need a graphics object to base on, once we get round to power
		// of two
		FontMetrics metrics = new BufferedImage(1, 1,
			BufferedImage.TYPE_INT_RGB).getGraphics().getFontMetrics(font);
		return MathUtils.nearestPowerOfTwo(metrics.getHeight());
	}
}
