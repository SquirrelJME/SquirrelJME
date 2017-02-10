// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.host.javase;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Text;
import net.multiphasicapps.squirreljme.lcdui.BasicGraphics;

/**
 * This wraps the AWT graphics to the one the LCDUI code uses and needs to
 * draw with.
 *
 * @since 2017/02/08
 */
public class AWTGraphicsAdapter
	extends BasicGraphics
{
	/** Dotted line. */
	private static final BasicStroke _DOTTED_STROKE =
		new BasicStroke(1.0F, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
			0.0F, new float[]{1.0F}, 0.0F);
	
	/** Solid line. */
	private static final BasicStroke _SOLID_STROKE =
		new BasicStroke(1.0F, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);
	
	/** No blending. */
	private static final AlphaComposite _SRC_BLEND =
		AlphaComposite.getInstance(AlphaComposite.SRC);
	
	/** Alpha colors. */
	private static final AlphaComposite[] _ALPHA_LEVELS =
		new AlphaComposite[256];
	
	/** Wrapped AWT graphics (where things go to). */
	volatile java.awt.Graphics2D _awt;
	
	/** The last color used. */
	private volatile int _lastcolor;
	
	/** The color cache. */
	private volatile Color _cachecolor =
		new Color(this._lastcolor);
	
	/**
	 * Initializes some common parts.
	 *
	 * @since 2017/02/10
	 */
	static
	{
		// Pre-cache all alpha levels
		AlphaComposite v = AlphaComposite.getInstance(AlphaComposite.SRC_OVER);
		AlphaComposite[] alphas = _ALPHA_LEVELS;
		for (int i = 0; i < 256; i++)
			alphas[i] = v.derive(i / 255.0F);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	protected void primitiveLine(int __x1, int __y1, int __x2,
		int __y2, int __color, boolean __dotted, boolean __blend)
	{
		// Set line details
		java.awt.Graphics2D awt = this._awt;
		awt.setStroke((__dotted ? _DOTTED_STROKE : _SOLID_STROKE));
		awt.setComposite((__blend ? _ALPHA_LEVELS[(__color >> 24) & 0xFF] :
			_SRC_BLEND));
		
		// Cached color?
		__color = __color & 0xFFFFFF;
		Color usecolor;
		if (__color != this._lastcolor)
		{
			this._lastcolor = __color;
			this._cachecolor = (usecolor = new Color(__color));
		}
		
		// Use-precached color
		else
			usecolor = this._cachecolor;
		
		// Set
		awt.setColor(usecolor);
		
		// Draw
		awt.drawLine(__x1, __y1, __x2, __y2);
	}
}

