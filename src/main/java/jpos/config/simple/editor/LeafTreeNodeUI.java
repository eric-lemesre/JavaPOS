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

import javax.swing.ImageIcon;

import jpos.config.JposEntry;

/**
 * implementation of tree nodes by device Category
 * @author Manuel M Monserrate
 * @author E. Michael Maximilien (maxim@us.ibm.com)
 * @since 1.3 (SF 2K meeting)
 */
public class LeafTreeNodeUI extends AbstractTreeNodeUI
{
    //---------------------------------------------------------------------------
    // Ctor(s)
    //

    /**
     * default ctor
     * @param logName string representing the logical Name in the tree node
     */
    public LeafTreeNodeUI( JposEntry entry ) { jposEntry = entry; }

    //---------------------------------------------------------------------------
    // Public methods
    //

    /** @return the logical name of the JposEntry in this tree Node */
    public String getName() { return toString(); }

    /** @return the ImageIcon for drawing the node */
    public ImageIcon getImageIcon() { return null; }

    /**
     * @return string representation of this TreeNodeUI so that the correct
     * name can be viewed in the user interface
     */
    public String toString() { return jposEntry.getLogicalName(); }

    /** @return the text to display as tool tip text in the tree view */
    public String getToolTipText()
    {
        return (String)jposEntry.getPropertyValue( JposEntry.VENDOR_NAME_PROP_NAME );
    }

    //---------------------------------------------------------------------------
    // Instance variables
    //

    private JposEntry jposEntry;
}

