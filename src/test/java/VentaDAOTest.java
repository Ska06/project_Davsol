import DAO.VentaDAO;
import Modelo.Producto;
import Modelo.Venta;
import org.junit.jupiter.api.Test;
import util.ConnectionMySQL;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class VentaDAOTest {

    @Test
    public void testInsertarVenta() {

        try (Connection con = ConnectionMySQL.getConexion()) {

            con.setAutoCommit(false);

            VentaDAO dao = new VentaDAO();

            Venta venta = new Venta();

        
            venta.setIdcliente(1);

            venta.setTotal(150.50);

            int idVenta = dao.insertarConConexion(con, venta);

            assertTrue(idVenta > 0,
                    "La venta debe registrarse correctamente.");

            con.rollback();

        } catch (Exception e) {

            fail(e.getMessage());

        }

    }

    @Test
    public void testRegistrarSalidaStock() {

        try (Connection con = ConnectionMySQL.getConexion()) {

            con.setAutoCommit(false);

            VentaDAO dao = new VentaDAO();

            Producto producto = new Producto();

          
            producto.setIdproducto(1);

            boolean resultado =
                    dao.registrarSalidaStock(con,
                                             producto,
                                             1);

            assertTrue(resultado,
                    "Debe descontar stock correctamente.");

            con.rollback();

        } catch (Exception e) {

            fail(e.getMessage());

        }

    }

}