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

import java.awt.*;

import javax.swing.*;
import javax.swing.tree.*;

/**
 * Renders tree cells to show icons for different device categories
 * @author Manuel M Monserrate
 * @author E. Michael Maximilien (maxim@us.ibm.com)
 * @version 1.3 (JDK 1.1.x with Swing 1.1.x)
 */
class JposEntryTreeCellRenderer extends DefaultTreeCellRenderer
{
    /**
     * Default ctor
     * @since 1.3 (SF 2K meeting)
     */
    public JposEntryTreeCellRenderer() {}
    
    /**
     * This is messaged from JTree whenever it needs to get the size
     * of the component or it wants to draw it
     * @param tree the JTree object that called this method
     * @param value the TreeNode object that is about to be redered in the tree
     * @param selected flag indicating that the node is selected
     * @param expanded flag indicating that the node is expanded
     * @param leaf flag indicating that the node is a leaf node
     * @param row indicates the row of this node
     * @param hasFocus flag indicating if this node has focus
     */
    public Component getTreeCellRendererComponent( JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) 
    {
        super.getTreeCellRendererComponent( tree, value, sel, expanded, leaf, row, hasFocus); 
        
        ToolTipManager.sharedInstance().registerComponent( tree );

        DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;

        if( node.getParent() != null)
        {
            TreeNodeUI treeNode = (TreeNodeUI)node.getUserObject();

            setText( treeNode.getName() );

            if( treeNode.getImageIcon() != null)
                setIcon( treeNode.getImageIcon() );
                    
            setToolTipText( treeNode.getToolTipText() );
        }
        
        return this;
    }

    //--------------------------------------------------------------------------
    // private methods
    //

    //--------------------------------------------------------------------------
    // Intance variables
    //
}
