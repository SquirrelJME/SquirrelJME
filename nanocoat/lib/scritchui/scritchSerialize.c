/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "lib/scritchui/core/core.h"
#include "lib/scritchui/core/coreSerial.h"
#include "lib/scritchui/scritchuiTypes.h"
#include "lib/scritchui/scritchuiPencil.h"
#include "sjme/alloc.h"
#include "sjme/debug.h"

/* clang-format off */ /* @formatter:off */
/* ------------------------------------------------------------------------ */

/** Serial variables. */
#define SJME_SDX_VARS(what) \
	sjme_errorCode error; \
	sjme_jboolean direct; \
	sjme_scritchui_serialData sdData;

/** Invoke serial call and wait for result. */
#define SJME_SDX_WAIT \
	do { sjme_atomic_barrier(); \
		if (inState->api == NULL || inState->api->loopExecuteWait == NULL) \
			return sjme_error_fatal(SJME_ERROR_ILLEGAL_STATE); \
		if (sjme_error_is(error = inState->api->loopExecuteWait(inState, \
		sjme_scritchui_serialDispatch, (void*)&sdData))) \
		return sjme_error_default(error); \
	return sdData.error; } while (0)

/** Pass a serial value. */
#define SJME_SDX_PASS(what) \
	serial->what = what

/** Setup pre-serialization. */
#define SJME_SDX_SETUP(key, what, extra) \
	do { memset((void*)&sdData, 0, sizeof(sdData)); \
	sdData.type = (key); \
	sdData.error = SJME_ERROR_UNKNOWN; \
	sdData.state = inState; \
	extra \
	serial = (void*)&sdData.data.what; } while (0)

/** The name for serial data. */
#define SDX_STRUCT_NAME(where, what) \
	SJME_TOKEN_PASTE4(sjme_scritch, where, _serialData_, what)

/* ------------------------------------------------------------------------ */

/** Serial variables. */
#define SJME_SDU_VARS(what) \
	SDX_STRUCT_NAME(ui, what)* serial

/** Pre-check call to make. */
#define SJME_SDU_PRE_CHECK \
	do { if (inState == NULL) \
	{ \
		return SJME_ERROR_NULL_ARGUMENTS; \
	} } while(0)

/** Check for being in the loop. */
#define SJME_SDU_LOOP_CHECK(what) \
	do { \
		error = SJME_NUM_ERROR_CODES; \
		direct = SJME_JNI_FALSE; \
		 \
		if (inState->api == NULL || inState->apiInThread == NULL || \
			inState->api->loopIsInThread == NULL || \
			inState->api->what == NULL || \
			inState->apiInThread->what == NULL) \
			return sjme_error_fatal(SJME_ERROR_ILLEGAL_STATE); \
		if (sjme_error_is(error = inState->api->loopIsInThread(inState, \
				&direct))) \
			return sjme_error_default(error); \
	} while (0)

/** Common shared chunk of forwarding code, to reduce duplicates. */
#define SJME_SDU_CHUNK(what, whatType, directInvokeArgs) \
	SJME_SDX_VARS(what) \
	SJME_SDU_VARS(what); \
	 \
	SJME_SDU_PRE_CHECK; \
	SJME_SDU_LOOP_CHECK(what); \
	 \
	/* Direct call? */ \
	if (direct) \
		return inState->apiInThread->what directInvokeArgs; \
	 \
	/* Serialize and Store. */ \
	SJME_SDX_SETUP( \
		whatType, \
		what, )

/** Performs dispatch call. */
#define SJME_SDU_CALL(what, args) \
	do { \
	if (state->apiInThread->what == NULL) \
		return SJME_THREAD_RESULT( \
			sjme_error_fatal(SJME_ERROR_ILLEGAL_STATE)); \
	sdData->error = state->apiInThread->what args; } while (0)

/** Simplified case call. */
#define SJME_SDU_CASE(what, whatType, args) \
	case whatType: \
		SJME_SDU_CALL(what, args); \
		break

/** Simplified listener dispatch call. */
#define SJME_SDU_CASE_LISTENER(what, whatType) \
	SJME_SDU_CASE(what, \
		whatType, \
		(state, \
		as->what.inComponent, \
		as->what.inListener, \
		as->what.copyFrontEnd))

/* ------------------------------------------------------------------------ */

/** Serial variables. */
#define SJME_SDP_VARS(what) \
	sjme_scritchui inState; \
	SDX_STRUCT_NAME(pen, what)* serial

/** Pre-check call to make. */
#define SJME_SDP_PRE_CHECK \
	do { if (g == NULL || g->common.state == NULL) \
	{ \
		return SJME_ERROR_NULL_ARGUMENTS; \
	} inState = g->common.state;} while(0)

/** Check for being in the loop. */
#define SJME_SDP_LOOP_CHECK(what) \
	do { \
		error = SJME_NUM_ERROR_CODES; \
		direct = SJME_JNI_FALSE; \
		 \
		if (inState->api == NULL || g->api == NULL || \
			g->apiInThread == NULL || \
			inState->api->loopIsInThread == NULL || \
			g->api->what == NULL || \
			g->apiInThread->what == NULL) \
			return sjme_error_fatal(SJME_ERROR_ILLEGAL_STATE); \
		if (sjme_error_is(error = inState->api->loopIsInThread(inState, \
				&direct))) \
			return sjme_error_default(error); \
	} while (0)

/** Common shared chunk of forwarding code, to reduce duplicates (graphics). */
#define SJME_SDP_CHUNK(what, whatType, directInvokeArgs) \
	SJME_SDX_VARS(what) \
	SJME_SDP_VARS(what); \
	 \
	SJME_SDP_PRE_CHECK; \
	SJME_SDP_LOOP_CHECK(what); \
	 \
	/* Direct call? */ \
	if (direct) \
		return g->apiInThread->what directInvokeArgs; \
	 \
	/* Serialize and Store. */ \
	SJME_SDX_SETUP( \
		whatType, \
		what, sdData.g = g;)

/** Performs dispatch call (pen). */
#define SJME_SDP_CALL(what, args) \
	do { \
	if (sdData->g->apiInThread->what == NULL) \
		return SJME_THREAD_RESULT( \
			sjme_error_fatal(SJME_ERROR_ILLEGAL_STATE)); \
	sdData->error = sdData->g->apiInThread->what args; } while (0)

/** Simplified case call (pen). */
#define SJME_SDP_CASE(what, whatType, args) \
	case whatType: \
		SJME_SDP_CALL(what, args); \
		break

/* ------------------------------------------------------------------------ */

#pragma region(font)

/** Serial variables. */
#define SJME_SDF_VARS(what) \
	sjme_scritchui inState; \
	SDX_STRUCT_NAME(font, what)* serial

/** Pre-check call to make. */
#define SJME_SDF_PRE_CHECK \
	do { if (inFont == NULL) \
			return SJME_ERROR_NULL_ARGUMENTS; \
		if (inFont->common.state == NULL) \
			return sjme_error_fatal(SJME_ERROR_ILLEGAL_STATE); \
		inState = inFont->common.state; \
	} while(0)

/** Check for being in the loop. */
#define SJME_SDF_LOOP_CHECK(what) \
	do { \
		error = SJME_NUM_ERROR_CODES; \
		direct = SJME_JNI_FALSE; \
		 \
		if (inState->api == NULL || inFont->api == NULL || \
			inFont->apiInThread == NULL || \
			inState->api->loopIsInThread == NULL || \
			inFont->api->what == NULL || \
			inFont->apiInThread->what == NULL) \
			return sjme_error_fatal(SJME_ERROR_ILLEGAL_STATE); \
		if (sjme_error_is(error = inState->api->loopIsInThread(inState, \
				&direct))) \
			return sjme_error_default(error); \
	} while (0)

/** Common shared chunk of forwarding code, to reduce duplicates (graphics). */
#define SJME_SDF_CHUNK(what, whatType, directInvokeArgs) \
	SJME_SDX_VARS(what) \
	SJME_SDF_VARS(what); \
	 \
	SJME_SDF_PRE_CHECK; \
	SJME_SDF_LOOP_CHECK(what); \
	 \
	/* Direct call? */ \
	if (direct) \
		return inFont->apiInThread->what directInvokeArgs; \
	 \
	/* Serialize and Store. */ \
	SJME_SDX_SETUP( \
		whatType, \
		what, sdData.inFont = inFont;)

/** Performs dispatch call (font). */
#define SJME_SDF_CALL(what, args) \
	do { \
	if (sdData->inFont->apiInThread->what == NULL) \
		return SJME_THREAD_RESULT( \
			sjme_error_fatal(SJME_ERROR_ILLEGAL_STATE)); \
	sdData->error = sdData->inFont->apiInThread->what args; \
	} while (0)

/** Simplified case call (font). */
#define SJME_SDF_CASE(what, whatType, args) \
	case whatType: \
		SJME_SDF_CALL(what, args); \
		break

#pragma endregion(font)

/* ------------------------------------------------------------------------ */
/* clang-format on */ /* @formatter:on */

static sjme_thread_result sjme_attrThreadCall sjme_scritchui_serialDispatch(
	sjme_attrInNullable sjme_thread_parameter anything)
{
#define SJME_SCRITCHUI_DISPATCH_SWITCH_BEGIN switch (sdData->type) {
#define SJME_SCRITCHUI_DISPATCH_SWITCH_END \
	default: \
		sjme_todo("Impl %d?", sdData->type);\
		return SJME_THREAD_RESULT(sjme_error_notImplemented(0)); }
	
	volatile sjme_scritchui_serialData* sdData;
	sjme_scritchui_serialDataUnion* as;
	sjme_scritchui state;
	
	if (anything == NULL)
		return SJME_THREAD_RESULT(SJME_ERROR_NULL_ARGUMENTS);
		
	/* Emit barrier so we can access this. */
	sdData = (sjme_scritchui_serialData*)anything;
	sjme_atomic_barrier();
	
	/* Restore info. */
	state = sdData->state;
	as = (void*)&sdData->data;
	
/* clang-format off */ /* @formatter:off */
/* ------------------------------------------------------------------------ */
	
	/* Begin cases. */
	SJME_SCRITCHUI_DISPATCH_SWITCH_BEGIN
	
	SJME_SDU_CASE(choiceGetSelectedIndex,
		SJME_SCRITCHUI_SERIAL_UI_CHOICE_GET_SELECTED_INDEX,
		(state,
		as->choiceGetSelectedIndex.inComponent,
		as->choiceGetSelectedIndex.outIndex));
	
	SJME_SDU_CASE(choiceItemGet,
		SJME_SCRITCHUI_SERIAL_UI_CHOICE_ITEM_GET,
		(state,
		as->choiceItemGet.inComponent,
		as->choiceItemGet.atIndex,
		as->choiceItemGet.outItemTemplate));
	
	SJME_SDU_CASE(choiceItemInsert,
		SJME_SCRITCHUI_SERIAL_UI_CHOICE_ITEM_INSERT,
		(state,
		as->choiceItemInsert.inComponent,
		as->choiceItemInsert.inOutIndex));
		
	SJME_SDU_CASE(choiceItemRemove,
		SJME_SCRITCHUI_SERIAL_UI_CHOICE_ITEM_REMOVE,
		(state,
		as->choiceItemRemove.inComponent,
		as->choiceItemRemove.atIndex));
		
	SJME_SDU_CASE(choiceItemRemoveAll,
		SJME_SCRITCHUI_SERIAL_UI_CHOICE_ITEM_REMOVE_ALL,
		(state,
		as->choiceItemRemoveAll.inComponent));
		
	SJME_SDU_CASE(choiceItemSetEnabled,
		SJME_SCRITCHUI_SERIAL_UI_CHOICE_ITEM_SET_ENABLED,
		(state,
		as->choiceItemSetEnabled.inComponent,
		as->choiceItemSetEnabled.atIndex,
		as->choiceItemSetEnabled.isEnabled));
		
	SJME_SDU_CASE(choiceItemSetImage,
		SJME_SCRITCHUI_SERIAL_UI_CHOICE_ITEM_SET_IMAGE,
		(state,
		as->choiceItemSetImage.inComponent,
		as->choiceItemSetImage.atIndex,
		as->choiceItemSetImage.inRgb,
		as->choiceItemSetImage.inRgbOff,
		as->choiceItemSetImage.inRgbDataLen,
		as->choiceItemSetImage.inRgbScanLen,
		as->choiceItemSetImage.width,
		as->choiceItemSetImage.height));
		
	SJME_SDU_CASE(choiceItemSetSelected,
		SJME_SCRITCHUI_SERIAL_UI_CHOICE_ITEM_SET_SELECTED,
		(state,
		as->choiceItemSetSelected.inComponent,
		as->choiceItemSetSelected.atIndex,
		as->choiceItemSetSelected.isSelected));
		
	SJME_SDU_CASE(choiceItemSetString,
		SJME_SCRITCHUI_SERIAL_UI_CHOICE_ITEM_SET_STRING,
		(state,
		as->choiceItemSetString.inComponent,
		as->choiceItemSetString.atIndex,
		as->choiceItemSetString.inString));
		
	SJME_SDU_CASE(choiceLength,
		SJME_SCRITCHUI_SERIAL_UI_CHOICE_LENGTH,
		(state,
		as->choiceLength.inComponent,
		as->choiceLength.outLength));
	
	SJME_SDU_CASE(componentFocusGrab,
		SJME_SCRITCHUI_SERIAL_UI_COMPONENT_FOCUS_GRAB,
		(state,
		as->componentFocusGrab.inComponent));
	
	SJME_SDU_CASE(componentFocusHas,
		SJME_SCRITCHUI_SERIAL_UI_COMPONENT_FOCUS_HAS,
		(state,
		as->componentFocusHas.inComponent,
		as->componentFocusHas.outHasFocus));
	
	SJME_SDU_CASE(componentGetParent,
		SJME_SCRITCHUI_SERIAL_UI_COMPONENT_GET_PARENT,
		(state,
		as->componentGetParent.inComponent,
		as->componentGetParent.outParent));
		
	SJME_SDU_CASE(componentPosition,
		SJME_SCRITCHUI_SERIAL_UI_COMPONENT_POSITION,
		(state,
		as->componentPosition.inComponent,
		as->componentPosition.outX,
		as->componentPosition.outY));
	
	SJME_SDU_CASE(componentRepaint,
		SJME_SCRITCHUI_SERIAL_UI_COMPONENT_REPAINT,
		(state,
		as->componentRepaint.inComponent,
		as->componentRepaint.x,
		as->componentRepaint.y,
		as->componentRepaint.width,
		as->componentRepaint.height));
	
	SJME_SDU_CASE(componentRevalidate,
		SJME_SCRITCHUI_SERIAL_UI_COMPONENT_REVALIDATE,
		(state,
		as->componentRevalidate.inComponent));
		
	SJME_SDU_CASE(componentSetActivateListener,
		SJME_SCRITCHUI_SERIAL_UI_CHOICE_SET_ACTIVATE_LISTENER,
		(state,
		as->componentSetActivateListener.inComponent,
		as->componentSetActivateListener.inListener,
		as->componentSetActivateListener.copyFrontEnd));
	
	SJME_SDU_CASE_LISTENER(componentSetInputListener,
		SJME_SCRITCHUI_SERIAL_UI_COMPONENT_SET_INPUT_LISTENER);
		
	SJME_SDU_CASE_LISTENER(componentSetPaintListener,
		SJME_SCRITCHUI_SERIAL_UI_COMPONENT_SET_PAINT_LISTENER);
	
	SJME_SDU_CASE_LISTENER(componentSetSizeListener,
		SJME_SCRITCHUI_SERIAL_UI_COMPONENT_SET_SIZE_LISTENER);
		
	SJME_SDU_CASE(componentSetValueUpdateListener,
		SJME_SCRITCHUI_SERIAL_UI_COMPONENT_SET_VALUE_UPDATE_LISTENER,
		(state,
		as->componentSetValueUpdateListener.inComponent,
		as->componentSetValueUpdateListener.inListener,
		as->componentSetValueUpdateListener.copyFrontEnd));
		
	SJME_SDU_CASE_LISTENER(componentSetVisibleListener,
		SJME_SCRITCHUI_SERIAL_UI_COMPONENT_SET_VISIBLE_LISTENER);
	
	SJME_SDU_CASE(componentSize,
		SJME_SCRITCHUI_SERIAL_UI_COMPONENT_SIZE,
		(state,
		as->componentSize.inComponent,
		as->componentSize.outWidth,
		as->componentSize.outHeight));
		
	SJME_SDU_CASE(containerAdd,
		SJME_SCRITCHUI_SERIAL_UI_CONTAINER_ADD,
		(state,
		as->containerAdd.inContainer,
		as->containerAdd.addComponent));
		
	SJME_SDU_CASE(containerGetFrame,
		SJME_SCRITCHUI_SERIAL_UI_CONTAINER_GET_FRAME,
		(state,
		as->containerGetFrame.inContainer,
		as->containerGetFrame.contentSize,
		as->containerGetFrame.frameBound,
		as->containerGetFrame.contentBound));
		
	SJME_SDU_CASE(containerRemove,
		SJME_SCRITCHUI_SERIAL_UI_CONTAINER_REMOVE,
		(state,
		as->containerRemove.inContainer,
		as->containerRemove.removeComponent));
		
	SJME_SDU_CASE(containerRemoveAll,
		SJME_SCRITCHUI_SERIAL_UI_CONTAINER_REMOVE_ALL,
		(state,
		as->containerAdd.inContainer));
		
	SJME_SDU_CASE(containerSetBounds,
		SJME_SCRITCHUI_SERIAL_UI_CONTAINER_SET_BOUNDS,
		(state,
		as->containerSetBounds.inContainer,
		as->containerSetBounds.inComponent,
		as->containerSetBounds.x,
		as->containerSetBounds.y,
		as->containerSetBounds.width,
		as->containerSetBounds.height));
	
	SJME_SDU_CASE(fontBuiltin,
		SJME_SCRITCHUI_SERIAL_UI_FONT_BUILTIN,
		(state,
		as->fontBuiltin.outFont));

	SJME_SDU_CASE(fontByFace,
		SJME_SCRITCHUI_SERIAL_UI_FONT_BY_FACE,
		(state,
		as->fontByFace.outFont,
		as->fontByFace.outParams,
		as->fontByFace.inFace,
		as->fontByFace.inParams));
	
	SJME_SDU_CASE(fontCount,
		SJME_SCRITCHUI_SERIAL_UI_FONT_COUNT,
		(state,
		as->fontCount.outCount));
	
	SJME_SDU_CASE(fontDerive,
		SJME_SCRITCHUI_SERIAL_UI_FONT_DERIVE,
		(state,
		as->fontDerive.newFont,
		as->fontDerive.newParams,
		as->fontDerive.oldFont,
		as->fontDerive.deriveParams));
	
	SJME_SDU_CASE(fontList,
		SJME_SCRITCHUI_SERIAL_UI_FONT_LIST,
		(state,
		as->fontList.outFonts,
		as->fontList.outValid,
		as->fontList.outCount));
		
	SJME_SDU_CASE(hardwareGraphics,
		SJME_SCRITCHUI_SERIAL_UI_HARDWARE_GRAPHICS,
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
		
	SJME_SDU_CASE(pseudoGraphics,
		SJME_SCRITCHUI_SERIAL_UI_PSEUDO_GRAPHICS,
		(state,
		as->pseudoGraphics.outPencil,
		as->pseudoGraphics.outWeakPencil,
		as->pseudoGraphics.pencils,
		as->pseudoGraphics.numPencils,
		as->pseudoGraphics.pencilFrontEndCopy));
		
	SJME_SDU_CASE(labelSetString,
		SJME_SCRITCHUI_SERIAL_UI_LABEL_SET_STRING,
		(state,
		as->labelSetString.inCommon,
		as->labelSetString.inString));
		
	SJME_SDU_CASE(lafDpiProject,
		SJME_SCRITCHUI_SERIAL_UI_LAF_DPI_PROJECT,
		(state,
		as->lafDpiProject.inContext,
		as->lafDpiProject.toBase,
		as->lafDpiProject.inOutX,
		as->lafDpiProject.inOutY,
		as->lafDpiProject.inOutW,
		as->lafDpiProject.inOutH));
		
	SJME_SDU_CASE(lafElementColor,
		SJME_SCRITCHUI_SERIAL_UI_LAF_ELEMENT_COLOR,
		(state,
		as->lafElementColor.inContext,
		as->lafElementColor.outRGB,
		as->lafElementColor.elementColor));

	SJME_SDU_CASE(lafMetric,
		SJME_SCRITCHUI_SERIAL_UI_LAF_METRIC,
		(state,
		as->lafMetric.inContext,
		as->lafMetric.outValue,
		as->lafMetric.metricType));

	SJME_SDU_CASE(listNew,
		SJME_SCRITCHUI_SERIAL_UI_LIST_NEW,
		(state,
		as->listNew.outList,
		as->listNew.inChoiceType));
			
	SJME_SDU_CASE(menuBarNew,
		SJME_SCRITCHUI_SERIAL_UI_MENU_BAR_NEW,
		(state,
		as->menuBarNew.outMenuBar));
			
	SJME_SDU_CASE(menuInsert,
		SJME_SCRITCHUI_SERIAL_UI_MENU_INSERT,
		(state,
		as->menuInsert.intoMenu,
		as->menuInsert.atIndex,
		as->menuInsert.childItem));
			
	SJME_SDU_CASE(menuItemNew,
		SJME_SCRITCHUI_SERIAL_UI_MENU_ITEM_NEW,
		(state,
		as->menuItemNew.outMenuItem));
			
	SJME_SDU_CASE(menuNew,
		SJME_SCRITCHUI_SERIAL_UI_MENU_NEW,
		(state,
		as->menuNew.outMenu));
			
	SJME_SDU_CASE(menuRemove,
		SJME_SCRITCHUI_SERIAL_UI_MENU_REMOVE,
		(state,
		as->menuRemove.fromMenu,
		as->menuRemove.atIndex));
			
	SJME_SDU_CASE(menuRemoveAll,
		SJME_SCRITCHUI_SERIAL_UI_MENU_REMOVE_ALL,
		(state,
		as->menuRemoveAll.fromMenu));
		
	SJME_SDU_CASE(objectDelete,
		SJME_SCRITCHUI_SERIAL_UI_OBJECT_DELETE,
		(state,
		as->objectDelete.inOutObject));
	
	SJME_SDU_CASE(panelEnableFocus,
		SJME_SCRITCHUI_SERIAL_UI_PANEL_ENABLE_FOCUS,
		(state,
		as->panelEnableFocus.inPanel,
		as->panelEnableFocus.enableFocus,
		as->panelEnableFocus.defaultFocus));

	SJME_SDU_CASE(panelNew,
		SJME_SCRITCHUI_SERIAL_UI_PANEL_NEW,
		(state,
		as->panelNew.outPanel));

	SJME_SDU_CASE(screenGetBounds,
		SJME_SCRITCHUI_SERIAL_UI_SCREEN_GET_BOUNDS,
		(state,
		as->screenGetBounds.inScreen,
		as->screenGetBounds.forComponent,
		as->screenGetBounds.pixelBound,
		as->screenGetBounds.mmBound));

	SJME_SDU_CASE(screenSetListener,
		SJME_SCRITCHUI_SERIAL_UI_SCREEN_SET_LISTENER,
		(state,
		as->screenSetListener.inListener,
		as->screenSetListener.copyFrontEnd));

	SJME_SDU_CASE(screens,
		SJME_SCRITCHUI_SERIAL_UI_SCREENS,
		(state,
		as->screens.outScreens,
		as->screens.inOutNumScreens));

	SJME_SDU_CASE(scrollPanelNew,
		SJME_SCRITCHUI_SERIAL_UI_SCROLL_PANEL_NEW,
		(state,
		as->scrollPanelNew.outScrollPanel));
	
	SJME_SDU_CASE(viewGetView,
		SJME_SCRITCHUI_SERIAL_UI_VIEW_GET_VIEW,
		(state,
		as->viewGetView.inComponent,
		as->viewGetView.outViewRect));
	
	SJME_SDU_CASE(viewSetArea,
		SJME_SCRITCHUI_SERIAL_UI_VIEW_SET_AREA,
		(state,
		as->viewSetArea.inComponent,
		as->viewSetArea.inViewArea));
	
	SJME_SDU_CASE(viewSetView,
		SJME_SCRITCHUI_SERIAL_UI_VIEW_SET_VIEW,
		(state,
		as->viewSetView.inComponent,
		as->viewSetView.inViewPos));
	
	SJME_SDU_CASE(viewSetSizeSuggestListener,
		SJME_SCRITCHUI_SERIAL_UI_VIEW_SET_SIZE_SUGGEST_LISTENER,
		(state,
		as->viewSetSizeSuggestListener.inComponent,
		as->viewSetSizeSuggestListener.inListener,
		as->viewSetSizeSuggestListener.copyFrontEnd));
	
	SJME_SDU_CASE(viewSetViewListener,
		SJME_SCRITCHUI_SERIAL_UI_VIEW_SET_VIEW_LISTENER,
		(state,
		as->viewSetViewListener.inComponent,
		as->viewSetViewListener.inListener,
		as->viewSetViewListener.copyFrontEnd));

	SJME_SDU_CASE(windowContentMinimumSize,
		SJME_SCRITCHUI_SERIAL_UI_WINDOW_CONTENT_MINIMUM_SIZE,
		(state,
		as->windowContentMinimumSize.inWindow,
		as->windowContentMinimumSize.width,
		as->windowContentMinimumSize.height));
			
	SJME_SDU_CASE(windowNew,
		SJME_SCRITCHUI_SERIAL_UI_WINDOW_NEW,
		(state,
		as->windowNew.outWindow));

	SJME_SDU_CASE(windowSetCloseListener,
		SJME_SCRITCHUI_SERIAL_UI_WINDOW_SET_CLOSE_LISTENER,
		(state,
		as->windowSetCloseListener.inWindow,
		as->windowSetCloseListener.inListener,
		as->windowSetCloseListener.copyFrontEnd));

	SJME_SDU_CASE(windowSetFlags,
		SJME_SCRITCHUI_SERIAL_UI_WINDOW_SET_FLAGS,
		(state,
		as->windowSetFlags.inWindow,
		as->windowSetFlags.setFlags,
		as->windowSetFlags.actualFlags));

	SJME_SDU_CASE(windowSetMenuBar,
		SJME_SCRITCHUI_SERIAL_UI_WINDOW_SET_MENU_BAR,
		(state,
		as->windowSetMenuBar.inWindow,
		as->windowSetMenuBar.inMenuBar));
		
	SJME_SDU_CASE(windowSetMenuItemActivateListener,
		SJME_SCRITCHUI_SERIAL_UI_WINDOW_SET_MENU_ITEM_ACTIVATE_LISTENER,
		(state,
		as->windowSetMenuItemActivateListener.inWindow,
		as->windowSetMenuItemActivateListener.inListener,
		as->windowSetMenuItemActivateListener.copyFrontEnd));

	SJME_SDU_CASE(windowSetState,
		SJME_SCRITCHUI_SERIAL_UI_WINDOW_SET_STATE,
		(state,
		as->windowSetState.inWindow,
		as->windowSetState.setState,
		as->windowSetState.actualState));
		
	SJME_SDU_CASE(windowSetVisible,
		SJME_SCRITCHUI_SERIAL_UI_WINDOW_SET_VISIBLE,
		(state,
		as->windowSetVisible.inWindow,
		as->windowSetVisible.isVisible));
	
/* ------------------------------------------------------------------------ */
	
	SJME_SDP_CASE(close,
		SJME_SCRITCHUI_SERIAL_PEN_CLOSE,
		(as->close.g));

	SJME_SDP_CASE(copyArea,
		SJME_SCRITCHUI_SERIAL_PEN_COPY_AREA,
		(as->copyArea.g,
		as->copyArea.sx,
		as->copyArea.sy,
		as->copyArea.w,
		as->copyArea.h,
		as->copyArea.dx,
		as->copyArea.dy,
		as->copyArea.anchor));

	SJME_SDP_CASE(drawArc,
		SJME_SCRITCHUI_SERIAL_PEN_DRAW_ARC,
		(as->drawArc.g,
		as->drawArc.x,
		as->drawArc.y,
		as->drawArc.w,
		as->drawArc.h,
		as->drawArc.startAngle,
		as->drawArc.arcAngle));

	SJME_SDP_CASE(drawHoriz,
		SJME_SCRITCHUI_SERIAL_PEN_DRAW_HORIZ,
		(as->drawHoriz.g,
		as->drawHoriz.x,
		as->drawHoriz.y,
		as->drawHoriz.w));

	SJME_SDP_CASE(drawRect,
		SJME_SCRITCHUI_SERIAL_PEN_DRAW_RECT,
		(as->drawRect.g,
		as->drawRect.x,
		as->drawRect.y,
		as->drawRect.w,
		as->drawRect.h));

	SJME_SDP_CASE(drawRegion,
		SJME_SCRITCHUI_SERIAL_PEN_DRAW_REGION,
		(as->drawRegion.g,
		as->drawRegion.pf,
		as->drawRegion.data,
		as->drawRegion.off,
		as->drawRegion.dataLen,
		as->drawRegion.scanLen,
		as->drawRegion.alpha,
		as->drawRegion.xSrc,
		as->drawRegion.ySrc,
		as->drawRegion.wSrc,
		as->drawRegion.hSrc,
		as->drawRegion.trans,
		as->drawRegion.xDest,
		as->drawRegion.yDest,
		as->drawRegion.anchor,
		as->drawRegion.wDest,
		as->drawRegion.hDest,
		as->drawRegion.origImgWidth,
		as->drawRegion.origImgHeight));

	SJME_SDP_CASE(drawRoundRect,
		SJME_SCRITCHUI_SERIAL_PEN_DRAW_ROUND_RECT,
		(as->drawRoundRect.g,
		as->drawRoundRect.x,
		as->drawRoundRect.y,
		as->drawRoundRect.w,
		as->drawRoundRect.h,
		as->drawRoundRect.arcWidth,
		as->drawRoundRect.arcHeight));

	SJME_SDP_CASE(drawChar,
		SJME_SCRITCHUI_SERIAL_PEN_DRAW_CHAR,
		(as->drawChar.g,
		as->drawChar.c,
		as->drawChar.x,
		as->drawChar.y,
		as->drawChar.anchor,
		as->drawChar.outCw));

	SJME_SDP_CASE(drawChars,
		SJME_SCRITCHUI_SERIAL_PEN_DRAW_CHARS,
		(as->drawChars.g,
		as->drawChars.s,
		as->drawChars.o,
		as->drawChars.l,
		as->drawChars.x,
		as->drawChars.y,
		as->drawChars.anchor));

	SJME_SDP_CASE(drawLine,
		SJME_SCRITCHUI_SERIAL_PEN_DRAW_LINE,
		(as->drawLine.g,
		as->drawLine.x1,
		as->drawLine.y1,
		as->drawLine.x2,
		as->drawLine.y2));

	SJME_SDP_CASE(drawPixel,
		SJME_SCRITCHUI_SERIAL_PEN_DRAW_PIXEL,
		(as->drawPixel.g,
		as->drawPixel.x,
		as->drawPixel.y));

	SJME_SDP_CASE(drawPolyline,
		SJME_SCRITCHUI_SERIAL_PEN_DRAW_POLYLINE,
		(as->drawPolyline.g,
		as->drawPolyline.inXPoints,
		as->drawPolyline.xOffset,
		as->drawPolyline.inYPoints,
		as->drawPolyline.yOffset,
		as->drawPolyline.nPoints));

	SJME_SDP_CASE(drawSubstring,
		SJME_SCRITCHUI_SERIAL_PEN_DRAW_SUBSTRING,
		(as->drawSubstring.g,
		as->drawSubstring.s,
		as->drawSubstring.o,
		as->drawSubstring.l,
		as->drawSubstring.x,
		as->drawSubstring.y,
		as->drawSubstring.anchor));

	SJME_SDP_CASE(drawTriangle,
		SJME_SCRITCHUI_SERIAL_PEN_DRAW_TRIANGLE,
		(as->drawTriangle.g,
		as->drawTriangle.x1,
		as->drawTriangle.y1,
		as->drawTriangle.x2,
		as->drawTriangle.y2,
		as->drawTriangle.x3,
		as->drawTriangle.y3));

	SJME_SDP_CASE(drawXRGB32Region,
		SJME_SCRITCHUI_SERIAL_PEN_DRAW_XRGB32REGION,
		(as->drawXRGB32Region.g,
		as->drawXRGB32Region.data,
		as->drawXRGB32Region.off,
		as->drawXRGB32Region.dataLen,
		as->drawXRGB32Region.scanLen,
		as->drawXRGB32Region.alpha,
		as->drawXRGB32Region.xSrc,
		as->drawXRGB32Region.ySrc,
		as->drawXRGB32Region.wSrc,
		as->drawXRGB32Region.hSrc,
		as->drawXRGB32Region.trans,
		as->drawXRGB32Region.xDest,
		as->drawXRGB32Region.yDest,
		as->drawXRGB32Region.anchor,
		as->drawXRGB32Region.wDest,
		as->drawXRGB32Region.hDest,
		as->drawXRGB32Region.origImgWidth,
		as->drawXRGB32Region.origImgHeight));

	SJME_SDP_CASE(fillArc,
		SJME_SCRITCHUI_SERIAL_PEN_FILL_ARC,
		(as->fillArc.g,
		as->fillArc.x,
		as->fillArc.y,
		as->fillArc.w,
		as->fillArc.h,
		as->fillArc.startAngle,
		as->fillArc.arcAngle));

	SJME_SDP_CASE(fillPolygon,
		SJME_SCRITCHUI_SERIAL_PEN_FILL_POLYGON,
		(as->fillPolygon.g,
		as->fillPolygon.inXPoints,
		as->fillPolygon.xOffset,
		as->fillPolygon.inYPoints,
		as->fillPolygon.yOffset,
		as->fillPolygon.nPoints));

	SJME_SDP_CASE(fillRect,
		SJME_SCRITCHUI_SERIAL_PEN_FILL_RECT,
		(as->fillRect.g,
		as->fillRect.x,
		as->fillRect.y,
		as->fillRect.w,
		as->fillRect.h));

	SJME_SDP_CASE(fillRoundRect,
		SJME_SCRITCHUI_SERIAL_PEN_FILL_ROUND_RECT,
		(as->fillRoundRect.g,
		as->fillRoundRect.x,
		as->fillRoundRect.y,
		as->fillRoundRect.w,
		as->fillRoundRect.h,
		as->fillRoundRect.arcWidth,
		as->fillRoundRect.arcHeight));

	SJME_SDP_CASE(fillTriangle,
		SJME_SCRITCHUI_SERIAL_PEN_FILL_TRIANGLE,
		(as->fillTriangle.g,
		as->fillTriangle.x1,
		as->fillTriangle.y1,
		as->fillTriangle.x2,
		as->fillTriangle.y2,
		as->fillTriangle.x3,
		as->fillTriangle.y3));

	SJME_SDP_CASE(getRegion,
		SJME_SCRITCHUI_SERIAL_PEN_GET_REGION,
		(as->getRegion.g,
		as->getRegion.pf,
		as->getRegion.data,
		as->getRegion.off,
		as->getRegion.dataLen,
		as->getRegion.scanLen,
		as->getRegion.alpha,
		as->getRegion.xSrc,
		as->getRegion.ySrc,
		as->getRegion.wSrc,
		as->getRegion.hSrc,
		as->getRegion.anchor));

	SJME_SDP_CASE(mapColor,
		SJME_SCRITCHUI_SERIAL_PEN_MAP_COLOR,
		(as->mapColor.g,
		as->mapColor.fromRaw,
		as->mapColor.inRgbOrRaw,
		as->mapColor.outColor));

	SJME_SDP_CASE(setAlphaColor,
		SJME_SCRITCHUI_SERIAL_PEN_SET_ALPHA_COLOR,
		(as->setAlphaColor.g,
		as->setAlphaColor.argb));

	SJME_SDP_CASE(setBlendingMode,
		SJME_SCRITCHUI_SERIAL_PEN_SET_BLENDING_MODE,
		(as->setBlendingMode.g,
		as->setBlendingMode.mode));

	SJME_SDP_CASE(setClip,
		SJME_SCRITCHUI_SERIAL_PEN_SET_CLIP,
		(as->setClip.g,
		as->setClip.x,
		as->setClip.y,
		as->setClip.w,
		as->setClip.h));

	SJME_SDP_CASE(setDefaultFont,
		SJME_SCRITCHUI_SERIAL_PEN_SET_DEFAULT_FONT,
		(as->setDefaultFont.g));

	SJME_SDP_CASE(setDefaults,
		SJME_SCRITCHUI_SERIAL_PEN_SET_DEFAULTS,
		(as->setDefaults.g));

	SJME_SDP_CASE(setStrokeStyle,
		SJME_SCRITCHUI_SERIAL_PEN_SET_STROKE_STYLE,
		(as->setStrokeStyle.g,
		as->setStrokeStyle.style));

	SJME_SDP_CASE(setFont,
		SJME_SCRITCHUI_SERIAL_PEN_SET_FONT,
		(as->setFont.g,
		as->setFont.font,
		as->setFont.params));

	SJME_SDP_CASE(setParametersFrom,
		SJME_SCRITCHUI_SERIAL_PEN_SET_PARAMETERS_FROM,
		(as->setParametersFrom.g,
		as->setParametersFrom.from));

	SJME_SDP_CASE(transferRegion,
		SJME_SCRITCHUI_SERIAL_PEN_TRANSFER_REGION,
		(as->transferRegion.g,
		as->transferRegion.srcPencil,
		as->transferRegion.alpha,
		as->transferRegion.xSrc,
		as->transferRegion.ySrc,
		as->transferRegion.wSrc,
		as->transferRegion.hSrc,
		as->transferRegion.trans,
		as->transferRegion.xDest,
		as->transferRegion.yDest,
		as->transferRegion.anchor,
		as->transferRegion.wDest,
		as->transferRegion.hDest,
		as->transferRegion.mode));

	SJME_SDP_CASE(translate,
		SJME_SCRITCHUI_SERIAL_PEN_TRANSLATE,
		(as->translate.g,
		as->translate.x,
		as->translate.y));

#pragma region(font)
	SJME_SDF_CASE(equals,
		SJME_SCRITCHUI_SERIAL_FONT_EQUALS,
		(as->equals.a,
		as->equals.aParams,
		as->equals.b,
		as->equals.bParams));

	SJME_SDF_CASE(metricCharDirection,
		SJME_SCRITCHUI_SERIAL_FONT_METRIC_CHAR_DIRECTION,
		(as->metricCharDirection.inFont,
		as->metricCharDirection.inCodepoint,
		as->metricCharDirection.outDirection));

	SJME_SDF_CASE(metricCharValid,
		SJME_SCRITCHUI_SERIAL_FONT_METRIC_CHAR_VALID,
		(as->metricCharValid.inFont,
		as->metricCharValid.inCodepoint,
		as->metricCharValid.outValid));

	SJME_SDF_CASE(metricFontFace,
		SJME_SCRITCHUI_SERIAL_FONT_METRIC_FONT_FACE,
		(as->metricFontFace.inFont,
		as->metricFontFace.outFace));

	SJME_SDF_CASE(metricFontName,
		SJME_SCRITCHUI_SERIAL_FONT_METRIC_FONT_NAME,
		(as->metricFontName.inFont,
		as->metricFontName.outName));

	SJME_SDF_CASE(metricFontStyle,
		SJME_SCRITCHUI_SERIAL_FONT_METRIC_FONT_STYLE,
		(as->metricFontStyle.inFont,
		as->metricFontStyle.outStyle));

	SJME_SDF_CASE(metricPixelAscent,
		SJME_SCRITCHUI_SERIAL_FONT_METRIC_PIXEL_ASCENT,
		(as->metricPixelAscent.inFont,
		as->metricPixelAscent.inParams,
		as->metricPixelAscent.isMax,
		as->metricPixelAscent.outAscent));

	SJME_SDF_CASE(metricPixelBaseline,
		SJME_SCRITCHUI_SERIAL_FONT_METRIC_PIXEL_BASELINE,
		(as->metricPixelBaseline.inFont,
		as->metricPixelBaseline.inParams,
		as->metricPixelBaseline.outBaseline));

	SJME_SDF_CASE(metricPixelDescent,
		SJME_SCRITCHUI_SERIAL_FONT_METRIC_PIXEL_DESCENT,
		(as->metricPixelDescent.inFont,
		as->metricPixelDescent.inParams,
		as->metricPixelDescent.isMax,
		as->metricPixelDescent.outDescent));

	SJME_SDF_CASE(metricPixelHeight,
		SJME_SCRITCHUI_SERIAL_FONT_METRIC_PIXEL_HEIGHT,
		(as->metricPixelHeight.inFont,
		as->metricPixelHeight.inParams,
		as->metricPixelHeight.outHeight));

	SJME_SDF_CASE(metricPixelLeading,
		SJME_SCRITCHUI_SERIAL_FONT_METRIC_PIXEL_LEADING,
		(as->metricPixelLeading.inFont,
		as->metricPixelLeading.inParams,
		as->metricPixelLeading.outLeading));

	SJME_SDF_CASE(metricPixelSize,
		SJME_SCRITCHUI_SERIAL_FONT_METRIC_PIXEL_SIZE,
		(as->metricPixelSize.inFont,
		as->metricPixelSize.inParams,
		as->metricPixelSize.inCodepoint,
		as->metricPixelSize.outSize));

	SJME_SDF_CASE(pixelCharWidth,
		SJME_SCRITCHUI_SERIAL_FONT_PIXEL_CHAR_WIDTH,
		(as->pixelCharWidth.inFont,
		as->pixelCharWidth.inParams,
		as->pixelCharWidth.inCodepoint,
		as->pixelCharWidth.outWidth));

	SJME_SDF_CASE(renderBitmap,
		SJME_SCRITCHUI_SERIAL_FONT_RENDER_BITMAP,
		(as->renderBitmap.inFont,
		as->renderBitmap.inParams,
		as->renderBitmap.inCodepoint,
		as->renderBitmap.buf,
		as->renderBitmap.bufOff,
		as->renderBitmap.bufScanLen,
		as->renderBitmap.bufHeight,
		as->renderBitmap.outOffX,
		as->renderBitmap.outOffY));

	SJME_SDF_CASE(renderChar,
		SJME_SCRITCHUI_SERIAL_FONT_RENDER_CHAR,
		(as->renderChar.inFont,
		as->renderChar.inParams,
		as->renderChar.inCodepoint,
		as->renderChar.inPencil,
		as->renderChar.xPos,
		as->renderChar.yPos,
		as->renderChar.nextXPos,
		as->renderChar.nextYPos));

	SJME_SDF_CASE(stringWidth,
		SJME_SCRITCHUI_SERIAL_FONT_STRING_WIDTH,
		(as->stringWidth.inFont,
		as->stringWidth.inParams,
		as->stringWidth.s,
		as->stringWidth.o,
		as->stringWidth.l,
		as->stringWidth.outWidth));
#pragma endregion(font)
	
	/* End. */
	SJME_SCRITCHUI_DISPATCH_SWITCH_END
	
/* ------------------------------------------------------------------------ */
/* clang-format on */ /* @formatter:on */
	
	/* Map result. */
	return SJME_THREAD_RESULT(sdData->error);

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
		SJME_SDU_CHUNK(id, \
			idCode, \
			(inState, arg, inListener, copyFrontEnd)); \
		 \
		SJME_SDX_PASS(arg); \
		SJME_SDX_PASS(inListener); \
		SJME_SDX_PASS(copyFrontEnd); \
		 \
		/* Invoke and wait. */ \
		SJME_SDX_WAIT; \
	}

/* ------------------------------------------------------------------------ */

sjme_errorCode sjme_scritchui_coreSerial_choiceGetSelectedIndex(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrOutNotNull sjme_jint* outIndex)
{
	SJME_SDU_CHUNK(choiceGetSelectedIndex,
		SJME_SCRITCHUI_SERIAL_UI_CHOICE_GET_SELECTED_INDEX,
		(inState, inComponent, outIndex));
		
	SJME_SDX_PASS(inComponent);
	SJME_SDX_PASS(outIndex);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}
	
sjme_errorCode sjme_scritchui_coreSerial_choiceItemGet(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint atIndex,
	sjme_attrOutNotNull sjme_scritchui_uiChoiceItem outItemTemplate)
{
	SJME_SDU_CHUNK(choiceItemGet,
		SJME_SCRITCHUI_SERIAL_UI_CHOICE_ITEM_GET,
		(inState, inComponent, atIndex, outItemTemplate));
		
	SJME_SDX_PASS(inComponent);
	SJME_SDX_PASS(atIndex);
	SJME_SDX_PASS(outItemTemplate);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_choiceItemInsert(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInOutNotNull sjme_jint* inOutIndex)
{
	SJME_SDU_CHUNK(choiceItemInsert,
		SJME_SCRITCHUI_SERIAL_UI_CHOICE_ITEM_INSERT,
		(inState, inComponent, inOutIndex));
		
	SJME_SDX_PASS(inComponent);
	SJME_SDX_PASS(inOutIndex);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_choiceItemRemove(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint atIndex)
{
	SJME_SDU_CHUNK(choiceItemRemove,
		SJME_SCRITCHUI_SERIAL_UI_CHOICE_ITEM_REMOVE,
		(inState, inComponent, atIndex));
		
	SJME_SDX_PASS(inComponent);
	SJME_SDX_PASS(atIndex);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_choiceItemRemoveAll(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent)
{
	SJME_SDU_CHUNK(choiceItemRemoveAll,
		SJME_SCRITCHUI_SERIAL_UI_CHOICE_ITEM_REMOVE_ALL,
		(inState, inComponent));
		
	SJME_SDX_PASS(inComponent);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_choiceItemSetEnabled(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint atIndex,
	sjme_attrInNotNull sjme_jboolean isEnabled)
{
	SJME_SDU_CHUNK(choiceItemSetEnabled,
		SJME_SCRITCHUI_SERIAL_UI_CHOICE_ITEM_SET_ENABLED,
		(inState, inComponent, atIndex, isEnabled));
		
	SJME_SDX_PASS(inComponent);
	SJME_SDX_PASS(atIndex);
	SJME_SDX_PASS(isEnabled);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
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
	SJME_SDU_CHUNK(choiceItemSetImage,
		SJME_SCRITCHUI_SERIAL_UI_CHOICE_ITEM_SET_IMAGE,
		(inState, inComponent, atIndex, inRgb, inRgbOff, inRgbDataLen,
			inRgbScanLen, width, height));
		
	SJME_SDX_PASS(inComponent);
	SJME_SDX_PASS(atIndex);
	SJME_SDX_PASS(inRgb);
	SJME_SDX_PASS(inRgbOff);
	SJME_SDX_PASS(inRgbDataLen);
	SJME_SDX_PASS(inRgbScanLen);
	SJME_SDX_PASS(width);
	SJME_SDX_PASS(height);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_choiceItemSetSelected(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint atIndex,
	sjme_attrInNotNull sjme_jboolean isSelected)
{
	SJME_SDU_CHUNK(choiceItemSetSelected,
		SJME_SCRITCHUI_SERIAL_UI_CHOICE_ITEM_SET_SELECTED,
		(inState, inComponent, atIndex, isSelected));
		
	SJME_SDX_PASS(inComponent);
	SJME_SDX_PASS(atIndex);
	SJME_SDX_PASS(isSelected);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_choiceItemSetString(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint atIndex,
	sjme_attrInNullable sjme_lpcstr inString)
{
	SJME_SDU_CHUNK(choiceItemSetString,
		SJME_SCRITCHUI_SERIAL_UI_CHOICE_ITEM_SET_STRING,
		(inState, inComponent, atIndex, inString));
		
	SJME_SDX_PASS(inComponent);
	SJME_SDX_PASS(atIndex);
	SJME_SDX_PASS(inString);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_choiceLength(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrOutNotNull sjme_jint* outLength)
{
	SJME_SDU_CHUNK(choiceLength,
		SJME_SCRITCHUI_SERIAL_UI_CHOICE_LENGTH,
		(inState, inComponent, outLength));
		
	SJME_SDX_PASS(inComponent);
	SJME_SDX_PASS(outLength);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_componentFocusGrab(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent)
{
	SJME_SDU_CHUNK(componentFocusGrab,
		SJME_SCRITCHUI_SERIAL_UI_COMPONENT_FOCUS_GRAB,
		(inState, inComponent));
		
	SJME_SDX_PASS(inComponent);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_componentFocusHas(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrOutNotNull sjme_jboolean* outHasFocus)
{
	SJME_SDU_CHUNK(componentFocusHas,
		SJME_SCRITCHUI_SERIAL_UI_COMPONENT_FOCUS_HAS,
		(inState, inComponent, outHasFocus));
		
	SJME_SDX_PASS(inComponent);
	SJME_SDX_PASS(outHasFocus);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_componentGetParent(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrOutNotNull sjme_scritchui_uiComponent* outParent)
{
	SJME_SDU_CHUNK(componentGetParent,
		SJME_SCRITCHUI_SERIAL_UI_COMPONENT_GET_PARENT,
		(inState, inComponent, outParent));
		
	SJME_SDX_PASS(inComponent);
	SJME_SDX_PASS(outParent);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_componentPosition(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrOutNullable sjme_jint* outX,
	sjme_attrOutNullable sjme_jint* outY)
{
	SJME_SDU_CHUNK(componentPosition,
		SJME_SCRITCHUI_SERIAL_UI_COMPONENT_POSITION,
		(inState, inComponent, outX, outY));
		
	SJME_SDX_PASS(inComponent);
	SJME_SDX_PASS(outX);
	SJME_SDX_PASS(outY);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_componentRepaint(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint x,
	sjme_attrInPositive sjme_jint y,
	sjme_attrInPositiveNonZero sjme_jint width,
	sjme_attrInPositiveNonZero sjme_jint height)
{
	SJME_SDU_CHUNK(componentRepaint,
		SJME_SCRITCHUI_SERIAL_UI_COMPONENT_REPAINT,
		(inState, inComponent, x, y, width, height));
		
	SJME_SDX_PASS(inComponent);
	SJME_SDX_PASS(x);
	SJME_SDX_PASS(y);
	SJME_SDX_PASS(width);
	SJME_SDX_PASS(height);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_componentRevalidate(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent)
{
	SJME_SDU_CHUNK(componentRevalidate,
		SJME_SCRITCHUI_SERIAL_UI_COMPONENT_REVALIDATE,
		(inState, inComponent));
		
	SJME_SDX_PASS(inComponent);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

SJME_SCRITCHUI_DISPATCH_GENERIC_LISTENER(componentSetActivateListener,
	SJME_SCRITCHUI_SERIAL_UI_CHOICE_SET_ACTIVATE_LISTENER,
	sjme_scritchui_uiComponent, inComponent, activate)

SJME_SCRITCHUI_DISPATCH_GENERIC_LISTENER(componentSetInputListener,
	SJME_SCRITCHUI_SERIAL_UI_COMPONENT_SET_INPUT_LISTENER,
	sjme_scritchui_uiComponent, inComponent, input)

SJME_SCRITCHUI_DISPATCH_GENERIC_LISTENER(componentSetPaintListener,
	SJME_SCRITCHUI_SERIAL_UI_COMPONENT_SET_PAINT_LISTENER,
	sjme_scritchui_uiComponent, inComponent, paint)

SJME_SCRITCHUI_DISPATCH_GENERIC_LISTENER(componentSetSizeListener,
	SJME_SCRITCHUI_SERIAL_UI_COMPONENT_SET_SIZE_LISTENER,
	sjme_scritchui_uiComponent, inComponent, size)

SJME_SCRITCHUI_DISPATCH_GENERIC_LISTENER(componentSetValueUpdateListener,
	SJME_SCRITCHUI_SERIAL_UI_COMPONENT_SET_VALUE_UPDATE_LISTENER,
	sjme_scritchui_uiComponent, inComponent, valueUpdate)

SJME_SCRITCHUI_DISPATCH_GENERIC_LISTENER(componentSetVisibleListener,
	SJME_SCRITCHUI_SERIAL_UI_COMPONENT_SET_VISIBLE_LISTENER,
	sjme_scritchui_uiComponent, inComponent, visible)

sjme_errorCode sjme_scritchui_coreSerial_componentSize(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrOutNullable sjme_jint* outWidth,
	sjme_attrOutNullable sjme_jint* outHeight)
{
	SJME_SDU_CHUNK(componentSize,
		SJME_SCRITCHUI_SERIAL_UI_COMPONENT_SIZE,
		(inState, inComponent, outWidth, outHeight));
		
	SJME_SDX_PASS(inComponent);
	SJME_SDX_PASS(outWidth);
	SJME_SDX_PASS(outHeight);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_containerAdd(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inContainer,
	sjme_attrInNotNull sjme_scritchui_uiComponent addComponent)
{
	SJME_SDU_CHUNK(containerAdd,
		SJME_SCRITCHUI_SERIAL_UI_CONTAINER_ADD,
		(inState, inContainer, addComponent));
		
	SJME_SDX_PASS(inContainer);
	SJME_SDX_PASS(addComponent);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

	
sjme_errorCode sjme_scritchui_coreSerial_containerGetFrame(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inContainer,
	sjme_attrOutNullable sjme_scritchui_dim* contentSize,
	sjme_attrOutNullable sjme_scritchui_rect* frameBound,
	sjme_attrOutNullable sjme_scritchui_rect* contentBound)
{
	SJME_SDU_CHUNK(containerGetFrame,
		SJME_SCRITCHUI_SERIAL_UI_CONTAINER_GET_FRAME,
		(inState, inContainer, contentSize, frameBound, contentBound));
		
	SJME_SDX_PASS(inContainer);
	SJME_SDX_PASS(contentSize);
	SJME_SDX_PASS(frameBound);
	SJME_SDX_PASS(contentBound);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_containerRemove(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inContainer,
	sjme_attrInNotNull sjme_scritchui_uiComponent removeComponent)
{
	SJME_SDU_CHUNK(containerRemove,
		SJME_SCRITCHUI_SERIAL_UI_CONTAINER_REMOVE,
		(inState, inContainer, removeComponent));
		
	SJME_SDX_PASS(inContainer);
	SJME_SDX_PASS(removeComponent);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_containerRemoveAll(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inContainer)
{
	SJME_SDU_CHUNK(containerRemoveAll,
		SJME_SCRITCHUI_SERIAL_UI_CONTAINER_REMOVE_ALL,
		(inState, inContainer));
		
	SJME_SDX_PASS(inContainer);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
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
	SJME_SDU_CHUNK(containerSetBounds,
		SJME_SCRITCHUI_SERIAL_UI_CONTAINER_SET_BOUNDS,
		(inState, inContainer, inComponent, x, y, width, height));
		
	SJME_SDX_PASS(inContainer);
	SJME_SDX_PASS(inComponent);
	SJME_SDX_PASS(x);
	SJME_SDX_PASS(y);
	SJME_SDX_PASS(width);
	SJME_SDX_PASS(height);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_fontBuiltin(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrOutNotNull sjme_scritchui_pencilFont* outFont)
{
	SJME_SDU_CHUNK(fontBuiltin,
		SJME_SCRITCHUI_SERIAL_UI_FONT_BUILTIN,
		(inState, outFont));
		
	SJME_SDX_PASS(outFont);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_fontByFace(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrOutNotNull sjme_scritchui_pencilFont* outFont,
	sjme_attrOutNullable sjme_scritchui_pencilFontParam* outParams,
	sjme_attrInValue sjme_scritchui_pencilFontFace inFace,
	sjme_attrInNullable const sjme_scritchui_pencilFontParam* inParams)
{
	SJME_SDU_CHUNK(fontByFace,
		SJME_SCRITCHUI_SERIAL_UI_FONT_BY_FACE,
		(inState, outFont, outParams, inFace, inParams));

	SJME_SDX_PASS(outFont);
	SJME_SDX_PASS(outParams);
	SJME_SDX_PASS(inFace);
	SJME_SDX_PASS(inParams);

	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_fontCount(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_jint* outCount)
{
	SJME_SDU_CHUNK(fontCount,
		SJME_SCRITCHUI_SERIAL_UI_FONT_COUNT,
		(inState, outCount));
		
	SJME_SDX_PASS(outCount);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_fontDerive(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrOutNotNull sjme_scritchui_pencilFont* newFont,
	sjme_attrOutNullable sjme_scritchui_pencilFontParam* newParams,
	sjme_attrInNotNull sjme_scritchui_pencilFont oldFont,
	sjme_attrInNullable const sjme_scritchui_pencilFontParam* deriveParams)
{
	SJME_SDU_CHUNK(fontDerive,
		SJME_SCRITCHUI_SERIAL_UI_FONT_DERIVE,
		(inState, newFont, newParams, oldFont, deriveParams));
		
	SJME_SDX_PASS(newFont);
	SJME_SDX_PASS(newParams);
	SJME_SDX_PASS(oldFont);
	SJME_SDX_PASS(deriveParams);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_fontList(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrOutNotNull sjme_list(sjme_scritchui_pencilFont)* outFonts,
	sjme_attrOutNotNull sjme_jint* outValid,
	sjme_attrOutNullable sjme_jint* outCount)
{
	SJME_SDU_CHUNK(fontList,
		SJME_SCRITCHUI_SERIAL_UI_FONT_LIST,
		(inState, outFonts, outValid, outCount));
		
	SJME_SDX_PASS(outFonts);
	SJME_SDX_PASS(outValid);
	SJME_SDX_PASS(outCount);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_hardwareGraphics(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrOutNotNull sjme_scritchui_pencil* outPencil,
	sjme_attrOutNullable sjme_alloc_weak* outWeakPencil,
	sjme_attrInValue sjme_gfx_pixelFormat pf,
	sjme_attrInPositiveNonZero sjme_jint bw,
	sjme_attrInPositiveNonZero sjme_jint bh,
	sjme_attrInNullable const sjme_scritchui_pencilLockFunctions* inLockFuncs,
	sjme_attrInNullable const sjme_frontEndBindable* inLockFrontEndCopy,
	sjme_attrInValue sjme_jint sx,
	sjme_attrInValue sjme_jint sy,
	sjme_attrInPositiveNonZero sjme_jint sw,
	sjme_attrInPositiveNonZero sjme_jint sh,
	sjme_attrInNullable const sjme_frontEndBindable* pencilFrontEndCopy)
{
	SJME_SDU_CHUNK(hardwareGraphics,
		SJME_SCRITCHUI_SERIAL_UI_HARDWARE_GRAPHICS,
		(inState, outPencil, outWeakPencil, pf, bw, bh,
			inLockFuncs, inLockFrontEndCopy,
			sx, sy, sw, sh, pencilFrontEndCopy));
		
	SJME_SDX_PASS(outPencil);
	SJME_SDX_PASS(outWeakPencil);
	SJME_SDX_PASS(pf);
	SJME_SDX_PASS(bw);
	SJME_SDX_PASS(bh);
	SJME_SDX_PASS(inLockFuncs);
	SJME_SDX_PASS(inLockFrontEndCopy);
	SJME_SDX_PASS(sx);
	SJME_SDX_PASS(sy);
	SJME_SDX_PASS(sw);
	SJME_SDX_PASS(sh);
	SJME_SDX_PASS(pencilFrontEndCopy);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}
	
sjme_errorCode sjme_scritchui_coreSerial_labelSetString(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiCommon inCommon,
	sjme_attrInNullable sjme_lpcstr inString)
{
	SJME_SDU_CHUNK(labelSetString,
		SJME_SCRITCHUI_SERIAL_UI_LABEL_SET_STRING,
		(inState, inCommon, inString));
		
	SJME_SDX_PASS(inCommon);
	SJME_SDX_PASS(inString);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_lafDpiProject(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNullable sjme_scritchui_uiComponent inContext,
	sjme_attrInValue sjme_jboolean toBase,
	sjme_attrInNullable sjme_jint* inOutX,
	sjme_attrInNullable sjme_jint* inOutY,
	sjme_attrInNullable sjme_jint* inOutW,
	sjme_attrInNullable sjme_jint* inOutH)
{
	SJME_SDU_CHUNK(lafDpiProject,
		SJME_SCRITCHUI_SERIAL_UI_LAF_DPI_PROJECT,
		(inState, inContext, toBase, inOutX, inOutY, inOutW, inOutH));
		
	SJME_SDX_PASS(inContext);
	SJME_SDX_PASS(toBase);
	SJME_SDX_PASS(inOutX);
	SJME_SDX_PASS(inOutY);
	SJME_SDX_PASS(inOutW);
	SJME_SDX_PASS(inOutH);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_lafElementColor(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNullable sjme_scritchui_uiComponent inContext,
	sjme_attrOutNotNull sjme_jint* outRGB,
	sjme_attrInValue sjme_scritchui_lafElementColorType elementColor)
{
	SJME_SDU_CHUNK(lafElementColor,
		SJME_SCRITCHUI_SERIAL_UI_LAF_ELEMENT_COLOR,
		(inState, inContext, outRGB, elementColor));
		
	SJME_SDX_PASS(inContext);
	SJME_SDX_PASS(outRGB);
	SJME_SDX_PASS(elementColor);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_lafMetric(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNullable sjme_scritchui_uiComponent inContext,
	sjme_attrOutNotNull sjme_jint* outValue,
	sjme_attrInValue sjme_scritchui_lafMetricType metricType)
{
	SJME_SDU_CHUNK(lafMetric,
		SJME_SCRITCHUI_SERIAL_UI_LAF_METRIC,
		(inState, inContext, outValue, metricType));

	SJME_SDX_PASS(inContext);
	SJME_SDX_PASS(outValue);
	SJME_SDX_PASS(metricType);

	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_listNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_uiList* outList,
	sjme_attrInValue sjme_scritchui_choiceType inChoiceType)
{
	SJME_SDU_CHUNK(listNew,
		SJME_SCRITCHUI_SERIAL_UI_LIST_NEW,
		(inState, outList, inChoiceType));
		
	SJME_SDX_PASS(outList);
	SJME_SDX_PASS(inChoiceType);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_menuBarNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_uiMenuBar* outMenuBar)
{
	SJME_SDU_CHUNK(menuBarNew,
		SJME_SCRITCHUI_SERIAL_UI_MENU_BAR_NEW,
		(inState, outMenuBar));
		
	SJME_SDX_PASS(outMenuBar);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_menuInsert(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiMenuKind intoMenu,
	sjme_attrInPositive sjme_jint atIndex,
	sjme_attrInNotNull sjme_scritchui_uiMenuKind childItem)
{
	SJME_SDU_CHUNK(menuInsert,
		SJME_SCRITCHUI_SERIAL_UI_MENU_INSERT,
		(inState, intoMenu, atIndex, childItem));
		
	SJME_SDX_PASS(intoMenu);
	SJME_SDX_PASS(atIndex);
	SJME_SDX_PASS(childItem);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_menuItemNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_uiMenuItem* outMenuItem)
{
	SJME_SDU_CHUNK(menuItemNew,
		SJME_SCRITCHUI_SERIAL_UI_MENU_ITEM_NEW,
		(inState, outMenuItem));
		
	SJME_SDX_PASS(outMenuItem);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_menuNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_uiMenu* outMenu)
{
	SJME_SDU_CHUNK(menuNew,
		SJME_SCRITCHUI_SERIAL_UI_MENU_NEW,
		(inState, outMenu));
		
	SJME_SDX_PASS(outMenu);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_menuRemove(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiMenuKind fromMenu,
	sjme_attrInPositive sjme_jint atIndex)
{
	SJME_SDU_CHUNK(menuRemove,
		SJME_SCRITCHUI_SERIAL_UI_MENU_REMOVE,
		(inState, fromMenu, atIndex));
		
	SJME_SDX_PASS(fromMenu);
	SJME_SDX_PASS(atIndex);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_menuRemoveAll(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiMenuKind fromMenu)
{
	SJME_SDU_CHUNK(menuRemoveAll,
		SJME_SCRITCHUI_SERIAL_UI_MENU_REMOVE_ALL,
		(inState, fromMenu));
		
	SJME_SDX_PASS(fromMenu);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_objectDelete(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_uiCommon* inOutObject)
{
	SJME_SDU_CHUNK(objectDelete,
		SJME_SCRITCHUI_SERIAL_UI_OBJECT_DELETE,
		(inState, inOutObject));
		
	SJME_SDX_PASS(inOutObject);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_panelEnableFocus(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiPanel inPanel,
	sjme_attrInValue sjme_jboolean enableFocus,
	sjme_attrInValue sjme_jboolean defaultFocus)
{
	SJME_SDU_CHUNK(panelEnableFocus,
		SJME_SCRITCHUI_SERIAL_UI_PANEL_ENABLE_FOCUS,
		(inState, inPanel, enableFocus, defaultFocus));
		
	SJME_SDX_PASS(inPanel);
	SJME_SDX_PASS(enableFocus);
	SJME_SDX_PASS(defaultFocus);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_panelNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_uiPanel* outPanel)
{
	SJME_SDU_CHUNK(panelNew,
		SJME_SCRITCHUI_SERIAL_UI_PANEL_NEW,
		(inState, outPanel));
		
	SJME_SDX_PASS(outPanel);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_pseudoGraphics(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrOutNotNull sjme_scritchui_pencil* outPencil,
	sjme_attrOutNullable sjme_alloc_weak* outWeakPencil,
	sjme_attrInNotNullBuf(numPencils) sjme_scritchui_pencil* pencils,
	sjme_attrInPositiveNonZero sjme_jint numPencils,
	sjme_attrInNullable const sjme_frontEndBindable* pencilFrontEndCopy)
{
	SJME_SDU_CHUNK(pseudoGraphics,
		SJME_SCRITCHUI_SERIAL_UI_PSEUDO_GRAPHICS,
		(inState, outPencil, outWeakPencil, pencils, numPencils,
			pencilFrontEndCopy));
		
	SJME_SDX_PASS(outPencil);
	SJME_SDX_PASS(outWeakPencil);
	SJME_SDX_PASS(pencils);
	SJME_SDX_PASS(numPencils);
	SJME_SDX_PASS(pencilFrontEndCopy);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_screenSetListener(
	sjme_attrInNotNull sjme_scritchui inState,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(screen))
{
	SJME_SDU_CHUNK(screenSetListener,
		SJME_SCRITCHUI_SERIAL_UI_SCREEN_SET_LISTENER,
		(inState, inListener, copyFrontEnd));
		
	SJME_SDX_PASS(inListener);
	SJME_SDX_PASS(copyFrontEnd);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_screenGetBounds(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiScreen inScreen,
	sjme_attrInNullable sjme_scritchui_uiComponent forComponent,
	sjme_attrOutNullable sjme_scritchui_rect* pixelBound,
	sjme_attrOutNullable sjme_scritchui_rect* mmBound)
{
	SJME_SDU_CHUNK(screenGetBounds,
		SJME_SCRITCHUI_SERIAL_UI_SCREEN_GET_BOUNDS,
		(inState, inScreen, forComponent, pixelBound, mmBound));
		
	SJME_SDX_PASS(inScreen);
	SJME_SDX_PASS(forComponent);
	SJME_SDX_PASS(pixelBound);
	SJME_SDX_PASS(mmBound);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_screens(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrOutNotNull sjme_scritchui_uiScreen* outScreens,
	sjme_attrInOutNotNull sjme_jint* inOutNumScreens)
{
	SJME_SDU_CHUNK(screens,
		SJME_SCRITCHUI_SERIAL_UI_SCREENS,
		(inState, outScreens, inOutNumScreens));
		
	SJME_SDX_PASS(outScreens);
	SJME_SDX_PASS(inOutNumScreens);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_scrollPanelNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrOutNotNull sjme_scritchui_uiScrollPanel* outScrollPanel)
{
	SJME_SDU_CHUNK(scrollPanelNew,
		SJME_SCRITCHUI_SERIAL_UI_SCROLL_PANEL_NEW,
		(inState, outScrollPanel));
	
	SJME_SDX_PASS(outScrollPanel);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_viewGetView(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrOutNotNull sjme_scritchui_rect* outViewRect)
{
	SJME_SDU_CHUNK(viewGetView,
		SJME_SCRITCHUI_SERIAL_UI_VIEW_GET_VIEW,
		(inState, inComponent, outViewRect));
	
	SJME_SDX_PASS(inComponent);
	SJME_SDX_PASS(outViewRect);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_viewSetArea(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInNotNull const sjme_scritchui_dim* inViewArea)
{
	SJME_SDU_CHUNK(viewSetArea,
		SJME_SCRITCHUI_SERIAL_UI_VIEW_SET_AREA,
		(inState, inComponent, inViewArea));
	
	SJME_SDX_PASS(inComponent);
	SJME_SDX_PASS(inViewArea);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_viewSetView(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInNotNull const sjme_scritchui_point* inViewPos)
{
	SJME_SDU_CHUNK(viewSetView,
		SJME_SCRITCHUI_SERIAL_UI_VIEW_SET_VIEW,
		(inState, inComponent, inViewPos));
	
	SJME_SDX_PASS(inComponent);
	SJME_SDX_PASS(inViewPos);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}
	
sjme_errorCode sjme_scritchui_coreSerial_viewSetSizeSuggestListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(sizeSuggest))
{
	SJME_SDU_CHUNK(viewSetSizeSuggestListener,
		SJME_SCRITCHUI_SERIAL_UI_VIEW_SET_SIZE_SUGGEST_LISTENER,
		(inState, inComponent, inListener, copyFrontEnd));
		
	SJME_SDX_PASS(inComponent);
	SJME_SDX_PASS(inListener);
	SJME_SDX_PASS(copyFrontEnd);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

SJME_SCRITCHUI_DISPATCH_GENERIC_LISTENER(viewSetViewListener,
	SJME_SCRITCHUI_SERIAL_UI_VIEW_SET_VIEW_LISTENER,
	sjme_scritchui_uiComponent, inComponent, view)

sjme_errorCode sjme_scritchui_coreSerial_windowContentMinimumSize(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInPositiveNonZero sjme_jint width,
	sjme_attrInPositiveNonZero sjme_jint height)
{
	SJME_SDU_CHUNK(windowContentMinimumSize,
		SJME_SCRITCHUI_SERIAL_UI_WINDOW_CONTENT_MINIMUM_SIZE,
		(inState, inWindow, width, height));
		
	SJME_SDX_PASS(inWindow);
	SJME_SDX_PASS(width);
	SJME_SDX_PASS(height);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}
	
sjme_errorCode sjme_scritchui_coreSerial_windowNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_uiWindow* outWindow)
{
	SJME_SDU_CHUNK(windowNew,
		SJME_SCRITCHUI_SERIAL_UI_WINDOW_NEW,
		(inState, outWindow));
		
	SJME_SDX_PASS(outWindow);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

SJME_SCRITCHUI_DISPATCH_GENERIC_LISTENER(windowSetCloseListener,
	SJME_SCRITCHUI_SERIAL_UI_WINDOW_SET_CLOSE_LISTENER,
	sjme_scritchui_uiWindow, inWindow, close)

sjme_errorCode sjme_scritchui_coreSerial_windowSetFlags(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInNotNull sjme_jint setFlags,
	sjme_attrOutNullable sjme_jint* actualFlags)
{
	SJME_SDU_CHUNK(windowSetFlags,
		SJME_SCRITCHUI_SERIAL_UI_WINDOW_SET_FLAGS,
		(inState, inWindow, setFlags, actualFlags));
		
	SJME_SDX_PASS(inWindow);
	SJME_SDX_PASS(setFlags);
	SJME_SDX_PASS(actualFlags);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_windowSetMenuBar(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInNullable sjme_scritchui_uiMenuBar inMenuBar)
{
	SJME_SDU_CHUNK(windowSetMenuBar,
		SJME_SCRITCHUI_SERIAL_UI_WINDOW_SET_MENU_BAR,
		(inState, inWindow, inMenuBar));
		
	SJME_SDX_PASS(inWindow);
	SJME_SDX_PASS(inMenuBar);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

SJME_SCRITCHUI_DISPATCH_GENERIC_LISTENER(windowSetMenuItemActivateListener,
	SJME_SCRITCHUI_SERIAL_UI_WINDOW_SET_MENU_ITEM_ACTIVATE_LISTENER,
	sjme_scritchui_uiWindow, inWindow, menuItemActivate)

sjme_errorCode sjme_scritchui_coreSerial_windowSetState(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInNotNull sjme_scritchui_windowState setState,
	sjme_attrOutNullable sjme_scritchui_windowState* actualState)
{
	SJME_SDU_CHUNK(windowSetState,
		SJME_SCRITCHUI_SERIAL_UI_WINDOW_SET_STATE,
		(inState, inWindow, setState, actualState));
		
	SJME_SDX_PASS(inWindow);
	SJME_SDX_PASS(setState);
	SJME_SDX_PASS(actualState);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_windowSetVisible(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInValue sjme_jboolean isVisible)
{
	SJME_SDU_CHUNK(windowSetVisible,
		SJME_SCRITCHUI_SERIAL_UI_WINDOW_SET_VISIBLE,
		(inState, inWindow, isVisible));
		
	SJME_SDX_PASS(inWindow);
	SJME_SDX_PASS(isVisible);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

/*--------------------------------------------------------------------------*/

sjme_errorCode sjme_scritchpen_coreSerial_close(
sjme_attrInNotNull sjme_scritchui_pencil g)
{
	SJME_SDP_CHUNK(close,
		SJME_SCRITCHUI_SERIAL_PEN_CLOSE,
		(g));
		
	SJME_SDX_PASS(g);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_attrDeprecated sjme_scritchpen_coreSerial_copyArea(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint sx,
	sjme_attrInValue sjme_jint sy,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h,
	sjme_attrInValue sjme_jint dx,
	sjme_attrInValue sjme_jint dy,
	sjme_attrInValue sjme_jint anchor)
{
	SJME_SDP_CHUNK(copyArea,
		SJME_SCRITCHUI_SERIAL_PEN_COPY_AREA,
		(g, sx, sy, w, h, dx, dy, anchor));
		
	SJME_SDX_PASS(g);
	SJME_SDX_PASS(sx);
	SJME_SDX_PASS(sy);
	SJME_SDX_PASS(w);
	SJME_SDX_PASS(h);
	SJME_SDX_PASS(dx);
	SJME_SDX_PASS(dy);
	SJME_SDX_PASS(anchor);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchpen_coreSerial_drawArc(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h,
	sjme_attrInValue sjme_jint startAngle,
	sjme_attrInValue sjme_jint arcAngle)
{
	SJME_SDP_CHUNK(drawArc,
		SJME_SCRITCHUI_SERIAL_PEN_DRAW_ARC,
		(g, x, y, w, h, startAngle, arcAngle));
		
	SJME_SDX_PASS(g);
	SJME_SDX_PASS(x);
	SJME_SDX_PASS(y);
	SJME_SDX_PASS(w);
	SJME_SDX_PASS(h);
	SJME_SDX_PASS(startAngle);
	SJME_SDX_PASS(arcAngle);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchpen_coreSerial_drawHoriz(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInValue sjme_jint w)
{
	SJME_SDP_CHUNK(drawHoriz,
		SJME_SCRITCHUI_SERIAL_PEN_DRAW_HORIZ,
		(g, x, y, w));
		
	SJME_SDX_PASS(g);
	SJME_SDX_PASS(x);
	SJME_SDX_PASS(y);
	SJME_SDX_PASS(w);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchpen_coreSerial_drawChar(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInPositive sjme_jint c,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInValue sjme_jint anchor,
	sjme_attrOutNullable sjme_jint* outCw)
{
	SJME_SDP_CHUNK(drawChar,
		SJME_SCRITCHUI_SERIAL_PEN_DRAW_CHAR,
		(g, c, x, y, anchor, outCw));
		
	SJME_SDX_PASS(g);
	SJME_SDX_PASS(c);
	SJME_SDX_PASS(x);
	SJME_SDX_PASS(y);
	SJME_SDX_PASS(anchor);
	SJME_SDX_PASS(outCw);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchpen_coreSerial_drawChars(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNull const sjme_jchar* s,
	sjme_attrInPositive sjme_jint o,
	sjme_attrInPositive sjme_jint l,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInValue sjme_jint anchor)
{
	SJME_SDP_CHUNK(drawChars,
		SJME_SCRITCHUI_SERIAL_PEN_DRAW_CHARS,
		(g, s, o, l, x, y, anchor));
		
	SJME_SDX_PASS(g);
	SJME_SDX_PASS(s);
	SJME_SDX_PASS(o);
	SJME_SDX_PASS(l);
	SJME_SDX_PASS(x);
	SJME_SDX_PASS(y);
	SJME_SDX_PASS(anchor);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchpen_coreSerial_drawLine(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x1,
	sjme_attrInValue sjme_jint y1,
	sjme_attrInValue sjme_jint x2,
	sjme_attrInValue sjme_jint y2)
{
	SJME_SDP_CHUNK(drawLine,
		SJME_SCRITCHUI_SERIAL_PEN_DRAW_LINE,
		(g, x1, y1, x2, y2));
		
	SJME_SDX_PASS(g);
	SJME_SDX_PASS(x1);
	SJME_SDX_PASS(y1);
	SJME_SDX_PASS(x2);
	SJME_SDX_PASS(y2);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchpen_coreSerial_drawPixel(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y)
{
	SJME_SDP_CHUNK(drawPixel,
		SJME_SCRITCHUI_SERIAL_PEN_DRAW_PIXEL,
		(g, x, y));
		
	SJME_SDX_PASS(g);
	SJME_SDX_PASS(x);
	SJME_SDX_PASS(y);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchpen_coreSerial_drawPolyline(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNull const sjme_jint* inXPoints,
	sjme_attrInPositive sjme_jint xOffset,
	sjme_attrInNotNull const sjme_jint* inYPoints,
	sjme_attrInPositive sjme_jint yOffset,
	sjme_attrInPositive sjme_jint nPoints)
{
	SJME_SDP_CHUNK(drawPolyline,
		SJME_SCRITCHUI_SERIAL_PEN_FILL_POLYGON,
		(g, inXPoints, xOffset,
			inYPoints, yOffset, nPoints));
		
	SJME_SDX_PASS(g);
	SJME_SDX_PASS(inXPoints);
	SJME_SDX_PASS(xOffset);
	SJME_SDX_PASS(inYPoints);
	SJME_SDX_PASS(yOffset);
	SJME_SDX_PASS(nPoints);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchpen_coreSerial_drawRect(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h)
{
	SJME_SDP_CHUNK(drawRect,
		SJME_SCRITCHUI_SERIAL_PEN_DRAW_RECT,
		(g, x, y, w, h));
		
	SJME_SDX_PASS(g);
	SJME_SDX_PASS(x);
	SJME_SDX_PASS(y);
	SJME_SDX_PASS(w);
	SJME_SDX_PASS(h);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchpen_coreSerial_drawRegion(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint pf,
	sjme_attrInNotNull sjme_cpointer data,
	sjme_attrInPositive sjme_jint off,
	sjme_attrInPositive sjme_jint dataLen,
	sjme_attrInPositive sjme_jint scanLen,
	sjme_attrInValue sjme_jboolean alpha,
	sjme_attrInValue sjme_jint xSrc,
	sjme_attrInValue sjme_jint ySrc,
	sjme_attrInPositive sjme_jint wSrc,
	sjme_attrInPositive sjme_jint hSrc,
	sjme_attrInValue sjme_jint trans,
	sjme_attrInValue sjme_jint xDest,
	sjme_attrInValue sjme_jint yDest,
	sjme_attrInValue sjme_jint anchor,
	sjme_attrInPositive sjme_jint wDest,
	sjme_attrInPositive sjme_jint hDest,
	sjme_attrInPositive sjme_jint origImgWidth,
	sjme_attrInPositive sjme_jint origImgHeight)
{
	SJME_SDP_CHUNK(drawRegion,
		SJME_SCRITCHUI_SERIAL_PEN_DRAW_REGION,
		(g, pf, data, off, dataLen, scanLen, alpha,
			xSrc, ySrc, wSrc, hSrc, trans, xDest, yDest, anchor,
			wDest, hDest, origImgWidth, origImgHeight));

	SJME_SDX_PASS(g);
	SJME_SDX_PASS(pf);
	SJME_SDX_PASS(data);
	SJME_SDX_PASS(off);
	SJME_SDX_PASS(dataLen);
	SJME_SDX_PASS(scanLen);
	SJME_SDX_PASS(alpha);
	SJME_SDX_PASS(xSrc);
	SJME_SDX_PASS(ySrc);
	SJME_SDX_PASS(wSrc);
	SJME_SDX_PASS(hSrc);
	SJME_SDX_PASS(trans);
	SJME_SDX_PASS(xDest);
	SJME_SDX_PASS(yDest);
	SJME_SDX_PASS(anchor);
	SJME_SDX_PASS(wDest);
	SJME_SDX_PASS(hDest);
	SJME_SDX_PASS(origImgWidth);
	SJME_SDX_PASS(origImgHeight);

	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchpen_coreSerial_drawRoundRect(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h,
	sjme_attrInPositive sjme_jint arcWidth,
	sjme_attrInPositive sjme_jint arcHeight)
{
	SJME_SDP_CHUNK(drawRoundRect,
		SJME_SCRITCHUI_SERIAL_PEN_FILL_ROUND_RECT,
		(g, x, y, w, h, arcWidth, arcHeight));
		
	SJME_SDX_PASS(g);
	SJME_SDX_PASS(x);
	SJME_SDX_PASS(y);
	SJME_SDX_PASS(w);
	SJME_SDX_PASS(h);
	SJME_SDX_PASS(arcWidth);
	SJME_SDX_PASS(arcHeight);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}
	
sjme_errorCode sjme_scritchpen_coreSerial_drawSubstring(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNull const sjme_charSeq s,
	sjme_attrInPositive sjme_jint o, 
	sjme_attrInPositive sjme_jint l,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInValue sjme_jint anchor)
{
	SJME_SDP_CHUNK(drawSubstring,
		SJME_SCRITCHUI_SERIAL_PEN_DRAW_SUBSTRING,
		(g, s, o, l, x, y, anchor));
		
	SJME_SDX_PASS(g);
	SJME_SDX_PASS(s);
	SJME_SDX_PASS(o);
	SJME_SDX_PASS(l);
	SJME_SDX_PASS(x);
	SJME_SDX_PASS(y);
	SJME_SDX_PASS(anchor);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchpen_coreSerial_drawTriangle(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x1,
	sjme_attrInValue sjme_jint y1,
	sjme_attrInValue sjme_jint x2,
	sjme_attrInValue sjme_jint y2,
	sjme_attrInValue sjme_jint x3,
	sjme_attrInValue sjme_jint y3)
{
	SJME_SDP_CHUNK(drawTriangle,
		SJME_SCRITCHUI_SERIAL_PEN_FILL_TRIANGLE,
		(g, x1, y1, x2, y2, x3, y3));
		
	SJME_SDX_PASS(g);
	SJME_SDX_PASS(x1);
	SJME_SDX_PASS(y1);
	SJME_SDX_PASS(x2);
	SJME_SDX_PASS(y2);
	SJME_SDX_PASS(x3);
	SJME_SDX_PASS(y3);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchpen_coreSerial_drawXRGB32Region(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNull sjme_jint* data,
	sjme_attrInPositive sjme_jint off,
	sjme_attrInPositive sjme_jint dataLen,
	sjme_attrInPositive sjme_jint scanLen,
	sjme_attrInValue sjme_jboolean alpha,
	sjme_attrInValue sjme_jint xSrc,
	sjme_attrInValue sjme_jint ySrc,
	sjme_attrInPositive sjme_jint wSrc,
	sjme_attrInPositive sjme_jint hSrc,
	sjme_attrInValue sjme_jint trans,
	sjme_attrInValue sjme_jint xDest,
	sjme_attrInValue sjme_jint yDest,
	sjme_attrInValue sjme_jint anchor,
	sjme_attrInPositive sjme_jint wDest,
	sjme_attrInPositive sjme_jint hDest,
	sjme_attrInPositive sjme_jint origImgWidth,
	sjme_attrInPositive sjme_jint origImgHeight)
{
	SJME_SDP_CHUNK(drawXRGB32Region,
		SJME_SCRITCHUI_SERIAL_PEN_DRAW_XRGB32REGION,
		(g, data, off, dataLen, scanLen, alpha,
			xSrc, ySrc, wSrc, hSrc, trans, xDest, yDest,
			anchor, wDest, hDest, origImgWidth, origImgHeight));
		
	SJME_SDX_PASS(g);
	SJME_SDX_PASS(data);
	SJME_SDX_PASS(off);
	SJME_SDX_PASS(dataLen);
	SJME_SDX_PASS(scanLen);
	SJME_SDX_PASS(alpha);
	SJME_SDX_PASS(xSrc);
	SJME_SDX_PASS(ySrc);
	SJME_SDX_PASS(wSrc);
	SJME_SDX_PASS(hSrc);
	SJME_SDX_PASS(trans);
	SJME_SDX_PASS(xDest);
	SJME_SDX_PASS(yDest);
	SJME_SDX_PASS(anchor);
	SJME_SDX_PASS(wDest);
	SJME_SDX_PASS(hDest);
	SJME_SDX_PASS(origImgWidth);
	SJME_SDX_PASS(origImgHeight);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchpen_coreSerial_fillArc(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h,
	sjme_attrInValue sjme_jint startAngle,
	sjme_attrInValue sjme_jint arcAngle)
{
	SJME_SDP_CHUNK(fillArc,
		SJME_SCRITCHUI_SERIAL_PEN_FILL_ARC,
		(g, x, y, w, h, startAngle, arcAngle));
		
	SJME_SDX_PASS(g);
	SJME_SDX_PASS(x);
	SJME_SDX_PASS(y);
	SJME_SDX_PASS(w);
	SJME_SDX_PASS(h);
	SJME_SDX_PASS(startAngle);
	SJME_SDX_PASS(arcAngle);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchpen_coreSerial_fillPolygon(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNull const sjme_jint* inXPoints,
	sjme_attrInPositive sjme_jint xOffset,
	sjme_attrInNotNull const sjme_jint* inYPoints,
	sjme_attrInPositive sjme_jint yOffset,
	sjme_attrInPositive sjme_jint nPoints)
{
	SJME_SDP_CHUNK(fillPolygon,
		SJME_SCRITCHUI_SERIAL_PEN_FILL_POLYGON,
		(g, inXPoints, xOffset,
			inYPoints, yOffset, nPoints));
		
	SJME_SDX_PASS(g);
	SJME_SDX_PASS(inXPoints);
	SJME_SDX_PASS(xOffset);
	SJME_SDX_PASS(inYPoints);
	SJME_SDX_PASS(yOffset);
	SJME_SDX_PASS(nPoints);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchpen_coreSerial_fillRect(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h)
{
	SJME_SDP_CHUNK(fillRect,
		SJME_SCRITCHUI_SERIAL_PEN_FILL_RECT,
		(g, x, y, w, h));
		
	SJME_SDX_PASS(g);
	SJME_SDX_PASS(x);
	SJME_SDX_PASS(y);
	SJME_SDX_PASS(w);
	SJME_SDX_PASS(h);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchpen_coreSerial_fillRoundRect(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h,
	sjme_attrInPositive sjme_jint arcWidth,
	sjme_attrInPositive sjme_jint arcHeight)
{
	SJME_SDP_CHUNK(fillRoundRect,
		SJME_SCRITCHUI_SERIAL_PEN_FILL_ROUND_RECT,
		(g, x, y, w, h, arcWidth, arcHeight));
		
	SJME_SDX_PASS(g);
	SJME_SDX_PASS(x);
	SJME_SDX_PASS(y);
	SJME_SDX_PASS(w);
	SJME_SDX_PASS(h);
	SJME_SDX_PASS(arcWidth);
	SJME_SDX_PASS(arcHeight);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchpen_coreSerial_fillTriangle(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x1,
	sjme_attrInValue sjme_jint y1,
	sjme_attrInValue sjme_jint x2,
	sjme_attrInValue sjme_jint y2,
	sjme_attrInValue sjme_jint x3,
	sjme_attrInValue sjme_jint y3)
{
	SJME_SDP_CHUNK(fillTriangle,
		SJME_SCRITCHUI_SERIAL_PEN_FILL_TRIANGLE,
		(g, x1, y1, x2, y2, x3, y3));
		
	SJME_SDX_PASS(g);
	SJME_SDX_PASS(x1);
	SJME_SDX_PASS(y1);
	SJME_SDX_PASS(x2);
	SJME_SDX_PASS(y2);
	SJME_SDX_PASS(x3);
	SJME_SDX_PASS(y3);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchpen_coreSerial_getRegion(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint pf,
	sjme_attrInNotNull sjme_cpointer data,
	sjme_attrInPositive sjme_jint off,
	sjme_attrInPositive sjme_jint dataLen,
	sjme_attrInPositive sjme_jint scanLen,
	sjme_attrInValue sjme_jboolean alpha,
	sjme_attrInValue sjme_jint xSrc,
	sjme_attrInValue sjme_jint ySrc,
	sjme_attrInPositive sjme_jint wSrc,
	sjme_attrInPositive sjme_jint hSrc,
	sjme_attrInValue sjme_jint anchor)
{
	SJME_SDP_CHUNK(getRegion,
		SJME_SCRITCHUI_SERIAL_PEN_GET_REGION,
		(g, pf, data, off, dataLen, scanLen,
			alpha, xSrc, ySrc, wSrc, hSrc, anchor));

	SJME_SDX_PASS(g);
	SJME_SDX_PASS(pf);
	SJME_SDX_PASS(data);
	SJME_SDX_PASS(off);
	SJME_SDX_PASS(dataLen);
	SJME_SDX_PASS(scanLen);
	SJME_SDX_PASS(alpha);
	SJME_SDX_PASS(xSrc);
	SJME_SDX_PASS(ySrc);
	SJME_SDX_PASS(wSrc);
	SJME_SDX_PASS(hSrc);
	SJME_SDX_PASS(anchor);

	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchpen_coreSerial_mapColor(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jboolean fromRaw,
	sjme_attrInValue sjme_jint inRgbOrRaw,
	sjme_attrOutNotNull sjme_scritchui_color* outColor)
{
	SJME_SDP_CHUNK(mapColor,
		SJME_SCRITCHUI_SERIAL_PEN_MAP_COLOR,
		(g, fromRaw, inRgbOrRaw, outColor));
		
	SJME_SDX_PASS(g);
	SJME_SDX_PASS(fromRaw);
	SJME_SDX_PASS(inRgbOrRaw);
	SJME_SDX_PASS(outColor);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchpen_coreSerial_setAlphaColor(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint argb)
{
	SJME_SDP_CHUNK(setAlphaColor,
		SJME_SCRITCHUI_SERIAL_PEN_SET_ALPHA_COLOR,
		(g, argb));
		
	SJME_SDX_PASS(g);
	SJME_SDX_PASS(argb);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchpen_coreSerial_setBlendingMode(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInRange(0, SJME_NUM_SCRITCHUI_PENCIL_BLENDS)
	sjme_scritchui_pencilBlendingMode mode)
{
	SJME_SDP_CHUNK(setBlendingMode,
		SJME_SCRITCHUI_SERIAL_PEN_SET_BLENDING_MODE,
		(g, mode));
		
	SJME_SDX_PASS(g);
	SJME_SDX_PASS(mode);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchpen_coreSerial_setClip(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h)
{
	SJME_SDP_CHUNK(setClip,
		SJME_SCRITCHUI_SERIAL_PEN_SET_CLIP,
		(g, x, y, w, h));
		
	SJME_SDX_PASS(g);
	SJME_SDX_PASS(x);
	SJME_SDX_PASS(y);
	SJME_SDX_PASS(w);
	SJME_SDX_PASS(h);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchpen_coreSerial_setDefaultFont(
sjme_attrInNotNull sjme_scritchui_pencil g)
{
	SJME_SDP_CHUNK(setDefaultFont,
		SJME_SCRITCHUI_SERIAL_PEN_SET_DEFAULT_FONT,
		(g));
		
	SJME_SDX_PASS(g);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}
	
sjme_errorCode sjme_scritchpen_coreSerial_setDefaults(
sjme_attrInNotNull sjme_scritchui_pencil g)
{
	SJME_SDP_CHUNK(setDefaults,
		SJME_SCRITCHUI_SERIAL_PEN_SET_DEFAULTS,
		(g));
		
	SJME_SDX_PASS(g);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}
	
sjme_errorCode sjme_scritchpen_coreSerial_setStrokeStyle(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInRange(0, SJME_NUM_SCRITCHUI_PENCIL_STROKES)
	sjme_scritchui_pencilStrokeMode style)
{
	SJME_SDP_CHUNK(setStrokeStyle,
		SJME_SCRITCHUI_SERIAL_PEN_SET_STROKE_STYLE,
		(g, style));
		
	SJME_SDX_PASS(g);
	SJME_SDX_PASS(style);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}
	
sjme_errorCode sjme_scritchpen_coreSerial_setFont(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNull sjme_scritchui_pencilFont font,
	sjme_attrInNullable const sjme_scritchui_pencilFontParam* params)
{
	SJME_SDP_CHUNK(setFont,
		SJME_SCRITCHUI_SERIAL_PEN_SET_FONT,
		(g, font, params));
		
	SJME_SDX_PASS(g);
	SJME_SDX_PASS(font);
	SJME_SDX_PASS(params);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchpen_coreSerial_setParametersFrom(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNull sjme_scritchui_pencil from)
{
	SJME_SDP_CHUNK(setParametersFrom,
		SJME_SCRITCHUI_SERIAL_PEN_SET_PARAMETERS_FROM,
		(g, from));
		
	SJME_SDX_PASS(g);
	SJME_SDX_PASS(from);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchpen_coreSerial_transferRegion(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNull sjme_scritchui_pencil srcPencil,
	sjme_attrInValue sjme_jboolean alpha,
	sjme_attrInValue sjme_jint xSrc,
	sjme_attrInValue sjme_jint ySrc,
	sjme_attrInPositive sjme_jint wSrc,
	sjme_attrInPositive sjme_jint hSrc,
	sjme_attrInValue sjme_jint trans,
	sjme_attrInValue sjme_jint xDest,
	sjme_attrInValue sjme_jint yDest,
	sjme_attrInValue sjme_jint anchor,
	sjme_attrInPositive sjme_jint wDest,
	sjme_attrInPositive sjme_jint hDest,
	sjme_attrInValue sjme_scritchui_transferRegionMode mode)
{
	SJME_SDP_CHUNK(transferRegion,
		SJME_SCRITCHUI_SERIAL_PEN_TRANSFER_REGION,
		(g, srcPencil, alpha, xSrc, ySrc, wSrc, hSrc,
			trans, xDest, yDest, anchor, wDest, hDest, mode));
		
	SJME_SDX_PASS(g);
	SJME_SDX_PASS(srcPencil);
	SJME_SDX_PASS(alpha);
	SJME_SDX_PASS(xSrc);
	SJME_SDX_PASS(ySrc);
	SJME_SDX_PASS(wSrc);
	SJME_SDX_PASS(hSrc);
	SJME_SDX_PASS(trans);
	SJME_SDX_PASS(xDest);
	SJME_SDX_PASS(yDest);
	SJME_SDX_PASS(anchor);
	SJME_SDX_PASS(wDest);
	SJME_SDX_PASS(hDest);
	SJME_SDX_PASS(mode);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchpen_coreSerial_translate(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y)
{
	SJME_SDP_CHUNK(translate,
		SJME_SCRITCHUI_SERIAL_PEN_TRANSLATE,
		(g, x, y));
		
	SJME_SDX_PASS(g);
	SJME_SDX_PASS(x);
	SJME_SDX_PASS(y);
	
	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

#pragma region(font)

static sjme_errorCode sjme_scritchui_coreSerial_fontEqualsSub(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInNullable sjme_scritchui_pencilFont a,
	sjme_attrInNullable const sjme_scritchui_pencilFontParam* aParams,
	sjme_attrInNullable sjme_scritchui_pencilFont b,
	sjme_attrInNullable const sjme_scritchui_pencilFontParam* bParams)
{
	SJME_SDF_CHUNK(equals,
		SJME_SCRITCHUI_SERIAL_FONT_EQUALS,
		(a, aParams, b, bParams));

	SJME_SDX_PASS(a);
	SJME_SDX_PASS(aParams);
	SJME_SDX_PASS(b);
	SJME_SDX_PASS(bParams);

	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_fontEquals(
	sjme_attrInNullable sjme_scritchui_pencilFont a,
	sjme_attrInNullable const sjme_scritchui_pencilFontParam* aParams,
	sjme_attrInNullable sjme_scritchui_pencilFont b,
	sjme_attrInNullable const sjme_scritchui_pencilFontParam* bParams)
{
	sjme_scritchui_pencilFont inFont;

	/* We need a font to grab the UI state from, this is so we do not need */
	/* to do any major rework for this one single special case for equality. */
	if (a == NULL)
	{
		if (b == NULL)
			return SJME_ERROR_NOT_MATCHED;
		inFont = b;
	}
	else
		inFont = a;

	/* Forward call. */
	return sjme_scritchui_coreSerial_fontEqualsSub(inFont,
		a, aParams, b, bParams);
}

sjme_errorCode sjme_scritchui_coreSerial_fontMetricCharDirection(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInPositive sjme_jint inCodepoint,
	sjme_attrOutNotNull sjme_attrInRange(-1, 1) sjme_jint* outDirection)
{
	SJME_SDF_CHUNK(metricCharDirection,
		SJME_SCRITCHUI_SERIAL_FONT_METRIC_CHAR_DIRECTION,
		(inFont, inCodepoint, outDirection));

	SJME_SDX_PASS(inFont);
	SJME_SDX_PASS(inCodepoint);
	SJME_SDX_PASS(outDirection);

	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_fontMetricCharValid(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInPositive sjme_jint inCodepoint,
	sjme_attrOutNotNull sjme_jboolean* outValid)
{
	SJME_SDF_CHUNK(metricCharValid,
		SJME_SCRITCHUI_SERIAL_FONT_METRIC_CHAR_VALID,
		(inFont, inCodepoint, outValid));

	SJME_SDX_PASS(inFont);
	SJME_SDX_PASS(inCodepoint);
	SJME_SDX_PASS(outValid);

	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_fontMetricFontFace(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrOutNotNull sjme_scritchui_pencilFontFace* outFace)
{
	SJME_SDF_CHUNK(metricFontFace,
		SJME_SCRITCHUI_SERIAL_FONT_METRIC_FONT_FACE,
		(inFont, outFace));

	SJME_SDX_PASS(inFont);
	SJME_SDX_PASS(outFace);

	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_fontMetricFontName(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInOutNotNull sjme_lpcstr* outName)
{
	SJME_SDF_CHUNK(metricFontName,
		SJME_SCRITCHUI_SERIAL_FONT_METRIC_FONT_NAME,
		(inFont, outName));

	SJME_SDX_PASS(inFont);
	SJME_SDX_PASS(outName);

	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_fontMetricFontStyle(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrOutNotNull sjme_scritchui_pencilFontStyle* outStyle)
{
	SJME_SDF_CHUNK(metricFontStyle,
		SJME_SCRITCHUI_SERIAL_FONT_METRIC_FONT_STYLE,
		(inFont, outStyle));

	SJME_SDX_PASS(inFont);
	SJME_SDX_PASS(outStyle);

	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_fontMetricPixelAscent(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInNullable const sjme_scritchui_pencilFontParam* inParams,
	sjme_attrInValue sjme_jboolean isMax,
	sjme_attrOutNotNull sjme_jint* outAscent)
{
	SJME_SDF_CHUNK(metricPixelAscent,
		SJME_SCRITCHUI_SERIAL_FONT_METRIC_PIXEL_ASCENT,
		(inFont, inParams, isMax, outAscent));

	SJME_SDX_PASS(inFont);
	SJME_SDX_PASS(inParams);
	SJME_SDX_PASS(isMax);
	SJME_SDX_PASS(outAscent);

	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_fontMetricPixelBaseline(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInNullable const sjme_scritchui_pencilFontParam* inParams,
	sjme_attrOutNotNull sjme_jint* outBaseline)
{
	SJME_SDF_CHUNK(metricPixelBaseline,
		SJME_SCRITCHUI_SERIAL_FONT_METRIC_PIXEL_BASELINE,
		(inFont, inParams, outBaseline));

	SJME_SDX_PASS(inFont);
	SJME_SDX_PASS(inParams);
	SJME_SDX_PASS(outBaseline);

	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_fontMetricPixelDescent(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInNullable const sjme_scritchui_pencilFontParam* inParams,
	sjme_attrInValue sjme_jboolean isMax,
	sjme_attrOutNotNull sjme_jint* outDescent)
{
	SJME_SDF_CHUNK(metricPixelDescent,
		SJME_SCRITCHUI_SERIAL_FONT_METRIC_PIXEL_DESCENT,
		(inFont, inParams, isMax, outDescent));

	SJME_SDX_PASS(inFont);
	SJME_SDX_PASS(inParams);
	SJME_SDX_PASS(isMax);
	SJME_SDX_PASS(outDescent);

	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_fontMetricPixelHeight(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInNullable const sjme_scritchui_pencilFontParam* inParams,
	sjme_attrOutNotNull sjme_jint* outHeight)
{
	SJME_SDF_CHUNK(metricPixelHeight,
		SJME_SCRITCHUI_SERIAL_FONT_METRIC_PIXEL_HEIGHT,
		(inFont, inParams, outHeight));

	SJME_SDX_PASS(inFont);
	SJME_SDX_PASS(inParams);
	SJME_SDX_PASS(outHeight);

	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_fontMetricPixelLeading(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInNullable const sjme_scritchui_pencilFontParam* inParams,
	sjme_attrOutNotNull sjme_attrOutPositiveNonZero sjme_jint* outLeading)
{
	SJME_SDF_CHUNK(metricPixelLeading,
		SJME_SCRITCHUI_SERIAL_FONT_METRIC_PIXEL_LEADING,
		(inFont, inParams, outLeading));

	SJME_SDX_PASS(inFont);
	SJME_SDX_PASS(inParams);
	SJME_SDX_PASS(outLeading);

	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_fontMetricPixelSize(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInNullable const sjme_scritchui_pencilFontParam* inParams,
	sjme_attrInNegativeOnePositive sjme_jint inCodepoint,
	sjme_attrOutNotNull sjme_attrOutPositiveNonZero sjme_jint* outSize)
{
	SJME_SDF_CHUNK(metricPixelSize,
		SJME_SCRITCHUI_SERIAL_FONT_METRIC_PIXEL_SIZE,
		(inFont, inParams, inCodepoint, outSize));

	SJME_SDX_PASS(inFont);
	SJME_SDX_PASS(inParams);
	SJME_SDX_PASS(inCodepoint);
	SJME_SDX_PASS(outSize);

	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_fontPixelCharWidth(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInNullable const sjme_scritchui_pencilFontParam* inParams,
	sjme_attrInPositive sjme_jint inCodepoint,
	sjme_attrOutNotNull sjme_attrOutPositiveNonZero sjme_jint* outWidth)
{
	SJME_SDF_CHUNK(pixelCharWidth,
		SJME_SCRITCHUI_SERIAL_FONT_PIXEL_CHAR_WIDTH,
		(inFont, inParams, inCodepoint, outWidth));

	SJME_SDX_PASS(inFont);
	SJME_SDX_PASS(inParams);
	SJME_SDX_PASS(inCodepoint);
	SJME_SDX_PASS(outWidth);

	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_fontRenderBitmap(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInNullable const sjme_scritchui_pencilFontParam* inParams,
	sjme_attrInPositive sjme_jint inCodepoint,
	sjme_attrInNotNull sjme_jubyte* buf,
	sjme_attrInPositive sjme_jint bufOff,
	sjme_attrInPositive sjme_jint bufScanLen,
	sjme_attrInPositive sjme_jint bufHeight,
	sjme_attrOutNullable sjme_jint* outOffX,
	sjme_attrOutNullable sjme_jint* outOffY)
{
	SJME_SDF_CHUNK(renderBitmap,
		SJME_SCRITCHUI_SERIAL_FONT_RENDER_BITMAP,
		(inFont, inParams, inCodepoint, buf, bufOff, bufScanLen, bufHeight,
			outOffX, outOffY));

	SJME_SDX_PASS(inFont);
	SJME_SDX_PASS(inParams);
	SJME_SDX_PASS(inCodepoint);
	SJME_SDX_PASS(buf);
	SJME_SDX_PASS(bufOff);
	SJME_SDX_PASS(bufScanLen);
	SJME_SDX_PASS(bufHeight);
	SJME_SDX_PASS(outOffX);
	SJME_SDX_PASS(outOffY);

	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_fontRenderChar(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInNullable const sjme_scritchui_pencilFontParam* inParams,
	sjme_attrInPositive sjme_jint inCodepoint,
	sjme_attrInNotNull sjme_scritchui_pencil inPencil,
	sjme_attrInValue sjme_jint xPos,
	sjme_attrInNotNull sjme_jint yPos,
	sjme_attrOutNullable sjme_jint* nextXPos,
	sjme_attrOutNullable sjme_jint* nextYPos)
{
	SJME_SDF_CHUNK(renderChar,
		SJME_SCRITCHUI_SERIAL_FONT_RENDER_CHAR,
		(inFont, inParams, inCodepoint, inPencil, xPos, yPos,
			nextXPos, nextYPos));

	SJME_SDX_PASS(inFont);
	SJME_SDX_PASS(inParams);
	SJME_SDX_PASS(inCodepoint);
	SJME_SDX_PASS(inPencil);
	SJME_SDX_PASS(xPos);
	SJME_SDX_PASS(yPos);
	SJME_SDX_PASS(nextXPos);
	SJME_SDX_PASS(nextYPos);

	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

sjme_errorCode sjme_scritchui_coreSerial_fontStringWidth(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInNullable const sjme_scritchui_pencilFontParam* inParams,
	sjme_attrInNotNull const sjme_charSeq s,
	sjme_attrInPositive sjme_jint o,
	sjme_attrInPositive sjme_jint l,
	sjme_attrOutNotNull sjme_jint* outWidth)
{
	SJME_SDF_CHUNK(stringWidth,
		SJME_SCRITCHUI_SERIAL_FONT_STRING_WIDTH,
		(inFont, inParams, s, o, l, outWidth));

	SJME_SDX_PASS(inFont);
	SJME_SDX_PASS(inParams);
	SJME_SDX_PASS(s);
	SJME_SDX_PASS(o);
	SJME_SDX_PASS(l);
	SJME_SDX_PASS(outWidth);

	/* Invoke and wait. */
	SJME_SDX_WAIT;
}

#pragma endregion(font)

