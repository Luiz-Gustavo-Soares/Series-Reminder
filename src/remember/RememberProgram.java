package remember;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RememberProgram {
    
    private int idDb;
    private String name;
    private int ep;
    private Date date;
    private String capaImg;
    private final String imgFundo = "src/imgs/fundo.jpg";
    private String analize;
    private int nota;

    public RememberProgram(int id){
        this.idDb = id;
    }

    public RememberProgram(int id, String name, int ep, Date date, String img, String analize, int nota){
        this.idDb = id;
        this.name = name;
        this.ep = ep;
        this.date = date;
        this.capaImg = (img == null) ? imgFundo:img ;
        this.analize = analize;
        this.nota = nota;
    }

    public RememberProgram(int id, String name, int ep, String img, String analize, int nota){
        this.idDb = id;
        this.name = name;
        this.ep = ep;
        this.date = new Date();
        this.capaImg = (img.equals("")) ? imgFundo:img;
        this.analize = analize;
        this.nota = nota;
    }

    public int getIdDb() {
        return idDb;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getEp() {
        return ep;
    }

    public void setEp(int ep) {
        this.ep = ep;
    }

    public String getDate() {

        return new SimpleDateFormat("dd/MM/yyyy").format(date);
    }

    public long getTime() {

        return date.getTime();
    }

    public void setDate(Date date) {
        this.date = date;
    }

    
    public String getCapaImg() {
        return capaImg;
    }

    public void setCapaImg(String capaImg) {
        this.capaImg = capaImg;
    }

    public String getAnalize() {
        return analize;
    }

    public void setAnalize(String analize) {
        this.analize = analize;
    }

    public int getNota() {
        return nota;
    }

    public void setNota(int nota) {
        this.nota = nota;
    }


    @Override
    public String toString() {
        return "RememberProgram [idDb=" + idDb + ", name=" + name + ", ep=" + ep + ", date=" + date + ", capaImg="
                + capaImg + ", analize=" + analize + ", nota=" + nota + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + idDb;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ep;
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RememberProgram other = (RememberProgram) obj;
        if (idDb != other.idDb)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (ep != other.ep)
            return false;
        if (date == null) {
            if (other.date != null)
                return false;
        } else if (!date.equals(other.date))
            return false;
        return true;
    }

}
