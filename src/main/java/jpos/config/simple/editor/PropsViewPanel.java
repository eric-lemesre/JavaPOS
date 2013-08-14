package jpos.config.simple.editor;

///////////////////////////////////////////////////////////////////////////////
//
// This software is provided "AS IS".  The JavaPOS working group (including
// each of the Corporate members, contributors and individuals)  MAKES NO
// REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE SOFTWARE,
// EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED 
// WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND 
// NON-INFRINGEMENT. The JavaPOS working group shall not be liable for
// any damages suffered as a result of using, modifying or distributing this
// software or its derivatives. Permission to use, copy, modify, and distribute
// the software and its documentation for any purpose is hereby granted. 
//
// The JavaPOS Config/Loader (aka JCL) is now under the CPL license, which 
// is an OSS Apache-like license.  The complete license is located at:
//    http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
//
///////////////////////////////////////////////////////////////////////////////

import java.util.*;

import javax.swing.*;    

import jpos.config.*;

/**
 * Super interface of all JposEntry properties view panel
 * @author E. Michael Maximilien (maxim@us.ibm.com)
 * @since 1.3 (Tokyo 2001 meeting)
 */
public interface PropsViewPanel
{
    //--------------------------------------------------------------------------
    // Public intance methods
    //

    /** Clears all the JTextField to "" */
    public void clearAll();

    /**
     * Makes all JTextField enabled
     * @param b the boolean param
     */
    public void setEnabledAll( boolean b );

    /**
     * Makes all JTextField editable
     * @param b the boolean param
     */
    public void setEditableAll( boolean b );

    /**
     * Sets the current JposEntry for this panel 
     * @param jposEntry the JposEntry
     */
    public void setJposEntry( JposEntry entry ) ;

    /** @return the current JposEntry for this panel */
    public JposEntry getJposEntry();

    /** Called this PropsViewPanel is about looses focus */
    public void aboutToLooseFocus();

    /** Called when the "Edit" command button is clicked */
    public void editButtonClicked();

    /** Called when the "OK" command button is clicked */
    public void okButtonClicked();

    /** Called when the "OK" command button is clicked */
    public void cancelButtonClicked();

	/** @return the JPanel of of custom JButton object to add to for this panel */
	public JPanel getCustomButtonPanel();

	/**
	 * Adds the PropsViewPanel listener for this PropsViewPanel
	 * @param listener The PropsViewPanel.Listener object
	 */
	public void addListener( PropsViewPanel.Listener listener );

	/**
	 * Removes the PropsViewPanel listener for this PropsViewPanel
	 * @param listener The PropsViewPanel.Listener object
	 */
	public void removeListener( PropsViewPanel.Listener listener );

	//-------------------------------------------------------------------------
	// Public inner interfaces/classes
	//

	/**
	 * Inner interface defining a Listener for this interface
	 * @author E. Michael Maximilien (maxim@us.ibm.com)
	 * @since 1.3 (Washington DC 2001 meeting)
	 */
	public interface Listener extends EventListener
	{
		/** 
		 * Called to indicate that the JposEntry has changed 
		 * @param event the PropsViewPanel.Event object
		 */
		public void jposEntryChanged( PropsViewPanel.Event event );
	}

	/**
	 * Inner class to indicate an Event for this PropsViewPanel
	 * @author E. Michael Maximilien (maxim@us.ibm.com)
	 * @since 1.3 (Washington DC 2001 meeting)
	 */
	public static class Event extends EventObject
	{
		/**
		 * Ctor
		 * @param source the source PropsViewPanel
		 */
		public Event( PropsViewPanel source ) { super( source ); }

		/** @return the PropsViewPanel generating this event */
		public PropsViewPanel getPropsViewPanel() { return (PropsViewPanel)getSource(); }
	}
}
