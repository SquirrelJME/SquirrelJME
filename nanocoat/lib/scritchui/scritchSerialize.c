/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "lib/scritchui/core/core.h"
#include "lib/scritchui/core/coreSerial.h"
#include "lib/scritchui/scritchuiTypes.h"
#include "sjme/alloc.h"
#include "sjme/debug.h"

/* clang-format off */
/* ------------------------------------------------------------------------ */

/** Serial variables. */
#define SJME_SCRITCHUI_SERIAL_VARS(what) \
	sjme_errorCode error; \
	sjme_jboolean direct; \
	volatile sjme_scritchui_serialData data; \
	SJME_TOKEN_PASTE(sjme_scritchui_serialData_, what)* volatile serial

/** Pre-check call to make. */
#define SJME_SCRITCHUI_SERIAL_PRE_CHECK \
	do { if (inState == NULL) \
    { \
		return SJME_ERROR_NULL_ARGUMENTS; \
	} } while(0)

/** Check for being in the loop. */
#define SJME_SCRITCHUI_SERIAL_LOOP_CHECK(what) \
	do { \
		error = SJME_ERROR_NOT_IMPLEMENTED; \
		direct = SJME_JNI_FALSE; \
		 \
		if (inState->api->loopIsInThread == NULL || \
            inState->api->what == NULL || \
            inState->apiInThread->what == NULL || \
			sjme_error_is(error = inState->api->loopIsInThread(inState, \
				&direct))) \
			return sjme_error_default(error); \
	} while (0)

/** Setup pre-serialization. */
#define SJME_SCRITCHUI_SERIAL_SETUP(key, what) \
	do { memset(&data, 0, sizeof(data)); \
	data.type = (key); \
	data.error = SJME_ERROR_UNKNOWN; \
	data.state = inState; \
	serial = &data.data.what; } while (0)

/** Invoke serial call and wait for result. */
#define SJME_SCRITCHUI_INVOKE_WAIT \
	do { sjme_thread_barrier(); \
		if (sjme_error_is(error = inState->api->loopExecuteWait(inState, \
		sjme_scritchui_serialDispatch, &data))) \
		return sjme_error_default(error); \
	return data.error; } while (0)

/** Common shared chunk of forwarding code, to reduce duplicates. */
#define SJME_SCRITCHUI_SERIAL_CHUNK(what, whatType, directInvokeArgs) \
	SJME_SCRITCHUI_SERIAL_VARS(what); \
	 \
	SJME_SCRITCHUI_SERIAL_PRE_CHECK; \
	SJME_SCRITCHUI_SERIAL_LOOP_CHECK(what); \
	 \
	/* Direct call? */ \
	if (direct) \
		return inState->apiInThread->what directInvokeArgs; \
	 \
	/* Serialize and Store. */ \
	SJME_SCRITCHUI_SERIAL_SETUP( \
		whatType, \
		what)

/** Pass a serial value. */
#define SJME_SCRITCHUI_SERIAL_PASS(what) \
	serial->what = what

/** Declares dispatch type. */
#define SJME_SCRITCHUI_DISPATCH_DECL(what) \
	SJME_TOKEN_PASTE(sjme_scritchui_serialData_, what)* volatile what

/** Performs dispatch call. */
#define SJME_SCRITCHUI_DISPATCH_CALL(what, args) \
	do { what = &data->data.what; \
	if (state->apiInThread->what == NULL) \
		return SJME_THREAD_RESULT(SJME_ERROR_NOT_IMPLEMENTED); \
	data->error = state->apiInThread->what args; } while (0)

/** Simplified case call. */
#define SJME_SCRITCHUI_DISPATCH_CASE(what, whatType, args) \
	case whatType: \
		SJME_SCRITCHUI_DISPATCH_CALL(what, args); \
		break

/** Simplified listener dispatch call. */
#define SJME_SCRITCHUI_DISPATCH_CASE_LISTENER(what, whatType) \
	SJME_SCRITCHUI_DISPATCH_CASE(what, \
		whatType, \
		(state, \
		what->inComponent, \
		what->inListener, \
		what->copyFrontEnd))

/* ------------------------------------------------------------------------ */
/* clang-format on */

static sjme_thread_result sjme_scritchui_serialDispatch(
	sjme_attrInNullable sjme_thread_parameter anything)
{
#define SJME_SCRITCHUI_DISPATCH_SWITCH_BEGIN switch (data->type) {
#define SJME_SCRITCHUI_DISPATCH_SWITCH_END \
	default: \
		return SJME_THREAD_RESULT(SJME_ERROR_NOT_IMPLEMENTED); }
	
	volatile sjme_scritchui_serialData* data;
	SJME_SCRITCHUI_DISPATCH_DECL(componentRepaint);
	SJME_SCRITCHUI_DISPATCH_DECL(componentRevalidate);
	SJME_SCRITCHUI_DISPATCH_DECL(componentSetInputListener);
	SJME_SCRITCHUI_DISPATCH_DECL(componentSetPaintListener);
	SJME_SCRITCHUI_DISPATCH_DECL(componentSetSizeListener);
	SJME_SCRITCHUI_DISPATCH_DECL(componentSetVisibleListener);
	SJME_SCRITCHUI_DISPATCH_DECL(componentSize);
	SJME_SCRITCHUI_DISPATCH_DECL(containerAdd);
	SJME_SCRITCHUI_DISPATCH_DECL(containerRemove);
	SJME_SCRITCHUI_DISPATCH_DECL(containerRemoveAll);
	SJME_SCRITCHUI_DISPATCH_DECL(containerSetBounds);
	SJME_SCRITCHUI_DISPATCH_DECL(fontBuiltin);
	SJME_SCRITCHUI_DISPATCH_DECL(fontDerive);
	SJME_SCRITCHUI_DISPATCH_DECL(hardwareGraphics);
	SJME_SCRITCHUI_DISPATCH_DECL(panelNew);
	SJME_SCRITCHUI_DISPATCH_DECL(panelEnableFocus);
	SJME_SCRITCHUI_DISPATCH_DECL(screenSetListener);
	SJME_SCRITCHUI_DISPATCH_DECL(screens);
	SJME_SCRITCHUI_DISPATCH_DECL(windowContentMinimumSize);
	SJME_SCRITCHUI_DISPATCH_DECL(windowNew);
	SJME_SCRITCHUI_DISPATCH_DECL(windowSetCloseListener);
	SJME_SCRITCHUI_DISPATCH_DECL(windowSetVisible);
	sjme_scritchui state;
	
	if (anything == NULL)
		return SJME_THREAD_RESULT(SJME_ERROR_NULL_ARGUMENTS);
		
	/* Emit barrier so we can access this. */
	data = (sjme_scritchui_serialData*)anything;
	sjme_thread_barrier();
	
	/* Restore info. */
	state = data->state;
	
	/* Debug. */
	sjme_message("ScritchUI Serial: %d", data->type);
	
	/* Begin cases. */
	SJME_SCRITCHUI_DISPATCH_SWITCH_BEGIN

	/* Depends on the type... */
	SJME_SCRITCHUI_DISPATCH_CASE(componentRepaint,
		SJME_SCRITCHUI_SERIAL_TYPE_COMPONENT_REPAINT,
		(state,
		componentRepaint->inComponent,
		componentRepaint->x,
		componentRepaint->y,
		componentRepaint->width,
		componentRepaint->height));
	
	SJME_SCRITCHUI_DISPATCH_CASE(componentRevalidate,
		SJME_SCRITCHUI_SERIAL_TYPE_COMPONENT_REVALIDATE,
		(state,
		componentRevalidate->inComponent));
	
	SJME_SCRITCHUI_DISPATCH_CASE_LISTENER(componentSetInputListener,
		SJME_SCRITCHUI_SERIAL_TYPE_COMPONENT_SET_INPUT_LISTENER);
		
	SJME_SCRITCHUI_DISPATCH_CASE_LISTENER(componentSetPaintListener,
		SJME_SCRITCHUI_SERIAL_TYPE_COMPONENT_SET_PAINT_LISTENER);
	
	SJME_SCRITCHUI_DISPATCH_CASE_LISTENER(componentSetSizeListener,
		SJME_SCRITCHUI_SERIAL_TYPE_COMPONENT_SET_SIZE_LISTENER);
		
	SJME_SCRITCHUI_DISPATCH_CASE_LISTENER(componentSetVisibleListener,
		SJME_SCRITCHUI_SERIAL_TYPE_COMPONENT_SET_VISIBLE_LISTENER);
	
	SJME_SCRITCHUI_DISPATCH_CASE(componentSize,
		SJME_SCRITCHUI_SERIAL_TYPE_COMPONENT_SIZE,
		(state,
		componentSize->inComponent,
		componentSize->outWidth,
		componentSize->outHeight));
		
	SJME_SCRITCHUI_DISPATCH_CASE(containerAdd,
		SJME_SCRITCHUI_SERIAL_TYPE_CONTAINER_ADD,
		(state,
		containerAdd->inContainer,
		containerAdd->addComponent));
		
	SJME_SCRITCHUI_DISPATCH_CASE(containerRemove,
		SJME_SCRITCHUI_SERIAL_TYPE_CONTAINER_REMOVE,
		(state,
		containerRemove->inContainer,
		containerRemove->removeComponent));
		
	SJME_SCRITCHUI_DISPATCH_CASE(containerRemoveAll,
		SJME_SCRITCHUI_SERIAL_TYPE_CONTAINER_REMOVE_ALL,
		(state,
		containerAdd->inContainer));
		
	SJME_SCRITCHUI_DISPATCH_CASE(containerSetBounds,
		SJME_SCRITCHUI_SERIAL_TYPE_CONTAINER_SET_BOUNDS,
		(state,
		containerSetBounds->inContainer,
		containerSetBounds->inComponent,
		containerSetBounds->x,
		containerSetBounds->y,
		containerSetBounds->width,
		containerSetBounds->height));
	
	SJME_SCRITCHUI_DISPATCH_CASE(fontBuiltin,
		SJME_SCRITCHUI_SERIAL_TYPE_FONT_BUILTIN,
		(state,
		fontBuiltin->outFont));
	
	SJME_SCRITCHUI_DISPATCH_CASE(fontDerive,
		SJME_SCRITCHUI_SERIAL_TYPE_FONT_DERIVE,
		(state,
		fontDerive->inFont,
		fontDerive->inStyle,
		fontDerive->inPixelSize,
		fontDerive->outDerived));
		
	SJME_SCRITCHUI_DISPATCH_CASE(hardwareGraphics,
		SJME_SCRITCHUI_SERIAL_TYPE_HARDWARE_GRAPHICS,
		(state,
		hardwareGraphics->outPencil,
		hardwareGraphics->outWeakPencil,
		hardwareGraphics->pf,
		hardwareGraphics->bw,
		hardwareGraphics->bh,
		hardwareGraphics->inLockFuncs,
		hardwareGraphics->inLockFrontEndCopy,
		hardwareGraphics->sx,
		hardwareGraphics->sy,
		hardwareGraphics->sw,
		hardwareGraphics->sh,
		hardwareGraphics->pencilFrontEndCopy));

	SJME_SCRITCHUI_DISPATCH_CASE(panelEnableFocus,
		SJME_SCRITCHUI_SERIAL_TYPE_PANEL_ENABLE_FOCUS,
		(state,
		panelEnableFocus->inPanel,
		panelEnableFocus->enableFocus,
		panelEnableFocus->defaultFocus));

	SJME_SCRITCHUI_DISPATCH_CASE(panelNew,
		SJME_SCRITCHUI_SERIAL_TYPE_PANEL_NEW,
		(state,
		panelNew->outPanel));

	SJME_SCRITCHUI_DISPATCH_CASE(screenSetListener,
		SJME_SCRITCHUI_SERIAL_TYPE_SCREEN_SET_LISTENER,
		(state,
		screenSetListener->inListener,
		screenSetListener->copyFrontEnd));

	SJME_SCRITCHUI_DISPATCH_CASE(screens,
		SJME_SCRITCHUI_SERIAL_TYPE_SCREENS,
		(state,
		screens->outScreens,
		screens->inOutNumScreens));
	
	SJME_SCRITCHUI_DISPATCH_CASE(windowContentMinimumSize,
		SJME_SCRITCHUI_SERIAL_TYPE_WINDOW_CONTENT_MINIMUM_SIZE,
		(state,
		windowContentMinimumSize->inWindow,
		windowContentMinimumSize->width,
		windowContentMinimumSize->height));
			
	SJME_SCRITCHUI_DISPATCH_CASE(windowNew,
		SJME_SCRITCHUI_SERIAL_TYPE_WINDOW_NEW,
		(state,
		windowNew->outWindow));

	SJME_SCRITCHUI_DISPATCH_CASE(windowSetCloseListener,
		SJME_SCRITCHUI_SERIAL_TYPE_WINDOW_SET_CLOSE_LISTENER,
		(state,
		windowSetCloseListener->inWindow,
		windowSetCloseListener->inListener,
		windowSetCloseListener->copyFrontEnd));
		
	SJME_SCRITCHUI_DISPATCH_CASE(windowSetVisible,
		SJME_SCRITCHUI_SERIAL_TYPE_WINDOW_SET_VISIBLE,
		(state,
		windowSetVisible->inWindow,
		windowSetVisible->isVisible));
	
	/* End. */
	SJME_SCRITCHUI_DISPATCH_SWITCH_END
	
	/* Map result. */
	return SJME_THREAD_RESULT(data->error);

#undef SJME_SCRITCHUI_DISPATCH_SWITCH_BEGIN
#undef SJME_SCRITCHUI_DISPATCH_SWITCH_END
}

/** Generic listener dispatch. */
#define SJME_SCRITCHUI_DISPATCH_GENERIC_LISTENER(id, idCode, type, arg, \
	listener) \
	sjme_errorCode SJME_TOKEN_PASTE_PP(sjme_scritchui_coreSerial_, id)( \
		sjme_attrInNotNull sjme_scritchui inState, \
		sjme_attrInNotNull type arg, \
		SJME_SCRITCHUI_SET_LISTENER_ARGS(listener)) \
	{ \
		SJME_SCRITCHUI_SERIAL_CHUNK(id, \
			idCode, \
			(inState, arg, inListener, copyFrontEnd)); \
		 \
		SJME_SCRITCHUI_SERIAL_PASS(arg); \
		SJME_SCRITCHUI_SERIAL_PASS(inListener); \
		SJME_SCRITCHUI_SERIAL_PASS(copyFrontEnd); \
		 \
		/* Invoke and wait. */ \
		SJME_SCRITCHUI_INVOKE_WAIT; \
	}

sjme_errorCode sjme_scritchui_coreSerial_componentRepaint(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint x,
	sjme_attrInPositive sjme_jint y,
	sjme_attrInPositiveNonZero sjme_jint width,
	sjme_attrInPositiveNonZero sjme_jint height)
{
	SJME_SCRITCHUI_SERIAL_CHUNK(componentRepaint,
		SJME_SCRITCHUI_SERIAL_TYPE_COMPONENT_REPAINT,
		(inState, inComponent, x, y, width, height));
		
	SJME_SCRITCHUI_SERIAL_PASS(inComponent);
	SJME_SCRITCHUI_SERIAL_PASS(x);
	SJME_SCRITCHUI_SERIAL_PASS(y);
	SJME_SCRITCHUI_SERIAL_PASS(width);
	SJME_SCRITCHUI_SERIAL_PASS(height);
	
	/* Invoke and wait. */
	SJME_SCRITCHUI_INVOKE_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_componentRevalidate(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent)
{
	SJME_SCRITCHUI_SERIAL_CHUNK(componentRevalidate,
		SJME_SCRITCHUI_SERIAL_TYPE_COMPONENT_REVALIDATE,
		(inState, inComponent));
		
	SJME_SCRITCHUI_SERIAL_PASS(inComponent);
	
	/* Invoke and wait. */
	SJME_SCRITCHUI_INVOKE_WAIT;
}

SJME_SCRITCHUI_DISPATCH_GENERIC_LISTENER(componentSetInputListener,
	SJME_SCRITCHUI_SERIAL_TYPE_COMPONENT_SET_INPUT_LISTENER,
	sjme_scritchui_uiComponent, inComponent, input)

SJME_SCRITCHUI_DISPATCH_GENERIC_LISTENER(componentSetPaintListener,
	SJME_SCRITCHUI_SERIAL_TYPE_COMPONENT_SET_PAINT_LISTENER,
	sjme_scritchui_uiComponent, inComponent, paint)

SJME_SCRITCHUI_DISPATCH_GENERIC_LISTENER(componentSetSizeListener,
	SJME_SCRITCHUI_SERIAL_TYPE_COMPONENT_SET_SIZE_LISTENER,
	sjme_scritchui_uiComponent, inComponent, size)

SJME_SCRITCHUI_DISPATCH_GENERIC_LISTENER(componentSetVisibleListener,
	SJME_SCRITCHUI_SERIAL_TYPE_COMPONENT_SET_VISIBLE_LISTENER,
	sjme_scritchui_uiComponent, inComponent, visible)

sjme_errorCode sjme_scritchui_coreSerial_componentSize(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrOutNullable sjme_jint* outWidth,
	sjme_attrOutNullable sjme_jint* outHeight)
{
	SJME_SCRITCHUI_SERIAL_CHUNK(componentSize,
		SJME_SCRITCHUI_SERIAL_TYPE_COMPONENT_SIZE,
		(inState, inComponent, outWidth, outHeight));
		
	SJME_SCRITCHUI_SERIAL_PASS(inComponent);
	SJME_SCRITCHUI_SERIAL_PASS(outWidth);
	SJME_SCRITCHUI_SERIAL_PASS(outHeight);
	
	/* Invoke and wait. */
	SJME_SCRITCHUI_INVOKE_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_containerAdd(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inContainer,
	sjme_attrInNotNull sjme_scritchui_uiComponent addComponent)
{
	SJME_SCRITCHUI_SERIAL_CHUNK(containerAdd,
		SJME_SCRITCHUI_SERIAL_TYPE_CONTAINER_ADD,
		(inState, inContainer, addComponent));
		
	SJME_SCRITCHUI_SERIAL_PASS(inContainer);
	SJME_SCRITCHUI_SERIAL_PASS(addComponent);
	
	/* Invoke and wait. */
	SJME_SCRITCHUI_INVOKE_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_containerRemove(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inContainer,
	sjme_attrInNotNull sjme_scritchui_uiComponent removeComponent)
{
	SJME_SCRITCHUI_SERIAL_CHUNK(containerRemove,
		SJME_SCRITCHUI_SERIAL_TYPE_CONTAINER_REMOVE,
		(inState, inContainer, removeComponent));
		
	SJME_SCRITCHUI_SERIAL_PASS(inContainer);
	SJME_SCRITCHUI_SERIAL_PASS(removeComponent);
	
	/* Invoke and wait. */
	SJME_SCRITCHUI_INVOKE_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_containerRemoveAll(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inContainer)
{
	SJME_SCRITCHUI_SERIAL_CHUNK(containerRemoveAll,
		SJME_SCRITCHUI_SERIAL_TYPE_CONTAINER_REMOVE_ALL,
		(inState, inContainer));
		
	SJME_SCRITCHUI_SERIAL_PASS(inContainer);
	
	/* Invoke and wait. */
	SJME_SCRITCHUI_INVOKE_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_containerSetBounds(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inContainer,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint x,
	sjme_attrInPositive sjme_jint y,
	sjme_attrInPositiveNonZero sjme_jint width,
	sjme_attrInPositiveNonZero sjme_jint height)
{
	SJME_SCRITCHUI_SERIAL_CHUNK(containerSetBounds,
		SJME_SCRITCHUI_SERIAL_TYPE_CONTAINER_SET_BOUNDS,
		(inState, inContainer, inComponent, x, y, width, height));
		
	SJME_SCRITCHUI_SERIAL_PASS(inContainer);
	SJME_SCRITCHUI_SERIAL_PASS(inComponent);
	SJME_SCRITCHUI_SERIAL_PASS(x);
	SJME_SCRITCHUI_SERIAL_PASS(y);
	SJME_SCRITCHUI_SERIAL_PASS(width);
	SJME_SCRITCHUI_SERIAL_PASS(height);
	
	/* Invoke and wait. */
	SJME_SCRITCHUI_INVOKE_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_fontBuiltin(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrOutNotNull sjme_scritchui_pencilFont* outFont)
{
	SJME_SCRITCHUI_SERIAL_CHUNK(fontBuiltin,
		SJME_SCRITCHUI_SERIAL_TYPE_FONT_BUILTIN,
		(inState, outFont));
		
	SJME_SCRITCHUI_SERIAL_PASS(outFont);
	
	/* Invoke and wait. */
	SJME_SCRITCHUI_INVOKE_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_fontDerive(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInValue sjme_scritchui_pencilFontStyle inStyle,
	sjme_attrInPositiveNonZero sjme_jint inPixelSize,
	sjme_attrOutNotNull sjme_scritchui_pencilFont* outDerived)
{
	SJME_SCRITCHUI_SERIAL_CHUNK(fontDerive,
		SJME_SCRITCHUI_SERIAL_TYPE_FONT_DERIVE,
		(inState, inFont, inStyle, inPixelSize, outDerived));
		
	SJME_SCRITCHUI_SERIAL_PASS(inFont);
	SJME_SCRITCHUI_SERIAL_PASS(inStyle);
	SJME_SCRITCHUI_SERIAL_PASS(inPixelSize);
	SJME_SCRITCHUI_SERIAL_PASS(outDerived);
	
	/* Invoke and wait. */
	SJME_SCRITCHUI_INVOKE_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_hardwareGraphics(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrOutNotNull sjme_scritchui_pencil* outPencil,
	sjme_attrOutNullable sjme_alloc_weak* outWeakPencil,
	sjme_attrInValue sjme_gfx_pixelFormat pf,
	sjme_attrInPositiveNonZero sjme_jint bw,
	sjme_attrInPositiveNonZero sjme_jint bh,
	sjme_attrInNullable const sjme_scritchui_pencilLockFunctions* inLockFuncs,
	sjme_attrInNullable const sjme_frontEnd* inLockFrontEndCopy,
	sjme_attrInValue sjme_jint sx,
	sjme_attrInValue sjme_jint sy,
	sjme_attrInPositiveNonZero sjme_jint sw,
	sjme_attrInPositiveNonZero sjme_jint sh,
	sjme_attrInNullable const sjme_frontEnd* pencilFrontEndCopy)
{
	SJME_SCRITCHUI_SERIAL_CHUNK(hardwareGraphics,
		SJME_SCRITCHUI_SERIAL_TYPE_HARDWARE_GRAPHICS,
		(inState, outPencil, outWeakPencil, pf, bw, bh,
			inLockFuncs, inLockFrontEndCopy,
			sx, sy, sw, sh, pencilFrontEndCopy));
		
	SJME_SCRITCHUI_SERIAL_PASS(outPencil);
	SJME_SCRITCHUI_SERIAL_PASS(outWeakPencil);
	SJME_SCRITCHUI_SERIAL_PASS(pf);
	SJME_SCRITCHUI_SERIAL_PASS(bw);
	SJME_SCRITCHUI_SERIAL_PASS(bh);
	SJME_SCRITCHUI_SERIAL_PASS(inLockFuncs);
	SJME_SCRITCHUI_SERIAL_PASS(inLockFrontEndCopy);
	SJME_SCRITCHUI_SERIAL_PASS(sx);
	SJME_SCRITCHUI_SERIAL_PASS(sy);
	SJME_SCRITCHUI_SERIAL_PASS(sw);
	SJME_SCRITCHUI_SERIAL_PASS(sh);
	SJME_SCRITCHUI_SERIAL_PASS(pencilFrontEndCopy);
	
	/* Invoke and wait. */
	SJME_SCRITCHUI_INVOKE_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_listNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_uiList* outList)
{
	SJME_SCRITCHUI_SERIAL_CHUNK(listNew,
		SJME_SCRITCHUI_SERIAL_TYPE_LIST_NEW,
		(inState, outList));
		
	SJME_SCRITCHUI_SERIAL_PASS(outList);
	
	/* Invoke and wait. */
	SJME_SCRITCHUI_INVOKE_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_panelEnableFocus(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiPanel inPanel,
	sjme_attrInValue sjme_jboolean enableFocus,
	sjme_attrInValue sjme_jboolean defaultFocus)
{
	SJME_SCRITCHUI_SERIAL_CHUNK(panelEnableFocus,
		SJME_SCRITCHUI_SERIAL_TYPE_PANEL_ENABLE_FOCUS,
		(inState, inPanel, enableFocus, defaultFocus));
		
	SJME_SCRITCHUI_SERIAL_PASS(inPanel);
	SJME_SCRITCHUI_SERIAL_PASS(enableFocus);
	SJME_SCRITCHUI_SERIAL_PASS(defaultFocus);
	
	/* Invoke and wait. */
	SJME_SCRITCHUI_INVOKE_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_panelNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_uiPanel* outPanel)
{
	SJME_SCRITCHUI_SERIAL_CHUNK(panelNew,
		SJME_SCRITCHUI_SERIAL_TYPE_PANEL_NEW,
		(inState, outPanel));
		
	SJME_SCRITCHUI_SERIAL_PASS(outPanel);
	
	/* Invoke and wait. */
	SJME_SCRITCHUI_INVOKE_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_screenSetListener(
	sjme_attrInNotNull sjme_scritchui inState,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(screen))
{
	SJME_SCRITCHUI_SERIAL_CHUNK(screenSetListener,
		SJME_SCRITCHUI_SERIAL_TYPE_SCREEN_SET_LISTENER,
		(inState, inListener, copyFrontEnd));
		
	SJME_SCRITCHUI_SERIAL_PASS(inListener);
	SJME_SCRITCHUI_SERIAL_PASS(copyFrontEnd);
	
	/* Invoke and wait. */
	SJME_SCRITCHUI_INVOKE_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_screens(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrOutNotNull sjme_scritchui_uiScreen* outScreens,
	sjme_attrInOutNotNull sjme_jint* inOutNumScreens)
{
	SJME_SCRITCHUI_SERIAL_CHUNK(screens,
		SJME_SCRITCHUI_SERIAL_TYPE_SCREENS,
		(inState, outScreens, inOutNumScreens));
		
	SJME_SCRITCHUI_SERIAL_PASS(outScreens);
	SJME_SCRITCHUI_SERIAL_PASS(inOutNumScreens);
	
	/* Invoke and wait. */
	SJME_SCRITCHUI_INVOKE_WAIT;
}
	
sjme_errorCode sjme_scritchui_coreSerial_windowContentMinimumSize(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInPositiveNonZero sjme_jint width,
	sjme_attrInPositiveNonZero sjme_jint height)
{
	SJME_SCRITCHUI_SERIAL_CHUNK(windowContentMinimumSize,
		SJME_SCRITCHUI_SERIAL_TYPE_WINDOW_CONTENT_MINIMUM_SIZE,
		(inState, inWindow, width, height));
		
	SJME_SCRITCHUI_SERIAL_PASS(inWindow);
	SJME_SCRITCHUI_SERIAL_PASS(width);
	SJME_SCRITCHUI_SERIAL_PASS(height);
	
	/* Invoke and wait. */
	SJME_SCRITCHUI_INVOKE_WAIT;
}
	
sjme_errorCode sjme_scritchui_coreSerial_windowNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_uiWindow* outWindow)
{
	SJME_SCRITCHUI_SERIAL_CHUNK(windowNew,
		SJME_SCRITCHUI_SERIAL_TYPE_WINDOW_NEW,
		(inState, outWindow));
		
	SJME_SCRITCHUI_SERIAL_PASS(outWindow);
	
	/* Invoke and wait. */
	SJME_SCRITCHUI_INVOKE_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_windowSetCloseListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(close))
{
	SJME_SCRITCHUI_SERIAL_CHUNK(windowSetCloseListener,
		SJME_SCRITCHUI_SERIAL_TYPE_WINDOW_SET_CLOSE_LISTENER,
		(inState, inWindow, inListener, copyFrontEnd));
		
	SJME_SCRITCHUI_SERIAL_PASS(inWindow);
	SJME_SCRITCHUI_SERIAL_PASS(inListener);
	SJME_SCRITCHUI_SERIAL_PASS(copyFrontEnd);
	
	/* Invoke and wait. */
	SJME_SCRITCHUI_INVOKE_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_windowSetVisible(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInValue sjme_jboolean isVisible)
{
	SJME_SCRITCHUI_SERIAL_CHUNK(windowSetVisible,
		SJME_SCRITCHUI_SERIAL_TYPE_WINDOW_SET_VISIBLE,
		(inState, inWindow, isVisible));
		
	SJME_SCRITCHUI_SERIAL_PASS(inWindow);
	SJME_SCRITCHUI_SERIAL_PASS(isVisible);
	
	/* Invoke and wait. */
	SJME_SCRITCHUI_INVOKE_WAIT;
}

