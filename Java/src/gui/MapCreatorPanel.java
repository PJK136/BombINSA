package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.Enumeration;
import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import game.Direction;
import game.GridCoordinates;
import game.Map;
import game.TileType;

/**
 * Créateur de carte
 */
@objid ("79589b62-c371-4298-9764-59fe82333d18")
public class MapCreatorPanel extends JPanel implements MouseListener, MouseMotionListener, ActionListener, ChangeListener {
    @objid ("c7402902-b98b-4edd-bdc8-fd85d1bff610")
    public static final String MAP_EXTENSION = "map";

    @objid ("7d6f4c24-acd8-4ff0-9039-879bd9a7eef8")
    private boolean isLoading;

    @objid ("a44da426-ce92-44f2-a8bd-cf679893bd77")
    private boolean saved;

    @objid ("6b055ef2-7efa-4cfe-bc18-5b61140d417a")
    private MainWindow mainWindow;

    @objid ("41376f8b-3a86-4de1-b613-3f7ffd0dca79")
    private GameSettings settings;

    @objid ("eff07d4d-ac61-40ee-9ef3-63a075e20b93")
    private GameViewer gameViewer;

    @objid ("95094d21-2d75-4c57-8308-9c56f2cc1b29")
    private JButton btnNew;

    @objid ("71a5e56e-3406-4bef-b348-adb5f831b0b6")
    private JButton btnOpen;

    @objid ("234a003d-fede-43dd-a308-31de1c7e1404")
    private JButton btnSave;

    @objid ("a74309a1-8a29-49af-9580-683d214c1d58")
    private JFileChooser fileChooser;

    @objid ("71b49965-f22b-4c3b-8854-7b6478ecaa51")
    private JLabel lblColumns;

    @objid ("924bdc0a-5fa1-492d-83b1-da05a91172bf")
    private JSpinner columnCount;

    @objid ("38395be1-f6f0-4fa4-93b5-2a36b1ff1e16")
    private JLabel lblRows;

    @objid ("33f032e8-510d-4492-9392-5e231a876963")
    private JSpinner rowCount;

    @objid ("7303354e-0b01-45e3-810b-35c47aeb562f")
    private JLabel lblTiles;

    @objid ("c4e22a4a-9f86-4e90-bbab-53b28a792a2d")
    private ButtonGroup tileTypeGroup;

    @objid ("acd0f57d-4333-48b5-b372-d90eaf606e91")
    private JButton btnExit;

    @objid ("0557317e-dc8a-4c00-a7a9-e1ed7ce62fa8")
    private Map map;

    private static final int ICON_SIZE = 32;
    
    /**
     * Construit le créateur de carte
     * @param mainWindow Fenêtre principale
     */
    @objid ("3eb3ee4b-6c57-4bb6-a160-6e58e812f6e2")
    public MapCreatorPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        setLayout(new BorderLayout(0, 0));
        
        this.settings = GameSettings.getInstance();
        
        JToolBar toolBar = new JToolBar();
        add(toolBar, BorderLayout.NORTH);
        
        btnNew = new JButton();
        btnNew.addActionListener(this);
        toolBar.add(btnNew);
        
        btnOpen = new JButton();
        btnOpen.addActionListener(this);
        toolBar.add(btnOpen);
        
        btnSave = new JButton();
        btnSave.addActionListener(this);
        toolBar.add(btnSave);
        
        fileChooser = new JFileChooser(".");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Cartes BombINSA", MAP_EXTENSION));
        
        toolBar.addSeparator();
        
        lblColumns = new JLabel("Cols : ");
        toolBar.add(lblColumns);
        columnCount = new JSpinner(new SpinnerNumberModel(new Integer(20), new Integer(1), null, new Integer(1)));
        columnCount.addChangeListener(this);
        toolBar.add(columnCount);
        toolBar.addSeparator();
        
        lblRows = new JLabel("Rows : ");
        toolBar.add(lblRows);
        rowCount = new JSpinner(new SpinnerNumberModel(new Integer(15), new Integer(1), null, new Integer(1)));
        rowCount.addChangeListener(this);
        toolBar.add(rowCount);
        toolBar.addSeparator();
        
        lblTiles = new JLabel("Tiles : ");
        toolBar.add(lblTiles);
        gameViewer = new GameViewer();
        gameViewer.setShowSpawningLocations(true);
        gameViewer.addMouseListener(this);
        gameViewer.addMouseMotionListener(this);
        add(gameViewer, BorderLayout.CENTER);
        
        tileTypeGroup = new ButtonGroup();
        for (TileType type : TileType.values()) {
            JToggleButton button = new JToggleButton();
            button.setActionCommand(type.name());
            tileTypeGroup.add(button);
            toolBar.add(button);           
            
            if (type == TileType.Breakable)
                button.setSelected(true);
        }
        
        toolBar.add(Box.createHorizontalGlue());
        
        btnExit = new JButton();
        btnExit.addActionListener(this);
        toolBar.add(btnExit);
        
        updateUISize();
        
        newMap();
    }

    /**
     * Met à jour la taille de l'interface
     */
    @objid ("d54323e5-69dc-4edd-8f55-4a3c3157f552")
    private void updateUISize() {
        final int size = settings.scale(ICON_SIZE);
        SpriteFactory factory = SpriteFactory.getInstance();
        btnNew.setIcon(factory.getImageIcon("New24", size));
        btnOpen.setIcon(factory.getImageIcon("Open24", size));
        btnSave.setIcon(factory.getImageIcon("Save24", size));
        btnExit.setIcon(factory.getImageIcon("Stop24", size));
        
        MainWindow.setFontSize(lblColumns, size/2);
        MainWindow.setFontSize(columnCount, size/2);
        MainWindow.setFontSize(lblRows, size/2);
        MainWindow.setFontSize(rowCount, size/2);
        
        MainWindow.setFontSize(lblTiles, size/2);
        
        int spinnerWidth = columnCount.getMinimumSize().width*3/2;
        columnCount.setMaximumSize(new Dimension(spinnerWidth, btnNew.getMaximumSize().height));
        rowCount.setMaximumSize(new Dimension(spinnerWidth, btnNew.getMaximumSize().height));
        columnCount.setPreferredSize(new Dimension(spinnerWidth, btnNew.getPreferredSize().height));
        rowCount.setPreferredSize(new Dimension(spinnerWidth, btnNew.getPreferredSize().height));
        
        Sprite[] tileImages = gameViewer.getTileSprites();
        Enumeration<AbstractButton> buttons = tileTypeGroup.getElements();
        for (TileType type : TileType.values()) {
            AbstractButton button = buttons.nextElement();
            button.setIcon(new ImageIcon(tileImages[type.ordinal()].getImage(size)));
        }
    }

    /**
     * Crée une nouvelle carte vierge
     */
    @objid ("b65fa18f-a789-4435-8290-32da712f6f42")
    public void newMap() {
        map = new Map((int) columnCount.getValue(), (int) rowCount.getValue(), settings.tileSize);
        saved = true;
        updateMap();
    }

    /**
     * Met à jour l'affichage de la carte
     */
    @objid ("584fc0de-2056-4b35-92aa-74f1525b0941")
    private void updateMap() {
        gameViewer.drawMap(map);
    }

    /**
     * @return Le type de la tuile actuellement sélectionnée
     */
    @objid ("a60ab1b5-08e4-4dd7-8ef4-8c20d5a4c37c")
    private TileType getActualType() {
        return TileType.valueOf(tileTypeGroup.getSelection().getActionCommand());
    }
    
    private GridCoordinates mousePosition(MouseEvent e) {
        double x = (e.getX()-gameViewer.getOffsetX())/gameViewer.getScaleFactor();
        double y = (e.getY()-gameViewer.getOffsetY())/gameViewer.getScaleFactor();
        return map.toGridCoordinates(x, y);
    }

    /**
     * Place une tuile à l'emplacement désignée par la souris
     * @param e Évènement de la souris
     */
    @objid ("97716585-b324-4d2b-9f0c-a120bcf0b328")
    private void placeTile(MouseEvent e) {
        GridCoordinates gc = mousePosition(e);
        if (tileTypeGroup.getSelection() != null && map.isInsideMap(gc)) {
            TileType type;
            if (SwingUtilities.isLeftMouseButton(e))
                type = getActualType();
            else if (SwingUtilities.isRightMouseButton(e))
                type = TileType.Empty;
            else
                return;
            
            if (map.getTileType(gc) != type) {
                saved = false;
                map.setTileType(type, gc);
                updateMap();
            }
        }
    }

    @objid ("02364307-a9ca-449a-8110-2e7ee527d368")
    @Override
    public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isMiddleMouseButton(e)) {
            GridCoordinates gc = mousePosition(e);
            if (map.getSpawningLocations().contains(gc))
                map.removeSpawningLocation(gc);
            else
                map.addSpawningLocation(gc);
            updateMap();
        }
    }

    @objid ("41e7252e-3fe2-4ef8-89c1-e2026e638132")
    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @objid ("fb60a80c-3a92-4752-9afd-d606bbed59f8")
    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @objid ("21292e44-21b2-4f77-bd31-c1f442411cab")
    @Override
    public void mousePressed(MouseEvent e) {
        GridCoordinates gc = mousePosition(e);
        if (getActualType() == TileType.Arrow && map.isInsideMap(gc) && map.getTileType(gc) == TileType.Arrow) {
            Direction[] directions = Direction.values();
            int i = (map.getArrowDirection(gc).ordinal()+1)%directions.length;
            map.setArrowDirection(directions[i], gc);
            updateMap();
        } else
            placeTile(e);
    }

    @objid ("c8bed0d2-ee70-454d-b575-724198d71c59")
    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @objid ("21696904-e3c3-4b05-8eb7-48db03a86d58")
    @Override
    public void mouseDragged(MouseEvent e) {
        placeTile(e);
    }

    @objid ("c19af884-3588-4b86-85ca-beb0112bafdf")
    @Override
    public void mouseMoved(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @objid ("2949aafd-f13a-4c7a-a4bc-750ccef814a4")
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnNew && checkSaved()) {
            newMap();
        }
        if (e.getSource() == btnOpen)
            openFile();
        else if (e.getSource() == btnSave)
            saveToFile();     
        else if (e.getSource() == btnExit)
        {
            if (checkSaved())
                mainWindow.showMenu();
        }
    }

    /**
     * @return vrai si toutes les modifications ont été sauvegardées ou si l'utilisateur ne veut pas les enregistrer
     */
    @objid ("0d4a0b27-2c68-4e19-af8c-4959d1c79097")
    public boolean checkSaved() {
        if (saved)
            return true;
        return JOptionPane.showConfirmDialog(this,
                                                "Il y a des modifications non sauvegardées, êtes-vous sûr de vouloir continuer ?",
                                                "Modifications non sauvegardées",
                                                JOptionPane.YES_NO_OPTION,
                                                JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION;
    }

    /**
     * Propose à l'utilisateur d'ouvrir un fichier
     */
    @objid ("2b651672-46d6-4d34-8cb8-b75257bc894e")
    private void openFile() {
        if (!checkSaved())
            return;
        
        int ret = fileChooser.showOpenDialog(this);
        if (ret == JFileChooser.APPROVE_OPTION) {
            try {
                isLoading = true;
                map.loadMap(new String(Files.readAllBytes(fileChooser.getSelectedFile().toPath())));
                columnCount.setValue(map.getColumnCount());
                rowCount.setValue(map.getRowCount());
                updateMap();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Impossible de lire le fichier demandé !",
                        "Erreur lors de la lecture",
                        JOptionPane.ERROR_MESSAGE);
            } finally {
                isLoading = false;
            }
        }
    }

    /**
     * Propose à l'utilisateur d'enregistrer la carte
     */
    @objid ("769a1578-c2bd-4c61-bcca-133d69c17718")
    private void saveToFile() {
        int ret = fileChooser.showSaveDialog(this);
        if (ret == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fileChooser.getSelectedFile();
        
                if(!file.getAbsolutePath().endsWith("." + MAP_EXTENSION)){
                    file = new File(fileChooser.getSelectedFile() + "." + MAP_EXTENSION);
                }
                PrintWriter printWriter = new PrintWriter(file);
                printWriter.write(map.saveMap());
                printWriter.close();
                saved = true;
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(this,
                        "Impossible de sauvegarder dans le fichier spécifié !",
                        "Erreur lors de la sauvegarde",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @objid ("b1fe0839-7272-4438-b2f9-74963a553532")
    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == columnCount || e.getSource() == rowCount) {
            if (!isLoading) {
                map.setsize((int) columnCount.getValue(), (int) rowCount.getValue());
                updateMap();
                saved = false;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        updateMap();
    }
}
