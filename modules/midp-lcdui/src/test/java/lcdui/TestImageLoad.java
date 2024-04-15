// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package lcdui;

import java.io.InputStream;
import javax.microedition.lcdui.Image;
import net.multiphasicapps.tac.TestFunction;

/**
 * Tests the loading of various images.
 *
 * @since 2022/02/10
 */
public class TestImageLoad
	extends TestFunction<String, Boolean>
{
	/**
	 * {@inheritDoc}
	 * @since 2022/02/10
	 */
	@Override
	public Boolean test(String __arg)
	{
		try (InputStream in = TestImageLoad.class.getResourceAsStream(
			"image." + __arg))
		{
			Image.createImage(in);
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
}
