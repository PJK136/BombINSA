package gui;

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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.InputMismatchException;

import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import game.Direction;
import game.GridCoordinates;
import game.Map;
import game.TileType;

public class MapCreatorPanel extends JPanel implements MouseListener, MouseMotionListener, ActionListener, ChangeListener {
    public static final String MAP_EXTENSION = "map";
    
    private MainWindow mainWindow;
    private GameViewer gameViewer;
    private Map map;
    
    private JButton btnNew;
    private JButton btnOpen;
    private JButton btnSave;
    private JFileChooser fileChooser;
    
    private JSpinner columnCount;
    private JSpinner rowCount;
    private JSpinner tileSize;
    
    private ButtonGroup tileTypeGroup;
    private JButton btnExit;
    
    private boolean isLoading;
    private boolean saved;
    /**
     * Create the panel.
     */
    public MapCreatorPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        setLayout(new BorderLayout(0, 0));
        
        JToolBar toolBar = new JToolBar();
        add(toolBar, BorderLayout.NORTH);
        
        btnNew = new JButton("📄");
        btnNew.addActionListener(this);
        toolBar.add(btnNew);

        btnOpen = new JButton("📂");
        btnOpen.addActionListener(this);
        toolBar.add(btnOpen);
        
        btnSave = new JButton("💾");
        btnSave.addActionListener(this);
        toolBar.add(btnSave);
        
        fileChooser = new JFileChooser(".");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Cartes BombINSA", MAP_EXTENSION));
        
        toolBar.addSeparator();
        
        columnCount = new JSpinner(new SpinnerNumberModel(new Integer(20), new Integer(1), null, new Integer(1)));
        columnCount.addChangeListener(this);
        toolBar.add(new JLabel("Cols : "));
        toolBar.add(columnCount);
        toolBar.addSeparator();
        
        rowCount = new JSpinner(new SpinnerNumberModel(new Integer(15), new Integer(1), null, new Integer(1)));
        rowCount.addChangeListener(this);
        toolBar.add(new JLabel("Rows : "));
        toolBar.add(rowCount);
        toolBar.addSeparator();
        
        tileSize = new JSpinner(new SpinnerNumberModel(new Integer(32), new Integer(8), null, new Integer(2)));
        tileSize.addChangeListener(this);
        toolBar.add(new JLabel("TS : "));
        toolBar.add(tileSize);
        toolBar.addSeparator();
        
        columnCount.setMaximumSize(new Dimension(48, btnNew.getMaximumSize().height));
        rowCount.setMaximumSize(new Dimension(48, btnNew.getMaximumSize().height));
        tileSize.setMaximumSize(new Dimension(48, btnNew.getMaximumSize().height));
        columnCount.setPreferredSize(new Dimension(48, btnNew.getPreferredSize().height));
        rowCount.setPreferredSize(new Dimension(48, btnNew.getPreferredSize().height));
        tileSize.setPreferredSize(new Dimension(48, btnNew.getPreferredSize().height));
        
        toolBar.add(new JLabel("Tiles : "));
        gameViewer = new GameViewer();
        gameViewer.setShowSpawningLocations(true);
        gameViewer.addMouseListener(this);
        gameViewer.addMouseMotionListener(this);
        add(gameViewer, BorderLayout.CENTER);
        
        tileTypeGroup = new ButtonGroup();
        Sprite[] tileImages = gameViewer.getTileSprites();
        for (TileType type : TileType.values()) {
            final int size = 20; // TODO : À ajuster ?
            JToggleButton button = new JToggleButton();
            button.setIcon(new ImageIcon(tileImages[type.ordinal()].getImage(size)));
            button.setActionCommand(type.name());
            tileTypeGroup.add(button);
            toolBar.add(button);           
            
            if (type == TileType.Breakable)
                button.setSelected(true);
        }
        
        toolBar.add(Box.createHorizontalGlue());
        
        btnExit = new JButton("🚪");
        btnExit.addActionListener(this);
        toolBar.add(btnExit);
        
        newMap(false);
    }

    private void newMap(boolean resizeWindow) {
        map = new Map((int) columnCount.getValue(), (int) rowCount.getValue(), (int) tileSize.getValue());
        saved = true;
        updateMap();
        if (resizeWindow)
            mainWindow.setToPreferredSize();
    }
    
    private void updateMap() {
        gameViewer.drawMap(map);
    }
    
    private TileType getActualType() {
        return TileType.valueOf(tileTypeGroup.getSelection().getActionCommand());
    }
    
    private void placeTile(MouseEvent e) {
        if (tileTypeGroup.getSelection() != null && map.isInsideMap(e.getX(), e.getY())) {
            TileType type;
            if (SwingUtilities.isLeftMouseButton(e))
                type = getActualType();
            else if (SwingUtilities.isRightMouseButton(e))
                type = TileType.Empty;
            else
                return;
            
            if (map.getTileType(e.getX(), e.getY()) != type) {
                saved = false;
                map.setTileType(type, e.getX(), e.getY());
                updateMap();
            }
        }
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isMiddleMouseButton(e)) {
            GridCoordinates gc = map.toGridCoordinates(e.getX(), e.getY());
            if (map.getSpawningLocations().contains(gc))
                map.removeSpawningLocation(gc);
            else
                map.addSpawningLocation(gc);
            updateMap();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (getActualType() == TileType.Arrow && map.isInsideMap(e.getX(), e.getY()) && map.getTileType(e.getX(), e.getY()) == TileType.Arrow) {
            Direction[] directions = Direction.values();
            int i = (map.getArrowDirection(e.getX(), e.getY()).ordinal()+1)%directions.length;
            map.setArrowDirection(directions[i], e.getX(), e.getY());
            updateMap();
        } else
            placeTile(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        placeTile(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnNew && isSaved()) {
            newMap(true);
        }
        if (e.getSource() == btnOpen)
            openFile();
        else if (e.getSource() == btnSave)
            saveToFile();     
        else if (e.getSource() == btnExit)
        {
            if (isSaved())
                mainWindow.showMenu();
        }
    }
    
    private boolean isSaved() {
        if (saved)
            return true;
        
        return JOptionPane.showConfirmDialog(this,
                "Il y a des modifications non sauvegardées, êtes-vous sûr de vouloir continuer ?",
                "Modifications non sauvegardées",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION;
    }
    
    private void openFile() {
        if (!isSaved())
            return;
        
        int ret = fileChooser.showOpenDialog(this);
        if (ret == JFileChooser.APPROVE_OPTION) {
            try {
                isLoading = true;
                map.loadMap(new String(Files.readAllBytes(fileChooser.getSelectedFile().toPath())));
                columnCount.setValue(map.getColumnCount());
                rowCount.setValue(map.getRowCount());
                updateMap();
                mainWindow.setToPreferredSize();
            } catch (InputMismatchException | IOException e) {
                JOptionPane.showMessageDialog(this,
                        "Impossible de lire le fichier demandé !",
                        "Erreur lors de la lecture",
                        JOptionPane.ERROR_MESSAGE);
            } finally {
                isLoading = false;
            }
        }
    }
    
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

    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == columnCount || e.getSource() == rowCount) {
            if (!isLoading) {
                map.setsize((int) columnCount.getValue(), (int) rowCount.getValue());
                updateMap();
                saved = false;
            }
        }
        else if (e.getSource() == tileSize) {
            map.setTileSize((int) tileSize.getValue());
            updateMap();
        }
    }
}
