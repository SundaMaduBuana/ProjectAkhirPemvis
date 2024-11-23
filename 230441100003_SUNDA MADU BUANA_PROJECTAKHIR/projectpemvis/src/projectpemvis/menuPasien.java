/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package projectpemvis;
import com.toedter.calendar.JDateChooser;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
public class menuPasien extends javax.swing.JFrame {
    private Connection conn; // Database connection
    private DefaultTableModel model_tbl_pasien;
    private DefaultTableModel model_tbl_janji;
    private javax.swing.JRadioButton jkPp; 
    private javax.swing.JRadioButton jkLl;
    private ButtonGroup genderGroup;
    private JDateChooser tanggalChooser;
    private javax.swing.JComboBox<String> cbPasien; // pasien
    private javax.swing.JComboBox<String> cbDokter; // dokter
    private javax.swing.JButton btnBatalkan;
    int no;
    
    public menuPasien() {
        initComponents();
        setLocationRelativeTo(null);
        conn = koneksi.getConnection();
        
        // pasien
        model_tbl_pasien = new DefaultTableModel();
        tbpasien.setModel(model_tbl_pasien);
        model_tbl_pasien.addColumn("ID");
        model_tbl_pasien.addColumn("Nama");
        model_tbl_pasien.addColumn("Umur");
        model_tbl_pasien.addColumn("Jenis Kelamin");
        model_tbl_pasien.addColumn("No Telp");

        jkL = new javax.swing.JRadioButton("Laki - laki");
        jkP = new javax.swing.JRadioButton("Perempuan");
        genderGroup = new ButtonGroup();
        genderGroup.add(jkP);
        genderGroup.add(jkL);
        loadData();
        tbpasien.setDefaultEditor(Object.class, null);
        
        // janji
        model_tbl_janji = new DefaultTableModel();
        tbjanji.setModel(model_tbl_janji);
        model_tbl_janji.addColumn("ID");
        model_tbl_janji.addColumn("ID Pasien");
        model_tbl_janji.addColumn("ID Dokter");
        model_tbl_janji.addColumn("Tanggal");
        model_tbl_janji.addColumn("Status");

        // Initialize Combo Boxes
        cbPasien = new javax.swing.JComboBox<>();
        cbDokter = new javax.swing.JComboBox<>();
        
        // Load data into combo boxes
        loadComboBoxPasien();
        loadComboBoxDokter();

        tanggalChooser = new JDateChooser();
        tanggalChooser.setDateFormatString("yyyy-MM-dd");
        loadDatajanji();
        tbjanji.setDefaultEditor(Object.class, null);
        
        btnBatalkan = new javax.swing.JButton("Batalkan Janji");
        btnBatalkan.addActionListener(e -> batalkanJanji());   
    }
    private void batalkanJanji() {
        int selectedRow = tbjanji.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Silakan pilih janji untuk dibatalkan", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Mendapatkan ID janji dari tabel
        int idJanji = (int) tbjanji.getValueAt(selectedRow, 0);

        // Meminta konfirmasi dan input nama pasien serta ID dokter
        String namaPasien = JOptionPane.showInputDialog(this, "Masukkan nama pasien:");
        String idDokter = JOptionPane.showInputDialog(this, "Masukkan ID dokter:");

        if (namaPasien != null && idDokter != null) {
            // Jika input tidak null, lanjutkan untuk membatalkan janji
            try {
                String sql = "DELETE FROM daftar_janji WHERE id = ?";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, idJanji);
                    ps.executeUpdate();

                    JOptionPane.showMessageDialog(this, "Janji berhasil dibatalkan untuk " + namaPasien + " dan ID dokter " + idDokter);
                    loadDatajanji(); // Refresh tabel
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error membatalkan janji: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Nama pasien dan ID dokter harus diisi", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void loadComboBoxPasien() {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        String sql = "SELECT id, nama FROM pasien";
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                model.addElement(rs.getInt("id") + " - " + rs.getString("nama"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading patients: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        idP.setModel(model);
    }
    private void loadComboBoxDokter() {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        String sql = "SELECT id, spesialis FROM dokter";
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                model.addElement(rs.getInt("id") + " - " + rs.getString("spesialis"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading doctors: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        idD.setModel(model);
    }
    private void loadData() {
        model_tbl_pasien.setRowCount(0);
        try {
            String sql = "SELECT * FROM pasien";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet hasil = ps.executeQuery();
            while (hasil.next()) {
                // Menambahkan baris ke dalam model tabel
                model_tbl_pasien.addRow(new Object[]{
                    hasil.getInt("id"),
                    hasil.getString("nama"),
                    hasil.getString("umur"),
                    hasil.getString("jenis_kelamin"),
                    hasil.getString("no_telp")
                });
            }
        } catch (SQLException e) {
            System.out.println("Error Save Data" + e.getMessage());
        }
    }
    
    private void loadDatajanji() {
        model_tbl_janji.setRowCount(0);
        try {
            String sql = "SELECT * FROM daftar_janji";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet hasil = ps.executeQuery();
            while (hasil.next()) {
                // Menambahkan baris ke dalam model tabel
                model_tbl_janji.addRow(new Object[]{
                    hasil.getInt("id"),
                    hasil.getString("id_pasien"),
                    hasil.getString("id_dokter"),
                    hasil.getString("tanggal"),
                    hasil.getString("status")
                });
            }
        } catch (SQLException e) {
            System.out.println("Error Save Data" + e.getMessage());
        }
    }
    
    private void saveData() {
        try {
            String sql = "INSERT INTO pasien (nama, umur, jenis_kelamin, no_telp) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nama.getText());
            ps.setString(2, umur.getText());
            String gender = jkP.isSelected() ? "Perempuan" : "Laki - laki";
            ps.setString(3, gender);
            ps.setString(4, telp.getText());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data saved successfully");
            loadData();
            nama.setText("");
            umur.setText("");
            telp.setText("");
            genderGroup.clearSelection();
        } catch (SQLException e) {
            System.out.println("Error Save Data" + e.getMessage());
        }
    }
    
    private void saveDatajanji() {
        try {
            String sql = "INSERT INTO daftar_janji (id_pasien, id_dokter, tanggal) VALUES (?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                String selectedPasien = (String) idP.getSelectedItem();
                String selectedDokter = (String) idD.getSelectedItem();
                
                // Extract ID from selected item (e.g., "1 - John Doe")
                int idPasien = Integer.parseInt(selectedPasien.split(" - ")[0]);
                int idDokter = Integer.parseInt(selectedDokter.split(" - ")[0]);
                
                ps.setInt(1, idPasien);
                ps.setInt(2, idDokter);
                ps.setString(3, new java.text.SimpleDateFormat("yyyy-MM-dd").format(tgl.getDate()));
                
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Data saved successfully");
                loadDatajanji();
                
                // Clear fields
                idP.setSelectedIndex(-1);
                idD.setSelectedIndex(-1);
                tgl.setDate(null);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error saving appointment data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        umur = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        nama = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jkP = new javax.swing.JRadioButton();
        jkL = new javax.swing.JRadioButton();
        jLabel9 = new javax.swing.JLabel();
        telp = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        idPasien = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbpasien = new javax.swing.JTable();
        simpan = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        tgl = new com.toedter.calendar.JDateChooser();
        idP = new javax.swing.JComboBox<>();
        idD = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbjanji = new javax.swing.JTable();
        simpan2 = new javax.swing.JButton();
        batalkan = new javax.swing.JButton();
        logout = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 207, 179));
        jPanel1.setPreferredSize(new java.awt.Dimension(150, 25));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setBackground(new java.awt.Color(222, 124, 125));
        jLabel1.setFont(new java.awt.Font("Perpetua Titling MT", 1, 20)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(179, 19, 18));
        jLabel1.setText("selamat datang di halaman pasien");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 20, -1, -1));

        jPanel3.setBackground(new java.awt.Color(234, 144, 122));

        jPanel5.setBackground(new java.awt.Color(222, 124, 125));
        jPanel5.setLayout(new java.awt.GridBagLayout());

        umur.setPreferredSize(new java.awt.Dimension(150, 25));
        umur.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                umurKeyReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel5.add(umur, gridBagConstraints);

        jLabel6.setFont(new java.awt.Font("Segoe UI Black", 0, 13)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Umur :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel5.add(jLabel6, gridBagConstraints);

        jLabel7.setFont(new java.awt.Font("Segoe UI Black", 0, 13)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Nama :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel5.add(jLabel7, gridBagConstraints);

        nama.setPreferredSize(new java.awt.Dimension(150, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel5.add(nama, gridBagConstraints);

        jLabel8.setFont(new java.awt.Font("Segoe UI Black", 0, 13)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Jenis Kelamin :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel5.add(jLabel8, gridBagConstraints);

        buttonGroup1.add(jkP);
        jkP.setFont(new java.awt.Font("Segoe UI Black", 0, 13)); // NOI18N
        jkP.setForeground(new java.awt.Color(255, 255, 255));
        jkP.setText("Perempuan");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel5.add(jkP, gridBagConstraints);

        buttonGroup1.add(jkL);
        jkL.setFont(new java.awt.Font("Segoe UI Black", 0, 13)); // NOI18N
        jkL.setForeground(new java.awt.Color(255, 255, 255));
        jkL.setText("Laki - laki");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel5.add(jkL, gridBagConstraints);

        jLabel9.setFont(new java.awt.Font("Segoe UI Black", 0, 13)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("No telp :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel5.add(jLabel9, gridBagConstraints);

        telp.setPreferredSize(new java.awt.Dimension(150, 25));
        telp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                telpKeyReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel5.add(telp, gridBagConstraints);

        jLabel4.setFont(new java.awt.Font("Segoe UI Black", 0, 13)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("ID :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel5.add(jLabel4, gridBagConstraints);

        idPasien.setPreferredSize(new java.awt.Dimension(150, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel5.add(idPasien, gridBagConstraints);

        tbpasien.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nama", "Umur", "Jenis Kelamin", "No telp"
            }
        ));
        jScrollPane2.setViewportView(tbpasien);

        simpan.setBackground(new java.awt.Color(222, 124, 125));
        simpan.setFont(new java.awt.Font("Segoe UI Black", 0, 12)); // NOI18N
        simpan.setForeground(new java.awt.Color(255, 255, 255));
        simpan.setText("Simpan");
        simpan.setPreferredSize(new java.awt.Dimension(85, 30));
        simpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                simpanActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 515, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(simpan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(14, 14, 14))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, 335, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(simpan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );

        jTabbedPane1.addTab("Data pasien", jPanel3);

        jPanel2.setBackground(new java.awt.Color(234, 144, 122));

        jPanel4.setBackground(new java.awt.Color(222, 124, 125));
        jPanel4.setPreferredSize(new java.awt.Dimension(224, 325));
        jPanel4.setLayout(new java.awt.GridBagLayout());

        jLabel2.setFont(new java.awt.Font("Segoe UI Black", 0, 13)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("ID Dokter");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel4.add(jLabel2, gridBagConstraints);

        jLabel3.setFont(new java.awt.Font("Segoe UI Black", 0, 13)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("ID Pasien :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel4.add(jLabel3, gridBagConstraints);

        jLabel5.setFont(new java.awt.Font("Segoe UI Black", 0, 13)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Tanggal Janji :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel4.add(jLabel5, gridBagConstraints);

        tgl.setPreferredSize(new java.awt.Dimension(150, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel4.add(tgl, gridBagConstraints);

        idP.setPreferredSize(new java.awt.Dimension(150, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel4.add(idP, gridBagConstraints);

        idD.setPreferredSize(new java.awt.Dimension(150, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel4.add(idD, gridBagConstraints);

        tbjanji.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "ID Pasien", "ID Dokter", "Tanggal", "Status"
            }
        ));
        jScrollPane1.setViewportView(tbjanji);

        simpan2.setBackground(new java.awt.Color(222, 124, 125));
        simpan2.setFont(new java.awt.Font("Segoe UI Black", 0, 12)); // NOI18N
        simpan2.setForeground(new java.awt.Color(255, 255, 255));
        simpan2.setText("Simpan");
        simpan2.setPreferredSize(new java.awt.Dimension(90, 35));
        simpan2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                simpan2ActionPerformed(evt);
            }
        });

        batalkan.setBackground(new java.awt.Color(222, 124, 125));
        batalkan.setFont(new java.awt.Font("Segoe UI Black", 0, 12)); // NOI18N
        batalkan.setForeground(new java.awt.Color(255, 255, 255));
        batalkan.setText("batalkan");
        batalkan.setPreferredSize(new java.awt.Dimension(90, 35));
        batalkan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batalkanActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 515, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(batalkan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(simpan2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 335, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(simpan2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(batalkan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14))
        );

        jTabbedPane1.addTab("Janji Temu", jPanel2);

        jPanel1.add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, -1, 370));

        logout.setBackground(new java.awt.Color(222, 124, 125));
        logout.setFont(new java.awt.Font("Segoe UI Black", 0, 12)); // NOI18N
        logout.setForeground(new java.awt.Color(255, 255, 255));
        logout.setText("Logout");
        logout.setPreferredSize(new java.awt.Dimension(85, 30));
        logout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutActionPerformed(evt);
            }
        });
        jPanel1.add(logout, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 440, -1, -1));

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/projectpemvis/img9.png"))); // NOI18N
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 0, -1, 90));

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/projectpemvis/img9.png"))); // NOI18N
        jPanel1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 90));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 730, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 489, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void simpan2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_simpan2ActionPerformed
        saveDatajanji();
    }//GEN-LAST:event_simpan2ActionPerformed

    private void logoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutActionPerformed
        menuLogin logout = new menuLogin();
        logout.setVisible(true);
        dispose();
    }//GEN-LAST:event_logoutActionPerformed

    private void umurKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_umurKeyReleased
        try {
            no = Integer.parseInt(umur.getText());
        } catch (NumberFormatException nfe){
            umur.setText("");
        }
    }//GEN-LAST:event_umurKeyReleased

    private void telpKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_telpKeyReleased
        try {
            no = Integer.parseInt(telp.getText());
        } catch (NumberFormatException nfe){
            telp.setText("");
        }
    }//GEN-LAST:event_telpKeyReleased

    private void simpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_simpanActionPerformed
        saveData();
    }//GEN-LAST:event_simpanActionPerformed

    private void batalkanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batalkanActionPerformed
        batalkanJanji();
    }//GEN-LAST:event_batalkanActionPerformed

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(menuPasien.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(menuPasien.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(menuPasien.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(menuPasien.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new menuPasien().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton batalkan;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> idD;
    private javax.swing.JComboBox<String> idP;
    private javax.swing.JTextField idPasien;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JRadioButton jkL;
    private javax.swing.JRadioButton jkP;
    private javax.swing.JButton logout;
    private javax.swing.JTextField nama;
    private javax.swing.JButton simpan;
    private javax.swing.JButton simpan2;
    private javax.swing.JTable tbjanji;
    private javax.swing.JTable tbpasien;
    private javax.swing.JTextField telp;
    private com.toedter.calendar.JDateChooser tgl;
    private javax.swing.JTextField umur;
    // End of variables declaration//GEN-END:variables
}
