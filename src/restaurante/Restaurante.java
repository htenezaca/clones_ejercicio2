package restaurante;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Restaurante {
    private List<Producto> productos;
    private List<Pedido> pedidos;
    private List<Usuario> usuarios;

    public Restaurante(List<Producto> productos, List<Pedido> pedidos, List<Usuario> usuarios) {
        this.productos = productos;
        this.pedidos = pedidos;
        this.usuarios = usuarios;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    public void generarReporte() {
        int total = calcularTotal();
        System.out.println("------------------------");
        System.out.println("El total de ventas para el restaurante es: " + total);
        List<Pedido> pedidosOrdenados = pedidosPorPrecio();
        pedidosOrdenados.forEach(Pedido::generarReporte);
    }

    private int calcularTotal() {
        return pedidos.stream()
                .flatMap(pedido -> pedido.getProductos().stream())
                .mapToInt(Producto::getPrecio)
                .sum();
    }

    public List<Pedido> pedidosPorPrecio() {
        List<Pedido> pedidosCopia = new ArrayList<>(pedidos);
        List<Pedido> pedidosOrdenados = ordenarPedidosPorPrecioHelper(pedidosCopia);
        Collections.reverse(pedidosOrdenados);
        return pedidosOrdenados;
    }

    private List<Pedido> ordenarPedidosPorPrecioHelper(List<Pedido> pedidos) {
        if (pedidos.size() <= 1) {
            return pedidos;
        }

        int mitad = pedidos.size() / 2;
        List<Pedido> izquierda = ordenarPedidosPorPrecioHelper(pedidos.subList(0, mitad));
        List<Pedido> derecha = ordenarPedidosPorPrecioHelper(pedidos.subList(mitad, pedidos.size()));

        return merge(izquierda, derecha);
    }

    private List<Pedido> merge(List<Pedido> izquierda, List<Pedido> derecha) {
        List<Pedido> resultado = new ArrayList<>();

        List<Pedido> copiaIzquierda = new ArrayList<>(izquierda);
        List<Pedido> copiaDerecha = new ArrayList<>(derecha);

        while (!copiaIzquierda.isEmpty() && !copiaDerecha.isEmpty()) {
            Pedido primero = (copiaIzquierda.get(0).calcularTotal() < copiaDerecha.get(0).calcularTotal())
                    ? copiaIzquierda.remove(0)
                    : copiaDerecha.remove(0);

            resultado.add(primero);
        }

        resultado.addAll(copiaIzquierda);
        resultado.addAll(copiaDerecha);

        return resultado;
    }

    public static void main(String[] args) {
        ArrayList<Producto> productos = new ArrayList<>();
        productos.add(new Producto("Hamburguesa", 100));
        productos.add(new Producto("Papas", 50));
        productos.add(new Producto("Refresco", 30));
        productos.add(new Producto("Helado", 20));

        ArrayList<Usuario> usuarios = new ArrayList<>();
        usuarios.add(new Usuario("Juan", "Calle 1", new ArrayList<Pedido>()));
        usuarios.add(new Usuario("Pedro", "Calle 2", new ArrayList<Pedido>()));

        ArrayList<Producto> productosPedido1 = new ArrayList<>();
        productosPedido1.add(productos.get(0));
        productosPedido1.add(productos.get(1));

        ArrayList<Producto> productosPedido2 = new ArrayList<>();
        productosPedido2.add(productos.get(2));
        productosPedido2.add(productos.get(3));

        ArrayList<Producto> productosPedido3 = new ArrayList<>();
        productosPedido3.add(productos.get(0));
        productosPedido3.add(productos.get(1));
        productosPedido3.add(productos.get(2));

        ArrayList<Pedido> pedidos = new ArrayList<>();
        pedidos.add(new Pedido(usuarios.get(0), productosPedido1));
        pedidos.add(new Pedido(usuarios.get(0), productosPedido2));
        pedidos.add(new Pedido(usuarios.get(1), productosPedido3));

        ArrayList<Pedido> pedidosUsuario1 = new ArrayList<>();
        pedidosUsuario1.add(pedidos.get(0));
        pedidosUsuario1.add(pedidos.get(1));

        ArrayList<Pedido> pedidosUsuario2 = new ArrayList<>();
        pedidosUsuario2.add(pedidos.get(2));

        usuarios.get(0).setPedidos(pedidosUsuario1);
        usuarios.get(1).setPedidos(pedidosUsuario2);

        Restaurante restaurante = new Restaurante(productos, pedidos, usuarios);

        restaurante.generarReporte();
        usuarios.get(0).generarReporte();
        usuarios.get(1).generarReporte();
    }
}
