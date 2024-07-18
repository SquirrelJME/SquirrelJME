// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.jvm.mle.brackets.UIDisplayBracket;
import cc.squirreljme.jvm.mle.brackets.UIDrawableBracket;
import cc.squirreljme.jvm.mle.brackets.UIFormBracket;
import cc.squirreljme.jvm.mle.brackets.UIItemBracket;
import cc.squirreljme.jvm.mle.brackets.UIWidgetBracket;
import cc.squirreljme.vm.springcoat.brackets.UIDisplayObject;
import cc.squirreljme.vm.springcoat.brackets.UIFormObject;
import cc.squirreljme.vm.springcoat.brackets.UIItemObject;
import cc.squirreljme.vm.springcoat.callbacks.UIDisplayCallbackAdapter;
import cc.squirreljme.vm.springcoat.callbacks.UIFormCallbackAdapter;
import cc.squirreljme.vm.springcoat.callbacks.UIFormCallbackProxy;
import cc.squirreljme.vm.springcoat.exceptions.SpringMLECallError;

/**
 * SpringCoat support for {@link UIFormShelf}.
 *
 * @since 2020/06/30
 */
@Deprecated
public enum MLEUIForm
	implements MLEFunction
{
	/** {@link UIFormShelf#callback(UIDisplayBracket, UIDisplayCallback)}. */
	CALLBACK_DISPLAY("callback:(" +
		"Lcc/squirreljme/jvm/mle/brackets/UIDisplayBracket;" +
		"Lcc/squirreljme/jvm/mle/callbacks/UIDisplayCallback;)V")
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/01/14
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			UIDisplayObject display = MLEObjects.uiDisplay(__args[0]);
			
			SpringObject callback = (SpringObject)__args[1];
			if (callback == null || callback == SpringNullObject.NULL)
				throw new SpringMLECallError("Null callback.");
			
			// The callback needs to be wrapped accordingly for SpringCoat
			UIFormShelf.callback(display.display,
				new UIDisplayCallbackAdapter(__thread.machine, callback));
			return null;
		}
	},
	
	/** {@link UIFormShelf#callback(UIFormBracket, UIFormCallback)}. */
	CALLBACK_FORM("callback:(Lcc/squirreljme/jvm/mle/brackets/" +
		"UIFormBracket;Lcc/squirreljme/jvm/mle/callbacks/UIFormCallback;)V")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/09/13
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			UIFormObject form = MLEObjects.uiForm(__args[0]);
			
			SpringObject callback = (SpringObject)__args[1];
			if (callback == null || callback == SpringNullObject.NULL)
				throw new SpringMLECallError("Null callback.");
			
			// Since the callback is in SpringCoat, it has to be wrapped to
			// forward calls into, there also needs to be threads to do work
			// in
			UIFormShelf.callback(form.form,
				new UIFormCallbackAdapter(__thread.machine, callback));
			return null;
		}
	}, 
	
	/** {@link UIFormShelf#displays()}. */
	DISPLAYS("displays:()[Lcc/squirreljme/jvm/mle/brackets/" +
		"UIDisplayBracket;")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/07/01
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			UIDisplayBracket[] natives = UIFormShelf.displays();
			
			// Wrap the array
			int n = natives.length;
			SpringObject[] result = new SpringObject[n];
			for (int i = 0; i < n; i++)
				result[i] = new UIDisplayObject(__thread.machine, natives[i]);
			
			// Use array as result
			return __thread.asVMObjectArray(__thread.resolveClass(
				"[Lcc/squirreljme/jvm/mle/brackets/UIDisplayBracket;"),
				result);
		}
	},
	
	/** {@link UIFormShelf#displayCurrent(UIDisplayBracket)}. */ 
	DISPLAY_CURRENT("displayCurrent:(Lcc/squirreljme/jvm/mle/" +
		"brackets/UIDisplayBracket;)Lcc/squirreljme/jvm/mle/brackets/" +
		"UIFormBracket;")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/10/04
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			UIFormBracket form = UIFormShelf.displayCurrent(
				MLEObjects.uiDisplay(__args[0]).display);
			
			return (form == null ? null : new UIFormObject(__thread.machine,
				form));
		}
	},
	
	/** {@link UIFormShelf#displayShow(UIDisplayBracket, boolean)}. */ 
	DISPLAY_SHOWZ("displayShow:(Lcc/squirreljme/jvm/mle/brackets/" +
		"UIDisplayBracket;Z)V")
	{
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			UIFormShelf.displayShow(MLEObjects.uiDisplay(__args[0]).display,
				(int)__args[1] != 0);
			return null;
		}
	},
	
	/** {@link UIFormShelf#displayShow(UIDisplayBracket, UIFormBracket)}. */ 
	DISPLAY_SHOW("displayShow:(Lcc/squirreljme/jvm/mle/brackets/" +
		"UIDisplayBracket;Lcc/squirreljme/jvm/mle/brackets/UIFormBracket;)V")
	{
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			SpringObject form = (SpringObject)__args[1];
			
			UIFormShelf.displayShow(MLEObjects.uiDisplay(__args[0]).display,
				(form == null || form == SpringNullObject.NULL ? null :
				MLEObjects.uiForm(__args[1]).form));
			return null;
		}
	},
	
	/** {@link UIFormShelf#equals(UIDrawableBracket, UIDrawableBracket)}. */
	EQUALS_DRAWABLE("equals:(Lcc/squirreljme/jvm/mle/brackets/" +
		"UIDrawableBracket;Lcc/squirreljme/jvm/mle/brackets/" +
		"UIDrawableBracket;)Z")
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/01/14
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return UIFormShelf.equals(MLEObjects.uiDrawable(__args[0]),
				MLEObjects.uiDrawable(__args[1]));
		}
	},
	
	/** {@link UIFormShelf#equals(UIDisplayBracket, UIDisplayBracket)}. */
	EQUALS_DISPLAY("equals:(Lcc/squirreljme/jvm/mle/brackets/" +
		"UIDisplayBracket;Lcc/squirreljme/jvm/mle/brackets/" +
		"UIDisplayBracket;)Z")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/09/20
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return UIFormShelf.equals(MLEObjects.uiDisplay(__args[0]).display,
				MLEObjects.uiDisplay(__args[1]).display);
		}
	},
	
	/** {@link UIFormShelf#equals(UIFormBracket, UIFormBracket)}. */
	EQUALS_FORM("equals:(Lcc/squirreljme/jvm/mle/brackets/" +
		"UIFormBracket;Lcc/squirreljme/jvm/mle/brackets/" +
		"UIFormBracket;)Z")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/09/20
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return UIFormShelf.equals(MLEObjects.uiForm(__args[0]).form,
				MLEObjects.uiForm(__args[1]).form);
		}
	},
	
	/** {@link UIFormShelf#equals(UIItemBracket, UIItemBracket)}. */
	EQUALS_ITEM("equals:(Lcc/squirreljme/jvm/mle/brackets/" +
		"UIItemBracket;Lcc/squirreljme/jvm/mle/brackets/" +
		"UIItemBracket;)Z")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/09/20
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return UIFormShelf.equals(MLEObjects.uiItem(__args[0]).item,
				MLEObjects.uiItem(__args[1]).item);
		}
	},
	
	/** {@link UIFormShelf#equals(UIWidgetBracket, UIWidgetBracket)}. */
	EQUALS_WIDGET("equals:(Lcc/squirreljme/jvm/mle/brackets/" +
		"UIWidgetBracket;Lcc/squirreljme/jvm/mle/brackets/" +
		"UIWidgetBracket;)Z")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/09/20
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return UIFormShelf.equals(MLEObjects.uiWidget(__args[0]).widget(),
				MLEObjects.uiWidget(__args[1]).widget());
		}
	},
	
	/** {@link UIFormShelf#flushEvents()}. */
	FLUSH_EVENTS("flushEvents:()V")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/10/03
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			UIFormShelf.flushEvents();
			return null;
		}
	},
	
	/** {@link UIFormShelf#formDelete(UIFormBracket)}. */
	FORM_DELETE("formDelete:(Lcc/squirreljme/jvm/mle/brackets/" +
		"UIFormBracket;)V")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/07/01
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			UIFormShelf.formDelete(MLEObjects.uiForm(__args[0]).form);
			return null;
		}
	},
	
	/** {@link UIFormShelf#formItemAtPosition(UIFormBracket, int)}. */
	FORM_ITEM_AT_POSITION("formItemAtPosition:(Lcc/squirreljme/jvm/" +
		"mle/brackets/UIFormBracket;I)Lcc/squirreljme/jvm/mle/brackets/" +
		"UIItemBracket;")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/10/03
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			UIItemBracket rv = UIFormShelf.formItemAtPosition(
				MLEObjects.uiForm(__args[0]).form, (int)__args[1]);
			
			return (rv == null ? null : new UIItemObject(__thread.machine, rv));
		}
	},
	
	/** {@link UIFormShelf#formItemCount(UIFormBracket)}. */ 
	FORM_ITEM_COUNT("formItemCount:(Lcc/squirreljme/jvm/mle/" +
		"brackets/UIFormBracket;)I")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/10/09
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return UIFormShelf.formItemCount(MLEObjects.uiForm(__args[0]).form);
		}
	},
	
	/** {@link UIFormShelf#formItemPosition(UIFormBracket, UIItemBracket)}. */
	FORM_ITEM_POSITION_GET("formItemPosition:(Lcc/squirreljme/jvm/mle/" +
		"brackets/UIFormBracket;Lcc/squirreljme/jvm/mle/brackets/" +
		"UIItemBracket;)I")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/07/18
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return UIFormShelf.formItemPosition(
				MLEObjects.uiForm(__args[0]).form,
				MLEObjects.uiItem(__args[1]).item);
		}
	},
	
	/**
	 * {@link
	 * UIFormShelf#formItemPosition(UIFormBracket, UIItemBracket, int)}.
	 */
	FORM_ITEM_POSITION_SET("formItemPosition:(Lcc/squirreljme/jvm/" +
		"mle/brackets/UIFormBracket;Lcc/squirreljme/jvm/mle/brackets/" +
		"UIItemBracket;I)V")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/07/18
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			UIFormShelf.formItemPosition(
				MLEObjects.uiForm(__args[0]).form,
				MLEObjects.uiItem(__args[1]).item,
				(int)__args[2]);
			return null;
		}
	},
	
	/** {@link UIFormShelf#formItemRemove(UIFormBracket, int)}. */ 
	FORM_ITEM_REMOVE("formItemRemove:(Lcc/squirreljme/jvm/mle/" +
		"brackets/UIFormBracket;I)Lcc/squirreljme/jvm/mle/brackets/" +
		"UIItemBracket;")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/10/09
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return new UIItemObject(__thread.machine, UIFormShelf.formItemRemove(
				MLEObjects.uiForm(__args[0]).form, (int)__args[1]));
		}
	},
	
	/** {@link UIFormShelf#formNew()}. */
	FORM_NEW("formNew:()Lcc/squirreljme/jvm/mle/brackets/UIFormBracket;")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/07/01
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return new UIFormObject(__thread.machine, UIFormShelf.formNew());
		}
	},
	
	/** {@link UIFormShelf#formRefresh(UIFormBracket)}. */
	FORM_REFRESH("formRefresh:" +
		"(Lcc/squirreljme/jvm/mle/brackets/UIFormBracket;)V")
	{
		/**
		 * {@inheritDoc}
		 * @since 2022/07/20
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			UIFormShelf.formRefresh(MLEObjects.uiForm(__args[0]).form);
			return null;
		}
	},
	
	/** {@link UIFormShelf#injector()}. */
	INJECTOR("injector:()Lcc/squirreljme/jvm/mle/callbacks/" +
		"UIFormCallback;")
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/02/25
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			// Use a proxy to call from SpringCoat to the real machine
			return new UIFormCallbackProxy(__thread.machine,
				UIFormShelf.injector());
		}
	},
	
	/** {@link UIFormShelf#itemDelete(UIItemBracket)}. */
	ITEM_DELETE("itemDelete:(Lcc/squirreljme/jvm/mle/brackets/" +
		"UIItemBracket;)V")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/07/18
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			UIFormShelf.itemDelete(MLEObjects.uiItem(__args[0]).item);
			return null;
		}
	},
	
	/** {@link UIFormShelf#itemForm(UIItemBracket)}. */
	ITEM_FORM("itemForm:(Lcc/squirreljme/jvm/mle/brackets/" +
		"UIItemBracket;)Lcc/squirreljme/jvm/mle/brackets/UIFormBracket;")
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/01/03
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			UIFormBracket form = UIFormShelf.itemForm(
				MLEObjects.uiItem(__args[0]).item);
			return (form == null ? null : new UIFormObject(__thread.machine,
				form));
		}
	}, 
	
	/** {@link UIFormShelf#itemNew(int)}. */  
	ITEM_NEW("itemNew:(I)Lcc/squirreljme/jvm/mle/brackets/" +
		"UIItemBracket;")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/07/18
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return new UIItemObject(__thread.machine, UIFormShelf.itemNew((int)__args[0]));
		}
	},
	
	/** {@link UIFormShelf#later(UIDisplayBracket, int)}. */ 
	LATER("later:(Lcc/squirreljme/jvm/mle/brackets/UIDisplayBracket;I)V")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/10/03
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			UIFormShelf.later(MLEObjects.uiDisplay(__args[0]).display,
				(int)__args[1]);
			return null;
		}
	},
	
	/** {@link UIFormShelf#metric(UIDisplayBracket, int)}. */
	METRIC("metric:(Lcc/squirreljme/jvm/mle/brackets/UIDisplayBracket;" +
		"I)I")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/30
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			int metric = (int)__args[1];
			
			return UIFormShelf.metric(MLEObjects.uiDisplay(__args[0]).display,
				metric);
		}
	},
	
	/** {@link UIFormShelf#widgetPropertyInt(UIWidgetBracket, int, int)}. */
	WIDGET_PROPERTY_GET_INT("widgetPropertyInt:(Lcc/squirreljme/jvm/" +
		"mle/brackets/UIWidgetBracket;II)I")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/09/21
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return UIFormShelf.widgetPropertyInt(
				MLEObjects.uiWidget(__args[0]).widget(),
				(int)__args[1], (int)__args[2]);
		}
	},
	
	/** {@link UIFormShelf#widgetPropertyStr(UIWidgetBracket, int, int)}. */
	WIDGET_PROPERTY_GET_SET("widgetPropertyStr:(Lcc/squirreljme/jvm/" +
		"mle/brackets/UIWidgetBracket;II)Ljava/lang/String;")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/09/21
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return UIFormShelf.widgetPropertyStr(
				MLEObjects.uiWidget(__args[0]).widget(),
				(int)__args[1], (int)__args[2]);
		}
	},
	
	/** {@link UIFormShelf#widgetProperty(UIWidgetBracket, int, int, int)}. */ 
	WIDGET_PROPERTY_SET_INT("widgetProperty:(Lcc/squirreljme/jvm/mle/" +
		"brackets/UIWidgetBracket;III)V")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/09/13
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			UIFormShelf.widgetProperty(
				MLEObjects.uiWidget(__args[0]).widget(),
				(int)__args[1], (int)__args[2], (int)__args[3]);
			return null;
		}
	},
	
	/** {@link UIFormShelf#widgetProperty(UIWidgetBracket, int, int, int)}. */ 
	WIDGET_PROPERTY_SET_STR("widgetProperty:(Lcc/squirreljme/jvm/mle/" +
		"brackets/UIWidgetBracket;IILjava/lang/String;)V")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/09/13
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			UIFormShelf.widgetProperty(
				MLEObjects.uiWidget(__args[0]).widget(),
				(int)__args[1], (int)__args[2],
				__thread.<String>asNativeObject(String.class, __args[3]));
			return null;
		}
	},
	
	/* End. */
	;
	
	/** The dispatch key. */
	protected final String key;
	
	/**
	 * Initializes the dispatcher info.
	 *
	 * @param __key The key.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/30
	 */
	MLEUIForm(String __key)
		throws NullPointerException
	{
		if (__key == null)
			throw new NullPointerException("NARG");
		
		this.key = __key;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/06/30
	 */
	@Override
	public String key()
	{
		return this.key;
	}
}
