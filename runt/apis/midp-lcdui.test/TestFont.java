// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

import cc.squirreljme.runtime.lcdui.font.PCFFont;
import java.io.InputStream;
import net.multiphasicapps.tac.TestRunnable;

/**
 * Tests that the fonts load.
 *
 * @since 2018/11/25
 */
public class TestFont
	extends TestRunnable
{
	/** The fonts to test reading. */
	private static final String[] _FONTS =
		{
			"monospace-8.pcf",
			"monospace-16.pcf",
			"monospace-24.pcf",
			"sansserif-8.pcf",
			"sansserif-16.pcf",
			"sansserif-24.pcf",
			"serif-8.pcf",
			"serif-16.pcf",
			"serif-24.pcf",
		};
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/25
	 */
	@Override
	public void test()
	{
		for (String s : _FONTS)
			try (InputStream in = PCFFont.class.getResourceAsStream(s))
			{
				// Try to read
				PCFFont f = PCFFont.read(in);
				
				// Did read
				this.secondary(s, true);
			}
			catch (Throwable t)
			{
				this.secondary(s, t);
			}
	}
}

