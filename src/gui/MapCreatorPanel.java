package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
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

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
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

import game.Direction;
import game.GridCoordinates;
import game.Map;
import game.TileType;

/**
 * Créateur de carte
 */
public class MapCreatorPanel extends JPanel implements MouseListener, MouseMotionListener, ActionListener, ChangeListener {
    public static final String MAP_EXTENSION = "map";

    private boolean isLoading;

    private boolean saved;

    private MainWindow mainWindow;

    private GameSettings settings;

    private GameViewer gameViewer;

    private NewAction newAction;
    private OpenAction openAction;
    private SaveAction saveAction;
    private SaveAction saveAsAction;
    private UndoAction undoAction;
    private RedoAction redoAction;
    private ReturnAction returnAction;
    private HelpAction helpAction;
    
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
    
    private JButton btnNew;

    private JButton btnOpen;

    private JButton btnSave;

    private JFileChooser fileChooser;
    
    private JButton btnUndo;
    
    private JButton btnRedo;

    private JLabel lblColumns;

    private JSpinner columnCount;

    private JLabel lblRows;

    private JSpinner rowCount;

    private JLabel lblTiles;

    private ButtonGroup tileTypeGroup;

    private JButton btnReturn;

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
    public MapCreatorPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        setLayout(new BorderLayout(0, 0));
        
        this.settings = GameSettings.getInstance();
        
        undoStack = new Stack<>();
        redoStack = new Stack<>();
        
        newAction = new NewAction("Nouveau");
        newAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        openAction = new OpenAction("Ouvrir...");
        openAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        saveAction = new SaveAction("Enregistrer", false);
        saveAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        saveAsAction = new SaveAction("Enregistrer sous...", true);
        saveAsAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK));
        undoAction = new UndoAction("Annuler");
        undoAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
        redoAction = new RedoAction("Rétablir");
        redoAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK));
        returnAction = new ReturnAction("Retour au menu");
        helpAction = new HelpAction("Aide...");

        updateUndoRedo();
        
        menuBar = new JMenuBar();
        add(menuBar, BorderLayout.NORTH);
        
        fileMenu = new JMenu("Fichier");
        menuBar.add(fileMenu);
        
        itmNew = new JMenuItem(newAction);
        fileMenu.add(itmNew);
        
        itmOpen = new JMenuItem(openAction);
        fileMenu.add(itmOpen);
        
        itmSave = new JMenuItem(saveAction);
        fileMenu.add(itmSave);
        
        itmSaveAs = new JMenuItem(saveAsAction);
        fileMenu.add(itmSaveAs);
        
        fileMenu.addSeparator();
        
        itmReturn = new JMenuItem(returnAction);
        fileMenu.add(itmReturn);
        
        editMenu = new JMenu("Édition");
        menuBar.add(editMenu);
        
        itmUndo = new JMenuItem(undoAction);
        editMenu.add(itmUndo);

        itmRedo = new JMenuItem(redoAction);
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
        
        itmHelp = new JMenuItem(helpAction);
        questionMenu.add(itmHelp);
        
        JPanel content = new JPanel(new BorderLayout(0,0));
        add(content, BorderLayout.CENTER);
        
        JToolBar toolBar = new JToolBar();
        content.add(toolBar, BorderLayout.NORTH);
        
        btnNew = new JButton(newAction);
        btnNew.setHideActionText(true);
        toolBar.add(btnNew);
        
        btnOpen = new JButton(openAction);
        btnOpen.setHideActionText(true);
        toolBar.add(btnOpen);
        
        btnSave = new JButton(saveAction);
        btnSave.setHideActionText(true);
        toolBar.add(btnSave);
               
        fileChooser = new JFileChooser(".");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Cartes BombINSA", MAP_EXTENSION));
        
        toolBar.addSeparator();
        
        btnUndo = new JButton(undoAction);
        btnUndo.setHideActionText(true);
        toolBar.add(btnUndo);
        
        btnRedo = new JButton(redoAction);
        btnRedo.setHideActionText(true);
        toolBar.add(btnRedo);
        
        toolBar.addSeparator();
        
        lblColumns = new JLabel("L : ");
        toolBar.add(lblColumns);
        columnCount = new JSpinner(new SpinnerNumberModel(new Integer(20), new Integer(1), null, new Integer(1)));
        columnCount.setAlignmentX(Component.LEFT_ALIGNMENT);
        columnCount.addChangeListener(this);
        toolBar.add(columnCount);
        toolBar.addSeparator();
        
        lblRows = new JLabel("H : ");
        toolBar.add(lblRows);
        rowCount = new JSpinner(new SpinnerNumberModel(new Integer(15), new Integer(1), null, new Integer(1)));
        rowCount.setAlignmentX(Component.LEFT_ALIGNMENT);
        rowCount.addChangeListener(this);
        toolBar.add(rowCount);
        toolBar.addSeparator();
        
        lblTiles = new JLabel("Tuiles : ");
        toolBar.add(lblTiles);
        
        gameViewer = new GameViewer();
        gameViewer.setShowSpawningLocations(true);
        gameViewer.addMouseListener(this);
        gameViewer.addMouseMotionListener(this);
        gameViewer.addComponentListener(new ComponentListener() {
            @Override
            public void componentShown(ComponentEvent e) {
            }
            
            @Override
            public void componentResized(ComponentEvent e) {
                updateMap();
            }
            
            @Override
            public void componentMoved(ComponentEvent e) {
            }
            
            @Override
            public void componentHidden(ComponentEvent e) {
            }
        });
        content.add(gameViewer, BorderLayout.CENTER);
        
        gameViewer.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, ActionEvent.CTRL_MASK), "move_up");
        gameViewer.getActionMap().put("move_up", new MoveAction(Direction.Up));
        gameViewer.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, ActionEvent.CTRL_MASK), "move_down");
        gameViewer.getActionMap().put("move_down", new MoveAction(Direction.Down));
        gameViewer.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, ActionEvent.CTRL_MASK), "move_left");
        gameViewer.getActionMap().put("move_left", new MoveAction(Direction.Left));
        gameViewer.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, ActionEvent.CTRL_MASK), "move_right");
        gameViewer.getActionMap().put("move_right", new MoveAction(Direction.Right));
        
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
        
        btnReturn = new JButton(returnAction);
        btnReturn.setHideActionText(true);
        toolBar.add(btnReturn);
        
        updateUISize();
        
        setSaved(true);
        newMap();
    }

    /**
     * Met à jour la taille de l'interface
     */
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
        
        newAction.putValue(Action.SMALL_ICON, factory.getImageIcon("New16", settings.scale(16)));
        openAction.putValue(Action.SMALL_ICON, factory.getImageIcon("Open16", settings.scale(16)));
        saveAction.putValue(Action.SMALL_ICON, factory.getImageIcon("Save16", settings.scale(16)));
        saveAsAction.putValue(Action.SMALL_ICON, factory.getImageIcon("SaveAs16", settings.scale(16)));
        undoAction.putValue(Action.SMALL_ICON, factory.getImageIcon("Undo16", settings.scale(16)));
        redoAction.putValue(Action.SMALL_ICON, factory.getImageIcon("Redo16", settings.scale(16)));
        returnAction.putValue(Action.SMALL_ICON, factory.getImageIcon("Stop16", settings.scale(16)));
        helpAction.putValue(Action.SMALL_ICON, factory.getImageIcon("Help16", settings.scale(16)));
        
        newAction.putValue(Action.LARGE_ICON_KEY, factory.getImageIcon("New24", size));
        openAction.putValue(Action.LARGE_ICON_KEY, factory.getImageIcon("Open24", size));
        saveAction.putValue(Action.LARGE_ICON_KEY, factory.getImageIcon("Save24", size));
        saveAsAction.putValue(Action.LARGE_ICON_KEY, factory.getImageIcon("SaveAs24", size));
        undoAction.putValue(Action.LARGE_ICON_KEY, factory.getImageIcon("Undo24", size));
        redoAction.putValue(Action.LARGE_ICON_KEY, factory.getImageIcon("Redo24", size));
        returnAction.putValue(Action.LARGE_ICON_KEY, factory.getImageIcon("Stop24", size));
        helpAction.putValue(Action.LARGE_ICON_KEY, factory.getImageIcon("Help24", size));
        
        MainWindow.setFontSize(lblColumns, size/2);
        MainWindow.setFontSize(columnCount, size/2);
        MainWindow.setFontSize(lblRows, size/2);
        MainWindow.setFontSize(rowCount, size/2);
        
        MainWindow.setFontSize(lblTiles, size/2);
        
        ((JSpinner.DefaultEditor) columnCount.getEditor()).getTextField().setColumns(2);
        ((JSpinner.DefaultEditor) rowCount.getEditor()).getTextField().setColumns(2);

        int spinnerWidth = columnCount.getMinimumSize().width;
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
    
    private void setSaved(boolean saved) {
        this.saved = saved;
        saveAction.setEnabled(!saved);
    }
    
    /**
     * Crée une nouvelle carte vierge
     */
    public void newMap() {
        if (!checkSaved())
            return;
        
        map = new Map((int) columnCount.getValue(), (int) rowCount.getValue(), settings.tileSize);
        setSaved(true);
        setFile(null);
        clearUndoStack();
        updateMap();
    }

    /**
     * Met à jour l'affichage de la carte
     */
    private void updateMap() {
        gameViewer.drawMap(map);
    }

    /**
     * @return Le type de la tuile actuellement sélectionnée
     */
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

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        gameViewer.requestFocusInWindow();
        
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
            setSaved(false);
            updateMap();
        } else if (SwingUtilities.isLeftMouseButton(e) != SwingUtilities.isRightMouseButton(e)) {
            newUndoTask();
            placeTile(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        placeTile(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == itmBorders)
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
    }

    /**
     * @return vrai si toutes les modifications ont été sauvegardées ou si l'utilisateur ne veut pas les enregistrer
     */
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
        if (file != null)
            mainWindow.setSuffixTitle(file.getName());
        else
            mainWindow.setSuffixTitle(null);
    }
    
    /**
     * Propose à l'utilisateur d'ouvrir un fichier
     */
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
                clearUndoStack();
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
    private void saveToFile(boolean ask) {
        if (map.getSpawningLocations().isEmpty() &&
                JOptionPane.showConfirmDialog(this, "Aucun emplacement d'apparition n'a été défini.\n"
                + "Êtes-vous sûr de vouloir enregistrer cette carte ?",
                "Aucun emplacement d'apparition défini",
                JOptionPane.YES_NO_OPTION,
               JOptionPane.WARNING_MESSAGE) == JOptionPane.NO_OPTION)
            return;
        
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
            setSaved(true);
            return true;
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this,
                    "Impossible de sauvegarder dans" + file.getName() + "!",
                    "Erreur lors de la sauvegarde",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

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
    
    private void showHelp() {
        JOptionPane.showMessageDialog(this,
                "Clic-gauche : placer la tuile sélectionnée\n"+
                    "Clic-droit : placer une tuile vide\n"+
                    "Clic-milieu : placer/enlever un emplacement d'apparition\n"+
                    "Ctrl + flèche : déplace toutes les tuiles dans une direction",
                "Aide",
                JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void updateUndoRedo() {
        undoAction.setEnabled(!undoStack.isEmpty());
        redoAction.setEnabled(!redoStack.isEmpty());
    }
    
    private void newUndoTask() {
        newUndoTask(true);
    }
    
    private void newUndoTask(boolean clearRedo) {
        undoStack.push(new Stack<>());
        
        if (clearRedo)
            redoStack.clear();
        
        updateUndoRedo();
    }
    
    private void addUndoTask(UndoTask task) {
        undoStack.peek().push(task);
        setSaved(false);
    }
    
    private void clearUndoStack() {
        undoStack.clear();
        redoStack.clear();
        
        updateUndoRedo();
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

        updateUndoRedo();
    }
    
    private void redo() {
        if (redoStack.isEmpty())
            return;
        
        Stack<UndoTask> infos = redoStack.pop();
        
        newUndoTask(false);
        executeTask(infos);
        
        updateUndoRedo();
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
    
    private class NewAction extends AbstractAction {
        public NewAction(String name) {
            super(name);
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            newMap();
        }
    }
    
    private class OpenAction extends AbstractAction {
        public OpenAction(String name) {
            super(name);
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            openFile();
        }
    }
    
    private class SaveAction extends AbstractAction {
        private boolean ask;
        
        public SaveAction(String name, boolean ask) {
            super(name);
            this.ask = ask;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            saveToFile(ask);
        }
    }
    
    private class UndoAction extends AbstractAction {
        public UndoAction(String name) {
            super(name);
        }        
   
        @Override
        public void actionPerformed(ActionEvent e) {
            undo();
        }
    }
    
    private class RedoAction extends AbstractAction {
        public RedoAction(String name) {
            super(name);
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            redo();
        }
    }
    
    private class ReturnAction extends AbstractAction {
        public ReturnAction(String name) {
            super(name);
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            if (checkSaved())
                mainWindow.showMenu();
        }
    }
    
    private class HelpAction extends AbstractAction {
        public HelpAction(String name) {
            super(name);
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            showHelp();
        }
    }
    
    
    private class MoveAction extends AbstractAction {
        private Direction direction;
        
        public MoveAction(Direction direction) {
            this.direction = direction;
        }
        
        private void copyTile(GridCoordinates from, GridCoordinates to) {
            if (map.isInsideMap(from)) {
                setTileType(map.getTileType(from), to);
                if (map.getTileType(from) == TileType.Arrow)
                    map.setArrowDirection(map.getArrowDirection(from), to);
            }
            else
                setTileType(TileType.Empty, to);
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            newUndoTask();

            GridCoordinates to = new GridCoordinates();
            Direction opposite = Direction.getOpposite(direction);
            if (direction == Direction.Up || direction == Direction.Left) {
                for (to.x = 0; to.x < map.getColumnCount(); to.x++) {
                    for (to.y = 0; to.y < map.getRowCount(); to.y++) {
                        copyTile(to.neighbor(opposite), to);
                    }
                }
            } else {
                for (to.x = map.getColumnCount()-1; to.x >= 0; to.x--) {
                    for (to.y = map.getRowCount()-1; to.y >= 0; to.y--) {
                        copyTile(to.neighbor(opposite), to);
                    }
                }
            }
            
            updateMap();
        }
    }
}
