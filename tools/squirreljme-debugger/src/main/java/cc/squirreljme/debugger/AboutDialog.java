// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

import cc.squirreljme.runtime.cldc.SquirrelJME;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.launcher.ui.SplashScreenImage;
import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * About SquirrelJME Debugger.
 *
 * @since 2024/01/28
 */
public class AboutDialog
	extends JDialog
{
	/**
	 * Initializes the about-dialog.
	 *
	 * @param __owner The owning window.
	 * @since 2024/01/28
	 */
	public AboutDialog(Window __owner)
	{
		super(__owner);
		
		// Set title and icon
		this.setTitle("About SquirrelJME Debugger");
		Utils.setIcon(this);
		
		// Do not allow this to be resized
		this.setResizable(false);
		
		// Get image for the splash screen
		SplashScreenImage gen = new SplashScreenImage();
		
		// Need to load in the image
		BufferedImage image = new BufferedImage(
			SplashScreenImage.WIDTH, SplashScreenImage.HEIGHT,
			BufferedImage.TYPE_INT_RGB);
		image.setRGB(0, 0,
			SplashScreenImage.WIDTH, SplashScreenImage.HEIGHT,
			gen.generateSplash(), 0, SplashScreenImage.WIDTH);
		
		// Border layout keeps things simple as always
		this.setLayout(new BorderLayout());
		
		// Splash on the left!
		JPanel splashPanel = new JPanel();
		splashPanel.setLayout(new FlowLayout(FlowLayout.LEADING,
			0, 0));
		splashPanel.setMinimumSize(new Dimension(
			SplashScreenImage.WIDTH, SplashScreenImage.HEIGHT));
		splashPanel.setPreferredSize(new Dimension(
			SplashScreenImage.WIDTH, SplashScreenImage.HEIGHT));
		
		// Add it in
		this.add(splashPanel, BorderLayout.LINE_START);
		
		// Add a cute picture of Lex!
		JButton splashLabel = new JButton("", new ImageIcon(image,
			"Lex is cute!"));
		Utils.prettyTextButton(splashLabel);
		splashLabel.addActionListener((__event) -> {
				JOptionPane.showMessageDialog(this,
					"Squeak!",
					"Squeak!",
					JOptionPane.INFORMATION_MESSAGE,
					new ImageIcon(Utils.lexIcon()));
			});
		splashPanel.add(splashLabel, BorderLayout.LINE_START);
		
		// Add information on the right side
		SequentialPanel sequence = new SequentialPanel(false);
		
		// Use bold at the start
		JLabel primary = new JLabel(String.format(
			"SquirrelJME Debugger %s", SquirrelJME.RUNTIME_VERSION));
		primary.setHorizontalAlignment(SwingConstants.CENTER);
		primary.setFont(primary.getFont().deriveFont(Font.BOLD));
		sequence.add(primary);
		
		// Space
		sequence.add(new JLabel(" "));
		
		// Website
		JButton site = new JButton(
			"https://squirreljme.cc/");
		site.setHorizontalAlignment(SwingConstants.CENTER);
		site.setFont(site.getFont().deriveFont(Font.PLAIN));
		Utils.prettyTextButton(site);
		site.addActionListener((__event) -> {
			if (Desktop.isDesktopSupported())
			{
				Desktop desktop = Desktop.getDesktop();
				if (desktop.isSupported(Desktop.Action.BROWSE))
					try
					{
						desktop.browse(URI.create("https://squirreljme.cc/"));
					}
					catch (IOException __ignored)
					{
						// Ignore
					}
			}
		});
		sequence.add(site);
		
		// Space
		sequence.add(new JLabel(" "));
		
		// Myself
		JLabel self = new JLabel(
			"Copyright (C) 2013-2024 Stephanie Gawroriski");
		self.setHorizontalAlignment(SwingConstants.CENTER);
		self.setFont(self.getFont().deriveFont(Font.PLAIN));
		sequence.add(self);
		
		// E-Mail
		JLabel email = new JLabel(
			"<xerthesquirrel@gmail.com>");
		email.setHorizontalAlignment(SwingConstants.CENTER);
		email.setFont(email.getFont().deriveFont(Font.PLAIN));
		sequence.add(email);
		
		// Space
		sequence.add(new JLabel(" "));
		
		// Trans rights!
		JLabel transRights = new JLabel("Trans Rights!");
		transRights.setHorizontalAlignment(SwingConstants.CENTER);
		transRights.setFont(email.getFont().deriveFont(Font.ITALIC));
		sequence.add(transRights);
		
		// Add the sequence in
		this.add(sequence.panel(), BorderLayout.CENTER);
		
		// Close the dialog
		JButton close = new JButton("Awesome!");
		close.addActionListener((__event) -> {
			this.setVisible(false);
			this.dispose();
		});
		this.add(close, BorderLayout.PAGE_END);
		
		// Efficiency
		this.pack();
	}
}
