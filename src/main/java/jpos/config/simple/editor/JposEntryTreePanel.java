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
import java.util.*;

import javax.swing.*;    
import javax.swing.event.*;
import javax.swing.border.*;
import javax.swing.tree.*;

import jpos.config.*;
import jpos.util.*;

/**
 * Panel subclass to show a tree of JposEntry objects
 * @since 0.1 (Philly 99 meeting)
 * @author E. Michael Maximilien (maxim@us.ibm.com)
 * @author Manuel Monseratte
 * @author Kriselie Rivera
 */
class JposEntryTreePanel extends JPanel
{
   public JposEntryTreePanel()
   {
       setLayout( new BorderLayout() );
       setBorder( BorderFactory.createTitledBorder( BorderFactory.createLineBorder( Color.gray ), JPOSENTRIES_STRING, TitledBorder.ABOVE_TOP, TitledBorder.LEFT ) );

       ( ( DefaultTreeModel)tree.getModel() ).setRoot( entriesNode );
       tree.getSelectionModel().setSelectionMode( TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION );
       
       tree.setRootVisible( false );
       tree.setShowsRootHandles( true );
       tree.putClientProperty( "JTree.lineStyle", "Angled" );

       JScrollPane jScrollPane = new JScrollPane( tree, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED );

       add( jScrollPane, BorderLayout.CENTER );

       tree.addTreeSelectionListener( new TreeSelectionListener()
                                      {
                                         public void valueChanged(TreeSelectionEvent e) 
                                         { JposEntryTreePanel.this.valueChanged( e ); }
                                      }
                                    );
   }

   //-------------------------------------------------------------------------
   // Private methods
   //

   private JposEntryListListener getJposEntryListListener()
   {
      if( jposEntryListListener == null )
      {
           jposEntryListListener = new JposEntryListListener()
                           {
                              public void jposEntryAdded(JposEntryListEvent event) 
                              { JposEntryTreePanel.this.jposEntryAdded( event ); }

                              public void jposEntryRemoved(JposEntryListEvent event) 
                              { JposEntryTreePanel.this.jposEntryRemoved( event ); }
                           };
      }

      return jposEntryListListener;
   }

   private void valueChanged( TreeSelectionEvent e )
   {
      Object selectedNode = tree.getLastSelectedPathComponent();

      if( selectedNode == null ) return;
          
      String logicalName = selectedNode.toString();

      if( entriesTable.containsKey( logicalName ))
      {
         JposEntry entry = (JposEntry)entriesTable.get( logicalName );

         fireJposEntryTreeEvent( new JposEntryTreeEvent( this, entry ) );
      }
      else
      {
          mainFrame.getJposEntryPanel().setJposEntry( null );
          mainFrame.getJposEntryPanel().clearAll();
          mainFrame.enableCopyDeleteSubMenus( false );
      }
   }

   private void fireJposEntryTreeEvent( JposEntryTreeEvent e )
   {
      Vector listenersClone = (Vector)listeners.clone();

      synchronized( listenersClone )
      {
         for( int i = 0; i < listenersClone.size(); ++i )
            ((JposEntryTreeListener)listenersClone.elementAt( i ) ).newJposEntrySelected( e );
      }
   }

   private void removeAllEntries()
   {
       entriesNode.removeAllChildren();
       entriesTable.clear();
       nodesTable.clear();

       tree.treeDidChange();

       tree.revalidate();
       tree.repaint();

       for( int i = 0; i < tree.getRowCount(); ++i )
            tree.collapseRow( i );
   }

   private void jposEntryAdded( JposEntryListEvent event ) 
   {
      JposEntry entry = event.getJposEntry();

      String logicalName = (String)entry.getPropertyValue( JposEntry.LOGICAL_NAME_PROP_NAME );

      entriesTable.put( logicalName, entry );

      LeafTreeNodeUI treeNode = new LeafTreeNodeUI( entry );

      DefaultMutableTreeNode node = new DefaultMutableTreeNode( treeNode );

      nodesTable.put( logicalName, node );

   }

   private void jposEntryRemoved( JposEntryListEvent event )
   { 
      String logicalName = (String)event.getJposEntry().getPropertyValue( JposEntry.LOGICAL_NAME_PROP_NAME );

      entriesTable.remove( logicalName );
      nodesTable.remove( logicalName );

      fireJposEntryTreeEvent( new JposEntryTreeEvent( this, null ) );
   }

   /**
    * called when sorted radio button is selected    
    * @author MMM (5-25-2000)
    * @since 1.3 (SF 2K Meeting)
    */
   private void sortedButtonSelected()
   {
       currentView = JposEntryEditorConfig.SORTEDVIEW;
       jposEntryList.sort();
       refreshTree();
   }

   /**
    * called when byCategory radio button is selected
    * @author MMM (5-25-2000)
    * @since 1.3 (SF 2K Meeting)
    */
   private void byCategoryButtonSelected()
   {
       currentView = JposEntryEditorConfig.CATEGORYVIEW;
       refreshTree();
   }

    /**
     * called when by Manufacturer radio button is selected
     * @author MMM (5-25-2000)
     * @since 1.3 (SF 2K Meeting)
     */
    private void byManufacturerButtonSelected()
    {
        currentView = JposEntryEditorConfig.MANUFACTURERVIEW; 
        refreshTree();
    }

    /**
     * creates the root node to be used by the tree sorting JposEntries by their manufacturer
     * @since 1.3 (SF 2K meeting)
     * @author MMM( 6-2-2000)
     */
    private void createNodesByManufacturer()
    {
        Enumeration entries = jposEntryList.getEntries();

        DefaultMutableTreeNode unknownNode = null;

        Hashtable vendors = new Hashtable();
        
        while( entries.hasMoreElements() )
        {
            JposEntry entry = (JposEntry)entries.nextElement();
            
            vendors.put( ( (String)entry.getPropertyValue( JposEntry.VENDOR_NAME_PROP_NAME ) ), new String( "" ) );
        }

        Enumeration hashKeys = vendors.keys();

        while( hashKeys.hasMoreElements() )
        {
            Enumeration entriesToSearch = jposEntryList.getEntries();
            
            String key = ( (String)hashKeys.nextElement() );
            
            ManufacturerTreeNodeUI vendorTreeNode = new ManufacturerTreeNodeUI( key );

            DefaultMutableTreeNode vendor = new DefaultMutableTreeNode( vendorTreeNode );

            while( entriesToSearch.hasMoreElements() )
            {
                JposEntry entry = (JposEntry)entriesToSearch.nextElement();

                if( ( (String)entry.getPropertyValue( JposEntry.VENDOR_NAME_PROP_NAME ) ).equalsIgnoreCase( key ) )
                {
                    String logicalName = (String)entry.getPropertyValue( JposEntry.LOGICAL_NAME_PROP_NAME );

                    entriesTable.put( logicalName, entry );

                    LeafTreeNodeUI treeNode = new LeafTreeNodeUI( entry );

                    DefaultMutableTreeNode node = new DefaultMutableTreeNode( treeNode );

                    nodesTable.put( logicalName, node );

                    vendor.add( node );
                }
            }

            if( !vendor.getUserObject().toString().equalsIgnoreCase( UNKNOWN_STRING ) )
                entriesNode.add( vendor );
            else
                unknownNode = vendor;
        }

        if( unknownNode != null) 
            entriesNode.add( unknownNode );
        
   }

   /**
    * creates the root node to be used by the tree sorting JposEntries by their category
    * @since 1.3 (SF 2K meeting)
    * @author MMM( 6-2-2000)
    */
   private void createNodesByCategories()
   {
        Enumeration entries = jposEntryList.getEntries();

        DefaultMutableTreeNode unknownNode = null;

        Hashtable categories = new Hashtable();
        
        while( entries.hasMoreElements() )
        {
            JposEntry entry = (JposEntry)entries.nextElement();
            
            categories.put( ( (String)entry.getPropertyValue( JposEntry.DEVICE_CATEGORY_PROP_NAME ) ).toLowerCase() , new String( "" ) );

        }

        Enumeration hashKeys = categories.keys();

        while( hashKeys.hasMoreElements() )
        {
            Enumeration entriesToSearch = jposEntryList.getEntries();
            
            String key = ( (String)hashKeys.nextElement() ).toLowerCase();
            
            CategoryTreeNodeUI catTreeNode = new CategoryTreeNodeUI( key );

            DefaultMutableTreeNode category = new DefaultMutableTreeNode( catTreeNode );

            while( entriesToSearch.hasMoreElements() )
            {
                JposEntry entry = (JposEntry)entriesToSearch.nextElement();

                if( ( (String)entry.getPropertyValue( JposEntry.DEVICE_CATEGORY_PROP_NAME ) ).equalsIgnoreCase( key ) )
                {
                    String logicalName = (String)entry.getPropertyValue( JposEntry.LOGICAL_NAME_PROP_NAME );

                    entriesTable.put( logicalName, entry );

                    LeafTreeNodeUI treeNode = new LeafTreeNodeUI( entry );

                    DefaultMutableTreeNode node = new DefaultMutableTreeNode( treeNode );

                    nodesTable.put( logicalName, node );
                    
                    category.add( node );
                }
            }
            
            if( !category.getUserObject().toString().equalsIgnoreCase( UNKNOWN_STRING ) )
                entriesNode.add( category );
            else
                unknownNode = category;
                
        }
        
        if( unknownNode != null) 
            entriesNode.add( unknownNode );
        
    }

    /**
     * creates the root node to be used by the tree without any specific arrangement
     * @return the root node used to display the tree
     * @since 1.3 (SF 2K meeting)
     * @author MMM( 6-2-2000)
     */
    private void createNodes()
    {
        Enumeration entries = jposEntryList.getEntries();

        while( entries.hasMoreElements() )
        {
            JposEntry entry = (JposEntry)entries.nextElement();

            if( entry.hasPropertyWithName( JposEntry.LOGICAL_NAME_PROP_NAME ) )
            {
                String logicalName = (String)entry.getPropertyValue( JposEntry.LOGICAL_NAME_PROP_NAME );
               
                entriesTable.put( logicalName, entry );

                LeafTreeNodeUI treeNode = new LeafTreeNodeUI( entry );

                DefaultMutableTreeNode node = new DefaultMutableTreeNode( treeNode );

                nodesTable.put( logicalName, node );

                entriesNode.add( node );
            }
        }
   }


   //-------------------------------------------------------------------------
   // Public instance methods
   //

   public void addJposEntryTreeListener( JposEntryTreeListener l ) { listeners.addElement( l ); }

   public void removeJposEntryTreeListener( JposEntryTreeListener l ) { listeners.removeElement( l ); }

   /**
    * refreshes the tree view after it changes, clears the whole tree if it is empty
    * @since 1.3 (SF 2K meeting)
    * @author MMM (5-10-2000)
    */
    public void refreshTree()
    {
        removeAllEntries();
       
        if( !jposEntryList.isEmpty() ) 
        {

            tree.setCellRenderer( new JposEntryTreeCellRenderer() );
            
            if( currentView == JposEntryEditorConfig.SORTEDVIEW )
            {
                jposEntryList.sort();
                createNodes();
            }
            else if( currentView == JposEntryEditorConfig.CATEGORYVIEW )
                createNodesByCategories();
            else if( currentView == JposEntryEditorConfig.MANUFACTURERVIEW )
                createNodesByManufacturer();
            
            ((DefaultTreeModel)tree.getModel()).nodeStructureChanged( entriesNode );
            
            tree.setRootVisible( true );
            
            if( expandTreeFlag )
            {
                expandTree();
            }
        }
        else
            tree.setRootVisible( false );
    
    }

   public void setJposEntryList( JposEntryList list )
   {
      jposEntryList = list;

      if( jposEntryList == null )
         removeAllEntries();
      else
      {  
         jposEntryList.addJposEntryListListener( getJposEntryListListener() );

         createNodes();

         if( expandTreeFlag ) 
            expandTree();
      }
   }

   public JposEntryList getJposEntryList() 
   {
   		if( jposEntryList == null )
   		{
   			jposEntryList = new JposEntryList();
   			setJposEntryList( jposEntryList );
   		}
   		 
   		return jposEntryList; 
   }

   /**
    * @return true if list is empty, false otherwise
    * @since 1.3 (SF 2K meeting)
    * @author MMM (5-10-2000)
    */
   public boolean isJposEntryListEmpty()
   {
     return jposEntryList.isEmpty();
   }

   /**
    * connects this panel to its parent MainFrame
    * @param parentMainFrame the parent's MainFrame
    * @since 1.3 (SF 2K meeting)
    * @author MMM (5-10-2000)
    */
   public void setMainFrame( MainFrame parentMainFrame )
   {
       mainFrame = parentMainFrame;
   }
   
   /**
    * displays/hides the tree's root
    * @param b the boolean param
    * @since 1.3 (SF 2K meeting)
    * @author MMM (5-15-2000)
    */
   public void setTreeRootVisible( boolean b )
   {
       tree.setRootVisible( b );
   }

   /**
    * expands the tree
    * @since 1.3 (SF 2K meeting)
    * @author MMM (5-15-2000)
    */
   public void expandTree()
   {
        if( expandTreeFlag )
            for( int i = 0; i < tree.getRowCount(); ++i )
                tree.expandRow( i );   
   }

    /**
     * sets the currentView variable for the Jtree data display
     * @since 1.3 (SF 2K meeting)
     * @author MMM (6-5-2000)
     */
    public void setCurrentTreeView( int currentTreeView )
    {
        currentView = currentTreeView;
            
        if( jposEntryList != null && !jposEntryList.isEmpty() )
        {
            if( currentView == JposEntryEditorConfig.CATEGORYVIEW )
            {
                byCategoryButtonSelected();
            }
            else if( currentView == JposEntryEditorConfig.MANUFACTURERVIEW )
            {
                byManufacturerButtonSelected();
            }
            else 
            {
                sortedButtonSelected();    
            }
        }       
   }

    /**
     * getter method for currentView variable
     * @since 1.3 (SF 2K meeting)
     * @author MMM (6-7-2000)
     */
    public int getCurrentTreeView() { return currentView; }
    

    /**
     * sets the variable expandTreeFlag
     * @param b the boolean param
     * @since 1.3 (SF 2K meeting)
     * @author MMM (6-6-2000)
     */
    public void setExpandTreeFlag( boolean b )
    {
        expandTreeFlag = b;
    }

    /**
     * getter method for the expandTreeFlag variable
     * @since 1.3 (SF 2K meeting)
     * @author MMM (6-7-2000)
     */
    public boolean getExpandTreeFlag() { return expandTreeFlag; }
  
    /**
     * registers the tree to react to mouse clicks and display a popup menu
     * @since 1.3 (SF 2K meeting)
     * @author MMM (6-23-2000)
     */
    public void registerTreeForPopupMenu( JPopupMenu popupMenu, PopupListener popupListener)
    {   
        PopupHelper.setPopup( tree, popupMenu, popupListener );
    }

    
    /**
     * getter method for selectioncount
     * @author RAR (12-1-2003)
     */
	public int getSelectionCount() {return tree.getSelectionCount(); }
	
	/**
	 * getter method for selectioncount
	 * @author RAR (12-1-2003)
	*/
	public TreePath[] getSelectionPaths() {return tree.getSelectionPaths();}
	
	/**
		* @return true if SelectionPath is valid, false otherwise
		* @since 1.3 (SF 2K meeting)
		* @author RAR (1-12-2004)
	*/
	public boolean isSelectionPathValid( int currentEntry, TreePath[] treePaths )
	{
		JposEntry entry = (JposEntry)entriesTable.get(treePaths[currentEntry].getLastPathComponent().toString());
		
		if( entry == null){ return false;}
		
		return true;
	}
	
	/**
	* setter method for setJposEntryToDelete sets JposEntry to be deleted
	* @author RAR (12-1-2003)
	*/    
    public void setJposEntryToDelete( int currentEntry, TreePath[] treePaths )
    {
		Object selectedNode = treePaths[currentEntry].getLastPathComponent();
		
		String logicalName = selectedNode.toString();
		
		JposEntry entry = (JposEntry)entriesTable.get( logicalName );
		
		mainFrame.getJposEntryPanel().setJposEntry(entry);
    }
    
    //-------------------------------------------------------------------------
    // Instance variables
    //

    private JposEntryListListener jposEntryListListener = null;

    private Hashtable entriesTable = new Hashtable();
    private Hashtable nodesTable = new Hashtable();
    private JposEntryList jposEntryList = null;

    private JTree tree = new JTree();
    private MainFrame mainFrame = null;

    private DefaultMutableTreeNode entriesNode = new DefaultMutableTreeNode( ENTRIES_STRING );

    private Vector listeners = new Vector();

    private int currentView = JposEntryEditorConfig.SORTEDVIEW;
    private boolean expandTreeFlag = false;
   
    //---------------------------------------------------------------------------
    // I18N  class constants
    //

    public static final String UNKNOWN_STRING = JposEntryEditorMsg.UNKNOWN_STRING;
    public static final String ENTRIES_STRING = JposEntryEditorMsg.ENTRIES_STRING;
    public static final String JPOSENTRIES_STRING = JposEntryEditorMsg.JPOSENTRIES_STRING;


}
