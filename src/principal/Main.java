package principal;

import controlador.CtrlMarca;
import java.sql.SQLException;
import modelo.DAOMarca;
import modelo.DTOMarca;
import vista.FrmMarca;

public class Main {

    public static void main(String[] args) throws SQLException{
        DTOMarca mod = new DTOMarca();
        DAOMarca dao = new DAOMarca();
        FrmMarca frm = new FrmMarca();
        CtrlMarca ct = new CtrlMarca(mod, dao, frm);
        
        ct.inicio();
        frm.setVisible(true);
    }
    
}
