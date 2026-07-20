import DAO.ProductoDAO;
import Modelo.Producto;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProductoDAOTest {

    private final Random random = new Random();

    private String generarNombre() {
        return "Producto " + System.currentTimeMillis();
    }

    @Test
    public void testListarProductos() {

        ProductoDAO dao = new ProductoDAO();

        List<Producto> productos = dao.listar();

        assertNotNull(productos, "La lista no debe ser nula");
    }

    @Test
    public void testInsertarProducto() {

        ProductoDAO dao = new ProductoDAO();

        Producto producto = new Producto();

        producto.setNombreProducto(generarNombre());
        producto.setDescripcion("Producto generado automáticamente");
        producto.setPrecio(100.50);

        boolean resultado = dao.insertar(producto);

        assertTrue(resultado, "El producto debería registrarse correctamente");

    }

    @Test
    public void testActualizarProducto() {

        ProductoDAO dao = new ProductoDAO();

        Producto producto = new Producto();

        producto.setNombreProducto(generarNombre());
        producto.setDescripcion("Producto Original");
        producto.setPrecio(50.00);

        assertTrue(dao.insertar(producto));

        List<Producto> lista = dao.listar();

        Producto ultimo = lista.get(lista.size() - 1);

        ultimo.setNombreProducto("Producto Actualizado");
        ultimo.setDescripcion("Descripción Actualizada");
        ultimo.setPrecio(150.00);

        boolean actualizado = dao.actualizar(ultimo);

        assertTrue(actualizado, "El producto debería actualizarse correctamente");

    }

    @Test
    public void testEliminarProducto() {

        ProductoDAO dao = new ProductoDAO();

        Producto producto = new Producto();

        producto.setNombreProducto(generarNombre());
        producto.setDescripcion("Producto Temporal");
        producto.setPrecio(75.00);

        assertTrue(dao.insertar(producto));

        List<Producto> lista = dao.listar();

        Producto ultimo = lista.get(lista.size() - 1);

        boolean eliminado = dao.eliminar(ultimo.getIdproducto());

        assertTrue(eliminado, "El producto debería eliminarse correctamente");

    }

}