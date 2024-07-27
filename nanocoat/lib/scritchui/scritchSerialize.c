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
	do { /*as.what = &data->data.what;*/ \
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
		as->what.inComponent, \
		as->what.inListener, \
		as->what.copyFrontEnd))

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
	sjme_scritchui_serialDataUnion* as;
	sjme_scritchui state;
	
	if (anything == NULL)
		return SJME_THREAD_RESULT(SJME_ERROR_NULL_ARGUMENTS);
		
	/* Emit barrier so we can access this. */
	data = (sjme_scritchui_serialData*)anything;
	sjme_thread_barrier();
	
	/* Restore info. */
	state = data->state;
	as = &data->data;
	
	/* Debug. */
	sjme_message("ScritchUI Serial: %d", data->type);
	
	/* Begin cases. */
	SJME_SCRITCHUI_DISPATCH_SWITCH_BEGIN
	
	SJME_SCRITCHUI_DISPATCH_CASE(choiceItemGet,
		SJME_SCRITCHUI_SERIAL_TYPE_CHOICE_ITEM_GET,
		(state,
		as->choiceItemGet.inComponent,
		as->choiceItemGet.atIndex,
		as->choiceItemGet.outItemTemplate));
	
	SJME_SCRITCHUI_DISPATCH_CASE(choiceItemInsert,
		SJME_SCRITCHUI_SERIAL_TYPE_CHOICE_ITEM_INSERT,
		(state,
		as->choiceItemInsert.inComponent,
		as->choiceItemInsert.inOutIndex));
		
	SJME_SCRITCHUI_DISPATCH_CASE(choiceItemRemove,
		SJME_SCRITCHUI_SERIAL_TYPE_CHOICE_ITEM_REMOVE,
		(state,
		as->choiceItemRemove.inComponent,
		as->choiceItemRemove.atIndex));
		
	SJME_SCRITCHUI_DISPATCH_CASE(choiceItemRemoveAll,
		SJME_SCRITCHUI_SERIAL_TYPE_CHOICE_ITEM_REMOVE_ALL,
		(state,
		as->choiceItemRemoveAll.inComponent));
		
	SJME_SCRITCHUI_DISPATCH_CASE(choiceItemSetEnabled,
		SJME_SCRITCHUI_SERIAL_TYPE_CHOICE_ITEM_SET_ENABLED,
		(state,
		as->choiceItemSetEnabled.inComponent,
		as->choiceItemSetEnabled.atIndex,
		as->choiceItemSetEnabled.isEnabled));
		
	SJME_SCRITCHUI_DISPATCH_CASE(choiceItemSetImage,
		SJME_SCRITCHUI_SERIAL_TYPE_CHOICE_ITEM_SET_IMAGE,
		(state,
		as->choiceItemSetImage.inComponent,
		as->choiceItemSetImage.atIndex,
		as->choiceItemSetImage.inRgb,
		as->choiceItemSetImage.inRgbOff,
		as->choiceItemSetImage.inRgbDataLen,
		as->choiceItemSetImage.inRgbScanLen,
		as->choiceItemSetImage.width,
		as->choiceItemSetImage.height));
		
	SJME_SCRITCHUI_DISPATCH_CASE(choiceItemSetSelected,
		SJME_SCRITCHUI_SERIAL_TYPE_CHOICE_ITEM_SET_SELECTED,
		(state,
		as->choiceItemSetSelected.inComponent,
		as->choiceItemSetSelected.atIndex,
		as->choiceItemSetSelected.isSelected));
		
	SJME_SCRITCHUI_DISPATCH_CASE(choiceItemSetString,
		SJME_SCRITCHUI_SERIAL_TYPE_CHOICE_ITEM_SET_STRING,
		(state,
		as->choiceItemSetString.inComponent,
		as->choiceItemSetString.atIndex,
		as->choiceItemSetString.inString));
		
	SJME_SCRITCHUI_DISPATCH_CASE(choiceLength,
		SJME_SCRITCHUI_SERIAL_TYPE_CHOICE_LENGTH,
		(state,
		as->choiceLength.inComponent,
		as->choiceLength.outLength));
	
	SJME_SCRITCHUI_DISPATCH_CASE(componentFocusGrab,
		SJME_SCRITCHUI_SERIAL_TYPE_COMPONENT_FOCUS_GRAB,
		(state,
		as->componentFocusGrab.inComponent));
	
	SJME_SCRITCHUI_DISPATCH_CASE(componentFocusHas,
		SJME_SCRITCHUI_SERIAL_TYPE_COMPONENT_FOCUS_HAS,
		(state,
		as->componentFocusHas.inComponent,
		as->componentFocusHas.outHasFocus));
	
	SJME_SCRITCHUI_DISPATCH_CASE(componentRepaint,
		SJME_SCRITCHUI_SERIAL_TYPE_COMPONENT_REPAINT,
		(state,
		as->componentRepaint.inComponent,
		as->componentRepaint.x,
		as->componentRepaint.y,
		as->componentRepaint.width,
		as->componentRepaint.height));
	
	SJME_SCRITCHUI_DISPATCH_CASE(componentRevalidate,
		SJME_SCRITCHUI_SERIAL_TYPE_COMPONENT_REVALIDATE,
		(state,
		as->componentRevalidate.inComponent));
		
	SJME_SCRITCHUI_DISPATCH_CASE(componentSetActivateListener,
		SJME_SCRITCHUI_SERIAL_TYPE_CHOICE_SET_ACTIVATE_LISTENER,
		(state,
		as->componentSetActivateListener.inComponent,
		as->componentSetActivateListener.inListener,
		as->componentSetActivateListener.copyFrontEnd));
	
	SJME_SCRITCHUI_DISPATCH_CASE_LISTENER(componentSetInputListener,
		SJME_SCRITCHUI_SERIAL_TYPE_COMPONENT_SET_INPUT_LISTENER);
		
	SJME_SCRITCHUI_DISPATCH_CASE_LISTENER(componentSetPaintListener,
		SJME_SCRITCHUI_SERIAL_TYPE_COMPONENT_SET_PAINT_LISTENER);
	
	SJME_SCRITCHUI_DISPATCH_CASE_LISTENER(componentSetSizeListener,
		SJME_SCRITCHUI_SERIAL_TYPE_COMPONENT_SET_SIZE_LISTENER);
		
	SJME_SCRITCHUI_DISPATCH_CASE(componentSetValueUpdateListener,
		SJME_SCRITCHUI_SERIAL_TYPE_COMPONENT_SET_VALUE_UPDATE_LISTENER,
		(state,
		as->componentSetValueUpdateListener.inComponent,
		as->componentSetValueUpdateListener.inListener,
		as->componentSetValueUpdateListener.copyFrontEnd));
		
	SJME_SCRITCHUI_DISPATCH_CASE_LISTENER(componentSetVisibleListener,
		SJME_SCRITCHUI_SERIAL_TYPE_COMPONENT_SET_VISIBLE_LISTENER);
	
	SJME_SCRITCHUI_DISPATCH_CASE(componentSize,
		SJME_SCRITCHUI_SERIAL_TYPE_COMPONENT_SIZE,
		(state,
		as->componentSize.inComponent,
		as->componentSize.outWidth,
		as->componentSize.outHeight));
		
	SJME_SCRITCHUI_DISPATCH_CASE(containerAdd,
		SJME_SCRITCHUI_SERIAL_TYPE_CONTAINER_ADD,
		(state,
		as->containerAdd.inContainer,
		as->containerAdd.addComponent));
		
	SJME_SCRITCHUI_DISPATCH_CASE(containerRemove,
		SJME_SCRITCHUI_SERIAL_TYPE_CONTAINER_REMOVE,
		(state,
		as->containerRemove.inContainer,
		as->containerRemove.removeComponent));
		
	SJME_SCRITCHUI_DISPATCH_CASE(containerRemoveAll,
		SJME_SCRITCHUI_SERIAL_TYPE_CONTAINER_REMOVE_ALL,
		(state,
		as->containerAdd.inContainer));
		
	SJME_SCRITCHUI_DISPATCH_CASE(containerSetBounds,
		SJME_SCRITCHUI_SERIAL_TYPE_CONTAINER_SET_BOUNDS,
		(state,
		as->containerSetBounds.inContainer,
		as->containerSetBounds.inComponent,
		as->containerSetBounds.x,
		as->containerSetBounds.y,
		as->containerSetBounds.width,
		as->containerSetBounds.height));
	
	SJME_SCRITCHUI_DISPATCH_CASE(fontBuiltin,
		SJME_SCRITCHUI_SERIAL_TYPE_FONT_BUILTIN,
		(state,
		as->fontBuiltin.outFont));
	
	SJME_SCRITCHUI_DISPATCH_CASE(fontDerive,
		SJME_SCRITCHUI_SERIAL_TYPE_FONT_DERIVE,
		(state,
		as->fontDerive.inFont,
		as->fontDerive.inStyle,
		as->fontDerive.inPixelSize,
		as->fontDerive.outDerived));
		
	SJME_SCRITCHUI_DISPATCH_CASE(hardwareGraphics,
		SJME_SCRITCHUI_SERIAL_TYPE_HARDWARE_GRAPHICS,
		(state,
		as->hardwareGraphics.outPencil,
		as->hardwareGraphics.outWeakPencil,
		as->hardwareGraphics.pf,
		as->hardwareGraphics.bw,
		as->hardwareGraphics.bh,
		as->hardwareGraphics.inLockFuncs,
		as->hardwareGraphics.inLockFrontEndCopy,
		as->hardwareGraphics.sx,
		as->hardwareGraphics.sy,
		as->hardwareGraphics.sw,
		as->hardwareGraphics.sh,
		as->hardwareGraphics.pencilFrontEndCopy));
		
	SJME_SCRITCHUI_DISPATCH_CASE(labelSetString,
		SJME_SCRITCHUI_SERIAL_TYPE_LABEL_SET_STRING,
		(state,
		as->labelSetString.inComponent,
		as->labelSetString.inString));
		
	SJME_SCRITCHUI_DISPATCH_CASE(lafElementColor,
		SJME_SCRITCHUI_SERIAL_TYPE_LAF_ELEMENT_COLOR,
		(state,
		as->lafElementColor.inContext,
		as->lafElementColor.outRGB,
		as->lafElementColor.elementColor));

	SJME_SCRITCHUI_DISPATCH_CASE(listNew,
		SJME_SCRITCHUI_SERIAL_TYPE_LIST_NEW,
		(state,
		as->listNew.outList,
		as->listNew.inChoiceType));
			
	SJME_SCRITCHUI_DISPATCH_CASE(menuBarNew,
		SJME_SCRITCHUI_SERIAL_TYPE_MENU_BAR_NEW,
		(state,
		as->menuBarNew.outMenuBar));
			
	SJME_SCRITCHUI_DISPATCH_CASE(menuInsert,
		SJME_SCRITCHUI_SERIAL_TYPE_MENU_INSERT,
		(state,
		as->menuInsert.intoMenu,
		as->menuInsert.atIndex,
		as->menuInsert.childItem));
			
	SJME_SCRITCHUI_DISPATCH_CASE(menuItemNew,
		SJME_SCRITCHUI_SERIAL_TYPE_MENU_ITEM_NEW,
		(state,
		as->menuItemNew.outMenuItem));
			
	SJME_SCRITCHUI_DISPATCH_CASE(menuNew,
		SJME_SCRITCHUI_SERIAL_TYPE_MENU_NEW,
		(state,
		as->menuNew.outMenu));
			
	SJME_SCRITCHUI_DISPATCH_CASE(menuRemove,
		SJME_SCRITCHUI_SERIAL_TYPE_MENU_REMOVE,
		(state,
		as->menuRemove.fromMenu,
		as->menuRemove.atIndex));
			
	SJME_SCRITCHUI_DISPATCH_CASE(menuRemoveAll,
		SJME_SCRITCHUI_SERIAL_TYPE_MENU_REMOVE_ALL,
		(state,
		as->menuRemoveAll.fromMenu));
		
	SJME_SCRITCHUI_DISPATCH_CASE(objectDelete,
		SJME_SCRITCHUI_SERIAL_TYPE_OBJECT_DELETE,
		(state,
		as->objectDelete.inOutObject));
	
	SJME_SCRITCHUI_DISPATCH_CASE(panelEnableFocus,
		SJME_SCRITCHUI_SERIAL_TYPE_PANEL_ENABLE_FOCUS,
		(state,
		as->panelEnableFocus.inPanel,
		as->panelEnableFocus.enableFocus,
		as->panelEnableFocus.defaultFocus));

	SJME_SCRITCHUI_DISPATCH_CASE(panelNew,
		SJME_SCRITCHUI_SERIAL_TYPE_PANEL_NEW,
		(state,
		as->panelNew.outPanel));

	SJME_SCRITCHUI_DISPATCH_CASE(screenSetListener,
		SJME_SCRITCHUI_SERIAL_TYPE_SCREEN_SET_LISTENER,
		(state,
		as->screenSetListener.inListener,
		as->screenSetListener.copyFrontEnd));

	SJME_SCRITCHUI_DISPATCH_CASE(screens,
		SJME_SCRITCHUI_SERIAL_TYPE_SCREENS,
		(state,
		as->screens.outScreens,
		as->screens.inOutNumScreens));
	
	SJME_SCRITCHUI_DISPATCH_CASE(windowContentMinimumSize,
		SJME_SCRITCHUI_SERIAL_TYPE_WINDOW_CONTENT_MINIMUM_SIZE,
		(state,
		as->windowContentMinimumSize.inWindow,
		as->windowContentMinimumSize.width,
		as->windowContentMinimumSize.height));
			
	SJME_SCRITCHUI_DISPATCH_CASE(windowNew,
		SJME_SCRITCHUI_SERIAL_TYPE_WINDOW_NEW,
		(state,
		as->windowNew.outWindow));

	SJME_SCRITCHUI_DISPATCH_CASE(windowSetCloseListener,
		SJME_SCRITCHUI_SERIAL_TYPE_WINDOW_SET_CLOSE_LISTENER,
		(state,
		as->windowSetCloseListener.inWindow,
		as->windowSetCloseListener.inListener,
		as->windowSetCloseListener.copyFrontEnd));

	SJME_SCRITCHUI_DISPATCH_CASE(windowSetMenuBar,
		SJME_SCRITCHUI_SERIAL_TYPE_WINDOW_SET_MENU_BAR,
		(state,
		as->windowSetMenuBar.inWindow,
		as->windowSetMenuBar.inMenuBar));
		
	SJME_SCRITCHUI_DISPATCH_CASE(windowSetVisible,
		SJME_SCRITCHUI_SERIAL_TYPE_WINDOW_SET_VISIBLE,
		(state,
		as->windowSetVisible.inWindow,
		as->windowSetVisible.isVisible));
	
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

/* ------------------------------------------------------------------------ */

sjme_errorCode sjme_scritchui_coreSerial_choiceItemGet(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint atIndex,
	sjme_attrOutNotNull sjme_scritchui_uiChoiceItem outItemTemplate)
{
	SJME_SCRITCHUI_SERIAL_CHUNK(choiceItemGet,
		SJME_SCRITCHUI_SERIAL_TYPE_CHOICE_ITEM_GET,
		(inState, inComponent, atIndex, outItemTemplate));
		
	SJME_SCRITCHUI_SERIAL_PASS(inComponent);
	SJME_SCRITCHUI_SERIAL_PASS(atIndex);
	SJME_SCRITCHUI_SERIAL_PASS(outItemTemplate);
	
	/* Invoke and wait. */
	SJME_SCRITCHUI_INVOKE_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_choiceItemInsert(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInOutNotNull sjme_jint* inOutIndex)
{
	SJME_SCRITCHUI_SERIAL_CHUNK(choiceItemInsert,
		SJME_SCRITCHUI_SERIAL_TYPE_CHOICE_ITEM_INSERT,
		(inState, inComponent, inOutIndex));
		
	SJME_SCRITCHUI_SERIAL_PASS(inComponent);
	SJME_SCRITCHUI_SERIAL_PASS(inOutIndex);
	
	/* Invoke and wait. */
	SJME_SCRITCHUI_INVOKE_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_choiceItemRemove(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint atIndex)
{
	SJME_SCRITCHUI_SERIAL_CHUNK(choiceItemRemove,
		SJME_SCRITCHUI_SERIAL_TYPE_CHOICE_ITEM_REMOVE,
		(inState, inComponent, atIndex));
		
	SJME_SCRITCHUI_SERIAL_PASS(inComponent);
	SJME_SCRITCHUI_SERIAL_PASS(atIndex);
	
	/* Invoke and wait. */
	SJME_SCRITCHUI_INVOKE_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_choiceItemRemoveAll(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent)
{
	SJME_SCRITCHUI_SERIAL_CHUNK(choiceItemRemoveAll,
		SJME_SCRITCHUI_SERIAL_TYPE_CHOICE_ITEM_REMOVE_ALL,
		(inState, inComponent));
		
	SJME_SCRITCHUI_SERIAL_PASS(inComponent);
	
	/* Invoke and wait. */
	SJME_SCRITCHUI_INVOKE_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_choiceItemSetEnabled(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint atIndex,
	sjme_attrInNotNull sjme_jboolean isEnabled)
{
	SJME_SCRITCHUI_SERIAL_CHUNK(choiceItemSetEnabled,
		SJME_SCRITCHUI_SERIAL_TYPE_CHOICE_ITEM_SET_ENABLED,
		(inState, inComponent, atIndex, isEnabled));
		
	SJME_SCRITCHUI_SERIAL_PASS(inComponent);
	SJME_SCRITCHUI_SERIAL_PASS(atIndex);
	SJME_SCRITCHUI_SERIAL_PASS(isEnabled);
	
	/* Invoke and wait. */
	SJME_SCRITCHUI_INVOKE_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_choiceItemSetImage(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint atIndex,
	sjme_attrInNullable sjme_jint* inRgb,
	sjme_attrInPositive sjme_jint inRgbOff,
	sjme_attrInPositiveNonZero sjme_jint inRgbDataLen,
	sjme_attrInPositiveNonZero sjme_jint inRgbScanLen,
	sjme_attrInPositiveNonZero sjme_jint width,
	sjme_attrInPositiveNonZero sjme_jint height)
{
	SJME_SCRITCHUI_SERIAL_CHUNK(choiceItemSetImage,
		SJME_SCRITCHUI_SERIAL_TYPE_CHOICE_ITEM_SET_IMAGE,
		(inState, inComponent, atIndex, inRgb, inRgbOff, inRgbDataLen,
			inRgbScanLen, width, height));
		
	SJME_SCRITCHUI_SERIAL_PASS(inComponent);
	SJME_SCRITCHUI_SERIAL_PASS(atIndex);
	SJME_SCRITCHUI_SERIAL_PASS(inRgb);
	SJME_SCRITCHUI_SERIAL_PASS(inRgbOff);
	SJME_SCRITCHUI_SERIAL_PASS(inRgbDataLen);
	SJME_SCRITCHUI_SERIAL_PASS(inRgbScanLen);
	SJME_SCRITCHUI_SERIAL_PASS(width);
	SJME_SCRITCHUI_SERIAL_PASS(height);
	
	/* Invoke and wait. */
	SJME_SCRITCHUI_INVOKE_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_choiceItemSetSelected(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint atIndex,
	sjme_attrInNotNull sjme_jboolean isSelected)
{
	SJME_SCRITCHUI_SERIAL_CHUNK(choiceItemSetSelected,
		SJME_SCRITCHUI_SERIAL_TYPE_CHOICE_ITEM_SET_SELECTED,
		(inState, inComponent, atIndex, isSelected));
		
	SJME_SCRITCHUI_SERIAL_PASS(inComponent);
	SJME_SCRITCHUI_SERIAL_PASS(atIndex);
	SJME_SCRITCHUI_SERIAL_PASS(isSelected);
	
	/* Invoke and wait. */
	SJME_SCRITCHUI_INVOKE_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_choiceItemSetString(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint atIndex,
	sjme_attrInNullable sjme_lpcstr inString)
{
	SJME_SCRITCHUI_SERIAL_CHUNK(choiceItemSetString,
		SJME_SCRITCHUI_SERIAL_TYPE_CHOICE_ITEM_SET_STRING,
		(inState, inComponent, atIndex, inString));
		
	SJME_SCRITCHUI_SERIAL_PASS(inComponent);
	SJME_SCRITCHUI_SERIAL_PASS(atIndex);
	SJME_SCRITCHUI_SERIAL_PASS(inString);
	
	/* Invoke and wait. */
	SJME_SCRITCHUI_INVOKE_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_choiceLength(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrOutNotNull sjme_jint* outLength)
{
	SJME_SCRITCHUI_SERIAL_CHUNK(choiceLength,
		SJME_SCRITCHUI_SERIAL_TYPE_CHOICE_LENGTH,
		(inState, inComponent, outLength));
		
	SJME_SCRITCHUI_SERIAL_PASS(inComponent);
	SJME_SCRITCHUI_SERIAL_PASS(outLength);
	
	/* Invoke and wait. */
	SJME_SCRITCHUI_INVOKE_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_componentFocusGrab(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent)
{
	SJME_SCRITCHUI_SERIAL_CHUNK(componentFocusGrab,
		SJME_SCRITCHUI_SERIAL_TYPE_COMPONENT_FOCUS_GRAB,
		(inState, inComponent));
		
	SJME_SCRITCHUI_SERIAL_PASS(inComponent);
	
	/* Invoke and wait. */
	SJME_SCRITCHUI_INVOKE_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_componentFocusHas(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrOutNotNull sjme_jboolean* outHasFocus)
{
	SJME_SCRITCHUI_SERIAL_CHUNK(componentFocusHas,
		SJME_SCRITCHUI_SERIAL_TYPE_COMPONENT_FOCUS_HAS,
		(inState, inComponent, outHasFocus));
		
	SJME_SCRITCHUI_SERIAL_PASS(inComponent);
	SJME_SCRITCHUI_SERIAL_PASS(outHasFocus);
	
	/* Invoke and wait. */
	SJME_SCRITCHUI_INVOKE_WAIT;
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

SJME_SCRITCHUI_DISPATCH_GENERIC_LISTENER(componentSetActivateListener,
	SJME_SCRITCHUI_SERIAL_TYPE_CHOICE_SET_ACTIVATE_LISTENER,
	sjme_scritchui_uiComponent, inComponent, activate)

SJME_SCRITCHUI_DISPATCH_GENERIC_LISTENER(componentSetInputListener,
	SJME_SCRITCHUI_SERIAL_TYPE_COMPONENT_SET_INPUT_LISTENER,
	sjme_scritchui_uiComponent, inComponent, input)

SJME_SCRITCHUI_DISPATCH_GENERIC_LISTENER(componentSetPaintListener,
	SJME_SCRITCHUI_SERIAL_TYPE_COMPONENT_SET_PAINT_LISTENER,
	sjme_scritchui_uiComponent, inComponent, paint)

SJME_SCRITCHUI_DISPATCH_GENERIC_LISTENER(componentSetSizeListener,
	SJME_SCRITCHUI_SERIAL_TYPE_COMPONENT_SET_SIZE_LISTENER,
	sjme_scritchui_uiComponent, inComponent, size)

SJME_SCRITCHUI_DISPATCH_GENERIC_LISTENER(componentSetValueUpdateListener,
	SJME_SCRITCHUI_SERIAL_TYPE_COMPONENT_SET_VALUE_UPDATE_LISTENER,
	sjme_scritchui_uiComponent, inComponent, valueUpdate)

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
	
sjme_errorCode sjme_scritchui_coreSerial_labelSetString(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInNullable sjme_lpcstr inString)
{
	SJME_SCRITCHUI_SERIAL_CHUNK(labelSetString,
		SJME_SCRITCHUI_SERIAL_TYPE_LABEL_SET_STRING,
		(inState, inComponent, inString));
		
	SJME_SCRITCHUI_SERIAL_PASS(inComponent);
	SJME_SCRITCHUI_SERIAL_PASS(inString);
	
	/* Invoke and wait. */
	SJME_SCRITCHUI_INVOKE_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_lafElementColor(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNullable sjme_scritchui_uiComponent inContext,
	sjme_attrOutNotNull sjme_jint* outRGB,
	sjme_attrInValue sjme_scritchui_lafElementColorType elementColor)
{
	SJME_SCRITCHUI_SERIAL_CHUNK(lafElementColor,
		SJME_SCRITCHUI_SERIAL_TYPE_LAF_ELEMENT_COLOR,
		(inState, inContext, outRGB, elementColor));
		
	SJME_SCRITCHUI_SERIAL_PASS(inContext);
	SJME_SCRITCHUI_SERIAL_PASS(outRGB);
	SJME_SCRITCHUI_SERIAL_PASS(elementColor);
	
	/* Invoke and wait. */
	SJME_SCRITCHUI_INVOKE_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_listNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_uiList* outList,
	sjme_attrInValue sjme_scritchui_choiceType inChoiceType)
{
	SJME_SCRITCHUI_SERIAL_CHUNK(listNew,
		SJME_SCRITCHUI_SERIAL_TYPE_LIST_NEW,
		(inState, outList, inChoiceType));
		
	SJME_SCRITCHUI_SERIAL_PASS(outList);
	SJME_SCRITCHUI_SERIAL_PASS(inChoiceType);
	
	/* Invoke and wait. */
	SJME_SCRITCHUI_INVOKE_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_menuBarNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_uiMenuBar* outMenuBar)
{
	SJME_SCRITCHUI_SERIAL_CHUNK(menuBarNew,
		SJME_SCRITCHUI_SERIAL_TYPE_MENU_BAR_NEW,
		(inState, outMenuBar));
		
	SJME_SCRITCHUI_SERIAL_PASS(outMenuBar);
	
	/* Invoke and wait. */
	SJME_SCRITCHUI_INVOKE_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_menuInsert(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiMenuKind intoMenu,
	sjme_attrInPositive sjme_jint atIndex,
	sjme_attrInNotNull sjme_scritchui_uiMenuKind childItem)
{
	SJME_SCRITCHUI_SERIAL_CHUNK(menuInsert,
		SJME_SCRITCHUI_SERIAL_TYPE_MENU_INSERT,
		(inState, intoMenu, atIndex, childItem));
		
	SJME_SCRITCHUI_SERIAL_PASS(intoMenu);
	SJME_SCRITCHUI_SERIAL_PASS(atIndex);
	SJME_SCRITCHUI_SERIAL_PASS(childItem);
	
	/* Invoke and wait. */
	SJME_SCRITCHUI_INVOKE_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_menuItemNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_uiMenuItem* outMenuItem)
{
	SJME_SCRITCHUI_SERIAL_CHUNK(menuItemNew,
		SJME_SCRITCHUI_SERIAL_TYPE_MENU_ITEM_NEW,
		(inState, outMenuItem));
		
	SJME_SCRITCHUI_SERIAL_PASS(outMenuItem);
	
	/* Invoke and wait. */
	SJME_SCRITCHUI_INVOKE_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_menuNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_uiMenu* outMenu)
{
	SJME_SCRITCHUI_SERIAL_CHUNK(menuNew,
		SJME_SCRITCHUI_SERIAL_TYPE_MENU_NEW,
		(inState, outMenu));
		
	SJME_SCRITCHUI_SERIAL_PASS(outMenu);
	
	/* Invoke and wait. */
	SJME_SCRITCHUI_INVOKE_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_menuRemove(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiMenuKind fromMenu,
	sjme_attrInPositive sjme_jint atIndex)
{
	SJME_SCRITCHUI_SERIAL_CHUNK(menuRemove,
		SJME_SCRITCHUI_SERIAL_TYPE_MENU_REMOVE,
		(inState, fromMenu, atIndex));
		
	SJME_SCRITCHUI_SERIAL_PASS(fromMenu);
	SJME_SCRITCHUI_SERIAL_PASS(atIndex);
	
	/* Invoke and wait. */
	SJME_SCRITCHUI_INVOKE_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_menuRemoveAll(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiMenuKind fromMenu)
{
	SJME_SCRITCHUI_SERIAL_CHUNK(menuRemoveAll,
		SJME_SCRITCHUI_SERIAL_TYPE_MENU_REMOVE_ALL,
		(inState, fromMenu));
		
	SJME_SCRITCHUI_SERIAL_PASS(fromMenu);
	
	/* Invoke and wait. */
	SJME_SCRITCHUI_INVOKE_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_objectDelete(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_uiCommon* inOutObject)
{
	SJME_SCRITCHUI_SERIAL_CHUNK(objectDelete,
		SJME_SCRITCHUI_SERIAL_TYPE_OBJECT_DELETE,
		(inState, inOutObject));
		
	SJME_SCRITCHUI_SERIAL_PASS(inOutObject);
	
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

sjme_errorCode sjme_scritchui_coreSerial_windowSetMenuBar(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInNullable sjme_scritchui_uiMenuBar inMenuBar)
{
	SJME_SCRITCHUI_SERIAL_CHUNK(windowSetMenuBar,
		SJME_SCRITCHUI_SERIAL_TYPE_WINDOW_SET_MENU_BAR,
		(inState, inWindow, inMenuBar));
		
	SJME_SCRITCHUI_SERIAL_PASS(inWindow);
	SJME_SCRITCHUI_SERIAL_PASS(inMenuBar);
	
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

