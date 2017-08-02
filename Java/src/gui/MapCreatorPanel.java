package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.Enumeration;
import java.util.Stack;

import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
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

    private JMenuBar menuBar;
    
    private JMenu fileMenu;
    
    private JMenu editMenu;
    
    private JMenu mirrorMenu;
    
    private JMenu questionMenu;
    
    private JMenuItem itmNew;
    
    private JMenuItem itmOpen;
    
    private JMenuItem itmSave;
    
    private JMenuItem itmSaveAs;
    
    private JMenuItem itmReturn;
    
    private JMenuItem itmUndo;
    
    private JMenuItem itmRedo;
    
    private JMenuItem itmBorders;
    
    private JMenuItem itmMirrorLtR;
    
    private JMenuItem itmMirrorRtL;
    
    private JMenuItem itmMirrorTtB;
    
    private JMenuItem itmMirrorBtT;
    
    private JMenuItem itmMirrorTL;
    
    private JMenuItem itmMirrorTR;
    
    private JMenuItem itmMirrorBL;
    
    private JMenuItem itmMirrorBR;
    
    private JMenuItem itmHelp;
    
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
    private JButton btnReturn;

    @objid ("0557317e-dc8a-4c00-a7a9-e1ed7ce62fa8")
    private Map map;
    
    private File file;
    
    private Stack<Stack<UndoTask>> undoStack;
    
    private Stack<Stack<UndoTask>> redoStack;

    private static final int ICON_SIZE = 24;
    
    private interface UndoTask { }
    
    private class PlaceTileTask implements UndoTask {
        public TileType type;
        public GridCoordinates position;
        public Direction direction;
        
        public PlaceTileTask(TileType type, GridCoordinates gc) {
            this(type, gc, null);
        }
        
        public PlaceTileTask(TileType type, GridCoordinates gc, Direction direction) {
            this.type = type;
            this.position = new GridCoordinates(gc);
            this.direction = direction;
        }
    }
    
    private class PlaceSpawnTask implements UndoTask {
        public int index;
        public GridCoordinates position;
        
        public PlaceSpawnTask(int index, GridCoordinates gc) {
            this.index = index;
            this.position = new GridCoordinates(gc);
        }
    }
    
    private class SetSizeTask implements UndoTask {
        public int width;
        public int height;
        
        public SetSizeTask(int width, int height) {
            this.width = width;
            this.height = height;
        }
    }
    
    /**
     * Construit le créateur de carte
     * @param mainWindow Fenêtre principale
     */
    @objid ("3eb3ee4b-6c57-4bb6-a160-6e58e812f6e2")
    public MapCreatorPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        setLayout(new BorderLayout(0, 0));
        
        this.settings = GameSettings.getInstance();
        
        
        menuBar = new JMenuBar();
        add(menuBar, BorderLayout.NORTH);
        
        fileMenu = new JMenu("Fichier");
        menuBar.add(fileMenu);
        
        itmNew = new JMenuItem("Nouveau");
        itmNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        itmNew.addActionListener(this);
        fileMenu.add(itmNew);
        
        itmOpen = new JMenuItem("Ouvrir...");
        itmOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        itmOpen.addActionListener(this);
        fileMenu.add(itmOpen);
        
        itmSave = new JMenuItem("Enregistrer");
        itmSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        itmSave.addActionListener(this);
        fileMenu.add(itmSave);
        
        itmSaveAs = new JMenuItem("Enregistrer sous...");
        itmSaveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK));
        itmSaveAs.addActionListener(this);
        fileMenu.add(itmSaveAs);
        
        fileMenu.addSeparator();
        
        itmReturn = new JMenuItem("Retour au menu");
        itmReturn.addActionListener(this);
        fileMenu.add(itmReturn);
        
        editMenu = new JMenu("Édition");
        menuBar.add(editMenu);
        
        itmUndo = new JMenuItem("Annuler");
        itmUndo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
        itmUndo.addActionListener(this);
        itmUndo.setEnabled(false);
        editMenu.add(itmUndo);

        itmRedo = new JMenuItem("Rétablir");
        itmRedo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK));
        itmRedo.addActionListener(this);
        itmRedo.setEnabled(false);
        editMenu.add(itmRedo);
        
        itmBorders = new JMenuItem("Bordures indestructibles");
        itmBorders.addActionListener(this);
        editMenu.add(itmBorders);
        
        mirrorMenu = new JMenu("Mirroir");
        editMenu.add(mirrorMenu);
        
        itmMirrorLtR = new JMenuItem("Gauche vers la droite");
        itmMirrorLtR.addActionListener(this);
        mirrorMenu.add(itmMirrorLtR);
        
        itmMirrorRtL = new JMenuItem("Droite vers la gauche");
        itmMirrorRtL.addActionListener(this);
        mirrorMenu.add(itmMirrorRtL);
        
        itmMirrorTtB = new JMenuItem("Haut vers le bas");
        itmMirrorTtB.addActionListener(this);
        mirrorMenu.add(itmMirrorTtB);
        
        itmMirrorBtT = new JMenuItem("Bas vers le haut");
        itmMirrorBtT.addActionListener(this);
        mirrorMenu.add(itmMirrorBtT);
        
        itmMirrorTL = new JMenuItem("Haut-gauche vers autres");
        itmMirrorTL.addActionListener(this);
        mirrorMenu.add(itmMirrorTL);
        
        itmMirrorTR = new JMenuItem("Haut-droite vers autres");
        itmMirrorTR.addActionListener(this);
        mirrorMenu.add(itmMirrorTR);
        
        itmMirrorBL = new JMenuItem("Bas-gauche vers autres");
        itmMirrorBL.addActionListener(this);
        mirrorMenu.add(itmMirrorBL);
        
        itmMirrorBR = new JMenuItem("Bas-droite vers autres");
        itmMirrorBR.addActionListener(this);
        mirrorMenu.add(itmMirrorBR);
        
        questionMenu = new JMenu("?");
        menuBar.add(questionMenu);
        
        itmHelp = new JMenuItem("Aide...");
        itmHelp.addActionListener(this);
        questionMenu.add(itmHelp);
        
        JPanel content = new JPanel(new BorderLayout(0,0));
        add(content, BorderLayout.CENTER);
        
        JToolBar toolBar = new JToolBar();
        content.add(toolBar, BorderLayout.NORTH);
        
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
        
        lblColumns = new JLabel("L : ");
        toolBar.add(lblColumns);
        columnCount = new JSpinner(new SpinnerNumberModel(new Integer(20), new Integer(1), null, new Integer(1)));
        columnCount.setAlignmentX(JSpinner.LEFT_ALIGNMENT);
        columnCount.addChangeListener(this);
        toolBar.add(columnCount);
        toolBar.addSeparator();
        
        lblRows = new JLabel("H : ");
        toolBar.add(lblRows);
        rowCount = new JSpinner(new SpinnerNumberModel(new Integer(15), new Integer(1), null, new Integer(1)));
        rowCount.setAlignmentX(JSpinner.LEFT_ALIGNMENT);
        rowCount.addChangeListener(this);
        toolBar.add(rowCount);
        toolBar.addSeparator();
        
        lblTiles = new JLabel("Tuiles : ");
        toolBar.add(lblTiles);
        gameViewer = new GameViewer();
        gameViewer.setShowSpawningLocations(true);
        gameViewer.addMouseListener(this);
        gameViewer.addMouseMotionListener(this);
        content.add(gameViewer, BorderLayout.CENTER);
        
        tileTypeGroup = new ButtonGroup();
        for (TileType type : TileType.values()) {
            JToggleButton button = new JToggleButton();
            button.setActionCommand(type.name());
            tileTypeGroup.add(button);
            toolBar.add(button);           
            
            if (type == TileType.Breakable)
                button.setSelected(true);
        }
        
        toolBar.add(Box.createGlue());
        
        btnReturn = new JButton();
        btnReturn.addActionListener(this);
        toolBar.add(btnReturn);
        
        undoStack = new Stack<>();
        
        redoStack = new Stack<>();
        
        updateUISize();
        
        newMap();
    }

    /**
     * Met à jour la taille de l'interface
     */
    @objid ("d54323e5-69dc-4edd-8f55-4a3c3157f552")
    private void updateUISize() {
        final int size = settings.scale(ICON_SIZE);
        
        settings.scaleFont(menuBar);
        settings.scaleFont(fileMenu);
        settings.scaleFont(editMenu);
        settings.scaleFont(mirrorMenu);
        settings.scaleFont(questionMenu);
        settings.scaleFont(itmNew);
        settings.scaleFont(itmOpen);
        settings.scaleFont(itmSave);
        settings.scaleFont(itmSaveAs);
        settings.scaleFont(itmReturn);
        settings.scaleFont(itmUndo);
        settings.scaleFont(itmRedo);
        settings.scaleFont(itmBorders);
        settings.scaleFont(itmMirrorLtR);
        settings.scaleFont(itmMirrorRtL);
        settings.scaleFont(itmMirrorTtB);
        settings.scaleFont(itmMirrorBtT);
        settings.scaleFont(itmMirrorTL);
        settings.scaleFont(itmMirrorTR);
        settings.scaleFont(itmMirrorBL);
        settings.scaleFont(itmMirrorBR);
        settings.scaleFont(itmHelp);
        
        SpriteFactory factory = SpriteFactory.getInstance();
        btnNew.setIcon(factory.getImageIcon("New24", size));
        btnOpen.setIcon(factory.getImageIcon("Open24", size));
        btnSave.setIcon(factory.getImageIcon("Save24", size));
        btnReturn.setIcon(factory.getImageIcon("Stop24", size));
        
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
        
        settings.scaleFontComponents(fileChooser.getComponents());
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
        if (SwingUtilities.isLeftMouseButton(e) == SwingUtilities.isRightMouseButton(e))
            return;
        
        GridCoordinates gc = mousePosition(e);
        if (tileTypeGroup.getSelection() != null && map.isInsideMap(gc)) {
            TileType type;
            
            if (SwingUtilities.isLeftMouseButton(e))
                type = getActualType();
            else //Right
                type = TileType.Empty;
            
            if (map.getTileType(gc) != type) {
                setTileType(type, gc);
                updateMap();
            }
        }
    }
    
    private void placeSpawningLocation(GridCoordinates gc) {
        placeSpawningLocation(-1, gc);
    }
    
    private void placeSpawningLocation(int index, GridCoordinates gc) {
        int previous = map.getSpawningLocations().indexOf(gc);
        if (previous >= 0)
            map.removeSpawningLocation(gc);
        else {
            if (index >= 0)
                map.addSpawningLocation(index, gc);
            else
                map.addSpawningLocation(gc);
        }
        
        addUndoTask(new PlaceSpawnTask(previous, gc));
    }

    @objid ("02364307-a9ca-449a-8110-2e7ee527d368")
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @objid ("41e7252e-3fe2-4ef8-89c1-e2026e638132")
    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @objid ("fb60a80c-3a92-4752-9afd-d606bbed59f8")
    @Override
    public void mouseExited(MouseEvent e) {
    }
    
    @objid ("21292e44-21b2-4f77-bd31-c1f442411cab")
    @Override
    public void mousePressed(MouseEvent e) {
        GridCoordinates gc = mousePosition(e);
        if (e.getButton() == MouseEvent.BUTTON2) {
            if (map.isInsideMap(gc)) {
                newUndoTask();
                placeSpawningLocation(gc);
                updateMap();
            }
        } else if (e.getButton() == MouseEvent.BUTTON1 &&
                getActualType() == TileType.Arrow && map.isInsideMap(gc) &&
                map.getTileType(gc) == TileType.Arrow) {
            Direction[] directions = Direction.values();
            int i = (map.getArrowDirection(gc).ordinal()+1)%directions.length;
            map.setArrowDirection(directions[i], gc);
            saved = false;
            updateMap();
        } else if (SwingUtilities.isLeftMouseButton(e) != SwingUtilities.isRightMouseButton(e)) {
            newUndoTask();
            placeTile(e);
        }
    }

    @objid ("c8bed0d2-ee70-454d-b575-724198d71c59")
    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @objid ("21696904-e3c3-4b05-8eb7-48db03a86d58")
    @Override
    public void mouseDragged(MouseEvent e) {
        placeTile(e);
    }

    @objid ("c19af884-3588-4b86-85ca-beb0112bafdf")
    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @objid ("2949aafd-f13a-4c7a-a4bc-750ccef814a4")
    @Override
    public void actionPerformed(ActionEvent e) {
        if ((e.getSource() == btnNew || e.getSource() == itmNew) && checkSaved())
            newMap();
        else if (e.getSource() == btnOpen || e.getSource() == itmOpen)
            openFile();
        else if (e.getSource() == btnSave || e.getSource() == itmSave)
            saveToFile(false);
        else if (e.getSource() == itmSaveAs)
            saveToFile(true);
        else if (e.getSource() == btnReturn || e.getSource() == itmReturn) {
            if (checkSaved())
                mainWindow.showMenu();
        }
        else if (e.getSource() == itmUndo)
            undo();
        else if (e.getSource() == itmRedo)
            redo();
        else if (e.getSource() == itmBorders)
            createUnbreakableBorders();
        else if (e.getSource() == itmMirrorLtR)
            mirrorLtR();
        else if (e.getSource() == itmMirrorRtL)
            mirrorRtL();
        else if (e.getSource() == itmMirrorTtB)
            mirrorTtB();
        else if (e.getSource() == itmMirrorBtT)
            mirrorBtT(); 
        else if (e.getSource() == itmMirrorTL) {
            mirrorTtB();
            mirrorLtR();
        } else if (e.getSource() == itmMirrorTR) {
            mirrorTtB();
            mirrorRtL();
        } else if (e.getSource() == itmMirrorBL) {
            mirrorBtT();
            mirrorLtR();
        } else if (e.getSource() == itmMirrorBR) {
            mirrorBtT();
            mirrorRtL();
        }
        else if (e.getSource() == itmHelp)
            showHelp();
    }

    /**
     * @return vrai si toutes les modifications ont été sauvegardées ou si l'utilisateur ne veut pas les enregistrer
     */
    @objid ("0d4a0b27-2c68-4e19-af8c-4959d1c79097")
    public boolean checkSaved() {
        if (saved)
            return true;
        
        if (JOptionPane.showConfirmDialog(this, "Voulez-vous sauvegarder votre travail ?",
                                                "Modifications non sauvegardées",
                                                JOptionPane.YES_NO_OPTION,
                                               JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
            saveToFile(false);
            return saved;
        } else
            return true;
    }

    private void setFile(File file) {
        this.file = file;
        mainWindow.setSuffixTitle(file.getName());
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
                setFile(fileChooser.getSelectedFile());
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

    private File askSaveFile() {
        int ret = fileChooser.showSaveDialog(this);
        if (ret == JFileChooser.APPROVE_OPTION) {
            File f = fileChooser.getSelectedFile();
            if (!f.getAbsolutePath().endsWith("." + MAP_EXTENSION)) {
                f = new File(fileChooser.getSelectedFile() + "." + MAP_EXTENSION);
            }
            
            return f;
        }
        
        return null;
    }
    
    /**
     * Propose à l'utilisateur d'enregistrer la carte
     */
    @objid ("769a1578-c2bd-4c61-bcca-133d69c17718")
    private void saveToFile(boolean ask) {
        if (file == null || ask) {
            File f = askSaveFile();
            if (f != null && saveToFile(f))
                setFile(f);
        } else
            saveToFile(file);
    }
    
    private boolean saveToFile(File f) {
        if (f == null)
            return false;
        
        try {
            PrintWriter printWriter = new PrintWriter(f);
            printWriter.write(map.saveMap());
            printWriter.close();
            saved = true;
            return true;
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this,
                    "Impossible de sauvegarder dans" + file.getName() + "!",
                    "Erreur lors de la sauvegarde",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    @objid ("b1fe0839-7272-4438-b2f9-74963a553532")
    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == columnCount || e.getSource() == rowCount) {
            if (!isLoading) {
                newUndoTask();
                setMapSize((int) columnCount.getValue(), (int) rowCount.getValue());
                updateMap();
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        updateMap();
        super.paintComponent(g);
    }
    
    private void showHelp() {
        JOptionPane.showMessageDialog(this,
                "Clic-gauche : placer la tuile sélectionnée\n"+
                    "Clic-droit : placer une tuile vide\n"+
                    "Clic-milieu : placer/enlever un emplacement d'apparition",
                "Aide",
                JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void newUndoTask() {
        newUndoTask(true);
    }
    
    private void newUndoTask(boolean clearRedo) {
        undoStack.push(new Stack<>());
        itmUndo.setEnabled(true);
        
        if (clearRedo) {
            redoStack.clear();
            itmRedo.setEnabled(false);
        }
    }
    
    private void addUndoTask(UndoTask task) {
        undoStack.peek().push(task);
        saved = false;
    }
    
    private void setTileType(TileType type, GridCoordinates gc) {
        TileType previousType = map.getTileType(gc);
        if (previousType != TileType.Arrow)
            addUndoTask(new PlaceTileTask(previousType, gc));
        else
            addUndoTask(new PlaceTileTask(TileType.Arrow, gc, map.getArrowDirection(gc)));

        map.setTileType(type, gc);
    }
    
    private void setMapSize(int width, int height) {
        if (map.getColumnCount() == width && map.getRowCount() == height)
            return;
        
        GridCoordinates gc = new GridCoordinates();
        for (gc.x = width; gc.x < map.getColumnCount(); gc.x++) {
            for (gc.y = 0; gc.y < map.getRowCount(); gc.y++)
                setTileType(TileType.Empty, gc);
        }
        
        for (gc.y = height; gc.y < map.getRowCount(); gc.y++) {
            for (gc.x = 0; gc.x < Math.min(width, map.getColumnCount()); gc.x++)
                setTileType(TileType.Empty, gc);
        }
        
        addUndoTask(new SetSizeTask(map.getColumnCount(), map.getRowCount()));
        
        map.setSize(width, height);
        
        isLoading = true;
        if ((int)columnCount.getValue() != width)
            columnCount.setValue(width);
        if ((int)columnCount.getValue() != height)
            rowCount.setValue(height);
        isLoading = false;
    }
    
    private void undo() {
        if (undoStack.isEmpty())
            return;
        
        Stack<UndoTask> infos = undoStack.pop();
        
        newUndoTask(false);
        executeTask(infos);
        redoStack.push(undoStack.pop());
        itmRedo.setEnabled(true);
        
        if (undoStack.isEmpty())
            itmUndo.setEnabled(false);
    }
    
    private void redo() {
        if (redoStack.isEmpty())
            return;
        
        Stack<UndoTask> infos = redoStack.pop();
        
        newUndoTask(false);
        executeTask(infos);
        itmUndo.setEnabled(true);
        
        if (redoStack.isEmpty())
            itmRedo.setEnabled(false);
    }
    
    private void executeTask(Stack<UndoTask> tasks) {
        while (!tasks.isEmpty()) {
            UndoTask task = tasks.pop();
            
            if (task instanceof PlaceTileTask) {
                setTileType(((PlaceTileTask)task).type, ((PlaceTileTask)task).position);
                if (((PlaceTileTask)task).type == TileType.Arrow)
                    map.setArrowDirection(((PlaceTileTask)task).direction, ((PlaceTileTask)task).position);
            }
            else if (task instanceof PlaceSpawnTask)
                placeSpawningLocation(((PlaceSpawnTask)task).index, ((PlaceSpawnTask)task).position);
            else if (task instanceof SetSizeTask)
                setMapSize(((SetSizeTask)task).width, ((SetSizeTask)task).height);
        }
        
        updateMap();
    }
    
    private void createUnbreakableBorders() {
        newUndoTask();
        for (int x = 0; x < map.getColumnCount(); x++) {
            setTileType(TileType.Unbreakable, new GridCoordinates(x, 0));
            setTileType(TileType.Unbreakable, new GridCoordinates(x, map.getRowCount()-1));
        }
        
        for (int y = 0; y < map.getRowCount(); y++) {
            setTileType(TileType.Unbreakable, new GridCoordinates(0, y));
            setTileType(TileType.Unbreakable, new GridCoordinates(map.getColumnCount()-1, y));
        }
        
        updateMap();
    }
    
    private void mirrorLtR() {
        newUndoTask();
        GridCoordinates from = new GridCoordinates();
        GridCoordinates to = new GridCoordinates();
        for (from.x = 0, to.x = map.getColumnCount()-1; to.x >= map.getColumnCount()/2; from.x++, to.x--) {
            for (from.y = 0, to.y = 0; from.y < map.getRowCount(); from.y++, to.y++) {
                setTileType(map.getTileType(from), to);
                if (map.getTileType(from) == TileType.Arrow) {
                    if (Direction.doHaveSameAxis(map.getArrowDirection(from), Direction.Left))
                        map.setArrowDirection(Direction.getOpposite(map.getArrowDirection(from)), to);
                    else
                        map.setArrowDirection(map.getArrowDirection(from), to);
                }
            }
        }
        
        updateMap();
    }
    
    private void mirrorRtL() {
        newUndoTask();
        GridCoordinates from = new GridCoordinates();
        GridCoordinates to = new GridCoordinates();
        for (from.x = map.getColumnCount()-1, to.x = 0; from.x >= map.getColumnCount()/2; from.x--, to.x++) {
            for (from.y = 0, to.y = 0; from.y < map.getRowCount(); from.y++, to.y++) {
                setTileType(map.getTileType(from), to);
                if (map.getTileType(from) == TileType.Arrow) {
                    if (Direction.doHaveSameAxis(map.getArrowDirection(from), Direction.Right))
                        map.setArrowDirection(Direction.getOpposite(map.getArrowDirection(from)), to);
                    else
                        map.setArrowDirection(map.getArrowDirection(from), to);
                }
            }
        }
        
        updateMap();
    }
    
    private void mirrorTtB() {
        newUndoTask();
        GridCoordinates from = new GridCoordinates();
        GridCoordinates to = new GridCoordinates();
        for (from.y = 0, to.y = map.getRowCount()-1; to.y >= map.getRowCount()/2; from.y++, to.y--) {
            for (from.x = 0, to.x = 0; from.x < map.getColumnCount(); from.x++, to.x++) {
                setTileType(map.getTileType(from), to);
                if (map.getTileType(from) == TileType.Arrow) {
                    if (Direction.doHaveSameAxis(map.getArrowDirection(from), Direction.Up))
                        map.setArrowDirection(Direction.getOpposite(map.getArrowDirection(from)), to);
                    else
                        map.setArrowDirection(map.getArrowDirection(from), to);
                }
            }
        }
        
        updateMap();
    }
    
    private void mirrorBtT() {
        newUndoTask();
        GridCoordinates from = new GridCoordinates();
        GridCoordinates to = new GridCoordinates();
        for (from.y = map.getRowCount()-1, to.y = 0; from.y >= map.getRowCount()/2; from.y--, to.y++) {
            for (from.x = 0, to.x = 0; from.x < map.getColumnCount(); from.x++, to.x++) {
                setTileType(map.getTileType(from), to);
                if (map.getTileType(from) == TileType.Arrow) {
                    if (Direction.doHaveSameAxis(map.getArrowDirection(from), Direction.Down))
                        map.setArrowDirection(Direction.getOpposite(map.getArrowDirection(from)), to);
                    else
                        map.setArrowDirection(map.getArrowDirection(from), to);
                }
            }
        }
        
        updateMap();
    }
}
