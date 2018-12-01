// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

import cc.squirreljme.runtime.lcdui.font.SQFFont;
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
			"monospace-8.sqf",
			"monospace-16.sqf",
			"monospace-24.sqf",
			"sansserif-8.sqf",
			"sansserif-16.sqf",
			"sansserif-24.sqf",
			"serif-8.sqf",
			"serif-16.sqf",
			"serif-24.sqf",
		};
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/25
	 */
	@Override
	public void test()
	{
		for (String s : _FONTS)
			try (InputStream in = SQFFont.class.getResourceAsStream(s))
			{
				// Try to read
				SQFFont f = SQFFont.read(in);
				
				// Did read
				this.secondary(s, true);
			}
			catch (Throwable t)
			{
				this.secondary(s, t);
				
				t.printStackTrace();
			}
	}
}

