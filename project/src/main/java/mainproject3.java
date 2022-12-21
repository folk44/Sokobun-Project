/*
6313126 Chayawat Nilsumrit
6313210 Krittawat Thongnoppakao
6313138 Yutthasat Phoncharoenpong
6313212 Chatrawit Chatnawakul
 */

import java.awt.*;
import java.util.List;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JComboBox.*;
import javax.swing.event.*;
import javax.swing.border.EmptyBorder;
import javax.naming.ContextNotEmptyException;
import javax.sound.sampled.*;     // for sounds
import java.io.PrintWriter;
import java.io.IOException;

import java.util.*;
import java.util.ArrayList;
import java.io.*;
import java.io.File;

import javax.swing.text.PlainDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;

public class mainproject3 extends JFrame{
    
    private int frameWidth = 800, frameHeight = 600;
    private long time, lastTime;
    private LoginMenu login;
    private ArrayList<String> IDList;
    private ArrayList<Integer> LevelList;
    private int indexAcc;
    private Menu menu;
    private LevelMenu levelmenu;
    private Credit credit;
    private int current;
    private Setting setting;
    private Boolean PMusic, PFx;
    private int VMusic, VFx;
    private MySoundEffect themesound, hitsound;
    private String Charactor_Shape = "circle";
    private String Charactor_Color = "Blue";
    
    public mainproject3(){
        setTitle("Sokoban");
        setBounds(50, 50, frameWidth, frameHeight);
        setVisible(true);
        setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );
        requestFocus();
        setFocusable(true);

        login = new LoginMenu(this);
        levelmenu = new LevelMenu(this, 0);
        menu = new Menu(this);
        setting = new Setting(this);
        credit = new Credit(this);
        themesound = new MySoundEffect("resources/theme.wav");
        hitsound   = new MySoundEffect("resources/beep.wav");
        IDList = new ArrayList<String>();
        LevelList = new ArrayList<Integer>();

        changeSetting(true, 80, true, 80);

        addWindowListener( new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                if(IDList.size() != 0) save(current);
                themesound.stop();           
            }
        });

        setContentPane(login);

        addComponentListener(new ComponentAdapter(){
            public void componentResized(ComponentEvent e){
                int width = getContentPane().getWidth();
                int height = getContentPane().getHeight();
                menu.resizeBG(width, height);
                levelmenu.resizeBG(width, height);
                login.resizeBG(width, height);
            }
        });
        validate();
    }
    public LoginMenu getLogin(){
        return login;
    }
    
    public void setAccount(){
        IDList=login.getIDList();
        LevelList = login.getLevelList();
        indexAcc = login.getIndexLogin();
        current = LevelList.get(indexAcc);
        levelmenu = new LevelMenu(this, current);
    }
    
    public void save(int level){
        //change level
        LevelList.set(indexAcc,level);
        //write File
        try {
            File inputFile = new File("Account/ListID.txt");
            FileWriter fileWriter = new FileWriter(inputFile);
            BufferedWriter bw = new BufferedWriter(fileWriter);
            PrintWriter printWriter = new PrintWriter(bw);
            Iterator i = IDList.iterator();
            Iterator j = LevelList.iterator();
            while(i.hasNext() && j.hasNext()){
                printWriter.print(i.next());
                printWriter.print("->");
                printWriter.println(j.next());
            }
            printWriter.close();
        } catch (IOException ex) {}
        login = new LoginMenu(this);
    }
    
    public int getCurrentLevel(){
        return current;
    }
    public Menu getMenu(){
        return menu;
    }
    public void setLevelMenu(int tcurr){
        if(tcurr > current) current = tcurr;
        levelmenu = new LevelMenu(this, current);
    }
    public void setCurrentLevel(int tcurr){
        current = tcurr;
    }
    public LevelMenu getLevelMenu(){
        return levelmenu;
    }
    public Setting getSetting(){
        return setting;
    }
    public Credit getCredit(){
        return credit;
    }
    public Boolean getPMusic(){
        return PMusic;
    }
    public Boolean getPFx(){
        return PFx;
    }
    public int getVMusic(){
        return VMusic;
    }
    public int getVFx(){
        return VFx;
    }

    public void changeSetting(Boolean m, int vm, Boolean f, int vf){
        PMusic = m; PFx = f;
        VMusic = vm; VFx = vf;
        if(PMusic){
            themesound.changeVolume(VMusic);
            themesound.playLoop();
        }
        else{
            themesound.stop();
        }
    }

    public void walkSound(){
        if(PFx){
            hitsound.playOnce();
        }
    }
    public void ChangeChar_Shape(String Shape){
        Charactor_Shape = Shape;
    }
    public void ChangeChar_Color(String Color){
        Charactor_Color = Color;
    }
    public String getCharactorShape(){
        return Charactor_Shape;
    }
    public String getCharactor(){
        return Charactor_Shape+"_"+Charactor_Color;
    }
    public static void main(String[] args) {
        new mainproject3();
    } 
}

class Menu extends JLabel{
    mainproject3 frame;
    public Menu(mainproject3 tframe){
        frame = tframe;
        setIcon(new MyImageIcon("resources/BG/BG_Cover.png").resize(frame.getWidth(), frame.getHeight()));
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        GridLayout gridLayout = new GridLayout();
        gridLayout.setRows(6);
        gridLayout.setColumns(1);
        gridLayout.setHgap(8);
        gridLayout.setVgap(8); 
 
        JPanel buttons = new JPanel(gridLayout);
        buttons.setOpaque(false);

        MyImageIcon imgnewgame = new MyImageIcon("resources/Button/NewGame.png").resizesmooth(200,50);
        JButton newgame = new JButton(imgnewgame);
        newgame.setFocusPainted(true);
        newgame.setContentAreaFilled(false);
        newgame.setMargin(new Insets(0, 0, 0, 0));
        newgame.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                frame.setCurrentLevel(0);
                frame.setLevelMenu(0);
                frame.setContentPane(frame.getLevelMenu());
                frame.getSetting().enableCharacter(false);
                frame.validate();
            }
        });

        MyImageIcon imgsetting = new MyImageIcon("resources/Button/Setting.png").resizesmooth(200,50);
        JButton setting = new JButton(imgsetting);
        setting.setFocusPainted(true);
        setting.setContentAreaFilled(false);
        setting.setMargin(new Insets(0, 0, 0, 0));
        setting.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                frame.getSetting().setVisible(true);
            }
        });
        
        MyImageIcon imgcontinues = new MyImageIcon("resources/Button/Continue.png").resizesmooth(200,50);
        JButton continues = new JButton(imgcontinues);
        continues.setFocusPainted(true);
        continues.setContentAreaFilled(false);
        continues.setMargin(new Insets(0, 0, 0, 0));
        continues.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                frame.setContentPane(frame.getLevelMenu());
                frame.getSetting().enableCharacter(false);
                frame.validate();
            }
        });
        MyImageIcon imglogout = new MyImageIcon("resources/Button/Logout.png").resizesmooth(200,50);
        JButton logout = new JButton(imglogout);
        logout.setFocusPainted(true);
        logout.setContentAreaFilled(false);
        logout.setMargin(new Insets(0, 0, 0, 0));
        logout.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                frame.save(frame.getCurrentLevel());
                frame.setContentPane(frame.getLogin());
                frame.validate();
            }
        });

        MyImageIcon imgcredit = new MyImageIcon("resources/Button/Credit.png").resizesmooth(200,50);
        JButton credit = new JButton(imgcredit);
        credit.setFocusPainted(true);
        credit.setContentAreaFilled(false);
        credit.setMargin(new Insets(0, 0, 0, 0));
        credit.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                frame.getCredit().setVisible(true);
            }
        });
        
        buttons.add(new JLabel());
        buttons.add(continues, gbc);
        buttons.add(newgame, gbc);
        buttons.add(setting, gbc);
        buttons.add(credit, gbc);
        buttons.add(logout, gbc);

        gbc.weighty = 1;
        add(buttons, gbc);      
        
    }
    
    public void resizeBG(int w, int h){
        setIcon(new MyImageIcon("resources/BG/BG_Cover.png").resize(w, h));
    }
}

class LevelMenu extends JLabel{
    mainproject3 frame;
    int current;
    public LevelMenu(mainproject3 tframe, int tcurr){

        //Setup
        frame = tframe;
        current = tcurr;

        //Display button.
        setIcon(new MyImageIcon("resources/BG/BG.jpg").resize(frame.getWidth(), frame.getHeight()));
        setLayout(new GridBagLayout());
        
        displayLevel();
    }
    public void displayLevel(){
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        GridLayout gridLayout = new GridLayout();
        gridLayout.setRows(2);
        gridLayout.setColumns(5);
        gridLayout.setHgap(20);
        gridLayout.setVgap(20); 

        JPanel selectedLevel = new JPanel(gridLayout);
        selectedLevel.setBackground(Color.WHITE);
        selectedLevel.setOpaque(false);
        for(int i = 0; i < 10; i++){
            MyImageIcon imagelevel;
            String nlevel = String.valueOf(i+1);
            if(i < current){
                imagelevel = new MyImageIcon("resources/Button/"+nlevel+"P.png").resize(100, 100);
            }
            else{
                imagelevel = new MyImageIcon("resources/Button/"+nlevel+".png").resize(100, 100);
            }
            JButton level = new JButton(imagelevel);
            level.setPreferredSize(new Dimension(100, 100));
            level.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    frame.setContentPane(new Level(nlevel, frame));
                    frame.validate();
                }
            });
            if(i > current){
                level.setEnabled(false);
                level.setDisabledIcon(new MyImageIcon("resources/Button/"+nlevel+"L.png").resize(100, 100));
            }
            else{
                level.addMouseListener(new MouseAdapter(){
                    public void mouseEntered(MouseEvent e){
                        level.setIcon(new MyImageIcon("resources/Button/"+nlevel+"F.png").resize(100, 100));
                    }
                    public void mouseExited(MouseEvent e){
                        level.setIcon(imagelevel);
                    }
                });
            }
            selectedLevel.add(level);
        }
        add(selectedLevel, gbc);        

        gbc.anchor = GridBagConstraints.SOUTHWEST;
        MyImageIcon imgback = new MyImageIcon("resources/Button/Back.png").resizesmooth(125,50);
        JButton back = new JButton(imgback);
        back.setPreferredSize(new Dimension(125,50));
        back.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                frame.setContentPane(frame.getMenu());
                frame.getSetting().enableCharacter(true);
                frame.validate();
            }
        });
        gbc.weighty = 1;
        gbc.weightx = 1;
        this.add(back,gbc);
    }
    public void resizeBG(int w, int h){
        setIcon(new MyImageIcon("resources/BG/BG.jpg").resize(w, h));
    }
}

class Level extends JLabel{
    Map map;
    mainproject3 frame;
    JPanel board;
    int Pwidth = 50, Pheight = 50;
    int nlevel;
    
    public Level(String level, mainproject3 f){
        map = new Map("resources/Map/map"+level+".txt");
        frame = f;
        nlevel = Integer.valueOf(level);
        MapAdapter key = new MapAdapter();
        frame.addKeyListener(key);

        // setBorder(new EmptyBorder(20, 20, 20, 20));
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.NORTHEAST; 
        MyImageIcon imgback = new MyImageIcon("resources/Button/Back.png").resizesmooth(150,50);
        JButton back = new JButton(imgback);
        back.setPreferredSize(new Dimension(150, 50));
        back.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                frame.setContentPane(frame.getLevelMenu());
                frame.removeKeyListener(key);
                frame.validate();
            }
        });
        gbc.anchor = GridBagConstraints.NORTHEAST;
        this.add(back, gbc);

        draw();
        revalidate();
        repaint();
    }
    private class MapAdapter extends KeyAdapter{
        public void keyTyped( KeyEvent e )      { }
        public void keyPressed( KeyEvent e )    {
                if(map.isFinised()) { return; }
                if(e.getKeyCode() == KeyEvent.VK_LEFT){map.move(-1,0);}
                if(e.getKeyCode() == KeyEvent.VK_RIGHT){map.move(1,0);}
                if(e.getKeyCode() == KeyEvent.VK_UP){map.move(0,-1);}
                if(e.getKeyCode() == KeyEvent.VK_DOWN){map.move(0,1);}
                frame.walkSound();
                draw();
                revalidate();
                repaint();
                if(map.isFinised()) { LevelFinised(); }    
            }
        public void keyReleased( KeyEvent e )	{ }
    }

    public void draw(){
        try{
            this.remove(board);
        }catch(Exception e) {}
        board = new JPanel();
        GridLayout gridLayout = new GridLayout();
        gridLayout.setRows(map.getHeight());
        gridLayout.setColumns(map.getWidth());
        board.setLayout(gridLayout);
        board.setFocusable(true);
        board.requestFocus();
        int[][] mapi = map.getMap();
        for(int row = 0; row < mapi.length; row++){
            for(int col = 0; col < mapi[0].length; col++){
                if(mapi[row][col] == 1 || mapi[row][col] == 4){
                    board.add(new Item("resources/char_"+frame.getCharactorShape()+"/"+frame.getCharactor()+".png", Pwidth, Pheight));
                }
                else if(mapi[row][col] == 2 || mapi[row][col] == 5){
                    board.add(new Item("resources/Item/Box.png", Pwidth, Pheight));
                }
                else if(mapi[row][col] == 3){
                    board.add(new Item("resources/Item/Star.png", Pwidth, Pheight));
                }
                else if(mapi[row][col] == 9){
                    board.add(new Item("resources/Item/Wall.jpg", Pwidth, Pheight));
                }
                else{
                    board.add(new JLabel());
                }
            }
        }
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.RELATIVE;
        gbc.anchor = GridBagConstraints.CENTER;
        add(board, gbc);
    }

    public void LevelFinised(){
        String[] options = {"Back", "Retry", "Next Level"};
        String[] display;
        if(nlevel < 10) {
            display = options;}
        else{
            display = new String[2];
            display[0] = options[0];
            display[1] = options[1];
        }       

        int op = JOptionPane.showOptionDialog(frame, "VICTORY !!", "Finised",
        JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, display, display[0]);
        frame.setLevelMenu(nlevel);
        if(op == 0){
            frame.setContentPane(frame.getLevelMenu());
            frame.validate();
        }
        else if(op == 1){
            frame.setContentPane(new Level(String.valueOf(nlevel), frame));
            frame.validate();
        }
        else if(op == 2){
            if(nlevel < 10){
                frame.setContentPane(new Level(String.valueOf(nlevel+1), frame));
                frame.validate();
            }
        }
    }
}

/*
0 = space
1 = player
2 = box
3 = star
4 = player on star
5 = box on star
9 = wall
*/

class Map{
    int[][] map;
    int x_pos, y_pos, width, height, unfinished;
    public Map(String filemap){
        try(
            Scanner filescan = new Scanner(new File(filemap));
        ){
            int row = 0;
            String line = filescan.nextLine();
            String [] buff = line.split("\\s+");
            width = Integer.parseInt(buff[0]);
            height = Integer.parseInt(buff[1]);
            unfinished = Integer.parseInt(buff[2]);
            map = new int[height][width];
            while(filescan.hasNext()){
                line = filescan.nextLine();
                buff = line.split("\\s+");
                for (int column = 0; column < buff.length; column++){
                    map [row][column] = Integer.parseInt(buff[column]);
                    if(map[row][column] == 1){
                        x_pos = column; y_pos = row;
                    }
                    if(map[row][column] == 5){
                        unfinished -= 1;
                    }
                }
                row++;
            }
        }catch(Exception e) { System.err.println(e);}
        
        // display();
    }

    public boolean isFinised(){
        return unfinished==0 ;
    }

    public int[][] getMap(){
        return map;
    }

    public int getWidth(){
        return width;
    }
    public int getHeight(){
        return height;
    }

    public void move(int right, int down){
        // Move x axis.
        if(x_pos+right >= 0 && x_pos+right < width){

            if(map[y_pos][x_pos+right] == 2 || map[y_pos][x_pos+right] == 5){
                if(map[y_pos][x_pos+right+right] == 0 || map[y_pos][x_pos+right+right] == 3){
                    map[y_pos][x_pos+right+right] = map[y_pos][x_pos+right+right] + 2;
                    if(map[y_pos][x_pos+right+right] == 5) {unfinished--;}
                    if(map[y_pos][x_pos+right] == 5) {unfinished++;}
                    map[y_pos][x_pos+right] = map[y_pos][x_pos+right] - 2;
                }
            }

            if(map[y_pos][x_pos+right] == 0 || map[y_pos][x_pos+right] == 3){
                map[y_pos][x_pos+right] = map[y_pos][x_pos+right] + 1;
                map[y_pos][x_pos] = map[y_pos][x_pos] - 1;
                x_pos = x_pos + right;
            }            
        }

        // Move y axis.
        if(y_pos+down >= 0 && y_pos+down < height){
            
            if(map[y_pos+down][x_pos] == 2 || map[y_pos+down][x_pos] == 5){
                if(map[y_pos+down+down][x_pos] == 0 || map[y_pos+down+down][x_pos] == 3){
                    map[y_pos+down+down][x_pos] = map[y_pos+down+down][x_pos] + 2;
                    if(map[y_pos+down+down][x_pos] == 5) {unfinished--;}
                    if(map[y_pos+down][x_pos] == 5) {unfinished++;}
                    map[y_pos+down][x_pos] = map[y_pos+down][x_pos] - 2;             
                }
            }
            if(map[y_pos+down][x_pos] == 0 || map[y_pos+down][x_pos] == 3){
                map[y_pos+down][x_pos] = map[y_pos+down][x_pos] + 1;
                map[y_pos][x_pos] = map[y_pos][x_pos] - 1;                
                y_pos = y_pos + down;
            }
        }
        
        // display();
    }
}

class Setting extends JFrame{
    int frameWidth = 400, frameHeight = 600;
    JLabel contentpane;
    mainproject3 frame;
    String Char_Color = "Blue";
    String Char_Shape = "circle";
    JComboBox<String> colors;
    JRadioButton[] radio;
    public Setting(mainproject3 f){
        setTitle("Settings");
        setBounds(100, 100, frameWidth, frameHeight);
        setVisible(false);
        setDefaultCloseOperation( WindowConstants.DISPOSE_ON_CLOSE );
        setBackground(Color.WHITE);
        
        frame = f;

        contentpane = new JLabel();
        this.setContentPane(contentpane);
        contentpane.setIcon(new MyImageIcon("resources/BG/BG_Setting.png").resize(frameWidth, frameHeight));
        contentpane.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        contentpane.add(new JLabel("<html><h1><strong><i>          </i></strong></h1><hr></html>"), gbc);

        String [] items = new String[2];
        items[0] = "Music";
        items[1] = "Effect";
        JCheckBox [] tb = new JCheckBox[2];
        JSlider[] slider = new JSlider[2];

        JCheckBox sound = new JCheckBox( "Sound", true );
        for(int i = 0; i < 2; i++){
            tb[i] = new JCheckBox( items[i], true );
            slider[i] = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
            slider[i].setMajorTickSpacing(20);
            slider[i].setMinorTickSpacing(5);
            slider[i].setPaintTicks(true);
            slider[i].setSnapToTicks(true);
        }
        slider[0].setValue(100);
        slider[1].setValue(100);

        sound.addItemListener( new ItemListener(){
            public void itemStateChanged(ItemEvent e){
                if(e.getStateChange() == ItemEvent.SELECTED){
                    for(int i = 0; i < 2; i++){
                        tb[i].setEnabled(true);
                        slider[i].setEnabled(true);
                    }
                    frame.changeSetting(true, frame.getVMusic(), true, frame.getVFx());
                }
                else{
                    for(int i = 0; i < 2; i++){
                        tb[i].setEnabled(false);
                        slider[i].setEnabled(false);
                    }
                    frame.changeSetting(false, frame.getVMusic(), false, frame.getVFx());
                }
            }
        });

        for(int i = 0; i < 2; i++){
            tb[i].addItemListener( new ItemListener(){
                public void itemStateChanged(ItemEvent e){
                    Boolean playable;
                    JCheckBox temp = (JCheckBox)e.getItem();
                    if(e.getStateChange() == ItemEvent.SELECTED){
                        playable = true;
                    }
                    else{
                        playable = false;
                    }
                    if(temp.getText().equals("Music")){
                        slider[0].setEnabled(playable); 
                        frame.changeSetting(playable, frame.getVMusic(), frame.getPFx(), frame.getVFx());
                    }
                    if(temp.getText().equals("Effect")){
                        slider[1].setEnabled(playable); 
                        frame.changeSetting(frame.getPMusic(), frame.getVMusic(), playable, frame.getVFx());
                    }
                }
            });
        }
        slider[0].addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e){
                int volume = slider[0].getValue();
                frame.changeSetting(frame.getPMusic(), volume, frame.getPFx(), frame.getVFx());
            }
        });
        slider[1].addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e){
                int volume = slider[1].getValue();
                frame.changeSetting(frame.getPMusic(), frame.getVMusic(), frame.getPFx(), volume);
            }
        });

        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        contentpane.add(sound, gbc);
        for(int i =  0; i < 2; i++){
            gbc.gridx = 0;
            gbc.gridy = i+2;
            gbc.weightx = 0.3;
            contentpane.add(tb[i], gbc);
            gbc.gridx = 1;
            gbc.gridy = i+2;
            gbc.weightx = 0.7;
            contentpane.add(slider[i], gbc);
        }

        String[] nchar = new String[3];
        nchar[0] = "circle";
        nchar[1] = "square"; 
        nchar[2] = "triangle"; 

        radio = new JRadioButton[3];
        ButtonGroup rGroup = new ButtonGroup();
        for(int i = 0; i < 3; i++){
            radio[i] = new JRadioButton(nchar[i]);
            rGroup.add(radio[i]);
        }
        radio[0].setSelected(true);
        
        gbc.gridy = 4;
        gbc.weightx = 1;
        for(int i = 0; i < 3; i++){
            gbc.gridx = i;
            contentpane.add(radio[i], gbc);
        }

        radio[0].addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                Char_Shape = "circle";
                frame.ChangeChar_Shape(Char_Shape);
            }
        });
        
        radio[1].addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                Char_Shape = "square";
                frame.ChangeChar_Shape(Char_Shape);
            }
        });
        
        radio[2].addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                Char_Shape = "triangle";
                frame.ChangeChar_Shape(Char_Shape);
            }
        });

        Item[] imagechar = new Item[3];
        imagechar[0] = new Item("resources/char_circle/circle_"+Char_Color+".png", 100, 100);
        imagechar[1] = new Item("resources/char_square/square_"+Char_Color+".png", 100, 100);
        imagechar[2] = new Item("resources/char_triangle/triangle_"+Char_Color+".png", 100, 100);
        
        gbc.gridy = 5;
        gbc.weightx = 1;
        for(int i = 0; i < 3; i++){
            gbc.gridx = i;
            contentpane.add(imagechar[i], gbc);
        }

        String[] color = {"Blue", "Green", "Orange", "Pink", "Red", "Purple"};
        colors = new JComboBox<String>(color);
        colors.setSelectedIndex(0);
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 1;
        contentpane.add(colors, gbc);
        colors.addActionListener (new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                Char_Color = colors.getSelectedItem().toString();
                imagechar[0].changeIcon("resources/char_circle/circle_"+Char_Color+".png");
                imagechar[1].changeIcon("resources/char_square/square_"+Char_Color+".png");
                imagechar[2].changeIcon("resources/char_triangle/triangle_"+Char_Color+".png");
                revalidate();
                repaint();
                frame.ChangeChar_Color(Char_Color);
            }
        });

        addComponentListener(new ComponentAdapter(){
            public void componentResized(ComponentEvent e){
                int width = getContentPane().getWidth();
                int height = getContentPane().getHeight();
                contentpane.setIcon(new MyImageIcon("resources/BG/BG_Setting.png").resize(width, height));
            }
        });

    }

    public void enableCharacter(Boolean e){
        for(int i = 0; i < radio.length; i++){
            radio[i].setEnabled(e);
        }
        colors.setEnabled(e);
    }
}

class Credit extends JFrame{
    int frameWidth = 400, frameHeight = 600;
    mainproject3 frame;
    JLabel contentpane;
    public Credit(mainproject3 f){
        setTitle("Credit");
        setBounds(100, 100, frameWidth, frameHeight);
        setVisible(false);
        setDefaultCloseOperation( WindowConstants.DISPOSE_ON_CLOSE );
        setBackground(Color.WHITE);
        
        frame = f;
        contentpane = new JLabel();
        this.setContentPane(contentpane);
        contentpane.setIcon(new MyImageIcon("resources/BG/BG_Credit.png").resize(frameWidth, frameHeight));
        contentpane.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();


        /*
        6313126 Chayawat Nilsumrit
        6313210 Krittawat Thongnoppakao
        6313138 Yutthasat Phoncharoenpong
        6313212 Chatrawit Chatnawakul
        */
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;
        gbc.gridy = 0;
        contentpane.add(new JLabel("<html><h2><i>6313126 Chayawat Nilsumrit</i></h2></html>"), gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        contentpane.add(new JLabel("<html><h2><i>6313210 Krittawat Thongnoppakao</i></h2></html>"), gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        contentpane.add(new JLabel("<html><h2><i>6313138 Yutthasat Phoncharoenpong</i></h2></html>"), gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        contentpane.add(new JLabel("<html><h2><i>6313212 Chatrawit Chatnawakul</i></h2></html>"), gbc);
    }
}

class Item extends JLabel{
    int width, height;
    MyImageIcon itemimage;
    public Item(String file1, int w, int h){ 
        width = w; height = h;
        itemimage = new MyImageIcon(file1).resize(width, height);
        setIcon(itemimage);
        setHorizontalAlignment(JLabel.CENTER);
    }
    public void changeIcon(String file){
        itemimage = new MyImageIcon(file).resize(width, height);
        setIcon(itemimage);
        setHorizontalAlignment(JLabel.CENTER);
    }
}

class MyImageIcon extends ImageIcon
{
    public MyImageIcon(String fname)  { super(fname); }
    public MyImageIcon(Image image)   { super(image); }

    public MyImageIcon resize(int width, int height)
    {
        Image oldimg = this.getImage();
        Image newimg = oldimg.getScaledInstance(width, height, java.awt.Image.SCALE_FAST);
        return new MyImageIcon(newimg);
    }
    public MyImageIcon resizesmooth(int width, int height){
        Image oldimg = this.getImage();
        Image newimg = oldimg.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
        return new MyImageIcon(newimg);
    }
};

class MySoundEffect
{
    private Clip clip;
    float maxVolume = 6.0f;
    float minVolume = -30.0f;
    public MySoundEffect(String filename)
    {
	try
	{
            java.io.File file = new java.io.File(filename);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
	}
	catch (Exception e) { e.printStackTrace(); }
    }
    public void playOnce()   { clip.setMicrosecondPosition(0); clip.start(); }
    public void playLoop()   { clip.loop(Clip.LOOP_CONTINUOUSLY); }
    public void stop()       { clip.stop(); }
    public void changeVolume(int v){
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        float volume = minVolume + (maxVolume - minVolume)*v/100; 
        gainControl.setValue(volume);
    }
}

class LoginMenu extends JLabel{
    mainproject3 frame;
    ArrayList<String> IDList = new ArrayList<String>();
    ArrayList<Integer> LevelList = new ArrayList<Integer>();
    int index;

    public LoginMenu(mainproject3 tframe){
        frame = tframe;
        setIcon(new MyImageIcon("resources/BG/BG_Login.png").resize(frame.getWidth(), frame.getHeight()));
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.NORTH;
        
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        GridLayout gridLayout = new GridLayout();
        gridLayout.setRows(7);
        gridLayout.setColumns(1);
        gridLayout.setHgap(8);
        gridLayout.setVgap(8);
        
        JPanel PanelLogin = new JPanel(gridLayout);
        PanelLogin.setOpaque(false);//Transparent
        
        // read file to update value
        if(RW.haveFile("Account/ListID.txt")!=true){
            RW.write("Account/ListID.txt", "Guest->0");
        }
        
        ArrayList<String> AllID = RW.read("Account/ListID.txt");
        
        for (int i=0; i<AllID.size();i++){
            
            if(i%2 == 0){
                IDList.add(AllID.get(i));
                
            }
            else{
                LevelList.add(Integer.parseInt(AllID.get(i)));
                
            }
        }
        
        String[] IDArray = new String[IDList.size()];
        IDList.toArray(IDArray);
               
        JComboBox comboBoxID = new JComboBox(IDArray);
        comboBoxID.setSelectedItem(null);
        IDList = new ArrayList<String>(Arrays.asList(IDArray));
        
        // Login
        JLabel chooseID = new JLabel();
        chooseID.setText("Please choose your ID.");

        MyImageIcon imglogin = new MyImageIcon("resources/Button/Login.png").resizesmooth(200,35);        
        JButton login = new JButton(imglogin);
        login.setPreferredSize(new Dimension(200,35));
        login.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                try{
                if(comboBoxID.getSelectedItem().toString()!=null){
                    
                    String id = comboBoxID.getSelectedItem().toString();
                    index=IDList.indexOf(id);

                    frame.setAccount();
                    frame.setContentPane(frame.getMenu());
                    frame.validate();

                }
                }
                catch(Exception er){
                    chooseID.setText("You must choose your ID before.");
                    chooseID.setForeground (Color.red);
                }
  
            }
        });
        PanelLogin.add(new JLabel());
        PanelLogin.add(chooseID);
        PanelLogin.add(comboBoxID);
        PanelLogin.add(login);
        

        // Register
        JLabel IDisTaken = new JLabel();
        IDisTaken.setText("or");
        
        JTextField Register = new JTextField("type your ID...");
        Register.setDocument( new JTextFieldLimit(10));

        MyImageIcon imgregister = new MyImageIcon("resources/Button/Register.png").resizesmooth(200,35);        
        JButton register = new JButton(imgregister);
        register.setPreferredSize(new Dimension(200,35));
        register.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if(Register.getText() !=null){
                    String id = Register.getText();
                    if(Arrays.asList(IDArray).contains(id)){
                        // add text to label
                        IDisTaken.setText("This ID is taken. Try another.");
                        IDisTaken.setForeground (Color.red);
                    }
                    else{
                        RW.write("Account/ListID.txt",id+"->0");
                        IDList.add(id);
                        LevelList.add(0);
                        index=IDList.indexOf(id);
                        
                        // go to Menu page
                        frame.setAccount();
                        frame.setContentPane(frame.getMenu());
                        frame.validate();
                    }
                }
            }
        });
        
        PanelLogin.add(IDisTaken);
        PanelLogin.add(Register);
        PanelLogin.add(register);
        

        gbc.weighty = 1;
        add(PanelLogin, gbc);  
    }
    
    public ArrayList<String> getIDList(){
        return IDList;
    }
    public ArrayList<Integer> getLevelList(){
        return LevelList;
    }
    public int getIndexLogin(){
        return index;
    }
    public void resizeBG(int w, int h){
        setIcon(new MyImageIcon("resources/BG/BG_Login.png").resize(w, h));
    }
}

// read & write
class RW {
    public static ArrayList<String> read (String FilePath){
        try (Scanner filescan = new Scanner(new File(FilePath))){
                        
            String line = filescan.nextLine();
            ArrayList<String> buff = new ArrayList<String>();
            buff.addAll(Arrays.asList(line.split("->")));
            while (filescan.hasNext()){
                line = filescan.nextLine(); //get the last game level
                buff.addAll(Arrays.asList(line.split("->")));
            }
            return buff;
        }
        catch(Exception ex){}
        return null;
        
    }

    public static void write (String FilePath, String text){
        try {
            File inputFile = new File(FilePath);
            FileWriter fileWriter = new FileWriter(inputFile,true);
            BufferedWriter bw = new BufferedWriter(fileWriter);
            PrintWriter printWriter = new PrintWriter(bw);
            printWriter.println(text);
            printWriter.close();
        } catch (IOException ex) {}
    }

    public static boolean haveFile (String FilePath){
        File f = new File(FilePath);
        return f.exists() && !f.isDirectory();
    }
}

class JTextFieldLimit extends PlainDocument {
  private int limit;

  JTextFieldLimit(int limit) {
   super();
   this.limit = limit;
   }

  public void insertString( int offset, String  str, AttributeSet attr ) throws BadLocationException {
    if (str == null) return;

    if ((getLength() + str.length()) <= limit) {
      super.insertString(offset, str, attr);
    }
  }
}