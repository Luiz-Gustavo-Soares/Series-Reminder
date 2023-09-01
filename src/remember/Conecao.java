package remember;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Conecao {
    
    private Connection connection = null;
    private Statement statement;

    
    public boolean createConnection(){
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:base.db");
            statement = connection.createStatement();
            statement.setQueryTimeout(30);  

            String SQL = 
            "CREATE TABLE IF NOT EXISTS rememberProgam (" +
                "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "name varchar(150)," +
                "ep int," +
                "date Date,"+
                "image varchar(150), "+
                "analize TEXT,"+
                "nota TINYINT)";

            statement.executeUpdate(SQL);

            System.out.println("Conectado...");
            return true;

        } catch(SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    public boolean closeConnection(){
        if (connection == null) return false;

        try {
            connection.close();
            System.out.println("Fechado...");
            return true;

        } catch(SQLException e){
            System.err.println(e.getMessage());
            return false;
        }
    }


    public boolean insertRemember(String name, int ep, String imagem, String analize, int nota) throws IOException {
        java.util.Date d = new java.util.Date();
        java.sql.Date date = new java.sql.Date(d.getTime());
        
        String capa = salvarArquivo(imagem, name);

        String SQL = String.format(
            "INSERT INTO rememberProgam (name, ep, date, image, analize, nota) "+
            "VALUES ('%s', '%d', '%s', '%s', '%s', '%d')", name, ep, date, capa, analize, nota
        );
        
        try {
            statement.executeUpdate(SQL);
            return true;
        } catch(SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }


    public boolean insertRemember(String name, int ep) {
        java.util.Date d = new java.util.Date();
        java.sql.Date date = new java.sql.Date(d.getTime());

        String SQL = String.format(
            "INSERT INTO rememberProgam (name, ep, date) "+
            "VALUES ('%s', '%d', '%s')", name, ep, date
        );
        
        try {
            statement.executeUpdate(SQL);
            return true;
        } catch(SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    public ArrayList<RememberProgram>  selectAllRemembers() throws ParseException{
        ArrayList<RememberProgram> rememberList = new ArrayList<RememberProgram>();

        String SQL = "SELECT id, name, ep, date, image, analize, nota FROM rememberProgam";

        try {
            ResultSet rs = statement.executeQuery(SQL);
            while (rs.next()) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date date= format.parse(rs.getString("date"));
                
                RememberProgram remeber = new RememberProgram(rs.getInt("id"), rs.getString("name"), rs.getInt("ep"), date, rs.getString("image"), rs.getString("analize"), rs.getInt("nota")); 
                rememberList.add(remeber);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return rememberList;
    }

    public boolean deleteRemember(int id) {
        String SQL = String.format("SELECT * FROM rememberProgam WHERE id == %s", id);

        try {
            ResultSet rs = statement.executeQuery(SQL);

            if (rs.next()) {
                SQL = String.format("Delete FROM rememberProgam WHERE id == %s", id);
                try {
                    String image = rs.getString("image");
                    
                    if (image != null) new File(image).delete();
                    statement.executeUpdate(SQL);
                    return true;

                } catch (SQLException e) {
                    System.err.println(e.getMessage());
                    return false;
                }
            } 
            
            return false;

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    } 

    public boolean updateRemember(int id, int ep, java.util.Date d, String analize, int nota) {
        String SQL = String.format("SELECT * FROM rememberProgam WHERE id == %s", id);

        try {
            ResultSet rs = statement.executeQuery(SQL);

            if (rs.next()) {

                java.sql.Date date = new java.sql.Date(d.getTime());

                if (analize.isBlank()) analize = rs.getString("analize");

                SQL = String.format("UPDATE rememberProgam " +
                "SET ep = '%d', date = '%s', analize = '%s', nota = '%d' "+
                "WHERE id = %d", ep, date, analize, nota, id);

                try {
                    statement.executeUpdate(SQL);
                    return true;

                } catch (SQLException e) {
                    System.err.println(e.getMessage());
                    return false;
                }
            } 
            
            return false;

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }


    private String salvarArquivo(String nameFile, String name) throws IOException{
        String caminho = "";

        try {
            File f = new File(nameFile);
            BufferedImage image = ImageIO.read(f);
            File cmn = new File(String.format("src/imgs/%s.jpeg", name));
            ImageIO.write(image, "jpeg", cmn);
            caminho = cmn.toString();

        } catch(IOException e) {
            System.err.println(e.getMessage());
        }

        return caminho;
    }
}

