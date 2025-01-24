// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.scritchui.extra;

import cc.squirreljme.jvm.mle.brackets.PencilBracket;
import cc.squirreljme.jvm.mle.brackets.PencilFontBracket;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.jvm.mle.scritchui.ScritchChoiceInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchComponentInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchContainerInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchEnvironmentInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchEventLoopInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchLAFInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchLabelInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchListInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchMenuInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchPaintableInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchPanelInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchScreenInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchScrollPanelInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchUnifiedInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchViewInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchWindowInterface;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchBaseBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchChoiceBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchComponentBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchContainerBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchLabelBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchListBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchMenuBarBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchMenuBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchMenuHasChildrenBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchMenuHasParentBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchMenuItemBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchPaintableBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchPanelBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchScreenBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchScrollPanelBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchViewBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchWindowBracket;
import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchActivateListener;
import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchCloseListener;
import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchInputListener;
import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchMenuItemActivateListener;
import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchPaintListener;
import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchSizeListener;
import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchSizeSuggestListener;
import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchValueUpdateListener;
import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchViewListener;
import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchVisibleListener;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import org.jetbrains.annotations.Nullable;

/**
 * Implements a wrapper for all ScritchUI interfaces from a base interface.
 *
 * @since 2024/08/02
 */
@SquirrelJMEVendorApi
public class ScritchUnifiedWrapper
	implements ScritchUnifiedInterface
{
	/** The interface to wrap. */
	@SquirrelJMEVendorApi
	protected final ScritchInterface api;
	
	/**
	 * Initializes the unified wrapper.
	 *
	 * @param __api The API to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/08/02
	 */
	@SquirrelJMEVendorApi
	public ScritchUnifiedWrapper(ScritchInterface __api)
		throws NullPointerException
	{
		if (__api == null)
			throw new NullPointerException("NARG");
		
		this.api = __api;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public void containerAdd(ScritchContainerBracket __container,
		ScritchComponentBracket __component)
		throws MLECallError
	{
		this.api.container().containerAdd(__container, __component);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public PencilFontBracket[] builtinFonts()
	{
		return this.api.environment().builtinFonts();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public void windowCallAttention(ScritchWindowBracket __window)
		throws MLECallError
	{
		this.api.window().windowCallAttention(__window);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public ScritchChoiceInterface choice()
	{
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public ScritchComponentInterface component()
	{
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public ScritchContainerInterface container()
	{
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public int windowContentHeight(
		ScritchWindowBracket __window)
		throws MLECallError
	{
		return this.api.window().windowContentHeight(__window);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public void windowContentMinimumSize(ScritchWindowBracket __window,
		int __w,
		int __h)
		throws MLECallError
	{
		this.api.window().windowContentMinimumSize(__window, __w, __h);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public int windowContentWidth(
		ScritchWindowBracket __window)
		throws MLECallError
	{
		return this.api.window().windowContentWidth(__window);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public void choiceDelete(ScritchChoiceBracket __choice,
		int __atIndex)
		throws MLECallError
	{
		this.api.choice().choiceDelete(__choice, __atIndex);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public void choiceDeleteAll(ScritchChoiceBracket __choice)
		throws MLECallError
	{
		this.api.choice().choiceDeleteAll(__choice);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public int screenDpi(
		ScritchScreenBracket __screen)
		throws MLECallError
	{
		return this.api.screen().screenDpi(__screen);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public int lafElementColor(@Nullable ScritchComponentBracket __context,
		int __element)
		throws MLECallError
	{
		return this.api.environment().lookAndFeel()
			.lafElementColor(__context, __element);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public void panelEnableFocus(ScritchPanelBracket __panel, boolean __enabled,
		boolean __default)
		throws MLECallError
	{
		this.api.panel().panelEnableFocus(__panel, __enabled, __default);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public ScritchEnvironmentInterface environment()
	{
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public ScritchEventLoopInterface eventLoop()
	{
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public void loopExecute(Runnable __task)
		throws MLECallError
	{
		this.api.eventLoop().loopExecute(__task);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public void loopExecuteLater(Runnable __task)
		throws MLECallError
	{
		this.api.eventLoop().loopExecuteLater(__task);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public void loopExecuteWait(Runnable __task)
		throws MLECallError
	{
		this.api.eventLoop().loopExecuteWait(__task);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public int lafFocusBorderStyle(boolean __focused)
	{
		return this.api.environment().lookAndFeel()
			.lafFocusBorderStyle(__focused);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public @Nullable PencilFontBracket lafFont(int __element)
		throws MLECallError
	{
		return this.api.environment().lookAndFeel().lafFont(__element);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public PencilFontBracket fontDerive(
		PencilFontBracket __font, int __style,
		int __pixelSize)
		throws MLECallError
	{
		return this.api.environment().fontDerive(__font, __style, __pixelSize);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public @Nullable ScritchComponentBracket componentGetParent(
		ScritchComponentBracket __component)
		throws MLECallError
	{
		return this.api.component().componentGetParent(__component);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public int choiceGetSelectedIndex(
		ScritchChoiceBracket __choice)
		throws MLECallError
	{
		return this.api.choice().choiceGetSelectedIndex(__choice);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public void viewGetView(ScritchViewBracket __view,
		int[] __outRect)
		throws MLECallError
	{
		this.api.view().viewGetView(__view, __outRect);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public PencilBracket hardwareGraphics(int __pf,
		int __bw,
		int __bh,
		Object __buf, @Nullable int[] __pal, int __sx, int __sy,
		int __sw,
		int __sh)
		throws MLECallError
	{
		return this.api.hardwareGraphics(__pf, __bw, __bh, __buf, __pal,
			__sx, __sy, __sw, __sh);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public boolean windowHasFocus(ScritchWindowBracket __window)
		throws MLECallError
	{
		return this.api.window().windowHasFocus(__window);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public int screenHeight(
		ScritchScreenBracket __screen)
		throws MLECallError
	{
		return this.api.screen().screenHeight(__screen);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public int componentGetHeight(
		ScritchComponentBracket __component)
		throws MLECallError
	{
		return this.api.component().componentGetHeight(__component);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public int screenId(
		ScritchScreenBracket __screen)
		throws MLECallError
	{
		return this.api.screen().screenId(__screen);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public int lafImageSize(int __elem,
		boolean __height)
		throws MLECallError
	{
		return this.api.environment().lookAndFeel()
			.lafImageSize(__elem, __height);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public boolean inLoop()
	{
		return this.api.eventLoop().inLoop();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public int windowInputTypes(ScritchWindowBracket __window)
		throws MLECallError
	{
		return this.api.window().windowInputTypes(__window);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public int choiceInsert(
		ScritchChoiceBracket __choice,
		int __atIndex)
		throws MLECallError
	{
		return this.api.choice().choiceInsert(__choice, __atIndex);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public boolean screenIsBuiltIn(ScritchScreenBracket __screen)
		throws MLECallError
	{
		return this.api.screen().screenIsBuiltIn(__screen);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public boolean lafIsDarkMode()
	{
		return this.api.environment().lookAndFeel().lafIsDarkMode();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public boolean isInhibitingSleep()
	{
		return this.api.environment().isInhibitingSleep();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public boolean isPanelOnly()
	{
		return this.api.environment().isPanelOnly();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public boolean screenIsPortrait(ScritchScreenBracket __screen)
		throws MLECallError
	{
		return this.api.screen().screenIsPortrait(__screen);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public boolean windowIsVisible(ScritchWindowBracket __window)
		throws MLECallError
	{
		return this.api.window().windowIsVisible(__window);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public ScritchLabelInterface label()
	{
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public int choiceLength(
		@Nullable ScritchChoiceBracket __choice)
		throws MLECallError
	{
		return this.api.choice().choiceLength(__choice);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public ScritchListInterface list()
	{
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public ScritchListBracket listNew(int __type)
		throws MLECallError
	{
		return this.api.list().listNew(__type);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public ScritchLAFInterface lookAndFeel()
	{
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public ScritchMenuInterface menu()
	{
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public ScritchMenuBarBracket menuBarNew()
		throws MLECallError
	{
		return this.api.menu().menuBarNew();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public void menuInsert(ScritchMenuHasChildrenBracket __into,
		int __at,
		ScritchMenuHasParentBracket __item)
		throws MLECallError
	{
		this.api.menu().menuInsert(__into, __at, __item);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public ScritchMenuItemBracket menuItemNew()
		throws MLECallError
	{
		return this.api.menu().menuItemNew();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public void menuItemSetKey(ScritchMenuItemBracket __item,
		int __key, int __modifier)
		throws MLECallError
	{
		this.api.menu().menuItemSetKey(__item, __key, __modifier);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public ScritchMenuBracket menuNew()
		throws MLECallError
	{
		return this.api.menu().menuNew();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public ScritchPanelBracket panelNew()
		throws MLECallError
	{
		return this.api.panel().panelNew();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public ScritchWindowBracket windowNew()
	{
		return this.api.window().windowNew();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public void objectDelete(ScritchBaseBracket __object)
		throws MLECallError
	{
		this.api.objectDelete(__object);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public ScritchPaintableInterface paintable()
	{
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public ScritchPanelInterface panel()
	{
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public void menuRemoveAll(ScritchMenuHasChildrenBracket __menuKind)
		throws MLECallError
	{
		this.api.menu().menuRemoveAll(__menuKind);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public void containerRemoveAll(ScritchContainerBracket __container)
		throws MLECallError
	{
		this.api.container().containerRemoveAll(__container);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public void componentRepaint(ScritchComponentBracket __component)
		throws MLECallError
	{
		this.api.paintable().componentRepaint(__component);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public void componentRevalidate(ScritchComponentBracket __component)
		throws MLECallError
	{
		this.api.component().componentRevalidate(__component);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public ScritchScreenInterface screen()
	{
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public ScritchScreenBracket[] screens()
	{
		return this.api.environment().screens();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public ScritchScrollPanelInterface scrollPanel()
	{
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public ScritchScrollPanelBracket scrollPanelNew()
		throws MLECallError
	{
		return this.api.scrollPanel().scrollPanelNew();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public void componentSetActivateListener(ScritchComponentBracket __component,
		ScritchActivateListener __listener)
		throws MLECallError
	{
		this.api.component().componentSetActivateListener(__component,
			__listener);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public void viewSetArea(ScritchViewBracket __view,
		int __width,
		int __height)
		throws MLECallError
	{
		this.api.view().viewSetArea(__view, __width, __height);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public void containerSetBounds(ScritchContainerBracket __container,
		ScritchComponentBracket __component, int __x, int __y,
		int __w,
		int __h)
		throws MLECallError
	{
		this.api.container().containerSetBounds(__container, __component,
			__x, __y, __w, __h);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public void windowSetCloseListener(ScritchWindowBracket __window,
		@Nullable ScritchCloseListener __listener)
		throws MLECallError
	{
		this.api.window().windowSetCloseListener(__window, __listener);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public void choiceSetEnabled(ScritchChoiceBracket __choice,
		int __atIndex,
		boolean __enabled)
		throws MLECallError
	{
		this.api.choice().choiceSetEnabled(__choice, __atIndex, __enabled);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public void choiceSetImage(ScritchChoiceBracket __choice,
		int __atIndex,
		@Nullable int[] __data,
		int __off,
		int __scanLen,
		int __width,
		int __height)
		throws MLECallError
	{
		this.api.choice().choiceSetImage(__choice, __atIndex, __data, __off,
			__scanLen, __width, __height);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public void setInhibitSleep(boolean __inhibit)
	{
		this.api.environment().setInhibitSleep(__inhibit);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public void panelSetInputListener(ScritchPanelBracket __panel,
		@Nullable ScritchInputListener __listener)
		throws MLECallError
	{
		this.api.panel().panelSetInputListener(__panel, __listener);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public void windowSetMenuBar(ScritchWindowBracket __window,
		@Nullable ScritchMenuBarBracket __menuBar)
		throws MLECallError
	{
		this.api.window().windowSetMenuBar(__window, __menuBar);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public void windowSetMenuItemActivateListener(
		ScritchWindowBracket __window,
		@Nullable ScritchMenuItemActivateListener __listener)
		throws MLECallError
	{
		this.api.window().windowSetMenuItemActivateListener(__window, __listener);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public void componentSetPaintListener(ScritchPaintableBracket __component,
		@Nullable ScritchPaintListener __listener)
		throws MLECallError
	{
		this.api.paintable().componentSetPaintListener(__component, __listener);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public void choiceSetSelected(ScritchChoiceBracket __choice,
		int __atIndex,
		boolean __selected)
		throws MLECallError
	{
		this.api.choice().choiceSetSelected(__choice, __atIndex, __selected);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public void componentSetSizeListener(ScritchComponentBracket __component,
		ScritchSizeListener __listener)
		throws MLECallError
	{
		this.api.component().componentSetSizeListener(__component, __listener);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public void viewSetSizeSuggestListener(ScritchViewBracket __view,
		@Nullable ScritchSizeSuggestListener __listener)
		throws MLECallError
	{
		this.api.view().viewSetSizeSuggestListener(__view, __listener);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public void labelSetString(ScritchLabelBracket __label,
		@Nullable String __string)
		throws MLECallError
	{
		this.api.label().labelSetString(__label, __string);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public void choiceSetString(ScritchChoiceBracket __choice,
		int __atIndex,
		@Nullable String __string)
		throws MLECallError
	{
		this.api.choice().choiceSetString(__choice, __atIndex, __string);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public void componentSetValueUpdateListener(ScritchComponentBracket __component,
		ScritchValueUpdateListener __listener)
		throws MLECallError
	{
		this.api.component().componentSetValueUpdateListener(__component, __listener);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public void viewSetView(ScritchViewBracket __view,
		int __x,
		int __y,
		int __width,
		int __height)
		throws MLECallError
	{
		this.api.view().viewSetView(__view, __x, __y, __width, __height);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public void viewSetViewListener(ScritchViewBracket __view,
		@Nullable ScritchViewListener __listener)
		throws MLECallError
	{
		this.api.view().viewSetViewListener(__view, __listener);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public void windowSetVisible(ScritchWindowBracket __window,
		boolean __visible)
		throws MLECallError
	{
		this.api.window().windowSetVisible(__window, __visible);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public void componentSetVisibleListener(ScritchComponentBracket __component,
		ScritchVisibleListener __listener)
		throws MLECallError
	{
		this.api.component().componentSetVisibleListener(__component, __listener);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public ScritchViewInterface view()
	{
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public int screenWidth(
		ScritchScreenBracket __screen)
		throws MLECallError
	{
		return this.api.screen().screenWidth(__screen);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public int componentWidth(
		ScritchComponentBracket __component)
		throws MLECallError
	{
		return this.api.component().componentWidth(__component);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public ScritchWindowInterface window()
	{
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	@SquirrelJMEVendorApi
	public int windowManagerType()
	{
		return this.api.environment().windowManagerType();
	}
}
