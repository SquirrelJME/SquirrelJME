// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

import cc.squirreljme.jdwp.JDWPCommandSet;
import cc.squirreljme.jdwp.JDWPCommandSetVirtualMachine;
import cc.squirreljme.jdwp.JDWPEventKind;
import cc.squirreljme.jdwp.JDWPId;
import cc.squirreljme.jdwp.JDWPIdKind;
import cc.squirreljme.jdwp.JDWPPacket;
import cc.squirreljme.jdwp.JDWPStepDepth;
import cc.squirreljme.jdwp.JDWPStepSize;
import cc.squirreljme.jdwp.JDWPSuspendPolicy;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import net.multiphasicapps.classfile.ClassFile;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.FieldDescriptor;
import net.multiphasicapps.classfile.InvalidClassFormatException;
import net.multiphasicapps.classfile.MethodNameAndType;

/**
 * Primary display window.
 *
 * @since 2024/01/19
 */
public class PrimaryFrame
	extends JFrame
{
	/** The debugger state. */
	protected final DebuggerState state;
	
	/** The status panel. */
	protected final StatusPanel statusPanel;
	
	/** The toolbar for common actions. */
	protected final JToolBar toolBar;
	
	/** Thread view. */
	protected final ShownThreads shownThreads;
	
	/** Currently shown context. */
	protected final ContextThreadFrame context;
	
	/** The shown context thread. */
	protected final ShownContextMethod shownContext;
	
	/** Debugger preferences. */
	protected final Preferences preferences;
	
	/**
	 * Initializes the primary frame.
	 *
	 * @param __state The main debugging state.
	 * @param __preferences Preferences for the debugger.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/19
	 */
	public PrimaryFrame(DebuggerState __state, Preferences __preferences)
		throws NullPointerException
	{
		if (__state == null || __preferences == null)
			throw new NullPointerException("NARG");
		
		// Store state for later usage
		this.state = __state;
		this.context = __state.context;
		this.preferences = __preferences;
		
		// To keep it more easily workable
		this.setMinimumSize(new Dimension(640, 480));
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		// Set window icon
		Utils.setIcon(this);
		
		// Insignia
		this.setTitle("SquirrelJME Debugger");
		
		// Default shortcut key mask, like Apple's Command button
		int metaMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
		
		// Preferences
		JMenuItem preferencesItem = new JMenuItem("Preferences...");
		preferencesItem.setMnemonic('P');
		preferencesItem.setAccelerator(
			KeyStroke.getKeyStroke(Character.valueOf('s'), 
				InputEvent.ALT_DOWN_MASK | metaMask));
		preferencesItem.addActionListener((__event) -> {
			PreferencesDialog prefs = new PreferencesDialog(this,
				this.preferences);
			prefs.setLocationRelativeTo(null);
			prefs.setVisible(true);
		});
		
		// About the debugger
		JMenuItem aboutItem = new JMenuItem("About...");
		aboutItem.setMnemonic('A');
		aboutItem.setIcon(new ImageIcon(Utils.lexIcon()));
		aboutItem.setAccelerator(
			KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
		aboutItem.addActionListener((__event) -> {
			AboutDialog about = new AboutDialog(this);
			about.setLocationRelativeTo(null);
			about.setVisible(true);
		});
		
		// Exit
		JMenuItem exitItem = PrimaryFrame.__menuItem(
			"Exit", "system-log-out");
		exitItem.setMnemonic('x');
		exitItem.setAccelerator(
			KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_DOWN_MASK));
		
		// File menu
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic('F');
		fileMenu.add(preferencesItem);
		fileMenu.addSeparator();
		fileMenu.add(aboutItem);
		fileMenu.addSeparator();
		fileMenu.add(exitItem);
		
		// Refresh
		JMenuItem refreshItem = PrimaryFrame.__menuItem(
			"Refresh", "view-refresh");
		refreshItem.setMnemonic('R');
		refreshItem.addActionListener(this::__refresh);
		refreshItem.setAccelerator(
			KeyStroke.getKeyStroke(Character.valueOf('r'), metaMask));
		
		// Capabilities view
		JMenuItem capsItem = new JMenuItem("Capabilities");
		capsItem.setMnemonic('C');
		capsItem.addActionListener(this::__inspectCapabilities);
		
		// Object view
		JMenuItem objectItem = new JMenuItem("Object");
		objectItem.setMnemonic('O');
		objectItem.setAccelerator(
			KeyStroke.getKeyStroke(Character.valueOf('o'), metaMask));
		
		// Type view
		JMenuItem typeItem = new JMenuItem("Type");
		typeItem.setMnemonic('y');
		typeItem.setAccelerator(
			KeyStroke.getKeyStroke(Character.valueOf('y'), metaMask));
		typeItem.addActionListener(this::__inspectType);
		
		// Thread group
		JMenuItem threadGroupItem = new JMenuItem("Thread Group");
		threadGroupItem.setMnemonic('G');
		threadGroupItem.setAccelerator(
			KeyStroke.getKeyStroke(Character.valueOf('g'), metaMask));
		
		// Thread
		JMenuItem threadItem = new JMenuItem("Thread");
		threadItem.setMnemonic('T');
		threadItem.setAccelerator(
			KeyStroke.getKeyStroke(Character.valueOf('t'), metaMask));
		threadItem.addActionListener(this::__inspectThread);
		
		// View menu
		JMenu viewMenu = new JMenu("View");
		viewMenu.setMnemonic('V');
		viewMenu.add(refreshItem);
		viewMenu.add(capsItem);
		viewMenu.add(objectItem);
		viewMenu.add(typeItem);
		viewMenu.add(threadGroupItem);
		viewMenu.add(threadItem);
		
		// Resume items
		JMenuItem resumeSingleItem = PrimaryFrame.__menuItem(
			"Resume Single Thread", "media-playback-start");
		resumeSingleItem.addActionListener(this::__threadSingleResume);
		resumeSingleItem.setAccelerator(
			KeyStroke.getKeyStroke(KeyEvent.VK_F9, 0));
		JMenuItem suspendSingleItem = PrimaryFrame.__menuItem(
			"Suspend Single Thread", "media-playback-pause");
		suspendSingleItem.addActionListener(this::__threadSingleSuspend);
		suspendSingleItem.setAccelerator(
			KeyStroke.getKeyStroke(KeyEvent.VK_F5, 
				InputEvent.SHIFT_DOWN_MASK));
		
		JMenuItem resumeAllItem = PrimaryFrame.__menuItem(
			"Resume All Threads", "weather-clear");
		resumeAllItem.addActionListener(this::__threadAllResume);
		resumeAllItem.setAccelerator(
			KeyStroke.getKeyStroke(KeyEvent.VK_F9,
				InputEvent.CTRL_DOWN_MASK));
		JMenuItem suspendAllItem = PrimaryFrame.__menuItem(
			"Suspend All Threads", "weather-snow");
		suspendAllItem.addActionListener(this::__threadAllSuspend);
		suspendAllItem.setAccelerator(
			KeyStroke.getKeyStroke(KeyEvent.VK_F5, 
				InputEvent.SHIFT_DOWN_MASK |
					InputEvent.CTRL_DOWN_MASK));
		
		JMenuItem singleStepIntoItem = PrimaryFrame.__menuItem(
			"Single Step Into", "go-down");
		singleStepIntoItem.addActionListener(this::__singleStepMinInto);
		singleStepIntoItem.setAccelerator(
			KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0));
		JMenuItem singleStepOverItem = PrimaryFrame.__menuItem(
			"Single Step Over", "go-jump");
		singleStepOverItem.addActionListener(this::__singleStepMinOver);
		singleStepOverItem.setAccelerator(
			KeyStroke.getKeyStroke(KeyEvent.VK_F8, 0));
		JMenuItem singleStepOutItem = PrimaryFrame.__menuItem(
			"Single Step Out", "go-top");
		singleStepOutItem.addActionListener(this::__singleStepMinOut);
		singleStepOutItem.setAccelerator(
			KeyStroke.getKeyStroke(KeyEvent.VK_F8,
				InputEvent.SHIFT_DOWN_MASK));
		
		JMenuItem singleLineIntoItem = PrimaryFrame.__menuItem(
			"Single Line Step Into", "format-indent-more");
		singleLineIntoItem.addActionListener(this::__singleStepLineInto);
		singleLineIntoItem.setAccelerator(
			KeyStroke.getKeyStroke(KeyEvent.VK_F7,
				InputEvent.CTRL_DOWN_MASK));
		JMenuItem singleLineOverItem = PrimaryFrame.__menuItem(
			"Single Line Step Over", "format-justify-center");
		singleLineOverItem.addActionListener(this::__singleStepLineOver);
		singleLineOverItem.setAccelerator(
			KeyStroke.getKeyStroke(KeyEvent.VK_F8,
				InputEvent.CTRL_DOWN_MASK));
		JMenuItem singleLineOutItem = PrimaryFrame.__menuItem(
			"Single Line Step Out", "edit-undo");
		singleLineOutItem.addActionListener(this::__singleStepLineOut);
		singleLineOutItem.setAccelerator(
			KeyStroke.getKeyStroke(KeyEvent.VK_F8,
				InputEvent.CTRL_DOWN_MASK |
					InputEvent.SHIFT_DOWN_MASK));
		
		// Debug Controls
		JMenu debugMenu = new JMenu("Debug");
		debugMenu.setMnemonic('D');
		debugMenu.add(resumeSingleItem);
		debugMenu.add(suspendSingleItem);
		debugMenu.addSeparator();
		debugMenu.add(resumeAllItem);
		debugMenu.add(suspendAllItem);
		debugMenu.addSeparator();
		debugMenu.add(singleStepIntoItem);
		debugMenu.add(singleStepOverItem);
		debugMenu.add(singleStepOutItem);
		debugMenu.addSeparator();
		debugMenu.add(singleLineIntoItem);
		debugMenu.add(singleLineOverItem);
		debugMenu.add(singleLineOutItem);
		
		// Probe vendor specific commands
		JMenuItem vendorProbeItem = new JMenuItem(
			"Probe Vendor Specific Commands");
		vendorProbeItem.setMnemonic('p');
		vendorProbeItem.addActionListener(this::__probeVendorCommands);
		
		// Enable every possible breakpoint to handle missed throws/catches
		JMenuItem paranoidThrows = new JMenuItem(
			"Paranoid Throwable");
		paranoidThrows.setMnemonic('t');
		paranoidThrows.addActionListener(this::__paranoidThrows);
		
		// Advanced Menu
		JMenu advancedMenu = new JMenu("Advanced");
		advancedMenu.add(vendorProbeItem);
		advancedMenu.add(paranoidThrows);
		
		// Menu bar
		JMenuBar mainMenu = new JMenuBar();
		mainMenu.add(fileMenu);
		mainMenu.add(viewMenu);
		mainMenu.add(debugMenu);
		mainMenu.add(advancedMenu);
		
		// Use the menu finally
		this.setJMenuBar(mainMenu);
		
		// Show thread view on the left
		ShownThreads shownThreads = new ShownThreads(__state, this.context);
		shownThreads.setMinimumSize(new Dimension(200, 300));
		shownThreads.setPreferredSize(new Dimension(200, 300));
		shownThreads.setMaximumSize(new Dimension(200, 9999));
		this.add(shownThreads, BorderLayout.LINE_START);
		this.shownThreads = shownThreads;
		
		// Context viewer for the current frame location
		ShownContextMethod shownContext = new ShownContextMethod(
			this.state, this.context, __preferences);
		this.shownContext = shownContext;
		this.add(shownContext, BorderLayout.CENTER);
		
		// Setup toolbar
		JToolBar toolBar = new JToolBar();
		this.toolBar = toolBar;
		
		// Do not allow this to float
		toolBar.setFloatable(false);
		
		JButton viewClassDisk = PrimaryFrame.__barButton(toolBar,
			"View Class From Disk", "document-open");
		viewClassDisk.addActionListener(this::__viewClassDisk);
		
		JButton viewClassLocal = PrimaryFrame.__barButton(toolBar,
			"View Class From Remote", "computer");
		viewClassLocal.addActionListener(this::__viewClassLocal);
		
		JButton viewClassNet = PrimaryFrame.__barButton(toolBar,
			"View Class From Remote", "network-receive");
		viewClassNet.addActionListener(this::__viewClassNetwork);
		
		toolBar.addSeparator();
		
		// Refresh
		JButton refresh = PrimaryFrame.__barButton(toolBar,
			"Refresh", "view-refresh");
		refresh.addActionListener(this::__refresh);
		
		toolBar.addSeparator();
		
		JButton resumeSingle = PrimaryFrame.__barButton(toolBar,
			"Resume Single Thread", "media-playback-start");
		resumeSingle.addActionListener(this::__threadSingleResume);
		JButton suspendSingle = PrimaryFrame.__barButton(toolBar,
			"Suspend Single Thread", "media-playback-pause");
		suspendSingle.addActionListener(this::__threadSingleSuspend);
		
		toolBar.addSeparator();
		
		JButton resumeAll = PrimaryFrame.__barButton(toolBar,
			"Resume All Threads", "weather-clear");
		resumeAll.addActionListener(this::__threadAllResume);
		JButton suspendAll = PrimaryFrame.__barButton(toolBar,
			"Suspend All Threads", "weather-snow");
		suspendAll.addActionListener(this::__threadAllSuspend);
		
		toolBar.addSeparator();
		
		JButton singleStepInto = PrimaryFrame.__barButton(toolBar,
			"Single Step Into", "go-down");
		singleStepInto.addActionListener(this::__singleStepMinInto);
		JButton singleStepOver = PrimaryFrame.__barButton(toolBar,
			"Single Step Over", "go-jump");
		singleStepOver.addActionListener(this::__singleStepMinOver);
		JButton singleStepOut = PrimaryFrame.__barButton(toolBar,
			"Single Step Out", "go-top");
		singleStepOut.addActionListener(this::__singleStepMinOut);
		
		toolBar.addSeparator();
		
		JButton singleLineStepInto = PrimaryFrame.__barButton(toolBar,
			"Single Line Step Into", "format-indent-more");
		singleLineStepInto.addActionListener(this::__singleStepLineInto);
		JButton singleLineStepOver = PrimaryFrame.__barButton(toolBar,
			"Single Line Step Over", "format-justify-center");
		singleLineStepOver.addActionListener(this::__singleStepLineOver);
		JButton singleLineStepOut = PrimaryFrame.__barButton(toolBar,
			"Single Line Step Out", "edit-undo");
		singleLineStepOut.addActionListener(this::__singleStepLineOut);
		
		// Add to the top
		this.add(toolBar, BorderLayout.PAGE_START);
		
		// Setup status panel
		StatusPanel statusPanel = new StatusPanel(__state);
		this.statusPanel = statusPanel;
		
		// Add that to the bottom
		this.add(statusPanel, BorderLayout.PAGE_END);
		
		// Add a timer to keep everything updated accordingly
		Timer updateTimer = new Timer(30_000,
			(__ignored) -> this.update());
		updateTimer.start();
	}
	
	/**
	 * Update via the event handler.
	 *
	 * @param __state The state used.
	 * @param __event The debugging event.
	 * @since 2024/01/26
	 */
	public void handleSingleStep(DebuggerState __state,
		SingleStepEvent __event)
	{
		// Note it
		Debugging.debugNote("Single stepped.");
		this.statusPanel.setMessage("Single stepped.");
		
		// Update context frame from this event, since we are now in a new
		// location
		InfoThread inThread = __event.thread;
		if (inThread != null)
			inThread.frames.update(__state, (__ignored, __value) -> {
				InfoFrame[] frames = __value.get();
				if (frames != null && frames.length > 0)
					this.context.set(frames[0]);
				
				this.update();
			});
		
		// Update information
		this.update();
	}
	
	/**
	 * General update.
	 *
	 * @since 2024/01/26
	 */
	public void update()
	{
		Utils.swingInvoke(() -> {
			// Needs to update second as the thread could have changed
			this.shownContext.update();
		});
	}
	
	/**
	 * Opens the inspector selection.
	 *
	 * @param __stored The stored items to look at.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/20
	 */
	private void __inspect(StoredInfo<?> __stored)
		throws NullPointerException
	{
		Utils.inspect(this, this.state, __stored);
	}
	
	/**
	 * Shows the capabilities of the connected virtual machine.
	 *
	 * @param __event Not used.
	 * @since 2024/01/19
	 */
	private void __inspectCapabilities(ActionEvent __event)
	{
		InspectCapabilities inspect = new InspectCapabilities(this,
			this.state.capabilities);
		
		// Show it
		inspect.setLocationRelativeTo(null);
		inspect.setVisible(true);
	}
	
	/**
	 * Opens the thread inspector.
	 *
	 * @param __event Not used.
	 * @since 2024/01/20
	 */
	private void __inspectThread(ActionEvent __event)
	{
		this.__inspect(this.state.storedInfo.getThreads());
	}
	
	/**
	 * Inspects the given type.
	 *
	 * @param __action Not used.
	 * @since 2024/01/23
	 */
	private void __inspectType(ActionEvent __action)
	{
		// Get every single loaded class because this is really expected
		DebuggerState state = this.state;
		try (JDWPPacket out = state.request(JDWPCommandSet.VIRTUAL_MACHINE,
			JDWPCommandSetVirtualMachine.ALL_CLASSES))
		{
			// Wait for response
			state.send(out,
				(__state, __reply) -> {
					// We need to store the actual classes
					StoredInfo<InfoClass> classes =
						this.state.storedInfo.getClasses();
				
					// Read in all the classes
					int count = __reply.readInt();
					for (int i = 0; i < count; i++)
					{
						// Ignore tag
						__reply.readByte();
						
						// Read the type ID
						JDWPId typeId = __reply.readId(
							JDWPIdKind.REFERENCE_TYPE_ID);
						
						// Read name
						String name = __reply.readString();
						
						// Ignore status
						__reply.readInt();
						
						// Setup class
						InfoClass infoClass = classes.get(__state, typeId);
						if (infoClass != null)
							try
							{
								infoClass.thisName.set(
									new FieldDescriptor(name).className());
							}
							catch (InvalidClassFormatException __e)
							{
								__e.printStackTrace(System.err);
							}
					}
					
					this.__inspect(state.storedInfo.getClasses());
				}, ReplyHandler.IGNORED);
		}
	}
	
	/**
	 * Enables many breakpoints for trying to find throwables that are missed.
	 *
	 * @param __event Ignored.
	 * @since 2024/07/27
	 */
	private void __paranoidThrows(ActionEvent __event)
	{
		DebuggerState state = this.state;
		
		// Run for all of these classes
		for (String classNameRaw : Arrays.asList(
			"java.io.IOException",
			"java.lang.ArrayIndexOutOfBoundsException",
			"java.lang.ArrayStoreException",
			"java.lang.AssertionError",
			"java.lang.ClassCastException",
			"java.lang.ClassNotFoundException",
			"java.lang.Error",
			"java.lang.IllegalAccessException",
			"java.lang.IllegalArgumentException",
			"java.lang.IllegalMonitorStateException",
			"java.lang.IllegalStateException",
			"java.lang.IllegalThreadStateException",
			"java.lang.IncompatibleClassChangeError",
			"java.lang.IndexOutOfBoundsException",
			"java.lang.InterruptedException",
			"java.lang.NegativeArraySizeException",
			"java.lang.NoClassDefFoundError",
			"java.lang.NoSuchFieldError",
			"java.lang.NullPointerException",
			"java.lang.NumberFormatException",
			"java.lang.OutOfMemoryError",
			"java.lang.RuntimeException",
			"java.lang.SecurityException",
			"java.lang.StackOverflowError",
			"java.lang.StringIndexOutOfBoundsException",
			"java.lang.Throwable",
			"java.lang.Throwable",
			"java.lang.UnsupportedClassVersionError",
			"java.lang.UnsupportedOperationException",
			"java.lang.VirtualMachineError"))
		{
			// Note
			Debugging.debugNote("Paranoid: %s", classNameRaw);
			
			// Class matching
			ClassName className = new ClassName(
				classNameRaw.replace('.', '/'));
			EventModifierClassMatch classMatch =
				new EventModifierClassMatch(classNameRaw);
			
			// Exception thrown for this class
			state.eventSet(JDWPEventKind.EXCEPTION,
				JDWPSuspendPolicy.ALL,
				new EventModifier[]{classMatch},
				(__state, __reply) -> {}, null);
			state.eventSet(JDWPEventKind.EXCEPTION_CATCH,
				JDWPSuspendPolicy.ALL,
				new EventModifier[]{classMatch},
				(__state, __reply) -> {}, null);
			
			// Exception by class ID
			state.lookupClass(className, (__info) -> {
				// Note
				Debugging.debugNote("Catching: %s (%d)", className);
				
				// Setup ID catch
				InfoClass lookAt = __info[0];
				EventModifierClassOnly onlyClass =
					new EventModifierClassOnly(lookAt.id);
				
				// Exception thrown for this class, as its ID
				state.eventSet(JDWPEventKind.EXCEPTION,
					JDWPSuspendPolicy.ALL,
					new EventModifier[]{onlyClass},
					(__state, __reply) -> {}, null);
				state.eventSet(JDWPEventKind.EXCEPTION_CATCH,
					JDWPSuspendPolicy.ALL,
					new EventModifier[]{onlyClass},
					(__state, __reply) -> {}, null);
				
				lookAt.methods();
				InfoMethod[] methods = lookAt.methods();
				
				// Try breaking on any <init> method
				if (methods != null)
					for (InfoMethod methodInfo : methods)
					{
						// Wrap viewer to find out what this is
						MethodViewer mv = new  RemoteMethodViewer(state,
							methodInfo);
						
						// Resolve method name and type
						// We mostly just care for the type
						mv.methodNameAndType();
						MethodNameAndType nat = mv.methodNameAndType();
						
						// If this is a constructor, then breakpoint when
						// entered
						if (nat != null && "<init>".equals(nat.name()
							.toString()))
						{
							// Set breakpoint
							EventModifierLocationOnly mod =
								new EventModifierLocationOnly(
									lookAt.id,
									methodInfo.id,
									0);
							state.eventSet(JDWPEventKind.BREAKPOINT,
								JDWPSuspendPolicy.ALL,
								new EventModifier[]{mod},
								(__state, __reply) -> {}, null);
						}
					}
			}, (__e) -> {
				Debugging.debugNote("Ignoring unknown: %s", className);
			});
		}
	}
	
	/**
	 * Probes for vendor specific commands in the remote virtual machine.
	 *
	 * @param __event Ignored.
	 * @since 2024/01/28
	 */
	private void __probeVendorCommands(ActionEvent __event)
	{
		// Really mean this
		if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(
			this,
			"This probes all 32768 vendor specific JDWP commands\n" +
				"which may overload the JVM and/or cause it to crash\n" +
				"and/or have undesired effects.\n" +
				"You have been warned, continue?",
			"Are you sure?",
			JOptionPane.YES_NO_OPTION,
			JOptionPane.QUESTION_MESSAGE,
			null))
		{
			VendorCommandProbe probe = new VendorCommandProbe(this,
				this.state);
			probe.setLocationRelativeTo(null);
			probe.setVisible(true);
		}
	}
	
	/**
	 * Refreshes the view.
	 *
	 * @param __event Ignored.
	 * @since 2024/01/28
	 */
	private void __refresh(ActionEvent __event)
	{
		Utils.swingInvoke(() -> {
			this.update();
		});
	}
	
	/**
	 * Line single step into.
	 *
	 * @param __event Ignored.
	 * @since 2024/01/27
	 */
	private void __singleStepLineInto(ActionEvent __event)
	{
		InfoThread thread = this.context.getThread();
		if (thread != null)
			this.state.threadStep(thread, 1,
				JDWPStepDepth.INTO, JDWPStepSize.LINE,
				this::handleSingleStep);
		
		// Update everything
		this.update();
	}
	
	/**
	 * Line single step out.
	 *
	 * @param __event Ignored.
	 * @since 2024/01/27
	 */
	private void __singleStepLineOut(ActionEvent __event)
	{
		InfoThread thread = this.context.getThread();
		if (thread != null)
			this.state.threadStep(thread, 1,
				JDWPStepDepth.OUT, JDWPStepSize.LINE,
				this::handleSingleStep);
		
		// Update everything
		this.update();
	}
	
	/**
	 * Line single step over.
	 *
	 * @param __event Ignored.
	 * @since 2024/01/27
	 */
	private void __singleStepLineOver(ActionEvent __event)
	{
		InfoThread thread = this.context.getThread();
		if (thread != null)
			this.state.threadStep(thread, 1,
				JDWPStepDepth.OVER, JDWPStepSize.LINE,
				this::handleSingleStep);
		
		// Update everything
		this.update();
	}
	
	/**
	 * Minimal single step into.
	 *
	 * @param __event Ignored.
	 * @since 2024/01/27
	 */
	private void __singleStepMinInto(ActionEvent __event)
	{
		InfoThread thread = this.context.getThread();
		if (thread != null)
			this.state.threadStep(thread, 1,
				JDWPStepDepth.INTO, JDWPStepSize.MIN,
				this::handleSingleStep);
		
		// Update everything
		this.update();
	}
	
	/**
	 * Minimal single step out.
	 *
	 * @param __event Ignored.
	 * @since 2024/01/27
	 */
	private void __singleStepMinOut(ActionEvent __event)
	{
		InfoThread thread = this.context.getThread();
		if (thread != null)
			this.state.threadStep(thread, 1,
				JDWPStepDepth.OUT, JDWPStepSize.MIN,
				this::handleSingleStep);
		
		// Update everything
		this.update();
	}
	
	/**
	 * Minimal single step over.
	 *
	 * @param __event Ignored.
	 * @since 2024/01/27
	 */
	private void __singleStepMinOver(ActionEvent __event)
	{
		InfoThread thread = this.context.getThread();
		if (thread != null)
			this.state.threadStep(thread, 1,
				JDWPStepDepth.OVER, JDWPStepSize.MIN,
				this::handleSingleStep);
		
		// Update everything
		this.update();
	}
	
	/**
	 * Resumes all threads.
	 *
	 * @param __event Ignored.
	 * @since 2024/01/27
	 */
	private void __threadAllResume(ActionEvent __event)
	{
		this.state.threadResumeAll(() -> {
			this.update();
		});
	}
	
	/**
	 * Suspends all threads.
	 *
	 * @param __event Ignored.
	 * @since 2024/01/27
	 */
	private void __threadAllSuspend(ActionEvent __event)
	{
		this.state.threadSuspendAll(() -> {
			this.update();
		});
	}
	
	/**
	 * Resumes a single thread.
	 *
	 * @param __event Ignored.
	 * @since 2024/01/27
	 */
	private void __threadSingleResume(ActionEvent __event)
	{
		InfoThread thread = this.context.getThread();
		if (thread != null)
			this.state.threadResume(thread, () -> {
				// Drop all frames and have no context
				thread.frames.drop();
				this.context.dropFrame(thread);
				
				// Update the UI
				this.update();
			});
	}
	
	/**
	 * Suspends a single thread.
	 *
	 * @param __event Ignored.
	 * @since 2024/01/27
	 */
	private void __threadSingleSuspend(ActionEvent __event)
	{
		InfoThread thread = this.context.getThread();
		if (thread != null)
			this.state.threadSuspend(thread, () -> {
				this.update();
			});
	}
	
	/**
	 * Views a class stored on the disk.
	 *
	 * @param __event The event.
	 * @since 2024/01/22
	 */
	private void __viewClassDisk(ActionEvent __event)
	{
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new FileNameExtensionFilter(
			"Java Class Files", "class"));
		
		if (fileChooser.showOpenDialog(this) ==
			JFileChooser.APPROVE_OPTION)
		{
			try (InputStream in = Files.newInputStream(
				fileChooser.getSelectedFile().toPath(),
				StandardOpenOption.READ))
			{
				// Decode the class
				ClassFile classFile = ClassFile.decode(in);
				
				// Use standard viewer
				this.__viewClass(new JavaClassViewer(classFile));
			}
			catch (IOException __e)
			{
				Utils.throwableTraceDialog(this,
					"Failed to load class from disk", __e);
			}
		}
	}
	
	/**
	 * Views the given class.
	 *
	 * @param __viewer The class to view.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/22
	 */
	private void __viewClass(ClassViewer __viewer)
		throws NullPointerException
	{
		if (__viewer == null)
			throw new NullPointerException("NARG");
		
		ShownClassDialog dialog = new ShownClassDialog(this,
			this.state, __viewer);
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);
	}
	
	/**
	 * Views a local class.
	 *
	 * @param __event Not used.
	 * @since 2024/01/29
	 */
	private void __viewClassLocal(ActionEvent __event)
	{
		String option = (String)JOptionPane.showInputDialog(
			this,
			"Choose local class",
			"Choose local class",
			JOptionPane.QUESTION_MESSAGE,
			null,
			null,
			"java/lang/Class");
		
		// Was a class specified?
		if (option != null)
		{
			// If there are dots, it really should be slashes
			if (option.indexOf('.') >= 0)
				option = option.replace('.', '/');
			
			// Perform lookup
			ClassName className = new ClassName(option);
			ClassFile[] classFiles = Utils.loadClass(className,
				this.preferences);
			
			// No class?
			if (classFiles == null || classFiles.length == 0)
			{
				Utils.throwableTraceDialog(this,
					"Could not find class: " + className,
					new Throwable());
				return;
			}
			
			// What do we look at?
			ClassFile lookAt;
			if (classFiles.length == 1)
				lookAt = classFiles[0];
			else
				lookAt = (ClassFile)JOptionPane.showInputDialog(
					this,
					"Select class:",
					"Multiple classes found",
					JOptionPane.QUESTION_MESSAGE,
					null,
					classFiles,
					classFiles[0]);
			
			// Look at the given class
			if (lookAt != null)
				this.__viewClass(new JavaClassViewer(lookAt));
		}
	}
	
	/**
	 * Views a class from the remote virtual machine.
	 *
	 * @param __event The event.
	 * @since 2024/01/22
	 */
	private void __viewClassNetwork(ActionEvent __event)
	{
		String option = (String)JOptionPane.showInputDialog(
			this,
			"Choose remote class",
			"Choose remote class",
			JOptionPane.QUESTION_MESSAGE,
			null,
			null,
			"java/lang/Class");
		
		// Was a class specified?
		if (option != null)
		{
			// If there are dots, it really should be slashes
			if (option.indexOf('.') >= 0)
				option = option.replace('.', '/');
			
			// Perform lookup
			ClassName className = new ClassName(option);
			this.state.lookupClass(className, (__info) -> {
				InfoClass lookAt;
				if (__info.length == 1)
					lookAt = __info[0];
				else
					lookAt = (InfoClass)JOptionPane.showInputDialog(
						this,
						"Select class:",
						"Multiple classes found",
						JOptionPane.QUESTION_MESSAGE,
						null,
						__info,
						__info[0]);
				
				// Look at the given class
				if (lookAt != null)
					this.__viewClass(new RemoteClassViewer(
						this.state, lookAt));
			}, (__e) -> {
				Utils.throwableTraceDialog(this,
					"Could not find class: " + className, __e);
			});
		}
	}
	
	/**
	 * Creates a toolbar button.
	 *
	 * @param __label The label to use.
	 * @param __tango The tango icon to use.
	 * @return The created button.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/21
	 */
	private static JButton __barButton(String __label, String __tango)
		throws NullPointerException
	{
		if (__label == null || __tango == null)
			throw new NullPointerException("NARG");
		
		JButton button = new JButton();
		
		button.setToolTipText(__label);
		button.setIcon(Utils.tangoIcon(__tango));
		
		return button;
	}
	
	/**
	 * Creates a toolbar button and adds it to the given toolbar.
	 *
	 * @param __toolBar The toolbar to add to.
	 * @param __label The label to use.
	 * @param __tango The tango icon to use.
	 * @return The created button.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/21
	 */
	private static JButton __barButton(JToolBar __toolBar, String __label,
		String __tango)
		throws NullPointerException
	{
		if (__toolBar == null)
			throw new NullPointerException("NARG");
		
		JButton button = PrimaryFrame.__barButton(__label, __tango);
		
		__toolBar.add(button);
		
		return button;
	}
	
	/**
	 * Creates a menu item.
	 *
	 * @param __label The label to use.
	 * @param __tango The tango icon to use.
	 * @return The created button.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/21
	 */
	private static JMenuItem __menuItem(String __label, String __tango)
		throws NullPointerException
	{
		if (__label == null || __tango == null)
			throw new NullPointerException("NARG");
		
		return new JMenuItem(__label,
			Utils.tangoIcon(__tango));
	}
}
