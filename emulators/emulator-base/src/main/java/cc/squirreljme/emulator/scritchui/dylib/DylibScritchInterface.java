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
import cc.squirreljme.jvm.mle.scritchui.ScritchComponentInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchContainerInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchEnvironmentInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchEventLoopInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchPanelInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchScreenInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchWindowInterface;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.util.StreamUtils;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
	/** The internal default order of interfaces. */
	private static final List<String> _ORDER =
		UnmodifiableList.of(Arrays.asList("win32", "gtk2", "motif", "gtk3",
			"x11"));
	
	/** The default instance of this. */
	private static volatile DylibScritchInterface _INSTANCE;
	
	/** The native dynamic library to use. */
	protected final NativeScritchDylib dyLib;
	
	/** Component interface. */
	protected final DylibComponentInterface component;
	
	/** Container interface. */
	protected final DylibContainerInterface container;
	
	/** Environment interface. */
	protected final DylibEnvironmentInterface environment;
	
	/** Event loop interface. */
	protected final DylibEventLoopInterface eventLoop;
	
	/** Panel interface. */
	protected final DylibPanelInterface panel;
	
	/** Screen interface. */
	protected final DylibScreenInterface screen;
	
	/** Window interface. */
	protected final DylibWindowInterface window;
	
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
		this.component = new DylibComponentInterface(self, __dyLib);
		this.container = new DylibContainerInterface(self, __dyLib);
		this.environment = new DylibEnvironmentInterface(self, __dyLib);
		this.eventLoop = new DylibEventLoopInterface(self, __dyLib);
		this.panel = new DylibPanelInterface(self, __dyLib);
		this.screen = new DylibScreenInterface(self, __dyLib);
		this.window = new DylibWindowInterface(self, __dyLib);
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
		return this.dyLib.hardwareGraphics(__pf, __bw, __bh, __buf, __pal,
			__sx, __sy, __sw, __sh);
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
	 * @throws UnsupportedOperationException If this is not supported.
	 * @since 2024/03/30
	 */
	public static DylibScritchInterface instance()
		throws UnsupportedOperationException
	{
		DylibScritchInterface instance = DylibScritchInterface._INSTANCE;
		if (instance != null)
			return instance;
		
		// Get list of available interfaces
		List<String> potentials = new ArrayList<>();
		try (InputStream in = DylibScritchInterface.class
			.getResourceAsStream("/libsquirreljme-scritchui.list"))
		{
			if (in == null)
				throw new UnsupportedOperationException(
					"No native ScritchUI libraries.");
			
			// Which interfaces are available?
			for (String str : StreamUtils.readAllLines(in, "utf-8"))
				if (str != null && !str.isEmpty())
					potentials.add(str.toLowerCase());
		}
		catch (IOException __e)
		{
			throw new RuntimeException(__e);
		}
		
		// Go through and find the best match
		for (String order : DylibScritchInterface._ORDER)
			if (potentials.contains(order))
				try
				{
					// What library is used?
					String libName = System.mapLibraryName(
						"squirreljme-scritchui-" + order);
					
					// Find library to load
					Path libPath = NativeBinding.libFromResources(
						libName, false);
					
					// Load instance
					instance = new DylibScritchInterface(
						new NativeScritchDylib(libPath.toAbsolutePath(),
							order));
					
					// Cache and use it
					DylibScritchInterface._INSTANCE = instance;
					return instance;
				}
				catch (IOException __e)
				{
					throw new RuntimeException(__e);
				}
		
		// Not found
		throw new UnsupportedOperationException(
			"Did not find a ScritchUI library to select.");
	}
}
