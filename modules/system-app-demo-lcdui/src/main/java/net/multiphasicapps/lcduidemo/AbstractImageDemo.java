// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.lcduidemo;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

/**
 * This is the base for image demos.
 *
 * @since 2019/04/15
 */
public abstract class AbstractImageDemo
	extends MIDlet
{
	/** No mirrors. */
	static final int[] _NO_MIRROR =
		new int[]{Sprite.TRANS_NONE, Sprite.TRANS_ROT90,
			Sprite.TRANS_ROT180, Sprite.TRANS_ROT270};
			
	/** Mirrors. */
	static final int[] _MIRROR =
		new int[]{Sprite.TRANS_MIRROR, Sprite.TRANS_MIRROR_ROT90,
			Sprite.TRANS_MIRROR_ROT180, Sprite.TRANS_MIRROR_ROT270};
	
	/** The images to use. */
	protected final Image[] images;
	
	/**
	 * Initializes the image demo.
	 *
	 * @param __pivot The class pivot for resource lookup.
	 * @param __ext The file extension to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2026/11/15
	 */
	public AbstractImageDemo(Class<?> __pivot, String __ext)
		throws NullPointerException
	{
		if (__pivot == null || __ext == null)
			throw new NullPointerException("NARG");
		
		// There may be multiple images
		List<Image> images = new ArrayList<>();
		
		// Load image data
		for (int i = 0; i >= 0; i++)
			try (InputStream in = __pivot.getResourceAsStream(
				String.format("image%s.%s", (i == 0 ? "" : i), __ext)))
			{
				// No more resources?
				if (in == null)
					break;
				
				images.add(Image.createImage(in));
			}
			
			// Failed to load, ignore
			catch (IOException e)
			{
				e.printStackTrace();
			}
		
		// Set all used images
		this.images = images.toArray(new Image[images.size()]);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/15
	 */
	@Override
	protected void destroyApp(boolean __uc)
		throws MIDletStateChangeException
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/15
	 */
	@Override
	protected void startApp()
		throws MIDletStateChangeException
	{
		// Setup canvas
		ImageDemoCanvas cv = new ImageDemoCanvas(this.images);
		cv.setTitle("Image Demo");
		
		// Exit command
		cv.addCommand(Exit.command);
		cv.setCommandListener(new Exit());
		
		// Set display to the canvas
		Display.getDisplay(this).setCurrent(cv);
	}
}

