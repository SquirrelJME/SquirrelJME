// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package mleui;

import cc.squirreljme.jvm.mle.brackets.UIDisplayBracket;
import cc.squirreljme.jvm.mle.constants.UIInputFlag;
import cc.squirreljme.jvm.mle.constants.UIMetricType;
import cc.squirreljme.jvm.mle.constants.UIPixelFormat;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.lcdui.mle.UIBackend;

/**
 * Tests that display metrics work properly.
 *
 * @since 2020/10/10
 */
public class TestDisplayMetrics
	extends BaseBackend
{
	/** The alpha mask for colors. */
	private static final int _ALPHA_MASK =
		0xFF_000000;
	
	/**
	 * {@inheritDoc}
	 * @since 2020/10/10
	 */
	@Override
	public void test(UIBackend __backend, UIDisplayBracket __display)
		throws Throwable
	{
		// Check that the lower count fails
		try
		{
			__backend.metric(-1);
			this.secondary("bad-lower", true);
		}
		catch (MLECallError e)
		{
			this.secondary("good-lower", true);
		}
		
		// Check that the upper count fails
		try
		{
			__backend.metric(UIMetricType.NUM_METRICS);
			this.secondary("bad-upper", true);
		}
		catch (MLECallError e)
		{
			this.secondary("good-upper", true);
		}
		
		// Test each individual metric
		int okayCount = 0;
		for (int metric = 0; metric < UIMetricType.NUM_METRICS; metric++)
			try
			{
				this.testMetric(__backend, metric);
				okayCount++;
			}
			catch (MLECallError e)
			{
				e.printStackTrace();
				
				this.secondary("fail-" + metric, true);
			}
		
		this.secondary("okay", okayCount);
		this.secondary("total", (int)UIMetricType.NUM_METRICS);
	}
	
	/**
	 * Tests that the given metrics are correctly implemented.
	 * 
	 * @param __backend The backend to test.
	 * @param __metric The {@link UIMetricType} to test.
	 * @since 2020/10/10
	 */
	private void testMetric(UIBackend __backend, int __metric)
	{
		switch (__metric)
		{
			case UIMetricType.UIFORMS_SUPPORTED: 
				this.secondary("forms-supported", 0 != __backend
					.metric(__metric));
				break;
			
			case UIMetricType.DISPLAY_MAX_WIDTH:
				this.secondary("positive-max-width", __backend
					.metric(__metric) > 0);
				break;
			
			case UIMetricType.DISPLAY_MAX_HEIGHT:
				this.secondary("positive-max-height", __backend
					.metric(__metric) > 0);
				break;
			
			case UIMetricType.DISPLAY_CURRENT_WIDTH:
				this.secondary("positive-current-width",
				__backend.metric(__metric) > 0);
				break;
			
			case UIMetricType.DISPLAY_CURRENT_HEIGHT:
				this.secondary("positive-current-height",
				__backend.metric(__metric) > 0);
				break;
			
				// Only two values are okay as this is a boolean
			case UIMetricType.DISPLAY_MONOCHROMATIC:
				int mono = __backend.metric(__metric);
				this.secondary("mono", (mono == 0 || mono == 1));
				break;
			
			case UIMetricType.DISPLAY_PIXEL_FORMAT:
				int pf = __backend.metric(__metric);
				this.secondary("pf",
					(pf >= 0 && pf < UIPixelFormat.NUM_PIXEL_FORMATS));
				
				// These pixel formats are allowed to be monochromatic, the
				// others are purely color based
				this.secondary("pf-mono-okay",
					TestDisplayMetrics.__canMono(pf) == (0 !=
					__backend.metric(UIMetricType.DISPLAY_MONOCHROMATIC)));
				break;
			
				// These do not make sense to test, but should still result in
				// success
			case UIMetricType.INPUT_FLAGS:
				int inputs = __backend.metric(__metric);
				this.secondary("inputs",
					((inputs & (~UIInputFlag.ALL_MASK))));
				break;
				
				// For colors, the alpha mask must never be set to anything as
				// these are purely RGB colors
			case UIMetricType.COLOR_CANVAS_BACKGROUND:
				int color = __backend.metric(__metric);
				if ((color & TestDisplayMetrics._ALPHA_MASK) != 0)
					this.secondary("bad-color-" + __metric, color);
				break;
			
				// Vibration support is limited to do
			case UIMetricType.SUPPORTS_VIBRATION:
				int vib = __backend.metric(__metric);
				this.secondary("vibrate", (vib == 0 || vib == 1));
				break;
			
			case UIMetricType.LIST_ITEM_HEIGHT:
				this.secondary("positive-list-height",
				__backend.metric(__metric) > 0);
				break;
			
			case UIMetricType.COMMAND_BAR_HEIGHT:
				this.secondary("positive-command-height",
				__backend.metric(__metric) > 0);
				break;
			
			case UIMetricType.SUPPORTS_BACKLIGHT_CONTROL:
				int bl = __backend.metric(__metric);
				this.secondary("backlight", (bl == 0 || bl == 1));
				break;
			
			default:
				throw new FailingExecution("Missing " + __metric);
		}
	}
	
	/**
	 * Tests if the display can be monochromatic with these pixel formats.
	 * 
	 * @param __pf The pixel format.
	 * @return If these can be mono.
	 * @since 2020/10/10
	 */
	private static boolean __canMono(int __pf)
	{
		return (__pf == UIPixelFormat.BYTE_INDEXED256 ||
			__pf == UIPixelFormat.PACKED_INDEXED4 ||
			__pf == UIPixelFormat.PACKED_INDEXED2 ||
			__pf == UIPixelFormat.PACKED_INDEXED1);
	}
}
