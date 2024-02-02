// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

import cc.squirreljme.runtime.cldc.util.StreamUtils;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import net.multiphasicapps.classfile.BinaryName;
import net.multiphasicapps.classfile.ClassFile;
import net.multiphasicapps.classfile.ClassIdentifier;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.InvalidClassFormatException;
import net.multiphasicapps.zip.ZipException;
import net.multiphasicapps.zip.blockreader.FileChannelBlockAccessor;
import net.multiphasicapps.zip.blockreader.ZipBlockEntry;
import net.multiphasicapps.zip.blockreader.ZipBlockReader;
import org.freedesktop.tango.TangoIconLoader;

/**
 * Utilities.
 *
 * @since 2024/01/22
 */
public final class Utils
{
	/** Short timeout. */
	public static final int SHORT_TIMEOUT =
		1000;
	
	/** Standard timeout value. */
	public static final int TIMEOUT =
		3000;
	
	/** A timeout for a very important item. */
	public static final int IMPORTANT_TIMEOUT =
		1000 * 30;
	
	/**
	 * Not used.
	 * 
	 * @since 2024/01/22
	 */
	private Utils()
	{
	}
	
	/**
	 * Gets the class directory name.
	 *
	 * @param __name The name to get.
	 * @return The directory for the class.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/29
	 */
	private static Path classDirName(BinaryName __name)
		throws NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		// Get identifier fragments
		List<ClassIdentifier> names = __name.identifiers();
		
		// Go down the path tree accordingly
		Path result = null;
		for (int i = 0, n = names.size() - 1; i < n; i++)
		{
			ClassIdentifier name = names.get(i);
			if (result == null)
				result = Paths.get(name.toString());
			else
				result = result.resolve(name.toString());
		}
		
		// Resolve final path
		String baseName = names.get(names.size() - 1).toString() + ".class";
		if (result == null)
			return Paths.get(baseName);
		return result.resolve(baseName);
	}
	
	/**
	 * Loads the given class from the given preferences.
	 *
	 * @param __name The name of the class to load.
	 * @param __prefs The preferences to use for the search path.
	 * @return The resultant class or {@code null} if not found.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/29
	 */
	public static ClassFile[] loadClass(ClassName __name, Preferences __prefs)
		throws NullPointerException
	{
		if (__name == null || __prefs == null)
			throw new NullPointerException("NARG");
		
		// We cannot natively load array or primitive classes in any way
		BinaryName binaryName = __name.binaryName();
		if (binaryName == null || __name.isArray() || __name.isPrimitive())
			return null;
		
		// Determine both the directory based name and the content based name
		Path dirName = Utils.classDirName(binaryName);
		String zipName = binaryName + ".class";
		
		// Go through all the paths to try to load all classes
		List<ClassFile> result = new ArrayList<>();
		for (Path searchPath : __prefs.getClassSearchPath())
			try
			{
				// Can we load from a directory?
				Path wantDir = searchPath.resolve(dirName);
				if (Files.exists(wantDir))
					try (InputStream in = Files.newInputStream(
						wantDir, StandardOpenOption.READ))
					{
						result.add(ClassFile.decode(in));
					}
					catch (InvalidClassFormatException|IOException __ignored)
					{
						// Ignore
					}
				
				// Try to load from Zip
				try (ZipBlockReader zip = new ZipBlockReader(
					new FileChannelBlockAccessor(searchPath)))
				{
					ZipBlockEntry entry = zip.get(zipName);
					if (entry != null)
						try (InputStream in = entry.open())
						{
							result.add(ClassFile.decode(in));
						}
				}
				catch (ZipException __ignored)
				{
					// Ignore
				}
			}
			catch (IOException __ignored)
			{
				// Ignored
			}
		
		// Return all the found classes
		return result.toArray(new ClassFile[result.size()]);
	}
	
	/**
	 * Opens the inspector selection.
	 *
	 * @param __owner The owning window.
	 * @param __state The debugger state.
	 * @param __items The items to choose for inspection.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/20
	 */
	public static void inspect(Window __owner, DebuggerState __state,
		InfoKind __kind, Info[] __items)
		throws NullPointerException
	{
		if (__state == null ||__items == null)
			throw new NullPointerException("NARG");
	
		// Pop up dialog asking what to inspect?
		Object result = JOptionPane.showInputDialog(__owner,
			String.format("Select a %s to inspect",
				(__kind == null ? "thing" : __kind.singular)),
			String.format("Choosing a %s",
				(__kind == null ? "thing" : __kind.singular)),
			JOptionPane.QUESTION_MESSAGE,
			null,
			__items,
			__items[0]);
		
		// When selected, show the inspector
		if (result != null)
			Utils.inspect(__owner, __state, (Info)result);
	}
	
	/**
	 * Opens the inspector selection.
	 *
	 * @param __owner The owning window.
	 * @param __state The debugger state.
	 * @param __stored The stored items to look at.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/20
	 */
	public static void inspect(Window __owner, DebuggerState __state,
		StoredInfo<?> __stored)
		throws NullPointerException
	{
		if (__state == null ||__stored == null)
			throw new NullPointerException("NARG");
		
		InfoKind type = __stored.type;
		
		// Check to see that we actually know stuff
		Info[] all = __stored.all(__state);
		if (all.length == 0)
		{
			JOptionPane.showMessageDialog(__owner,
				String.format("There are no %s.", type.plural),
				"Error",
				JOptionPane.ERROR_MESSAGE,
				null);
			return;
		}
		
		// Show inspector with all items
		Utils.inspect(__owner, __state, __stored.type, all);
	}
	
	/**
	 * Opens the inspector on the item.
	 *
	 * @param __owner The owning window.
	 * @param __state The debugger state.
	 * @param __info The information to expect.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/20
	 */
	public static void inspect(Window __owner, DebuggerState __state,
		Info __info)
		throws NullPointerException
	{
		if (__state == null || __info == null)
			throw new NullPointerException("NARG");
		
		// Depends on the type
		Inspect<?> dialog;
		switch (__info.kind)
		{
				// Class
			case CLASS:
				dialog = new InspectClass(__owner, __state,
					(InfoClass)__info);
				break;
				
				// Method
			case METHOD:
				dialog = new InspectMethod(__owner, __state,
					(InfoMethod)__info);
				break;
			
				// Thread
			case THREAD:
				dialog = new InspectThread(__owner, __state,
					(InfoThread)__info);
				break;
				
				// Unknown, so ignore
			default:
				return;
		}
		
		// Show it
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);
	}
	
	/**
	 * Returns the lex icon.
	 *
	 * @return The lex icon.
	 * @since 2024/01/28
	 */
	public static Image lexIcon()
	{
		
		// Set icon for the application
		try (InputStream in = PrimaryFrame.class.getResourceAsStream(
			"icon.png"))
		{
			if (in != null)
				return ImageIO.read(in);
		}
		catch (IOException ignored)
		{
		}
		
		// Not found or loadable?
		return null;
	}
	
	/**
	 * Maximizes the given frame.
	 *
	 * @param __frame The frame to maximize.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/02/02
	 */
	public static void maximize(JFrame __frame)
		throws NullPointerException
	{
		if (__frame == null)
			throw new NullPointerException("NARG");
		
		// Maximizing on Windows causes the frame to cover the taskbar,
		// especially if it is not in the standard position of being at the
		// bottom of the screen
		if (System.getProperty("os.name").toLowerCase(Locale.ROOT)
			.contains("windows"))
		{
			GraphicsEnvironment env =
				GraphicsEnvironment.getLocalGraphicsEnvironment();
			__frame.setMaximizedBounds(env.getMaximumWindowBounds());
		}
		
		__frame.setExtendedState(__frame.getExtendedState() |
			Frame.MAXIMIZED_BOTH);
	}
	
	/**
	 * Makes a text like button.
	 *
	 * @param __button The button to update.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/25
	 */
	public static void prettyTextButton(JButton __button)
		throws NullPointerException
	{
		if (__button == null)
			throw new NullPointerException("NARG");
		
		// Reduce padding and looks of the button
		__button.setBorder(new EmptyBorder(0, 0, 0, 0));
		__button.setBorderPainted(false);
		__button.setContentAreaFilled(false);
		
		// Align to the left
		__button.setHorizontalAlignment(SwingConstants.LEFT);
		
		// Allow it to be copied
		__button.setBorder(new EmptyBorder(0, 0, 0, 0));
		
		// Use a better font for the label
		Font descFont = __button.getFont();
		__button.setFont(new Font("monospaced",
			descFont.getStyle(), descFont.getSize()));
	}
	
	/**
	 * Makes the label look better.
	 *
	 * @param __label The label to edit.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/21
	 */
	public static void prettyTextField(JTextField __label)
		throws NullPointerException
	{
		if (__label == null)
			throw new NullPointerException("NARG");
		
		// Reduce padding
		__label.setBorder(new EmptyBorder(0, 0, 0, 0));
		
		// Align to the left
		__label.setHorizontalAlignment(SwingConstants.LEFT);
		
		// Allow it to be copied
		__label.setEditable(false);
		__label.setBorder(new EmptyBorder(0, 0, 0, 0));
		
		// Use a better font for the label
		Font descFont = __label.getFont();
		__label.setFont(new Font("monospaced",
			descFont.getStyle(), descFont.getSize()));
	}
	
	/**
	 * Revalidates and paints the component.
	 *
	 * @param __component The component to update.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/27
	 */
	public static void revalidate(JComponent __component)
		throws NullPointerException
	{
		if (__component == null)
			throw new NullPointerException("NARG");
		
		__component.revalidate();
		__component.repaint();
	}
	
	/**
	 * Sets the icon for the window.
	 *
	 * @param __window The window to set the icon for.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/24
	 */
	public static void setIcon(Window __window)
		throws NullPointerException
	{
		if (__window == null)
			throw new NullPointerException("NARG");
		
		__window.setIconImage(Utils.lexIcon());
	}
	
	/**
	 * If the current thread is the Swing dispatch thread then the specified
	 * {@link Runnable} is run immediately, otherwise it will be invoked later
	 * in the event thread.
	 *
	 * @param __run The runnable to execute.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/27
	 */
	public static void swingInvoke(Runnable __run)
		throws NullPointerException
	{
		if (__run == null)
			throw new NullPointerException("NARG");
		
		// It is completely safe to call this as we are in the event thread
		// so do that
		if (SwingUtilities.isEventDispatchThread())
			__run.run();
		
		// Otherwise execute it later
		else
			SwingUtilities.invokeLater(__run);
	}
	
	/**
	 * Gets the stack trace of the given exception.
	 *
	 * @param __e The exception to get the stack trace of.
	 * @return The string containing the stack trace.
	 * @since 2024/01/22
	 */
	public static String throwableTrace(Throwable __e)
	{
		String message;
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
			 PrintStream ps = new PrintStream(baos, true,
				"utf-8"))
		{
			// Print to the output
			__e.printStackTrace(ps);
			
			// Make sure it is flushed
			ps.flush();
			
			message = baos.toString("utf-8");
		}
		catch (IOException __f)
		{
			// Ignore
			message = "Could not emit trace, check stderr.";
		}
		return message;
	}
	
	/**
	 * Gets an icon from the Tango theme.
	 *
	 * @param __name The name of the icon to get.
	 * @return The resultant icon data.
	 * @since 2024/01/24
	 */
	public static ImageIcon tangoIcon(String __name)
		throws NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		// Set icon for the toolbar item
		if (!__name.equals("-"))
			try (InputStream in = TangoIconLoader.loadIcon(16, __name))
			{
				if (in != null)
					return new ImageIcon(StreamUtils.readAll(in));
			}
			catch (IOException __e)
			{
				__e.printStackTrace();
			}
		
		// Blank nothingness
		return new ImageIcon(new BufferedImage(16, 16,
			BufferedImage.TYPE_INT_ARGB));
	}
	
	/**
	 * Pops up a dialog showing the given trace.
	 *
	 * @param __parent The parent dialog.
	 * @param __title The dialog title.
	 * @param __e The exception to show.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/22
	 */
	public static void throwableTraceDialog(JFrame __parent,
		String __title, Throwable __e)
		throws NullPointerException
	{
		JOptionPane.showMessageDialog(__parent,
			Utils.throwableTrace(__e),
			__title,
			JOptionPane.ERROR_MESSAGE);
	}
}
