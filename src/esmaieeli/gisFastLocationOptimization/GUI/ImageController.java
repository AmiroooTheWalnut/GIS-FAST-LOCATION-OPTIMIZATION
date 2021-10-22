/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package esmaieeli.gisFastLocationOptimization.GUI;

import esmaieeli.gisFastLocationOptimization.GIS3D.ImageLayer;
import esmaieeli.gisFastLocationOptimization.GIS3D.ReportResults;
import esmaieeli.gisFastLocationOptimization.GIS3D.Viewing;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeCanvasContext;
import com.jme3.texture.Image;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;
import com.jme3.texture.plugins.AWTLoader;
import com.jme3.util.BufferUtils;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JButton;

/**
 *
 * @author Amir72c
 */
public class ImageController extends javax.swing.JDialog {

    /**
     * Creates new form ImageController
     */
    ImageLayer imageControlled;
    Viewing parentApp;
    public Geometry imageGeo;
    Canvas canvas;
    AppSettings settings;
    ArrayList colorDefinitions = new ArrayList();
    boolean isPickColor = false;
    int pickColorIndex = -1;
    MainFramePanel myParent;
    BufferedImage bImageFromConvert;
    boolean isEditMode = true;
    boolean isEditNeeded = true;
    int editIndex;

    public esmaieeli.gisFastLocationOptimization.GUI.ImagePloter imagePloter1;

    ReportResults localReport;

    private void imagePloter1KeyPressed(java.awt.event.KeyEvent evt) {
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            isPickColor = false;
            setCursor();
        }
    }

    public void myInitComponent() {
        imagePloter1 = new esmaieeli.gisFastLocationOptimization.GUI.ImagePloter();
        imagePloter1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                imagePloter1FocusLost(evt);
            }
        });
        imagePloter1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                imagePloter1KeyPressed(evt);
            }
        });

        jPanel10.add(imagePloter1);
    }

    public ImageController(java.awt.Window window, MainFramePanel parent, ModalityType modal, final ImageLayer imageLayer, byte[] bImage, int isEdit) {
        super(window, modal);
        initComponents();
        myInitComponent();
        layerNameTextField.setText(imageLayer.layerName);
        editIndex = isEdit;
        if (editIndex == -1) {
            isEditMode = false;
        } else {
            isEditMode = true;
        }
        
        System.out.println(bImage.length);
        InputStream in = new ByteArrayInputStream(bImage);
        
        System.out.println(in);

        try {
            bImageFromConvert = ImageIO.read(in);
        } catch (IOException ex) {
            System.out.println("ERROR READING IMAGE FILE!!!");
            Logger.getLogger(ImageController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println(bImageFromConvert);
        
        imageControlled = imageLayer;

        myParent = parent;
        xText.setValue(imageControlled.my_position.x);
        yText.setValue(imageControlled.my_position.y);
        widthText.setValue(imageControlled.my_width);
        heightText.setValue(imageControlled.my_height);
        parentApp = new Viewing(parent);

        settings = new AppSettings(true);
        settings.setFrameRate(myParent.capFrameRate);
        parentApp.setSettings(settings);
        parentApp.mySettings = settings;
        parentApp.createCanvas();
        parentApp.startCanvas();
        JmeCanvasContext context = (JmeCanvasContext) parentApp.getContext();
        canvas = context.getCanvas();
        canvas.setSize(jPanel6.getWidth(), jPanel6.getHeight());
        jPanel6.add(canvas);

        imagePloter1.isfed_outside = true;
        imagePloter1.outside_plot_request_type = "image";
        imagePloter1.img = bImageFromConvert;
        imagePloter1.setVisible(true);
        imagePloter1.repaint();

        imagePloter1.axis.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                isPickColor = false;
                setCursor();
            }

            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                if (pickColorIndex > -1) {
                    ((ColorDefinition) colorDefinitions.get(pickColorIndex)).myColor = imagePloter1.tooltip_data;
                    ((ColorDefinition) colorDefinitions.get(pickColorIndex)).refreshColor();
                    pickColorIndex = -1;
                    isPickColor = false;
                    setCursor();
                }
            }
        });

        parentApp.enqueue(new Callable() {
            public Object call() throws Exception {
                // call methods that modify the scene graph here
//                    System.out.println("here1");

                Vector3f[] vertices;
                Vector2f[] texCoord;
                short[] indexes;

                vertices = new Vector3f[1 * 4];
                texCoord = new Vector2f[1 * 8];
                indexes = new short[1 * 6];
                int m = 0;

                vertices[4 * m + 2] = new Vector3f(0, 0, 0.02f);
                vertices[4 * m + 3] = new Vector3f(0 + 1, 0, 0.02f);
                vertices[4 * m] = new Vector3f(0, 0 + 1, 0.02f);
                vertices[4 * m + 1] = new Vector3f(0 + 1, 0 + 1, 0.02f);

                texCoord[4 * m] = new Vector2f(0, 0);
                texCoord[4 * m + 1] = new Vector2f((float) 1 / (float) 1, 0);
                texCoord[4 * m + 2] = new Vector2f(0, (float) 1 / (float) 1);
                texCoord[4 * m + 3] = new Vector2f((float) 1 / (float) 1, (float) 1 / (float) 1);

                indexes[6 * m] = (short) (4 * m + 1);
                indexes[6 * m + 1] = (short) (4 * m);
                indexes[6 * m + 2] = (short) (4 * m + 2);
                indexes[6 * m + 3] = (short) (4 * m + 1);
                indexes[6 * m + 4] = (short) (4 * m + 2);
                indexes[6 * m + 5] = (short) (4 * m + 3);

                Mesh myMesh = new Mesh();
                myMesh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
                myMesh.setBuffer(VertexBuffer.Type.TexCoord, 2, BufferUtils.createFloatBuffer(texCoord));
                myMesh.setBuffer(VertexBuffer.Type.Index, 3, BufferUtils.createShortBuffer(indexes));
                myMesh.updateBound();
                imageGeo = new Geometry("image", myMesh);
                scaleLayer();
                translateLayer();
                Material imageMat = new Material(parentApp.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
                Image image_temp = new AWTLoader().load(bImageFromConvert, false);

                Texture2D temp_tex = new Texture2D(image_temp);
                temp_tex.getImage();
                temp_tex.setWrap(Texture.WrapMode.Clamp);
                imageMat.setTexture("ColorMap", temp_tex);
                imageGeo.setMaterial(imageMat);
                parentApp.myRootNode.attachChild(imageGeo);
                if (imageControlled.categories != null) {
                    isEditNeeded = false;
                }
                return null;
            }
        });

        if (isEditMode) {
            for (int i = 0; i < imageControlled.colors.length; i++) {
                ColorDefinition temp = new ColorDefinition(this, imageControlled.colors[i], colorDefinitions.size());
                temp.NameText.setText(imageControlled.categories[i]);
                temp.valueText.setText(String.valueOf(imageControlled.values[i]));
                colorDefinitions.add(temp);
            }
            refreshList();
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        jSplitPane2 = new javax.swing.JSplitPane();
        jSplitPane3 = new javax.swing.JSplitPane();
        jPanel5 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        IncWidth = new javax.swing.JButton();
        DecWidth = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        widthText = new javax.swing.JFormattedTextField();
        jPanel3 = new javax.swing.JPanel();
        MoveUp = new javax.swing.JButton();
        MoveDown = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        yText = new javax.swing.JFormattedTextField();
        jPanel2 = new javax.swing.JPanel();
        IncHeight = new javax.swing.JButton();
        DecHeight = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        heightText = new javax.swing.JFormattedTextField();
        jPanel4 = new javax.swing.JPanel();
        MoveLeft = new javax.swing.JButton();
        MoveRight = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        xText = new javax.swing.JFormattedTextField();
        parameterSetDefaults = new javax.swing.JButton();
        parameterGetDefaults = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel6 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jFormattedTextField1 = new javax.swing.JFormattedTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        colorsPanel = new javax.swing.JPanel();
        processButton = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jFormattedTextField2 = new javax.swing.JFormattedTextField();
        jPanel9 = new javax.swing.JPanel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        layerNameTextField = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jSplitPane3.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jPanel5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel5.setMinimumSize(new java.awt.Dimension(434, 245));
        jPanel5.setPreferredSize(new java.awt.Dimension(434, 245));

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        IncWidth.setText("Increase width");
        IncWidth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IncWidthActionPerformed(evt);
            }
        });

        DecWidth.setText("Decrease width");
        DecWidth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DecWidthActionPerformed(evt);
            }
        });

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/esmaieeli/gisFastLocationOptimization/GUI/mousehweel.png"))); // NOI18N
        jButton3.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                jButton3MouseWheelMoved(evt);
            }
        });
        jButton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton3MouseEntered(evt);
            }
        });

        widthText.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        widthText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                widthTextActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(IncWidth)
                .addGap(18, 18, 18)
                .addComponent(DecWidth)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(widthText, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(IncWidth)
                    .addComponent(DecWidth)
                    .addComponent(widthText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(16, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        MoveUp.setText("Move up");
        MoveUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MoveUpActionPerformed(evt);
            }
        });

        MoveDown.setText("Move down");
        MoveDown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MoveDownActionPerformed(evt);
            }
        });

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/esmaieeli/gisFastLocationOptimization/GUI/mousehweel.png"))); // NOI18N
        jButton5.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                jButton5MouseWheelMoved(evt);
            }
        });
        jButton5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton5MouseEntered(evt);
            }
        });

        yText.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        yText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                yTextActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(MoveDown)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(yText, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(22, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(MoveUp)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(MoveUp))
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(MoveDown)
                    .addComponent(yText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        IncHeight.setText("Increase height");
        IncHeight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IncHeightActionPerformed(evt);
            }
        });

        DecHeight.setText("Decrease height");
        DecHeight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DecHeightActionPerformed(evt);
            }
        });

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/esmaieeli/gisFastLocationOptimization/GUI/mousehweel.png"))); // NOI18N
        jButton4.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                jButton4MouseWheelMoved(evt);
            }
        });
        jButton4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton4MouseEntered(evt);
            }
        });

        heightText.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        heightText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                heightTextActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(DecHeight)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(heightText)
                        .addContainerGap())
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(IncHeight)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 58, Short.MAX_VALUE)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(IncHeight))
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(DecHeight)
                    .addComponent(heightText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        MoveLeft.setText("Move left");
        MoveLeft.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MoveLeftActionPerformed(evt);
            }
        });

        MoveRight.setText("Move right");
        MoveRight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MoveRightActionPerformed(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/esmaieeli/gisFastLocationOptimization/GUI/mousehweel.png"))); // NOI18N
        jButton2.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                jButton2MouseWheelMoved(evt);
            }
        });
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton2MouseEntered(evt);
            }
        });

        xText.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        xText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                xTextActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(MoveLeft)
                .addGap(18, 18, 18)
                .addComponent(MoveRight)
                .addGap(18, 18, 18)
                .addComponent(xText, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(MoveLeft)
                    .addComponent(MoveRight)
                    .addComponent(xText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        parameterSetDefaults.setText("Set as default");
        parameterSetDefaults.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                parameterSetDefaultsActionPerformed(evt);
            }
        });

        parameterGetDefaults.setText("Reset to defaults");
        parameterGetDefaults.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                parameterGetDefaultsActionPerformed(evt);
            }
        });

        jButton6.setText("Layer description");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel5Layout.createSequentialGroup()
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(parameterSetDefaults)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(parameterGetDefaults)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton6)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(parameterSetDefaults)
                    .addComponent(parameterGetDefaults)
                    .addComponent(jButton6))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jSplitPane3.setLeftComponent(jPanel5);

        jPanel7.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel7.setMinimumSize(new java.awt.Dimension(50, 50));
        jPanel7.setLayout(new java.awt.GridLayout(1, 0));

        jPanel10.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel10.setLayout(new java.awt.BorderLayout());
        jPanel7.add(jPanel10);

        jSplitPane3.setRightComponent(jPanel7);

        jSplitPane2.setLeftComponent(jSplitPane3);

        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel6.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                jPanel6ComponentResized(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 383, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 726, Short.MAX_VALUE)
        );

        jSplitPane1.setRightComponent(jPanel6);

        jPanel8.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel8.setMinimumSize(new java.awt.Dimension(160, 10));
        jPanel8.setPreferredSize(new java.awt.Dimension(180, 700));

        jLabel1.setText("RGB telorance:");

        jFormattedTextField1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        jFormattedTextField1.setText("5");
        jFormattedTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFormattedTextField1ActionPerformed(evt);
            }
        });

        colorsPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        colorsPanel.setPreferredSize(new java.awt.Dimension(170, 100));

        javax.swing.GroupLayout colorsPanelLayout = new javax.swing.GroupLayout(colorsPanel);
        colorsPanel.setLayout(colorsPanelLayout);
        colorsPanelLayout.setHorizontalGroup(
            colorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 166, Short.MAX_VALUE)
        );
        colorsPanelLayout.setVerticalGroup(
            colorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 390, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(colorsPanel);

        processButton.setText("Ok");
        processButton.setEnabled(false);
        processButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                processButtonActionPerformed(evt);
            }
        });

        jButton7.setText("Cancel");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jLabel2.setText("Scroll Step size:");

        jFormattedTextField2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jFormattedTextField2.setText("10");
        jFormattedTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFormattedTextField2ActionPerformed(evt);
            }
        });

        jPanel9.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setSelected(true);
        jRadioButton1.setText("Benefit");

        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setText("Cost");

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/esmaieeli/gisFastLocationOptimization/GUI/Cost.png"))); // NOI18N

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/esmaieeli/gisFastLocationOptimization/GUI/Profit.png"))); // NOI18N

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jRadioButton1)
                    .addComponent(jRadioButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jRadioButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jRadioButton2))
                .addContainerGap())
        );

        jButton1.setText("Add color");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel5.setText("Layer name:");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jFormattedTextField1))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(processButton)
                                .addGap(18, 18, 18)
                                .addComponent(jButton7))
                            .addComponent(jLabel2)
                            .addComponent(jButton1)
                            .addComponent(jFormattedTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(layerNameTextField))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 415, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(layerNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jFormattedTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(processButton)
                    .addComponent(jButton7))
                .addContainerGap())
        );

        jSplitPane1.setLeftComponent(jPanel8);

        jSplitPane2.setRightComponent(jSplitPane1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1007, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jSplitPane2)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void MoveLeftActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MoveLeftActionPerformed
        // TODO add your handling code here:
        parentApp.enqueue(new Callable() {
            public Object call() throws Exception {
                // call methods that modify the scene graph here
                imageControlled.my_position.x = imageControlled.my_position.x - 0.1f;
                xText.setValue(imageControlled.my_position.x);
                translateLayer();
                return null;
            }
        });
    }//GEN-LAST:event_MoveLeftActionPerformed

    private void MoveRightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MoveRightActionPerformed
        // TODO add your handling code here:
        parentApp.enqueue(new Callable() {
            public Object call() throws Exception {
                // call methods that modify the scene graph here
                imageControlled.my_position.x = imageControlled.my_position.x + 0.1f;
                xText.setValue(imageControlled.my_position.x);
                translateLayer();
                return null;
            }
        });
    }//GEN-LAST:event_MoveRightActionPerformed

    private void IncWidthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IncWidthActionPerformed
        // TODO add your handling code here:
        parentApp.enqueue(new Callable() {
            public Object call() throws Exception {
                // call methods that modify the scene graph here
                imageControlled.my_width = imageControlled.my_width + 0.1f;
                widthText.setValue(imageControlled.my_width);
                scaleLayer();
                return null;
            }
        });
    }//GEN-LAST:event_IncWidthActionPerformed

    private void DecWidthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DecWidthActionPerformed
        // TODO add your handling code here:
        parentApp.enqueue(new Callable() {
            public Object call() throws Exception {
                // call methods that modify the scene graph here
                imageControlled.my_width = imageControlled.my_width - 0.1f;
                widthText.setValue(imageControlled.my_width);
                scaleLayer();
                return null;
            }
        });
    }//GEN-LAST:event_DecWidthActionPerformed

    private void IncHeightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IncHeightActionPerformed
        // TODO add your handling code here:
        parentApp.enqueue(new Callable() {
            public Object call() throws Exception {
                // call methods that modify the scene graph here
                imageControlled.my_height = imageControlled.my_height + 0.1f;
                heightText.setValue(imageControlled.my_height);
                scaleLayer();
                return null;
            }
        });
    }//GEN-LAST:event_IncHeightActionPerformed

    private void DecHeightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DecHeightActionPerformed
        // TODO add your handling code here:
        parentApp.enqueue(new Callable() {
            public Object call() throws Exception {
                // call methods that modify the scene graph here
                imageControlled.my_height = imageControlled.my_height - 0.1f;
                heightText.setValue(imageControlled.my_height);
                scaleLayer();
                return null;
            }
        });
    }//GEN-LAST:event_DecHeightActionPerformed

    private void MoveUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MoveUpActionPerformed
        // TODO add your handling code here:
        parentApp.enqueue(new Callable() {
            public Object call() throws Exception {
                // call methods that modify the scene graph here
                imageControlled.my_position.y = imageControlled.my_position.y + 0.1f;
                yText.setValue(imageControlled.my_position.y);
                translateLayer();
                return null;
            }
        });
    }//GEN-LAST:event_MoveUpActionPerformed

    private void MoveDownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MoveDownActionPerformed
        // TODO add your handling code here:
        parentApp.enqueue(new Callable() {
            public Object call() throws Exception {
                // call methods that modify the scene graph here
                imageControlled.my_position.y = imageControlled.my_position.y - 0.1f;
                yText.setValue(imageControlled.my_position.y);
                translateLayer();
                return null;
            }
        });
    }//GEN-LAST:event_MoveDownActionPerformed

    private void jPanel6ComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jPanel6ComponentResized
        // TODO add your handling code here:
        settings.setWidth(jPanel6.getWidth());
        settings.setHeight(jPanel6.getHeight());
        canvas.setSize(settings.getWidth(), settings.getHeight());
    }//GEN-LAST:event_jPanel6ComponentResized

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        colorDefinitions.add(new ColorDefinition(this, new Color(255, 0, 0), colorDefinitions.size()));
        isEditNeeded = true;
        refreshList();
    }//GEN-LAST:event_jButton1ActionPerformed


    private void imagePloter1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_imagePloter1FocusLost
        // TODO add your handling code here:
        isPickColor = false;
        setCursor();
    }//GEN-LAST:event_imagePloter1FocusLost

    private void jFormattedTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFormattedTextField1ActionPerformed
        // TODO add your handling code here:
        if (Integer.valueOf(jFormattedTextField1.getText()) > 254) {
            jFormattedTextField1.setText("254");
        }
        if (Integer.valueOf(jFormattedTextField1.getText()) < 0) {
            jFormattedTextField1.setText("0");
        }
        isEditNeeded = true;
    }//GEN-LAST:event_jFormattedTextField1ActionPerformed

    private void jButton2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseEntered
        // TODO add your handling code here:
        ((JButton) evt.getSource()).requestFocus();
    }//GEN-LAST:event_jButton2MouseEntered

    private void jButton2MouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_jButton2MouseWheelMoved
        // TODO add your handling code here:
        int notches = evt.getWheelRotation();
        if (notches < 0) {
            parentApp.enqueue(new Callable() {
                public Object call() throws Exception {
                    // call methods that modify the scene graph here
                    imageControlled.my_position.x = imageControlled.my_position.x - 0.1f * Float.valueOf(jFormattedTextField2.getText());
                    xText.setValue(imageControlled.my_position.x);
                    translateLayer();
                    return null;
                }
            });
        } else {
            parentApp.enqueue(new Callable() {
                public Object call() throws Exception {
                    // call methods that modify the scene graph here
                    imageControlled.my_position.x = imageControlled.my_position.x + 0.1f * Float.valueOf(jFormattedTextField2.getText());
                    xText.setValue(imageControlled.my_position.x);
                    translateLayer();
                    return null;
                }
            });
        }
    }//GEN-LAST:event_jButton2MouseWheelMoved

    private void jButton4MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton4MouseEntered
        // TODO add your handling code here:
        ((JButton) evt.getSource()).requestFocus();
    }//GEN-LAST:event_jButton4MouseEntered

    private void jButton4MouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_jButton4MouseWheelMoved
        // TODO add your handling code here:
        int notches = evt.getWheelRotation();
        if (notches < 0) {
            parentApp.enqueue(new Callable() {
                public Object call() throws Exception {
                    // call methods that modify the scene graph here
                    imageControlled.my_height = imageControlled.my_height + 0.1f * Float.valueOf(jFormattedTextField2.getText());
                    heightText.setValue(imageControlled.my_height);
                    scaleLayer();
                    return null;
                }
            });
        } else {
            parentApp.enqueue(new Callable() {
                public Object call() throws Exception {
                    // call methods that modify the scene graph here
                    imageControlled.my_height = imageControlled.my_height - 0.1f * Float.valueOf(jFormattedTextField2.getText());
                    heightText.setValue(imageControlled.my_height);
                    scaleLayer();
                    return null;
                }
            });
        }
    }//GEN-LAST:event_jButton4MouseWheelMoved

    private void jButton5MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton5MouseEntered
        // TODO add your handling code here:
        ((JButton) evt.getSource()).requestFocus();
    }//GEN-LAST:event_jButton5MouseEntered

    private void jButton5MouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_jButton5MouseWheelMoved
        // TODO add your handling code here:
        int notches = evt.getWheelRotation();
        if (notches < 0) {
            parentApp.enqueue(new Callable() {
                public Object call() throws Exception {
                    // call methods that modify the scene graph here
                    imageControlled.my_position.y = imageControlled.my_position.y + 0.1f * Float.valueOf(jFormattedTextField2.getText());
                    yText.setValue(imageControlled.my_position.y);
                    translateLayer();
                    return null;
                }
            });
        } else {
            parentApp.enqueue(new Callable() {
                public Object call() throws Exception {
                    // call methods that modify the scene graph here
                    imageControlled.my_position.y = imageControlled.my_position.y - 0.1f * Float.valueOf(jFormattedTextField2.getText());
                    yText.setValue(imageControlled.my_position.y);
                    translateLayer();
                    return null;
                }
            });
        }
    }//GEN-LAST:event_jButton5MouseWheelMoved

    private void jButton3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseEntered
        // TODO add your handling code here:
        ((JButton) evt.getSource()).requestFocus();
    }//GEN-LAST:event_jButton3MouseEntered

    private void jButton3MouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_jButton3MouseWheelMoved
        // TODO add your handling code here:
        int notches = evt.getWheelRotation();
        if (notches < 0) {
            parentApp.enqueue(new Callable() {
                public Object call() throws Exception {
                    // call methods that modify the scene graph here
                    imageControlled.my_width = imageControlled.my_width + 0.1f * Float.valueOf(jFormattedTextField2.getText());
                    widthText.setValue(imageControlled.my_width);
                    scaleLayer();
                    return null;
                }
            });
        } else {
            parentApp.enqueue(new Callable() {
                public Object call() throws Exception {
                    // call methods that modify the scene graph here
                    imageControlled.my_width = imageControlled.my_width - 0.1f * Float.valueOf(jFormattedTextField2.getText());
                    widthText.setValue(imageControlled.my_width);
                    scaleLayer();
                    return null;
                }
            });
        }
    }//GEN-LAST:event_jButton3MouseWheelMoved

    private void processButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_processButtonActionPerformed
        // TODO add your handling code here:
        imageControlled.layerName = layerNameTextField.getText();
        imageControlled.colors = new Color[colorDefinitions.size()];
        imageControlled.values = new double[colorDefinitions.size()];
        imageControlled.categories = new String[colorDefinitions.size()];
        for (int i = 0; i < colorDefinitions.size(); i++) {
            imageControlled.colors[i] = ((ColorDefinition) colorDefinitions.get(i)).myColor;
            imageControlled.values[i] = Double.valueOf(((ColorDefinition) colorDefinitions.get(i)).valueText.getText());
            imageControlled.categories[i] = ((ColorDefinition) colorDefinitions.get(i)).NameText.getText();
//            System.out.println("red " + ((ColorDefinition) colorDefinitions.get(i)).myColor.getRed());
//            System.out.println("green " + ((ColorDefinition) colorDefinitions.get(i)).myColor.getGreen());
//            System.out.println("blue " + ((ColorDefinition) colorDefinitions.get(i)).myColor.getBlue());
        }
        if (isEditMode == false) {
            myParent.allData.all_Layers.add(imageControlled);
        }
        System.out.println("isEditNeeded: " + isEditNeeded);
        if (isEditNeeded == true && isEditMode == false) {

            //REPORTING
            String detailedResults = "Projecting image on vector data." + System.lineSeparator();
            Calendar currentDate = Calendar.getInstance();
            Date date = currentDate.getTime();
            double startRAM = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024.0;
            long startTime = System.nanoTime();

            detailedResults = detailedResults + "Layer name: " + imageControlled.layerName + System.lineSeparator();
            //REPORTING

            int numLayers = myParent.allData.all_Layers.size();
            boolean isBroken;
            for (int i = 0; i < myParent.allData.all_Nodes.length; i++) {
                //System.out.println((float)i/(float)myParent.allData.all_Nodes.length);
                if (myParent.allData.all_Nodes[i].layers.size() < numLayers) {
                    if(myParent.allData.all_Nodes[i].renderingLocation==null){
                        boolean isFound=false;
                        for (int j = 0; j < myParent.allData.all_Nodes[i].myWays.length; j++) {
                            for (int k = 0; k < myParent.allData.all_Nodes[i].myWays[j].myNodes.length; k++) {
                                if(myParent.allData.all_Nodes[i].myWays[j].myNodes[k].id==myParent.allData.all_Nodes[i].id){
                                    myParent.allData.all_Nodes[i].renderingLocation=myParent.allData.all_Nodes[i].myWays[j].lines[k];
                                    isFound=true;
                                    break;
                                }
                            }
                            if(isFound==true){
                                break;
                            }
                        }
                    }
                    int imageX = (int) (((double) (myParent.allData.all_Nodes[i].renderingLocation.x - imageControlled.my_position.x) / (double) imageControlled.my_width) * (double) imagePloter1.img.getWidth());
                    int imageY = (int) ((((double) myParent.allData.all_Nodes[i].renderingLocation.y - (double) imageControlled.my_position.y) / (double) imageControlled.my_height) * (double) imagePloter1.img.getHeight());
                    isBroken = checkColor(imageX, imageY, i);
                    if (isBroken == false) {
                        int dist = 1;
                        while (isBroken == false) {
                            for (int l = -dist; l < dist; l++) {
                                for (int m = -dist; m < dist; m++) {
                                    isBroken = checkColor(imageX + l, imageY + m, i);
                                    if (isBroken == true) {
                                        break;
                                    }
                                }
                                if (isBroken == true) {
                                    break;
                                }
                            }
                            dist = dist + 1;
                        }
                    }
                }

                //System.out.println(i);
            }

            //REPORTING
            long endTime = System.nanoTime();
            double elapsed = ((endTime - startTime) / 1000000000);
            double endRAM = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024.0;
            double usedRAM = endRAM - startRAM;
            localReport = new ReportResults(date, "Add image layer", startTime, endTime, elapsed, 1, startRAM, endRAM, usedRAM, detailedResults);
            myParent.allData.results.add(localReport);
            myParent.refreshReportList();
            //REPORTING

        } else if (isEditNeeded == true && isEditMode == true) {
            short temp[] = new short[1];
            temp[0] = -1;
            for (int i = 0; i < myParent.allData.all_Ways.length; i++) {
                for (int j = 0; j < myParent.allData.all_Ways[i].myNodes.length; j++) {
                    myParent.allData.all_Ways[i].myNodes[j].layers.set(editIndex, temp);
                }
            }
            boolean isBroken;
            for (int i = 0; i < myParent.allData.all_Nodes.length; i++) {
                if (((short[]) myParent.allData.all_Nodes[i].layers.get(editIndex))[0] == -1) {
                    int imageX = (int) (((double) (myParent.allData.all_Nodes[i].renderingLocation.x - imageControlled.my_position.x) / (double) imageControlled.my_width) * (double) imagePloter1.img.getWidth());
                    int imageY = (int) ((((double) myParent.allData.all_Nodes[i].renderingLocation.y - (double) imageControlled.my_position.y) / (double) imageControlled.my_height) * (double) imagePloter1.img.getHeight());
                    isBroken = checkColor(imageX, imageY, i);
                    if (isBroken == false) {
                        int dist = 1;
                        while (isBroken == false) {
                            for (int l = -dist; l < dist; l++) {
                                for (int m = -dist; m < dist; m++) {
                                    isBroken = checkColor(imageX + l, imageY + m, i);
                                    if (isBroken == true) {
                                        break;
                                    }
                                }
                                if (isBroken == true) {
                                    break;
                                }
                            }
                            dist = dist + 1;
                        }
                    }
                }

            }
        }

        parentApp.stop();
        myParent.refreshLayersList();
        this.dispose();
    }//GEN-LAST:event_processButtonActionPerformed

    private boolean checkColor(int imageX, int imageY, int i) {
        boolean isBroken = false;
        if (imageX > -1 && imageX < imagePloter1.img.getWidth() - 1 && -imageY + imagePloter1.img.getHeight() > -1 && -imageY + imagePloter1.img.getHeight() < imagePloter1.img.getHeight() - 1) {
            Color temp = new Color(imagePloter1.img.getRGB(imageX, -imageY + imagePloter1.img.getHeight()));
            for (int k = 0; k < imageControlled.colors.length; k++) {
                if (Math.abs(temp.getRed() - imageControlled.colors[k].getRed()) + Math.abs(temp.getGreen() - imageControlled.colors[k].getGreen()) + Math.abs(temp.getBlue() - imageControlled.colors[k].getBlue()) < Integer.parseInt(jFormattedTextField1.getText())) {
                    short[] pass = new short[1];
                    pass[0] = (short) (k + 1);
                    if (isEditMode == false) {
                        myParent.allData.all_Nodes[i].layers.add(pass);
                    } else {
                        myParent.allData.all_Nodes[i].layers.set(editIndex, pass);
                    }
                    isBroken = true;
                    break;
                }
            }
        }
        return isBroken;
    }

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        parentApp.stop();
        this.dispose();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jFormattedTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFormattedTextField2ActionPerformed
        // TODO add your handling code here:
        if (Float.parseFloat(jFormattedTextField2.getText()) < 0.001) {
            jFormattedTextField2.setText(String.valueOf(0.001));
        }
    }//GEN-LAST:event_jFormattedTextField2ActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        parentApp.stop();
    }//GEN-LAST:event_formWindowClosing

    private void xTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xTextActionPerformed
        // TODO add your handling code here:
        parentApp.enqueue(new Callable() {
            public Object call() throws Exception {
                // call methods that modify the scene graph here
                imageControlled.my_position.x = Float.valueOf(xText.getText());
                translateLayer();
                return null;
            }
        });
    }//GEN-LAST:event_xTextActionPerformed

    private void heightTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_heightTextActionPerformed
        // TODO add your handling code here:
        parentApp.enqueue(new Callable() {
            public Object call() throws Exception {
                // call methods that modify the scene graph here
                imageControlled.my_height = ((Double) heightText.getValue()).floatValue();
                scaleLayer();
                return null;
            }
        });
    }//GEN-LAST:event_heightTextActionPerformed

    private void yTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_yTextActionPerformed
        // TODO add your handling code here:
        parentApp.enqueue(new Callable() {
            public Object call() throws Exception {
                // call methods that modify the scene graph here
                imageControlled.my_position.y = ((Double) yText.getValue()).floatValue();
                translateLayer();
                return null;
            }
        });
    }//GEN-LAST:event_yTextActionPerformed

    private void widthTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_widthTextActionPerformed
        // TODO add your handling code here:
        parentApp.enqueue(new Callable() {
            public Object call() throws Exception {
                // call methods that modify the scene graph here
                imageControlled.my_width = ((Double) widthText.getValue()).floatValue();
                scaleLayer();
                return null;
            }
        });
    }//GEN-LAST:event_widthTextActionPerformed

    private void parameterSetDefaultsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_parameterSetDefaultsActionPerformed
        // TODO add your handling code here:
        myParent.imageControllerDefaults.xPosition = Float.valueOf(xText.getText());
        System.out.println(Float.valueOf(xText.getText()));
        myParent.imageControllerDefaults.yPosition = Float.valueOf(yText.getText());
        myParent.imageControllerDefaults.width = Float.valueOf(widthText.getText());
        myParent.imageControllerDefaults.height = Float.valueOf(heightText.getText());
    }//GEN-LAST:event_parameterSetDefaultsActionPerformed

    private void parameterGetDefaultsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_parameterGetDefaultsActionPerformed
        // TODO add your handling code here:
        parentApp.enqueue(new Callable() {
            public Object call() throws Exception {
                // call methods that modify the scene graph here
                imageControlled.my_position.x = myParent.imageControllerDefaults.xPosition;
                imageControlled.my_position.y = myParent.imageControllerDefaults.yPosition;
                imageControlled.my_width = myParent.imageControllerDefaults.width;
                imageControlled.my_height = myParent.imageControllerDefaults.height;
                xText.setValue((double) myParent.imageControllerDefaults.xPosition);
                yText.setValue((double) myParent.imageControllerDefaults.yPosition);
                widthText.setValue((double) myParent.imageControllerDefaults.width);
                heightText.setValue((double) myParent.imageControllerDefaults.height);
                scaleLayer();
                translateLayer();
                return null;
            }
        });
        isEditNeeded = true;
    }//GEN-LAST:event_parameterGetDefaultsActionPerformed

    public void setCursor() {
        if (isPickColor == true) {
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            java.awt.Image image = toolkit.getImage("src/esmaieeli/gisFastLocationOptimization/GUI/colorPicker.png");
            Cursor c = toolkit.createCustomCursor(image, new Point(0, 31), "img");
            this.setCursor(c);
            imagePloter1.requestFocus();
        } else {
            this.setCursor(Cursor.getDefaultCursor());
        }

    }

    public void refreshList() {
        colorsPanel.removeAll();
        for (int i = 0; i < colorDefinitions.size(); i++) {
            ColorDefinition temp = (ColorDefinition) colorDefinitions.get(i);
            ((ColorDefinition) colorDefinitions.get(i)).myOrder = i;
            temp.setBounds(0, i * 55, 243, 55);
            colorsPanel.add(temp);
        }
        colorsPanel.setPreferredSize(new Dimension(243, (int) (colorDefinitions.size()) * 55));
        colorsPanel.revalidate();
        colorsPanel.repaint();
        if (colorDefinitions.size() > 0) {
            processButton.setEnabled(true);
        } else {
            processButton.setEnabled(false);
        }
    }

    private void translateLayer() {
        imageGeo.setLocalTranslation(imageControlled.my_position.x, imageControlled.my_position.y, -0.5f);
        isEditNeeded = true;
    }

    private void scaleLayer() {
        imageGeo.setLocalScale(imageControlled.my_width, imageControlled.my_height, 1);
        isEditNeeded = true;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton DecHeight;
    private javax.swing.JButton DecWidth;
    private javax.swing.JButton IncHeight;
    private javax.swing.JButton IncWidth;
    private javax.swing.JButton MoveDown;
    private javax.swing.JButton MoveLeft;
    private javax.swing.JButton MoveRight;
    private javax.swing.JButton MoveUp;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JPanel colorsPanel;
    private javax.swing.JFormattedTextField heightText;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JFormattedTextField jFormattedTextField1;
    private javax.swing.JFormattedTextField jFormattedTextField2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JSplitPane jSplitPane3;
    private javax.swing.JTextField layerNameTextField;
    private javax.swing.JButton parameterGetDefaults;
    private javax.swing.JButton parameterSetDefaults;
    private javax.swing.JButton processButton;
    private javax.swing.JFormattedTextField widthText;
    private javax.swing.JFormattedTextField xText;
    private javax.swing.JFormattedTextField yText;
    // End of variables declaration//GEN-END:variables
}
