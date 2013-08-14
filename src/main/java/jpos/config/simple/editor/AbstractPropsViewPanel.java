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

import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

import javax.swing.*;    

import jpos.config.*;

/**
 * Super class of all JposEntry properties view panel
 * @author E. Michael Maximilien (maxim@us.ibm.com)
 * @since 1.3 (SF 2K meeting)
 */
abstract class AbstractPropsViewPanel extends JPanel implements PropsViewPanel
{
    //--------------------------------------------------------------------------
    // Ctor(s)
    //

    /** Default ctor */
    public AbstractPropsViewPanel() {}

    //--------------------------------------------------------------------------
    // Public intance methods
    //

    /** Clears all the JTextField to "" */
    public abstract void clearAll();

    /**
     * Makes all JTextField enabled
     * @param b the boolean param
     */
    public abstract void setEnabledAll( boolean b );

    /**
     * Makes all JTextField editable
     * @param b the boolean param
     */
    public abstract void setEditableAll( boolean b );

    /**
     * Sets the current JposEntry for this panel 
     * @param jposEntry the JposEntry
     */
    public abstract void setJposEntry( JposEntry entry ) ;

    /** Called when this PropsViewPanel about to looses focus */
    public abstract void aboutToLooseFocus();

    /** Called when the "Edit" command button is clicked */
    public abstract void editButtonClicked();

    /** Called when the "OK" command button is clicked */
    public abstract void okButtonClicked();

    /** Called when the "OK" command button is clicked */
    public abstract void cancelButtonClicked();

    //-------------------------------------------------------------------------
    // Public methods
    //

    /** @return the current JposEntry for this panel */
    public JposEntry getJposEntry() { return jposEntry; }

	/** @return the JPanel of of custom JButton object to add to for this panel */
	public JPanel getCustomButtonPanel() { return customButtonPanel; }

	/**
	 * Adds the PropsViewPanel listener for this PropsViewPanel
	 * @param listener The PropsViewPanel.Listener object
	 */
	public synchronized void addListener( PropsViewPanel.Listener listener )
	{ listeners.add( listener ); }

	/**
	 * Removes the PropsViewPanel listener for this PropsViewPanel
	 * @param listener The PropsViewPanel.Listener object
	 */
	public synchronized void removeListener( PropsViewPanel.Listener listener )
	{ listeners.remove( listener ); }

    //-------------------------------------------------------------------------
    // Protected methods
    //

	/**
	 * Called to fire a jposEntryChanged event to all listeners 
	 * @param event the PropsViewPanel.Event object
	 */
	protected void fireJposEntryChanged( PropsViewPanel.Event event )
	{
		Iterator iterator = listeners.iterator();

		while( iterator.hasNext() )
			( (PropsViewPanel.Listener)iterator.next() ).jposEntryChanged( event );
	}

	/**
	 * Sets the MainFrame that this panel is part of
	 * @param frame the MainFrame object
	 * @since 2.0.0
	 */
	protected void setMainFrame( MainFrame frame ) { mainFrame = frame; }

	/** 
	 * @return the MainFrame that is associated with this panel
	 * @since 2.0.0
	 */
	protected MainFrame getMainFrame() { return mainFrame; }

    //--------------------------------------------------------------------------
    // Intance variables
    //

    protected JposEntry jposEntry = null;
	protected JPanel customButtonPanel = new JPanel();

	private List listeners = new ArrayList();
	private MainFrame mainFrame = null;
}
