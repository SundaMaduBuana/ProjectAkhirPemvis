/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package projectpemvis;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

public class dokter extends javax.swing.JFrame {

    Connection conn; //mendapatkan koneksi database
    private DefaultTableModel model_tbl_dokter;
    private DefaultTableModel model_tbl_janji;
    int no;

    public dokter() {
        initComponents();
        setLocationRelativeTo(null);
        conn = koneksi.getConnection();
        model_tbl_dokter = new DefaultTableModel(); // tabel dokter
        tbdokter.setModel(model_tbl_dokter);
        model_tbl_dokter.addColumn("ID");
        model_tbl_dokter.addColumn("Nama");
        model_tbl_dokter.addColumn("Spesialis");
        model_tbl_dokter.addColumn("No telp");
        model_tbl_dokter.addColumn("Email");
        model_tbl_dokter.addColumn("Kode dokter");

        loadDatadokter();
        setButtondokter(); // Set button dokter
        Listenerdokter(); // mengatur status tombol dokter berdasarkan input
        tbdokter.setDefaultEditor(Object.class, null);

        // janji
        model_tbl_janji = new DefaultTableModel();
        tbjanji.setModel(model_tbl_janji);
        model_tbl_janji.addColumn("ID");
        model_tbl_janji.addColumn("ID Pasien");
        model_tbl_janji.addColumn("ID Dokter");
        model_tbl_janji.addColumn("Tanggal");
        model_tbl_janji.addColumn("Status");
        loadDatajanji();
        tbjanji.setDefaultEditor(Object.class, null);
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
        tbjanji.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                int selectedRow = tbjanji.getSelectedRow();
                if (selectedRow != -1) {
                    int id = (int) tbjanji.getValueAt(selectedRow, 0);
                    String idPasien = (String) tbjanji.getValueAt(selectedRow, 1);
                    String idDokter = (String) tbjanji.getValueAt(selectedRow, 2);
                    String tanggal = (String) tbjanji.getValueAt(selectedRow, 3);
                    String status = (String) tbjanji.getValueAt(selectedRow, 4);

                    // Tampilkan informasi janji yang dipilih
                    JOptionPane.showMessageDialog(this, "ID: " + id + "\n"
                            + "ID Pasien: " + idPasien + "\n"
                            + "ID Dokter: " + idDokter + "\n"
                            + "Tanggal: " + tanggal + "\n"
                            + "Status: " + status);
                }
            }
        });
        terima.addActionListener(event -> {
            int selectedRow = tbjanji.getSelectedRow();
            if (selectedRow != -1) {
                int id = (int) tbjanji.getValueAt(selectedRow, 0);
                try {
                    // Update status menjadi "Diterima"
                    String sql = "UPDATE daftar_janji SET status = 'Diterima' WHERE id = ?";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setInt(1, id);
                    ps.executeUpdate();

                    // Refresh data tabel janji
                    loadDatajanji();

                    JOptionPane.showMessageDialog(this, "Janji telah diterima.");
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(this, "Terjadi kesalahan: " + e.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(this, "Silakan pilih baris yang akan diterima.");
            }
        });
        
        tolak.addActionListener(event -> {
            int selectedRow = tbjanji.getSelectedRow();
            if (selectedRow != -1) {
                int id = (int) tbjanji.getValueAt(selectedRow, 0);
                try {
                    // Update status menjadi "Ditolak"
                    String sql = "UPDATE daftar_janji SET status = 'Ditolak' WHERE id = ?";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setInt(1, id);
                    ps.executeUpdate();

                    // Refresh data tabel janji
                    loadDatajanji();

                    JOptionPane.showMessageDialog(this, "Janji telah ditolak.");
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(this, "Terjadi kesalahan: " + e.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(this, "Silakan pilih baris yang akan diterima.");
            }
        });
        
        selesai.addActionListener(event -> {
            int selectedRow = tbjanji.getSelectedRow();
            if (selectedRow != -1) {
                int id = (int) tbjanji.getValueAt(selectedRow, 0);
                try {
                    // Update status menjadi "selesai"
                    String sql = "UPDATE daftar_janji SET status = 'Selesai melakukan janji temu' WHERE id = ?";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setInt(1, id);
                    ps.executeUpdate();

                    // Refresh data tabel janji
                    loadDatajanji();

                    JOptionPane.showMessageDialog(this, "Janji telah selesai.");
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(this, "Terjadi kesalahan: " + e.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(this, "Silakan pilih baris yang akan diterima.");
            }
        });
    }

    private void loadDatadokter() {
        model_tbl_dokter.setRowCount(0);
        try {
            String sql = "SELECT * FROM dokter";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet hasil = ps.executeQuery();
            while (hasil.next()) {
                // Menambahkan baris ke dalam model tabel
                model_tbl_dokter.addRow(new Object[]{
                    hasil.getInt("id"),
                    hasil.getString("nama"),
                    hasil.getString("spesialis"),
                    hasil.getString("no_telp"),
                    hasil.getString("email"),
                    hasil.getString("kode_dokter")
                });
            }
        } catch (SQLException e) {
            System.out.println("Error Save Data" + e.getMessage());
        }
    }

    private void saveDatadokter() {
        try {
            String sql = "INSERT INTO dokter (nama, spesialis, no_telp, email, kode_dokter) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, namaD.getText());
            ps.setString(2, spesialis.getSelectedItem().toString());
            ps.setString(3, telpD.getText());
            ps.setString(4, emailD.getText());
            ps.setString(5, kodeD.getText());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data berhasil disimpan");
            namaD.setText("");
            spesialis.setSelectedIndex(0); // Mengatur combo box kembali ke item pertama
            telpD.setText("");
            emailD.setText("");
            kodeD.setText("");
            ID.setText("");
            loadDatadokter();
            setButtondokter(); // Update button state
        } catch (SQLException e) {
            System.out.println("Error Save Data: " + e.getMessage());
        }
    }

    private void updateDatadokter() {
        try {
            String sql = "UPDATE dokter SET nama = ?, spesialis = ?, no_telp = ?, email = ?, kode_dokter = ? WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, namaD.getText());
            ps.setString(2, spesialis.getSelectedItem().toString());
            ps.setString(3, telpD.getText());
            ps.setString(4, emailD.getText());
            ps.setString(5, kodeD.getText());
            ps.setInt(6, Integer.parseInt(ID.getText()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data berhasil diperbarui");
            namaD.setText("");
            spesialis.setSelectedIndex(0); // Mengatur combo box kembali ke item pertama
            telpD.setText("");
            emailD.setText("");
            kodeD.setText("");
            ID.setText("");
            loadDatadokter();
            setButtondokter(); // Update button state
        } catch (SQLException e) {
            System.out.println("Error Update Data: " + e.getMessage());
        }
    }

    private void deleteDatadokter() {
        try {
            String sql = "DELETE FROM dokter WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(ID.getText()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data berhasil dihapus");
            namaD.setText("");
            spesialis.setSelectedIndex(0); // Mengatur combo box kembali ke item pertama
            telpD.setText("");
            emailD.setText("");
            kodeD.setText("");
            ID.setText("");
            loadDatadokter();
            setButtondokter(); // Update button state
        } catch (SQLException e) {
            System.out.println("Error Delete Data: " + e.getMessage());
        }
    }

    private void setButtondokter() {
        // Mengatur status tombol berdasarkan isi text field
        boolean isIdFilled = !ID.getText().trim().isEmpty();
        boolean isAllFilled = !namaD.getText().trim().isEmpty()
                && spesialis.getSelectedItem() != null
                && !telpD.getText().trim().isEmpty() && !emailD.getText().trim().isEmpty()
                && !kodeD.getText().trim().isEmpty();

        // nonaktifkan button kl semuanya kosong
        if (!isIdFilled && !isAllFilled) {
            simpan.setEnabled(false);
            edit.setEnabled(false);
            hapus.setEnabled(false);
        } else {
            simpan.setEnabled(!isIdFilled); // Simpan hanya aktif jika ID kosong
            edit.setEnabled(isIdFilled && isAllFilled); // Update hanya aktif jika semua field terisi
            hapus.setEnabled(isIdFilled && !isAllFilled); // Delete hanya aktif jika ID terisi
        }
    }

    private void Listenerdokter() {
        // Menambahkan DocumentListener untuk memantau perubahan pada text field
        DocumentListener Dokter = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                setButtondokter();
            }

            public void removeUpdate(DocumentEvent e) {
                setButtondokter();
            }

            public void changedUpdate(DocumentEvent e) {
                setButtondokter();
            }
        };

        ID.getDocument().addDocumentListener(Dokter);
        namaD.getDocument().addDocumentListener(Dokter);
        spesialis.addActionListener(e -> setButtondokter());
        telpD.getDocument().addDocumentListener(Dokter);
        kodeD.getDocument().addDocumentListener(Dokter);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        ID = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        emailD = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        telpD = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        kodeD = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        namaD = new javax.swing.JTextField();
        spesialis = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        simpan = new javax.swing.JButton();
        edit = new javax.swing.JButton();
        hapus = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbdokter = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbjanji = new javax.swing.JTable();
        terima = new javax.swing.JButton();
        tolak = new javax.swing.JButton();
        selesai = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        logout = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(179, 200, 207));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Perpetua Titling MT", 1, 20)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("selamat datang di halaman dokter");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 20, -1, -1));

        jTabbedPane1.setBackground(new java.awt.Color(120, 157, 188));

        jPanel2.setBackground(new java.awt.Color(137, 168, 178));

        jPanel6.setBackground(new java.awt.Color(137, 168, 178));
        jPanel6.setLayout(new java.awt.GridBagLayout());

        jLabel2.setFont(new java.awt.Font("Segoe UI Black", 1, 15)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("ID :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel6.add(jLabel2, gridBagConstraints);

        ID.setBackground(new java.awt.Color(229, 225, 218));
        ID.setPreferredSize(new java.awt.Dimension(150, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel6.add(ID, gridBagConstraints);

        jLabel3.setFont(new java.awt.Font("Segoe UI Black", 1, 15)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Spesialis :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel6.add(jLabel3, gridBagConstraints);

        emailD.setBackground(new java.awt.Color(229, 225, 218));
        emailD.setPreferredSize(new java.awt.Dimension(150, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel6.add(emailD, gridBagConstraints);

        jLabel4.setFont(new java.awt.Font("Segoe UI Black", 1, 15)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("No telp :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel6.add(jLabel4, gridBagConstraints);

        jLabel5.setFont(new java.awt.Font("Segoe UI Black", 1, 15)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Email :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel6.add(jLabel5, gridBagConstraints);

        telpD.setBackground(new java.awt.Color(229, 225, 218));
        telpD.setPreferredSize(new java.awt.Dimension(150, 25));
        telpD.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                telpDKeyReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel6.add(telpD, gridBagConstraints);

        jLabel8.setFont(new java.awt.Font("Segoe UI Black", 1, 15)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Kode Dokter :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel6.add(jLabel8, gridBagConstraints);

        kodeD.setBackground(new java.awt.Color(229, 225, 218));
        kodeD.setPreferredSize(new java.awt.Dimension(150, 25));
        kodeD.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                kodeDKeyReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel6.add(kodeD, gridBagConstraints);

        jLabel7.setFont(new java.awt.Font("Segoe UI Black", 1, 15)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Nama :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel6.add(jLabel7, gridBagConstraints);

        namaD.setBackground(new java.awt.Color(229, 225, 218));
        namaD.setPreferredSize(new java.awt.Dimension(150, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel6.add(namaD, gridBagConstraints);

        spesialis.setBackground(new java.awt.Color(229, 225, 218));
        spesialis.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "pilih", "jantung", "anak", "gigi", "mata", "kulit" }));
        spesialis.setPreferredSize(new java.awt.Dimension(150, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel6.add(spesialis, gridBagConstraints);

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/projectpemvis/img6.png"))); // NOI18N

        simpan.setBackground(new java.awt.Color(120, 157, 188));
        simpan.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        simpan.setForeground(new java.awt.Color(255, 255, 255));
        simpan.setText("Simpan");
        simpan.setPreferredSize(new java.awt.Dimension(90, 30));
        simpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                simpanActionPerformed(evt);
            }
        });

        edit.setBackground(new java.awt.Color(120, 157, 188));
        edit.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        edit.setForeground(new java.awt.Color(255, 255, 255));
        edit.setText("Edit");
        edit.setPreferredSize(new java.awt.Dimension(90, 30));
        edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editActionPerformed(evt);
            }
        });

        hapus.setBackground(new java.awt.Color(120, 157, 188));
        hapus.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        hapus.setForeground(new java.awt.Color(255, 255, 255));
        hapus.setText("Hapus");
        hapus.setPreferredSize(new java.awt.Dimension(90, 30));
        hapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hapusActionPerformed(evt);
            }
        });

        tbdokter.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nama", "Spesialis", "No telp", "Email", "Kode dokter"
            }
        ));
        jScrollPane1.setViewportView(tbdokter);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(simpan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(edit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(hapus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel6))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 647, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(11, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(simpan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(edit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(hapus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Dokter", jPanel2);

        jPanel3.setBackground(new java.awt.Color(137, 168, 178));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tbjanji.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "ID Pasien", "ID Dokter", "Tanggal", "Status"
            }
        ));
        jScrollPane2.setViewportView(tbjanji);

        jPanel3.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(17, 17, 636, 260));

        terima.setBackground(new java.awt.Color(120, 157, 188));
        terima.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        terima.setForeground(new java.awt.Color(255, 255, 255));
        terima.setText("Terima");
        terima.setPreferredSize(new java.awt.Dimension(85, 30));
        terima.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                terimaActionPerformed(evt);
            }
        });
        jPanel3.add(terima, new org.netbeans.lib.awtextra.AbsoluteConstraints(362, 295, -1, -1));

        tolak.setBackground(new java.awt.Color(120, 157, 188));
        tolak.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        tolak.setForeground(new java.awt.Color(255, 255, 255));
        tolak.setText("Tolak");
        tolak.setPreferredSize(new java.awt.Dimension(85, 30));
        jPanel3.add(tolak, new org.netbeans.lib.awtextra.AbsoluteConstraints(465, 295, -1, -1));

        selesai.setBackground(new java.awt.Color(120, 157, 188));
        selesai.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        selesai.setForeground(new java.awt.Color(255, 255, 255));
        selesai.setText("Selesai");
        selesai.setPreferredSize(new java.awt.Dimension(85, 30));
        jPanel3.add(selesai, new org.netbeans.lib.awtextra.AbsoluteConstraints(568, 295, -1, -1));

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/projectpemvis/img8.png"))); // NOI18N
        jPanel3.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 180, -1, -1));

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/projectpemvis/img8.png"))); // NOI18N
        jPanel3.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(-10, 260, -1, -1));

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/projectpemvis/img8.png"))); // NOI18N
        jPanel3.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 230, -1, -1));

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/projectpemvis/img8.png"))); // NOI18N
        jPanel3.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 230, -1, -1));

        jTabbedPane1.addTab("Daftar janji", jPanel3);

        jPanel1.add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 670, 420));

        logout.setBackground(new java.awt.Color(120, 157, 188));
        logout.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        logout.setForeground(new java.awt.Color(255, 255, 255));
        logout.setText("Logout");
        logout.setPreferredSize(new java.awt.Dimension(90, 30));
        logout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutActionPerformed(evt);
            }
        });
        jPanel1.add(logout, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 490, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 709, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 535, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void kodeDKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kodeDKeyReleased
        try {
            no = Integer.parseInt(kodeD.getText());
        } catch (NumberFormatException nfe) {
            kodeD.setText("");
        }
    }//GEN-LAST:event_kodeDKeyReleased

    private void telpDKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_telpDKeyReleased
        try {
            no = Integer.parseInt(telpD.getText());
        } catch (NumberFormatException nfe) {
            telpD.setText("");
        }
    }//GEN-LAST:event_telpDKeyReleased

    private void simpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_simpanActionPerformed
        saveDatadokter();
    }//GEN-LAST:event_simpanActionPerformed

    private void editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editActionPerformed
        updateDatadokter();
    }//GEN-LAST:event_editActionPerformed

    private void hapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hapusActionPerformed
        deleteDatadokter();
    }//GEN-LAST:event_hapusActionPerformed

    private void logoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutActionPerformed
        menuLogin logout = new menuLogin();
        logout.setVisible(true);
        dispose(); // Menutup halaman utama
    }//GEN-LAST:event_logoutActionPerformed

    private void terimaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_terimaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_terimaActionPerformed

    /**
     * @param args the command line arguments
     */
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
            java.util.logging.Logger.getLogger(dokter.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(dokter.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(dokter.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(dokter.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new dokter().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField ID;
    private javax.swing.JButton edit;
    private javax.swing.JTextField emailD;
    private javax.swing.JButton hapus;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
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
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField kodeD;
    private javax.swing.JButton logout;
    private javax.swing.JTextField namaD;
    private javax.swing.JButton selesai;
    private javax.swing.JButton simpan;
    private javax.swing.JComboBox<String> spesialis;
    private javax.swing.JTable tbdokter;
    private javax.swing.JTable tbjanji;
    private javax.swing.JTextField telpD;
    private javax.swing.JButton terima;
    private javax.swing.JButton tolak;
    // End of variables declaration//GEN-END:variables
}
