/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "lib/scritchui/scritchuiTypes.h"
#include "lib/scritchui/win32/win32.h"
#include "lib/scritchui/win32/win32Intern.h"

static sjme_jint sjme_scritchui_win32_keyModifiers(void)
{
	sjme_jint result;
	
	result = 0;
	if (GetKeyState(VK_SHIFT))
		result |= SJME_SCRITCHINPUT_MODIFIER_SHIFT;
	if (GetKeyState(VK_CONTROL))
		result |= SJME_SCRITCHINPUT_MODIFIER_CTRL;
	if (GetKeyState(VK_MENU))
		result |= SJME_SCRITCHINPUT_MODIFIER_ALT;
	if (GetKeyState(VK_LWIN) || GetKeyState(VK_RWIN))
		result |= SJME_SCRITCHINPUT_MODIFIER_COMMAND;
	
	return result;
}

static sjme_jint sjme_scritchui_win32_keyCode(sjme_jint inKey)
{
	/* Common keys. */
	if ((inKey >= '0' && inKey <= '9') ||
		(inKey >= 'A' && inKey <= 'Z'))
		return inKey;
	
	/* Map specific virtual codes first. */
	switch (inKey)
	{
		case VK_MENU:
			return SJME_SCRITCHINPUT_KEY_ALT;
		case VK_BACK:
			return SJME_SCRITCHINPUT_KEY_BACKSPACE;
		case VK_CAPITAL:
			return SJME_SCRITCHINPUT_KEY_CAPSLOCK;
		case VK_LMENU:
		case VK_RMENU:
			return SJME_SCRITCHINPUT_KEY_CONTEXT_MENU;
		case VK_CONTROL:
			return SJME_SCRITCHINPUT_KEY_CONTROL;
		case VK_DELETE:
			return SJME_SCRITCHINPUT_KEY_DELETE;
		case VK_DOWN:
			return SJME_SCRITCHINPUT_KEY_DOWN;
		case VK_END:
			return SJME_SCRITCHINPUT_KEY_END;
		case VK_RETURN:
			return SJME_SCRITCHINPUT_KEY_ENTER;
		case VK_ESCAPE:
			return SJME_SCRITCHINPUT_KEY_ESCAPE;
		case VK_F1:
			return SJME_SCRITCHINPUT_KEY_F1;
		case VK_F2:
			return SJME_SCRITCHINPUT_KEY_F2;
		case VK_F3:
			return SJME_SCRITCHINPUT_KEY_F3;
		case VK_F4:
			return SJME_SCRITCHINPUT_KEY_F4;
		case VK_F5:
			return SJME_SCRITCHINPUT_KEY_F5;
		case VK_F6:
			return SJME_SCRITCHINPUT_KEY_F6;
		case VK_F7:
			return SJME_SCRITCHINPUT_KEY_F7;
		case VK_F8:
			return SJME_SCRITCHINPUT_KEY_F8;
		case VK_F9:
			return SJME_SCRITCHINPUT_KEY_F9;
		case VK_F10:
			return SJME_SCRITCHINPUT_KEY_F10;
		case VK_F11:
			return SJME_SCRITCHINPUT_KEY_F11;
		case VK_F12:
			return SJME_SCRITCHINPUT_KEY_F12;
		case VK_F13:
			return SJME_SCRITCHINPUT_KEY_F13;
		case VK_F14:
			return SJME_SCRITCHINPUT_KEY_F14;
		case VK_F15:
			return SJME_SCRITCHINPUT_KEY_F15;
		case VK_F16:
			return SJME_SCRITCHINPUT_KEY_F16;
		case VK_F17:
			return SJME_SCRITCHINPUT_KEY_F17;
		case VK_F18:
			return SJME_SCRITCHINPUT_KEY_F18;
		case VK_F19:
			return SJME_SCRITCHINPUT_KEY_F19;
		case VK_F20:
			return SJME_SCRITCHINPUT_KEY_F20;
		case VK_F21:
			return SJME_SCRITCHINPUT_KEY_F21;
		case VK_F22:
			return SJME_SCRITCHINPUT_KEY_F22;
		case VK_F23:
			return SJME_SCRITCHINPUT_KEY_F23;
		case VK_F24:
			return SJME_SCRITCHINPUT_KEY_F24;
		case VK_HOME:
			return SJME_SCRITCHINPUT_KEY_HOME;
		case VK_INSERT:
			return SJME_SCRITCHINPUT_KEY_INSERT;
		case VK_LEFT:
			return SJME_SCRITCHINPUT_KEY_LEFT;
		case VK_LWIN:
		case VK_RWIN:
			return SJME_SCRITCHINPUT_KEY_LOGO;
		case VK_NUMLOCK:
			return SJME_SCRITCHINPUT_KEY_NUMLOCK;
		case VK_NUMPAD0:
			return SJME_SCRITCHINPUT_KEY_NUMPAD_0;
		case VK_NUMPAD1:
			return SJME_SCRITCHINPUT_KEY_NUMPAD_1;
		case VK_NUMPAD2:
			return SJME_SCRITCHINPUT_KEY_NUMPAD_2;
		case VK_NUMPAD3:
			return SJME_SCRITCHINPUT_KEY_NUMPAD_3;
		case VK_NUMPAD4:
			return SJME_SCRITCHINPUT_KEY_NUMPAD_4;
		case VK_NUMPAD5:
			return SJME_SCRITCHINPUT_KEY_NUMPAD_5;
		case VK_NUMPAD6:
			return SJME_SCRITCHINPUT_KEY_NUMPAD_6;
		case VK_NUMPAD7:
			return SJME_SCRITCHINPUT_KEY_NUMPAD_7;
		case VK_NUMPAD8:
			return SJME_SCRITCHINPUT_KEY_NUMPAD_8;
		case VK_NUMPAD9:
			return SJME_SCRITCHINPUT_KEY_NUMPAD_9;
		case VK_DECIMAL:
			return SJME_SCRITCHINPUT_KEY_NUMPAD_DECIMAL;
		case VK_DIVIDE:
			return SJME_SCRITCHINPUT_KEY_NUMPAD_DIVIDE;
#if 0
		case VK_NUMPAD_ENTER:
			return SJME_SCRITCHINPUT_KEY_NUMPAD_ENTER;
#endif
		case VK_SUBTRACT:
			return SJME_SCRITCHINPUT_KEY_NUMPAD_MINUS;
		case VK_MULTIPLY:
			return SJME_SCRITCHINPUT_KEY_NUMPAD_MULTIPLY;
		case VK_ADD:
			return SJME_SCRITCHINPUT_KEY_NUMPAD_PLUS;
		case VK_PRIOR:
			return SJME_SCRITCHINPUT_KEY_PAGE_UP;
		case VK_NEXT:
			return SJME_SCRITCHINPUT_KEY_PAGE_DOWN;
		case VK_PAUSE:
			return SJME_SCRITCHINPUT_KEY_PAUSE;
		case VK_SNAPSHOT:
			return SJME_SCRITCHINPUT_KEY_PRINTSCREEN;
		case VK_RIGHT:
			return SJME_SCRITCHINPUT_KEY_RIGHT;
		case VK_SCROLL:
			return SJME_SCRITCHINPUT_KEY_SCROLLLOCK;
		case VK_SHIFT:
			return SJME_SCRITCHINPUT_KEY_SHIFT;
		case VK_SPACE:
			return SJME_SCRITCHINPUT_KEY_SPACE;
		case VK_TAB:
			return SJME_SCRITCHINPUT_KEY_TAB;
		case VK_UP:
			return SJME_SCRITCHINPUT_KEY_UP;
	}
	
	/* Unknown. */
	return SJME_SCRITCHINPUT_KEY_UNKNOWN;
}

static sjme_jint sjme_scritchui_win32_mouseButtons(sjme_jint inMod)
{
	sjme_jint result;
	
	/* Primary mouse buttons. */
	result = 0;
	if (inMod & MK_LBUTTON)
		result |= (1 << 0);
	if (inMod & MK_RBUTTON)
		result |= (1 << 1);
	if (inMod & MK_MBUTTON)
		result |= (1 << 2);
	
	/* Extra side buttons. */
	if (inMod & MK_XBUTTON1)
		result |= (1 << 3);
	if (inMod & MK_XBUTTON2)
		result |= (1 << 4);
	
	return result;
}

static sjme_jint sjme_scritchui_win32_mouseModifiers(sjme_jint inMod)
{
	sjme_jint result;
	
	result = 0;
	if (inMod & MK_CONTROL)
		result |= SJME_SCRITCHINPUT_MODIFIER_CTRL;
	if (inMod & MK_SHIFT)
		result |= SJME_SCRITCHINPUT_MODIFIER_SHIFT;
	
	/* Have key modifiers on top, except for CTRL and SHIFT. */
	return result | (sjme_scritchui_win32_keyModifiers() &
		~(SJME_SCRITCHINPUT_MODIFIER_CTRL |
		SJME_SCRITCHINPUT_MODIFIER_SHIFT));
}

static sjme_errorCode sjme_scritchui_win32_windowProc_CHAR(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNullable HWND hWnd,
	sjme_attrInValue UINT message,
	sjme_attrInValue WPARAM wParam,
	sjme_attrInValue LPARAM lParam,
	sjme_attrOutNullable LRESULT* lResult)
{
	sjme_scritchui_uiComponent inComponent;
	sjme_scritchui_listener_input* infoCore;
	sjme_scritchinput_event inputEvent;
	
	if (inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Initially set that we did not handle this. */
	if (lResult != NULL)
		*lResult = 1;
	
	/* Recover component. */
	inComponent = NULL;
	if (sjme_error_is(inState->implIntern->recoverComponent(inState,
		hWnd, &inComponent)))
		return SJME_ERROR_USE_FALLBACK;
	
	/* If this is a window, we need the bound component. */
	if (inComponent->common.type == SJME_SCRITCHUI_TYPE_WINDOW)
		inComponent = ((sjme_scritchui_uiWindow)inComponent)->focusedComponent;
	if (inComponent == NULL)
		return SJME_ERROR_USE_FALLBACK;
	
	/* Only call if there is a handler for it. */
	infoCore = &SJME_SCRITCHUI_LISTENER_CORE(inComponent, input);
	if (infoCore->callback == NULL)
		return SJME_ERROR_USE_FALLBACK;
		
	/* We are handling this now. */
	if (lResult != NULL)
		*lResult = 0;
	
	/* Setup event. */
	memset(&inputEvent, 0, sizeof(inputEvent));
	inputEvent.type = SJME_SCRITCHINPUT_TYPE_KEY_CHAR_PRESSED;
	inState->nanoTime(&inputEvent.time);
	inputEvent.data.key.code = wParam & 0xFFFF;
	inputEvent.data.key.modifiers = sjme_scritchui_win32_keyModifiers();
	
	/* Call listener. */
	return infoCore->callback(inState, inComponent, &inputEvent);
}

static sjme_errorCode sjme_scritchui_win32_windowProc_CLOSE(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNullable HWND hWnd,
	sjme_attrInValue UINT message,
	sjme_attrInValue WPARAM wParam,
	sjme_attrInValue LPARAM lParam,
	sjme_attrOutNullable LRESULT* lResult)
{
	sjme_scritchui_uiComponent inComponent;
	sjme_scritchui_uiWindow inWindow;
	sjme_scritchui_listener_close* infoCore;
	
	if (inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* We always treat this as handled. */
	lResult = 0;
		
	/* Recover component, if not one then not one of ours. */
	inComponent = NULL;
	if (sjme_error_is(inState->implIntern->recoverComponent(inState,
		hWnd, &inComponent)))
		return SJME_ERROR_NONE;
	
	/* We can only do this on windows. */
	if (inComponent->common.type != SJME_SCRITCHUI_TYPE_WINDOW)
		return SJME_ERROR_NONE;
	inWindow = (sjme_scritchui_uiWindow)inComponent;
	
	/* Get target listener. */
	infoCore = &SJME_SCRITCHUI_LISTENER_CORE(inWindow, close);
	
	/* Invoke callback if there is one. */
	if (infoCore->callback != NULL)
		return infoCore->callback(inState, inWindow);
	
	/* Success when there is nothing to call! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_win32_windowProc_COMMAND(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNullable HWND hWnd,
	sjme_attrInValue UINT message,
	sjme_attrInValue WPARAM wParam,
	sjme_attrInValue LPARAM lParam,
	sjme_attrOutNullable LRESULT* lResult)
{
	sjme_scritchui_uiComponent inComponent;
	sjme_scritchui_uiWindow inWindow;
	sjme_scritchui_uiMenuBar menuBar;
	sjme_scritchui_listener_menuItemActivate* infoCore;
	sjme_jint id;
	
	if (inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Initially set that we did not handle this. */
	if (lResult != NULL)
		*lResult = 1;
	
	/* Recover component, if not one then not one of ours. */
	inComponent = NULL;
	if (sjme_error_is(inState->implIntern->recoverComponent(inState,
		hWnd, &inComponent)))
		return SJME_ERROR_USE_FALLBACK;
	
	/* Must be a window to activate menu items. */
	if (inComponent->common.type != SJME_SCRITCHUI_TYPE_WINDOW)
		return SJME_ERROR_USE_FALLBACK;
	inWindow = (sjme_scritchui_uiWindow)inComponent;
	
	/* We can handle this. */
	if (lResult != NULL)
		*lResult = 0;
	
	/* Get target listener. */
	infoCore = &SJME_SCRITCHUI_LISTENER_USER(inWindow, menuItemActivate);
	
	/* If there is none or there is no bar, then we do not care. */
	menuBar = inWindow->menuBar;
	if (infoCore->callback == NULL || menuBar == NULL)
		return SJME_ERROR_NONE;
	
	/* Recover the menu ID used. */
	id = LOWORD(wParam);
	
	/* Go through the menu system to find a matching ID. */
	return inState->intern->menuItemActivateById(inState, inWindow,
		(sjme_scritchui_uiMenuKind)menuBar,
		id, 0xFFFF);
}

static sjme_errorCode sjme_scritchui_win32_windowProc_ERASEBKGND(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNullable HWND hWnd,
	sjme_attrInValue UINT message,
	sjme_attrInValue WPARAM wParam,
	sjme_attrInValue LPARAM lParam,
	sjme_attrOutNullable LRESULT* lResult)
{
	return SJME_ERROR_USE_FALLBACK;
}

static sjme_errorCode sjme_scritchui_win32_windowProc_GETMINMAXINFO(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNullable HWND hWnd,
	sjme_attrInValue UINT message,
	sjme_attrInValue WPARAM wParam,
	sjme_attrInValue LPARAM lParam,
	sjme_attrOutNullable LRESULT* lResult)
{
	sjme_errorCode error;
	LPMINMAXINFO minMax;
	sjme_scritchui_uiComponent inComponent;
	sjme_scritchui_uiWindow inWindow;
	
	if (inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover component. */
	inComponent = NULL;
	if (sjme_error_is(inState->implIntern->recoverComponent(inState,
		hWnd, &inComponent)))
		return SJME_ERROR_USE_FALLBACK;
	
	/* We can only do this on windows. */
	if (inComponent->common.type != SJME_SCRITCHUI_TYPE_WINDOW)
		return SJME_ERROR_USE_FALLBACK;
	inWindow = (sjme_scritchui_uiWindow)inComponent;
	
	/* Copy size details. */
	minMax = (LPMINMAXINFO)lParam;
	if (minMax != NULL && inWindow->min.width != 0 &&
		inWindow->min.height != 0)
	{
		minMax->ptMinTrackSize.x =
			inWindow->min.width + inWindow->minOverhead.width;
		minMax->ptMinTrackSize.y =
			inWindow->min.height + inWindow->minOverhead.height;
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_win32_windowProc_KEY(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNullable HWND hWnd,
	sjme_attrInValue UINT message,
	sjme_attrInValue WPARAM wParam,
	sjme_attrInValue LPARAM lParam,
	sjme_attrOutNullable LRESULT* lResult)
{
	sjme_scritchui_uiComponent inComponent;
	sjme_scritchui_listener_input* infoCore;
	sjme_scritchinput_event inputEvent;
	
	if (inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Initially set that we did not handle this. */
	if (lResult != NULL)
		*lResult = 1;
	
	/* Recover component. */
	inComponent = NULL;
	if (sjme_error_is(inState->implIntern->recoverComponent(inState,
		hWnd, &inComponent)))
		return SJME_ERROR_USE_FALLBACK;
	
	/* If this is a window, we need the bound component. */
	if (inComponent->common.type == SJME_SCRITCHUI_TYPE_WINDOW)
		inComponent = ((sjme_scritchui_uiWindow)inComponent)->focusedComponent;
	if (inComponent == NULL)
		return SJME_ERROR_USE_FALLBACK;
	
	/* Only call if there is a handler for it. */
	infoCore = &SJME_SCRITCHUI_LISTENER_CORE(inComponent, input);
	if (infoCore->callback == NULL)
		return SJME_ERROR_USE_FALLBACK;
		
	/* We are handling this now. */
	if (lResult != NULL)
		*lResult = 0;
	
	/* Setup event. */
	memset(&inputEvent, 0, sizeof(inputEvent));
	inState->nanoTime(&inputEvent.time);
	if (message == WM_KEYDOWN)
	{
		if ((lParam & 0x0F) > 1)
			inputEvent.type = SJME_SCRITCHINPUT_TYPE_KEY_REPEATED;
		else
			inputEvent.type = SJME_SCRITCHINPUT_TYPE_KEY_PRESSED;
	}
	else
		inputEvent.type = SJME_SCRITCHINPUT_TYPE_KEY_RELEASED;
	inputEvent.data.key.modifiers = sjme_scritchui_win32_keyModifiers();
	inputEvent.data.key.code = sjme_scritchui_win32_keyCode(wParam);
	
	/* Call listener. */
	return infoCore->callback(inState, inComponent, &inputEvent);
}

static sjme_errorCode sjme_scritchui_win32_windowProc_MOUSE(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNullable HWND hWnd,
	sjme_attrInValue UINT message,
	sjme_attrInValue WPARAM wParam,
	sjme_attrInValue LPARAM lParam,
	sjme_attrOutNullable LRESULT* lResult)
{
	sjme_errorCode error;
	sjme_scritchui_uiComponent inComponent;
	sjme_scritchui_listener_input* infoCore;
	sjme_scritchinput_event inputEvent;
	sjme_jint normalButton, normalShift, tx, ty;
	sjme_jboolean pressed;
	sjme_scritchui_uiView view;
	
	if (inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Initially set that we did not handle this. */
	if (lResult != NULL)
		*lResult = 1;
	
	/* Recover component. */
	inComponent = NULL;
	if (sjme_error_is(inState->implIntern->recoverComponent(inState,
		hWnd, &inComponent)))
		return SJME_ERROR_USE_FALLBACK;
	
	/* Only call if there is a handler for it. */
	infoCore = &SJME_SCRITCHUI_LISTENER_CORE(inComponent, input);
	if (infoCore->callback == NULL)
		return SJME_ERROR_USE_FALLBACK;
		
	/* We are handling this now. */
	if (lResult != NULL)
		*lResult = 0;
	
	/* Normalize press. */
	pressed = SJME_JNI_FALSE;
	if (message == WM_LBUTTONDOWN || message == WM_MBUTTONDOWN ||
		message == WM_RBUTTONDOWN || message == WM_XBUTTONDOWN)
		pressed = SJME_JNI_TRUE;
	
	/* Normalize button, oddly the window events here are right-handed */
	/* mice while the modifiers in wParam are left-handed, but then also */
	/* as well they might not be depending on the types of events we */
	/* handle? This makes absolutely zero sense. *facepaw* */
	normalButton = 0;
	if (message == WM_LBUTTONDOWN || message == WM_LBUTTONUP)
		normalButton = 1;
	else if (message == WM_MBUTTONDOWN || message == WM_MBUTTONUP)
		normalButton = 3;
	else if (message == WM_RBUTTONDOWN || message == WM_RBUTTONUP)
		normalButton = 2;
	
	/* Determine button mask shift. */
	normalShift = 0;
	if (normalButton != 0)
		normalShift = (1 << (normalButton - 1));
		
	/* If there is a parent, and it is a view, get the view offset. */
	view = NULL;
	if (inComponent->parent != NULL)
		if (sjme_error_is(error = inState->intern->getView(inState,
			inComponent->parent, &view)))
			if (error != SJME_ERROR_INVALID_ARGUMENT)
				return sjme_error_default(error);
	
	/* Is this in a view? */
	tx = 0;
	ty = 0;
	if (view != NULL)
	{
		tx = view->view.s.x;
		ty = view->view.s.y;
	}
	
	/* Setup event. */
	memset(&inputEvent, 0, sizeof(inputEvent));
	inState->nanoTime(&inputEvent.time);
	if (message == WM_MOUSEMOVE)
	{
		inputEvent.type = SJME_SCRITCHINPUT_TYPE_MOUSE_MOTION;
		inputEvent.data.mouseMotion.buttonMask =
			sjme_scritchui_win32_mouseButtons(LOWORD(wParam));
		inputEvent.data.mouseMotion.modifiers =
			sjme_scritchui_win32_mouseModifiers(LOWORD(wParam));
		inputEvent.data.mouseMotion.x = LOWORD(lParam) + tx;
		inputEvent.data.mouseMotion.y = HIWORD(lParam) + ty;
	}
	
	/* Pressed or released. */
	else
	{
		/* Which one? */
		if (pressed)
			inputEvent.type = SJME_SCRITCHINPUT_TYPE_MOUSE_BUTTON_PRESSED;
		else
			inputEvent.type = SJME_SCRITCHINPUT_TYPE_MOUSE_BUTTON_RELEASED;
		
		inputEvent.data.mouseButton.button = normalButton;
		inputEvent.data.mouseButton.buttonMask =
			sjme_scritchui_win32_mouseButtons(LOWORD(wParam)) |
			(pressed ? normalShift : 0);
		inputEvent.data.mouseButton.modifiers =
			sjme_scritchui_win32_mouseModifiers(LOWORD(wParam));
		inputEvent.data.mouseButton.x = LOWORD(lParam) + tx;
		inputEvent.data.mouseButton.y = HIWORD(lParam) + ty;
	}
	
	/* Call listener. */
	return infoCore->callback(inState, inComponent, &inputEvent);
}

static sjme_errorCode sjme_scritchui_win32_windowProc_PAINT(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNullable HWND hWnd,
	sjme_attrInValue UINT message,
	sjme_attrInValue WPARAM wParam,
	sjme_attrInValue LPARAM lParam,
	sjme_attrOutNullable LRESULT* lResult)
{
	sjme_errorCode error;
	sjme_scritchui_uiComponent inComponent;
	sjme_scritchui_uiPaintable paintable;
	sjme_scritchui_pencil pencil;
	sjme_scritchui_pencilFont defaultFont;
	sjme_scritchui_listener_paint* infoPaintCore;
	sjme_frontEnd frontEnd;
	HDC hDc;
	PAINTSTRUCT paintInfo;
	sjme_jint w, h, tx, ty;
	sjme_scritchui_uiView view;
	
	/* Initially set that we did not paint this. */
	if (lResult != NULL)
		*lResult = 1;
	
	if (inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Recover component, ignore if this is something else. */
	inComponent = NULL;
	if (sjme_error_is(inState->implIntern->recoverComponent(inState,
		hWnd, &inComponent)))
		return SJME_ERROR_USE_FALLBACK;
	
	/* Get component size. */
	w = 0;
	h = 0;
	if (sjme_error_is(error = inState->apiInThread->componentSize(inState,
		inComponent, &w, &h)))
		return sjme_error_default(error);
	
	/* Can this actually be painted on? */
	paintable = NULL;
	if (sjme_error_is(error = inState->intern->getPaintable(inState,
		inComponent, &paintable)) || paintable == NULL)
	{
		if (error == SJME_ERROR_INVALID_ARGUMENT)
			return SJME_ERROR_USE_FALLBACK;
		return sjme_error_default(error);
	}
	
	/* Get callback info, if there is none then do nothing */
	infoPaintCore = &SJME_SCRITCHUI_LISTENER_CORE(paintable, paint);
	if (infoPaintCore->callback == NULL)
		return SJME_ERROR_USE_FALLBACK;
	
	/* A default font is required. */
	defaultFont = NULL;
	if (sjme_error_is(inState->intern->fontBuiltin(
		inState, &defaultFont)) ||
		defaultFont == NULL)
		return sjme_error_default(error);
	
	/* If there is a parent, and it is a view, get the view offset. */
	view = NULL;
	if (inComponent->parent != NULL)
		if (sjme_error_is(error = inState->intern->getView(inState,
			inComponent->parent, &view)))
			if (error != SJME_ERROR_INVALID_ARGUMENT)
				return sjme_error_default(error);
	
	/* Is this in a view? */
	tx = 0;
	ty = 0;
	if (view != NULL)
	{
		tx = -view->view.s.x;
		ty = -view->view.s.y;
	}
	
	/* Begin painting. */
	memset(&paintInfo, 0, sizeof(paintInfo));
	SetLastError(0);
	hDc = BeginPaint(hWnd, &paintInfo);
	if (hDc == NULL)
		return inState->implIntern->getLastError(inState,
			SJME_ERROR_NATIVE_WIDGET_FAILURE);
		
	/* Setup frontend info. */
	memset(&frontEnd, 0, sizeof(frontEnd));
	frontEnd.wrapper = hWnd;
	frontEnd.data = hDc;
	
	/* Setup pencil. */
	pencil = &paintable->pencil;
	memset(pencil, 0, sizeof(*pencil));
	if (sjme_error_is(error = sjme_scritchpen_initStatic(
		pencil, inState,
		&sjme_scritchui_win32_pencilFunctions,
		NULL, NULL,
		SJME_GFX_PIXEL_FORMAT_INT_RGB888,
		tx, ty,
		w, h, w,
		defaultFont, &frontEnd)))
		goto fail_badPaint;

	/* The clipping area is set to the region that needs redrawing. */
	pencil->api->setClip(pencil,
		paintInfo.rcPaint.left, paintInfo.rcPaint.top,
		paintInfo.rcPaint.right - paintInfo.rcPaint.left,
		paintInfo.rcPaint.bottom - paintInfo.rcPaint.top);
	
	/* Perform any painting. */
	if (sjme_error_is(error = infoPaintCore->callback(
		inState, inComponent, pencil, w, h,
		0)))
		goto fail_badPaint;
	
	/* Stop painting. */
	SetLastError(0);
	EndPaint(hWnd, &paintInfo);
	
	/* We did paint this successfully. */
	if (lResult != NULL)
		*lResult = 0;
	
	/* Success! */
	return SJME_ERROR_NONE;

fail_badPaint:
	if (hDc != NULL)
	{
		SetLastError(0);
		EndPaint(hWnd, &paintInfo);
	}
	
	return sjme_error_default(error);
}

static sjme_errorCode sjme_scritchui_win32_windowProc_SCROLL(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNullable HWND hWnd,
	sjme_attrInValue UINT message,
	sjme_attrInValue WPARAM wParam,
	sjme_attrInValue LPARAM lParam,
	sjme_attrOutNullable LRESULT* lResult)
{
	sjme_errorCode error;
	sjme_scritchui_uiComponent inComponent;
	sjme_scritchui_uiView view;
	sjme_scritchui_rect rect;
	sjme_jboolean tracking;
	sjme_jint* adjust;
	sjme_jint* page;
	sjme_jint by;
	
	if (inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Initially set that we did not handle this. */
	if (lResult != NULL)
		*lResult = 1;
	
	/* Recover component, if not one then not one of ours. */
	inComponent = NULL;
	if (sjme_error_is(inState->implIntern->recoverComponent(inState,
		hWnd, &inComponent)))
		return SJME_ERROR_USE_FALLBACK;
	
	/* Recover view from component, ignore if not one. */
	view = NULL;
	if (sjme_error_is(inState->intern->getView(inState, inComponent,
		&view)) || view == NULL)
		return SJME_ERROR_USE_FALLBACK;
		
	/* Get base view rectangle. */
	memset(&rect, 0, sizeof(rect));
	if (sjme_error_is(error = inState->apiInThread->viewGetView(inState,
		inComponent, &rect)))
		return sjme_error_default(error);
	
#if 0
	/* Debug. */
	sjme_message("View rect: (%d, %d) [%d, %d]",
		rect.s.x, rect.s.y, rect.d.width, rect.d.height);
#endif
	
	/* Which are we adjusting? */
	if (message == WM_HSCROLL)
	{
		adjust = &rect.s.x;
		page = &view->pageSize.width;
	}
	else
	{
		adjust = &rect.s.y;
		page = &view->pageSize.height;
	}
	
	/* Actively tracking scrolling? */
	tracking = (LOWORD(wParam) == SB_THUMBPOSITION ||
		LOWORD(wParam) == SB_THUMBTRACK);
	if (tracking)
	{
		/* We only say we handled this if we are doing active tracking. */
		if (lResult != NULL)
			*lResult = 0;
		
		/* Which position is getting adjusted? */
		*adjust = HIWORD(wParam);
	}
	
	/* Button actions. */
	else if (LOWORD(wParam) == SB_LINELEFT)
	{
		by = *page / 8;
		if (by <= 0)
			by = 1;
		
		*adjust -= by;
	}
	else if (LOWORD(wParam) == SB_LINERIGHT)
	{
		by = *page / 8;
		if (by <= 0)
			by = 1;
		
		*adjust += by;
	}
	else if (LOWORD(wParam) == SB_PAGELEFT)
		*adjust -= *page;
	else if (LOWORD(wParam) == SB_PAGERIGHT)
		*adjust += *page;
	
	/* Set new scroll position. */
	if (sjme_error_is(error = inState->apiInThread->viewSetView(inState,
		inComponent, &rect.s)))
		return sjme_error_default(error);
	
	/* Unless we are tracking, let Windows handle this. */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_win32_windowProc_SHOWWINDOW(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNullable HWND hWnd,
	sjme_attrInValue UINT message,
	sjme_attrInValue WPARAM wParam,
	sjme_attrInValue LPARAM lParam,
	sjme_attrOutNullable LRESULT* lResult)
{
	sjme_scritchui_uiComponent inComponent;
	
	/* Default to unhandled. */
	if (lResult != NULL)
		*lResult = 1;
	
	if (inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Recover component, ignore if this is something else. */
	inComponent = NULL;
	if (sjme_error_is(inState->implIntern->recoverComponent(inState,
		hWnd, &inComponent)))
		return SJME_ERROR_USE_FALLBACK;
	
	/* We need to recurse and have ScritchUI handle this. */
	if (inComponent->common.type == SJME_SCRITCHUI_TYPE_WINDOW)
	{
		/* We are handling this. */
		if (lResult != NULL)
			*lResult = 0;
		
		/* Let ScritchUI determine this. */
		return inState->intern->updateVisibleWindow(
			inState, inComponent,
			wParam != FALSE);
	}
	
	/* Let Windows handle this. */
	return SJME_ERROR_USE_FALLBACK;
}

static sjme_errorCode sjme_scritchui_win32_windowProc_USER(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNullable HWND hWnd,
	sjme_attrInValue UINT message,
	sjme_attrInValue WPARAM wParam,
	sjme_attrInValue LPARAM lParam,
	sjme_attrOutNullable LRESULT* lResult)
{
	sjme_errorCode error;
	sjme_thread_mainFunc threadMain;
	sjme_thread_parameter threadAnything;
	
	if (inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	threadMain = (sjme_thread_mainFunc)wParam;
	threadAnything = (sjme_thread_parameter)lParam;
	
	if (threadMain == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Forward call. */
	if (sjme_error_is(error = SJME_THREAD_RESULT_AS_ERROR(
		threadMain(threadAnything))))
		return sjme_error_defaultOr(error, SJME_ERROR_NATIVE_WIDGET_FAILURE);
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_win32_intern_getLastError(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInValue sjme_errorCode ifOkay)
{
	DWORD winErr;
	
	if (inState == NULL)
		return SJME_ERROR_NONE;
	
	/* Get Windows error, but then also clear it. */
	winErr = GetLastError();
	SetLastError(0);
	
#if 0 && defined(SJME_CONFIG_DEBUG)
	/* Debug. */
	sjme_message("GetLastError() == %d 0x%08x",
		winErr, winErr);
#endif
	
	/* Use given error, if successful. */
	if (winErr == ERROR_SUCCESS)
		return ifOkay;
	
	/* Did not actually map to anything? */
	return sjme_error_default(ifOkay);
}

sjme_errorCode sjme_scritchui_win32_intern_recoverComponent(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNullable HWND hWnd,
	sjme_attrOutNullable sjme_scritchui_uiComponent* outComponent)
{
	sjme_scritchui_uiComponent result;
	
	if (inState == NULL || outComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* NULL maps to a NULL component. */
	if (hWnd == NULL)
	{
		*outComponent = NULL;
		return SJME_ERROR_NONE;
	}
	
	/* Get the component this refers to. */
	SetLastError(0);
	result = (sjme_scritchui_uiComponent)GetWindowLongPtr(hWnd,
		GWLP_USERDATA);
	
	/* Basic validation in case we got some other user data. */
	if (result != NULL && result->common.state != inState)
		result = NULL;
		
	/* Success? */
	*outComponent = result;
	return inState->implIntern->getLastError(inState, SJME_ERROR_NONE);
}

sjme_errorCode sjme_scritchui_win32_intern_windowProc(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNullable HWND hWnd,
	sjme_attrInValue UINT message,
	sjme_attrInValue WPARAM wParam,
	sjme_attrInValue LPARAM lParam,
	sjme_attrOutNullable LRESULT* lResult)
{
	sjme_errorCode error;
	LRESULT useResult;
	
	if (inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
#if 0 && defined(SJME_CONFIG_DEBUG)
	/* Debug. */
	sjme_message("Win32 message: %p %d %p %p)",
		hWnd, message, wParam, lParam);
#endif
	
	/* Handle. */
	useResult = 0;
	error = SJME_ERROR_NONE;
	switch (message)
	{
			/* Unicode character. */
		case WM_CHAR:
			error = sjme_scritchui_win32_windowProc_CHAR(
				inState, hWnd, message, wParam, lParam, &useResult);
			break;
		
			/* Window is closed. */
		case WM_CLOSE:
			error = sjme_scritchui_win32_windowProc_CLOSE(
				inState, hWnd, message, wParam, lParam, &useResult);
			break;
			
			/* Menu or button command. */
		case WM_COMMAND:
			error = sjme_scritchui_win32_windowProc_COMMAND(
				inState, hWnd, message, wParam, lParam, &useResult);
			break;
		
			/* Erase background. */
		case WM_ERASEBKGND:
			error = sjme_scritchui_win32_windowProc_ERASEBKGND(
				inState, hWnd, message, wParam, lParam, &useResult);
			break;
		
			/* Get minimum and maximum window bounds. */
		case WM_GETMINMAXINFO:
			error = sjme_scritchui_win32_windowProc_GETMINMAXINFO(
				inState, hWnd, message, wParam, lParam, &useResult);
			break;
		
		case WM_KEYDOWN:
		case WM_KEYUP:
			error = sjme_scritchui_win32_windowProc_KEY(
				inState, hWnd, message, wParam, lParam, &useResult);
			break;
			
			/* Mouse interaction in window. */
		case WM_LBUTTONDOWN:
		case WM_LBUTTONUP:
		case WM_MBUTTONDOWN:
		case WM_MBUTTONUP:
		case WM_RBUTTONDOWN:
		case WM_RBUTTONUP:
		case WM_XBUTTONDOWN:
		case WM_XBUTTONUP:
		case WM_MOUSEMOVE:
			error = sjme_scritchui_win32_windowProc_MOUSE(
				inState, hWnd, message, wParam, lParam, &useResult);
			break;

			/* Paint window. */
		case WM_PAINT:
			error = sjme_scritchui_win32_windowProc_PAINT(
				inState, hWnd, message, wParam, lParam, &useResult);
			break;
			
			/* Window is scrolled. */
		case WM_HSCROLL:
		case WM_VSCROLL:
			error = sjme_scritchui_win32_windowProc_SCROLL(
				inState, hWnd, message, wParam, lParam, &useResult);
			break;
			
			/* Window being shown or hidden. */
		case WM_SHOWWINDOW:
			error = sjme_scritchui_win32_windowProc_SHOWWINDOW(
				inState, hWnd, message, wParam, lParam, &useResult);
			break;
			
			/* Callback function. */
		case WM_USER:
			error = sjme_scritchui_win32_windowProc_USER(
				inState, hWnd, message, wParam, lParam, &useResult);
			break;
		
			/* Unknown, let Windows handle it. */
		default:
			error = SJME_ERROR_USE_FALLBACK;
			break;
	}
	
#if 0 && defined(SJME_CONFIG_DEBUG)
	/* Debug. */
	if (sjme_error_is(error))
		sjme_message("Win32 message FAIL: %p %d %p %p -> %d)",
			hWnd, message, wParam, lParam, error);
#endif
	
	/* Fallback to default? */
	if (sjme_error_is(error) || error == SJME_ERROR_USE_FALLBACK)
		useResult = DefWindowProc(hWnd, message,
			wParam, lParam);
	
	/* Success? */
	if (lResult != NULL)
		*lResult = useResult;
	if (error == SJME_ERROR_USE_FALLBACK)
		return SJME_ERROR_NONE;
	return error;
}
