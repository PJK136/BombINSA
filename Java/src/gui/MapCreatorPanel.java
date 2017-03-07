package gui;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.InputMismatchException;

import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.sun.java.swing.plaf.windows.WindowsBorders.ToolBarBorder;

import game.Map;
import game.TileType;

public class MapCreatorPanel extends JPanel implements MouseListener, MouseMotionListener, ActionListener {
    private MainWindow mainWindow;
    private GameViewer gameViewer;
    private Map map;
    
    private JButton btnOpen;
    private JButton btnSave;
    private JFileChooser fileChooser;
    
    private ButtonGroup tileTypeGroup;
    
    /**
     * Create the panel.
     */
    public MapCreatorPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        setLayout(new BorderLayout(0, 0));
        
        JToolBar toolBar = new JToolBar();
        add(toolBar, BorderLayout.NORTH);

        btnOpen = new JButton("📂");
        btnOpen.addActionListener(this);
        toolBar.add(btnOpen);
        
        btnSave = new JButton("💾");
        btnSave.addActionListener(this);
        toolBar.add(btnSave);
        
        fileChooser = new JFileChooser(".");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Cartes de BombINSA", "map"));
        
        toolBar.addSeparator();
        
        tileTypeGroup = new ButtonGroup();
        for (TileType type : TileType.values()) {
            JToggleButton button = new JToggleButton(type.toString()); //TODO : Remplacer par image
            button.setActionCommand(type.name());
            tileTypeGroup.add(button);
            toolBar.add(button);
        }
        
        map = new Map(20, 15, 32);
        
        gameViewer = new GameViewer();
        gameViewer.addMouseListener(this);
        gameViewer.addMouseMotionListener(this);
        add(gameViewer, BorderLayout.CENTER);
        
        updateMap();
    }

    private void updateMap() {
        gameViewer.drawMap(map);
    }
    
    private void placeTile(MouseEvent e) {
        if (tileTypeGroup.getSelection() != null && map.isInsideMap(e.getX(), e.getY())) {
            TileType type = TileType.valueOf(tileTypeGroup.getSelection().getActionCommand());
            if (map.getTileType(e.getX(), e.getY()) != type) {
                map.setTileType(type, e.getX(), e.getY());
                updateMap();
            }
        }
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub
        
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
        if (SwingUtilities.isLeftMouseButton(e)) {
            placeTile(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            placeTile(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnOpen)
            openFile();
        else if (e.getSource() == btnSave)
            saveToFile();
        //else if (e.getSource() == btnSave)      
    }
    
    private void openFile() {
        int ret = fileChooser.showOpenDialog(this);
        if (ret == JFileChooser.APPROVE_OPTION) {
            try {
                map.loadMap(new String(Files.readAllBytes(fileChooser.getSelectedFile().toPath())));
                updateMap();
                mainWindow.setToPreferredSize();
            } catch (InputMismatchException | IOException e) {
                // TODO Afficher un message d'erreur
                e.printStackTrace();
            }
        }
    }
    
    private void saveToFile() {
        int ret = fileChooser.showSaveDialog(this);
        if (ret == JFileChooser.APPROVE_OPTION) {
            try {
                PrintWriter printWriter = new PrintWriter(fileChooser.getSelectedFile());
                printWriter.write(map.saveMap());
                printWriter.close();
            } catch (FileNotFoundException e) {
             // TODO Afficher un message d'erreur
                e.printStackTrace();
            }
        }
    }
}
