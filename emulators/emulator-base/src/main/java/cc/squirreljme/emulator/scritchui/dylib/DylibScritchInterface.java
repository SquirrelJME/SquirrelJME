// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.scritchui.dylib;

import cc.squirreljme.emulator.NativeBinding;
import cc.squirreljme.jvm.mle.brackets.PencilBracket;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.jvm.mle.scritchui.ScritchChoiceInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchComponentInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchContainerInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchEnvironmentInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchEventLoopInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchLabelInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchListInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchMenuInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchPaintableInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchPanelInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchScreenInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchScrollPanelInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchViewInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchWindowInterface;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchBaseBracket;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.util.StreamUtils;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import net.multiphasicapps.collections.UnmodifiableList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

/**
 * Dynamic library based ScritchUI interface.
 *
 * @since 2024/03/29
 */
public class DylibScritchInterface
	implements ScritchInterface
{
	/**
	 * {@squirreljme.property cc.squirreljme.scritchui=ui Specifies
	 * a different UI library to use instead of the default.}
	 */
	public static final String PREFER_PROPERTY =
		"cc.squirreljme.scritchui";
	
	/** The preferred ScritchUI interface. */
	public static final String PREFER_ENV =
		"SQUIRRELJME_SCRITCHUI_INTERFACE";
	
	/** Libraries may be under this environment. */
	public static final String LIB_JVM_ENV =
		"SQUIRRELJME_LIB_JVM";
	
	/** The internal default order of interfaces. */
	private static final List<String> _ORDER =
		UnmodifiableList.of(Arrays.asList("cocoa", "win32", "gtk2"));
	
	/** The default instance of this. */
	private static volatile DylibScritchInterface _INSTANCE;
	
	/** The native dynamic library to use. */
	protected final NativeScritchDylib dyLib;
	
	/** The choice implementation. */
	protected final DylibChoiceInterface choice;
	
	/** Component interface. */
	protected final DylibComponentInterface component;
	
	/** Container interface. */
	protected final DylibContainerInterface container;
	
	/** Environment interface. */
	protected final DylibEnvironmentInterface environment;
	
	/** Event loop interface. */
	protected final DylibEventLoopInterface eventLoop;
	
	/** The label interface. */
	protected final DylibLabelInterface label;
	
	/** List interface. */
	protected final DylibListInterface list;
	
	/** Menu interface. */
	protected final DylibMenuInterface menu;
	
	/** Paintable interface. */
	protected final DylibPaintableInterface paintable;
	
	/** Panel interface. */
	protected final DylibPanelInterface panel;
	
	/** Screen interface. */
	protected final DylibScreenInterface screen;
	
	/** Window interface. */
	protected final DylibWindowInterface window;
	
	/** The scroll panel interface. */
	protected final ScritchScrollPanelInterface scrollPanel;
	
	/** The view interface. */
	protected final ScritchViewInterface view;
	
	/**
	 * Initializes the native dynamic library interface.
	 *
	 * @param __dyLib The dynamic library interface to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/03/29
	 */
	public DylibScritchInterface(NativeScritchDylib __dyLib)
		throws NullPointerException
	{
		if (__dyLib == null)
			throw new NullPointerException("NARG");
		
		// Store for later
		this.dyLib = __dyLib;
		
		// Initialize all sub-interfaces
		Reference<DylibScritchInterface> self =
			new WeakReference<>(this);
		this.choice = new DylibChoiceInterface(self, __dyLib);
		this.component = new DylibComponentInterface(self, __dyLib);
		this.container = new DylibContainerInterface(self, __dyLib);
		this.environment = new DylibEnvironmentInterface(self, __dyLib);
		this.eventLoop = new DylibEventLoopInterface(self, __dyLib);
		this.label = new DylibLabelInterface(self, __dyLib);
		this.list = new DylibListInterface(self, __dyLib);
		this.menu = new DylibMenuInterface(self, __dyLib);
		this.paintable = new DylibPaintableInterface(self, __dyLib);
		this.panel = new DylibPanelInterface(self, __dyLib);
		this.screen = new DylibScreenInterface(self, __dyLib);
		this.scrollPanel = new DylibScrollPanelInterface(self, __dyLib);
		this.view = new DylibViewInterface(self, __dyLib);
		this.window = new DylibWindowInterface(self, __dyLib);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/07/16
	 */
	@Override
	public ScritchChoiceInterface choice()
	{
		return this.choice;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/29
	 */
	@Override
	public ScritchComponentInterface component()
	{
		return this.component;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/29
	 */
	@Override
	public ScritchContainerInterface container()
	{
		return this.container;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/07/20
	 */
	@Override
	public void objectDelete(ScritchBaseBracket __object)
		throws MLECallError
	{
		if (__object == null)
			throw new MLECallError("Null arguments");
		
		NativeScritchDylib.__objectDelete(this.dyLib._stateP,
			((DylibBaseObject)__object).objectPointer());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/29
	 */
	@Override
	public ScritchEnvironmentInterface environment()
	{
		return this.environment;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/29
	 */
	@Override
	public ScritchEventLoopInterface eventLoop()
	{
		return this.eventLoop;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/07/10
	 */
	@Override
	public PencilBracket hardwareGraphics(int __pf,
		@Range(from = 0, to = Integer.MAX_VALUE) int __bw,
		@Range(from = 0, to = Integer.MAX_VALUE) int __bh,
		@NotNull Object __buf, @Nullable int[] __pal, int __sx, int __sy,
		@Range(from = 0, to = Integer.MAX_VALUE) int __sw,
		@Range(from = 0, to = Integer.MAX_VALUE) int __sh)
		throws MLECallError
	{
		PencilBracket result = NativeScritchDylib.__hardwareGraphics(
			this.dyLib._stateP, __pf, __bw, __bh, __buf, __pal, __sx, __sy,
			__sw, __sh);
		if (result == null)
			throw new MLECallError("Did not make a pencil?");
		
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/07/22
	 */
	@Override
	public ScritchLabelInterface label()
	{
		return this.label;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/07/16
	 */
	@Override
	public ScritchListInterface list()
	{
		return this.list;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/07/20
	 */
	@Override
	public @NotNull ScritchMenuInterface menu()
	{
		return this.menu;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/07/16
	 */
	@Override
	public @NotNull ScritchPaintableInterface paintable()
	{
		return this.paintable;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/29
	 */
	@Override
	public ScritchPanelInterface panel()
	{
		return this.panel;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/29
	 */
	@Override
	public ScritchScreenInterface screen()
	{
		return this.screen;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/07/29
	 */
	@Override
	public ScritchScrollPanelInterface scrollPanel()
	{
		return this.scrollPanel;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/07/29
	 */
	@Override
	public ScritchViewInterface view()
	{
		return this.view;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/29
	 */
	@Override
	public ScritchWindowInterface window()
	{
		return this.window;
	}
	
	/**
	 * Returns the native ScritchUI interface.
	 *
	 * @return The default instance interface.
	 * @throws MLECallError If this is not supported.
	 * @since 2024/03/30
	 */
	public static DylibScritchInterface instance()
		throws MLECallError
	{
		DylibScritchInterface instance = DylibScritchInterface._INSTANCE;
		if (instance != null)
			return instance;
		
		// Get list of available interfaces
		List<String> potentials = new ArrayList<>();
		try (InputStream in = DylibScritchInterface.class
			.getResourceAsStream(
				NativeBinding.nativePrefix() +
					"/ScritchUi.list"))
		{
			if (in == null)
				throw new MLECallError(
					"No native ScritchUI libraries.");
			
			// Which interfaces are available?
			for (String str : StreamUtils.readAllLines(in, "utf-8"))
				if (str != null && !str.isEmpty())
					potentials.add(str.toLowerCase());
		}
		catch (IOException __e)
		{
			throw new MLECallError(__e);
		}
		
		// Preferred library
		String prefer = System.getenv(DylibScritchInterface.PREFER_ENV); 
		if (prefer == null || prefer.isEmpty())
			prefer = System.getProperty(
			DylibScritchInterface.PREFER_PROPERTY);
		
		// Deferred exceptions for later failing
		List<Throwable> defer = new ArrayList<>();
		
		// Is the SquirrelJME native path specified?
		String libJvmEnvRaw = System.getenv(DylibScritchInterface.LIB_JVM_ENV);
		Path libJvmEnv = (libJvmEnvRaw == null ? null :
			Paths.get(libJvmEnvRaw));
		
		// Never allow non-absolute paths
		if (libJvmEnv != null && !libJvmEnv.isAbsolute())
			libJvmEnv = null;
		
		// Determine the order to check, make sure the preference is always
		// added first
		Set<String> checkOrder = new LinkedHashSet<>();
		if (prefer != null && !prefer.isEmpty())
			checkOrder.add(prefer);
		checkOrder.addAll(potentials);
		
		// Go through and find all available libraries
		// Note that if a preference is selected then assume it may always
		// exist... if libJvm is specified then anything could be used, even
		// ones unknown to ScritchUI
		List<Path> libPaths = new ArrayList<>();
		List<String> libNames = new ArrayList<>();
		for (String order : checkOrder)
		{
			// What library is used?
			String libName = System.mapLibraryName(
				"squirreljme-scritchui-" + order);
			
			// Use from environment if available
			if (libJvmEnv != null)
			{
				libPaths.add(libJvmEnv.resolve(libName));
				libNames.add(order);
			}
			
			// Find library to load
			Path libPath;
			try
			{
				libPath = NativeBinding.libFromResources(
					libName, false);
			}
			
			// If it fails to extract, skip it
			catch (LinkageError __e)
			{
				defer.add(__e);
				continue;
			}
			
			// This library path is valid, so store it for later
			libPaths.add(libPath);
			libNames.add(order);
		}
		
		// Reorder to preferred libraries are first
		if (prefer != null)
			for (int i = 0, prefSlot = 0; i < libNames.size(); i++)
				if (prefer.equals(libNames.get(i)))
				{
					// Swap with the preference, if not already in the first
					// slot
					if (i != prefSlot)
					{
						Collections.swap(libPaths, prefSlot, i);
						Collections.swap(libPaths, prefSlot, i);
					}
					
					// Preference slot moves up, as they can be specified
					// multiple times!
					prefSlot++;
				}
		
		// Debug
		Debugging.debugNote("ScritchUI Order: [%s, %s]",
			libPaths, libNames);
		
		// Use the first one that successfully loads!
		for (int i = 0; i < libPaths.size(); i++)
		{
			// Determine the library to load
			Path libPath = libPaths.get(i);
			String libName = libNames.get(i);
			
			// Debug
			Debugging.debugNote("ScritchUI Load: %s from %s",
				libName, libPath);
			
			try
			{
				// Load instance
				instance = new DylibScritchInterface(
					new NativeScritchDylib(libPath.toAbsolutePath(), libName));
				
				// Cache and use it
				DylibScritchInterface._INSTANCE = instance;
				return instance;
			}
			catch (RuntimeException|LinkageError|MLECallError __e)
			{
				defer.add(__e);
			}
		}
		
		// Not found
		MLECallError fail = new MLECallError(
			"Did not find a ScritchUI library to select.");
		for (Throwable e : defer)
			fail.addSuppressed(e);
		throw fail;
	}
}
