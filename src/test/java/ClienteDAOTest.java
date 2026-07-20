import DAO.ClienteDAO;
import Modelo.Cliente;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ClienteDAOTest {

    private final Random random = new Random();

    // Genera un DNI válido de 8 dígitos
    private String generarDni() {
        int numero = 10000000 + random.nextInt(90000000);
        return String.valueOf(numero);
    }

    // Genera un correo diferente cada vez
    private String generarCorreo() {
        return "cliente" + System.currentTimeMillis() + "@test.com";
    }

    @Test
    public void testListarClientes() {
        ClienteDAO dao = new ClienteDAO();

        List<Cliente> clientes = dao.listar();

        assertNotNull(clientes, "La lista no debe ser null");
    }

    @Test
    public void testInsertarCliente() {

        ClienteDAO dao = new ClienteDAO();

        Cliente cliente = new Cliente();
        cliente.setDniRUC(generarDni());
        cliente.setNombres("Cliente Prueba");
        cliente.setTelefono("999999999");
        cliente.setCorreo(generarCorreo());

        boolean resultado = dao.insertar(cliente);

        assertTrue(resultado, "El cliente debería registrarse correctamente");
    }

    @Test
    public void testActualizarCliente() {

        ClienteDAO dao = new ClienteDAO();

        // Crear cliente
        Cliente cliente = new Cliente();
        cliente.setDniRUC(generarDni());
        cliente.setNombres("Cliente Original");
        cliente.setTelefono("900000000");
        cliente.setCorreo(generarCorreo());

        assertTrue(dao.insertar(cliente));

        // Obtener el último cliente registrado
        List<Cliente> lista = dao.listar();
        Cliente ultimo = lista.get(lista.size() - 1);

        // Modificar datos
        ultimo.setNombres("Cliente Actualizado");
        ultimo.setTelefono("911111111");
        ultimo.setCorreo(generarCorreo());

        boolean actualizado = dao.actualizarCliente(ultimo);

        assertTrue(actualizado);
    }

    @Test
    public void testEliminarCliente() {

        ClienteDAO dao = new ClienteDAO();

        // Crear cliente
        Cliente cliente = new Cliente();
        cliente.setDniRUC(generarDni());
        cliente.setNombres("Cliente Eliminar");
        cliente.setTelefono("922222222");
        cliente.setCorreo(generarCorreo());

        assertTrue(dao.insertar(cliente));

        // Buscar el cliente recién insertado
        List<Cliente> lista = dao.listar();
        Cliente ultimo = lista.get(lista.size() - 1);

        boolean eliminado = dao.eliminarCliente(ultimo.getIdcliente());

        assertTrue(eliminado);
    }
}