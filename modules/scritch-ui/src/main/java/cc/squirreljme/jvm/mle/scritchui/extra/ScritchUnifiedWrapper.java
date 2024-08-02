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
	protected final ScritchInterface api;
	
	/**
	 * Initializes the unified wrapper.
	 *
	 * @param __api The API to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/08/02
	 */
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
	public void add(ScritchContainerBracket __container,
		ScritchComponentBracket __component)
		throws MLECallError
	{
		this.api.container().add(__container, __component);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public PencilFontBracket[] builtinFonts()
	{
		return this.api.environment().builtinFonts();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public void callAttention(ScritchWindowBracket __window)
		throws MLECallError
	{
		this.api.window().callAttention(__window);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public ScritchChoiceInterface choice()
	{
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public ScritchComponentInterface component()
	{
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public ScritchContainerInterface container()
	{
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public int contentHeight(
		ScritchWindowBracket __window)
		throws MLECallError
	{
		return this.api.window().contentHeight(__window);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public void contentMinimumSize(ScritchWindowBracket __window,
		int __w,
		int __h)
		throws MLECallError
	{
		this.api.window().contentMinimumSize(__window, __w, __h);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public int contentWidth(
		ScritchWindowBracket __window)
		throws MLECallError
	{
		return this.api.window().contentWidth(__window);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public void delete(ScritchChoiceBracket __choice,
		int __atIndex)
		throws MLECallError
	{
		this.api.choice().delete(__choice, __atIndex);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public void deleteAll(ScritchChoiceBracket __choice)
		throws MLECallError
	{
		this.api.choice().deleteAll(__choice);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public int dpi(
		ScritchScreenBracket __screen)
		throws MLECallError
	{
		return this.api.screen().dpi(__screen);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public int elementColor(@Nullable ScritchComponentBracket __context,
		int __element)
		throws MLECallError
	{
		return this.api.environment().lookAndFeel()
			.elementColor(__context, __element);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public void enableFocus(ScritchPanelBracket __panel, boolean __enabled,
		boolean __default)
		throws MLECallError
	{
		this.api.panel().enableFocus(__panel, __enabled, __default);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public ScritchEnvironmentInterface environment()
	{
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public ScritchEventLoopInterface eventLoop()
	{
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public void execute(Runnable __task)
		throws MLECallError
	{
		this.api.eventLoop().execute(__task);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public void executeLater(Runnable __task)
		throws MLECallError
	{
		this.api.eventLoop().executeLater(__task);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public void executeWait(Runnable __task)
		throws MLECallError
	{
		this.api.eventLoop().executeWait(__task);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public int focusBorderStyle(boolean __focused)
	{
		return this.api.environment().lookAndFeel()
			.focusBorderStyle(__focused);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public @Nullable PencilFontBracket font(int __element)
		throws MLECallError
	{
		return this.api.environment().lookAndFeel().font(__element);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
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
	public @Nullable ScritchComponentBracket getParent(
		ScritchComponentBracket __component)
		throws MLECallError
	{
		return this.api.component().getParent(__component);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public int getSelectedIndex(
		ScritchChoiceBracket __choice)
		throws MLECallError
	{
		return this.api.choice().getSelectedIndex(__choice);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public void getView(ScritchViewBracket __view,
		int[] __outRect)
		throws MLECallError
	{
		this.api.view().getView(__view, __outRect);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
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
	public boolean hasFocus(ScritchWindowBracket __window)
		throws MLECallError
	{
		return this.api.window().hasFocus(__window);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public int height(
		ScritchScreenBracket __screen)
		throws MLECallError
	{
		return this.api.screen().height(__screen);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public int height(
		ScritchComponentBracket __component)
		throws MLECallError
	{
		return this.api.component().height(__component);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public int id(
		ScritchScreenBracket __screen)
		throws MLECallError
	{
		return this.api.screen().id(__screen);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public int imageSize(int __elem,
		boolean __height)
		throws MLECallError
	{
		return this.api.environment().lookAndFeel()
			.imageSize(__elem, __height);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public boolean inLoop()
	{
		return this.api.eventLoop().inLoop();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public int inputTypes(ScritchWindowBracket __window)
		throws MLECallError
	{
		return this.api.window().inputTypes(__window);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public int insert(
		ScritchChoiceBracket __choice,
		int __atIndex)
		throws MLECallError
	{
		return this.api.choice().insert(__choice, __atIndex);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public boolean isBuiltIn(ScritchScreenBracket __screen)
		throws MLECallError
	{
		return this.api.screen().isBuiltIn(__screen);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public boolean isDarkMode()
	{
		return this.api.environment().lookAndFeel().isDarkMode();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public boolean isInhibitingSleep()
	{
		return this.api.environment().isInhibitingSleep();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public boolean isPanelOnly()
	{
		return this.api.environment().isPanelOnly();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public boolean isPortrait(ScritchScreenBracket __screen)
		throws MLECallError
	{
		return this.api.screen().isPortrait(__screen);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public boolean isVisible(ScritchWindowBracket __window)
		throws MLECallError
	{
		return this.api.window().isVisible(__window);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public ScritchLabelInterface label()
	{
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public int length(
		@Nullable ScritchChoiceBracket __choice)
		throws MLECallError
	{
		return this.api.choice().length(__choice);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public ScritchListInterface list()
	{
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
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
	public ScritchLAFInterface lookAndFeel()
	{
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public ScritchMenuInterface menu()
	{
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
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
	public ScritchPanelBracket newPanel()
		throws MLECallError
	{
		return this.api.panel().newPanel();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public ScritchWindowBracket newWindow()
	{
		return this.api.window().newWindow();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
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
	public ScritchPaintableInterface paintable()
	{
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public ScritchPanelInterface panel()
	{
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public void removeAll(ScritchMenuHasChildrenBracket __menuKind)
		throws MLECallError
	{
		this.api.menu().removeAll(__menuKind);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public void removeAll(ScritchContainerBracket __container)
		throws MLECallError
	{
		this.api.container().removeAll(__container);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public void repaint(ScritchComponentBracket __component)
		throws MLECallError
	{
		this.api.paintable().repaint(__component);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public void revalidate(ScritchComponentBracket __component)
		throws MLECallError
	{
		this.api.component().revalidate(__component);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public ScritchScreenInterface screen()
	{
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public ScritchScreenBracket[] screens()
	{
		return this.api.environment().screens();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public ScritchScrollPanelInterface scrollPanel()
	{
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
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
	public void setActivateListener(ScritchComponentBracket __component,
		ScritchActivateListener __listener)
		throws MLECallError
	{
		this.api.component().setActivateListener(__component, __listener);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public void setArea(ScritchViewBracket __view,
		int __width,
		int __height)
		throws MLECallError
	{
		this.api.view().setArea(__view, __width, __height);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public void setBounds(ScritchContainerBracket __container,
		ScritchComponentBracket __component, int __x, int __y,
		int __w,
		int __h)
		throws MLECallError
	{
		this.api.container().setBounds(__container, __component,
			__x, __y, __w, __h);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public void setCloseListener(ScritchWindowBracket __window,
		@Nullable ScritchCloseListener __listener)
		throws MLECallError
	{
		this.api.window().setCloseListener(__window, __listener);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public void setEnabled(ScritchChoiceBracket __choice,
		int __atIndex,
		boolean __enabled)
		throws MLECallError
	{
		this.api.choice().setEnabled(__choice, __atIndex, __enabled);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public void setImage(ScritchChoiceBracket __choice,
		int __atIndex,
		@Nullable int[] __data,
		int __off,
		int __scanLen,
		int __width,
		int __height)
		throws MLECallError
	{
		this.api.choice().setImage(__choice, __atIndex, __data, __off,
			__scanLen, __width, __height);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public void setInhibitSleep(boolean __inhibit)
	{
		this.api.environment().setInhibitSleep(__inhibit);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public void setInputListener(ScritchPanelBracket __panel,
		@Nullable ScritchInputListener __listener)
		throws MLECallError
	{
		this.api.panel().setInputListener(__panel, __listener);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public void setMenuBar(ScritchWindowBracket __window,
		@Nullable ScritchMenuBarBracket __menuBar)
		throws MLECallError
	{
		this.api.window().setMenuBar(__window, __menuBar);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public void setMenuItemActivateListener(
		ScritchWindowBracket __window,
		@Nullable ScritchMenuItemActivateListener __listener)
		throws MLECallError
	{
		this.api.window().setMenuItemActivateListener(__window, __listener);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public void setPaintListener(ScritchPaintableBracket __component,
		@Nullable ScritchPaintListener __listener)
		throws MLECallError
	{
		this.api.paintable().setPaintListener(__component, __listener);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public void setSelected(ScritchChoiceBracket __choice,
		int __atIndex,
		boolean __selected)
		throws MLECallError
	{
		this.api.choice().setSelected(__choice, __atIndex, __selected);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public void setSizeListener(ScritchComponentBracket __component,
		ScritchSizeListener __listener)
		throws MLECallError
	{
		this.api.component().setSizeListener(__component, __listener);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public void setSizeSuggestListener(ScritchViewBracket __view,
		@Nullable ScritchSizeSuggestListener __listener)
		throws MLECallError
	{
		this.api.view().setSizeSuggestListener(__view, __listener);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public void setString(ScritchLabelBracket __label,
		@Nullable String __string)
		throws MLECallError
	{
		this.api.label().setString(__label, __string);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public void setString(ScritchChoiceBracket __choice,
		int __atIndex,
		@Nullable String __string)
		throws MLECallError
	{
		this.api.choice().setString(__choice, __atIndex, __string);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public void setValueUpdateListener(ScritchComponentBracket __component,
		ScritchValueUpdateListener __listener)
		throws MLECallError
	{
		this.api.component().setValueUpdateListener(__component, __listener);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public void setView(ScritchViewBracket __view,
		int __x,
		int __y,
		int __width,
		int __height)
		throws MLECallError
	{
		this.api.view().setView(__view, __x, __y, __width, __height);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public void setViewListener(ScritchViewBracket __view,
		@Nullable ScritchViewListener __listener)
		throws MLECallError
	{
		this.api.view().setViewListener(__view, __listener);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public void setVisible(ScritchWindowBracket __window,
		boolean __visible)
		throws MLECallError
	{
		this.api.window().setVisible(__window, __visible);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public void setVisibleListener(ScritchComponentBracket __component,
		ScritchVisibleListener __listener)
		throws MLECallError
	{
		this.api.component().setVisibleListener(__component, __listener);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public ScritchViewInterface view()
	{
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public int width(
		ScritchScreenBracket __screen)
		throws MLECallError
	{
		return this.api.screen().width(__screen);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public int width(
		ScritchComponentBracket __component)
		throws MLECallError
	{
		return this.api.component().width(__component);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public ScritchWindowInterface window()
	{
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public int windowManagerType()
	{
		return this.api.environment().windowManagerType();
	}
}
