/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import GIS3D.ImageLayer;
import GIS3D.LayerDefinition;
import GIS3D.LocationNode;
import GIS3D.NumericLayer;
import GIS3D.ReportResults;
import GIS3D.Viewing;
import Simulation.FacilityLocation;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeCanvasContext;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Callable;
import javax.swing.ImageIcon;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Amir72c
 */
public class NumericController extends javax.swing.JDialog {

    main_frame myParent;
    Viewing parentApp;
    String[] layers; 
    AppSettings settings;
    Canvas canvas;
    ArrayList locationDefinitions = new ArrayList();
    public LocationNode passedNode;
    public int passedOrder = -1;
    ArrayList<FacilityLocation> validFacilityList;
    AutoFolderReader autoFolderReader;
    boolean isEdit = false;
    int editIndex = -1;
    
    ReportResults localReport;

    /**
     * Creates new form NumericController
     */
    public NumericController(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        myParent = (main_frame) parent;
        numProcessor.setModel(new javax.swing.SpinnerNumberModel(1, 1, Runtime.getRuntime().availableProcessors(), 1));
        final CustomjSlider customjSlider = new CustomjSlider();
        customjSlider.setMaximum(360);
        customjSlider.setBounds(1, 1, 360, 15);
        customjSlider.setValue(0);
        customjSlider.setVisible(true);
        jPanel1.setBackground(Color.getHSBColor((float) customjSlider.getValue() / 360f, 1, 1));
        customjSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                jPanel1.setBackground(Color.getHSBColor((float) customjSlider.getValue() / 360f, 1, 1));
            }
        });
        jPanel2.add(customjSlider);

        final CustomjSlider customjSlider1 = new CustomjSlider();
        customjSlider1.setMaximum(360);
        customjSlider1.setBounds(1, 1, 360, 15);
        customjSlider1.setValue(239);
        customjSlider1.setVisible(true);
        jPanel3.setBackground(Color.getHSBColor((float) customjSlider1.getValue() / 360f, 1, 1));
        customjSlider1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                jPanel3.setBackground(Color.getHSBColor((float) customjSlider1.getValue() / 360f, 1, 1));
            }
        });
        jPanel4.add(customjSlider1);

        layers = new String[myParent.allData.all_Layers.size()];
        for (int i = 0; i < layers.length; i++) {
            //System.out.println(((LayerDefinition) mainFrame.allData.all_Layers.get(i)).layerName);
            layers[i] = ((LayerDefinition) myParent.allData.all_Layers.get(i)).layerName;
        }

        jList1.setModel(new javax.swing.AbstractListModel() {
            @Override
            public int getSize() {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                return layers.length;
            }

            @Override
            public Object getElementAt(int index) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                return layers[index];
            }
        });

        parentApp = new Viewing((main_frame) parent);
//        myParent.preProcessor.setWaysColor(myParent.allData, 1);
        settings = new AppSettings(true);
        settings.setWidth(viewPanel.getWidth());
        settings.setHeight(viewPanel.getHeight());
        settings.setFrameRate(myParent.capFrameRate);

        parentApp.setSettings(settings);

        parentApp.createCanvas();
        parentApp.startCanvas();

        JmeCanvasContext context = (JmeCanvasContext) parentApp.getContext();
        canvas = context.getCanvas();
        canvas.setSize(settings.getWidth(), settings.getHeight());
        viewPanel.add(canvas);

        parentApp.mySettings = settings;
    }

    public class CustomjSlider extends JSlider {

        private Image img = null;

        public CustomjSlider() {
            ImageIcon temp = new javax.swing.ImageIcon(getClass().getResource("/GUI/hue1.jpg"));
            img = temp.getImage();
        }

        @Override
        public void paintComponent(Graphics g) {
            // Draw the previously loaded image to Component
            g.drawImage(img, 0, 0, null);
            super.paintComponent(g);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        simulate = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jSplitPane1 = new javax.swing.JSplitPane();
        jSplitPane2 = new javax.swing.JSplitPane();
        layersPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        categoriesPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        legendPanel = new javax.swing.JPanel();
        jSplitPane3 = new javax.swing.JSplitPane();
        inputPanel = new javax.swing.JPanel();
        addFacilityButton = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        locationsPanel = new javax.swing.JPanel();
        viewPanel = new javax.swing.JPanel();
        confirm = new javax.swing.JButton();
        numProcessor = new javax.swing.JSpinner();
        jLabel5 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        simulate.setText("Simulate");
        simulate.setEnabled(false);
        simulate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                simulateActionPerformed(evt);
            }
        });

        jButton2.setText("Cancel");

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 61, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 365, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 61, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 365, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 34, Short.MAX_VALUE)
        );

        jLabel1.setText("High value color:");

        jLabel2.setText("Low value color:");

        jLabel4.setText("Layer name:");

        jTextField1.setText("Numeric layer");

        jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane2.setPreferredSize(new java.awt.Dimension(100, 243));

        layersPanel.setMinimumSize(new java.awt.Dimension(40, 20));
        layersPanel.setPreferredSize(new java.awt.Dimension(95, 100));

        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jList1.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList1ValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jList1);

        javax.swing.GroupLayout layersPanelLayout = new javax.swing.GroupLayout(layersPanel);
        layersPanel.setLayout(layersPanelLayout);
        layersPanelLayout.setHorizontalGroup(
            layersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
        );
        layersPanelLayout.setVerticalGroup(
            layersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
        );

        jSplitPane2.setTopComponent(layersPanel);

        categoriesPanel.setMinimumSize(new java.awt.Dimension(40, 20));
        categoriesPanel.setPreferredSize(new java.awt.Dimension(95, 136));

        javax.swing.GroupLayout legendPanelLayout = new javax.swing.GroupLayout(legendPanel);
        legendPanel.setLayout(legendPanelLayout);
        legendPanelLayout.setHorizontalGroup(
            legendPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        legendPanelLayout.setVerticalGroup(
            legendPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 257, Short.MAX_VALUE)
        );

        jScrollPane2.setViewportView(legendPanel);

        javax.swing.GroupLayout categoriesPanelLayout = new javax.swing.GroupLayout(categoriesPanel);
        categoriesPanel.setLayout(categoriesPanelLayout);
        categoriesPanelLayout.setHorizontalGroup(
            categoriesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
        );
        categoriesPanelLayout.setVerticalGroup(
            categoriesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
        );

        jSplitPane2.setRightComponent(categoriesPanel);

        jSplitPane1.setLeftComponent(jSplitPane2);

        jSplitPane3.setPreferredSize(new java.awt.Dimension(500, 243));

        inputPanel.setMinimumSize(new java.awt.Dimension(40, 20));
        inputPanel.setPreferredSize(new java.awt.Dimension(210, 241));

        addFacilityButton.setText("Add facility");
        addFacilityButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addFacilityButtonActionPerformed(evt);
            }
        });

        locationsPanel.setMinimumSize(new java.awt.Dimension(100, 100));
        locationsPanel.setPreferredSize(new java.awt.Dimension(200, 240));

        javax.swing.GroupLayout locationsPanelLayout = new javax.swing.GroupLayout(locationsPanel);
        locationsPanel.setLayout(locationsPanelLayout);
        locationsPanelLayout.setHorizontalGroup(
            locationsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 208, Short.MAX_VALUE)
        );
        locationsPanelLayout.setVerticalGroup(
            locationsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 339, Short.MAX_VALUE)
        );

        jScrollPane3.setViewportView(locationsPanel);

        javax.swing.GroupLayout inputPanelLayout = new javax.swing.GroupLayout(inputPanel);
        inputPanel.setLayout(inputPanelLayout);
        inputPanelLayout.setHorizontalGroup(
            inputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(addFacilityButton)
                .addContainerGap(45, Short.MAX_VALUE))
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE)
        );
        inputPanelLayout.setVerticalGroup(
            inputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, inputPanelLayout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 341, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addFacilityButton)
                .addContainerGap())
        );

        jSplitPane3.setLeftComponent(inputPanel);

        viewPanel.setMinimumSize(new java.awt.Dimension(20, 20));
        viewPanel.setPreferredSize(new java.awt.Dimension(300, 241));
        viewPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                viewPanelComponentResized(evt);
            }
        });

        javax.swing.GroupLayout viewPanelLayout = new javax.swing.GroupLayout(viewPanel);
        viewPanel.setLayout(viewPanelLayout);
        viewPanelLayout.setHorizontalGroup(
            viewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 744, Short.MAX_VALUE)
        );
        viewPanelLayout.setVerticalGroup(
            viewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 381, Short.MAX_VALUE)
        );

        jSplitPane3.setRightComponent(viewPanel);

        jSplitPane1.setRightComponent(jSplitPane3);

        confirm.setText("Confirm");
        confirm.setEnabled(false);
        confirm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmActionPerformed(evt);
            }
        });

        numProcessor.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));

        jLabel5.setText("Number of processors:");

        jButton1.setText("Auto folder reader");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1068, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(numProcessor, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(simulate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(confirm))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 385, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(simulate)
                    .addComponent(jButton2)
                    .addComponent(confirm)
                    .addComponent(numProcessor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jButton1))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void simulateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_simulateActionPerformed
        // TODO add your handling code here:
        
        //REPORTING
        String detailedResults = "Adding a numeric layer by simulating area catchment."+System.lineSeparator();
        detailedResults=detailedResults+"Generated layer name: "+jTextField1.getText()+System.lineSeparator();
        Calendar currentDate = Calendar.getInstance();
        Date date = currentDate.getTime();
        double startRAM = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024.0;
        long startTime = System.nanoTime();
        //REPORTING
//        System.out.println("Starting");
//        myParent.flowControl.resetDeepOneLayer();
//        System.out.println("Set colors");
        myParent.preProcessor.setWaysColorLayerBased(myParent.allData, 0);
        parentApp.isRefreshing = true;
//        System.out.println("Check validity");
        checkIDValidity();

        final int numThreads = (int) numProcessor.getValue();
        myParent.flowControl.minLava = Double.POSITIVE_INFINITY;
        myParent.flowControl.maxLava = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < validFacilityList.size(); i++) {
            if (((FacilityLocation) validFacilityList.get(i)).capacity > myParent.flowControl.maxLava) {
                myParent.flowControl.maxLava = ((FacilityLocation) validFacilityList.get(i)).capacity;
            }
        }
        int trafficLayerIndex=myParent.findLayer("traffic");
//        System.out.println("Simulate started");
        FacilityLocation fls[]=new FacilityLocation[validFacilityList.size()];
        for(int i=0;i<fls.length;i++)
        {
            fls[i]=validFacilityList.get(i);
        }
        myParent.flowControl.isDecoyable=false;
        myParent.flowControl.simulateOneLayerCompetingLavaBased(fls,trafficLayerIndex,numThreads,-2,false);
        System.out.println("MAX LAVA: "+myParent.flowControl.maxLava);
        System.out.println("MIN LAVA: "+myParent.flowControl.minLava);

        for (int i = 0; i < myParent.allData.all_Nodes.length; i++) {
            if (myParent.allData.all_Nodes[i].isBurned == true) {
                myParent.allData.all_Nodes[i].lava_value_indexed[0] = Math.pow((double) (myParent.allData.all_Nodes[i].lava_value_indexed[0] - myParent.flowControl.minLava) / (double) (myParent.flowControl.maxLava - myParent.flowControl.minLava), 4);
//                System.out.println(myParent.allData.all_Nodes[i].lava_value_indexed[0]);
            }
        }
        
        //REPORTING
        long endTime = System.nanoTime();
        double elapsed = ((endTime - startTime) / 1000000000);
        double endRAM = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024.0;
        double usedRAM = endRAM - startRAM;
        localReport = new ReportResults(date, "Add numeric layer", startTime, endTime, elapsed, numThreads, startRAM, endRAM, usedRAM, detailedResults);
        myParent.allData.results.add(localReport);
        myParent.refreshReportList();
        //REPORTING
        
        confirm.setEnabled(true);
        for (int i = 0; i < myParent.allData.all_Ways.length; i++) {
            //myParent.allData.all_Ways[i].color = new float[3 * myParent.allData.all_Ways[i].myNodes.length];
            for (int j = 0; j < myParent.allData.all_Ways[i].myNodes.length; j++) {
                if (myParent.allData.all_Ways[i].myNodes[j].isBurned == true) {
                    myParent.allData.all_Ways[i].color[3 * j + 0] = (float) myParent.allData.all_Ways[i].myNodes[j].lava_value_indexed[0];
                    myParent.allData.all_Ways[i].color[3 * j + 1] = 0;
                    myParent.allData.all_Ways[i].color[3 * j + 2] = 1 - (float) myParent.allData.all_Ways[i].myNodes[j].lava_value_indexed[0];
                }
            }
        }
        parentApp.isRefreshing = true;
    }//GEN-LAST:event_simulateActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
        parentApp.stop();
    }//GEN-LAST:event_formWindowClosed

    private void viewPanelComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_viewPanelComponentResized
        // TODO add your handling code here:
        if (this.isVisible()) {
            settings.setWidth(viewPanel.getWidth());
            settings.setHeight(viewPanel.getHeight());
            canvas.setSize(settings.getWidth(), settings.getHeight());
            parentApp.enqueue(new Callable() {
                public Object call() throws Exception {
                    if (parentApp.isPickingNode) {
                        parentApp.resizeCrossHair();
                    }
                    // call methods that modify the scene graph here
                    return null;
                }
            });
        }
    }//GEN-LAST:event_viewPanelComponentResized

    private void jList1ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList1ValueChanged
        // TODO add your handling code here:
        parentApp.enqueue(new Callable() {
            public Object call() throws Exception {
                refreshLegendList(jList1.getSelectedIndex());
                myParent.preProcessor.setWaysColorLayerBased(myParent.allData, jList1.getSelectedIndex());
                parentApp.refreshApp();
                checkIDValidity();
                // call methods that modify the scene graph here
                return null;
            }
        });
    }//GEN-LAST:event_jList1ValueChanged

    private void addFacilityButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addFacilityButtonActionPerformed
        // TODO add your handling code here:
        locationDefinitions.add(new FacilityLocationDefinition(this, locationDefinitions.size()));
        refreshFacilityLocationList();
    }//GEN-LAST:event_addFacilityButtonActionPerformed

    private void confirmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmActionPerformed
        // TODO add your handling code here:
//        myParent.allData.setParallelLayers(1,-1);
        if (isEdit == true) {
            NumericLayer temp = new NumericLayer(jTextField1.getText());
            int size = validFacilityList.size();
            temp.locationIds = new long[size];
            temp.locationNodes = new LocationNode[size];
            temp.capacities = new double[size];
            temp.maxColor = jPanel1.getBackground();
            temp.minColor = jPanel3.getBackground();
            for (int i = 0; i < validFacilityList.size(); i++) {
                temp.locationIds[i] = ((FacilityLocation) validFacilityList.get(i)).nodeLocation.id;
                temp.locationNodes[i] = ((FacilityLocation) validFacilityList.get(i)).nodeLocation;
                temp.capacities[i] = ((FacilityLocation) validFacilityList.get(i)).capacity;
            }
            for (int i = 0; i < myParent.allData.all_Nodes.length; i++) {
                Object[] toArray = myParent.allData.all_Nodes[i].layers.toArray();
                myParent.allData.all_Nodes[i].layers.clear();
                for (int j = 0; j < toArray.length; j++) {
                    if (j == editIndex) {
                        if (myParent.allData.all_Nodes[i].isBurned == true) {
                            myParent.allData.all_Nodes[i].layers.add(myParent.allData.all_Nodes[i].lava_value_indexed[0]);
                        } else {
                            myParent.allData.all_Nodes[i].layers.add(new Double(-1));
                        }
                    } else {
                        if(toArray[j] instanceof short[])
                        {
                            myParent.allData.all_Nodes[i].layers.add((short[])toArray[j]);
                        }else if(toArray[j] instanceof Double)
                        {
                            myParent.allData.all_Nodes[i].layers.add((double)toArray[j]);
                        }
                    }
                }
            }
            Object[] toArray = myParent.allData.all_Layers.toArray();
            myParent.allData.all_Layers.clear();
            for (int i = 0; i < toArray.length; i++) {
                if (i == editIndex) {
                    myParent.allData.all_Layers.add(temp);
                } else {
                    if(toArray[i] instanceof ImageLayer)
                    {
                        myParent.allData.all_Layers.add((ImageLayer)toArray[i]);
                    }else if(toArray[i] instanceof LayerDefinition)
                    {
                        myParent.allData.all_Layers.add((LayerDefinition)toArray[i]);
                    }else if(toArray[i] instanceof NumericLayer)
                    {
                        myParent.allData.all_Layers.add((NumericLayer)toArray[i]);
                    }
                }
            }
            parentApp.stop();
            myParent.refreshLayersList();
            this.dispose();
        } else {
            NumericLayer temp = new NumericLayer(jTextField1.getText());
            int size = validFacilityList.size();
            temp.locationIds = new long[size];
            temp.locationNodes = new LocationNode[size];
            temp.capacities = new double[size];
            temp.maxColor = jPanel1.getBackground();
            temp.minColor = jPanel3.getBackground();
            for (int i = 0; i < validFacilityList.size(); i++) {
                temp.locationIds[i] = ((FacilityLocation) validFacilityList.get(i)).nodeLocation.id;
                temp.locationNodes[i] = ((FacilityLocation) validFacilityList.get(i)).nodeLocation;
                temp.capacities[i] = ((FacilityLocation) validFacilityList.get(i)).capacity;
            }
            for (int i = 0; i < myParent.allData.all_Nodes.length; i++) {
                if (myParent.allData.all_Nodes[i].isBurned == true) {
                    
                    myParent.allData.all_Nodes[i].layers.add(myParent.allData.all_Nodes[i].lava_value_indexed[0]);
                } else {
                    myParent.allData.all_Nodes[i].layers.add(new Double(-1));
                }
            }

            myParent.allData.all_Layers.add(temp);
            parentApp.stop();
            myParent.refreshLayersList();
            this.dispose();
        }

    }//GEN-LAST:event_confirmActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        autoFolderReader = new AutoFolderReader(myParent, true);
        autoFolderReader.setVisible(true);
        for (int i = 0; i < autoFolderReader.data.length; i++) {
            if ((boolean)autoFolderReader.data[i][3] == true) {
                FacilityLocationDefinition temp = new FacilityLocationDefinition(this, locationDefinitions.size());
                temp.IDText.setText(String.valueOf(autoFolderReader.data[i][1]));
                temp.CapacityText.setText(String.valueOf(autoFolderReader.data[i][2]));
                locationDefinitions.add(temp);
            }
        }
        refreshFacilityLocationList();
        this.repaint();
    }//GEN-LAST:event_jButton1ActionPerformed

    public void refreshLegendList(int layerIndex) {
        legendPanel.removeAll();
        if (((LayerDefinition) myParent.allData.all_Layers.get(layerIndex)).myType.equals("base") || ((LayerDefinition) myParent.allData.all_Layers.get(layerIndex)).myType.equals("image")) {
            for (int i = 0; i < ((LayerDefinition) myParent.allData.all_Layers.get(layerIndex)).colors.length; i++) {
                ColorDefinition temp = new ColorDefinition(null, ((LayerDefinition) myParent.allData.all_Layers.get(layerIndex)).colors[i], i);
                temp.valueText.setText(String.valueOf(((LayerDefinition) myParent.allData.all_Layers.get(layerIndex)).values[i]));
                temp.NameText.setText(((LayerDefinition) myParent.allData.all_Layers.get(layerIndex)).categories[i]);
                temp.setBounds(0, i * 55, 243, 55);
                legendPanel.add(temp);
            }
            legendPanel.setPreferredSize(new Dimension(243, (int) ((LayerDefinition) myParent.allData.all_Layers.get(layerIndex)).colors.length * 55));
        } else if (((LayerDefinition) myParent.allData.all_Layers.get(layerIndex)).myType.equals("numeric")) {
            ColorDefinition tempmax = new ColorDefinition(null, ((NumericLayer) myParent.allData.all_Layers.get(layerIndex)).maxColor, 0);
            tempmax.valueText.setText("1");
            tempmax.NameText.setText("Max value");
            tempmax.setBounds(0, 0 * 55, 243, 55);
            legendPanel.add(tempmax);

            ColorDefinition tempmin = new ColorDefinition(null, ((NumericLayer) myParent.allData.all_Layers.get(layerIndex)).minColor, 1);
            tempmin.valueText.setText("0");
            tempmin.NameText.setText("Min value");
            tempmin.setBounds(0, 1 * 55, 243, 55);
            legendPanel.add(tempmin);
            legendPanel.setPreferredSize(new Dimension(243, 2 * 55));
        }
        
        legendPanel.revalidate();
        legendPanel.repaint();
    }

    public void refreshFacilityLocationList() {
        locationsPanel.removeAll();
        for (int i = 0; i < locationDefinitions.size(); i++) {
            FacilityLocationDefinition temp = (FacilityLocationDefinition) locationDefinitions.get(i);
            ((FacilityLocationDefinition) locationDefinitions.get(i)).myOrder = i;
            temp.setBounds(0, i * 90, 190, 90);
            locationsPanel.add(temp);
        }
        locationsPanel.setPreferredSize(new Dimension(190, (int) (locationDefinitions.size()) * 90));
        locationsPanel.revalidate();
        locationsPanel.repaint();
        if (locationDefinitions.size() > 0) {
            simulate.setEnabled(true);
        } else {
            simulate.setEnabled(false);
        }
    }

    public void fillPassedData() {
        ((FacilityLocationDefinition) locationDefinitions.get(passedOrder)).IDText.setText(String.valueOf(passedNode.id));
        passedNode = null;
        passedOrder = -1;
    }

    public void checkIDValidity() {
        validFacilityList = new ArrayList();
        parentApp.removeAllHeadquarters();
        for (int i = 0; i < locationDefinitions.size(); i++) {
            String temp = ((FacilityLocationDefinition) locationDefinitions.get(i)).IDText.getText();
            boolean isBroken = false;
            for (int j = 0; j < myParent.allData.all_Nodes.length; j++) {
                if (String.valueOf(myParent.allData.all_Nodes[j].id).equals(temp)) {
                    isBroken = true;
                    ((FacilityLocationDefinition) locationDefinitions.get(i)).myNode = myParent.allData.all_Nodes[j];
                    final int passedI = i;
                    final int passedJ = j;
                    parentApp.enqueue(new Callable() {
                        public Object call() throws Exception {
                            // call methods that modify the scene graph here
                            parentApp.headquarter(myParent.allData.all_Nodes[passedJ].renderingLocation, Double.parseDouble(((FacilityLocationDefinition) locationDefinitions.get(passedI)).CapacityText.getText()), "center");
                            return null;
                        }
                    });

                    FacilityLocation tempFacility = new FacilityLocation(myParent, ((FacilityLocationDefinition) locationDefinitions.get(i)).myNode, ((FacilityLocationDefinition) locationDefinitions.get(i)).myNode.myWays[0], Double.valueOf(((FacilityLocationDefinition) locationDefinitions.get(i)).CapacityText.getText()));
                    tempFacility.isDecoyable = false;
                    validFacilityList.add(tempFacility);
                    break;
                }
            }
            if (isBroken == false) {
                ((FacilityLocationDefinition) locationDefinitions.get(i)).IDText.setBackground(Color.red);
            } else {

                ((FacilityLocationDefinition) locationDefinitions.get(i)).IDText.setBackground(new Color(240, 240, 240));
            }
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addFacilityButton;
    private javax.swing.JPanel categoriesPanel;
    public javax.swing.JButton confirm;
    private javax.swing.JPanel inputPanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    public javax.swing.JList jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JSplitPane jSplitPane3;
    public javax.swing.JTextField jTextField1;
    private javax.swing.JPanel layersPanel;
    private javax.swing.JPanel legendPanel;
    private javax.swing.JPanel locationsPanel;
    private javax.swing.JSpinner numProcessor;
    private javax.swing.JButton simulate;
    public javax.swing.JPanel viewPanel;
    // End of variables declaration//GEN-END:variables
}
