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
import java.awt.FlowLayout;
import java.awt.Window;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Objects;
import java.util.function.BiFunction;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

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
	
	/** The owning window. */
	protected final Window owner;
	
	/** The state of the debugger. */
	protected final DebuggerState state;
	
	/** The value being inspected. */
	private final Reference<KnownValue<?>> _value;
	
	/**
	 * Initializes the inspector.
	 *
	 * @param __owner The owning window, is optional.
	 * @param __state The debugger state.
	 * @param __value The value to inspect.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/20
	 */
	public InspectKnownValue(Window __owner, DebuggerState __state,
		KnownValue<?> __value)
		throws NullPointerException
	{
		if (__state == null || __value == null)
			throw new NullPointerException("NARG");
		
		// Store owner and state for later usage
		this.owner = __owner;
		this.state = __state;
		
		// Set value
		this._value = new WeakReference<>(__value);
		
		// Determine how this updates...
		BiFunction<JComponent, KnownValue<?>, JComponent> updater;
		if (__value.type == Boolean.class)
			updater = this::__updateBoolean;
		else if (__value.type == InfoByteCode.class)
			updater = this::__updateByteCode;
		else if (__value.type == InfoMethod[].class)
			updater = this::__updateMethods;
		else if (__value.type == String.class)
			updater = this::__updateString;
		else if (Number.class.isAssignableFrom(__value.type))
			updater = this::__updateNumber;
		else
			updater = this::__updateUnknown;
		
		// Setup initial item and updater
		JComponent base = updater.apply(null, __value);
		
		// Add item to the center of this panel
		FlowLayout layout = new FlowLayout(FlowLayout.LEADING);
		layout.setVgap(0);
		layout.setHgap(0);
		layout.setAlignment(FlowLayout.LEADING);
		this.setLayout(layout);
		this.add(base);
		
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
	 * Updates the boolean value.
	 *
	 * @param __base The base component.
	 * @param __value The value to show.
	 * @return The component used.
	 * @since 2024/01/20
	 */
	private JComponent __updateBoolean(JComponent __base,
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
	 * Initializes a byte code viewer.
	 *
	 * @param __base The base component.
	 * @param __value The byte code value.
	 * @return The component used to show the byte code.
	 * @since 2024/01/24
	 */
	private JComponent __updateByteCode(JComponent __base,
		KnownValue<?> __value)
	{
		// Need to initialize?
		JButton button;
		if (__base != null)
			button = (JButton)__base;
		else
		{
			button = new JButton();
			
			// Add handler for showing the byte code
			button.addActionListener((__event) -> {
				InfoByteCode byteCode = __value.get(InfoByteCode.class);
				if (byteCode == null)
					return;
				
				// Was this garbage collected?
				InfoMethod infoMethod = byteCode.method.get();
				if (infoMethod == null)
					return;
				
				// Show dialog for the byte code
				ShownMethodDialog dialog = new ShownMethodDialog(
					this.owner, this.state, new RemoteMethodViewer(this.state,
						infoMethod));
				dialog.setLocationRelativeTo(null);
				dialog.setVisible(true);
			});
		}
		
		// Set label value
		if (!__value.isKnown())
			button.setText("Unknown?");
		else
			button.setText("Show Bytecode");
		
		// Return self
		return button;
	}
	
	/**
	 * Updates the method value.
	 *
	 * @param __base The base component.
	 * @param __value The value used.
	 * @return The resultant component.
	 * @since 2024/01/24
	 */
	private JComponent __updateMethods(JComponent __base,
		KnownValue<?> __value)
	{
		// Need to initialize?
		JButton button;
		if (__base != null)
			button = (JButton)__base;
		else
		{
			button = new JButton();
			
			// Show list of methods when selecting the button, initialize this
			// here because otherwise there would be multiple actions ran
			// every time there was an update
			button.addActionListener((__event) -> {
				InfoMethod[] methods = __value.get(InfoMethod[].class);
				if (methods == null)
					return;
				
				// Update all methods
				DebuggerState state = this.state;
				for (InfoMethod method : methods)
					method.update(state, null);
				
				// Show inspector on it
				Utils.inspect(this.owner, state,
					InfoKind.METHOD, methods);
			});
		}
		
		// Set label value
		if (!__value.isKnown())
			button.setText("Unknown?");
		else
		{
			// Get all the methods
			InfoMethod[] methods = __value.get(InfoMethod[].class);
			
			// The button text is just the number of methods
			button.setText(String.format("%d Methods", methods.length));
		}
		
		// Return self
		return button;
	}
	
	/**
	 * Updates the number value.
	 *
	 * @param __base The base component.
	 * @param __value The value of the number.
	 * @return The resultant component.
	 * @since 2024/01/25
	 */
	private JComponent __updateNumber(JComponent __base, KnownValue<?> __value)
	{
		// Need to initialize?
		JTextField text;
		if (__base != null)
			text = (JTextField)__base;
		else
		{
			text = new JTextField();
			text.setEditable(false);
			text.setBorder(new EmptyBorder(0, 0, 0, 0));
		}
		
		// Set label value
		if (!__value.isKnown())
			text.setText("Unknown?");
		else
			text.setText(Long.toString(
				((Number)__value.get()).longValue(), 10));
		
		return text;
	}
	
	/**
	 * Updates the string value.
	 *
	 * @param __base The base component.
	 * @param __value The value to show.
	 * @return The component used.
	 * @since 2024/01/20
	 */
	private JComponent __updateString(JComponent __base,
		KnownValue<?> __value)
	{
		// Need to initialize?
		JTextField text;
		if (__base != null)
			text = (JTextField)__base;
		else
		{
			text = new JTextField();
			text.setEditable(false);
			text.setBorder(new EmptyBorder(0, 0, 0, 0));
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
	private JComponent __updateUnknown(JComponent __base,
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
}
