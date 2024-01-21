// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

import java.awt.BorderLayout;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Objects;
import java.util.function.BiFunction;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Component for inspecting a known value.
 *
 * @since 2024/01/20
 */
public class InspectKnownValue
	extends JPanel
{
	/** The updater for the value. */
	protected final BiFunction<JComponent, KnownValue<?>, JComponent> updater;
	
	/** The base root component. */
	protected final JComponent base;
	
	/** The value being inspected. */
	private final Reference<KnownValue<?>> _value;
	
	/**
	 * Initializes the inspector.
	 *
	 * @param __value The value to inspect.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/20
	 */
	public InspectKnownValue(KnownValue<?> __value)
		throws NullPointerException
	{
		if (__value == null)
			throw new NullPointerException("NARG");
		
		// Set value
		this._value = new WeakReference<>(__value);
		
		// Determine how this updates...
		BiFunction<JComponent, KnownValue<?>, JComponent> updater;
		if (__value.type == Boolean.class)
			updater = InspectKnownValue::__updateBoolean;
		else if (__value.type == String.class)
			updater = InspectKnownValue::__updateString;
		else
			updater = InspectKnownValue::__updateUnknown;
		
		// Setup initial item and updater
		JComponent base = updater.apply(null, __value);
		
		// Add item to the center of this panel
		this.setLayout(new BorderLayout());
		this.add(base, BorderLayout.CENTER);
		
		// Store for later usage and updating
		this.base = base;
		this.updater = updater;
	}
	
	/**
	 * Updates the given value.
	 *
	 * @since 2024/01/20
	 */
	public void update()
	{
		KnownValue<?> value = this.__value();
		if (value != null)
			this.updater.apply(this.base, value);
	}
	
	/**
	 * Returns the stored known value.
	 *
	 * @return The resultant value.
	 * @since 2024/01/21
	 */
	private KnownValue<?> __value()
	{
		return this._value.get();
	}
	
	/**
	 * Updates the boolean value.
	 *
	 * @param __base The base component.
	 * @param __value The value to show.
	 * @return The component used.
	 * @since 2024/01/20
	 */
	private static JComponent __updateBoolean(JComponent __base,
		KnownValue<?> __value)
	{
		// Need to initialize?
		JCheckBox check;
		if (__base != null)
			check = (JCheckBox)__base;
		else
		{
			check = new JCheckBox();
			check.setEnabled(false);
		}
		
		// Set label value
		if (!__value.isKnown())
			check.setText("Unknown?");
		else
		{
			check.setSelected((Boolean)__value.get());
			check.setText("");
		}
		
		return check;
	}
	
	/**
	 * Updates the string value.
	 *
	 * @param __base The base component.
	 * @param __value The value to show.
	 * @return The component used.
	 * @since 2024/01/20
	 */
	private static JComponent __updateString(JComponent __base,
		KnownValue<?> __value)
	{
		// Need to initialize?
		JTextField text;
		if (__base != null)
			text = (JTextField)__base;
		else
		{
			text = new JTextField();
			text.setEnabled(false);
		}
		
		// Set label value
		if (!__value.isKnown())
			text.setText("Unknown?");
		else
			text.setText((String)__value.get());
		
		return text;
	}
	
	/**
	 * Updates the boolean value.
	 *
	 * @param __base The base component.
	 * @param __value The value to show.
	 * @return The component used.
	 * @since 2024/01/20
	 */
	private static JComponent __updateUnknown(JComponent __base,
		KnownValue<?> __value)
	{
		// Need to initialize?
		JLabel label;
		if (__base != null)
			label = (JLabel)__base;
		else
			label = new JLabel();
		
		// Set label value
		if (!__value.isKnown())
			label.setText("Unknown?");
		else
			label.setText(Objects.toString(__value.get()));
		
		return label;
	}
}
