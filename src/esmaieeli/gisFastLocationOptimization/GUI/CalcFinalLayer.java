/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package esmaieeli.gisFastLocationOptimization.GUI;

import esmaieeli.gisFastLocationOptimization.GIS3D.AllData;
import esmaieeli.gisFastLocationOptimization.GIS3D.LayerDefinition;
import esmaieeli.gisFastLocationOptimization.GIS3D.NumericLayer;
import esmaieeli.gisFastLocationOptimization.GIS3D.ReportResults;
import esmaieeli.gisFastLocationOptimization.GIS3D.Viewing;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeCanvasContext;
import java.awt.Canvas;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Callable;

/**
 *
 * @author Amir72c
 */
public class CalcFinalLayer extends javax.swing.JDialog {

    MainFramePanel myParent;
    String layerNames[];
    String mainLayersDirections[];
    String subLayersDirections[];
    Viewing parentApp;
    AppSettings settings;
    Canvas canvas;
    double maxValue;
    
    ReportResults localReport;

    /**
     * Creates new form CalcFinalLayer
     */
    public CalcFinalLayer(java.awt.Window window, MainFramePanel parent, ModalityType modal) {
        super(window, modal);
        initComponents();
        myParent = parent;
        layerNames = new String[myParent.allData.all_Layers.size()];
        mainLayersDirections = new String[layerNames.length];
        subLayersDirections = new String[layerNames.length];
        for (int i = 0; i < myParent.allData.all_Layers.size(); i++) {
            layerNames[i] = ((LayerDefinition) myParent.allData.all_Layers.get(i)).layerName;
            mainLayersDirections[i] = "Direct";
            subLayersDirections[i] = "Direct";
        }
        mainLayersList.setModel(new javax.swing.AbstractListModel() {
            @Override
            public int getSize() {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                return layerNames.length;
            }

            @Override
            public Object getElementAt(int index) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                return layerNames[index];
            }
        });
        mainLayerDirectionList.setModel(new javax.swing.AbstractListModel() {
            @Override
            public int getSize() {
                return mainLayersDirections.length;
            }

            @Override
            public Object getElementAt(int index) {
                for (int i = 0; i < mainLayersDirections.length; i++) {
                    if (mainLayerDirectionList.isSelectedIndex(i)) {
                        mainLayersDirections[i] = "Inverse";
                    } else {
                        mainLayersDirections[i] = "Direct";
                    }
                }
                return mainLayersDirections[index];
            }
        });
        subLayersList.setModel(new javax.swing.AbstractListModel() {
            @Override
            public int getSize() {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                return layerNames.length;
            }

            @Override
            public Object getElementAt(int index) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                return layerNames[index];
            }
        });
        
        subLayerDirectionList.setModel(new javax.swing.AbstractListModel() {
            @Override
            public int getSize() {
                return subLayersDirections.length;
            }

            @Override
            public Object getElementAt(int index) {
                for (int i = 0; i < subLayersDirections.length; i++) {
                    if (subLayerDirectionList.isSelectedIndex(i)) {
                        subLayersDirections[i] = "Inverse";
                    } else {
                        subLayersDirections[i] = "Direct";
                    }
                }
                return subLayersDirections[index];
            }
        });
        
        parentApp = new Viewing(myParent);

        settings = new AppSettings(true);
        settings.setWidth(jPanel1.getWidth());
        settings.setHeight(jPanel1.getHeight());
        settings.setFrameRate(myParent.capFrameRate);

        parentApp.setSettings(settings);

        parentApp.createCanvas();
        parentApp.startCanvas();

        JmeCanvasContext context = (JmeCanvasContext) parentApp.getContext();
        canvas = context.getCanvas();
        canvas.setSize(settings.getWidth(), settings.getHeight());
        jPanel1.add(canvas);

        parentApp.mySettings = settings;
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        mainLayersList = new javax.swing.JList();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        subLayersList = new javax.swing.JList();
        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jCheckBox1 = new javax.swing.JCheckBox();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        mainLayerDirectionList = new javax.swing.JList<>();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        subLayerDirectionList = new javax.swing.JList<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Main layers:      ");

        mainLayersList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(mainLayersList);

        jLabel2.setText("Sub Layers:      ");

        subLayersList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(subLayersList);

        jButton1.setText("Calculate");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                jPanel1ComponentResized(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jButton2.setText("Confirm");
        jButton2.setEnabled(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jCheckBox1.setSelected(true);
        jCheckBox1.setText("OverWrite");

        jLabel3.setText("Direction:     ");

        mainLayerDirectionList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane3.setViewportView(mainLayerDirectionList);

        jLabel4.setText("Direction:    ");

        subLayerDirectionList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane4.setViewportView(subLayerDirectionList);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 289, Short.MAX_VALUE)
                        .addComponent(jCheckBox1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton1)
                            .addComponent(jButton2)
                            .addComponent(jCheckBox1)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 472, Short.MAX_VALUE)
                            .addComponent(jScrollPane2)
                            .addComponent(jScrollPane3)
                            .addComponent(jScrollPane4))))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        //REPORTING
        String detailedResults="Calculating mixed layer from several layers to feed into clustering algorithm.";
        Calendar currentDate=Calendar.getInstance();
        Date date=currentDate.getTime();
        double startRAM=(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/1024.0;
        long startTime=System.nanoTime();
        //REPORTING
        
        myParent.allData.setParallelLayers(1,-1);
        int mainLayers[] = mainLayersList.getSelectedIndices();
        int subLayers[] = subLayersList.getSelectedIndices();
        maxValue=0;
        for (int i = 0; i < myParent.allData.all_Nodes.length; i++) {
            double subValue=0;
            for (int j = 0; j < subLayers.length; j++) {
//                System.out.println("sub index: "+subLayers[j]);
                if (myParent.allData.all_Nodes[i].layers.get(subLayers[j]) instanceof short[]) {
                    if (subLayerDirectionList.isSelectedIndex(subLayers[j])) {
                        double maxValue = 0;
                        for (int k = 0; k < ((LayerDefinition) myParent.allData.all_Layers.get(subLayers[j])).values.length; k++) {
                            if (((LayerDefinition) myParent.allData.all_Layers.get(subLayers[j])).values[k] > maxValue) {
                                maxValue = ((LayerDefinition) myParent.allData.all_Layers.get(subLayers[j])).values[k];
                            }
                        }
//                        System.out.println("maxValue: "+maxValue);
                        subValue = subValue + maxValue - ((LayerDefinition) myParent.allData.all_Layers.get(subLayers[j])).values[((short[]) myParent.allData.all_Nodes[i].layers.get(subLayers[j]))[0] - 1];
                    } else {
                        subValue = subValue + ((LayerDefinition) myParent.allData.all_Layers.get(subLayers[j])).values[((short[]) myParent.allData.all_Nodes[i].layers.get(subLayers[j]))[0] - 1];
                    }
                } else if (myParent.allData.all_Nodes[i].layers.get(subLayers[j]) instanceof Double) {
                    if (subLayerDirectionList.isSelectedIndex(subLayers[j])) {
                        double maxValue = 0;
//                        for (int k = 0; k < ((LayerDefinition) myParent.allData.all_Layers.get(subLayers[j])).values.length; k++) {
                        if (((LayerDefinition) myParent.allData.all_Layers.get(subLayers[j])).maxValue > maxValue) {
                            maxValue = ((LayerDefinition) myParent.allData.all_Layers.get(subLayers[j])).maxValue;
                        }
//                        }
//                        System.out.println("maxValue: "+maxValue);
                        if (((double) myParent.allData.all_Nodes[i].layers.get(subLayers[j])) > 0) {
                            subValue = subValue + maxValue - ((double) myParent.allData.all_Nodes[i].layers.get(subLayers[j]));
                        }
                    } else {
                        if (((double) myParent.allData.all_Nodes[i].layers.get(subLayers[j])) > 0) {
                            subValue = subValue + ((double) myParent.allData.all_Nodes[i].layers.get(subLayers[j]));
                        }
                    }
                }
            }
            subValue=(double)subValue/(double)subLayers.length;
//            System.out.println("sub: "+subValue);
            double mainValue=1;
            for (int k = 0; k < mainLayers.length; k++) {
//                System.out.println("main index: "+mainLayers[k]);
                if (myParent.allData.all_Nodes[i].layers.get(mainLayers[k]) instanceof short[]) {
                    if (subLayerDirectionList.isSelectedIndex(mainLayers[k])) {
                        double maxValue = 0;
                        for (int l = 0; l < ((LayerDefinition) myParent.allData.all_Layers.get(mainLayers[k])).values.length; l++) {
                            if (((LayerDefinition) myParent.allData.all_Layers.get(mainLayers[k])).values[k] > maxValue) {
                                maxValue = ((LayerDefinition) myParent.allData.all_Layers.get(mainLayers[k])).values[k];
                            }
                        }
                        mainValue = mainValue * (maxValue - ((LayerDefinition) myParent.allData.all_Layers.get(mainLayers[k])).values[((short[]) myParent.allData.all_Nodes[i].layers.get(mainLayers[k]))[0] - 1]);
                    } else {
                        mainValue = mainValue * ((LayerDefinition) myParent.allData.all_Layers.get(mainLayers[k])).values[((short[]) myParent.allData.all_Nodes[i].layers.get(mainLayers[k]))[0] - 1];
                    }
                } else if (myParent.allData.all_Nodes[i].layers.get(mainLayers[k]) instanceof Double) {
                    if (subLayerDirectionList.isSelectedIndex(mainLayers[k])) {
                        double maxValue = 0;
                        if (((LayerDefinition) myParent.allData.all_Layers.get(mainLayers[k])).maxValue > maxValue) {
                            maxValue = ((LayerDefinition) myParent.allData.all_Layers.get(mainLayers[k])).maxValue;
                        }
                        if(((double) myParent.allData.all_Nodes[i].layers.get(mainLayers[k]))>0)
                        {
                            mainValue = mainValue * (maxValue - ((double) myParent.allData.all_Nodes[i].layers.get(mainLayers[k])));
                        }
                    } else {
                        if(((double) myParent.allData.all_Nodes[i].layers.get(mainLayers[k]))>0)
                        {
                            mainValue = mainValue * ((double) myParent.allData.all_Nodes[i].layers.get(mainLayers[k]));
                        }
                    }
                }
            }
//            System.out.println("main: "+subValue);
            mainValue=mainValue*subValue;
//            System.out.println("mixed: "+subValue);
            if(maxValue<mainValue)
            {
                maxValue=mainValue;
            }
        }
        
        for (int i = 0; i < myParent.allData.all_Nodes.length; i++) {
            double subValue=0;
            for (int j = 0; j < subLayers.length; j++) {
//                System.out.println("sub index: "+subLayers[j]);
                if (myParent.allData.all_Nodes[i].layers.get(subLayers[j]) instanceof short[]) {
                    if (subLayerDirectionList.isSelectedIndex(subLayers[j])) {
                        double maxValue = 0;
                        for (int k = 0; k < ((LayerDefinition) myParent.allData.all_Layers.get(subLayers[j])).values.length; k++) {
                            if (((LayerDefinition) myParent.allData.all_Layers.get(subLayers[j])).values[k] > maxValue) {
                                maxValue = ((LayerDefinition) myParent.allData.all_Layers.get(subLayers[j])).values[k];
                            }
                        }
//                        System.out.println("maxValue: "+maxValue);
                        subValue = subValue + maxValue - ((LayerDefinition) myParent.allData.all_Layers.get(subLayers[j])).values[((short[]) myParent.allData.all_Nodes[i].layers.get(subLayers[j]))[0] - 1];
                    } else {
                        subValue = subValue + ((LayerDefinition) myParent.allData.all_Layers.get(subLayers[j])).values[((short[]) myParent.allData.all_Nodes[i].layers.get(subLayers[j]))[0] - 1];
                    }
                } else if (myParent.allData.all_Nodes[i].layers.get(subLayers[j]) instanceof Double) {
                    if (subLayerDirectionList.isSelectedIndex(subLayers[j])) {
                        double maxValue = 0;
                        if (((LayerDefinition) myParent.allData.all_Layers.get(subLayers[j])).maxValue > maxValue) {
                            maxValue = ((LayerDefinition) myParent.allData.all_Layers.get(subLayers[j])).maxValue;
                        }
//                        System.out.println("maxValue: "+maxValue);
                        if(((double) myParent.allData.all_Nodes[i].layers.get(subLayers[j]))>0)
                        {
                            subValue = subValue + maxValue - ((double) myParent.allData.all_Nodes[i].layers.get(subLayers[j]));
                        }
                    } else {
                        if(((double) myParent.allData.all_Nodes[i].layers.get(subLayers[j]))>0)
                        {
                            subValue = subValue + ((double) myParent.allData.all_Nodes[i].layers.get(subLayers[j]));
                        }
                    }
                }
            }
            subValue=(double)subValue/(double)subLayers.length;
//            System.out.println("sub: "+subValue);
            double mainValue=1;
            for (int k = 0; k < mainLayers.length; k++) {
//                System.out.println("main index: "+mainLayers[k]);
                if (myParent.allData.all_Nodes[i].layers.get(mainLayers[k]) instanceof short[]) {
                    if (subLayerDirectionList.isSelectedIndex(mainLayers[k])) {
                        double maxValue = 0;
                        for (int l = 0; l < ((LayerDefinition) myParent.allData.all_Layers.get(mainLayers[k])).values.length; l++) {
                            if (((LayerDefinition) myParent.allData.all_Layers.get(mainLayers[k])).values[k] > maxValue) {
                                maxValue = ((LayerDefinition) myParent.allData.all_Layers.get(mainLayers[k])).values[k];
                            }
                        }
                        mainValue = mainValue * (maxValue - ((LayerDefinition) myParent.allData.all_Layers.get(mainLayers[k])).values[((short[]) myParent.allData.all_Nodes[i].layers.get(mainLayers[k]))[0] - 1]);
                    } else {
                        mainValue = mainValue * ((LayerDefinition) myParent.allData.all_Layers.get(mainLayers[k])).values[((short[]) myParent.allData.all_Nodes[i].layers.get(mainLayers[k]))[0] - 1];
                    }
                } else if (myParent.allData.all_Nodes[i].layers.get(mainLayers[k]) instanceof Double) {
                    if (subLayerDirectionList.isSelectedIndex(mainLayers[k])) {
                        double maxValue = 0;
                        if (((LayerDefinition) myParent.allData.all_Layers.get(mainLayers[k])).maxValue > maxValue) {
                            maxValue = ((LayerDefinition) myParent.allData.all_Layers.get(mainLayers[k])).maxValue;
                        }
                        if(((double) myParent.allData.all_Nodes[i].layers.get(mainLayers[k]))>0)
                        {
                            mainValue = mainValue * (maxValue - ((double) myParent.allData.all_Nodes[i].layers.get(mainLayers[k])));
                        }
                    } else {
                        if(((double) myParent.allData.all_Nodes[i].layers.get(mainLayers[k]))>0)
                        {
                            mainValue = mainValue * ((double) myParent.allData.all_Nodes[i].layers.get(mainLayers[k]));
                        }
                    }
                }
            }
            mainValue=mainValue*subValue;
//            System.out.println("main: "+subValue);
//            System.out.println("raw"+mainValue);
            mainValue=mainValue/maxValue;
//            System.out.println("final: "+subValue);
            myParent.allData.all_Nodes[i].lava_value_indexed[0]=mainValue;
        }
        
        //REPORTING
        long endTime = System.nanoTime();
        double elapsed=((endTime - startTime) / 1000000000);
        double endRAM=(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/1024.0;
        double usedRAM=endRAM-startRAM;
        localReport=new ReportResults(date,"Calcute final Layer",startTime,endTime,elapsed,1,startRAM,endRAM,usedRAM,detailedResults);
        myParent.allData.results.add(localReport);
        myParent.refreshReportList();
        //REPORTING
        
        jButton2.setEnabled(true);
        setWaysColor(myParent.allData);
        parentApp.enqueue(new Callable() {
            public Object call() throws Exception {
                parentApp.isRefreshing = true;
                return null;
            }
        });
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        int mainLayers[] = mainLayersList.getSelectedIndices();
        int subLayers[] = subLayersList.getSelectedIndices();
        myParent.allData.all_Layers.add(new NumericLayer("AllMixed"));
        for (int i = 0; i < myParent.allData.all_Nodes.length; i++) {
            double subValue=0;
            for (int j = 0; j < subLayers.length; j++) {
//                System.out.println("sub index: "+subLayers[j]);
                if (myParent.allData.all_Nodes[i].layers.get(subLayers[j]) instanceof short[]) {
                    if (subLayerDirectionList.isSelectedIndex(subLayers[j])) {
                        double maxValue = 0;
                        for (int k = 0; k < ((LayerDefinition) myParent.allData.all_Layers.get(subLayers[j])).values.length; k++) {
                            if (((LayerDefinition) myParent.allData.all_Layers.get(subLayers[j])).values[k] > maxValue) {
                                maxValue = ((LayerDefinition) myParent.allData.all_Layers.get(subLayers[j])).values[k];
                            }
                        }
//                        System.out.println("maxValue: "+maxValue);
                        subValue = subValue + maxValue - ((LayerDefinition) myParent.allData.all_Layers.get(subLayers[j])).values[((short[]) myParent.allData.all_Nodes[i].layers.get(subLayers[j]))[0] - 1];
                    } else {
                        subValue = subValue + ((LayerDefinition) myParent.allData.all_Layers.get(subLayers[j])).values[((short[]) myParent.allData.all_Nodes[i].layers.get(subLayers[j]))[0] - 1];
                    }
                } else if (myParent.allData.all_Nodes[i].layers.get(subLayers[j]) instanceof Double) {
                    if (subLayerDirectionList.isSelectedIndex(subLayers[j])) {
                        double maxValue = 0;
                        if (((LayerDefinition) myParent.allData.all_Layers.get(subLayers[j])).maxValue > maxValue) {
                            maxValue = ((LayerDefinition) myParent.allData.all_Layers.get(subLayers[j])).maxValue;
                        }
//                        System.out.println("maxValue: "+maxValue);
                        if(((double) myParent.allData.all_Nodes[i].layers.get(subLayers[j]))>0)
                        {
                            subValue = subValue + maxValue - ((double) myParent.allData.all_Nodes[i].layers.get(subLayers[j]));
                        }
                    } else {
                        if(((double) myParent.allData.all_Nodes[i].layers.get(subLayers[j]))>0)
                        {
                            subValue = subValue + ((double) myParent.allData.all_Nodes[i].layers.get(subLayers[j]));
                        }
                    }
                }
            }
            subValue=(double)subValue/(double)subLayers.length;
//            System.out.println("sub: "+subValue);
            double mainValue=1;
            for (int k = 0; k < mainLayers.length; k++) {
//                System.out.println("main index: "+mainLayers[k]);
                if (myParent.allData.all_Nodes[i].layers.get(mainLayers[k]) instanceof short[]) {
                    if (subLayerDirectionList.isSelectedIndex(mainLayers[k])) {
                        double maxValue = 0;
                        for (int l = 0; l < ((LayerDefinition) myParent.allData.all_Layers.get(mainLayers[k])).values.length; l++) {
                            if (((LayerDefinition) myParent.allData.all_Layers.get(mainLayers[k])).values[k] > maxValue) {
                                maxValue = ((LayerDefinition) myParent.allData.all_Layers.get(mainLayers[k])).values[k];
                            }
                        }
                        mainValue = mainValue * (maxValue - ((LayerDefinition) myParent.allData.all_Layers.get(mainLayers[k])).values[((short[]) myParent.allData.all_Nodes[i].layers.get(mainLayers[k]))[0] - 1]);
                    } else {
                        mainValue = mainValue * ((LayerDefinition) myParent.allData.all_Layers.get(mainLayers[k])).values[((short[]) myParent.allData.all_Nodes[i].layers.get(mainLayers[k]))[0] - 1];
                    }
                } else if (myParent.allData.all_Nodes[i].layers.get(mainLayers[k]) instanceof Double) {
                    if (subLayerDirectionList.isSelectedIndex(mainLayers[k])) {
                        double maxValue = 0;
                        if (((LayerDefinition) myParent.allData.all_Layers.get(mainLayers[k])).maxValue > maxValue) {
                            maxValue = ((LayerDefinition) myParent.allData.all_Layers.get(mainLayers[k])).maxValue;
                        }
                        if(((double) myParent.allData.all_Nodes[i].layers.get(mainLayers[k]))>0)
                        {
                            mainValue = mainValue * (maxValue - ((double) myParent.allData.all_Nodes[i].layers.get(mainLayers[k])));
                        }
                    } else {
                        if(((double) myParent.allData.all_Nodes[i].layers.get(mainLayers[k]))>0)
                        {
                            mainValue = mainValue * ((double) myParent.allData.all_Nodes[i].layers.get(mainLayers[k]));
                        }
                    }
                }
            }
            mainValue=mainValue*subValue;
//            System.out.println("main: "+subValue);
//            System.out.println("raw"+mainValue);
            mainValue=mainValue/maxValue;
//            System.out.println("final: "+subValue);
            myParent.allData.all_Nodes[i].layers.add(mainValue);
        }
        parentApp.stop();
        myParent.refreshLayersList();
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jPanel1ComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jPanel1ComponentResized
        // TODO add your handling code here:
        if (this.isVisible()) {
            settings.setWidth(jPanel1.getWidth());
            settings.setHeight(jPanel1.getHeight());
            canvas.setSize(settings.getWidth(), settings.getHeight());
        }
    }//GEN-LAST:event_jPanel1ComponentResized

    public void setWaysColor(AllData allData) {
        for (int i = 0; i < allData.all_Ways.length; i++) {
            allData.all_Ways[i].color = new float[3 * allData.all_Ways[i].myNodes.length];

            for (int j = 0; j < allData.all_Ways[i].myNodes.length; j++) {
                allData.all_Ways[i].color[j * 3 + 0] = (float)allData.all_Ways[i].myNodes[j].lava_value_indexed[0];
                allData.all_Ways[i].color[j * 3 + 1] = 0f;
                allData.all_Ways[i].color[j * 3 + 2] = (float)(1-allData.all_Ways[i].myNodes[j].lava_value_indexed[0]);
            }
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JList<String> mainLayerDirectionList;
    private javax.swing.JList mainLayersList;
    private javax.swing.JList<String> subLayerDirectionList;
    private javax.swing.JList subLayersList;
    // End of variables declaration//GEN-END:variables
}
