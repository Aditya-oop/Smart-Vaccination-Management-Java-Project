import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class VaccinationManagementSystem {
    static final String DATA_FILE = "vaccination_data.dat";
    static AppData data = AppData.load();

    static final Color NAVY = new Color(18, 67, 96);
    static final Color BLUE = new Color(34, 117, 166);
    static final Color LIGHT = new Color(244, 248, 251);
    static final Color GREEN = new Color(45, 140, 95);
    static final Color RED = new Color(190, 70, 70);
    static final Font TITLE = new Font("Segoe UI", Font.BOLD, 24);
    static final Font HEAD = new Font("Segoe UI", Font.BOLD, 16);
    static final Font BODY_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }

    static void save() { data.save(); }

    static class LoginFrame extends JFrame {
        JTextField user = new JTextField();
        JPasswordField pass = new JPasswordField();

        LoginFrame() {
            setTitle("Smart Vaccination Management System - Login");
            setSize(470, 360);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setResizable(false);

            JPanel root = new JPanel(new BorderLayout());
            root.setBackground(LIGHT);

            JPanel top = new JPanel(new BorderLayout());
            top.setBackground(NAVY);
            top.setBorder(new EmptyBorder(22, 25, 22, 25));
            JLabel title = new JLabel("Smart Vaccination");
            title.setForeground(Color.WHITE);
            title.setFont(new Font("Segoe UI", Font.BOLD, 25));
            JLabel sub = new JLabel("Management System • SDG 3");
            sub.setForeground(new Color(220,235,245));
            sub.setFont(BODY_FONT);
            top.add(title, BorderLayout.NORTH);
            top.add(sub, BorderLayout.SOUTH);

            JPanel form = new JPanel(new GridBagLayout());
            form.setBackground(Color.WHITE);
            form.setBorder(new EmptyBorder(20, 35, 15, 35));
            GridBagConstraints g = new GridBagConstraints();
            g.insets = new Insets(8, 8, 8, 8);
            g.fill = GridBagConstraints.HORIZONTAL;
            g.gridx = 0; g.gridy = 0;
            form.add(label("Username"), g);
            g.gridx = 1; user.setPreferredSize(new Dimension(210, 32)); form.add(user, g);
            g.gridx = 0; g.gridy++;
            form.add(label("Password"), g);
            g.gridx = 1; pass.setPreferredSize(new Dimension(210, 32)); form.add(pass, g);

            JButton login = button("LOGIN", BLUE);
            g.gridx = 1; g.gridy++;
            form.add(login, g);

            JLabel hint = new JLabel("Demo login: admin / admin");
            hint.setForeground(Color.GRAY);
            hint.setFont(new Font("Segoe UI", Font.ITALIC, 12));
            g.gridy++;
            form.add(hint, g);

            login.addActionListener(e -> doLogin());
            pass.addActionListener(e -> doLogin());

            root.add(top, BorderLayout.NORTH);
            root.add(form, BorderLayout.CENTER);
            setContentPane(root);
        }

        void doLogin() {
            if (user.getText().equals("admin") && new String(pass.getPassword()).equals("admin")) {
                dispose();
                new MainFrame().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid login. Use admin / admin.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    static class MainFrame extends JFrame {
        JPanel content = new JPanel(new BorderLayout());
        JLabel totalPatients = statValue();
        JLabel totalVaccines = statValue();
        JLabel totalAppointments = statValue();
        JLabel fullyVaccinated = statValue();

        MainFrame() {
            setTitle("Smart Vaccination Management System");
            setSize(1180, 720);
            setMinimumSize(new Dimension(1000, 650));
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JPanel header = new JPanel(new BorderLayout());
            header.setBackground(NAVY);
            header.setBorder(new EmptyBorder(15, 22, 15, 22));
            JLabel h = new JLabel("Smart Vaccination Management System");
            h.setForeground(Color.WHITE);
            h.setFont(new Font("Segoe UI", Font.BOLD, 22));
            JLabel sdg = new JLabel("SDG 3 • Good Health & Well-being");
            sdg.setForeground(new Color(220,235,245));
            sdg.setFont(BODY_FONT);
            header.add(h, BorderLayout.WEST);
            header.add(sdg, BorderLayout.EAST);

            JPanel nav = new JPanel(new GridLayout(5, 1, 0, 8));
            nav.setBackground(new Color(29, 51, 67));
            nav.setBorder(new EmptyBorder(18, 12, 18, 12));
            JButton dash = navButton("Dashboard");
            JButton patients = navButton("Patients");
            JButton vaccines = navButton("Vaccinations");
            JButton appointments = navButton("Appointments");
            JButton exit = navButton("Exit");
            nav.add(dash); nav.add(patients); nav.add(vaccines); nav.add(appointments); nav.add(exit);

            dash.addActionListener(e -> showDashboard());
            patients.addActionListener(e -> showPatients());
            vaccines.addActionListener(e -> showVaccinations());
            appointments.addActionListener(e -> showAppointments());
            exit.addActionListener(e -> System.exit(0));

            content.setBackground(LIGHT);
            add(header, BorderLayout.NORTH);
            add(nav, BorderLayout.WEST);
            add(content, BorderLayout.CENTER);

            showDashboard();
        }

        JButton navButton(String text) {
            JButton b = new JButton(text);
            b.setForeground(Color.WHITE);
            b.setBackground(new Color(29,51,67));
            b.setBorderPainted(false);
            b.setFocusPainted(false);
            b.setFont(new Font("Segoe UI", Font.BOLD, 14));
            b.setHorizontalAlignment(SwingConstants.LEFT);
            b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            return b;
        }

        void setPage(JComponent c) {
            content.removeAll();
            content.add(c, BorderLayout.CENTER);
            content.revalidate();
            content.repaint();
        }

        void showDashboard() {
            JPanel p = new JPanel(new BorderLayout(15,15));
            p.setBackground(LIGHT);
            p.setBorder(new EmptyBorder(22,22,22,22));

            JPanel title = new JPanel(new BorderLayout());
            title.setOpaque(false);
            JLabel t = new JLabel("Dashboard");
            t.setFont(TITLE); t.setForeground(NAVY);
            JLabel d = new JLabel("Monitor patients, vaccination records and upcoming appointments.");
            d.setFont(BODY_FONT); d.setForeground(Color.DARK_GRAY);
            title.add(t, BorderLayout.NORTH); title.add(d, BorderLayout.SOUTH);

            JPanel cards = new JPanel(new GridLayout(1,4,15,0));
            cards.setOpaque(false);
            cards.add(card("Registered Patients", totalPatients, BLUE));
            cards.add(card("Vaccination Records", totalVaccines, GREEN));
            cards.add(card("Appointments", totalAppointments, new Color(145,95,170)));
            cards.add(card("Fully Vaccinated", fullyVaccinated, new Color(205,130,45)));

            JPanel info = new JPanel(new BorderLayout());
            info.setBackground(Color.WHITE);
            info.setBorder(new CompoundBorder(new LineBorder(new Color(220,225,230)), new EmptyBorder(18,20,18,20)));
            JLabel it = new JLabel("System Overview");
            it.setFont(HEAD); it.setForeground(NAVY);
            JTextArea ta = new JTextArea(
                "Purpose: Digitally manage vaccination information in one place.\n\n" +
                "Key functions:\n" +
                "• Register and search patients\n" +
                "• Record vaccine doses and dates\n" +
                "• Schedule vaccination appointments\n" +
                "• Track vaccination status\n" +
                "• Store data locally for later use\n\n" +
                "Sustainable Development Goal: SDG 3 – Good Health and Well-being"
            );
            ta.setEditable(false);
            ta.setFont(BODY_FONT);
            ta.setBackground(Color.WHITE);
            ta.setForeground(new Color(60,60,60));
            ta.setBorder(new EmptyBorder(12,0,0,0));
            info.add(it, BorderLayout.NORTH);
            info.add(ta, BorderLayout.CENTER);

            JPanel center = new JPanel(new BorderLayout(0,15));
            center.setOpaque(false);
            center.add(cards, BorderLayout.NORTH);
            center.add(info, BorderLayout.CENTER);

            p.add(title, BorderLayout.NORTH);
            p.add(center, BorderLayout.CENTER);
            refreshStats();
            setPage(p);
        }

        JPanel card(String name, JLabel value, Color c) {
            JPanel x = new JPanel(new BorderLayout());
            x.setBackground(Color.WHITE);
            x.setBorder(new CompoundBorder(new LineBorder(new Color(220,225,230)), new EmptyBorder(16,18,16,18)));
            JLabel n = new JLabel(name);
            n.setFont(BODY_FONT); n.setForeground(Color.GRAY);
            value.setForeground(c);
            x.add(n, BorderLayout.NORTH);
            x.add(value, BorderLayout.CENTER);
            return x;
        }

        JLabel statValue() {
            JLabel x = new JLabel("0");
            x.setFont(new Font("Segoe UI", Font.BOLD, 30));
            return x;
        }

        void refreshStats() {
            totalPatients.setText(String.valueOf(data.patients.size()));
            totalVaccines.setText(String.valueOf(data.vaccinations.size()));
            totalAppointments.setText(String.valueOf(data.appointments.size()));
            Set<String> vaccinated = new HashSet<>();
            for (Vaccination v : data.vaccinations) vaccinated.add(v.patientId);
            fullyVaccinated.setText(String.valueOf(vaccinated.size()));
        }

        void showPatients() {
            JPanel p = new JPanel(new BorderLayout(10,10));
            p.setBackground(LIGHT);
            p.setBorder(new EmptyBorder(20,20,20,20));

            JPanel top = new JPanel(new BorderLayout(10,10));
            top.setOpaque(false);
            JLabel title = new JLabel("Patient Management");
            title.setFont(TITLE); title.setForeground(NAVY);
            JButton add = button("+ Add Patient", GREEN);
            top.add(title, BorderLayout.WEST); top.add(add, BorderLayout.EAST);

            JTextField search = new JTextField();
            search.setPreferredSize(new Dimension(250,32));
            search.setToolTipText("Search by ID, name or phone");
            JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,5,0));
            searchPanel.setOpaque(false);
            searchPanel.add(new JLabel("Search:"));
            searchPanel.add(search);

            String[] cols = {"Patient ID","Name","Age","Gender","Phone","City"};
            DefaultTableModel model = new DefaultTableModel(cols,0) { public boolean isCellEditable(int r,int c){return false;} };
            JTable table = new JTable(model);
            styleTable(table);
            JScrollPane sp = new JScrollPane(table);

            Runnable refresh = () -> {
                model.setRowCount(0);
                String q = search.getText().trim().toLowerCase();
                for (Patient x : data.patients) {
                    String row = (x.id+" "+x.name+" "+x.phone+" "+x.city).toLowerCase();
                    if (q.isEmpty() || row.contains(q))
                        model.addRow(new Object[]{x.id,x.name,x.age,x.gender,x.phone,x.city});
                }
            };
            refresh.run();
            search.getDocument().addDocumentListener(new SimpleDocumentListener(refresh));

            JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            actions.setOpaque(false);
            JButton delete = button("Delete Selected", RED);
            actions.add(delete);

            add.addActionListener(e -> addPatient(refresh));
            delete.addActionListener(e -> {
                int r = table.getSelectedRow();
                if (r < 0) { msg("Select a patient first."); return; }
                String id = String.valueOf(model.getValueAt(r,0));
                int ok = JOptionPane.showConfirmDialog(this,"Delete patient "+id+"?","Confirm",JOptionPane.YES_NO_OPTION);
                if(ok == JOptionPane.YES_OPTION){
                    data.patients.removeIf(x -> x.id.equals(id));
                    data.vaccinations.removeIf(x -> x.patientId.equals(id));
                    data.appointments.removeIf(x -> x.patientId.equals(id));
                    save(); refresh.run(); refreshStats();
                }
            });

            JPanel north = new JPanel(new BorderLayout(0,12));
            north.setOpaque(false);
            north.add(top, BorderLayout.NORTH);
            north.add(searchPanel, BorderLayout.SOUTH);
            p.add(north, BorderLayout.NORTH);
            p.add(sp, BorderLayout.CENTER);
            p.add(actions, BorderLayout.SOUTH);
            setPage(p);
        }

        void addPatient(Runnable refresh) {
            JTextField id = new JTextField();
            JTextField name = new JTextField();
            JTextField age = new JTextField();
            JComboBox<String> gender = new JComboBox<>(new String[]{"Male","Female","Other"});
            JTextField phone = new JTextField();
            JTextField city = new JTextField();

            JPanel f = formPanel();
            addField(f,"Patient ID *",id);
            addField(f,"Full Name *",name);
            addField(f,"Age *",age);
            addField(f,"Gender",gender);
            addField(f,"Phone",phone);
            addField(f,"City",city);

            int r = JOptionPane.showConfirmDialog(this,f,"Register New Patient",JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);
            if(r==JOptionPane.OK_OPTION){
                try {
                    if(id.getText().trim().isEmpty() || name.getText().trim().isEmpty()) throw new Exception("ID and name are required.");
                    int a = Integer.parseInt(age.getText().trim());
                    if(a<0 || a>120) throw new Exception("Enter a valid age.");
                    if(data.findPatient(id.getText().trim())!=null) throw new Exception("Patient ID already exists.");
                    data.patients.add(new Patient(id.getText().trim(),name.getText().trim(),a,
                            String.valueOf(gender.getSelectedItem()),phone.getText().trim(),city.getText().trim()));
                    save(); refresh.run(); refreshStats(); msg("Patient registered successfully.");
                } catch(Exception ex) { msg("Could not save patient: "+ex.getMessage()); }
            }
        }

        void showVaccinations() {
            JPanel p = new JPanel(new BorderLayout(10,10));
            p.setBackground(LIGHT); p.setBorder(new EmptyBorder(20,20,20,20));
            JPanel top = new JPanel(new BorderLayout());
            top.setOpaque(false);
            JLabel title = new JLabel("Vaccination Records");
            title.setFont(TITLE); title.setForeground(NAVY);
            JButton add = button("+ Record Vaccination", GREEN);
            top.add(title,BorderLayout.WEST); top.add(add,BorderLayout.EAST);

            String[] cols={"Record ID","Patient ID","Patient Name","Vaccine","Dose","Date","Next Due"};
            DefaultTableModel model=new DefaultTableModel(cols,0){public boolean isCellEditable(int r,int c){return false;}};
            JTable table=new JTable(model); styleTable(table);
            Runnable refresh=()->{
                model.setRowCount(0);
                for(Vaccination v:data.vaccinations){
                    Patient pt=data.findPatient(v.patientId);
                    model.addRow(new Object[]{v.recordId,v.patientId,pt==null?"Unknown":pt.name,v.vaccine,v.dose,v.date,v.nextDue});
                }
            };
            refresh.run();
            add.addActionListener(e->addVaccination(refresh));
            p.add(top,BorderLayout.NORTH); p.add(new JScrollPane(table),BorderLayout.CENTER);
            setPage(p);
        }

        void addVaccination(Runnable refresh) {
            JComboBox<String> patient = new JComboBox<>();
            for(Patient p:data.patients) patient.addItem(p.id+" - "+p.name);
            if(data.patients.isEmpty()){ msg("Please register a patient first."); return; }
            JComboBox<String> vaccine = new JComboBox<>(new String[]{
                "BCG","Polio","Hepatitis B","DTP","MMR","COVID-19","Influenza","Typhoid","HPV","Other"
            });
            JComboBox<String> dose = new JComboBox<>(new String[]{"Dose 1","Dose 2","Dose 3","Booster"});
            JTextField date = new JTextField(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
            JTextField next = new JTextField();

            JPanel f=formPanel();
            addField(f,"Patient",patient); addField(f,"Vaccine",vaccine); addField(f,"Dose",dose);
            addField(f,"Date",date); addField(f,"Next Due Date",next);
            int r=JOptionPane.showConfirmDialog(this,f,"Record Vaccination",JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);
            if(r==JOptionPane.OK_OPTION){
                String pid=String.valueOf(patient.getSelectedItem()).split(" - ",2)[0];
                String rid="VR-"+(1000+data.vaccinations.size()+1);
                data.vaccinations.add(new Vaccination(rid,pid,String.valueOf(vaccine.getSelectedItem()),
                        String.valueOf(dose.getSelectedItem()),date.getText().trim(),next.getText().trim()));
                save(); refresh.run(); refreshStats(); msg("Vaccination record saved.");
            }
        }

        void showAppointments() {
            JPanel p=new JPanel(new BorderLayout(10,10));
            p.setBackground(LIGHT); p.setBorder(new EmptyBorder(20,20,20,20));
            JPanel top=new JPanel(new BorderLayout());
            top.setOpaque(false);
            JLabel title=new JLabel("Appointment Management");
            title.setFont(TITLE); title.setForeground(NAVY);
            JButton add=button("+ Schedule Appointment",GREEN);
            top.add(title,BorderLayout.WEST); top.add(add,BorderLayout.EAST);

            String[] cols={"Appointment ID","Patient ID","Patient Name","Vaccine","Date","Time","Status"};
            DefaultTableModel model=new DefaultTableModel(cols,0){public boolean isCellEditable(int r,int c){return false;}};
            JTable table=new JTable(model); styleTable(table);
            Runnable refresh=()->{
                model.setRowCount(0);
                for(Appointment a:data.appointments){
                    Patient pt=data.findPatient(a.patientId);
                    model.addRow(new Object[]{a.id,a.patientId,pt==null?"Unknown":pt.name,a.vaccine,a.date,a.time,a.status});
                }
            };
            refresh.run();
            add.addActionListener(e->addAppointment(refresh));
            p.add(top,BorderLayout.NORTH); p.add(new JScrollPane(table),BorderLayout.CENTER);
            setPage(p);
        }

        void addAppointment(Runnable refresh) {
            if(data.patients.isEmpty()){msg("Please register a patient first.");return;}
            JComboBox<String> patient=new JComboBox<>();
            for(Patient p:data.patients) patient.addItem(p.id+" - "+p.name);
            JComboBox<String> vaccine=new JComboBox<>(new String[]{"BCG","Polio","Hepatitis B","DTP","MMR","COVID-19","Influenza","Typhoid","HPV","Other"});
            JTextField date=new JTextField();
            JTextField time=new JTextField();
            JComboBox<String> status=new JComboBox<>(new String[]{"Scheduled","Completed","Cancelled"});

            JPanel f=formPanel();
            addField(f,"Patient",patient); addField(f,"Vaccine",vaccine); addField(f,"Date",date);
            addField(f,"Time",time); addField(f,"Status",status);
            int r=JOptionPane.showConfirmDialog(this,f,"Schedule Appointment",JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);
            if(r==JOptionPane.OK_OPTION){
                String pid=String.valueOf(patient.getSelectedItem()).split(" - ",2)[0];
                String id="APT-"+(1000+data.appointments.size()+1);
                data.appointments.add(new Appointment(id,pid,String.valueOf(vaccine.getSelectedItem()),
                        date.getText().trim(),time.getText().trim(),String.valueOf(status.getSelectedItem())));
                save(); refresh.run(); refreshStats(); msg("Appointment scheduled.");
            }
        }
    }

    static JPanel formPanel(){
        JPanel p=new JPanel(new GridLayout(0,2,10,10));
        p.setBorder(new EmptyBorder(10,10,10,10));
        p.setBackground(Color.WHITE);
        return p;
    }
    static void addField(JPanel p,String label,JComponent c){
        JLabel l=new JLabel(label); l.setFont(BODY_FONT);
        c.setPreferredSize(new Dimension(220,30));
        p.add(l); p.add(c);
    }
    static JLabel label(String s){ JLabel l=new JLabel(s); l.setFont(BODY_FONT); return l; }
    static JButton button(String text,Color bg){
        JButton b=new JButton(text); b.setBackground(bg); b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI",Font.BOLD,13)); b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(9,15,9,15)); return b;
    }
    static void styleTable(JTable t){
        t.setRowHeight(30); t.setFont(BODY_FONT); t.getTableHeader().setFont(new Font("Segoe UI",Font.BOLD,13));
        t.getTableHeader().setBackground(NAVY); t.getTableHeader().setForeground(Color.WHITE);
        t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        t.setGridColor(new Color(225,225,225));
    }
    static void msg(String s){ JOptionPane.showMessageDialog(null,s,"Smart Vaccination System",JOptionPane.INFORMATION_MESSAGE); }

    static class Patient implements Serializable{
        String id,name,gender,phone,city; int age;
        Patient(String i,String n,int a,String g,String ph,String c){id=i;name=n;age=a;gender=g;phone=ph;city=c;}
    }
    static class Vaccination implements Serializable{
        String recordId,patientId,vaccine,dose,date,nextDue;
        Vaccination(String r,String p,String v,String d,String dt,String nd){recordId=r;patientId=p;vaccine=v;dose=d;date=dt;nextDue=nd;}
    }
    static class Appointment implements Serializable{
        String id,patientId,vaccine,date,time,status;
        Appointment(String i,String p,String v,String d,String t,String s){id=i;patientId=p;vaccine=v;date=d;time=t;status=s;}
    }
    static class AppData implements Serializable{
        ArrayList<Patient> patients=new ArrayList<>();
        ArrayList<Vaccination> vaccinations=new ArrayList<>();
        ArrayList<Appointment> appointments=new ArrayList<>();
        Patient findPatient(String id){for(Patient p:patients)if(p.id.equals(id))return p;return null;}
        void save(){try(ObjectOutputStream o=new ObjectOutputStream(new FileOutputStream(DATA_FILE))){o.writeObject(this);}catch(Exception e){e.printStackTrace();}}
        static AppData load(){try(ObjectInputStream o=new ObjectInputStream(new FileInputStream(DATA_FILE))){return (AppData)o.readObject();}catch(Exception e){return new AppData();}}
    }
    static class SimpleDocumentListener implements javax.swing.event.DocumentListener{
        Runnable r; SimpleDocumentListener(Runnable x){r=x;}
        public void insertUpdate(javax.swing.event.DocumentEvent e){r.run();}
        public void removeUpdate(javax.swing.event.DocumentEvent e){r.run();}
        public void changedUpdate(javax.swing.event.DocumentEvent e){r.run();}
    }
}
